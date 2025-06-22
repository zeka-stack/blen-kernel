package dev.dong4j.zeka.kernel.common.asserts;

import dev.dong4j.zeka.kernel.common.exception.AssertionFailedException;
import dev.dong4j.zeka.kernel.common.function.CheckedCallable;
import dev.dong4j.zeka.kernel.common.function.CheckedRunnable;
import dev.dong4j.zeka.kernel.common.util.ClassUtils;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Map;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.09.13 20:34
 * @since 1.6.0
 */
@UtilityClass
@SuppressWarnings("all")
public class Assertions {

    /**
     * 编译传入正则表达式和字符串去匹配,忽略大小写
     *
     * @param regex        正则
     * @param beTestString 字符串
     * @since 1.0.0
     */
    public static void isMatch(String regex, String beTestString) {
        AssertMatch.isMatch(regex, beTestString);
    }

    /**
     * Is match
     *
     * @param regex        regex
     * @param beTestString be test string
     * @param message      message
     * @since 1.7.0
     */
    public static void isMatch(String regex, String beTestString, String message) {
        AssertMatch.isMatch(regex, beTestString, message);
    }

    /**
     * Is match
     *
     * @param regex           regex
     * @param beTestString    be test string
     * @param messageSupplier message supplier
     * @since 1.7.0
     */
    public static void isMatch(String regex, String beTestString, Supplier<? extends RuntimeException> messageSupplier) {
        AssertMatch.isMatch(regex, beTestString, messageSupplier);
    }

    /**
     * Is match
     *
     * @param regex           regex
     * @param beTestString    be test string
     * @param messageSupplier message supplier
     * @param runnable        runnable
     * @since 1.7.0
     */
    public static void isMatch(String regex, String beTestString, Supplier<? extends RuntimeException> messageSupplier,
                               CheckedRunnable runnable) {
        AssertMatch.isMatch(regex, beTestString, messageSupplier, runnable);
    }

    /**
     * 编译传入正则表达式和字符串去匹配,忽略大小写
     *
     * @param regex        正则
     * @param beTestString 字符串
     * @since 1.0.0
     */
    public static void notMatch(String regex, String beTestString) {
        AssertNotMatch.notMatch(regex, beTestString);
    }

    /**
     * Is match
     *
     * @param regex        regex
     * @param beTestString be test string
     * @param message      message
     * @since 1.7.0
     */
    public static void notMatch(String regex, String beTestString, String message) {
        AssertNotMatch.notMatch(regex, beTestString, message);
    }

    /**
     * Is match
     *
     * @param regex           regex
     * @param beTestString    be test string
     * @param messageSupplier message supplier
     * @since 1.7.0
     */
    public static void notMatch(String regex, String beTestString, Supplier<? extends RuntimeException> messageSupplier) {
        AssertNotMatch.notMatch(regex, beTestString, messageSupplier);
    }

    /**
     * Is match
     *
     * @param regex           regex
     * @param beTestString    be test string
     * @param messageSupplier message supplier
     * @param runnable        runnable
     * @since 1.7.0
     */
    public static void notMatch(String regex, String beTestString, Supplier<? extends RuntimeException> messageSupplier,
                                CheckedRunnable runnable) {
        AssertNotMatch.notMatch(regex, beTestString, messageSupplier, runnable);
    }

    /**
     * Not blank
     *
     * @param content content
     * @since 1.7.0
     */
    public static void notBlank(String content) {
        notBlank(content, "参数不能是空字符串或 null");
    }

    /**
     * Not blank
     *
     * @param content content
     * @param message message
     * @since 1.6.0
     */
    public static void notBlank(String content, @NotNull String message) {
        notBlank(content, () -> new AssertionFailedException(message));
    }

    /**
     * Not blank
     *
     * @param content           content
     * @param exceptionSupplier exception supplier
     * @since 1.6.0
     */
    public static void notBlank(@Nullable String content, Supplier<? extends RuntimeException> exceptionSupplier) {
        notBlank(content, exceptionSupplier, () -> {
        });
    }

    /**
     * Not blank
     *
     * @param content           content
     * @param exceptionSupplier exception supplier
     * @param runnable          runnable
     * @since 1.7.0
     */
    public static void notBlank(@Nullable String content,
                                Supplier<? extends RuntimeException> exceptionSupplier,
                                CheckedRunnable runnable) {
        if (!StringUtils.hasText(content)) {
            AssertUtils.fail(exceptionSupplier, runnable);
        }
    }

    /**
     * Not null
     *
     * @param object object
     * @since 1.7.0
     */
    public static void notNull(@Nullable Object object) {
        AssertNotNull.notNull(object);
    }

    /**
     * Not null
     *
     * @param object  object
     * @param message message
     * @since 1.6.0
     */
    public static void notNull(@Nullable Object object, @NotNull String message) {
        AssertNotNull.notNull(object, message);
    }

    /**
     * Not null
     *
     * @param object   object
     * @param message  message
     * @param runnable runnable
     * @since 1.7.0
     */
    public static void notNull(@Nullable Object object, @NotNull String message, CheckedRunnable runnable) {
        AssertNotNull.notNull(object, () -> new AssertionFailedException(message), runnable);
    }

    /**
     * Not null s
     *
     * @param object            object
     * @param exceptionSupplier exception supplier
     * @since 1.6.0
     */
    public static void notNull(@Nullable Object object, Supplier<? extends RuntimeException> exceptionSupplier) {
        AssertNotNull.notNull(object, exceptionSupplier);
    }

    /**
     * Not null
     *
     * @param object            object
     * @param exceptionSupplier exception supplier
     * @param runnable          runnable
     * @since 1.7.0
     */
    public static void notNull(@Nullable Object object, Supplier<? extends RuntimeException> exceptionSupplier, CheckedRunnable runnable) {
        AssertNotNull.notNull(object, exceptionSupplier, runnable);
    }

    /**
     * Is null
     *
     * @param object object
     * @since 1.6.0
     */
    public static void isNull(@Nullable Object object) {
        AssertNull.isNull(object);
    }

    /**
     * Is null
     *
     * @param object  object
     * @param message message
     * @since 1.6.0
     */
    public static void isNull(@Nullable Object object, String message) {
        AssertNull.isNull(object, message);
    }

    /**
     * Is null
     *
     * @param object            object
     * @param exceptionSupplier message supplier
     * @since 1.6.0
     */
    public static void isNull(@Nullable Object object, Supplier<? extends RuntimeException> exceptionSupplier) {
        AssertNull.isNull(object, exceptionSupplier);
    }

    /**
     * Is null
     *
     * @param object            object
     * @param exceptionSupplier exception supplier
     * @param runnable          runnable
     * @since 1.7.0
     */
    public static void isNull(@Nullable Object object, Supplier<? extends RuntimeException> exceptionSupplier, CheckedRunnable runnable) {
        AssertNull.isNull(object, exceptionSupplier, runnable);
    }

    /**
     * Is true
     *
     * @param expression expression
     * @since 1.6.0
     */
    public static void isTrue(boolean expression) {
        AssertTrue.isTrue(expression);
    }

    /**
     * Is true
     *
     * @param expression expression
     * @param message    message
     * @since 1.6.0
     */
    public static void isTrue(boolean expression, String message) {
        AssertTrue.isTrue(expression, message);
    }

    /**
     * Is true
     *
     * @param expression        expression
     * @param exceptionSupplier exception supplier
     * @since 1.6.0
     */
    public static void isTrue(boolean expression, Supplier<? extends RuntimeException> exceptionSupplier) {
        AssertTrue.isTrue(expression, exceptionSupplier);
    }

    /**
     * Is true
     *
     * @param expression        expression
     * @param exceptionSupplier exception supplier
     * @param runnable          runnable
     * @since 1.7.0
     */
    public static void isTrue(boolean expression, Supplier<? extends RuntimeException> exceptionSupplier, CheckedRunnable runnable) {
        AssertTrue.isTrue(expression, exceptionSupplier, runnable);
    }

    /**
     * Is true
     *
     * @param booleanSupplier boolean supplier
     * @since 1.7.0
     */
    public static void isTrue(BooleanSupplier booleanSupplier) {
        AssertTrue.isTrue(booleanSupplier);
    }

    /**
     * Is true
     *
     * @param booleanSupplier boolean supplier
     * @param message         message
     * @since 1.7.0
     */
    public static void isTrue(BooleanSupplier booleanSupplier, String message) {
        AssertTrue.isTrue(booleanSupplier, message);
    }

    /**
     * Is true
     *
     * @param booleanSupplier   boolean supplier
     * @param exceptionSupplier message supplier
     * @since 1.7.0
     */
    public static void isTrue(BooleanSupplier booleanSupplier, Supplier<? extends RuntimeException> exceptionSupplier) {
        AssertTrue.isTrue(booleanSupplier, exceptionSupplier);
    }

    /**
     * Is false
     *
     * @param expression expression
     * @since 1.6.0
     */
    public static void isFalse(boolean expression) {
        AssertFalse.isFalse(expression);
    }

    /**
     * Is false
     *
     * @param expression expression
     * @param message    message
     * @since 1.6.0
     */
    public static void isFalse(boolean expression, String message) {
        AssertFalse.isFalse(expression, () -> new AssertionFailedException(message));
    }

    /**
     * Is false
     *
     * @param expression        expression
     * @param exceptionSupplier exception supplier
     * @since 1.6.0
     */
    public static void isFalse(boolean expression, Supplier<? extends RuntimeException> exceptionSupplier) {
        AssertFalse.isFalse(expression, exceptionSupplier);
    }

    /**
     * Is false
     *
     * @param expression        expression
     * @param exceptionSupplier exception supplier
     * @param runnable          runnable
     * @since 1.7.0
     */
    public static void isFalse(boolean expression, Supplier<? extends RuntimeException> exceptionSupplier, CheckedRunnable runnable) {
        AssertFalse.isFalse(expression, exceptionSupplier, runnable);
    }


    /**
     * Is false
     *
     * @param booleanSupplier boolean supplier
     * @since 1.7.0
     */
    public static void isFalse(BooleanSupplier booleanSupplier) {
        AssertFalse.isFalse(booleanSupplier);
    }

    /**
     * Is false
     *
     * @param booleanSupplier boolean supplier
     * @param message         message
     * @since 1.7.0
     */
    public static void isFalse(BooleanSupplier booleanSupplier, String message) {
        AssertFalse.isFalse(booleanSupplier, message);
    }

    /**
     * Is false
     *
     * @param booleanSupplier   boolean supplier
     * @param exceptionSupplier message supplier
     * @since 1.7.0
     */
    public static void isFalse(BooleanSupplier booleanSupplier, Supplier<? extends RuntimeException> exceptionSupplier) {
        AssertFalse.isFalse(booleanSupplier, exceptionSupplier);
    }

    /**
     * Does not contain
     *
     * @param textToSearch text to search
     * @param substring    substring
     * @since 1.6.0
     */
    public static void doesNotContain(@Nullable String textToSearch, String substring) {
        doesNotContain(textToSearch, substring, "字符串内不能包含: " + substring);
    }

    /**
     * Does not contain
     *
     * @param textToSearch text to search
     * @param substring    substring
     * @param message      message
     * @since 1.6.0
     */
    public static void doesNotContain(@Nullable String textToSearch, String substring, String message) {
        doesNotContain(textToSearch, substring, () -> new AssertionFailedException(message));
    }

    /**
     * Does not contain
     *
     * @param textToSearch      text to search
     * @param substring         substring
     * @param exceptionSupplier message supplier
     * @since 1.6.0
     */
    public static void doesNotContain(@Nullable String textToSearch, String substring,
                                      Supplier<? extends RuntimeException> exceptionSupplier) {
        if (StringUtils.hasLength(textToSearch)
            && StringUtils.hasLength(substring)
            && textToSearch.contains(substring)) {
            AssertUtils.fail(exceptionSupplier);
        }
    }

    /**
     * Not empty
     *
     * @param map map
     * @since 1.6.0
     */
    public static void notEmpty(@Nullable Map<?, ?> map) {
        notEmpty(map, "map 必须有元素");
    }

    /**
     * Not empty
     *
     * @param map     map
     * @param message message
     * @since 1.6.0
     */
    public static void notEmpty(@Nullable Map<?, ?> map, String message) {
        notEmpty(map, () -> new AssertionFailedException(message));
    }

    /**
     * Not empty
     *
     * @param map               map
     * @param exceptionSupplier exception supplier
     * @since 1.6.0
     */
    public static void notEmpty(@Nullable Map<?, ?> map, Supplier<? extends RuntimeException> exceptionSupplier) {
        if (CollectionUtils.isEmpty(map)) {
            AssertUtils.fail(exceptionSupplier);
        }
    }

    /**
     * Not empty
     *
     * @param array array
     * @since 1.6.0
     */
    public static void notEmpty(@Nullable Object[] array) {
        notEmpty(array, "数组必须包含元素");
    }

    /**
     * Not empty
     *
     * @param array   array
     * @param message message
     * @since 1.6.0
     */
    public static void notEmpty(@Nullable Object[] array, String message) {
        notEmpty(array, () -> new AssertionFailedException(message));
    }

    /**
     * Not empty
     *
     * @param array             array
     * @param exceptionSupplier message supplier
     * @since 1.6.0
     */
    public static void notEmpty(@Nullable Object[] array, Supplier<? extends RuntimeException> exceptionSupplier) {
        if (ObjectUtils.isEmpty(array)) {
            AssertUtils.fail(exceptionSupplier);
        }
    }

    /**
     * No null elements
     *
     * @param array array
     * @since 1.6.0
     */
    public static void noNullElements(@Nullable Object[] array) {
        noNullElements(array, "数组内所有元素必须全部不为 null");
    }

    /**
     * No null elements
     *
     * @param array   array
     * @param message message
     * @since 1.6.0
     */
    public static void noNullElements(@Nullable Object[] array, String message) {
        noNullElements(array, () -> new AssertionFailedException(message));
    }

    /**
     * No null elements
     *
     * @param array             array
     * @param exceptionSupplier message supplier
     * @since 1.6.0
     */
    @SuppressWarnings("all")
    public static void noNullElements(@Nullable Object[] array, Supplier<? extends RuntimeException> exceptionSupplier) {
        notEmpty(array);

        for (Object element : array) {
            if (element == null) {
                AssertUtils.fail(exceptionSupplier);
            }
        }
    }

    /**
     * Not empty
     *
     * @param collection collection
     * @since 1.6.0
     */
    public static void notEmpty(@Nullable Collection<?> collection) {
        notEmpty(collection, "集合必须包含元素");
    }

    /**
     * Not empty
     *
     * @param collection collection
     * @param message    message
     * @since 1.6.0
     */
    public static void notEmpty(@Nullable Collection<?> collection, String message) {
        notEmpty(collection, () -> new AssertionFailedException(message));
    }

    /**
     * Not empty
     *
     * @param collection        collection
     * @param exceptionSupplier exception supplier
     * @since 1.6.0
     */
    public static void notEmpty(@Nullable Collection<?> collection, Supplier<? extends RuntimeException> exceptionSupplier) {
        if (CollectionUtils.isEmpty(collection)) {
            AssertUtils.fail(exceptionSupplier);
        }
    }

    /**
     * No null elements
     *
     * @param collection collection
     * @since 1.7.0
     */
    public static void noNullElements(@Nullable Collection<?> collection) {
        noNullElements(collection, "集合内所有元素必须全部不为 null");
    }

    /**
     * No null elements
     *
     * @param collection collection
     * @param message    message
     * @since 1.6.0
     */
    public static void noNullElements(@Nullable Collection<?> collection, String message) {
        noNullElements(collection, () -> new AssertionFailedException(message));
    }

    /**
     * No null elements
     *
     * @param collection        collection
     * @param exceptionSupplier message supplier
     * @since 1.6.0
     */
    @SuppressWarnings("all")
    public static void noNullElements(@Nullable Collection<?> collection, Supplier<? extends RuntimeException> exceptionSupplier) {
        notEmpty(collection);

        for (Object element : collection) {
            if (element == null) {
                AssertUtils.fail(exceptionSupplier);
            }
        }
    }

    /**
     * Not empty
     *
     * @param array array
     * @since 1.6.0
     */
    public static void notEmpty(byte[] array) {
        notEmpty(array, "字节数据必须包含元素");
    }

    /**
     * Not empty
     *
     * @param array   array
     * @param message message
     * @since 1.7.0
     */
    public static void notEmpty(byte[] array, @NotNull String message) {
        notEmpty(array, () -> new AssertionFailedException(message));
    }

    /**
     * Not empty
     *
     * @param array             array
     * @param exceptionSupplier exception supplier
     * @since 1.7.0
     */
    public static void notEmpty(byte[] array, Supplier<? extends RuntimeException> exceptionSupplier) {
        if (ObjectUtils.isEmpty(array)) {
            AssertUtils.fail(exceptionSupplier);
        }
    }

    /**
     * Equals
     *
     * @param expected expected
     * @param actual   actual
     * @since 1.7.0
     */
    public static void equals(byte expected, byte actual) {
        AssertEquals.equals(expected, actual);
    }

    /**
     * Equals
     *
     * @param expected expected
     * @param actual   actual
     * @param message  message
     * @since 1.7.0
     */
    public static void equals(byte expected, byte actual, String message) {
        AssertEquals.equals(expected, actual, message);
    }

    /**
     * Equals
     *
     * @param expected        expected
     * @param actual          actual
     * @param messageSupplier message supplier
     * @since 1.7.0
     */
    public static void equals(byte expected, byte actual, Supplier<? extends RuntimeException> messageSupplier) {
        AssertEquals.equals(expected, actual, messageSupplier);
    }

    /**
     * Equals
     *
     * @param expected expected
     * @param actual   actual
     * @since 1.7.0
     */
    public static void equals(char expected, char actual) {
        AssertEquals.equals(expected, actual);
    }

    /**
     * Equals
     *
     * @param expected expected
     * @param actual   actual
     * @param message  message
     * @since 1.7.0
     */
    public static void equals(char expected, char actual, String message) {
        AssertEquals.equals(expected, actual, message);
    }

    /**
     * Equals
     *
     * @param expected        expected
     * @param actual          actual
     * @param messageSupplier message supplier
     * @since 1.7.0
     */
    public static void equals(char expected, char actual, Supplier<? extends RuntimeException> messageSupplier) {
        AssertEquals.equals(expected, actual, messageSupplier);
    }

    /**
     * Equals
     *
     * @param expected expected
     * @param actual   actual
     * @since 1.7.0
     */
    public static void equals(double expected, double actual) {
        AssertEquals.equals(expected, actual);
    }

    /**
     * Equals
     *
     * @param expected expected
     * @param actual   actual
     * @param message  message
     * @since 1.7.0
     */
    public static void equals(double expected, double actual, String message) {
        AssertEquals.equals(expected, actual, message);
    }

    /**
     * Equals
     *
     * @param expected        expected
     * @param actual          actual
     * @param messageSupplier message supplier
     * @since 1.7.0
     */
    public static void equals(double expected, double actual, Supplier<? extends RuntimeException> messageSupplier) {
        AssertEquals.equals(expected, actual, messageSupplier);
    }

    /**
     * Equals
     *
     * @param expected expected
     * @param actual   actual
     * @param delta    delta
     * @since 1.7.0
     */
    public static void equals(double expected, double actual, double delta) {
        AssertEquals.equals(expected, actual, delta);
    }

    /**
     * Equals
     *
     * @param expected expected
     * @param actual   actual
     * @param delta    delta
     * @param message  message
     * @since 1.7.0
     */
    public static void equals(double expected, double actual, double delta, String message) {
        AssertEquals.equals(expected, actual, delta, message);
    }

    /**
     * Equals
     *
     * @param expected        expected
     * @param actual          actual
     * @param delta           delta
     * @param messageSupplier message supplier
     * @since 1.7.0
     */
    public static void equals(double expected, double actual, double delta, Supplier<? extends RuntimeException> messageSupplier) {
        AssertEquals.equals(expected, actual, delta, messageSupplier);
    }

    /**
     * Equals
     *
     * @param expected expected
     * @param actual   actual
     * @since 1.7.0
     */
    public static void equals(float expected, float actual) {
        AssertEquals.equals(expected, actual);
    }

    /**
     * Equals
     *
     * @param expected expected
     * @param actual   actual
     * @param message  message
     * @since 1.7.0
     */
    public static void equals(float expected, float actual, String message) {
        AssertEquals.equals(expected, actual, message);
    }

    /**
     * Equals
     *
     * @param expected        expected
     * @param actual          actual
     * @param messageSupplier message supplier
     * @since 1.7.0
     */
    public static void equals(float expected, float actual, Supplier<? extends RuntimeException> messageSupplier) {
        AssertEquals.equals(expected, actual, messageSupplier);
    }

    /**
     * Equals
     *
     * @param expected expected
     * @param actual   actual
     * @param delta    delta
     * @since 1.7.0
     */
    public static void equals(float expected, float actual, float delta) {
        AssertEquals.equals(expected, actual, delta);
    }

    /**
     * Equals
     *
     * @param expected expected
     * @param actual   actual
     * @param delta    delta
     * @param message  message
     * @since 1.7.0
     */
    public static void equals(float expected, float actual, float delta, String message) {
        AssertEquals.equals(expected, actual, delta, message);
    }

    /**
     * Equals
     *
     * @param expected        expected
     * @param actual          actual
     * @param delta           delta
     * @param messageSupplier message supplier
     * @since 1.7.0
     */
    public static void equals(float expected, float actual, float delta, Supplier<? extends RuntimeException> messageSupplier) {
        AssertEquals.equals(expected, actual, delta, messageSupplier);
    }

    /**
     * Equals
     *
     * @param expected expected
     * @param actual   actual
     * @since 1.7.0
     */
    public static void equals(short expected, short actual) {
        AssertEquals.equals(expected, actual);
    }

    /**
     * Equals
     *
     * @param expected expected
     * @param actual   actual
     * @param message  message
     * @since 1.7.0
     */
    public static void equals(short expected, short actual, String message) {
        AssertEquals.equals(expected, actual, message);
    }

    /**
     * Equals
     *
     * @param expected        expected
     * @param actual          actual
     * @param messageSupplier message supplier
     * @since 1.7.0
     */
    public static void equals(short expected, short actual, Supplier<? extends RuntimeException> messageSupplier) {
        AssertEquals.equals(expected, actual, messageSupplier);
    }

    /**
     * Equals
     *
     * @param expected expected
     * @param actual   actual
     * @since 1.7.0
     */
    public static void equals(int expected, int actual) {
        AssertEquals.equals(expected, actual);
    }

    /**
     * Equals
     *
     * @param expected expected
     * @param actual   actual
     * @param message  message
     * @since 1.7.0
     */
    public static void equals(int expected, int actual, String message) {
        equals(expected, actual, () -> new AssertionFailedException(message));
    }

    /**
     * Equals
     *
     * @param expected        expected
     * @param actual          actual
     * @param messageSupplier message supplier
     * @since 1.7.0
     */
    public static void equals(int expected, int actual, Supplier<? extends RuntimeException> messageSupplier) {
        AssertEquals.equals(expected, actual, messageSupplier);
    }

    /**
     * Equals
     *
     * @param expected expected
     * @param actual   actual
     * @since 1.7.0
     */
    public static void equals(long expected, long actual) {
        AssertEquals.equals(expected, actual);
    }

    /**
     * Equals
     *
     * @param expected expected
     * @param actual   actual
     * @param message  message
     * @since 1.7.0
     */
    public static void equals(long expected, long actual, String message) {
        AssertEquals.equals(expected, actual, message);
    }

    /**
     * Equals
     *
     * @param expected        expected
     * @param actual          actual
     * @param messageSupplier message supplier
     * @since 1.7.0
     */
    public static void equals(long expected, long actual, Supplier<? extends RuntimeException> messageSupplier) {
        AssertEquals.equals(expected, actual, messageSupplier);
    }

    /**
     * Equals
     *
     * @param expected expected
     * @param actual   actual
     * @since 1.7.0
     */
    public static void equals(Object expected, Object actual) {
        AssertEquals.equals(expected, actual);
    }

    /**
     * Equals
     *
     * @param expected expected
     * @param actual   actual
     * @param message  message
     * @since 1.7.0
     */
    public static void equals(Object expected, Object actual, String message) {
        AssertEquals.equals(expected, actual, message);
    }

    /**
     * Equals
     *
     * @param expected        expected
     * @param actual          actual
     * @param messageSupplier message supplier
     * @since 1.7.0
     */
    public static void equals(Object expected, Object actual, Supplier<? extends RuntimeException> messageSupplier) {
        AssertEquals.equals(expected, actual, messageSupplier);
    }

    /**
     * Not equals
     *
     * @param unexpected unexpected
     * @param actual     actual
     * @since 1.7.0
     */
    public static void notEquals(byte unexpected, byte actual) {
        AssertNotEquals.notEquals(unexpected, actual);
    }

    /**
     * Not equals
     *
     * @param unexpected unexpected
     * @param actual     actual
     * @param message    message
     * @since 1.7.0
     */
    public static void notEquals(byte unexpected, byte actual, String message) {
        AssertNotEquals.notEquals(unexpected, actual, message);
    }

    /**
     * Not equals
     *
     * @param unexpected      unexpected
     * @param actual          actual
     * @param messageSupplier message supplier
     * @since 1.7.0
     */
    public static void notEquals(byte unexpected, byte actual, Supplier<? extends RuntimeException> messageSupplier) {
        AssertNotEquals.notEquals(unexpected, actual, messageSupplier);
    }

    /**
     * Not equals
     *
     * @param unexpected unexpected
     * @param actual     actual
     * @since 1.7.0
     */
    public static void notEquals(short unexpected, short actual) {
        AssertNotEquals.notEquals(unexpected, actual);
    }

    /**
     * Not equals
     *
     * @param unexpected unexpected
     * @param actual     actual
     * @param message    message
     * @since 1.7.0
     */
    public static void notEquals(short unexpected, short actual, String message) {
        AssertNotEquals.notEquals(unexpected, actual, message);
    }

    /**
     * Not equals
     *
     * @param unexpected      unexpected
     * @param actual          actual
     * @param messageSupplier message supplier
     * @since 1.7.0
     */
    public static void notEquals(short unexpected, short actual, Supplier<? extends RuntimeException> messageSupplier) {
        AssertNotEquals.notEquals(unexpected, actual, messageSupplier);
    }

    /**
     * Not equals
     *
     * @param unexpected unexpected
     * @param actual     actual
     * @since 1.7.0
     */
    public static void notEquals(int unexpected, int actual) {
        AssertNotEquals.notEquals(unexpected, actual);
    }

    /**
     * Not equals
     *
     * @param unexpected unexpected
     * @param actual     actual
     * @param message    message
     * @since 1.7.0
     */
    public static void notEquals(int unexpected, int actual, String message) {
        AssertNotEquals.notEquals(unexpected, actual, message);
    }

    /**
     * Not equals
     *
     * @param unexpected      unexpected
     * @param actual          actual
     * @param messageSupplier message supplier
     * @since 1.7.0
     */
    public static void notEquals(int unexpected, int actual, Supplier<? extends RuntimeException> messageSupplier) {
        AssertNotEquals.notEquals(unexpected, actual, messageSupplier);
    }

    /**
     * Not equals
     *
     * @param unexpected unexpected
     * @param actual     actual
     * @since 1.7.0
     */
    public static void notEquals(long unexpected, long actual) {
        AssertNotEquals.notEquals(unexpected, actual);
    }

    /**
     * Not equals
     *
     * @param unexpected unexpected
     * @param actual     actual
     * @param message    message
     * @since 1.7.0
     */
    public static void notEquals(long unexpected, long actual, String message) {
        AssertNotEquals.notEquals(unexpected, actual, message);
    }

    /**
     * Not equals
     *
     * @param unexpected      unexpected
     * @param actual          actual
     * @param messageSupplier message supplier
     * @since 1.7.0
     */
    public static void notEquals(long unexpected, long actual, Supplier<? extends RuntimeException> messageSupplier) {
        AssertNotEquals.notEquals(unexpected, actual, messageSupplier);
    }

    /**
     * Not equals
     *
     * @param unexpected unexpected
     * @param actual     actual
     * @since 1.7.0
     */
    public static void notEquals(float unexpected, float actual) {
        AssertNotEquals.notEquals(unexpected, actual);
    }

    /**
     * Not equals
     *
     * @param unexpected unexpected
     * @param actual     actual
     * @param message    message
     * @since 1.7.0
     */
    public static void notEquals(float unexpected, float actual, String message) {
        AssertNotEquals.notEquals(unexpected, actual, message);
    }

    /**
     * Not equals
     *
     * @param unexpected      unexpected
     * @param actual          actual
     * @param messageSupplier message supplier
     * @since 1.7.0
     */
    public static void notEquals(float unexpected, float actual, Supplier<? extends RuntimeException> messageSupplier) {
        AssertNotEquals.notEquals(unexpected, actual, messageSupplier);
    }

    /**
     * Not equals
     *
     * @param unexpected unexpected
     * @param actual     actual
     * @param delta      delta
     * @since 1.7.0
     */
    public static void notEquals(float unexpected, float actual, float delta) {
        AssertNotEquals.notEquals(unexpected, actual, delta);
    }

    /**
     * Not equals
     *
     * @param unexpected unexpected
     * @param actual     actual
     * @param delta      delta
     * @param message    message
     * @since 1.7.0
     */
    public static void notEquals(float unexpected, float actual, float delta, String message) {
        AssertNotEquals.notEquals(unexpected, actual, delta, message);
    }

    /**
     * Not equals
     *
     * @param unexpected      unexpected
     * @param actual          actual
     * @param delta           delta
     * @param messageSupplier message supplier
     * @since 1.7.0
     */
    public static void notEquals(float unexpected, float actual, float delta, Supplier<? extends RuntimeException> messageSupplier) {
        AssertNotEquals.notEquals(unexpected, actual, messageSupplier);
    }

    /**
     * Not equals
     *
     * @param unexpected unexpected
     * @param actual     actual
     * @since 1.7.0
     */
    public static void notEquals(double unexpected, double actual) {
        AssertNotEquals.notEquals(unexpected, actual);
    }

    /**
     * Not equals
     *
     * @param unexpected unexpected
     * @param actual     actual
     * @param message    message
     * @since 1.7.0
     */
    public static void notEquals(double unexpected, double actual, String message) {
        AssertNotEquals.notEquals(unexpected, actual, message);
    }

    /**
     * Not equals
     *
     * @param unexpected      unexpected
     * @param actual          actual
     * @param messageSupplier message supplier
     * @since 1.7.0
     */
    public static void notEquals(double unexpected, double actual, Supplier<? extends RuntimeException> messageSupplier) {
        AssertNotEquals.notEquals(unexpected, actual, messageSupplier);
    }

    /**
     * Not equals
     *
     * @param unexpected unexpected
     * @param actual     actual
     * @param delta      delta
     * @since 1.7.0
     */
    public static void notEquals(double unexpected, double actual, double delta) {
        AssertNotEquals.notEquals(unexpected, actual, delta);
    }

    /**
     * Not equals
     *
     * @param unexpected unexpected
     * @param actual     actual
     * @param delta      delta
     * @param message    message
     * @since 1.7.0
     */
    public static void notEquals(double unexpected, double actual, double delta, String message) {
        AssertNotEquals.notEquals(unexpected, actual, delta, message);
    }

    /**
     * Not equals
     *
     * @param unexpected      unexpected
     * @param actual          actual
     * @param delta           delta
     * @param messageSupplier message supplier
     * @since 1.7.0
     */
    public static void notEquals(double unexpected, double actual, double delta, Supplier<? extends RuntimeException> messageSupplier) {
        AssertNotEquals.notEquals(unexpected, actual, messageSupplier);
    }

    /**
     * Not equals
     *
     * @param unexpected unexpected
     * @param actual     actual
     * @since 1.7.0
     */
    public static void notEquals(char unexpected, char actual) {
        AssertNotEquals.notEquals(unexpected, actual);
    }

    /**
     * Not equals
     *
     * @param unexpected unexpected
     * @param actual     actual
     * @param message    message
     * @since 1.7.0
     */
    public static void notEquals(char unexpected, char actual, String message) {
        AssertNotEquals.notEquals(unexpected, actual, message);
    }

    /**
     * Not equals
     *
     * @param unexpected      unexpected
     * @param actual          actual
     * @param messageSupplier message supplier
     * @since 1.7.0
     */
    public static void notEquals(char unexpected, char actual, Supplier<? extends RuntimeException> messageSupplier) {
        AssertNotEquals.notEquals(unexpected, actual, messageSupplier);
    }

    /**
     * Not equals
     *
     * @param unexpected unexpected
     * @param actual     actual
     * @since 1.7.0
     */
    public static void notEquals(Object unexpected, Object actual) {
        AssertNotEquals.notEquals(unexpected, actual);
    }

    /**
     * Not equals
     *
     * @param unexpected unexpected
     * @param actual     actual
     * @param message    message
     * @since 1.7.0
     */
    public static void notEquals(Object unexpected, Object actual, String message) {
        AssertNotEquals.notEquals(unexpected, actual, message);
    }

    /**
     * Not equals
     *
     * @param unexpected      unexpected
     * @param actual          actual
     * @param messageSupplier message supplier
     * @since 1.7.0
     */
    public static void notEquals(Object unexpected, Object actual, Supplier<? extends RuntimeException> messageSupplier) {
        AssertNotEquals.notEquals(unexpected, actual, messageSupplier);
    }

    /**
     * Is instance of
     *
     * @param type type
     * @param obj  obj
     * @since 1.7.0
     */
    public static void isInstanceOf(Class<?> type, @Nullable Object obj) {
        isInstanceOf(type, obj, "obj 不是 " + type.getName() + " 类型参数: " + ClassUtils.getDescriptiveType(obj));
    }

    /**
     * Is instance of
     *
     * @param type    type
     * @param obj     obj
     * @param message message
     * @since 1.6.0
     */
    public static void isInstanceOf(Class<?> type, @Nullable Object obj, String message) {
        notNull(type, "Type 不能为 null");
        if (!type.isInstance(obj)) {
            AssertUtils.instanceCheckFailed(type, obj, message);
        }
    }

    /**
     * Is assignable
     *
     * @param superType super type
     * @param subType   sub type
     * @since 1.7.0
     */
    public static void isAssignable(Class<?> superType, @Nullable Class<?> subType) {
        isAssignable(superType, subType, "subType 不是 " + superType.getName() + "的子类");
    }

    /**
     * Is assignable
     *
     * @param superType super type
     * @param subType   sub type
     * @param message   message
     * @since 1.6.0
     */
    public static void isAssignable(Class<?> superType, @Nullable Class<?> subType, String message) {
        notNull(superType, "superType 不能为 null");
        if (subType == null || !superType.isAssignableFrom(subType)) {
            AssertUtils.assignableCheckFailed(superType, subType, message);
        }
    }

    /**
     * Fail
     *
     * @param message message
     * @since 1.7.0
     */
    @Contract("_ -> fail")
    public static void fail(String message) {
        AssertUtils.fail(message);
    }

    /**
     * Fail
     *
     * @param exceptionSupplier exception supplier
     * @since 1.6.0
     */
    @Contract("_ -> fail")
    public static void fail(Supplier<? extends RuntimeException> exceptionSupplier) {
        AssertUtils.fail(exceptionSupplier);
    }

    /**
     * Fail
     *
     * @param exceptionSupplier exception supplier
     * @param runnable          runnable
     * @since 1.7.0
     */
    public static void fail(Supplier<? extends RuntimeException> exceptionSupplier, CheckedRunnable runnable) {
        AssertUtils.fail(exceptionSupplier, runnable);
    }

    /**
     * 包装异常
     *
     * @param runnable 可运行的
     * @param message  消息
     */
    public static void wrapper(CheckedRunnable runnable, String message) {
        try {
            runnable.run();
        } catch (Throwable throwable) {
            fail(message);
        }
    }

    /**
     * 包装异常
     *
     * @param <T>      parameter
     * @param runnable runnable
     * @param message  message
     * @return the t
     * @since 1.7.1
     */
    @NotNull
    public static <T> T wrapper(CheckedCallable<T> runnable, String message) {
        try {
            return runnable.call();
        } catch (Throwable throwable) {
            fail(message);
        }
        return null;
    }
}
