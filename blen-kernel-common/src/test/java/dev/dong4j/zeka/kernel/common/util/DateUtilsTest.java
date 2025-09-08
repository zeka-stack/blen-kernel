package dev.dong4j.zeka.kernel.common.util;

import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.27 18:25
 * @since 1.0.0
 */
@Slf4j
class DateUtilsTest {
    /**
     * Test
     *
     * @since 1.0.0
     */
    @Test
    void test() {
        log.info("{}", DateUtils.format(new Date(), DateUtils.PATTERN_TIME));
    }

    /**
     * Test 1
     *
     * @since 1.0.0
     */
    @Test
    void test1() {
        log.info("{}", DateUtils.formatTime(new Date()));
    }

    /**
     * Test 2
     *
     * @since 1.0.0
     */
    @Test
    void test2() {
        log.info("{}", DateUtils.parse("22:11:34", DateUtils.PATTERN_TIME));
    }

    /**
     * Test 3
     *
     * @since 1.0.0
     */
    @Test
    void test_3() {
        log.info("{}", DateUtils.timeToSecond("2020-10-12 10:10:10"));
    }

    /**
     * Test 4
     *
     * @since 1.0.0
     */
    @Test
    void test_4() {
        log.info("{}", DateUtils.timeToMillisecond("2020-10-12 10:10:10:121"));
    }

    @Test
    void test_5() {
        log.info("{}", Jsons.toJson(DateUtils.plusDays(new Date(), -7)));
    }
}
