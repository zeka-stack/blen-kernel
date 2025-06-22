package dev.dong4j.zeka.kernel.web.support;

import dev.dong4j.zeka.kernel.common.exception.BaseException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dong4j@gmail.com"
 * @date 2019.18.18 01:18
 * @since 1.0.0
 */
@Slf4j
public class CacheResponseEnhanceWrapper extends HttpServletResponseWrapper {
    /** Response */
    @Getter
    private final ContentCachingResponseWrapper response;

    /**
     * Instantiates a new Response logging wrapper.
     *
     * @param response response from which we want to extract stream data
     * @since 1.0.0
     */
    public CacheResponseEnhanceWrapper(ContentCachingResponseWrapper response) {
        super(response);
        this.response = response;
    }

    /**
     * Get content byte [ ]
     *
     * @return the byte [ ]
     * @since 1.0.0
     */
    public byte[] getContent() {
        return this.response.getContentAsByteArray();
    }

    /**
     * Gets output stream *
     *
     * @return the output stream
     * @throws IOException io exception
     * @since 1.0.0
     */
    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return this.response.getOutputStream();
    }

    /**
     * Copy body to response
     *
     * @since 1.0.0
     */
    public void copyBodyToResponse() {
        try {
            this.response.copyBodyToResponse();
        } catch (IOException e) {
            throw new BaseException("缓存 Response 失败", e);
        }
    }
}
