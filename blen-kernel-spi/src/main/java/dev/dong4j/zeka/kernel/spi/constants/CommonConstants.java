package dev.dong4j.zeka.kernel.spi.constants;

import java.util.regex.Pattern;

/**
 * SPI框架通用常量定义接口，提供框架内部使用的各种常量定义
 * <p>
 * 该接口集中定义了SPI框架中使用的所有通用常量
 * 包括协议名称、分隔符、正则表达式、配置键名等
 * <p>
 * 主要分类：
 * - 协议相关常量（DUBBO、PROTOCOL_KEY等）
 * - 分隔符和正则表达式（COMMA_SEPARATOR、COMMA_SPLIT_PATTERN等）
 * - 配置键名常量（GROUP_KEY、VERSION_KEY等）
 * - 网络地址相关常量（ANYHOST_KEY、LOCALHOST_KEY等）
 * <p>
 * 注意：使用@SuppressWarnings("java:S1214")抑制接口中定义常量的警告
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2021.02.26 17:47
 * @since 1.0.0
 */
@SuppressWarnings("java:S1214")
public interface CommonConstants {
    /** Dubbo协议名称 */
    String DUBBO = "dubbo";
    /** Dubbo属性文件配置键 */
    String DUBBO_PROPERTIES_KEY = "dubbo.properties.file";
    /** 默认Dubbo属性文件名 */
    String DEFAULT_DUBBO_PROPERTIES = "dubbo.properties";
    /** 逗号分隔符 */
    String COMMA_SEPARATOR = ",";
    /** 逗号分割正则表达式 */
    Pattern COMMA_SPLIT_PATTERN = Pattern.compile("\\s*[,]+\\s*");
    /** 协议配置键 */
    String PROTOCOL_KEY = "protocol";
    /** 移除值前缀标识 */
    String REMOVE_VALUE_PREFIX = "-";
    /** 下划线分隔符 */
    String UNDERLINE_SEPARATOR = "_";
    /** 分隔符正则表达式（下划线或连字符） */
    String SEPARATOR_REGEX = "_|-";
    /** 隐藏键前缀 */
    String HIDE_KEY_PREFIX = ".";
    /** 点号正则表达式 */
    String DOT_REGEX = "\\.";
    /** 默认键前缀 */
    String DEFAULT_KEY_PREFIX = "default.";
    /** 默认键名 */
    String DEFAULT_KEY = "default";
    /** 任意主机配置键 */
    String ANYHOST_KEY = "anyhost";
    /** 任意主机地址值 */
    String ANYHOST_VALUE = "0.0.0.0";
    /** 本地主机配置键 */
    String LOCALHOST_KEY = "localhost";
    /** 分组配置键 */
    String GROUP_KEY = "group";
    /** 路径配置键 */
    String PATH_KEY = "path";
    /** 接口配置键 */
    String INTERFACE_KEY = "interface";
    /** 版本配置键 */
    String VERSION_KEY = "version";
    /** 用户名配置键 */
    String USERNAME_KEY = "username";
    /** 密码配置键 */
    String PASSWORD_KEY = "password";
    /** 主机配置键 */
    String HOST_KEY = "host";
    /** 端口配置键 */
    String PORT_KEY = "port";

}
