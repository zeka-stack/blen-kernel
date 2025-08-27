package dev.dong4j.zeka.kernel.common.asserts;

import dev.dong4j.zeka.kernel.common.exception.AssertionFailedException;
import dev.dong4j.zeka.kernel.common.exception.LowestException;
import dev.dong4j.zeka.kernel.common.function.CheckedRunnable;
import java.util.function.Supplier;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

/**
 * <p>Description:  </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.11.18 17:19
 * @since 1.7.0
 */
@UtilityClass
@SuppressWarnings("all")
class AssertUtils {

    /**
     * Fail
     *
     * @param message message
     * @since 1.6.0
     */
    @Contract("_ -> fail")
    static void fail(String message) {
        fail(() -> new AssertionFailedException(message));
    }

    /**
     * Fail
     *
     * @param exceptionSupplier exception supplier
     * @since 1.6.0
     */
    @Contract("_ -> fail")
    @SneakyThrows
    static void fail(Supplier<? extends RuntimeException> exceptionSupplier) {
        throw nullSafeGetException(exceptionSupplier);
    }

    /**
     * Fail
     *
     * @param exceptionSupplier exception supplier
     * @param runnable          runnable
     * @since 1.7.0
     */
    @SneakyThrows
    static void fail(Supplier<? extends RuntimeException> exceptionSupplier, CheckedRunnable runnable) {
        try {
            runnable.run();
        } catch (Throwable throwable) {
            throw nullSafeGetException(exceptionSupplier).initCause(throwable);
        }

        throw nullSafeGetException(exceptionSupplier);
    }

    /**
     * Null safe get exception
     *
     * @param exceptionSupplier message supplier
     * @return the exception
     * @since 1.6.0
     */
    @Contract("null -> new")
    static Exception nullSafeGetException(@Nullable Supplier<? extends RuntimeException> exceptionSupplier) {
        return (exceptionSupplier != null ? exceptionSupplier.get() : new AssertionFailedException());
    }

    /**
     * Instance check failed
     *
     * @param type    type
     * @param obj     obj
     * @param message message
     * @since 1.6.0
     */
    static void instanceCheckFailed(Class<?> type, @Nullable Object obj, @Nullable String message) {
        String className = (obj != null ? obj.getClass().getName() : "null");
        String result = "";
        boolean defaultMessage = true;
        if (StringUtils.hasLength(message)) {
            if (endsWithSeparator(message)) {
                result = message + " ";
            } else {
                result = messageWithTypeName(message, className);
                defaultMessage = false;
            }
        }
        if (defaultMessage) {
            result = result + ("Object 类型 [" + className + "] 不是: " + type);
        }
        throw new LowestException(result);
    }

    /**
     * Assignable check failed
     *
     * @param superType super type
     * @param subType   sub type
     * @param message   message
     * @since 1.6.0
     */
    static void assignableCheckFailed(Class<?> superType, @Nullable Class<?> subType, @Nullable String message) {
        String result = "";
        boolean defaultMessage = true;
        if (StringUtils.hasLength(message)) {
            if (endsWithSeparator(message)) {
                result = message + " ";
            } else {
                result = messageWithTypeName(message, subType);
                defaultMessage = false;
            }
        }
        if (defaultMessage) {
            result = result + (subType + " 不是 " + superType + "的子类");
        }
        throw new LowestException(result);
    }

    /**
     * Ends with separator
     *
     * @param message message
     * @return the boolean
     * @since 1.6.0
     */
    static boolean endsWithSeparator(@NotNull String message) {
        return (message.endsWith(":") || message.endsWith(";") || message.endsWith(",") || message.endsWith("."));
    }

    /**
     * Message with type name
     *
     * @param message  message
     * @param typeName type name
     * @return the string
     * @since 1.6.0
     */
    @Contract(pure = true)
    static @NotNull String messageWithTypeName(@NotNull String message, @Nullable Object typeName) {
        return message + (message.endsWith(" ") ? "" : ": ") + typeName;
    }

    /**
     * Floats are equal
     *
     * @param value1 value 1
     * @param value2 value 2
     * @return the boolean
     * @since 1.7.0
     */
    static boolean floatsAreEqual(float value1, float value2) {
        return Float.floatToIntBits(value1) == Float.floatToIntBits(value2);
    }

    /**
     * Floats are equal
     *
     * @param value1 value 1
     * @param value2 value 2
     * @param delta  delta
     * @return the boolean
     * @since 1.7.0
     */
    static boolean floatsAreEqual(float value1, float value2, float delta) {
        assertValidDelta(delta);
        return floatsAreEqual(value1, value2) || Math.abs(value1 - value2) <= delta;
    }

    /**
     * Doubles are equal
     *
     * @param value1 value 1
     * @param value2 value 2
     * @param delta  delta
     * @return the boolean
     * @since 1.7.0
     */
    static boolean doublesAreEqual(double value1, double value2, double delta) {
        assertValidDelta(delta);
        return doublesAreEqual(value1, value2) || Math.abs(value1 - value2) <= delta;
    }

    /**
     * Assert valid delta
     *
     * @param delta delta
     * @since 1.7.0
     */
    static void assertValidDelta(double delta) {
        if (Double.isNaN(delta) || delta < 0.0) {
            failIllegalDelta(String.valueOf(delta));
        }
    }

    /**
     * Fail illegal delta
     *
     * @param delta delta
     * @since 1.7.0
     */
    private static void failIllegalDelta(String delta) {
        fail("positive delta expected but was: <" + delta + ">");
    }

    /**
     * Doubles are equal
     *
     * @param value1 value 1
     * @param value2 value 2
     * @return the boolean
     * @since 1.7.0
     */
    static boolean doublesAreEqual(double value1, double value2) {
        return Double.doubleToLongBits(value1) == Double.doubleToLongBits(value2);
    }

    /**
     * 判断 2 个对象是否相等
     *
     * @param o1 待判断对象, 同时为 null 则为 true
     * @param o2 待判断对象
     * @return the boolean
     * @since 1.7.0
     */
    @Contract(value = "null, null -> true; null, !null -> false", pure = true)
    static boolean objectsAreEqual(Object o1, Object o2) {
        if (o1 == null) {
            return (o2 == null);
        }
        return o1.equals(o2);
    }
}
