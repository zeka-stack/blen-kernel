package dev.dong4j.zeka.kernel.common.reflection.invoker;

import java.lang.reflect.InvocationTargetException;

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.3.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.04.12 11:46
 * @since 1.0.0
 */
public interface Invoker {
    /**
     * Invoke object
     *
     * @param target target
     * @param args   args
     * @return the object
     * @throws IllegalAccessException    illegal access exception
     * @throws InvocationTargetException invocation target exception
     * @since 1.0.0
     */
    Object invoke(Object target, Object[] args) throws IllegalAccessException, InvocationTargetException;

    /**
     * Gets type *
     *
     * @return the type
     * @since 1.0.0
     */
    Class<?> getType();
}
