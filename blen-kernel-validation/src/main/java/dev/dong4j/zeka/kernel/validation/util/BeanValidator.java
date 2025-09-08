package dev.dong4j.zeka.kernel.validation.util;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import lombok.experimental.UtilityClass;

/**
 * Bean验证工具类，基于JSR-303规范的Bean Validation实现
 *
 * 提供了统一的Java Bean数据校验功能，支持单个对象、多个对象和集合的校验
 * 使用Hibernate Validator作为默认实现，支持所有标准注解和自定义验证注解
 *
 * 主要功能：
 * - 单个对象校验（支持分组验证）
 * - 多个对象批量校验
 * - 集合元素逐个校验
 * - 验证结果统一返回（Map格式）
 * - 高性能验证器实例复用
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2019.12.27 13:20
 * @since 1.0.0
 */
@UtilityClass
public class BeanValidator {
    /** 校验工厂 */
    private static final ValidatorFactory VALIDATOR_FACTORY = Validation.buildDefaultValidatorFactory();

    /**
     * Validate map
     *
     * @param <T>    parameter
     * @param t      t
     * @param groups groups
     * @return the map
     * @since 1.0.0
     */
    private static <T> Map<String, String> validate(T t, Class<?>... groups) {
        Validator validator = VALIDATOR_FACTORY.getValidator();
        Set<ConstraintViolation<T>> validate = validator.validate(t, groups);
        if (validate.isEmpty()) {
            return Collections.emptyMap();
        } else {
            Map<String, String> errors = Maps.newLinkedHashMap();
            for (ConstraintViolation<T> violation : validate) {
                errors.put(violation.getPropertyPath().toString(), violation.getMessage());
            }
            return errors;
        }
    }

    /**
     * Validatelist map.
     *
     * @param collection the collection
     * @return the map
     * @since 1.0.0
     */
    private static Map<String, String> validatelist(Collection<?> collection) {
        Preconditions.checkNotNull(collection);
        Iterator<?> iterator = collection.iterator();
        Map<String, String> errors;
        do {
            if (!iterator.hasNext()) {
                return Collections.emptyMap();
            }
            Object next = iterator.next();
            errors = validate(next);
        } while (errors.isEmpty());
        return errors;
    }

    /**
     * Validateobject map.
     *
     * @param first   the first
     * @param objects the objects
     * @return the map
     * @since 1.0.0
     */
    public static Map<String, String> validateobject(Object first, Object... objects) {
        return objects != null && objects.length > 0 ? validatelist(Lists.asList(first, objects)) : validate(first);
    }
}
