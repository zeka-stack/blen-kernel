package dev.dong4j.zeka.kernel.common.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * <p>Description:  </p>
 *
 * @author dong4j
 * @version 1.4.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.05.20 16:59
 * @since 1.9.0
 */
@Slf4j
class AesUtilsTest {

    /**
     * Test 1
     *
     * @since 1.9.0
     */
    @Test
    void test_1() {
        byte[] encrypt = AesUtils.encrypt("aaaaa", "qwewfwefwefwefwef12312fwefwefe1r");
        log.info("{}", Base64Utils.encodeToString(encrypt));
        log.info("{}", AesUtils.decryptToStr(Base64Utils.decodeFromString(Base64Utils.encodeToString(encrypt)),
            "qwewfwefwefwefwef12312fwefwefe1r"));
    }

    @Test
    void test_2() {
        String encrypt = AesUtils.encryptToStr("aaaaa", "1234567890123456");
        log.info("{}", encrypt);
        log.info("{}", AesUtils.decryptToStr(encrypt, "1234567890123456"));
    }

    @Test
    void test_3() {
        final String rFsHHirtsGuST7HtBzebLge1uVYCg2ZS = AesUtils.decryptToStr(Base64Utils.decodeFromString(
            "YiXZeUA17ub53aLZL2d8dxPnFOCuGQ+Ym0wHKdVYdKI="), "rFsHHirtsGuST7HtBzebLge1uVYCg2ZS");

        log.info("{}", rFsHHirtsGuST7HtBzebLge1uVYCg2ZS);


        final String rFsHHirtsGuST7HtBzebLge1uVYCg2ZS1 = AesUtils.encryptToStr("6215256100001821", "rFsHHirtsGuST7HtBzebLge1uVYCg2ZS");

        log.info("{}", rFsHHirtsGuST7HtBzebLge1uVYCg2ZS1);
    }
}
