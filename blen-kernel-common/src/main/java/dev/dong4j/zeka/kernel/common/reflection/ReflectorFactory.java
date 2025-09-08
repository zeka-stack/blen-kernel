package dev.dong4j.zeka.kernel.common.reflection;

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.04.12 11:56
 * @since 1.0.0
 */
public interface ReflectorFactory {

    /**
     * Is class cache enabled boolean
     *
     * @return the boolean
     * @since 1.0.0
     */
    boolean isClassCacheEnabled();

    /**
     * Sets class cache enabled *
     *
     * @param classCacheEnabled class cache enabled
     * @since 1.0.0
     */
    void setClassCacheEnabled(boolean classCacheEnabled);

    /**
     * Find for class reflector
     *
     * @param type type
     * @return the reflector
     * @since 1.0.0
     */
    Reflector findForClass(Class<?> type);
}
