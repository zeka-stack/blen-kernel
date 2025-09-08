package dev.dong4j.zeka.kernel.test.mock.util;

import dev.dong4j.zeka.kernel.test.mock.MockException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.util.StreamUtils;

/**
 * <p>Description: 字符串工具 </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2019.05.18 01:05
 * @since 1.0.0
 */
@Slf4j
@UtilityClass
public class MockFileUtil {

    /**
     * The constant MOCK_FILE_NAME.
     */
    public static final String MOCK_FILE_NAME = "string.text";

    /**
     * 去除指定文件 文件内容的所有空白字符 包括回车,空格,制表符等
     *
     * @param path 文件路径
     * @return String string
     * @since 1.0.0
     */
    @NotNull
    public static String trimFile(String path) {
        StringBuilder text = new StringBuilder();
        // 读取文件
        File file = new File(path);
        // 判断文件是否存在
        if (file.isFile() && file.exists()) {
            String line;
            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)))
            ) {
                while ((line = bufferedReader.readLine()) != null) {
                    // 去除空白字符
                    text.append(trimAll(line));
                }
                log.info("{}", text);
            } catch (IOException e) {
                throw new MockException(e.getMessage(), e);
            }

            // 写回文件
            try (
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)))
            ) {
                bufferedWriter.write(text.toString());
            } catch (IOException e) {
                throw new MockException(e.getMessage(), e);
            }

        } else {
            log.error("找不到指定的文件");
        }
        return text.toString();
    }

    /**
     * 去除string的所有空白字符 包括回车,空格,制表符等
     *
     * @param str 字符串
     * @return String string
     * @since 1.0.0
     */
    @NotNull
    @Contract(pure = true)
    public static String trimAll(@NotNull String str) {
        return str.replaceAll("\\s*", "");
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     * @since 1.0.0
     */
    public static void main(String[] args) {
        log.info("{}", readerText(MOCK_FILE_NAME));
    }

    /**
     * 读取text文本
     *
     * @param path 文件路径
     * @return String string
     * @since 1.0.0
     */
    @NotNull
    public static String readerText(String path) {
        // 方法1
        InputStream io = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
        if (io == null) {
            throw new MockException("string.txt 文件不在 classpath 目录下");
        }
        try {
            return StreamUtils.copyToString(io, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new MockException("转换为 String 失败");
        }
    }
}
