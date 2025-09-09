package dev.dong4j.zeka.kernel.auth.context;

import org.springframework.util.Assert;

/**
 * 全局安全上下文持有策略实现类
 * <p>
 * 使用静态变量存储安全上下文，全局共享同一个安全上下文实例
 * 适用于单线程应用或需要全局共享用户认证信息的特殊场景
 * <p>
 * 特点：
 * - 所有线程共享同一个安全上下文实例
 * - 不适用于多线程并发场景
 * - 需要谨慎使用，可能存在线程安全问题
 * <p>
 * 警告：该策略在多用户并发场景下会导致用户身份混乱，请谨慎使用
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.09.15 01:41
 * @since 1.0.0
 */
final class GlobalSecurityContextHolderStrategy implements SecurityContextHolderStrategy {
    /** 全局共享的安全上下文实例 */
    private static SecurityContext contextHolder;

    /**
     * 清除全局安全上下文
     *
     * @since 1.0.0
     */
    @Override
    public void clearContext() {
        contextHolder = null;
    }

    /**
     * 获取全局安全上下文
     *
     * @return 全局安全上下文对象，如果不存在则创建新的
     * @since 1.0.0
     */
    @Override
    public SecurityContext getContext() {
        if (contextHolder == null) {
            contextHolder = new SecurityContextImpl();
        }

        return contextHolder;
    }

    /**
     * 设置全局安全上下文
     *
     * @param context 安全上下文对象，不允许为 null
     * @since 1.0.0
     */
    @Override
    public void setContext(SecurityContext context) {
        Assert.notNull(context, "Only non-null SecurityContext instances are permitted");
        contextHolder = context;
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
