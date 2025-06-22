package dev.dong4j.zeka.kernel.common.basic.bundle;

import dev.dong4j.zeka.kernel.common.bundle.BasicBundle;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * <p>Description:  </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.05.19 16:39
 * @since 1.4.0
 */
@Slf4j
class BasicBundleTest {
    /**
     * Test 1
     *
     * @since 1.4.0
     */
    @Test
    void test_1() {
        Assertions.assertEquals("a", BasicBundle.message("a"));
        Assertions.assertEquals("b 参数 1 参数 2", BasicBundle.message("b", "参数 1", "参数 2"));
        Assertions.assertEquals("c 参数 1 参数 2", BasicBundle.message("c", "参数 1", "参数 2"));

        // 不支持混合
        Assertions.assertEquals("d 参数 1 {1}", BasicBundle.message("d", "参数 1", "参数 2"));
        Assertions.assertEquals("!invalid format: `e {0} {}`!", BasicBundle.message("e", "参数 1", "参数 2"));
    }
}
