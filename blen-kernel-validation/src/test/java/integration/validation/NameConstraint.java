package integration.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.27 14:52
 * @since 1.0.0
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NameConstraintValidator.class)
public @interface NameConstraint {
    /**
     * Allowed values string [ ].
     *
     * @return the string [ ]
     * @since 1.0.0
     */
    String[] allowedValues();

    /**
     * Groups class [ ].
     *
     * @return the class [ ]
     * @since 1.0.0
     */
    Class<?>[] groups() default {};

    /**
     * Payload class [ ].
     *
     * @return the class [ ]
     * @since 1.0.0
     */
    Class<? extends Payload>[] payload() default {};

    /**
     * Message string.
     *
     * @return the string
     * @since 1.0.0
     */
    String message();
}
