package dev.dong4j.zeka.kernel.test;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.AliasFor;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.BootstrapWith;

/**
 * Zeka框架统一测试注解，集成了Spring Boot测试和自定义测试配置
 * <p>
 * 该注解封装了Spring Boot测试的常用配置，同时集成了Zeka框架特有的测试支持
 * 提供了简化的测试环境搭建和统一的测试配置管理
 * <p>
 * 主要特性：
 * - 集成Spring Boot测试注解和配置
 * - 支持自定义的测试上下文引导程序
 * - 集成Zeka框架的启动扩展和环境配置
 * - 支持多种测试环境和配置模式
 * - 提供简化的应用名和配置文件管理
 * <p>
 * 使用方式：在测试类上添加此注解，替代原生的@SpringBootTest注解
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.03.18 19:37
 * @since 1.0.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@BootstrapWith(ZekaStackTestContextBootstrapper.class)
@ExtendWith(value = {StarterExtension.class})
@ActiveProfiles
public @interface ZekaTest {

    /**
     * 测试属性配置（与properties互为别名）
     * <p>
     * 用于设置测试环境中的属性配置，将被添加到Spring环境中
     * 支持key=value格式的属性设置
     *
     * @return 属性配置数组
     * @since 1.0.0
     */
    @AliasFor("properties")
    String[] value() default {};

    /**
     * 测试属性配置（与value互为别名）
     * <p>
     * 用于设置测试环境中的属性配置，将被添加到Spring环境中
     * 支持key=value格式的属性设置
     *
     * @return 属性配置数组
     * @since 1.0.0
     */
    @AliasFor("value")
    String[] properties() default {};

    /**
     * 命令行参数配置
     * <p>
     * 用于设置测试环境中的命令行参数，模拟应用启动时的参数
     * 可以用于测试不同的启动参数配置
     *
     * @return 命令行参数数组
     * @since 1.0.0
     */
    String[] args() default {};

    /**
     * 指定测试上下文的配置类
     * <p>
     * 用于指定测试环境中使用的配置类，通常是主应用类或配置类
     * <p>
     * 使用指导：
     * 1. 如果启动类使用@SpringBootApplication注解，可不配置classes属性
     * 2. 如果测试主类和应用主类不在相同的package下，则必须指定classes
     * 3. 如果未使用@SpringBootApplication注解，则必须指定classes
     * 4. 可以是任何被@Configuration或@EnableAutoConfiguration标识的类
     *
     * @return 配置类数组
     * @since 1.0.0
     */
    Class<?>[] classes() default {};

    /**
     * 应用名称配置
     * <p>
     * 用于指定测试环境中的应用名称，影响配置文件加载和服务发现
     * 通常对应application.yml中的spring.application.name配置
     *
     * @return 应用名称
     * @since 1.0.0
     */
    String appName() default "";

    /**
     * 测试环境配置文件
     * <p>
     * 指定测试中使用的Spring Profile，用于加载不同的配置文件
     * 对应@ActiveProfiles注解的profiles属性
     *
     * @return 测试环境配置数组
     * @since 1.0.0
     */
    @AliasFor(value = "profiles", annotation = ActiveProfiles.class)
    String[] profiles() default {};

    /**
     * 是否启用ServiceLoader加载机制
     * <p>
     * 控制是否使用Java SPI机制加载Launcher服务
     * 默认为true，启用Zeka框架的自动加载机制
     *
     * @return 是否启用加载机制
     * @since 1.0.0
     */
    boolean enableLoader() default true;

    /**
     * Web测试环境配置
     * <p>
     * 指定测试中使用的Web环境类型，默认为MOCK模式
     * MOCK: 不启动真实的Web服务器，使用Mock环境
     * RANDOM_PORT: 随机端口启动Web服务器
     * DEFINED_PORT: 使用定义的端口启动Web服务器
     * NONE: 不加载Web环境
     *
     * @return Web环境类型
     * @since 1.0.0
     */
    SpringBootTest.WebEnvironment webEnvironment() default SpringBootTest.WebEnvironment.MOCK;

}
