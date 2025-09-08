package dev.dong4j.zeka.kernel.common.function;

import org.springframework.lang.Nullable;

/**
 * <p>Description: 受检的 function</p>
 *
 * @param <T> parameter
 * @param <R> parameter
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.27 14:55
 * @since 1.0.0
 */
@FunctionalInterface
@SuppressWarnings("java:S112")
public interface CheckedFunction<T, R> {

    /**
     * Run the Function
     *
     * @param t T
     * @return R R
     * @throws Throwable CheckedException
     * @since 1.0.0
     */
    @Nullable
    R apply(@Nullable T t) throws Throwable;

}
