package dev.dong4j.zeka.kernel.common.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.27 18:11
 * @since 1.0.0
 */
@Slf4j
class MethodLimitTest {
    /** 请求总数 */
    private int clientTotal;
    /** 同时并发执行的线程数 */
    private int ThreadTotal;
    /** 是否发生过 */
    private AtomicBoolean isHappened;

    /**
     * Before
     *
     * @since 1.0.0
     */
    @BeforeEach
    void before() {
        this.clientTotal = 5000;
        this.ThreadTotal = 200;
        this.isHappened = new AtomicBoolean(false);
    }

    /**
     * 测试多线程下只执行一次代码
     *
     * @throws InterruptedException the interrupted exception
     * @since 1.0.0
     */
    @Test
    void test() throws InterruptedException {
        ExecutorService service = Executors.newCachedThreadPool();
        Semaphore semaphore = new Semaphore(this.ThreadTotal);
        CountDownLatch countDownLatch = new CountDownLatch(this.clientTotal);
        for (int i = 0; i < this.clientTotal; i++) {
            service.execute(() -> {
                try {
                    semaphore.acquire();
                    MethodLimit.execute(this.getClass(), 1);
                    semaphore.release();
                } catch (InterruptedException e) {
                    log.info("", e);
                }
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
        service.shutdown();
        log.info("isHappened: {}", this.isHappened.get());
    }

    /**
     * Once code
     *
     * @since 1.0.0
     */
    private void onceCode() {
        if (this.isHappened.compareAndSet(false, true)) {
            log.info("test");
        }
    }
}
