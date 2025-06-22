package dev.dong4j.zeka.kernel.common.support;

import org.jetbrains.annotations.Contract;

import java.util.Locale;

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.05.19 09:43
 * @since 1.4.0
 */
public final class SystemInfoRt {
    /** OS_NAME */
    public static final String OS_NAME = System.getProperty("os.name");
    /** OS_VERSION */
    public static final String OS_VERSION = System.getProperty("os.version").toLowerCase(Locale.ENGLISH);
    /** IS_AT_LEAST_JAVA9 */
    public static final boolean IS_AT_LEAST_JAVA9 = isModularJava();
    /** _OS_NAME */
    private static final String OS_NAME_LOWERCASE = OS_NAME.toLowerCase(Locale.ENGLISH);
    /** isWindows */
    public static final boolean IS_WINDOWS = OS_NAME_LOWERCASE.startsWith("windows");
    /** isMac */
    public static final boolean IS_MAC = OS_NAME_LOWERCASE.startsWith("mac");
    /** isLinux */
    public static final boolean IS_LINUX = OS_NAME_LOWERCASE.startsWith("linux");
    /** ARCH_DATA_MODEL */
    private static final String ARCH_DATA_MODEL = System.getProperty("sun.arch.data.model");
    /** isUnix */
    public static final boolean IS_UNIX = !IS_WINDOWS;
    /** 系统是否大小写敏感 */
    public static final boolean IS_FILE_SYSTEM_CASE_SENSITIVE =
        IS_UNIX && !IS_MAC || "true".equalsIgnoreCase(System.getProperty("idea.case.sensitive.fs"));

    /**
     * Is modular java
     *
     * @return the boolean
     * @since 1.4.0
     */
    @Contract(pure = true)
    @SuppressWarnings("JavaReflectionMemberAccess")
    private static boolean isModularJava() {
        try {
            Class.class.getMethod("getModule");
            return true;
        } catch (Throwable t) {
            return false;
        }
    }
}
