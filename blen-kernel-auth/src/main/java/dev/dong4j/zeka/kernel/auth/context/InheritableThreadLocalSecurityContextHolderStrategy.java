package dev.dong4j.zeka.kernel.auth.context;

import org.springframework.util.Assert;

/**
 * 可继承的 ThreadLocal 安全上下文持有策略实现类
 * <p>
 * 使用 InheritableThreadLocal 存储安全上下文，支持子线程继承父线程的安全上下文
 * 适用于需要在子线程中传递用户认证信息的场景，如异步任务处理
 * <p>
 * 特点：
 * - 支持子线程自动继承父线程的安全上下文
 * - 适合多线程并发处理场景
 * - 在异步操作中保持用户身份一致性
 * <p>
 * 使用场景：异步任务、定时任务、线程池任务等需要保持用户上下文的场景
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.09.15 01:41
 * @since 1.0.0
 */
final class InheritableThreadLocalSecurityContextHolderStrategy implements
    SecurityContextHolderStrategy {
    /** 可继承的线程本地存储的安全上下文持有者 */
    private static final ThreadLocal<SecurityContext> CONTEXT_HOLDER = new InheritableThreadLocal<>();

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
     * 获取当前线程的安全上下文，支持从父线程继承
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
     * 设置当前线程的安全上下文，子线程可继承此上下文
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
