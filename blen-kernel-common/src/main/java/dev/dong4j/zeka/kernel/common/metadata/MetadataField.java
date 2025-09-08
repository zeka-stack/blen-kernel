package dev.dong4j.zeka.kernel.common.metadata;

import dev.dong4j.zeka.kernel.common.enums.FieldFill;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.04.12 13:10
 * @since 1.0.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface MetadataField {

    /**
     * Value string
     *
     * @return the string
     * @since 1.0.0
     */
    String value() default "";

    /**
     * 字段自动填充策略
     *
     * @return the field fill
     * @since 1.0.0
     */
    FieldFill fill() default FieldFill.DEFAULT;
}
