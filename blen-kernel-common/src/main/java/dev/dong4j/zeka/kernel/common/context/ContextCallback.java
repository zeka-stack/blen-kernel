package dev.dong4j.zeka.kernel.common.context;

/**
 * <p>Description: 针对某些初始化方法, 在 SpringContext 初始化前时, 可提交一个 提交回调任务. 在 SpringContext 初始化后, 进行回调使用
 * </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.11.25 20:37
 * @since 1.7.0
 */
public interface ContextCallback {
    /**
     * 回调执行方法
     *
     * @since 1.7.0
     */
    void executor();

    /**
     * 本回调任务名称
     *
     * @return /
     * @since 1.7.0
     */
    default String getCallbackName() {
        return Thread.currentThread().getId() + ":" + this.getClass().getName();
    }
}

