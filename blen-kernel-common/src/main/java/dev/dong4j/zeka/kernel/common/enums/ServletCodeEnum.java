package dev.dong4j.zeka.kernel.common.enums;

import dev.dong4j.zeka.kernel.common.api.IResultCode;
import dev.dong4j.zeka.kernel.common.exception.ServletCodeBundle;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Servlet异常类型错误码枚举，统一定义Web层各种异常的错误码和状态码
 * <p>
 * 该枚举定义了Spring MVC中常见的Servlet层异常的错误码映射
 * 为全局异常处理器提供统一的错误响应格式，提高API的一致性
 * <p>
 * 主要特性：
 * - 完整覆盖：包含Spring MVC中常见的所有Web层异常类型
 * - 错误码映射：为每个异常提供自定义的业务错误码
 * - HTTP状态码：包含对应的标准HTTP状态码
 * - 国际化支持：错误消息支持多语言
 * - 异常类名：提供异常类名方便调试和日志记录
 * <p>
 * 包含的异常类型：
 * <b>4xx 客户端错误：</b>
 * - 参数验证异常：MethodArgumentNotValidException、BindException
 * - 参数类型异常：MethodArgumentTypeMismatchException、TypeMismatchException
 * - 参数缺失异常：MissingServletRequestParameterException、MissingPathVariableException
 * - 请求体异常：HttpMessageNotReadableException、MissingServletRequestPartException
 * - 路由异常：NoHandlerFoundException、NoSuchRequestHandlingMethodException
 * - 方法不支持：HttpRequestMethodNotSupportedException
 * - 媒体类型异常：HttpMediaTypeNotSupportedException、HttpMediaTypeNotAcceptableException
 * <p>
 * <b>5xx 服务器错误：</b>
 * - 转换异常：ConversionNotSupportedException
 * - 消息写入异常：HttpMessageNotWritableException
 * - 异步请求超时：AsyncRequestTimeoutException
 * - 内部错误：InnerErrorException
 * <p>
 * 使用场景：
 * - 全局异常处理器中的错误码映射
 * - API响应格式的统一化处理
 * - 日志记录和监控系统的错误分类
 * - 前后端错误码的统一约定
 * <p>
 * 使用示例：
 * <pre>{@code
 * @RestControllerAdvice
 * public class GlobalExceptionHandler {
 *
 *     @ExceptionHandler(MethodArgumentNotValidException.class)
 *     public Result<Void> handleValidationException(MethodArgumentNotValidException ex) {
 *         ServletCodeEnum errorCode = ServletCodeEnum.METHOD_ARGUMENT_NOT_VALID;
 *         return Result.failure(errorCode.getCode(), errorCode.getMessage());
 *     }
 * }
 * }</pre>
 * <p>
 * 错误码格式说明：
 * - 错误码：4位数字，前2位表示HTTP状态码类别，后2位表示具体错误
 * - HTTP状态码：标准的HTTP状态码，用于设置响应状态
 * - 错误消息：支持国际化，可以根据语言返回不同消息
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2021.01.31 11:53
 * @since 1.0.0
 */
@Getter
@AllArgsConstructor
public enum ServletCodeEnum implements IResultCode {
    /** 方法级的参数验证时候后的异常 */
    METHOD_ARGUMENT_NOT_VALID(4400,
        ServletCodeBundle.message("method.argument.not.valid.exception"),
        HttpServletResponse.SC_BAD_REQUEST, "MethodArgumentNotValidException"),
    /** Method argument type mismatch exception servlet code enum */
    METHOD_ARGUMENT_TYPE_MISMATCH(4400,
        ServletCodeBundle.message("method.argument.type.mismatch.exception"),
        HttpServletResponse.SC_BAD_REQUEST, "MethodArgumentTypeMismatchException"),
    /** Missing servlet request part exception servlet code enum */
    MISSING_SERVLET_REQUEST_PART(4400,
        ServletCodeBundle.message("missing.servlet.request.part.exception"),
        HttpServletResponse.SC_BAD_REQUEST, "MissingServletRequestPartException"),
    /** Missing path variable exception servlet code enum */
    MISSING_PATH_VARIABLE(4400,
        ServletCodeBundle.message("missing.path.variable.exception"),
        HttpServletResponse.SC_BAD_REQUEST, "MissingPathVariableException"),
    /** 参数绑定异常 */
    BIND_ERROR(4400,
        ServletCodeBundle.message("bind.exception"),
        HttpServletResponse.SC_BAD_REQUEST, "BindException"),
    /** Missing servlet request parameter exception servlet code enum */
    MISSING_SERVLET_REQUEST_PARAMETER(4400,
        ServletCodeBundle.message("missing.servlet.request.parameter.exception"),
        HttpServletResponse.SC_BAD_REQUEST, "MissingServletRequestParameterException"),
    /** Type mismatch exception servlet code enum */
    TYPE_MISMATCH(4400, ServletCodeBundle.message("type.mismatch.exception"),
        HttpServletResponse.SC_BAD_REQUEST, "TypeMismatchException"),
    /** Servlet request binding exception servlet code enum */
    SERVLET_REQUEST_BINDING(4400,
        ServletCodeBundle.message("servlet.request.binding.exception"),
        HttpServletResponse.SC_BAD_REQUEST, "ServletRequestBindingException"),
    /** Missing request header exception servlet code enum */
    MISSING_REQUEST_HEADER(4400,
        ServletCodeBundle.message("missing.request.params.exception"),
        HttpServletResponse.SC_BAD_REQUEST, "MissingServletRequestParameterException"),
    /** Http message not readable exception servlet code enum */
    HTTP_MESSAGE_NOT_READABLE(4400,
        ServletCodeBundle.message("http.message.not.readable.exception"),
        HttpServletResponse.SC_BAD_REQUEST, "HttpMessageNotReadableException"),
    /** No handler found exception servlet code enum */
    NO_HANDLER_FOUND(4404,
        ServletCodeBundle.message("no.handler.found.exception"),
        HttpServletResponse.SC_NOT_FOUND, "NoHandlerFoundException"),
    /** No such request handling method exception servlet code enum */
    NO_SUCH_REQUEST_HANDLING_METHOD(4404,
        ServletCodeBundle.message("no.such.request.handling.method.exception"),
        HttpServletResponse.SC_NOT_FOUND, "NoSuchRequestHandlingMethodException"),
    /** Http request method not supported exception servlet code enum */
    HTTP_REQUEST_METHOD_NOT_SUPPORTED(4405,
        ServletCodeBundle.message("http.request.method.not.supported.exception"),
        HttpServletResponse.SC_METHOD_NOT_ALLOWED, "HttpRequestMethodNotSupportedException"),
    /** Http media type not acceptable exception servlet code enum */
    HTTP_MEDIA_TYPE_NOT_ACCEPTABLE(4406,
        ServletCodeBundle.message("http.media.type.not.acceptable.exception"),
        HttpServletResponse.SC_NOT_ACCEPTABLE, "HttpMediaTypeNotAcceptableException"),
    /** Http media type not supported exception servlet code enum */
    HTTP_MEDIA_TYPE_NOT_SUPPORTED(4415,
        ServletCodeBundle.message("http.media.type.not.supported.exception"),
        HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE, "HttpMediaTypeNotSupportedException"),
    /** Conversion not supported exception servlet code enum */
    CONVERSION_NOT_SUPPORTED(4500,
        ServletCodeBundle.message("conversion.not.supported.exception"),
        HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "ConversionNotSupportedException"),
    /** Http message not writable exception servlet code enum */
    HTTP_MESSAGE_NOT_WRITABLE(4500,
        ServletCodeBundle.message("http.message.not.writable.exception"),
        HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "HttpMessageNotWritableException"),
    /** Async request timeout exception servlet code enum */
    ASYNC_REQUEST_TIMEOUT(4503,
        ServletCodeBundle.message("async.request.timeout.exception"),
        HttpServletResponse.SC_SERVICE_UNAVAILABLE, "AsyncRequestTimeoutException"),
    /** Missing requestheader servlet code enum */
    MISSING_REQUESTHEADER(4504,
        ServletCodeBundle.message("missing.request.header.exception"),
        HttpServletResponse.SC_BAD_REQUEST, "MissingRequestHeaderException"),

    /** Inner error servlet code enum */
    INNER_ERROR(5000, ServletCodeBundle.message("inner.error"), HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
        "InnerErrorException");

    /** 返回码,目前与 {@link #statusCode} 相同 */
    public final Integer code;
    /** 返回信息,直接读取异常的 message */
    public final String message;
    /** HTTP 状态码 */
    public final int statusCode;
    /** 异常名 */
    public final String exceptionName;
}
