package org.springframework.cloud.context.config.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

/**
 * 1: 自定义注解，用于标记可动态刷新的配置类，支持与 SpringCloud 的 @RefreshScope 兼容。
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2025.07.29
 * @since 1.0.0
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface RefreshScope {
    @AliasFor(annotation = Component.class)
    String value() default "";
}
