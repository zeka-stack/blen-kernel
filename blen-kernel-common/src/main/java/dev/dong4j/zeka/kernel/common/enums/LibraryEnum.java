package dev.dong4j.zeka.kernel.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>Description: 启动成功后将实现指定的提示信息, 主要用于 starter 的自动装配类.</p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.27 11:28
 * @since 1.0.0
 */
@Getter
@AllArgsConstructor
public enum LibraryEnum {

    /** Rest library enum */
    REST("Rest", LibraryEnum.START_URL),
    /** Agent library enum */
    AGENT("Agent", "/agent/ping"),
    /** Dubbo library enum */
    DUBBO("Dubbo", ""),
    /** Druid library enum */
    DRUID("Druid", "/druid/"),
    /** Swagger rest default library enum */
    SWAGGER_REST_DEFAULT("Swagger(D)", "/swagger-ui.html"),
    /** Swagger rest bootstrap library enum */
    SWAGGER_REST_BOOTSTRAP("Swagger(B)", "/doc.html"),
    /** Swagger dubbo json */
    SWAGGER_DUBBO_JSON("Swagger(Dubbo API)", "/swagger-dubbo/api-docs"),
    /** Swagger dubbo library enum */
    SWAGGER_DUBBO("Swagger(Dubbo)", "/dubbo.html"),
    /** Swagger json library enum */
    SWAGGER_JSON("Swagger(API)", "/v2/api-docs");

    /** START_URL */
    public static final String START_URL = "/actuator/info";

    /** Name */
    private final String name;
    /** Uri */
    private final String uri;
}
