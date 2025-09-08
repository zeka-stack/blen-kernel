package dev.dong4j.zeka.kernel.test.mock.mocker;

import dev.dong4j.zeka.kernel.test.mock.MockConfig;
import dev.dong4j.zeka.kernel.test.mock.Mocker;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import org.jetbrains.annotations.Contract;

/**
 * <p>Description: </p>
 *
 * @param <T> parameter
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.27 18:20
 * @since 1.0.0
 */
@SuppressWarnings("all")
public class BaseMocker<T> implements Mocker<T> {

    /** Type */
    private final Type type;

    /** Generic types */
    private final Type[] genericTypes;

    /**
     * Instantiates a new Base mocker.
     *
     * @param type         the type
     * @param genericTypes the generic types
     * @since 1.0.0
     */
    @Contract(pure = true)
    public BaseMocker(Type type, Type... genericTypes) {
        this.type = type;
        this.genericTypes = genericTypes;
    }

    /**
     * Mocks t.
     *
     * @param mockConfig the mock config
     * @return the t
     * @since 1.0.0
     */
    @Override
    public T mock(MockConfig mockConfig) {
        Mocker mocker;
        if (type instanceof ParameterizedType) {
            mocker = new GenericMocker((ParameterizedType) type);
        } else if (type instanceof GenericArrayType) {
            mocker = new ArrayMocker(type);
        } else if (type instanceof TypeVariable) {
            mocker = new BaseMocker(mockConfig.getVariableType(((TypeVariable) type).getName()));
        } else {
            mocker = new ClassMocker((Class) type, genericTypes);
        }
        return (T) mocker.mock(mockConfig);
    }

}
