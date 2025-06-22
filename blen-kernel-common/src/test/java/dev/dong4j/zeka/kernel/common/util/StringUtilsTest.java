package dev.dong4j.zeka.kernel.common.util;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StringSubstitutor;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.27 18:26
 * @since 1.0.0
 */
@Slf4j
class StringUtilsTest {
    /**
     * Test
     *
     * @since 1.0.0
     */
    @Test
    void test() {
        log.info("{}", StringUtils.padAfter("REST", 20, " ").length());
    }

    /**
     * Test remove suffix
     *
     * @since 1.0.0
     */
    @Test
    void testRemoveSuffix() {
        log.info("{}", StringUtils.removeSuffix("/www/vvv/qqq/", "/"));
    }

    /**
     * Test 2
     *
     * @since 1.0.0
     */
    @Test
    void test_2() {
        log.info("{}", StringUtils.subSuf("Bearer xxxx", 7).length());
    }

    /**
     * Test first char to upper
     *
     * @since 1.0.0
     */
    @Test
    void testFirstCharToUpper() {
        assertEquals("Dong4j", StringUtils.firstCharToUpper("dong4j"));
        assertEquals("1234", StringUtils.firstCharToUpper("1234"));
        assertEquals("@1234", StringUtils.firstCharToUpper("@1234"));
        assertEquals(" 1234", StringUtils.firstCharToUpper(" 1234"));
        assertEquals("", StringUtils.firstCharToUpper(""));
        assertEquals("  ", StringUtils.firstCharToUpper("  "));
        assertNull(StringUtils.firstCharToUpper(null));
    }

    /**
     * Test replace
     *
     * @since 1.0.0
     */
    @Test
    void test_replace() {
        // 替换字符串中的占位符
        Map<String, Object> params = Maps.newHashMapWithExpectedSize(5);
        params.put("user", "admin");
        params.put("password", "123456");
        params.put("system-version", "windows 10");
        params.put("版本", "version");
        params.put("详", "翔");

        log.info("{}", StringUtils.replace("你的用户名是${user},密码是${password}. 系统版本${system-${版本}}", params));
        log.info("{}", StringUtils.replace("表达对一个人无比的崇拜怎么表述最好？答: “愿闻其${详}”", params));

        log.info("{}", StringSubstitutor.replaceSystemProperties("You are running with java.version = ${java.version} and os.name = " +
            "${user.name}."));

    }

    /**
     * Test uuid
     *
     * @since 1.5.0
     */
    @Test
    void test_uuid() {
        log.info("{}", StringUtils.getUid());
        log.info("{}", UUID.randomUUID().toString().replace("-", ""));
    }
}
