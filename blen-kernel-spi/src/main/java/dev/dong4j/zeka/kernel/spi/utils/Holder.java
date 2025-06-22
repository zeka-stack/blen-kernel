package dev.dong4j.zeka.kernel.spi.utils;

/**
 * <p>Description: </p>
 *
 * @param <T> parameter
 * @author dong4j
 * @version 1.8.0
 * @email "mailto:dong4j@gmaidl.com"
 * @date 2021.02.26 17:47
 * @since 1.8.0
 */
@SuppressWarnings("all")
public class Holder<T> {

    /** Value */
    private volatile T value;

    /**
     * Set
     *
     * @param value value
     * @since 1.8.0
     */
    public void set(T value) {
        this.value = value;
    }

    /**
     * Get
     *
     * @return the t
     * @since 1.8.0
     */
    public T get() {
        return value;
    }

}
