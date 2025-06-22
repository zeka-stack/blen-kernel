package dev.dong4j.zeka.kernel.web.filter;

import dev.dong4j.zeka.kernel.common.constant.BasicConstant;
import dev.dong4j.zeka.kernel.web.handler.ServletErrorController;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.autoconfigure.web.ServerProperties;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * <p>Description: 由于在 Filter 中抛出的异常无法被 controller 处理, 为了将异常结果返回到 controller 层, 这里将异常信息保存到 Request,
 * 然后在 {@link ServletErrorController} 处理 Filter 异常
 *
 * </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2019.12.02 01:31
 * @since 1.0.0
 */
@Slf4j
public class ExceptionFilter implements Filter {

    /** Server properties */
    private final ServerProperties serverProperties;

    /**
     * Exception filter
     *
     * @param serverProperties server properties
     * @since 1.0.0
     */
    @Contract(pure = true)
    public ExceptionFilter(ServerProperties serverProperties) {
        this.serverProperties = serverProperties;
    }

    /**
     * Do filter *
     *
     * @param request  request
     * @param response response
     * @param chain    chain
     * @throws IOException      io exception
     * @throws ServletException servlet exception
     * @see ServletErrorController
     * @since 1.0.0
     */
    @Override
    public void doFilter(@NotNull ServletRequest request,
                         ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        // 是否已经放有异常栈, 避免循环
        boolean isRethrow = !Objects.isNull(request.getAttribute(BasicConstant.REQUEST_EXCEPTION_INFO_ATTR));
        if (isRethrow) {
            chain.doFilter(request, response);
            return;
        }
        try {
            chain.doFilter(request, response);
        } catch (Exception e) {
            // 发生异常,保存异常栈
            request.setAttribute(BasicConstant.REQUEST_EXCEPTION_INFO_ATTR, e);
            // 转发到 /error , 会由 ServletErrorController 抛出异常, 最后转交给全局异常处理器处理
            request.getRequestDispatcher(this.serverProperties.getError().getPath()).forward(request, response);
        }
    }

}
