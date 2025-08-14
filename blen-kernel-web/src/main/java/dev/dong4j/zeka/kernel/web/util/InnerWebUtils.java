package dev.dong4j.zeka.kernel.web.util;

import dev.dong4j.zeka.kernel.common.constant.ConfigKey;
import dev.dong4j.zeka.kernel.common.util.ConfigKit;
import dev.dong4j.zeka.kernel.common.util.StringPool;
import dev.dong4j.zeka.kernel.common.util.StringUtils;
import jakarta.servlet.Filter;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import org.springframework.boot.web.servlet.FilterRegistrationBean;

/**
 * <p>Description:  </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2022.01.06 23:04
 * @since 2022.1.1
 */
@UtilityClass
public class InnerWebUtils {

    /**
     * Sets url patterns *
     *
     * @param filter logging rb
     * @param order  order
     * @since 1.0.0
     */
    public void setUrlPatterns(@NotNull FilterRegistrationBean<? extends Filter> filter, int order) {
        setUrlPatterns(filter, order, buildUrlPatterns());
    }

    /**
     * Sets url patterns *
     *
     * @param filter      filter
     * @param order       order
     * @param urlPatterns url patterns
     * @since 1.5.0
     */
    public void setUrlPatterns(@NotNull FilterRegistrationBean<? extends Filter> filter, int order, Collection<String> urlPatterns) {
        filter.setUrlPatterns(urlPatterns);
        filter.setOrder(order);
    }


    /**
     * Build url patterns
     *
     * @return the list
     * @since 1.5.0
     */
    public @NotNull @Unmodifiable List<String> buildUrlPatterns() {
        String contextPath = ConfigKit.getProperty(ConfigKey.SpringConfigKey.SERVER_CONTEXT_PATH, StringPool.SLASH);
        contextPath = StringUtils.removeSuffix(contextPath, StringPool.SLASH);
        return Collections.singletonList(contextPath + StringPool.ANY_URL_PATTERNS);
    }
}
