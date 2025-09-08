package dev.dong4j.zeka.kernel.common.util;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/**
 * <p>Description: 针对多网卡获取 IP 错误的问题, 使用此类可配置获取 IP 的方式 </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2022.02.11 13:22
 * @since 1.0.0
 */
@Data
class InetUtilsProperties {
    /** Properties utils */
    private final PropertiesUtils propertiesUtils = PropertiesUtils.getProperties();

    /** LOCALHOST_KEY */
    public static final String LOCALHOST_KEY = "localhost";
    /** LOCALHOST_VALUE */
    public static final String LOCALHOST_VALUE = "127.0.0.1";

    /** Default hostname */
    private String defaultHostname = LOCALHOST_KEY;
    /** Default ip address */
    private String defaultIpAddress = LOCALHOST_VALUE;
    /** Timeout seconds */
    private int timeoutSeconds;
    /** 是否只使用带有站点本地地址的接口, {@link InetAddress#isSiteLocalAddress()} */
    private boolean useOnlySiteLocalInterfaces;
    /** 将被忽略的网络接口的Java正则表达式列表. */
    private List<String> ignoredInterfaces = new ArrayList<>();
    /** 首选网络地址的Java正则表达式列表 */
    private List<String> preferredNetworks = new ArrayList<>();

    /**
     * Inet utils properties
     *
     * @since 1.0.0
     */
    InetUtilsProperties() {
        this.timeoutSeconds = this.propertiesUtils.getIntegerProperty("timeoutSeconds", 1);
        this.useOnlySiteLocalInterfaces = this.propertiesUtils.getBooleanProperty("useOnlySiteLocalInterfaces", false);

        String ignoredInterfacesStr = this.propertiesUtils.getStringProperty("ignoredInterfaces", "");
        if (StringUtils.isNotBlank(ignoredInterfacesStr)) {
            this.ignoredInterfaces = StringUtils.splitTrim(ignoredInterfacesStr, ",");
        }

        String preferredNetworksStr = this.propertiesUtils.getStringProperty("preferredNetworks", "");
        if (StringUtils.isNotBlank(preferredNetworksStr)) {
            this.preferredNetworks = StringUtils.splitTrim(preferredNetworksStr, ",");
        }
    }

}
