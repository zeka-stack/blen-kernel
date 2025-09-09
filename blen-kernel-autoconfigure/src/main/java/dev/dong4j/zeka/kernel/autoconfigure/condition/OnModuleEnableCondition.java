package dev.dong4j.zeka.kernel.autoconfigure.condition;

import dev.dong4j.zeka.kernel.autoconfigure.ZekaProperties;
import dev.dong4j.zeka.kernel.common.util.StringUtils;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * <p>模块启用条件判断实现类.
 * <p>实现 Spring Condition 接口，用于检查配置中是否启用特定模块.
 * <p>与 @ConditionalOnEnabled 注解配合使用，实现模块级别的条件化配置.
 * <p>判断逻辑：
 * <ul>
 *     <li>从注解获取模块前缀配置 (prefix)</li>
 *     <li>查找配置项 {prefix}.enabled 的值</li>
 *     <li>配置值为 'on' 或 null 时，条件成立</li>
 *     <li>配置值为其他值时，条件不成立</li>
 * </ul>
 * <p>默认启用策略：当配置项不存在时，默认为启用状态.
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2025.07.23
 * @since 1.0.0
 */
public class OnModuleEnableCondition implements Condition {
    /**
     * <p>条件匹配方法.
     * <p>检查配置中是否启用指定模块.
     *
     * @param context  条件上下文，提供环境信息
     * @param metadata 注解元数据，包含 @ConditionalOnEnabled 的配置信息
     * @return {@code true} 模块启用；{@code false} 模块禁用
     */
    @Override
    public boolean matches(@NotNull ConditionContext context, AnnotatedTypeMetadata metadata) {
        // 获取 @ConditionalOnEnabled 注解的属性
        Map<String, Object> attributes = metadata.getAnnotationAttributes(ConditionalOnEnabled.class.getName());
        String prefix = null;
        if (attributes != null) {
            // 获取注解的 value 属性作为配置前缀
            prefix = (String) attributes.get("value");
        }
        // 如果前缀为空，则条件不成立
        if (StringUtils.isBlank(prefix)) {
            return false;
        }
        // 构造完整的配置项路径：{prefix}.enabled
        String enabledValue = context.getEnvironment().getProperty(prefix + "." + ZekaProperties.ENABLED);
        // 判断条件：配置值为 'on'（忽略大小写）或配置不存在时（默认启用）
        return ZekaProperties.ON.equalsIgnoreCase(enabledValue) || enabledValue == null;
    }
}
