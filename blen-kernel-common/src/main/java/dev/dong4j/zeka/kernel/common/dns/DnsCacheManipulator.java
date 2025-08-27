package dev.dong4j.zeka.kernel.common.dns;

import dev.dong4j.zeka.kernel.common.dns.internal.InetAddressCacheUtils;
import dev.dong4j.zeka.kernel.common.util.CollectionUtils;
import dev.dong4j.zeka.kernel.common.util.ConfigKit;
import dev.dong4j.zeka.kernel.common.util.DateUtils;
import dev.dong4j.zeka.kernel.common.util.HostUtils;
import dev.dong4j.zeka.kernel.common.util.Jsons;
import dev.dong4j.zeka.kernel.common.util.JustOnceLogger;
import dev.dong4j.zeka.kernel.common.util.StringUtils;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Pattern;
import javax.annotation.Nullable;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.io.Resource;

/**
 * <p>Description: DNS 工具类 </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.06.09 19:52
 * @since 1.5.0
 */
@Slf4j
@UtilityClass
public final class DnsCacheManipulator {
    /** NEVER_EXPIRATION */
    private static final long NEVER_EXPIRATION = DateUtils.plusYears(new Date(), 100).getTime();
    /** DNS_CONFIG_FILENAME */
    public static final String DNS_CONFIG_FILENAME = "dns.config.filename";

    /**
     * Sets dns cache *
     *
     * @param host host
     * @param ips  ips
     * @since 1.5.0
     */
    public static void setDnsCache(String host, String... ips) {
        try {
            InetAddressCacheUtils.setInetAddressCache(host, ips, NEVER_EXPIRATION);
        } catch (Exception e) {
            String message = String.format("Fail to setDnsCache for host %s ip %s, cause: %s",
                host, Arrays.toString(ips), e);
            throw new DnsCacheManipulatorException(message, e);
        }
    }

    /**
     * Sets dns cache *
     *
     * @param expireMillis expire millis
     * @param host         host
     * @param ips          ips
     * @since 1.5.0
     */
    public static void setDnsCache(long expireMillis, String host, String... ips) {
        try {
            InetAddressCacheUtils.setInetAddressCache(host, ips, System.currentTimeMillis() + expireMillis);
        } catch (Exception e) {
            String message = String.format("Fail to setDnsCache for host %s ip %s expireMillis %s, cause: %s",
                host, Arrays.toString(ips), expireMillis, e);
            throw new DnsCacheManipulatorException(message, e);
        }
    }

    /** COMMA_SEPARATOR */
    private static final Pattern COMMA_SEPARATOR = Pattern.compile("\\s*,\\s*");

    /**
     * Sets dns cache *
     *
     * @param properties properties
     * @since 1.5.0
     */
    public static void setDnsCache(@NotNull Properties properties) {
        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            String host = (String) entry.getKey();
            String ipList = (String) entry.getValue();

            ipList = ipList.trim();
            if (ipList.isEmpty()) {
                continue;
            }

            String[] ips = COMMA_SEPARATOR.split(ipList);
            setDnsCache(host, ips);
        }
    }

    /**
     * Load dns cache config
     *
     * @since 1.5.0
     */
    public static void loadDnsCacheConfig() {
        String dcmConfigFileName = System.getProperty(DNS_CONFIG_FILENAME, "dns-cache.properties");
        loadDnsCacheConfig(dcmConfigFileName);
    }

    /**
     * Load dns cache config
     *
     * @param propertiesFileName properties file name
     * @since 1.5.0
     */
    public static void loadDnsCacheConfig(String propertiesFileName) {
        InputStream inputStream;
        try {
            // 先加载本地文件, 开发环境在 target 目录下, 服务器部署环境在 config 目录下
            Resource resource = ConfigKit.getResource(propertiesFileName);
            inputStream = resource.getInputStream();
        } catch (Exception e) {
            // 从 jar 中加载
            inputStream = DnsCacheManipulator.class.getClassLoader().getResourceAsStream(propertiesFileName);
        }

        if (inputStream == null) {
            throw new DnsCacheManipulatorException("Fail to find " + propertiesFileName + " on classpath!");
        }

        try {
            Properties merged = merge(inputStream);
            if (!merged.isEmpty()) {
                setDnsCache(merged);
            }
        } catch (Exception e) {
            String message = String.format("Fail to loadDnsCacheConfig from %s, cause: %s",
                propertiesFileName, e);
            throw new DnsCacheManipulatorException(message, e);
        }
    }

    /**
     * 如果 hosts 文件中已存在, 则优先使用 hosts 的记录
     *
     * @param inputStream input stream
     * @throws IOException io exception
     * @since 1.5.0
     */
    private static @NotNull Properties merge(InputStream inputStream) throws IOException {
        Properties properties = new Properties();
        properties.load(inputStream);
        inputStream.close();

        // 需要被加载的配置
        Properties cacheProperties = new Properties();
        // 与 hosts 相同的配置
        Map<String, Set<String>> existsRecords = new HashMap<>(8);
        Set<String> allHosts = HostUtils.read();
        properties.forEach((k, v) -> {
            Set<String> ips = HostUtils.getIps(allHosts, String.valueOf(k));
            if (CollectionUtils.isNotEmpty(ips)) {
                existsRecords.put(String.valueOf(k), ips);
            } else {
                cacheProperties.put(k, v);
            }
        });

        if (CollectionUtils.isNotEmpty(existsRecords)) {
            JustOnceLogger.printOnce(DnsCacheManipulator.class.getName(),
                StringUtils.format("hosts 已存在相同记录, 优先使用 hosts 配置: \n{}\n原始配置: \n{}\n待加载配置: \n{}",
                    Jsons.toJson(existsRecords, true),
                    Jsons.toJson(properties, true),
                    Jsons.toJson(cacheProperties, true)));
        }

        return cacheProperties;
    }

    /**
     * Gets dns cache *
     *
     * @param host host
     * @return the dns cache
     * @since 1.5.0
     */
    @Nullable
    public static DnsCacheEntry getDnsCache(String host) {
        try {
            return InetAddressCacheUtils.getInetAddressCache(host);
        } catch (Exception e) {
            throw new DnsCacheManipulatorException("Fail to getDnsCache [DNS Cache 不支持 JDK8 以上的版本], cause: " + e, e);
        }
    }

    /**
     * List dns cache
     *
     * @return the list
     * @since 1.5.0
     */
    public static List<DnsCacheEntry> listDnsCache() {
        try {
            return InetAddressCacheUtils.listInetAddressCache().getCache();
        } catch (Exception e) {
            throw new DnsCacheManipulatorException("Fail to listDnsCache, cause: " + e, e);
        }
    }

    /**
     * Gets whole dns cache *
     *
     * @return the whole dns cache
     * @since 1.5.0
     */
    public static @NotNull DnsCache getWholeDnsCache() {
        try {
            return InetAddressCacheUtils.listInetAddressCache();
        } catch (Exception e) {
            throw new DnsCacheManipulatorException("Fail to getWholeDnsCache, cause: " + e, e);
        }
    }

    /**
     * Remove dns cache
     *
     * @param host host
     * @since 1.5.0
     */
    public static void removeDnsCache(String host) {
        try {
            InetAddressCacheUtils.removeInetAddressCache(host);
        } catch (Exception e) {
            String message = String.format("Fail to removeDnsCache for host %s, cause: %s", host, e);
            throw new DnsCacheManipulatorException(message, e);
        }
    }

    /**
     * Clear dns cache
     *
     * @since 1.5.0
     */
    public static void clearDnsCache() {
        try {
            InetAddressCacheUtils.clearInetAddressCache();
        } catch (Exception e) {
            throw new DnsCacheManipulatorException("Fail to clearDnsCache, cause: " + e, e);
        }
    }

    /**
     * Gets dns cache policy *
     *
     * @return the dns cache policy
     * @since 1.5.0
     */
    public static int getDnsCachePolicy() {
        try {
            return InetAddressCacheUtils.getDnsCachePolicy();
        } catch (Exception e) {
            throw new DnsCacheManipulatorException("Fail to getDnsCachePolicy, cause: " + e, e);
        }
    }

    /**
     * Sets dns cache policy *
     *
     * @param cacheSeconds cache seconds
     * @since 1.5.0
     */
    public static void setDnsCachePolicy(int cacheSeconds) {
        try {
            InetAddressCacheUtils.setDnsCachePolicy(cacheSeconds);
        } catch (Exception e) {
            throw new DnsCacheManipulatorException("Fail to setDnsCachePolicy, cause: " + e, e);
        }
    }

    /**
     * Gets dns negative cache policy *
     *
     * @return the dns negative cache policy
     * @since 1.5.0
     */
    public static int getDnsNegativeCachePolicy() {
        try {
            return InetAddressCacheUtils.getDnsNegativeCachePolicy();
        } catch (Exception e) {
            throw new DnsCacheManipulatorException("Fail to getDnsNegativeCachePolicy, cause: " + e, e);
        }
    }

    /**
     * Sets dns negative cache policy *
     *
     * @param negativeCacheSeconds negative cache seconds
     * @since 1.5.0
     */
    public static void setDnsNegativeCachePolicy(int negativeCacheSeconds) {
        try {
            InetAddressCacheUtils.setDnsNegativeCachePolicy(negativeCacheSeconds);
        } catch (Exception e) {
            throw new DnsCacheManipulatorException("Fail to setDnsNegativeCachePolicy, cause: " + e, e);
        }
    }
}
