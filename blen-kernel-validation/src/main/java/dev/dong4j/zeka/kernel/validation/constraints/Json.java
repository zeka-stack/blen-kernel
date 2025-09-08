package dev.dong4j.zeka.kernel.validation.constraints;

import dev.dong4j.zeka.kernel.common.util.Jsons;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import org.springframework.util.StringUtils;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * JSON字符串验证注解，用于验证字符串是否为有效的JSON格式
 *
 * 该注解基于JSR-303规范实现，使用Jsons工具类验证JSON字符串的有效性
 * 可以验证JSON对象、JSON数组等各种类型的JSON结构
 *
 * 主要特性：
 * - 支持完整的JSON格式验证
 * - 允许空值和空字符串（应配合@NotBlank使用）
 * - 基于Jackson库的高性能解析
 * - 支持Bean Validation分组验证
 *
 * 使用示例：
 * {@code @Json private String jsonData;}
 * {@code @Json @NotBlank private String requiredJson;}
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.03.04 11:27
 * @since 1.0.0
 */
@Target({FIELD, METHOD, PARAMETER, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = Json.Validator.class)
@Documented
public @interface Json {
    /**
     * 验证失败时的错误消息
     *
     * @return 错误消息模板，支持国际化
     * @since 1.0.0
     */
    String message() default "{validation.constraints.Json.message}";

    /**
     * 验证分组，用于分组验证场景
     *
     * @return 验证分组类数组
     * @since 1.0.0
     */
    // 验证分组
    Class<?>[] groups() default {};

    /**
     * 负载信息，用于传递验证相关的元数据
     *
     * @return 负载类数组
     * @since 1.0.0
     */
    Class<? extends Payload>[] payload() default {};

    /**
     * JSON字符串验证器实现类
     *
     * 实现ConstraintValidator接口，提供JSON字符串验证的具体逻辑
     * 使用Jsons工具类进行JSON格式验证，支持完整的JSON结构解析
     *
     * @author dong4j
     * @version 1.0.0
     * @email "mailto:dong4j@gmail.com"
     * @date 2020.03.04 11:27
     * @since 1.0.0
     */
    class Validator implements ConstraintValidator<Json, String> {

        /**
         * 初始化验证器（空实现）
         *
         * @param constraintAnnotation JSON验证注解实例
         * @since 1.0.0
         */
        @Override
        public void initialize(Json constraintAnnotation) {
        }

        /**
         * 执行JSON字符串验证
         *
         * 对于空值和空字符串返回true（由@NotBlank处理非空验证）
         * 使用Jsons.isJson()方法验证JSON字符串的格式正确性
         *
         * @param value   待验证的JSON字符串
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
            return Jsons.isJson(value);
        }
    }

}
