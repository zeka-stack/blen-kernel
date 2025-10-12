package dev.dong4j.zeka.kernel.common.support;

import cn.hutool.core.util.NumberUtil;
import dev.dong4j.zeka.kernel.common.util.NumberUtils;

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2024.02.01 15:41
 * @since 2024.2.0
 */
@SuppressWarnings("all")
public enum JavaVersion {
    /** Java 1 8 java version */
    JAVA_1_8(1.8F, "1.8"),
    /** Java 9 java version */
    JAVA_9(9.0F, "9"),
    /** Java 10 java version */
    JAVA_10(10.0F, "10"),
    /** Java 11 java version */
    JAVA_11(11.0F, "11"),
    /** Java 12 java version */
    JAVA_12(12.0F, "12"),
    /** Java 13 java version */
    JAVA_13(13.0F, "13"),
    /** Java 14 java version */
    JAVA_14(14.0F, "14"),
    /** Java 15 java version */
    JAVA_15(15.0F, "15"),
    /** Java 16 java version */
    JAVA_16(16.0F, "16"),
    /** Java 17 java version */
    JAVA_17(17.0F, "17"),
    /** Java recent java version */
    JAVA_RECENT(maxVersion(), Float.toString(maxVersion()));

    /** Value */
    private final float value;
    /** Name */
    private final String name;

    /**
     * Java version
     *
     * @param value value
     * @param name  name
     * @since 2024.2.0
     */
    JavaVersion(float value, String name) {
        this.value = value;
        this.name = name;
    }

    /**
     * At least
     *
     * @param requiredVersion required version
     * @return the boolean
     * @since 2024.2.0
     */
    public boolean atLeast(JavaVersion requiredVersion) {
        return this.value >= requiredVersion.value;
    }

    /**
     * At most
     *
     * @param requiredVersion required version
     * @return the boolean
     * @since 2024.2.0
     */
    public boolean atMost(JavaVersion requiredVersion) {
        return this.value <= requiredVersion.value;
    }

    /**
     * Gets java version *
     *
     * @param nom nom
     * @return the java version
     * @since 2024.2.0
     */
    static JavaVersion getJavaVersion(String nom) {
        return get(nom);
    }

    /**
     * Get
     *
     * @param versionStr version str
     * @return the java version
     * @since 2024.2.0
     */
    public static JavaVersion get(String versionStr) {
        if (versionStr == null) {
            return null;
        } else {
            switch (versionStr) {
                case "1.8":
                    return JAVA_1_8;
                case "9":
                    return JAVA_9;
                case "10":
                    return JAVA_10;
                case "11":
                    return JAVA_11;
                case "12":
                    return JAVA_12;
                case "13":
                    return JAVA_13;
                case "14":
                    return JAVA_14;
                case "15":
                    return JAVA_15;
                case "16":
                    return JAVA_16;
                case "17":
                    return JAVA_17;
                default:
                    float v = toFloatVersion(versionStr);
                    if ((double) v - 1.0 < 1.0) {
                        int firstComma = Math.max(versionStr.indexOf(46), versionStr.indexOf(44));
                        int end = Math.max(versionStr.length(), versionStr.indexOf(44, firstComma));
                        if (Float.parseFloat(versionStr.substring(firstComma + 1, end)) > 0.9F) {
                            return JAVA_RECENT;
                        }
                    } else if (v > 10.0F) {
                        return JAVA_RECENT;
                    }

                    return null;
            }
        }
    }

    /**
     * To string
     *
     * @return the string
     * @since 2024.2.0
     */
    @Override
    public String toString() {
        return this.name;
    }

    /**
     * Max version
     *
     * @return the float
     * @since 2024.2.0
     */
    private static float maxVersion() {
        float v = toFloatVersion(System.getProperty("java.specification.version", "99.0"));
        return v > 0.0F ? v : 99.0F;
    }

    /**
     * To float version
     *
     * @param value value
     * @return the float
     * @since 2024.2.0
     */
    private static float toFloatVersion(String value) {
        if (value.contains(".")) {
            String[] toParse = value.split("\\.");
            return toParse.length >= 2 ? NumberUtils.toFloat(toParse[0] + '.' + toParse[1], -1.0F) : -1.0F;
        } else {
            return NumberUtil.parseFloat(value, -1.0F);
        }
    }
}
