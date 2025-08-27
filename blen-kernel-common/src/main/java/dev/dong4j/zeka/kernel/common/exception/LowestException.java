package dev.dong4j.zeka.kernel.common.exception;

import dev.dong4j.zeka.kernel.common.api.BaseCodes;
import dev.dong4j.zeka.kernel.common.api.IResultCode;
import dev.dong4j.zeka.kernel.common.context.Trace;
import dev.dong4j.zeka.kernel.common.support.StrFormatter;
import java.io.PrintStream;
import java.io.Serial;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

/**
 * <p>Description: 基础异常类,所有自定义异常类都需要继承本类 </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2023.11.11 23:45
 * @since 2024.1.1
 */
@Slf4j
@Getter
public class LowestException extends RuntimeException {

    /** serialVersionUID */
    @Serial
    private static final long serialVersionUID = 1L;

    /** 返回码 */
    protected IResultCode resultCode;
    /** 异常消息参数 */
    protected Object[] args;
    /** Trace id */
    @Getter
    protected String traceId;

    /**
     * Instantiates a new Base exception.
     *
     * @since 1.0.0
     */
    public LowestException() {
        super(BaseCodes.FAILURE.getMessage());
        this.resultCode = BaseCodes.FAILURE;
        this.traceId = Trace.context().get();
    }

    /**
     * Instantiates a new Base exception.
     *
     * @param msg the msg
     * @since 1.0.0
     */
    public LowestException(String msg) {
        super(msg);
        this.resultCode = BaseCodes.FAILURE;
        this.traceId = Trace.context().get();
    }

    /**
     * Instantiates a new Base exception.
     *
     * @param code the code
     * @param msg  the msg
     * @since 1.0.0
     */
    public LowestException(int code, String msg) {
        super(msg);
        this.traceId = Trace.context().get();
        this.resultCode = new IResultCode() {
            @Serial
            private static final long serialVersionUID = 2590640370242410124L;

            @Override
            public String getMessage() {
                return msg;
            }

            @Override
            public Integer getCode() {
                return code;
            }
        };
    }

    /**
     * msg 占位符替换
     *
     * @param msg  msg
     * @param args args
     * @since 1.0.0
     */
    public LowestException(String msg, Object... args) {
        super(StrFormatter.mergeFormat(msg, args));
        this.resultCode = BaseCodes.FAILURE;
        this.traceId = Trace.context().get();
    }

    /**
     * Base exception
     *
     * @param cause cause
     * @since 1.0.0
     */
    public LowestException(Throwable cause) {
        super(cause);
        this.resultCode = BaseCodes.FAILURE;
        this.traceId = Trace.context().get();
    }

    /**
     * Base exception
     *
     * @param msg   msg
     * @param cause cause
     * @since 1.0.0
     */
    public LowestException(String msg, Throwable cause) {
        super(msg, cause);
        this.resultCode = BaseCodes.FAILURE;
        this.traceId = Trace.context().get();
    }

    /**
     * Instantiates a new Base exception.
     *
     * @param resultCode the response enum
     * @since 1.0.0
     */
    public LowestException(@NotNull IResultCode resultCode) {
        super(resultCode.getMessage());
        this.resultCode = resultCode;
        this.traceId = Trace.context().get();
    }

    /**
     * Base exception
     *
     * @param resultCode result code
     * @param cause      cause
     * @since 1.0.0
     */
    public LowestException(@NotNull IResultCode resultCode, Throwable cause) {
        super(resultCode.getMessage(), cause);
        this.resultCode = resultCode;
        this.traceId = Trace.context().get();
    }

    /**
     * Instantiates a new Base exception.
     *
     * @param code  the code
     * @param msg   the msg
     * @param cause cause
     * @since 1.0.0
     */
    public LowestException(int code, String msg, Throwable cause) {
        super(msg, cause);
        this.traceId = Trace.context().get();
        this.resultCode = new IResultCode() {
            @Serial
            private static final long serialVersionUID = 2590640370242410124L;

            @Override
            public String getMessage() {
                return msg;
            }

            @Override
            public Integer getCode() {
                return code;
            }
        };
    }

    /**
     * Instantiates a new Base exception.
     *
     * @param resultCode the response enum
     * @param args       the args
     * @param msg        msg        替换占位符后的消息
     * @since 1.0.0
     */
    public LowestException(IResultCode resultCode, Object[] args, String msg) {
        super(StrFormatter.mergeFormat(msg, args));
        this.resultCode = resultCode;
        this.args = args;
        this.traceId = Trace.context().get();
    }

    /**
     * Instantiates a new Base exception.
     *
     * @param resultCode the response enum
     * @param args       the args
     * @param msg        msg
     * @param cause      the cause
     * @since 1.0.0
     */
    public LowestException(IResultCode resultCode, Object[] args, String msg, Throwable cause) {
        super(StrFormatter.mergeFormat(msg, args), cause);
        this.resultCode = resultCode;
        this.args = args;
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
