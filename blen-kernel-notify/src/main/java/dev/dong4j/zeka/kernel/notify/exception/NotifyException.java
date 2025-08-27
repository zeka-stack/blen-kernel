package dev.dong4j.zeka.kernel.notify.exception;

import dev.dong4j.zeka.kernel.common.exception.LowestException;
import java.io.Serial;

/**
 * <p>Description: 通知接口异常 </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dongshijie@gmail.com"
 * @date 2020.05.07 21:43
 * @since 1.0.0
 */
public class NotifyException extends LowestException {

    /** serialVersionUID */
    @Serial
    private static final long serialVersionUID = 2614645628866480060L;

    /**
     * Instantiates a new Base exception.
     *
     * @param msg the msg
     * @since 1.0.0
     */
    public NotifyException(String msg) {
        super(msg);
    }

    /**
     * msg 占位符替换
     *
     * @param msg  msg
     * @param args args
     * @since 1.0.0
     */
    public NotifyException(String msg, Object... args) {
        super(msg, args);
    }

    /**
     * Base exception
     *
     * @param cause cause
     * @since 1.0.0
     */
    public NotifyException(Throwable cause) {
        super(cause);
    }

    /**
     * Base exception
     *
     * @param msg   msg
     * @param cause cause
     * @since 1.0.0
     */
    public NotifyException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
