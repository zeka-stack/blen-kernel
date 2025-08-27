package dev.dong4j.zeka.kernel.common.constant;

import dev.dong4j.zeka.kernel.common.util.StringPool;
import lombok.experimental.UtilityClass;

/**
 * <p>Description: 常用的配置默认值</p>
 *
 * @author dong4j
 * @version 1.2.4
 * @email "mailto:dongshijie@gmail.com"
 * @date 2020.02.04 16:03
 * @since 1.0.0
 */
@UtilityClass
public final class ConfigDefaultValue {

    /** DEFAULT_PROPERTY_INCLUSION_VALUE */
    public static final String DEFAULT_PROPERTY_INCLUSION_VALUE = "non_null";
    /** DEFAULT_ENCRYPTOR_PASSWORD */
    public static final String DEFAULT_ENCRYPTOR_PASSWORD = "06020986-3127-40be-9134-90fd033896a1";
    /** DEFAULT_TIME_ZONE */
    public static final String TIME_ZONE = "GMT+8";
    /** Wiki */
    public static final String WIKI = "https://zeka.dong4j.dev";
    /** DEFAULT_TIME_ZONE */
    public static final String DEFAULT_TIME_ZONE = "GMT+8";
    /** DEFAULT_DATE_FORMAT */
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    /** TRUE_STRING */
    public static final String TRUE_STRING = "true";
    /** TRUE */
    public static final Boolean TRUE = Boolean.TRUE;
    /** FALSE_STRING */
    public static final String FALSE_STRING = "false";
    /** FALSE */
    public static final Boolean FALSE = Boolean.FALSE;
    /** EMPTY */
    public static final String EMPTY = StringPool.EMPTY;
    /** 应用启动成功后, 将在此路径下创建一个 app.pid 文件, 内容是当前应用的 pid */
    public static final String PROP_PID_FILE = "./app.pid";
    /** NACOS_SERVER */
    public static final String NACOS_HOST = "nacos.server";
    /** NACOS_PORT */
    public static final String NACOS_PORT = "8848";
    /** NACOS_SERVER */
    public static final String NACOS_SERVER = NACOS_HOST + StringPool.COLON + NACOS_PORT;
    /** RIBBON_NAME */
    public static final String RIBBON_NAME = "gateway";
    /** BASE_PACKAGES */
    public static final String BASE_PACKAGES = System.getProperty("PARENT_PACKAGE_NAME", "dev.dong4j.zeka");
    /** CONTAINER_LOCATION */
    public static final String CONTAINER_LOCATION = "/mnt/tmp";
    /** v8 服务默认的日志主目录 */
    public static final String DEFAULT_LOGGING_LOCATION = "/mnt/syslogs/zeka.stack";
    /** 容器日志的默认目录 */
    public static final String DEFAULE_ACCESS_LOG_DIR = "container";

}
