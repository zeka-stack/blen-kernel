package dev.dong4j.zeka.kernel.common.type.exception;

import java.io.Serial;

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.26 20:43
 * @since 1.0.0
 */
public class TypeException extends RuntimeException {
    /** serialVersionUID */
    @Serial
    private static final long serialVersionUID = -2841591264003682971L;

    /**
     * Instantiates a new Type exception.
     *
     * @since 1.0.0
     */
    public TypeException() {
    }

    /**
     * Instantiates a new Type exception.
     *
     * @param message the message
     * @since 1.0.0
     */
    public TypeException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Type exception.
     *
     * @param message the message
     * @param cause   the cause
     * @since 1.0.0
     */
    public TypeException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Instantiates a new Type exception.
     *
     * @param cause the cause
     * @since 1.0.0
     */
    public TypeException(Throwable cause) {
        super(cause);
    }

    /**
     * Instantiates a new Type exception.
     *
     * @param message            the message
     * @param cause              the cause
     * @param enableSuppression  the enable suppression
     * @param writableStackTrace the writable stack traceId
     * @since 1.0.0
     */
    public TypeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
