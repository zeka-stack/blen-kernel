package dev.dong4j.zeka.kernel.web.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.dong4j.zeka.kernel.common.api.Result;
import dev.dong4j.zeka.kernel.common.constant.BasicConstant;
import dev.dong4j.zeka.kernel.common.exception.LowestException;
import dev.dong4j.zeka.kernel.common.util.Jsons;
import dev.dong4j.zeka.kernel.web.exception.ServletGlobalExceptionHandler;
import io.swagger.annotations.Api;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

/**
 * <p>Description: 更改 html 请求异常为 ajax </p>
 *
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dong4j@gmail.com"
 * @date 2019.12.02 01:40
 * @since 1.0.0
 */
@Slf4j
@Api(value = "Filter 异常处理")
public class ServletErrorController extends BasicErrorController {
    /** Object mapper */
    @Resource
    private ObjectMapper objectMapper;
    /** Global exception handler */
    private final ServletGlobalExceptionHandler globalExceptionHandler;

    /**
     * Servlet error controller
     *
     * @param errorAttributes error attributes
     * @param errorProperties error properties
     * @since 1.0.0
     */
    public ServletErrorController(ErrorAttributes errorAttributes,
                                  ErrorProperties errorProperties,
                                  ServletGlobalExceptionHandler servletGlobalExceptionHandler) {
        super(errorAttributes, errorProperties);
        globalExceptionHandler = servletGlobalExceptionHandler;
    }


    /**
     * Error response entity
     *
     * @param request request
     * @return the response entity
     * @since 1.0.0
     */
    @Override
    @RequestMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
        final Exception exception = this.checkException(request);
        final Result<?> result = globalExceptionHandler.handleError(exception, request);
        return new ResponseEntity<>(Jsons.toMap(result, String.class, Object.class), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 统一将  MediaType.TEXT_HTML_VALUE 转换成 json 返回
     *
     * @param request  request
     * @param response response
     * @return the model and view
     * @since 1.0.0
     */
    @Override
    public ModelAndView errorHtml(HttpServletRequest request, @NotNull HttpServletResponse response) {
        final Exception exception = this.checkException(request);
        final Result<?> result = globalExceptionHandler.handleError(exception, request);
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        MappingJackson2JsonView view = new MappingJackson2JsonView();
        view.setObjectMapper(this.objectMapper);
        view.setContentType(MediaType.APPLICATION_JSON_VALUE);
        return new ModelAndView(view, Jsons.toMap(result, String.class, Object.class));
    }

    /**
     * Check exception
     *
     * @param request request
     * @since 1.7.0
     */
    private Exception checkException(HttpServletRequest request) {
        Exception exception = (Exception) request.getAttribute(BasicConstant.REQUEST_EXCEPTION_INFO_ATTR);
        if (exception instanceof LowestException lowestException) {
            throw lowestException;
        } else {
            log.error("Filter 异常兜底处理, 请在业务系统中处理自定义 Filter 异常", exception);
            return exception;
        }
    }

    /**
     * Process body map
     *
     * @param request request
     * @param body    body
     * @return the map
     * @since 1.0.0
     */
    @Contract("_, _ -> new")
    private ResponseData processBody(@NotNull HttpServletRequest request, Map<String, Object> body) {
        return ResponseData.builder().body(body).status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    /**
     * <p>Description: </p>
     *
     * @author dong4j
     * @version 1.2.3
     * @email "mailto:dong4j@gmail.com"
     * @date 2019.12.02 02:37
     * @since 1.0.0
     */
    @Data
    @Builder
    private static class ResponseData {
        /** Body */
        private Map<String, Object> body;
        /** Status */
        private HttpStatus status;
    }

}
