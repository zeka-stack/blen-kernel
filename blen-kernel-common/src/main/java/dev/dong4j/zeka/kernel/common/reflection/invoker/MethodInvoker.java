package dev.dong4j.zeka.kernel.common.reflection.invoker;

import dev.dong4j.zeka.kernel.common.reflection.Reflector;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.3.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.04.12 11:46
 * @since 1.0.0
 */
public class MethodInvoker implements Invoker {

    /** Type */
    private final Class<?> type;
    /** Method */
    private final Method method;

    /**
     * Method invoker
     *
     * @param method method
     * @since 1.0.0
     */
    public MethodInvoker(@NotNull Method method) {
        this.method = method;

        if (method.getParameterTypes().length == 1) {
            this.type = method.getParameterTypes()[0];
        } else {
            this.type = method.getReturnType();
        }
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
        try {
            return this.method.invoke(target, args);
        } catch (IllegalAccessException e) {
            if (Reflector.canControlMemberAccessible()) {
                this.method.setAccessible(true);
                return this.method.invoke(target, args);
            } else {
                throw e;
            }
        }
    }

    /**
     * Gets type *
     *
     * @return the type
     * @since 1.0.0
     */
    @Override
    public Class<?> getType() {
        return this.type;
    }
}
