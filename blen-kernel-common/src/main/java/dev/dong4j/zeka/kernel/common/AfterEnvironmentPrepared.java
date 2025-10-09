package dev.dong4j.zeka.kernel.common;

import org.springframework.core.env.ConfigurableEnvironment;

/**
 * <p>应用环境初始化完成后调用, 一般用于根据应用当前环境二次处理配置 </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2024.03.22 11:11
 * @since 1.0.0
 */
public interface AfterEnvironmentPrepared {

    /**
     * Post processing
     *
     * @param environment environment
     * @since 2024.2.0
     */
    default void postProcessing(ConfigurableEnvironment environment) {
    }
}
