package dev.dong4j.zeka.kernel.common.dns.internal;

import org.junit.jupiter.api.Test;

import java.net.InetAddress;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.06.09 19:53
 * @since 1.5.0
 */
class IpParserUtilsTest {
    /**
     * Test ip 2 byte array
     *
     * @throws Exception exception
     * @since 1.5.0
     */
    @Test
    void test_ip2ByteArray() throws Exception {
        assertArrayEquals(new byte[]{10, 1, 1, 1},
            IpParserUtils.ip2ByteArray("10.1.1.1"));
        assertArrayEquals(new byte[]{(byte) 192, (byte) 168, (byte) 0, 13},
            IpParserUtils.ip2ByteArray("192.168.0.13"));
        assertArrayEquals(new byte[]{10, (byte) 192, (byte) 255, 0},
            IpParserUtils.ip2ByteArray("10.192.255.0"));

        final String ip = "2404:6800:4005:80a:0:0:0:200e";
        byte[] bytes = IpParserUtils.ip2ByteArray(ip);
        assertArrayEquals(getInetAddressByGetAllByName(ip).getAddress(), bytes);
    }

    /**
     * Test ip 2 byte array ipv 4 exception
     *
     * @throws Exception exception
     * @since 1.5.0
     */
    @Test
    void test_ip2ByteArray_ipv4_exception() throws Exception {
        // ipv4 with char
        try {
            IpParserUtils.ip2ByteArray("a.1.1.1");
            fail();
        } catch (IllegalArgumentException expected) {
            expected.printStackTrace();
        }

        // ipv4_minus
        try {
            IpParserUtils.ip2ByteArray("-2.168.0.13");
            fail();
        } catch (IllegalArgumentException expected) {
            expected.printStackTrace();
        }

        // ipv4 overflow
        try {
            IpParserUtils.ip2ByteArray("1.1.1.256");
            fail();
        } catch (IllegalArgumentException expected) {
            expected.printStackTrace();
        }

        // ipv4 too long
        try {
            IpParserUtils.ip2ByteArray("192.168.0.13.1");
            fail();
        } catch (IllegalArgumentException expected) {
            expected.printStackTrace();
        }
    }

    /**
     * Test ip 2 byte array ipv 6 exception
     *
     * @throws Exception exception
     * @since 1.5.0
     */
    @Test
    void test_ip2ByteArray_ipv6_exception() throws Exception {
        // ipv6 with char
        try {
            IpParserUtils.ip2ByteArray("2404:6800:4005:80a:0:0:0:200z");
            fail();
        } catch (IllegalArgumentException expected) {
            expected.printStackTrace();
        }

        // ipv6 minus
        try {
            IpParserUtils.ip2ByteArray("-2404:6800:4005:80a:0:0:0:200e");
            fail();
        } catch (IllegalArgumentException expected) {
            expected.printStackTrace();
        }

        // ipv6 too long
        try {
            IpParserUtils.ip2ByteArray("2404:6800:4005:80a:0:0:0:200:123");
            fail();
        } catch (IllegalArgumentException expected) {
            expected.printStackTrace();
        }
    }

    /**
     * Gets inet address by get all by name *
     *
     * @param ip ip
     * @return the inet address by get all by name
     * @throws Exception exception
     * @since 1.5.0
     */
    private static InetAddress getInetAddressByGetAllByName(String ip) throws Exception {
        InetAddress[] addresses = InetAddress.getAllByName(ip);
        assertEquals(1, addresses.length);
        return addresses[0];
    }
}
