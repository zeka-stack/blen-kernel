package dev.dong4j.zeka.kernel.common.util;

import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.util.Assert;

/**
 * 完全兼容微信所使用的AES加密方式.
 * aes的key必须是256byte长 (比如32个字符) ,可以使用AesKit.genAesKey()来生成一组key
 *
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dong4j@gmail.com"
 * @date 2019.12.26 21:39
 * @since 1.0.0
 */
@UtilityClass
@SuppressWarnings("all")
public class AesUtils {

    /** 默认 16 长度 */
    private static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    /** MAXIMUM_CAPACITY */
    private static final int MAXIMUM_CAPACITY = 1 << 30;

    /**
     * 返回 2 的整数倍
     *
     * @param cap cap
     * @return the int
     * @since 1.9.0
     */
    private static final int relengh(int cap) {
        int n = cap - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
    }

    /**
     * Check power of 2
     *
     * @param n n
     * @return the boolean
     * @since 1.9.0
     */
    private boolean checkPowerOf2(int n) {
        if (n <= 0) {
            return false;
        }
        int t = n & (n - 1);
        return t == 0 ? true : false;
    }


    /**
     * Gen aes key string.
     *
     * @return the string
     * @since 1.0.0
     */
    @NotNull
    public static String genAesKey() {
        return RandomUtils.random(DEFAULT_INITIAL_CAPACITY);
    }

    /**
     * Gen aes key
     *
     * @param length length
     * @return the string
     * @since 1.9.0
     */
    public static String genAesKey(int length) {
        return RandomUtils.random(relengh(length));
    }

    /**
     * Encrypt byte [ ].
     *
     * @param content    the content
     * @param aesTextKey the aes text key
     * @return the byte [ ]
     * @since 1.0.0
     */
    public static byte[] encrypt(byte[] content, @NotNull String aesTextKey) {
        return encrypt(content, aesTextKey.getBytes(Charsets.UTF_8));
    }

    /**
     * Encrypt byte [ ].
     *
     * @param content the content
     * @param aesKey  the aes key
     * @return the byte [ ]
     * @since 1.0.0
     */
    public static byte[] encrypt(byte[] content, byte[] aesKey) {
        Assert.isTrue(aesKey.length >= DEFAULT_INITIAL_CAPACITY && checkPowerOf2(aesKey.length),
            "密钥必须为 2 的幂次方且大于等于 16 位");
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            SecretKeySpec keySpec = new SecretKeySpec(aesKey, "AES");
            IvParameterSpec iv = new IvParameterSpec(aesKey, 0, DEFAULT_INITIAL_CAPACITY);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);
            return cipher.doFinal(Pkcs7Encoder.encode(content));
        } catch (Exception e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * Encrypt byte [ ].
     *
     * @param content    the content
     * @param aesTextKey the aes text key
     * @return the byte [ ]
     * @since 1.0.0
     */
    public static byte[] encrypt(@NotNull String content, @NotNull String aesTextKey) {
        return encrypt(content.getBytes(Charsets.UTF_8), aesTextKey.getBytes(Charsets.UTF_8));
    }

    /**
     * Encrypt to str
     *
     * @param content    content
     * @param aesTextKey aes text key
     * @return the string
     * @since 1.9.0
     */
    public static String encryptToStr(@NotNull String content, @NotNull String aesTextKey) {
        return Base64Utils.encodeToString(encrypt(content, aesTextKey));
    }

    /**
     * Encrypt byte [ ].
     *
     * @param content    the content
     * @param charset    the charset
     * @param aesTextKey the aes text key
     * @return the byte [ ]
     * @since 1.0.0
     */
    public static byte[] encrypt(@NotNull String content, java.nio.charset.Charset charset, @NotNull String aesTextKey) {
        return encrypt(content.getBytes(charset), aesTextKey.getBytes(Charsets.UTF_8));
    }

    /**
     * Decrypt to str
     *
     * @param content    content
     * @param aesTextKey aes text key
     * @return the string
     * @since 1.9.0
     */
    public static String decryptToStr(@NotNull String content, @NotNull String aesTextKey) {
        return new String(decrypt(content, aesTextKey));
    }

    /**
     * Decrypt
     *
     * @param content    content
     * @param aesTextKey aes text key
     * @return the byte [ ]
     * @since 1.9.0
     */
    public static byte[] decrypt(@NotNull String content, @NotNull String aesTextKey) {
        return decrypt(Base64Utils.decodeFromString(content), aesTextKey);
    }

    /**
     * Decrypt byte [ ].
     *
     * @param content    the content
     * @param aesTextKey the aes text key
     * @return the byte [ ]
     * @since 1.0.0
     */
    public static byte[] decrypt(byte[] content, @NotNull String aesTextKey) {
        return decrypt(content, aesTextKey.getBytes(Charsets.UTF_8));
    }

    /**
     * Decrypt byte [ ].
     *
     * @param encrypted the encrypted
     * @param aesKey    the aes key
     * @return the byte [ ]
     * @since 1.0.0
     */
    public static byte[] decrypt(byte[] encrypted, byte[] aesKey) {
        Assert.isTrue(aesKey.length >= DEFAULT_INITIAL_CAPACITY && checkPowerOf2(aesKey.length),
            "密钥必须为 2 的幂次方且大于等于 16 位");
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            SecretKeySpec keySpec = new SecretKeySpec(aesKey, "AES");
            IvParameterSpec iv = new IvParameterSpec(Arrays.copyOfRange(aesKey, 0, DEFAULT_INITIAL_CAPACITY));
            cipher.init(Cipher.DECRYPT_MODE, keySpec, iv);
            return Pkcs7Encoder.decode(cipher.doFinal(encrypted));
        } catch (Exception e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * Decrypt to str string.
     *
     * @param content    the content
     * @param aesTextKey the aes text key
     * @return the string
     * @since 1.0.0
     */
    @NotNull
    @Contract("_, _ -> new")
    public static String decryptToStr(byte[] content, @NotNull String aesTextKey) {
        return new String(decrypt(content, aesTextKey.getBytes(Charsets.UTF_8)), Charsets.UTF_8);
    }

    /**
     * Decrypt to str string.
     *
     * @param content    the content
     * @param aesTextKey the aes text key
     * @param charset    the charset
     * @return the string
     * @since 1.0.0
     */
    @NotNull
    @Contract("_, _, _ -> new")
    public static String decryptToStr(byte[] content, @NotNull String aesTextKey, java.nio.charset.Charset charset) {
        return new String(decrypt(content, aesTextKey.getBytes(Charsets.UTF_8)), charset);
    }

    /**
     * 提供基于PKCS7算法的加解密接口.
     *
     * @author dong4j
     * @version 1.2.3
     * @email "mailto:dong4j@gmail.com"
     * @date 2019.12.26 21:39
     * @since 1.0.0
     */
    @UtilityClass
    static class Pkcs7Encoder {
        /**
         * The Block size.
         */
        static final int BLOCK_SIZE = 32;

        /**
         * Encode byte [ ].
         *
         * @param src the src
         * @return the byte [ ]
         * @since 1.0.0
         */
        static byte[] encode(byte[] src) {
            int count = src.length;
            // 计算需要填充的位数
            int amountToPad = BLOCK_SIZE - (count % BLOCK_SIZE);
            // 获得补位所用的字符
            byte pad = (byte) (amountToPad & 0xFF);
            byte[] pads = new byte[amountToPad];
            for (int index = 0; index < amountToPad; index++) {
                pads[index] = pad;
            }
            int length = count + amountToPad;
            byte[] dest = new byte[length];
            System.arraycopy(src, 0, dest, 0, count);
            System.arraycopy(pads, 0, dest, count, amountToPad);
            return dest;
        }

        /**
         * Decode byte [ ].
         *
         * @param decrypted the decrypted
         * @return the byte [ ]
         * @since 1.0.0
         */
        @Contract(pure = true)
        static byte[] decode(byte[] decrypted) {
            int pad = (int) decrypted[decrypted.length - 1];
            if (pad < 1 || pad > BLOCK_SIZE) {
                pad = 0;
            }
            if (pad > 0) {
                return Arrays.copyOfRange(decrypted, 0, decrypted.length - pad);
            }
            return decrypted;
        }
    }
}
