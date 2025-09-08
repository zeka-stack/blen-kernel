package dev.dong4j.zeka.kernel.common.reflection.invoker;

import dev.dong4j.zeka.kernel.common.reflection.Reflector;
import java.lang.reflect.Field;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.04.12 11:47
 * @since 1.0.0
 */
public class SetFieldInvoker implements Invoker {
    /** Field */
    private final Field field;

    /**
     * Set field invoker
     *
     * @param field field
     * @since 1.0.0
     */
    @Contract(pure = true)
    public SetFieldInvoker(Field field) {
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
    public Object invoke(Object target, Object @NotNull [] args) throws IllegalAccessException {
        try {
            this.field.set(target, args[0]);
        } catch (IllegalAccessException e) {
            if (Reflector.canControlMemberAccessible()) {
                this.field.setAccessible(true);
                this.field.set(target, args[0]);
            } else {
                throw e;
            }
        }
        return null;
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
