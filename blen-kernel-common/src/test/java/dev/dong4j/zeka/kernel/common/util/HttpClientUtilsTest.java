package dev.dong4j.zeka.kernel.common.util;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

/**
 * <p>Description:  </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.06.14 11:17
 * @since 1.0.0
 */
class HttpClientUtilsTest {

    @SneakyThrows
    @Test
    void test_post() {
        HttpClientUtils.getForObject("http://www.baidu.com", String.class);
    }

}
