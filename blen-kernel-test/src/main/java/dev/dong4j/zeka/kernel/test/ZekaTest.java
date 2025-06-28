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
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.3.0
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
     * Value string [ ]
     *
     * @return the string [ ]
     * @since 1.0.0
     */
    @AliasFor("properties")
    String[] value() default {};

    /**
     * Properties string [ ]
     *
     * @return the string [ ]
     * @since 1.0.0
     */
    @AliasFor("value")
    String[] properties() default {};

    /**
     * Args string [ ]
     *
     * @return the string [ ]
     * @since 1.0.0
     */
    String[] args() default {};

    /**
     * 指定 Configuration 类, 如果有被 @SpringBootApplication 标识的类且测试主类在相同的 package 下可不指定,否则将抛出如下异常
     * 未找到使用 @SpringBootApplication 的启动类, 如果启动类使用 @EnableAutoConfiguration 标识,
     * 请使用 classes 属性指定此类, 请见 dev.dong4j.zeka.kernel.test.ZekaTest.classes 说明
     * 1. 如果启动类使用 @SpringBootApplication 注解, 可不配置 classes 属性;
     * 2. 如果测试主类和应用主类不在相同的 package 下, 则必须指定 classes;
     * 3. 如果未使用 @SpringBootApplication 注解, 则必须指定 classes, 可以是任何被 @Configuration 标识的类;
     *
     * @return the class [ ]
     * @since 1.0.0
     */
    Class<?>[] classes() default {};

    /**
     * 服务名: appName
     *
     * @return appName string
     * @since 1.0.0
     */
    String appName() default "";

    /**
     * profile
     *
     * @return profile string
     * @since 1.0.0
     */
    @AliasFor(value = "profiles", annotation = ActiveProfiles.class)
    String[] profiles() default {};

    /**
     * 启用 ServiceLoader 加载 launcherService
     *
     * @return 是否启用 boolean
     * @since 1.0.0
     */
    boolean enableLoader() default true;

    /**
     * 默认随机端口
     *
     * @return the spring boot test . web environment
     * @since 1.0.0
     */
    SpringBootTest.WebEnvironment webEnvironment() default SpringBootTest.WebEnvironment.MOCK;

}
