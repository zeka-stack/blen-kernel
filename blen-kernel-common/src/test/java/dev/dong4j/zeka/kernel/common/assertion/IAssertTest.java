package dev.dong4j.zeka.kernel.common.assertion;

import cn.hutool.core.thread.ThreadUtil;
import dev.dong4j.zeka.kernel.common.api.BaseCodes;
import dev.dong4j.zeka.kernel.common.exception.LowestException;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * <p>Description:  </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.11.17 19:55
 * @since 1.0.0
 */
@Slf4j
class IAssertTest {

    /**
     * Test 1
     *
     * @since 1.0.0
     */
    @Test
    void test_1() {
        BaseCodes.AGENT_DISABLE_EXCEPTION.notEquals(new Object(), null);
        BaseCodes.AGENT_DISABLE_EXCEPTION.notEquals(null, new Object());
    }

    /**
     * Test 2
     *
     * @since 1.0.0
     */
    @Test
    void test_2() {
        BaseCodes.AGENT_DISABLE_EXCEPTION.notEquals(User.builder().password("aa").username("dong4j").build(),
            User.builder().password("aa").username("dong4j").build());

        BaseCodes.AGENT_DISABLE_EXCEPTION.notEquals("aaa", "aaaa");
    }

    /**
     * Test 3
     *
     * @since 1.0.0
     */
    @Test
    void test_3() {
        Assertions.assertThrows(LowestException.class, () -> BaseCodes.AGENT_DISABLE_EXCEPTION.notEquals(User1.builder()
                .password("aa")
                .username("dong4j")
                .build(),
            User1.builder()
                .password("aa")
                .username("dong4j")
                .build()));

    }

    @Test
    void newException() {
    }

    @Test
    void testNewException() {
    }

    @Test
    void wrapper() {
    }

    @Test
    void testWrapper() {
    }

    @Test
    void notNull() {
        LowestException lowestException1 = Assertions.assertThrows(LowestException.class,
            () -> BaseCodes.AGENT_DISABLE_EXCEPTION.notNull(null));
        Assertions.assertEquals(BaseCodes.AGENT_DISABLE_EXCEPTION.getMessage(), lowestException1.getMessage());

        LowestException lowestException2 = Assertions.assertThrows(LowestException.class,
            () -> BaseCodes.AGENT_INVOKE_EXCEPTION.notNull(null, "占位符替换"));

        Assertions.assertEquals("Rest Client 调用失败: [占位符替换]", lowestException2.getMessage());

        LowestException lowestException3 = Assertions.assertThrows(LowestException.class,
            () -> BaseCodes.AGENT_INVOKE_EXCEPTION.notNull(null));

        Assertions.assertEquals("Rest Client 调用失败: [{}]", lowestException3.getMessage());

    }

    @Test
    void testNotNull() {
    }

    @Test
    void notEmpty() {
    }

    @Test
    void notBlank() {
    }

    @Test
    void testNotBlank() {
    }

    @Test
    void testNotEmpty() {
    }

    @Test
    void testNotEmpty1() {
    }

    @Test
    void testNotEmpty2() {
    }

    @Test
    void testNotEmpty3() {
    }

    @Test
    void testNotEmpty4() {
    }

    @Test
    void testNotEmpty5() {
    }

    @Test
    void testNotEmpty6() {
    }

    @Test
    void isFalse() {
    }

    @Test
    void testIsFalse() {
    }

    @Test
    void isTrue() {
    }

    @Test
    void testIsTrue() {
    }

    @Test
    void isNull() {
    }

    @Test
    void testIsNull() {
    }

    @Test
    void fail() {
    }

    @Test
    void testFail() {
    }

    @Test
    void testFail1() {
    }

    @Test
    void testFail2() {
    }

    @Test
    void testEquals() {
    }

    @Test
    void notEquals() {
    }

    @Test
    void objectsAreEqual() {
    }

    @Test
    void testEquals1() {
    }

    private String veryExpensive() {
        ThreadUtil.sleep(3000);
        return "aaaa";
    }

    /**
     * <p>Description: 未实现 hashcode 和 equals </p>
     *
     * @author dong4j
     * @version 1.0.0
     * @email "mailto:dong4j@gmail.com"
     * @date 2020.11.17 21:08
     * @since 1.0.0
     */
    @Builder
    static class User {
        /** Username */
        private final String username;
        /** Password */
        private final String password;
    }

    /**
     * <p>Description: 实现 hashcode 和 equals </p>
     *
     * @author dong4j
     * @version 1.0.0
     * @email "mailto:dong4j@gmail.com"
     * @date 2020.11.17 21:08
     * @since 1.0.0
     */
    @Data
    @Builder
    static class User1 {
        /** Username */
        private String username;
        /** Password */
        private String password;
    }
}
