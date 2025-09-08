package dev.dong4j.zeka.kernel.auth.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.27 14:53
 * @since 1.0.0
 */
@Slf4j
class CryptoUtilsTest {
    /**
     * Test
     *
     * @since 1.0.0
     */
    @Test
    void test() {
        String encodePassword = CryptoUtils.encode("123456");
        log.info("123456 -> {}", encodePassword);
        Assertions.assertFalse(CryptoUtils.matches("1234", encodePassword));
    }
}
