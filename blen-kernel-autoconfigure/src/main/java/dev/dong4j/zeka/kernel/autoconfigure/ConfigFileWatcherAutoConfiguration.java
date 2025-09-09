package dev.dong4j.zeka.kernel.autoconfigure;

import dev.dong4j.zeka.kernel.common.config.refresh.ConfigFileWatcherCustomizer;
import dev.dong4j.zeka.kernel.common.config.refresh.ConfigFileWatcherRunner;
import java.util.Collections;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * 配置文件监控自动配置类，负责初始化和配置文件监控系统
 * <p>
 * 该自动配置类基于 Spring Boot 的自动配置机制，为系统提供文件变更监听和动态刷新功能
 * 支持自定义配置监控规则，提供灵活的扩展机制，可以根据业务需求进行定制化配置
 * <p>
 * 主要功能：
 * - 自动初始化配置文件监控器
 * - 支持多种自定义配置策略
 * - 提供统一的配置加载和启动机制
 * - 支持业务端自定义扩展
 * <p>
 * 使用场景：微服务配置中心、应用配置热更新、开发环境下的配置调试等
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2019.12.26 21:43
 * @since 1.0.0
 */
@Slf4j
@AutoConfiguration
public class ConfigFileWatcherAutoConfiguration {

    /**
     * 配置文件观察器初始化器，加载组件和业务端的自定义配置，然后启动监控线程
     * <p>
     * 该方法作为 Spring 的智能初始化单例，在所有 Bean 初始化完成后执行
     * 支持通过 ObjectProvider 注入多个自定义配置器，实现灵活的扩展机制
     *
     * @param runner              配置文件监控运行器，负责实际的文件监控和刷新逐辑
     * @param customizersProvider 自定义配置器提供者，包含来自业务端的配置定制逻辑
     * @return 智能初始化单例，用于在容器启动后执行初始化逻辑
     * @since 1.0.0
     */
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Bean
    @ConditionalOnMissingBean(ConfigFileWatcherRunner.class)
    public SmartInitializingSingleton configFileWatcherSmartInit(ConfigFileWatcherRunner runner,
                                                                 ObjectProvider<List<ConfigFileWatcherCustomizer>> customizersProvider) {
        return () -> {
            List<ConfigFileWatcherCustomizer> customizers = customizersProvider.getIfAvailable(Collections::emptyList);
            customizers.forEach(c -> c.customize(runner));

            // 启动监听线程
            runner.start();
        };
    }

}
