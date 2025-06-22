package dev.dong4j.zeka.kernel.common.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * <p>Description:  </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.03.04 11:48
 * @since 1.0.0
 */
@Slf4j
class DateTimeUtilsTest {

    /**
     * Parse
     *
     * @since 1.0.0
     */
    @Test
    void parse() {
        log.info("{}", DateTimeUtils.parse("2020-11-11", DateTimeUtils.DATETIME_FORMAT));
    }
}
