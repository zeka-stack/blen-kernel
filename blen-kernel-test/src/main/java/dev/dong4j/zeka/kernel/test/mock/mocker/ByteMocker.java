package dev.dong4j.zeka.kernel.test.mock.mocker;

import dev.dong4j.zeka.kernel.common.util.RandomUtils;
import dev.dong4j.zeka.kernel.test.mock.MockConfig;
import dev.dong4j.zeka.kernel.test.mock.Mocker;
import org.jetbrains.annotations.NotNull;

/**
 * <p>Description: Byte对象模拟器 </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.27 18:20
 * @since 1.0.0
 */
public class ByteMocker implements Mocker<Byte> {

    /**
     * Mocks byte.
     *
     * @param mockConfig the mock config
     * @return the byte
     * @since 1.0.0
     */
    @Override
    public Byte mock(@NotNull MockConfig mockConfig) {
        return (byte) RandomUtils.nextInt(mockConfig.getByteRange()[0], mockConfig.getByteRange()[1]);
    }

}
