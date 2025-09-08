package dev.dong4j.zeka.kernel.common.util;

import dev.dong4j.zeka.kernel.common.enums.EnabledEnum;
import dev.dong4j.zeka.kernel.common.enums.SerializeEnum;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * ClassUtils 单元测试
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2025.09.08 19:52
 * @since 1.0.0
 */
@Slf4j
class ClassUtilsTest {

    @Test
    void getInterfaceT() {
        log.info("{}", ClassUtils.getInterfaceT(EnabledEnum.class, SerializeEnum.class, 0));
    }

    @Test
    void getSuperClassT() {
        log.info("{}", ClassUtils.getSuperClassT(EnabledEnum.class, 0));
    }
}
