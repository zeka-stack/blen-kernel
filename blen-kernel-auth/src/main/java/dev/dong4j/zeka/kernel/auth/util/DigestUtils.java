package dev.dong4j.zeka.kernel.auth.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 摘要算法工具类，提供多种数字摘要算法的统一封装
 * <p>
 * 该工具类支持常用的数字摘要算法，包括 MD2、MD5、SHA-1、SHA-256、SHA-384、SHA-512
 * 提供统一的接口进行数据摘要计算，返回十六进制字符串格式的结果
 * <p>
 * 主要功能：
 * - 支持多种摘要算法（MD5、SHA 系列）
 * - 统一的算法接口和调用方式
 * - 自动处理字符串转换和格式化
 * - 返回标准化的十六进制结果
 * <p>
 * 使用场景：密码加密、数据完整性校验、数字签名等
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2023.04.13 09:21
 * @since 1.0.0
 */
@SuppressWarnings("all")
public class DigestUtils {
    /**
     * 默认构造函数
     *
     * @since 1.0.0
     */
    public DigestUtils() {
    }

    /**
     * 使用指定算法对数据进行摘要计算
     *
     * @param msg       待计算摘要的数据字符串
     * @param algorithm 摘要算法类型
     * @return 十六进制格式的摘要结果字符串
     * @since 1.0.0
     */
    public static String encode(String msg, ALGORITHM algorithm) {
        try {
            byte[] digest = msg.getBytes();
            MessageDigest md;
            (md = MessageDigest.getInstance(algorithm.name())).update(digest);
            digest = md.digest();
            StringBuilder hexValue = new StringBuilder();

            for (byte b : digest) {
                String dghex;
                if ((dghex = Integer.toHexString(b & 255)).length() == 1) {
                    hexValue.append("0");
                }

                hexValue.append(dghex);
            }

            return hexValue.toString();
        } catch (NoSuchAlgorithmException var4) {
            var4.printStackTrace();
            return null;
        }
    }

    /**
     * 摘要算法枚举类，定义了系统支持的所有摘要算法类型
     * <p>
     * 支持的算法包括：
     * - MD2：128 位摘要，安全性较低，不建议使用
     * - MD5：128 位摘要，广泛使用，但存在安全隐患
     * - SHA1：160 位摘要，存在安全隐患
     * - SHA256：256 位摘要，安全性较好，推荐使用
     * - SHA384：384 位摘要，高安全性
     * - SHA512：512 位摘要，最高安全性
     *
     * @author dong4j
     * @version 1.0.0
     * @email "mailto:dong4j@gmail.com"
     * @date 2023.04.13 09:21
     * @since 1.0.0
     */
    public static enum ALGORITHM {
        /** MD2 摘要算法 */
        MD2,
        /** MD5 摘要算法 */
        MD5,
        /** SHA-1 摘要算法 */
        SHA1,
        /** SHA-256 摘要算法 */
        SHA256,
        /** SHA-384 摘要算法 */
        SHA384,
        /** SHA-512 摘要算法 */
        SHA512;

        /**
         * 私有构造函数，防止实例化
         *
         * @since 1.0.0
         */
        private ALGORITHM() {
        }
    }
}
