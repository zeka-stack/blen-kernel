package dev.dong4j.zeka.kernel.web.filter;

import dev.dong4j.zeka.kernel.common.util.SecurityUtils;
import dev.dong4j.zeka.kernel.common.util.StringPool;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.lang.Nullable;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * <p>Description: 能够配置需要跳过的 url 的抽象 filter, 子类
 *
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dong4j@gmail.com"
 * @date 2019.11.26 15:48
 * @since 1.0.0
 */
@Slf4j
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class AbstractSkipFilter extends OncePerRequestFilter {
    /** Path matcher */
    protected PathMatcher pathMatcher = new AntPathMatcher();
    /** 需要执行过滤器逻辑的 url */
    @Nullable
    protected String[] includePatterns;
    /** 不需要执行过滤器逻辑的 url */
    @Nullable
    protected String[] excludePatterns;
    /** 业务自定义需要忽略的 url, 最终会写入到 {@link AbstractSkipFilter#includePatterns} */
    protected String skipUrl;

    /**
     * Abstract skip filter
     *
     * @since 2.1.0
     */
    public AbstractSkipFilter() {
        init();
    }

    /**
     * Abstract skip filter
     *
     * @param skipUrl skip url
     * @since 2.1.0
     */
    public AbstractSkipFilter(String skipUrl) {
        this.skipUrl = skipUrl;
        merge();
    }

    /**
     * Init
     *
     * @since 1.5.0
     */
    @PostConstruct
    public void init() {
        merge();
    }

    /**
     * Merge
     *
     * @since 2.1.0
     */
    private void merge() {
        if (this.includePatterns == null) {
            this.includePatterns = new String[]{StringPool.ANY_PATH};
        }

        if (this.excludePatterns == null) {
            this.excludePatterns = SecurityUtils.mergeSkipPatterns(this.skipUrl).toArray(new String[0]);
        } else {
            Set<String> excluded = new HashSet<>(Arrays.asList(this.excludePatterns));
            this.excludePatterns = SecurityUtils.mergeSkipPatterns(excluded, this.skipUrl).toArray(new String[0]);
        }
    }

    /**
     * 不需要拦截的 url
     *
     * @param request request
     * @return the boolean
     * @since 1.5.0
     */
    @Override
    protected boolean shouldNotFilter(@NotNull HttpServletRequest request) {
        return SecurityUtils.matches(request.getRequestURI(), this.includePatterns, this.excludePatterns, this.pathMatcher);
    }

}
