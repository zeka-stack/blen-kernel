package dev.dong4j.zeka.kernel.validation.constraints;

import dev.dong4j.zeka.kernel.validation.util.RegexUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.util.StringUtils;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>Description: 车牌号验证, 可为 null 和 "", 一旦赋值必须满足正则</p>
 *
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dong4j@gmail.com"
 * @date 2019.12.25 01:22
 * @since 1.0.0
 */
@Target(value = {ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IdCard.Validator.class)
public @interface IdCard {
    /** 身份证号 */
    String ID_CARD = RegexUtils.ID_CARD;

    /**
     * Message string.
     *
     * @return the string
     * @since 1.0.0
     */
    String message() default "{javax.validation.constraints.IdCard.message}";

    /**
     * Groups class [ ].
     *
     * @return the class [ ]
     * @since 1.0.0
     */
    Class<?>[] groups() default {

    };

    /**
     * Payload class [ ].
     *
     * @return the class [ ]
     * @since 1.0.0
     */
    Class<? extends Payload>[] payload() default {

    };

    /**
     * 验证车牌号, 包括新能源
     * 川A-06D4J 或者 川A06D4J 都行
     *
     * @return an additional regular expression the annotated element must match. The default is any string ('.*')
     * @since 1.0.0
     */
    String regexp() default ID_CARD;

    /**
     * <p>Description: 验证逻辑实现</p>
     *
     * @author dong4j
     * @version 1.2.3
     * @email "mailto:dong4j@gmail.com"
     * @date 2019.12.25 01:22
     * @since 1.0.0
     */
    class Validator implements ConstraintValidator<IdCard, String> {
        /** Regexp */
        private String regexp;

        /**
         * Initialize *
         *
         * @param constraintAnnotation constraint annotation
         * @since 1.0.0
         */
        @Override
        public void initialize(@NotNull IdCard constraintAnnotation) {
            this.regexp = constraintAnnotation.regexp();
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
            return RegexUtils.match(this.regexp, value);
        }
    }
}
