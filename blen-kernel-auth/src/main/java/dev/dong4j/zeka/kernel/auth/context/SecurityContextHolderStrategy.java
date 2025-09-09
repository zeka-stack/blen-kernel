package dev.dong4j.zeka.kernel.auth.context;

/**
 * 安全上下文持有策略接口，定义了安全上下文的存储和管理策略
 * <p>
 * 该接口作为策略模式的抽象，定义了不同存储方式的统一操作规范
 * 支持多种存储实现方式，适应不同的线程安全策略和数据传递方式
 * <p>
 * 支持的存储策略实现：
 * - ThreadLocal：线程本地存储，每个线程独立
 * - InheritableThreadLocal：可继承的线程本地存储，支持子线程继承
 * - Global：全局共享存储，适用于单线程场景
 * <p>
 * 通过不同的实现类来支持不同的线程安全策略和数据传递方式
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.09.15 01:41
 * @since 1.0.0
 */
public interface SecurityContextHolderStrategy {

    /**
     * 清除当前存储位置的安全上下文
     *
     * @since 1.0.0
     */
    void clearContext();

    /**
     * 获取当前存储位置的安全上下文
     *
     * @return 安全上下文对象，如果不存在则创建新的空上下文
     * @since 1.0.0
     */
    SecurityContext getContext();

    /**
     * 设置当前存储位置的安全上下文
     *
     * @param context 安全上下文对象，不允许为 null
     * @since 1.0.0
     */
    void setContext(SecurityContext context);

    /**
     * 创建一个新的空安全上下文实例
     *
     * @return 新的空安全上下文对象
     * @since 1.0.0
     */
    SecurityContext createEmptyContext();
}
