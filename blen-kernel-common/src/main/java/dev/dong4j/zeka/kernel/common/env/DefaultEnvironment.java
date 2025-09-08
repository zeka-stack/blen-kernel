package dev.dong4j.zeka.kernel.common.env;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.StandardEnvironment;

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.04.10 16:51
 * @since 1.0.0
 */
public class DefaultEnvironment extends StandardEnvironment {

    /** DEFAULT_PROPERTIES_PROPERTY_SOURCE_NAME */
    public static final String DEFAULT_PROPERTIES_PROPERTY_SOURCE_NAME = "defaultProperties";
    /** DEFAULT_EXTEND_PROPERTIES_PROPERTY_SOURCE_NAME */
    public static final String DEFAULT_EXTEND_PROPERTIES_PROPERTY_SOURCE_NAME = "defaultExtendProperties";

    /** Property source */
    private Map<String, Object> mapProperties;
    /** Property source */
    private Collection<PropertySource<?>> propertySources;

    /**
     * Default environment
     *
     * @since 1.0.0
     */
    public DefaultEnvironment() {
        super();
    }

    /**
     * Default environment
     *
     * @param mapProperties map properties
     * @since 1.0.0
     */
    public DefaultEnvironment(Map<String, Object> mapProperties) {
        this();
        this.mapProperties = mapProperties;
    }

    /**
     * Default environment
     *
     * @param propertySource property source
     * @since 1.0.0
     */
    public DefaultEnvironment(PropertySource<?> propertySource) {
        this(new ArrayList<>(Collections.singleton(propertySource)));
    }

    /**
     * Default environment
     *
     * @param propertySources property sources
     * @since 1.0.0
     */
    public DefaultEnvironment(Collection<PropertySource<?>> propertySources) {
        this();
        this.propertySources = propertySources;
    }


    /**
     * Customize property sources *
     *
     * @param propertySources property sources
     * @since 1.0.0
     */
    @Override
    protected void customizePropertySources(@NotNull MutablePropertySources propertySources) {
        if (this.mapProperties != null) {
            propertySources.addLast(new DefaultEnvironmentPropertySource(DEFAULT_PROPERTIES_PROPERTY_SOURCE_NAME, this.mapProperties));
        }

        if (this.propertySources != null && propertySources.size() > 0) {
            propertySources.forEach(propertySources::addFirst);
        }

        super.customizePropertySources(propertySources);
    }

}
