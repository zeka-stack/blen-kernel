package dev.dong4j.zeka.kernel.sentinel.config;

import dev.dong4j.zeka.kernel.common.constant.ConfigKey;
import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * Sentinel配置属性类
 * <p>
 * 负责加载和管理Sentinel相关的配置属性，主要用于配置Sentinel与Nacos的集成参数
 * 通过Spring Boot的@ConfigurationProperties注解自动绑定配置文件中的属性值
 * </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2019.12.21 23:41
 * @since 1.0.0
 */
@Data
@ConfigurationProperties(prefix = SentinelProperties.PREFIX)
public class SentinelProperties {
    /** 配置前缀 */
    public static final String PREFIX = ConfigKey.PREFIX + "sentinel";

    /** Nacos配置信息 */
    private NacosConfig nacos;

    /**
     * Nacos配置信息内部类
     * <p>
     * 负责管理Sentinel集成Nacos所需的配置参数
     * 目前主要包含Nacos服务器地址配置
     * </p>
     *
     * @author dong4j
     * @version 1.0.0
     * @email "mailto:dong4j@gmail.com"
     * @date 2019.12.21 23:44
     * @since 1.0.0
     */
    @Data
    public static class NacosConfig {
        /** Nacos服务器地址，默认为nacos.server:8848 */
        private String serverAddr = "nacos.server:8848";
    }
}
