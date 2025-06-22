package dev.dong4j.zeka.kernel.spi.config;


import dev.dong4j.zeka.kernel.spi.constants.CommonConstants;
import dev.dong4j.zeka.kernel.spi.utils.SpiStringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.8.0
 * @email "mailto:dong4j@gmaidl.com"
 * @date 2021.02.26 17:47
 * @since 1.8.0
 */
@SuppressWarnings("PMD.UndefineMagicConstantRule")
public class Environment {
    /** INSTANCE */
    private static final Environment INSTANCE = new Environment();

    /** Properties configs */
    private final Map<String, PropertiesConfiguration> propertiesConfigs = new ConcurrentHashMap<>();
    /** System configs */
    private final Map<String, SystemConfiguration> systemConfigs = new ConcurrentHashMap<>();
    /** Environment configs */
    private final Map<String, EnvironmentConfiguration> environmentConfigs = new ConcurrentHashMap<>();
    /** External configs */
    private final Map<String, InmemoryConfiguration> externalConfigs = new ConcurrentHashMap<>();
    /** App external configs */
    private final Map<String, InmemoryConfiguration> appExternalConfigs = new ConcurrentHashMap<>();

    /** External configuration map */
    private Map<String, String> externalConfigurationMap = new HashMap<>();
    /** App external configuration map */
    private Map<String, String> appExternalConfigurationMap = new HashMap<>();

    /** Config center first */
    private boolean configCenterFirst = true;

    /** Dynamic configuration */
    private Configuration dynamicConfiguration;

    /**
     * Gets instance *
     *
     * @return the instance
     * @since 1.8.0
     */
    public static Environment getInstance() {
        return INSTANCE;
    }

    /**
     * Gets properties config *
     *
     * @param prefix prefix
     * @param id     id
     * @return the properties config
     * @since 1.8.0
     */
    public PropertiesConfiguration getPropertiesConfig(String prefix, String id) {
        return this.propertiesConfigs.computeIfAbsent(toKey(prefix, id), k -> new PropertiesConfiguration(prefix, id));
    }

    /**
     * Gets system config *
     *
     * @param prefix prefix
     * @param id     id
     * @return the system config
     * @since 1.8.0
     */
    public SystemConfiguration getSystemConfig(String prefix, String id) {
        return this.systemConfigs.computeIfAbsent(toKey(prefix, id), k -> new SystemConfiguration(prefix, id));
    }

    /**
     * Gets external config *
     *
     * @param prefix prefix
     * @param id     id
     * @return the external config
     * @since 1.8.0
     */
    public InmemoryConfiguration getExternalConfig(String prefix, String id) {
        return this.externalConfigs.computeIfAbsent(toKey(prefix, id), k -> {
            InmemoryConfiguration configuration = new InmemoryConfiguration(prefix, id);
            configuration.setProperties(this.externalConfigurationMap);
            return configuration;
        });
    }

    /**
     * Gets app external config *
     *
     * @param prefix prefix
     * @param id     id
     * @return the app external config
     * @since 1.8.0
     */
    public InmemoryConfiguration getAppExternalConfig(String prefix, String id) {
        return this.appExternalConfigs.computeIfAbsent(toKey(prefix, id), k -> {
            InmemoryConfiguration configuration = new InmemoryConfiguration(prefix, id);
            configuration.setProperties(this.appExternalConfigurationMap);
            return configuration;
        });
    }

    /**
     * Gets environment config *
     *
     * @param prefix prefix
     * @param id     id
     * @return the environment config
     * @since 1.8.0
     */
    public EnvironmentConfiguration getEnvironmentConfig(String prefix, String id) {
        return this.environmentConfigs.computeIfAbsent(toKey(prefix, id), k -> new EnvironmentConfiguration(prefix, id));
    }

    /**
     * Sets external config map *
     *
     * @param externalConfiguration external configuration
     * @since 1.8.0
     */
    public void setExternalConfigMap(Map<String, String> externalConfiguration) {
        this.externalConfigurationMap = externalConfiguration;
    }

    /**
     * Sets app external config map *
     *
     * @param appExternalConfiguration app external configuration
     * @since 1.8.0
     */
    public void setAppExternalConfigMap(Map<String, String> appExternalConfiguration) {
        this.appExternalConfigurationMap = appExternalConfiguration;
    }

    /**
     * Gets external configuration map *
     *
     * @return the external configuration map
     * @since 1.8.0
     */
    public Map<String, String> getExternalConfigurationMap() {
        return this.externalConfigurationMap;
    }

    /**
     * Gets app external configuration map *
     *
     * @return the app external configuration map
     * @since 1.8.0
     */
    public Map<String, String> getAppExternalConfigurationMap() {
        return this.appExternalConfigurationMap;
    }

    /**
     * Update external configuration map
     *
     * @param externalMap external map
     * @since 1.8.0
     */
    public void updateExternalConfigurationMap(Map<String, String> externalMap) {
        this.externalConfigurationMap.putAll(externalMap);
    }

    /**
     * Update app external configuration map
     *
     * @param externalMap external map
     * @since 1.8.0
     */
    public void updateAppExternalConfigurationMap(Map<String, String> externalMap) {
        this.appExternalConfigurationMap.putAll(externalMap);
    }

    /**
     * Gets configuration *
     *
     * @param prefix prefix
     * @param id     id
     * @return the configuration
     * @since 1.8.0
     */
    public CompositeConfiguration getConfiguration(String prefix, String id) {
        CompositeConfiguration compositeConfiguration = new CompositeConfiguration();
        // Config center has the highest priority
        compositeConfiguration.addConfiguration(this.getSystemConfig(prefix, id));
        compositeConfiguration.addConfiguration(this.getEnvironmentConfig(prefix, id));
        compositeConfiguration.addConfiguration(this.getAppExternalConfig(prefix, id));
        compositeConfiguration.addConfiguration(this.getExternalConfig(prefix, id));
        compositeConfiguration.addConfiguration(this.getPropertiesConfig(prefix, id));
        return compositeConfiguration;
    }

    /**
     * Gets configuration *
     *
     * @return the configuration
     * @since 1.8.0
     */
    public Configuration getConfiguration() {
        return this.getConfiguration(null, null);
    }

    /**
     * To key
     *
     * @param prefix prefix
     * @param id     id
     * @return the string
     * @since 1.8.0
     */
    private static String toKey(String prefix, String id) {
        StringBuilder sb = new StringBuilder();
        if (SpiStringUtils.isNotEmpty(prefix)) {
            sb.append(prefix);
        }
        if (SpiStringUtils.isNotEmpty(id)) {
            sb.append(id);
        }

        if (sb.length() > 0 && sb.charAt(sb.length() - 1) != '.') {
            sb.append(".");
        }

        if (sb.length() > 0) {
            return sb.toString();
        }
        return CommonConstants.DUBBO;
    }

    /**
     * Is config center first
     *
     * @return the boolean
     * @since 1.8.0
     */
    public boolean isConfigCenterFirst() {
        return this.configCenterFirst;
    }

    /**
     * Sets config center first *
     *
     * @param configCenterFirst config center first
     * @since 1.8.0
     */
    public void setConfigCenterFirst(boolean configCenterFirst) {
        this.configCenterFirst = configCenterFirst;
    }

    /**
     * Gets dynamic configuration *
     *
     * @return the dynamic configuration
     * @since 1.8.0
     */
    public Optional<Configuration> getDynamicConfiguration() {
        return Optional.ofNullable(this.dynamicConfiguration);
    }

    /**
     * Sets dynamic configuration *
     *
     * @param dynamicConfiguration dynamic configuration
     * @since 1.8.0
     */
    public void setDynamicConfiguration(Configuration dynamicConfiguration) {
        this.dynamicConfiguration = dynamicConfiguration;
    }

    /**
     * Clear external configs
     * For test
     *
     * @since 1.8.0
     */
    public void clearExternalConfigs() {
        this.externalConfigs.clear();
        this.externalConfigurationMap.clear();
    }

    /**
     * Clear app external configs
     * For test
     *
     * @since 1.8.0
     */
    public void clearAppExternalConfigs() {
        this.appExternalConfigs.clear();
        this.appExternalConfigurationMap.clear();
    }
}
