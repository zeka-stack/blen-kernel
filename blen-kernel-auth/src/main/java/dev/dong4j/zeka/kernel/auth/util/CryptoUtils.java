package dev.dong4j.zeka.kernel.auth.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

/**
 * 密码加密工具类，提供安全的密码加密和验证功能
 * <p>
 * 该工具类基于 MD5 摘要算法和盐值机制，为用户密码提供可靠的加密保护
 * 采用“盐值 + 密码 + 盐值”的加密模式，有效防止彩虹表攻击和字典攻击
 * <p>
 * 主要功能：
 * - 密码加密：支持默认盐值和自定义盐值加密
 * - 密码验证：比较用户输入密码与存储的加密密码
 * - 安全盐值：内置默认盐值，也支持自定义盐值
 * <p>
 * 安全注意事项：
 * - 默认盐值仅供开发和测试使用
 * - 生产环境建议使用独特的盐值
 * - 密码加密后不可逆，只能通过验证方式检查
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.27 14:47
 * @since 1.0.0
 */
@Slf4j
@UtilityClass
public class CryptoUtils {

    /** 认证系统内部默认盐值 */
    private static final String AUTH_SALT = "7xL9sTqN2zXaVbKm";

    /**
     * 使用默认盐值加密密码
     *
     * @param rawPassword 原始密码字符序列
     * @return 加密后的密码字符串
     * @since 1.0.0
     */
    public static String encode(@NotNull CharSequence rawPassword) {
        return encode(rawPassword, AUTH_SALT);
    }

    /**
     * 使用指定盐值加密密码
     *
     * @param rawPassword 原始密码字符序列
     * @param salt        自定义盐值
     * @return 加密后的密码字符串
     * @since 1.0.0
     */
    public static String encode(@NotNull CharSequence rawPassword, String salt) {
        return generatePassword(rawPassword.toString(), salt);
    }

    /**
     * 验证用户输入密码与数据库存储密码是否一致
     *
     * @param rawPassword     用户输入的原始密码
     * @param encodedPassword 数据库中存储的加密密码
     * @return 密码是否匹配，true 表示匹配，false 表示不匹配
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
     * 生成加密密码，使用格式：md5(盐值 + 密码 + 盐值)
     *
     * @param password 原始密码
     * @param salt     加密盐值
     * @return 加密后的密码字符串
     * @since 1.0.0
     */
    private static String generatePassword(String password, String salt) {
        return DigestUtils.encode(salt + password + salt, DigestUtils.ALGORITHM.MD5);
    }

    /**
     * 使用默认盐值生成加密密码
     *
     * @param password 原始密码
     * @return 加密后的密码字符串
     * @since 1.0.0
     */
    private static String generatePassword(String password) {
        return generatePassword(password, AUTH_SALT);
    }
}
