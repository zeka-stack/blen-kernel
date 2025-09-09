package dev.dong4j.zeka.kernel.common.util;

import com.google.common.collect.Sets;
import dev.dong4j.zeka.kernel.common.enums.LibraryEnum;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.springframework.util.ObjectUtils;
import org.springframework.util.PathMatcher;

/**
 * <p>安全相关工具类.
 * <p>提供安全相关的工具方法，包括URL过滤、路径匹配等.
 * <p>主要功能：
 * <ul>
 *     <li>默认忽略URL列表管理</li>
 *     <li>自定义忽略URL合并</li>
 *     <li>路径匹配检查</li>
 *     <li>安全路径过滤</li>
 * </ul>
 * <p>使用示例：
 * <pre>
 * // 获取默认忽略的URL列表
 * Set<String> defaultSkipUrls = SecurityUtils.DEFAULT_SKIP_URL;
 *
 * // 合并自定义忽略URL
 * String customIgnoreUrls = "/api/test/**,/static/**";
 * Set<String> mergedUrls = SecurityUtils.mergeSkipPatterns(customIgnoreUrls);
 *
 * // 路径匹配检查
 * String requestPath = "/api/users";
 * String[] includePatterns = {"/api/**"};
 * String[] excludePatterns = {"/api/test/**"};
 * PathMatcher pathMatcher = new AntPathMatcher();
 * boolean shouldSkip = SecurityUtils.matches(requestPath, includePatterns, excludePatterns, pathMatcher);
 * </pre>
 * <p>技术特性：
 * <ul>
 *     <li>预定义常用忽略URL模式</li>
 *     <li>支持自定义忽略URL配置</li>
 *     <li>集成Spring PathMatcher进行路径匹配</li>
 *     <li>提供灵活的URL过滤机制</li>
 * </ul>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.06.19 23:44
 * @since 1.0.0
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
     * @since 1.0.0
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
     * @since 1.0.0
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
     * @since 1.0.0
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
