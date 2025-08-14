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
 * <p>Description: 手机号码验证, 可为 null 和 "", 一旦赋值必须满足正则</p>
 *
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dong4j@gmail.com"
 * @date 2019.12.25 01:22
 * @since 1.0.0
 */
@Target(value = {ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = Phone.Validator.class)
public @interface Phone {
    /** PHONE */
    String PHONE = RegexUtils.PHONE;

    /**
     * Message string.
     *
     * @return the string
     * @since 1.0.0
     */
    String message() default "{validation.constraints.Phone.message}";

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
     * 简单验证是否为电话号码, 没有区分运营商
     *
     * @return an additional regular expression the annotated element must match. The default is any string ('.*')
     * @since 1.0.0
     */
    String regexp() default PHONE;

    /**
     * <p>Description: 验证逻辑实现</p>
     *
     * @author dong4j
     * @version 1.2.3
     * @email "mailto:dong4j@gmail.com"
     * @date 2019.12.25 01:22
     * @since 1.0.0
     */
    class Validator implements ConstraintValidator<Phone, String> {
        /** Regexp */
        private String regexp;

        /**
         * Initialize *
         *
         * @param constraintAnnotation constraint annotation
         * @since 1.0.0
         */
        @Override
        public void initialize(@NotNull Phone constraintAnnotation) {
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
            // 不处理为空的情况, 由 NotBlank 处理 (电话号码可不填, 填了就必须符合正则)
            if (!StringUtils.hasText(value)) {
                return true;
            }
            return value.matches(regexp);
        }
    }
}
