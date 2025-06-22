package dev.dong4j.zeka.kernel.auth.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

/**
 * <p>Description: 密码加密方式 </p>
 *
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.27 14:47
 * @since 1.0.0
 */
@Slf4j
@UtilityClass
public class CryptoUtils {

    /** auth 系统内部盐值 */
    private static final String AUTH_SALT = "cDfkHw13y3temu3eR";

    /**
     * 密码加密
     *
     * @param rawPassword the raw password
     * @return the string
     * @since 1.0.0
     */
    public static String encode(@NotNull CharSequence rawPassword) {
        return encode(rawPassword, AUTH_SALT);
    }

    /**
     * Encode
     *
     * @param rawPassword raw password
     * @param salt        salt
     * @return the string
     * @since 1.8.0
     */
    public static String encode(@NotNull CharSequence rawPassword, String salt) {
        return generatePassword(rawPassword.toString(), salt);
    }

    /**
     * 用户输入的密码与数据库密码对比
     *
     * @param rawPassword     the raw password 用户输入的密码
     * @param encodedPassword the encoded password 已加密的密码
     * @return the boolean
     * @since 1.0.0
     */
    public static boolean matches(CharSequence rawPassword, String encodedPassword) {
        if (encodedPassword == null || encodedPassword.isEmpty()) {
            log.warn("Empty encoded password");
            return false;
        }

        return generatePassword(rawPassword.toString()).equals(encodedPassword);
    }

    /**
     * 生成管理员密码 password = md5(盐值 password 盐值)
     *
     * @param password the password
     * @param salt     salt
     * @return String string
     * @since 1.0.0
     */
    private static String generatePassword(String password, String salt) {
        return DigestUtils.encode(salt + password + salt, DigestUtils.ALGORITHM.MD5);
    }

    /**
     * Generate password
     *
     * @param password password
     * @return the string
     * @since 1.8.0
     */
    private static String generatePassword(String password) {
        return generatePassword(password, AUTH_SALT);
    }
}
