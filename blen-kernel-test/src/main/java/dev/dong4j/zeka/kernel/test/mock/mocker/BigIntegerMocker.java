package dev.dong4j.zeka.kernel.test.mock.mocker;

import dev.dong4j.zeka.kernel.common.util.RandomUtils;
import dev.dong4j.zeka.kernel.test.mock.MockConfig;
import dev.dong4j.zeka.kernel.test.mock.Mocker;
import java.math.BigInteger;
import org.jetbrains.annotations.NotNull;

/**
 * <p>Description: BigInteger对象模拟器 </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.27 18:20
 * @since 1.0.0
 */
public class BigIntegerMocker implements Mocker<BigInteger> {

    /**
     * Mocks big integer.
     *
     * @param mockConfig the mock config
     * @return the big integer
     * @since 1.0.0
     */
    @Override
    public BigInteger mock(@NotNull MockConfig mockConfig) {
        return BigInteger.valueOf(RandomUtils.nextLong(mockConfig.getLongRange()[0], mockConfig.getLongRange()[1]));
    }

}
