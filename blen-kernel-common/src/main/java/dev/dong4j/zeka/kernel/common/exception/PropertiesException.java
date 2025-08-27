package dev.dong4j.zeka.kernel.common.exception;

import java.io.Serial;

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.26 20:42
 * @since 1.0.0
 */
public class PropertiesException extends LowestException {
    /** serialVersionUID */
    @Serial
    private static final long serialVersionUID = -6498727260647427447L;

    /**
     * Properties exception
     *
     * @param msg  msg
     * @param args args
     * @since 1.0.0
     */
    public PropertiesException(String msg, Object... args) {
        super(msg, args);
    }

    /**
     * Properties exception
     *
     * @param cause cause
     * @since 1.0.0
     */
    public PropertiesException(Throwable cause) {
        super(cause);
    }
}
