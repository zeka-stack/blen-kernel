package dev.dong4j.zeka.kernel.spi.utils;


import dev.dong4j.zeka.kernel.spi.constants.CommonConstants;
import dev.dong4j.zeka.kernel.spi.extension.SPILoader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;

import static dev.dong4j.zeka.kernel.spi.constants.CommonConstants.COMMA_SPLIT_PATTERN;
import static dev.dong4j.zeka.kernel.spi.constants.CommonConstants.DEFAULT_KEY;
import static dev.dong4j.zeka.kernel.spi.constants.CommonConstants.REMOVE_VALUE_PREFIX;


/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmaidl.com"
 * @date 2021.02.26 17:47
 * @since 1.0.0
 */
@Slf4j
@SuppressWarnings("all")
public class ConfigUtils {

    /** VARIABLE_PATTERN */
    private static Pattern VARIABLE_PATTERN = Pattern.compile(
        "\\$\\s*\\{?\\s*([\\._0-9a-zA-Z]+)\\s*\\}?");
    /** PROPERTIES */
    private static volatile Properties PROPERTIES;
    /** PID */
    private static int PID = -1;

    /**
     * Config utils
     *
     * @since 1.0.0
     */
    private ConfigUtils() {
    }

    /**
     * Is not empty
     *
     * @param value value
     * @return the boolean
     * @since 1.0.0
     */
    public static boolean isNotEmpty(String value) {
        return !isEmpty(value);
    }

    /**
     * Is empty
     *
     * @param value value
     * @return the boolean
     * @since 1.0.0
     */
    public static boolean isEmpty(String value) {
        return SpiStringUtils.isEmpty(value)
            || "false".equalsIgnoreCase(value)
            || "0".equalsIgnoreCase(value)
            || "null".equalsIgnoreCase(value)
            || "N/A".equalsIgnoreCase(value);
    }

    /**
     * Is default
     *
     * @param value value
     * @return the boolean
     * @since 1.0.0
     */
    public static boolean isDefault(String value) {
        return "true".equalsIgnoreCase(value)
            || "default".equalsIgnoreCase(value);
    }

    /**
     * Merge values
     *
     * @param type type
     * @param cfg  cfg
     * @param def  def
     * @return the list
     * @since 1.0.0
     */
    public static List<String> mergeValues(Class<?> type, String cfg, List<String> def) {
        List<String> defaults = new ArrayList<String>();
        if (def != null) {
            for (String name : def) {
                if (SPILoader.getExtensionLoader(type).hasExtension(name)) {
                    defaults.add(name);
                }
            }
        }

        List<String> names = new ArrayList<String>();

        // add initial values
        String[] configs = (cfg == null || cfg.trim().length() == 0) ? new String[0] : COMMA_SPLIT_PATTERN.split(cfg);
        for (String config : configs) {
            if (config != null && config.trim().length() > 0) {
                names.add(config);
            }
        }

        // -default is not included
        if (!names.contains(REMOVE_VALUE_PREFIX + DEFAULT_KEY)) {
            // add default extension
            int i = names.indexOf(DEFAULT_KEY);
            if (i > 0) {
                names.addAll(i, defaults);
            } else {
                names.addAll(0, defaults);
            }
            names.remove(DEFAULT_KEY);
        } else {
            names.remove(DEFAULT_KEY);
        }

        // merge - configuration
        for (String name : new ArrayList<String>(names)) {
            if (name.startsWith(REMOVE_VALUE_PREFIX)) {
                names.remove(name);
                names.remove(name.substring(1));
            }
        }
        return names;
    }

    /**
     * Replace property
     *
     * @param expression expression
     * @param params     params
     * @return the string
     * @since 1.0.0
     */
    public static String replaceProperty(String expression, Map<String, String> params) {
        if (expression == null || expression.length() == 0 || expression.indexOf('$') < 0) {
            return expression;
        }
        Matcher matcher = VARIABLE_PATTERN.matcher(expression);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String key = matcher.group(1);
            String value = System.getProperty(key);
            if (value == null && params != null) {
                value = params.get(key);
            }
            if (value == null) {
                value = "";
            }
            matcher.appendReplacement(sb, Matcher.quoteReplacement(value));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * Gets properties *
     *
     * @return the properties
     * @since 1.0.0
     */
    public static Properties getProperties() {
        if (PROPERTIES == null) {
            synchronized (ConfigUtils.class) {
                if (PROPERTIES == null) {
                    String path = System.getProperty(CommonConstants.DUBBO_PROPERTIES_KEY);
                    if (path == null || path.length() == 0) {
                        path = System.getenv(CommonConstants.DUBBO_PROPERTIES_KEY);
                        if (path == null || path.length() == 0) {
                            path = CommonConstants.DEFAULT_DUBBO_PROPERTIES;
                        }
                    }
                    PROPERTIES = ConfigUtils.loadProperties(path, false, true);
                }
            }
        }
        return PROPERTIES;
    }

    /**
     * Sets properties *
     *
     * @param properties properties
     * @since 1.0.0
     */
    public static void setProperties(Properties properties) {
        PROPERTIES = properties;
    }

    /**
     * Add properties
     *
     * @param properties properties
     * @since 1.0.0
     */
    public static void addProperties(Properties properties) {
        if (properties != null) {
            getProperties().putAll(properties);
        }
    }

    /**
     * Gets property *
     *
     * @param key key
     * @return the property
     * @since 1.0.0
     */
    public static String getProperty(String key) {
        return getProperty(key, null);
    }

    /**
     * Gets property *
     *
     * @param key          key
     * @param defaultValue default value
     * @return the property
     * @since 1.0.0
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static String getProperty(String key, String defaultValue) {
        String value = System.getProperty(key);
        if (value != null && value.length() > 0) {
            return value;
        }
        Properties properties = getProperties();
        return replaceProperty(properties.getProperty(key, defaultValue), (Map) properties);
    }

    /**
     * Gets system property *
     *
     * @param key key
     * @return the system property
     * @since 1.0.0
     */
    public static String getSystemProperty(String key) {
        String value = System.getenv(key);
        if (SpiStringUtils.isEmpty(value)) {
            value = System.getProperty(key);
        }
        return value;
    }

    /**
     * Load properties
     *
     * @param fileName file name
     * @return the properties
     * @since 1.0.0
     */
    public static Properties loadProperties(String fileName) {
        return loadProperties(fileName, false, false);
    }

    /**
     * Load properties
     *
     * @param fileName       file name
     * @param allowMultiFile allow multi file
     * @return the properties
     * @since 1.0.0
     */
    public static Properties loadProperties(String fileName, boolean allowMultiFile) {
        return loadProperties(fileName, allowMultiFile, false);
    }

    /**
     * Load properties
     *
     * @param fileName       file name
     * @param allowMultiFile allow multi file
     * @param optional       optional
     * @return the properties
     * @since 1.0.0
     */
    public static Properties loadProperties(String fileName, boolean allowMultiFile, boolean optional) {
        Properties properties = new Properties();
        // add scene judgement in windows environment Fix 2557
        if (checkFileNameExist(fileName)) {
            try {
                FileInputStream input = new FileInputStream(fileName);
                try {
                    properties.load(input);
                } finally {
                    input.close();
                }
            } catch (Throwable e) {
                log.warn("Failed to load " + fileName + " file from " + fileName + "(ignore this file): " + e.getMessage(), e);
            }
            return properties;
        }

        List<java.net.URL> list = new ArrayList<java.net.URL>();
        try {
            Enumeration<java.net.URL> urls = SpiClassUtils.getClassLoader().getResources(fileName);
            list = new ArrayList<java.net.URL>();
            while (urls.hasMoreElements()) {
                list.add(urls.nextElement());
            }
        } catch (Throwable t) {
            log.warn("Fail to load " + fileName + " file: " + t.getMessage(), t);
        }

        if (list.isEmpty()) {
            if (!optional) {
                log.warn("No " + fileName + " found on the class path.");
            }
            return properties;
        }

        if (!allowMultiFile) {
            if (list.size() > 1) {
                String errMsg = String.format("only 1 %s file is expected, but %d dubbo.properties files found on class path: %s",
                    fileName, list.size(), list.toString());
                log.warn(errMsg);
            }

            // fall back to use method getResourceAsStream
            try {
                properties.load(SpiClassUtils.getClassLoader().getResourceAsStream(fileName));
            } catch (Throwable e) {
                log.warn("Failed to load " + fileName + " file from " + fileName + "(ignore this file): " + e.getMessage(), e);
            }
            return properties;
        }

        log.info("load " + fileName + " properties file from " + list);

        for (java.net.URL url : list) {
            try {
                Properties p = new Properties();
                InputStream input = url.openStream();
                if (input != null) {
                    try {
                        p.load(input);
                        properties.putAll(p);
                    } finally {
                        try {
                            input.close();
                        } catch (Throwable t) {
                        }
                    }
                }
            } catch (Throwable e) {
                log.warn("Fail to load " + fileName + " file from " + url + "(ignore this file): " + e.getMessage(), e);
            }
        }

        return properties;
    }

    /**
     * Check file name exist
     *
     * @param fileName file name
     * @return the boolean
     * @since 1.0.0
     */
    private static boolean checkFileNameExist(String fileName) {
        File file = new File(fileName);
        return file.exists();
    }


    /**
     * Gets pid *
     *
     * @return the pid
     * @since 1.0.0
     */
    public static int getPid() {
        if (PID < 0) {
            try {
                RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
                String name = runtime.getName(); // format: "pid@hostname"
                PID = Integer.parseInt(name.substring(0, name.indexOf('@')));
            } catch (Throwable e) {
                PID = 0;
            }
        }
        return PID;
    }
}
