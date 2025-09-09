package dev.dong4j.zeka.kernel.autoconfigure.condition;

import dev.dong4j.zeka.kernel.autoconfigure.ZekaProperties;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.Conditional;

/**
 * 模块启用条件注解，用于控制指定模块的启用状态
 * <p>
 * 该注解基于 Spring 的条件化配置机制，只有在指定的模块 enabled 属性为 true 时
 * 被标注的组件或方法才会生效，提供了统一的模块开关控制机制
 * <p>
 * 主要特性：
 * - 简化模块的开关管理和配置
 * - 支持组件级别的精细化控制
 * - 与 {@link ZekaProperties} 配合使用，实现统一的配置模式
 * - 支持运行时动态切换组件状态
 * <p>
 * 使用示例：
 * {@code @ConditionalOnEnabled("blen.kernel.auth")}
 * 当配置中 blen.kernel.auth.enabled=true 时，相关组件才会启用
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2025.07.23
 * @since 1.0.0
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Conditional(OnModuleEnableCondition.class)
public @interface ConditionalOnEnabled {

    /**
     * 模块名称，用于指定需要检查 enabled 状态的模块
     * <p>
     * 该值将与 ".enabled" 后缀组合成完整的配置键名
     * 例如：当 value="blen.kernel.auth" 时，实际检查的配置键为 "blen.kernel.auth.enabled"
     *
     * @return 模块名称前缀
     * @since 1.0.0
     */
    String value();
}
