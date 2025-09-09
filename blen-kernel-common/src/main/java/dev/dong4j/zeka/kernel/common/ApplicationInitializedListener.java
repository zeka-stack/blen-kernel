package dev.dong4j.zeka.kernel.common;

/**
 * <p>应用程序初始化监听器接口.
 * <p>用于在应用程序所有组件初始化完成后执行特定的回调操作.
 * <p>该接口允许开发者在应用启动的关键时刻注入自定义逻辑，确保在所有必要组件就绪后执行.
 * <p>主要特点：
 * <ul>
 *     <li>组件初始化完成后的回调机制</li>
 *     <li>在同一线程中执行，保证顺序性</li>
 *     <li>适用于应用启动后的初始化工作</li>
 * </ul>
 * <p>使用场景：
 * <ul>
 *     <li>数据预加载和缓存初始化</li>
 *     <li>定时任务的启动配置</li>
 *     <li>第三方服务的连接建立</li>
 *     <li>应用状态的初始化设置</li>
 * </ul>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.05.19 14:33
 * @since 1.0.0
 */
public interface ApplicationInitializedListener {

    /**
     * 当所有应用程序级组件都在组件初始化的同一线程中初始化时调用
     *
     * @since 1.0.0
     */
    void componentsInitialized();
}
