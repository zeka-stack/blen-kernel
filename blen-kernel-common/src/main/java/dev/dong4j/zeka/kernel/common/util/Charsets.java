package dev.dong4j.zeka.kernel.common.util;

import lombok.experimental.UtilityClass;
import org.springframework.util.StringUtils;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;

/**
 * <p>Description: 字符集工具类 </p>
 *
 * @author dong4j
 * @version 1.2.3
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
