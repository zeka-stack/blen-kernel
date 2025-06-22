package dev.dong4j.zeka.kernel.common.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

/**
 * <p>Description: 系统工具类</p>
 *
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.27 18:24
 * @since 1.0.0
 */
@Slf4j
@UtilityClass
public class SystemUtils extends org.apache.commons.lang3.SystemUtils {
    /** NAMESPACE */
    public static final String USER_NAMESPACE = "user.namespace";
    /** 获取 user home */
    @Nullable
    public static final String USER_HOME = getProperty("user.home");
    /** 获取用户地址 */
    @Nullable
    public static final String USER_DIR = getProperty("user.dir");
    /** 获取用户名 */
    @Nullable
    public static final String USER_NAME = getProperty("user.name");
    /** os 名 */
    @Nullable
    public static final String OS_NAME = getProperty("os.name");
    /** 在启动参数中直接修改 namespace, 优先级最高 */
    @Nullable
    public static final String USER_NAMESPACE_VALUE = getProperty(USER_NAMESPACE);

    /**
     * 优先获取 JVM 参数, 然后才是系统环境变量
     *
     * @param property property
     * @return the system property
     * @since 1.0.0
     */
    @Nullable
    public static String getProperty(String property) {
        String value = System.getProperty(property);
        return StringUtils.isBlank(value) ? System.getenv(property) : value;
    }

    /**
     * Is linux boolean
     *
     * @return boolean boolean
     * @since 1.0.0
     */
    public static boolean isLinux() {
        return org.apache.commons.lang3.SystemUtils.IS_OS_LINUX;
    }

    /**
     * Is mac boolean
     *
     * @return the boolean
     * @since 1.0.0
     */
    @Contract(pure = true)
    public static boolean isMac() {
        return org.apache.commons.lang3.SystemUtils.IS_OS_MAC;
    }

    /**
     * Is windows boolean
     *
     * @return the boolean
     * @since 1.0.0
     */
    @Contract(pure = true)
    public static boolean isWindows() {
        return org.apache.commons.lang3.SystemUtils.IS_OS_WINDOWS;
    }

    /**
     * <p>Description: </p>
     *
     * @author dong4j
     * @version 1.2.5
     * @email "mailto:dong4j@gmail.com"
     * @date 2020.02.28 19:28
     * @since 1.0.0
     */
    public enum OsType {
        /** Linux os type */
        LINUX,
        /** Mac os type */
        MAC,
        /** Windows os type */
        WINDOWS
    }
}
