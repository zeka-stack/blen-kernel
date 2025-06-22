package dev.dong4j.zeka.kernel.validation.constraints;

import dev.dong4j.zeka.kernel.common.constant.ConfigDefaultValue;
import org.jetbrains.annotations.NotNull;
import org.springframework.util.StringUtils;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * <p>Description: 字符串类型的时间格式验证 </p>
 *
 * @author dong4j
 * @version 1.3.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.03.04 11:40
 * @since 1.0.0
 */
@Target(value = {FIELD, METHOD, PARAMETER, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = Date.Validator.class)
@Documented
public @interface Date {
    /**
     * Message string
     *
     * @return the string
     * @since 1.0.0
     */
    String message() default "{javax.validation.constraints.Date.message}";

    /**
     * Pattern string
     *
     * @return the string
     * @since 1.0.0
     */
    String pattern() default ConfigDefaultValue.DEFAULT_DATE_FORMAT;

    /**
     * Groups class [ ]
     *
     * @return the class [ ]
     * @since 1.0.0
     */
    Class<?>[] groups() default {};

    /**
     * Payload class [ ]
     *
     * @return the class [ ]
     * @since 1.0.0
     */
    Class<? extends Payload>[] payload() default {};

    /**
     * <p>Description: </p>
     *
     * @author dong4j
     * @version 1.3.0
     * @email "mailto:dong4j@gmail.com"
     * @date 2020.03.04 11:41
     * @since 1.0.0
     */
    class Validator implements ConstraintValidator<Date, String> {

        /** Pattern */
        private String pattern;

        /**
         * Initialize *
         *
         * @param constraintAnnotation constraint annotation
         * @since 1.0.0
         */
        @Override
        public void initialize(@NotNull Date constraintAnnotation) {
            this.pattern = constraintAnnotation.pattern();
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
            // 不处理为空的情况, 由 NotBlank 处理 (可不填, 填了就必须符合正则)
            if (!StringUtils.hasText(value)) {
                return true;
            }

            try {
                DateTimeFormatter.ofPattern(this.pattern).parse(value);
                return true;
            } catch (DateTimeParseException e) {
                return false;
            }
        }

    }
}
