package dev.dong4j.zeka.kernel.common.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.27 18:12
 * @since 1.0.0
 */
@Slf4j
class INetUtilsTest {
    /**
     * Test
     *
     * @since 1.0.0
     */
    @Test
    void test() {
        log.info("{}", NetUtils.getLocalHost());
        log.info("{}", NetUtils.available(2181));
        log.info("{}", NetUtils.getLocalAddress().getHostAddress());
        log.info("{}", NetUtils.getIpByHost("www.baidu.com"));
        log.info("{}", NetUtils.getIpByHost("nacos.server"));
        log.info("{}", NetUtils.getIpByHost("dong4j.com"));
        log.info("{}", SystemUtils.getHostName());
    }

}
