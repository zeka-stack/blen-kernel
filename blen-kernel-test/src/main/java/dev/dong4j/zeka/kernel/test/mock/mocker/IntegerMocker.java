package dev.dong4j.zeka.kernel.test.mock.mocker;

import dev.dong4j.zeka.kernel.common.util.RandomUtils;
import dev.dong4j.zeka.kernel.test.mock.MockConfig;
import dev.dong4j.zeka.kernel.test.mock.Mocker;
import org.jetbrains.annotations.NotNull;

/**
 * <p>Description: Integer对象模拟器</p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.27 18:19
 * @since 1.0.0
 */
public class IntegerMocker implements Mocker<Integer> {

    /**
     * Mocks integer.
     *
     * @param mockConfig the mock config
     * @return the integer
     * @since 1.0.0
     */
    @Override
    public Integer mock(@NotNull MockConfig mockConfig) {
        return RandomUtils.nextInt(mockConfig.getIntRange()[0], mockConfig.getIntRange()[1]);
    }

}
