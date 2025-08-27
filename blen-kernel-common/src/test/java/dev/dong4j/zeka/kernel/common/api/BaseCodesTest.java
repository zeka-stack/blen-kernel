package dev.dong4j.zeka.kernel.common.api;

import dev.dong4j.zeka.kernel.common.CoreBundle;
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
 * @date 2020.05.19 16:03
 * @since 1.4.0
 */
@Slf4j
class BaseCodesTest {

    /**
     * Test 1
     *
     * @since 1.4.0
     */
    @Test
    void test_1() {
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
