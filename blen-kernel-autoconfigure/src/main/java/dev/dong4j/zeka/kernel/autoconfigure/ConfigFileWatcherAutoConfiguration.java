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

@Slf4j
@AutoConfiguration
public class ConfigFileWatcherAutoConfiguration {

    /**
     * 配置文件观察器初始化器, 加载组件和业务端的自定义配置, 然后启动监控线程
     *
     * @param runner              跑步者
     * @param customizersProvider 定制器提供商
     * @return 聪明初始化单例
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
