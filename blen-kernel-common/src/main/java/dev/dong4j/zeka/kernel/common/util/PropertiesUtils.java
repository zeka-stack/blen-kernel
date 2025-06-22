package dev.dong4j.zeka.kernel.common.util;

import com.google.common.collect.Maps;
import dev.dong4j.zeka.kernel.common.config.PropertiesPropertySource;
import dev.dong4j.zeka.kernel.common.config.PropertyFilePropertySource;
import dev.dong4j.zeka.kernel.common.config.PropertySource;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.06.04 19:22
 * @see PropertySource
 * @since 1.4.0
 */
@SuppressWarnings("checkstyle:ModifierOrder")
public final class PropertiesUtils {

    /** PROPERTIES_FILE_NAME */
    private static final String PROPERTIES_FILE_NAME = "zeka-stack.component.properties";
    /** SYSTEM_PROPERTIES_FILE_NAME */
    private static final String SYSTEM_PROPERTIES_FILE_NAME = "zeka-stack.system.properties";
    /** PROPERTIES */
    private static final PropertiesUtils PROPERTIES = new PropertiesUtils(PROPERTIES_FILE_NAME);
    /** Environment */
    private final Environment environment;

    /**
     * 提供通过环境变量、属性文件和系统属性查找全局配置属性的支持
     *
     * @author dong4j
     * @version 1.0.0
     * @email "mailto:dong4j@gmail.com"
     * @date 2020.06.04 19:22
     * @since 1.4.0
     */
    private final static class Environment {

        /** Sources */
        private final Set<PropertySource> sources = new TreeSet<>(new PropertySource.Comparator());
        /** Literal */
        private final Map<CharSequence, String> literal = new ConcurrentHashMap<>();
        /** Normalized */
        private final Map<CharSequence, String> normalized = new ConcurrentHashMap<>();
        /** Tokenized */
        private final Map<List<CharSequence>, String> tokenized = new ConcurrentHashMap<>();

        /**
         * Environment
         *
         * @param propertySource property source
         * @since 1.5.0
         */
        private Environment(PropertySource propertySource) {
            PropertyFilePropertySource sysProps = new PropertyFilePropertySource(SYSTEM_PROPERTIES_FILE_NAME);
            try {
                sysProps.forEach((key, value) -> {
                    if (System.getProperty(key) == null) {
                        System.setProperty(key, value);
                    }
                });
            } catch (SecurityException ex) {
                // Access to System Properties is restricted so just skip it.
            }
            this.sources.add(propertySource);
            for (ClassLoader classLoader : LoaderUtils.getClassLoaders()) {
                try {
                    for (PropertySource source : ServiceLoader.load(PropertySource.class, classLoader)) {
                        this.sources.add(source);
                    }
                } catch (Throwable ignored) {
                }
            }

            this.reload();
        }

        /**
         * Reload
         *
         * @since 1.5.0
         */
        private synchronized void reload() {
            this.literal.clear();
            this.normalized.clear();
            this.tokenized.clear();
            for (PropertySource source : this.sources) {
                source.forEach((key, value) -> {
                    if (key != null && value != null) {
                        Environment.this.literal.put(key, value);
                        List<CharSequence> tokens = PropertySource.Util.tokenize(key);
                        if (tokens.isEmpty()) {
                            Environment.this.normalized.put(source.getNormalForm(Collections.singleton(key)), value);
                        } else {
                            Environment.this.normalized.put(source.getNormalForm(tokens), value);
                            Environment.this.tokenized.put(tokens, value);
                        }
                    }
                });
            }
        }

        /**
         * Has system property
         *
         * @param key key
         * @return the boolean
         * @since 1.5.0
         */
        private static boolean hasSystemProperty(String key) {
            try {
                return System.getProperties().containsKey(key);
            } catch (SecurityException ignored) {
                return false;
            }
        }

        /**
         * Get
         *
         * @param key key
         * @return the string
         * @since 1.5.0
         */
        private String get(String key) {
            if (this.normalized.containsKey(key)) {
                return this.normalized.get(key);
            }
            if (this.literal.containsKey(key)) {
                return this.literal.get(key);
            }
            if (hasSystemProperty(key)) {
                return System.getProperty(key);
            }
            return this.tokenized.get(PropertySource.Util.tokenize(key));
        }

        /**
         * Contains key
         *
         * @param key key
         * @return the boolean
         * @since 1.5.0
         */
        private boolean containsKey(String key) {
            return this.normalized.containsKey(key) ||
                this.literal.containsKey(key) ||
                hasSystemProperty(key) ||
                this.tokenized.containsKey(PropertySource.Util.tokenize(key));
        }
    }

    /**
     * Constructs a PropertiesUtil using a given Properties object as its source of defined properties.
     *
     * @param props the Properties to use by default
     * @since 1.5.0
     */
    public PropertiesUtils(Properties props) {
        this.environment = new Environment(new PropertiesPropertySource(props));
    }

    /**
     * Constructs a PropertiesUtil for a given properties file name on the classpath. The properties specified in this
     * file are used by default. If a property is not defined in this file, then the equivalent system property is used.
     *
     * @param propertiesFileName the location of properties file to load
     * @since 1.5.0
     */
    public PropertiesUtils(String propertiesFileName) {
        this.environment = new Environment(new PropertyFilePropertySource(propertiesFileName));
    }

    /**
     * Loads and closes the given property input stream. If an error occurs, log to the status logger.
     *
     * @param in     a property input stream.
     * @param source a source object describing the source, like a resource string or a URL.
     * @return a new Properties object
     * @since 1.5.0
     */
    static @NotNull Properties loadClose(InputStream in, Object source) {
        Properties props = new Properties();
        if (null != in) {
            try {
                props.load(in);
            } catch (IOException e) {
                LowLevelLogUtils.logException("Unable to read " + source, e);
            } finally {
                try {
                    in.close();
                } catch (IOException e) {
                    LowLevelLogUtils.logException("Unable to close " + source, e);
                }
            }
        }
        return props;
    }

    /**
     * Returns the PropertiesUtil used by Log4j.
     *
     * @return the main Log4j PropertiesUtil instance.
     * @since 1.5.0
     */
    @Contract(pure = true)
    public static PropertiesUtils getProperties() {
        return PROPERTIES;
    }

    /**
     * Returns {@code true} if the specified property is defined, regardless of its value (it may not have a value).
     *
     * @param name the name of the property to verify
     * @return {@code true} if the specified property is defined, regardless of its value
     * @since 1.5.0
     */
    public boolean hasProperty(String name) {
        return this.environment.containsKey(name);
    }

    /**
     * Gets the named property as a boolean value. If the property matches the string {@code "true"} (case-insensitive),
     * then it is returned as the boolean value {@code true}. Any other non-{@code null} text in the property is
     * considered {@code false}.
     *
     * @param name the name of the property to look up
     * @return the boolean value of the property or {@code false} if undefined.
     * @since 1.5.0
     */
    public boolean getBooleanProperty(String name) {
        return this.getBooleanProperty(name, false);
    }

    /**
     * Gets the named property as a boolean value.
     *
     * @param name         the name of the property to look up
     * @param defaultValue the default value to use if the property is undefined
     * @return the boolean value of the property or {@code defaultValue} if undefined.
     * @since 1.5.0
     */
    public boolean getBooleanProperty(String name, boolean defaultValue) {
        String prop = this.getStringProperty(name);
        return prop == null ? defaultValue : "true".equalsIgnoreCase(prop);
    }

    /**
     * Gets the named property as a boolean value.
     *
     * @param name                  the name of the property to look up
     * @param defaultValueIfAbsent  the default value to use if the property is undefined
     * @param defaultValueIfPresent the default value to use if the property is defined but not assigned
     * @return the boolean value of the property or {@code defaultValue} if undefined.
     * @since 1.5.0
     */
    public boolean getBooleanProperty(String name, boolean defaultValueIfAbsent,
                                      boolean defaultValueIfPresent) {
        String prop = this.getStringProperty(name);
        return prop == null ? defaultValueIfAbsent
            : prop.isEmpty() ? defaultValueIfPresent : "true".equalsIgnoreCase(prop);
    }

    /**
     * Gets the named property as a Charset value.
     *
     * @param name the name of the property to look up
     * @return the Charset value of the property or {@link Charset#defaultCharset()} if undefined.
     * @since 1.5.0
     */
    public Charset getCharsetProperty(String name) {
        return this.getCharsetProperty(name, Charset.defaultCharset());
    }

    /**
     * Gets the named property as a Charset value. If we cannot find the named Charset, see if it is mapped in
     * file {@code Log4j-charsets.properties} on the class path.
     *
     * @param name         the name of the property to look up
     * @param defaultValue the default value to use if the property is undefined
     * @return the Charset value of the property or {@code defaultValue} if undefined.
     * @since 1.5.0
     */
    public Charset getCharsetProperty(String name, Charset defaultValue) {
        String charsetName = this.getStringProperty(name);
        if (charsetName == null) {
            return defaultValue;
        }
        if (Charset.isSupported(charsetName)) {
            return Charset.forName(charsetName);
        }
        ResourceBundle bundle = getCharsetsResourceBundle();
        if (bundle.containsKey(name)) {
            String mapped = bundle.getString(name);
            if (Charset.isSupported(mapped)) {
                return Charset.forName(mapped);
            }
        }
        LowLevelLogUtils.log("Unable to get Charset '" + charsetName + "' for property '" + name + "', using default "
            + defaultValue + " and continuing.");
        return defaultValue;
    }

    /**
     * Gets the named property as a double.
     *
     * @param name         the name of the property to look up
     * @param defaultValue the default value to use if the property is undefined
     * @return the parsed double value of the property or {@code defaultValue} if it was undefined or could not be parsed.
     * @since 1.5.0
     */
    public double getDoubleProperty(String name, double defaultValue) {
        String prop = this.getStringProperty(name);
        if (prop != null) {
            try {
                return Double.parseDouble(prop);
            } catch (Exception ignored) {
                return defaultValue;
            }
        }
        return defaultValue;
    }

    /**
     * Gets the named property as an integer.
     *
     * @param name         the name of the property to look up
     * @param defaultValue the default value to use if the property is undefined
     * @return the parsed integer value of the property or {@code defaultValue} if it was undefined or could not be     parsed.
     * @since 1.5.0
     */
    public int getIntegerProperty(String name, int defaultValue) {
        String prop = this.getStringProperty(name);
        if (prop != null) {
            try {
                return Integer.parseInt(prop);
            } catch (Exception ignored) {
                return defaultValue;
            }
        }
        return defaultValue;
    }

    /**
     * Gets the named property as a long.
     *
     * @param name         the name of the property to look up
     * @param defaultValue the default value to use if the property is undefined
     * @return the parsed long value of the property or {@code defaultValue} if it was undefined or could not be parsed.
     * @since 1.5.0
     */
    public long getLongProperty(String name, long defaultValue) {
        String prop = this.getStringProperty(name);
        if (prop != null) {
            try {
                return Long.parseLong(prop);
            } catch (Exception ignored) {
                return defaultValue;
            }
        }
        return defaultValue;
    }

    /**
     * Gets the named property as a String.
     *
     * @param name the name of the property to look up
     * @return the String value of the property or {@code null} if undefined.
     * @since 1.5.0
     */
    public String getStringProperty(String name) {
        return this.environment.get(name);
    }

    /**
     * Gets the named property as a String.
     *
     * @param name         the name of the property to look up
     * @param defaultValue the default value to use if the property is undefined
     * @return the String value of the property or {@code defaultValue} if undefined.
     * @since 1.5.0
     */
    public String getStringProperty(String name, String defaultValue) {
        String prop = this.getStringProperty(name);
        return (prop == null) ? defaultValue : prop;
    }

    /**
     * Return the system properties or an empty Properties object if an error occurs.
     *
     * @return The system properties.
     * @since 1.5.0
     */
    @Contract(" -> new")
    public static @NotNull Properties getSystemProperties() {
        try {
            return new Properties(System.getProperties());
        } catch (SecurityException ex) {
            LowLevelLogUtils.logException("Unable to access system properties.", ex);
            // Sandboxed - can't read System Properties
            return new Properties();
        }
    }

    /**
     * Reloads all properties. This is primarily useful for unit tests.
     *
     * @since 1.4.0
     */
    public void reload() {
        this.environment.reload();
    }

    /**
     * Extracts properties that start with or are equals to the specific prefix and returns them in a new Properties
     * object with the prefix removed.
     *
     * @param properties The Properties to evaluate.
     * @param prefix     The prefix to extract.
     * @return The subset of properties.
     * @since 1.5.0
     */
    public static @NotNull Properties extractSubset(Properties properties, String prefix) {
        Properties subset = new Properties();

        if (prefix == null || prefix.length() == 0) {
            return subset;
        }

        String prefixToMatch = prefix.charAt(prefix.length() - 1) != '.' ? prefix + '.' : prefix;

        List<String> keys = new ArrayList<>();

        for (String key : properties.stringPropertyNames()) {
            if (key.startsWith(prefixToMatch)) {
                subset.setProperty(key.substring(prefixToMatch.length()), properties.getProperty(key));
                keys.add(key);
            }
        }
        for (String key : keys) {
            properties.remove(key);
        }

        return subset;
    }

    /**
     * Gets charsets resource bundle *
     *
     * @return the charsets resource bundle
     * @since 1.5.0
     */
    static ResourceBundle getCharsetsResourceBundle() {
        return ResourceBundle.getBundle("Log4j-charsets");
    }

    /**
     * Partitions a properties map based on common key prefixes up to the first period.
     *
     * @param properties properties to partition
     * @return the partitioned properties where each key is the common prefix (minus the period) and the values are     new property maps
     * without the prefix and period in the key
     * @since 1.4.0
     */
    public static @NotNull Map<String, Properties> partitionOnCommonPrefixes(@NotNull Properties properties) {
        Map<String, Properties> parts = Maps.newConcurrentMap();
        for (String key : properties.stringPropertyNames()) {
            String prefix = key.substring(0, key.indexOf('.'));
            if (!parts.containsKey(prefix)) {
                parts.put(prefix, new Properties());
            }
            parts.get(prefix).setProperty(key.substring(key.indexOf('.') + 1), properties.getProperty(key));
        }
        return parts;
    }

    /**
     * Returns true if system properties tell us we are running on Windows.
     *
     * @return true if system properties tell us we are running on Windows.
     * @since 1.5.0
     */
    @Contract(pure = true)
    public boolean isOsWindows() {
        return org.apache.commons.lang3.SystemUtils.IS_OS_WINDOWS;
    }

}
