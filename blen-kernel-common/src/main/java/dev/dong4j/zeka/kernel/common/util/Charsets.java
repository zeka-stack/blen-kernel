package dev.dong4j.zeka.kernel.common.util;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;
import lombok.experimental.UtilityClass;
import org.springframework.util.StringUtils;

/**
 * <p>字符集工具类.
 * <p>提供常用的字符集常量和字符集处理功能，简化字符集操作.
 * <p>主要功能：
 * <ul>
 *     <li>定义常用的字符集常量（UTF-8、GBK、ISO-8859-1等）</li>
 *     <li>提供字符集对象的创建和转换功能</li>
 *     <li>支持根据字符集名称获取字符集对象</li>
 *     <li>处理字符集相关的异常情况</li>
 * </ul>
 * <p>使用示例：
 * <pre>
 * // 使用预定义的字符集常量
 * String text = "中文测试";
 * byte[] bytes = text.getBytes(Charsets.UTF_8);
 *
 * // 根据名称获取字符集
 * Charset charset = Charsets.of("GBK");
 * String decoded = new String(bytes, charset);
 * </pre>
 * <p>技术特性：
 * <ul>
 *     <li>使用 lombok 的 @UtilityClass 注解，确保类为 final 且构造函数私有</li>
 *     <li>继承 Spring 的字符集处理功能</li>
 *     <li>提供安全的字符集获取方法，避免 UnsupportedCharsetException</li>
 *     <li>支持默认字符集的自动获取</li>
 * </ul>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.27 14:55
 * @since 1.0.0
 */
@UtilityClass
public class Charsets {

    /** 字符集ISO-8859-1 */
    public static final Charset ISO_8859_1 = StandardCharsets.ISO_8859_1;
    /** The constant ISO_8859_1_NAME. */
    public static final String ISO_8859_1_NAME = ISO_8859_1.name();
    /** 字符集GBK */
    public static final Charset GBK = of("GBK");
    /** The constant GBK_NAME. */
    public static final String GBK_NAME = GBK.name();
    /** 字符集utf-8 */
    public static final Charset UTF_8 = StandardCharsets.UTF_8;
    /** The constant UTF_8_NAME. */
    public static final String UTF_8_NAME = UTF_8.name();

    /**
     * 转换为Charset对象
     *
     * @param charsetName 字符集,为空则返回默认字符集
     * @return Charsets charset
     * @throws UnsupportedCharsetException 编码不支持
     * @since 1.0.0
     */
    public static Charset of(String charsetName) throws UnsupportedCharsetException {
        return StringUtils.hasText(charsetName) ? Charset.forName(charsetName) : Charset.defaultCharset();
    }

}
