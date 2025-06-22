package dev.dong4j.zeka.kernel.common.reflection.invoker;

import dev.dong4j.zeka.kernel.common.reflection.Reflector;
import org.jetbrains.annotations.Contract;

import java.lang.reflect.Field;

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.3.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.04.12 11:46
 * @since 1.0.0
 */
public class GetFieldInvoker implements Invoker {
    /** Field */
    private final Field field;

    /**
     * Get field invoker
     *
     * @param field field
     * @since 1.0.0
     */
    @Contract(pure = true)
    public GetFieldInvoker(Field field) {
        this.field = field;
    }

    /**
     * Invoke object
     *
     * @param target target
     * @param args   args
     * @return the object
     * @throws IllegalAccessException illegal access exception
     * @since 1.0.0
     */
    @Override
    public Object invoke(Object target, Object[] args) throws IllegalAccessException {
        try {
            return this.field.get(target);
        } catch (IllegalAccessException e) {
            if (Reflector.canControlMemberAccessible()) {
                this.field.setAccessible(true);
                return this.field.get(target);
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
        return this.field.getType();
    }
}
