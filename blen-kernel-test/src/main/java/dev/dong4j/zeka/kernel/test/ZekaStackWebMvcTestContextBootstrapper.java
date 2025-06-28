package dev.dong4j.zeka.kernel.test;

import org.jetbrains.annotations.NotNull;
import org.springframework.test.context.MergedContextConfiguration;
import org.springframework.test.context.web.WebMergedContextConfiguration;

/**
 * Zeka Stack Web MVC 测试上下文引导程序
 * 用于处理 Web MVC 测试环境的上下文配置
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2025.06.28
 * @since 1.0.0
 */
class ZekaStackWebMvcTestContextBootstrapper extends ZekaStackTestContextBootstrapper {
    /**
     * 处理合并后的上下文配置
     * 将普通的 MergedContextConfiguration 转换为 Web 环境的 WebMergedContextConfiguration
     *
     * @param mergedConfig 合并配置
     * @return {@link MergedContextConfiguration } Web 环境的上下文配置
     */
    protected @NotNull MergedContextConfiguration processMergedContextConfiguration(MergedContextConfiguration mergedConfig) {
        MergedContextConfiguration processedMergedConfiguration = super.processMergedContextConfiguration(mergedConfig);
        return new WebMergedContextConfiguration(processedMergedConfiguration, this.determineResourceBasePath(mergedConfig));
    }

}
