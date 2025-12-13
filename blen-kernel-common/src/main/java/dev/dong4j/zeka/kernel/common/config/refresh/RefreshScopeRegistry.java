package dev.dong4j.zeka.kernel.common.config.refresh;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.ApplicationContext;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import dev.dong4j.zeka.kernel.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * 刷新作用域注册类
 * <p>
 * 用于注册和管理带有 @RefreshScope 注解的 Bean, 支持根据配置前缀绑定目标对象, 实现配置的动态刷新功能.
 * 该类通过扫描上下文中带有 @RefreshScope 注解的 Bean, 并检查是否添加了 @ConfigurationProperties 注解,
 * 以确定是否将其纳入可刷新的配置对象集合中.
 *
 * @author zeka.stack.team
 * @version 1.0.0
 * @email "mailto:zeka.stack@gmail.com"
 * @date 2025.12.13
 * @since 2.0.0
 */
@Slf4j
public class RefreshScopeRegistry {
    /**
     * 用于存储前缀与可绑定目标的映射关系
     * <p>
     * 该映射表使用 ConcurrentHashMap 实现, 保证线程安全
     */
    private final Map<String, BindableTarget> prefixBindableMap = new ConcurrentHashMap<>();
    /**
     * 可刷新的 Bean 映射
     * <p>
     * 用于存储可以被刷新的 Bean 实例, 键为 Bean 名称, 值为 Bean 对象
     *
     * @see ConcurrentHashMap
     */
    private final Map<String, Object> refreshableBeans = new ConcurrentHashMap<>();
    /**
     * 应用上下文对象
     * <p>
     * 用于访问 Spring 容器中的 Bean 和其他应用资源
     */
    private final ApplicationContext context;

    /**
     * 初始化 RefreshScope 注册表
     * <p>
     * 使用提供的应用上下文进行初始化, 并调用初始化方法以设置注册表
     *
     * @param context 应用上下文, 用于获取相关 Bean 和配置信息
     */
    public RefreshScopeRegistry(ApplicationContext context) {
        this.context = context;
        initialize();
    }

    /**
     * 初始化可刷新的配置 Bean
     * <p>
     * 从上下文中获取所有带有 {@code @RefreshScope} 注解的 Bean, 并检查它们是否带有 {@code @ConfigurationProperties} 注解.
     * 如果存在, 则将其添加到可刷新 Bean 集合中, 并建立前缀与绑定目标的映射关系.
     * 如果未添加 {@code @ConfigurationProperties} 注解, 则记录警告信息.
     *
     * @param context 应用上下文, 用于获取带有 {@code @RefreshScope} 注解的 Bean
     */
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

    /**
     * 获取所有可刷新的 Bean 信息
     * <p>
     * 返回一个不可修改的 Map, 包含所有可刷新的 Bean 名称及其对应的对象
     *
     * @return 包含所有可刷新 Bean 名称和对象的不可修改 Map
     */
    public Map<String, Object> getAll() {
        return Collections.unmodifiableMap(refreshableBeans);
    }

    /**
     * 根据指定的 bean 名称获取对应的对象
     * <p>
     * 从 refreshableBeans 集合中查找并返回与给定名称关联的 bean 对象
     *
     * @param beanName 要获取的 bean 的名称
     * @return 与指定名称关联的 bean 对象, 如果不存在则返回 null
     */
    public Object get(String beanName) {
        return refreshableBeans.get(beanName);
    }

    /**
     * 获取所有可绑定的目标对象
     * <p>
     * 返回存储在 prefixBindableMap 中的所有可绑定目标的集合
     *
     * @return 所有可绑定的目标对象的集合
     */
    public Collection<BindableTarget> getBindableTargets() {
        return prefixBindableMap.values();
    }

    /**
     * 对前缀进行标准化处理
     * <p>
     * 如果传入的前缀为空或空白字符串, 则返回空字符串. 否则, 如果前缀不以点号结尾, 则在末尾添加点号.
     *
     * @param prefix 需要标准化的前缀字符串
     * @return 标准化后的前缀字符串
     */
    private String normalizePrefix(String prefix) {
        if (StringUtils.isBlank(prefix)) {
            return "";
        }
        return prefix.endsWith(".") ? prefix : prefix + ".";
    }

    /**
     * 可绑定目标记录类
     * <p>
     * 用于表示一个可绑定的目标对象, 包含目标对象和绑定前缀信息, 通常用于数据绑定或配置场景中
     *
     * @author zeka.stack.team
     * @version 1.0.0
     * @email "mailto:zeka.stack@gmail.com"
     * @date 2025.12.13
     * @since 2.0.0
     */
    public record BindableTarget(Object bean, String prefix) {
    }
}
