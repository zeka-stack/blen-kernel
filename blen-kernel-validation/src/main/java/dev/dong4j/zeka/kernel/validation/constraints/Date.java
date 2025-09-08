package dev.dong4j.zeka.kernel.validation.constraints;

import dev.dong4j.zeka.kernel.common.constant.ConfigDefaultValue;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import org.jetbrains.annotations.NotNull;
import org.springframework.util.StringUtils;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 日期格式验证注解，用于验证字符串类型的日期格式是否符合指定的模式
 *
 * 该注解基于JSR-303规范实现，支持自定义日期格式模式验证
 * 使用Java 8的DateTimeFormatter进行日期格式解析，确保日期字符串的有效性
 *
 * 主要特性：
 * - 支持自定义日期格式模式（默认为yyyy-MM-dd HH:mm:ss）
 * - 允许空值和空字符串（应配合@NotBlank使用）
 * - 基于DateTimeFormatter的严格解析
 * - 支持Bean Validation分组验证
 *
 * 使用示例：
 * {@code @Date(pattern = "yyyy-MM-dd") private String birthday;}
 * {@code @Date private String createTime; // 使用默认格式}
 *
 * @author dong4j
 * @version 1.0.0
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
     * 验证失败时的错误消息
     *
     * @return 错误消息模板，支持国际化
     * @since 1.0.0
     */
    String message() default "{validation.constraints.Date.message}";

    /**
     * 日期格式模式字符串
     *
     * 使用Java DateTimeFormatter支持的格式模式
     * 默认格式为yyyy-MM-dd HH:mm:ss
     *
     * @return 日期格式模式
     * @since 1.0.0
     */
    String pattern() default ConfigDefaultValue.DEFAULT_DATE_FORMAT;

    /**
     * 验证分组，用于分组验证场景
     *
     * @return 验证分组类数组
     * @since 1.0.0
     */
    Class<?>[] groups() default {};

    /**
     * 负载信息，用于传递验证相关的元数据
     *
     * @return 负载类数组
     * @since 1.0.0
     */
    Class<? extends Payload>[] payload() default {};

    /**
     * 日期格式验证器实现类
     *
     * 实现ConstraintValidator接口，提供具体的日期格式验证逻辑
     * 使用DateTimeFormatter进行日期解析，支持自定义格式模式
     *
     * @author dong4j
     * @version 1.0.0
     * @email "mailto:dong4j@gmail.com"
     * @date 2020.03.04 11:41
     * @since 1.0.0
     */
    class Validator implements ConstraintValidator<Date, String> {

        /** 日期格式模式 */
        private String pattern;

        /**
         * 初始化验证器，从注解中获取日期格式模式
         *
         * @param constraintAnnotation 日期验证注解实例
         * @since 1.0.0
         */
        @Override
        public void initialize(@NotNull Date constraintAnnotation) {
            this.pattern = constraintAnnotation.pattern();
        }

        /**
         * 执行日期格式验证
         *
         * 对于空值和空字符串返回true（由@NotBlank处理非空验证）
         * 使用DateTimeFormatter解析日期字符串，解析成功则验证通过
         *
         * @param value   待验证的日期字符串
         * @param context 验证上下文
         * @return true-验证通过，false-验证失败
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
