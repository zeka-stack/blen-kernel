package dev.dong4j.zeka.kernel.common.exception;

import dev.dong4j.zeka.kernel.common.api.BaseCodes;
import dev.dong4j.zeka.kernel.common.api.R;
import dev.dong4j.zeka.kernel.common.api.Result;
import dev.dong4j.zeka.kernel.common.constant.BasicConstant;
import dev.dong4j.zeka.kernel.common.constant.ConfigKey;
import dev.dong4j.zeka.kernel.common.context.SpringContext;
import dev.dong4j.zeka.kernel.common.context.Trace;
import dev.dong4j.zeka.kernel.common.enums.ServletCodeEnum;
import dev.dong4j.zeka.kernel.common.enums.ZekaEnv;
import dev.dong4j.zeka.kernel.common.event.ExceptionEvent;
import dev.dong4j.zeka.kernel.common.start.ZekaAutoConfiguration;
import dev.dong4j.zeka.kernel.common.util.ConfigKit;
import dev.dong4j.zeka.kernel.common.util.EnumUtils;
import dev.dong4j.zeka.kernel.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.RestClientException;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/**
 * <p>Description:  </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.11.20 14:11
 * @since 1.0.0
 */
@Slf4j
public class GlobalExceptionHandler implements ZekaAutoConfiguration {

    /** RPC_ECEPTION */
    private static final String RPC_ECEPTION = "org.apache.dubbo.rpc.RpcException";
    /** HYPER_LINK */
    public static String hyperLink = StringUtils.format("{}?env={}&traceId={}",
        System.getProperty(ConfigKey.LogcatConfigKey.ADMIN_URL,
            "http://logcat.server:5555/"));

    /**
     * Build result result
     *
     * @param e       e
     * @param request request
     * @return the result
     * @since 1.0.0
     */
    @NotNull
    private Result<?> buildResult(LowestException e, @NotNull HttpServletRequest request) {
        if (ZekaEnv.PROD.equals(ConfigKit.getEnv())) {
            return R.failed(e.getResultCode().getCode(), e.getMessage());
        }

        Result<Object> failed = R.failed(e.getResultCode().getCode(), e.getMessage());
        failed.setExtend(buildExceptionData(buildErrorLink(), e, request));
        return failed;
    }

    /**
     * Build exception data exception info
     *
     * @param hyperlink hyperlink
     * @param throwable throwable
     * @param request   request
     * @return the exception info
     * @since 1.0.0
     */
    @NotNull
    @SuppressWarnings("checkstyle:Regexp")
    public static ExceptionInfo buildExceptionData(String hyperlink, @NotNull Throwable throwable, @NotNull HttpServletRequest request) {
        return ExceptionInfo.builder().build().create(hyperlink, throwable, request);
    }

    /**
     * 自定义异常
     *
     * @param e       异常
     * @param request request
     * @return 异常结果 result
     * @since 1.0.0
     */
    @ExceptionHandler(value = {
        LowestException.class
    })
    public Result<?> handleLowestException(@NotNull LowestException e, @NotNull HttpServletRequest request) {
        log.warn("{} path: {}", e.getMessage(), request.getRequestURI());
        return this.buildResult(e, request);
    }

    /**
     * Handle basic exception
     *
     * @return the result
     * @since 1.0.0
     */
    @ExceptionHandler(value = {
        NoResourceFoundException.class
    })
    public Result<?> handleNoResourceFoundException() {
        return R.failed(BaseCodes.DATA_ERROR);
    }

    /**
     * 如果是正式环境, 将服务器异常转换为对用户友好信息, 不暴露具体异常信息, 而是通过日志输出
     * 非正式则输出正确异常信息
     *
     * @param e       异常
     * @param request request
     * @return 异常结果 result
     * @since 1.0.0
     */
    @ExceptionHandler(value = {
        NoHandlerFoundException.class,
        HttpMediaTypeNotSupportedException.class,
        HttpMediaTypeNotAcceptableException.class,
        MissingPathVariableException.class,
        MissingRequestHeaderException.class,
        MissingServletRequestParameterException.class,
        HttpMessageNotReadableException.class,
        HttpMessageNotWritableException.class,
        MethodArgumentTypeMismatchException.class,
        ServletRequestBindingException.class,
        ConversionNotSupportedException.class,
        MissingServletRequestPartException.class,
        AsyncRequestTimeoutException.class
    })
    @SuppressWarnings("all")
    public Result<?> handleServletException(@NotNull Exception e, HttpServletRequest request) {
        log.warn(e.getMessage());

        if (ConfigKit.isProd()) {
            log.error(e.getMessage(), e);
            // 当为生产环境,不适合把具体的异常信息展示给用户,比如 404.
            return R.failed(BaseCodes.SERVER_ERROR, BaseCodes.SERVER_ERROR.getMessage());
        }

        ExceptionInfo exceptionEntity = this.buildExceptionData(buildErrorLink(), e, request);

        try {
            String message = e.getMessage();
            if (e instanceof MissingServletRequestParameterException) {
                MissingServletRequestParameterException exception = (MissingServletRequestParameterException) e;
                message = exception.getParameterName()
                    + " ("
                    + exception.getParameterType()
                    + ")";
            }
            ServletCodeEnum servletExceptionEnum = EnumUtils.of(ServletCodeEnum.class,
                    servletCodeEnum -> e.getClass().getSimpleName()
                        .equals(servletCodeEnum.exceptionName))
                .orElse(ServletCodeEnum.INNER_ERROR);
            if (servletExceptionEnum == ServletCodeEnum.INNER_ERROR) {
                log.error(e.getMessage(), e);
            }
            final Result<Object> result = R.failed(servletExceptionEnum,
                StringUtils.format(servletExceptionEnum.getMessage(), message));
            result.setExtend(exceptionEntity);
            return result;
        } catch (IllegalArgumentException ignored) {
        }

        final Result<Object> failed = R.failed(BaseCodes.FAILURE, e.getMessage());
        failed.setExtend(exceptionEntity);
        return failed;
    }

    /**
     * 参数绑定异常
     *
     * @param e 异常
     * @return 异常结果 result
     * @since 1.0.0
     */
    @ExceptionHandler(value = {
        BindException.class,
    })
    public Result<?> handleBindException(@NotNull BindException e) {
        log.debug("exception from BindException");
        return this.wrapperBindingResult(e.getBindingResult());
    }

    /**
     * 包装绑定异常结果, 输出全部绑定异常信息
     *
     * @param bindingResult 绑定结果
     * @return 异常结果 result
     * @since 1.0.0
     */
    @NotNull
    private Result<Void> wrapperBindingResult(@NotNull BindingResult bindingResult) {
        StringBuilder warnMessage = new StringBuilder();
        String message = "";
        for (ObjectError error : bindingResult.getAllErrors()) {
            if (StringUtils.isBlank(message)) {
                message = bindingResult.getAllErrors().get(0).getDefaultMessage();
            }
            warnMessage.append(", ");
            if (error instanceof FieldError) {
                warnMessage.append(((FieldError) error).getField()).append(": ");
            }
            warnMessage.append(error.getDefaultMessage() == null ? "" : error.getDefaultMessage());

        }
        log.warn("参数绑定校验异常: {} params: {}", warnMessage.substring(2), bindingResult.getTarget());
        return R.failed(BaseCodes.PARAM_VERIFY_ERROR, message);
    }

    /**
     * 处理 @Validated (spring) 验证异常,将校验失败的所有异常组合成一条错误信息
     *
     * @param e 异常
     * @return 异常结果 result
     * @since 1.0.0
     */
    @ExceptionHandler(value = {
        MethodArgumentNotValidException.class
    })
    public Result<Void> handleValidException(@NotNull MethodArgumentNotValidException e) {
        log.debug("exception from MethodArgumentNotValidException");
        return this.wrapperBindingResult(e.getBindingResult());
    }

    /**
     * 处理 @Valid (javax.validation [api], hibernate-validator [impl]) 验证异常
     *
     * @param e the e
     * @return the result
     * @since 1.0.0
     */
    @ExceptionHandler(value = {
        ConstraintViolationException.class
    })
    public Result<Void> handleValidException(@NotNull ConstraintViolationException e) {
        log.warn("参数校验失败: [{}]", e.getMessage());
        return R.failed(BaseCodes.PARAM_VERIFY_ERROR, e.getMessage());
    }

    /**
     * Handle resource access exception
     *
     * @param e e
     * @return the result
     * @since 1.0.0
     */
    @ExceptionHandler(value = {
        RestClientException.class
    })
    public Result<Void> handleResourceAccessException(@NotNull RestClientException e) {
        log.error("Agent Service 不可用", e);
        return R.failed(BaseCodes.AGENT_DISABLE_EXCEPTION, BaseCodes.AGENT_DISABLE_EXCEPTION.getMessage());
    }

    /**
     * 未定义异常
     *
     * @param e       异常
     * @param request request
     * @return 异常结果 result
     * @since 1.0.0
     */
    @ExceptionHandler(value = {
        Exception.class
    })
    public Result<?> handleException(Exception e, HttpServletRequest request) {
        return this.handleError(e, request);
    }

    /**
     * 捕获 error 级异常
     *
     * @param e       the e
     * @param request request
     * @return the result
     * @since 1.0.0
     */
    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<?> handleError(Throwable e, HttpServletRequest request) {
        return lastHandle(e, request);
    }

    /**
     * 兜底处理
     *
     * @param e       e
     * @param request request
     * @return the result
     * @since 1.0.0
     */
    public static @NotNull Result<?> lastHandle(Throwable e, HttpServletRequest request) {
        String hyperlink = buildErrorLink();

        if (e instanceof ServiceInternalException exception) {
            if (exception.isRpc()) {
                hyperlink = hyperlink + "\n\t--> " + StringUtils.format(hyperLink,
                    exception.getEnv(),
                    exception.getTraceId());
            }
        }

        log.error(hyperlink, e);
        SpringContext.publishEvent(new ExceptionEvent(e));

        String message = BaseCodes.SERVER_ERROR.getMessage();
        if (RPC_ECEPTION.equals(e.getClass().getName())) {
            message = BaseCodes.RPC_ERROR.getMessage();
        }

        if (ConfigKit.isProd()) {
            // 当为生产环境,不适合把具体的异常信息展示给用户,比如数据库异常信息.
            return R.failed(BaseCodes.SERVER_ERROR, message);
        }

        Result<?> failed = R.failed(BaseCodes.SERVER_INNER_ERROR, e.getMessage());
        failed.setExtend(buildExceptionData(hyperlink, e, request));

        request.setAttribute(BasicConstant.REQUEST_EXCEPTION_INFO_ATTR, e);
        return failed;
    }

    /**
     * Build error link
     *
     * @return the string
     * @since 1.0.0
     */
    public static String buildErrorLink() {
        return StringUtils.format(hyperLink,
            ConfigKit.getEnv().getName(),
            Trace.context().get());
    }

    /**
     * Result
     *
     * @param e       e
     * @param req     req
     * @param message message
     * @return the result
     * @since 1.0.0
     */
    public static @NotNull Result<?> result(@NotNull Exception e, HttpServletRequest req, String message) {
        final Result<?> failed = R.failed(BaseCodes.FAILURE, message);
        if (ConfigKit.isProd()) {
            log.error(e.getMessage(), e);
            // 当为生产环境,不适合把具体的异常信息展示给用户,比如 404.
            return failed;
        }

        ExceptionInfo exceptionEntity = GlobalExceptionHandler.buildExceptionData(GlobalExceptionHandler.buildErrorLink(), e, req);
        failed.setExtend(exceptionEntity);
        return failed;
    }

}
