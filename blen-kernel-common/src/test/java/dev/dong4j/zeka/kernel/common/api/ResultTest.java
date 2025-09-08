package dev.dong4j.zeka.kernel.common.api;

import dev.dong4j.zeka.kernel.common.util.Jsons;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * <p>Description:  </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2019.12.19 14:43
 * @since 1.0.0
 */
@Slf4j
class ResultTest {
    /**
     * Sets up
     */
    @BeforeEach
    void setUp() {
    }

    /**
     * Test is fail *
     *
     * @throws Exception exception
     * @since 1.0.0
     */
    @Test
    void testIsFail() throws Exception {
        boolean result = R.isFail(null);
        assertTrue(result);
    }

    /**
     * Test is ok *
     *
     * @throws Exception exception
     * @since 1.0.0
     */
    @Test
    void testIsOk() throws Exception {
        boolean result = R.isOk(null);
        assertTrue(result);
    }

    /**
     * Test data *
     *
     * @throws Exception exception
     * @since 1.0.0
     */
    @Test
    void testData() throws Exception {
        Result<String> result = R.succeed("hello");
        assertEquals("hello", result.getData());
    }

    /**
     * Test data 2 *
     *
     * @throws Exception exception
     * @since 1.0.0
     */
    @Test
    void testData2() throws Exception {
        Result<Integer> result = R.succeed(129);
        assertEquals(129, result);
    }

    /**
     * Test void 1
     *
     * @since 1.0.0
     */
    @Test
    void test_void_1() {
        log.info("{}", Jsons.toJson(R.succeed()));
    }

    /**
     * Test void 2
     *
     * @since 1.0.0
     */
    @Test
    void test_void_2() {
        Result<Void> result = R.succeed();
        log.info("{}", Jsons.toJson(result));
    }

}

