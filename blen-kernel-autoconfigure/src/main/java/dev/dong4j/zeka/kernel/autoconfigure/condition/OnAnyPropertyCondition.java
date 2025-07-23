package dev.dong4j.zeka.kernel.autoconfigure.condition;

import cn.hutool.core.util.ObjectUtil;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * 存在任一配置就满足条件
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2025.07.23
 * @since 1.0.0
 */
public class OnAnyPropertyCondition implements Condition {
    @Override
    public boolean matches(@SuppressWarnings("NullableProblems") ConditionContext context, AnnotatedTypeMetadata metadata) {

        // 获取注解上的属性
        Map<String, Object> attributes = metadata.getAnnotationAttributes(ConditionalOnAnyProperty.class.getName());
        String[] value = null;
        if (attributes != null) {
            value = (String[]) attributes.get("value");
        }
        if (ObjectUtil.isEmpty(value)) {
            return false;
        }

        return Arrays.stream(Objects.requireNonNull(value))
            .anyMatch(prop -> context.getEnvironment().containsProperty(prop));
    }
}
