package dev.dong4j.zeka.kernel.common.util;

import java.io.Serial;
import java.io.Serializable;
import java.util.function.Supplier;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * <p>Description: Holder of a value that is computed lazy. </p>
 *
 * @param <T> the type parameter
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.27 18:26
 * @since 1.0.0
 */
public final class Lazy<T> implements Supplier<T>, Serializable {

    /** serialVersionUID */
    @Serial
    private static final long serialVersionUID = -7146199805533234594L;
    /** Supplier */
    private transient volatile Supplier<? extends T> supplier;
    /** Value */
    private T value;

    /**
     * Lazy
     *
     * @param supplier supplier
     * @since 1.0.0
     */
    @Contract(pure = true)
    private Lazy(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    /**
     * Creates new instance of Lazy.
     *
     * @param <T>      泛型标记
     * @param supplier Supplier
     * @return Lazy lazy
     * @since 1.0.0
     */
    @NotNull
    @Contract(value = "_ -> new", pure = true)
    public static <T> Lazy<T> of(Supplier<T> supplier) {
        return new Lazy<>(supplier);
    }

    /**
     * Returns the value. Value will be computed on first call.
     *
     * @return lazy value
     * @since 1.0.0
     */
    @Override
    public T get() {
        return (this.supplier == null) ? this.value : this.computeValue();
    }

    /**
     * Compute value t
     *
     * @return the t
     * @since 1.0.0
     */
    private synchronized T computeValue() {
        Supplier<? extends T> s = this.supplier;
        if (s != null) {
            this.value = s.get();
            this.supplier = null;
        }
        return this.value;
    }

}
