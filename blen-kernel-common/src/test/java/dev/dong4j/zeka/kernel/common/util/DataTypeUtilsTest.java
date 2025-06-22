package dev.dong4j.zeka.kernel.common.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * <p>Description:  </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2021.01.27 13:14
 * @since 1.7.1
 */
@Slf4j
class DataTypeUtilsTest {

    /**
     * Test is primitive
     *
     * @since 1.7.1
     */
    @Test
    void test_isPrimitive() {
        log.info("{}", DataTypeUtils.isPrimitive(int.class));
        log.info("{}", DataTypeUtils.isPrimitive(Integer.class));
    }

}
