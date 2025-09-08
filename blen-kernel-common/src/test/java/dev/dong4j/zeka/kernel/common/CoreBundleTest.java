package dev.dong4j.zeka.kernel.common;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * <p>Description:  </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.05.19 16:00
 * @since 1.0.0
 */
@Slf4j
class CoreBundleTest {
    /**
     * Test 1
     *
     * @since 1.0.0
     */
    @Test
    void test_1() {
        log.info("{}", CoreBundle.message("a"));
        log.info("{}", CoreBundle.message("b", "aaaaa"));
    }
}
