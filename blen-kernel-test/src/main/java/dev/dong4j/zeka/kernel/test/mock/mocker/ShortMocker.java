package dev.dong4j.zeka.kernel.test.mock.mocker;

import dev.dong4j.zeka.kernel.common.util.RandomUtils;
import dev.dong4j.zeka.kernel.test.mock.MockConfig;
import dev.dong4j.zeka.kernel.test.mock.Mocker;
import org.jetbrains.annotations.NotNull;

/**
 * <p>Description: 模拟Short对象</p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.27 18:24
 * @since 1.0.0
 */
public class ShortMocker implements Mocker<Short> {

    /**
     * Mocks short.
     *
     * @param mockConfig the mock config
     * @return the short
     * @since 1.0.0
     */
    @Override
    public Short mock(@NotNull MockConfig mockConfig) {
        return (short) RandomUtils.nextInt(mockConfig.getShortRange()[0], mockConfig.getShortRange()[1]);
    }

}
