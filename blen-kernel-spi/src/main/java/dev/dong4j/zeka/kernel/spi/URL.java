package dev.dong4j.zeka.kernel.spi;

import dev.dong4j.zeka.kernel.spi.config.Configuration;
import dev.dong4j.zeka.kernel.spi.config.InmemoryConfiguration;
import dev.dong4j.zeka.kernel.spi.constants.RemotingConstants;
import dev.dong4j.zeka.kernel.spi.utils.CollectionUtils;
import dev.dong4j.zeka.kernel.spi.utils.NetUtils;
import dev.dong4j.zeka.kernel.spi.utils.SpiStringUtils;
import java.io.Serial;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.lang3.ArrayUtils;

import static dev.dong4j.zeka.kernel.spi.constants.CommonConstants.ANYHOST_KEY;
import static dev.dong4j.zeka.kernel.spi.constants.CommonConstants.ANYHOST_VALUE;
import static dev.dong4j.zeka.kernel.spi.constants.CommonConstants.COMMA_SPLIT_PATTERN;
import static dev.dong4j.zeka.kernel.spi.constants.CommonConstants.DEFAULT_KEY_PREFIX;
import static dev.dong4j.zeka.kernel.spi.constants.CommonConstants.GROUP_KEY;
import static dev.dong4j.zeka.kernel.spi.constants.CommonConstants.HOST_KEY;
import static dev.dong4j.zeka.kernel.spi.constants.CommonConstants.INTERFACE_KEY;
import static dev.dong4j.zeka.kernel.spi.constants.CommonConstants.LOCALHOST_KEY;
import static dev.dong4j.zeka.kernel.spi.constants.CommonConstants.PASSWORD_KEY;
import static dev.dong4j.zeka.kernel.spi.constants.CommonConstants.PATH_KEY;
import static dev.dong4j.zeka.kernel.spi.constants.CommonConstants.PORT_KEY;
import static dev.dong4j.zeka.kernel.spi.constants.CommonConstants.PROTOCOL_KEY;
import static dev.dong4j.zeka.kernel.spi.constants.CommonConstants.USERNAME_KEY;
import static dev.dong4j.zeka.kernel.spi.constants.CommonConstants.VERSION_KEY;

/**
 * SPI框架中的URL地址封装类，提供统一的网络地址表示和操作功能
 * <p>
 * 该类封装了完整的URL结构，包括协议、主机、端口、路径和参数等信息
 * 支持URL的解析、构建、参数操作和缓存机制，广泛用于微服务通信和服务发现
 * <p>
 * 主要功能：
 * - URL字符串的解析和构建
 * - 支持多种数据类型的参数获取
 * - 提供缓存机制优化性能
 * - 支持备用地址和集群配置
 * - 提供不可变对象的线程安全操作
 * <p>
 * 使用示例：
 * {@code URL url = URL.valueOf("dubbo://localhost:20880/service?version=1.0.0");}
 * {@code String version = url.getParameter("version", "1.0.0");}
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2021.02.26 17:47
 * @since 1.0.0
 */
@SuppressWarnings("all")
public class URL implements Serializable {

    /** 序列化版本号 */
    @Serial
    private static final long serialVersionUID = -1985165475234910535L;

    /** 协议名称（如http、dubbo等） */
    private final String protocol;

    /** 用户名（用于认证） */
    private final String username;

    /** 密码（用于认证） */
    private final String password;

    /** 主机地址，默认为注册中心地址 */
    // by default, host to registry
    private final String host;

    /** 端口号，默认为注册中心端口 */
    private final int port;

    /** 路径信息 */
    private final String path;

    /** URL参数集合 */
    private final Map<String, String> parameters;

    // ==== 缓存字段 ====

    /** 数值参数缓存 */
    private volatile transient Map<String, Number> numbers;

    /** URL参数缓存 */
    private volatile transient Map<String, URL> urls;

    /** IP地址缓存 */
    private volatile transient String ip;

    /** 完整URL字符串缓存 */
    private volatile transient String full;

    /** 身份标识缓存 */
    private volatile transient String identity;

    /** 参数字符串缓存 */
    private volatile transient String parameter;

    /** URL字符串缓存 */
    private volatile transient String string;

    /**
     * Url
     *
     * @since 1.0.0
     */
    protected URL() {
        this.protocol = null;
        this.username = null;
        this.password = null;
        this.host = null;
        this.port = 0;
        this.path = null;
        this.parameters = null;
    }

    /**
     * Url
     *
     * @param protocol protocol
     * @param host     host
     * @param port     port
     * @since 1.0.0
     */
    public URL(String protocol, String host, int port) {
        this(protocol, null, null, host, port, null, (Map<String, String>) null);
    }

    /**
     * Url
     *
     * @param protocol protocol
     * @param host     host
     * @param port     port
     * @param pairs    pairs
     * @since 1.0.0
     */
    public URL(String protocol, String host, int port, String[] pairs) { // varargs ... conflict with the following path argument, use
        // array instead.
        this(protocol, null, null, host, port, null, CollectionUtils.toStringMap(pairs));
    }

    /**
     * Url
     *
     * @param protocol   protocol
     * @param host       host
     * @param port       port
     * @param parameters parameters
     * @since 1.0.0
     */
    public URL(String protocol, String host, int port, Map<String, String> parameters) {
        this(protocol, null, null, host, port, null, parameters);
    }

    /**
     * Url
     *
     * @param protocol protocol
     * @param host     host
     * @param port     port
     * @param path     path
     * @since 1.0.0
     */
    public URL(String protocol, String host, int port, String path) {
        this(protocol, null, null, host, port, path, (Map<String, String>) null);
    }

    /**
     * Url
     *
     * @param protocol protocol
     * @param host     host
     * @param port     port
     * @param path     path
     * @param pairs    pairs
     * @since 1.0.0
     */
    public URL(String protocol, String host, int port, String path, String... pairs) {
        this(protocol, null, null, host, port, path, CollectionUtils.toStringMap(pairs));
    }

    /**
     * Url
     *
     * @param protocol   protocol
     * @param host       host
     * @param port       port
     * @param path       path
     * @param parameters parameters
     * @since 1.0.0
     */
    public URL(String protocol, String host, int port, String path, Map<String, String> parameters) {
        this(protocol, null, null, host, port, path, parameters);
    }

    /**
     * Url
     *
     * @param protocol protocol
     * @param username username
     * @param password password
     * @param host     host
     * @param port     port
     * @param path     path
     * @since 1.0.0
     */
    public URL(String protocol, String username, String password, String host, int port, String path) {
        this(protocol, username, password, host, port, path, (Map<String, String>) null);
    }

    /**
     * Url
     *
     * @param protocol protocol
     * @param username username
     * @param password password
     * @param host     host
     * @param port     port
     * @param path     path
     * @param pairs    pairs
     * @since 1.0.0
     */
    public URL(String protocol, String username, String password, String host, int port, String path, String... pairs) {
        this(protocol, username, password, host, port, path, CollectionUtils.toStringMap(pairs));
    }

    /**
     * Url
     *
     * @param protocol   protocol
     * @param username   username
     * @param password   password
     * @param host       host
     * @param port       port
     * @param path       path
     * @param parameters parameters
     * @since 1.0.0
     */
    public URL(String protocol, String username, String password, String host, int port, String path, Map<String, String> parameters) {
        if (SpiStringUtils.isEmpty(username)
            && SpiStringUtils.isNotEmpty(password)) {
            throw new IllegalArgumentException("Invalid url, password without username!");
        }
        this.protocol = protocol;
        this.username = username;
        this.password = password;
        this.host = host;
        this.port = (port < 0 ? 0 : port);
        // trim the beginning "/"
        while (path != null && path.startsWith("/")) {
            path = path.substring(1);
        }
        this.path = path;
        if (parameters == null) {
            parameters = Collections.emptyMap();
        } else {
            parameters = new HashMap<>(parameters);
        }
        this.parameters = Collections.unmodifiableMap(parameters);
    }

    /**
     * Value of
     *
     * @param url url
     * @return the url
     * @since 1.0.0
     */
    public static URL valueOf(String url) {
        if (url == null || (url = url.trim()).length() == 0) {
            throw new IllegalArgumentException("url == null");
        }
        String protocol = null;
        String username = null;
        String password = null;
        String host = null;
        int port = 0;
        String path = null;
        Map<String, String> parameters = null;
        int i = url.indexOf("?"); // separator between body and parameters
        if (i >= 0) {
            String[] parts = url.substring(i + 1).split("&");
            parameters = new HashMap<>(16);
            for (String part : parts) {
                part = part.trim();
                if (part.length() > 0) {
                    int j = part.indexOf('=');
                    if (j >= 0) {
                        parameters.put(part.substring(0, j), part.substring(j + 1));
                    } else {
                        parameters.put(part, part);
                    }
                }
            }
            url = url.substring(0, i);
        }
        i = url.indexOf("://");
        if (i >= 0) {
            if (i == 0) {
                throw new IllegalStateException("url missing protocol: \"" + url + "\"");
            }
            protocol = url.substring(0, i);
            url = url.substring(i + 3);
        } else {
            // case: file:/path/to/file.txt
            i = url.indexOf(":/");
            if (i >= 0) {
                if (i == 0) {
                    throw new IllegalStateException("url missing protocol: \"" + url + "\"");
                }
                protocol = url.substring(0, i);
                url = url.substring(i + 1);
            }
        }

        i = url.indexOf("/");
        if (i >= 0) {
            path = url.substring(i + 1);
            url = url.substring(0, i);
        }
        i = url.lastIndexOf("@");
        if (i >= 0) {
            username = url.substring(0, i);
            int j = username.indexOf(":");
            if (j >= 0) {
                password = username.substring(j + 1);
                username = username.substring(0, j);
            }
            url = url.substring(i + 1);
        }
        i = url.lastIndexOf(":");
        if (i >= 0 && i < url.length() - 1) {
            if (url.lastIndexOf("%") > i) {
                // ipv6 address with scope id
                // e.g. fe80:0:0:0:894:aeec:f37d:23e1%en0
                // see https://howdoesinternetwork.com/2013/ipv6-zone-id
                // ignore
            } else {
                port = Integer.parseInt(url.substring(i + 1));
                url = url.substring(0, i);
            }
        }
        if (url.length() > 0) {
            host = url;
        }
        return new URL(protocol, username, password, host, port, path, parameters);
    }

    /**
     * Value of
     *
     * @param url           url
     * @param reserveParams reserve params
     * @return the url
     * @since 1.0.0
     */
    public static URL valueOf(String url, String... reserveParams) {
        URL result = valueOf(url);
        if (reserveParams == null || reserveParams.length == 0) {
            return result;
        }
        Map<String, String> newMap = new HashMap<>(reserveParams.length);
        Map<String, String> oldMap = result.getParameters();
        for (String reserveParam : reserveParams) {
            String tmp = oldMap.get(reserveParam);
            if (SpiStringUtils.isNotEmpty(tmp)) {
                newMap.put(reserveParam, tmp);
            }
        }
        return result.clearParameters().addParameters(newMap);
    }

    /**
     * Value of
     *
     * @param url                 url
     * @param reserveParams       reserve params
     * @param reserveParamPrefixs reserve param prefixs
     * @return the url
     * @since 1.0.0
     */
    public static URL valueOf(URL url, String[] reserveParams, String[] reserveParamPrefixs) {
        Map<String, String> newMap = new HashMap<>(16);
        Map<String, String> oldMap = url.getParameters();
        if (reserveParamPrefixs != null && reserveParamPrefixs.length != 0) {
            for (Map.Entry<String, String> entry : oldMap.entrySet()) {
                for (String reserveParamPrefix : reserveParamPrefixs) {
                    if (entry.getKey().startsWith(reserveParamPrefix) && SpiStringUtils.isNotEmpty(entry.getValue())) {
                        newMap.put(entry.getKey(), entry.getValue());
                    }
                }
            }
        }

        if (reserveParams != null) {
            for (String reserveParam : reserveParams) {
                String tmp = oldMap.get(reserveParam);
                if (SpiStringUtils.isNotEmpty(tmp)) {
                    newMap.put(reserveParam, tmp);
                }
            }
        }
        return newMap.isEmpty() ? new URL(url.getProtocol(), url.getUsername(), url.getPassword(), url.getHost(), url.getPort(),
            url.getPath())
            : new URL(url.getProtocol(), url.getUsername(), url.getPassword(), url.getHost(), url.getPort(),
            url.getPath(), newMap);
    }

    /**
     * Encode
     *
     * @param value value
     * @return the string
     * @since 1.0.0
     */
    public static String encode(String value) {
        if (SpiStringUtils.isEmpty(value)) {
            return "";
        }
        try {
            return URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * Decode
     *
     * @param value value
     * @return the string
     * @since 1.0.0
     */
    public static String decode(String value) {
        if (SpiStringUtils.isEmpty(value)) {
            return "";
        }
        try {
            return URLDecoder.decode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * Gets protocol *
     *
     * @return the protocol
     * @since 1.0.0
     */
    public String getProtocol() {
        return protocol;
    }

    /**
     * Sets protocol *
     *
     * @param protocol protocol
     * @return the protocol
     * @since 1.0.0
     */
    public URL setProtocol(String protocol) {
        return new URL(protocol, username, password, host, port, path, getParameters());
    }

    /**
     * Gets username *
     *
     * @return the username
     * @since 1.0.0
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets username *
     *
     * @param username username
     * @return the username
     * @since 1.0.0
     */
    public URL setUsername(String username) {
        return new URL(protocol, username, password, host, port, path, getParameters());
    }

    /**
     * Gets password *
     *
     * @return the password
     * @since 1.0.0
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets password *
     *
     * @param password password
     * @return the password
     * @since 1.0.0
     */
    public URL setPassword(String password) {
        return new URL(protocol, username, password, host, port, path, getParameters());
    }

    /**
     * Gets authority *
     *
     * @return the authority
     * @since 1.0.0
     */
    public String getAuthority() {
        if (SpiStringUtils.isEmpty(username)
            && SpiStringUtils.isEmpty(password)) {
            return null;
        }
        return (username == null ? "" : username)
            + ":" + (password == null ? "" : password);
    }

    /**
     * Gets host *
     *
     * @return the host
     * @since 1.0.0
     */
    public String getHost() {
        return host;
    }

    /**
     * Sets host *
     *
     * @param host host
     * @return the host
     * @since 1.0.0
     */
    public URL setHost(String host) {
        return new URL(protocol, username, password, host, port, path, getParameters());
    }

    /**
     * Gets ip *
     *
     * @return the ip
     * @since 1.0.0
     */
    public String getIp() {
        if (ip == null) {
            ip = NetUtils.getIpByHost(host);
        }
        return ip;
    }

    /**
     * Gets port *
     *
     * @return the port
     * @since 1.0.0
     */
    public int getPort() {
        return port;
    }

    /**
     * Sets port *
     *
     * @param port port
     * @return the port
     * @since 1.0.0
     */
    public URL setPort(int port) {
        return new URL(protocol, username, password, host, port, path, getParameters());
    }

    /**
     * Gets port *
     *
     * @param defaultPort default port
     * @return the port
     * @since 1.0.0
     */
    public int getPort(int defaultPort) {
        return port <= 0 ? defaultPort : port;
    }

    /**
     * Gets address *
     *
     * @return the address
     * @since 1.0.0
     */
    public String getAddress() {
        return port <= 0 ? host : host + ":" + port;
    }

    /**
     * Sets address *
     *
     * @param address address
     * @return the address
     * @since 1.0.0
     */
    public URL setAddress(String address) {
        int i = address.lastIndexOf(':');
        String host;
        int port = this.port;
        if (i >= 0) {
            host = address.substring(0, i);
            port = Integer.parseInt(address.substring(i + 1));
        } else {
            host = address;
        }
        return new URL(protocol, username, password, host, port, path, getParameters());
    }

    /**
     * Gets backup address *
     *
     * @return the backup address
     * @since 1.0.0
     */
    public String getBackupAddress() {
        return getBackupAddress(0);
    }

    /**
     * Gets backup address *
     *
     * @param defaultPort default port
     * @return the backup address
     * @since 1.0.0
     */
    public String getBackupAddress(int defaultPort) {
        StringBuilder address = new StringBuilder(appendDefaultPort(getAddress(), defaultPort));
        String[] backups = getParameter(RemotingConstants.BACKUP_KEY, new String[0]);
        if (ArrayUtils.isNotEmpty(backups)) {
            for (String backup : backups) {
                address.append(",");
                address.append(appendDefaultPort(backup, defaultPort));
            }
        }
        return address.toString();
    }

    /**
     * Gets backup urls *
     *
     * @return the backup urls
     * @since 1.0.0
     */
    public List<URL> getBackupUrls() {
        List<URL> urls = new ArrayList<>();
        urls.add(this);
        String[] backups = getParameter(RemotingConstants.BACKUP_KEY, new String[0]);
        if (backups != null && backups.length > 0) {
            for (String backup : backups) {
                urls.add(this.setAddress(backup));
            }
        }
        return urls;
    }

    /**
     * Append default port
     *
     * @param address     address
     * @param defaultPort default port
     * @return the string
     * @since 1.0.0
     */
    static String appendDefaultPort(String address, int defaultPort) {
        if (address != null && address.length() > 0 && defaultPort > 0) {
            int i = address.indexOf(':');
            if (i < 0) {
                return address + ":" + defaultPort;
            } else if (Integer.parseInt(address.substring(i + 1)) == 0) {
                return address.substring(0, i + 1) + defaultPort;
            }
        }
        return address;
    }

    /**
     * Gets path *
     *
     * @return the path
     * @since 1.0.0
     */
    public String getPath() {
        return path;
    }

    /**
     * Sets path *
     *
     * @param path path
     * @return the path
     * @since 1.0.0
     */
    public URL setPath(String path) {
        return new URL(protocol, username, password, host, port, path, getParameters());
    }

    /**
     * Gets absolute path *
     *
     * @return the absolute path
     * @since 1.0.0
     */
    public String getAbsolutePath() {
        if (path != null && !path.startsWith("/")) {
            return "/" + path;
        }
        return path;
    }

    /**
     * Gets parameters *
     *
     * @return the parameters
     * @since 1.0.0
     */
    public Map<String, String> getParameters() {
        return parameters;
    }

    /**
     * Gets parameter and decoded *
     *
     * @param key key
     * @return the parameter and decoded
     * @since 1.0.0
     */
    public String getParameterAndDecoded(String key) {
        return getParameterAndDecoded(key, null);
    }

    /**
     * Gets parameter and decoded *
     *
     * @param key          key
     * @param defaultValue default value
     * @return the parameter and decoded
     * @since 1.0.0
     */
    public String getParameterAndDecoded(String key, String defaultValue) {
        return decode(getParameter(key, defaultValue));
    }

    /**
     * Gets parameter *
     *
     * @param key key
     * @return the parameter
     * @since 1.0.0
     */
    public String getParameter(String key) {
        String value = parameters.get(key);
        return SpiStringUtils.isEmpty(value) ? parameters.get(DEFAULT_KEY_PREFIX + key) : value;
    }

    /**
     * Gets parameter *
     *
     * @param key          key
     * @param defaultValue default value
     * @return the parameter
     * @since 1.0.0
     */
    public String getParameter(String key, String defaultValue) {
        String value = getParameter(key);
        return SpiStringUtils.isEmpty(value) ? defaultValue : value;
    }

    /**
     * Get parameter
     *
     * @param key          key
     * @param defaultValue default value
     * @return the string [ ]
     * @since 1.0.0
     */
    public String[] getParameter(String key, String[] defaultValue) {
        String value = getParameter(key);
        return SpiStringUtils.isEmpty(value) ? defaultValue : COMMA_SPLIT_PATTERN.split(value);
    }

    /**
     * Gets parameter *
     *
     * @param key          key
     * @param defaultValue default value
     * @return the parameter
     * @since 1.0.0
     */
    public List<String> getParameter(String key, List<String> defaultValue) {
        String value = getParameter(key);
        if (value == null || value.length() == 0) {
            return defaultValue;
        }
        String[] strArray = COMMA_SPLIT_PATTERN.split(value);
        return Arrays.asList(strArray);
    }

    /**
     * Gets numbers *
     *
     * @return the numbers
     * @since 1.0.0
     */
    private Map<String, Number> getNumbers() {
        // concurrent initialization is tolerant
        return numbers == null ? new ConcurrentHashMap<>() : numbers;
    }

    /**
     * Gets urls *
     *
     * @return the urls
     * @since 1.0.0
     */
    private Map<String, URL> getUrls() {
        // concurrent initialization is tolerant
        return urls == null ? new ConcurrentHashMap<>() : urls;
    }

    /**
     * Gets url parameter *
     *
     * @param key key
     * @return the url parameter
     * @since 1.0.0
     */
    public URL getUrlParameter(String key) {
        URL u = getUrls().get(key);
        if (u != null) {
            return u;
        }
        String value = getParameterAndDecoded(key);
        if (SpiStringUtils.isEmpty(value)) {
            return null;
        }
        u = URL.valueOf(value);
        getUrls().put(key, u);
        return u;
    }

    /**
     * Gets parameter *
     *
     * @param key          key
     * @param defaultValue default value
     * @return the parameter
     * @since 1.0.0
     */
    public double getParameter(String key, double defaultValue) {
        Number n = getNumbers().get(key);
        if (n != null) {
            return n.doubleValue();
        }
        String value = getParameter(key);
        if (SpiStringUtils.isEmpty(value)) {
            return defaultValue;
        }
        double d = Double.parseDouble(value);
        getNumbers().put(key, d);
        return d;
    }

    /**
     * Gets parameter *
     *
     * @param key          key
     * @param defaultValue default value
     * @return the parameter
     * @since 1.0.0
     */
    public float getParameter(String key, float defaultValue) {
        Number n = getNumbers().get(key);
        if (n != null) {
            return n.floatValue();
        }
        String value = getParameter(key);
        if (SpiStringUtils.isEmpty(value)) {
            return defaultValue;
        }
        float f = Float.parseFloat(value);
        getNumbers().put(key, f);
        return f;
    }

    /**
     * Gets parameter *
     *
     * @param key          key
     * @param defaultValue default value
     * @return the parameter
     * @since 1.0.0
     */
    public long getParameter(String key, long defaultValue) {
        Number n = getNumbers().get(key);
        if (n != null) {
            return n.longValue();
        }
        String value = getParameter(key);
        if (SpiStringUtils.isEmpty(value)) {
            return defaultValue;
        }
        long l = Long.parseLong(value);
        getNumbers().put(key, l);
        return l;
    }

    /**
     * Gets parameter *
     *
     * @param key          key
     * @param defaultValue default value
     * @return the parameter
     * @since 1.0.0
     */
    public int getParameter(String key, int defaultValue) {
        Number n = getNumbers().get(key);
        if (n != null) {
            return n.intValue();
        }
        String value = getParameter(key);
        if (SpiStringUtils.isEmpty(value)) {
            return defaultValue;
        }
        int i = Integer.parseInt(value);
        getNumbers().put(key, i);
        return i;
    }

    /**
     * Gets parameter *
     *
     * @param key          key
     * @param defaultValue default value
     * @return the parameter
     * @since 1.0.0
     */
    public short getParameter(String key, short defaultValue) {
        Number n = getNumbers().get(key);
        if (n != null) {
            return n.shortValue();
        }
        String value = getParameter(key);
        if (SpiStringUtils.isEmpty(value)) {
            return defaultValue;
        }
        short s = Short.parseShort(value);
        getNumbers().put(key, s);
        return s;
    }

    /**
     * Gets parameter *
     *
     * @param key          key
     * @param defaultValue default value
     * @return the parameter
     * @since 1.0.0
     */
    public byte getParameter(String key, byte defaultValue) {
        Number n = getNumbers().get(key);
        if (n != null) {
            return n.byteValue();
        }
        String value = getParameter(key);
        if (SpiStringUtils.isEmpty(value)) {
            return defaultValue;
        }
        byte b = Byte.parseByte(value);
        getNumbers().put(key, b);
        return b;
    }

    /**
     * Gets positive parameter *
     *
     * @param key          key
     * @param defaultValue default value
     * @return the positive parameter
     * @since 1.0.0
     */
    public float getPositiveParameter(String key, float defaultValue) {
        if (defaultValue <= 0) {
            throw new IllegalArgumentException("defaultValue <= 0");
        }
        float value = getParameter(key, defaultValue);
        return value <= 0 ? defaultValue : value;
    }

    /**
     * Gets positive parameter *
     *
     * @param key          key
     * @param defaultValue default value
     * @return the positive parameter
     * @since 1.0.0
     */
    public double getPositiveParameter(String key, double defaultValue) {
        if (defaultValue <= 0) {
            throw new IllegalArgumentException("defaultValue <= 0");
        }
        double value = getParameter(key, defaultValue);
        return value <= 0 ? defaultValue : value;
    }

    /**
     * Gets positive parameter *
     *
     * @param key          key
     * @param defaultValue default value
     * @return the positive parameter
     * @since 1.0.0
     */
    public long getPositiveParameter(String key, long defaultValue) {
        if (defaultValue <= 0) {
            throw new IllegalArgumentException("defaultValue <= 0");
        }
        long value = getParameter(key, defaultValue);
        return value <= 0 ? defaultValue : value;
    }

    /**
     * Gets positive parameter *
     *
     * @param key          key
     * @param defaultValue default value
     * @return the positive parameter
     * @since 1.0.0
     */
    public int getPositiveParameter(String key, int defaultValue) {
        if (defaultValue <= 0) {
            throw new IllegalArgumentException("defaultValue <= 0");
        }
        int value = getParameter(key, defaultValue);
        return value <= 0 ? defaultValue : value;
    }

    /**
     * Gets positive parameter *
     *
     * @param key          key
     * @param defaultValue default value
     * @return the positive parameter
     * @since 1.0.0
     */
    public short getPositiveParameter(String key, short defaultValue) {
        if (defaultValue <= 0) {
            throw new IllegalArgumentException("defaultValue <= 0");
        }
        short value = getParameter(key, defaultValue);
        return value <= 0 ? defaultValue : value;
    }

    /**
     * Gets positive parameter *
     *
     * @param key          key
     * @param defaultValue default value
     * @return the positive parameter
     * @since 1.0.0
     */
    public byte getPositiveParameter(String key, byte defaultValue) {
        if (defaultValue <= 0) {
            throw new IllegalArgumentException("defaultValue <= 0");
        }
        byte value = getParameter(key, defaultValue);
        return value <= 0 ? defaultValue : value;
    }

    /**
     * Gets parameter *
     *
     * @param key          key
     * @param defaultValue default value
     * @return the parameter
     * @since 1.0.0
     */
    public char getParameter(String key, char defaultValue) {
        String value = getParameter(key);
        return SpiStringUtils.isEmpty(value) ? defaultValue : value.charAt(0);
    }

    /**
     * Gets parameter *
     *
     * @param key          key
     * @param defaultValue default value
     * @return the parameter
     * @since 1.0.0
     */
    public boolean getParameter(String key, boolean defaultValue) {
        String value = getParameter(key);
        return SpiStringUtils.isEmpty(value) ? defaultValue : Boolean.parseBoolean(value);
    }

    /**
     * Has parameter
     *
     * @param key key
     * @return the boolean
     * @since 1.0.0
     */
    public boolean hasParameter(String key) {
        String value = getParameter(key);
        return value != null && value.length() > 0;
    }

    /**
     * Gets method parameter and decoded *
     *
     * @param method method
     * @param key    key
     * @return the method parameter and decoded
     * @since 1.0.0
     */
    public String getMethodParameterAndDecoded(String method, String key) {
        return URL.decode(getMethodParameter(method, key));
    }

    /**
     * Gets method parameter and decoded *
     *
     * @param method       method
     * @param key          key
     * @param defaultValue default value
     * @return the method parameter and decoded
     * @since 1.0.0
     */
    public String getMethodParameterAndDecoded(String method, String key, String defaultValue) {
        return URL.decode(getMethodParameter(method, key, defaultValue));
    }

    /**
     * Gets method parameter *
     *
     * @param method method
     * @param key    key
     * @return the method parameter
     * @since 1.0.0
     */
    public String getMethodParameter(String method, String key) {
        String value = parameters.get(method + "." + key);
        return SpiStringUtils.isEmpty(value) ? getParameter(key) : value;
    }

    /**
     * Gets method parameter *
     *
     * @param method       method
     * @param key          key
     * @param defaultValue default value
     * @return the method parameter
     * @since 1.0.0
     */
    public String getMethodParameter(String method, String key, String defaultValue) {
        String value = getMethodParameter(method, key);
        return SpiStringUtils.isEmpty(value) ? defaultValue : value;
    }

    /**
     * Gets method parameter *
     *
     * @param method       method
     * @param key          key
     * @param defaultValue default value
     * @return the method parameter
     * @since 1.0.0
     */
    public double getMethodParameter(String method, String key, double defaultValue) {
        String methodKey = method + "." + key;
        Number n = getNumbers().get(methodKey);
        if (n != null) {
            return n.doubleValue();
        }
        String value = getMethodParameter(method, key);
        if (SpiStringUtils.isEmpty(value)) {
            return defaultValue;
        }
        double d = Double.parseDouble(value);
        getNumbers().put(methodKey, d);
        return d;
    }

    /**
     * Gets method parameter *
     *
     * @param method       method
     * @param key          key
     * @param defaultValue default value
     * @return the method parameter
     * @since 1.0.0
     */
    public float getMethodParameter(String method, String key, float defaultValue) {
        String methodKey = method + "." + key;
        Number n = getNumbers().get(methodKey);
        if (n != null) {
            return n.floatValue();
        }
        String value = getMethodParameter(method, key);
        if (SpiStringUtils.isEmpty(value)) {
            return defaultValue;
        }
        float f = Float.parseFloat(value);
        getNumbers().put(methodKey, f);
        return f;
    }

    /**
     * Gets method parameter *
     *
     * @param method       method
     * @param key          key
     * @param defaultValue default value
     * @return the method parameter
     * @since 1.0.0
     */
    public long getMethodParameter(String method, String key, long defaultValue) {
        String methodKey = method + "." + key;
        Number n = getNumbers().get(methodKey);
        if (n != null) {
            return n.longValue();
        }
        String value = getMethodParameter(method, key);
        if (SpiStringUtils.isEmpty(value)) {
            return defaultValue;
        }
        long l = Long.parseLong(value);
        getNumbers().put(methodKey, l);
        return l;
    }

    /**
     * Gets method parameter *
     *
     * @param method       method
     * @param key          key
     * @param defaultValue default value
     * @return the method parameter
     * @since 1.0.0
     */
    public int getMethodParameter(String method, String key, int defaultValue) {
        String methodKey = method + "." + key;
        Number n = getNumbers().get(methodKey);
        if (n != null) {
            return n.intValue();
        }
        String value = getMethodParameter(method, key);
        if (SpiStringUtils.isEmpty(value)) {
            return defaultValue;
        }
        int i = Integer.parseInt(value);
        getNumbers().put(methodKey, i);
        return i;
    }

    /**
     * Gets method parameter *
     *
     * @param method       method
     * @param key          key
     * @param defaultValue default value
     * @return the method parameter
     * @since 1.0.0
     */
    public short getMethodParameter(String method, String key, short defaultValue) {
        String methodKey = method + "." + key;
        Number n = getNumbers().get(methodKey);
        if (n != null) {
            return n.shortValue();
        }
        String value = getMethodParameter(method, key);
        if (SpiStringUtils.isEmpty(value)) {
            return defaultValue;
        }
        short s = Short.parseShort(value);
        getNumbers().put(methodKey, s);
        return s;
    }

    /**
     * Gets method parameter *
     *
     * @param method       method
     * @param key          key
     * @param defaultValue default value
     * @return the method parameter
     * @since 1.0.0
     */
    public byte getMethodParameter(String method, String key, byte defaultValue) {
        String methodKey = method + "." + key;
        Number n = getNumbers().get(methodKey);
        if (n != null) {
            return n.byteValue();
        }
        String value = getMethodParameter(method, key);
        if (SpiStringUtils.isEmpty(value)) {
            return defaultValue;
        }
        byte b = Byte.parseByte(value);
        getNumbers().put(methodKey, b);
        return b;
    }

    /**
     * Gets method positive parameter *
     *
     * @param method       method
     * @param key          key
     * @param defaultValue default value
     * @return the method positive parameter
     * @since 1.0.0
     */
    public double getMethodPositiveParameter(String method, String key, double defaultValue) {
        if (defaultValue <= 0) {
            throw new IllegalArgumentException("defaultValue <= 0");
        }
        double value = getMethodParameter(method, key, defaultValue);
        return value <= 0 ? defaultValue : value;
    }

    /**
     * Gets method positive parameter *
     *
     * @param method       method
     * @param key          key
     * @param defaultValue default value
     * @return the method positive parameter
     * @since 1.0.0
     */
    public float getMethodPositiveParameter(String method, String key, float defaultValue) {
        if (defaultValue <= 0) {
            throw new IllegalArgumentException("defaultValue <= 0");
        }
        float value = getMethodParameter(method, key, defaultValue);
        return value <= 0 ? defaultValue : value;
    }

    /**
     * Gets method positive parameter *
     *
     * @param method       method
     * @param key          key
     * @param defaultValue default value
     * @return the method positive parameter
     * @since 1.0.0
     */
    public long getMethodPositiveParameter(String method, String key, long defaultValue) {
        if (defaultValue <= 0) {
            throw new IllegalArgumentException("defaultValue <= 0");
        }
        long value = getMethodParameter(method, key, defaultValue);
        return value <= 0 ? defaultValue : value;
    }

    /**
     * Gets method positive parameter *
     *
     * @param method       method
     * @param key          key
     * @param defaultValue default value
     * @return the method positive parameter
     * @since 1.0.0
     */
    public int getMethodPositiveParameter(String method, String key, int defaultValue) {
        if (defaultValue <= 0) {
            throw new IllegalArgumentException("defaultValue <= 0");
        }
        int value = getMethodParameter(method, key, defaultValue);
        return value <= 0 ? defaultValue : value;
    }

    /**
     * Gets method positive parameter *
     *
     * @param method       method
     * @param key          key
     * @param defaultValue default value
     * @return the method positive parameter
     * @since 1.0.0
     */
    public short getMethodPositiveParameter(String method, String key, short defaultValue) {
        if (defaultValue <= 0) {
            throw new IllegalArgumentException("defaultValue <= 0");
        }
        short value = getMethodParameter(method, key, defaultValue);
        return value <= 0 ? defaultValue : value;
    }

    /**
     * Gets method positive parameter *
     *
     * @param method       method
     * @param key          key
     * @param defaultValue default value
     * @return the method positive parameter
     * @since 1.0.0
     */
    public byte getMethodPositiveParameter(String method, String key, byte defaultValue) {
        if (defaultValue <= 0) {
            throw new IllegalArgumentException("defaultValue <= 0");
        }
        byte value = getMethodParameter(method, key, defaultValue);
        return value <= 0 ? defaultValue : value;
    }

    /**
     * Gets method parameter *
     *
     * @param method       method
     * @param key          key
     * @param defaultValue default value
     * @return the method parameter
     * @since 1.0.0
     */
    public char getMethodParameter(String method, String key, char defaultValue) {
        String value = getMethodParameter(method, key);
        return SpiStringUtils.isEmpty(value) ? defaultValue : value.charAt(0);
    }

    /**
     * Gets method parameter *
     *
     * @param method       method
     * @param key          key
     * @param defaultValue default value
     * @return the method parameter
     * @since 1.0.0
     */
    public boolean getMethodParameter(String method, String key, boolean defaultValue) {
        String value = getMethodParameter(method, key);
        return SpiStringUtils.isEmpty(value) ? defaultValue : Boolean.parseBoolean(value);
    }

    /**
     * Has method parameter
     *
     * @param method method
     * @param key    key
     * @return the boolean
     * @since 1.0.0
     */
    public boolean hasMethodParameter(String method, String key) {
        if (method == null) {
            String suffix = "." + key;
            for (String fullKey : parameters.keySet()) {
                if (fullKey.endsWith(suffix)) {
                    return true;
                }
            }
            return false;
        }
        if (key == null) {
            String prefix = method + ".";
            for (String fullKey : parameters.keySet()) {
                if (fullKey.startsWith(prefix)) {
                    return true;
                }
            }
            return false;
        }
        String value = getMethodParameter(method, key);
        return value != null && value.length() > 0;
    }

    /**
     * Is local host
     *
     * @return the boolean
     * @since 1.0.0
     */
    public boolean isLocalHost() {
        return NetUtils.isLocalHost(host) || getParameter(LOCALHOST_KEY, false);
    }

    /**
     * Is any host
     *
     * @return the boolean
     * @since 1.0.0
     */
    public boolean isAnyHost() {
        return ANYHOST_VALUE.equals(host) || getParameter(ANYHOST_KEY, false);
    }

    /**
     * Add parameter and encoded
     *
     * @param key   key
     * @param value value
     * @return the url
     * @since 1.0.0
     */
    public URL addParameterAndEncoded(String key, String value) {
        if (SpiStringUtils.isEmpty(value)) {
            return this;
        }
        return addParameter(key, encode(value));
    }

    /**
     * Add parameter
     *
     * @param key   key
     * @param value value
     * @return the url
     * @since 1.0.0
     */
    public URL addParameter(String key, boolean value) {
        return addParameter(key, String.valueOf(value));
    }

    /**
     * Add parameter
     *
     * @param key   key
     * @param value value
     * @return the url
     * @since 1.0.0
     */
    public URL addParameter(String key, char value) {
        return addParameter(key, String.valueOf(value));
    }

    /**
     * Add parameter
     *
     * @param key   key
     * @param value value
     * @return the url
     * @since 1.0.0
     */
    public URL addParameter(String key, byte value) {
        return addParameter(key, String.valueOf(value));
    }

    /**
     * Add parameter
     *
     * @param key   key
     * @param value value
     * @return the url
     * @since 1.0.0
     */
    public URL addParameter(String key, short value) {
        return addParameter(key, String.valueOf(value));
    }

    /**
     * Add parameter
     *
     * @param key   key
     * @param value value
     * @return the url
     * @since 1.0.0
     */
    public URL addParameter(String key, int value) {
        return addParameter(key, String.valueOf(value));
    }

    /**
     * Add parameter
     *
     * @param key   key
     * @param value value
     * @return the url
     * @since 1.0.0
     */
    public URL addParameter(String key, long value) {
        return addParameter(key, String.valueOf(value));
    }

    /**
     * Add parameter
     *
     * @param key   key
     * @param value value
     * @return the url
     * @since 1.0.0
     */
    public URL addParameter(String key, float value) {
        return addParameter(key, String.valueOf(value));
    }

    /**
     * Add parameter
     *
     * @param key   key
     * @param value value
     * @return the url
     * @since 1.0.0
     */
    public URL addParameter(String key, double value) {
        return addParameter(key, String.valueOf(value));
    }

    /**
     * Add parameter
     *
     * @param key   key
     * @param value value
     * @return the url
     * @since 1.0.0
     */
    public URL addParameter(String key, Enum<?> value) {
        if (value == null) {
            return this;
        }
        return addParameter(key, String.valueOf(value));
    }

    /**
     * Add parameter
     *
     * @param key   key
     * @param value value
     * @return the url
     * @since 1.0.0
     */
    public URL addParameter(String key, Number value) {
        if (value == null) {
            return this;
        }
        return addParameter(key, String.valueOf(value));
    }

    /**
     * Add parameter
     *
     * @param key   key
     * @param value value
     * @return the url
     * @since 1.0.0
     */
    public URL addParameter(String key, CharSequence value) {
        if (value == null || value.length() == 0) {
            return this;
        }
        return addParameter(key, String.valueOf(value));
    }

    /**
     * Add parameter
     *
     * @param key   key
     * @param value value
     * @return the url
     * @since 1.0.0
     */
    public URL addParameter(String key, String value) {
        if (SpiStringUtils.isEmpty(key)
            || SpiStringUtils.isEmpty(value)) {
            return this;
        }
        // if value doesn't change, return immediately
        if (value.equals(getParameters().get(key))) { // value != null
            return this;
        }

        Map<String, String> map = new HashMap<>(getParameters());
        map.put(key, value);
        return new URL(protocol, username, password, host, port, path, map);
    }

    /**
     * Add parameter if absent
     *
     * @param key   key
     * @param value value
     * @return the url
     * @since 1.0.0
     */
    public URL addParameterIfAbsent(String key, String value) {
        if (SpiStringUtils.isEmpty(key)
            || SpiStringUtils.isEmpty(value)) {
            return this;
        }
        if (hasParameter(key)) {
            return this;
        }
        Map<String, String> map = new HashMap<>(getParameters());
        map.put(key, value);
        return new URL(protocol, username, password, host, port, path, map);
    }

    /**
     * Add parameters
     *
     * @param parameters parameters
     * @return the url
     * @since 1.0.0
     */
    public URL addParameters(Map<String, String> parameters) {
        if (CollectionUtils.isEmptyMap(parameters)) {
            return this;
        }

        boolean hasAndEqual = true;
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            String value = getParameters().get(entry.getKey());
            if (value == null) {
                if (entry.getValue() != null) {
                    hasAndEqual = false;
                    break;
                }
            } else {
                if (!value.equals(entry.getValue())) {
                    hasAndEqual = false;
                    break;
                }
            }
        }
        // return immediately if there's no change
        if (hasAndEqual) {
            return this;
        }

        Map<String, String> map = new HashMap<>(getParameters());
        map.putAll(parameters);
        return new URL(protocol, username, password, host, port, path, map);
    }

    /**
     * Add parameters if absent
     *
     * @param parameters parameters
     * @return the url
     * @since 1.0.0
     */
    public URL addParametersIfAbsent(Map<String, String> parameters) {
        if (CollectionUtils.isEmptyMap(parameters)) {
            return this;
        }
        Map<String, String> map = new HashMap<>(parameters);
        map.putAll(getParameters());
        return new URL(protocol, username, password, host, port, path, map);
    }

    /**
     * Add parameters
     *
     * @param pairs pairs
     * @return the url
     * @since 1.0.0
     */
    public URL addParameters(String... pairs) {
        if (pairs == null || pairs.length == 0) {
            return this;
        }
        if (pairs.length % 2 != 0) {
            throw new IllegalArgumentException("Map pairs can not be odd number.");
        }
        Map<String, String> map = new HashMap<>(16);
        int len = pairs.length / 2;
        for (int i = 0; i < len; i++) {
            map.put(pairs[2 * i], pairs[2 * i + 1]);
        }
        return addParameters(map);
    }

    /**
     * Add parameter string
     *
     * @param query query
     * @return the url
     * @since 1.0.0
     */
    public URL addParameterString(String query) {
        if (SpiStringUtils.isEmpty(query)) {
            return this;
        }
        return addParameters(SpiStringUtils.parseQueryString(query));
    }

    /**
     * Remove parameter
     *
     * @param key key
     * @return the url
     * @since 1.0.0
     */
    public URL removeParameter(String key) {
        if (SpiStringUtils.isEmpty(key)) {
            return this;
        }
        return removeParameters(key);
    }

    /**
     * Remove parameters
     *
     * @param keys keys
     * @return the url
     * @since 1.0.0
     */
    public URL removeParameters(Collection<String> keys) {
        if (CollectionUtils.isEmpty(keys)) {
            return this;
        }
        return removeParameters(keys.toArray(new String[0]));
    }

    /**
     * Remove parameters
     *
     * @param keys keys
     * @return the url
     * @since 1.0.0
     */
    public URL removeParameters(String... keys) {
        if (keys == null || keys.length == 0) {
            return this;
        }
        Map<String, String> map = new HashMap<>(getParameters());
        for (String key : keys) {
            map.remove(key);
        }
        if (map.size() == getParameters().size()) {
            return this;
        }
        return new URL(protocol, username, password, host, port, path, map);
    }

    /**
     * Clear parameters
     *
     * @return the url
     * @since 1.0.0
     */
    public URL clearParameters() {
        return new URL(protocol, username, password, host, port, path, new HashMap<>(16));
    }

    /**
     * Gets raw parameter *
     *
     * @param key key
     * @return the raw parameter
     * @since 1.0.0
     */
    public String getRawParameter(String key) {
        if (PROTOCOL_KEY.equals(key)) {
            return protocol;
        }
        if (USERNAME_KEY.equals(key)) {
            return username;
        }
        if (PASSWORD_KEY.equals(key)) {
            return password;
        }
        if (HOST_KEY.equals(key)) {
            return host;
        }
        if (PORT_KEY.equals(key)) {
            return String.valueOf(port);
        }
        if (PATH_KEY.equals(key)) {
            return path;
        }
        return getParameter(key);
    }

    /**
     * To map
     *
     * @return the map
     * @since 1.0.0
     */
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>(parameters);
        if (protocol != null) {
            map.put(PROTOCOL_KEY, protocol);
        }
        if (username != null) {
            map.put(USERNAME_KEY, username);
        }
        if (password != null) {
            map.put(PASSWORD_KEY, password);
        }
        if (host != null) {
            map.put(HOST_KEY, host);
        }
        if (port > 0) {
            map.put(PORT_KEY, String.valueOf(port));
        }
        if (path != null) {
            map.put(PATH_KEY, path);
        }
        return map;
    }

    /**
     * To string
     *
     * @return the string
     * @since 1.0.0
     */
    @Override
    public String toString() {
        if (string != null) {
            return string;
        }
        return string = buildString(false, true); // no show username and password
    }

    /**
     * To string
     *
     * @param parameters parameters
     * @return the string
     * @since 1.0.0
     */
    public String toString(String... parameters) {
        return buildString(false, true, parameters); // no show username and password
    }

    /**
     * To identity string
     *
     * @return the string
     * @since 1.0.0
     */
    public String toIdentityString() {
        if (identity != null) {
            return identity;
        }
        return identity = buildString(true, false); // only return identity message, see the method "equals" and "hashCode"
    }

    /**
     * To identity string
     *
     * @param parameters parameters
     * @return the string
     * @since 1.0.0
     */
    public String toIdentityString(String... parameters) {
        return buildString(true, false, parameters); // only return identity message, see the method "equals" and "hashCode"
    }

    /**
     * To full string
     *
     * @return the string
     * @since 1.0.0
     */
    public String toFullString() {
        if (full != null) {
            return full;
        }
        return full = buildString(true, true);
    }

    /**
     * To full string
     *
     * @param parameters parameters
     * @return the string
     * @since 1.0.0
     */
    public String toFullString(String... parameters) {
        return buildString(true, true, parameters);
    }

    /**
     * To parameter string
     *
     * @return the string
     * @since 1.0.0
     */
    public String toParameterString() {
        if (parameter != null) {
            return parameter;
        }
        return parameter = toParameterString(new String[0]);
    }

    /**
     * To parameter string
     *
     * @param parameters parameters
     * @return the string
     * @since 1.0.0
     */
    public String toParameterString(String... parameters) {
        StringBuilder buf = new StringBuilder();
        buildParameters(buf, false, parameters);
        return buf.toString();
    }

    /**
     * Build parameters
     *
     * @param buf        buf
     * @param concat     concat
     * @param parameters parameters
     * @since 1.0.0
     */
    private void buildParameters(StringBuilder buf, boolean concat, String[] parameters) {
        if (CollectionUtils.isNotEmptyMap(getParameters())) {
            List<String> includes = (ArrayUtils.isEmpty(parameters) ? null : Arrays.asList(parameters));
            boolean first = true;
            for (Map.Entry<String, String> entry : new TreeMap<>(getParameters()).entrySet()) {
                if (entry.getKey() != null && entry.getKey().length() > 0
                    && (includes == null || includes.contains(entry.getKey()))) {
                    if (first) {
                        if (concat) {
                            buf.append("?");
                        }
                        first = false;
                    } else {
                        buf.append("&");
                    }
                    buf.append(entry.getKey());
                    buf.append("=");
                    buf.append(entry.getValue() == null ? "" : entry.getValue().trim());
                }
            }
        }
    }

    /**
     * Build string
     *
     * @param appendUser      append user
     * @param appendParameter append parameter
     * @param parameters      parameters
     * @return the string
     * @since 1.0.0
     */
    private String buildString(boolean appendUser, boolean appendParameter, String... parameters) {
        return buildString(appendUser, appendParameter, false, false, parameters);
    }

    /**
     * Build string
     *
     * @param appendUser      append user
     * @param appendParameter append parameter
     * @param useIP           use ip
     * @param useService      use service
     * @param parameters      parameters
     * @return the string
     * @since 1.0.0
     */
    private String buildString(boolean appendUser, boolean appendParameter, boolean useIP, boolean useService, String... parameters) {
        StringBuilder buf = new StringBuilder();
        if (SpiStringUtils.isNotEmpty(protocol)) {
            buf.append(protocol);
            buf.append("://");
        }
        if (appendUser && SpiStringUtils.isNotEmpty(username)) {
            buf.append(username);
            if (password != null && password.length() > 0) {
                buf.append(":");
                buf.append(password);
            }
            buf.append("@");
        }
        String host;
        if (useIP) {
            host = getIp();
        } else {
            host = getHost();
        }
        if (host != null && host.length() > 0) {
            buf.append(host);
            if (port > 0) {
                buf.append(":");
                buf.append(port);
            }
        }
        String path;
        if (useService) {
            path = getServiceKey();
        } else {
            path = getPath();
        }
        if (path != null && path.length() > 0) {
            buf.append("/");
            buf.append(path);
        }

        if (appendParameter) {
            buildParameters(buf, true, parameters);
        }
        return buf.toString();
    }

    /**
     * To java url
     *
     * @return the java . net . url
     * @since 1.0.0
     */
    public java.net.URL toJavaURL() {
        try {
            return new java.net.URL(toString());
        } catch (MalformedURLException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    /**
     * To inet socket address
     *
     * @return the inet socket address
     * @since 1.0.0
     */
    public InetSocketAddress toInetSocketAddress() {
        return new InetSocketAddress(host, port);
    }

    /**
     * Gets colon separated key *
     *
     * @return the colon separated key
     * @since 1.0.0
     */
    public String getColonSeparatedKey() {
        StringBuilder serviceNameBuilder = new StringBuilder();
        append(serviceNameBuilder, INTERFACE_KEY, true);
        append(serviceNameBuilder, VERSION_KEY, false);
        append(serviceNameBuilder, GROUP_KEY, false);
        return serviceNameBuilder.toString();
    }

    /**
     * Append
     *
     * @param target        target
     * @param parameterName parameter name
     * @param first         first
     * @since 1.0.0
     */
    private void append(StringBuilder target, String parameterName, boolean first) {
        String parameterValue = this.getParameter(parameterName);
        if (!SpiStringUtils.isBlank(parameterValue)) {
            if (!first) {
                target.append(":");
            }
            target.append(parameterValue);
        } else {
            target.append(":");
        }
    }

    /**
     * Gets service key *
     *
     * @return the service key
     * @since 1.0.0
     */
    public String getServiceKey() {
        String inf = getServiceInterface();
        if (inf == null) {
            return null;
        }
        return buildKey(inf, getParameter(GROUP_KEY), getParameter(VERSION_KEY));
    }

    /**
     * Gets path key *
     *
     * @return the path key
     * @since 1.0.0
     */
    public String getPathKey() {
        String inf = SpiStringUtils.isNotEmpty(path) ? path : getServiceInterface();
        if (inf == null) {
            return null;
        }
        return buildKey(inf, getParameter(GROUP_KEY), getParameter(VERSION_KEY));
    }

    /**
     * Build key
     *
     * @param path    path
     * @param group   group
     * @param version version
     * @return the string
     * @since 1.0.0
     */
    public static String buildKey(String path, String group, String version) {
        StringBuilder buf = new StringBuilder();
        if (group != null && group.length() > 0) {
            buf.append(group).append("/");
        }
        buf.append(path);
        if (version != null && version.length() > 0) {
            buf.append(":").append(version);
        }
        return buf.toString();
    }

    /**
     * To service string without resolving
     *
     * @return the string
     * @since 1.0.0
     */
    public String toServiceStringWithoutResolving() {
        return buildString(true, false, false, true);
    }

    /**
     * To service string
     *
     * @return the string
     * @since 1.0.0
     */
    public String toServiceString() {
        return buildString(true, false, true, true);
    }

    /**
     * Gets service name *
     *
     * @return the service name
     * @since 1.0.0
     */
    @Deprecated
    public String getServiceName() {
        return getServiceInterface();
    }

    /**
     * Gets service interface *
     *
     * @return the service interface
     * @since 1.0.0
     */
    public String getServiceInterface() {
        return getParameter(INTERFACE_KEY, path);
    }

    /**
     * Sets service interface *
     *
     * @param service service
     * @return the service interface
     * @since 1.0.0
     */
    public URL setServiceInterface(String service) {
        return addParameter(INTERFACE_KEY, service);
    }

    /**
     * Gets int parameter *
     *
     * @param key key
     * @return the int parameter
     * @since 1.0.0
     */
    @Deprecated
    public int getIntParameter(String key) {
        return getParameter(key, 0);
    }

    /**
     * Gets int parameter *
     *
     * @param key          key
     * @param defaultValue default value
     * @return the int parameter
     * @since 1.0.0
     */
    @Deprecated
    public int getIntParameter(String key, int defaultValue) {
        return getParameter(key, defaultValue);
    }

    /**
     * Gets positive int parameter *
     *
     * @param key          key
     * @param defaultValue default value
     * @return the positive int parameter
     * @since 1.0.0
     */
    @Deprecated
    public int getPositiveIntParameter(String key, int defaultValue) {
        return getPositiveParameter(key, defaultValue);
    }

    /**
     * Gets boolean parameter *
     *
     * @param key key
     * @return the boolean parameter
     * @since 1.0.0
     */
    @Deprecated
    public boolean getBooleanParameter(String key) {
        return getParameter(key, false);
    }

    /**
     * Gets boolean parameter *
     *
     * @param key          key
     * @param defaultValue default value
     * @return the boolean parameter
     * @since 1.0.0
     */
    @Deprecated
    public boolean getBooleanParameter(String key, boolean defaultValue) {
        return getParameter(key, defaultValue);
    }

    /**
     * Gets method int parameter *
     *
     * @param method method
     * @param key    key
     * @return the method int parameter
     * @since 1.0.0
     */
    @Deprecated
    public int getMethodIntParameter(String method, String key) {
        return getMethodParameter(method, key, 0);
    }

    /**
     * Gets method int parameter *
     *
     * @param method       method
     * @param key          key
     * @param defaultValue default value
     * @return the method int parameter
     * @since 1.0.0
     */
    @Deprecated
    public int getMethodIntParameter(String method, String key, int defaultValue) {
        return getMethodParameter(method, key, defaultValue);
    }

    /**
     * Gets method positive int parameter *
     *
     * @param method       method
     * @param key          key
     * @param defaultValue default value
     * @return the method positive int parameter
     * @since 1.0.0
     */
    @Deprecated
    public int getMethodPositiveIntParameter(String method, String key, int defaultValue) {
        return getMethodPositiveParameter(method, key, defaultValue);
    }

    /**
     * Gets method boolean parameter *
     *
     * @param method method
     * @param key    key
     * @return the method boolean parameter
     * @since 1.0.0
     */
    @Deprecated
    public boolean getMethodBooleanParameter(String method, String key) {
        return getMethodParameter(method, key, false);
    }

    /**
     * Gets method boolean parameter *
     *
     * @param method       method
     * @param key          key
     * @param defaultValue default value
     * @return the method boolean parameter
     * @since 1.0.0
     */
    @Deprecated
    public boolean getMethodBooleanParameter(String method, String key, boolean defaultValue) {
        return getMethodParameter(method, key, defaultValue);
    }

    /**
     * To configuration
     *
     * @return the configuration
     * @since 1.0.0
     */
    public Configuration toConfiguration() {
        InmemoryConfiguration configuration = new InmemoryConfiguration();
        configuration.addProperties(parameters);
        return configuration;
    }

    /**
     * Hash code
     *
     * @return the int
     * @since 1.0.0
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((host == null) ? 0 : host.hashCode());
        result = prime * result + ((parameters == null) ? 0 : parameters.hashCode());
        result = prime * result + ((password == null) ? 0 : password.hashCode());
        result = prime * result + ((path == null) ? 0 : path.hashCode());
        result = prime * result + port;
        result = prime * result + ((protocol == null) ? 0 : protocol.hashCode());
        result = prime * result + ((username == null) ? 0 : username.hashCode());
        return result;
    }

    /**
     * Equals
     *
     * @param obj obj
     * @return the boolean
     * @since 1.0.0
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        URL other = (URL) obj;
        if (host == null) {
            if (other.host != null) {
                return false;
            }
        } else if (!host.equals(other.host)) {
            return false;
        }
        if (parameters == null) {
            if (other.parameters != null) {
                return false;
            }
        } else if (!parameters.equals(other.parameters)) {
            return false;
        }
        if (password == null) {
            if (other.password != null) {
                return false;
            }
        } else if (!password.equals(other.password)) {
            return false;
        }
        if (path == null) {
            if (other.path != null) {
                return false;
            }
        } else if (!path.equals(other.path)) {
            return false;
        }
        if (port != other.port) {
            return false;
        }
        if (protocol == null) {
            if (other.protocol != null) {
                return false;
            }
        } else if (!protocol.equals(other.protocol)) {
            return false;
        }
        if (username == null) {
            if (other.username != null) {
                return false;
            }
        } else if (!username.equals(other.username)) {
            return false;
        }
        return true;
    }

}
