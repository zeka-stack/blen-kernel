package dev.dong4j.zeka.kernel.test.mock.mocker;

import com.google.common.collect.Maps;
import dev.dong4j.zeka.kernel.common.util.RandomUtils;
import dev.dong4j.zeka.kernel.test.mock.MockConfig;
import dev.dong4j.zeka.kernel.test.mock.Mocker;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * <p>Description: 模拟Map</p>
 *
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.27 18:24
 * @since 1.0.0
 */
public class MapMocker implements Mocker<Object> {

    /** Types */
    private final Type[] types;

    /**
     * Instantiates a new Map mocker.
     *
     * @param types the types
     * @since 1.0.0
     */
    @Contract(pure = true)
    MapMocker(Type[] types) {
        this.types = types;
    }

    /**
     * Mocks object.
     *
     * @param mockConfig the mock config
     * @return the object
     * @since 1.0.0
     */
    @Override
    public Object mock(@NotNull MockConfig mockConfig) {
        int size = RandomUtils.nextSize(mockConfig.getSizeRange()[0], mockConfig.getSizeRange()[1]);
        Map<Object, Object> result = Maps.newHashMapWithExpectedSize(size);
        BaseMocker keyMocker = new BaseMocker(types[0]);
        BaseMocker valueMocker = new BaseMocker(types[1]);
        for (int index = 0; index < size; index++) {
            result.put(keyMocker.mock(mockConfig), valueMocker.mock(mockConfig));
        }
        return result;
    }

}
