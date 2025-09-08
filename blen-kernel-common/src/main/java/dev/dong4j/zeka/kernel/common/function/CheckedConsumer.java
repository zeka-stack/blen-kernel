package dev.dong4j.zeka.kernel.common.function;

/**
 * <p>Description: 受检的 Consumer </p>
 *
 * @param <T> parameter
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.27 14:55
 * @since 1.0.0
 */
@FunctionalInterface
@SuppressWarnings("java:S112")
public interface CheckedConsumer<T> {

    /**
     * Run the Consumer
     *
     * @param t T
     * @throws Throwable UncheckedException
     * @since 1.0.0
     */
    void accept(T t) throws Throwable;

}
