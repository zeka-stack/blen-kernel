package dev.dong4j.zeka.kernel.common.config.refresh;

import dev.dong4j.zeka.kernel.common.util.Jsons;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.origin.OriginTrackedValue;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;

/**
 * 6: 根据配置变更项（扁平化 key 集合）精准刷新受影响的 {@code @ConfigurationProperties} Bean。
 *
 * <p>刷新逻辑：</p>
 * <ol>
 *   <li>从 {@link RefreshScopeRegistry} 获取所有被 {@code @RefreshScope} 标记且
 *       同时带 {@code @ConfigurationProperties} 的 Bean。</li>
 *   <li>读取其 {@code prefix}，判断变更的 key 是否以该前缀开头。</li>
 *   <li>若受影响则使用 Spring Boot {@link Binder} 将最新
 *       {@link Environment} 值重新绑定到现有 Bean 实例。</li>
 * </ol>
 *
 * <p>注意：此实现<strong>不会</strong>重新创建 Bean，只是把新值绑定到已有实例，
 * 因此要求属性通过标准的 setter 或可变字段暴露。</p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2025.07.29
 * @since 1.0.0
 */
@Slf4j
public class RefreshScopeRefresher {

    /** Spring 的环境对象，包含最新 PropertySource */
    private final Environment environment;
    private final DynamicConfigLoader dynamicConfigLoader;

    /** 可刷新 Bean 注册表 */
    private final RefreshScopeRegistry registry;

    public RefreshScopeRefresher(Environment environment,
                                 RefreshScopeRegistry registry,
                                 DynamicConfigLoader dynamicConfigLoader) {
        this.environment = environment;
        this.registry = registry;
        this.dynamicConfigLoader = dynamicConfigLoader;
    }

    /**
     * 根据发生变化的配置 key 刷新对应的配置类。
     *
     * @param changedKeys 以点号扁平化的配置 key，如 {@code spring.datasource.url}
     */
    public void refreshByChangedKeys(Set<String> changedKeys) {
        if (changedKeys == null || changedKeys.isEmpty()) {
            return;
        }

        for (RefreshScopeRegistry.BindableTarget target : registry.getBindableTargets()) {
            String prefix = target.getPrefix();
            Object bean = target.getBean();

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
     * 使用最新配置更新环境
     *
     * @param latestConfig 最新配置
     */
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
        sources.addFirst(override); // 确保优先级最高
    }

    /**
     * 平坦而干净
     *
     * @param original 原来
     * @return 地图<字符串 ， 对象>
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
