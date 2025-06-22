package dev.dong4j.zeka.kernel.common.function;

/**
 * <p>Description: 受检的 Comparator </p>
 *
 * @param <T> parameter
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.27 14:55
 * @since 1.0.0
 */
@FunctionalInterface
@SuppressWarnings("java:S112")
public interface CheckedComparator<T> {

    /**
     * Compares its two arguments for order.
     *
     * @param o1 o1
     * @param o2 o2
     * @return int int
     * @throws Throwable CheckedException
     * @since 1.0.0
     */
    int compare(T o1, T o2) throws Throwable;

}
