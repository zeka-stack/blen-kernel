package dev.dong4j.zeka.kernel.test.mock.mocker;

import dev.dong4j.zeka.kernel.common.util.RandomUtils;
import dev.dong4j.zeka.kernel.test.mock.MockConfig;
import dev.dong4j.zeka.kernel.test.mock.Mocker;
import org.jetbrains.annotations.NotNull;

/**
 * <p>Description: 模拟Long对象</p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.27 18:26
 * @since 1.0.0
 */
public class LongMocker implements Mocker<Long> {

    /**
     * Mocks long.
     *
     * @param mockConfig the mock config
     * @return the long
     * @since 1.0.0
     */
    @Override
    public Long mock(@NotNull MockConfig mockConfig) {
        return RandomUtils.nextLong(mockConfig.getLongRange()[0], mockConfig.getLongRange()[1]);
    }

}
