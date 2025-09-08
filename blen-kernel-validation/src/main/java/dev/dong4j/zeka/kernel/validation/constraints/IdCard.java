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
 * 身份证号验证注解，用于验证字符串是否为有效的身份证号码格式
 *
 * 该注解基于JSR-303规范实现，使用正则表达式验证中国大陆地区身份证号码
 * 支持15位和18位身份证号码的验证，可为null和空字符串
 *
 * 主要特性：
 * - 支持15位和18位身份证号码验证
 * - 允许空值和空字符串（应配合@NotBlank使用）
 * - 可自定义正则表达式模式
 * - 基于RegexUtils工具类的高性能匹配
 *
 * 使用示例：
 * {@code @IdCard private String idNumber;}
 * {@code @IdCard(regexp = "自定义正则") private String customId;}
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2019.12.25 01:22
 * @since 1.0.0
 */
@Target(value = {ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IdCard.Validator.class)
public @interface IdCard {
    /** 身份证号码正则表达式常量 */
    String ID_CARD = RegexUtils.ID_CARD;

    /**
     * 验证失败时的错误消息
     *
     * @return 错误消息模板，支持国际化
     * @since 1.0.0
     */
    String message() default "{validation.constraints.IdCard.message}";

    /**
     * 验证分组，用于分组验证场景
     *
     * @return 验证分组类数组
     * @since 1.0.0
     */
    Class<?>[] groups() default {

    };

    /**
     * 负载信息，用于传递验证相关的元数据
     *
     * @return 负载类数组
     * @since 1.0.0
     */
    Class<? extends Payload>[] payload() default {

    };

    /**
     * 身份证号码验证的正则表达式
     *
     * 默认使用ID_CARD常量定义的身份证号码正则
     * 支持15位和18位身份证号码的验证
     * 可以自定义正则表达式来适应不同的验证需求
     *
     * @return 身份证号码正则表达式，默认为任意字符串('.*')
     * @since 1.0.0
     */
    String regexp() default ID_CARD;

    /**
     * 身份证号码验证器实现类
     *
     * 实现ConstraintValidator接口，提供身份证号码验证的具体逻辑
     * 使用RegexUtils工具类进行正则表达式匹配，支持自定义正则模式
     *
     * @author dong4j
     * @version 1.0.0
     * @email "mailto:dong4j@gmail.com"
     * @date 2019.12.25 01:22
     * @since 1.0.0
     */
    class Validator implements ConstraintValidator<IdCard, String> {
        /** 身份证号码正则表达式 */
        private String regexp;

        /**
         * 初始化验证器，从注解中获取正则表达式
         *
         * @param constraintAnnotation 身份证验证注解实例
         * @since 1.0.0
         */
        @Override
        public void initialize(@NotNull IdCard constraintAnnotation) {
            this.regexp = constraintAnnotation.regexp();
        }

        /**
         * 执行身份证号码验证
         *
         * 对于空值和空字符串返回true（由@NotBlank处理非空验证）
         * 使用RegexUtils工具类进行正则匹配，验证身份证号码格式
         *
         * @param value   待验证的身份证号码字符串
         * @param context 验证上下文
         * @return true-验证通过，false-验证失败
         * @since 1.0.0
         */
        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            // 不处理为空的情况，由@NotBlank处理（可不填，填了就必须符合正则）
            if (!StringUtils.hasText(value)) {
                return true;
            }
            return RegexUtils.match(this.regexp, value);
        }
    }
}
