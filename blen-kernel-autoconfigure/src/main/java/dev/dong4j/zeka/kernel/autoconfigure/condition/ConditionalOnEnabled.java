package dev.dong4j.zeka.kernel.autoconfigure.condition;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.Conditional;

/**
 * 模块启用条件注解，用于控制指定模块的启用状态
 * 只有在指定的模块enabled属性为true时，被标注的组件或方法才会生效
 * 提供了统一的模块开关控制机制，简化模块管理和配置
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
     * 模块名称，用于指定需要检查enabled状态的模块
     *
     * @return 模块名
     * @since 1.0.0
     */
    String value();
}
