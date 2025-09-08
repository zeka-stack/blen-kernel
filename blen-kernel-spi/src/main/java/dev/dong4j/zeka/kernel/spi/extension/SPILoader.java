package dev.dong4j.zeka.kernel.spi.extension;

import dev.dong4j.zeka.kernel.spi.Extension;
import dev.dong4j.zeka.kernel.spi.URL;
import dev.dong4j.zeka.kernel.spi.compiler.Compiler;
import dev.dong4j.zeka.kernel.spi.extension.support.ActivateComparator;
import dev.dong4j.zeka.kernel.spi.utils.CollectionUtils;
import dev.dong4j.zeka.kernel.spi.utils.ConcurrentHashSet;
import dev.dong4j.zeka.kernel.spi.utils.ConfigUtils;
import dev.dong4j.zeka.kernel.spi.utils.Holder;
import dev.dong4j.zeka.kernel.spi.utils.SpiClassUtils;
import dev.dong4j.zeka.kernel.spi.utils.SpiReflectUtils;
import dev.dong4j.zeka.kernel.spi.utils.SpiStringUtils;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;

import static dev.dong4j.zeka.kernel.spi.constants.CommonConstants.COMMA_SPLIT_PATTERN;
import static dev.dong4j.zeka.kernel.spi.constants.CommonConstants.DEFAULT_KEY;
import static dev.dong4j.zeka.kernel.spi.constants.CommonConstants.REMOVE_VALUE_PREFIX;

/**
 * <p>Description: </p>
 *
 * @param <T> parameter
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmaidl.com"
 * @date 2021.02.26 17:47
 * @since 1.0.0
 */
@Slf4j
@SuppressWarnings("all")
public final class SPILoader<T> {

    /** SERVICES_DIRECTORY */
    private static final String SERVICES_DIRECTORY = "META-INF/spiservices/";

    /** SPI_DIRECTORY */
    private static final String SPI_DIRECTORY = "META-INF/spi/";

    /** SPI_INTERNAL_DIRECTORY */
    private static final String SPI_INTERNAL_DIRECTORY = SPI_DIRECTORY + "internal/";

    /** NAME_SEPARATOR */
    private static final Pattern NAME_SEPARATOR = Pattern.compile("\\s*[,]+\\s*");

    /** EXTENSION_LOADERS */
    private static final ConcurrentMap<Class<?>, SPILoader<?>> EXTENSION_LOADERS = new ConcurrentHashMap<>();

    /** EXTENSION_INSTANCES */
    private static final ConcurrentMap<Class<?>, Object> EXTENSION_INSTANCES = new ConcurrentHashMap<>();

    /** Type */
    private final Class<?> type;

    /** Object factory */
    private final ExtensionFactory objectFactory;

    /** Cached names */
    private final ConcurrentMap<Class<?>, String> cachedNames = new ConcurrentHashMap<>();

    /** Cached classes */
    private final Holder<Map<String, Class<?>>> cachedClasses = new Holder<>();

    /** Cached activates */
    private final Map<String, Object> cachedActivates = new ConcurrentHashMap<>();
    /** Cached instances */
    private final ConcurrentMap<String, Holder<Object>> cachedInstances = new ConcurrentHashMap<>();
    /** Cached adaptive instance */
    private final Holder<Object> cachedAdaptiveInstance = new Holder<>();
    /** Cached adaptive class */
    private volatile Class<?> cachedAdaptiveClass = null;
    /** Cached default name */
    private String cachedDefaultName;
    /** Create adaptive instance error */
    private volatile Throwable createAdaptiveInstanceError;

    /** Cached wrapper classes */
    private Set<Class<?>> cachedWrapperClasses;

    /** Exceptions */
    private final Map<String, IllegalStateException> exceptions = new ConcurrentHashMap<>();

    /**
     * Extension loader
     *
     * @param type type
     * @since 1.0.0
     */
    private SPILoader(Class<?> type) {
        this.type = type;
        this.objectFactory = (type == ExtensionFactory.class ? null :
            SPILoader.getExtensionLoader(ExtensionFactory.class).getAdaptiveExtension());
    }

    /**
     * With extension annotation
     *
     * @param <T>  parameter
     * @param type type
     * @return the boolean
     * @since 1.0.0
     */
    private static <T> boolean withExtensionAnnotation(Class<T> type) {
        return type.isAnnotationPresent(SPI.class);
    }

    /**
     * Gets extension loader *
     *
     * @param <T>  parameter
     * @param type type
     * @return the extension loader
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    public static <T> SPILoader<T> getExtensionLoader(Class<T> type) {
        if (type == null) {
            throw new IllegalArgumentException("Extension type == null");
        }
        if (!type.isInterface()) {
            throw new IllegalArgumentException("Extension type (" + type + ") is not an interface!");
        }
        if (!withExtensionAnnotation(type)) {
            throw new IllegalArgumentException("Extension type (" + type + ") is not an extension, because it is NOT annotated with @"
                + SPI.class.getSimpleName()
                + "!");
        }

        SPILoader<T> loader = (SPILoader<T>) EXTENSION_LOADERS.get(type);
        if (loader == null) {
            EXTENSION_LOADERS.putIfAbsent(type, new SPILoader<T>(type));
            loader = (SPILoader<T>) EXTENSION_LOADERS.get(type);
        }
        return loader;
    }

    /**
     * Reset extension loader
     *
     * @param type type
     * @since 1.0.0
     */
    public static void resetExtensionLoader(Class type) {
        SPILoader loader = EXTENSION_LOADERS.get(type);
        if (loader != null) {
            // Remove all instances associated with this loader as well
            Map<String, Class<?>> classes = loader.getExtensionClasses();
            for (Map.Entry<String, Class<?>> entry : classes.entrySet()) {
                EXTENSION_INSTANCES.remove(entry.getValue());
            }
            classes.clear();
            EXTENSION_LOADERS.remove(type);
        }
    }

    /**
     * Find class loader
     *
     * @return the class loader
     * @since 1.0.0
     */
    private static ClassLoader findClassLoader() {
        return SpiClassUtils.getClassLoader(SPILoader.class);
    }

    /**
     * Gets extension name *
     *
     * @param extensionInstance extension instance
     * @return the extension name
     * @since 1.0.0
     */
    public String getExtensionName(T extensionInstance) {
        return this.getExtensionName(extensionInstance.getClass());
    }

    /**
     * Gets extension name *
     *
     * @param extensionClass extension class
     * @return the extension name
     * @since 1.0.0
     */
    public String getExtensionName(Class<?> extensionClass) {
        this.getExtensionClasses();
        return this.cachedNames.get(extensionClass);
    }

    /**
     * Gets activate extension *
     *
     * @param url url
     * @param key key
     * @return the activate extension
     * @since 1.0.0
     */
    public List<T> getActivateExtension(URL url, String key) {
        return this.getActivateExtension(url, key, null);
    }

    /**
     * Gets activate extension *
     *
     * @param url    url
     * @param values values
     * @return the activate extension
     * @since 1.0.0
     */
    public List<T> getActivateExtension(URL url, String[] values) {
        return this.getActivateExtension(url, values, null);
    }

    /**
     * Gets activate extension *
     *
     * @param url   url
     * @param key   key
     * @param group group
     * @return the activate extension
     * @since 1.0.0
     */
    public List<T> getActivateExtension(URL url, String key, String group) {
        String value = url.getParameter(key);
        return this.getActivateExtension(url, SpiStringUtils.isEmpty(value) ? null : COMMA_SPLIT_PATTERN.split(value), group);
    }

    /**
     * Gets activate extension *
     *
     * @param url    url
     * @param values values
     * @param group  group
     * @return the activate extension
     * @since 1.0.0
     */
    public List<T> getActivateExtension(URL url, String[] values, String group) {
        List<T> exts = new ArrayList<>();
        List<String> names = values == null ? new ArrayList<>(0) : Arrays.asList(values);
        if (!names.contains(REMOVE_VALUE_PREFIX + DEFAULT_KEY)) {
            this.getExtensionClasses();
            for (Map.Entry<String, Object> entry : this.cachedActivates.entrySet()) {
                String name = entry.getKey();
                Object activate = entry.getValue();

                String[] activateGroup, activateValue;

                if (activate instanceof Activate) {
                    activateGroup = ((Activate) activate).group();
                    activateValue = ((Activate) activate).value();
                } else if (activate instanceof Activate) {
                    activateGroup = ((Activate) activate).group();
                    activateValue = ((Activate) activate).value();
                } else {
                    continue;
                }
                if (this.isMatchGroup(group, activateGroup)
                    && !names.contains(name)
                    && !names.contains(REMOVE_VALUE_PREFIX + name)
                    && this.isActive(activateValue, url)) {
                    exts.add(this.getExtension(name));
                }
            }
            exts.sort(ActivateComparator.COMPARATOR);
        }
        List<T> usrs = new ArrayList<>();
        for (int i = 0; i < names.size(); i++) {
            String name = names.get(i);
            if (!name.startsWith(REMOVE_VALUE_PREFIX)
                && !names.contains(REMOVE_VALUE_PREFIX + name)) {
                if (DEFAULT_KEY.equals(name)) {
                    if (!usrs.isEmpty()) {
                        exts.addAll(0, usrs);
                        usrs.clear();
                    }
                } else {
                    usrs.add(this.getExtension(name));
                }
            }
        }
        if (!usrs.isEmpty()) {
            exts.addAll(usrs);
        }
        return exts;
    }

    /**
     * Is match group
     *
     * @param group  group
     * @param groups groups
     * @return the boolean
     * @since 1.0.0
     */
    private boolean isMatchGroup(String group, String[] groups) {
        if (SpiStringUtils.isEmpty(group)) {
            return true;
        }
        if (groups != null && groups.length > 0) {
            for (String g : groups) {
                if (group.equals(g)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Is active
     *
     * @param keys keys
     * @param url  url
     * @return the boolean
     * @since 1.0.0
     */
    private boolean isActive(String[] keys, URL url) {
        if (keys.length == 0) {
            return true;
        }
        for (String key : keys) {
            for (Map.Entry<String, String> entry : url.getParameters().entrySet()) {
                String k = entry.getKey();
                String v = entry.getValue();
                if ((k.equals(key) || k.endsWith("." + key))
                    && ConfigUtils.isNotEmpty(v)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Gets loaded extension *
     *
     * @param name name
     * @return the loaded extension
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    public T getLoadedExtension(String name) {
        if (SpiStringUtils.isEmpty(name)) {
            throw new IllegalArgumentException("Extension name == null");
        }
        Holder<Object> holder = this.getOrCreateHolder(name);
        return (T) holder.get();
    }

    /**
     * Gets or create holder *
     *
     * @param name name
     * @return the or create holder
     * @since 1.0.0
     */
    private Holder<Object> getOrCreateHolder(String name) {
        Holder<Object> holder = this.cachedInstances.get(name);
        if (holder == null) {
            this.cachedInstances.putIfAbsent(name, new Holder<>());
            holder = this.cachedInstances.get(name);
        }
        return holder;
    }

    /**
     * Gets loaded extensions *
     *
     * @return the loaded extensions
     * @since 1.0.0
     */
    public Set<String> getLoadedExtensions() {
        return Collections.unmodifiableSet(new TreeSet<>(this.cachedInstances.keySet()));
    }

    /**
     * Gets loaded adaptive extension instances *
     *
     * @return the loaded adaptive extension instances
     * @since 1.0.0
     */
    public Object getLoadedAdaptiveExtensionInstances() {
        return this.cachedAdaptiveInstance.get();
    }

    /**
     * Gets extension *
     *
     * @param name name
     * @return the extension
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    public T getExtension(String name) {
        if (SpiStringUtils.isEmpty(name)) {
            throw new IllegalArgumentException("Extension name == null");
        }
        if ("true".equals(name)) {
            return this.getDefaultExtension();
        }
        Holder<Object> holder = this.getOrCreateHolder(name);
        Object instance = holder.get();
        if (instance == null) {
            synchronized (holder) {
                instance = holder.get();
                if (instance == null) {
                    instance = this.createExtension(name);
                    holder.set(instance);
                }
            }
        }
        return (T) instance;
    }

    /**
     * Gets default extension *
     *
     * @return the default extension
     * @since 1.0.0
     */
    public T getDefaultExtension() {
        this.getExtensionClasses();
        if (SpiStringUtils.isBlank(this.cachedDefaultName) || "true".equals(this.cachedDefaultName)) {
            return null;
        }
        return this.getExtension(this.cachedDefaultName);
    }

    /**
     * Has extension
     *
     * @param name name
     * @return the boolean
     * @since 1.0.0
     */
    public boolean hasExtension(String name) {
        if (SpiStringUtils.isEmpty(name)) {
            throw new IllegalArgumentException("Extension name == null");
        }
        Class<?> c = this.getExtensionClass(name);
        return c != null;
    }

    /**
     * Gets supported extensions *
     *
     * @return the supported extensions
     * @since 1.0.0
     */
    public Set<String> getSupportedExtensions() {
        Map<String, Class<?>> clazzes = this.getExtensionClasses();
        return Collections.unmodifiableSet(new TreeSet<>(clazzes.keySet()));
    }

    /**
     * Gets default extension name *
     *
     * @return the default extension name
     * @since 1.0.0
     */
    public String getDefaultExtensionName() {
        this.getExtensionClasses();
        return this.cachedDefaultName;
    }

    /**
     * Add extension
     *
     * @param name  name
     * @param clazz clazz
     * @since 1.0.0
     */
    public void addExtension(String name, Class<?> clazz) {
        this.getExtensionClasses(); // load classes

        if (!this.type.isAssignableFrom(clazz)) {
            throw new IllegalStateException("Input type " +
                clazz + " doesn't implement the Extension " + this.type);
        }
        if (clazz.isInterface()) {
            throw new IllegalStateException("Input type " +
                clazz + " can't be interface!");
        }

        if (!clazz.isAnnotationPresent(Adaptive.class)) {
            if (SpiStringUtils.isBlank(name)) {
                throw new IllegalStateException("Extension name is blank (Extension " + this.type + ")!");
            }
            if (this.cachedClasses.get().containsKey(name)) {
                throw new IllegalStateException("Extension name " +
                    name + " already exists (Extension " + this.type + ")!");
            }

            this.cachedNames.put(clazz, name);
            this.cachedClasses.get().put(name, clazz);
        } else {
            if (this.cachedAdaptiveClass != null) {
                throw new IllegalStateException("Adaptive Extension already exists (Extension " + this.type + ")!");
            }

            this.cachedAdaptiveClass = clazz;
        }
    }

    /**
     * Replace extension
     *
     * @param name  name
     * @param clazz clazz
     * @since 1.0.0
     */
    @Deprecated
    public void replaceExtension(String name, Class<?> clazz) {
        this.getExtensionClasses(); // load classes

        if (!this.type.isAssignableFrom(clazz)) {
            throw new IllegalStateException("Input type " +
                clazz + " doesn't implement Extension " + this.type);
        }
        if (clazz.isInterface()) {
            throw new IllegalStateException("Input type " +
                clazz + " can't be interface!");
        }

        if (!clazz.isAnnotationPresent(Adaptive.class)) {
            if (SpiStringUtils.isBlank(name)) {
                throw new IllegalStateException("Extension name is blank (Extension " + this.type + ")!");
            }
            if (!this.cachedClasses.get().containsKey(name)) {
                throw new IllegalStateException("Extension name " +
                    name + " doesn't exist (Extension " + this.type + ")!");
            }

            this.cachedNames.put(clazz, name);
            this.cachedClasses.get().put(name, clazz);
            this.cachedInstances.remove(name);
        } else {
            if (this.cachedAdaptiveClass == null) {
                throw new IllegalStateException("Adaptive Extension doesn't exist (Extension " + this.type + ")!");
            }

            this.cachedAdaptiveClass = clazz;
            this.cachedAdaptiveInstance.set(null);
        }
    }

    /**
     * Gets adaptive extension *
     *
     * @return the adaptive extension
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    public T getAdaptiveExtension() {
        Object instance = this.cachedAdaptiveInstance.get();
        if (instance == null) {
            if (this.createAdaptiveInstanceError != null) {
                throw new IllegalStateException("Failed to create adaptive instance: " +
                    this.createAdaptiveInstanceError.toString(),
                    this.createAdaptiveInstanceError);
            }

            synchronized (this.cachedAdaptiveInstance) {
                instance = this.cachedAdaptiveInstance.get();
                if (instance == null) {
                    try {
                        instance = this.createAdaptiveExtension();
                        this.cachedAdaptiveInstance.set(instance);
                    } catch (Throwable t) {
                        this.createAdaptiveInstanceError = t;
                        throw new IllegalStateException("Failed to create adaptive instance: " + t.toString(), t);
                    }
                }
            }
        }

        return (T) instance;
    }

    /**
     * Find exception
     *
     * @param name name
     * @return the illegal state exception
     * @since 1.0.0
     */
    private IllegalStateException findException(String name) {
        for (Map.Entry<String, IllegalStateException> entry : this.exceptions.entrySet()) {
            if (entry.getKey().toLowerCase().contains(name.toLowerCase())) {
                return entry.getValue();
            }
        }
        StringBuilder buf = new StringBuilder("No such extension " + this.type.getName() + " by name " + name);


        int i = 1;
        for (Map.Entry<String, IllegalStateException> entry : this.exceptions.entrySet()) {
            if (i == 1) {
                buf.append(", possible causes: ");
            }

            buf.append("\r\n(");
            buf.append(i++);
            buf.append(") ");
            buf.append(entry.getKey());
            buf.append(":\r\n");
            buf.append(SpiStringUtils.toString(entry.getValue()));
        }
        return new IllegalStateException(buf.toString());
    }

    /**
     * Create extension
     *
     * @param name name
     * @return the t
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    private T createExtension(String name) {
        Class<?> clazz = this.getExtensionClasses().get(name);
        if (clazz == null) {
            throw this.findException(name);
        }
        try {
            T instance = (T) EXTENSION_INSTANCES.get(clazz);
            if (instance == null) {
                EXTENSION_INSTANCES.putIfAbsent(clazz, clazz.newInstance());
                instance = (T) EXTENSION_INSTANCES.get(clazz);
            }
            this.injectExtension(instance);
            Set<Class<?>> wrapperClasses = this.cachedWrapperClasses;
            if (CollectionUtils.isNotEmpty(wrapperClasses)) {
                for (Class<?> wrapperClass : wrapperClasses) {
                    instance = this.injectExtension((T) wrapperClass.getConstructor(this.type).newInstance(instance));
                }
            }
            return instance;
        } catch (Throwable t) {
            throw new IllegalStateException("Extension instance (name: " + name + ", class: " +
                this.type + ") couldn't be instantiated: " + t.getMessage(), t);
        }
    }

    /**
     * Inject extension
     *
     * @param instance instance
     * @return the t
     * @since 1.0.0
     */
    private T injectExtension(T instance) {

        if (this.objectFactory == null) {
            return instance;
        }

        try {
            for (Method method : instance.getClass().getMethods()) {
                if (!this.isSetter(method)) {
                    continue;
                }
                /**
                 * Check {@link DisableInject} to see if we need auto injection for this property
                 */
                if (method.getAnnotation(DisableInject.class) != null) {
                    continue;
                }
                Class<?> pt = method.getParameterTypes()[0];
                if (SpiReflectUtils.isPrimitives(pt)) {
                    continue;
                }

                try {
                    String property = this.getSetterProperty(method);
                    Object object = this.objectFactory.getExtension(pt, property);
                    if (object != null) {
                        method.invoke(instance, object);
                    }
                } catch (Exception e) {
                    log.error("Failed to inject via method " + method.getName()
                        + " of interface " + this.type.getName() + ": " + e.getMessage(), e);
                }

            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return instance;
    }

    /**
     * Gets setter property *
     *
     * @param method method
     * @return the setter property
     * @since 1.0.0
     */
    private String getSetterProperty(Method method) {
        return method.getName().length() > 3 ? method.getName().substring(3, 4).toLowerCase() + method.getName().substring(4) : "";
    }

    /**
     * Is setter
     *
     * @param method method
     * @return the boolean
     * @since 1.0.0
     */
    private boolean isSetter(Method method) {
        return method.getName().startsWith("set")
            && method.getParameterTypes().length == 1
            && Modifier.isPublic(method.getModifiers());
    }

    /**
     * Gets extension class *
     *
     * @param name name
     * @return the extension class
     * @since 1.0.0
     */
    private Class<?> getExtensionClass(String name) {
        if (this.type == null) {
            throw new IllegalArgumentException("Extension type == null");
        }
        if (name == null) {
            throw new IllegalArgumentException("Extension name == null");
        }
        return this.getExtensionClasses().get(name);
    }

    /**
     * Gets extension classes *
     *
     * @return the extension classes
     * @since 1.0.0
     */
    private Map<String, Class<?>> getExtensionClasses() {
        Map<String, Class<?>> classes = this.cachedClasses.get();
        if (classes == null) {
            synchronized (this.cachedClasses) {
                classes = this.cachedClasses.get();
                if (classes == null) {
                    classes = this.loadExtensionClasses();
                    this.cachedClasses.set(classes);
                }
            }
        }
        return classes;
    }

    /**
     * Load extension classes
     *
     * @return the map
     * @since 1.0.0
     */
    private Map<String, Class<?>> loadExtensionClasses() {
        this.cacheDefaultExtensionName();

        Map<String, Class<?>> extensionClasses = new HashMap<>();
        this.loadDirectory(extensionClasses, SPI_INTERNAL_DIRECTORY, this.type.getName());
        this.loadDirectory(extensionClasses, SPI_INTERNAL_DIRECTORY, this.type.getName().replace("org.apache", "com.alibaba"));
        this.loadDirectory(extensionClasses, SPI_DIRECTORY, this.type.getName());
        this.loadDirectory(extensionClasses, SPI_DIRECTORY, this.type.getName().replace("org.apache", "com.alibaba"));
        this.loadDirectory(extensionClasses, SERVICES_DIRECTORY, this.type.getName());
        this.loadDirectory(extensionClasses, SERVICES_DIRECTORY, this.type.getName().replace("org.apache", "com.alibaba"));
        return extensionClasses;
    }

    /**
     * Cache default extension name
     *
     * @since 1.0.0
     */
    private void cacheDefaultExtensionName() {
        SPI defaultAnnotation = this.type.getAnnotation(SPI.class);
        if (defaultAnnotation == null) {
            return;
        }

        String value = defaultAnnotation.value();
        if ((value = value.trim()).length() > 0) {
            String[] names = NAME_SEPARATOR.split(value);
            if (names.length > 1) {
                throw new IllegalStateException("More than 1 default extension name on extension " + this.type.getName()
                    + ": " + Arrays.toString(names));
            }
            if (names.length == 1) {
                this.cachedDefaultName = names[0];
            }
        }
    }

    /**
     * Load directory
     *
     * @param extensionClasses extension classes
     * @param dir              dir
     * @param type             type
     * @since 1.0.0
     */
    private void loadDirectory(Map<String, Class<?>> extensionClasses, String dir, String type) {
        String fileName = dir + type;
        try {
            Enumeration<java.net.URL> urls;
            ClassLoader classLoader = findClassLoader();
            if (classLoader != null) {
                urls = classLoader.getResources(fileName);
            } else {
                urls = ClassLoader.getSystemResources(fileName);
            }
            if (urls != null) {
                while (urls.hasMoreElements()) {
                    java.net.URL resourceURL = urls.nextElement();
                    this.loadResource(extensionClasses, classLoader, resourceURL);
                }
            }
        } catch (Throwable t) {
            log.error("Exception occurred when loading extension class (interface: " +
                type + ", description file: " + fileName + ").", t);
        }
    }

    /**
     * Load resource
     *
     * @param extensionClasses extension classes
     * @param classLoader      class loader
     * @param resourceURL      resource url
     * @since 1.0.0
     */
    private void loadResource(Map<String, Class<?>> extensionClasses, ClassLoader classLoader, java.net.URL resourceURL) {
        try {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(resourceURL.openStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    int ci = line.indexOf('#');
                    if (ci >= 0) {
                        line = line.substring(0, ci);
                    }
                    line = line.trim();
                    if (line.length() > 0) {
                        try {
                            String name = null;
                            int i = line.indexOf('=');
                            if (i > 0) {
                                name = line.substring(0, i).trim();
                                line = line.substring(i + 1).trim();
                            }
                            if (line.length() > 0) {
                                this.loadClass(extensionClasses, resourceURL, Class.forName(line, true, classLoader), name);
                            }
                        } catch (Throwable t) {
                            IllegalStateException e =
                                new IllegalStateException("Failed to load extension class (interface: " + this.type + "," +
                                    " class line: " + line + ") in " + resourceURL + ", cause" +
                                    ": " + t.getMessage(), t);
                            this.exceptions.put(line, e);
                        }
                    }
                }
            }
        } catch (Throwable t) {
            log.error("Exception occurred when loading extension class (interface: " +
                this.type + ", class file: " + resourceURL + ") in " + resourceURL, t);
        }
    }

    /**
     * Load class
     *
     * @param extensionClasses extension classes
     * @param resourceURL      resource url
     * @param clazz            clazz
     * @param name             name
     * @throws NoSuchMethodException no such method exception
     * @since 1.0.0
     */
    private void loadClass(Map<String, Class<?>> extensionClasses, java.net.URL resourceURL, Class<?> clazz, String name) throws NoSuchMethodException {
        if (!this.type.isAssignableFrom(clazz)) {
            throw new IllegalStateException("Error occurred when loading extension class (interface: " +
                this.type + ", class line: " + clazz.getName() + "), class "
                + clazz.getName() + " is not subtype of interface.");
        }
        if (clazz.isAnnotationPresent(Adaptive.class)) {
            this.cacheAdaptiveClass(clazz);
        } else if (this.isWrapperClass(clazz)) {
            this.cacheWrapperClass(clazz);
        } else {
            clazz.getConstructor();
            if (SpiStringUtils.isEmpty(name)) {
                name = this.findAnnotationName(clazz);
                if (name.length() == 0) {
                    throw new IllegalStateException("No such extension name for the class " + clazz.getName() + " in the config " + resourceURL);
                }
            }

            String[] names = NAME_SEPARATOR.split(name);
            if (ArrayUtils.isNotEmpty(names)) {
                this.cacheActivateClass(clazz, names[0]);
                for (String n : names) {
                    this.cacheName(clazz, n);
                    this.saveInExtensionClass(extensionClasses, clazz, n);
                }
            }
        }
    }

    /**
     * Cache name
     *
     * @param clazz clazz
     * @param name  name
     * @since 1.0.0
     */
    private void cacheName(Class<?> clazz, String name) {
        if (!this.cachedNames.containsKey(clazz)) {
            this.cachedNames.put(clazz, name);
        }
    }

    /**
     * Save in extension class
     *
     * @param extensionClasses extension classes
     * @param clazz            clazz
     * @param name             name
     * @since 1.0.0
     */
    private void saveInExtensionClass(Map<String, Class<?>> extensionClasses, Class<?> clazz, String name) {
        Class<?> c = extensionClasses.get(name);
        if (c == null) {
            extensionClasses.put(name, clazz);
        } else if (c != clazz) {
            String duplicateMsg =
                "Duplicate extension " + this.type.getName() + " name " + name + " on " + c.getName() + " and " + clazz.getName();
            log.error(duplicateMsg);
            throw new IllegalStateException(duplicateMsg);
        }
    }

    /**
     * Cache activate class
     *
     * @param clazz clazz
     * @param name  name
     * @since 1.0.0
     */
    private void cacheActivateClass(Class<?> clazz, String name) {
        Activate activate = clazz.getAnnotation(Activate.class);
        if (activate != null) {
            this.cachedActivates.put(name, activate);
        } else {
            // support Activate
            Activate oldActivate = clazz.getAnnotation(Activate.class);
            if (oldActivate != null) {
                this.cachedActivates.put(name, oldActivate);
            }
        }
    }

    /**
     * Cache adaptive class
     *
     * @param clazz clazz
     * @since 1.0.0
     */
    private void cacheAdaptiveClass(Class<?> clazz) {
        if (this.cachedAdaptiveClass == null) {
            this.cachedAdaptiveClass = clazz;
        } else if (!this.cachedAdaptiveClass.equals(clazz)) {
            throw new IllegalStateException("More than 1 adaptive class found: "
                + this.cachedAdaptiveClass.getName()
                + ", " + clazz.getName());
        }
    }

    /**
     * Cache wrapper class
     *
     * @param clazz clazz
     * @since 1.0.0
     */
    private void cacheWrapperClass(Class<?> clazz) {
        if (this.cachedWrapperClasses == null) {
            this.cachedWrapperClasses = new ConcurrentHashSet<>();
        }
        this.cachedWrapperClasses.add(clazz);
    }

    /**
     * Is wrapper class
     *
     * @param clazz clazz
     * @return the boolean
     * @since 1.0.0
     */
    private boolean isWrapperClass(Class<?> clazz) {
        try {
            clazz.getConstructor(this.type);
            return true;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

    /**
     * Find annotation name
     *
     * @param clazz clazz
     * @return the string
     * @since 1.0.0
     */
    @SuppressWarnings("deprecation")
    private String findAnnotationName(Class<?> clazz) {
        Extension extension = clazz.getAnnotation(Extension.class);
        if (extension != null) {
            return extension.value();
        }

        String name = clazz.getSimpleName();
        if (name.endsWith(this.type.getSimpleName())) {
            name = name.substring(0, name.length() - this.type.getSimpleName().length());
        }
        return name.toLowerCase();
    }

    /**
     * Create adaptive extension
     *
     * @return the t
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    private T createAdaptiveExtension() {
        try {
            return this.injectExtension((T) this.getAdaptiveExtensionClass().newInstance());
        } catch (Exception e) {
            throw new IllegalStateException("Can't create adaptive extension " + this.type + ", cause: " + e.getMessage(), e);
        }
    }

    /**
     * Gets adaptive extension class *
     *
     * @return the adaptive extension class
     * @since 1.0.0
     */
    private Class<?> getAdaptiveExtensionClass() {
        this.getExtensionClasses();
        if (this.cachedAdaptiveClass != null) {
            return this.cachedAdaptiveClass;
        }
        return this.cachedAdaptiveClass = this.createAdaptiveExtensionClass();
    }

    /**
     * Create adaptive extension class
     *
     * @return the class
     * @since 1.0.0
     */
    private Class<?> createAdaptiveExtensionClass() {
        String code = new AdaptiveClassCodeGenerator(this.type, this.cachedDefaultName).generate();
        ClassLoader classLoader = findClassLoader();
        Compiler compiler = SPILoader.getExtensionLoader(Compiler.class).getAdaptiveExtension();
        return compiler.compile(code, classLoader);
    }

    /**
     * To string
     *
     * @return the string
     * @since 1.0.0
     */
    @Override
    public String toString() {
        return this.getClass().getName() + "[" + this.type.getName() + "]";
    }

}
