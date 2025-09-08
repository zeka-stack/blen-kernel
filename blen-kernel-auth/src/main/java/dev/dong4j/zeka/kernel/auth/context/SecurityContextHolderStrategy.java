package dev.dong4j.zeka.kernel.auth.context;

/**
 * 安全上下文持有策略接口，定义了安全上下文的存储和管理策略
 * 提供了不同的存储方式：ThreadLocal、InheritableThreadLocal和Global
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
     * Clear context
     *
     * @since 1.0.0
     */
    void clearContext();

    /**
     * Gets context *
     *
     * @return the context
     * @since 1.0.0
     */
    SecurityContext getContext();

    /**
     * Sets context *
     *
     * @param context context
     * @since 1.0.0
     */
    void setContext(SecurityContext context);

    /**
     * Create empty context
     *
     * @return the security context
     * @since 1.0.0
     */
    SecurityContext createEmptyContext();
}
