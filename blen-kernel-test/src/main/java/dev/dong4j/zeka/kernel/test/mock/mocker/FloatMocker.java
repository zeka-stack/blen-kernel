package dev.dong4j.zeka.kernel.test.mock.mocker;

import dev.dong4j.zeka.kernel.common.util.RandomUtils;
import dev.dong4j.zeka.kernel.test.mock.MockConfig;
import dev.dong4j.zeka.kernel.test.mock.Mocker;
import org.jetbrains.annotations.NotNull;

/**
 * <p>Description: Float对象模拟器</p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.27 18:20
 * @since 1.0.0
 */
public class FloatMocker implements Mocker<Float> {

    /**
     * Mocks float.
     *
     * @param mockConfig the mock config
     * @return the float
     * @since 1.0.0
     */
    @Override
    public Float mock(@NotNull MockConfig mockConfig) {
        return RandomUtils.nextFloat(mockConfig.getFloatRange()[0], mockConfig.getFloatRange()[1]);
    }

}
