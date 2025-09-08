package dev.dong4j.zeka.kernel.spi.extension;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * SPI自适应扩展注解，用于标记需要动态生成代理类的SPI扩展点
 * <p>
 * 该注解使得SPI框架能够根据URL参数动态选择具体的扩展实现
 * 支持运行时根据不同的参数值加载不同的扩展实现，实现真正的动态扩展
 * <p>
 * 主要功能：
 * - 支持动态代理类生成
 * - 支持多个参数的自适应选择
 * - 支持方法级别的自适应控制
 * - 提供高性能的运行时扩展选择
 * <p>
 * 使用示例：
 * {@code @Adaptive({"protocol", "version"})}
 * {@code @Adaptive} // 使用默认参数
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2021.02.26 17:47
 * @since 1.0.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.TYPE, ElementType.METHOD})
public @interface Adaptive {
    /**
     * 自适应参数名称数组
     *
     * 指定用于自适应选择的参数名称
     * 框架将根据这些参数的值动态决定加载哪个扩展实现
     * 如果不指定，则使用接口名作为默认参数
     *
     * @return 参数名称数组
     * @since 1.0.0
     */
    String[] value() default {};

}
