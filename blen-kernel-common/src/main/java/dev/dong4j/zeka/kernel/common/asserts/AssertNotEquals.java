package dev.dong4j.zeka.kernel.common.asserts;

import dev.dong4j.zeka.kernel.common.exception.AssertionFailedException;

import java.util.function.Supplier;

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.11.18 18:06
 * @since 1.7.0
 */
@SuppressWarnings("all")
final class AssertNotEquals {
    /** DEFAULT_MESSAGE */
    private static final String DEFAULT_MESSAGE = "2 个参数不能相等";

    /**
     * Assert not equals
     *
     * @since 1.7.0
     */
    private AssertNotEquals() {
    }

    /**
     * Not equals
     *
     * @param unexpected unexpected
     * @param actual     actual
     * @since 1.7.0
     */
    static void notEquals(byte unexpected, byte actual) {
        notEquals(unexpected, actual, DEFAULT_MESSAGE);
    }

    /**
     * Not equals
     *
     * @param unexpected unexpected
     * @param actual     actual
     * @param message    message
     * @since 1.7.0
     */
    static void notEquals(byte unexpected, byte actual, String message) {
        notEquals(unexpected, actual, () -> new AssertionFailedException(message));
    }

    /**
     * Not equals
     *
     * @param unexpected      unexpected
     * @param actual          actual
     * @param messageSupplier message supplier
     * @since 1.7.0
     */
    static void notEquals(byte unexpected, byte actual, Supplier<? extends RuntimeException> messageSupplier) {
        if (unexpected == actual) {
            failEqual(actual, messageSupplier);
        }
    }

    /**
     * Not equals
     *
     * @param unexpected unexpected
     * @param actual     actual
     * @since 1.7.0
     */
    static void notEquals(short unexpected, short actual) {
        notEquals(unexpected, actual, DEFAULT_MESSAGE);
    }

    /**
     * Not equals
     *
     * @param unexpected unexpected
     * @param actual     actual
     * @param message    message
     * @since 1.7.0
     */
    static void notEquals(short unexpected, short actual, String message) {
        notEquals(unexpected, actual, () -> new AssertionFailedException(message));
    }

    /**
     * Not equals
     *
     * @param unexpected      unexpected
     * @param actual          actual
     * @param messageSupplier message supplier
     * @since 1.7.0
     */
    static void notEquals(short unexpected, short actual, Supplier<? extends RuntimeException> messageSupplier) {
        if (unexpected == actual) {
            failEqual(actual, messageSupplier);
        }
    }

    /**
     * Not equals
     *
     * @param unexpected unexpected
     * @param actual     actual
     * @since 1.7.0
     */
    static void notEquals(int unexpected, int actual) {
        notEquals(unexpected, actual, DEFAULT_MESSAGE);
    }

    /**
     * Not equals
     *
     * @param unexpected unexpected
     * @param actual     actual
     * @param message    message
     * @since 1.7.0
     */
    static void notEquals(int unexpected, int actual, String message) {
        notEquals(unexpected, actual, () -> new AssertionFailedException(message));
    }

    /**
     * Not equals
     *
     * @param unexpected      unexpected
     * @param actual          actual
     * @param messageSupplier message supplier
     * @since 1.7.0
     */
    static void notEquals(int unexpected, int actual, Supplier<? extends RuntimeException> messageSupplier) {
        if (unexpected == actual) {
            failEqual(actual, messageSupplier);
        }
    }

    /**
     * Not equals
     *
     * @param unexpected unexpected
     * @param actual     actual
     * @since 1.7.0
     */
    static void notEquals(long unexpected, long actual) {
        notEquals(unexpected, actual, DEFAULT_MESSAGE);
    }

    /**
     * Not equals
     *
     * @param unexpected unexpected
     * @param actual     actual
     * @param message    message
     * @since 1.7.0
     */
    static void notEquals(long unexpected, long actual, String message) {
        notEquals(unexpected, actual, () -> new AssertionFailedException(message));
    }

    /**
     * Not equals
     *
     * @param unexpected      unexpected
     * @param actual          actual
     * @param messageSupplier message supplier
     * @since 1.7.0
     */
    static void notEquals(long unexpected, long actual, Supplier<? extends RuntimeException> messageSupplier) {
        if (unexpected == actual) {
            failEqual(actual, messageSupplier);
        }
    }

    /**
     * Not equals
     *
     * @param unexpected unexpected
     * @param actual     actual
     * @since 1.7.0
     */
    static void notEquals(float unexpected, float actual) {
        notEquals(unexpected, actual, DEFAULT_MESSAGE);
    }

    /**
     * Not equals
     *
     * @param unexpected unexpected
     * @param actual     actual
     * @param message    message
     * @since 1.7.0
     */
    static void notEquals(float unexpected, float actual, String message) {
        notEquals(unexpected, actual, () -> new AssertionFailedException(message));
    }

    /**
     * Not equals
     *
     * @param unexpected      unexpected
     * @param actual          actual
     * @param messageSupplier message supplier
     * @since 1.7.0
     */
    static void notEquals(float unexpected, float actual, Supplier<? extends RuntimeException> messageSupplier) {
        if (AssertUtils.floatsAreEqual(unexpected, actual)) {
            failEqual(actual, messageSupplier);
        }
    }

    /**
     * Not equals
     *
     * @param unexpected unexpected
     * @param actual     actual
     * @param delta      delta
     * @since 1.7.0
     */
    static void notEquals(float unexpected, float actual, float delta) {
        notEquals(unexpected, actual, delta, DEFAULT_MESSAGE);
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
    static void notEquals(float unexpected, float actual, float delta, String message) {
        notEquals(unexpected, actual, () -> new AssertionFailedException(message));
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
    static void notEquals(float unexpected, float actual, float delta, Supplier<? extends RuntimeException> messageSupplier) {
        if (AssertUtils.floatsAreEqual(unexpected, actual, delta)) {
            failEqual(actual, messageSupplier);
        }
    }

    /**
     * Not equals
     *
     * @param unexpected unexpected
     * @param actual     actual
     * @since 1.7.0
     */
    static void notEquals(double unexpected, double actual) {
        notEquals(unexpected, actual, DEFAULT_MESSAGE);
    }

    /**
     * Not equals
     *
     * @param unexpected unexpected
     * @param actual     actual
     * @param message    message
     * @since 1.7.0
     */
    static void notEquals(double unexpected, double actual, String message) {
        notEquals(unexpected, actual, () -> new AssertionFailedException(message));
    }

    /**
     * Not equals
     *
     * @param unexpected      unexpected
     * @param actual          actual
     * @param messageSupplier message supplier
     * @since 1.7.0
     */
    static void notEquals(double unexpected, double actual, Supplier<? extends RuntimeException> messageSupplier) {
        if (AssertUtils.doublesAreEqual(unexpected, actual)) {
            failEqual(actual, messageSupplier);
        }
    }

    /**
     * Not equals
     *
     * @param unexpected unexpected
     * @param actual     actual
     * @param delta      delta
     * @since 1.7.0
     */
    static void notEquals(double unexpected, double actual, double delta) {
        notEquals(unexpected, actual, delta, DEFAULT_MESSAGE);
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
    static void notEquals(double unexpected, double actual, double delta, String message) {
        notEquals(unexpected, actual, () -> new AssertionFailedException(message));
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
    static void notEquals(double unexpected, double actual, double delta, Supplier<? extends RuntimeException> messageSupplier) {
        if (AssertUtils.doublesAreEqual(unexpected, actual, delta)) {
            failEqual(actual, messageSupplier);
        }
    }

    /**
     * Not equals
     *
     * @param unexpected unexpected
     * @param actual     actual
     * @since 1.7.0
     */
    static void notEquals(char unexpected, char actual) {
        notEquals(unexpected, actual, DEFAULT_MESSAGE);
    }

    /**
     * Not equals
     *
     * @param unexpected unexpected
     * @param actual     actual
     * @param message    message
     * @since 1.7.0
     */
    static void notEquals(char unexpected, char actual, String message) {
        notEquals(unexpected, actual, () -> new AssertionFailedException(message));
    }

    /**
     * Not equals
     *
     * @param unexpected      unexpected
     * @param actual          actual
     * @param messageSupplier message supplier
     * @since 1.7.0
     */
    static void notEquals(char unexpected, char actual, Supplier<? extends RuntimeException> messageSupplier) {
        if (unexpected == actual) {
            failEqual(actual, messageSupplier);
        }
    }

    /**
     * Not equals
     *
     * @param unexpected unexpected
     * @param actual     actual
     * @since 1.7.0
     */
    static void notEquals(Object unexpected, Object actual) {
        notEquals(unexpected, actual, DEFAULT_MESSAGE);
    }

    /**
     * Not equals
     *
     * @param unexpected unexpected
     * @param actual     actual
     * @param message    message
     * @since 1.7.0
     */
    static void notEquals(Object unexpected, Object actual, String message) {
        notEquals(unexpected, actual, () -> new AssertionFailedException(message));
    }

    /**
     * Not equals
     *
     * @param unexpected      unexpected
     * @param actual          actual
     * @param messageSupplier message supplier
     * @since 1.7.0
     */
    static void notEquals(Object unexpected, Object actual, Supplier<? extends RuntimeException> messageSupplier) {
        if (AssertUtils.objectsAreEqual(unexpected, actual)) {
            AssertUtils.fail(messageSupplier);
        }
    }

    /**
     * Fail equal
     *
     * @param actual          actual
     * @param messageSupplier message supplier
     * @since 1.7.0
     */
    private static void failEqual(Object actual, Supplier<? extends RuntimeException> messageSupplier) {
        AssertUtils.fail(messageSupplier);
    }

}
