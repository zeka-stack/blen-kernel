package dev.dong4j.zeka.kernel.web.filter;

import dev.dong4j.zeka.kernel.web.support.CacheRequestEnhanceWrapper;
import dev.dong4j.zeka.kernel.web.support.CacheResponseEnhanceWrapper;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p>Description: request 缓存过滤器, 优先级最高 </p>
 *
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.03 11:51
 * @since 1.0.0
 */
@Slf4j
public class ServletGlobalCacheFilter extends AbstractSkipFilter {

    /**
     * Servlet global cache filter
     *
     * @param skipUrl skip url
     * @since 2022.1.1
     */
    public ServletGlobalCacheFilter(String skipUrl) {
        super(skipUrl);
    }

    /**
     * Do filter internal *
     *
     * @param request  request
     * @param response response
     * @param chain    chain
     * @throws ServletException servlet exception
     * @throws IOException      io exception
     * @since 1.0.0
     */
    @Override
    public void doFilterInternal(@NotNull HttpServletRequest request,
                                 @NotNull HttpServletResponse response,
                                 @NotNull FilterChain chain) throws ServletException, IOException {
        CacheRequestEnhanceWrapper cacheRequest;
        CacheResponseEnhanceWrapper cacheResponse;
        // 缓存 request, 解决二次读取问题
        if (request instanceof CacheRequestEnhanceWrapper) {
            cacheRequest = (CacheRequestEnhanceWrapper) request;
        } else {
            cacheRequest = new CacheRequestEnhanceWrapper(new ContentCachingRequestWrapper(request));
        }
        // 缓存 response, 解决二次读取问题
        if (response instanceof CacheResponseEnhanceWrapper) {
            cacheResponse = (CacheResponseEnhanceWrapper) response;
        } else {
            cacheResponse = new CacheResponseEnhanceWrapper(new ContentCachingResponseWrapper(response));
        }
        chain.doFilter(cacheRequest, cacheResponse);
        // 在 filter 执行完成后, 将 response 拷贝回去, 避免二次读取时没有数据
        cacheResponse.copyBodyToResponse();
    }

}
