package dev.dong4j.zeka.kernel.common;

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.05.19 14:33
 * @since 1.4.0
 */
public interface ApplicationInitializedListener {

    /**
     * 当所有应用程序级组件都在组件初始化的同一线程中初始化时调用
     *
     * @since 1.4.0
     */
    void componentsInitialized();
}
