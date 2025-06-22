package dev.dong4j.zeka.kernel.sentinel.rule.nacos;

import lombok.experimental.UtilityClass;

/**
* <p>Description: </p>
 *
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dong4j@gmail.com"
 * @date 2019.12.21 23:36
 * @since 1.0.0
 */
@UtilityClass
public final class NacosConfigUtil {

    /** GROUP_ID */
    static final String GROUP_ID = "SENTINEL_GROUP";

    /** FLOW_DATA_ID_POSTFIX */
    public static final String FLOW_DATA_ID_POSTFIX = "-flow-rules";
    /** GATEWAY_FLOW_DATA_ID_POSTFIX */
    public static final String GATEWAY_FLOW_DATA_ID_POSTFIX = "-gateway-flow-rules";
    /** AUTHORITY_DATA_ID_POSTFIX */
    public static final String AUTHORITY_DATA_ID_POSTFIX = "-authority-rules";
    /** DEGRADE_DATA_ID_POSTFIX */
    public static final String DEGRADE_DATA_ID_POSTFIX = "-degrade-rules";
    /** PARAM_FLOW_DATA_ID_POSTFIX */
    public static final String PARAM_FLOW_DATA_ID_POSTFIX = "-param-rules";
    /** SYSTEM_FLOW_DATA_ID_POSTFIX */
    public static final String SYSTEM_FLOW_DATA_ID_POSTFIX = "-system-rules";
    /** CLUSTER_MAP_DATA_ID_POSTFIX */
    public static final String CLUSTER_MAP_DATA_ID_POSTFIX = "-cluster-map";
    /** CLIENT_CONFIG_DATA_ID_POSTFIX */
    public static final String CLIENT_CONFIG_DATA_ID_POSTFIX = "-cc-config";
    /** SERVER_TRANSPORT_CONFIG_DATA_ID_POSTFIX */
    public static final String SERVER_TRANSPORT_CONFIG_DATA_ID_POSTFIX = "-cs-transport-config";
    /** SERVER_FLOW_CONFIG_DATA_ID_POSTFIX */
    public static final String SERVER_FLOW_CONFIG_DATA_ID_POSTFIX = "-cs-flow-config";
    /** SERVER_NAMESPACE_SET_DATA_ID_POSTFIX */
    public static final String SERVER_NAMESPACE_SET_DATA_ID_POSTFIX = "-cs-namespace-set";

}
