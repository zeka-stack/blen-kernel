package dev.dong4j.zeka.kernel.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.core.annotation.AliasFor;

/**
 * <p>Description: 项目标识 </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.06.10 16:16
 * @since 1.0.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ModelSerial {
    /** DEFAULT */
    String DEFAULT = "F";

    /**
     * Model name
     *
     * @return the module name
     * @since 1.0.0
     */
    @AliasFor("value")
    String modelName() default DEFAULT;

    /**
     * Value
     *
     * @return the string
     * @since 1.0.0
     */
    @AliasFor("modelName")
    String value() default DEFAULT;
}
