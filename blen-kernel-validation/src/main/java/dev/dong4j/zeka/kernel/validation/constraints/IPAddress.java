package dev.dong4j.zeka.kernel.validation.constraints;

import dev.dong4j.zeka.kernel.validation.util.RegexUtils;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.jetbrains.annotations.NotNull;
import org.springframework.util.StringUtils;

/**
 * <p>Description: IP 验证</p>
 *
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dong4j@gmail.com"
 * @date 2019.12.27 17:55
 * @since 1.0.0
 */
@Target(value = {ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IPAddress.Validator.class)
public @interface IPAddress {
    /** IP */
    String IP = RegexUtils.IP;

    /**
     * Message string.
     *
     * @return the string
     * @since 1.0.0
     */
    String message() default "{validation.constraints.IPAddress.message}";

    /**
     * Groups class [ ].
     *
     * @return the class [ ]
     * @since 1.0.0
     */
    Class<?>[] groups() default {};

    /**
     * Payload class [ ].
     *
     * @return the class [ ]
     * @since 1.0.0
     */
    Class<? extends Payload>[] payload() default {};

    /**
     * Regexp string.
     *
     * @return an additional regular expression the annotated element must match. The default is any string ('.*')
     * @since 1.0.0
     */
    String regexp() default IP;

    /**
     * <p>Description: 验证逻辑实现</p>
     *
     * @author dong4j
     * @version 1.2.3
     * @email "mailto:dong4j@gmail.com"
     * @date 2019.12.27 17:55
     * @since 1.0.0
     */
    class Validator implements ConstraintValidator<IPAddress, String> {
        /** Regexp */
        private String regexp;

        /**
         * Initialize *
         *
         * @param constraintAnnotation constraint annotation
         * @since 1.0.0
         */
        @Override
        public void initialize(@NotNull IPAddress constraintAnnotation) {
            regexp = constraintAnnotation.regexp();
        }

        /**
         * Is valid boolean
         *
         * @param value   value
         * @param context context
         * @return the boolean
         * @since 1.0.0
         */
        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            // 如果单独使用此注解, 只判断是否满足正则, 不判断是否有值 (由 NotBlank 处理)
            if (!StringUtils.hasText(value)) {
                return true;
            }
            return RegexUtils.match(regexp, value);
        }

    }
}
