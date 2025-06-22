package dev.dong4j.zeka.kernel.spi.config;


import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.8.0
 * @email "mailto:dong4j@gmaidl.com"
 * @date 2021.02.26 17:47
 * @since 1.8.0
 */
@Slf4j
public class CompositeConfiguration implements Configuration {

    /** Config list */
    private final List<Configuration> configList = new LinkedList<>();

    /**
     * Composite configuration
     *
     * @since 1.8.0
     */
    public CompositeConfiguration() {

    }

    /**
     * Composite configuration
     *
     * @param configurations configurations
     * @since 1.8.0
     */
    public CompositeConfiguration(Configuration... configurations) {
        if (configurations != null && configurations.length > 0) {
            Arrays.stream(configurations).filter(config -> !this.configList.contains(config)).forEach(this.configList::add);
        }
    }

    /**
     * Add configuration
     *
     * @param configuration configuration
     * @since 1.8.0
     */
    public void addConfiguration(Configuration configuration) {
        if (this.configList.contains(configuration)) {
            return;
        }
        this.configList.add(configuration);
    }

    /**
     * Add configuration first
     *
     * @param configuration configuration
     * @since 1.8.0
     */
    public void addConfigurationFirst(Configuration configuration) {
        this.addConfiguration(0, configuration);
    }

    /**
     * Add configuration
     *
     * @param pos           pos
     * @param configuration configuration
     * @since 1.8.0
     */
    public void addConfiguration(int pos, Configuration configuration) {
        this.configList.add(pos, configuration);
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
        Configuration firstMatchingConfiguration = null;
        for (Configuration config : this.configList) {
            try {
                if (config.containsKey(key)) {
                    firstMatchingConfiguration = config;
                    break;
                }
            } catch (Exception e) {
                log.error("Error when trying to get value for key " + key + " from " + config + ", will continue to try the next one.");
            }
        }
        if (firstMatchingConfiguration != null) {
            return firstMatchingConfiguration.getProperty(key);
        } else {
            return null;
        }
    }

    /**
     * Contains key
     *
     * @param key key
     * @return the boolean
     * @since 1.8.0
     */
    @Override
    public boolean containsKey(String key) {
        return this.configList.stream().anyMatch(c -> c.containsKey(key));
    }
}
