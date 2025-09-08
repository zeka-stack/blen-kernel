package dev.dong4j.zeka.kernel.common.support;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.27 18:11
 * @since 1.0.0
 */
@Slf4j
class ChainMapTest {

    /**
     * Test
     *
     * @since 1.0.0
     */
    @Test
    void test() {
        log.info("{}", ChainMap.build(5).put("aaa", "bbb").put("ccc", "ddd"));
    }

    /**
     * Test null
     *
     * @since 1.0.0
     */
    @Test
    void test_null() {
        log.info("{}", ChainMap.build(5).put("null", null).put("ccc", null));
    }
}
