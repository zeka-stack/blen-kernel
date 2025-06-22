package dev.dong4j.zeka.kernel.test;

import dev.dong4j.zeka.kernel.common.env.DefaultEnvironment;
import dev.dong4j.zeka.kernel.common.util.Tools;
import lombok.experimental.UtilityClass;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jetbrains.annotations.Contract;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import java.util.LinkedHashMap;
import java.util.Properties;

/**
 * <p>Description:  </p>
 *
 * @author dong4j
 * @version 1.3.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.03.23 16:47
 * @since 1.0.0
 */
@UtilityClass
class ZekaTestPropertySourceUtils extends TestPropertySourceUtils {
    /** logger */
    private static final Log LOGGER = LogFactory.getLog(ZekaTestPropertySourceUtils.class);
    /** INLINED_PROPERTIES_PROPERTY_SOURCE_NAME */
    private static final String INLINED_PROPERTIES_PROPERTY_SOURCE_NAME = DefaultEnvironment.DEFAULT_PROPERTIES_PROPERTY_SOURCE_NAME;

    /**
     * Add inlined properties to environment *
     *
     * @param environment       environment
     * @param defaultProperties default properties
     * @since 1.0.0
     */
    @Contract("_, _ -> param1")
    static ConfigurableEnvironment addInlinedPropertiesToEnvironment(ConfigurableEnvironment environment, Properties defaultProperties) {
        Assert.notNull(environment, "'environment' must not be null");
        Assert.notNull(defaultProperties, "'defaultProperties' must not be null");
        if (!ObjectUtils.isEmpty(defaultProperties)) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("添加各个模块默认配置到 environment: "
                    + ObjectUtils.nullSafeToString(defaultProperties));
            }
            MapPropertySource ps = (MapPropertySource)
                environment.getPropertySources().get(INLINED_PROPERTIES_PROPERTY_SOURCE_NAME);
            if (ps == null) {
                ps = new MapPropertySource(INLINED_PROPERTIES_PROPERTY_SOURCE_NAME, new LinkedHashMap<>());
                // 优先级最低, 作为默认配置, 保证应用启动正常
                environment.getPropertySources().addLast(ps);
                environment.setDefaultProfiles("junit", "default");
            }
            ps.getSource().putAll(Tools.getMapFromProperties(defaultProperties));

        }
        return environment;
    }
}
