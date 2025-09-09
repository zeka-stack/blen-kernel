package dev.dong4j.zeka.kernel.autoconfigure.task;

import java.io.Serial;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * <p>可视化线程池任务执行器.
 * <p>继承 Spring ThreadPoolTaskExecutor，添加线程池状态监控功能.
 * <p>在每次任务执行时，输出线程池的关键指标，便于性能监控和问题排查.
 * <p>监控指标包括：
 * <ul>
 *     <li>任务总数 (taskCount)</li>
 *     <li>已完成任务数 (completedTaskCount)</li>
 *     <li>活跃线程数 (activeCount)</li>
 *     <li>队列大小 (queueSize)</li>
 * </ul>
 * <p>适用场景：生产环境线程池监控、性能调优、资源使用情况追踪.
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.11.21 22:35
 * @since 1.0.0
 */
@Slf4j
public class VisiableThreadPoolTaskExecutor extends ThreadPoolTaskExecutor {

    /** 序列化版本号 */
    @Serial
    private static final long serialVersionUID = -8027792824672301579L;

    /**
     * <p>显示线程池信息.
     * <p>输出线程池的关键运行指标，用于监控和调优.
     *
     * @param prefix 日志前缀，用于区分不同的调用场景
     * @since 1.0.0
     */
    private void showThreadPoolInfo(String prefix) {
        ThreadPoolExecutor threadPoolExecutor = this.getThreadPoolExecutor();
        log.info("{}, {},taskCount [{}], completedTaskCount [{}], activeCount [{}], queueSize [{}]",
            this.getThreadNamePrefix(),
            prefix,
            threadPoolExecutor.getTaskCount(),           // 任务总数
            threadPoolExecutor.getCompletedTaskCount(),  // 已完成任务数
            threadPoolExecutor.getActiveCount(),         // 活跃线程数
            threadPoolExecutor.getQueue().size());       // 队列中等待的任务数
    }

    /**
     * <p>执行任务 (Runnable).
     * <p>重写父类方法，在执行前输出线程池状态信息.
     *
     * @param task 要执行的任务
     * @since 1.0.0
     */
    @Override
    public void execute(@NotNull Runnable task) {
        this.showThreadPoolInfo("1. do execute");
        super.execute(task);
    }

    /**
     * <p>执行任务 (Runnable 带超时).
     * <p>重写父类方法，在执行前输出线程池状态信息.
     *
     * @param task         要执行的任务
     * @param startTimeout 启动超时时间 (毫秒)
     * @since 1.0.0
     */
    @Override
    public void execute(@NotNull Runnable task, long startTimeout) {
        this.showThreadPoolInfo("2. do execute");
        super.execute(task, startTimeout);
    }

    /**
     * <p>提交任务 (Runnable).
     * <p>重写父类方法，在提交前输出线程池状态信息.
     *
     * @param task 要提交的任务
     * @return Future 对象，用于获取任务执行结果
     * @since 1.0.0
     */
    @Override
    public @NotNull Future<?> submit(@NotNull Runnable task) {
        this.showThreadPoolInfo("1. do submit");
        return super.submit(task);
    }

    /**
     * <p>提交任务 (Callable).
     * <p>重写父类方法，在提交前输出线程池状态信息.
     *
     * @param <T>  返回值类型参数
     * @param task 要提交的任务
     * @return Future 对象，用于获取任务执行结果
     * @since 1.0.0
     */
    @Override
    public <T> @NotNull Future<T> submit(@NotNull Callable<T> task) {
        this.showThreadPoolInfo("2. do submit");
        return super.submit(task);
    }

}
