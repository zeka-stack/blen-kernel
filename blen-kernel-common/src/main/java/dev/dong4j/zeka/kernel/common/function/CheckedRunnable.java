package dev.dong4j.zeka.kernel.common.function;

/**
 * <p>Description: 受检的 runnable </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.27 14:54
 * @since 1.0.0
 */
@FunctionalInterface
@SuppressWarnings("java:S112")
public interface CheckedRunnable {

    /**
     * Run this runnable.
     *
     * @throws Throwable CheckedException
     * @since 1.0.0
     */
    void run() throws Throwable;

}
