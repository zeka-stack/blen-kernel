package dev.dong4j.zeka.kernel.common.dns;


import java.text.SimpleDateFormat;
import java.util.Date;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.06.09 19:53
 * @since 1.0.0
 */
class DnsCacheEntryTest {
    /**
     * Test equals
     *
     * @throws Exception exception
     * @since 1.0.0
     */
    @Test
    void test_equals() throws Exception {
        Date date = new Date(System.currentTimeMillis() + 1000 * 60);
        DnsCacheEntry entry1 = new DnsCacheEntry("a.com", new String[]{"1.1.1.1"}, date);
        DnsCacheEntry entry2 = new DnsCacheEntry("a.com", new String[]{"1.1.1.1"}, date);
        assertEquals(entry1, entry2);

        DnsCacheEntry entryIps1 = new DnsCacheEntry("a.com", new String[]{"1.1.1.1", "2.2.2.2"}, date);
        DnsCacheEntry entryIps2 = new DnsCacheEntry("a.com", new String[]{"1.1.1.1", "2.2.2.2"}, date);
        assertEquals(entryIps1, entryIps2);
    }

    /**
     * Test not equals
     *
     * @throws Exception exception
     * @since 1.0.0
     */
    @Test
    void test_notEquals() throws Exception {
        Date date = new Date(System.currentTimeMillis() + 1000 * 60);

        DnsCacheEntry entry1 = new DnsCacheEntry("a.com", new String[]{"1.1.1.1"}, date);
        DnsCacheEntry entry2 = new DnsCacheEntry("a.com", new String[]{"2.2.2.2"}, date);

        assertNotEquals(entry1, entry2);

        DnsCacheEntry entryNow = new DnsCacheEntry("a.com", new String[]{"1.1.1.1"}, new Date());
        assertNotEquals(entry1, entryNow);

        DnsCacheEntry entryIps = new DnsCacheEntry("a.com", new String[]{"1.1.1.1", "2.2.2.2"}, date);
        assertNotEquals(entry1, entryIps);

        DnsCacheEntry entryDomainB = new DnsCacheEntry("b.com", new String[]{"1.1.1.1"}, date);
        assertNotEquals(entry1, entryDomainB);
    }

    /**
     * Test getter
     *
     * @throws Exception exception
     * @since 1.0.0
     */
    @Test
    void test_getter() throws Exception {
        Date expiration = new Date();
        DnsCacheEntry entry1 = new DnsCacheEntry("a.com", new String[]{"1.1.1.1"}, expiration);
        assertEquals("a.com", entry1.getHost());
        assertEquals("1.1.1.1", entry1.getIp());

        assertArrayEquals(new String[]{"1.1.1.1"}, entry1.getIps());
        assertNotSame(entry1.getIps(), entry1.getIps());
        assertArrayEquals(entry1.getIps(), entry1.getIps());

        assertSame(expiration, entry1.getExpiration());

        DnsCacheEntry entryIps = new DnsCacheEntry("a.com", new String[]{"1.1.1.1", "2.2.2.2"},
            expiration);
        assertEquals("1.1.1.1", entryIps.getIp());
        assertArrayEquals(new String[]{"1.1.1.1", "2.2.2.2"}, entryIps.getIps());
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
        DnsCacheEntry entry = new DnsCacheEntry("a.com", new String[]{"1.1.1.1"}, expiration);

        String expected = String.format("DnsCacheEntry{host='a.com', ips=[1.1.1.1], expiration=%s}", date);
        assertEquals(expected, entry.toString());
    }
}
