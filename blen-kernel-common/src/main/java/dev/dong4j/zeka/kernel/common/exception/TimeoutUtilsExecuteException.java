package dev.dong4j.zeka.kernel.common.exception;

import java.io.Serial;

/**
 * <p>Description:  </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2021.01.26 13:22
 * @since 1.7.1
 */
public class TimeoutUtilsExecuteException extends Exception {
    /** serialVersionUID */
    @Serial
    private static final long serialVersionUID = 5896555103722435483L;

    /**
     * Timeout utils execute exception
     *
     * @param message message
     * @since 1.7.1
     */
    public TimeoutUtilsExecuteException(String message) {
        super(message);

    }
}
