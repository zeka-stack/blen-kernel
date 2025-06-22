package dev.dong4j.zeka.kernel.common.support;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2021.12.09 22:10
 * @since 2.1.0
 */
@SuppressWarnings("all")
public class NamedThreadFactory implements ThreadFactory {

    /** POOL_SEQ */
    protected static final AtomicInteger POOL_SEQ = new AtomicInteger(1);

    /** M thread num */
    protected final AtomicInteger mThreadNum = new AtomicInteger(1);

    /** M prefix */
    protected final String mPrefix;

    /** M daemon */
    protected final boolean mDaemon;

    /** M group */
    protected final ThreadGroup mGroup;

    /**
     * Named thread factory
     *
     * @since 2.1.0
     */
    public NamedThreadFactory() {
        this("pool-" + POOL_SEQ.getAndIncrement(), false);
    }

    /**
     * Named thread factory
     *
     * @param prefix prefix
     * @since 2.1.0
     */
    public NamedThreadFactory(String prefix) {
        this(prefix, false);
    }

    /**
     * Named thread factory
     *
     * @param prefix prefix
     * @param daemon daemon
     * @since 2.1.0
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
     * @since 2.1.0
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
     * @since 2.1.0
     */
    public ThreadGroup getThreadGroup() {
        return mGroup;
    }
}
