package dev.dong4j.zeka.kernel.test.mock.mocker;

import dev.dong4j.zeka.kernel.test.mock.MockConfig;
import dev.dong4j.zeka.kernel.test.mock.Mocker;
import org.jetbrains.annotations.Contract;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.27 18:26
 * @since 1.0.0
 */
public class ClassMocker implements Mocker<Object> {

    /** Clazz */
    private final Class<?> clazz;

    /** Generic types */
    private final Type[] genericTypes;

    /**
     * Instantiates a new Class mocker.
     *
     * @param clazz        the clazz
     * @param genericTypes the generic types
     * @since 1.0.0
     */
    @Contract(pure = true)
    ClassMocker(Class<?> clazz, Type[] genericTypes) {
        this.clazz = clazz;
        this.genericTypes = genericTypes;
    }

    /**
     * Mocks object.
     *
     * @param mockConfig the mock config
     * @return the object
     * @since 1.0.0
     */
    @Override
    public Object mock(MockConfig mockConfig) {
        Mocker<?> mocker;
        if (this.clazz.isArray()) {
            mocker = new ArrayMocker(this.clazz);
        } else if (Map.class.isAssignableFrom(this.clazz)) {
            mocker = new MapMocker(this.genericTypes);
        } else if (Collection.class.isAssignableFrom(this.clazz)) {
            mocker = new CollectionMocker(this.clazz, this.genericTypes[0]);
        } else if (this.clazz.isEnum()) {
            mocker = new EnumMocker<>(this.clazz);
        } else {
            mocker = mockConfig.getMocker(this.clazz);
            if (mocker == null) {
                mocker = new BeanMocker(this.clazz);
            }
        }
        return mocker.mock(mockConfig);
    }

}
