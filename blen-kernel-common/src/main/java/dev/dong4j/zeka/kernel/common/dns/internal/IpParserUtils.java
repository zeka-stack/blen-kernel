package dev.dong4j.zeka.kernel.common.dns.internal;

import org.jetbrains.annotations.NotNull;
import sun.net.util.IPAddressUtil;

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.06.09 19:52
 * @since 1.0.0
 */
@SuppressWarnings("all")
public final class IpParserUtils {
    /**
     * Ip 2 byte array
     *
     * @param ip ip
     * @return the byte [ ]
     * @since 1.0.0
     */
    static byte @NotNull [] ip2ByteArray(@NotNull String ip) {
        boolean ipv6Expected = false;
        if (ip.charAt(0) == '[') {
            // This is supposed to be an IPv6 literal
            if (ip.length() > 2 && ip.charAt(ip.length() - 1) == ']') {
                ip = ip.substring(1, ip.length() - 1);
                ipv6Expected = true;
            } else {
                // This was supposed to be a IPv6 address, but it's not!
                throw new IllegalArgumentException(ip + ": invalid IPv6 address");
            }
        }

        if (Character.digit(ip.charAt(0), 16) != -1 || (ip.charAt(0) == ':')) {
            // see if it is IPv4 address
            byte[] address = IPAddressUtil.textToNumericFormatV4(ip);
            if (address != null) {
                return address;
            }

            // see if it is IPv6 address
            // Check if a numeric or string zone id is present
            address = IPAddressUtil.textToNumericFormatV6(ip);
            if (address != null) {
                return address;
            }


            if (ipv6Expected) {
                throw new IllegalArgumentException(ip + ": invalid IPv6 address");
            } else {
                throw new IllegalArgumentException(ip + ": invalid IP address");
            }
        } else {
            throw new IllegalArgumentException(ip + ": invalid IP address");
        }
    }
}
