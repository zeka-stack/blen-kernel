package dev.dong4j.zeka.kernel.common.type;

import dev.dong4j.zeka.kernel.common.type.exception.TypeException;
import lombok.Getter;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * <p>Description: </p>
 *
 * @param <T> parameter
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.27 14:47
 * @since 1.0.0
 */
@SuppressWarnings("checkyle:HiddenField")
public abstract class TypeToken<T> {
    /** Type */
    @Getter
    private final Type type;

    /**
     * Instantiates a new Type token.
     *
     * @since 1.0.0
     */
    protected TypeToken() {
        Type superclass = this.getClass().getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new TypeException("No generics found!");
        }
        this.type = ((ParameterizedType) superclass).getActualTypeArguments()[0];
    }
}
