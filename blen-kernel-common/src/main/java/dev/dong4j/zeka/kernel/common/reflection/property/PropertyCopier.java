package dev.dong4j.zeka.kernel.common.reflection.property;

import dev.dong4j.zeka.kernel.common.reflection.Reflector;
import org.jetbrains.annotations.Contract;

import java.lang.reflect.Field;

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.3.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.04.12 11:47
 * @since 1.0.0
 */
public final class PropertyCopier {

    /**
     * Property copier
     *
     * @since 1.0.0
     */
    @Contract(pure = true)
    private PropertyCopier() {
        // Prevent Instantiation of Static Class
    }

    /**
     * Copy bean properties *
     *
     * @param type            type
     * @param sourceBean      source bean
     * @param destinationBean destination bean
     * @since 1.0.0
     */
    public static void copyBeanProperties(Class<?> type, Object sourceBean, Object destinationBean) {
        Class<?> parent = type;
        while (parent != null) {
            Field[] fields = parent.getDeclaredFields();
            for (Field field : fields) {
                try {
                    try {
                        field.set(destinationBean, field.get(sourceBean));
                    } catch (IllegalAccessException e) {
                        if (Reflector.canControlMemberAccessible()) {
                            field.setAccessible(true);
                            field.set(destinationBean, field.get(sourceBean));
                        } else {
                            throw e;
                        }
                    }
                } catch (Exception e) {
                    // Nothing useful to do, will only fail on final fields, which will be ignored.
                }
            }
            parent = parent.getSuperclass();
        }
    }

}
