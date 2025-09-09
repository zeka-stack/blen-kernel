package dev.dong4j.zeka.kernel.common.asserts;

import dev.dong4j.zeka.kernel.common.exception.AssertionFailedException;
import dev.dong4j.zeka.kernel.common.function.CheckedRunnable;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

/**
 * <p>True 断言工具类.
 * <p>专门用于验证布尔值为 true 的断言操作，支持直接布尔值和布尔供应器.
 * <p>与 AssertFalse 类互为补充，提供完整的布尔断言验证能力.
 * <p>主要功能：
 * <ul>
 *     <li>验证布尔表达式的结果为 true</li>
 *     <li>支持 BooleanSupplier 的延迟计算</li>
 *     <li>自定义错误消息和异常类型</li>
 *     <li>支持异常后的清理操作</li>
 * </ul>
 * <p>使用场景：
 * <ul>
 *     <li>业务规则和条件判断的验证</li>
 *     <li>权限检查和状态验证</li>
 *     <li>参数有效性和前置条件检查</li>
 *     <li>单元测试中的期望结果验证</li>
 * </ul>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.11.18 00:26
 * @since 1.0.0
 */
final class AssertTrue {
    /**
     * Assert true
     *
     * @since 1.0.0
     */
    private AssertTrue() {
    }

    /**
     * Is true
     *
     * @param booleanSupplier boolean supplier
     * @since 1.0.0
     */
    static void isTrue(BooleanSupplier booleanSupplier) {
        isTrue(booleanSupplier.getAsBoolean());
    }

    /**
     * Is true
     *
     * @param booleanSupplier boolean supplier
     * @param message         message
     * @since 1.0.0
     */
    static void isTrue(BooleanSupplier booleanSupplier, String message) {
        isTrue(booleanSupplier.getAsBoolean(), message);
    }

    /**
     * Is true
     *
     * @param booleanSupplier   boolean supplier
     * @param exceptionSupplier message supplier
     * @since 1.0.0
     */
    static void isTrue(BooleanSupplier booleanSupplier, Supplier<? extends RuntimeException> exceptionSupplier) {
        isTrue(booleanSupplier.getAsBoolean(), exceptionSupplier);
    }

    /**
     * Is true
     *
     * @param condition condition
     * @since 1.0.0
     */
    static void isTrue(boolean condition) {
        isTrue(condition, "表达式必须是 true");
    }

    /**
     * Is true
     *
     * @param condition condition
     * @param message   message
     * @since 1.0.0
     */
    static void isTrue(boolean condition, String message) {
        isTrue(condition, () -> new AssertionFailedException(message));
    }

    /**
     * Is true
     *
     * @param condition         condition
     * @param exceptionSupplier exception supplier
     * @since 1.0.0
     */
    static void isTrue(boolean condition, Supplier<? extends RuntimeException> exceptionSupplier) {
        isTrue(condition, exceptionSupplier, () -> {
        });
    }

    /**
     * Is true
     *
     * @param condition         condition
     * @param exceptionSupplier exception supplier
     * @param runnable          runnable
     * @since 1.0.0
     */
    static void isTrue(boolean condition, Supplier<? extends RuntimeException> exceptionSupplier, CheckedRunnable runnable) {
        if (!condition) {
            AssertUtils.fail(exceptionSupplier, runnable);
        }
    }

}
