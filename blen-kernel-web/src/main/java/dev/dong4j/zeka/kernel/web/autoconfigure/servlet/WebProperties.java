package dev.dong4j.zeka.kernel.web.autoconfigure.servlet;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2022.01.06 23:14
 * @since 2022.1.1
 */
@SuppressWarnings("ConfigurationProperties")
@Data
@ConfigurationProperties(prefix = WebProperties.PREFIX)
public class WebProperties {
    /** PREFIX */
    public static final String PREFIX = "zeka-stack.web";

    /** Enable global cache filter */
    private boolean enableGlobalCacheFilter = Boolean.TRUE;
    /** filter 异常处理器 */
    private boolean enableExceptionFilter = Boolean.TRUE;
    /** 不需要缓存 request 的 url (正则) */
    private String ignoreCacheRequestUrl;

}
