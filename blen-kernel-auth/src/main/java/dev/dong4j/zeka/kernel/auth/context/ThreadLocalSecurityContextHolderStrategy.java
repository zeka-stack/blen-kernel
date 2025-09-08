package dev.dong4j.zeka.kernel.auth.context;

import org.springframework.util.Assert;

/**
 * ThreadLocal安全上下文持有策略实现类
 * 使用ThreadLocal存储安全上下文，确保每个线程拥有独立的安全上下文副本
 * 适用于大多数Web应用场景，提供线程安全的用户认证信息存储
 * 不支持子线程继承父线程的安全上下文，如需此功能请使用InheritableThreadLocalSecurityContextHolderStrategy
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.09.15 01:41
 * @since 1.0.0
 */
final class ThreadLocalSecurityContextHolderStrategy implements
    SecurityContextHolderStrategy {

    /** 线程本地存储的安全上下文持有者 */
    private static final ThreadLocal<SecurityContext> CONTEXT_HOLDER = new ThreadLocal<>();

    /**
     * Clear context
     *
     * @since 1.0.0
     */
    @Override
    public void clearContext() {
        CONTEXT_HOLDER.remove();
    }

    /**
     * Gets context *
     *
     * @return the context
     * @since 1.0.0
     */
    @Override
    public SecurityContext getContext() {
        SecurityContext ctx = CONTEXT_HOLDER.get();

        if (ctx == null) {
            ctx = this.createEmptyContext();
            CONTEXT_HOLDER.set(ctx);
        }

        return ctx;
    }

    /**
     * Sets context *
     *
     * @param context context
     * @since 1.0.0
     */
    @Override
    public void setContext(SecurityContext context) {
        Assert.notNull(context, "Only non-null SecurityContext instances are permitted");
        CONTEXT_HOLDER.set(context);
    }

    /**
     * Create empty context
     *
     * @return the security context
     * @since 1.0.0
     */
    @Override
    public SecurityContext createEmptyContext() {
        return new SecurityContextImpl();
    }
}
