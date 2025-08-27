package dev.dong4j.zeka.kernel.common.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Maps;
import com.google.common.primitives.Ints;
import dev.dong4j.zeka.kernel.common.enums.RandomType;
import dev.dong4j.zeka.kernel.common.function.CheckedConsumer;
import dev.dong4j.zeka.kernel.common.function.CheckedRunnable;
import dev.dong4j.zeka.kernel.common.support.StrFormatter;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAccessor;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;
import java.util.function.Supplier;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.BeansException;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.io.Resource;
import org.springframework.util.PatternMatchUtils;
import org.springframework.util.StopWatch;
import org.springframework.web.method.HandlerMethod;

/**
 * <p>Description: 工具包集合,只做简单的调用,不删除原有工具类 </p>
 *
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.27 18:13
 * @since 1.0.0
 */
@UtilityClass
@SuppressWarnings("all")
public class Tools {

    /**
     * 断言,必须不能为 null
     * <blockquote><pre>
     * public Foo(Bar bar) {
     *     this.bar = $.requireNotNull(bar);
     * }
     * </pre></blockquote>
     *
     * @param <T> the type of the reference
     * @param obj the object reference to check for nullity
     * @return {@code obj} if not {@code null}
     * @throws NullPointerException if {@code obj} is {@code null}
     * @since 1.0.0
     */
    @Contract(value = "null -> fail; !null -> param1", pure = true)
    public static <T> T requireNotNull(T obj) {
        return Objects.requireNonNull(obj);
    }

    /**
     * 断言,必须不能为 null
     * <blockquote><pre>
     * public Foo(Bar bar, Baz baz) {
     *     this.bar = $.requireNotNull(bar, "bar must not be null");
     *     this.baz = $.requireNotNull(baz, "baz must not be null");
     * }
     * </pre></blockquote>
     *
     * @param <T>     the type of the reference
     * @param obj     the object reference to check for nullity
     * @param message detail message to be used in the event that a {@code
     *                NullPointerException} is thrown
     * @return {@code obj} if not {@code null}
     * @throws NullPointerException if {@code obj} is {@code null}
     * @since 1.0.0
     */
    @Contract(value = "null, _ -> fail; !null, _ -> param1", pure = true)
    public static <T> T requireNotNull(T obj, String message) {
        return Objects.requireNonNull(obj, message);
    }

    /**
     * 断言,必须不能为 null
     * <blockquote><pre>
     * public Foo(Bar bar, Baz baz) {
     *     this.bar = $.requireNotNull(bar, () -> "bar must not be null");
     * }
     * </pre></blockquote>
     *
     * @param <T>             the type of the reference
     * @param obj             the object reference to check for nullity
     * @param messageSupplier supplier of the detail message to be used in the event that a {@code NullPointerException} is thrown
     * @return {@code obj} if not {@code null}
     * @throws NullPointerException if {@code obj} is {@code null}
     * @since 1.0.0
     */
    @Contract("null, _ -> fail; !null, _ -> param1")
    public static <T> T requireNotNull(T obj, Supplier<String> messageSupplier) {
        return Objects.requireNonNull(obj, messageSupplier);
    }

    /**
     * 判断对象为true
     *
     * @param object 对象
     * @return 对象是否为true boolean
     * @since 1.0.0
     */
    @Contract(value = "null -> false", pure = true)
    public static boolean isTrue(@Nullable Boolean object) {
        return ObjectUtils.isTrue(object);
    }

    /**
     * 判断对象为false
     *
     * @param object 对象
     * @return 对象是否为false boolean
     * @since 1.0.0
     */
    @Contract(value = "null -> true", pure = true)
    public static boolean isFalse(@Nullable Boolean object) {
        return ObjectUtils.isFalse(object);
    }

    /**
     * 判断对象是否为null
     * <p>
     * This method exists to be used as a
     * {@link java.util.function.Predicate}, {@code context($::isNull)}
     * </p>
     *
     * @param obj a reference to be checked against {@code null}
     * @return {@code true} if the provided reference is {@code null} otherwise     {@code false}
     * @see java.util.function.Predicate
     * @since 1.0.0
     */
    @Contract(value = "!null -> false; null -> true", pure = true)
    public static boolean isNull(@Nullable Object obj) {
        return Objects.isNull(obj);
    }

    /**
     * 判断对象是否 not null
     * <p>
     * This method exists to be used as a
     * {@link java.util.function.Predicate}, {@code context($::notNull)}
     * </p>
     *
     * @param obj a reference to be checked against {@code null}
     * @return {@code true} if the provided reference is non-{@code null}     otherwise {@code false}
     * @see java.util.function.Predicate
     * @since 1.0.0
     */
    @Contract(value = "!null -> true; null -> false", pure = true)
    public static boolean isNotNull(@Nullable Object obj) {
        return Objects.nonNull(obj);
    }

    /**
     * 首字母变小写
     *
     * @param str 字符串
     * @return {String}
     * @since 1.0.0
     */
    @NotNull
    public static String firstCharToLower(String str) {
        return StringUtils.firstCharToLower(str);
    }

    /**
     * 首字母变大写
     *
     * @param str 字符串
     * @return {String}
     * @since 1.0.0
     */
    @NotNull
    public static String firstCharToUpper(String str) {
        return StringUtils.firstCharToUpper(str);
    }

    /**
     * 判断不为空字符串
     * <pre>
     * $.isNotBlank(null)     = false
     * $.isNotBlank("")          = false
     * $.isNotBlank(" ")     = false
     * $.isNotBlank("bob")     = true
     * $.isNotBlank("  bob  ") = true
     * </pre>
     *
     * @param cs the CharSequence to check, may be null
     * @return {@code true} if the CharSequence is     not empty and not null and not whitespace
     * @see Character#isWhitespace Character#isWhitespaceCharacter#isWhitespaceCharacter#isWhitespaceCharacter#isWhitespace
     * @since 1.0.0
     */
    @Contract("null -> false")
    public static boolean isNotBlank(@Nullable CharSequence cs) {
        return StringUtils.isNotBlank(cs);
    }

    /**
     * 有 任意 一个 Blank
     *
     * @param css CharSequence
     * @return boolean boolean
     * @since 1.0.0
     */
    public static boolean isAnyBlank(CharSequence... css) {
        return StringUtils.isAnyBlank(css);
    }

    /**
     * 判断是否全为非空字符串
     *
     * @param css CharSequence
     * @return boolean boolean
     * @since 1.0.0
     */
    public static boolean isNoneBlank(CharSequence... css) {
        return StringUtils.isNoneBlank(css);
    }

    /**
     * 判断对象是数组
     *
     * @param obj the object to check
     * @return 是否数组 boolean
     * @since 1.0.0
     */
    @Contract("null -> false")
    public static boolean isArray(@Nullable Object obj) {
        return ObjectUtils.isArray(obj);
    }

    /**
     * 对象不为空 object、map、list、set、字符串、数组
     *
     * @param obj the object to check
     * @return 是否不为空 boolean
     * @since 1.0.0
     */
    @Contract("null -> false")
    public static boolean isNotEmpty(@Nullable Object obj) {
        return !ObjectUtils.isEmpty(obj);
    }

    /**
     * 判断数组为空
     *
     * @param array the array to check
     * @return 数组是否为空 boolean
     * @since 1.0.0
     */
    @Contract(value = "null -> true", pure = true)
    public static boolean isEmpty(@Nullable Object[] array) {
        return ObjectUtils.isEmpty(array);
    }

    /**
     * 判断数组不为空
     *
     * @param array 数组
     * @return 数组是否不为空 boolean
     * @since 1.0.0
     */
    @Contract("null -> false")
    public static boolean isNotEmpty(@Nullable Object[] array) {
        return ObjectUtils.isNotEmpty(array);
    }

    /**
     * 对象组中是否存在 Empty Object
     *
     * @param os 对象组
     * @return boolean boolean
     * @since 1.0.0
     */
    public static boolean hasEmpty(@NotNull Object... os) {
        for (Object o : os) {
            if (isEmpty(o)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断空对象 object、map、list、set、字符串、数组
     *
     * @param obj the object to check
     * @return 数组是否为空 boolean
     * @since 1.0.0
     */
    @Contract("null -> true")
    public static boolean isEmpty(@Nullable Object obj) {
        return ObjectUtils.isEmpty(obj);
    }

    /**
     * 对象组中是否全是 Empty Object
     *
     * @param os 对象组
     * @return boolean boolean
     * @since 1.0.0
     */
    public static boolean allEmpty(@NotNull Object... os) {
        for (Object o : os) {
            if (!isEmpty(o)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 将字符串中特定模式的字符转换成map中对应的值
     * <p>
     * use: format("my name is ${name}, and i like ${like}!", {"name":"L.cm", "like": "Java"})
     *
     * @param message 需要转换的字符串
     * @param params  转换所需的键值对集合
     * @return 转换后的字符串 string
     * @since 1.0.0
     */
    @Contract("null, _ -> null; !null, null -> !null")
    public static String format(@Nullable String message, @Nullable Map<String, Object> params) {
        return StringUtils.format(message, params);
    }

    /**
     * 同 log 格式的 format 规则
     * <p>
     * use: format("my name is {}, and i like {}!", "L.cm", "Java")
     *
     * @param message   需要转换的字符串
     * @param arguments 需要替换的变量
     * @return 转换后的字符串 string
     * @since 1.0.0
     */
    @Contract("null, _ -> null")
    public static String format(@Nullable String message, @Nullable Object... arguments) {
        return StringUtils.format(message, arguments);
    }

    /**
     * 清理字符串,清理出某些不可见字符和一些sql特殊字符
     *
     * @param txt 文本
     * @return {String}
     * @since 1.0.0
     */
    @Contract("null -> null")
    @Nullable
    public static String cleanText(@Nullable String txt) {
        return StringUtils.cleanText(txt);
    }

    /**
     * 获取标识符,用于参数清理
     *
     * @param param 参数
     * @return 清理后的标识符 string
     * @since 1.0.0
     */
    @Contract("null -> null")
    @Nullable
    public static String cleanIdentifier(@Nullable String param) {
        return StringUtils.cleanIdentifier(param);
    }

    /**
     * 比较两个对象是否相等. <br>
     * 相同的条件有两个,满足其一即可: <br>
     *
     * @param obj1 对象1
     * @param obj2 对象2
     * @return 是否相等 boolean
     * @since 1.0.0
     */
    @Contract(value = "null, !null -> false; !null, null -> false", pure = true)
    public static boolean equals(Object obj1, Object obj2) {
        return Objects.equals(obj1, obj2);
    }

    /**
     * Determine if the given objects are equal, returning {@code true} if
     * both are {@code null} or {@code false} if only one is {@code null}.
     * <p>Compares arrays with {@code Arrays.equals}, performing an equality
     * check based on the array elements rather than the array reference.
     *
     * @param o1 first Object to compare
     * @param o2 second Object to compare
     * @return whether the given objects are equal
     * @see Object#equals(Object) Object#equals(Object)Object#equals(Object)Object#equals(Object)Object#equals(Object)Object#equals
     * (Object)Object#equals(Object)
     * @see Arrays#equals Arrays#equalsArrays#equalsArrays#equalsArrays#equalsArrays#equalsArrays#equals
     * @since 1.0.0
     */
    public static boolean equalsSafe(@Nullable Object o1, @Nullable Object o2) {
        return ObjectUtils.nullSafeEquals(o1, o2);
    }

    /**
     * 比较两个对象是否不相等. <br>
     *
     * @param o1 对象1
     * @param o2 对象2
     * @return 是否不eq boolean
     * @since 1.0.0
     */
    @Contract(value = "null, !null -> true; !null, null -> true", pure = true)
    public static boolean isNotEqual(Object o1, Object o2) {
        return !Objects.equals(o1, o2);
    }

    /**
     * 返回对象的 hashCode
     *
     * @param obj Object
     * @return hashCode int
     * @since 1.0.0
     */
    public static int hashCode(@Nullable Object obj) {
        return Objects.hashCode(obj);
    }

    /**
     * 如果对象为null,返回默认值
     *
     * @param object       Object
     * @param defaultValue 默认值
     * @return Object object
     * @since 1.0.0
     */
    @Contract(value = "!null, _ -> param1; null, _ -> param2", pure = true)
    public static Object defaultIfNull(@Nullable Object object, Object defaultValue) {
        return object != null ? object : defaultValue;
    }

    /**
     * 判断数组中是否包含元素
     *
     * @param <T>     The generic tag
     * @param array   the Array to check
     * @param element the element to look for
     * @return {@code true} if found, {@code false} else
     * @since 1.0.0
     */
    @Contract("null, _ -> false")
    public static <T> boolean contains(@Nullable T[] array, T element) {
        return CollectionUtils.contains(array, element);
    }

    /**
     * 判断迭代器中是否包含元素
     *
     * @param iterator the Iterator to check
     * @param element  the element to look for
     * @return {@code true} if found, {@code false} otherwise
     * @since 1.0.0
     */
    @Contract("null, _ -> false")
    public static boolean contains(@Nullable Iterator<?> iterator, Object element) {
        return CollectionUtils.contains(iterator, element);
    }

    /**
     * 判断枚举是否包含该元素
     *
     * @param enumeration the Enumeration to check
     * @param element     the element to look for
     * @return {@code true} if found, {@code false} otherwise
     * @since 1.0.0
     */
    @Contract("null, _ -> false")
    public static boolean contains(@Nullable Enumeration<?> enumeration, Object element) {
        return CollectionUtils.contains(enumeration, element);
    }

    /**
     * Concatenates 2 arrays
     *
     * @param one   数组1
     * @param other 数组2
     * @return 新数组 string [ ]
     * @since 1.0.0
     */
    public static String[] concat(String[] one, String[] other) {
        return CollectionUtils.concat(one, other, String.class);
    }

    /**
     * Concatenates 2 arrays
     *
     * @param <T>   parameter
     * @param one   数组1
     * @param other 数组2
     * @param clazz 数组类
     * @return 新数组 t [ ]
     * @since 1.0.0
     */
    public static <T> T[] concat(T[] one, T[] other, Class<T> clazz) {
        return CollectionUtils.concat(one, other, clazz);
    }

    /**
     * 不可变 Set
     *
     * @param <E> 泛型
     * @param es  对象
     * @return 集合 set
     * @since 1.0.0
     */
    public static <E> Set<E> ofImmutableSet(E[] es) {
        return CollectionUtils.ofImmutableSet(es);
    }

    /**
     * 不可变 List
     *
     * @param <E> 泛型
     * @param es  对象
     * @return 集合 list
     * @since 1.0.0
     */
    public static <E> List<E> ofImmutableList(E[] es) {
        return CollectionUtils.ofImmutableList(es);
    }

    /**
     * 强转string,并去掉多余空格
     *
     * @param str 字符串
     * @return String string
     * @since 1.0.0
     */
    @Contract(pure = true)
    public static String toStr(Object str) {
        return toStr(str, "");
    }

    /**
     * 强转string,并去掉多余空格
     *
     * @param str          字符串
     * @param defaultValue 默认值
     * @return String string
     * @since 1.0.0
     */
    @Contract(value = "null, _ -> param2", pure = true)
    public static String toStr(Object str, String defaultValue) {
        return StrFormatter.toStr(str, defaultValue);
    }

    /**
     * 判断一个字符串是否是数字
     *
     * @param cs the CharSequence to check, may be null
     * @return {boolean}
     * @since 1.0.0
     */
    public static boolean isNumeric(CharSequence cs) {
        return StringUtils.isNumeric(cs);
    }

    /**
     * <p>Convert a <code>String</code> to an <code>int</code>, returning
     * <code>zero</code> if the conversion fails.</p>
     * <p>If the string is <code>null</code>, <code>zero</code> is returned.</p>
     * <pre>
     *   $.toInt(null) = 0
     *   $.toInt("")   = 0
     *   $.toInt("1")  = 1
     * </pre>
     *
     * @param value the string to convert, may be null
     * @return the int represented by the string, or <code>zero</code> if     conversion fails
     * @since 1.0.0
     */
    public static int toInt(Object value) {
        return NumberUtils.toInt(String.valueOf(value));
    }

    /**
     * <p>Convert a <code>String</code> to a <code>long</code>, returning
     * <code>zero</code> if the conversion fails.</p>
     * <p>If the string is <code>null</code>, <code>zero</code> is returned.</p>
     * <pre>
     *   $.toLong(null) = 0L
     *   $.toLong("")   = 0L
     *   $.toLong("1")  = 1L
     * </pre>
     *
     * @param value the string to convert, may be null
     * @return the long represented by the string, or <code>0</code> if     conversion fails
     * @since 1.0.0
     */
    public static long toLong(Object value) {
        return NumberUtils.toLong(String.valueOf(value));
    }

    /**
     * <p>Convert a <code>String</code> to a <code>long</code>, returning a
     * default value if the conversion fails.</p>
     * <p>If the string is <code>null</code>, the default value is returned.</p>
     * <pre>
     *   $.toLong(null, 1L) = 1L
     *   $.toLong("", 1L)   = 1L
     *   $.toLong("1", 0L)  = 1L
     * </pre>
     *
     * @param value        the string to convert, may be null
     * @param defaultValue the default value
     * @return the long represented by the string, or the default if conversion fails
     * @since 1.0.0
     */
    public static long toLong(Object value, long defaultValue) {
        return NumberUtils.toLong(String.valueOf(value), defaultValue);
    }

    /**
     * <p>Convert a <code>String</code> to an <code>Double</code>, returning a
     * default value if the conversion fails.</p>
     * <p>If the string is <code>null</code>, the default value is returned.</p>
     * <pre>
     *   $.toDouble(null, 1) = 1.0
     *   $.toDouble("", 1)   = 1.0
     *   $.toDouble("1", 0)  = 1.0
     * </pre>
     *
     * @param value the string to convert, may be null
     * @return the int represented by the string, or the default if conversion fails
     * @since 1.0.0
     */
    public static Double toDouble(Object value) {
        return toDouble(String.valueOf(value), -1.00);
    }

    /**
     * <p>Convert a <code>String</code> to an <code>Double</code>, returning a
     * default value if the conversion fails.</p>
     * <p>If the string is <code>null</code>, the default value is returned.</p>
     * <pre>
     *   $.toDouble(null, 1) = 1.0
     *   $.toDouble("", 1)   = 1.0
     *   $.toDouble("1", 0)  = 1.0
     * </pre>
     *
     * @param value        the string to convert, may be null
     * @param defaultValue the default value
     * @return the int represented by the string, or the default if conversion fails
     * @since 1.0.0
     */
    public static Double toDouble(Object value, Double defaultValue) {
        return NumberUtils.toDouble(String.valueOf(value), defaultValue);
    }

    /**
     * <p>Convert a <code>String</code> to an <code>Float</code>, returning a
     * default value if the conversion fails.</p>
     * <p>If the string is <code>null</code>, the default value is returned.</p>
     * <pre>
     *   $.toFloat(null, 1) = 1.00f
     *   $.toFloat("", 1)   = 1.00f
     *   $.toFloat("1", 0)  = 1.00f
     * </pre>
     *
     * @param value the string to convert, may be null
     * @return the int represented by the string, or the default if conversion fails
     * @since 1.0.0
     */
    public static Float toFloat(Object value) {
        return toFloat(String.valueOf(value), -1.0f);
    }

    /**
     * <p>Convert a <code>String</code> to an <code>Float</code>, returning a
     * default value if the conversion fails.</p>
     * <p>If the string is <code>null</code>, the default value is returned.</p>
     * <pre>
     *   $.toFloat(null, 1) = 1.00f
     *   $.toFloat("", 1)   = 1.00f
     *   $.toFloat("1", 0)  = 1.00f
     * </pre>
     *
     * @param value        the string to convert, may be null
     * @param defaultValue the default value
     * @return the int represented by the string, or the default if conversion fails
     * @since 1.0.0
     */
    public static Float toFloat(Object value, Float defaultValue) {
        return NumberUtils.toFloat(String.valueOf(value), defaultValue);
    }

    /**
     * 对象转为 Byte
     *
     * @param object Object
     * @return 结果 byte
     * @since 1.0.0
     */
    public static byte toByte(@Nullable Object object) {
        return ObjectUtils.toByte(object, (byte) 0);
    }

    /**
     * 对象转为 Byte
     *
     * @param object       Object
     * @param defaultValue byte
     * @return 结果 byte
     * @since 1.0.0
     */
    @Contract("null, _ -> param2")
    public static byte toByte(@Nullable Object object, byte defaultValue) {
        return ObjectUtils.toByte(object, defaultValue);
    }

    /**
     * 对象转为 Short
     *
     * @param object Object
     * @return 结果 short
     * @since 1.0.0
     */
    public static short toShort(@Nullable Object object) {
        return ObjectUtils.toShort(object, (short) 0);
    }

    /**
     * 对象转为 Short
     *
     * @param object       Object
     * @param defaultValue short
     * @return 结果 short
     * @since 1.0.0
     */
    @Contract("null, _ -> param2")
    public static short toShort(@Nullable Object object, short defaultValue) {
        return ObjectUtils.toShort(object, defaultValue);
    }

    /**
     * <p>Convert a <code>String</code> to an <code>Boolean</code>, returning a
     * default value if the conversion fails.</p>
     * <p>If the string is <code>null</code>, the default value is returned.</p>
     * <pre>
     *   $.toBoolean("true", true)  = true
     *   $.toBoolean("false")        = false
     *   $.toBoolean("", false)       = false
     * </pre>
     *
     * @param value the string to convert, may be null
     * @return the int represented by the string, or the default if conversion fails
     * @since 1.0.0
     */
    public static Boolean toBoolean(Object value) {
        return toBoolean(value, null);
    }

    /**
     * <p>Convert a <code>String</code> to an <code>Boolean</code>, returning a
     * default value if the conversion fails.</p>
     * <p>If the string is <code>null</code>, the default value is returned.</p>
     * <pre>
     *   $.toBoolean("true", true)  = true
     *   $.toBoolean("false")        = false
     *   $.toBoolean("", false)       = false
     * </pre>
     *
     * @param value        the string to convert, may be null
     * @param defaultValue the default value
     * @return the int represented by the string, or the default if conversion fails
     * @since 1.0.0
     */
    @Contract("null, _ -> param2")
    public static Boolean toBoolean(Object value, Boolean defaultValue) {
        if (value != null) {
            String val = String.valueOf(value);
            val = val.toLowerCase().trim();
            return Boolean.parseBoolean(val);
        }
        return defaultValue;
    }

    /**
     * 转换为Integer集合<br>
     *
     * @param str 结果被转换的值
     * @return 结果 list
     * @since 1.0.0
     */
    @NotNull
    public static List<Integer> toIntList(String str) {
        return Arrays.asList(toIntArray(str));
    }

    /**
     * 转换为Integer数组<br>
     *
     * @param str 被转换的值
     * @return 结果 integer [ ]
     * @since 1.0.0
     */
    public static Integer[] toIntArray(String str) {
        return toIntArray(",", str);
    }

    /**
     * 转换为Integer数组<br>
     *
     * @param split 分隔符
     * @param str   被转换的值
     * @return 结果 integer [ ]
     * @since 1.0.0
     */
    public static Integer[] toIntArray(String split, String str) {
        if (StringUtils.isEmpty(str)) {
            return new Integer[]{};
        }
        String[] arr = str.split(split);
        Integer[] ints = new Integer[arr.length];
        for (int i = 0; i < arr.length; i++) {
            int v = toInt(arr[i], 0);
            ints[i] = v;
        }
        return ints;
    }

    /**
     * <p>Convert a <code>String</code> to an <code>int</code>, returning a
     * default value if the conversion fails.</p>
     * <p>If the string is <code>null</code>, the default value is returned.</p>
     * <pre>
     *   $.toInt(null, 1) = 1
     *   $.toInt("", 1)   = 1
     *   $.toInt("1", 0)  = 1
     * </pre>
     *
     * @param value        the string to convert, may be null
     * @param defaultValue the default value
     * @return the int represented by the string, or the default if conversion fails
     * @since 1.0.0
     */
    public static int toInt(Object value, int defaultValue) {
        return NumberUtils.toInt(String.valueOf(value), defaultValue);
    }

    /**
     * 转换为Integer集合<br>
     *
     * @param split 分隔符
     * @param str   被转换的值
     * @return 结果 list
     * @since 1.0.0
     */
    @NotNull
    public static List<Integer> toIntList(String split, String str) {
        return Arrays.asList(toIntArray(split, str));
    }

    /**
     * 转换为String集合<br>
     *
     * @param str 结果被转换的值
     * @return 结果 list
     * @since 1.0.0
     */
    @NotNull
    public static List<String> toStrList(String str) {
        return Arrays.asList(toStrArray(str));
    }

    /**
     * 转换为String数组<br>
     *
     * @param str 被转换的值
     * @return 结果 string [ ]
     * @since 1.0.0
     */
    @NotNull
    public static String[] toStrArray(String str) {
        return toStrArray(",", str);
    }

    /**
     * 转换为String数组<br>
     *
     * @param split 分隔符
     * @param str   被转换的值
     * @return 结果 string [ ]
     * @since 1.0.0
     */
    @NotNull
    public static String[] toStrArray(String split, String str) {
        if (isBlank(str)) {
            return new String[0];
        }
        return str.split(split);
    }

    /**
     * 判断是否为空字符串
     * <pre class="code">
     * $.isBlank(null) = true
     * $.isBlank("") = true
     * $.isBlank(" ") = true
     * $.isBlank("12345") = false
     * $.isBlank(" 12345 ") = false
     * </pre>
     *
     * @param cs the {@code CharSequence} to check (may be {@code null})
     * @return {@code true} if the {@code CharSequence} is not {@code null},     its length is greater than 0, and it does not contain
     * whitespace only
     * @see Character#isWhitespace Character#isWhitespaceCharacter#isWhitespaceCharacter#isWhitespaceCharacter#isWhitespace
     * @since 1.0.0
     */
    @Contract("null -> true")
    public static boolean isBlank(@Nullable CharSequence cs) {
        return StringUtils.isBlank(cs);
    }

    /**
     * 转换为String集合<br>
     *
     * @param split 分隔符
     * @param str   被转换的值
     * @return 结果 list
     * @since 1.0.0
     */
    @NotNull
    public static List<String> toStrList(String split, String str) {
        return Arrays.asList(toStrArray(split, str));
    }

    /**
     * 将 long 转短字符串 为 62 进制
     *
     * @param num 数字
     * @return 短字符串 string
     * @since 1.0.0
     */
    @NotNull
    @Contract(pure = true)
    public static String to62String(long num) {
        return NumberUtils.to62String(num);
    }

    /**
     * 将集合拼接成字符串,默认使用`,`拼接
     *
     * @param coll the {@code Collection} to convert
     * @return the delimited {@code String}
     * @since 1.0.0
     */
    @NotNull
    public static String join(Collection<?> coll) {
        return StringUtils.join(coll);
    }

    /**
     * 将集合拼接成字符串,默认指定分隔符
     *
     * @param coll  the {@code Collection} to convert
     * @param delim the delimiter to use (typically a ",")
     * @return the delimited {@code String}
     * @since 1.0.0
     */
    @NotNull
    public static String join(Collection<?> coll, String delim) {
        return StringUtils.join(coll, delim);
    }

    /**
     * 将数组拼接成字符串,默认使用`,`拼接
     *
     * @param arr the array to display
     * @return the delimited {@code String}
     * @since 1.0.0
     */
    @NotNull
    @Contract(pure = true)
    public static String join(Object[] arr) {
        return StringUtils.join(arr);
    }

    /**
     * 将数组拼接成字符串,默认指定分隔符
     *
     * @param arr   the array to display
     * @param delim the delimiter to use (typically a ",")
     * @return the delimited {@code String}
     * @since 1.0.0
     */
    @NotNull
    @Contract(pure = true)
    public static String join(Object[] arr, String delim) {
        return StringUtils.join(arr, delim);
    }

    /**
     * 分割 字符串
     *
     * @param str       字符串
     * @param delimiter 分割符
     * @return 字符串数组 string [ ]
     * @since 1.0.0
     */
    @NotNull
    public static String[] split(@Nullable String str, @Nullable String delimiter) {
        return StringUtils.delimitedListToStringArray(str, delimiter);
    }

    /**
     * 分割 字符串 删除常见 空白符
     *
     * @param str       字符串
     * @param delimiter 分割符
     * @return 字符串数组 string [ ]
     * @since 1.0.0
     */
    @NotNull
    public static String[] splitTrim(@Nullable String str, @Nullable String delimiter) {
        return StringUtils.splitTrim(str, delimiter).toArray(new String[0]);
    }

    /**
     * 字符串是否符合指定的 表达式
     * <p>
     * pattern styles: "xxx*", "*xxx", "*xxx*" and "xxx*yyy"
     * </p>
     *
     * @param pattern 表达式
     * @param str     字符串
     * @return 是否匹配 boolean
     * @since 1.0.0
     */
    @Contract("_, null -> false; null, !null -> false")
    public static boolean simpleMatch(@Nullable String pattern, @Nullable String str) {
        return PatternMatchUtils.simpleMatch(pattern, str);
    }

    /**
     * 字符串是否符合指定的 表达式
     * <p>
     * pattern styles: "xxx*", "*xxx", "*xxx*" and "xxx*yyy"
     * </p>
     *
     * @param patterns 表达式 数组
     * @param str      字符串
     * @return 是否匹配 boolean
     * @since 1.0.0
     */
    @Contract("null, _ -> false")
    public static boolean simpleMatch(@Nullable String[] patterns, String str) {
        return PatternMatchUtils.simpleMatch(patterns, str);
    }

    /**
     * 生成uuid
     *
     * @return UUID string
     * @since 1.0.0
     */
    @NotNull
    public static String randomUid() {
        return StringUtils.randomUid();
    }

    /**
     * 转义HTML用于安全过滤
     *
     * @param html html
     * @return {String}
     * @since 1.0.0
     */
    @NotNull
    public static String escapeHtml(String html) {
        return StringUtils.escapeHtml(html);
    }

    /**
     * 随机数生成
     *
     * @param count 字符长度
     * @return 随机数 string
     * @since 1.0.0
     */
    @NotNull
    public static String random(int count) {
        return RandomUtils.random(count);
    }

    /**
     * 随机数生成
     *
     * @param count      字符长度
     * @param randomType 随机数类别
     * @return 随机数 string
     * @since 1.0.0
     */
    @NotNull
    public static String random(int count, RandomType randomType) {
        return RandomUtils.random(count, randomType);
    }

    /**
     * Calculates the MD5 digest.
     *
     * @param data Data to digest
     * @return MD5 digest as a hex array
     * @since 1.0.0
     */
    public static byte[] md5(byte[] data) {
        return DigestUtils.md5(data);
    }

    /**
     * Calculates the MD5 digest.
     *
     * @param data Data to digest
     * @return MD5 digest as a hex array
     * @since 1.0.0
     */
    public static byte[] md5(String data) {
        return DigestUtils.md5(data);
    }


    /**
     * Calculates the MD5 digest and returns the value as a 32 character hex string.
     *
     * @param data Data to digest
     * @return MD5 digest as a hex string
     * @since 1.0.0
     */
    @NotNull
    public static String md5Hex(String data) {
        return DigestUtils.md5Hex(data);
    }

    /**
     * Return a hexadecimal string representation of the MD5 digest of the given bytes.
     *
     * @param bytes the bytes to calculate the digest over
     * @return a hexadecimal digest string
     * @since 1.0.0
     */
    @NotNull
    public static String md5Hex(byte[] bytes) {
        return DigestUtils.md5Hex(bytes);
    }

    /**
     * sha1
     *
     * @param data Data to digest
     * @return digest as a hex array
     * @since 1.0.0
     */
    public static byte[] sha1(@NotNull String data) {
        return DigestUtils.sha1(data.getBytes(Charsets.UTF_8));
    }

    /**
     * sha1Hex
     *
     * @param data Data to digest
     * @return digest as a hex string
     * @since 1.0.0
     */
    @NotNull
    public static String sha1Hex(@NotNull String data) {
        return DigestUtils.encodeHex(sha1(data.getBytes(Charsets.UTF_8)));
    }

    /**
     * sha1
     *
     * @param bytes Data to digest
     * @return digest as a hex array
     * @since 1.0.0
     */
    public static byte[] sha1(byte[] bytes) {
        return DigestUtils.digest("SHA-1", bytes);
    }

    /**
     * sha1Hex
     *
     * @param bytes Data to digest
     * @return digest as a hex string
     * @since 1.0.0
     */
    @NotNull
    public static String sha1Hex(byte[] bytes) {
        return DigestUtils.encodeHex(sha1(bytes));
    }

    /**
     * SHA224
     *
     * @param data Data to digest
     * @return digest as a byte array
     * @since 1.0.0
     */
    public static byte[] sha224(@NotNull String data) {
        return DigestUtils.sha224(data.getBytes(Charsets.UTF_8));
    }

    /**
     * SHA224Hex
     *
     * @param data Data to digest
     * @return digest as a hex string
     * @since 1.0.0
     */
    @NotNull
    public static String sha224Hex(@NotNull String data) {
        return DigestUtils.encodeHex(sha224(data.getBytes(Charsets.UTF_8)));
    }

    /**
     * SHA224
     *
     * @param bytes Data to digest
     * @return digest as a byte array
     * @since 1.0.0
     */
    public static byte[] sha224(byte[] bytes) {
        return DigestUtils.digest("SHA-224", bytes);
    }

    /**
     * SHA224Hex
     *
     * @param bytes Data to digest
     * @return digest as a hex string
     * @since 1.0.0
     */
    @NotNull
    public static String sha224Hex(byte[] bytes) {
        return DigestUtils.encodeHex(sha224(bytes));
    }

    /**
     * sha256Hex
     *
     * @param data Data to digest
     * @return digest as a byte array
     * @since 1.0.0
     */
    public static byte[] sha256(@NotNull String data) {
        return DigestUtils.sha256(data.getBytes(Charsets.UTF_8));
    }

    /**
     * sha256Hex
     *
     * @param data Data to digest
     * @return digest as a hex string
     * @since 1.0.0
     */
    @NotNull
    public static String sha256Hex(@NotNull String data) {
        return DigestUtils.encodeHex(sha256(data.getBytes(Charsets.UTF_8)));
    }

    /**
     * sha256Hex
     *
     * @param bytes Data to digest
     * @return digest as a byte array
     * @since 1.0.0
     */
    public static byte[] sha256(byte[] bytes) {
        return DigestUtils.digest("SHA-256", bytes);
    }

    /**
     * sha256Hex
     *
     * @param bytes Data to digest
     * @return digest as a hex string
     * @since 1.0.0
     */
    @NotNull
    public static String sha256Hex(byte[] bytes) {
        return DigestUtils.encodeHex(sha256(bytes));
    }

    /**
     * sha384
     *
     * @param data Data to digest
     * @return digest as a byte array
     * @since 1.0.0
     */
    public static byte[] sha384(@NotNull String data) {
        return DigestUtils.sha384(data.getBytes(Charsets.UTF_8));
    }

    /**
     * sha384Hex
     *
     * @param data Data to digest
     * @return digest as a hex string
     * @since 1.0.0
     */
    @NotNull
    public static String sha384Hex(@NotNull String data) {
        return DigestUtils.encodeHex(sha384(data.getBytes(Charsets.UTF_8)));
    }

    /**
     * sha384
     *
     * @param bytes Data to digest
     * @return digest as a byte array
     * @since 1.0.0
     */
    public static byte[] sha384(byte[] bytes) {
        return DigestUtils.digest("SHA-384", bytes);
    }

    /**
     * sha384Hex
     *
     * @param bytes Data to digest
     * @return digest as a hex string
     * @since 1.0.0
     */
    @NotNull
    public static String sha384Hex(byte[] bytes) {
        return DigestUtils.encodeHex(sha384(bytes));
    }

    /**
     * sha512Hex
     *
     * @param data Data to digest
     * @return digest as a byte array
     * @since 1.0.0
     */
    public static byte[] sha512(@NotNull String data) {
        return DigestUtils.sha512(data.getBytes(Charsets.UTF_8));
    }

    /**
     * sha512Hex
     *
     * @param data Data to digest
     * @return digest as a hex string
     * @since 1.0.0
     */
    @NotNull
    public static String sha512Hex(@NotNull String data) {
        return DigestUtils.encodeHex(sha512(data.getBytes(Charsets.UTF_8)));
    }

    /**
     * sha512Hex
     *
     * @param bytes Data to digest
     * @return digest as a byte array
     * @since 1.0.0
     */
    public static byte[] sha512(byte[] bytes) {
        return DigestUtils.digest("SHA-512", bytes);
    }

    /**
     * sha512Hex
     *
     * @param bytes Data to digest
     * @return digest as a hex string
     * @since 1.0.0
     */
    @NotNull
    public static String sha512Hex(byte[] bytes) {
        return DigestUtils.encodeHex(sha512(bytes));
    }

    /**
     * digest Hex
     *
     * @param algorithm 算法
     * @param bytes     Data to digest
     * @return digest as a hex string
     * @since 1.0.0
     */
    @NotNull
    public static String digestHex(String algorithm, byte[] bytes) {
        return DigestUtils.encodeHex(digest(algorithm, bytes));
    }

    /**
     * digest
     *
     * @param algorithm 算法
     * @param bytes     Data to digest
     * @return digest byte array
     * @since 1.0.0
     */
    public static byte[] digest(String algorithm, byte[] bytes) {
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            return md.digest(bytes);
        } catch (NoSuchAlgorithmException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * hmacMd5
     *
     * @param data Data to digest
     * @param key  key
     * @return digest as a byte array
     * @since 1.0.0
     */
    public static byte[] hmacMd5(@NotNull String data, String key) {
        return DigestUtils.hmacMd5(data.getBytes(Charsets.UTF_8), key);
    }

    /**
     * hmacMd5 Hex
     *
     * @param data Data to digest
     * @param key  key
     * @return digest as a hex string
     * @since 1.0.0
     */
    @NotNull
    public static String hmacMd5Hex(@NotNull String data, String key) {
        return DigestUtils.encodeHex(hmacMd5(data.getBytes(Charsets.UTF_8), key));
    }

    /**
     * hmacMd5
     *
     * @param bytes Data to digest
     * @param key   key
     * @return digest as a byte array
     * @since 1.0.0
     */
    public static byte[] hmacMd5(byte[] bytes, String key) {
        return DigestUtils.digestHmac("HmacMD5", bytes, key);
    }

    /**
     * hmacMd5 Hex
     *
     * @param bytes Data to digest
     * @param key   key
     * @return digest as a hex string
     * @since 1.0.0
     */
    @NotNull
    public static String hmacMd5Hex(byte[] bytes, String key) {
        return DigestUtils.encodeHex(hmacMd5(bytes, key));
    }

    /**
     * hmacSha1
     *
     * @param data Data to digest
     * @param key  key
     * @return digest as a byte array
     * @since 1.0.0
     */
    public static byte[] hmacSha1(@NotNull String data, String key) {
        return DigestUtils.hmacSha1(data.getBytes(Charsets.UTF_8), key);
    }

    /**
     * hmacSha1 Hex
     *
     * @param data Data to digest
     * @param key  key
     * @return digest as a hex string
     * @since 1.0.0
     */
    @NotNull
    public static String hmacSha1Hex(@NotNull String data, String key) {
        return DigestUtils.encodeHex(hmacSha1(data.getBytes(Charsets.UTF_8), key));
    }

    /**
     * hmacSha1
     *
     * @param bytes Data to digest
     * @param key   key
     * @return digest as a byte array
     * @since 1.0.0
     */
    public static byte[] hmacSha1(byte[] bytes, String key) {
        return DigestUtils.digestHmac("HmacSHA1", bytes, key);
    }

    /**
     * hmacSha1 Hex
     *
     * @param bytes Data to digest
     * @param key   key
     * @return digest as a hex string
     * @since 1.0.0
     */
    @NotNull
    public static String hmacSha1Hex(byte[] bytes, String key) {
        return DigestUtils.encodeHex(hmacSha1(bytes, key));
    }

    /**
     * hmacSha224
     *
     * @param data Data to digest
     * @param key  key
     * @return digest as a hex string
     * @since 1.0.0
     */
    public static byte[] hmacSha224(@NotNull String data, String key) {
        return DigestUtils.hmacSha224(data.getBytes(Charsets.UTF_8), key);
    }

    /**
     * hmacSha224 Hex
     *
     * @param data Data to digest
     * @param key  key
     * @return digest as a hex string
     * @since 1.0.0
     */
    @NotNull
    public static String hmacSha224Hex(@NotNull String data, String key) {
        return DigestUtils.encodeHex(hmacSha224(data.getBytes(Charsets.UTF_8), key));
    }

    /**
     * hmacSha224
     *
     * @param bytes Data to digest
     * @param key   key
     * @return digest as a hex string
     * @since 1.0.0
     */
    public static byte[] hmacSha224(byte[] bytes, String key) {
        return DigestUtils.digestHmac("HmacSHA224", bytes, key);
    }

    /**
     * hmacSha224 Hex
     *
     * @param bytes Data to digest
     * @param key   key
     * @return digest as a hex string
     * @since 1.0.0
     */
    @NotNull
    public static String hmacSha224Hex(byte[] bytes, String key) {
        return DigestUtils.encodeHex(hmacSha224(bytes, key));
    }

    /**
     * hmacSha256
     *
     * @param data Data to digest
     * @param key  key
     * @return digest as a hex string
     * @since 1.0.0
     */
    public static byte[] hmacSha256(@NotNull String data, String key) {
        return DigestUtils.hmacSha256(data.getBytes(Charsets.UTF_8), key);
    }

    /**
     * hmacSha256 Hex
     *
     * @param data Data to digest
     * @param key  key
     * @return digest as a byte array
     * @since 1.0.0
     */
    @NotNull
    public static String hmacSha256Hex(@NotNull String data, String key) {
        return DigestUtils.encodeHex(hmacSha256(data.getBytes(Charsets.UTF_8), key));
    }

    /**
     * hmacSha256
     *
     * @param bytes Data to digest
     * @param key   key
     * @return digest as a byte array
     * @since 1.0.0
     */
    public static byte[] hmacSha256(byte[] bytes, String key) {
        return DigestUtils.digestHmac("HmacSHA256", bytes, key);
    }

    /**
     * hmacSha256 Hex
     *
     * @param bytes Data to digest
     * @param key   key
     * @return digest as a hex string
     * @since 1.0.0
     */
    @NotNull
    public static String hmacSha256Hex(byte[] bytes, String key) {
        return DigestUtils.encodeHex(hmacSha256(bytes, key));
    }

    /**
     * hmacSha384
     *
     * @param data Data to digest
     * @param key  key
     * @return digest as a byte array
     * @since 1.0.0
     */
    public static byte[] hmacSha384(@NotNull String data, String key) {
        return DigestUtils.hmacSha384(data.getBytes(Charsets.UTF_8), key);
    }

    /**
     * hmacSha384 Hex
     *
     * @param data Data to digest
     * @param key  key
     * @return digest as a hex string
     * @since 1.0.0
     */
    @NotNull
    public static String hmacSha384Hex(@NotNull String data, String key) {
        return DigestUtils.encodeHex(hmacSha384(data.getBytes(Charsets.UTF_8), key));
    }

    /**
     * hmacSha384
     *
     * @param bytes Data to digest
     * @param key   key
     * @return digest as a byte array
     * @since 1.0.0
     */
    public static byte[] hmacSha384(byte[] bytes, String key) {
        return DigestUtils.digestHmac("HmacSHA384", bytes, key);
    }

    /**
     * hmacSha384 Hex
     *
     * @param bytes Data to digest
     * @param key   key
     * @return digest as a hex string
     * @since 1.0.0
     */
    @NotNull
    public static String hmacSha384Hex(byte[] bytes, String key) {
        return DigestUtils.encodeHex(hmacSha384(bytes, key));
    }

    /**
     * hmacSha512
     *
     * @param data Data to digest
     * @param key  key
     * @return digest as a byte array
     * @since 1.0.0
     */
    public static byte[] hmacSha512(@NotNull String data, String key) {
        return DigestUtils.hmacSha512(data.getBytes(Charsets.UTF_8), key);
    }

    /**
     * hmacSha512 Hex
     *
     * @param data Data to digest
     * @param key  key
     * @return digest as a hex string
     * @since 1.0.0
     */
    @NotNull
    public static String hmacSha512Hex(@NotNull String data, String key) {
        return DigestUtils.encodeHex(hmacSha512(data.getBytes(Charsets.UTF_8), key));
    }

    /**
     * hmacSha512
     *
     * @param bytes Data to digest
     * @param key   key
     * @return digest as a byte array
     * @since 1.0.0
     */
    public static byte[] hmacSha512(byte[] bytes, String key) {
        return DigestUtils.digestHmac("HmacSHA512", bytes, key);
    }

    /**
     * hmacSha512 Hex
     *
     * @param bytes Data to digest
     * @param key   key
     * @return digest as a hex string
     * @since 1.0.0
     */
    @NotNull
    public static String hmacSha512Hex(byte[] bytes, String key) {
        return DigestUtils.encodeHex(hmacSha512(bytes, key));
    }

    /**
     * digest Hmac Hex
     *
     * @param algorithm 算法
     * @param bytes     Data to digest
     * @param key       key
     * @return digest as a hex string
     * @since 1.0.0
     */
    @NotNull
    public static String digestHmacHex(String algorithm, byte[] bytes, String key) {
        return DigestUtils.encodeHex(DigestUtils.digestHmac(algorithm, bytes, key));
    }

    /**
     * digest Hmac
     *
     * @param algorithm 算法
     * @param bytes     Data to digest
     * @param key       key
     * @return digest as a byte array
     * @since 1.0.0
     */
    public static byte[] digestHmac(String algorithm, byte[] bytes, @NotNull String key) {
        return DigestUtils.digestHmac(algorithm, bytes, key);
    }

    /**
     * byte 数组序列化成 hex
     *
     * @param bytes bytes to encode
     * @return MD5 digest as a hex string
     * @since 1.0.0
     */
    @NotNull
    public static String encodeHex(byte[] bytes) {
        return DigestUtils.encodeHex(sha1(bytes));
    }

    /**
     * 字符串反序列化成 hex
     *
     * @param hexStr String to decode
     * @return MD5 digest as a hex string
     * @since 1.0.0
     */
    public static byte[] decodeHex(@NotNull String hexStr) {
        return DigestUtils.decodeHex(hexStr);
    }

    /**
     * 自定义加密 先MD5再SHA1
     *
     * @param data 字符串
     * @return String string
     * @since 1.0.0
     */
    @NotNull
    public static String encrypt(String data) {
        return DigestUtils.encrypt(data);
    }

    /**
     * 编码
     *
     * @param value 字符串
     * @return {String}
     * @since 1.0.0
     */
    @NotNull
    public static String encodeBase64(String value) {
        return Base64Utils.encode(value);
    }

    /**
     * 编码
     *
     * @param value   字符串
     * @param charset 字符集
     * @return {String}
     * @since 1.0.0
     */
    @NotNull
    public static String encodeBase64(String value, Charset charset) {
        return Base64Utils.encode(value, charset);
    }

    /**
     * 编码URL安全
     *
     * @param value 字符串
     * @return {String}
     * @since 1.0.0
     */
    @NotNull
    public static String encodeBase64UrlSafe(String value) {
        return Base64Utils.encodeUrlSafe(value);
    }

    /**
     * 编码URL安全
     *
     * @param value   字符串
     * @param charset 字符集
     * @return {String}
     * @since 1.0.0
     */
    @NotNull
    public static String encodeBase64UrlSafe(String value, Charset charset) {
        return Base64Utils.encodeUrlSafe(value, charset);
    }

    /**
     * 解码
     *
     * @param value 字符串
     * @return {String}
     * @since 1.0.0
     */
    @NotNull
    public static String decodeBase64(String value) {
        return Base64Utils.decode(value);
    }

    /**
     * 解码
     *
     * @param value   字符串
     * @param charset 字符集
     * @return {String}
     * @since 1.0.0
     */
    @NotNull
    public static String decodeBase64(String value, Charset charset) {
        return Base64Utils.decode(value, charset);
    }

    /**
     * 解码URL安全
     *
     * @param value 字符串
     * @return {String}
     * @since 1.0.0
     */
    @NotNull
    public static String decodeBase64UrlSafe(String value) {
        return Base64Utils.decodeUrlSafe(value);
    }

    /**
     * 解码URL安全
     *
     * @param value   字符串
     * @param charset 字符集
     * @return {String}
     * @since 1.0.0
     */
    @NotNull
    public static String decodeBase64UrlSafe(String value, Charset charset) {
        return Base64Utils.decodeUrlSafe(value, charset);
    }

    /**
     * closeQuietly
     *
     * @param closeable 自动关闭
     * @since 1.0.0
     */
    public static void closeQuietly(@Nullable Closeable closeable) {
        IoUtils.closeQuietly(closeable);
    }

    /**
     * InputStream to String utf-8
     *
     * @param input the <code>InputStream</code> to read from
     * @return the requested String
     * @throws NullPointerException if the input is null
     * @since 1.0.0
     */
    @NotNull
    public static String toString(InputStream input) {
        return IoUtils.toString(input);
    }

    /**
     * InputStream to String
     *
     * @param input   the <code>InputStream</code> to read from
     * @param charset the <code>Charsets</code>
     * @return the requested String
     * @throws NullPointerException if the input is null
     * @since 1.0.0
     */
    @NotNull
    public static String toString(@Nullable InputStream input, Charset charset) {
        return IoUtils.toString(input, charset);
    }

    /**
     * InputStream to bytes 数组
     *
     * @param input InputStream
     * @return the requested byte array
     * @since 1.0.0
     */
    @NotNull
    public static byte[] toByteArray(@Nullable InputStream input) {
        return IoUtils.toByteArray(input);
    }

    /**
     * 读取文件为字符串
     *
     * @param file the file to read, must not be {@code null}
     * @return the file contents, never {@code null}
     * @since 1.0.0
     */
    @NotNull
    public static String toString(File file) {
        return FileUtils.readToString(file);
    }

    /**
     * 读取文件为字符串
     *
     * @param file     the file to read, must not be {@code null}
     * @param encoding the encoding to use, {@code null} means platform default
     * @return the file contents, never {@code null}
     * @since 1.0.0
     */
    @NotNull
    public static String toString(File file, Charset encoding) {
        return FileUtils.readToString(file, encoding);
    }

    /**
     * 读取文件为 byte 数组
     *
     * @param file the file to read, must not be {@code null}
     * @return the file contents, never {@code null}
     * @since 1.0.0
     */
    public static byte[] toByteArray(File file) {
        return FileUtils.readToByteArray(file);
    }


    /**
     * 拼接临时文件目录.
     *
     * @param subDirFile sub dir file
     * @return 临时文件目录. string
     * @since 1.0.0
     */
    @NotNull
    public static String toTempDirPath(String subDirFile) {
        return FileUtils.toTempDirPath(subDirFile);
    }

    /**
     * Returns a {@link File} representing the system temporary directory.
     *
     * @return the system temporary directory.
     * @since 1.0.0
     */
    @NotNull
    public static File getTempDir() {
        return FileUtils.getTempDir();
    }

    /**
     * 拼接临时文件目录.
     *
     * @param subDirFile sub dir file
     * @return 临时文件目录. file
     * @since 1.0.0
     */
    public static @NotNull File toTempDir(String subDirFile) {
        return FileUtils.toTempDir(subDirFile);
    }

    /**
     * 获取资源,注意: boot 中请不要使用 Resource getFile,应该使用 getInputStream,支持一下协议:
     * <p>
     * 1. classpath:
     * 2. file:
     * 3. ftp:
     * 4. http: and https:
     * 6. C:/dir1/ and /Users/lcm
     * </p>
     *
     * @param resourceLocation 资源路径
     * @return Resource resource
     * @throws IOException io异常
     * @since 1.0.0
     */
    @NotNull
    public static Resource getResource(String resourceLocation) throws IOException {
        return ResourceUtils.getResource(resourceLocation);
    }

    /**
     * 将对象序列化成json字符串
     *
     * @param object javaBean
     * @return jsonString json字符串
     * @since 1.0.0
     */
    @NotNull
    public static String toJson(Object object) {
        return Jsons.toJson(object);
    }

    /**
     * 将对象序列化成 json byte 数组
     *
     * @param object javaBean
     * @return jsonString json字符串
     * @since 1.0.0
     */
    public static byte[] toJsonAsBytes(Object object) {
        return Jsons.toJsonAsBytes(object);
    }

    /**
     * 将json字符串转成 JsonNode
     *
     * @param jsonString jsonString
     * @return jsonString json字符串
     * @since 1.0.0
     */
    @NotNull
    public static JsonNode readTree(String jsonString) {
        return Jsons.readTree(jsonString);
    }

    /**
     * 将json字符串转成 JsonNode
     *
     * @param in InputStream
     * @return jsonString json字符串
     * @since 1.0.0
     */
    @NotNull
    public static JsonNode readTree(InputStream in) {
        return Jsons.readTree(in);
    }

    /**
     * 将json字符串转成 JsonNode
     *
     * @param content content
     * @return jsonString json字符串
     * @since 1.0.0
     */
    @NotNull
    public static JsonNode readTree(byte[] content) {
        return Jsons.readTree(content);
    }

    /**
     * 将json字符串转成 JsonNode
     *
     * @param jsonParser JsonParser
     * @return jsonString json字符串
     * @since 1.0.0
     */
    @NotNull
    public static JsonNode readTree(JsonParser jsonParser) {
        return Jsons.readTree(jsonParser);
    }

    /**
     * 将json byte 数组反序列化成对象
     *
     * @param <T>       T 泛型标记
     * @param bytes     json bytes
     * @param valueType class
     * @return Bean t
     * @since 1.0.0
     */
    @Nullable
    public static <T> T parse(byte[] bytes, Class<T> valueType) {
        return Jsons.parse(bytes, valueType);
    }

    /**
     * 将json反序列化成对象
     *
     * @param <T>        T 泛型标记
     * @param jsonString jsonString
     * @param valueType  class
     * @return Bean t
     * @since 1.0.0
     */
    @NotNull
    public static <T> T parse(String jsonString, Class<T> valueType) {
        return Jsons.parse(jsonString, valueType);
    }

    /**
     * 将json反序列化成对象
     *
     * @param <T>       T 泛型标记
     * @param in        InputStream
     * @param valueType class
     * @return Bean t
     * @since 1.0.0
     */
    @NotNull
    public static <T> T parse(InputStream in, Class<T> valueType) {
        return Jsons.parse(in, valueType);
    }

    /**
     * 将json反序列化成对象
     *
     * @param <T>           T 泛型标记
     * @param bytes         bytes
     * @param typeReference 泛型类型
     * @return Bean t
     * @since 1.0.0
     */
    @NotNull
    public static <T> T parse(byte[] bytes, TypeReference<T> typeReference) {
        return Jsons.parse(bytes, typeReference);
    }

    /**
     * 将json反序列化成对象
     *
     * @param <T>           T 泛型标记
     * @param jsonString    jsonString
     * @param typeReference 泛型类型
     * @return Bean t
     * @since 1.0.0
     */
    @NotNull
    public static <T> T parse(String jsonString, TypeReference<T> typeReference) {
        return Jsons.parse(jsonString, typeReference);
    }

    /**
     * 将json反序列化成对象
     *
     * @param <T>           T 泛型标记
     * @param in            InputStream
     * @param typeReference 泛型类型
     * @return Bean t
     * @since 1.0.0
     */
    @NotNull
    public static <T> T parse(InputStream in, TypeReference<T> typeReference) {
        return Jsons.parse(in, typeReference);
    }

    /**
     * 读取集合
     *
     * @param <T>          泛型
     * @param content      bytes
     * @param elementClass elementClass
     * @return 集合 list
     * @since 1.0.0
     */
    @NotNull
    public static <T> List<T> toList(@Nullable byte[] content, Class<T> elementClass) {
        return Jsons.toList(content, elementClass);
    }

    /**
     * 读取集合
     *
     * @param <T>          泛型
     * @param content      InputStream
     * @param elementClass elementClass
     * @return 集合 list
     * @since 1.0.0
     */
    @NotNull
    public static <T> List<T> toList(@Nullable InputStream content, Class<T> elementClass) {
        return Jsons.toList(content, elementClass);
    }

    /**
     * 读取集合
     *
     * @param <T>          泛型
     * @param content      bytes
     * @param elementClass elementClass
     * @return 集合 list
     * @since 1.0.0
     */
    @NotNull
    public static <T> List<T> toList(@Nullable String content, Class<T> elementClass) {
        return Jsons.toList(content, elementClass);
    }

    /**
     * 读取集合
     *
     * @param <K>        泛型
     * @param <V>        泛型
     * @param content    bytes
     * @param keyClass   key类型
     * @param valueClass 值类型
     * @return 集合 map
     * @since 1.0.0
     */
    @NotNull
    public static <K, V> Map<K, V> toMap(@Nullable byte[] content, Class<?> keyClass, Class<?> valueClass) {
        return Jsons.toMap(content, keyClass, valueClass);
    }

    /**
     * 读取集合
     *
     * @param <K>        泛型
     * @param <V>        泛型
     * @param content    InputStream
     * @param keyClass   key类型
     * @param valueClass 值类型
     * @return 集合 map
     * @since 1.0.0
     */
    @NotNull
    public static <K, V> Map<K, V> toMap(@Nullable InputStream content, Class<?> keyClass, Class<?> valueClass) {
        return Jsons.toMap(content, keyClass, valueClass);
    }

    /**
     * 读取集合
     *
     * @param <K>        泛型
     * @param <V>        泛型
     * @param content    bytes
     * @param keyClass   key类型
     * @param valueClass 值类型
     * @return 集合 map
     * @since 1.0.0
     */
    @NotNull
    public static <K, V> Map<K, V> toMap(@Nullable String content, Class<?> keyClass, Class<?> valueClass) {
        return Jsons.toMap(content, keyClass, valueClass);
    }


    /**
     * Encode all characters that are either illegal, or have any reserved
     * meaning, anywhere within a URI, as defined in
     * <a href="https://tools.ietf.org/html/rfc3986">RFC 3986</a>.
     * This is useful to ensure that the given String will be preserved as-is
     * and will not have any o impact on the structure or meaning of the URI.
     *
     * @param source the String to be encoded
     * @return the encoded String
     * @since 1.0.0
     */
    @NotNull
    public static String encode(String source) {
        return UrlUtils.encode(source, Charsets.UTF_8);
    }

    /**
     * Encode all characters that are either illegal, or have any reserved
     * meaning, anywhere within a URI, as defined in
     * <a href="https://tools.ietf.org/html/rfc3986">RFC 3986</a>.
     * This is useful to ensure that the given String will be preserved as-is
     * and will not have any o impact on the structure or meaning of the URI.
     *
     * @param source  the String to be encoded
     * @param charset the character encoding to encode to
     * @return the encoded String
     * @since 1.0.0
     */
    @NotNull
    public static String encode(String source, Charset charset) {
        return UrlUtils.encode(source, charset);
    }

    /**
     * Decode the given encoded URI component.
     * <p>See {@link org.springframework.util.StringUtils#uriDecode(String, Charset)} for the decoding rules.
     *
     * @param source the encoded String
     * @return the decoded value
     * @throws IllegalArgumentException when the given source contains invalid encoded sequences
     * @see org.springframework.util.StringUtils#uriDecode org.springframework.util.StringUtils#uriDecodeorg.springframework.util
     * .StringUtils#uriDecodeorg.springframework.util .StringUtils#uriDecode
     * @see java.net.URLDecoder#decode java.net.URLDecoder#decodejava.net.URLDecoder#decodejava.net.URLDecoder#decode
     * @since 1.0.0
     */
    @NotNull
    public static String decode(String source) {
        return org.springframework.util.StringUtils.uriDecode(source, Charsets.UTF_8);
    }

    /**
     * Decode the given encoded URI component.
     * <p>See {@link org.springframework.util.StringUtils#uriDecode(String, Charset)} for the decoding rules.
     *
     * @param source  the encoded String
     * @param charset the character encoding to use
     * @return the decoded value
     * @throws IllegalArgumentException when the given source contains invalid encoded sequences
     * @see org.springframework.util.StringUtils#uriDecode org.springframework.util.StringUtils#uriDecodeorg.springframework.util
     * .StringUtils#uriDecodeorg.springframework.util .StringUtils#uriDecode
     * @see java.net.URLDecoder#decode(String, String) java.net.URLDecoder#decode(String, String)java.net.URLDecoder#decode(String,
     * String)java.net.URLDecoder#decode(String, String)
     * @since 1.0.0
     */
    @NotNull
    public static String decode(String source, Charset charset) {
        return org.springframework.util.StringUtils.uriDecode(source, charset);
    }

    /**
     * 日期时间格式化
     *
     * @param date 时间
     * @return 格式化后的时间 string
     * @since 1.0.0
     */
    public static @NotNull String formatDateTime(Date date) {
        return DateUtils.formatDateTime(date);
    }

    /**
     * 日期格式化
     *
     * @param date 时间
     * @return 格式化后的时间 string
     * @since 1.0.0
     */
    public static @NotNull String formatDate(Date date) {
        return DateUtils.formatDate(date);
    }

    /**
     * 时间格式化
     *
     * @param date 时间
     * @return 格式化后的时间 string
     * @since 1.0.0
     */
    public static @NotNull String formatTime(Date date) {
        return DateUtils.formatTime(date);
    }

    /**
     * 日期格式化
     *
     * @param date    时间
     * @param pattern 表达式
     * @return 格式化后的时间 string
     * @since 1.0.0
     */
    public static @NotNull String format(Date date, String pattern) {
        return DateUtils.format(date, pattern);
    }

    /**
     * 将字符串转换为时间
     *
     * @param dateStr 时间字符串
     * @param pattern 表达式
     * @return 时间 date
     * @since 1.0.0
     */
    public static Date parseDate(String dateStr, String pattern) {
        return DateUtils.parse(dateStr, pattern);
    }

    /**
     * 将字符串转换为时间
     *
     * @param dateStr 时间字符串
     * @param format  ConcurrentDateFormat
     * @return 时间 date
     * @since 1.0.0
     */
    public static Date parse(String dateStr, ConcurrentDateFormat format) {
        return DateUtils.parse(dateStr, format);
    }

    /**
     * 日期时间格式化
     *
     * @param temporal 时间
     * @return 格式化后的时间 string
     * @since 1.0.0
     */
    @NotNull
    public static String formatDateTime(TemporalAccessor temporal) {
        return DateTimeUtils.formatDateTime(temporal);
    }

    /**
     * 日期时间格式化
     *
     * @param temporal 时间
     * @return 格式化后的时间 string
     * @since 1.0.0
     */
    @NotNull
    public static String formatDate(TemporalAccessor temporal) {
        return DateTimeUtils.formatDate(temporal);
    }

    /**
     * 时间格式化
     *
     * @param temporal 时间
     * @return 格式化后的时间 string
     * @since 1.0.0
     */
    @NotNull
    public static String formatTime(TemporalAccessor temporal) {
        return DateTimeUtils.formatTime(temporal);
    }

    /**
     * 日期格式化
     *
     * @param temporal 时间
     * @param pattern  表达式
     * @return 格式化后的时间 string
     * @since 1.0.0
     */
    @NotNull
    public static String format(TemporalAccessor temporal, String pattern) {
        return DateTimeUtils.format(temporal, pattern);
    }

    /**
     * 将字符串转换为时间
     *
     * @param dateStr 时间字符串
     * @param pattern 表达式
     * @return 时间 temporal accessor
     * @since 1.0.0
     */
    @NotNull
    public static TemporalAccessor parse(String dateStr, String pattern) {
        return DateTimeUtils.parse(dateStr, pattern);
    }

    /**
     * 将字符串转换为时间
     *
     * @param dateStr   时间字符串
     * @param formatter DateTimeFormatter
     * @return 时间 temporal accessor
     * @since 1.0.0
     */
    @NotNull
    public static TemporalAccessor parse(String dateStr, DateTimeFormatter formatter) {
        return DateTimeUtils.parse(dateStr, formatter);
    }

    /**
     * 对象类型转换
     *
     * @param <T>        泛型标记
     * @param source     the source object
     * @param targetType the target type
     * @return the converted value
     * @throws IllegalArgumentException if targetType is {@code null},or sourceType is {@code null} but source is not {@code null}
     * @since 1.0.0
     */
    @Contract("null, _ -> null")
    @Nullable
    public static <T> T convert(@Nullable Object source, Class<T> targetType) {
        if (source instanceof String) {
            String json = (String) source;
            if (Jsons.isJson(json)) {
                return Jsons.parse(json, targetType);
            }
        }

        return ConvertUtils.convert(source, targetType);
    }

    /**
     * 对象类型转换
     *
     * @param <T>        泛型标记
     * @param source     the source object
     * @param sourceType the source type
     * @param targetType the target type
     * @return the converted value
     * @throws IllegalArgumentException if targetType is {@code null}, or sourceType is {@code null} but source is not {@code null}
     * @since 1.0.0
     */
    @Contract("null, _, _ -> null")
    @Nullable
    public static <T> T convert(@Nullable Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        return ConvertUtils.convert(source, sourceType, targetType);
    }

    /**
     * 对象类型转换
     *
     * @param <T>        泛型标记
     * @param source     the source object
     * @param targetType the target type
     * @return the converted value
     * @throws IllegalArgumentException if targetType is {@code null}, or sourceType is {@code null} but source is not {@code null}
     * @since 1.0.0
     */
    @Contract("null, _ -> null")
    @Nullable
    public static <T> T convert(@Nullable Object source, TypeDescriptor targetType) {
        return ConvertUtils.convert(source, targetType);
    }

    /**
     * 时间比较
     *
     * @param startInclusive the start instant, inclusive, not null
     * @param endExclusive   the end instant, exclusive, not null
     * @return a {@code Duration}, not null
     * @since 1.0.0
     */
    public static Duration between(Temporal startInclusive, Temporal endExclusive) {
        return Duration.between(startInclusive, endExclusive);
    }

    /**
     * 比较2个 时间差
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return 时间间隔 duration
     * @since 1.0.0
     */
    public static Duration between(Date startDate, Date endDate) {
        return DateUtils.between(startDate, endDate);
    }

    /**
     * 获取方法参数信息
     *
     * @param constructor    构造器
     * @param parameterIndex 参数序号
     * @return {MethodParameter}
     * @since 1.0.0
     */
    public static @NotNull MethodParameter getMethodParameter(Constructor<?> constructor, int parameterIndex) {
        return ClassUtils.getMethodParameter(constructor, parameterIndex);
    }

    /**
     * 获取方法参数信息
     *
     * @param method         方法
     * @param parameterIndex 参数序号
     * @return {MethodParameter}
     * @since 1.0.0
     */
    public static MethodParameter getMethodParameter(Method method, int parameterIndex) {
        return ClassUtils.getMethodParameter(method, parameterIndex);
    }

    /**
     * 获取Annotation
     *
     * @param <A>              泛型标记
     * @param annotatedElement AnnotatedElement
     * @param annotationType   注解类
     * @return {Annotation}
     * @since 1.0.0
     */
    @Nullable
    public static <A extends Annotation> A getAnnotation(AnnotatedElement annotatedElement, Class<A> annotationType) {
        return AnnotatedElementUtils.findMergedAnnotation(annotatedElement, annotationType);
    }

    /**
     * 获取Annotation, 先找方法,没有则再找方法上的类
     *
     * @param <A>            泛型标记
     * @param method         Method
     * @param annotationType 注解类
     * @return {Annotation}
     * @since 1.0.0
     */
    @Nullable
    public static <A extends Annotation> A getAnnotation(Method method, Class<A> annotationType) {
        return ClassUtils.getAnnotation(method, annotationType);
    }

    /**
     * 获取Annotation, 先找HandlerMethod,没有则再找对应的类
     *
     * @param <A>            泛型标记
     * @param handlerMethod  HandlerMethod
     * @param annotationType 注解类
     * @return {Annotation}
     * @since 1.0.0
     */
    @Nullable
    public static <A extends Annotation> A getAnnotation(HandlerMethod handlerMethod, Class<A> annotationType) {
        return ClassUtils.getAnnotation(handlerMethod, annotationType);
    }

    /**
     * 实例化对象
     *
     * @param <T>   泛型标记
     * @param clazz 类
     * @return 对象 t
     * @since 1.0.0
     */
    @NotNull
    @SuppressWarnings("unchecked")
    public static <T> T newInstance(Class<?> clazz) {
        return (T) BeanUtils.instantiateClass(clazz);
    }

    /**
     * 实例化对象
     *
     * @param <T>      泛型标记
     * @param clazzStr 类名
     * @return 对象 t
     * @since 1.0.0
     */
    @NotNull
    public static <T> T newInstance(String clazzStr) {
        return BeanUtils.newInstance(clazzStr);
    }

    /**
     * 获取Bean的属性
     *
     * @param bean         bean
     * @param propertyName 属性名
     * @return 属性值 property
     * @since 1.0.0
     */
    public static Object getProperty(Object bean, String propertyName) {
        return BeanUtils.getProperty(bean, propertyName);
    }

    /**
     * 设置Bean属性
     *
     * @param bean         bean
     * @param propertyName 属性名
     * @param value        属性值
     * @since 1.0.0
     */
    public static void setProperty(Object bean, String propertyName, Object value) {
        BeanUtils.setProperty(bean, propertyName, value);
    }

    /**
     * 深复制
     * <p>
     * 注意: 不支持链式Bean
     *
     * @param <T>    泛型标记
     * @param source 源对象
     * @return T t
     * @since 1.0.0
     */
    public static <T> T clone(T source) {
        return BeanUtils.clone(source);
    }

    /**
     * copy 对象属性到另一个对象,默认不使用Convert
     * <p>
     * 注意: 不支持链式Bean,链式用 copyProperties
     *
     * @param <T>    泛型标记
     * @param source 源对象
     * @param clazz  类名
     * @return T t
     * @since 1.0.0
     */
    @Contract("null, _ -> null")
    public static <T> T copy(Object source, Class<T> clazz) {
        return BeanUtils.copy(source, clazz);
    }

    /**
     * 拷贝对象
     * <p>
     * 注意: 不支持链式Bean,链式用 copyProperties
     *
     * @param source     源对象
     * @param targetBean 需要赋值的对象
     * @since 1.0.0
     */
    public static void copy(Object source, Object targetBean) {
        BeanUtils.copy(source, targetBean);
    }

    /**
     * 拷贝列表对象,并对不同类型属性进行转换
     * <p>
     * 支持 map bean copy
     * </p>
     *
     * @param <T>         泛型标记
     * @param sourceList  源对象列表
     * @param targetClazz 转换成的类
     * @return List list
     * @since 1.0.0
     */
    @Contract("null, _ -> !null")
    public static <T> @NotNull List<T> copyWithConvert(@Nullable Collection<?> sourceList, Class<T> targetClazz) {
        return BeanUtils.copyWithConvert(sourceList, targetClazz);
    }

    /**
     * Copy the property values of the given source bean into the target class.
     * <p>Note: The source and target classes do not have to match or even be derived
     * from each other, as long as the properties match. Any bean properties that the
     * source bean exposes but the target bean does not will silently be ignored.
     * <p>This is just a convenience method. For more complex transfer needs,
     *
     * @param <T>    泛型标记
     * @param source the source bean
     * @param clazz  the target bean class
     * @return T t
     * @throws BeansException if the copying failed
     * @since 1.0.0
     */
    @Contract("null, _ -> null")
    public static <T> T copyProperties(Object source, Class<T> clazz) throws BeansException {
        return BeanUtils.copyProperties(source, clazz);
    }

    /**
     * 将对象装成map形式
     *
     * @param bean 源对象
     * @return {Map}
     * @since 1.0.0
     */
    @Contract("null -> new")
    public static Map<String, Object> toMap(Object bean) {
        return BeanUtils.toMap(bean);
    }

    /**
     * 将map 转为 bean
     *
     * @param <T>       泛型标记
     * @param beanMap   map
     * @param valueType 对象类型
     * @return {T}
     * @since 1.0.0
     */
    public static <T> @NotNull T toBean(Map<String, Object> beanMap, Class<T> valueType) {
        return BeanUtils.toBean(beanMap, valueType);
    }

    /**
     * 初始化 map 容量
     *
     * @param expectedSize the expected size
     * @return the int
     * @since 1.0.0
     */
    @Contract(pure = true)
    @SuppressWarnings("PMD.UndefineMagicConstantRule")
    public static int capacity(int expectedSize) {
        if (expectedSize < 3) {
            if (expectedSize < 0) {
                throw new IllegalArgumentException("expectedSize cannot be negative but was: " + expectedSize);
            }
            return expectedSize + 1;
        }
        if (expectedSize < Ints.MAX_POWER_OF_TWO) {
            // This is the calculation used in JDK8 to resize when a putAll
            // happens; it seems to be the most conservative calculation we
            // can make.  0.75 is the default load factor.
            return (int) ((float) expectedSize / 0.75F + 1.0F);
        }
        // any large value
        return Integer.MAX_VALUE;
    }

    /**
     * 重复执行代码
     *
     * @param count    count 执行次数
     * @param runnable runnable
     * @since 1.0.0
     */
    @SneakyThrows
    public static void repeat(int count, CheckedRunnable runnable) {
        for (int i = 0; i < count; i++) {
            runnable.run();
        }
    }

    /**
     * 重复执行代码, 下标从 0 开始
     *
     * @param count    count
     * @param consumer consumer
     * @since 1.6.0
     */
    @SneakyThrows
    public static void repeat(int count, CheckedConsumer<Integer> consumer) {
        for (int i = 0; i < count; i++) {
            consumer.accept(i);
        }
    }


    /**
     * Properties -> map
     *
     * @param properties properties
     * @return the map from properties
     * @since 1.0.0
     */
    public static @NotNull Map<String, Object> getMapFromProperties(@NotNull Properties properties) {
        Map<String, Object> map = Maps.newHashMap();
        for (Object key : Collections.list(properties.propertyNames())) {
            map.put((String) key, properties.get(key));
        }
        return map;
    }

    /**
     * Record stop watch
     *
     * @param runnable runnable
     * @return the stop watch
     * @since 1.0.0
     */
    @SneakyThrows
    public static @NotNull StopWatch record(@NotNull CheckedRunnable runnable) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        runnable.run();

        stopWatch.stop();
        return stopWatch;
    }
}
