package dev.dong4j.zeka.kernel.common;

import dev.dong4j.zeka.kernel.common.api.BaseCodes;
import dev.dong4j.zeka.kernel.common.exception.LowestException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * <p>Description:  </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.05.19 16:00
 * @since 1.0.0
 */
@Slf4j
class CoreBundleTest {
    /**
     * Test 1
     *
     * @since 1.0.0
     */
    @Test
    void test_1() {
        log.info("{}", CoreBundle.message("a"));
        log.info("{}", CoreBundle.message("b", "aaaaa"));
    }

    @Test
    void test_2() {
        Assertions.assertEquals("a", CoreBundle.message("a"));
        Assertions.assertEquals("b 参数 1 参数 2", CoreBundle.message("b", "参数 1", "参数 2"));
        Assertions.assertEquals("c 参数 1 参数 2", CoreBundle.message("c", "参数 1", "参数 2"));

        // 不支持混合
        Assertions.assertEquals("d 参数 1 {1}", CoreBundle.message("d", "参数 1", "参数 2"));
        Assertions.assertEquals("!invalid format: `e {0} {}`!", CoreBundle.message("e", "参数 1", "参数 2"));
    }

    @Test
    void test_3() {
        Assertions.assertEquals("请求成功", BaseCodes.SUCCESS.getMessage());
        Assertions.assertEquals("请求失败: [{}]", BaseCodes.FAILURE.getMessage());
        Assertions.assertEquals("暂无数据", BaseCodes.DEFAULT_NULL_DATA.getMessage());
        Assertions.assertEquals("服务不可用", BaseCodes.SERVICE_INVOKE_ERROR.getMessage());
        Assertions.assertEquals("服务不可用", CoreBundle.message("code.service.invoke.error", "xxxx"));

        LowestException lowestException = Assertions.assertThrows(LowestException.class, () -> {
            BaseCodes.SERVICE_INVOKE_ERROR.notNull(null, "加入参数");
        });

        Assertions.assertEquals("服务不可用", lowestException.getMessage());
    }
}
