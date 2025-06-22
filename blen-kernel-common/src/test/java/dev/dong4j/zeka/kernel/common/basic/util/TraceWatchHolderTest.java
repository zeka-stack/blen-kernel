package dev.dong4j.zeka.kernel.common.basic.util;

import dev.dong4j.zeka.kernel.common.support.TraceWatch;
import dev.dong4j.zeka.kernel.common.support.TraceWatchHolder;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;
import java.util.function.IntConsumer;
import java.util.function.Supplier;

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2021.05.11 15:21
 * @since 1.8.0
 */
@Slf4j
class TraceWatchHolderTest {

    /**
     * 方式一: 使用 {@link Supplier} 或 {@link IntConsumer} 自动关闭任务
     *
     * @since 1.8.0
     */
    @Test
    void test_1() {
        TraceWatch stopWatch = new TraceWatch();

        TraceWatchHolder.run(stopWatch, "function1", () -> {
            try {
                TimeUnit.SECONDS.sleep(1); // 模拟业务代码
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        TraceWatchHolder.run(stopWatch, "function2", () -> {
            try {
                TimeUnit.SECONDS.sleep(2); // 模拟业务代码
                return "Yes";
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "No";
        });

        TraceWatchHolder.run(stopWatch, "function1", () -> {
            try {
                TimeUnit.SECONDS.sleep(3); // 模拟业务代码
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        log.info("{}", stopWatch.prettyPrint());
    }

    /**
     * 方式二: 使用 try-with-resource 语法关闭任务
     *
     * @throws InterruptedException interrupted exception
     * @since 1.8.0
     */
    @Test
    void test_2() throws InterruptedException {
        TraceWatch stopWatch = new TraceWatch();

        try (TraceWatch ignored = stopWatch.startings("function1")) {
            TimeUnit.SECONDS.sleep(1); // 模拟业务代码
        }

        try (TraceWatch ignored = stopWatch.startings("function2")) {
            TimeUnit.SECONDS.sleep(2); // 模拟业务代码
        }

        try (TraceWatch ignored = stopWatch.startings("function1")) {
            TimeUnit.SECONDS.sleep(3); // 模拟业务代码
        }

        log.info("{}", stopWatch.prettyPrint());
    }

}
