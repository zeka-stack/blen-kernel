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
