package dev.dong4j.zeka.kernel.common.asserts;

import dev.dong4j.zeka.kernel.common.exception.AssertionFailedException;
import dev.dong4j.zeka.kernel.common.function.CheckedRunnable;
import java.util.function.Supplier;
import org.jetbrains.annotations.Contract;

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.11.18 18:06
 * @since 1.0.0
 */
@SuppressWarnings("all")
final class AssertEquals {

    /** default_message */
    private static final String DEFAULT_MESSAGE = "2 个参数不相等";

    /**
     * Assert equals
     *
     * @since 1.0.0
     */
    private AssertEquals() {
    }

    /**
     * Equals
     *
     * @param expected expected
     * @param actual   actual
     * @since 1.0.0
     */
    static void equals(byte expected, byte actual) {
        equals(expected, actual, DEFAULT_MESSAGE);
    }

    /**
     * Equals
     *
     * @param expected expected
     * @param actual   actual
     * @param message  message
     * @since 1.0.0
     */
    static void equals(byte expected, byte actual, String message) {
        equals(expected, actual, () -> new AssertionFailedException(message));
    }

    /**
     * Equals
     *
     * @param expected        expected
     * @param actual          actual
     * @param messageSupplier message supplier
     * @since 1.0.0
     */
    static void equals(byte expected, byte actual, Supplier<? extends RuntimeException> messageSupplier) {
        if (expected != actual) {
            failNotEqual(expected, actual, messageSupplier);
        }
    }

    /**
     * Equals
     *
     * @param expected expected
     * @param actual   actual
     * @since 1.0.0
     */
    static void equals(char expected, char actual) {
        equals(expected, actual, DEFAULT_MESSAGE);
    }

    /**
     * Equals
     *
     * @param expected expected
     * @param actual   actual
     * @param message  message
     * @since 1.0.0
     */
    static void equals(char expected, char actual, String message) {
        equals(expected, actual, () -> new AssertionFailedException(message));
    }

    /**
     * Equals
     *
     * @param expected        expected
     * @param actual          actual
     * @param messageSupplier message supplier
     * @since 1.0.0
     */
    static void equals(char expected, char actual, Supplier<? extends RuntimeException> messageSupplier) {
        equals(expected, actual, messageSupplier, () -> {
        });
    }

    /**
     * Equals
     *
     * @param expected        expected
     * @param actual          actual
     * @param messageSupplier message supplier
     * @param runnable        runnable
     * @since 1.0.0
     */
    static void equals(char expected, char actual, Supplier<? extends RuntimeException> messageSupplier, CheckedRunnable runnable) {
        if (expected != actual) {
            failNotEqual(expected, actual, messageSupplier, runnable);
        }
    }

    /**
     * Equals
     *
     * @param expected expected
     * @param actual   actual
     * @since 1.0.0
     */
    static void equals(double expected, double actual) {
        equals(expected, actual, DEFAULT_MESSAGE);
    }

    /**
     * Equals
     *
     * @param expected expected
     * @param actual   actual
     * @param message  message
     * @since 1.0.0
     */
    static void equals(double expected, double actual, String message) {
        equals(expected, actual, () -> new AssertionFailedException(message));
    }

    /**
     * Equals
     *
     * @param expected        expected
     * @param actual          actual
     * @param messageSupplier message supplier
     * @since 1.0.0
     */
    static void equals(double expected, double actual, Supplier<? extends RuntimeException> messageSupplier) {
        if (!AssertUtils.doublesAreEqual(expected, actual)) {
            failNotEqual(expected, actual, messageSupplier);
        }
    }

    /**
     * Equals
     *
     * @param expected expected
     * @param actual   actual
     * @param delta    delta
     * @since 1.0.0
     */
    static void equals(double expected, double actual, double delta) {
        equals(expected, actual, delta, DEFAULT_MESSAGE);
    }

    /**
     * Equals
     *
     * @param expected expected
     * @param actual   actual
     * @param delta    delta
     * @param message  message
     * @since 1.0.0
     */
    static void equals(double expected, double actual, double delta, String message) {
        equals(expected, actual, () -> new AssertionFailedException(message));
    }

    /**
     * Equals
     *
     * @param expected        expected
     * @param actual          actual
     * @param delta           delta
     * @param messageSupplier message supplier
     * @since 1.0.0
     */
    static void equals(double expected, double actual, double delta, Supplier<? extends RuntimeException> messageSupplier) {
        if (!AssertUtils.doublesAreEqual(expected, actual, delta)) {
            failNotEqual(expected, actual, messageSupplier);
        }
    }

    /**
     * Equals
     *
     * @param expected expected
     * @param actual   actual
     * @since 1.0.0
     */
    static void equals(float expected, float actual) {
        equals(expected, actual, DEFAULT_MESSAGE);
    }

    /**
     * Equals
     *
     * @param expected expected
     * @param actual   actual
     * @param message  message
     * @since 1.0.0
     */
    static void equals(float expected, float actual, String message) {
        equals(expected, actual, () -> new AssertionFailedException(message));
    }

    /**
     * Equals
     *
     * @param expected        expected
     * @param actual          actual
     * @param messageSupplier message supplier
     * @since 1.0.0
     */
    static void equals(float expected, float actual, Supplier<? extends RuntimeException> messageSupplier) {
        if (!AssertUtils.floatsAreEqual(expected, actual)) {
            failNotEqual(expected, actual, messageSupplier);
        }
    }

    /**
     * Equals
     *
     * @param expected expected
     * @param actual   actual
     * @param delta    delta
     * @since 1.0.0
     */
    static void equals(float expected, float actual, float delta) {
        equals(expected, actual, delta, DEFAULT_MESSAGE);
    }

    /**
     * Equals
     *
     * @param expected expected
     * @param actual   actual
     * @param delta    delta
     * @param message  message
     * @since 1.0.0
     */
    static void equals(float expected, float actual, float delta, String message) {
        equals(expected, actual, () -> new AssertionFailedException(message));
    }

    /**
     * Equals
     *
     * @param expected        expected
     * @param actual          actual
     * @param delta           delta
     * @param messageSupplier message supplier
     * @since 1.0.0
     */
    static void equals(float expected, float actual, float delta, Supplier<? extends RuntimeException> messageSupplier) {
        if (!AssertUtils.floatsAreEqual(expected, actual, delta)) {
            failNotEqual(expected, actual, messageSupplier);
        }
    }

    /**
     * Equals
     *
     * @param expected expected
     * @param actual   actual
     * @since 1.0.0
     */
    static void equals(short expected, short actual) {
        equals(expected, actual, DEFAULT_MESSAGE);
    }

    /**
     * Equals
     *
     * @param expected expected
     * @param actual   actual
     * @param message  message
     * @since 1.0.0
     */
    static void equals(short expected, short actual, String message) {
        equals(expected, actual, () -> new AssertionFailedException(message));
    }

    /**
     * Equals
     *
     * @param expected        expected
     * @param actual          actual
     * @param messageSupplier message supplier
     * @since 1.0.0
     */
    static void equals(short expected, short actual, Supplier<? extends RuntimeException> messageSupplier) {
        if (expected != actual) {
            failNotEqual(expected, actual, messageSupplier);
        }
    }

    /**
     * Equals
     *
     * @param expected expected
     * @param actual   actual
     * @since 1.0.0
     */
    static void equals(int expected, int actual) {
        equals(expected, actual, DEFAULT_MESSAGE);
    }

    /**
     * Equals
     *
     * @param expected expected
     * @param actual   actual
     * @param message  message
     * @since 1.0.0
     */
    static void equals(int expected, int actual, String message) {
        equals(expected, actual, () -> new AssertionFailedException(message));
    }

    /**
     * Equals
     *
     * @param expected        expected
     * @param actual          actual
     * @param messageSupplier message supplier
     * @since 1.0.0
     */
    static void equals(int expected, int actual, Supplier<? extends RuntimeException> messageSupplier) {
        if (expected != actual) {
            failNotEqual(expected, actual, messageSupplier);
        }
    }

    /**
     * Equals
     *
     * @param expected expected
     * @param actual   actual
     * @since 1.0.0
     */
    static void equals(long expected, long actual) {
        equals(expected, actual, DEFAULT_MESSAGE);
    }

    /**
     * Equals
     *
     * @param expected expected
     * @param actual   actual
     * @param message  message
     * @since 1.0.0
     */
    static void equals(long expected, long actual, String message) {
        equals(expected, actual, () -> new AssertionFailedException(message));
    }

    /**
     * Equals
     *
     * @param expected        expected
     * @param actual          actual
     * @param messageSupplier message supplier
     * @since 1.0.0
     */
    static void equals(long expected, long actual, Supplier<? extends RuntimeException> messageSupplier) {
        if (expected != actual) {
            failNotEqual(expected, actual, messageSupplier);
        }
    }

    /**
     * Equals
     *
     * @param expected expected
     * @param actual   actual
     * @since 1.0.0
     */
    static void equals(Object expected, Object actual) {
        equals(expected, actual, DEFAULT_MESSAGE);
    }

    /**
     * Equals
     *
     * @param expected expected
     * @param actual   actual
     * @param message  message
     * @since 1.0.0
     */
    static void equals(Object expected, Object actual, String message) {
        equals(expected, actual, () -> new AssertionFailedException(message));
    }

    /**
     * Equals
     *
     * @param expected        expected
     * @param actual          actual
     * @param messageSupplier message supplier
     * @since 1.0.0
     */
    static void equals(Object expected, Object actual, Supplier<? extends RuntimeException> messageSupplier) {
        if (!AssertUtils.objectsAreEqual(expected, actual)) {
            failNotEqual(expected, actual, messageSupplier);
        }
    }

    /**
     * Fail not equal
     *
     * @param expected        expected
     * @param actual          actual
     * @param messageSupplier message supplier
     * @since 1.0.0
     */
    @Contract("_, _, _ -> fail")
    static void failNotEqual(Object expected, Object actual, Supplier<? extends RuntimeException> messageSupplier) {
        failNotEqual(expected, actual, messageSupplier, () -> {
        });
    }

    /**
     * Fail not equal
     *
     * @param expected        expected
     * @param actual          actual
     * @param messageSupplier message supplier
     * @param runnable        runnable
     * @since 1.0.0
     */
    static void failNotEqual(Object expected,
                             Object actual,
                             Supplier<? extends RuntimeException> messageSupplier,
                             CheckedRunnable runnable) {
        AssertUtils.fail(messageSupplier, runnable);
    }

}
