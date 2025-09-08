package dev.dong4j.zeka.kernel.spi.extension;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 禁用依赖注入注解，用于标记不需要进行依赖注入的类或方法
 * <p>
 * 该注解可以应用在类或方法上，用于告知SPI框架跳过依赖注入处理
 * 通常用于一些特殊情况，如需要手动控制依赖或避免循环依赖的场景
 * <p>
 * 主要特性：
 * - 支持类级别和方法级别的禁用
 * - 运行时保留，支持反射检查
 * - 可以精确控制具体的注入行为
 * <p>
 * 使用场景：
 * - 避免循环依赖问题
 * - 手动控制依赖注入的时机
 * - 特殊的依赖初始化逻辑
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
public @interface DisableInject {
}
