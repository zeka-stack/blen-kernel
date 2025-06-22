package dev.dong4j.zeka.kernel.common.util;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.lang.Nullable;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * <p>Description: IoUtil </p>
 *
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dong4j@gmail.com"
 * @date 2019.12.26 21:37
 * @since 1.0.0
 */
@UtilityClass
public class IoUtils extends org.springframework.util.StreamUtils {

    /**
     * byte[] 转 String, 编码默认 UTF-8
     *
     * @param input input
     * @return the string
     * @since 1.0.0
     */
    @NotNull
    public static String toString(byte[] input) {
        return toString(input, Charsets.UTF_8_NAME);
    }

    /**
     * 使用指定的字符编码以字符串形式获取 byte[] 的内容
     *
     * @param input    the byte array to read from
     * @param encoding the encoding to use, null means platform default
     * @return the requested String
     * @throws NullPointerException if the input is null
     * @throws IOException          if an I/O error occurs (never occurs)
     * @since 1.0.0
     */
    @NotNull
    @Contract("_, _ -> new")
    public static String toString(byte[] input, String encoding) {
        return new String(input, Charsets.of(encoding));
    }

    /**
     * InputStream to String utf-8
     *
     * @param input the <code>InputStream</code> to read from
     * @return the requested String
     * @since 1.0.0
     */
    @NotNull
    public static String toString(InputStream input) {
        return toString(input, Charsets.UTF_8);
    }

    /**
     * InputStream to String
     *
     * @param input   the <code>InputStream</code> to read from
     * @param charset the <code>Charsets</code>
     * @return the requested String
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
     * closeQuietly
     *
     * @param closeable 自动关闭
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
     * To byte array byte [ ].
     *
     * @param input the input
     * @return the byte [ ]
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
     * Writes chars from a <code>String</code> to bytes on an
     * <code>OutputStream</code> using the specified character encoding.
     * <p>
     * This method uses {@link String#getBytes(String)}.
     *
     * @param data     the <code>String</code> to write, null ignored
     * @param output   the <code>OutputStream</code> to write to
     * @param encoding the encoding to use, null means platform default
     * @throws IOException if an I/O error occurs
     * @since 1.0.0
     */
    public static void write(@Nullable String data, OutputStream output, java.nio.charset.Charset encoding) throws IOException {
        if (data != null) {
            output.write(data.getBytes(encoding));
        }
    }
}
