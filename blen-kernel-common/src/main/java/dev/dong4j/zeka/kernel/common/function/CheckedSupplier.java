package dev.dong4j.zeka.kernel.common.function;

import org.springframework.lang.Nullable;

/**
 * <p>Description: 受检的 Supplier </p>
 *
 * @param <T> parameter
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.27 14:54
 * @since 1.0.0
 */
@FunctionalInterface
@SuppressWarnings("java:S112")
public interface CheckedSupplier<T> {

    /**
     * Run the Supplier
     *
     * @return T t
     * @throws Throwable CheckedException
     * @since 1.0.0
     */
    @Nullable
    T get() throws Throwable;

}
