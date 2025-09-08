package dev.dong4j.zeka.kernel.spi.config;


import dev.dong4j.zeka.kernel.spi.utils.SpiStringUtils;

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmaidl.com"
 * @date 2021.02.26 17:47
 * @since 1.0.0
 */
public class EnvironmentConfiguration extends AbstractPrefixConfiguration {

    /**
     * Environment configuration
     *
     * @param prefix prefix
     * @param id     id
     * @since 1.0.0
     */
    public EnvironmentConfiguration(String prefix, String id) {
        super(prefix, id);
    }

    /**
     * Environment configuration
     *
     * @since 1.0.0
     */
    public EnvironmentConfiguration() {
        this(null, null);
    }

    /**
     * Gets internal property *
     *
     * @param key key
     * @return the internal property
     * @since 1.0.0
     */
    @Override
    public Object getInternalProperty(String key) {
        String value = System.getenv(key);
        if (SpiStringUtils.isEmpty(value)) {
            value = System.getenv(SpiStringUtils.toOSStyleKey(key));
        }
        return value;
    }

}
