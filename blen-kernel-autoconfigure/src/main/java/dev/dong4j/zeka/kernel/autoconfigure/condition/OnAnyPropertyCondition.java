package dev.dong4j.zeka.kernel.autoconfigure.condition;

import cn.hutool.core.util.ObjectUtil;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * <p>任意属性条件判断实现类.
 * <p>实现 Spring Condition 接口，用于检查配置中是否存在任意指定的属性.
 * <p>与 @ConditionalOnAnyProperty 注解配合使用，实现灵活的条件化配置.
 * <p>判断逻辑：
 * <ul>
 *     <li>从注解获取属性名称数组</li>
 *     <li>检查环境中是否存在任意一个指定的属性</li>
 *     <li>只要有一个属性存在，条件就成立</li>
 * </ul>
 * <p>适用场景：多种配置方式兼容、可选配置项检查等.
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2025.07.23
 * @since 1.0.0
 */
public class OnAnyPropertyCondition implements Condition {
    /**
     * <p>条件匹配方法.
     * <p>检查环境中是否存在任意一个指定的配置属性.
     *
     * @param context  条件上下文，提供环境信息
     * @param metadata 注解元数据，包含 @ConditionalOnAnyProperty 的配置信息
     * @return {@code true} 存在任意一个指定属性；{@code false} 不存在任何指定属性
     */
    @Override
    public boolean matches(@SuppressWarnings("NullableProblems") ConditionContext context, AnnotatedTypeMetadata metadata) {

        // 获取 @ConditionalOnAnyProperty 注解的属性
        Map<String, Object> attributes = metadata.getAnnotationAttributes(ConditionalOnAnyProperty.class.getName());
        String[] value = null;
        if (attributes != null) {
            // 获取注解的 value 属性作为要检查的属性名数组
            value = (String[]) attributes.get("value");
        }
        // 如果属性数组为空，则条件不成立
        if (ObjectUtil.isEmpty(value)) {
            return false;
        }

        // 使用流式处理检查是否存在任意一个指定的属性
        return Arrays.stream(Objects.requireNonNull(value))
            .anyMatch(prop -> context.getEnvironment().containsProperty(prop));
    }
}
