package dev.dong4j.zeka.kernel.spi.extension;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.8.0
 * @email "mailto:dong4j@gmaidl.com"
 * @date 2021.02.26 17:47
 * @since 1.8.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.TYPE, ElementType.METHOD})
@SuppressWarnings("all")
public @interface Activate {
    /**
     * Group
     *
     * @return the string [ ]
     * @since 1.8.0
     */
    String[] group() default {};

    /**
     * Value
     *
     * @return the string [ ]
     * @since 1.8.0
     */
    String[] value() default {};

    /**
     * Before
     *
     * @return the string [ ]
     * @since 1.8.0
     */
    String[] before() default {};

    /**
     * After
     *
     * @return the string [ ]
     * @since 1.8.0
     */
    String[] after() default {};

    /**
     * Order
     *
     * @return the int
     * @since 1.8.0
     */
    int order() default 0;
}
