package dev.dong4j.zeka.kernel.common.timer;

import dev.dong4j.zeka.kernel.common.util.ThreadUtils;
import dev.dong4j.zeka.kernel.common.util.Tools;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * <p>Description:  </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2021.12.09 21:13
 * @since 1.0.0
 */
@Slf4j
class HashedWheelTimerTest {

    @Test
    void test_1() {
        HashedWheelTimer hashedWheelTimer = new HashedWheelTimer(20, TimeUnit.SECONDS, 20);

        hashedWheelTimer.start();

        Tools.repeat(10, () -> {
            hashedWheelTimer.newTimeout(timeout -> log.info("running...."), 1000L, TimeUnit.MILLISECONDS);
        });

        ThreadUtils.join();
    }
}
