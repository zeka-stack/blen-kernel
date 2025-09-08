package dev.dong4j.zeka.kernel.common.exception;

import dev.dong4j.zeka.kernel.common.util.StringUtils;
import java.io.PrintStream;
import java.io.Serial;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>Description: 服务内部错误异常, 会输出堆栈信息, traceId, 当前应用名和 ip </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.11.18 16:11
 * @since 1.0.0
 */
@Slf4j
public class ServiceInternalException extends RuntimeException {
    /** serialVersionUID */
    @Serial
    private static final long serialVersionUID = -3466744408568354601L;

    /** 应用名 */
    @Setter
    @Getter
    protected String applicationName;
    /** 应用 ip */
    @Setter
    @Getter
    protected String ip;
    /** 应用端口 */
    @Setter
    @Getter
    protected Integer port;
    /** 当前环境 */
    @Setter
    @Getter
    protected String env;
    /** Rpc */
    @Setter
    @Getter
    protected boolean rpc;
    /** Trace id */
    @Setter
    @Getter
    protected String traceId;
    /** DEFAULT_ERROR_CODE */
    public static final String DEFAULT_ERROR_CODE = "S.I-5000";
    /** DEFAULT_MESSAGE */
    public static final String DEFAULT_MESSAGE = "服务内部错误";

    /**
     * Basic exception
     *
     * @since 1.0.0
     */
    public ServiceInternalException() {
        super();
    }

    /**
     * Service internal exception
     *
     * @param throwable throwable
     * @since 1.0.0
     */
    public ServiceInternalException(Throwable throwable) {
        super(DEFAULT_MESSAGE, throwable);
    }

    /**
     * Service internal exception
     *
     * @param message message
     * @since 1.0.0
     */
    public ServiceInternalException(String message) {
        super(message);
    }

    /**
     * Service internal exception
     *
     * @param message   message
     * @param throwable throwable
     * @since 1.0.0
     */
    public ServiceInternalException(String message, Throwable throwable) {
        super(message, throwable);
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
     * @since 1.0.0
     */
    @Override
    public void printStackTrace(PrintStream s) {
        log.error("", this);
    }

    /**
     * Gets message *
     *
     * @return the message
     * @since 1.0.0
     */
    @Override
    public String getMessage() {
        return super.getMessage()
            + (StringUtils.isBlank(this.applicationName) ? "" : ": applicationName='" + this.applicationName + '\'')
            + (StringUtils.isBlank(this.ip) ? "" : ", ip='" + this.ip + '\'')
            + (this.port == null ? "" : ", port='" + this.port + '\'')
            + (StringUtils.isBlank(this.env) ? "" : ", env='" + this.env + '\'')
            + (StringUtils.isBlank(this.traceId) ? "" : ", traceId='" + this.traceId);
    }

}
