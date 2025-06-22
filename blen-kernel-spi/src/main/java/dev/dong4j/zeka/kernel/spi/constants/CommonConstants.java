package dev.dong4j.zeka.kernel.spi.constants;

import java.util.regex.Pattern;

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.8.0
 * @email "mailto:dong4j@gmaidl.com"
 * @date 2021.02.26 17:47
 * @since 1.8.0
 */
@SuppressWarnings("java:S1214")
public interface CommonConstants {
    /** DUBBO */
    String DUBBO = "dubbo";
    /** DUBBO_PROPERTIES_KEY */
    String DUBBO_PROPERTIES_KEY = "dubbo.properties.file";
    /** DEFAULT_DUBBO_PROPERTIES */
    String DEFAULT_DUBBO_PROPERTIES = "dubbo.properties";
    /** COMMA_SEPARATOR */
    String COMMA_SEPARATOR = ",";
    /** COMMA_SPLIT_PATTERN */
    Pattern COMMA_SPLIT_PATTERN = Pattern.compile("\\s*[,]+\\s*");
    /** PROTOCOL_KEY */
    String PROTOCOL_KEY = "protocol";
    /** REMOVE_VALUE_PREFIX */
    String REMOVE_VALUE_PREFIX = "-";
    /** UNDERLINE_SEPARATOR */
    String UNDERLINE_SEPARATOR = "_";
    /** SEPARATOR_REGEX */
    String SEPARATOR_REGEX = "_|-";
    /** HIDE_KEY_PREFIX */
    String HIDE_KEY_PREFIX = ".";
    /** DOT_REGEX */
    String DOT_REGEX = "\\.";
    /** DEFAULT_KEY_PREFIX */
    String DEFAULT_KEY_PREFIX = "default.";
    /** DEFAULT_KEY */
    String DEFAULT_KEY = "default";
    /** ANYHOST_KEY */
    String ANYHOST_KEY = "anyhost";
    /** ANYHOST_VALUE */
    String ANYHOST_VALUE = "0.0.0.0";
    /** LOCALHOST_KEY */
    String LOCALHOST_KEY = "localhost";
    /** GROUP_KEY */
    String GROUP_KEY = "group";
    /** PATH_KEY */
    String PATH_KEY = "path";
    /** INTERFACE_KEY */
    String INTERFACE_KEY = "interface";
    /** VERSION_KEY */
    String VERSION_KEY = "version";
    /** USERNAME_KEY */
    String USERNAME_KEY = "username";
    /** PASSWORD_KEY */
    String PASSWORD_KEY = "password";
    /** HOST_KEY */
    String HOST_KEY = "host";
    /** PORT_KEY */
    String PORT_KEY = "port";

}
