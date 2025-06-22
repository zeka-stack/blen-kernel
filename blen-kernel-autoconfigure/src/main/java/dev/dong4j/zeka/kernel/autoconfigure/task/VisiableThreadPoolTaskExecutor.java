package dev.dong4j.zeka.kernel.autoconfigure.task;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * <p>Description: 输出任务总数, 已完成数, 活跃线程数, 队列大小 </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.11.21 22:35
 * @since 1.7.0
 */
@Slf4j
public class VisiableThreadPoolTaskExecutor extends ThreadPoolTaskExecutor {

    /** serialVersionUID */
    private static final long serialVersionUID = -8027792824672301579L;

    /**
     * Show thread pool info
     *
     * @param prefix prefix
     * @since 1.7.0
     */
    private void showThreadPoolInfo(String prefix) {
        ThreadPoolExecutor threadPoolExecutor = this.getThreadPoolExecutor();
        log.info("{}, {},taskCount [{}], completedTaskCount [{}], activeCount [{}], queueSize [{}]",
            this.getThreadNamePrefix(),
            prefix,
            threadPoolExecutor.getTaskCount(),
            threadPoolExecutor.getCompletedTaskCount(),
            threadPoolExecutor.getActiveCount(),
            threadPoolExecutor.getQueue().size());
    }

    /**
     * Execute
     *
     * @param task task
     * @since 1.7.0
     */
    @Override
    public void execute(@NotNull Runnable task) {
        this.showThreadPoolInfo("1. do execute");
        super.execute(task);
    }

    /**
     * Execute
     *
     * @param task         task
     * @param startTimeout start timeout
     * @since 1.7.0
     */
    @Override
    public void execute(@NotNull Runnable task, long startTimeout) {
        this.showThreadPoolInfo("2. do execute");
        super.execute(task, startTimeout);
    }

    /**
     * Submit
     *
     * @param task task
     * @return the future
     * @since 1.7.0
     */
    @Override
    public @NotNull Future<?> submit(@NotNull Runnable task) {
        this.showThreadPoolInfo("1. do submit");
        return super.submit(task);
    }

    /**
     * Submit
     *
     * @param <T>  parameter
     * @param task task
     * @return the future
     * @since 1.7.0
     */
    @Override
    public <T> @NotNull Future<T> submit(@NotNull Callable<T> task) {
        this.showThreadPoolInfo("2. do submit");
        return super.submit(task);
    }

    /**
     * Submit listenable
     *
     * @param task task
     * @return the listenable future
     * @since 1.7.0
     */
    @Override
    public @NotNull ListenableFuture<?> submitListenable(@NotNull Runnable task) {
        this.showThreadPoolInfo("1. do submitListenable");
        return super.submitListenable(task);
    }

    /**
     * Submit listenable
     *
     * @param <T>  parameter
     * @param task task
     * @return the listenable future
     * @since 1.7.0
     */
    @Override
    public <T> @NotNull ListenableFuture<T> submitListenable(@NotNull Callable<T> task) {
        this.showThreadPoolInfo("2. do submitListenable");
        return super.submitListenable(task);
    }
}
