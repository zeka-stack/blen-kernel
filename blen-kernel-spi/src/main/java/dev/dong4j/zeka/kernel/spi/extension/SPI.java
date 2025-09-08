package dev.dong4j.zeka.kernel.spi.extension;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * SPI扩展点注解，用于标记一个接口为SPI扩展点
 * <p>
 * 该注解是SPI框架的核心注解，用于标识哪些接口是可扩展的
 * 被标记的接口可以有多个实现，并通过SPI机制进行动态加载
 * <p>
 * 主要特性：
 * - 只能应用在接口类型上
 * - 支持设置默认实现名称
 * - 运行时保留，支持反射检查
 * - 与SPILoader配合使用实现扩展加载
 * <p>
 * 使用示例：
 * {@code @SPI("default")} // 指定默认实现
 * {@code @SPI} // 不指定默认实现
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2021.02.26 17:47
 * @since 1.0.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.TYPE})
public @interface SPI {

    /**
     * 指定默认的扩展实现名称
     * <p>
     * 当没有显式指定扩展名称时，将使用此默认值
     * 默认为空字符串，表示没有默认实现
     *
     * @return 默认扩展实现的名称
     * @since 1.0.0
     */
    String value() default "";

}
