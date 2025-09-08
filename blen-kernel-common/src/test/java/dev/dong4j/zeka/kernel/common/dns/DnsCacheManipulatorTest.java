package dev.dong4j.zeka.kernel.common.dns;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static java.lang.System.currentTimeMillis;
import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.06.09 19:53
 * @since 1.0.0
 */
@SuppressWarnings("all")
class DnsCacheManipulatorTest {
    /** DOMAIN1 */
    private static final String DOMAIN1 = "www.hello1.com";
    /** IP1 */
    private static final String IP1 = "42.42.41.41";
    /** DOMAIN2 */
    private static final String DOMAIN2 = "www.hello2.com";
    /** IP2 */
    private static final String IP2 = "42.42.41.42";
    /** IP3 */
    private static final String IP3 = "42.42.43.43";
    /** DOMAIN_CUSTOMIZED */
    private static final String DOMAIN_CUSTOMIZED = "nacos.server";
    /** IP_CUSTOMIZED */
    private static final String IP_CUSTOMIZED = "192.168.2.81";

    /** DOMAIN_NOT_EXISTED */
    private static final String DOMAIN_NOT_EXISTED = "www.domain-not-existed-7352jt-12559-AZ-7524087.com";

    /**
     * Before class
     * / https://docs.oracle.com/javase/tutorial/essential/environment/sysprop.html
     *
     * @since 1.0.0
     */
    @BeforeAll
    static void beforeClass() {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        System.out.printf("Env info:\njava home: %s\njdk version: %s\n",
            System.getProperty("java.home"),
            System.getProperty("java.version"));
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    }

    /**
     * Before
     *
     * @since 1.0.0
     */
    @BeforeEach
    void before() {
        DnsCacheManipulator.clearDnsCache();
        assertTrue(DnsCacheManipulator.listDnsCache().isEmpty());
        assertTrue(DnsCacheManipulator.getWholeDnsCache().getNegativeCache().isEmpty());
    }

    /**
     * Test load dns cache config
     *
     * @throws Exception exception
     * @since 1.0.0
     */
    @Test
    void test_loadDnsCacheConfig() throws Exception {
        DnsCacheManipulator.loadDnsCacheConfig();
        String ip = InetAddress.getByName(DOMAIN1).getHostAddress();
        assertEquals(IP1, ip);
    }

    /**
     * Test load dns cache config from d option
     *
     * @throws Exception exception
     * @since 1.0.0
     */
    @Test
    void test_loadDnsCacheConfig_from_D_Option() throws Exception {
        final String key = "dns.config.filename";
        System.setProperty(key, "dns-default/dns-cache-local.properties");
        DnsCacheManipulator.loadDnsCacheConfig();
        String ip = InetAddress.getByName(DOMAIN_CUSTOMIZED).getHostAddress();
        assertEquals(IP_CUSTOMIZED, ip);
        System.clearProperty(key);
    }

    /**
     * Test load dns cache config from my config
     *
     * @throws Exception exception
     * @since 1.0.0
     */
    @Test
    void test_loadDnsCacheConfig_fromMyConfig() throws Exception {
        DnsCacheManipulator.loadDnsCacheConfig("my-dns-cache.properties");
        String ip = InetAddress.getByName(DOMAIN2).getHostAddress();
        assertEquals(IP2, ip);
    }

    /**
     * Test set multi ip
     *
     * @throws Exception exception
     * @since 1.0.0
     */
    @Test
    void test_setMultiIp() throws Exception {
        DnsCacheManipulator.setDnsCache("multi.ip.com", "1.1.1.1", "2.2.2.2");
        String ip = InetAddress.getByName("multi.ip.com").getHostAddress();
        assertEquals("1.1.1.1", ip);

        InetAddress[] all = InetAddress.getAllByName("multi.ip.com");
        assertEquals(2, all.length);
        String[] ips = {all[0].getHostAddress(), all[1].getHostAddress()};
        assertArrayEquals(new String[]{"1.1.1.1", "2.2.2.2"}, ips);
    }

    /**
     * Test config not found
     *
     * @throws Exception exception
     * @since 1.0.0
     */
    @Test
    void test_configNotFound() throws Exception {
        try {
            DnsCacheManipulator.loadDnsCacheConfig("not-existed.properties");
            fail();
        } catch (DnsCacheManipulatorException expected) {
            assertEquals("Fail to find not-existed.properties on classpath!", expected.getMessage());
        }
    }

    /**
     * Test set dns cache get all dns cache
     *
     * @throws Exception exception
     * @since 1.0.0
     */
    @Test
    void test_setDnsCache_getAllDnsCache() throws Exception {
        final String host = "www.test_setDnsCache_getAllDnsCache.com";
        DnsCacheManipulator.setDnsCache(host, IP3);

        List<DnsCacheEntry> allDnsCacheEntries = DnsCacheManipulator.listDnsCache();
        List<DnsCacheEntry> expected = Collections.singletonList(
            new DnsCacheEntry(host.toLowerCase(), new String[]{IP3}, new Date(Long.MAX_VALUE)));

        assertEquals(expected, allDnsCacheEntries);
        assertTrue(DnsCacheManipulator.getWholeDnsCache().getNegativeCache().isEmpty());
    }

    /**
     * Test can set existed domain can expire then re lookup back
     *
     * @throws Exception exception
     * @since 1.0.0
     */
    @Test
    void test_canSetExistedDomain_canExpire_thenReLookupBack() throws Exception {
        final String domain = "github.com";

        Set<String> expected = getAllHostAddresses(domain);

        DnsCacheManipulator.setDnsCache(30, domain, IP3);
        assertEquals(IP3, InetAddress.getByName(domain).getHostAddress());

        sleep(32);

        assertEquals(expected, getAllHostAddresses(domain));
    }

    /**
     * Gets all host addresses *
     *
     * @param domain domain
     * @return the all host addresses
     * @throws Exception exception
     * @since 1.0.0
     */
    private static Set<String> getAllHostAddresses(String domain) throws Exception {
        InetAddress[] allByName = InetAddress.getAllByName(domain);
        Set<String> all = new HashSet<String>();
        for (InetAddress inetAddress : allByName) {
            all.add(inetAddress.getHostAddress());
        }
        return all;
    }

    /**
     * Test set not existed domain remove then re lookup and not existed
     *
     * @throws Exception exception
     * @since 1.0.0
     */
    @Test
    void test_setNotExistedDomain_RemoveThenReLookupAndNotExisted() throws Exception {
        System.out.printf("%s(%s) test_setNotExistedDomain_RemoveThenReLookupAndNotExisted %s\n",
            new Date(), currentTimeMillis(), DnsCacheManipulator.getWholeDnsCache());
        DnsCacheManipulator.setDnsCache(DOMAIN_NOT_EXISTED, IP3);

        System.out.printf("%s(%s) test_setNotExistedDomain_RemoveThenReLookupAndNotExisted %s\n",
            new Date(), currentTimeMillis(), DnsCacheManipulator.getWholeDnsCache());
        String ip = InetAddress.getByName(DOMAIN_NOT_EXISTED).getHostAddress();
        assertEquals(IP3, ip);

        DnsCacheManipulator.removeDnsCache(DOMAIN_NOT_EXISTED);

        assertDomainNotExisted();


        System.out.printf("%s(%s) test_setNotExistedDomain_RemoveThenReLookupAndNotExisted %s\n",
            new Date(), currentTimeMillis(), DnsCacheManipulator.getWholeDnsCache());
        List<DnsCacheEntry> cache = DnsCacheManipulator.listDnsCache();
        assertTrue(cache.isEmpty());

        List<DnsCacheEntry> negativeCache = DnsCacheManipulator.getWholeDnsCache().getNegativeCache();
        assertEquals(1, negativeCache.size());
        assertEquals(DOMAIN_NOT_EXISTED.toLowerCase(), negativeCache.get(0).getHost());
    }

    /**
     * Assert domain not existed
     *
     * @since 1.0.0
     */
    private static void assertDomainNotExisted() {
        try {
            InetAddress.getByName(DOMAIN_NOT_EXISTED).getHostAddress();
            fail();
        } catch (UnknownHostException expected) {
            System.out.println(expected.toString());
            assertTrue(true);
        }
    }

    /**
     * Test set not existed domain can expire then re lookup and not existed
     *
     * @throws Exception exception
     * @since 1.0.0
     */
    @Test
    void test_setNotExistedDomain_canExpire_thenReLookupAndNotExisted() throws Exception {
        System.out.printf("%s(%s) test_setNotExistedDomain_canExpire_thenReLookupAndNotExisted %s\n",
            new Date(), currentTimeMillis(), DnsCacheManipulator.getWholeDnsCache());
        DnsCacheManipulator.setDnsCache(100, DOMAIN_NOT_EXISTED, IP3);

        System.out.printf("%s(%s) test_setNotExistedDomain_canExpire_thenReLookupAndNotExisted %s\n",
            new Date(), currentTimeMillis(), DnsCacheManipulator.getWholeDnsCache());
        String ip = InetAddress.getByName(DOMAIN_NOT_EXISTED).getHostAddress();
        assertEquals(IP3, ip);

        sleep(100);

        assertDomainNotExisted();

        System.out.printf("%s(%s) test_setNotExistedDomain_canExpire_thenReLookupAndNotExisted %s\n",
            new Date(), currentTimeMillis(), DnsCacheManipulator.getWholeDnsCache());
        List<DnsCacheEntry> cache = DnsCacheManipulator.listDnsCache();
        assertTrue(cache.isEmpty());

        List<DnsCacheEntry> negativeCache = DnsCacheManipulator.getWholeDnsCache().getNegativeCache();
        assertEquals(1, negativeCache.size());
        assertEquals(DOMAIN_NOT_EXISTED.toLowerCase(), negativeCache.get(0).getHost());
    }

    /**
     * Test multi ips in config file
     *
     * @throws Exception exception
     * @since 1.0.0
     */
    @Test
    void test_multi_ips_in_config_file() throws Exception {
        DnsCacheManipulator.loadDnsCacheConfig("dns-cache-multi-ips.properties");

        final String host = "www.hello-multi-ips.com";
        DnsCacheEntry entry = new DnsCacheEntry(host,
            new String[]{"42.42.41.1", "42.42.41.2"},
            new Date(Long.MAX_VALUE));
        assertEquals(entry, DnsCacheManipulator.getDnsCache(host));

        final String hostLoose = "www.hello-multi-ips-loose.com";
        DnsCacheEntry entryLoose = new DnsCacheEntry(hostLoose,
            new String[]{"42.42.41.1", "42.42.41.2", "42.42.41" +
                ".3", "42.42.41.4"},
            new Date(Long.MAX_VALUE));
        assertEquals(entryLoose, DnsCacheManipulator.getDnsCache(hostLoose));
    }

    /**
     * Test null safe for get dns cache
     *
     * @throws Exception exception
     * @since 1.0.0
     */
    @Test
    void test_nullSafeForGetDnsCache() throws Exception {
        DnsCacheEntry dnsCache = DnsCacheManipulator.getDnsCache(DOMAIN_NOT_EXISTED);
        assertNull(dnsCache);
    }

    /**
     * Test set dns cache policy
     *
     * @throws Exception exception
     * @since 1.0.0
     */
    @Test
    void test_setDnsCachePolicy() throws Exception {
        final String host = "baidu.com";
        DnsCacheManipulator.setDnsCachePolicy(2);
        assertEquals(2, DnsCacheManipulator.getDnsCachePolicy());

        InetAddress.getByName(host).getHostAddress();
        long tick = currentTimeMillis();

        sleep(1000);
        InetAddress.getByName(host).getHostAddress();

        DnsCacheEntry dnsCache = DnsCacheManipulator.getDnsCache(host);
        assertBetween(dnsCache.getExpiration().getTime(), tick, tick + 2001);

        sleep(1001);

        // return expired entry, because of no dns cache touch by external related operation!
        DnsCacheEntry next = DnsCacheManipulator.getDnsCache(host);
        assertNotSame(dnsCache, next);
        assertEquals(dnsCache, next);

        // touch dns cache with external other host operation
        InetAddress.getByName("www.baidu.com").getHostAddress();
        assertNull(DnsCacheManipulator.getDnsCache(host));

        // relookup
        InetAddress.getByName(host).getHostAddress();
        DnsCacheEntry relookup = DnsCacheManipulator.getDnsCache(host);
        long relookupTick = currentTimeMillis();
        assertBetween(relookup.getExpiration().getTime(), relookupTick, relookupTick + 2001);
    }

    /**
     * Test set negative dns cache policy
     *
     * @throws Exception exception
     * @since 1.0.0
     */
    @Test
    void test_setNegativeDnsCachePolicy() throws Exception {
        DnsCacheManipulator.setDnsNegativeCachePolicy(2);
        assertEquals(2, DnsCacheManipulator.getDnsNegativeCachePolicy());

        try {
            InetAddress.getByName(DOMAIN_NOT_EXISTED).getHostAddress();
            fail();
        } catch (UnknownHostException expected) {
            assertTrue(true);
        }
        long tick = currentTimeMillis();

        List<DnsCacheEntry> negativeCache = DnsCacheManipulator.getWholeDnsCache().getNegativeCache();
        assertEquals(1, negativeCache.size());
        DnsCacheEntry dnsCache = negativeCache.get(0);
        assertBetween(dnsCache.getExpiration().getTime(), tick, tick + 2001);

        sleep(1000);
        try {
            InetAddress.getByName(DOMAIN_NOT_EXISTED).getHostAddress();
            fail();
        } catch (UnknownHostException expected) {
            assertTrue(true);
        }
        assertEquals(dnsCache, DnsCacheManipulator.getWholeDnsCache().getNegativeCache().get(0));

        sleep(1001);
        try {
            InetAddress.getByName(DOMAIN_NOT_EXISTED).getHostAddress();
            fail();
        } catch (UnknownHostException expected) {
            assertTrue(true);
        }
        long relookupTick = currentTimeMillis();
        List<DnsCacheEntry> relookupNegativeCache = DnsCacheManipulator.getWholeDnsCache().getNegativeCache();
        assertEquals(1, relookupNegativeCache.size());
        DnsCacheEntry relookup = relookupNegativeCache.get(0);
        assertBetween(relookup.getExpiration().getTime(), relookupTick, relookupTick + 2001);
    }

    /**
     * Assert between
     *
     * @param actual actual
     * @param start  start
     * @param end    end
     * @since 1.0.0
     */
    static void assertBetween(long actual, long start, long end) {
        boolean ok = (start <= actual) && (actual <= end);
        if (!ok) {
            fail(start + " <= " + actual + " <= " + end + ", failed!");
        }
    }
}
