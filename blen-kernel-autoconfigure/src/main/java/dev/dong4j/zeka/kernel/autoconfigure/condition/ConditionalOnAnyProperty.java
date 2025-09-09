package dev.dong4j.zeka.kernel.autoconfigure.condition;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.Conditional;

/**
 * 任意属性条件注解，用于条件化配置组件或方法
 * <p>
 * 该注解基于 Spring 的条件化配置机制，当指定的多个属性中任意一个存在时
 * 被标注的组件或方法就会生效，提供了比传统 @ConditionalOnProperty 更灵活的条件匹配机制
 * <p>
 * 主要特性：
 * - 支持多个属性的 OR 逻辑匹配
 * - 只检查属性是否存在，不关心具体值
 * - 适用于多种配置来源的兼容性场景
 * - 提供更灵活的组件启用控制
 * <p>
 * 使用示例：
 * {@code @ConditionalOnAnyProperty({"app.feature1.enabled", "app.feature2.enabled"})}
 * 当 feature1 或 feature2 中任意一个存在配置时，相关组件才会启用
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2025.07.23
 * @since 1.0.0
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Conditional(OnAnyPropertyCondition.class)
public @interface ConditionalOnAnyProperty {
    /**
     * 需要检查的属性名数组，任意一个存在即可满足条件
     * <p>
     * 该注解将检查数组中指定的所有属性名，只要任意一个属性在环境中存在
     * （不管其值为何），条件就会成立，被标注的组件将被加载
     * 这种设计适用于多种配置源的兼容性场景
     *
     * @return 属性名数组，支持多个属性名
     * @since 1.0.0
     */
    String[] value();
}
