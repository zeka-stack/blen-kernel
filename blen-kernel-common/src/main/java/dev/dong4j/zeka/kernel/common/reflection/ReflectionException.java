package dev.dong4j.zeka.kernel.common.reflection;

import dev.dong4j.zeka.kernel.common.exception.BaseException;
import java.io.Serial;

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.3.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.04.12 11:54
 * @since 1.0.0
 */
public class ReflectionException extends BaseException {

    /** serialVersionUID */
    @Serial
    private static final long serialVersionUID = 7642570221267566591L;

    /**
     * Reflection exception
     *
     * @since 1.0.0
     */
    public ReflectionException() {
        super();
    }

    /**
     * Reflection exception
     *
     * @param message message
     * @since 1.0.0
     */
    public ReflectionException(String message) {
        super(message);
    }

    /**
     * Reflection exception
     *
     * @param msg  msg
     * @param args args
     * @since 1.0.0
     */
    public ReflectionException(String msg, Object... args) {
        super(msg, args);
    }

    /**
     * Reflection exception
     *
     * @param message message
     * @param cause   cause
     * @since 1.0.0
     */
    public ReflectionException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Reflection exception
     *
     * @param cause cause
     * @since 1.0.0
     */
    public ReflectionException(Throwable cause) {
        super(cause);
    }

}
