package dev.dong4j.zeka.kernel.common.config.refresh;

import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.origin.OriginTrackedValue;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import dev.dong4j.zeka.kernel.common.util.Jsons;
import lombok.extern.slf4j.Slf4j;

/**
 * 配置刷新管理类
 * <p>
 * 负责根据配置项的变化触发对应的配置刷新操作, 主要处理配置前缀匹配的 Bean, 并更新其配置值.
 * 该类通过动态加载配置, 扁平化处理配置数据, 并将最新配置绑定到对应的 Bean 实例上, 实现配置的动态刷新功能.
 *
 * @author zeka.stack.team
 * @version 1.0.0
 * @email "mailto:zeka.stack@gmail.com"
 * @date 2025.12.13
 * @since 2.0.0
 */
@Slf4j
public class RefreshScopeRefresher {

    /**
     * 应用程序的环境配置
     * <p>
     * 用于获取和管理应用程序运行时的环境变量和配置信息
     */
    private final Environment environment;
    /**
     * 动态配置加载器
     * <p>
     * 用于加载和管理动态配置信息
     */
    private final DynamicConfigLoader dynamicConfigLoader;

    /**
     * 注册表, 用于管理刷新作用域
     * <p>
     * 该注册表负责跟踪和管理所有刷新作用域的生命周期和状态
     *
     * @see RefreshScopeRegistry
     */
    private final RefreshScopeRegistry registry;

    /**
     * 初始化 RefreshScopeRefresher 实例
     * <p>
     * 通过传入的环境, 刷新作用域注册表和动态配置加载器来初始化对象
     *
     * @param environment         环境配置对象
     * @param registry            刷新作用域注册表
     * @param dynamicConfigLoader 动态配置加载器
     */
    public RefreshScopeRefresher(Environment environment,
                                 RefreshScopeRegistry registry,
                                 DynamicConfigLoader dynamicConfigLoader) {
        this.environment = environment;
        this.registry = registry;
        this.dynamicConfigLoader = dynamicConfigLoader;
    }

    /**
     * 根据变更的配置键刷新对应的配置对象
     * <p>
     * 遍历所有可绑定的目标对象, 检查是否有配置键以目标对象的前缀开头. 如果有, 则加载最新的配置并刷新该对象.
     *
     * @param changedKeys 已变更的配置键集合, 用于判断哪些配置对象需要刷新
     */
    public void refreshByChangedKeys(Set<String> changedKeys) {
        if (changedKeys == null || changedKeys.isEmpty()) {
            return;
        }

        for (RefreshScopeRegistry.BindableTarget target : registry.getBindableTargets()) {
            String prefix = target.prefix();
            Object bean = target.bean();

            boolean affected = changedKeys.stream().anyMatch(k -> k.startsWith(prefix));
            if (!affected) {
                log.trace("不满足配置动态刷新要求,被修改的配置前缀: {}", changedKeys);
                continue;
            }

            log.info("[Refresher] 刷新配置类 {}（前缀：{})", bean.getClass().getSimpleName(), prefix);

            Map<String, Object> latest = dynamicConfigLoader.loadCurrentEnvironmentConfig();
            Map<String, Object> flat = flattenAndClean(latest);
            updateEnvironmentWithLatestConfig(flat);

            // 将最新配置重新绑定到配置类上
            Binder.get(environment).bind(
                prefix.substring(0, prefix.length() - 1),
                Bindable.ofInstance(bean)
            );

            log.debug("刷新后的配置类: {}", Jsons.toJson(bean, true));
        }
    }

    /**
     * 使用最新的配置更新环境属性
     * <p>
     * 将指定的最新配置作为 "refresh-overrides" 属性源添加到环境的最前面, 如果已存在同名属性源则先移除
     *
     * @param latestConfig 最新的配置项, 以键值对形式表示
     */
    @SuppressWarnings("PMD.UndefineMagicConstantRule")
    private void updateEnvironmentWithLatestConfig(Map<String, Object> latestConfig) {
        ConfigurableEnvironment configurableEnvironment =
            (ConfigurableEnvironment) this.environment;

        MutablePropertySources sources = configurableEnvironment.getPropertySources();

        // 移除旧的我们添加的临时 PropertySource（如有）
        if (sources.contains("refresh-overrides")) {
            sources.remove("refresh-overrides");
        }

        // 加入新的临时 PropertySource
        MapPropertySource override = new MapPropertySource("refresh-overrides", latestConfig);
        // 确保优先级最高
        sources.addFirst(override);
    }

    /**
     * 将嵌套的 Map 结构展平并清理, 去除 OriginTrackedValue 包装
     * <p>
     * 遍历原始 Map, 若值为 OriginTrackedValue 则提取其内部值; 若值为 Map 则递归展平并合并到结果中, 使用父键名拼接子键名; 否则直接保留原值.
     *
     * @param original 原始的嵌套 Map 对象
     * @return 展平并清理后的 Map 对象
     */
    private Map<String, Object> flattenAndClean(Map<String, Object> original) {
        Map<String, Object> result = new LinkedHashMap<>();
        original.forEach((key, value) -> {
            if (value instanceof OriginTrackedValue) {
                result.put(key, ((OriginTrackedValue) value).getValue());
            } else if (value instanceof Map) {
                // 递归处理嵌套 Map
                @SuppressWarnings("unchecked")
                Map<String, Object> nested = flattenAndClean((Map<String, Object>) value);
                nested.forEach((k, v) -> result.put(key + "." + k, v));
            } else {
                result.put(key, value);
            }
        });
        return result;
    }

}
