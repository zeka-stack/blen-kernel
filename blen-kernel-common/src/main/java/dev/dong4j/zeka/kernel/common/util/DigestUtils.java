package dev.dong4j.zeka.kernel.common.util;

import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.lang.Nullable;

/**
 * <p>Description: 加密相关工具类 </p>
 *
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.27 18:25
 * @since 1.0.0
 */
@UtilityClass
@SuppressWarnings("all")
public class DigestUtils extends org.springframework.util.DigestUtils {

    /**
     * HEX_VALUE
     */
    private static final String HEX_VALUE = "0123456789abcdef";
    /**
     * HEX_CODE
     */
    private static final char[] HEX_CODE = HEX_VALUE.toCharArray();

    /**
     * Calculates the MD5 digest.
     *
     * @param data Data to digest
     * @return MD5 digest as a hex array
     * @since 1.0.0
     */
    public static byte[] md5(byte[] data) {
        return org.springframework.util.DigestUtils.md5Digest(data);
    }

    /**
     * Calculates the MD5 digest.
     *
     * @param data Data to digest
     * @return MD5 digest as a hex array
     * @since 1.0.0
     */
    public static byte[] md5(@NotNull String data) {
        return org.springframework.util.DigestUtils.md5Digest(data.getBytes(Charsets.UTF_8));
    }

    /**
     * Return a hexadecimal string representation of the MD5 digest of the given bytes.
     *
     * @param bytes the bytes to calculate the digest over
     * @return a hexadecimal digest string
     * @since 1.0.0
     */
    @NotNull
    public static String md5Hex(byte[] bytes) {
        return org.springframework.util.DigestUtils.md5DigestAsHex(bytes);
    }

    /**
     * sha1Hex
     *
     * @param data Data to digest
     * @return digest as a hex string
     * @since 1.0.0
     */
    @NotNull
    public static String sha1Hex(@NotNull String data) {
        return DigestUtils.encodeHex(sha1(data.getBytes(Charsets.UTF_8)));
    }

    /**
     * encode Hex
     *
     * @param bytes Data to Hex
     * @return bytes as a hex string
     * @since 1.0.0
     */
    @NotNull
    public static String encodeHex(byte[] bytes) {
        StringBuilder r = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            r.append(HEX_CODE[(b >> 4) & 0xF]);
            r.append(HEX_CODE[(b & 0xF)]);
        }
        return r.toString();
    }

    /**
     * sha1
     *
     * @param bytes Data to digest
     * @return digest as a hex array
     * @since 1.0.0
     */
    public static byte[] sha1(byte[] bytes) {
        return DigestUtils.digest("SHA-1", bytes);
    }

    /**
     * digest
     *
     * @param algorithm 算法
     * @param bytes     Data to digest
     * @return digest byte array
     * @since 1.0.0
     */
    public static byte[] digest(String algorithm, byte[] bytes) {
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            return md.digest(bytes);
        } catch (NoSuchAlgorithmException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * sha1Hex
     *
     * @param bytes Data to digest
     * @return digest as a hex string
     * @since 1.0.0
     */
    @NotNull
    public static String sha1Hex(byte[] bytes) {
        return DigestUtils.encodeHex(sha1(bytes));
    }

    /**
     * SHA224
     *
     * @param data Data to digest
     * @return digest as a byte array
     * @since 1.0.0
     */
    public static byte[] sha224(@NotNull String data) {
        return DigestUtils.sha224(data.getBytes(Charsets.UTF_8));
    }

    /**
     * SHA224
     *
     * @param bytes Data to digest
     * @return digest as a byte array
     * @since 1.0.0
     */
    public static byte[] sha224(byte[] bytes) {
        return DigestUtils.digest("SHA-224", bytes);
    }

    /**
     * SHA224Hex
     *
     * @param data Data to digest
     * @return digest as a hex string
     * @since 1.0.0
     */
    @NotNull
    public static String sha224Hex(@NotNull String data) {
        return DigestUtils.encodeHex(sha224(data.getBytes(Charsets.UTF_8)));
    }

    /**
     * SHA224Hex
     *
     * @param bytes Data to digest
     * @return digest as a hex string
     * @since 1.0.0
     */
    @NotNull
    public static String sha224Hex(byte[] bytes) {
        return DigestUtils.encodeHex(sha224(bytes));
    }

    /**
     * sha256Hex
     *
     * @param data Data to digest
     * @return digest as a byte array
     * @since 1.0.0
     */
    public static byte[] sha256(@NotNull String data) {
        return DigestUtils.sha256(data.getBytes(Charsets.UTF_8));
    }

    /**
     * sha256Hex
     *
     * @param bytes Data to digest
     * @return digest as a byte array
     * @since 1.0.0
     */
    public static byte[] sha256(byte[] bytes) {
        return DigestUtils.digest("SHA-256", bytes);
    }

    /**
     * sha256Hex
     *
     * @param data Data to digest
     * @return digest as a hex string
     * @since 1.0.0
     */
    @NotNull
    public static String sha256Hex(@NotNull String data) {
        return DigestUtils.encodeHex(sha256(data.getBytes(Charsets.UTF_8)));
    }

    /**
     * sha256Hex
     *
     * @param bytes Data to digest
     * @return digest as a hex string
     * @since 1.0.0
     */
    @NotNull
    public static String sha256Hex(byte[] bytes) {
        return DigestUtils.encodeHex(sha256(bytes));
    }

    /**
     * sha384
     *
     * @param data Data to digest
     * @return digest as a byte array
     * @since 1.0.0
     */
    public static byte[] sha384(@NotNull String data) {
        return DigestUtils.sha384(data.getBytes(Charsets.UTF_8));
    }

    /**
     * sha384
     *
     * @param bytes Data to digest
     * @return digest as a byte array
     * @since 1.0.0
     */
    public static byte[] sha384(byte[] bytes) {
        return DigestUtils.digest("SHA-384", bytes);
    }

    /**
     * sha384Hex
     *
     * @param data Data to digest
     * @return digest as a hex string
     * @since 1.0.0
     */
    @NotNull
    public static String sha384Hex(@NotNull String data) {
        return DigestUtils.encodeHex(sha384(data.getBytes(Charsets.UTF_8)));
    }

    /**
     * sha384Hex
     *
     * @param bytes Data to digest
     * @return digest as a hex string
     * @since 1.0.0
     */
    @NotNull
    public static String sha384Hex(byte[] bytes) {
        return DigestUtils.encodeHex(sha384(bytes));
    }

    /**
     * sha512Hex
     *
     * @param data Data to digest
     * @return digest as a byte array
     * @since 1.0.0
     */
    public static byte[] sha512(@NotNull String data) {
        return DigestUtils.sha512(data.getBytes(Charsets.UTF_8));
    }

    /**
     * sha512Hex
     *
     * @param bytes Data to digest
     * @return digest as a byte array
     * @since 1.0.0
     */
    public static byte[] sha512(byte[] bytes) {
        return DigestUtils.digest("SHA-512", bytes);
    }

    /**
     * sha512Hex
     *
     * @param data Data to digest
     * @return digest as a hex string
     * @since 1.0.0
     */
    @NotNull
    public static String sha512Hex(@NotNull String data) {
        return DigestUtils.encodeHex(sha512(data.getBytes(Charsets.UTF_8)));
    }

    /**
     * sha512Hex
     *
     * @param bytes Data to digest
     * @return digest as a hex string
     * @since 1.0.0
     */
    @NotNull
    public static String sha512Hex(byte[] bytes) {
        return DigestUtils.encodeHex(sha512(bytes));
    }

    /**
     * digest Hex
     *
     * @param algorithm 算法
     * @param bytes     Data to digest
     * @return digest as a hex string
     * @since 1.0.0
     */
    @NotNull
    public static String digestHex(String algorithm, byte[] bytes) {
        return DigestUtils.encodeHex(digest(algorithm, bytes));
    }

    /**
     * hmacMd5
     *
     * @param data Data to digest
     * @param key  key
     * @return digest as a byte array
     * @since 1.0.0
     */
    public static byte[] hmacMd5(@NotNull String data, String key) {
        return DigestUtils.hmacMd5(data.getBytes(Charsets.UTF_8), key);
    }

    /**
     * hmacMd5
     *
     * @param bytes Data to digest
     * @param key   key
     * @return digest as a byte array
     * @since 1.0.0
     */
    public static byte[] hmacMd5(byte[] bytes, String key) {
        return DigestUtils.digestHmac("HmacMD5", bytes, key);
    }

    /**
     * digest Hmac
     *
     * @param algorithm 算法
     * @param bytes     Data to digest
     * @param key       the key
     * @return digest as a byte array
     * @since 1.0.0
     */
    public static byte[] digestHmac(String algorithm, byte[] bytes, @NotNull String key) {
        SecretKey secretKey = new SecretKeySpec(key.getBytes(Charsets.UTF_8), algorithm);
        try {
            Mac mac = Mac.getInstance(secretKey.getAlgorithm());
            mac.init(secretKey);
            return mac.doFinal(bytes);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * hmacMd5 Hex
     *
     * @param data Data to digest
     * @param key  key
     * @return digest as a hex string
     * @since 1.0.0
     */
    @NotNull
    public static String hmacMd5Hex(@NotNull String data, String key) {
        return DigestUtils.encodeHex(hmacMd5(data.getBytes(Charsets.UTF_8), key));
    }

    /**
     * hmacMd5 Hex
     *
     * @param bytes Data to digest
     * @param key   key
     * @return digest as a hex string
     * @since 1.0.0
     */
    @NotNull
    public static String hmacMd5Hex(byte[] bytes, String key) {
        return DigestUtils.encodeHex(hmacMd5(bytes, key));
    }

    /**
     * hmacSha1
     *
     * @param data Data to digest
     * @param key  key
     * @return digest as a byte array
     * @since 1.0.0
     */
    public static byte[] hmacSha1(@NotNull String data, String key) {
        return DigestUtils.hmacSha1(data.getBytes(Charsets.UTF_8), key);
    }

    /**
     * hmacSha1
     *
     * @param bytes Data to digest
     * @param key   key
     * @return digest as a byte array
     * @since 1.0.0
     */
    public static byte[] hmacSha1(byte[] bytes, String key) {
        return DigestUtils.digestHmac("HmacSHA1", bytes, key);
    }

    /**
     * hmacSha1 Hex
     *
     * @param data Data to digest
     * @param key  key
     * @return digest as a hex string
     * @since 1.0.0
     */
    @NotNull
    public static String hmacSha1Hex(@NotNull String data, String key) {
        return DigestUtils.encodeHex(hmacSha1(data.getBytes(Charsets.UTF_8), key));
    }

    /**
     * hmacSha1 Hex
     *
     * @param bytes Data to digest
     * @param key   key
     * @return digest as a hex string
     * @since 1.0.0
     */
    @NotNull
    public static String hmacSha1Hex(byte[] bytes, String key) {
        return DigestUtils.encodeHex(hmacSha1(bytes, key));
    }

    /**
     * hmacSha224
     *
     * @param data Data to digest
     * @param key  key
     * @return digest as a hex string
     * @since 1.0.0
     */
    public static byte[] hmacSha224(@NotNull String data, String key) {
        return DigestUtils.hmacSha224(data.getBytes(Charsets.UTF_8), key);
    }

    /**
     * hmacSha224
     *
     * @param bytes Data to digest
     * @param key   key
     * @return digest as a hex string
     * @since 1.0.0
     */
    public static byte[] hmacSha224(byte[] bytes, String key) {
        return DigestUtils.digestHmac("HmacSHA224", bytes, key);
    }

    /**
     * hmacSha224 Hex
     *
     * @param data Data to digest
     * @param key  key
     * @return digest as a hex string
     * @since 1.0.0
     */
    @NotNull
    public static String hmacSha224Hex(@NotNull String data, String key) {
        return DigestUtils.encodeHex(hmacSha224(data.getBytes(Charsets.UTF_8), key));
    }

    /**
     * hmacSha224 Hex
     *
     * @param bytes Data to digest
     * @param key   key
     * @return digest as a hex string
     * @since 1.0.0
     */
    @NotNull
    public static String hmacSha224Hex(byte[] bytes, String key) {
        return DigestUtils.encodeHex(hmacSha224(bytes, key));
    }

    /**
     * hmacSha256
     *
     * @param data Data to digest
     * @param key  key
     * @return digest as a hex string
     * @since 1.0.0
     */
    public static byte[] hmacSha256(@NotNull String data, String key) {
        return DigestUtils.hmacSha256(data.getBytes(Charsets.UTF_8), key);
    }

    /**
     * hmacSha256
     *
     * @param bytes Data to digest
     * @param key   key
     * @return digest as a byte array
     * @since 1.0.0
     */
    public static byte[] hmacSha256(byte[] bytes, String key) {
        return DigestUtils.digestHmac("HmacSHA256", bytes, key);
    }

    /**
     * hmacSha256 Hex
     *
     * @param data Data to digest
     * @param key  key
     * @return digest as a byte array
     * @since 1.0.0
     */
    @NotNull
    public static String hmacSha256Hex(@NotNull String data, String key) {
        return DigestUtils.encodeHex(hmacSha256(data.getBytes(Charsets.UTF_8), key));
    }

    /**
     * hmacSha256 Hex
     *
     * @param bytes Data to digest
     * @param key   key
     * @return digest as a hex string
     * @since 1.0.0
     */
    @NotNull
    public static String hmacSha256Hex(byte[] bytes, String key) {
        return DigestUtils.encodeHex(hmacSha256(bytes, key));
    }

    /**
     * hmacSha384
     *
     * @param data Data to digest
     * @param key  key
     * @return digest as a byte array
     * @since 1.0.0
     */
    public static byte[] hmacSha384(@NotNull String data, String key) {
        return DigestUtils.hmacSha384(data.getBytes(Charsets.UTF_8), key);
    }

    /**
     * hmacSha384
     *
     * @param bytes Data to digest
     * @param key   key
     * @return digest as a byte array
     * @since 1.0.0
     */
    public static byte[] hmacSha384(byte[] bytes, String key) {
        return DigestUtils.digestHmac("HmacSHA384", bytes, key);
    }

    /**
     * hmacSha384 Hex
     *
     * @param data Data to digest
     * @param key  key
     * @return digest as a hex string
     * @since 1.0.0
     */
    @NotNull
    public static String hmacSha384Hex(@NotNull String data, String key) {
        return DigestUtils.encodeHex(hmacSha384(data.getBytes(Charsets.UTF_8), key));
    }

    /**
     * hmacSha384 Hex
     *
     * @param bytes Data to digest
     * @param key   key
     * @return digest as a hex string
     * @since 1.0.0
     */
    @NotNull
    public static String hmacSha384Hex(byte[] bytes, String key) {
        return DigestUtils.encodeHex(hmacSha384(bytes, key));
    }

    /**
     * hmacSha512
     *
     * @param data Data to digest
     * @param key  key
     * @return digest as a byte array
     * @since 1.0.0
     */
    public static byte[] hmacSha512(@NotNull String data, String key) {
        return DigestUtils.hmacSha512(data.getBytes(Charsets.UTF_8), key);
    }

    /**
     * hmacSha512
     *
     * @param bytes Data to digest
     * @param key   key
     * @return digest as a byte array
     * @since 1.0.0
     */
    public static byte[] hmacSha512(byte[] bytes, String key) {
        return DigestUtils.digestHmac("HmacSHA512", bytes, key);
    }

    /**
     * hmacSha512 Hex
     *
     * @param data Data to digest
     * @param key  key
     * @return digest as a hex string
     * @since 1.0.0
     */
    @NotNull
    public static String hmacSha512Hex(@NotNull String data, String key) {
        return DigestUtils.encodeHex(hmacSha512(data.getBytes(Charsets.UTF_8), key));
    }

    /**
     * hmacSha512 Hex
     *
     * @param bytes Data to digest
     * @param key   key
     * @return digest as a hex string
     * @since 1.0.0
     */
    @NotNull
    public static String hmacSha512Hex(byte[] bytes, String key) {
        return DigestUtils.encodeHex(hmacSha512(bytes, key));
    }

    /**
     * digest Hmac Hex
     *
     * @param algorithm 算法
     * @param bytes     Data to digest
     * @param key       the key
     * @return digest as a hex string
     * @since 1.0.0
     */
    @NotNull
    public static String digestHmacHex(String algorithm, byte[] bytes, String key) {
        return DigestUtils.encodeHex(DigestUtils.digestHmac(algorithm, bytes, key));
    }

    /**
     * decode Hex
     *
     * @param hexStr Hex string
     * @return decode hex to bytes
     * @since 1.0.0
     */
    @SuppressWarnings("PMD.UndefineMagicConstantRule")
    public static byte[] decodeHex(@NotNull String hexStr) {
        int len = hexStr.length();
        if ((len & 0x01) != 0) {
            throw new IllegalArgumentException("hexBinary needs to be even-length: " + hexStr);
        }
        String hexText = hexStr.toLowerCase();
        byte[] out = new byte[len >> 1];
        for (int i = 0; i < len; i += 2) {
            int hn = HEX_VALUE.indexOf(hexText.charAt(i));
            int ln = HEX_VALUE.indexOf(hexText.charAt(i + 1));
            if (hn == -1 || ln == -1) {
                throw new IllegalArgumentException("contains illegal character for hexBinary: " + hexStr);
            }
            out[i / 2] = (byte) ((hn << 4) | ln);
        }
        return out;
    }

    /**
     * 比较字符串,避免字符串因为过长,产生耗时
     *
     * @param a String
     * @param b String
     * @return 是否相同 boolean
     * @since 1.0.0
     */
    @Contract("null, _ -> false; !null, null -> false")
    public static boolean slowEquals(@Nullable String a, @Nullable String b) {
        if (a == null || b == null) {
            return false;
        }
        return DigestUtils.slowEquals(a.getBytes(Charsets.UTF_8), b.getBytes(Charsets.UTF_8));
    }

    /**
     * 比较 byte 数组,避免字符串因为过长,产生耗时
     *
     * @param a byte array
     * @param b byte array
     * @return 是否相同 boolean
     * @since 1.0.0
     */
    @Contract(value = "null, _ -> false; !null, null -> false", pure = true)
    public static boolean slowEquals(@Nullable byte[] a, @Nullable byte[] b) {
        if (a == null || b == null) {
            return false;
        }
        if (a.length != b.length) {
            return false;
        }
        int diff = a.length ^ b.length;
        for (int i = 0; i < a.length; i++) {
            diff |= a[i] ^ b[i];
        }
        return diff == 0;
    }

    /**
     * 自定义加密 先MD5再SHA1
     *
     * @param data 数据
     * @return String string
     * @since 1.0.0
     */
    @NotNull
    public static String encrypt(String data) {
        return DigestUtils.encodeHex(sha1(md5Hex(data)));
    }

    /**
     * sha1
     *
     * @param data Data to digest
     * @return digest as a hex array
     * @since 1.0.0
     */
    public static byte[] sha1(@NotNull String data) {
        return DigestUtils.sha1(data.getBytes(Charsets.UTF_8));
    }

    /**
     * Calculates the MD5 digest and returns the value as a 32 character hex string.
     *
     * @param data Data to digest
     * @return MD5 digest as a hex string
     * @since 1.0.0
     */
    @NotNull
    public static String md5Hex(@NotNull String data) {
        return org.springframework.util.DigestUtils.md5DigestAsHex(data.getBytes(Charsets.UTF_8));
    }

}
