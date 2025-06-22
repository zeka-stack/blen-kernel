package dev.dong4j.zeka.kernel.common.util;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.util.Assert;

import java.io.File;
import java.io.FileFilter;
import java.io.Serializable;

/**
 * <p>Description: 文件后缀过滤器 </p>
 *
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.27 18:17
 * @since 1.0.0
 */
public class SuffixFileFilter implements FileFilter, Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = -3389157631240246157L;

    /** Suffixes */
    private final String[] suffixes;

    /**
     * Instantiates a new Suffix file filter.
     *
     * @param suffix the suffix
     * @since 1.0.0
     */
    @Contract(pure = true)
    public SuffixFileFilter(String suffix) {
        Assert.notNull(suffix, "The suffix must not be null");
        this.suffixes = new String[]{suffix};
    }

    /**
     * Instantiates a new Suffix file filter.
     *
     * @param suffixes the suffixes
     * @since 1.0.0
     */
    public SuffixFileFilter(String[] suffixes) {
        Assert.notNull(suffixes, "The suffix must not be null");
        this.suffixes = new String[suffixes.length];
        System.arraycopy(suffixes, 0, this.suffixes, 0, suffixes.length);
    }

    /**
     * Accept boolean
     *
     * @param pathname pathname
     * @return the boolean
     * @since 1.0.0
     */
    @Override
    public boolean accept(@NotNull File pathname) {
        String name = pathname.getName();
        for (String suffix : this.suffixes) {
            if (this.checkEndsWith(name, suffix)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check ends with boolean.
     *
     * @param str the str
     * @param end the end
     * @return the boolean
     * @since 1.0.0
     */
    public boolean checkEndsWith(@NotNull String str, @NotNull String end) {
        int endLen = end.length();
        return str.regionMatches(true, str.length() - endLen, end, 0, endLen);
    }
}
