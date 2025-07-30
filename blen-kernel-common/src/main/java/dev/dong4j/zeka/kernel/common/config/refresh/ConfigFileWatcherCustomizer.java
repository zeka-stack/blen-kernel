package dev.dong4j.zeka.kernel.common.config.refresh;

/**
 * 自定义需要监听的文件
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2025.07.30
 * @since 1.0.0
 */
@FunctionalInterface
public interface ConfigFileWatcherCustomizer {
    void customize(ConfigFileWatcherRunner runner);
}
