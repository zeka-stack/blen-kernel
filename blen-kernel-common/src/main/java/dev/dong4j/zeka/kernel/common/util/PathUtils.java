package dev.dong4j.zeka.kernel.common.util;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.Contract;
import org.springframework.lang.Nullable;

import java.io.File;
import java.net.URL;

/**
 * <p>Description: 用来获取各种目录 </p>
 *
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.27 01:17
 * @since 1.0.0
 */
@UtilityClass
@SuppressWarnings("checkstyle:ReturnCount")
public class PathUtils {
    /** FILE_PROTOCOL */
    public static final String FILE_PROTOCOL = "file";
    /** JAR_PROTOCOL */
    public static final String JAR_PROTOCOL = "jar";
    /** ZIP_PROTOCOL */
    public static final String ZIP_PROTOCOL = "zip";
    /** FILE_PROTOCOL_PREFIX */
    public static final String FILE_PROTOCOL_PREFIX = "file:";
    /** JAR_FILE_SEPARATOR */
    public static final String JAR_FILE_SEPARATOR = "!/";

    /**
     * 获取jar包运行时的当前目录
     *
     * @return {String}
     * @since 1.0.0
     */
    @Nullable
    public static String getJarPath() {
        try {
            URL url = PathUtils.class.getResource("/").toURI().toURL();
            return PathUtils.toFilePath(url);
        } catch (Exception e) {
            String path = PathUtils.class.getResource("").getPath();
            return new File(path).getParentFile().getParentFile().getAbsolutePath();
        }
    }

    /**
     * To file path string.
     *
     * @param url the url
     * @return the string
     * @since 1.0.0
     */
    @Contract("null -> null")
    @Nullable
    public static String toFilePath(@Nullable URL url) {
        if (url == null) {
            return null;
        }
        String protocol = url.getProtocol();
        String file = UrlUtils.decodeUrl(url.getPath(), Charsets.UTF_8);
        if (FILE_PROTOCOL.equals(protocol)) {
            return new File(file).getParentFile().getParentFile().getAbsolutePath();
        } else if (JAR_PROTOCOL.equals(protocol) || ZIP_PROTOCOL.equals(protocol)) {
            int ipos = file.indexOf(JAR_FILE_SEPARATOR);
            if (ipos > 0) {
                file = file.substring(0, ipos);
            }
            if (file.startsWith(FILE_PROTOCOL_PREFIX)) {
                file = file.substring(FILE_PROTOCOL_PREFIX.length());
            }
            return new File(file).getParentFile().getAbsolutePath();
        }
        return file;
    }

}
