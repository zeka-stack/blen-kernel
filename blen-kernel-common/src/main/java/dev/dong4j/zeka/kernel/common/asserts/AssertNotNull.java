package dev.dong4j.zeka.kernel.common.asserts;

import dev.dong4j.zeka.kernel.common.exception.AssertionFailedException;
import dev.dong4j.zeka.kernel.common.function.CheckedRunnable;
import java.util.function.Supplier;
import org.springframework.lang.Nullable;

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.11.18 17:12
 * @since 1.0.0
 */
final class AssertNotNull {

    /**
     * Assert not null
     *
     * @since 1.0.0
     */
    private AssertNotNull() {
    }

    /**
     * Not null
     *
     * @param actual actual
     * @since 1.0.0
     */
    static void notNull(Object actual) {
        notNull(actual, "参数不能为 null");
    }

    /**
     * Not null
     *
     * @param actual  actual
     * @param message message
     * @since 1.0.0
     */
    static void notNull(Object actual, String message) {
        notNull(actual, () -> new AssertionFailedException(message));
    }

    /**
     * Not null
     *
     * @param actual          actual
     * @param messageSupplier message supplier
     * @since 1.0.0
     */
    static void notNull(Object actual, Supplier<? extends RuntimeException> messageSupplier) {
        notNull(actual, messageSupplier, () -> {
        });
    }

    /**
     * Not null
     *
     * @param object            object
     * @param exceptionSupplier exception supplier
     * @param runnable          runnable
     * @since 1.0.0
     */
    static void notNull(@Nullable Object object, Supplier<? extends RuntimeException> exceptionSupplier, CheckedRunnable runnable) {
        if (object == null) {
            AssertUtils.fail(exceptionSupplier, runnable);
        }
    }

}
