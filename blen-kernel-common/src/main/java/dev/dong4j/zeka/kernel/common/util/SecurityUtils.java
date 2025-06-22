package dev.dong4j.zeka.kernel.common.util;

import com.google.common.collect.Sets;
import dev.dong4j.zeka.kernel.common.enums.LibraryEnum;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.springframework.util.ObjectUtils;
import org.springframework.util.PathMatcher;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>Description:  </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.06.19 23:44
 * @since 1.5.0
 */
@UtilityClass
public class SecurityUtils {
    /** 应用默认的忽略 url 列表 */
    public static final Set<String> DEFAULT_SKIP_URL = Sets.newHashSetWithExpectedSize(32);

    static {
        // 忽略启动信息接口
        DEFAULT_SKIP_URL.add(LibraryEnum.DRUID.getUri() + StringPool.DOUBLE_ASTERISK);
        // 忽略应用检查接口
        DEFAULT_SKIP_URL.add("/actuator/**");
        // 忽略 swagger 相关接口
        DEFAULT_SKIP_URL.add("/v2/api-docs/**");
        DEFAULT_SKIP_URL.add("/v2/api-docs-ext/**");
        DEFAULT_SKIP_URL.add("/swagger-resources/**");
        DEFAULT_SKIP_URL.add("/swagger-dubbo/**");
        DEFAULT_SKIP_URL.add("/doc.html");
        // 忽略验证码相关接口
        DEFAULT_SKIP_URL.add("/captcha/**");
        DEFAULT_SKIP_URL.add("/request-urls");
        // 检查是否需要验证码
        DEFAULT_SKIP_URL.add("/check");
        // 忽略 oauth 相关接口
        DEFAULT_SKIP_URL.add("/oauth/**");
        // 忽略日志相关接口
        DEFAULT_SKIP_URL.add("/log/**");
        // 忽略错误接口
        DEFAULT_SKIP_URL.add("/error/**");
        // 忽略静态资源
        DEFAULT_SKIP_URL.add("/**/*.ico");
        DEFAULT_SKIP_URL.add("/**/*.css");
        DEFAULT_SKIP_URL.add("/**/*.js");
        DEFAULT_SKIP_URL.add("/**/*.html");
        DEFAULT_SKIP_URL.add("/**/*.map");
        DEFAULT_SKIP_URL.add("/**/*.svg");
        DEFAULT_SKIP_URL.add("/**/*.png");
        DEFAULT_SKIP_URL.add("/**/*.gif");
        DEFAULT_SKIP_URL.add("/**/*.bmp");
    }

    /**
     * 将自定义需要忽略的 url 和默认被忽略的 url 合并
     *
     * @param consumterIgnoreUrls 自定义的需要忽略的 url
     * @return the set 合并后的 url
     * @since 1.5.0
     */
    public static @NotNull Set<String> mergeSkipPatterns(String consumterIgnoreUrls) {
        return mergeSkipPatterns(SecurityUtils.DEFAULT_SKIP_URL, consumterIgnoreUrls);
    }

    /**
     * Merge skip patterns
     *
     * @param defaultSkipUrl      default skip url
     * @param consumterIgnoreUrls consumter ignore urls
     * @return the set
     * @since 1.5.0
     */
    public static @NotNull Set<String> mergeSkipPatterns(Collection<String> defaultSkipUrl, String consumterIgnoreUrls) {
        Set<String> ignores = new HashSet<>(defaultSkipUrl);

        if (StringUtils.isNotBlank(consumterIgnoreUrls)) {
            String[] split = consumterIgnoreUrls.split(StringPool.COMMA);
            ignores.addAll(Arrays.stream(split).filter(url -> StringUtils.hasText(url.trim())).collect(Collectors.toSet()));
        }

        return ignores;
    }

    /**
     * 返回 true 则不进入 filter, 否则进入 filter
     *
     * @param lookupPath      lookup path
     * @param includePatterns include patterns
     * @param excludePatterns exclude patterns
     * @param pathMatcher     path matcher
     * @return the boolean
     * @since 1.5.0
     */
    public static boolean matches(String lookupPath,
                                  String[] includePatterns,
                                  String[] excludePatterns,
                                  PathMatcher pathMatcher) {
        if (!ObjectUtils.isEmpty(excludePatterns)) {
            for (String pattern : excludePatterns) {
                if (pathMatcher.match(pattern, lookupPath)) {
                    return true;
                }
            }
        }
        if (ObjectUtils.isEmpty(includePatterns)) {
            return false;
        }
        for (String pattern : includePatterns) {
            if (pathMatcher.match(pattern, lookupPath)) {
                return false;
            }
        }
        return false;
    }
}
