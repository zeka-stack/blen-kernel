package dev.dong4j.zeka.kernel.common.base;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * <p>Description:  </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2019.12.16 21:12
 * @since 1.0.0
 */
@Slf4j
class BasePOTest {
    /**
     * Test l ombok build
     *
     * @since 1.0.0
     */
    @Test
    void test_LOmbok_build() {
        Sub sub = Sub.builder().nickName("dong4j").name("dong4j").password("123456").build();
        log.info("{}", sub.toString());

        Super superClass = new Sub().nickName("aaa").name("dong4j").password("aaa");
        log.info("{}", superClass.toString());
    }

}

