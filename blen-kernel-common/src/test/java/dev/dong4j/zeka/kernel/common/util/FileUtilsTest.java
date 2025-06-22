package dev.dong4j.zeka.kernel.common.util;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;

/**
 * <p>Description:  </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.05.05 21:25
 * @since 1.0.0
 */
@Slf4j
class FileUtilsTest {

    /**
     * Append path
     *
     * @since 1.0.0
     */
    @Test
    void appendPath() {
        String path1 = FileUtils.appendPath("a", "b", "c");
        String path2 = FileUtils.appendPath("a", "b/", "c");
        String path3 = FileUtils.appendPath("a", "/b/", "/c");
        String path4 = FileUtils.appendPath();
        String path5 = FileUtils.appendPath("/a/d/e/", "/b/", "/c");
        Assertions.assertEquals("a/b/c", path1);
        Assertions.assertEquals("a/b/c", path2);
        Assertions.assertEquals("a/b/c", path3);
        Assertions.assertEquals("", path4);
        Assertions.assertEquals("/a/d/e/b/c", path5);
    }

    /**
     * Append path 1
     *
     * @since 1.0.0
     */
    @Test
    void appendPath_1() {
        String path1 = this.sample("a", "b", "c");
        String path2 = this.sample("a", "b/", "c");
        String path3 = this.sample("a", "/b/", "/c");
        String path4 = this.sample();
        String path5 = this.sample("/a/d/e/", "/b/", "/c");
        Assertions.assertEquals("a/b/c", path1);
        Assertions.assertEquals("a/b/c", path2);
        Assertions.assertEquals("a/b/c", path3);
        Assertions.assertEquals("", path4);
        Assertions.assertEquals("/a/d/e/b/c", path5);
    }


    /**
     * Sample
     *
     * @param paths paths
     * @return the string
     * @since 1.0.0
     */
    private @NotNull String sample(String... paths) {
        // 删除前缀后后缀
        for (int i = 0; i < paths.length; i++) {
            if (paths[i].endsWith(File.separator)) {
                paths[i] = paths[i].substring(0, paths[i].lastIndexOf(File.separator));
            }
            // 第一个路径不删除前缀
            if (i == 0) {
                continue;
            }
            if (paths[i].startsWith(File.separator)) {
                paths[i] = paths[i].substring(paths[i].indexOf(File.separator) + 1);
            }
        }

        return String.join(File.separator, paths);
    }
}
