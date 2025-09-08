package dev.dong4j.zeka.kernel.common.support;

import dev.dong4j.zeka.kernel.common.util.Exceptions;
import java.util.Objects;
import java.util.function.Function;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * <p>Description: Lambda 受检异常 </p>
 * <a href="https://segmentfault.com/a/1190000007832130">...</a>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.27 14:52
 * @since 1.0.0
 */
public class Try {

    /**
     * Of function.
     *
     * @param <T>    the type parameter
     * @param <R>    the type parameter
     * @param mapper the mapper
     * @return the function
     * @since 1.0.0
     */
    @NotNull
    @Contract(pure = true)
    public static <T, R> Function<T, R> of(UncheckedFunction<T, R> mapper) {
        Objects.requireNonNull(mapper);
        return t -> {
            try {
                return mapper.apply(t);
            } catch (Exception e) {
                throw Exceptions.unchecked(e);
            }
        };
    }

    /**
     * The interface Unchecked function.
     *
     * @param <T> the type parameter
     * @param <R> the type parameter
     * @author dong4j
     * @version 1.0.0
     * @email "mailto:dong4j@gmail.com"
     * @date 2020.01.27 14:52
     * @since 1.0.0
     */
    @FunctionalInterface
    public interface UncheckedFunction<T, R> {
        /**
         * 调用
         *
         * @param t t
         * @return R r
         * @throws Exception Exception
         * @since 1.0.0
         */
        R apply(T t) throws Exception;
    }
}
