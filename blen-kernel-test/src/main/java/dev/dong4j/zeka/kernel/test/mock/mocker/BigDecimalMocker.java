package dev.dong4j.zeka.kernel.test.mock.mocker;

import dev.dong4j.zeka.kernel.common.util.RandomUtils;
import dev.dong4j.zeka.kernel.test.mock.MockConfig;
import dev.dong4j.zeka.kernel.test.mock.Mocker;
import java.math.BigDecimal;
import org.jetbrains.annotations.NotNull;

/**
 * <p>Description: BigDecimal对象模拟器 </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.27 18:20
 * @since 1.0.0
 */
public class BigDecimalMocker implements Mocker<BigDecimal> {

    /**
     * Mocks big decimal.
     *
     * @param mockConfig the mock config
     * @return the big decimal
     * @since 1.0.0
     */
    @Override
    public BigDecimal mock(@NotNull MockConfig mockConfig) {
        return BigDecimal.valueOf(RandomUtils.nextDouble(mockConfig.getDoubleRange()[0], mockConfig.getDoubleRange()[1]));
    }

}
