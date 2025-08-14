package integration.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.jetbrains.annotations.NotNull;

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.27 14:52
 * @since 1.0.0
 */
public class NameConstraintValidator implements ConstraintValidator<NameConstraint, String> {

    /** Valid values */
    private String[] validValues;

    /**
     * Initialize *
     *
     * @param constraintAnnotation constraint annotation
     * @since 1.0.0
     */
    @Override
    public void initialize(@NotNull NameConstraint constraintAnnotation) {
        validValues = constraintAnnotation.allowedValues();
    }

    /**
     * Is valid boolean
     *
     * @param value   value
     * @param context context
     * @return the boolean
     * @since 1.0.0
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        for (String s : validValues) {
            if (s.equals(value)) {
                return true;
            }
        }
        return false;
    }

}
