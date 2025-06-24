package dev.dong4j.zeka.kernel.test;

import dev.dong4j.zeka.kernel.common.constant.App;
import dev.dong4j.zeka.kernel.common.constant.ConfigDefaultValue;
import dev.dong4j.zeka.kernel.common.constant.ConfigKey;
import dev.dong4j.zeka.kernel.common.env.DefaultEnvironment;
import dev.dong4j.zeka.kernel.common.start.LauncherInitiation;
import dev.dong4j.zeka.kernel.common.util.ConfigKit;
import dev.dong4j.zeka.kernel.common.util.StartUtils;
import dev.dong4j.zeka.kernel.common.util.StringPool;
import dev.dong4j.zeka.kernel.common.util.Tools;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.ConfigurationPropertySource;
import org.springframework.boot.context.properties.source.MapConfigurationPropertySource;
import org.springframework.boot.test.context.ReactiveWebMergedContextConfiguration;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.web.SpringBootMockServletContext;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.boot.web.reactive.context.GenericReactiveWebApplicationContext;
import org.springframework.boot.web.servlet.support.ServletContextApplicationContextInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.core.SpringVersion;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.SimpleCommandLinePropertySource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.MergedContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.springframework.test.context.web.WebMergedContextConfiguration;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.support.GenericWebApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Properties;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

import static org.springframework.test.context.support.TestPropertySourceUtils.INLINED_PROPERTIES_PROPERTY_SOURCE_NAME;
import static org.springframework.test.context.support.TestPropertySourceUtils.convertInlinedPropertiesToMap;

/**
 * <p>Description:  </p>
 *
 * @author dong4j
 * @version 1.3.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.03.23 16:34
 * @since 1.0.0
 */
@Slf4j
public class ZekaSpringBootContextLoader extends SpringBootContextLoader {
    /** Environment */
    private ConfigurableEnvironment environment;

    /**
     * Gets environment *
     *
     * @return the environment
     * @since 1.5.0
     */
    @Override
    protected ConfigurableEnvironment getEnvironment() {
        if (this.environment == null) {
            this.environment = new DefaultEnvironment();
        }
        return this.environment;
    }

    /**
     * Get inlined properties
     *
     * @param config config
     * @return the string [ ]
     * @since 1.5.0
     */
    @Override
    protected String[] getInlinedProperties(@NotNull MergedContextConfiguration config) {
        ArrayList<String> properties = new ArrayList<>();
        // JMX bean names will clash if the same bean is used in multiple contexts
        properties.add("spring.jmx.enabled=false");
        properties.addAll(Arrays.asList(config.getPropertySourceProperties()));
        if (this.notEmbeddedWebEnvironment(config) && !this.hasCustomServerPort(properties)) {
            properties.add("server.port=-1");
        }
        return StringUtils.toStringArray(properties);
    }

    /**
     * Is embedded web environment
     *
     * @param config config
     * @return the boolean
     * @since 1.5.0
     */
    private boolean notEmbeddedWebEnvironment(@NotNull MergedContextConfiguration config) {
        return !MergedAnnotations.from(config.getTestClass(), MergedAnnotations.SearchStrategy.TYPE_HIERARCHY)
            .get(ZekaTest.class)
            .getValue("webEnvironment", SpringBootTest.WebEnvironment.class)
            .orElse(SpringBootTest.WebEnvironment.NONE)
            .isEmbedded();
    }

    /**
     * Has custom server port
     *
     * @param properties properties
     * @return the boolean
     * @since 1.5.0
     */
    private boolean hasCustomServerPort(List<String> properties) {
        Binder binder = new Binder(this.convertToConfigurationPropertySource(properties));
        boolean bound = binder.bind(ConfigKey.SpringConfigKey.SERVER_PORT, Bindable.of(String.class)).isBound();
        return bound && StringUtils.hasText(this.environment.getProperty(ConfigKey.SpringConfigKey.SERVER_PORT));
    }

    /**
     * Convert to configuration property source
     *
     * @param properties properties
     * @return the configuration property source
     * @since 1.5.0
     */
    @Contract("_ -> new")
    private @NotNull
    ConfigurationPropertySource convertToConfigurationPropertySource(List<String> properties) {
        return new MapConfigurationPropertySource(
            convertInlinedPropertiesToMap(StringUtils.toStringArray(properties)));
    }

    /**
     * Load context application context
     * 优先级：
     * TestPropertySource.properties (独立的配置) > TestPropertySource.value/locations（指定的配置文件） > application.yml（默认的配置文件）
     *
     * @param config config
     * @return the application context
     * @since 1.0.0
     */
    @Override
    public ApplicationContext loadContext(@NotNull MergedContextConfiguration config) {
        // 各个模块通过 SPI 加载的默认配置
        ConfigurableEnvironment configurableEnvironment = this.getEnvironment();
        Properties defaultProperties = setUpTestClass(configurableEnvironment);
        this.environment = ZekaTestPropertySourceUtils.addInlinedPropertiesToEnvironment(configurableEnvironment, defaultProperties);

        Class<?>[] configClasses = config.getClasses();
        String[] configLocations = config.getLocations();

        Assert.state(!ObjectUtils.isEmpty(configClasses) || !ObjectUtils.isEmpty(configLocations),
            () -> "No configuration classes or locations found in @SpringApplicationConfiguration. "
                + "For default configuration detection to work you need Spring 4.0.3 or better (found "
                + SpringVersion.getVersion() + ").");

        SpringApplication application = this.getSpringApplication();
        application.setMainApplicationClass(config.getTestClass());
        application.addPrimarySources(Arrays.asList(configClasses));
        application.getSources().addAll(Arrays.asList(configLocations));

        if (!ObjectUtils.isEmpty(config.getActiveProfiles())) {
            this.setActiveProfilesOverride(this.environment, config.getActiveProfiles());
        }
        ResourceLoader resourceLoader = application.getResourceLoader() != null ? application.getResourceLoader() :
            new DefaultResourceLoader(this.getClass().getClassLoader());

        // 优先解析 TestPropertySource.value/locations
        addPropertiesFilesToEnvironment(this.environment, resourceLoader, config.getPropertySourceLocations());
        // 再解析 TestPropertySource.properties
        addInlinedPropertiesToEnvironment(this.environment, this.getInlinedProperties(config));

        application.setEnvironment(this.environment);
        List<ApplicationContextInitializer<?>> initializers = this.getInitializers(config, application);

        if (config instanceof WebMergedContextConfiguration) {
            application.setWebApplicationType(WebApplicationType.SERVLET);
            if (this.notEmbeddedWebEnvironment(config)) {
                new WebConfigurer().configure(config, application, initializers);
            }
        } else if (config instanceof ReactiveWebMergedContextConfiguration) {
            application.setWebApplicationType(WebApplicationType.REACTIVE);
            if (this.notEmbeddedWebEnvironment(config)) {
                new ReactiveWebConfigurer().configure(application);
            }
        } else {
            application.setWebApplicationType(WebApplicationType.NONE);
        }
        application.setInitializers(initializers);
        return application.run(SpringBootTestArgs.get(config.getContextCustomizers()));
    }

    /**
     * <p>Description: </p>
     *
     * @author dong4j
     * @version 1.0.0
     * @email "mailto:dong4j@gmail.com"
     * @date 2020.07.18 23:56
     * @since 1.5.0
     */
    private static class WebConfigurer {

        /** WEB_CONTEXT_CLASS */
        private static final Class<GenericWebApplicationContext> WEB_CONTEXT_CLASS = GenericWebApplicationContext.class;

        /**
         * Configure
         *
         * @param configuration configuration
         * @param application   application
         * @param initializers  initializers
         * @since 1.5.0
         */
        void configure(MergedContextConfiguration configuration, @NotNull SpringApplication application,
                       List<ApplicationContextInitializer<?>> initializers) {
            WebMergedContextConfiguration webConfiguration = (WebMergedContextConfiguration) configuration;
            this.addMockServletContext(initializers, webConfiguration);
            application.setApplicationContextClass(WEB_CONTEXT_CLASS);
        }

        /**
         * Add mock servlet context
         *
         * @param initializers     initializers
         * @param webConfiguration web configuration
         * @since 1.5.0
         */
        private void addMockServletContext(@NotNull List<ApplicationContextInitializer<?>> initializers,
                                           @NotNull WebMergedContextConfiguration webConfiguration) {
            SpringBootMockServletContext servletContext = new SpringBootMockServletContext(webConfiguration.getResourceBasePath());
            initializers.add(0, new ServletContextApplicationContextInitializer(servletContext, true));
        }

    }

    /**
     * Inner class to configure {@link ReactiveWebMergedContextConfiguration}.
     *
     * @author dong4j
     * @version 1.0.0
     * @email "mailto:dong4j@gmail.com"
     * @date 2020.07.18 23:56
     * @since 1.5.0
     */
    private static class ReactiveWebConfigurer {

        /** WEB_CONTEXT_CLASS */
        private static final Class<GenericReactiveWebApplicationContext> WEB_CONTEXT_CLASS = GenericReactiveWebApplicationContext.class;

        /**
         * Configure
         *
         * @param application application
         * @since 1.5.0
         */
        void configure(@NotNull SpringApplication application) {
            application.setApplicationContextClass(WEB_CONTEXT_CLASS);
        }

    }

    /**
     * Sets active profiles *
     *
     * @param environment environment
     * @param profiles    profiles
     * @since 1.5.0
     */
    private void setActiveProfilesOverride(ConfigurableEnvironment environment, String[] profiles) {
        TestPropertyValues.of("spring.profiles.active=" + StringUtils.arrayToCommaDelimitedString(profiles)).applyTo(environment);
    }

    /**
     * 由于 {@link TestPropertySourceUtils#addPropertiesFilesToEnvironment} 不支持 {@link ZekaTest#value()} 的 yml 解析,
     * 这里使用自定义解析逻辑， 且加载的配置优先级低于 系统配置
     * 不再支持 properties 配置文件， 会抛出 {@link IllegalStateException}
     *
     * @param environment    environment
     * @param resourceLoader resource loader
     * @param locations      locations
     * @since 1.5.0
     */
    private static void addPropertiesFilesToEnvironment(ConfigurableEnvironment environment,
                                                        ResourceLoader resourceLoader,
                                                        String... locations) {

        Assert.notNull(environment, "'environment' must not be null");
        Assert.notNull(resourceLoader, "'resourceLoader' must not be null");
        Assert.notNull(locations, "'locations' must not be null");
        for (String location : locations) {
            if (!location.endsWith(ConfigKit.YAML_FILE_EXTENSION)) {
                throw new IllegalStateException("不支持非 yml 配置文件， 请使用正确的配置文件格式");
            }
            String resolvedLocation = environment.resolveRequiredPlaceholders(location);
            Resource resource = resourceLoader.getResource(resolvedLocation);
            environment.getPropertySources().addAfter(ConfigKit.SYSTEM_ENVIRONMENT_NAME,
                ConfigKit.getPropertySource(resource));
        }
    }

    /**
     * 加载 {@link TestPropertySource#properties()} 配置
     * 优先级： JVM 配置 > 系统配置 > {@link TestPropertySource#properties()}
     *
     * @param environment       environment
     * @param inlinedProperties inlined properties
     * @since 2.1.0
     */
    private static void addInlinedPropertiesToEnvironment(ConfigurableEnvironment environment, String... inlinedProperties) {
        Assert.notNull(environment, "'environment' must not be null");
        Assert.notNull(inlinedProperties, "'inlinedProperties' must not be null");
        if (!ObjectUtils.isEmpty(inlinedProperties)) {
            if (log.isDebugEnabled()) {
                log.debug("Adding inlined properties to environment: " + ObjectUtils.nullSafeToString(inlinedProperties));
            }
            MapPropertySource ps = (MapPropertySource)
                environment.getPropertySources().get(INLINED_PROPERTIES_PROPERTY_SOURCE_NAME);
            if (ps == null) {
                ps = new MapPropertySource(INLINED_PROPERTIES_PROPERTY_SOURCE_NAME, new LinkedHashMap<>());
                environment.getPropertySources().addAfter(ConfigKit.SYSTEM_ENVIRONMENT_NAME, ps);
            }
            ps.getSource().putAll(TestPropertySourceUtils.convertInlinedPropertiesToMap(inlinedProperties));
        }
    }

    /**
     * 通过 SPI 加载所有模块的默认配置（优先级最低）
     *
     * @param environment environment
     * @return the up test class
     * @since 1.0.0
     */
    private static @NotNull Properties setUpTestClass(@NotNull ConfigurableEnvironment environment) {
        StartUtils.setFrameworkVersion();
        System.setProperty(App.START_TYPE, App.START_JUNIT);

        // 读取环境变量,使用 spring boot 的规则 (获取系统参数和 JVM 参数)
        MutablePropertySources propertySources = environment.getPropertySources();
        propertySources.addFirst(new SimpleCommandLinePropertySource());

        String appName = System.getProperty(ConfigKey.SpringConfigKey.APPLICATION_NAME);
        // 启动标识
        System.setProperty(App.START_APPLICATION, App.START_APPLICATION);
        System.setProperty(ConfigKey.SERVICE_VERSION, StringPool.NULL_STRING);

        Properties defaultProperties = new Properties();
        defaultProperties.put(ConfigKey.POM_INFO_VERSION, StringPool.NULL_STRING);
        defaultProperties.put(ConfigKey.POM_INFO_GROUPID, App.BASE_PACKAGES);
        defaultProperties.put(ConfigKey.POM_INFO_ARTIFACTID, appName);
        defaultProperties.put(ConfigKey.SpringConfigKey.MAIN_ALLOW_BEAN_DEFINITION_OVERRIDING, ConfigDefaultValue.TRUE);

        // 加载自定义组件
        List<LauncherInitiation> launcherList = new ArrayList<>();
        ServiceLoader.load(LauncherInitiation.class).forEach(launcherList::add);
        launcherList.stream().sorted(Comparator.comparing(LauncherInitiation::getOrder))
            .collect(Collectors.toList())
            .forEach(launcherService -> launcherService.launcherWrapper(environment,
                defaultProperties,
                appName,
                true));

        propertySources.addLast(new MapPropertySource(DefaultEnvironment.DEFAULT_PROPERTIES_PROPERTY_SOURCE_NAME,
            Tools.getMapFromProperties(defaultProperties)));

        return defaultProperties;
    }

}
