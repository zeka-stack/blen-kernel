package dev.dong4j.zeka.kernel.common.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * <p>Description:  </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.07.02 12:09
 * @since 1.5.0
 */
@Slf4j
class NetUtilsTest {
    @Test
    void test_1() {
        log.info("{}", NetUtils.getLocalIpAddr());
        log.info("{}", NetUtils.getHostName());

        log.info("{}", INetUtils.getLocalHost());
        log.info("{}", INetUtils.getIpByConfig());
        log.info("{}", INetUtils.getLocalAddress().getHostName());
    }

    @Test
    void test_2() {
        log.info("{}", INetUtils.getLocalAddress());
    }
}
