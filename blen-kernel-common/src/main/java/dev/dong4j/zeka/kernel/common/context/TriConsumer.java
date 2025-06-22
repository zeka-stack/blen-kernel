package dev.dong4j.zeka.kernel.common.context;

/**
 * <p>Description:  </p>
 *
 * @param <K> parameter
 * @param <V> parameter
 * @param <S> parameter
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.06.04 19:28
 * @since 1.4.0
 */
public interface TriConsumer<K, V, S> {

    /**
     * Performs the operation given the specified arguments.
     *
     * @param k the first input argument
     * @param v the second input argument
     * @param s the third input argument
     * @since 1.5.0
     */
    void accept(K k, V v, S s);
}
