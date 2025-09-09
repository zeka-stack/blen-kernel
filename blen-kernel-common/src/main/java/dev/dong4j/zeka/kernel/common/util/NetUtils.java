package dev.dong4j.zeka.kernel.common.util;

import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.util.ObjectUtils;

/**
 * <p>Description: 网络工具类，提供网络相关的工具方法，包括IP地址处理、端口检测等</p>
 * <p>
 * 主要功能：
 * <ul>
 *     <li>本地IP地址获取</li>
 *     <li>端口可用性检测</li>
 *     <li>HTTP请求IP地址解析</li>
 *     <li>主机名获取</li>
 * </ul>
 * </p>
 * <p>
 * 使用示例：
 * <pre>
 * // 获取本地IP地址
 * String localIp = NetUtils.getLocalHost();
 *
 * // 检查端口是否可用
 * boolean isPortAvailable = NetUtils.available(8080);
 *
 * // 获取HTTP请求的客户端IP
 * HttpServletRequest request = ...;
 * String clientIp = NetUtils.ip(request);
 *
 * // 获取主机名
 * String hostName = NetUtils.getLocalAddress().getHostName();
 * </pre>
 * </p>
 * <p>
 * 技术特性：
 * <ul>
 *     <li>继承INetUtils功能</li>
 *     <li>支持HTTP请求IP解析</li>
 *     <li>提供端口可用性检测</li>
 *     <li>处理代理和负载均衡场景</li>
 * </ul>
 * </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2019.12.30 20:43
 * @since 1.0.0
 */
@Slf4j
@UtilityClass
public class NetUtils extends INetUtils {
    /** 本地主机地址 */
    public static final String LOCAL_HOST = INetUtils.LOCALHOST_VALUE;
    /** 最小端口号 */
    public static final int MIN_PORT_NUMBER = INetUtils.MIN_PORT;
    /** 最大端口号 */
    public static final int MAX_PORT_NUMBER = INetUtils.MAX_PORT;

    /**
     * 获取本地IP地址
     *
     * @return 本地IP地址
     * @since 1.0.0
     * @deprecated 使用 {@link NetUtils#getLocalHost()} 替代
     */
    @Nullable
    @Deprecated
    public static String getLocalIpAddr() {
        return getLocalHost();
    }

    /**
     * 获取服务器主机名
     *
     * @return 主机名
     * @since 1.0.0
     * @deprecated 使用 {@link NetUtils#getLocalAddress()} 替代
     */
    @Deprecated
    public static String getHostName() {
        InetAddress localAddress = getLocalAddress();
        if (localAddress == null) {
            return LOCALHOST_KEY;
        }
        return localAddress.getHostName();
    }

    /**
     * 尝试端口时候被占用
     *
     * @param port 端口号
     * @return 没有被占用返回true, 被占用返回false
     * @since 1.0.0
     * @deprecated 使用 {@link NetUtils#available(int)} 替代
     */
    @Deprecated
    public static boolean tryPort(int port) {
        return available(port);
    }

    /**
     * 检查本机 TCP/UDP 端口是否可用, 可用返回 true, 否则返回 false
     *
     * @param port 端口号
     * @return 端口是否可用
     * @since 1.0.0
     */
    public static boolean available(int port) {
        if (port < MIN_PORT_NUMBER || port > MAX_PORT_NUMBER) {
            throw new IllegalArgumentException("Invalid start port: " + port);
        }
        try (ServerSocket ss = new ServerSocket(port);
             DatagramSocket ds = new DatagramSocket(port)) {
            ss.setReuseAddress(true);
            ds.setReuseAddress(true);
            return true;
        } catch (IOException ignored) {
            return false;
        }
    }

    /**
     * 获取请求方 IP 地址
     *
     * @param request HttpServletRequest对象
     * @return 客户端IP地址
     * @since 1.0.0
     */
    @SuppressWarnings({"PMD.UndefineMagicConstantRule", "D"})
    public static String ip(@NotNull HttpServletRequest request) {
        String ipAddress;
        ipAddress = request.getHeader("x-forwarded-for");
        if (ObjectUtils.isEmpty(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ObjectUtils.isEmpty(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if (LOCAL_HOST.equals(ipAddress)) {
                // 根据网卡取本机配置的IP
                InetAddress inetAddress = null;
                try {
                    inetAddress = InetAddress.getLocalHost();
                } catch (UnknownHostException ignored) {
                }
                ipAddress = inetAddress != null ? inetAddress.getHostAddress() : null;
            }
        }
        // 对于通过多个代理的情况,第一个IP为客户端真实IP,多个IP按照','分割
        if (ipAddress != null && ipAddress.length() > 15) {
            if (ipAddress.indexOf(StringPool.COMMA) > 0) {
                ipAddress = ipAddress.substring(0, ipAddress.indexOf(StringPool.COMMA));
            }
        }
        if (ObjectUtils.isEmpty(ipAddress)) {
            return "NONE";
        }
        return ipAddress;
    }

}
