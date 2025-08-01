package dev.dong4j.zeka.kernel.common.config.refresh;

import dev.dong4j.zeka.kernel.common.util.StringUtils;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.ApplicationContext;

/**
 * 2: 构建核心配置类注册与管理中心
 * 该类负责记录所有配置类的 Bean 引用；
 * 初始化时自动扫描容器中带有 @RefreshScope + @ConfigurationProperties 的 Bean；
 * 提供获取所有可刷新的 Bean 的能力，供后续刷新逻辑使用。
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2025.07.29
 * @since 1.0.0
 */
@Slf4j
public class RefreshScopeRegistry {
    private final Map<String, BindableTarget> prefixBindableMap = new ConcurrentHashMap<>();
    private final Map<String, Object> refreshableBeans = new ConcurrentHashMap<>();
    private final ApplicationContext context;

    public RefreshScopeRegistry(ApplicationContext context) {
        this.context = context;
        initialize();
    }

    private void initialize() {
        Map<String, Object> candidates = context.getBeansWithAnnotation(RefreshScope.class);
        for (Map.Entry<String, Object> entry : candidates.entrySet()) {
            String beanName = entry.getKey();
            Object bean = entry.getValue();

            ConfigurationProperties cpAnno = bean.getClass().getAnnotation(ConfigurationProperties.class);
            if (cpAnno != null) {
                refreshableBeans.put(beanName, bean);

                String prefix = normalizePrefix(cpAnno.prefix());
                prefixBindableMap.put(prefix, new BindableTarget(bean, prefix));
            } else {
                log.warn("{} 未添加 @ConfigurationProperties, 无法自动刷新配置", beanName);
            }
        }
    }

    public Map<String, Object> getAll() {
        return Collections.unmodifiableMap(refreshableBeans);
    }

    public Object get(String beanName) {
        return refreshableBeans.get(beanName);
    }

    public Collection<BindableTarget> getBindableTargets() {
        return prefixBindableMap.values();
    }

    private String normalizePrefix(String prefix) {
        if (StringUtils.isBlank(prefix)) {
            return "";
        }
        return prefix.endsWith(".") ? prefix : prefix + ".";
    }

    @Getter
    public static class BindableTarget {
        private final Object bean;
        private final String prefix;

        public BindableTarget(Object bean, String prefix) {
            this.bean = bean;
            this.prefix = prefix;
        }
    }
}
