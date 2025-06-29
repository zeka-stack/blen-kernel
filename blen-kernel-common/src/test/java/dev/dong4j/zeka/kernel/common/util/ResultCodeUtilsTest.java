package dev.dong4j.zeka.kernel.common.util;

import dev.dong4j.zeka.kernel.common.api.BaseCodes;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2025.06.29 15:05
 * @since x.x.x
 */
@Slf4j
class ResultCodeUtilsTest {

    @Test
    void test1() throws Exception {
        log.info("{}", ResultCodeUtils.generateCode(BaseCodes.PARAM_VERIFY_ERROR));
    }
}
