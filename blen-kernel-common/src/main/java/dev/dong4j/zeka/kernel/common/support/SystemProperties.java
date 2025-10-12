package dev.dong4j.zeka.kernel.common.support;

import cn.hutool.core.util.StrUtil;
import java.util.function.BooleanSupplier;
import java.util.function.IntSupplier;
import java.util.function.LongSupplier;
import java.util.function.Supplier;

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2024.04.09 22:42
 * @since 2024.2.0
 */
@SuppressWarnings("all")
public final class SystemProperties {
    /** NULL_SUPPLIER */
    private static final Supplier<String> NULL_SUPPLIER = () -> null;
    /** AWT_TOOLKIT */
    public static final String AWT_TOOLKIT = "awt.toolkit";
    /** FILE_ENCODING */
    public static final String FILE_ENCODING = "file.encoding";
    /** FILE_SEPARATOR */
    public static final String FILE_SEPARATOR = "file.separator";
    /** JAVA_AWT_FONTS */
    public static final String JAVA_AWT_FONTS = "java.awt.fonts";
    /** JAVA_AWT_GRAPHICSENV */
    public static final String JAVA_AWT_GRAPHICSENV = "java.awt.graphicsenv";
    /** JAVA_AWT_HEADLESS */
    public static final String JAVA_AWT_HEADLESS = "java.awt.headless";
    /** JAVA_AWT_PRINTERJOB */
    public static final String JAVA_AWT_PRINTERJOB = "java.awt.printerjob";
    /** JAVA_CLASS_PATH */
    public static final String JAVA_CLASS_PATH = "java.class.path";
    /** JAVA_CLASS_VERSION */
    public static final String JAVA_CLASS_VERSION = "java.class.version";
    /** JAVA_COMPILER */
    public static final String JAVA_COMPILER = "java.compiler";
    /** JAVA_ENDORSED_DIRS */
    public static final String JAVA_ENDORSED_DIRS = "java.endorsed.dirs";
    /** JAVA_EXT_DIRS */
    public static final String JAVA_EXT_DIRS = "java.ext.dirs";
    /** JAVA_HOME */
    public static final String JAVA_HOME = "java.home";
    /** JAVA_IO_TMPDIR */
    public static final String JAVA_IO_TMPDIR = "java.io.tmpdir";
    /** JAVA_LIBRARY_PATH */
    public static final String JAVA_LIBRARY_PATH = "java.library.path";
    /** JAVA_LOCALE_PROVIDERS */
    public static final String JAVA_LOCALE_PROVIDERS = "java.locale.providers";
    /** JAVA_RUNTIME_NAME */
    public static final String JAVA_RUNTIME_NAME = "java.runtime.name";
    /** JAVA_RUNTIME_VERSION */
    public static final String JAVA_RUNTIME_VERSION = "java.runtime.version";
    /** JAVA_SPECIFICATION_NAME */
    public static final String JAVA_SPECIFICATION_NAME = "java.specification.name";
    /** JAVA_SPECIFICATION_VENDOR */
    public static final String JAVA_SPECIFICATION_VENDOR = "java.specification.vendor";
    /** JAVA_SPECIFICATION_VERSION */
    public static final String JAVA_SPECIFICATION_VERSION = "java.specification.version";
    /** JAVA_UTIL_PREFS_PREFERENCES_FACTORY */
    public static final String JAVA_UTIL_PREFS_PREFERENCES_FACTORY = "java.util.prefs.PreferencesFactory";
    /** JAVA_VENDOR */
    public static final String JAVA_VENDOR = "java.vendor";
    /** JAVA_VENDOR_URL */
    public static final String JAVA_VENDOR_URL = "java.vendor.url";
    /** JAVA_VERSION */
    public static final String JAVA_VERSION = "java.version";
    /** JAVA_VM_INFO */
    public static final String JAVA_VM_INFO = "java.vm.info";
    /** JAVA_VM_NAME */
    public static final String JAVA_VM_NAME = "java.vm.name";
    /** JAVA_VM_SPECIFICATION_NAME */
    public static final String JAVA_VM_SPECIFICATION_NAME = "java.vm.specification.name";
    /** JAVA_VM_SPECIFICATION_VENDOR */
    public static final String JAVA_VM_SPECIFICATION_VENDOR = "java.vm.specification.vendor";
    /** JAVA_VM_SPECIFICATION_VERSION */
    public static final String JAVA_VM_SPECIFICATION_VERSION = "java.vm.specification.version";
    /** JAVA_VM_VENDOR */
    public static final String JAVA_VM_VENDOR = "java.vm.vendor";
    /** JAVA_VM_VERSION */
    public static final String JAVA_VM_VERSION = "java.vm.version";
    /** LINE_SEPARATOR */
    public static final String LINE_SEPARATOR = "line.separator";
    /** OS_ARCH */
    public static final String OS_ARCH = "os.arch";
    /** OS_NAME */
    public static final String OS_NAME = "os.name";
    /** OS_VERSION */
    public static final String OS_VERSION = "os.version";
    /** PATH_SEPARATOR */
    public static final String PATH_SEPARATOR = "path.separator";
    /** USER_COUNTRY */
    public static final String USER_COUNTRY = "user.country";
    /** USER_DIR */
    public static final String USER_DIR = "user.dir";
    /** USER_HOME */
    public static final String USER_HOME = "user.home";
    /** USER_LANGUAGE */
    public static final String USER_LANGUAGE = "user.language";
    /** USER_NAME */
    public static final String USER_NAME = "user.name";
    /** USER_REGION */
    public static final String USER_REGION = "user.region";
    /** USER_TIMEZONE */
    public static final String USER_TIMEZONE = "user.timezone";

    /**
     * System properties
     *
     * @since 2024.2.0
     */
    public SystemProperties() {
    }

    /**
     * Gets awt toolkit *
     *
     * @return the awt toolkit
     * @since 2024.2.0
     */
    public static String getAwtToolkit() {
        return getProperty("awt.toolkit");
    }

    /**
     * Gets boolean *
     *
     * @param key             key
     * @param defaultIfAbsent default if absent
     * @return the boolean
     * @since 2024.2.0
     */
    public static boolean getBoolean(String key, BooleanSupplier defaultIfAbsent) {
        String str = getProperty(key);
        return str == null ? defaultIfAbsent != null && defaultIfAbsent.getAsBoolean() : Boolean.parseBoolean(str);
    }

    /**
     * Gets file encoding *
     *
     * @return the file encoding
     * @since 2024.2.0
     */
    public static String getFileEncoding() {
        return getProperty("file.encoding");
    }

    /**
     * Gets file separator *
     *
     * @return the file separator
     * @since 2024.2.0
     */
    public static String getFileSeparator() {
        return getProperty("file.separator");
    }

    /**
     * Gets int *
     *
     * @param key             key
     * @param defaultIfAbsent default if absent
     * @return the int
     * @since 2024.2.0
     */
    public static int getInt(String key, IntSupplier defaultIfAbsent) {
        String str = getProperty(key);
        return str == null ? (defaultIfAbsent != null ? defaultIfAbsent.getAsInt() : 0) : Integer.parseInt(str);
    }

    /**
     * Gets java awt fonts *
     *
     * @return the java awt fonts
     * @since 2024.2.0
     */
    public static String getJavaAwtFonts() {
        return getProperty("java.awt.fonts");
    }

    /**
     * Gets java awt graphicsenv *
     *
     * @return the java awt graphicsenv
     * @since 2024.2.0
     */
    public static String getJavaAwtGraphicsenv() {
        return getProperty("java.awt.graphicsenv");
    }

    /**
     * Gets java awt headless *
     *
     * @return the java awt headless
     * @since 2024.2.0
     */
    public static String getJavaAwtHeadless() {
        return getProperty("java.awt.headless");
    }

    /**
     * Gets java awt printerjob *
     *
     * @return the java awt printerjob
     * @since 2024.2.0
     */
    public static String getJavaAwtPrinterjob() {
        return getProperty("java.awt.printerjob");
    }

    /**
     * Gets java class path *
     *
     * @return the java class path
     * @since 2024.2.0
     */
    public static String getJavaClassPath() {
        return getProperty("java.class.path");
    }

    /**
     * Gets java class version *
     *
     * @return the java class version
     * @since 2024.2.0
     */
    public static String getJavaClassVersion() {
        return getProperty("java.class.version");
    }

    /**
     * Gets java compiler *
     *
     * @return the java compiler
     * @since 2024.2.0
     */
    public static String getJavaCompiler() {
        return getProperty("java.compiler");
    }

    /**
     * Gets java endorsed dirs *
     *
     * @return the java endorsed dirs
     * @since 2024.2.0
     */
    public static String getJavaEndorsedDirs() {
        return getProperty("java.endorsed.dirs");
    }

    /**
     * Gets java ext dirs *
     *
     * @return the java ext dirs
     * @since 2024.2.0
     */
    public static String getJavaExtDirs() {
        return getProperty("java.ext.dirs");
    }

    /**
     * Gets java home *
     *
     * @return the java home
     * @since 2024.2.0
     */
    public static String getJavaHome() {
        return getProperty("java.home");
    }

    /**
     * Gets java io tmpdir *
     *
     * @return the java io tmpdir
     * @since 2024.2.0
     */
    public static String getJavaIoTmpdir() {
        return getProperty("java.io.tmpdir");
    }

    /**
     * Gets java library path *
     *
     * @return the java library path
     * @since 2024.2.0
     */
    public static String getJavaLibraryPath() {
        return getProperty("java.library.path");
    }

    /**
     * Gets java locale providers *
     *
     * @return the java locale providers
     * @since 2024.2.0
     */
    public static String getJavaLocaleProviders() {
        return getProperty("java.locale.providers");
    }

    /**
     * Gets java runtime name *
     *
     * @return the java runtime name
     * @since 2024.2.0
     */
    public static String getJavaRuntimeName() {
        return getProperty("java.runtime.name");
    }

    /**
     * Gets java runtime version *
     *
     * @return the java runtime version
     * @since 2024.2.0
     */
    public static String getJavaRuntimeVersion() {
        return getProperty("java.runtime.version");
    }

    /**
     * Gets java specification name *
     *
     * @return the java specification name
     * @since 2024.2.0
     */
    public static String getJavaSpecificationName() {
        return getProperty("java.specification.name");
    }

    /**
     * Gets java specification vendor *
     *
     * @return the java specification vendor
     * @since 2024.2.0
     */
    public static String getJavaSpecificationVendor() {
        return getProperty("java.specification.vendor");
    }

    /**
     * Gets java specification version *
     *
     * @return the java specification version
     * @since 2024.2.0
     */
    public static String getJavaSpecificationVersion() {
        return getProperty("java.specification.version");
    }

    /**
     * Gets java util prefs preferences factory *
     *
     * @return the java util prefs preferences factory
     * @since 2024.2.0
     */
    public static String getJavaUtilPrefsPreferencesFactory() {
        return getProperty("java.util.prefs.PreferencesFactory");
    }

    /**
     * Gets java vendor *
     *
     * @return the java vendor
     * @since 2024.2.0
     */
    public static String getJavaVendor() {
        return getProperty("java.vendor");
    }

    /**
     * Gets java vendor url *
     *
     * @return the java vendor url
     * @since 2024.2.0
     */
    public static String getJavaVendorUrl() {
        return getProperty("java.vendor.url");
    }

    /**
     * Gets java version *
     *
     * @return the java version
     * @since 2024.2.0
     */
    public static String getJavaVersion() {
        return getProperty("java.version");
    }

    /**
     * Gets java vm info *
     *
     * @return the java vm info
     * @since 2024.2.0
     */
    public static String getJavaVmInfo() {
        return getProperty("java.vm.info");
    }

    /**
     * Gets java vm name *
     *
     * @return the java vm name
     * @since 2024.2.0
     */
    public static String getJavaVmName() {
        return getProperty("java.vm.name");
    }

    /**
     * Gets java vm specification name *
     *
     * @return the java vm specification name
     * @since 2024.2.0
     */
    public static String getJavaVmSpecificationName() {
        return getProperty("java.vm.specification.name");
    }

    /**
     * Gets java vm specification vendor *
     *
     * @return the java vm specification vendor
     * @since 2024.2.0
     */
    public static String getJavaVmSpecificationVendor() {
        return getProperty("java.vm.specification.vendor");
    }

    /**
     * Gets java vm specification version *
     *
     * @return the java vm specification version
     * @since 2024.2.0
     */
    public static String getJavaVmSpecificationVersion() {
        return getProperty("java.vm.specification.version");
    }

    /**
     * Gets java vm vendor *
     *
     * @return the java vm vendor
     * @since 2024.2.0
     */
    public static String getJavaVmVendor() {
        return getProperty("java.vm.vendor");
    }

    /**
     * Gets java vm version *
     *
     * @return the java vm version
     * @since 2024.2.0
     */
    public static String getJavaVmVersion() {
        return getProperty("java.vm.version");
    }

    /**
     * Gets line separator *
     *
     * @return the line separator
     * @since 2024.2.0
     */
    public static String getLineSeparator() {
        return getProperty("line.separator");
    }

    /**
     * Gets long *
     *
     * @param key             key
     * @param defaultIfAbsent default if absent
     * @return the long
     * @since 2024.2.0
     */
    public static long getLong(String key, LongSupplier defaultIfAbsent) {
        String str = getProperty(key);
        return str == null ? (defaultIfAbsent != null ? defaultIfAbsent.getAsLong() : 0L) : Long.parseLong(str);
    }

    /**
     * Gets os arch *
     *
     * @return the os arch
     * @since 2024.2.0
     */
    public static String getOsArch() {
        return getProperty("os.arch");
    }

    /**
     * Gets os name *
     *
     * @return the os name
     * @since 2024.2.0
     */
    public static String getOsName() {
        return getProperty("os.name");
    }

    /**
     * Gets os version *
     *
     * @return the os version
     * @since 2024.2.0
     */
    public static String getOsVersion() {
        return getProperty("os.version");
    }

    /**
     * Gets path separator *
     *
     * @return the path separator
     * @since 2024.2.0
     */
    public static String getPathSeparator() {
        return getProperty("path.separator");
    }

    /**
     * Gets property *
     *
     * @param property property
     * @return the property
     * @since 2024.2.0
     */
    public static String getProperty(String property) {
        return getProperty(property, NULL_SUPPLIER);
    }

    /**
     * Gets property *
     *
     * @param property     property
     * @param defaultValue default value
     * @return the property
     * @since 2024.2.0
     */
    public static String getProperty(String property, Supplier<String> defaultValue) {
        try {
            if (StrUtil.isEmpty(property)) {
                return defaultValue.get();
            } else {
                String value = System.getProperty(property);
                return StrUtil.isEmpty(value) ? defaultValue.get() : value;
            }
        } catch (SecurityException var3) {
            return defaultValue.get();
        }
    }

    /**
     * Gets user country *
     *
     * @return the user country
     * @since 2024.2.0
     */
    public static String getUserCountry() {
        return getProperty("user.country");
    }

    /**
     * Gets user dir *
     *
     * @return the user dir
     * @since 2024.2.0
     */
    public static String getUserDir() {
        return getProperty("user.dir");
    }

    /**
     * Gets user home *
     *
     * @return the user home
     * @since 2024.2.0
     */
    public static String getUserHome() {
        return getProperty("user.home");
    }

    /**
     * Gets user language *
     *
     * @return the user language
     * @since 2024.2.0
     */
    public static String getUserLanguage() {
        return getProperty("user.language");
    }

    /**
     * Gets user name *
     *
     * @return the user name
     * @since 2024.2.0
     */
    public static String getUserName() {
        return getProperty("user.name");
    }

    /**
     * Gets user timezone *
     *
     * @return the user timezone
     * @since 2024.2.0
     */
    public static String getUserTimezone() {
        return getProperty("user.timezone");
    }
}
