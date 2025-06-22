package dev.dong4j.zeka.kernel.common.util;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>Description:  </p>
 *
 * @author dong4j
 * @version 1.3.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.04.14 16:58
 * @since 1.0.0
 */
class ThreadUtilsTest {
    /** M */
    private static int m = 0;

    /**
     * Test 1
     *
     * @since 1.0.0
     */
    @SneakyThrows
    @Test
    void test_1() {

        AtomicInteger integer = new AtomicInteger(0);
        ThreadUtils.submit(10,
            () -> {
                for (int i = 0; i < 10; i++) {
                    integer.addAndGet(1);
                    m++;
                }
            },
            () -> {
                System.out.println(m);
                System.out.println(integer.get());
            });
    }
}
