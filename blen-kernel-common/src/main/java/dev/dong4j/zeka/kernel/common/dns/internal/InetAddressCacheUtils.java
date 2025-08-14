package dev.dong4j.zeka.kernel.common.dns.internal;

import dev.dong4j.zeka.kernel.common.dns.DnsCache;
import dev.dong4j.zeka.kernel.common.dns.DnsCacheEntry;
import dev.dong4j.zeka.kernel.common.dns.DnsCacheManipulator;
import dev.dong4j.zeka.kernel.common.dns.DnsCacheManipulatorException;
import dev.dong4j.zeka.kernel.common.enums.ZekaEnv;
import dev.dong4j.zeka.kernel.common.util.ConfigKit;
import dev.dong4j.zeka.kernel.common.util.JsonUtils;
import dev.dong4j.zeka.kernel.common.util.JustOnceLogger;
import dev.dong4j.zeka.kernel.common.util.StringUtils;
import jakarta.annotation.Nullable;
import jakarta.annotation.concurrent.GuardedBy;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import sun.net.InetAddressCachePolicy;

/**
 * <p>Description: todo-dong4j : (2020.07.2 20:00) [支持 JDK11] </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.06.09 19:52
 * @since 1.5.0
 */
@Slf4j
@SuppressWarnings("all")
public final class InetAddressCacheUtils {

    /**
     * Load dns properties
     *
     * @since 1.5.0
     */
    public static void loadDnsProperties() {
        loadDnsProperties(ZekaEnv.LOCAL.getName());
    }

    /**
     * 通过 profile 加载指定的 dns 配置
     * 默认先读取 includes/dns 目录下的配置, 如果业务端没有配置则读取默认配置
     *
     * @param profile profile
     * @since 1.5.0
     */
    public static void loadDnsProperties(String profile) {
        String dnsConfig = "includes/dns-cache-{}.properties";
        System.setProperty(DnsCacheManipulator.DNS_CONFIG_FILENAME,
            StringUtils.format(dnsConfig, profile));
        String message = "加载自定义 DNS 配置: ";
        try {
            DnsCacheManipulator.loadDnsCacheConfig();
            log.info("{}", JsonUtils.toJson(DnsCacheManipulator.listDnsCache(), true));
        } catch (DnsCacheManipulatorException e) {
            message = "加载默认 DNS 配置: ";

            dnsConfig = "dns-default/dns-cache-{}.properties";
            System.setProperty(DnsCacheManipulator.DNS_CONFIG_FILENAME,
                StringUtils.format("dns-default/dns-cache-{}.properties", profile));
            DnsCacheManipulator.loadDnsCacheConfig();
        }

        message += dnsConfig + "\n{}";

        if (ConfigKit.isDebugModel()) {
            JustOnceLogger.printOnce(InetAddressCacheUtils.class.getName(),
                StringUtils.format(message,
                    profile,
                    JsonUtils.toJson(DnsCacheManipulator.listDnsCache(), true)));
        }
    }

    /**
     * Sets inet address cache *
     *
     * @param host       host
     * @param ips        ips
     * @param expiration expiration
     * @throws NoSuchMethodException     no such method exception
     * @throws UnknownHostException      unknown host exception
     * @throws IllegalAccessException    illegal access exception
     * @throws InstantiationException    instantiation exception
     * @throws InvocationTargetException invocation target exception
     * @throws ClassNotFoundException    class not found exception
     * @throws NoSuchFieldException      no such field exception
     * @since 1.5.0
     */
    public static void setInetAddressCache(String host, String[] ips, long expiration)
        throws NoSuchMethodException, UnknownHostException,
        IllegalAccessException, InstantiationException, InvocationTargetException,
        ClassNotFoundException, NoSuchFieldException {
        host = host.toLowerCase();
        Object entry = newCacheEntry(host, ips, expiration);

        synchronized (getAddressCacheFieldOfInetAddress()) {
            getCacheFiledOfAddressCacheFiledOfInetAddress().put(host, entry);
            getCacheFiledOfNegativeCacheFiledOfInetAddress().remove(host);
        }
    }

    /**
     * Remove inet address cache
     *
     * @param host host
     * @throws NoSuchFieldException   no such field exception
     * @throws IllegalAccessException illegal access exception
     * @since 1.5.0
     */
    public static void removeInetAddressCache(String host)
        throws NoSuchFieldException, IllegalAccessException {
        host = host.toLowerCase();

        synchronized (getAddressCacheFieldOfInetAddress()) {
            getCacheFiledOfAddressCacheFiledOfInetAddress().remove(host);
            getCacheFiledOfNegativeCacheFiledOfInetAddress().remove(host);
        }
    }

    /**
     * New cache entry
     *
     * @param host       host
     * @param ips        ips
     * @param expiration expiration
     * @return the object
     * @throws UnknownHostException      unknown host exception
     * @throws ClassNotFoundException    class not found exception
     * @throws NoSuchMethodException     no such method exception
     * @throws IllegalAccessException    illegal access exception
     * @throws InvocationTargetException invocation target exception
     * @throws InstantiationException    instantiation exception
     * @since 1.5.0
     */
    static @NotNull Object newCacheEntry(String host, String[] ips, long expiration)
        throws UnknownHostException, ClassNotFoundException, NoSuchMethodException,
        IllegalAccessException, InvocationTargetException, InstantiationException {
        String className = "java.net.InetAddress$CacheEntry";
        Class<?> clazz = Class.forName(className);

        // InetAddress.CacheEntry has only a constructor:
        // - for jdk 6, constructor signature is CacheEntry(Object address, long expiration)
        // - for jdk 7+, constructor signature is CacheEntry(InetAddress[] addresses, long expiration)
        // code in jdk 6:
        //   http://hg.openjdk.java.net/jdk6/jdk6/jdk/file/tip/src/share/classes/java/net/InetAddress.java#l739
        // code in jdk 7:
        //   http://hg.openjdk.java.net/jdk7u/jdk7u/jdk/file/tip/src/share/classes/java/net/InetAddress.java#l742
        // code in jdk 8:
        //   http://hg.openjdk.java.net/jdk8u/jdk8u/jdk/file/tip/src/share/classes/java/net/InetAddress.java#l748
        Constructor<?> constructor = clazz.getDeclaredConstructors()[0];
        constructor.setAccessible(true);
        return constructor.newInstance(toInetAddressArray(host, ips), expiration);
    }

    /**
     * Gets cache filed of address cache filed of inet address *
     *
     * @return the cache filed of address cache filed of inet address
     * @throws NoSuchFieldException   no such field exception
     * @throws IllegalAccessException illegal access exception
     * @since 1.5.0
     */
    @GuardedBy("getAddressCacheFieldOfInetAddress()")
    static Map<String, Object> getCacheFiledOfAddressCacheFiledOfInetAddress()
        throws NoSuchFieldException, IllegalAccessException {
        return getCacheFiledOfInetAddress$Cache0(getAddressCacheFieldOfInetAddress());
    }

    /**
     * Gets cache filed of negative cache filed of inet address *
     *
     * @return the cache filed of negative cache filed of inet address
     * @throws NoSuchFieldException   no such field exception
     * @throws IllegalAccessException illegal access exception
     * @since 1.5.0
     */
    @GuardedBy("getAddressCacheFieldOfInetAddress()")
    static Map<String, Object> getCacheFiledOfNegativeCacheFiledOfInetAddress()
        throws NoSuchFieldException, IllegalAccessException {
        return getCacheFiledOfInetAddress$Cache0(getNegativeCacheFieldOfInetAddress());
    }

    /**
     * Get cache filed of inet address cache 0
     *
     * @param inetAddressCache inet address cache
     * @return the map
     * @throws NoSuchFieldException   no such field exception
     * @throws IllegalAccessException illegal access exception
     * @since 1.5.0
     */
    @SuppressWarnings("unchecked")
    static Map<String, Object> getCacheFiledOfInetAddress$Cache0(@NotNull Object inetAddressCache)
        throws NoSuchFieldException, IllegalAccessException {
        Class clazz = inetAddressCache.getClass();

        Field cacheMapField = clazz.getDeclaredField("cache");
        cacheMapField.setAccessible(true);
        return (Map<String, Object>) cacheMapField.get(inetAddressCache);
    }

    /**
     * Gets address cache field of inet address *
     *
     * @return the address cache field of inet address
     * @throws NoSuchFieldException   no such field exception
     * @throws IllegalAccessException illegal access exception
     * @since 1.5.0
     */
    static Object getAddressCacheFieldOfInetAddress()
        throws NoSuchFieldException, IllegalAccessException {
        return getAddressCacheFieldsOfInetAddress0()[0];
    }

    /**
     * Gets negative cache field of inet address *
     *
     * @return the negative cache field of inet address
     * @throws NoSuchFieldException   no such field exception
     * @throws IllegalAccessException illegal access exception
     * @since 1.5.0
     */
    static Object getNegativeCacheFieldOfInetAddress()
        throws NoSuchFieldException, IllegalAccessException {
        return getAddressCacheFieldsOfInetAddress0()[1];
    }

    /** Address cache and negative cache */
    static volatile Object[] ADDRESS_CACHE_AND_NEGATIVE_CACHE = null;

    /**
     * Get address cache fields of inet address 0
     *
     * @return the object [ ]
     * @throws NoSuchFieldException   no such field exception
     * @throws IllegalAccessException illegal access exception
     * @since 1.5.0
     */
    static Object[] getAddressCacheFieldsOfInetAddress0()
        throws NoSuchFieldException, IllegalAccessException {
        if (ADDRESS_CACHE_AND_NEGATIVE_CACHE == null) {
            synchronized (InetAddressCacheUtils.class) {
                if (ADDRESS_CACHE_AND_NEGATIVE_CACHE == null) {  // double check
                    Field cacheField = InetAddress.class.getDeclaredField("addressCache");
                    cacheField.setAccessible(true);

                    Field negativeCacheField = InetAddress.class.getDeclaredField("negativeCache");
                    negativeCacheField.setAccessible(true);

                    ADDRESS_CACHE_AND_NEGATIVE_CACHE = new Object[]{
                        cacheField.get(InetAddress.class),
                        negativeCacheField.get(InetAddress.class)
                    };
                }
            }
        }
        return ADDRESS_CACHE_AND_NEGATIVE_CACHE;
    }

    /**
     * To inet address array
     *
     * @param host host
     * @param ips  ips
     * @return the inet address [ ]
     * @throws UnknownHostException unknown host exception
     * @since 1.5.0
     */
    static InetAddress @NotNull [] toInetAddressArray(String host, String @NotNull [] ips) throws UnknownHostException {
        InetAddress[] addresses = new InetAddress[ips.length];
        for (int i = 0; i < addresses.length; i++) {
            addresses[i] = InetAddress.getByAddress(host, IpParserUtils.ip2ByteArray(ips[i]));
        }

        return addresses;
    }

    /**
     * Gets inet address cache *
     *
     * @param host host
     * @return the inet address cache
     * @throws NoSuchFieldException   no such field exception
     * @throws IllegalAccessException illegal access exception
     * @since 1.5.0
     */
    @Nullable
    public static DnsCacheEntry getInetAddressCache(String host)
        throws NoSuchFieldException, IllegalAccessException {
        host = host.toLowerCase();

        Object cacheEntry;
        synchronized (getAddressCacheFieldOfInetAddress()) {
            cacheEntry = getCacheFiledOfAddressCacheFiledOfInetAddress().get(host);
        }

        if (null == cacheEntry) {
            return null;
        }

        DnsCacheEntry dnsCacheEntry = inetAddress$CacheEntry2DnsCacheEntry(host, cacheEntry);
        if (isDnsCacheEntryExpired(dnsCacheEntry.getHost())) {
            return null;
        }

        return dnsCacheEntry;
    }

    /**
     * Is dns cache entry expired
     *
     * @param host host
     * @return the boolean
     * @since 1.5.0
     */
    @Contract(value = "null -> true", pure = true)
    static boolean isDnsCacheEntryExpired(String host) {
        return null == host || "0.0.0.0".equals(host);
    }

    /**
     * List inet address cache
     *
     * @return the dns cache
     * @throws NoSuchFieldException   no such field exception
     * @throws IllegalAccessException illegal access exception
     * @since 1.5.0
     */
    @Contract(" -> new")
    public static @NotNull DnsCache listInetAddressCache()
        throws NoSuchFieldException, IllegalAccessException {

        Map<String, Object> cache;
        Map<String, Object> negativeCache;
        synchronized (getAddressCacheFieldOfInetAddress()) {
            cache = new HashMap<>(getCacheFiledOfAddressCacheFiledOfInetAddress());
            negativeCache = new HashMap<>(getCacheFiledOfNegativeCacheFiledOfInetAddress());
        }

        List<DnsCacheEntry> retCache = new ArrayList<>();
        for (Map.Entry<String, Object> entry : cache.entrySet()) {
            String host = entry.getKey();

            if (isDnsCacheEntryExpired(host)) { // exclude expired entries!
                continue;
            }
            retCache.add(inetAddress$CacheEntry2DnsCacheEntry(host, entry.getValue()));
        }
        List<DnsCacheEntry> retNegativeCache = new ArrayList<>();
        for (Map.Entry<String, Object> entry : negativeCache.entrySet()) {
            String host = entry.getKey();
            retNegativeCache.add(inetAddress$CacheEntry2DnsCacheEntry(host, entry.getValue()));
        }
        return new DnsCache(retCache, retNegativeCache);
    }


    /** Expiration field of inet address cache entry */
    static volatile Field expirationFieldOfInetAddress$CacheEntry = null;
    /** Addresses field of inet address cache entry */
    static volatile Field addressesFieldOfInetAddress$CacheEntry = null;

    /**
     * Inet address cache entry 2 dns cache entry
     *
     * @param host  host
     * @param entry entry
     * @return the dns cache entry
     * @throws NoSuchFieldException   no such field exception
     * @throws IllegalAccessException illegal access exception
     * @since 1.5.0
     */
    @Contract("_, _ -> new")
    static @NotNull DnsCacheEntry inetAddress$CacheEntry2DnsCacheEntry(String host, Object entry)
        throws NoSuchFieldException, IllegalAccessException {
        if (expirationFieldOfInetAddress$CacheEntry == null || addressesFieldOfInetAddress$CacheEntry == null) {
            synchronized (InetAddressCacheUtils.class) {
                if (expirationFieldOfInetAddress$CacheEntry == null) { // double check
                    Class<?> cacheEntryClass = entry.getClass();
                    // InetAddress.CacheEntry has 2 filed:
                    // - for jdk 6, address and expiration
                    // - for jdk 7+, addresses(*renamed* from 6!) and expiration
                    // code in jdk 6:
                    //   http://hg.openjdk.java.net/jdk6/jdk6/jdk/file/tip/src/share/classes/java/net/InetAddress.java#l739
                    // code in jdk 7:
                    //   http://hg.openjdk.java.net/jdk7u/jdk7u/jdk/file/tip/src/share/classes/java/net/InetAddress.java#l742
                    // code in jdk 8:
                    //   http://hg.openjdk.java.net/jdk8u/jdk8u/jdk/file/tip/src/share/classes/java/net/InetAddress.java#l748
                    Field[] fields = cacheEntryClass.getDeclaredFields();
                    for (Field field : fields) {
                        String name = field.getName();
                        if (name.equals("expiration")) {
                            field.setAccessible(true);
                            expirationFieldOfInetAddress$CacheEntry = field;
                        } else if (name.startsWith("address")) {
                            field.setAccessible(true);
                            addressesFieldOfInetAddress$CacheEntry = field;
                        } else {
                            throw new IllegalStateException("JDK add new Field " + name +
                                " for class InetAddress.CacheEntry, report bug for dns-cache-manipulator lib!");
                        }
                    }
                }
            }
        }

        long expiration = (Long) expirationFieldOfInetAddress$CacheEntry.get(entry);
        InetAddress[] addresses = (InetAddress[]) addressesFieldOfInetAddress$CacheEntry.get(entry);

        String[] ips = new String[addresses.length];
        for (int i = 0; i < addresses.length; i++) {
            ips[i] = addresses[i].getHostAddress();
        }
        return new DnsCacheEntry(host, ips, new Date(expiration));
    }

    /**
     * Clear inet address cache
     *
     * @throws NoSuchFieldException   no such field exception
     * @throws IllegalAccessException illegal access exception
     * @since 1.5.0
     */
    public static void clearInetAddressCache() throws NoSuchFieldException, IllegalAccessException {
        synchronized (getAddressCacheFieldOfInetAddress()) {
            getCacheFiledOfAddressCacheFiledOfInetAddress().clear();
            getCacheFiledOfNegativeCacheFiledOfInetAddress().clear();
        }
    }

    /**
     * Sets dns cache policy *
     *
     * @param cacheSeconds cache seconds
     * @throws NoSuchFieldException   no such field exception
     * @throws IllegalAccessException illegal access exception
     * @since 1.5.0
     */
    public static void setDnsCachePolicy(int cacheSeconds)
        throws NoSuchFieldException, IllegalAccessException {
        setCachePolicy0(false, cacheSeconds);
    }

    /**
     * Gets dns cache policy *
     *
     * @return the dns cache policy
     * @throws NoSuchFieldException   no such field exception
     * @throws IllegalAccessException illegal access exception
     * @since 1.5.0
     */
    @Contract(pure = true)
    public static int getDnsCachePolicy()
        throws NoSuchFieldException, IllegalAccessException {
        return InetAddressCachePolicy.get();
    }

    /**
     * Sets dns negative cache policy *
     *
     * @param negativeCacheSeconds negative cache seconds
     * @throws NoSuchFieldException   no such field exception
     * @throws IllegalAccessException illegal access exception
     * @since 1.5.0
     */
    public static void setDnsNegativeCachePolicy(int negativeCacheSeconds)
        throws NoSuchFieldException, IllegalAccessException {
        setCachePolicy0(true, negativeCacheSeconds);
    }

    /**
     * Gets dns negative cache policy *
     *
     * @return the dns negative cache policy
     * @throws NoSuchFieldException   no such field exception
     * @throws IllegalAccessException illegal access exception
     * @since 1.5.0
     */
    @Contract(pure = true)
    public static int getDnsNegativeCachePolicy()
        throws NoSuchFieldException, IllegalAccessException {
        return InetAddressCachePolicy.getNegative();
    }

    /** Set filed inet address cache policy */
    static volatile Field setFiled$InetAddressCachePolicy = null;
    /** Negative set inet address cache policy */
    static volatile Field negativeSet$InetAddressCachePolicy = null;

    /**
     * Sets cache policy 0 *
     *
     * @param isNegative is negative
     * @param seconds    seconds
     * @throws NoSuchFieldException   no such field exception
     * @throws IllegalAccessException illegal access exception
     * @since 1.5.0
     */
    static void setCachePolicy0(boolean isNegative, int seconds)
        throws NoSuchFieldException, IllegalAccessException {
        if (seconds < 0) {
            seconds = -1;
        }

        final Class<?> clazz = InetAddressCachePolicy.class;
        Field cachePolicyFiled = clazz.getDeclaredField(
            isNegative ? "negativeCachePolicy" : "cachePolicy");
        cachePolicyFiled.setAccessible(true);

        Field setField;
        if (isNegative) {
            if (negativeSet$InetAddressCachePolicy == null) {
                synchronized (InetAddressCacheUtils.class) {
                    if (negativeSet$InetAddressCachePolicy == null) {
                        try {
                            negativeSet$InetAddressCachePolicy = clazz.getDeclaredField("propertyNegativeSet");
                        } catch (NoSuchFieldException e) {
                            negativeSet$InetAddressCachePolicy = clazz.getDeclaredField("negativeSet");
                        }
                        negativeSet$InetAddressCachePolicy.setAccessible(true);
                    }
                }
            }
            setField = negativeSet$InetAddressCachePolicy;
        } else {
            if (setFiled$InetAddressCachePolicy == null) {
                synchronized (InetAddressCacheUtils.class) {
                    if (setFiled$InetAddressCachePolicy == null) {
                        try {
                            setFiled$InetAddressCachePolicy = clazz.getDeclaredField("propertySet");
                        } catch (NoSuchFieldException e) {
                            setFiled$InetAddressCachePolicy = clazz.getDeclaredField("set");
                        }
                        setFiled$InetAddressCachePolicy.setAccessible(true);
                    }
                }
            }
            setField = setFiled$InetAddressCachePolicy;
        }

        synchronized (InetAddressCachePolicy.class) { // static synchronized method!
            cachePolicyFiled.set(null, seconds);
            setField.set(null, true);
        }
    }

    /**
     * Inet address cache util
     *
     * @since 1.5.0
     */
    @Contract(pure = true)
    private InetAddressCacheUtils() {
    }
}
