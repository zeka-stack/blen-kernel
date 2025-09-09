package dev.dong4j.zeka.kernel.common.constant;

import dev.dong4j.zeka.kernel.common.util.StringPool;
import lombok.experimental.UtilityClass;

/**
 * <p>Description:  所有的配置常量</p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dongshijie@gmail.com"
 * @date 2020.02.04 15:51
 * @since 1.0.0
 */
@UtilityClass
@SuppressWarnings("all")
public final class ConfigKey {
    /** 全局配置前缀 */
    public static final String PREFIX = "zeka-stack.";

    /** PARENT_PACKAGE_NAME */
    public static final String PARENT_PACKAGE_NAME = "PARENT_PACKAGE_NAME";
    /** 框架版本 */
    public static final String APPLICATION_FRAMEWORK_VERSION = PREFIX + "framework.version";
    /** EXTEND_ENABLE_AUTOWIRED_IS_NULL */
    public static final String EXTEND_ENABLE_AUTOWIRED_IS_NULL = PREFIX + "extend.enable-autowired-is-null";
    /** EXTEND_ENABLE_RESOURCE_IS_NULL */
    public static final String EXTEND_ENABLE_RESOURCE_IS_NULL = PREFIX + "extend.enable-resource-is-null";
    /** REST_ENABLE_GLOBAL_CACHE_FILTER */
    public static final String WEB_ENABLE_GLOBAL_CACHE_FILTER = PREFIX + "web.enable-global-cache-filter";
    /** REST_ENABLE_EXCEPTION_FILTER */
    public static final String WEB_ENABLE_EXCEPTION_FILTER = PREFIX + "web.enable-exception-filter";
    /** REST_ENABLE_REQUEST_LOG */
    public static final String REST_ENABLE_REQUEST_LOG = PREFIX + "rest.enable-request-log";
    /** REST_ENABLE_GLOBAL_PARAMETER_FILTER */
    public static final String REST_ENABLE_GLOBAL_PARAMETER_FILTER = PREFIX + "rest.enable-global-parameter-filter";
    /** XSS_ENABLE_XSS_FILTER */
    public static final String XSS_ENABLE_XSS_FILTER = PREFIX + "xss.enable-xss-filter";
    /** JSON_TIME_ZONE */
    public static final String JSON_TIME_ZONE = PREFIX + "rest.json.time-zone";
    /** JSON_DATE_FORMAT */
    public static final String JSON_DATE_FORMAT = PREFIX + "rest.json.date-formate";
    /** jackson null 处理配置 */
    public static final String JSON_SERIALIZE_NULL = PREFIX + "rest.json.default-property-inclusion";
    /** MYBATIS_ENABLE_ILLEGAL_SQL_INTERCEPTOR */
    public static final String MYBATIS_ENABLE_ILLEGAL_SQL_INTERCEPTOR = PREFIX + "mybatis.enable-illegal-sql-interceptor";
    /** MYBATIS_ENABLE_SQL_EXPLAIN_INTERCEPTOR */
    public static final String MYBATIS_ENABLE_SQL_EXPLAIN_INTERCEPTOR = PREFIX + "mybatis.enable-sql-explain-interceptor";
    /** POM_INFO_VERSION */
    public static final String POM_INFO_VERSION = "info.version";
    /** POM_INFO_GROUPID */
    public static final String POM_INFO_GROUPID = "info.groupId";
    /** POM_INFO_ARTIFACTID */
    public static final String POM_INFO_ARTIFACTID = "info.artifactId";
    /** SERVICE_VERSION */
    public static final String SERVICE_VERSION = PREFIX + "service.version";
    /** JASYPT_ENCRYPTOR_PASSWORD */
    public static final String JASYPT_ENCRYPTOR_PASSWORD = "jasypt.encryptor.password";
    /** 是否开启 banner */
    public static final String ENABLE_BANNER = PREFIX + "enable-banner";
    /** 应用分组 */
    public static final String APP_GROUP = PREFIX + "app.group";
    /** APP_CONFIG_GROUP */
    public static final String APP_CONFIG_GROUP = PREFIX + "app.config-group";
    /** DISCOVERY_GROUP */
    public static final String APP_DISCOVERY_GROUP = PREFIX + "app.discovery-group";
    /** Wiki 链接 */
    public static final String WIKI = PREFIX + "app.wiki";

    /**
     * <p>JVM 配置常量类.
     * <p>定义了 JVM 相关的配置参数常量，用于获取和设置 JVM 运行时参数.
     *
     * @author dong4j
     * @version 1.0.0
     * @email "mailto:dong4j@gmail.com"
     * @date 2020.02.26 17:13
     * @since 1.0.0
     */
    @UtilityClass
    public static class JvmConfigKey {
        /** TMP_DIR */
        public static final String TMP_DIR = "java.io.tmpdir";

    }

    /**
     * <p>日志系统配置常量类.
     * <p>定义了日志系统相关的所有配置参数常量，包括日志级别、文件配置、输出格式等.
     *
     * @author dong4j
     * @version 1.0.0
     * @email "mailto:dong4j@gmail.com"
     * @date 2020.02.26 10:01
     * @since 1.0.0
     */
    @UtilityClass
    public static class LogSystemConfigKey {
        /** 日志应用名 */
        public static final String LOG_APP_NAME = PREFIX + "logging.app-name";
        /** 是否现在 location 信息 */
        public static final String SHOW_LOG_LOCATION = PREFIX + "logging.enable-show-location";

        /** 日志等级 */
        public static final String LOG_LEVEL = PREFIX + "logging.level";
        /** 日志分组 */
        public static final String LOG_GROUP = PREFIX + "logging.group";
        /** 日志系统配置文件 */
        public static final String LOG_CONFIG = PREFIX + "logging.config";

        /** LOG_FILE */
        public static final String LOG_FILE = PREFIX + "logging.file";
        /** 日志文件名 */
        public static final String LOG_FILE_NAME = PREFIX + "logging.file.name";
        /** 日志文件保存路径 */
        public static final String LOG_FILE_PATH = PREFIX + "logging.file.path";
        /** 是否在启动应用时清理历史日志 */
        public static final String LOG_FILE_CLEAN_HISTORY = PREFIX + "logging.file.clean-history-on-start";
        /** 最大允许保存的日志历史 */
        public static final String LOG_FILE_MAX_HISTORY = PREFIX + "logging.file.max-history";
        /** 日志文件最大容量 */
        public static final String LOG_FILE_MAX_SIZE = PREFIX + "logging.file.max-size";
        /** 日志文件最大数量 */
        public static final String LOG_FILE_TOTAL_SIZE_CAP = PREFIX + "logging.file.total-size-cap";

        /** LOG_PATTERN */
        public static final String LOG_PATTERN = PREFIX + "logging.pattern";
        /** LOG_PATTERN_CONSOLE */
        public static final String LOG_PATTERN_CONSOLE = PREFIX + "logging.pattern.console";
        /** LOG_PATTERN_FILE */
        public static final String LOG_PATTERN_FILE = PREFIX + "logging.pattern.file";
        /** LOG_PATTERN_LEVEL */
        public static final String LOG_PATTERN_LEVEL = PREFIX + "logging.pattern.level";
        /** LOG_PATTERN_DATEFORMAT */
        public static final String LOG_PATTERN_DATEFORMAT = PREFIX + "logging.pattern.dateformat";
        /** ROLLING_FILE_NAME */
        public static final String ROLLING_FILE_NAME = PREFIX + "logging.pattern.rolling-file-name";
        /** MARKER_PATTERN */
        public static final String MARKER_PATTERN = PREFIX + "logging.pattern.marker";

        /** 日志路由 key */
        public static final String ROUTING_APPENDER_KEY = "ROUTING_APPENDER_KEY";

    }

    /**
     * <p>Description: </p>
     *
     * @author dong4j
     * @version 1.0.0
     * @email "mailto:dong4j@gmail.com"
     * @date 2020.02.04 17:05
     * @since 1.0.0
     */
    @UtilityClass
    public static class SpringConfigKey {
        /** SERVER_CONTEXT_PATH */
        public static final String SERVER_CONTEXT_PATH = "server.servlet.context-path";
        /** SERVER_PORT */
        public static final String SERVER_PORT = "server.port";
        /** MANAGEMENT_SERVER_PORT */
        public static final String MANAGEMENT_SERVER_PORT = "management.server.port";
        /** PROP_APPLICATION_NAME */
        public static final String APPLICATION_NAME = "spring.application.name";
        /** PACKAGE_NAME */
        public static final String PACKAGE_NAME = "package.name";
        /** PROFILE_ACTIVE */
        public static final String PROFILE_ACTIVE = "spring.profiles.active";
        /** MAIN_ALLOW_BEAN_DEFINITION_OVERRIDING */
        public static final String MAIN_ALLOW_BEAN_DEFINITION_OVERRIDING = "spring.main.allow-bean-definition-overriding";
        /** 根据此配置判断是否创建 pid 文件 */
        public static final String PID_FILE = "spring.pid.file";
        /** 排除的自动装配类 */
        public static final String AUTOCONFIGURE_EXCLUDE = "spring.autoconfigure.exclude";
        /** jackson 序列化配置 */
        public static final String JACKSON_DEFAULT_PROPERTY_INCLUSION = "spring.jackson.default-property-inclusion";
        /** DATASOURCE_URL */
        public static final String DATASOURCE_URL = "spring.datasource.url";
    }

    /**
     * <p>Description: </p>
     *
     * @author dong4j
     * @version 1.0.0
     * @email "mailto:dong4j@gmail.com"
     * @date 2020.02.04 18:07
     * @since 1.0.0
     */
    @UtilityClass
    public static class MvcConfigKey {
        /** NO_HANDLER_FOUND */
        public static final String NO_HANDLER_FOUND = "spring.mvc.throw-exception-if-no-handler-found";
        /** ENCODING_FORCE */
        public static final String ENCODING_FORCE = "spring.http.encoding.force";
        /** ENCODING_CHARSET */
        public static final String ENCODING_CHARSET = "spring.http.encoding.charset";
        /** ENCODING_ENABLED */
        public static final String ENCODING_ENABLED = "spring.http.encoding.enabled";
    }

    /**
     * <p>Description: Undertow 请求日志配置 </p>
     *
     * @author dong4j
     * @version 1.0.0
     * @email "mailto:dong4j@gmail.com"
     * @date 2020.03.10 12:46
     * @since 1.0.0
     */
    @UtilityClass
    public static class UndertowConfigKye {
        /** 日志存放目录 */
        public static final String ACCESSLOG_DIR = "server.undertow.accesslog.dir";
        /** 是否启动日志 */
        public static final String ENABLE_ACCESSLOG = "server.undertow.accesslog.enabled";
        /** 日志文件名前缀 */
        public static final String ACCESSLOG_PREFIX = "server.undertow.accesslog.prefix";
        /** 日志文件名后缀 */
        public static final String ACCESSLOG_SUFFIX = "server.undertow.accesslog.suffix";
        /** 日志格式 */
        public static final String ACCESSLOG_PATTERN = "server.undertow.accesslog.pattern";
    }

    /**
     * <p>Description: </p>
     *
     * @author dong4j
     * @version 1.0.0
     * @email "mailto:dong4j@gmail.com"
     * @date 2020.02.04 17:31
     * @since 1.0.0
     */
    @UtilityClass
    public static class CloudConfigKey {
        /** 是否加载配置中心配置 */
        public static final String CONFIG_OVERRIDE_NONE = "spring.cloud.config.override-none";
        /** CONFIG */
        public static final String NACOS_CONFIG = "spring.cloud.nacos.config.";
        /** nacos config 的配置 key */
        public static final String CONFIG_SERVER_ADDRESS = NACOS_CONFIG + "server-addr";
        /** CONFIG_FILE_EXTENSION */
        public static final String CONFIG_FILE_EXTENSION = NACOS_CONFIG + "file-extension";
        /** CONFIG_ENCODE */
        public static final String CONFIG_ENCODE = NACOS_CONFIG + "encode";
        /** nacos readtimeout */
        public static final String TIMEOUT = NACOS_CONFIG + "timeout";
        /** CONFIG_NAMESPACE */
        public static final String CONFIG_NAMESPACE = NACOS_CONFIG + "namespace";
        /** CONFIG_GROUP */
        public static final String CONFIG_GROUP = NACOS_CONFIG + "group";
        /** EXT_CONFIG_1_DATA_ID */
        public static final String EXT_CONFIG_1_DATA_ID = NACOS_CONFIG + "ext-config.data-id";
        /** EXT_CONFIG_1_GROUP */
        public static final String EXT_CONFIG_1_GROUP = NACOS_CONFIG + "ext-config.group";
        /** EXT_CONFIG_1_NAMESPACE */
        public static final String EXT_CONFIG_1_NAMESPACE = NACOS_CONFIG + "ext-config.namespace";
        /** EXT_CONFIG_1_REFRESH */
        public static final String EXT_CONFIG_1_REFRESH = NACOS_CONFIG + "ext-config.refresh";
        /** DISCOVERY_NAMESPACE */
        public static final String DISCOVERY = "spring.cloud.nacos.discovery.";
        /** DISCOVERY_NAMESPACE */
        public static final String DISCOVERY_NAMESPACE = DISCOVERY + "namespace";
        /** DISCOVERY_SERVER_ADDRESS */
        public static final String DISCOVERY_SERVER_ADDRESS = DISCOVERY + "server-addr";
        /** DISCOVERY_GROUP */
        public static final String DISCOVERY_GROUP = DISCOVERY + "group";
        /** DISCOVERY_IP */
        public static final String DISCOVERY_IP = DISCOVERY + "ip";
        /** DISCOVERY_METADATA */
        public static final String DISCOVERY_METADATA = DISCOVERY + "metadata";
        /** INETUTILS_PREFERRED_NETWORKS */
        public static final String INETUTILS_PREFERRED_NETWORKS = "spring.cloud.inetutils.preferred-networks";
        /** USE_ONLY_SITE_LOCAL_INTERFACES */
        public static final String USE_ONLY_SITE_LOCAL_INTERFACES = "spring.cloud.inetutils.use-only-site-local-interfaces";
    }

    /**
     * <p>Description: 自定义 nacos 配置, 用于访问自定义配置和服务 </p>
     *
     * @author dong4j
     * @version 1.0.0
     * @email "mailto:dong4j@gmail.com"
     * @date 2020.03.20 17:39
     * @since 1.0.0
     */
    @UtilityClass
    public static class NacosConfigKey {
        /** 配置中心地址 */
        public static final String CONFIG = PREFIX + "nacos.config.";
        /** CONFIG_SERVER_ADDRESS */
        public static final String CONFIG_SERVER_ADDRESS = CONFIG + "server-addr";
        /** 配置中心 namespace */
        public static final String CONFIG_NAMESPACE = CONFIG + "namespace";
        /** 配置中心 dataId */
        public static final String CONFIG_DATA_ID = CONFIG + "data-id";
        /** 配置中心 分组 */
        public static final String CONFIG_GROUP = CONFIG + "group";
        /** DISCOVERY */
        public static final String DISCOVERY = PREFIX + "nacos.discovery.";
        /** 服务注册与发现地址 */
        public static final String DISCOVERY_SERVER_ADDR = DISCOVERY + "server-addr";
        /** 服务注册与发现 namespace */
        public static final String DISCOVERY_NAMESPACE = DISCOVERY + "namespace";
        /** 服务注册与发现 分组 */
        public static final String DISCOVERY_GROUP = DISCOVERY + "group";
        /** 是否自动创建配置 */
        public static final String ENABLE_AUTO_CREATE_CONFIG = PREFIX + "nacos.enable-auto-create-config";
        /** 是否使用 Nacos 配置 */
        public static final String ENABLE_NACOS_CONFIG = PREFIX + "nacos.enable-nacos-config";

    }

    /**
     * <p>Description: </p>
     *
     * @author dong4j
     * @version 1.0.0
     * @email "mailto:dong4j@gmail.com"
     * @date 2020.02.04 17:42
     * @since 1.0.0
     */
    @UtilityClass
    public static class ManagementConfigKey {
        /** ENABLED */
        public static final String ENABLED = "management.endpoint.env.enabled";
        /** HEALTH_DETAILS */
        public static final String HEALTH_DETAILS = "management.endpoint.health.show-details";
        /** GIT_MODE */
        public static final String GIT_MODE = "management.info.git.mode";
        /** EXPOSURE_INCLUDE */
        public static final String EXPOSURE_INCLUDE = "management.endpoints.web.exposure.include";
        /** BASE_URL */
        public static final String BASE_URL = "management.endpoints.web.base-path";

    }

    /**
     * <p>Description: </p>
     *
     * @author dong4j
     * @version 1.0.0
     * @email "mailto:dong4j@gmail.com"
     * @date 2020.02.04 17:50
     * @since 1.0.0
     */
    @UtilityClass
    public static class DubboConfigKey {
        /** APPLICATION_LOGGER */
        public static final String APPLICATION_LOGGER = "dubbo.application.logger";
        /** DUBBO_HOST */
        public static final String DUBBO_HOST = "dubbo.protocol.host";
        /** REGISTRY_ADDRESS */
        public static final String REGISTRY_ADDRESS = "dubbo.registry.address";
        /** PROTOCOL_NAME */
        public static final String PROTOCOL_NAME = "dubbo.protocol.name";
        /** CONSUMER_CHECK */
        public static final String CONSUMER_CHECK = "dubbo.consumer.check";
        /** CONSUMER_VALIDATION */
        public static final String CONSUMER_VALIDATION = "dubbo.consumer.validation";
        /** PROVIDER_TIMEOUT */
        public static final String PROVIDER_TIMEOUT = "dubbo.provider.timeout";
        /** PROTOCOL_PORT */
        public static final String PROTOCOL_PORT = "dubbo.protocol.port";
        /** 元数据中心地址 */
        public static final String METADATA_REPORT_ADDRESS = "dubbo.metadata-report.address";
        /** DUBBO_CONSUMER_FILTER */
        public static final String DUBBO_CONSUMER_FILTER = "dubbo.consumer.filter";
        /** DUBBO_PROVIDER_FILTER */
        public static final String DUBBO_PROVIDER_FILTER = "dubbo.provider.filter";
    }

    /**
     * <p>Description: </p>
     *
     * @author dong4j
     * @version 1.0.0
     * @email "mailto:dong4j@gmail.com"
     * @date 2020.02.04 17:54
     * @since 1.0.0
     */
    @UtilityClass
    public static class MongoConfigKey {
        /** ENABLE_AUTO_INCREMENT_KEY */
        public static final String ENABLE_AUTO_INCREMENT_KEY = PREFIX + "mongo.enable-auto-increment-key";
        /** ENABLE_AUTO_CREATE_INDEX */
        public static final String ENABLE_AUTO_CREATE_INDEX = PREFIX + "mongo.enable-auto-create-index";
        /** ENABLE_AUTO_CREATE_KEY */
        public static final String ENABLE_AUTO_CREATE_KEY = PREFIX + "mongo.enable-auto-create-key";
        /** ENABLE_AUTO_CREATE_TIME */
        public static final String ENABLE_AUTO_CREATE_TIME = PREFIX + "mongo.enable-auto-create-time";
    }

    /**
     * <p>Description: </p>
     *
     * @author dong4j
     * @version 1.0.0
     * @email "mailto:dong4j@gmail.com"
     * @date 2020.02.04 18:10
     * @since 1.0.0
     */
    @UtilityClass
    public static class MybatisConfigKey {
        /** PAGE */
        public static final String PAGE = PREFIX + "mybatis.page";
        /** LIMIT */
        public static final String LIMIT = PREFIX + "mybatis.limit";
        /** MYBATIS_ENABLE_LOG */
        public static final String MYBATIS_ENABLE_LOG = PREFIX + "mybatis.enable-log";
        /** 附加SQL文件 */
        public static final String APPEND_SQL_FILE = PREFIX + "mybatis.append-sql-file";
        /** MYBATIS_ENABLE_SENSITIVE */
        public static final String MYBATIS_ENABLE_SENSITIVE = PREFIX + "mybatis.enable-sensitive";
        /** MAPPER_LOCATIONS */
        public static final String MAPPER_LOCATIONS = "mybatis-plus.mapper-locations";
        /** CONFIGURATION_LOG_IMPL */
        public static final String CONFIGURATION_LOG_IMPL = "mybatis-plus.configuration.log-impl";
        /** CONFIGURATION_CALL_SETTERS_ON_NULLS */
        public static final String CONFIGURATION_CALL_SETTERS_ON_NULLS = "mybatis-plus.configuration.call-setters-on-nulls";
        /** CONFIGURATION_CACHE_ENABLED */
        public static final String CONFIGURATION_CACHE_ENABLED = "mybatis-plus.configuration.cache-enabled";
        /** CONFIGURATION_MAP_UNDERSCORE_TO_CAMEL_CASE */
        public static final String CONFIGURATION_MAP_UNDERSCORE_TO_CAMEL_CASE = "mybatis-plus.configuration.map-underscore-to-camel-case";
        /** GLOBAL_LOGIC_DELETE_VALUE */
        public static final String GLOBAL_LOGIC_DELETE_VALUE = "mybatis-plus.global-config.db-config.logic-delete-value";
        /** GLOBAL_LOGIC_NOT_DELETE_VALUE */
        public static final String GLOBAL_LOGIC_NOT_DELETE_VALUE = "mybatis-plus.global-config.db-config.logic-not-delete-value";
        /** 组件类型 */
        public static final String GLOBAL_ID_TYPE = "mybatis-plus.global-config.db-config.id-type";
        /** GLOBAL_LOGIC_BANNER */
        public static final String GLOBAL_LOGIC_BANNER = "mybatis-plus.global-config.banner";

    }

    /**
     * <p>Description: </p>
     * todo-dong4j : (2020年02月28日 12:38) [重构]
     *
     * @author dong4j
     * @version 1.0.0
     * @email "mailto:dong4j@gmail.com"
     * @date 2020.02.04 18:13
     * @since 1.0.0
     */
    @UtilityClass
    public static class DruidConfigKey {
        /** TYPE */
        public static final String TYPE = "spring.datasource.type";
        /** DB_TYPE */
        public static final String DB_TYPE = "spring.datasource.druid.db-type";
        /** DRIVER_CLASS */
        public static final String DRIVER_CLASS = "spring.datasource.driver-class-name";
        /** INITIALSIZE */
        public static final String INITIALSIZE = "spring.datasource.druid.initialSize";
        /** MINIDLE */
        public static final String MINIDLE = "spring.datasource.druid.minIdle";
        /** MAXACTIVE */
        public static final String MAXACTIVE = "spring.datasource.druid.maxActive";
        /** MAXWAIT */
        public static final String MAXWAIT = "spring.datasource.druid.maxWait";
        /** TIMEBETWEENEVICTIONRUNSMILLIS */
        public static final String TIMEBETWEENEVICTIONRUNSMILLIS = "spring.datasource.druid.timeBetweenEvictionRunsMillis";
        /** MINEVICTABLEIDLETIMEMILLIS */
        public static final String MINEVICTABLEIDLETIMEMILLIS = "spring.datasource.druid.minEvictableIdleTimeMillis";
        /** VALIDATIONQUERY */
        public static final String VALIDATIONQUERY = "spring.datasource.druid.validationQuery";
        /** TESTWHILEIDLE */
        public static final String TESTWHILEIDLE = "spring.datasource.druid.testWhileIdle";
        /** TESTONBORROW */
        public static final String TESTONBORROW = "spring.datasource.druid.testOnBorrow";
        /** TESTONRETURN */
        public static final String TESTONRETURN = "spring.datasource.druid.testOnReturn";
        /** POOLPREPAREDSTATEMENTS */
        public static final String POOLPREPAREDSTATEMENTS = "spring.datasource.druid.poolPreparedStatements";
        /** MAXPOOLPREPAREDSTATEMENTPERCONNECTIONSIZE */
        public static final String MAXPOOLPREPAREDSTATEMENTPERCONNECTIONSIZE =
            "spring.datasource.druid.maxPoolPreparedStatementPerConnectionSize";
        /** FILTERS */
        public static final String FILTERS = "spring.datasource.druid.filters";
        /** CONNECTIONPROPERTIES */
        public static final String CONNECTIONPROPERTIES = "spring.datasource.druid.connectionProperties";
        /** USUEGLOBALDATASOURCESTAT */
        public static final String USUEGLOBALDATASOURCESTAT = "spring.datasource.druid.useGlobalDataSourceStat";
        /** WEB_FILTER */
        public static final String WEB_FILTER = "spring.datasource.druid.web-stat-filter.enabled";
        /** WEB_FILTER_URL_PATTERN */
        public static final String WEB_FILTER_URL_PATTERN = "spring.datasource.druid.web-stat-filter.url-pattern";
        /** WEB_FILTER_EXCLUSIONS */
        public static final String WEB_FILTER_EXCLUSIONS = "spring.datasource.druid.web-stat-filter.exclusions";
        /** STAT */
        public static final String STAT = "spring.datasource.druid.stat-view-servlet.enabled";
        /** STAT_URL_PATTERN */
        public static final String STAT_URL_PATTERN = "spring.datasource.druid.stat-view-servlet.url-pattern";
        /** STAT_ALLOW */
        public static final String STAT_ALLOW = "spring.datasource.druid.stat-view-servlet.allow";
        /** STAT_DENY */
        public static final String STAT_DENY = "spring.datasource.druid.stat-view-servlet.deny";
        /** STAT_RESET */
        public static final String STAT_RESET = "spring.datasource.druid.stat-view-servlet.reset-enable";
        /** STAT_USERNAME */
        public static final String STAT_USERNAME = "spring.datasource.druid.stat-view-servlet.login-username";
        /** STAT_PASSWORD */
        public static final String STAT_PASSWORD = "spring.datasource.druid.stat-view-servlet.login-password";
    }

    /**
     * <p>Description: </p>
     *
     * @author dong4j
     * @version 1.0.0
     * @email "mailto:dong4j@gmail.com"
     * @date 2020.02.04 18:59
     * @since 1.0.0
     */
    @UtilityClass
    public static class FeignConfigKey {
        /** FEIGN_URL_PROFIX */
        public static final String FEIGN_URL_PROFIX = PREFIX + "feign.url.";
        /** RIBBON_SERVICE_LIST */
        public static final String RIBBON_SERVICE_LIST = PREFIX + "feign.ribbon.list-of-servers";
        /** MAX_AUTO_RETRIES */
        public static final String MAX_AUTO_RETRIES = PREFIX + "feign.ribbon.max-auto-retries";
        /** MAX_AUTO_RETRIES_NEXT_SERVER */
        public static final String MAX_AUTO_RETRIES_NEXT_SERVER = PREFIX + "feign.ribbon.max-auto-retries-next-server";
        /** OK_TO_RETRY_ON_ALL_OPERATIONS */
        public static final String OK_TO_RETRY_ON_ALL_OPERATIONS = PREFIX + "feign.ribbon.ok-to-retry-on-all-operations";
        /** SERVER_LIST_REFRESH_INTERVAL */
        public static final String SERVER_LIST_REFRESH_INTERVAL = PREFIX + "feign.ribbon.server-list-refresh-interval";
        /** CONNECT_TIMEOUT */
        public static final String CONNECT_TIMEOUT = PREFIX + "feign.ribbon.connect-timeout";
        /** READ_TIMEOUT */
        public static final String READ_TIMEOUT = PREFIX + "feign.ribbon.read-timeout";
    }

    /**
     * <p>Description: </p>
     *
     * @author dong4j
     * @version 1.0.0
     * @email "mailto:dong4j@gmail.com"
     * @date 2020.02.07 16:30
     * @since 1.0.0
     */
    @UtilityClass
    public static class AgentConfigKey {
        /** agent 路由服务后缀, 服务端需要依赖 cubo-agent-spring-boot-starter */
        public static final String AGENT_SUFFIX = "/agent";
        /** 网关地址前缀, 发起请求时将使用 zeka-stack.feign.ribbon.list-of-servers 配置的 ip 地址替换此字符串 */
        public static final String GATEWAY_PREFIX = "http://" + RibbonConfigKey.CLIENT_NAME + StringPool.SLASH;
        /** FEIGN_URL_PROFIX */
        public static final String FEIGN_URL_PROFIX = PREFIX + "agent.url.";

        /**
         * RIBBON_SERVICE_LIST
         *
         * @deprecated 使用 {@link AgentConfigKey#GATEWAY_SERVICE_LIST} 替换
         */
        @Deprecated
        public static final String RIBBON_SERVICE_LIST = PREFIX + "agent.ribbon.list-of-servers";
        /** GATEWAY_SERVICE_LIST */
        public static final String GATEWAY_SERVICE_LIST = PREFIX + "gateway.servers";
        /**
         * SERVER_LIST_REFRESH_INTERVAL
         *
         * @deprecated 使用 {@link AgentConfigKey#GATEWAY_SERVER_LIST_REFRESH_INTERVAL} 替换
         */
        @Deprecated
        public static final String SERVER_LIST_REFRESH_INTERVAL = PREFIX + "agent.ribbon.server-list-refresh-interval";
        /** GATEWAY_SERVER_LIST_REFRESH_INTERVAL */
        public static final String GATEWAY_SERVER_LIST_REFRESH_INTERVAL = PREFIX + "gateway.server-list-refresh-interval";
        /**
         * CONNECT_TIMEOUT
         *
         * @deprecated 使用 {@link AgentConfigKey#GATEWAY_CONNECT_TIMEOUT} 替换
         */
        @Deprecated
        public static final String CONNECT_TIMEOUT = PREFIX + "agent.ribbon.connect-timeout";
        /** GATEWAY_CONNECT_TIMEOUT */
        public static final String GATEWAY_CONNECT_TIMEOUT = PREFIX + "gateway.connect-timeout";
        /**
         * READ_TIMEOUT
         *
         * @deprecated 使用 {@link AgentConfigKey#GATEWAY_READ_TIMEOUT} 替换
         */
        @Deprecated
        public static final String READ_TIMEOUT = PREFIX + "agent.ribbon.read-timeout";
        /** GATEWAY_READ_TIMEOUT */
        public static final String GATEWAY_READ_TIMEOUT = PREFIX + "gateway.read-timeout";
        /**
         * 是否开启路由转发
         *
         * @deprecated 使用 {@link AgentConfigKey#GATEWAY_REST_ENABLE_ROUTER} 替换
         */
        @Deprecated
        public static final String REST_ENABLE_ROUTER = PREFIX + "agent.rest.enable-router";
        /** GATEWAY_REST_ENABLE_ROUTER */
        public static final String GATEWAY_REST_ENABLE_ROUTER = PREFIX + "gateway.enable-router";
        /** GATEWAY_REST_ENABLE_endpoint */
        public static final String GATEWAY_REST_ENABLE_ENDPOINT = PREFIX + "gateway.enable-endpoint";
        /**
         * request 请求最大长度限制
         *
         * @deprecated 使用 {@link AgentConfigKey#GATEWAY_REQUEST_MAX_LINE_LENGTH} 替换
         */
        @Deprecated
        public static final String REQUEST_MAX_LINE_LENGTH = PREFIX + "agent.rest.request-max-line-length";
        /** GATEWAY_REQUEST_MAX_LINE_LENGTH */
        public static final String GATEWAY_REQUEST_MAX_LINE_LENGTH = PREFIX + "gateway.request-max-line-length";


        /** 全局重复提交检查 (如果为 false, 则不再检查每个 agent 服务是否需要检查) */
        public static final String ENABLE_REPLY_CHECK = PREFIX + "agent.endpoint.enable-reply-check";
        /** 全局签名检查 (如果为 false, 则不再检查每个 agent 服务是否需要检查) */
        public static final String ENABLE_SIGN_CHECK = PREFIX + "agent.endpoint.enable-sign-check";
        /** 启动时检查 agent service 写法是否正确 */
        public static final String ENABLE_FAIL_FAST = PREFIX + "agent.endpoint.enable-fail-fast";
    }

    /**
     * <p>Description: </p>
     *
     * @author dong4j
     * @version 1.0.0
     * @email "mailto:dong4j@gmail.com"
     * @date 2020.02.07 16:39
     * @since 1.0.0
     */
    @UtilityClass
    public static class RibbonConfigKey {
        /** ribbon 的负载 url 必须以 http://gateway 开始, 可通过 JVM 参数替换 */
        public static final String CLIENT_NAME = System.getProperty("ribbon.prefix", ConfigDefaultValue.RIBBON_NAME);
        /** MAX_AUTO_RETRIES */
        public static final String MAX_AUTO_RETRIES = CLIENT_NAME + ".ribbon.MaxAutoRetries";
        /** MAX_AUTO_RETRIES_NEXT_SERVER */
        public static final String MAX_AUTO_RETRIES_NEXT_SERVER = CLIENT_NAME + ".ribbon.MaxAutoRetriesNextServer";
        /** OK_TO_RETRY_ON_ALL_OPERATIONS */
        public static final String OK_TO_RETRY_ON_ALL_OPERATIONS = CLIENT_NAME + ".ribbon.OkToRetryOnAllOperations";
        /** SERVER_LIST_REFRESH_INTERVAL */
        public static final String SERVER_LIST_REFRESH_INTERVAL = CLIENT_NAME + ".ribbon.ServerListRefreshInterval";
        /** CONNECT_TIMEOUT */
        public static final String CONNECT_TIMEOUT = CLIENT_NAME + ".ribbon.ConnectTimeout";
        /** READ_TIMEOUT */
        public static final String READ_TIMEOUT = CLIENT_NAME + ".ribbon.ReadTimeout";
        /** LIST_OF_SERVERS */
        public static final String LIST_OF_SERVERS = CLIENT_NAME + ".ribbon.listOfServers";
    }

    /**
     * <p>Description: </p>
     *
     * @author dong4j
     * @version 1.0.0
     * @email "mailto:dong4j@gmail.com"
     * @date 2020.04.03 11:40
     * @since 1.0.0
     */
    @UtilityClass
    public static class ScheduleConfigKey {
        /** ENABLE */
        public static final String ENABLE = PREFIX + "schedule.enable";
        /** EXECUTOR_APP_NAME */
        public static final String EXECUTOR_APP_NAME = PREFIX + "schedule.executor.app-name";
        /** EXECUTOR_APP_IP */
        public static final String EXECUTOR_APP_IP = PREFIX + "schedule.executor.id";
        /** EXECUTOR_APP_PORT */
        public static final String EXECUTOR_APP_PORT = PREFIX + "schedule.executor.port";
        /** EXECUTOR_APP_LOG_PATH */
        public static final String EXECUTOR_APP_LOG_PATH = PREFIX + "schedule.executor.log-path";
        /** EXECUTOR_APP_LOG_RETENTIONDAYS */
        public static final String EXECUTOR_APP_LOG_RETENTIONDAYS = PREFIX + "schedule.executor.log-retention-days";
        /** SCHEDULE_ACCESSTOKEN */
        public static final String SCHEDULE_ACCESSTOKEN = PREFIX + "schedule.access-token";
        /** SCHEDULE_ADMIN_ADDRESSES */
        public static final String SCHEDULE_ADMIN_ADDRESSES = PREFIX + "schedule.admin-addresses";
    }

    /**
     * <p>Description: </p>
     *
     * @author dong4j
     * @version 1.0.0
     * @email "mailto:dong4j@gmail.com"
     * @date 2020.12.08 12:08
     * @since 1.0.0
     */
    @UtilityClass
    public static class MqConfigKey {

        /** BOOTSTRAP_SERVERS */
        public static final String BOOTSTRAP_SERVERS = PREFIX + "mq.kafka.bootstrap-servers";
        /** NAME_SERVER_ADDRESS */
        public static final String NAME_SERVER_ADDRESS = PREFIX + "mq.rocketmq.name-server-address";
    }

    /**
     * <p>Description: </p>
     *
     * @author dong4j
     * @version 1.0.0
     * @email "mailto:dong4j@gmail.com"
     * @date 2022.01.21 11:38
     * @since 1.0.0
     */
    public static class LogcatConfigKey {
        /** ADMIN_URL */
        public static final String ADMIN_URL = PREFIX + "logcat.server.url";
    }
}
