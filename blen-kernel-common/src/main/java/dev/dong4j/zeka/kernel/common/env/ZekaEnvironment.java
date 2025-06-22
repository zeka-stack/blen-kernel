package dev.dong4j.zeka.kernel.common.env;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.context.properties.bind.PlaceholdersResolver;
import org.springframework.boot.context.properties.bind.PropertySourcesPlaceholdersResolver;
import org.springframework.boot.context.properties.source.ConfigurationPropertySources;
import org.springframework.boot.origin.Origin;
import org.springframework.boot.origin.OriginLookup;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.lang.Nullable;
import org.springframework.util.PropertyPlaceholderHelper;
import org.springframework.util.StringUtils;
import org.springframework.util.SystemPropertyUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * <p>Description: 解析所有配置</p>
 *
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.26 20:40
 * @since 1.0.0
 */
public class ZekaEnvironment {

    /** Sanitizer */
    private final Sanitizer sanitizer = new Sanitizer();

    /** Environment */
    private final Environment environment;

    /**
     * Instantiates a new environment.
     *
     * @param environment the environment
     * @since 1.0.0
     */
    public ZekaEnvironment(Environment environment) {
        this.environment = environment;
    }

    /**
     * Sets keys to sanitize.
     *
     * @param keysToSanitize the keys to sanitize
     * @since 1.0.0
     */
    public void setKeysToSanitize(String... keysToSanitize) {
        sanitizer.setKeysToSanitize(keysToSanitize);
    }

    /**
     * Environment environment descriptor.
     *
     * @param pattern the pattern
     * @return the environment descriptor
     * @since 1.0.0
     */
    public EnvironmentDescriptor environment(@Nullable String pattern) {
        if (StringUtils.hasText(pattern)) {
            return getEnvironmentDescriptor(Pattern.compile(pattern).asPredicate());
        }
        return getEnvironmentDescriptor((name) -> true);
    }

    /**
     * Environment entry environment entry descriptor.
     *
     * @param toMatch the to match
     * @return the environment entry descriptor
     * @since 1.0.0
     */
    public EnvironmentEntryDescriptor environmentEntry(String toMatch) {
        return getEnvironmentEntryDescriptor(toMatch);
    }

    /**
     * Gets environment descriptor *
     *
     * @param propertyNamePredicate property name predicate
     * @return the environment descriptor
     * @since 1.0.0
     */
    @NotNull
    @Contract("_ -> new")
    private EnvironmentDescriptor getEnvironmentDescriptor(Predicate<String> propertyNamePredicate) {
        PlaceholdersResolver resolver = getResolver();
        List<PropertySourceDescriptor> propertySources = new ArrayList<>();
        getPropertySourcesAsMap().forEach((sourceName, source) -> {
            if (source instanceof EnumerablePropertySource) {
                propertySources.add(describeSource(sourceName, (EnumerablePropertySource<?>) source, resolver,
                    propertyNamePredicate));
            }
        });
        return new EnvironmentDescriptor(Arrays.asList(environment.getActiveProfiles()), propertySources);
    }

    /**
     * Gets environment entry descriptor *
     *
     * @param propertyName property name
     * @return the environment entry descriptor
     * @since 1.0.0
     */
    @NotNull
    private EnvironmentEntryDescriptor getEnvironmentEntryDescriptor(String propertyName) {
        Map<String, PropertyValueDescriptor> descriptors = getPropertySourceDescriptors(propertyName);
        PropertySummaryDescriptor summary = getPropertySummaryDescriptor(descriptors);
        return new EnvironmentEntryDescriptor(summary, Arrays.asList(environment.getActiveProfiles()),
            toPropertySourceDescriptors(descriptors));
    }

    /**
     * To property source descriptors list
     *
     * @param descriptors descriptors
     * @return the list
     * @since 1.0.0
     */
    @NotNull
    private List<PropertySourceEntryDescriptor> toPropertySourceDescriptors(
        @NotNull Map<String, PropertyValueDescriptor> descriptors) {
        List<PropertySourceEntryDescriptor> result = new ArrayList<>();
        descriptors.forEach((name, property) -> result.add(new PropertySourceEntryDescriptor(name, property)));
        return result;
    }

    /**
     * Gets property summary descriptor *
     *
     * @param descriptors descriptors
     * @return the property summary descriptor
     * @since 1.0.0
     */
    @org.jetbrains.annotations.Nullable
    private PropertySummaryDescriptor getPropertySummaryDescriptor(@NotNull Map<String, PropertyValueDescriptor> descriptors) {
        for (Map.Entry<String, PropertyValueDescriptor> entry : descriptors.entrySet()) {
            if (entry.getValue() != null) {
                return new PropertySummaryDescriptor(entry.getKey(), entry.getValue().getValue());
            }
        }
        return null;
    }

    /**
     * Gets property source descriptors *
     *
     * @param propertyName property name
     * @return the property source descriptors
     * @since 1.0.0
     */
    @NotNull
    private Map<String, PropertyValueDescriptor> getPropertySourceDescriptors(String propertyName) {
        Map<String, PropertyValueDescriptor> propertySources = new LinkedHashMap<>();
        PlaceholdersResolver resolver = getResolver();
        getPropertySourcesAsMap()
            .forEach((sourceName, source) ->
                propertySources.put(sourceName, source.containsProperty(propertyName)
                    ? describeValueOf(propertyName, source, resolver)
                    : null));
        return propertySources;
    }

    /**
     * Describe source property source descriptor
     *
     * @param sourceName    source name
     * @param source        source
     * @param resolver      resolver
     * @param namePredicate name predicate
     * @return the property source descriptor
     * @since 1.0.0
     */
    @NotNull
    @Contract("_, _, _, _ -> new")
    private PropertySourceDescriptor describeSource(String sourceName, @NotNull EnumerablePropertySource<?> source,
                                                    PlaceholdersResolver resolver, Predicate<String> namePredicate) {
        Map<String, PropertyValueDescriptor> properties = new LinkedHashMap<>();
        Stream.of(source.getPropertyNames()).filter(namePredicate)
            .forEach((name) -> properties.put(name, describeValueOf(name, source, resolver)));
        return new PropertySourceDescriptor(sourceName, properties);
    }

    /**
     * Describe value of property value descriptor
     *
     * @param name     name
     * @param source   source
     * @param resolver resolver
     * @return the property value descriptor
     * @since 1.0.0
     */
    @NotNull
    @SuppressWarnings("unchecked")
    private PropertyValueDescriptor describeValueOf(String name, @NotNull PropertySource<?> source,
                                                    @NotNull PlaceholdersResolver resolver) {
        Object resolved = resolver.resolvePlaceholders(source.getProperty(name));
        String origin = ((source instanceof OriginLookup) ? getOrigin((OriginLookup<Object>) source, name) : null);
        return new PropertyValueDescriptor(sanitize(name, resolved), origin);
    }

    /**
     * Gets origin *
     *
     * @param lookup lookup
     * @param name   name
     * @return the origin
     * @since 1.0.0
     */
    @org.jetbrains.annotations.Nullable
    private String getOrigin(@NotNull OriginLookup<Object> lookup, String name) {
        Origin origin = lookup.getOrigin(name);
        return (origin != null) ? origin.toString() : null;
    }

    /**
     * Gets resolver *
     *
     * @return the resolver
     * @since 1.0.0
     */
    @NotNull
    @Contract(" -> new")
    private PlaceholdersResolver getResolver() {
        return new PropertySourcesPlaceholdersSanitizingResolver(getPropertySources(), sanitizer);
    }

    /**
     * Gets property sources as map *
     *
     * @return the property sources as map
     * @since 1.0.0
     */
    @NotNull
    private Map<String, PropertySource<?>> getPropertySourcesAsMap() {
        Map<String, PropertySource<?>> map = new LinkedHashMap<>();
        for (PropertySource<?> source : getPropertySources()) {
            if (!ConfigurationPropertySources.isAttachedConfigurationPropertySource(source)) {
                extract("", map, source);
            }
        }
        return map;
    }

    /**
     * Gets property sources *
     *
     * @return the property sources
     * @since 1.0.0
     */
    @NotNull
    private MutablePropertySources getPropertySources() {
        if (environment instanceof ConfigurableEnvironment) {
            return ((ConfigurableEnvironment) environment).getPropertySources();
        }
        return new StandardEnvironment().getPropertySources();
    }

    /**
     * Extract *
     *
     * @param root   root
     * @param map    map
     * @param source source
     * @since 1.0.0
     */
    private void extract(String root, Map<String, PropertySource<?>> map, PropertySource<?> source) {
        if (source instanceof CompositePropertySource) {
            for (PropertySource<?> nest : ((CompositePropertySource) source).getPropertySources()) {
                extract(source.getName() + ":", map, nest);
            }
        } else {
            map.put(root + source.getName(), source);
        }
    }

    /**
     * Sanitize object
     *
     * @param name   name
     * @param object object
     * @return the object
     * @since 1.0.0
     */
    private Object sanitize(String name, Object object) {
        return sanitizer.sanitize(name, object);
    }

    /**
     * {@link PropertySourcesPlaceholdersResolver} that sanitizes sensitive placeholders
     * if present.
     *
     * @author dong4j
     * @version 1.2.3
     * @email "mailto:dong4j@gmail.com"
     * @date 2020.01.26 20:40
     * @since 1.0.0
     */
    private static class PropertySourcesPlaceholdersSanitizingResolver extends PropertySourcesPlaceholdersResolver {

        /** Sanitizer */
        private final Sanitizer sanitizer;

        /**
         * Instantiates a new Property sources placeholders sanitizing resolver.
         *
         * @param sources   the sources
         * @param sanitizer the sanitizer
         * @since 1.0.0
         */
        PropertySourcesPlaceholdersSanitizingResolver(Iterable<PropertySource<?>> sources, Sanitizer sanitizer) {
            super(sources, new PropertyPlaceholderHelper(SystemPropertyUtils.PLACEHOLDER_PREFIX,
                SystemPropertyUtils.PLACEHOLDER_SUFFIX,
                SystemPropertyUtils.VALUE_SEPARATOR,
                true));
            this.sanitizer = sanitizer;
        }

        /**
         * Resolve placeholder string
         *
         * @param placeholder placeholder
         * @return the string
         * @since 1.0.0
         */
        @Override
        protected String resolvePlaceholder(String placeholder) {
            String value = super.resolvePlaceholder(placeholder);
            if (value == null) {
                return null;
            }
            return (String) sanitizer.sanitize(placeholder, value);
        }

    }

    /**
     * A description of an {@link Environment}.
     *
     * @author dong4j
     * @version 1.2.3
     * @email "mailto:dong4j@gmail.com"
     * @date 2020.01.26 20:40
     * @since 1.0.0
     */
    public static final class EnvironmentDescriptor {

        /** Active profiles */
        private final List<String> activeProfiles;

        /** Property sources */
        private final List<PropertySourceDescriptor> propertySources;

        /**
         * Environment descriptor
         *
         * @param activeProfiles  active profiles
         * @param propertySources property sources
         * @since 1.0.0
         */
        @Contract(pure = true)
        private EnvironmentDescriptor(List<String> activeProfiles, List<PropertySourceDescriptor> propertySources) {
            this.activeProfiles = activeProfiles;
            this.propertySources = propertySources;
        }

        /**
         * Gets active profiles.
         *
         * @return the active profiles
         * @since 1.0.0
         */
        @Contract(pure = true)
        public List<String> getActiveProfiles() {
            return activeProfiles;
        }

        /**
         * Gets property sources.
         *
         * @return the property sources
         * @since 1.0.0
         */
        @Contract(pure = true)
        public List<PropertySourceDescriptor> getPropertySources() {
            return propertySources;
        }

    }

    /**
     * A description of an entry of the {@link Environment}.
     *
     * @author dong4j
     * @version 1.2.3
     * @email "mailto:dong4j@gmail.com"
     * @date 2020.01.26 20:40
     * @since 1.0.0
     */
    public static final class EnvironmentEntryDescriptor {

        /** Property */
        private final PropertySummaryDescriptor property;

        /** Active profiles */
        private final List<String> activeProfiles;

        /** Property sources */
        private final List<PropertySourceEntryDescriptor> propertySources;

        /**
         * Environment entry descriptor
         *
         * @param property        property
         * @param activeProfiles  active profiles
         * @param propertySources property sources
         * @since 1.0.0
         */
        @Contract(pure = true)
        private EnvironmentEntryDescriptor(PropertySummaryDescriptor property, List<String> activeProfiles,
                                           List<PropertySourceEntryDescriptor> propertySources) {
            this.property = property;
            this.activeProfiles = activeProfiles;
            this.propertySources = propertySources;
        }

        /**
         * Gets property.
         *
         * @return the property
         * @since 1.0.0
         */
        @Contract(pure = true)
        public PropertySummaryDescriptor getProperty() {
            return property;
        }

        /**
         * Gets active profiles.
         *
         * @return the active profiles
         * @since 1.0.0
         */
        @Contract(pure = true)
        public List<String> getActiveProfiles() {
            return activeProfiles;
        }

        /**
         * Gets property sources.
         *
         * @return the property sources
         * @since 1.0.0
         */
        @Contract(pure = true)
        public List<PropertySourceEntryDescriptor> getPropertySources() {
            return propertySources;
        }

    }

    /**
     * A summary of a particular entry of the {@link Environment}.
     *
     * @author dong4j
     * @version 1.2.3
     * @email "mailto:dong4j@gmail.com"
     * @date 2020.01.26 20:40
     * @since 1.0.0
     */
    public static final class PropertySummaryDescriptor {

        /** Source */
        private final String source;

        /** Value */
        private final Object value;

        /**
         * Instantiates a new Property summary descriptor.
         *
         * @param source the source
         * @param value  the value
         * @since 1.0.0
         */
        @Contract(pure = true)
        public PropertySummaryDescriptor(String source, Object value) {
            this.source = source;
            this.value = value;
        }

        /**
         * Gets source.
         *
         * @return the source
         * @since 1.0.0
         */
        @Contract(pure = true)
        public String getSource() {
            return source;
        }

        /**
         * Gets value.
         *
         * @return the value
         * @since 1.0.0
         */
        @Contract(pure = true)
        public Object getValue() {
            return value;
        }

    }

    /**
     * A description of a {@link PropertySource}.
     *
     * @author dong4j
     * @version 1.2.3
     * @email "mailto:dong4j@gmail.com"
     * @date 2020.01.26 20:40
     * @since 1.0.0
     */
    public static final class PropertySourceDescriptor {

        /** Name */
        private final String name;

        /** Properties */
        private final Map<String, PropertyValueDescriptor> properties;

        /**
         * Property source descriptor
         *
         * @param name       name
         * @param properties properties
         * @since 1.0.0
         */
        @Contract(pure = true)
        private PropertySourceDescriptor(String name, Map<String, PropertyValueDescriptor> properties) {
            this.name = name;
            this.properties = properties;
        }

        /**
         * Gets name.
         *
         * @return the name
         * @since 1.0.0
         */
        @Contract(pure = true)
        public String getName() {
            return name;
        }

        /**
         * Gets properties.
         *
         * @return the properties
         * @since 1.0.0
         */
        @Contract(pure = true)
        public Map<String, PropertyValueDescriptor> getProperties() {
            return properties;
        }

    }

    /**
     * A description of a particular entry of {@link PropertySource}.
     *
     * @author dong4j
     * @version 1.2.3
     * @email "mailto:dong4j@gmail.com"
     * @date 2020.01.26 20:40
     * @since 1.0.0
     */
    public static final class PropertySourceEntryDescriptor {

        /** Name */
        private final String name;

        /** Property */
        private final PropertyValueDescriptor property;

        /**
         * Property source entry descriptor
         *
         * @param name     name
         * @param property property
         * @since 1.0.0
         */
        @Contract(pure = true)
        private PropertySourceEntryDescriptor(String name, PropertyValueDescriptor property) {
            this.name = name;
            this.property = property;
        }

        /**
         * Gets name.
         *
         * @return the name
         * @since 1.0.0
         */
        @Contract(pure = true)
        public String getName() {
            return name;
        }

        /**
         * Gets property.
         *
         * @return the property
         * @since 1.0.0
         */
        @Contract(pure = true)
        public PropertyValueDescriptor getProperty() {
            return property;
        }

    }

    /**
     * A description of a property's value, including its origin if available.
     *
     * @author dong4j
     * @version 1.2.3
     * @email "mailto:dong4j@gmail.com"
     * @date 2020.01.26 20:40
     * @since 1.0.0
     */
    public static final class PropertyValueDescriptor {

        /** Value */
        private final Object value;

        /** Origin */
        private final String origin;

        /**
         * Property value descriptor
         *
         * @param value  value
         * @param origin origin
         * @since 1.0.0
         */
        @Contract(pure = true)
        private PropertyValueDescriptor(Object value, String origin) {
            this.value = value;
            this.origin = origin;
        }

        /**
         * Gets value.
         *
         * @return the value
         * @since 1.0.0
         */
        @Contract(pure = true)
        public Object getValue() {
            return value;
        }

        /**
         * Gets origin.
         *
         * @return the origin
         * @since 1.0.0
         */
        @Contract(pure = true)
        public String getOrigin() {
            return origin;
        }

    }

}
