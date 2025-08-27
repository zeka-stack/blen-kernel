package dev.dong4j.zeka.kernel.web.exception;

import dev.dong4j.zeka.kernel.common.api.Result;
import dev.dong4j.zeka.kernel.common.exception.GlobalExceptionHandler;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * <p>Description: servlet 全局异常处理器 </p>
 * {@link org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler}
 * {@link org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver}
 *
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dong4j@gmail.com"
 * @date 2019.11.20 10:23
 * @since 1.0.0
 */
@Slf4j
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE + 1000)
public class ServletGlobalExceptionHandler extends GlobalExceptionHandler {


    /**
     * 处理非法参数异常
     *
     * @param req 请求：可以记录一些传参的内容。你可以自定义日志与向响应的信息，只是当前没用到。
     * @param e   异常对象。
     * @return 响应实体
     */
    @ExceptionHandler(value = IllegalStateException.class)
    public Result<?> bizExceptionHandler(HttpServletRequest req, IllegalStateException e) {
        log.error("接收了一个非法参数的异常", e);
        return result(e, req, "数据传输错误，导致非法参数异常，请联系管理员处理！");
    }

    /**
     * 处理空指针的异常
     *
     * @param req 请求：可以记录一些传参的内容。你可以自定义日志与向响应的信息，只是当前没用到。
     * @param e   异常对象。
     * @return 响应实体
     */
    @ExceptionHandler(value = NullPointerException.class)
    public Result<?> exceptionHandler(HttpServletRequest req, NullPointerException e) {
        log.error("接收了一个空指针的异常", e);
        return result(e, req, "空指针异常，请联系管理员处理！");
    }

    /**
     * 数字格式异常异常处理
     *
     * @param req 请求：可以记录一些传参的内容。你可以自定义日志与向响应的信息，只是当前没用到。
     * @param e   异常对象。
     * @return 响应实体
     */
    @ExceptionHandler(value = NumberFormatException.class)
    public Result<?> numberFormatException(HttpServletRequest req, NumberFormatException e) {
        log.error("接收了一个数字格式的异常", e);
        return result(e, req, "数字转换异常！请检查传参的数据类型！");
    }

    /**
     * 请求方法不支持异常
     *
     * @param req 请求：可以记录一些传参的内容。你可以自定义日志与向响应的信息，只是当前没用到。
     * @param e   异常对象。
     * @return 响应实体
     */
    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public Result<?> httpRequestMethodNotSupportedException(HttpServletRequest req, NumberFormatException e) {
        log.error("请求方式错误的异常：{}", e.getMessage());
        return result(e, req, "请求方式不对！请检查请求方式！");
    }

    /**
     * 类型转换异常处理
     *
     * @param req 请求：可以记录一些传参的内容。你可以自定义日志与向响应的信息，只是当前没用到。
     * @param e   异常对象。
     * @return 响应实体
     */
    @ExceptionHandler(value = ClassCastException.class)
    public Result<?> classCastException(HttpServletRequest req, NumberFormatException e) {
        log.error("类型转换异常", e);
        return result(e, req, "类型转换异常！请检查传参的数据类型！");
    }
}
