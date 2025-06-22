package dev.dong4j.zeka.kernel.common.exception;

import dev.dong4j.zeka.kernel.common.api.BaseCodes;

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.02 14:59
 * @since 1.0.0
 */
public class StarterException extends BaseException {
    /** serialVersionUID */
    private static final long serialVersionUID = 4076461843028836262L;

    /**
     * Instantiates a new Json exception.
     *
     * @since 1.0.0
     */
    public StarterException() {
        super();
    }

    /**
     * Instantiates a new Json exception.
     *
     * @param message the message
     * @since 1.0.0
     */
    public StarterException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Json exception.
     *
     * @param message the message
     * @param cause   the cause
     * @since 1.0.0
     */
    public StarterException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * msg 占位符替换
     *
     * @param msg  msg
     * @param args args
     * @since 1.0.0
     */
    public StarterException(String msg, Object... args) {
        super(msg, args);
        this.resultCode = BaseCodes.FAILURE;
    }

    /**
     * Instantiates a new Json exception.
     *
     * @param cause the cause
     * @since 1.0.0
     */
    public StarterException(Throwable cause) {
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
    protected StarterException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
