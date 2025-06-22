package dev.dong4j.zeka.kernel.auth.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * <p>Description: </p>
 *
 * @author Spark.Team
 * @version 1.0.0
 * @email "mailto:Spark.Team@gmail.com"
 * @date 2023.04.13 09:21
 * @since 2022.1.1
 */
@SuppressWarnings("all")
public class DigestUtils {
    /**
     * Digest utils
     *
     * @since 2022.1.1
     */
    public DigestUtils() {
    }

    /**
     * Encode
     *
     * @param msg       msg
     * @param algorithm algorithm
     * @return the string
     * @since 2022.1.1
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
     * <p>Description: </p>
     *
     * @author Spark.Team
     * @version 1.0.0
     * @email "mailto:Spark.Team@gmail.com"
     * @date 2023.04.13 09:21
     * @since 2022.1.1
     */
    public static enum ALGORITHM {
        /** Md 2 algorithm */
        MD2,
        /** Md 5 algorithm */
        MD5,
        /** Sha 1 algorithm */
        SHA1,
        /** Sha 256 algorithm */
        SHA256,
        /** Sha 384 algorithm */
        SHA384,
        /** Sha 512 algorithm */
        SHA512;

        /**
         * Algorithm
         *
         * @since 2022.1.1
         */
        private ALGORITHM() {
        }
    }
}
