package dev.dong4j.zeka.kernel.common.basic.util;

import dev.dong4j.zeka.kernel.common.util.GsonUtils;
import dev.dong4j.zeka.kernel.common.util.HostUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * <p>Description:  </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.07.14 23:21
 * @since 1.0.0
 */
@Slf4j
class HostUtilsTest {

    /**
     * Test read
     *
     * @since 1.0.0
     */
    @Test
    void test_read() {
        log.info("{}", GsonUtils.toJson(HostUtils.read(), true));
    }

    /**
     * Test delete
     *
     * @since 1.0.0
     */
    @Test
    void test_delete() {
        HostUtils.deleteHost("127.0.0.1", "xxx");

        this.test_read();
    }

    /**
     * Test exists
     *
     * @since 1.0.0
     */
    @Test
    void test_exists() {
        log.info("{}", HostUtils.exists("nacos.server"));
    }

    /**
     * Test get ips
     *
     * @since 1.0.0
     */
    @Test
    void test_getIps() {
        log.info("{}", HostUtils.getIps(HostUtils.read(), "nacos.server"));
    }
}
