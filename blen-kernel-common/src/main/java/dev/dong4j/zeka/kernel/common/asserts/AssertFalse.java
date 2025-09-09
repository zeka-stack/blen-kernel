package dev.dong4j.zeka.kernel.common.asserts;

import dev.dong4j.zeka.kernel.common.exception.AssertionFailedException;
import dev.dong4j.zeka.kernel.common.function.CheckedRunnable;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

/**
 * <p>False 断言工具类.
 * <p>专门用于验证布尔值为 false 的断言操作，支持直接布尔值和布尔供应器.
 * <p>提供了灵活的错误处理机制，支持自定义错误消息和异常后操作.
 * <p>主要功能：
 * <ul>
 *     <li>验证布尔表达式的结果为 false</li>
 *     <li>支持 BooleanSupplier 的延迟计算</li>
 *     <li>自定义错误消息和异常类型</li>
 *     <li>支持异常后的清理操作</li>
 * </ul>
 * <p>使用场景：
 * <ul>
 *     <li>参数校验和前置条件检查</li>
 *     <li>业务规则验证和状态检查</li>
 *     <li>单元测试中的期望结果验证</li>
 *     <li>调试和开发阶段的断言检查</li>
 * </ul>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.11.18 16:55
 * @since 1.0.0
 */
final class AssertFalse {
    /**
     * Assert false
     *
     * @since 1.0.0
     */
    private AssertFalse() {
    }

    /**
     * Is false
     *
     * @param booleanSupplier boolean supplier
     * @since 1.0.0
     */
    static void isFalse(BooleanSupplier booleanSupplier) {
        isFalse(booleanSupplier.getAsBoolean());
    }

    /**
     * Is false
     *
     * @param booleanSupplier boolean supplier
     * @param message         message
     * @since 1.0.0
     */
    static void isFalse(BooleanSupplier booleanSupplier, String message) {
        isFalse(booleanSupplier.getAsBoolean(), message);
    }

    /**
     * Is false
     *
     * @param booleanSupplier boolean supplier
     * @param messageSupplier message supplier
     * @since 1.0.0
     */
    static void isFalse(BooleanSupplier booleanSupplier, Supplier<? extends RuntimeException> messageSupplier) {
        isFalse(booleanSupplier.getAsBoolean(), messageSupplier);
    }

    /**
     * Is true
     *
     * @param condition condition
     * @since 1.0.0
     */
    static void isFalse(boolean condition) {
        isFalse(condition, "表达式必须是 false");
    }

    /**
     * Is true
     *
     * @param condition condition
     * @param message   message
     * @since 1.0.0
     */
    static void isFalse(boolean condition, String message) {
        isFalse(condition, () -> new AssertionFailedException(message));
    }

    /**
     * Is true
     *
     * @param condition         condition
     * @param exceptionSupplier exception supplier
     * @since 1.0.0
     */
    static void isFalse(boolean condition, Supplier<? extends RuntimeException> exceptionSupplier) {
        isFalse(condition, exceptionSupplier, () -> {
        });
    }

    /**
     * Is false
     *
     * @param condition         condition
     * @param exceptionSupplier exception supplier
     * @param runnable          runnable
     * @since 1.0.0
     */
    static void isFalse(boolean condition, Supplier<? extends RuntimeException> exceptionSupplier, CheckedRunnable runnable) {
        if (condition) {
            AssertUtils.fail(exceptionSupplier, runnable);
        }
    }
}
