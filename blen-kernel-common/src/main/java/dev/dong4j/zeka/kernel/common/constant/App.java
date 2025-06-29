package dev.dong4j.zeka.kernel.common.constant;

import dev.dong4j.zeka.kernel.common.util.JustOnceLogger;
import dev.dong4j.zeka.kernel.common.util.StringPool;
import dev.dong4j.zeka.kernel.common.util.StringUtils;
import dev.dong4j.zeka.kernel.common.util.SystemUtils;
import lombok.experimental.UtilityClass;

/**
 * <p>Description: 系统常量</p>
 *
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dong4j@gmail.com"
 * @date 2019.12.26 21:40
 * @since 1.0.0
 */
@UtilityClass
public final class App {
    /** 用于判断是否为 agent service 应用 */
    public static final String IS_AGENT_SERVICE = "IS_AGENT_SERVICE";
    /** FRAMEWORK_VERSION */
    public static final String FRAMEWORK_VERSION_PREFIX = "zeka-stack-";
    /** 基础包 */
    public static final String BASE_PACKAGES = ConfigDefaultValue.BASE_PACKAGES;
    /** 启动标识, 所有项目只能使用 ZekaApplication 启动 */
    public static final String START_APPLICATION = "start_zeka_application";
    /** 依赖的 library */
    public static final String LIBRARY_NAME = "used.librarys";
    /** server.sh 中的启动参数, 用于获取配置文件路径 */
    public static final String APP_CONFIG_PATH = "config.path";
    /** 应用类型 {@link org.springframework.boot.WebApplicationType} */
    public static final String APP_TYPE = "app.type";
    /**
     * 系统自定义应用类型, 比  {@link org.springframework.boot.WebApplicationType}
     * 多了 SERVICE 类型 (dev.dong4j.zeka.starter.launcher.enums.ApplicationType)
     */
    public static final String APPLICATION_TYPE = "APPLICATION_TYPE";
    /** 启动方式, 用于区分是否通过 server.sh 脚本启动 */
    public static final String START_TYPE = "start.type";
    /** 通过 server.sh 启动的类型, 用于区分本地开发和服务端运行 */
    public static final String START_SHELL = "shell";
    /** 单元测试启动 */
    public static final String START_JUNIT = "junit";
    /** 本地开发时 idea 启动 */
    public static final String START_IDEA = "idea";
    /** rest 服务启动完成后将 url 写入到环境变量中, 用于其他组件获取此信息 */
    public static final String START_URL = "start_url";
    /** 用于打印 bean 信息 */
    public static final String DEBUG_MODEL = "debug";
    /** 以下是环境标识 */
    public static final String ENV_LOCAL = "local";
    /** ENV_DEV */
    public static final String ENV_DEV = "dev";
    /** ENV_TEST */
    public static final String ENV_TEST = "test";
    /** ENV_PREV */
    public static final String ENV_PREV = "prev";
    /** ENV_PROD */
    public static final String ENV_PROD = "prod";
    /** maven 三要素, 会从中获取应用名, 应用版本(不需要手动配置, 应用打包时会自动创建) */
    public static final String APP_POM_PROP_NAME = "pom.properties";
    /** 由 arco-assist-maven-plugin 自动生成 */
    public static final String APP_BULID_INFO_FILE_NAME = "build-info.properties";
    /** GIT_CONFIG_FILE_NAME */
    public static final String GIT_CONFIG_FILE_NAME = "git.properties";
    /** WINDOWS_DEFAULT_USER_NAME */
    private static final String WINDOWS_DEFAULT_USER_NAME = "Administrator";

    /**
     * <p>Description: </p>
     *
     * @author dong4j
     * @version 1.0.0
     * @email "mailto:dong4j@gmail.com"
     * @date 2021.01.20 01:43
     * @since 1.7.1
     */
    @UtilityClass
    public static final class BuildInfo {
        /** VERSION */
        public static final String VERSION = "build.version";
        /** TIME */
        public static final String TIME = "build.time";
        /** USER_NAME */
        public static final String USER_NAME = "build.user.name";
    }

    /**
     * <p>Description: </p>
     *
     * @author dong4j
     * @version 1.0.0
     * @email "mailto:dong4j@gmail.com"
     * @date 2021.01.20 01:50
     * @since 1.7.1
     */
    @UtilityClass
    public static final class GitInfo {
        /** VERSION */
        public static final String BRANCH = "git.branch";
        /** BUILD_VERSION */
        public static final String BUILD_VERSION = "git.build.version";
        /** 最后提交时间 */
        public static final String BUILD_TIME = "git.build.time";
        /** COMMIT_TIME */
        public static final String COMMIT_TIME = "git.commit.time";
        /** USER_NAME */
        public static final String BUILD_USER_NAME = "git.build.user.name";
        /** COMMIT_USER_NAME */
        public static final String COMMIT_USER_NAME = "git.commit.user.name";
        /** COMMIT_MESSAGE */
        public static final String COMMIT_MESSAGE = "git.commit.message.short";

    }

    /**
     * <p>Description: 组件名常量 </p>
     *
     * @author dong4j
     * @version 1.0.0
     * @email "mailto:dong4j@gmail.com"
     * @date 2021.01.16 02:32
     * @since 1.7.1
     */
    @UtilityClass
    public static class Components {
        /** REST_SPRING_BOOT */
        public static final String REST_SPRING_BOOT = "cubo-rest-spring-boot";
        /** AGENT_SPRING_BOOT */
        public static final String AGENT_SPRING_BOOT = "cubo-agent-spring-boot";
    }

    /**
     * 本地开发时, 优先从 JVM 环境变量中读取 {@link SystemUtils#USER_NAMESPACE_VALUE}, 然后是 user.name,
     * 如果 {@link SystemUtils#USER_NAME} 是 {@link App#WINDOWS_DEFAULT_USER_NAME}, 则读取系统环境变量 ZEKA_NAME_SPACE).
     * 如果通过 shell 启动, 会自动设置 ZEKA_NAME_SPACE 为传入的 env 参数, 最终找到 {env} 的 nacos namespace.
     */
    public static final String ZEKA_NAME_SPACE;

    static {
        // JVM 参数 user.namespace 优先级最高, 可通过 System.setProperty(SystemUtils.USER_NAMESPACE, "xxxx") 或 JVM 参数设置
        String currentEnv = SystemUtils.USER_NAMESPACE_VALUE;

        // 第二优先级是 ZEKA_NAME_SPACE
        if (StringUtils.isBlank(currentEnv)) {
            currentEnv = StringUtils.isBlank(SystemUtils.getProperty("ZEKA_NAME_SPACE"))
                ? StringPool.EMPTY
                : SystemUtils.getProperty("ZEKA_NAME_SPACE");
        }

        // 第三优先级是 user.name, 如果在 windows 未设置计算机名, 此名称将是 Administrator
        currentEnv = StringUtils.isBlank(currentEnv)
            ? SystemUtils.USER_NAME
            : currentEnv;

        // 如果是 Administrator 则获取 ZEKA_NAME_SPACE
        if (WINDOWS_DEFAULT_USER_NAME.equalsIgnoreCase(currentEnv)) {
            JustOnceLogger.printOnce(App.class.getName(), "请配置环境变量 「ZEKA_NAME_SPACE」 或修改本地的计算机名. "
                + "如果需要在本地开发时修改 namespace, 请设置 JVM 参数 「user.namespace」"
                + "或直接使用 System.setProperty(SystemUtils.USER_NAMESPACE, \"xxxx\"))");
            currentEnv = "public";
        }

        ZEKA_NAME_SPACE = currentEnv;
    }

}
