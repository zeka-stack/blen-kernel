package dev.dong4j.zeka.kernel.common.util;

import cn.hutool.core.thread.ConcurrencyTester;
import cn.hutool.core.thread.ThreadUtil;
import dev.dong4j.zeka.kernel.common.asserts.Assertions;
import dev.dong4j.zeka.kernel.common.exception.LowestException;
import dev.dong4j.zeka.kernel.common.function.CheckedRunnable;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * <p>Description: 多线程工具类 </p>
 *
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.27 18:29
 * @since 1.0.0
 */
@Slf4j
@UtilityClass
@SuppressWarnings("checkstyle:MethodLimit")
public class ThreadUtils {

    /** Executor */
    @Setter
    @Getter
    private ExecutorService executor;

    /**
     * Thread sleep
     *
     * @param millis 时长
     * @since 1.0.0
     */
    public void sleep(long millis) {
        sleep(millis, TimeUnit.MILLISECONDS);
    }

    /**
     * Sleep *
     *
     * @param time     time
     * @param timeUnit time unit
     * @since 1.0.0
     */
    public void sleep(long time, @NotNull TimeUnit timeUnit) {
        LockSupport.parkNanos(timeUnit.toNanos(time));
    }

    /**
     * Join
     *
     * @since 1.0.0
     */
    @SneakyThrows
    public void join() {
        Thread.currentThread().join();
    }

    /**
     * Submit
     *
     * @param executorService executor service
     * @param threadCount     thread count
     * @param runnable        runnable
     * @since 16.0
     */
    public void submit(Executor executorService, int threadCount, CheckedRunnable runnable) {
        Assertions.isTrue(threadCount > 0, "线程数量必须大于 0");
        for (int i = 0; i < threadCount; i++) {
            execute(executorService, runnable, null);
        }

    }

    /**
     * 新增线程执行逻辑
     *
     * @param threadCount thread count
     * @param runnable    runnable
     * @since 1.6.0
     */
    public void submit(int threadCount, CheckedRunnable runnable) {
        Assertions.isTrue(threadCount > 0, "线程数量必须大于 0");

        if (executor == null) {
            executor = newExecutor(threadCount, threadCount);
        }

        submit(executor, threadCount, runnable);
    }

    /**
     * 提交多个并行任务
     *
     * @param runnables runnables
     * @since 1.7.1
     */
    public void submit(List<CheckedRunnable> runnables) throws BrokenBarrierException, InterruptedException {
        if (executor == null) {
            executor = newExecutor(5, 5);
        }

        CyclicBarrier barrier = new CyclicBarrier(runnables.size());

        runnables.forEach(runnable -> execute(executor, runnable, barrier));
        barrier.await();
    }

    /**
     * 执行任务
     *
     * @param executor executor
     * @param runnable runnable
     * @param barrier  barrier
     * @since 1.7.1
     */
    private static void execute(Executor executor, CheckedRunnable runnable, CyclicBarrier barrier) {
        executor.execute(() -> {
            try {
                runnable.run();
            } catch (Throwable throwable) {
                if (throwable instanceof LowestException exception) {
                    log.error("线程池执行异常: <{}>: {}:{}", exception.getTraceId(), exception.getResultCode().getCode(), exception.getMessage());
                    return;
                }
                log.error("", throwable);
            }

            if (barrier != null) {
                try {
                    barrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    log.error(e.getMessage(), e);
                }
            }
        });
    }

    /**
     * Submit
     *
     * @param runnable runnable
     * @since 1.6.0
     */
    @SuppressWarnings("PMD.ThreadPoolCreationRule")
    public void submit(CheckedRunnable runnable) {
        Executor fixedThreadPool = Executors.newSingleThreadExecutor();
        submit(fixedThreadPool, 1, runnable);
    }

    /**
     * 多线程测试用例
     *
     * @param threadCount 开启线程数
     * @param runnable    runnable
     * @param callback    线程结束后执行的方法
     * @since 1.0.0
     */
    @SuppressWarnings("all")
    public void submit(int threadCount, CheckedRunnable runnable, CheckedRunnable callback) {
        Assertions.isTrue(threadCount > 0, "线程数量必须大于 0");
        CountDownLatch countDownLatch = new CountDownLatch(1);

        if (executor == null) {
            executor = newExecutor(threadCount, threadCount);
        }

        for (int i = 0; i < threadCount; i++) {
            executor.execute(() -> {
                try {
                    countDownLatch.await();
                    runnable.run();
                } catch (Throwable throwable) {
                    if (throwable instanceof LowestException) {
                        throw (LowestException) throwable;
                    }
                    log.error("", throwable);
                }
            });
        }
        countDownLatch.countDown();
        executor.shutdown();
        while (!executor.isTerminated()) {
            try {
                Thread.sleep(0L);
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
            }
        }
        try {
            callback.run();
        } catch (Throwable throwable) {
            if (throwable instanceof LowestException) {
                throw (LowestException) throwable;
            }
            log.error(throwable.getMessage(), throwable);
        }
    }

    /**
     * New executor
     *
     * @param corePoolSize core pool size
     * @return the executor service
     * @since 1.7.0
     */
    public static ExecutorService newExecutor(int corePoolSize) {
        return ThreadUtil.newExecutor(corePoolSize);
    }

    /**
     * New executor
     *
     * @return the executor service
     * @since 1.7.0
     */
    public static ExecutorService newExecutor() {
        return ThreadUtil.newExecutor();
    }

    /**
     * New single executor
     *
     * @return the executor service
     * @since 1.7.0
     */
    public static ExecutorService newSingleExecutor() {
        return ThreadUtil.newSingleExecutor();
    }

    /**
     * New executor
     *
     * @param corePoolSize    core pool size
     * @param maximumPoolSize maximum pool size
     * @return the thread pool executor
     * @since 1.7.0
     */
    public static ThreadPoolExecutor newExecutor(int corePoolSize, int maximumPoolSize) {
        return ThreadUtil.newExecutor(corePoolSize, maximumPoolSize);
    }

    /**
     * New executor
     *
     * @param corePoolSize     core pool size
     * @param maximumPoolSize  maximum pool size
     * @param maximumQueueSize maximum queue size
     * @return the executor service
     * @since 1.7.0
     */
    public static ExecutorService newExecutor(int corePoolSize, int maximumPoolSize, int maximumQueueSize) {
        return ThreadUtil.newExecutor(corePoolSize, maximumPoolSize, maximumQueueSize);
    }

    /**
     * New executor by blocking coefficient
     *
     * @param blockingCoefficient blocking coefficient
     * @return the thread pool executor
     * @since 1.7.0
     */
    public static ThreadPoolExecutor newExecutorByBlockingCoefficient(float blockingCoefficient) {
        return ThreadUtil.newExecutorByBlockingCoefficient(blockingCoefficient);

    }

    /**
     * Execute
     *
     * @param runnable runnable
     * @since 1.7.0
     */
    public static void execute(Runnable runnable) {
        ThreadUtil.execute(runnable);
    }

    /**
     * Exec async
     *
     * @param runnable runnable
     * @param isDaemon is daemon
     * @return the runnable
     * @since 1.7.0
     */
    @Contract("_, _ -> param1")
    public static Runnable execAsync(Runnable runnable, boolean isDaemon) {
        return ThreadUtil.execAsync(runnable, isDaemon);
    }

    /**
     * Exec async
     *
     * @param <T>  parameter
     * @param task task
     * @return the future
     * @since 1.7.0
     */
    public static <T> Future<T> execAsync(Callable<T> task) {
        return ThreadUtil.execAsync(task);
    }

    /**
     * Exec async
     *
     * @param runnable runnable
     * @return the future
     * @since 1.7.0
     */
    public static Future<?> execAsync(Runnable runnable) {
        return ThreadUtil.execAsync(runnable);
    }

    /**
     * New completion service
     *
     * @param <T> parameter
     * @return the completion service
     * @since 1.7.0
     */
    @Contract(" -> new")
    public static <T> @NotNull CompletionService<T> newCompletionService() {
        return ThreadUtil.newCompletionService();
    }

    /**
     * New completion service
     *
     * @param <T>      parameter
     * @param executor executor
     * @return the completion service
     * @since 1.7.0
     */
    @Contract("_ -> new")
    public static <T> @NotNull CompletionService<T> newCompletionService(ExecutorService executor) {
        return ThreadUtil.newCompletionService(executor);
    }

    /**
     * New count down latch
     *
     * @param threadCount thread count
     * @return the count down latch
     * @since 1.7.0
     */
    public static CountDownLatch newCountDownLatch(int threadCount) {
        return ThreadUtil.newCountDownLatch(threadCount);
    }

    /**
     * New thread
     *
     * @param runnable runnable
     * @param name     name
     * @return the thread
     * @since 1.7.0
     */
    @Contract("_, _ -> new")
    public static @NotNull
    Thread newThread(Runnable runnable, String name) {
        return ThreadUtil.newThread(runnable, name);
    }

    /**
     * New thread
     *
     * @param runnable runnable
     * @param name     name
     * @param isDaemon is daemon
     * @return the thread
     * @since 1.7.0
     */
    @Contract("_, _, _ -> new")
    public static @NotNull
    Thread newThread(Runnable runnable, String name, boolean isDaemon) {
        return ThreadUtil.newThread(runnable, name, isDaemon);
    }

    /**
     * Safe sleep
     *
     * @param millis millis
     * @return the boolean
     * @since 1.7.0
     */
    @Contract("null -> true")
    public static boolean safeSleep(Number millis) {
        return ThreadUtil.safeSleep(millis);
    }

    /**
     * Safe sleep
     *
     * @param millis millis
     * @return the boolean
     * @since 1.7.0
     */
    public static boolean safeSleep(long millis) {
        return ThreadUtil.safeSleep(millis);

    }

    /**
     * Get stack trace
     *
     * @return the stack trace element [ ]
     * @since 1.7.0
     */
    public static StackTraceElement[] getStackTrace() {
        return ThreadUtil.getStackTrace();
    }

    /**
     * Gets stack trace element *
     *
     * @param i 堆栈的层级
     * @return the stack trace element
     * @since 1.7.0
     */
    public static StackTraceElement getStackTraceElement(int i) {
        return ThreadUtil.getStackTraceElement(i);

    }

    /**
     * Create thread local
     *
     * @param <T>           parameter
     * @param isInheritable is inheritable
     * @return the thread local
     * @since 1.7.0
     */
    @Contract("_ -> new")
    public static <T> @NotNull ThreadLocal<T> createThreadLocal(boolean isInheritable) {
        return ThreadUtil.createThreadLocal(isInheritable);
    }

    /**
     * Create thread factory builder
     *
     * @return the cn . hutool . core . thread . thread factory builder
     * @since 1.7.0
     */
    @Contract(value = " -> new", pure = true)
    public static cn.hutool.core.thread.@NotNull ThreadFactoryBuilder createThreadFactoryBuilder() {
        return ThreadUtil.createThreadFactoryBuilder();
    }

    /**
     * Interrupt
     *
     * @param thread thread
     * @param isJoin is join
     * @since 1.7.0
     */
    public static void interrupt(Thread thread, boolean isJoin) {
        ThreadUtil.interrupt(thread, isJoin);

    }

    /**
     * Wait for die
     *
     * @since 1.7.0
     */
    public static void waitForDie() {
        ThreadUtil.waitForDie();
    }

    /**
     * Wait for die
     *
     * @param thread thread
     * @since 1.7.0
     */
    public static void waitForDie(Thread thread) {
        ThreadUtil.waitForDie();
    }

    /**
     * Get threads
     *
     * @return the thread [ ]
     * @since 1.7.0
     */
    @Contract(" -> new")
    public static Thread[] getThreads() {
        return ThreadUtil.getThreads();
    }

    /**
     * Get threads
     *
     * @param group group
     * @return the thread [ ]
     * @since 1.7.0
     */
    @Contract("_ -> new")
    public static Thread[] getThreads(ThreadGroup group) {
        return ThreadUtil.getThreads(group);
    }

    /**
     * Gets main thread *
     *
     * @return the main thread
     * @since 1.7.0
     */
    public static Thread getMainThread() {
        return ThreadUtil.getMainThread();
    }

    /**
     * Current thread group
     *
     * @return the thread group
     * @since 1.7.0
     */
    public static ThreadGroup currentThreadGroup() {
        return ThreadUtil.currentThreadGroup();
    }

    /**
     * New named thread factory
     *
     * @param prefix   prefix
     * @param isDaemon is daemon
     * @return the thread factory
     * @since 1.7.0
     */
    @Contract("_, _ -> new")
    public static @NotNull
    ThreadFactory newNamedThreadFactory(String prefix, boolean isDaemon) {
        return ThreadUtil.newNamedThreadFactory(prefix, isDaemon);
    }

    /**
     * New named thread factory
     *
     * @param prefix      prefix
     * @param threadGroup thread group
     * @param isDaemon    is daemon
     * @return the thread factory
     * @since 1.7.0
     */
    @Contract("_, _, _ -> new")
    public static @NotNull
    ThreadFactory newNamedThreadFactory(String prefix, ThreadGroup threadGroup, boolean isDaemon) {
        return ThreadUtil.newNamedThreadFactory(prefix, threadGroup, isDaemon);
    }

    /**
     * New named thread factory
     *
     * @param prefix      prefix
     * @param threadGroup thread group
     * @param isDaemon    is daemon
     * @param handler     handler
     * @return the thread factory
     * @since 1.7.0
     */
    @Contract("_, _, _, _ -> new")
    public static @NotNull
    ThreadFactory newNamedThreadFactory(String prefix, ThreadGroup threadGroup, boolean isDaemon,
                                        Thread.UncaughtExceptionHandler handler) {
        return ThreadUtil.newNamedThreadFactory(prefix, threadGroup, isDaemon, handler);
    }

    /**
     * Sync
     *
     * @param obj obj
     * @since 1.7.0
     */
    public static void sync(Object obj) {
        ThreadUtil.sync(obj);

    }

    /**
     * Concurrency test
     *
     * @param threadSize thread size
     * @param runnable   runnable
     * @return the concurrency tester
     * @since 1.7.0
     */
    public static ConcurrencyTester concurrencyTest(int threadSize, Runnable runnable) {
        return ThreadUtil.concurrencyTest(threadSize, runnable);
    }

}
