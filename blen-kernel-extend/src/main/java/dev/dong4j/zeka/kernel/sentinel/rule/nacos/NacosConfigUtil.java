package dev.dong4j.zeka.kernel.sentinel.rule.nacos;

import lombok.experimental.UtilityClass;

/**
 * Sentinel Nacos配置工具类
 * <p>
 * 提供Sentinel与Nacos集成时使用的各种配置常量，包括组ID和各种规则配置的数据ID后缀
 * 这些常量被用于在Nacos中存储和读取Sentinel的各类规则配置
 * </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2019.12.21 23:36
 * @since 1.0.0
 */
@UtilityClass
public final class NacosConfigUtil {

    /** Sentinel配置在Nacos中的组ID */
    static final String GROUP_ID = "SENTINEL_GROUP";

    /** 流量控制规则数据ID后缀 */
    public static final String FLOW_DATA_ID_POSTFIX = "-flow-rules";
    /** 网关流量控制规则数据ID后缀 */
    public static final String GATEWAY_FLOW_DATA_ID_POSTFIX = "-gateway-flow-rules";
    /** 授权规则数据ID后缀 */
    public static final String AUTHORITY_DATA_ID_POSTFIX = "-authority-rules";
    /** 熔断降级规则数据ID后缀 */
    public static final String DEGRADE_DATA_ID_POSTFIX = "-degrade-rules";
    /** 热点参数规则数据ID后缀 */
    public static final String PARAM_FLOW_DATA_ID_POSTFIX = "-param-rules";
    /** 系统保护规则数据ID后缀 */
    public static final String SYSTEM_FLOW_DATA_ID_POSTFIX = "-system-rules";
    /** 集群限流映射数据ID后缀 */
    public static final String CLUSTER_MAP_DATA_ID_POSTFIX = "-cluster-map";
    /** 客户端配置数据ID后缀 */
    public static final String CLIENT_CONFIG_DATA_ID_POSTFIX = "-cc-config";
    /** 服务器传输配置数据ID后缀 */
    public static final String SERVER_TRANSPORT_CONFIG_DATA_ID_POSTFIX = "-cs-transport-config";
    /** 服务器流控配置数据ID后缀 */
    public static final String SERVER_FLOW_CONFIG_DATA_ID_POSTFIX = "-cs-flow-config";
    /** 服务器命名空间集合数据ID后缀 */
    public static final String SERVER_NAMESPACE_SET_DATA_ID_POSTFIX = "-cs-namespace-set";

}
