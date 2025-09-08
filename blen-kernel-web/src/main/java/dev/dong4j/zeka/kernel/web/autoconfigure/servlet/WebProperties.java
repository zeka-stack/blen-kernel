package dev.dong4j.zeka.kernel.web.autoconfigure.servlet;

import dev.dong4j.zeka.kernel.common.constant.ConfigKey;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Web相关配置属性类，管理Web层的各种配置参数
 * 包括全局缓存过滤器、异常过滤器、请求缓存忽略URL等配置
 * 通过配置文件统一管理Web组件的行为，提供灵活的开关控制
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2022.01.06 23:14
 * @since 1.0.0
 */
@Data
@ConfigurationProperties(prefix = WebProperties.PREFIX)
public class WebProperties {
    /** 配置前缀 */
    public static final String PREFIX = ConfigKey.PREFIX + "web";

    /** 启用全局缓存过滤器 */
    private boolean enableGlobalCacheFilter = Boolean.TRUE;
    /** 启用过滤器异常处理器 */
    private boolean enableExceptionFilter = Boolean.TRUE;
    /** 不需要缓存request的URL正则表达式 */
    private String ignoreCacheRequestUrl;

}
