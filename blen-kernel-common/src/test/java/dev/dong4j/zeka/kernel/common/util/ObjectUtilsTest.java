package dev.dong4j.zeka.kernel.common.util;

import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2025.07.23 15:35
 * @since 1.0.0
 */
@Slf4j
class ObjectUtilsTest {
    private String[] xx;


    @Test
    void test1() throws Exception {
        log.info("{}", ObjectUtil.isEmpty(xx));
    }

}
