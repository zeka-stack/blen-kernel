package dev.dong4j.zeka.kernel.common.util;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/**
 * A class that enables to get an IP range from CIDR specification. It supports
 * both IPv4 and IPv6.
 * <p>
 * From https://github.com/edazdarevic/CIDRUtils/blob/master/CIDRUtils.java
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.07.02 11:56
 * @since 1.0.0
 */
@SuppressWarnings("all")
public class CIDRUtils {
    /** Cidr */
    private final String CIDR;
    /** Inet address */
    private final InetAddress inetAddress;
    /** Start address */
    private InetAddress startAddress;
    /** End address */
    private InetAddress endAddress;
    /** Prefix length */
    private final int prefixLength;


    /**
     * Cidr utils
     *
     * @param cidr cidr
     * @throws UnknownHostException unknown host exception
     * @since 1.0.0
     */
    public CIDRUtils(String cidr) throws UnknownHostException {

        this.CIDR = cidr;

        /* split CIDR to address and prefix part */
        if (this.CIDR.contains("/")) {
            int index = this.CIDR.indexOf("/");
            String addressPart = this.CIDR.substring(0, index);
            String networkPart = this.CIDR.substring(index + 1);

            this.inetAddress = InetAddress.getByName(addressPart);
            this.prefixLength = Integer.parseInt(networkPart);

            this.calculate();
        } else {
            throw new IllegalArgumentException("not an valid CIDR format!");
        }
    }


    /**
     * Calculate
     *
     * @throws UnknownHostException unknown host exception
     * @since 1.0.0
     */
    private void calculate() throws UnknownHostException {

        ByteBuffer maskBuffer;
        int targetSize;
        if (this.inetAddress.getAddress().length == 4) {
            maskBuffer =
                ByteBuffer
                    .allocate(4)
                    .putInt(-1);
            targetSize = 4;
        } else {
            maskBuffer = ByteBuffer.allocate(16)
                .putLong(-1L)
                .putLong(-1L);
            targetSize = 16;
        }

        BigInteger mask = (new BigInteger(1, maskBuffer.array())).not().shiftRight(this.prefixLength);

        ByteBuffer buffer = ByteBuffer.wrap(this.inetAddress.getAddress());
        BigInteger ipVal = new BigInteger(1, buffer.array());

        BigInteger startIp = ipVal.and(mask);
        BigInteger endIp = startIp.add(mask.not());

        byte[] startIpArr = this.toBytes(startIp.toByteArray(), targetSize);
        byte[] endIpArr = this.toBytes(endIp.toByteArray(), targetSize);

        this.startAddress = InetAddress.getByAddress(startIpArr);
        this.endAddress = InetAddress.getByAddress(endIpArr);

    }

    /**
     * To bytes
     *
     * @param array      array
     * @param targetSize target size
     * @return the byte [ ]
     * @since 1.0.0
     */
    private byte @NotNull [] toBytes(byte[] array, int targetSize) {
        int counter = 0;
        List<Byte> newArr = new ArrayList<Byte>();
        while (counter < targetSize && (array.length - 1 - counter >= 0)) {
            newArr.add(0, array[array.length - 1 - counter]);
            counter++;
        }

        int size = newArr.size();
        for (int i = 0; i < (targetSize - size); i++) {

            newArr.add(0, (byte) 0);
        }

        byte[] ret = new byte[newArr.size()];
        for (int i = 0; i < newArr.size(); i++) {
            ret[i] = newArr.get(i);
        }
        return ret;
    }

    /**
     * Gets network address *
     *
     * @return the network address
     * @since 1.0.0
     */
    public String getNetworkAddress() {

        return this.startAddress.getHostAddress();
    }

    /**
     * Gets broadcast address *
     *
     * @return the broadcast address
     * @since 1.0.0
     */
    public String getBroadcastAddress() {
        return this.endAddress.getHostAddress();
    }

    /**
     * Is in range
     *
     * @param ipAddress ip address
     * @return the boolean
     * @throws UnknownHostException unknown host exception
     * @since 1.0.0
     */
    public boolean isInRange(String ipAddress) throws UnknownHostException {
        InetAddress address = InetAddress.getByName(ipAddress);
        BigInteger start = new BigInteger(1, this.startAddress.getAddress());
        BigInteger end = new BigInteger(1, this.endAddress.getAddress());
        BigInteger target = new BigInteger(1, address.getAddress());

        int st = start.compareTo(target);
        int te = target.compareTo(end);

        return (st < 0 || st == 0) && (te < 0 || te == 0);
    }
}
