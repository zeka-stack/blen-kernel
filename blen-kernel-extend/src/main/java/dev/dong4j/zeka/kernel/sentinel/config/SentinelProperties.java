package dev.dong4j.zeka.kernel.sentinel.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
* <p>Description:  </p>
 *
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dong4j@gmail.com"
 * @date 2019.12.21 23:41
 * @since 1.0.0
 */
@Data
@ConfigurationProperties(prefix = SentinelProperties.PREFIX)
public class SentinelProperties {
    /** PREFIX */
    public static final String PREFIX = ConfigKey.PREFIX + "sentinel";

    /** Nacos */
    private NacosConfig nacos;

    /**
         * <p>Description: </p>
     *
     * @author dong4j
     * @version 1.2.3
     * @email "mailto:dong4j@gmail.com"
     * @date 2019.12.21 23:44
     * @since 1.0.0
     */
    @Data
    public static class NacosConfig {
        /** Server addr */
        private String serverAddr = "nacos.server:8848";
    }
}
