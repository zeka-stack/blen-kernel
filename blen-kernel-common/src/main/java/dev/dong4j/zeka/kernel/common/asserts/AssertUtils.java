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
 * <p>断言工具类.
 * <p>为 Assertions 类提供底层的工具方法支持，包含异常处理、消息格式化、类型检查等通用功能.
 * <p>定义了断言失败时的标准处理流程和异常信息格式化规则.
 * <p>主要功能：
 * <ul>
 *     <li>异常抛出和包装处理</li>
 *     <li>类型检查和实例验证</li>
 *     <li>数值比较和精度处理</li>
 *     <li>错误消息格式化和校验</li>
 * </ul>
 * <p>使用说明：
 * <ul>
 *     <li>该类为包级可见，仅供 Assertions 内部使用</li>
 *     <li>所有方法都是静态方法，通过 @UtilityClass 保证不可实例化</li>
 *     <li>提供统一的异常处理机制和安全性检查</li>
 * </ul>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.11.18 17:19
 * @since 1.0.0
 */
@UtilityClass
@SuppressWarnings("all")
class AssertUtils {

    /**
     * <p>直接抛出断言失败异常.
     * <p>使用指定的消息创建 AssertionFailedException 并抛出.
     *
     * @param message 异常消息
     * @since 1.0.0
     */
    @Contract("_ -> fail")
    static void fail(String message) {
        fail(() -> new AssertionFailedException(message));
    }

    /**
     * <p>使用异常供应器抛出异常.
     * <p>通过供应器获取异常实例并抛出，支持延迟创建和自定义异常类型.
     *
     * @param exceptionSupplier 异常供应器
     * @since 1.0.0
     */
    @Contract("_ -> fail")
    @SneakyThrows
    static void fail(Supplier<? extends RuntimeException> exceptionSupplier) {
        throw nullSafeGetException(exceptionSupplier);
    }

    /**
     * <p>包装执行操作并在失败时抛出异常.
     * <p>先执行指定操作，如果执行失败则将原异常作为因果链附加到新异常中.
     *
     * @param exceptionSupplier 异常供应器
     * @param runnable          要执行的操作
     * @since 1.0.0
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
     * <p>安全获取异常实例.
     * <p>从供应器中获取异常，如果供应器为 null 则返回默认的 AssertionFailedException.
     *
     * @param exceptionSupplier 异常供应器
     * @return 异常实例
     * @since 1.0.0
     */
    @Contract("null -> new")
    static Exception nullSafeGetException(@Nullable Supplier<? extends RuntimeException> exceptionSupplier) {
        return (exceptionSupplier != null ? exceptionSupplier.get() : new AssertionFailedException());
    }

    /**
     * <p>实例类型检查失败处理.
     * <p>当对象不是指定类型的实例时，生成详细的错误信息并抛出异常.
     *
     * @param type    期望的类型
     * @param obj     实际对象
     * @param message 自定义错误消息
     * @since 1.0.0
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
     * <p>可分配性检查失败处理.
     * <p>当子类不能分配给父类时，生成详细的错误信息并抛出异常.
     *
     * @param superType 父类类型
     * @param subType   子类类型
     * @param message   自定义错误消息
     * @since 1.0.0
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
     * <p>检查字符串是否以分隔符结尾.
     * <p>用于判断错误消息是否需要添加额外的格式化.
     *
     * @param message 要检查的消息字符串
     * @return {@code true} 如果以分隔符结尾；{@code false} 否则
     * @since 1.0.0
     */
    static boolean endsWithSeparator(@NotNull String message) {
        return (message.endsWith(":") || message.endsWith(";") || message.endsWith(",") || message.endsWith("."));
    }

    /**
     * <p>将类型名称附加到错误消息中.
     * <p>格式化错误消息，在消息和类型名之间添加适当的分隔符.
     *
     * @param message  原始错误消息
     * @param typeName 类型名称
     * @return 格式化后的错误消息
     * @since 1.0.0
     */
    @Contract(pure = true)
    static @NotNull String messageWithTypeName(@NotNull String message, @Nullable Object typeName) {
        return message + (message.endsWith(" ") ? "" : ": ") + typeName;
    }

    /**
     * <p>判断两个浮点数是否相等.
     * <p>使用 Float.floatToIntBits() 进行精确比较，处理 NaN 和特殊值.
     *
     * @param value1 第一个浮点数
     * @param value2 第二个浮点数
     * @return {@code true} 如果相等；{@code false} 否则
     * @since 1.0.0
     */
    static boolean floatsAreEqual(float value1, float value2) {
        return Float.floatToIntBits(value1) == Float.floatToIntBits(value2);
    }

    /**
     * <p>判断两个浮点数是否在指定误差范围内相等.
     * <p>支持误差容忍的浮点数比较，用于处理浮点计算的精度问题.
     *
     * @param value1 第一个浮点数
     * @param value2 第二个浮点数
     * @param delta  允许的误差范围
     * @return {@code true} 如果在误差范围内相等；{@code false} 否则
     * @since 1.0.0
     */
    static boolean floatsAreEqual(float value1, float value2, float delta) {
        assertValidDelta(delta);
        return floatsAreEqual(value1, value2) || Math.abs(value1 - value2) <= delta;
    }

    /**
     * <p>判断两个双精度浮点数是否在指定误差范围内相等.
     * <p>支持误差容忍的双精度浮点数比较，先验证 delta 的有效性.
     *
     * @param value1 第一个双精度浮点数
     * @param value2 第二个双精度浮点数
     * @param delta  允许的误差范围
     * @return {@code true} 如果在误差范围内相等；{@code false} 否则
     * @since 1.0.0
     */
    static boolean doublesAreEqual(double value1, double value2, double delta) {
        assertValidDelta(delta);
        return doublesAreEqual(value1, value2) || Math.abs(value1 - value2) <= delta;
    }

    /**
     * <p>验证 delta 值的有效性.
     * <p>检查 delta 值不是 NaN 且大于等于 0，确保比较的有效性.
     *
     * @param delta 要验证的 delta 值
     * @since 1.0.0
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
     * @since 1.0.0
     */
    private static void failIllegalDelta(String delta) {
        fail("positive delta expected but was: <" + delta + ">");
    }

    /**
     * <p>判断两个双精度浮点数是否相等.
     * <p>使用 Double.doubleToLongBits() 进行精确比较，处理 NaN 和特殊值.
     *
     * @param value1 第一个双精度浮点数
     * @param value2 第二个双精度浮点数
     * @return {@code true} 如果相等；{@code false} 否则
     * @since 1.0.0
     */
    static boolean doublesAreEqual(double value1, double value2) {
        return Double.doubleToLongBits(value1) == Double.doubleToLongBits(value2);
    }

    /**
     * <p>判断两个对象是否相等.
     * <p>安全的对象比较，正确处理 null 值情况，同时为 null 则返回 true.
     *
     * @param o1 待判断对象，同时为 null 则为 true
     * @param o2 待判断对象
     * @return {@code true} 如果相等；{@code false} 否则
     * @since 1.0.0
     */
    @Contract(value = "null, null -> true; null, !null -> false", pure = true)
    static boolean objectsAreEqual(Object o1, Object o2) {
        if (o1 == null) {
            return (o2 == null);
        }
        return o1.equals(o2);
    }
}
