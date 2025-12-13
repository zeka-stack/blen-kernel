package dev.dong4j.zeka.kernel.common.config.refresh;

/**
 * 配置文件监视器自定义接口
 * <p>
 * 用于自定义配置文件监视器的行为, 允许在配置文件监视器运行前进行个性化配置或扩展功能
 *
 * @author zeka.stack.team
 * @version 1.0.0
 * @email "mailto:zeka.stack@gmail.com"
 * @date 2025.12.13
 * @since 2.0.0
 */
@FunctionalInterface
public interface ConfigFileWatcherCustomizer {
    /**
     * 自定义 ConfigFileWatcherRunner 的行为
     * <p>
     * 允许通过传入的参数对 ConfigFileWatcherRunner 进行个性化配置或扩展
     *
     * @param runner 要自定义的 ConfigFileWatcherRunner 实例
     */
    void customize(ConfigFileWatcherRunner runner);
}
