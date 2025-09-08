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
 * IP地址验证注解，用于验证字符串是否为有效的IP地址格式
 *
 * 该注解基于JSR-303规范实现，使用正则表达式验证IP地址的格式
 * 支持IPv4地址的验证，可自定义正则表达式模式
 *
 * 主要特性：
 * - 默认支持IPv4地址验证
 * - 允许空值和空字符串（应配合@NotBlank使用）
 * - 可自定义正则表达式模式
 * - 基于RegexUtils工具类的高性能匹配
 *
 * 使用示例：
 * {@code @IPAddress private String serverIp;}
 * {@code @IPAddress(regexp = "自定义正则") private String customIp;}
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2019.12.27 17:55
 * @since 1.0.0
 */
@Target(value = {ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IPAddress.Validator.class)
public @interface IPAddress {
    /** IP地址正则表达式常量 */
    String IP = RegexUtils.IP;

    /**
     * 验证失败时的错误消息
     *
     * @return 错误消息模板，支持国际化
     * @since 1.0.0
     */
    String message() default "{validation.constraints.IPAddress.message}";

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
     * IP地址验证的正则表达式
     *
     * 默认使用IP常量定义的IPv4地址正则
     * 可以自定义正则表达式来适应不同的IP地址格式需求
     *
     * @return IP地址正则表达式，默认为任意字符串('.*')
     * @since 1.0.0
     */
    String regexp() default IP;

    /**
     * IP地址验证器实现类
     *
     * 实现ConstraintValidator接口，提供IP地址验证的具体逻辑
     * 使用RegexUtils工具类进行正则表达式匹配，支持自定义正则模式
     *
     * @author dong4j
     * @version 1.0.0
     * @email "mailto:dong4j@gmail.com"
     * @date 2019.12.27 17:55
     * @since 1.0.0
     */
    class Validator implements ConstraintValidator<IPAddress, String> {
        /** IP地址正则表达式 */
        private String regexp;

        /**
         * 初始化验证器，从注解中获取正则表达式
         *
         * @param constraintAnnotation IP地址验证注解实例
         * @since 1.0.0
         */
        @Override
        public void initialize(@NotNull IPAddress constraintAnnotation) {
            regexp = constraintAnnotation.regexp();
        }

        /**
         * 执行IP地址验证
         *
         * 对于空值和空字符串返回true（由@NotBlank处理非空验证）
         * 使用RegexUtils工具类进行正则匹配，验证IP地址格式
         *
         * @param value   待验证的IP地址字符串
         * @param context 验证上下文
         * @return true-验证通过，false-验证失败
         * @since 1.0.0
         */
        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            // 如果单独使用此注解，只判断是否满足正则，不判断是否有值（由@NotBlank处理）
            if (!StringUtils.hasText(value)) {
                return true;
            }
            return RegexUtils.match(regexp, value);
        }

    }
}
