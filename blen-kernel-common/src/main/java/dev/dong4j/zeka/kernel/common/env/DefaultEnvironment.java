package dev.dong4j.zeka.kernel.common.env;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.StandardEnvironment;

/**
 * 默认环境配置实现类，继承自Spring的StandardEnvironment
 * <p>
 * 该类扩展了Spring的标准环境实现，支持自定义属性源的动态注入
 * 可以通过Map或PropertySource集合的方式添加额外的配置信息
 * 提供了灵活的构造方式，适应不同的配置加载需求
 * <p>
 * 主要特性：
 * - 自定义属性源：支持注入自定义的配置属性源
 * - 多种构造方式：支持Map、单个PropertySource、多个PropertySource的注入
 * - 优先级管理：自动管理不同属性源的优先级顺序
 * - Spring集成：完全兼容Spring的Environment接口和特性
 * - 可扩展性：可以进一步继承和自定义
 * <p>
 * 属性源优先级（从高到低）：
 * 1. 构造函数传入的PropertySource集合（最高优先级）
 * 2. Spring标准属性源（系统属性、环境变量等）
 * 3. 构造函数传入的Map属性（默认属性）
 * <p>
 * 使用场景：
 * - 单元测试中的环境模拟和配置注入
 * - 非 Spring Boot 应用中的自定义环境管理
 * - 多数据源配置的统一管理
 * - 动态配置加载和环境切换
 * - 微服务中的特殊配置需求
 * <p>
 * 使用示例：
 * <pre>{@code
 * // 使用Map注入自定义配置
 * Map<String, Object> customConfig = new HashMap<>();
 * customConfig.put("app.name", "my-app");
 * customConfig.put("app.version", "1.0.0");
 * Environment env = new DefaultEnvironment(customConfig);
 *
 * // 使用PropertySource注入配置
 * PropertySource<?> source = new MapPropertySource("custom", customConfig);
 * Environment env = new DefaultEnvironment(source);
 *
 * // 使用多个PropertySource
 * List<PropertySource<?>> sources = Arrays.asList(source1, source2);
 * Environment env = new DefaultEnvironment(sources);
 * }</pre>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.04.10 16:51
 * @since 1.0.0
 */
public class DefaultEnvironment extends StandardEnvironment {

    /** DEFAULT_PROPERTIES_PROPERTY_SOURCE_NAME */
    public static final String DEFAULT_PROPERTIES_PROPERTY_SOURCE_NAME = "defaultProperties";
    /** DEFAULT_EXTEND_PROPERTIES_PROPERTY_SOURCE_NAME */
    public static final String DEFAULT_EXTEND_PROPERTIES_PROPERTY_SOURCE_NAME = "defaultExtendProperties";

    /** Property source */
    private Map<String, Object> mapProperties;
    /** Property source */
    private Collection<PropertySource<?>> propertySources;

    /**
     * Default environment
     *
     * @since 1.0.0
     */
    public DefaultEnvironment() {
        super();
    }

    /**
     * Default environment
     *
     * @param mapProperties map properties
     * @since 1.0.0
     */
    public DefaultEnvironment(Map<String, Object> mapProperties) {
        this();
        this.mapProperties = mapProperties;
    }

    /**
     * Default environment
     *
     * @param propertySource property source
     * @since 1.0.0
     */
    public DefaultEnvironment(PropertySource<?> propertySource) {
        this(new ArrayList<>(Collections.singleton(propertySource)));
    }

    /**
     * Default environment
     *
     * @param propertySources property sources
     * @since 1.0.0
     */
    public DefaultEnvironment(Collection<PropertySource<?>> propertySources) {
        this();
        this.propertySources = propertySources;
    }


    /**
     * Customize property sources *
     *
     * @param propertySources property sources
     * @since 1.0.0
     */
    @Override
    protected void customizePropertySources(@NotNull MutablePropertySources propertySources) {
        if (this.mapProperties != null) {
            propertySources.addLast(new DefaultEnvironmentPropertySource(DEFAULT_PROPERTIES_PROPERTY_SOURCE_NAME, this.mapProperties));
        }

        if (this.propertySources != null && propertySources.size() > 0) {
            propertySources.forEach(propertySources::addFirst);
        }

        super.customizePropertySources(propertySources);
    }

}
