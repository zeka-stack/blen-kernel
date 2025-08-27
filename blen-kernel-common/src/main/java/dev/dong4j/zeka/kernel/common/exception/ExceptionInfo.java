package dev.dong4j.zeka.kernel.common.exception;

import dev.dong4j.zeka.kernel.common.context.Trace;
import dev.dong4j.zeka.kernel.common.util.Exceptions;
import dev.dong4j.zeka.kernel.common.util.StringPool;
import dev.dong4j.zeka.kernel.common.util.WebUtils;
import jakarta.servlet.http.HttpServletRequest;
import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

/**
 * <p>Description: 开发环境时输出更多的异常信息 </p>
 *
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.27 14:55
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionInfo implements Serializable {
    /** serialVersionUID */
    @Serial
    private static final long serialVersionUID = -5072425562316472427L;
    /** 请求路径 */
    private String path;
    /** 请求参数 */
    private Object params;
    /** 请求方式 */
    private String method;
    /** 请求方地址 */
    private String remoteAddr;
    /** header */
    private Object headers;
    /** 追踪 id */
    private String traceId;
    /** 异常类 */
    private String exceptionClass;
    /** 错误信息 */
    private String errorMessage;
    /** 异常堆栈 */
    private String stackTrace;
    /** 日志系统链接 */
    private String hyperlink;

    /**
     * Build exception data
     *
     * @param hyperlink hyperlink
     * @param throwable throwable
     * @param request   request
     * @return the exception info
     * @since 2024.2.0
     */
    public ExceptionInfo create(String hyperlink, @NotNull Throwable throwable, @NotNull HttpServletRequest request) {
        ExceptionInfo exceptionEntity = new ExceptionInfo();
        exceptionEntity.setPath(request.getRequestURI());
        exceptionEntity.setParams(WebUtils.getRequestParamString(request));
        exceptionEntity.setMethod(request.getMethod());
        exceptionEntity.setRemoteAddr(request.getRemoteAddr());
        exceptionEntity.setHeaders(WebUtils.getHeader(request));
        exceptionEntity.setExceptionClass(throwable.getClass().getName());
        exceptionEntity.setTraceId(Trace.context().get());
        exceptionEntity.setErrorMessage(throwable.getMessage());
        exceptionEntity.setHyperlink(StringPool.NEWLINE + hyperlink);
        exceptionEntity.setStackTrace(Exceptions.getStackTraceAsString(throwable));
        return exceptionEntity;
    }

}
