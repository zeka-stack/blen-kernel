package dev.dong4j.zeka.kernel.autoconfigure.condition;

import dev.dong4j.zeka.kernel.autoconfigure.ZekaProperties;
import dev.dong4j.zeka.kernel.common.util.StringUtils;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * 条件判断：检查配置中是否启用模块
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2025.07.23
 * @since 1.0.0
 */
public class OnModuleEnableCondition implements Condition {
    @Override
    public boolean matches(@NotNull ConditionContext context, AnnotatedTypeMetadata metadata) {
        // 获取注解上的属性
        Map<String, Object> attributes = metadata.getAnnotationAttributes(ConditionalOnEnabled.class.getName());
        String prefix = null;
        if (attributes != null) {
            prefix = (String) attributes.get("prefix");
        }
        if (StringUtils.isBlank(prefix)) {
            return false;
        }
        // 获取配置值：prefix + ".enabled"
        String enabledValue = context.getEnvironment().getProperty(prefix + "." + ZekaProperties.ENABLED);
        // 默认为开启状态
        return ZekaProperties.ON.equalsIgnoreCase(enabledValue) || enabledValue == null;
    }
}
