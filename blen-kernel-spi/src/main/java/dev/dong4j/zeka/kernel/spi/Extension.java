package dev.dong4j.zeka.kernel.spi;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * SPI扩展标记注解（已废弃）
 * <p>
 * 该注解用于标记SPI扩展实现类，提供扩展点的标识功能
 * 注解已被标记为@Deprecated，建议使用新的SPI机制替代
 * <p>
 * 主要功能：
 * - 标识SPI扩展实现类
 * - 提供扩展名称配置
 * - 支持运行时反射获取扩展信息
 * <p>
 * 注意：此注解已废弃，仅为兼容性保留
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2021.02.26 17:47
 * @since 1.0.0
 */
@Deprecated
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.TYPE})
public @interface Extension {

    /**
     * 扩展名称值（已废弃）
     *
     * 用于指定SPI扩展的名称标识
     * 该属性已废弃，建议使用新的扩展配置方式
     *
     * @return 扩展名称字符串
     * @since 1.0.0
     */
    @Deprecated
    String value() default "";

}
