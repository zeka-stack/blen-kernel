package dev.dong4j.zeka.kernel.validation.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.dong4j.zeka.kernel.common.util.JsonUtils;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.ValidationException;
import jakarta.validation.Validator;
import java.beans.PropertyDescriptor;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.CollectionUtils;

/**
 * <p>Description:  </p>
 *
 * @author dong4j
 * @version 1.3.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.04.18 11:59
 * @since 1.0.0
 */
public class ValidatorUtils implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    /** validator */
    private static Validator validator;
    /** MAPPER */
    private static final ObjectMapper MAPPER = JsonUtils.getCopyMapper();
    /** applicationContext */
    private static ConfigurableApplicationContext applicationContext = null;

    /**
     * Initialize the given application context.
     *
     * @param applicationContext the application to configure
     * @since 1.6.0
     */
    @Override
    public void initialize(@NotNull ConfigurableApplicationContext applicationContext) {
        ValidatorUtils.applicationContext = applicationContext;
    }

    /**
     * Validate result process optional
     *
     * @param obj    待验证的实体参数
     * @param groups 分组验证接口
     * @return the optional
     * @throws ValidationException validation exception
     * @since 1.0.0
     */
    public static Optional<String> validateResultProcess(Object obj, Class<?>... groups) throws ValidationException {
        Set<ConstraintViolation<Object>> results = getValidator().validate(obj, groups);
        if (CollectionUtils.isEmpty(results)) {
            return Optional.empty();
        }

        List<ErrorMessage> errorMessages = results.stream().map(result -> {
            try {
                return MAPPER.readValue(result.getMessage(), new TypeReference<List<ErrorMessage>>() {
                });
            } catch (Exception e) {
                ErrorMessage errorMessage = new ErrorMessage();
                errorMessage.setPropertyPath(String.format("%s.%s",
                    result.getRootBeanClass().getSimpleName(),
                    result.getPropertyPath().toString()));
                errorMessage.setMessage(result.getMessage());
                return Collections.singletonList(errorMessage);
            }
        }).flatMap(Collection::stream).collect(Collectors.toList());

        try {
            return Optional.of(MAPPER.writeValueAsString(errorMessages));
        } catch (JsonProcessingException e) {
            throw new ValidationException("JsonProcessingException " + e.getMessage());
        }
    }

    /**
     * Validate result proces with id optional
     *
     * @param obj         obj
     * @param idFieldName id field name
     * @return the optional
     * @throws ValidationException validation exception
     * @since 1.0.0
     */
    public static @NotNull Optional<String> validateResultProcesWithId(Object obj, String idFieldName) throws ValidationException {
        Optional<String> result = ValidatorUtils.validateResultProcess(obj);
        if (result.isPresent()) {
            ErrorMessageWithId errorMessageWithId = new ErrorMessageWithId();
            try {
                PropertyDescriptor pd = BeanUtils.getPropertyDescriptor(obj.getClass(), idFieldName);
                errorMessageWithId.setId(Objects.requireNonNull(pd).getReadMethod().invoke(obj).toString());
            } catch (Exception e) {
                errorMessageWithId.setId("");
            }
            errorMessageWithId.setErrorMessage(result.get());
            try {
                return Optional.of(MAPPER.writeValueAsString(errorMessageWithId));
            } catch (JsonProcessingException e) {
                throw new ValidationException("JsonProcessingException " + e.getMessage());
            }
        } else {
            return result;
        }
    }

    /**
     * Validate result process with exception *
     *
     * @param obj obj
     * @throws ValidationException validation exception
     * @since 1.0.0
     */
    public static void validateResultProcessWithException(Object obj) throws ValidationException {
        Optional<String> validateResult = ValidatorUtils.validateResultProcess(obj);
        if (validateResult.isPresent()) {
            throw new ValidationException(validateResult.get());
        }
    }

    /**
     * Validate result proces with id and exception *
     *
     * @param obj         obj
     * @param idFieldName id field name
     * @throws ValidationException validation exception
     * @since 1.0.0
     */
    public static void validateResultProcesWithIdAndException(Object obj, String idFieldName) throws ValidationException {
        Optional<String> validateResult = ValidatorUtils.validateResultProcesWithId(obj, idFieldName);
        if (validateResult.isPresent()) {
            throw new ValidationException(validateResult.get());
        }
    }

    /**
     * Gets validator *
     *
     * @return the validator
     * @since 1.6.0
     */
    public static Validator getValidator() {
        if (ValidatorUtils.validator == null) {
            if (applicationContext != null) {
                ValidatorUtils.validator = applicationContext.getBean(Validator.class);
            } else {
                ValidatorUtils.validator = Validation.buildDefaultValidatorFactory().getValidator();
            }
        }
        return ValidatorUtils.validator;
    }

    /**
     * <p>Description: </p>
     *
     * @author dong4j
     * @version 1.2.5
     * @email "mailto:dong4j@gmail.com"
     * @date 2020.03.04 11:37
     * @since 1.0.0
     */
    @Data
    private static class ErrorMessage {
        /** Property path */
        private String propertyPath;
        /** Message */
        private String message;
    }

    /**
     * <p>Description: </p>
     *
     * @author dong4j
     * @version 1.2.5
     * @email "mailto:dong4j@gmail.com"
     * @date 2020.03.04 11:37
     * @since 1.0.0
     */
    @Data
    private static class ErrorMessageWithId {
        /** Id */
        private String id;
        /** Error message */
        private String errorMessage;
    }

    /**
     * <p>Description: </p>
     *
     * @author dong4j
     * @version 1.2.5
     * @email "mailto:dong4j@gmail.com"
     * @date 2020.03.04 11:37
     * @since 1.0.0
     */
    public static class ValidatorGroup {
        /**
         * <p>Description: </p>
         *
         * @author dong4j
         * @version 1.2.5
         * @email "mailto:dong4j@gmail.com"
         * @date 2020.03.04 11:37
         * @since 1.0.0
         */
        public interface First {
        }

        /**
         * <p>Description: </p>
         *
         * @author dong4j
         * @version 1.2.5
         * @email "mailto:dong4j@gmail.com"
         * @date 2020.03.04 11:37
         * @since 1.0.0
         */
        public interface Second {
        }

        /**
         * <p>Description: </p>
         *
         * @author dong4j
         * @version 1.2.5
         * @email "mailto:dong4j@gmail.com"
         * @date 2020.03.04 11:37
         * @since 1.0.0
         */
        public interface Third {
        }
    }

}
