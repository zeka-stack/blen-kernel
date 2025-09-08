package dev.dong4j.zeka.kernel.spi.extension;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * SPI扩展激活条件注解，用于控制SPI扩展的动态加载和激活
 * <p>
 * 该注解允许开发者为SPI扩展指定激活条件，包括分组、参数值、执行顺序等
 * 支持根据运行时环境和配置参数动态决定是否加载指定的扩展实现
 * <p>
 * 主要功能：
 * - 支持分组条件激活
 * - 支持参数值条件激活
 * - 支持执行顺序控制
 * - 支持前置和后置依赖关系
 * <p>
 * 使用示例：
 * {@code @Activate(group = "consumer", value = "cache", order = 100)}
 * {@code @Activate(before = "logging", after = "monitoring")}
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
@SuppressWarnings("all")
public @interface Activate {
    /**
     * 激活分组条件
     *
     * 指定在哪些分组中激活该扩展，如consumer、provider等
     * 支持多个分组，满足任意一个分组条件即可激活
     *
     * @return 分组名称数组
     * @since 1.0.0
     */
    String[] group() default {};

    /**
     * 激活参数条件
     *
     * 指定需要存在的参数名称，只有当这些参数存在时才会激活扩展
     * 支持多个参数，满足任意一个参数条件即可激活
     *
     * @return 参数名称数组
     * @since 1.0.0
     */
    String[] value() default {};

    /**
     * 前置依赖扩展
     *
     * 指定在哪些扩展之前执行，用于控制扩展的执行顺序
     * 支持多个扩展名称，确保在指定的扩展之前执行
     *
     * @return 扩展名称数组
     * @since 1.0.0
     */
    String[] before() default {};

    /**
     * 后置依赖扩展
     *
     * 指定在哪些扩展之后执行，用于控制扩展的执行顺序
     * 支持多个扩展名称，确保在指定的扩展之后执行
     *
     * @return 扩展名称数组
     * @since 1.0.0
     */
    String[] after() default {};

    /**
     * 执行顺序
     *
     * 指定扩展的执行优先级，数值越小优先级越高
     * 默认为0，支持负数和正数
     *
     * @return 执行顺序值
     * @since 1.0.0
     */
    int order() default 0;
}
