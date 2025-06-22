package dev.dong4j.zeka.kernel.common.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * <p>Description:  </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.06.04 19:33
 * @since 1.5.0
 */
@Slf4j
class ProcessIdUtilsTest {

    /**
     * Test 1
     *
     * @since 1.9.0
     */
    @Test
    void test_1() {
        log.info("{}", ProcessIdUtils.getProcessId());
    }

}
