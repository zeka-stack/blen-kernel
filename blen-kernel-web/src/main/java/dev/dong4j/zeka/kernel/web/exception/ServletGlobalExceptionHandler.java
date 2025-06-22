package dev.dong4j.zeka.kernel.web.exception;

import dev.dong4j.zeka.kernel.common.exception.GlobalExceptionHandler;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
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
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE + 1000)
public class ServletGlobalExceptionHandler extends GlobalExceptionHandler {

}
