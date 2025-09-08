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
 * 车牌号验证注解，用于验证字符串是否为有效的车牌号码格式
 *
 * 该注解基于JSR-303规范实现，使用正则表达式验证中国车牌号码格式
 * 支持普通车牌和新能源车牌的验证，可为null和空字符串
 *
 * 主要特性：
 * - 支持普通车牌和新能源车牌验证
 * - 支持带横线和不带横线的格式（如：川A-06D4J或川A06D4J）
 * - 允许空值和空字符串（应配合@NotBlank使用）
 * - 可自定义正则表达式模式
 * - 基于RegexUtils工具类的高性能匹配
 *
 * 使用示例：
 * {@code @VehicleNumber private String plateNumber;}
 * {@code @VehicleNumber(regexp = "自定义正则") private String customPlate;}
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2019.12.25 01:22
 * @since 1.0.0
 */
@Target(value = {ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = VehicleNumber.Validator.class)
public @interface VehicleNumber {
    /** 车牌号码正则表达式常量 */
    String VEHICLE_NUMBER = RegexUtils.VEHICLE_NUMBER;

    /**
     * 验证失败时的错误消息
     *
     * @return 错误消息模板，支持国际化
     * @since 1.0.0
     */
    String message() default "{validation.constraints.VehicleNumber.message}";

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
     * 车牌号码验证的正则表达式
     *
     * 支持普通车牌和新能源车牌的验证
     * 如：川A-06D4J或者川A06D4J都支持
     * 默认使用VEHICLE_NUMBER常量定义的车牌号正则
     *
     * @return 车牌号码正则表达式，默认为任意字符串('.*')
     * @since 1.0.0
     */
    String regexp() default VEHICLE_NUMBER;

    /**
     * 车牌号码验证器实现类
     *
     * 实现ConstraintValidator接口，提供车牌号码验证的具体逻辑
     * 使用RegexUtils工具类进行正则表达式匹配，支持自定义正则模式
     *
     * @author dong4j
     * @version 1.0.0
     * @email "mailto:dong4j@gmail.com"
     * @date 2019.12.25 01:22
     * @since 1.0.0
     */
    class Validator implements ConstraintValidator<VehicleNumber, String> {
        /** 车牌号码正则表达式 */
        private String regexp;

        /**
         * 初始化验证器，从注解中获取正则表达式
         *
         * @param constraintAnnotation 车牌号验证注解实例
         * @since 1.0.0
         */
        @Override
        public void initialize(@NotNull VehicleNumber constraintAnnotation) {
            regexp = constraintAnnotation.regexp();
        }

        /**
         * 执行车牌号码验证
         *
         * 对于空值和空字符串返回true（由@NotBlank处理非空验证）
         * 使用RegexUtils工具类进行正则匹配，验证车牌号码格式
         *
         * @param value   待验证的车牌号码字符串
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
            return RegexUtils.match(regexp, value);
        }
    }
}
