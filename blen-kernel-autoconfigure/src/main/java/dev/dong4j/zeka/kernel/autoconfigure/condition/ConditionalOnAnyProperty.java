package dev.dong4j.zeka.kernel.autoconfigure.condition;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.Conditional;

/**
 * 任意属性条件注解，用于条件化配置组件或方法
 * 当指定的多个属性中任意一个为true时，被标注的组件或方法就会生效
 * 提供了比传统@ConditionalOnProperty更灵活的条件匹配机制
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
     * 需要检查的属性名数组，任意一个为true即可满足条件
     *
     * @return 属性名数组
     * @since 1.0.0
     */
    String[] value();
}
