package dev.dong4j.zeka.kernel.common.exception;

import java.io.Serial;

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.26 20:41
 * @since 1.0.0
 */
public class JsonException extends LowestException {
    /** serialVersionUID */
    @Serial
    private static final long serialVersionUID = 4076461843028836262L;

    /**
     * Instantiates a new Json exception.
     *
     * @since 1.0.0
     */
    public JsonException() {
        super();
    }

    /**
     * Instantiates a new Json exception.
     *
     * @param message the message
     * @since 1.0.0
     */
    public JsonException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Json exception.
     *
     * @param message the message
     * @param cause   the cause
     * @since 1.0.0
     */
    public JsonException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Instantiates a new Json exception.
     *
     * @param cause the cause
     * @since 1.0.0
     */
    public JsonException(Throwable cause) {
        super(cause);
    }

    /**
     * Instantiates a new Json exception.
     *
     * @param message            the message
     * @param cause              the cause
     * @param enableSuppression  the enable suppression
     * @param writableStackTrace the writable stack traceId
     * @since 1.0.0
     */
    protected JsonException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
