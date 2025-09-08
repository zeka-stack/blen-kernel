package dev.dong4j.zeka.kernel.common.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * <p>Description:  </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.07.11 13:52
 * @since 1.0.0
 */
@Slf4j
class RandomUtilsTest {
    /**
     * Test 1
     *
     * @since 1.0.0
     */
    @Test
    void test_1() {
        Tools.repeat(1000, () -> {
            log.info("{}", RandomUtils.nextInt(32));
        });
    }
}
