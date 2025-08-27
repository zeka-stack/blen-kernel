package dev.dong4j.zeka.kernel.common.exception;

import java.io.Serial;

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.26 20:41
 * @since 1.0.0
 */
public class InstanceException extends BaseException {
    /** serialVersionUID */
    @Serial
    private static final long serialVersionUID = -5426025146601282119L;

    /**
     * Instantiates a new Instance exception.
     *
     * @since 1.0.0
     */
    public InstanceException() {
    }

    /**
     * Instantiates a new Instance exception.
     *
     * @param message the message
     * @since 1.0.0
     */
    public InstanceException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Instance exception.
     *
     * @param cause the cause
     * @since 1.0.0
     */
    public InstanceException(Throwable cause) {
        super(cause);
    }

    /**
     * Instantiates a new Instance exception.
     *
     * @param message the message
     * @param cause   the cause
     * @since 1.0.0
     */
    public InstanceException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Instantiates a new Instance exception.
     *
     * @param message            the message
     * @param cause              the cause
     * @param enableSuppression  the enable suppression
     * @param writableStackTrace the writable stack traceId
     * @since 1.0.0
     */
    public InstanceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
