package dev.dong4j.zeka.kernel.spi.config;


/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.8.0
 * @email "mailto:dong4j@gmaidl.com"
 * @date 2021.02.26 17:47
 * @since 1.8.0
 */
public class SystemConfiguration extends AbstractPrefixConfiguration {

    /**
     * System configuration
     *
     * @param prefix prefix
     * @param id     id
     * @since 1.8.0
     */
    public SystemConfiguration(String prefix, String id) {
        super(prefix, id);
    }

    /**
     * System configuration
     *
     * @since 1.8.0
     */
    public SystemConfiguration() {
        this(null, null);
    }

    /**
     * Gets internal property *
     *
     * @param key key
     * @return the internal property
     * @since 1.8.0
     */
    @Override
    public Object getInternalProperty(String key) {
        return System.getProperty(key);
    }

}
