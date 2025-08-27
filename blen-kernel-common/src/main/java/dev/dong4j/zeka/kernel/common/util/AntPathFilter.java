package dev.dong4j.zeka.kernel.common.util;

import java.io.File;
import java.io.FileFilter;
import java.io.Serial;
import java.io.Serializable;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

/**
 * <p>Description: spring AntPath 规则文件过滤 </p>
 *
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dong4j@gmail.com"
 * @date 2019.12.26 21:40
 * @since 1.0.0
 */
@RequiredArgsConstructor
public class AntPathFilter implements FileFilter, Serializable {
    /** serialVersionUID */
    @Serial
    private static final long serialVersionUID = 812598009067554612L;
    /** PATH_MATCHER */
    private static final PathMatcher PATH_MATCHER = new AntPathMatcher();

    /** Pattern */
    private final String pattern;

    /**
     * Accept boolean
     *
     * @param pathname pathname
     * @return the boolean
     * @since 1.0.0
     */
    @Override
    public boolean accept(@NotNull File pathname) {
        String filePath = pathname.getAbsolutePath();
        return PATH_MATCHER.match(pattern, filePath);
    }
}
