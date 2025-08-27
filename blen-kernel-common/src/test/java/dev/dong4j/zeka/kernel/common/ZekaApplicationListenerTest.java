package dev.dong4j.zeka.kernel.common;

import dev.dong4j.zeka.kernel.common.util.Tools;
import java.io.Serial;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationEvent;

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dong4j@gmail.com"
 * @date 2019.12.26 21:37
 * @since 1.0.0
 */
@Slf4j
class ZekaApplicationListenerTest {
    /** 请求总数 */
    private int clientTotal;
    /** 同时并发执行的线程数 */
    private int ThreadTotal;
    /** 是否发生过 */
    private AtomicBoolean isHappened;

    /**
     * Before.
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
                    this.onceCode();
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

    /**
     * Test executed 1
     *
     * @since 1.0.0
     */
    @Test
    void testExecuted_first() {
        // 最大执行次数
        Tools.repeat(5, ZekaApplicationListener.Runner::executeCount);

        this.executeOnce();
    }

    @Test
    void testExecuted_last() {
        // 最大执行次数
        Tools.repeat(5, ZekaApplicationListener.Runner::executeCount);

        this.executeAtLast();
    }

    /**
     * Execute once
     *
     * @since 1.4.0
     */
    private void executeOnce() {
        String key = Event1.class.getName() + ":" + this.getClass().getName();
        AtomicInteger count = new AtomicInteger();
        for (int index = 0; index < 5; index++) {
            // 第一次就退出
            ZekaApplicationListener.Runner.executeAtFirst(key, () -> {
                count.getAndIncrement();
                log.info("执行了一次");
            });
        }
        Assertions.assertEquals(1, count.get());
        Assertions.assertEquals(5, ZekaApplicationListener.Runner.getExecuteCurrentCount(key));
    }

    /**
     * Execute at last
     *
     * @since 1.4.0
     */
    private void executeAtLast() {
        String key = Event1.class.getName() + ":" + this.getClass().getName();
        AtomicInteger count = new AtomicInteger();
        for (int index = 0; index < 5; index++) {

            ZekaApplicationListener.Runner.executeAtLast(key, () -> {
                count.getAndIncrement();
                log.info("执行了一次");
            });
            // 最后一次退出 (如果当前循环次数与最后(5)不合符)则不 break
        }
        Assertions.assertEquals(1, count.get());
        Assertions.assertEquals(5, ZekaApplicationListener.Runner.getExecuteCurrentCount(key));
    }

    /**
     * <p>Description: </p>
     *
     * @author dong4j
     * @version 1.2.3
     * @email "mailto:dong4j@gmail.com"
     * @date 2019.12.26 21:37
     * @since 1.0.0
     */
    private static class Event1 extends ApplicationEvent {
        /** serialVersionUID */
        @Serial
        private static final long serialVersionUID = 5111317759302076143L;

        /**
         * Instantiates a new Event 1.
         *
         * @param source the source
         * @since 1.0.0
         */
        Event1(Object source) {
            super(source);
        }
    }

    /**
     * <p>Description: </p>
     *
     * @author dong4j
     * @version 1.2.3
     * @email "mailto:dong4j@gmail.com"
     * @date 2019.12.26 21:37
     * @since 1.0.0
     */
    private static class Event2 extends ApplicationEvent {
        /** serialVersionUID */
        @Serial
        private static final long serialVersionUID = -4890598272340926170L;

        /**
         * Instantiates a new Event 2.
         *
         * @param source the source
         * @since 1.0.0
         */
        Event2(Object source) {
            super(source);
        }
    }
}
