package dev.dong4j.zeka.kernel.devtools;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 属性配置类，用于配置代码生成器生成相关组件的配置文件
 * <p>
 * 该类定义了各种中间件和框架组件的配置开关
 * 支持Email、WebSocket、ActiveMQ、Redis、MyBatis、Dubbo等组件的配置生成
 * 同时支持Spring Boot和Spring Cloud的配置文件生成
 * <p>
 * 主要功能：
 * - 支持多种中间件的配置生成
 * - 支持Spring Boot/Cloud配置文件
 * - 支持微服务相关组件配置
 * - 提供链式调用API
 * <p>
 * 使用@Accessors(chain = true)生成链式调用方法，方便配置使用
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.27 14:09
 * @since 1.0.0
 */
@Data
@Accessors(chain = true)
public class PropertiesConfig {
    /** Email组件配置常量 */
    public static final String EMAIL = "email";
    /** WebSocket组件配置常量 */
    public static final String WEBSOCKET = "websocket";
    /** WebService组件配置常量 */
    public static final String WEBSERVICE = "webservice";
    /** ActiveMQ消息中间件配置常量 */
    public static final String ACTIVEMQ = "activemq";
    /** RocketMQ消息中间件配置常量 */
    public static final String ROCKTEMQ = "rocktemq";
    /** Redis缓存中间件配置常量 */
    public static final String REDIS = "redis";
    /** MyBatis持久层框架配置常量 */
    public static final String MYBATIS = "mybatis";
    /** Spring Boot Admin监控组件配置常量 */
    public static final String ADMIN = "admin";
    /** REST API相关配置常量 */
    public static final String REST = "rest";
    /** Dubbo微服务框架配置常量 */
    public static final String DUBBO = "dubbo";
    /** Spring Boot配置文件常量 */
    public static final String BOOT_CONFIG = "boot";
    /** Spring Cloud配置文件常量 */
    public static final String CLOUD_CONFIG = "cloud";

    /** 是否生成Email相关配置 */
    private boolean email = false;
    /** 是否生成WebSocket相关配置 */
    private boolean websocket = false;
    /** 是否生成WebService相关配置 */
    private boolean webservice = false;
    /** 是否生成ActiveMQ相关配置 */
    private boolean activemq = false;
    /** 是否生成RocketMQ相关配置 */
    private boolean rocktemq = false;
    /** 是否生成Redis相关配置 */
    private boolean redis = false;
    /** 是否生成MyBatis相关配置 */
    private boolean mybatis = false;
    /** 是否生成Spring Boot Admin相关配置 */
    private boolean admin = false;
    /** 是否生成REST API相关配置 */
    private boolean rest = false;
    /** 是否生成Dubbo相关配置 */
    private boolean dubbo = false;
    /** 是否生成Spring Boot配置文件 */
    private boolean bootConfig = false;
    /** 是否生成Spring Cloud配置文件 */
    private boolean cloudConfig = false;
}
