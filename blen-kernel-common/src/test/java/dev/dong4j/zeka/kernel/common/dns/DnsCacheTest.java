package dev.dong4j.zeka.kernel.common.dns;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.06.09 19:53
 * @since 1.0.0
 */
class DnsCacheTest {
    /**
     * Test equals
     *
     * @throws Exception exception
     * @since 1.0.0
     */
    @Test
    void test_equals() throws Exception {
        Date expiration = new Date();
        DnsCacheEntry entry1 = new DnsCacheEntry("a.com", new String[]{"1.1.1.1"}, expiration);
        DnsCacheEntry entry2 = new DnsCacheEntry("b.com", new String[]{"1.1.1.2"}, expiration);
        DnsCacheEntry entry3 = new DnsCacheEntry("c.com", new String[]{"1.1.1.2"}, expiration);
        DnsCacheEntry entry4 = new DnsCacheEntry("d.com", new String[]{"1.1.1.2"}, expiration);

        DnsCache dnsCache1 = new DnsCache(
            Arrays.asList(entry1, entry2),
            Collections.singletonList(entry3));
        DnsCache dnsCache2 = new DnsCache(
            Arrays.asList(entry1, entry2),
            Collections.singletonList(entry4));

        assertNotEquals(dnsCache1, dnsCache2);
    }

    /**
     * Test to string
     *
     * @throws Exception exception
     * @since 1.0.0
     */
    @Test
    void test_toString() throws Exception {
        Date expiration = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        String date = dateFormat.format(expiration);

        DnsCacheEntry entry1 = new DnsCacheEntry("a.com", new String[]{"1.1.1.1"}, expiration);
        DnsCacheEntry entry2 = new DnsCacheEntry("b.com", new String[]{"1.1.1.2"}, expiration);
        DnsCacheEntry entry3 = new DnsCacheEntry("c.com", new String[]{"1.1.1.2"}, expiration);

        DnsCache dnsCache = new DnsCache(
            Arrays.asList(entry1, entry2),
            Collections.singletonList(entry3));

        String expected = String.format("DnsCache{cache=[DnsCacheEntry{host='a.com', ips=[1.1.1.1], expiration=%s}" +
            ", DnsCacheEntry{host='b.com', ips=[1.1.1.2], expiration=%<s}]" +
            ", negativeCache=[DnsCacheEntry{host='c.com', ips=[1.1.1.2], expiration=%<s}]}", date);

        assertEquals(expected, dnsCache.toString());
    }
}
