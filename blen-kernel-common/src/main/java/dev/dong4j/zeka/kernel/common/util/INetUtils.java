package dev.dong4j.zeka.kernel.common.util;

import java.io.Closeable;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SystemUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.07.02 12:00
 * @since 1.5.0
 */
@Slf4j
@SuppressWarnings("all")
abstract class INetUtils {
    /** returned port range is [30000, 39999] */
    private static final int RND_PORT_START = 30000;
    /** RND_PORT_RANGE */
    private static final int RND_PORT_RANGE = 10000;
    /** ADDRESS_PATTERN */
    private static final Pattern ADDRESS_PATTERN = Pattern.compile("^\\d{1,3}(\\.\\d{1,3}){3}\\:\\d{1,5}$");
    /** LOCAL_IP_PATTERN */
    private static final Pattern LOCAL_IP_PATTERN = Pattern.compile("127(\\.\\d{1,3}){3}$");
    /** IP_PATTERN */
    private static final Pattern IP_PATTERN = Pattern.compile("\\d{1,3}(\\.\\d{1,3}){3,5}$");
    /** HOST_NAME_CACHE */
    private static final Map<String, String> HOST_NAME_CACHE = new LRUCache<>(1000);
    /** LOCAL_ADDRESS */
    private static volatile InetAddress localAddress = null;
    /** SPLIT_IPV4_CHARECTER */
    private static final String SPLIT_IPV4_CHARECTER = "\\.";
    /** SPLIT_IPV6_CHARECTER */
    private static final String SPLIT_IPV6_CHARECTER = ":";
    /** ANYHOST_VALUE */
    public static final String ANYHOST_VALUE = "0.0.0.0";
    /** DUBBO_IP_TO_BIND */
    public static final String IP_TO_BIND = "ZEKA_IP_TO_BIND";
    /** LOCALHOST_KEY */
    public static final String LOCALHOST_KEY = InetUtilsProperties.LOCALHOST_KEY;
    /** LOCALHOST_VALUE */
    public static final String LOCALHOST_VALUE = InetUtilsProperties.LOCALHOST_VALUE;
    /** MIN_PORT */
    public static final int MIN_PORT = 0;
    /** MAX_PORT */
    public static final int MAX_PORT = 65535;

    /**
     * Gets random port *
     *
     * @return the random port
     * @since 1.5.0
     */
    public static int getRandomPort() {
        return RND_PORT_START + ThreadLocalRandom.current().nextInt(RND_PORT_RANGE);
    }

    /**
     * Gets available port *
     *
     * @return the available port
     * @since 1.5.0
     */
    public static int getAvailablePort() {
        try (ServerSocket ss = new ServerSocket()) {
            ss.bind(null);
            return ss.getLocalPort();
        } catch (IOException e) {
            return getRandomPort();
        }
    }

    /**
     * Gets available port *
     *
     * @param port port
     * @return the available port
     * @since 1.5.0
     */
    public static int getAvailablePort(int port) {
        if (port <= 0) {
            return getAvailablePort();
        }
        for (int i = port; i < MAX_PORT; i++) {
            try (ServerSocket ss = new ServerSocket(i)) {
                int available = i;
                // 资源会在 try 块结束时自动关闭
                return available;
            } catch (IOException ignore) {
            }
        }
        return port;
    }

    /**
     * Is invalid port
     *
     * @param port port
     * @return the boolean
     * @since 1.5.0
     */
    @Contract(pure = true)
    public static boolean isInvalidPort(int port) {
        return port <= MIN_PORT || port > MAX_PORT;
    }

    /**
     * Is valid address
     *
     * @param address address
     * @return the boolean
     * @since 1.5.0
     */
    public static boolean isValidAddress(String address) {
        return ADDRESS_PATTERN.matcher(address).matches();
    }

    /**
     * Is local host
     *
     * @param host host
     * @return the boolean
     * @since 1.5.0
     */
    @Contract("null -> false")
    public static boolean isLocalHost(String host) {
        return host != null
            && (LOCAL_IP_PATTERN.matcher(host).matches()
            || host.equalsIgnoreCase(LOCALHOST_KEY));
    }

    /**
     * Is any host
     *
     * @param host host
     * @return the boolean
     * @since 1.5.0
     */
    @Contract(value = "null -> false", pure = true)
    public static boolean isAnyHost(String host) {
        return ANYHOST_VALUE.equals(host);
    }

    /**
     * Is invalid local host
     *
     * @param host host
     * @return the boolean
     * @since 1.5.0
     */
    @Contract("null -> true")
    public static boolean isInvalidLocalHost(String host) {
        return host == null
            || host.length() == 0
            || host.equalsIgnoreCase(LOCALHOST_KEY)
            || host.equals(ANYHOST_VALUE)
            || (LOCAL_IP_PATTERN.matcher(host).matches());
    }

    /**
     * Is valid local host
     *
     * @param host host
     * @return the boolean
     * @since 1.5.0
     */
    @Contract("null -> false")
    public static boolean isValidLocalHost(String host) {
        return !isInvalidLocalHost(host);
    }

    /**
     * Gets local socket address *
     *
     * @param host host
     * @param port port
     * @return the local socket address
     * @since 1.5.0
     */
    public static @NotNull InetSocketAddress getLocalSocketAddress(String host, int port) {
        return isInvalidLocalHost(host)
            ? new InetSocketAddress(port)
            : new InetSocketAddress(host, port);
    }

    /**
     * Is valid v 4 address
     *
     * @param address address
     * @return the boolean
     * @since 1.5.0
     */
    @Contract("null -> false")
    static boolean isValidV4Address(InetAddress address) {
        if (address == null || address.isLoopbackAddress()) {
            return false;
        }
        String name = address.getHostAddress();
        return (name != null
            && IP_PATTERN.matcher(name).matches()
            && !ANYHOST_VALUE.equals(name)
            && !LOCALHOST_VALUE.equals(name));
    }

    /**
     * Check if an ipv6 address
     *
     * @return true if it is reachable
     * @since 1.5.0
     */
    @Contract(pure = true)
    static boolean isPreferIPV6Address() {
        return Boolean.getBoolean("java.net.preferIPv6Addresses");
    }

    /**
     * normalize the ipv6 Address, convert scope name to scope id.
     * e.g.
     * convert
     * fe80:0:0:0:894:aeec:f37d:23e1%en0
     * to
     * fe80:0:0:0:894:aeec:f37d:23e1%5
     * <p>
     * The %5 after ipv6 address is called scope id.
     * see java doc of {@link Inet6Address} for more details.
     *
     * @param address the input address
     * @return the normalized address, with scope id converted to int
     * @since 1.5.0
     */
    static InetAddress normalizeV6Address(@NotNull Inet6Address address) {
        String addr = address.getHostAddress();
        int i = addr.lastIndexOf('%');
        if (i > 0) {
            try {
                return TimeoutUtils.process(new Callable<InetAddress>() {
                    @Override
                    public InetAddress call() throws Exception {
                        return InetAddress.getByName(addr.substring(0, i) + '%' + address.getScopeId());
                    }
                }, 1000);
            } catch (Exception e) {
                // ignore
                log.debug("Unknown IPV6 address: ", e);
            }
        }
        return address;
    }

    /**
     * Gets local host *
     *
     * @return the local host
     * @since 1.5.0
     */
    public static String getLocalHost() {
        InetAddress address = getLocalAddress();
        return address == null ? LOCALHOST_VALUE : address.getHostAddress();
    }

    /**
     * Gets ip by config *
     * todo-dong4j : (2021.01.24 02:46) [从配置文件中加载]
     *
     * @return the ip by config
     * @since 1.5.0
     */
    public static String getIpByConfig() {
        String configIp = System.getProperty(IP_TO_BIND);
        if (configIp != null) {
            return configIp;
        }

        return getIpByHost(getLocalAddress().getHostName());
    }

    /**
     * Find first valid IP from local network card
     *
     * @return first valid local IP
     * @since 1.5.0
     */
    @SuppressWarnings("all")
    public static InetAddress getLocalAddress() {
        if (localAddress != null) {
            return localAddress;
        }

        InetAddress localAddress = getLocalAddress0();
        if (localAddress == null || LOCALHOST_VALUE.equals(localAddress.getHostAddress())) {
            log.warn("多网卡下获取服务器 IP[127.0.0.1] 错误，使用备用方案重新获取, 可通过添加 -D 直接覆盖 zeka-stack.system.properties 配置");
            InetUtils inetUtils = new InetUtils(new InetUtilsProperties());
            localAddress = inetUtils.findFirstNonLoopbackAddress();
        }

        INetUtils.localAddress = localAddress;
        return localAddress;
    }

    /**
     * To valid address
     *
     * @param address address
     * @return the optional
     * @since 1.5.0
     */
    private static Optional<InetAddress> toValidAddress(InetAddress address) {
        if (address instanceof Inet6Address) {
            Inet6Address v6Address = (Inet6Address) address;
            if (isPreferIPV6Address()) {
                return Optional.ofNullable(normalizeV6Address(v6Address));
            }
        }
        if (isValidV4Address(address)) {
            return Optional.of(address);
        }
        return Optional.empty();
    }

    /**
     * https://stackoverflow.com/questions/9481865/getting-the-ip-address-of-the-current-machine-using-java
     * 返回一个 InetAddress 对象, 该对象封装了最有可能是计算机的 LAN IP 地址.
     * 此方法用于替代 JDK 方法 InetAddress.getLocalHost, 因为该方法在 Linux 系统上不明确.
     * Linux 系统枚举环回网络接口的方式与常规 LAN 网络接口相同, 但 JDKInetAddress.getLocalHost 方法不指定用于选择在这种情况下返回的地址的算法,
     * 并且通常会返回环回地址, 这对网络通信无效. 详情请看这里.  此方法将扫描主机上所有网络接口上的所有 IP 地址, 以确定最有可能是计算机的 LAN 地址的 IP 地址.
     * 如果计算机有多个 IP 地址, 则如果计算机有一个站点本地 IP 地址 (例如 192.168.x.x 或 10.10.x.x, 通常是 IPv4) ,
     * 则此方法将首选该站点本地 IP 地址 (如果计算机有多个站点本地地址, 则返回第一个站点本地地址) , 但如果计算机没有保存站点本地地址,
     * 则此方法将只返回第一个非环回找到地址 (IPv4 或 IPv6) .
     * 如果此方法使用此选择算法找不到非环回地址, 则返回调用并返回 JDK 方法的结果 InetAddress
     * <p/>
     * 127.xxx.xxx.xxx 属于 loopback 地址, 即只能你自己的本机可见, 就是本机地址, 比较常见的有 127.0.0.1;
     * 192.168.xxx.xxx(10.xxx.xxx.xxx、从172.16.xxx.xxx 到 172.31.xxx) 属于 private 私有地址(site local address), 只能在本地局域网可见;
     * 169.254.xxx.xxx 属于连接本地地址 (link local IP), 在单独网段可用
     * 224.xxx.xxx.xxx 到 239.xxx.xxx.xxx 属于组播地址
     * 比较特殊的 255.255.255.255 属于广播地址
     * 除此之外的地址就是点对点的可用的公开 IPv4 地址
     *
     * @return the local host lan address
     * @throws UnknownHostException If the LAN address of the machine cannot be found.
     * @since 1.0.0
     */
    private static @Nullable InetAddress getLocalAddress0() {
        InetAddress localAddress = null;

        try {
            // 遍历所有的网络接口
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            if (null == interfaces) {
                return localAddress;
            }
            while (interfaces.hasMoreElements()) {
                try {
                    // 过滤掉 loopback 设备、虚拟网卡和不可用的网卡
                    NetworkInterface network = interfaces.nextElement();
                    if (network.isLoopback() || network.isVirtual() || !network.isUp()
                        // 如果是 mac 系统, 不使用 wifi
                        || (network.getDisplayName().equals("en1") && SystemUtils.IS_OS_MAC_OSX)) {
                        continue;
                    }

                    // 在所有的接口下再遍历 IP
                    Enumeration<InetAddress> addresses = network.getInetAddresses();
                    while (addresses.hasMoreElements()) {
                        InetAddress inetAddress = addresses.nextElement();
                        // 使用局域网 ip
                        if (inetAddress.isSiteLocalAddress()) {
                            try {
                                Optional<InetAddress> addressOp = toValidAddress(inetAddress);
                                if (addressOp.isPresent()) {
                                    try {
                                        if (addressOp.get().isReachable(100)) {
                                            return addressOp.get();
                                        }
                                    } catch (IOException ignored) {
                                    }
                                }
                            } catch (Throwable e) {
                                log.warn("", e);
                            }
                        }
                    }
                } catch (Throwable e) {
                    log.warn("", e);
                }
            }
        } catch (Throwable e) {
            log.warn("", e);
        }
        return localAddress;
    }

    /**
     * Gets host name *
     *
     * @param address address
     * @return the host name
     * @since 1.5.0
     */
    public static String getHostName(String address) {
        int i = address.indexOf(':');
        if (i > -1) {
            address = address.substring(0, i);
        }
        String hostname = HOST_NAME_CACHE.get(address);
        if (hostname != null && hostname.length() > 0) {
            return hostname;
        }
        String finalAddress = address;
        InetAddress inetAddress = getInetAddressWithTimeout(finalAddress);

        if (inetAddress != null) {
            hostname = inetAddress.getHostName();
            HOST_NAME_CACHE.put(address, hostname);
            return hostname;
        }

        return address;
    }

    /**
     * Gets ip by host *
     *
     * @param hostName host name
     * @return ip address or hostName if UnknownHostException
     * @since 1.5.0
     */
    public static String getIpByHost(String hostName) {
        InetAddress inetAddress = getInetAddressWithTimeout(hostName);
        return inetAddress != null ? inetAddress.getHostAddress() : hostName;
    }

    /**
     * {@link java.net.InetAddress#getByName(java.lang.String)} 有可能会走 DNS (java.net.InetAddress#getAddressesFromNameService) 查询,
     * 如果 DNS 服务挂掉或长时间未返回数据, 将导致应用阻塞, 因此添加一个超时逻辑(1 秒).
     *
     * @param hostName host name
     * @return the inet address with timeout
     * @since 1.7.1
     */
    public static InetAddress getInetAddressWithTimeout(String hostName) {
        return getInetAddressWithTimeout(hostName, 1000);
    }

    /**
     * Gets inet address with timeout *
     *
     * @param hostName host name
     * @param timeout  timeout
     * @return the inet address with timeout
     * @since 1.7.1
     */
    public static InetAddress getInetAddressWithTimeout(String hostName, long timeout) {
        try {
            return TimeoutUtils.process(new Callable<InetAddress>() {
                @Override
                public InetAddress call() throws Exception {
                    return InetAddress.getByName(hostName);
                }
            }, timeout);
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * To address string
     *
     * @param address address
     * @return the string
     * @since 1.5.0
     */
    public static @NotNull String toAddressString(@NotNull InetSocketAddress address) {
        return address.getAddress().getHostAddress() + ":" + address.getPort();
    }

    /**
     * To address
     *
     * @param address address
     * @return the inet socket address
     * @since 1.5.0
     */
    @Contract("_ -> new")
    public static @NotNull InetSocketAddress toAddress(@NotNull String address) {
        int i = address.indexOf(':');
        String host;
        int port;
        if (i > -1) {
            host = address.substring(0, i);
            port = Integer.parseInt(address.substring(i + 1));
        } else {
            host = address;
            port = 0;
        }
        return new InetSocketAddress(host, port);
    }

    /**
     * To url
     *
     * @param protocol protocol
     * @param host     host
     * @param port     port
     * @param path     path
     * @return the string
     * @since 1.5.0
     */
    public static @NotNull String toURL(String protocol, String host, int port, @NotNull String path) {
        StringBuilder sb = new StringBuilder();
        sb.append(protocol).append("://");
        sb.append(host).append(':').append(port);
        if (path.charAt(0) != '/') {
            sb.append('/');
        }
        sb.append(path);
        return sb.toString();
    }

    /**
     * Join multicast group
     *
     * @param multicastSocket  multicast socket
     * @param multicastAddress multicast address
     * @throws IOException io exception
     * @since 1.5.0
     */
    public static void joinMulticastGroup(MulticastSocket multicastSocket, InetAddress multicastAddress) throws IOException {
        setInterface(multicastSocket, multicastAddress instanceof Inet6Address);
        multicastSocket.setLoopbackMode(false);
        multicastSocket.joinGroup(multicastAddress);
    }

    /**
     * Sets interface *
     *
     * @param multicastSocket multicast socket
     * @param preferIpv6      prefer ipv 6
     * @throws IOException io exception
     * @since 1.5.0
     */
    public static void setInterface(MulticastSocket multicastSocket, boolean preferIpv6) throws IOException {
        boolean interfaceSet = false;
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        while (interfaces.hasMoreElements()) {
            NetworkInterface ni = interfaces.nextElement();
            Enumeration<InetAddress> addresses = ni.getInetAddresses();
            while (addresses.hasMoreElements()) {
                InetAddress address = addresses.nextElement();
                if (preferIpv6 && address instanceof Inet6Address) {
                    try {
                        if (address.isReachable(100)) {
                            multicastSocket.setNetworkInterface(ni); // ✅ 新写法
                            interfaceSet = true;
                            break;
                        }
                    } catch (IOException ignored) {
                    }
                } else if (!preferIpv6 && address instanceof Inet4Address) {
                    try {
                        if (address.isReachable(100)) {
                            multicastSocket.setNetworkInterface(ni); // ✅ 新写法
                            interfaceSet = true;
                            break;
                        }
                    } catch (IOException ignored) {
                    }
                }
            }
            if (interfaceSet) {
                break;
            }
        }
    }

    /**
     * Match ip expression
     *
     * @param pattern pattern
     * @param host    host
     * @param port    port
     * @return the boolean
     * @throws UnknownHostException unknown host exception
     * @since 1.5.0
     */
    public static boolean matchIpExpression(@NotNull String pattern, String host, int port) throws UnknownHostException {

        // if the pattern is subnet format, it will not be allowed to config port param in pattern.
        if (pattern.contains("/")) {
            CIDRUtils utils = new CIDRUtils(pattern);
            return utils.isInRange(host);
        }

        return matchIpRange(pattern, host, port);
    }

    /**
     * Match ip range
     *
     * @param pattern pattern
     * @param host    host
     * @param port    port
     * @return boolean boolean
     * @throws UnknownHostException unknown host exception
     * @since 1.5.0
     */
    @SneakyThrows
    @Contract("null, _, _ -> fail; !null, null, _ -> fail")
    public static boolean matchIpRange(String pattern, String host, int port) throws UnknownHostException {
        if (pattern == null || host == null) {
            throw new IllegalArgumentException("Illegal Argument pattern or hostName. Pattern:" + pattern + ", Host:" + host);
        }
        pattern = pattern.trim();
        if ("*.*.*.*".equals(pattern) || "*".equals(pattern)) {
            return true;
        }

        String finalHost = host;
        InetAddress inetAddress = getInetAddressWithTimeout(finalHost);

        if (inetAddress == null) {
            return false;
        }

        boolean isIpv4 = isValidV4Address(inetAddress);
        String[] hostAndPort = getPatternHostAndPort(pattern, isIpv4);
        if (hostAndPort[1] != null && !hostAndPort[1].equals(String.valueOf(port))) {
            return false;
        }
        pattern = hostAndPort[0];

        String splitCharacter = SPLIT_IPV4_CHARECTER;
        if (!isIpv4) {
            splitCharacter = SPLIT_IPV6_CHARECTER;
        }
        String[] mask = pattern.split(splitCharacter);
        // check format of pattern
        checkHostPattern(pattern, mask, isIpv4);

        host = inetAddress.getHostAddress();

        String[] ipAddress = host.split(splitCharacter);
        if (pattern.equals(host)) {
            return true;
        }
        // short name condition
        if (!ipPatternContainExpression(pattern)) {
            String finalPattern = pattern;
            InetAddress patternAddress = getInetAddressWithTimeout(finalPattern);
            return inetAddress == null ? false : patternAddress.getHostAddress().equals(host);
        }
        for (int i = 0; i < mask.length; i++) {
            if ("*".equals(mask[i]) || mask[i].equals(ipAddress[i])) {
                continue;
            } else if (mask[i].contains("-")) {
                String[] rangeNumStrs = mask[i].split("-");
                if (rangeNumStrs.length != 2) {
                    throw new IllegalArgumentException("There is wrong format of ip Address: " + mask[i]);
                }
                Integer min = getNumOfIpSegment(rangeNumStrs[0], isIpv4);
                Integer max = getNumOfIpSegment(rangeNumStrs[1], isIpv4);
                Integer ip = getNumOfIpSegment(ipAddress[i], isIpv4);
                if (ip < min || ip > max) {
                    return false;
                }
            } else if ("0".equals(ipAddress[i])
                && ("0".equals(mask[i]) || "00".equals(mask[i]) || "000".equals(mask[i]) || "0000".equals(mask[i]))) {
                continue;
            } else if (!mask[i].equals(ipAddress[i])) {
                return false;
            }
        }
        return true;
    }

    /**
     * Ip pattern contain expression
     *
     * @param pattern pattern
     * @return the boolean
     * @since 1.5.0
     */
    private static boolean ipPatternContainExpression(@NotNull String pattern) {
        return pattern.contains("*") || pattern.contains("-");
    }

    /**
     * Check host pattern
     *
     * @param pattern pattern
     * @param mask    mask
     * @param isIpv4  is ipv 4
     * @since 1.5.0
     */
    private static void checkHostPattern(String pattern, String[] mask, boolean isIpv4) {
        if (!isIpv4) {
            if (mask.length != 8 && ipPatternContainExpression(pattern)) {
                throw new IllegalArgumentException("If you config ip expression that contains '*' or '-', "
                    + "please fill qulified ip pattern like 234e:0:4567:0:0:0:3d:*. ");
            }
            if (mask.length != 8 && !pattern.contains("::")) {
                throw new IllegalArgumentException("The host is ipv6, but the pattern is not ipv6 pattern : " + pattern);
            }
        } else {
            if (mask.length != 4) {
                throw new IllegalArgumentException("The host is ipv4, but the pattern is not ipv4 pattern : " + pattern);
            }
        }
    }

    /**
     * Get pattern host and port
     *
     * @param pattern pattern
     * @param isIpv4  is ipv 4
     * @return the string [ ]
     * @since 1.5.0
     */
    private static String @NotNull [] getPatternHostAndPort(@NotNull String pattern, boolean isIpv4) {
        String[] result = new String[2];
        if (pattern.startsWith("[") && pattern.contains("]:")) {
            int end = pattern.indexOf("]:");
            result[0] = pattern.substring(1, end);
            result[1] = pattern.substring(end + 2);
            return result;
        } else if (pattern.startsWith("[") && pattern.endsWith("]")) {
            result[0] = pattern.substring(1, pattern.length() - 1);
            result[1] = null;
            return result;
        } else if (isIpv4 && pattern.contains(":")) {
            int end = pattern.indexOf(":");
            result[0] = pattern.substring(0, end);
            result[1] = pattern.substring(end + 1);
            return result;
        } else {
            result[0] = pattern;
            return result;
        }
    }

    /**
     * Gets num of ip segment *
     *
     * @param ipSegment ip segment
     * @param isIpv4    is ipv 4
     * @return the num of ip segment
     * @since 1.5.0
     */
    private static @NotNull Integer getNumOfIpSegment(String ipSegment, boolean isIpv4) {
        if (isIpv4) {
            return Integer.parseInt(ipSegment);
        }
        return Integer.parseInt(ipSegment, 16);
    }

    /**
     * <p>Description: </p>
     *
     * @author dong4j
     * @version 1.0.0
     * @email "mailto:dong4j@gmail.com"
     * @date 2022.02.11 15:27
     * @since 2022.1.1
     */
    private static class InetUtils implements Closeable {
        /** Executor service */
        private final ExecutorService executorService;
        /** Properties */
        private final InetUtilsProperties properties;

        /**
         * Inet utils
         *
         * @param properties properties
         * @since 2022.1.1
         */
        InetUtils(final InetUtilsProperties properties) {
            this.properties = properties;
            this.executorService = Executors.newSingleThreadExecutor(r -> {
                Thread thread = new Thread(r);
                thread.setName("INetUtils-Thread");
                thread.setDaemon(true);
                return thread;
            });
        }

        /**
         * Close
         *
         * @since 2022.1.1
         */
        @Override
        public void close() {
            this.executorService.shutdown();
        }

        /**
         * Find first non loopback host info
         *
         * @return the host info
         * @since 2022.1.1
         */
        HostInfo findFirstNonLoopbackHostInfo() {
            InetAddress address = findFirstNonLoopbackAddress();
            if (address != null) {
                return convertAddress(address);
            }
            HostInfo hostInfo = new HostInfo();
            hostInfo.setHostname(this.properties.getDefaultHostname());
            hostInfo.setIpAddress(this.properties.getDefaultIpAddress());
            return hostInfo;
        }

        /**
         * Find first non loopback address
         *
         * @return the inet address
         * @since 2022.1.1
         */
        InetAddress findFirstNonLoopbackAddress() {
            InetAddress result = null;
            try {
                int lowest = Integer.MAX_VALUE;
                for (Enumeration<NetworkInterface> nics = NetworkInterface
                    .getNetworkInterfaces(); nics.hasMoreElements(); ) {
                    NetworkInterface ifc = nics.nextElement();
                    if (ifc.isUp()) {
                        log.trace("Testing interface: " + ifc.getDisplayName());
                        if (ifc.getIndex() < lowest || result == null) {
                            lowest = ifc.getIndex();
                        } else if (result != null) {
                            continue;
                        }

                        // @formatter:off
                        if (!ignoreInterface(ifc.getDisplayName())) {
                            for (Enumeration<InetAddress> addrs = ifc
                                .getInetAddresses(); addrs.hasMoreElements();) {
                                InetAddress address = addrs.nextElement();
                                if (address instanceof Inet4Address
                                    && !address.isLoopbackAddress()
                                    && isPreferredAddress(address)) {
                                    log.trace("Found non-loopback interface: "
                                                   + ifc.getDisplayName());
                                    result = address;
                                }
                            }
                        }
                        // @formatter:on
                    }
                }
            } catch (IOException ex) {
                log.error("Cannot get first non-loopback address", ex);
            }

            if (result != null) {
                return result;
            }

            try {
                return InetAddress.getLocalHost();
            } catch (UnknownHostException e) {
                log.warn("Unable to retrieve localhost");
            }

            return null;
        }

        /**
         * Is preferred address
         *
         * @param address address
         * @return the boolean
         * @since 2022.1.1
         */
        boolean isPreferredAddress(InetAddress address) {

            if (this.properties.isUseOnlySiteLocalInterfaces()) {
                final boolean siteLocalAddress = address.isSiteLocalAddress();
                if (!siteLocalAddress) {
                    log.trace("Ignoring address: " + address.getHostAddress());
                }
                return siteLocalAddress;
            }
            final List<String> preferredNetworks = this.properties.getPreferredNetworks();
            if (preferredNetworks.isEmpty()) {
                return true;
            }
            for (String regex : preferredNetworks) {
                final String hostAddress = address.getHostAddress();
                if (hostAddress.matches(regex) || hostAddress.startsWith(regex)) {
                    return true;
                }
            }
            log.trace("Ignoring address: " + address.getHostAddress());
            return false;
        }

        /**
         * Ignore interface
         *
         * @param interfaceName interface name
         * @return the boolean
         * @since 2022.1.1
         */
        boolean ignoreInterface(String interfaceName) {
            for (String regex : this.properties.getIgnoredInterfaces()) {
                if (interfaceName.matches(regex)) {
                    log.trace("Ignoring interface: " + interfaceName);
                    return true;
                }
            }
            return false;
        }

        /**
         * Convert address
         *
         * @param address address
         * @return the host info
         * @since 2022.1.1
         */
        HostInfo convertAddress(final InetAddress address) {
            HostInfo hostInfo = new HostInfo();
            Future<String> result = this.executorService.submit(address::getHostName);

            String hostname;
            try {
                hostname = result.get(this.properties.getTimeoutSeconds(), TimeUnit.SECONDS);
            } catch (Exception e) {
                log.info("Cannot determine local hostname");
                hostname = "localhost";
            }
            hostInfo.setHostname(hostname);
            hostInfo.setIpAddress(address.getHostAddress());
            return hostInfo;
        }

        /**
         * <p>Description: </p>
         *
         * @author dong4j
         * @version 1.0.0
         * @email "mailto:dong4j@gmail.com"
         * @date 2022.02.11 15:27
         * @since 2022.1.1
         */
        @Data
        private static class HostInfo {
            /** Override */
            public boolean override;
            /** Ip address */
            private String ipAddress;
            /** Hostname */
            private String hostname;

            /**
             * Gets ip address as int *
             *
             * @return the ip address as int
             * @since 2022.1.1
             */
            public int getIpAddressAsInt() {
                InetAddress inetAddress = null;
                String host = this.ipAddress;
                if (host == null) {
                    host = this.hostname;
                }
                try {
                    inetAddress = InetAddress.getByName(host);
                } catch (final UnknownHostException e) {
                    throw new IllegalArgumentException(e);
                }
                return ByteBuffer.wrap(inetAddress.getAddress()).getInt();
            }

        }
    }
}
