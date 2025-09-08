package dev.dong4j.zeka.kernel.common.support;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import org.jetbrains.annotations.NotNull;

/**
 * 命名线程工厂，用于创建带有有意义名称的线程
 * <p>
 * 该类实现ThreadFactory接口，提供了为线程设置有意义名称的能力
 * 相比于默认的线程工厂，它能够生成更加清晰、可识别的线程名称
 * <p>
 * 主要特性：
 * - 自定义线程名称前缀，方便进行线程识别和调试
 * - 自动编号：线程名称包含递增的编号，确保唯一性
 * - 支持守护线程设置：可以指定创建的线程是否为守护线程
 * - 线程组管理：自动处理线程组的分配和安全性检查
 * - 全局计数器：使用原子计数器保证线程池编号的全局唯一性
 * <p>
 * 使用场景：
 * - 线程池的创建，为不同的业务场景设置不同的线程名称
 * - 异步任务的执行，通过线程名称识别任务类型
 * - 微服务架构中，为不同的服务模块创建可识别的线程
 * - 性能监控和调试，通过线程名称快速定位问题
 * - 定时任务和后台任务的执行，区分不同类型的任务
 * <p>
 * 使用示例：
 * <pre>{@code
 * // 创建默认命名的线程工厂
 * ThreadFactory factory = new NamedThreadFactory();
 * // 线程名称: pool-1-thread-1, pool-1-thread-2, ...
 *
 * // 创建自定义前缀的线程工厂
 * ThreadFactory factory = new NamedThreadFactory("business");
 * // 线程名称: business-thread-1, business-thread-2, ...
 *
 * // 创建守护线程工厂
 * ThreadFactory factory = new NamedThreadFactory("daemon", true);
 *
 * // 在线程池中使用
 * ExecutorService executor = Executors.newFixedThreadPool(5,
 *     new NamedThreadFactory("worker"));
 * }</pre>
 * <p>
 * 线程安全性：
 * - 使用AtomicInteger保证线程编号的原子性
 * - 线程组的获取支持SecurityManager的安全检查
 * - 支持并发环境下的高性能线程创建
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2021.12.09 22:10
 * @since 1.0.0
 */
@SuppressWarnings("all")
public class NamedThreadFactory implements ThreadFactory {

    /** 全局线程池序列号，用于生成唯一的线程池ID */
    protected static final AtomicInteger POOL_SEQ = new AtomicInteger(1);

    /** 当前线程池内的线程编号计数器 */
    protected final AtomicInteger mThreadNum = new AtomicInteger(1);

    /** 线程名称的前缀部分 */
    protected final String mPrefix;

    /** 是否为守护线程的标识 */
    protected final boolean mDaemon;

    /** 线程所属的线程组 */
    protected final ThreadGroup mGroup;

    /**
     * 默认构造函数，创建使用默认命名规则的线程工厂
     * <p>
     * 使用"pool-N"作为线程名称前缀，其中N是全局线程池计数器
     * 创建的线程为非守护线程，适用于普通的业务处理任务
     * <p>
     * 线程命名格式：pool-1-thread-1, pool-1-thread-2, ...
     *
     * @since 1.0.0
     */
    public NamedThreadFactory() {
        this("pool-" + POOL_SEQ.getAndIncrement(), false);
    }

    /**
     * 指定名称前缀的构造函数
     * <p>
     * 使用自定义的前缀作为线程名称的前缀部分
     * 创建的线程为非守护线程，适用于需要区分业务类型的场景
     * <p>
     * 线程命名格式：{prefix}-thread-1, {prefix}-thread-2, ...
     *
     * @param prefix 线程名称的前缀部分
     * @since 1.0.0
     */
    public NamedThreadFactory(String prefix) {
        this(prefix, false);
    }

    /**
     * Named thread factory
     *
     * @param prefix prefix
     * @param daemon daemon
     * @since 1.0.0
     */
    public NamedThreadFactory(String prefix, boolean daemon) {
        mPrefix = prefix + "-thread-";
        mDaemon = daemon;
        SecurityManager s = System.getSecurityManager();
        mGroup = (s == null) ? Thread.currentThread().getThreadGroup() : s.getThreadGroup();
    }

    /**
     * New thread
     *
     * @param runnable runnable
     * @return the thread
     * @since 1.0.0
     */
    @Override
    public Thread newThread(@NotNull Runnable runnable) {
        String name = mPrefix + mThreadNum.getAndIncrement();
        Thread ret = new Thread(mGroup, runnable, name, 0);
        ret.setDaemon(mDaemon);
        return ret;
    }

    /**
     * Gets thread group *
     *
     * @return the thread group
     * @since 1.0.0
     */
    public ThreadGroup getThreadGroup() {
        return mGroup;
    }
}
