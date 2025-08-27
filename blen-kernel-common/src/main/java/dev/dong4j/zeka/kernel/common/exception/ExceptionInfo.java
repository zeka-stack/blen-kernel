package dev.dong4j.zeka.kernel.common.exception;

import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

}
