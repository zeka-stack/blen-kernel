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
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.2.3
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
