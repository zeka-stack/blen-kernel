package dev.dong4j.zeka.kernel.auth.context;

import org.springframework.util.Assert;

/**
 * ThreadLocal 安全上下文持有策略实现类
 * <p>
 * 使用 ThreadLocal 存储安全上下文，确保每个线程拥有独立的安全上下文副本
 * 适用于大多数 Web 应用场景，提供线程安全的用户认证信息存储
 * <p>
 * 特点：
 * - 每个线程的安全上下文完全独立，不会相互干扰
 * - 不支持子线程继承父线程的安全上下文
 * - 适合单线程处理请求的场景
 * <p>
 * 注意：如需子线程继承功能，请使用 {@link InheritableThreadLocalSecurityContextHolderStrategy}
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
     * 清除当前线程的安全上下文
     *
     * @since 1.0.0
     */
    @Override
    public void clearContext() {
        CONTEXT_HOLDER.remove();
    }

    /**
     * 获取当前线程的安全上下文
     *
     * @return 安全上下文对象，如果不存在则创建新的
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
     * 设置当前线程的安全上下文
     *
     * @param context 安全上下文对象，不允许为 null
     * @since 1.0.0
     */
    @Override
    public void setContext(SecurityContext context) {
        Assert.notNull(context, "Only non-null SecurityContext instances are permitted");
        CONTEXT_HOLDER.set(context);
    }

    /**
     * 创建新的空安全上下文实例
     *
     * @return 新的空安全上下文对象
     * @since 1.0.0
     */
    @Override
    public SecurityContext createEmptyContext() {
        return new SecurityContextImpl();
    }
}
