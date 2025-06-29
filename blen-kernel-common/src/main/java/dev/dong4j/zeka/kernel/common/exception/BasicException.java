package dev.dong4j.zeka.kernel.common.exception;

import dev.dong4j.zeka.kernel.common.context.Trace;
import dev.dong4j.zeka.kernel.common.support.StrFormatter;
import java.io.PrintStream;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>Description: 框架基础异常基类 </p>
 *
 * @author dong4j
 * @version 1.0.4
 * @email "mailto:dongshijie@gmail.com"
 * @date 2020.02.09 16:50
 * @since 1.0.4
 */
@Slf4j
@SuppressWarnings("java:S1165")
public class BasicException extends RuntimeException {
    /** serialVersionUID */
    private static final long serialVersionUID = 3076052230646484392L;
    /** 异常消息参数 */
    protected transient Object[] args;
    /** Code */
    @Getter
    protected String code;
    /** Message */
    protected String message;
    /** Trace id */
    @Getter
    protected String traceId;
    /** DEFAULT_ERROR_CODE */
    public static final String DEFAULT_ERROR_CODE = "B.F-5000";
    /** DEFAULT_MESSAGE */
    public static final String DEFAULT_MESSAGE = "服务内部错误";

    /**
     * Basic exception
     *
     * @since 1.4.0
     */
    public BasicException() {
        this(DEFAULT_MESSAGE);
    }

    /**
     * msg 占位符替换
     *
     * @param message message
     * @param args    args
     * @since 1.0.0
     */
    public BasicException(String message, Object... args) {
        this(StrFormatter.mergeFormat(message, args));
    }

    /**
     * Instantiates a new Base exception.
     *
     * @param message message
     * @since 1.0.0
     */
    public BasicException(String message) {
        this(DEFAULT_ERROR_CODE, message);
    }

    /**
     * Basic exception
     *
     * @param code    code
     * @param message message
     * @since 1.6.0
     */
    public BasicException(String code, String message) {
        super(message);
        this.init(code, message);
    }

    /**
     * Base exception
     *
     * @param cause cause
     * @since 1.0.0
     */
    public BasicException(Throwable cause) {
        this(DEFAULT_MESSAGE, cause);
    }

    /**
     * Base exception
     *
     * @param message message
     * @param cause   cause
     * @since 1.0.0
     */
    public BasicException(String message, Throwable cause) {
        this(DEFAULT_ERROR_CODE, message, cause);
    }

    /**
     * Basic exception
     *
     * @param code    code
     * @param message message
     * @param cause   cause
     * @since 1.6.0
     */
    public BasicException(String code, String message, Throwable cause) {
        super(message, cause);
        this.init(code, message);
    }

    /**
     * Init
     *
     * @param code    code
     * @param message message
     * @since 1.9.0
     */
    private void init(String code, String message) {
        this.code = code;
        this.message = message;
        this.traceId = Trace.context().get();
    }

    /**
     * 重写打印异常堆栈, 转为日志输出.
     *
     * @since 1.0.0
     */
    @Override
    public void printStackTrace() {
        log.error("", this);
    }

    /**
     * Prints this throwable and its backtrace to the specified print stream.
     *
     * @param s {@code PrintStream} to use for output
     * @since 1.6.0
     */
    @Override
    public void printStackTrace(PrintStream s) {
        log.error("", this);
    }
}
