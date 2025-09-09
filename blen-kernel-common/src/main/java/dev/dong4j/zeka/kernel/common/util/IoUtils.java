package dev.dong4j.zeka.kernel.common.util;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.lang.Nullable;

/**
 * <p>Description: IO工具类，提供IO操作相关的工具方法，包括字节流处理、字符编码转换等</p>
 * <p>
 * 主要功能：
 * <ul>
 *     <li>字节数组与字符串转换</li>
 *     <li>输入输出流处理</li>
 *     <li>字符编码转换</li>
 *     <li>资源安全关闭</li>
 * </ul>
 * </p>
 * <p>
 * 使用示例：
 * <pre>
 * // 字节数组转字符串
 * byte[] bytes = "Hello World".getBytes(StandardCharsets.UTF_8);
 * String str = IoUtils.toString(bytes);
 *
 * // InputStream转字符串
 * InputStream inputStream = new ByteArrayInputStream(bytes);
 * String str2 = IoUtils.toString(inputStream);
 *
 * // 字符串写入OutputStream
 * ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 * IoUtils.write("Hello World", outputStream, StandardCharsets.UTF_8);
 * byte[] result = outputStream.toByteArray();
 *
 * // 安全关闭资源
 * InputStream is = null;
 * try {
 *     is = new FileInputStream("file.txt");
 *     // 处理文件
 * } finally {
 *     IoUtils.closeQuietly(is);
 * }
 * </pre>
 * </p>
 * <p>
 * 技术特性：
 * <ul>
 *     <li>继承Spring的StreamUtils功能</li>
 *     <li>提供多种字符编码支持</li>
 *     <li>安全的资源关闭机制</li>
 *     <li>异常安全处理</li>
 * </ul>
 * </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2019.12.26 21:37
 * @since 1.0.0
 */
@UtilityClass
public class IoUtils extends org.springframework.util.StreamUtils {

    /**
     * byte[] 转 String, 编码默认 UTF-8
     *
     * @param input 输入的字节数组
     * @return 转换后的字符串
     * @since 1.0.0
     */
    @NotNull
    public static String toString(byte[] input) {
        return toString(input, Charsets.UTF_8_NAME);
    }

    /**
     * 使用指定的字符编码以字符串形式获取 byte[] 的内容
     *
     * @param input    字节数组
     * @param encoding 编码格式
     * @return 转换后的字符串
     * @throws NullPointerException 如果输入为null
     * @throws IOException          如果发生I/O错误
     * @since 1.0.0
     */
    @NotNull
    @Contract("_, _ -> new")
    public static String toString(byte[] input, String encoding) {
        return new String(input, Charsets.of(encoding));
    }

    /**
     * InputStream 转 String，使用UTF-8编码
     *
     * @param input 输入流
     * @return 转换后的字符串
     * @since 1.0.0
     */
    @NotNull
    public static String toString(InputStream input) {
        return toString(input, Charsets.UTF_8);
    }

    /**
     * InputStream 转 String
     *
     * @param input   输入流
     * @param charset 字符集
     * @return 转换后的字符串
     * @since 1.0.0
     */
    @NotNull
    public static String toString(@Nullable InputStream input, java.nio.charset.Charset charset) {
        try {
            return IoUtils.copyToString(input, charset);
        } catch (IOException e) {
            throw Exceptions.unchecked(e);
        } finally {
            IoUtils.closeQuietly(input);
        }
    }

    /**
     * 安静地关闭资源，不抛出异常
     *
     * @param closeable 可关闭的资源
     * @since 1.0.0
     */
    public static void closeQuietly(@Nullable Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException ioe) {
            // ignore
        }
    }

    /**
     * InputStream 转 byte[]
     *
     * @param input 输入流
     * @return 字节数组
     * @since 1.0.0
     */
    public static byte[] toByteArray(@Nullable InputStream input) {
        try {
            return IoUtils.copyToByteArray(input);
        } catch (IOException e) {
            throw Exceptions.unchecked(e);
        } finally {
            IoUtils.closeQuietly(input);
        }
    }

    /**
     * 将字符串写入OutputStream
     * <p>
     * 此方法使用 {@link String#getBytes(String)} 方法
     *
     * @param data     要写入的字符串
     * @param output   输出流
     * @param encoding 编码格式
     * @throws IOException 如果发生I/O错误
     * @since 1.0.0
     */
    public static void write(@Nullable String data, OutputStream output, java.nio.charset.Charset encoding) throws IOException {
        if (data != null) {
            output.write(data.getBytes(encoding));
        }
    }
}
