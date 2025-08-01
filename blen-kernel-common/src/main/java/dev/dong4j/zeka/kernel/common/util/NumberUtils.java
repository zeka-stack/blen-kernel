package dev.dong4j.zeka.kernel.common.util;

import cn.hutool.core.util.NumberUtil;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.lang.Nullable;

/**
 * <p>Description: 数字类型工具类 </p>
 *
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.27 18:22
 * @since 1.0.0
 */
@UtilityClass
public class NumberUtils extends org.springframework.util.NumberUtils {

    /**
     * All possible chars for representing a number as a String
     */
    public static final char[] DIGITS = {
        '0', '1', '2', '3', '4', '5',
        '6', '7', '8', '9', 'a', 'b',
        'c', 'd', 'e', 'f', 'g', 'h',
        'i', 'j', 'k', 'l', 'm', 'n',
        'o', 'p', 'q', 'r', 's', 't',
        'u', 'v', 'w', 'x', 'y', 'z',
        'A', 'B', 'C', 'D', 'E', 'F',
        'G', 'H', 'I', 'J', 'K', 'L',
        'M', 'N', 'O', 'P', 'Q', 'R',
        'S', 'T', 'U', 'V', 'W', 'X',
        'Y', 'Z'
    };

    /**
     * <p>Convert a <code>String</code> to an <code>int</code>, returning
     * <code>zero</code> if the conversion fails.</p>
     * <p>If the string is <code>null</code>, <code>zero</code> is returned.</p>
     * <pre>
     *   NumberUtil.toInt(null) = 0
     *   NumberUtil.toInt("")   = 0
     *   NumberUtil.toInt("1")  = 1
     * </pre>
     *
     * @param str the string to convert, may be null
     * @return the int represented by the string, or <code>zero</code> if     conversion fails
     * @since 1.0.0
     */
    public static int toInt(String str) {
        return toInt(str, -1);
    }

    /**
     * Is numer boolean
     *
     * @param str str
     * @return the boolean
     * @since 1.0.0
     */
    @Contract("null -> false")
    public static boolean isNumer(String str) {
        return NumberUtil.isNumber(str);
    }

    /**
     * <p>Convert a <code>String</code> to an <code>int</code>, returning a
     * default value if the conversion fails.</p>
     * <p>If the string is <code>null</code>, the default value is returned.</p>
     * <pre>
     *   NumberUtil.toInt(null, 1) = 1
     *   NumberUtil.toInt("", 1)   = 1
     *   NumberUtil.toInt("1", 0)  = 1
     * </pre>
     *
     * @param str          the string to convert, may be null
     * @param defaultValue the default value
     * @return the int represented by the string, or the default if conversion fails
     * @since 1.0.0
     */
    @Contract("null, _ -> param2")
    public static int toInt(@Nullable String str, int defaultValue) {
        if (str == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException nfe) {
            return defaultValue;
        }
    }

    /**
     * <p>Convert a <code>String</code> to a <code>long</code>, returning
     * <code>zero</code> if the conversion fails.</p>
     * <p>If the string is <code>null</code>, <code>zero</code> is returned.</p>
     * <pre>
     *   NumberUtil.toLong(null) = 0L
     *   NumberUtil.toLong("")   = 0L
     *   NumberUtil.toLong("1")  = 1L
     * </pre>
     *
     * @param str the string to convert, may be null
     * @return the long represented by the string, or <code>0</code> if     conversion fails
     * @since 1.0.0
     */
    public static long toLong(String str) {
        return toLong(str, 0L);
    }

    /**
     * <p>Convert a <code>String</code> to a <code>long</code>, returning a
     * default value if the conversion fails.</p>
     * <p>If the string is <code>null</code>, the default value is returned.</p>
     * <pre>
     *   NumberUtil.toLong(null, 1L) = 1L
     *   NumberUtil.toLong("", 1L)   = 1L
     *   NumberUtil.toLong("1", 0L)  = 1L
     * </pre>
     *
     * @param str          the string to convert, may be null
     * @param defaultValue the default value
     * @return the long represented by the string, or the default if conversion fails
     * @since 1.0.0
     */
    @Contract("null, _ -> param2")
    public static long toLong(@Nullable String str, long defaultValue) {
        if (str == null) {
            return defaultValue;
        }
        try {
            return Long.parseLong(str);
        } catch (NumberFormatException nfe) {
            return defaultValue;
        }
    }

    /**
     * <p>Convert a <code>String</code> to a <code>Double</code>
     *
     * @param value value
     * @return double value
     * @since 1.0.0
     */
    @Contract("!null -> !null")
    public static Double toDouble(String value) {
        return toDouble(value, null);
    }

    /**
     * <p>Convert a <code>String</code> to a <code>Double</code>
     *
     * @param value        value
     * @param defaultValue 默认值
     * @return double value
     * @since 1.0.0
     */
    @Contract("!null, _ -> !null; null, _ -> param2")
    public static Double toDouble(@Nullable String value, Double defaultValue) {
        if (value != null) {
            return Double.valueOf(value.trim());
        }
        return defaultValue;
    }

    /**
     * <p>Convert a <code>String</code> to a <code>Double</code>
     *
     * @param value value
     * @return double value
     * @since 1.0.0
     */
    @Contract("!null -> !null")
    public static Float toFloat(String value) {
        return toFloat(value, null);
    }

    /**
     * <p>Convert a <code>String</code> to a <code>Double</code>
     *
     * @param value        value
     * @param defaultValue 默认值
     * @return double value
     * @since 1.0.0
     */
    @Contract("!null, _ -> !null; null, _ -> param2")
    public static Float toFloat(@Nullable String value, Float defaultValue) {
        if (value != null) {
            return Float.valueOf(value.trim());
        }
        return defaultValue;
    }

    /**
     * 将 long 转短字符串 为 62 进制
     *
     * @param i 数字
     * @return 短字符串 string
     * @since 1.0.0
     */
    @NotNull
    @Contract(value = "_ -> new", pure = true)
    public static String to62String(long i) {
        int radix = DIGITS.length;
        char[] buf = new char[65];
        int charPos = 64;
        i = -i;
        while (i <= -radix) {
            buf[charPos--] = DIGITS[(int) (-(i % radix))];
            i = i / radix;
        }
        buf[charPos] = DIGITS[(int) (-i)];

        return new String(buf, charPos, (65 - charPos));
    }

}
