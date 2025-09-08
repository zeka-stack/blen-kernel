package dev.dong4j.zeka.kernel.validation.constraints;

import dev.dong4j.zeka.kernel.common.enums.SerializeEnum;
import dev.dong4j.zeka.kernel.common.exception.ServiceInternalException;
import dev.dong4j.zeka.kernel.common.support.StrFormatter;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;

/**
 * 枚举值验证注解，用于验证字段值是否为指定枚举类的有效值
 *
 * 该注解基于JSR-303规范实现，支持通过反射机制动态获取枚举值
 * 支持通过指定方法名获取枚举的业务值，默认使用getValue()方法
 *
 * 主要特性：
 * - 支持枚举名称（name()）和业务值的双重匹配
 * - 可自定义获取枚举值的方法名
 * - 支持SerializeEnum接口的标准化验证
 * - 错误处理和日志记录
 *
 * 使用示例：
 * {@code @EnumX(StatusEnum.class) private String status;}
 * {@code @EnumX(value = ColorEnum.class, method = "getCode") private Integer colorCode;}
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2022.02.15 14:31
 * @since 1.0.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {FIELD, METHOD, PARAMETER, ANNOTATION_TYPE})
@Constraint(validatedBy = EnumX.Validator.class)
public @interface EnumX {
    /**
     * 要验证的枚举类型
     *
     * 指定一个枚举类，验证器将从该枚举中获取所有可用值
     *
     * @return 枚举类型
     * @since 1.0.0
     */
    Class<? extends Enum<?>> value();

    /**
     * 验证失败时的错误消息
     *
     * @return 错误消息模板，支持国际化
     * @since 1.0.0
     */
    String message() default "{validation.constraints.EnumX.message}";

    /**
     * 获取枚举值的方法名
     *
     * 默认使用SerializeEnum.VALUE_METHOD_NAME（即"getValue"）
     * 可以指定其他方法名来获取枚举的业务值
     *
     * @return 方法名
     * @since 1.0.0
     */
    String method() default SerializeEnum.VALUE_METHOD_NAME;

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
     * 枚举值验证器实现类
     *
     * 实现ConstraintValidator接口，提供枚举值验证的具体逻辑
     * 通过反射机制获取枚举的所有可用值，支持name()和自定义方法的双重匹配
     *
     * @author dong4j
     * @version 1.0.0
     * @email "mailto:dong4j@gmail.com"
     * @date 2022.02.15 14:52
     * @since 1.0.0
     */
    @Slf4j
    class Validator implements ConstraintValidator<EnumX, Object> {

        /** 保存枚举的所有业务值 */
        private final Set<String> values = new HashSet<>();
        /** 保存枚举的所有名称 */
        private final Set<String> enumsNames = new HashSet<>();

        /**
         * 初始化验证器，获取枚举的所有可用值
         *
         * 通过反射机制获取枚举的所有实例，并调用指定方法获取业务值
         * 同时保存枚举的name()值，支持双重匹配验证
         *
         * @param enumValidator 枚举验证注解实例
         * @since 1.0.0
         */
        @Override
        public void initialize(EnumX enumValidator) {
            Class<? extends Enum<?>> aClass = enumValidator.value();
            // 获取枚举类的所有实例
            Enum<?>[] enumConstants = aClass.getEnumConstants();

            try {
                // 通过反射获取指定的方法
                Method method = aClass.getMethod(enumValidator.method());
                for (Enum<?> item : enumConstants) {
                    // 调用方法获取业务值并转为字符串
                    values.add(String.valueOf(method.invoke(item)));
                    // 保存枚举的name()值
                    enumsNames.add(item.name());
                }
            } catch (NoSuchMethodException e) {
                throw new ServiceInternalException(StrFormatter.format("{} 不存在 method: {}",
                    aClass.getName(),
                    enumValidator.method()));
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                log.error(StrFormatter.format("获取枚举值失败, class: {}, method: {}",
                    aClass.getName(),
                    enumValidator.method()));
            }
        }

        /**
         * 执行枚举值验证
         *
         * 将待验证值转为字符串，并在values和enumNames集合中查找匹配
         * 支持枚举名称和业务值的双重匹配
         *
         * @param value                      待验证的值
         * @param constraintValidatorContext 验证上下文
         * @return true-验证通过，false-验证失败
         * @since 1.0.0
         */
        @Override
        public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
            // 匹配业务值或枚举名称即可通过验证
            return values.contains(String.valueOf(value)) || enumsNames.contains(String.valueOf(value));
        }
    }

}
