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
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2022.02.15 14:31
 * @since 2022.1.1
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {FIELD, METHOD, PARAMETER, ANNOTATION_TYPE})
@Constraint(validatedBy = EnumX.Validator.class)
public @interface EnumX {
    /**
     * Value
     *
     * @return the class
     * @since 2022.1.1
     */
    Class<? extends Enum<?>> value();

    /**
     * Message
     *
     * @return the string
     * @since 2022.1.1
     */
    String message() default "{validation.constraints.EnumX.message}";

    /**
     * Method
     *
     * @return the string
     * @since 2022.1.1
     */
    String method() default SerializeEnum.VALUE_METHOD_NAME;

    /**
     * Groups
     *
     * @return the class [ ]
     * @since 2022.1.1
     */
    Class<?>[] groups() default {};

    /**
     * Payload
     *
     * @return the class [ ]
     * @since 2022.1.1
     */
    Class<? extends Payload>[] payload() default {};

    /**
     * <p>Description: </p>
     *
     * @author dong4j
     * @version 1.0.0
     * @email "mailto:dong4j@gmail.com"
     * @date 2022.02.15 14:52
     * @since 2022.1.1
     */
    @Slf4j
    class Validator implements ConstraintValidator<EnumX, Object> {

        /** 保存所有的 value */
        private final Set<String> values = new HashSet<>();
        /** Enums names */
        private final Set<String> enumsNames = new HashSet<>();

        /**
         * Initialize
         *
         * @param enumValidator enum validator
         * @since 2022.1.1
         */
        @Override
        public void initialize(EnumX enumValidator) {
            Class<? extends Enum<?>> aClass = enumValidator.value();
            // 获取所有枚举实例
            Enum<?>[] enumConstants = aClass.getEnumConstants();

            try {
                Method method = aClass.getMethod(enumValidator.method());
                for (Enum<?> item : enumConstants) {
                    values.add(String.valueOf(method.invoke(item)));
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
         * Is valid
         *
         * @param value                      value
         * @param constraintValidatorContext constraint validator context
         * @return the boolean
         * @since 2022.1.1
         */
        @Override
        public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
            // 匹配 value 或者 name 即可
            return values.contains(String.valueOf(value)) || enumsNames.contains(String.valueOf(value));
        }
    }

}
