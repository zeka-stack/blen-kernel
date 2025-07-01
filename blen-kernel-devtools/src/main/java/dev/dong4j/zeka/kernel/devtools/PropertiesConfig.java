package dev.dong4j.zeka.kernel.devtools;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>Description: 自动生成配置文件相关配置, 使用 @Accessors(chain = true) 生成链式调用方法 </p>
 *
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.27 14:09
 * @since 1.0.0
 */
@Data
@Accessors(chain = true)
public class PropertiesConfig {
    /** EMAIL */
    public static final String EMAIL = "email";
    /** WEBSOCKET */
    public static final String WEBSOCKET = "websocket";
    /** WEBSERVICE */
    public static final String WEBSERVICE = "webservice";
    /** ACTIVEMQ */
    public static final String ACTIVEMQ = "activemq";
    /** ROCKTEMQ */
    public static final String ROCKTEMQ = "rocktemq";
    /** REDIS */
    public static final String REDIS = "redis";
    /** MYBATIS */
    public static final String MYBATIS = "mybatis";
    /** ADMIN */
    public static final String ADMIN = "admin";
    /** REST */
    public static final String REST = "rest";
    /** DUBBO */
    public static final String DUBBO = "dubbo";
    /** BOOT_CONFIG */
    public static final String BOOT_CONFIG = "boot";
    /** CLOUD_CONFIG */
    public static final String CLOUD_CONFIG = "cloud";

    /** Email */
    private boolean email = false;
    /** Websocket */
    private boolean websocket = false;
    /** Webservice */
    private boolean webservice = false;
    /** Activemq */
    private boolean activemq = false;
    /** Rocktemq */
    private boolean rocktemq = false;
    /** Redis */
    private boolean redis = false;
    /** Mybatis */
    private boolean mybatis = false;
    /** Admin */
    private boolean admin = false;
    /** Rest */
    private boolean rest = false;
    /** Dubbo */
    private boolean dubbo = false;
    /** Boot config */
    private boolean bootConfig = false;
    /** Cloud config */
    private boolean cloudConfig = false;
}
