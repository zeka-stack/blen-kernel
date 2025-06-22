package dev.dong4j.zeka.kernel.common.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * <p>Description:  </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.02.28 19:40
 * @since 1.0.0
 */
@Slf4j
class SystemUtilsTest {

    /**
     * Test os
     *
     * @since 1.0.0
     */
    @Test
    void test_OS() {
        log.info("{}", SystemUtils.isMac());
        log.info("{}", SystemUtils.isLinux());
        log.info("{}", SystemUtils.isWindows());
    }
}
