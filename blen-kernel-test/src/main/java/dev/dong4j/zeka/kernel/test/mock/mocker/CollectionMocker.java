package dev.dong4j.zeka.kernel.test.mock.mocker;

import dev.dong4j.zeka.kernel.common.util.RandomUtils;
import dev.dong4j.zeka.kernel.test.mock.MockConfig;
import dev.dong4j.zeka.kernel.test.mock.Mocker;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import org.jetbrains.annotations.Contract;

/**
 * <p>Description: 模拟Collection </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.27 18:25
 * @since 1.0.0
 */
public class CollectionMocker implements Mocker<Object> {

    /** Clazz */
    private final Class<?> clazz;

    /** Generic type */
    private final Type genericType;

    /**
     * Instantiates a new Collection mocker.
     *
     * @param clazz       the clazz
     * @param genericType the generic type
     * @since 1.0.0
     */
    @Contract(pure = true)
    CollectionMocker(Class<?> clazz, Type genericType) {
        this.clazz = clazz;
        this.genericType = genericType;
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
        int size = RandomUtils.nextSize(mockConfig.getSizeRange()[0], mockConfig.getSizeRange()[1]);
        Collection<Object> result;
        if (List.class.isAssignableFrom(this.clazz)) {
            result = new ArrayList<>(size);
        } else {
            result = new HashSet<>(size);
        }
        BaseMocker<?> baseMocker = new BaseMocker<>(this.genericType);
        for (int index = 0; index < size; index++) {
            result.add(baseMocker.mock(mockConfig));
        }
        return result;
    }

}
