package dev.dong4j.zeka.kernel.common.reflection.invoker;

import dev.dong4j.zeka.kernel.common.reflection.ReflectionException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.04.12 11:46
 * @since 1.0.0
 */
public class AmbiguousMethodInvoker extends MethodInvoker {
    /** Exception message */
    private final String exceptionMessage;

    /**
     * Ambiguous method invoker
     *
     * @param method           method
     * @param exceptionMessage exception message
     * @since 1.0.0
     */
    public AmbiguousMethodInvoker(Method method, String exceptionMessage) {
        super(method);
        this.exceptionMessage = exceptionMessage;
    }

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
    @Override
    public Object invoke(Object target, Object[] args) throws IllegalAccessException, InvocationTargetException {
        throw new ReflectionException(this.exceptionMessage);
    }
}
