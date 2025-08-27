package dev.dong4j.zeka.kernel.common.util;

import dev.dong4j.zeka.kernel.common.constant.ConfigKey;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * <p>Description: 类加载器的实用程序类 </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.06.04 19:21
 * @see ClassLoader
 * @see RuntimePermission
 * @see Thread#getContextClassLoader()
 * @see ClassLoader#getSystemClassLoader()
 * @since 1.4.0
 */
@UtilityClass
public final class LoaderUtils {

    /** 如果设置为 true, 则只使用本地类加载器, 如果没有设置则使用线程上下文加载器 */
    public static final String IGNORE_TCCL_PROPERTY = ConfigKey.PREFIX + "ignoreTCL";
    /** ignoreTCCL */
    private static Boolean ignoreTCCL;
    /**
     * GET_CLASS_LOADER_DISABLED
     * JDK 17+ 默认允许获取 ClassLoader，无需安全检查
     */
    private static final boolean GET_CLASS_LOADER_DISABLED = false;
    /** TCCL_GETTER */
    private static final PrivilegedAction<ClassLoader> TCCL_GETTER = new ThreadContextClassLoaderGetter();

    /**
     * 获取当前线程类加载器.
     * 如果TCCL为空, 则返回系统类加载器.
     * 如果系统类加载器也为空, 则返回该类的类加载器.
     * 如果使用不允许访问线程类加载器或系统类加载器的SecurityManager运行, 则返回该类的类加载器.
     *
     * @return the current ThreadContextClassLoader.
     * @since 1.5.0
     */
    public static ClassLoader getThreadContextClassLoader() {
        if (GET_CLASS_LOADER_DISABLED) {
            // 如果禁止获取 TCCL，就返回当前类的 ClassLoader
            return LoaderUtils.class.getClassLoader();
        }
        // 直接运行 PrivilegedAction，不再使用 AccessController
        return TCCL_GETTER.run();
    }

    /**
     * <p>Description: </p>
     *
     * @author dong4j
     * @version 1.0.0
     * @email "mailto:dong4j@gmail.com"
     * @date 2020.06.04 19:21
     * @since 1.5.0
     */
    private static class ThreadContextClassLoaderGetter implements PrivilegedAction<ClassLoader> {
        /**
         * Run
         *
         * @return the class loader
         * @since 1.5.0
         */
        @Override
        public ClassLoader run() {
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            if (cl != null) {
                return cl;
            }
            ClassLoader ccl = LoaderUtils.class.getClassLoader();
            return ccl == null && !GET_CLASS_LOADER_DISABLED ? ClassLoader.getSystemClassLoader() : ccl;
        }
    }

    /**
     * Get class loaders
     *
     * @return the class loader [ ]
     * @since 1.5.0
     */
    public static ClassLoader[] getClassLoaders() {
        List<ClassLoader> classLoaders = new ArrayList<>();
        ClassLoader tcl = getThreadContextClassLoader();
        classLoaders.add(tcl);
        // Some implementations may use null to represent the bootstrap class loader.
        ClassLoader current = LoaderUtils.class.getClassLoader();
        if (current != null && current != tcl) {
            classLoaders.add(current);
            ClassLoader parent = current.getParent();
            while (parent != null && !classLoaders.contains(parent)) {
                classLoaders.add(parent);
            }
        }
        ClassLoader parent = tcl == null ? null : tcl.getParent();
        while (parent != null && !classLoaders.contains(parent)) {
            classLoaders.add(parent);
            parent = parent.getParent();
        }
        ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
        if (!classLoaders.contains(systemClassLoader)) {
            classLoaders.add(systemClassLoader);
        }
        return classLoaders.toArray(new ClassLoader[0]);
    }

    /**
     * Determines if a named Class can be loaded or not.
     *
     * @param className The class name.
     * @return {@code true} if the class could be found or {@code false} otherwise.
     * @since 1.4.0
     */
    public static boolean isClassAvailable(String className) {
        try {
            Class<?> clazz = loadClass(className);
            return clazz != null;
        } catch (ClassNotFoundException | LinkageError e) {
            return false;
        } catch (Throwable e) {
            LowLevelLogUtils.logException("Unknown error checking for existence of class: " + className, e);
            return false;
        }
    }

    /**
     * 按名称加载类, 如果指定 IGNORE_TCCL_PROPERTY 属性并将其设置为除 false 之外的任何值, 则将使用默认的类加载器
     *
     * @param className The class name.
     * @return the Class for the given name.
     * @throws ClassNotFoundException if the specified class name could not be found
     * @since 1.4.0
     */
    public static Class<?> loadClass(String className) throws ClassNotFoundException {
        if (isIgnoreTccl()) {
            return Class.forName(className);
        }
        try {
            return getThreadContextClassLoader().loadClass(className);
        } catch (Throwable ignored) {
            return Class.forName(className);
        }
    }

    /**
     * Loads and instantiates a Class using the default constructor.
     *
     * @param <T>   parameter
     * @param clazz The class.
     * @return new instance of the class.
     * @throws InstantiationException    if there was an exception whilst instantiating the class
     * @throws IllegalAccessException    if the class can't be instantiated through a public constructor
     * @throws InvocationTargetException if there was an exception whilst constructing the class
     * @since 1.4.0
     */
    public static <T> @NotNull T newInstanceOf(@NotNull Class<T> clazz)
        throws InstantiationException, IllegalAccessException, InvocationTargetException {
        try {
            return clazz.getConstructor().newInstance();
        } catch (NoSuchMethodException ignored) {
            try {
                return clazz.getDeclaredConstructor().newInstance();
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Loads and instantiates a Class using the default constructor.
     *
     * @param <T>       parameter
     * @param className The class name.
     * @return new instance of the class.
     * @throws ClassNotFoundException    if the class isn't available to the usual ClassLoaders
     * @throws IllegalAccessException    if the class can't be instantiated through a public constructor
     * @throws InstantiationException    if there was an exception whilst instantiating the class
     * @throws NoSuchMethodException     if there isn't a no-args constructor on the class
     * @throws InvocationTargetException if there was an exception whilst constructing the class
     * @since 1.4.0
     */
    @SuppressWarnings("unchecked")
    public static <T> @NotNull T newInstanceOf(String className) throws ClassNotFoundException, IllegalAccessException,
        InstantiationException, NoSuchMethodException,
        InvocationTargetException {
        return newInstanceOf((Class<T>) loadClass(className));
    }

    /**
     * Loads and instantiates a derived class using its default constructor.
     *
     * @param <T>       The type of the class to check.
     * @param className The class name.
     * @param clazz     The class to cast it to.
     * @return new instance of the class cast to {@code T}
     * @throws ClassNotFoundException    if the class isn't available to the usual ClassLoaders
     * @throws NoSuchMethodException     if there isn't a no-args constructor on the class
     * @throws InvocationTargetException if there was an exception whilst constructing the class
     * @throws InstantiationException    if there was an exception whilst instantiating the class
     * @throws IllegalAccessException    if the class can't be instantiated through a public constructor
     * @since 1.4.0
     */
    @Contract("_, _ -> param1")
    public static <T> T newCheckedInstanceOf(String className, @NotNull Class<T> clazz)
        throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException,
        IllegalAccessException {
        return clazz.cast(newInstanceOf(className));
    }

    /**
     * Loads and instantiates a class given by a property name.
     *
     * @param <T>          The type to cast it to.
     * @param propertyName The property name to look up a class name for.
     * @param clazz        The class to cast it to.
     * @return new instance of the class given in the property or {@code null} if the property was unset.
     * @throws ClassNotFoundException    if the class isn't available to the usual ClassLoaders
     * @throws NoSuchMethodException     if there isn't a no-args constructor on the class
     * @throws InvocationTargetException if there was an exception whilst constructing the class
     * @throws InstantiationException    if there was an exception whilst instantiating the class
     * @throws IllegalAccessException    if the class can't be instantiated through a public constructor
     * @since 1.4.0
     */
    public static <T> @Nullable T newCheckedInstanceOfProperty(String propertyName, Class<T> clazz)
        throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException,
        IllegalAccessException {
        String className = PropertiesUtils.getProperties().getStringProperty(propertyName);
        if (className == null) {
            return null;
        }
        return newCheckedInstanceOf(className, clazz);
    }

    /**
     * Is ignore tccl
     *
     * @return the boolean
     * @since 1.5.0
     */
    private static boolean isIgnoreTccl() {
        if (ignoreTCCL == null) {
            String ignoreTccl = PropertiesUtils.getProperties().getStringProperty(IGNORE_TCCL_PROPERTY, null);
            ignoreTCCL = ignoreTccl != null && !"false".equalsIgnoreCase(ignoreTccl.trim());
        }
        return ignoreTCCL;
    }

    /**
     * Finds classpath {@linkplain URL resources}.
     *
     * @param resource the name of the resource to find.
     * @return a Collection of URLs matching the resource name. If no resources could be found, then this will be empty.
     * @since 1.4.0
     */
    public static @NotNull Collection<URL> findResources(String resource) {
        Collection<UrlResource> urlResources = findUrlResources(resource);
        Collection<URL> resources = new LinkedHashSet<>(urlResources.size());
        for (UrlResource urlResource : urlResources) {
            resources.add(urlResource.url());
        }
        return resources;
    }

    /**
     * Find url resources
     *
     * @param resource resource
     * @return the collection
     * @since 1.5.0
     */
    @SuppressWarnings("PMD.Indentation")
    static @NotNull Collection<UrlResource> findUrlResources(String resource) {
        // @formatter:off
        ClassLoader[] candidates = {
            getThreadContextClassLoader(),
            LoaderUtils.class.getClassLoader(),
            GET_CLASS_LOADER_DISABLED ? null : ClassLoader.getSystemClassLoader()};
        // @formatter:on
        Collection<UrlResource> resources = new LinkedHashSet<>();
        for (ClassLoader cl : candidates) {
            if (cl != null) {
                try {
                    Enumeration<URL> resourceEnum = cl.getResources(resource);
                    while (resourceEnum.hasMoreElements()) {
                        resources.add(new UrlResource(cl, resourceEnum.nextElement()));
                    }
                } catch (IOException e) {
                    LowLevelLogUtils.logException(e);
                }
            }
        }
        return resources;
    }

    /**
     * {@link URL} and {@link ClassLoader} pair.
     *
     * @param classLoader Class loader
     * @param url         Url
     * @author dong4j
     * @version 1.0.0
     * @email "mailto:dong4j@gmail.com"
     * @date 2020.06.04 19:21
     * @since 1.5.0
     */
        record UrlResource(ClassLoader classLoader, URL url) {
        /**
         * Url resource
         *
         * @param classLoader class loader
         * @param url         url
         * @since 1.5.0
         */
        @Contract(pure = true)
        UrlResource {
        }

            /**
             * Equals
             *
             * @param o o
             * @return the boolean
             * @since 1.5.0
             */
            @Contract(value = "null -> false", pure = true)
            @Override
            public boolean equals(Object o) {
                if (this == o) {
                    return true;
                }
                if (o == null || this.getClass() != o.getClass()) {
                    return false;
                }

                UrlResource that = (UrlResource) o;

                if (!Objects.equals(this.classLoader, that.classLoader)) {
                    return false;
                }
                return Objects.equals(this.url, that.url);
            }

    }
}
