package dev.dong4j.zeka.kernel.test;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.OverrideAutoConfiguration;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.autoconfigure.filter.TypeExcludeFilters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.annotation.AliasFor;
import org.springframework.test.context.BootstrapWith;

/**
 * Web MVC 测试注解, 用于测试 Controller 层
 * 1. 提供 MVC 环境支持
 * 2. 自动配置 MockMvc
 * 3. 不会启动真实的 Web 容器
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2025.06.28
 * @since 1.0.0
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@BootstrapWith(ZekaStackWebMvcTestContextBootstrapper.class)
@ExtendWith({StarterExtension.class})
@OverrideAutoConfiguration(
    enabled = false
)
@TypeExcludeFilters({WebMvcTypeExcludeFilter.class})
@AutoConfigureCache
@AutoConfigureWebMvc
@AutoConfigureMockMvc
@ImportAutoConfiguration
public @interface ZekaStackWebMvcTest {
    /** 配置属性 */
    String[] properties() default {};

    /** 指定要测试的 controller */
    @AliasFor("controllers")
    Class<?>[] value() default {};

    /** 指定要测试的 controller */
    @AliasFor("value")
    Class<?>[] controllers() default {};

    /** 是否使用默认过滤器 */
    boolean useDefaultFilters() default true;

    /** 包含的过滤器 */
    ComponentScan.Filter[] includeFilters() default {};

    /** 排除的过滤器 */
    ComponentScan.Filter[] excludeFilters() default {};

    /** 排除自动配置类 */
    @AliasFor(
        annotation = ImportAutoConfiguration.class,
        attribute = "exclude"
    )
    Class<?>[] excludeAutoConfiguration() default {};
}
