package dev.dong4j.zeka.kernel.common.function;

import org.springframework.lang.Nullable;

/**
 * <p>Description: 受检的 Callable </p>
 *
 * @param <T> the type parameter
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.27 14:54
 * @since 1.0.0
 */
@FunctionalInterface
@SuppressWarnings("java:S112")
public interface CheckedCallable<T> {

    /**
     * Run this callable.
     *
     * @return result t
     * @throws Throwable CheckedException
     * @since 1.0.0
     */
    @Nullable
    T call() throws Throwable;
}
