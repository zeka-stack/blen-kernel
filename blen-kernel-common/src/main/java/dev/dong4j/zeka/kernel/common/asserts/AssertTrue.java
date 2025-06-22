package dev.dong4j.zeka.kernel.common.asserts;

import dev.dong4j.zeka.kernel.common.exception.AssertionFailedException;
import dev.dong4j.zeka.kernel.common.function.CheckedRunnable;

import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.11.18 00:26
 * @since 1.7.0
 */
final class AssertTrue {
    /**
     * Assert true
     *
     * @since 1.7.0
     */
    private AssertTrue() {
    }

    /**
     * Is true
     *
     * @param booleanSupplier boolean supplier
     * @since 1.7.0
     */
    static void isTrue(BooleanSupplier booleanSupplier) {
        isTrue(booleanSupplier.getAsBoolean());
    }

    /**
     * Is true
     *
     * @param booleanSupplier boolean supplier
     * @param message         message
     * @since 1.7.0
     */
    static void isTrue(BooleanSupplier booleanSupplier, String message) {
        isTrue(booleanSupplier.getAsBoolean(), message);
    }

    /**
     * Is true
     *
     * @param booleanSupplier   boolean supplier
     * @param exceptionSupplier message supplier
     * @since 1.7.0
     */
    static void isTrue(BooleanSupplier booleanSupplier, Supplier<? extends RuntimeException> exceptionSupplier) {
        isTrue(booleanSupplier.getAsBoolean(), exceptionSupplier);
    }

    /**
     * Is true
     *
     * @param condition condition
     * @since 1.6.0
     */
    static void isTrue(boolean condition) {
        isTrue(condition, "表达式必须是 true");
    }

    /**
     * Is true
     *
     * @param condition condition
     * @param message   message
     * @since 1.6.0
     */
    static void isTrue(boolean condition, String message) {
        isTrue(condition, () -> new AssertionFailedException(message));
    }

    /**
     * Is true
     *
     * @param condition         condition
     * @param exceptionSupplier exception supplier
     * @since 1.6.0
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
     * @since 1.7.0
     */
    static void isTrue(boolean condition, Supplier<? extends RuntimeException> exceptionSupplier, CheckedRunnable runnable) {
        if (!condition) {
            AssertUtils.fail(exceptionSupplier, runnable);
        }
    }

}
