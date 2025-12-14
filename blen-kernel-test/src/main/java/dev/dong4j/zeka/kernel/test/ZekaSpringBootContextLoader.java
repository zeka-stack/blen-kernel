package dev.dong4j.zeka.kernel.test;

import org.jetbrains.annotations.NotNull;
import org.springframework.aot.hint.ExecutableMode;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.ApplicationContextFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationHook;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.UseMainMethod;
import org.springframework.boot.test.mock.web.SpringBootMockServletContext;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.boot.test.util.TestPropertyValues.Type;
import org.springframework.boot.web.reactive.context.GenericReactiveWebApplicationContext;
import org.springframework.boot.web.servlet.support.ServletContextApplicationContextInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.aot.AotApplicationContextInitializer;
import org.springframework.core.KotlinDetector;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.SpringVersion;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.core.annotation.MergedAnnotations.SearchStrategy;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.SimpleCommandLinePropertySource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.ContextConfigurationAttributes;
import org.springframework.test.context.ContextCustomizer;
import org.springframework.test.context.ContextLoadException;
import org.springframework.test.context.MergedContextConfiguration;
import org.springframework.test.context.aot.AotContextLoader;
import org.springframework.test.context.support.AbstractContextLoader;
import org.springframework.test.context.support.AnnotationConfigContextLoaderUtils;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.springframework.test.context.web.WebMergedContextConfiguration;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.support.GenericWebApplicationContext;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;
import java.util.ServiceLoader;
import java.util.function.Consumer;

import dev.dong4j.zeka.kernel.common.constant.App;
import dev.dong4j.zeka.kernel.common.constant.ConfigDefaultValue;
import dev.dong4j.zeka.kernel.common.constant.ConfigKey;
import dev.dong4j.zeka.kernel.common.env.DefaultEnvironment;
import dev.dong4j.zeka.kernel.common.start.LauncherInitiation;
import dev.dong4j.zeka.kernel.common.util.StartUtils;
import dev.dong4j.zeka.kernel.common.util.StringPool;
import dev.dong4j.zeka.kernel.common.util.Tools;
import lombok.extern.slf4j.Slf4j;

/**
 * ZekaSpringBootContextLoader 类
 * <p>
 * 该类继承自 AbstractContextLoader, 并实现 AotContextLoader 接口, 用于在 Spring Boot 测试环境中加载应用上下文.
 * 它提供了多种上下文加载模式, 包括标准模式,AOT 处理模式和 AOT 运行时模式, 适用于不同测试阶段的上下文初始化需求.
 * 主要功能包括配置 Spring 应用, 处理测试属性, 设置 Web 环境类型, 加载默认配置类等.
 *
 * @author zeka.stack.team
 * @version 1.0.0
 * @email "mailto:zeka.stack@gmail.com"
 * @date 2025.12.14
 * @since 2.0.0
 */
@Slf4j
public final class ZekaSpringBootContextLoader extends AbstractContextLoader implements AotContextLoader {

    /**
     * 用于标记已配置的 SpringApplication 实例
     * <p>
     * 该 Consumer 用于处理已配置的 SpringApplication 对象, 通常在初始化过程中使用
     */
    private static final Consumer<SpringApplication> ALREADY_CONFIGURED = (springApplication) -> {
    };

    /** 应用程序的可配置环境对象 */
    private ConfigurableEnvironment environment;

    /**
     * 加载指定配置的应用程序上下文
     * <p>
     * 使用标准模式加载由给定合并配置定义的应用程序上下文, 其他参数使用默认值或 null
     *
     * @param mergedConfig 合并后的上下文配置对象
     * @return 加载的应用程序上下文
     * @throws Exception 如果在加载过程中发生错误
     */
    @Override
    public @NotNull ApplicationContext loadContext(@NotNull MergedContextConfiguration mergedConfig) throws Exception {
        return this.loadContext(mergedConfig, Mode.STANDARD, null, null);
    }

    /**
     * 为 AOT 处理加载应用上下文
     * <p>
     * 使用指定的合并上下文配置和运行时提示信息, 加载用于 AOT(Ahead-Of-Time) 处理的 ApplicationContext 实例.
     *
     * @param mergedConfig 合并后的上下文配置信息
     * @param runtimeHints 运行时提示信息, 用于指导 AOT 处理过程
     * @return 加载的 ApplicationContext 实例
     * @throws Exception 如果在加载上下文过程中发生错误
     */
    @Override
    public @NotNull ApplicationContext loadContextForAotProcessing(@NotNull MergedContextConfiguration mergedConfig,
                                                                   @NotNull RuntimeHints runtimeHints) throws Exception {
        return this.loadContext(mergedConfig, Mode.AOT_PROCESSING, null, runtimeHints);
    }

    /**
     * 为 AOT 运行时加载应用上下文
     * <p>
     * 使用指定的合并上下文配置和应用上下文初始化器, 加载适用于 AOT(Ahead-Of-Time) 运行时的上下文.
     *
     * @param mergedConfig 合并后的上下文配置对象
     * @param initializer  用于初始化应用上下文的初始化器
     * @return 加载后的应用上下文
     * @throws Exception 在加载上下文过程中发生错误时抛出
     */
    @Override
    public @NotNull ApplicationContext loadContextForAotRuntime(@NotNull MergedContextConfiguration mergedConfig,
                                                                @NotNull ApplicationContextInitializer<ConfigurableApplicationContext> initializer)
        throws Exception {
        return this.loadContext(mergedConfig, Mode.AOT_RUNTIME, initializer, null);
    }

    /**
     * 加载 Spring 应用上下文
     * <p>
     * 根据提供的配置和模式初始化并返回一个应用上下文. 如果检测到主方法, 则优先使用主方法启动应用, 否则使用 SpringApplication 启动.
     *
     * @param mergedConfig 合并后的上下文配置信息
     * @param mode         应用上下文加载模式
     * @param initializer  应用上下文初始化器, 用于在上下文创建时进行初始化操作
     * @param runtimeHints 运行时提示信息, 用于注册反射信息
     * @return 加载完成的 Spring 应用上下文
     * @throws Exception 如果在加载上下文过程中发生错误
     */
    private ApplicationContext loadContext(MergedContextConfiguration mergedConfig,
                                           Mode mode,
                                           ApplicationContextInitializer<ConfigurableApplicationContext> initializer,
                                           RuntimeHints runtimeHints) throws Exception {
        this.assertHasClassesOrLocations(mergedConfig);
        ZekaSpringBootAttributes attributes = this.getAttributes(mergedConfig);
        String[] args = attributes.args();
        UseMainMethod useMainMethod = attributes.useMainMethod();
        Method mainMethod = this.getMainMethod(mergedConfig, useMainMethod);
        if (mainMethod != null) {
            if (runtimeHints != null) {
                runtimeHints.reflection().registerMethod(mainMethod, ExecutableMode.INVOKE);
            }
            ContextLoaderHook hook = new ContextLoaderHook(mode, initializer,
                                                           (application) -> this.configure(mergedConfig, application));
            return hook.runMain(() -> ReflectionUtils.invokeMethod(mainMethod, null, new Object[] {args}));
        }
        SpringApplication application = this.getSpringApplication();
        this.configure(mergedConfig, application);
        ContextLoaderHook hook = new ContextLoaderHook(mode, initializer, ALREADY_CONFIGURED);
        return hook.run(() -> application.run(args));
    }

    /**
     * 验证合并后的上下文配置是否包含配置类或配置位置
     * <p>
     * 检查传入的 MergedContextConfiguration 对象是否至少包含配置类或配置位置中的一种.
     * 如果两者都不存在, 将抛出 IllegalStateException 并提示需要 Spring 4.0.3 或更高版本以支持默认配置检测.
     *
     * @param mergedConfig 合并后的上下文配置对象
     * @throws IllegalStateException 当配置类和配置位置都不存在时抛出
     */
    private void assertHasClassesOrLocations(MergedContextConfiguration mergedConfig) {
        boolean hasClasses = !ObjectUtils.isEmpty(mergedConfig.getClasses());
        boolean hasLocations = !ObjectUtils.isEmpty(mergedConfig.getLocations());
        Assert.state(hasClasses || hasLocations,
            () -> "No configuration classes or locations found in @SpringApplicationConfiguration. "
                + "For default configuration detection to work you need Spring 4.0.3 or better (found "
                + SpringVersion.getVersion() + ").");
    }

    /**
     * 获取主方法
     * <p>
     * 根据提供的上下文配置和主方法使用策略, 查找并返回主方法. 如果使用策略为 NEVER, 则直接返回 null.
     * 如果使用策略不允许在层次化上下文中使用, 则抛出异常. 如果未找到 Spring Boot 配置类且使用策略不是 WHEN_AVAILABLE, 也会抛出异常.
     * 最后查找指定类的主方法, 若未找到且使用策略不是 WHEN_AVAILABLE, 同样抛出异常.
     *
     * @param mergedConfig  合并后的上下文配置对象
     * @param useMainMethod 主方法使用的策略, 决定是否查找主方法
     * @return 主方法对象, 若未找到或策略为 NEVER 则返回 null
     * @throws IllegalStateException 当使用策略与当前上下文配置不兼容或未找到主方法时抛出
     */
    private Method getMainMethod(MergedContextConfiguration mergedConfig, UseMainMethod useMainMethod) {
        if (useMainMethod == UseMainMethod.NEVER) {
            return null;
        }
        Assert.state(mergedConfig.getParent() == null,
                     () -> "UseMainMethod.%s cannot be used with @ContextHierarchy tests".formatted(useMainMethod));
        Class<?> springBootConfiguration = Arrays.stream(mergedConfig.getClasses())
            .filter(this::isSpringBootConfiguration)
            .findFirst()
            .orElse(null);
        Assert.state(springBootConfiguration != null || useMainMethod == UseMainMethod.WHEN_AVAILABLE,
                     "Cannot use main method as no @SpringBootConfiguration-annotated class is available");
        Method mainMethod = findMainMethod(springBootConfiguration);
        Assert.state(mainMethod != null || useMainMethod == UseMainMethod.WHEN_AVAILABLE,
                     () -> "Main method not found on '%s'".formatted(springBootConfiguration.getName()));
        return mainMethod;
    }

    /**
     * 根据给定的类查找 main 方法
     * <p>
     * 如果类不为 null, 则尝试查找该类的 main 方法. 如果未找到且 Kotlin 存在, 则尝试查找对应的 Kotlin 转换类 (类名后加 "Kt") 的 main 方法.
     *
     * @param type 要查找 main 方法的类
     * @return 找到的 main 方法, 若未找到则返回 null
     */
    private static Method findMainMethod(Class<?> type) {
        Method mainMethod = (type != null) ? ReflectionUtils.findMethod(type, "main", String[].class) : null;
        if (mainMethod == null && KotlinDetector.isKotlinPresent()) {
            try {
                Class<?> kotlinClass = ClassUtils.forName(type.getName() + "Kt", type.getClassLoader());
                mainMethod = ReflectionUtils.findMethod(kotlinClass, "main", String[].class);
            } catch (ClassNotFoundException ex) {
                // Ignore
            }
        }
        return mainMethod;
    }

    /**
     * 判断指定的类是否为 Spring Boot 配置类
     * <p>
     * 使用 MergedAnnotations 检查给定类及其继承层次中是否存在 SpringBootConfiguration 注解
     *
     * @param candidate 需要检查的类
     * @return 如果类或其继承层次中存在 SpringBootConfiguration 注解则返回 true, 否则返回 false
     */
    private boolean isSpringBootConfiguration(Class<?> candidate) {
        return MergedAnnotations.from(candidate, SearchStrategy.TYPE_HIERARCHY)
            .isPresent(SpringBootConfiguration.class);
    }

    /**
     * 配置 SpringApplication 实例
     * <p>
     * 根据提供的 MergedContextConfiguration 设置 SpringApplication 的主类, 源,Web 应用类型, 上下文工厂和环境.
     * 如果配置中包含父上下文, 则关闭横幅模式. 如果环境未设置, 则添加 PrepareEnvironmentListener 监听器.
     *
     * @param mergedConfig 合并后的上下文配置对象, 用于获取测试类, 类路径, 位置等信息
     * @param application  要配置的 SpringApplication 实例
     * @throws IllegalArgumentException 如果配置信息无法正确解析或设置
     */
    private void configure(MergedContextConfiguration mergedConfig, SpringApplication application) {
        application.setMainApplicationClass(mergedConfig.getTestClass());
        application.addPrimarySources(Arrays.asList(mergedConfig.getClasses()));
        application.getSources().addAll(Arrays.asList(mergedConfig.getLocations()));
        List<ApplicationContextInitializer<?>> initializers = this.getInitializers(mergedConfig, application);
        if (mergedConfig instanceof WebMergedContextConfiguration) {
            application.setWebApplicationType(WebApplicationType.SERVLET);
            if (!this.isEmbeddedWebEnvironment(mergedConfig)) {
                new WebConfigurer().configure(mergedConfig, initializers);
            }
        } else if (mergedConfig instanceof org.springframework.boot.test.context.ReactiveWebMergedContextConfiguration) {
            application.setWebApplicationType(WebApplicationType.REACTIVE);
        } else {
            application.setWebApplicationType(WebApplicationType.NONE);
        }
        application.setApplicationContextFactory(this.getApplicationContextFactory(mergedConfig));
        if (mergedConfig.getParent() != null) {
            application.setBannerMode(Banner.Mode.OFF);
        }
        application.setInitializers(initializers);
        ConfigurableEnvironment env = this.getEnvironment();
        if (env != null) {
            this.prepareEnvironment(mergedConfig, application, env, false);
            application.setEnvironment(env);
        } else {
            application.addListeners(new PrepareEnvironmentListener(mergedConfig));
        }
    }

    /**
     * 获取当前配置的环境对象
     * <p>
     * 如果尚未初始化环境对象, 则创建一个新的 DefaultEnvironment 实例, 并应用 Zeka 默认配置.
     * 然后返回该环境对象.
     *
     * @return 配置好的 ConfigurableEnvironment 对象
     */
    private ConfigurableEnvironment getEnvironment() {
        if (this.environment == null) {
            ConfigurableEnvironment env = new DefaultEnvironment();
            this.applyZekaDefaults(env);
            this.environment = env;
        }
        return this.environment;
    }

    /**
     * 根据合并的上下文配置和 Web 应用类型获取对应的应用上下文工厂
     * <p>
     * 判断传入的 Web 应用类型是否为非 NONE 类型, 并且当前环境是否不是嵌入式 Web 环境. 如果是, 则根据 Web 应用类型返回相应的默认上下文工厂实例. 否则, 使用默认的 ApplicationContextFactory 创建实例.
     *
     * @param mergedConfig 合并的上下文配置对象
     * @return 对应的 ApplicationContextFactory 实例
     * @throws IllegalArgumentException 如果无法根据 Web 应用类型创建合适的上下文工厂
     */
    private ApplicationContextFactory getApplicationContextFactory(MergedContextConfiguration mergedConfig) {
        return (webApplicationType) -> {
            if (webApplicationType != WebApplicationType.NONE && !this.isEmbeddedWebEnvironment(mergedConfig)) {
                if (webApplicationType == WebApplicationType.REACTIVE) {
                    return new GenericReactiveWebApplicationContext();
                }
                if (webApplicationType == WebApplicationType.SERVLET) {
                    return new GenericWebApplicationContext();
                }
            }
            return ApplicationContextFactory.DEFAULT.create(webApplicationType);
        };
    }

    /**
     * 准备测试环境配置
     * <p>
     * 应用 Zeka 默认配置, 设置激活的 Profile, 添加属性源和内联属性, 并设置非嵌入式服务器端口的回退值.
     *
     * @param mergedConfig           合并后的上下文配置对象
     * @param application            Spring 应用程序对象
     * @param env                    可配置的环境对象
     * @param applicationEnvironment 是否使用应用程序的环境
     */
    private void prepareEnvironment(MergedContextConfiguration mergedConfig,
                                    SpringApplication application,
                                    ConfigurableEnvironment env,
                                    boolean applicationEnvironment) {
        this.applyZekaDefaults(env);
        this.setActiveProfiles(env, mergedConfig.getActiveProfiles(), applicationEnvironment);
        ResourceLoader resourceLoader = (application.getResourceLoader() != null) ? application.getResourceLoader()
                                                                                  : new DefaultResourceLoader(null);
        TestPropertySourceUtils.addPropertySourcesToEnvironment(env, resourceLoader,
                                                                mergedConfig.getPropertySourceDescriptors());
        TestPropertySourceUtils.addInlinedPropertiesToEnvironment(env, this.getInlinedProperties(mergedConfig));
        this.applyNonEmbeddedServerPortFallback(mergedConfig, env);
    }

    /**
     * 设置环境中的活动配置文件
     * <p>
     * 如果提供了配置文件数组, 则将其设置为环境的活动配置文件. 如果 applicationEnvironment 参数为 false,
     * 则直接设置配置文件, 否则将配置文件以键值对形式添加到环境属性中.
     *
     * @param environment            要设置配置文件的环境对象
     * @param profiles               配置文件名称数组, 若为空则不进行任何操作
     * @param applicationEnvironment 是否将配置文件作为应用环境配置处理, 若为 false 则直接设置为活动配置文件
     * @throws IllegalArgumentException 如果 profiles 参数为 null 或者 environment 参数为 null
     */
    private void setActiveProfiles(ConfigurableEnvironment environment, String[] profiles,
                                   boolean applicationEnvironment) {
        if (ObjectUtils.isEmpty(profiles)) {
            return;
        }
        if (!applicationEnvironment) {
            environment.setActiveProfiles(profiles);
        }
        String[] pairs = new String[profiles.length];
        for (int i = 0; i < profiles.length; i++) {
            pairs[i] = "spring.profiles.active[" + i + "]=" + profiles[i];
        }
        TestPropertyValues.of(pairs).applyTo(environment, Type.MAP, "active-test-profiles");
    }

    /**
     * 应用非嵌入式服务器端口的回退配置
     * <p>
     * 检查是否为嵌入式 Web 环境, 如果是则直接返回. 否则检查环境属性中是否包含服务器端口配置, 若不存在则设置默认端口为 -1.
     *
     * @param mergedConfig 合并后的上下文配置对象
     * @param env          可配置的环境对象, 用于读取和设置属性
     */
    private void applyNonEmbeddedServerPortFallback(MergedContextConfiguration mergedConfig,
                                                    ConfigurableEnvironment env) {
        if (this.isEmbeddedWebEnvironment(mergedConfig)) {
            return;
        }
        boolean hasServerPort = false;
        for (org.springframework.core.env.PropertySource<?> propertySource : env.getPropertySources()) {
            if (propertySource.containsProperty(ConfigKey.SpringConfigKey.SERVER_PORT)) {
                hasServerPort = true;
                break;
            }
        }
        if (!hasServerPort) {
            TestPropertyValues.of(ConfigKey.SpringConfigKey.SERVER_PORT + "=-1")
                .applyTo(env, Type.MAP, "zeka-non-embedded-server-port");
        }
    }

    /**
     * 创建一个新的 SpringApplication 实例
     * <p>
     * 该方法用于生成一个新的 SpringApplication 对象, 通常用于启动 Spring 应用程序.
     *
     * @return 新创建的 SpringApplication 实例
     */
    private SpringApplication getSpringApplication() {
        return new SpringApplication();
    }

    /**
     * 获取内联的属性数组
     * <p>
     * 创建一个包含默认属性 "spring.jmx.enabled=false" 和从合并配置中获取的属性的字符串数组
     *
     * @param mergedConfig 合并后的上下文配置对象, 用于获取属性源中的属性
     * @return 包含内联属性的字符串数组
     */
    private String[] getInlinedProperties(MergedContextConfiguration mergedConfig) {
        ArrayList<String> properties = new ArrayList<>();
        properties.add("spring.jmx.enabled=false");
        properties.addAll(Arrays.asList(mergedConfig.getPropertySourceProperties()));
        return StringUtils.toStringArray(properties);
    }

    /**
     * 获取用于初始化应用上下文的初始化器列表
     * <p>
     * 根据提供的合并上下文配置和 Spring 应用, 收集所有适用的上下文初始化器.
     *
     * @param mergedConfig 合并后的上下文配置对象, 包含自定义器和初始化器类
     * @param application  SpringApplication 实例, 用于获取默认的初始化器
     * @return 包含所有上下文初始化器的列表
     */
    private List<ApplicationContextInitializer<?>> getInitializers(MergedContextConfiguration mergedConfig,
                                                                   SpringApplication application) {
        List<ApplicationContextInitializer<?>> initializers = new ArrayList<>();
        for (org.springframework.test.context.ContextCustomizer contextCustomizer : mergedConfig.getContextCustomizers()) {
            initializers.add(new ContextCustomizerAdapter(contextCustomizer, mergedConfig));
        }
        initializers.addAll(application.getInitializers());
        for (Class<? extends ApplicationContextInitializer<?>> initializerClass : mergedConfig
            .getContextInitializerClasses()) {
            initializers.add(BeanUtils.instantiateClass(initializerClass));
        }
        if (mergedConfig.getParent() != null) {
            ApplicationContext parentApplicationContext = mergedConfig.getParentApplicationContext();
            initializers.add(new ParentContextApplicationContextInitializer(parentApplicationContext));
        }
        return initializers;
    }

    /**
     * 判断是否为嵌入式 Web 环境
     * <p>
     * 通过获取合并后的上下文配置属性, 检查其 Web 环境是否为嵌入式.
     *
     * @param mergedConfig 合并后的上下文配置对象
     * @return 如果 Web 环境是嵌入式的, 返回 true; 否则返回 false
     */
    private boolean isEmbeddedWebEnvironment(MergedContextConfiguration mergedConfig) {
        return this.getAttributes(mergedConfig).webEnvironment().isEmbedded();
    }

    /**
     * 根据合并的上下文配置获取 ZekaSpringBoot 属性
     * <p>
     * 使用提供的合并上下文配置实例, 从 ZekaSpringBootAttributes 类中创建并返回对应的属性对象
     *
     * @param mergedConfig 合并的上下文配置实例
     * @return 对应的 ZekaSpringBootAttributes 实例
     */
    private ZekaSpringBootAttributes getAttributes(MergedContextConfiguration mergedConfig) {
        return ZekaSpringBootAttributes.from(mergedConfig);
    }

    /**
     * 处理上下文配置属性
     * <p>
     * 调用父类的处理方法, 并检查配置属性是否包含资源. 如果未包含资源, 则检测默认的配置类并设置到配置属性中.
     *
     * @param configAttributes 上下文配置属性对象, 包含配置信息
     */
    @Override
    public void processContextConfiguration(@NotNull ContextConfigurationAttributes configAttributes) {
        super.processContextConfiguration(configAttributes);
        if (!configAttributes.hasResources()) {
            Class<?>[] defaultConfigClasses = this.detectDefaultConfigurationClasses(configAttributes.getDeclaringClass());
            configAttributes.setClasses(defaultConfigClasses);
        }
    }

    /**
     * 检测默认的配置类
     * <p>
     * 用于识别指定类所在的默认配置类, 通常用于基于注解的配置加载.
     *
     * @param declaringClass 需要检测配置类的类
     * @return 默认配置类的数组
     */
    private Class<?>[] detectDefaultConfigurationClasses(Class<?> declaringClass) {
        return AnnotationConfigContextLoaderUtils.detectDefaultConfigurationClasses(declaringClass);
    }

    /**
     * 返回资源后缀数组
     * <p>
     * 该方法用于指定当前资源处理器支持的文件后缀名, 通常用于识别特定配置文件格式.
     *
     * @return 包含资源后缀的字符串数组, 例如 "-context.xml" 和 "Context.groovy"
     */
    @Override
    protected String @NotNull [] getResourceSuffixes() {
        return new String[] {"-context.xml", "Context.groovy"};
    }

    /**
     * 获取资源后缀
     * <p>
     * 该方法用于获取资源后缀, 但当前实现中未提供具体逻辑, 仅抛出异常表示方法未完成.
     *
     * @throws IllegalStateException 当方法未被正确实现时抛出
     */
    @Override
    protected @NotNull String getResourceSuffix() {
        throw new IllegalStateException();
    }

    /**
     * 模式枚举类
     * <p>
     * 用于定义不同的运行模式, 包括标准模式,AOT 处理模式和 AOT 运行时模式, 适用于区分不同阶段的处理逻辑或运行环境
     *
     * @author zeka.stack.team
     * @version 1.0.0
     * @email "mailto:zeka.stack@gmail.com"
     * @date 2025.12.14
     * @since 2.0.0
     */
    private enum Mode {
        /** 标准 */
        STANDARD,
        /** 表示是否启用 AOT(Ahead-Of-Time) 编译处理 */
        AOT_PROCESSING,
        /** AOT 运行时环境标识 */
        AOT_RUNTIME
    }

    /**
     * ZekaSpringBootAttributes 记录类
     * <p>
     * 用于封装与 Spring Boot 测试相关的属性配置, 包括命令行参数, 主方法使用策略以及 Web 环境设置.
     * 该类通过从测试类的注解中提取配置信息, 提供统一的访问方式.
     *
     * @author zeka.stack.team
     * @version 1.0.0
     * @email "mailto:zeka.stack@gmail.com"
     * @date 2025.12.14
     * @since 2.0.0
     */
    private record ZekaSpringBootAttributes(String[] args,
                                            UseMainMethod useMainMethod,
                                            SpringBootTest.WebEnvironment webEnvironment) {

        /**
         * 从合并的上下文配置中创建 ZekaSpringBootAttributes 实例
         * <p>
         * 使用给定的合并上下文配置, 提取相关的注解属性并构建 ZekaSpringBootAttributes 对象.
         *
         * @param mergedConfig 合并的上下文配置对象, 用于获取测试类的注解信息
         * @return ZekaSpringBootAttributes 实例, 包含从注解中提取的参数,useMainMethod 和 webEnvironment 属性
         */
        static ZekaSpringBootAttributes from(MergedContextConfiguration mergedConfig) {
            MergedAnnotations annotations = MergedAnnotations.from(mergedConfig.getTestClass(), SearchStrategy.TYPE_HIERARCHY);
            String[] args = annotations.get(ZekaTest.class)
                .getValue("args", String[].class)
                .orElseGet(() -> annotations.get(SpringBootTest.class)
                    .getValue("args", String[].class)
                    .orElse(new String[0]));
            UseMainMethod useMainMethod = annotations.get(SpringBootTest.class)
                .getValue("useMainMethod", UseMainMethod.class)
                .orElse(UseMainMethod.WHEN_AVAILABLE);
            SpringBootTest.WebEnvironment webEnvironment = annotations.get(ZekaTest.class)
                .getValue("webEnvironment", SpringBootTest.WebEnvironment.class)
                .orElse(annotations.get(SpringBootTest.class)
                            .getValue("webEnvironment", SpringBootTest.WebEnvironment.class)
                            .orElse(SpringBootTest.WebEnvironment.MOCK));
            return new ZekaSpringBootAttributes(args, useMainMethod, webEnvironment);
        }
    }

    /**
     * Web 配置器类
     * <p>
     * 用于配置 Web 应用的上下文环境, 主要功能是为测试环境添加模拟的 Servlet 上下文初始化器, 确保 Web 应用上下文能够正确初始化.
     * 该类包含内部静态类, 用于封装防御性初始化逻辑, 防止在非 Web 上下文中执行不兼容的操作.
     *
     * @author zeka.stack.team
     * @version 1.0.0
     * @email "mailto:zeka.stack@gmail.com"
     * @date 2025.12.14
     * @since 2.0.0
     */
    private static final class WebConfigurer {

        /**
         * 配置测试上下文
         * <p>
         * 将传入的 MergedContextConfiguration 转换为 WebMergedContextConfiguration, 并添加模拟的 ServletContext 到初始化器列表中.
         *
         * @param mergedConfig 合并后的上下文配置
         * @param initializers 应用上下文初始化器列表
         * @throws ClassCastException 如果 mergedConfig 不是 WebMergedContextConfiguration 类型时抛出
         */
        void configure(MergedContextConfiguration mergedConfig, List<ApplicationContextInitializer<?>> initializers) {
            WebMergedContextConfiguration webMergedConfig = (WebMergedContextConfiguration) mergedConfig;
            this.addMockServletContext(initializers, webMergedConfig);
        }

        /**
         * 向初始化器列表中添加模拟的 ServletContext
         * <p>
         * 创建一个 SpringBootMockServletContext 实例, 并将其包装在 DefensiveWebApplicationContextInitializer 中,
         * 然后将该初始化器插入到初始化器列表的最前面.
         *
         * @param initializers    要添加初始化器的列表
         * @param webMergedConfig WebMergedContextConfiguration 实例, 用于获取资源基础路径
         * @throws IllegalArgumentException 如果 initializers 为 null 或 webMergedConfig 为 null
         */
        private void addMockServletContext(List<ApplicationContextInitializer<?>> initializers,
                                           WebMergedContextConfiguration webMergedConfig) {
            SpringBootMockServletContext servletContext = new SpringBootMockServletContext(
                webMergedConfig.getResourceBasePath());
            initializers.add(0, new DefensiveWebApplicationContextInitializer(
                new ServletContextApplicationContextInitializer(servletContext, true)));
        }

        /**
         * 防御性 Web 应用上下文初始化器
         * <p>
         * 该类用于在初始化应用上下文时提供额外的保护逻辑, 确保只有符合要求的 Web 应用上下文才会被处理. 它封装了 {@link ServletContextApplicationContextInitializer} 的功能, 并在初始化过程中进行类型检查.
         *
         * @param delegate 用于委托的 ServletContextApplicationContextInitializer 实例
         *                 <p>
         *                 该字段用于将实际的初始化逻辑委托给另一个实现
         * @author zeka.stack.team
         * @version 1.0.0
         * @email "mailto:zeka.stack@gmail.com"
         * @date 2025.12.14
         * @since 2.0.0
         */
                private record DefensiveWebApplicationContextInitializer(ServletContextApplicationContextInitializer delegate)
                    implements ApplicationContextInitializer<ConfigurableApplicationContext> {

            /**
             * 构造函数, 用于初始化 DefensiveWebApplicationContextInitializer 实例
             * <p>
             * 该构造函数接收一个 ServletContextApplicationContextInitializer 类型的委托对象, 并将其赋值给内部的 delegate 字段, 用于后续的初始化操作.
             *
             * @param delegate 委托的 ServletContextApplicationContextInitializer 实例
             */
            private DefensiveWebApplicationContextInitializer {
            }

                    /**
                     * 初始化应用上下文
                     * <p>
                     * 如果提供的应用上下文是 ConfigurableWebApplicationContext 类型, 则调用 delegate 的 initialize 方法进行初始化.
                     *
                     * @param applicationContext 需要初始化的配置化应用上下文
                     * @throws IllegalArgumentException 如果 applicationContext 不是 ConfigurableWebApplicationContext 类型, 可能无法正确初始化
                     */
                    @Override
                    public void initialize(@NotNull ConfigurableApplicationContext applicationContext) {
                        if (applicationContext instanceof ConfigurableWebApplicationContext webApplicationContext) {
                            this.delegate.initialize(webApplicationContext);
                        }
                    }
                }
    }

    /**
     * 上下文自定义适配器类
     * <p>
     * 用于将 Spring 测试框架中的 ContextCustomizer 接口适配为 ApplicationContextInitializer 接口, 以便在测试环境中初始化应用上下文.
     * 该类主要负责桥接不同接口之间的方法调用, 确保测试配置能够正确应用到上下文中.
     *
     * @param contextCustomizer 用于自定义 Spring 测试上下文的定制器
     * @param mergedConfig      合并后的上下文配置信息
     *                          <p>
     *                          用于在测试过程中表示当前测试类的上下文配置, 包含所有相关的配置类和配置参数.
     * @author zeka.stack.team
     * @version 1.0.0
     * @email "mailto:zeka.stack@gmail.com"
     * @date 2025.12.14
     * @since 2.0.0
     */
        private record ContextCustomizerAdapter(ContextCustomizer contextCustomizer, MergedContextConfiguration mergedConfig)
            implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        /**
         * 构造函数, 用于初始化 ContextCustomizerAdapter 实例
         * <p>
         * 该构造函数接收一个 ContextCustomizer 和一个 MergedContextConfiguration 对象, 并将其分别赋值给实例变量
         *
         * @param contextCustomizer 用于自定义测试上下文的 ContextCustomizer 对象
         * @param mergedConfig      合并后的上下文配置对象
         */
        private ContextCustomizerAdapter {
        }

            /**
             * 初始化应用上下文
             * <p>
             * 使用配置信息对给定的应用上下文进行自定义初始化
             *
             * @param applicationContext 需要初始化的配置化应用上下文
             */
            @Override
            public void initialize(@NotNull ConfigurableApplicationContext applicationContext) {
                this.contextCustomizer.customizeContext(applicationContext, this.mergedConfig);
            }
        }

    /**
     * 父上下文应用上下文初始化器
     * <p>
     * 用于在应用上下文初始化时设置其父上下文, 确保子上下文能够继承父上下文的配置和 Bean 定义.
     * 该初始化器优先级最高, 保证在其他初始化器之前执行.
     *
     * @author zeka.stack.team
     * @version 1.0.0
     * @email "mailto:zeka.stack@gmail.com"
     * @date 2025.12.14
     * @since 2.0.0
     */
    @org.springframework.core.annotation.Order(Ordered.HIGHEST_PRECEDENCE)
    private static class ParentContextApplicationContextInitializer
        implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        /**
         * 父级应用上下文
         * <p>
         * 用于表示当前上下文的父上下文, 通常用于继承配置或资源
         */
        private final ApplicationContext parent;

        /**
         * 初始化父上下文应用上下文
         * <p>
         * 为当前上下文设置父级应用上下文
         *
         * @param parent 父级应用上下文
         */
        ParentContextApplicationContextInitializer(ApplicationContext parent) {
            this.parent = parent;
        }

        /**
         * 初始化应用上下文
         * <p>
         * 将当前应用上下文的父上下文设置为指定的父上下文
         *
         * @param applicationContext 需要初始化的配置型应用上下文
         * @throws IllegalStateException 如果父上下文已设置或无法设置
         */
        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            applicationContext.setParent(this.parent);
        }
    }

    /**
     * 环境准备监听器
     * <p>
     * 用于在 Spring 应用程序环境准备阶段执行自定义的环境配置逻辑, 确保测试环境或运行环境的正确初始化.
     * 该监听器具有最高优先级, 以便在其他监听器之前执行必要的环境设置.
     *
     * @author zeka.stack.team
     * @version 1.0.0
     * @email "mailto:zeka.stack@gmail.com"
     * @date 2025.12.14
     * @since 2.0.0
     */
    private class PrepareEnvironmentListener
        implements ApplicationListener<ApplicationEnvironmentPreparedEvent>, PriorityOrdered {

        /**
         * 合并后的上下文配置信息
         * <p>
         * 用于在测试过程中统一管理多个配置源的合并结果
         *
         * @see MergedContextConfiguration
         */
        private final MergedContextConfiguration mergedConfig;

        /**
         * 构造函数, 用于初始化 PrepareEnvironmentListener 实例
         * <p>
         * 该构造函数接收一个合并后的上下文配置对象, 并将其赋值给实例变量 mergedConfig.
         *
         * @param mergedConfig 合并后的上下文配置对象
         */
        PrepareEnvironmentListener(MergedContextConfiguration mergedConfig) {
            this.mergedConfig = mergedConfig;
        }

        /**
         * 获取当前对象的优先级顺序
         * <p>
         * 返回一个整数值, 表示该对象在排序中的优先级. 值越小, 优先级越高.
         *
         * @return 优先级顺序, 值越小优先级越高
         */
        @Override
        public int getOrder() {
            return Ordered.HIGHEST_PRECEDENCE;
        }

        /**
         * 处理应用环境准备事件
         * <p>
         * 当应用环境准备事件发生时, 调用 prepareEnvironment 方法进行环境配置
         *
         * @param event 应用环境准备事件对象
         */
        @Override
        public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
            prepareEnvironment(this.mergedConfig, event.getSpringApplication(), event.getEnvironment(), true);
        }
    }

    /**
     * 上下文加载钩子类
     * <p>
     * 用于在 Spring Boot 应用启动过程中加载和管理应用上下文, 实现自定义的初始化和配置逻辑. 该类作为 SpringApplicationHook 的实现, 能够在应用启动的不同阶段执行特定操作, 如上下文加载, 失败处理等.
     *
     * @author zeka.stack.team
     * @version 1.0.0
     * @email "mailto:zeka.stack@gmail.com"
     * @date 2025.12.14
     * @since 2.0.0
     */
    private static class ContextLoaderHook implements SpringApplicationHook {

        /** 表示当前模式 */
        private final Mode mode;

        /**
         * 应用上下文初始化器
         * <p>
         * 用于在应用启动时初始化配置可变的上下文
         *
         * @see ApplicationContextInitializer
         * @see ConfigurableApplicationContext
         */
        private final ApplicationContextInitializer<ConfigurableApplicationContext> initializer;

        /**
         * 用于配置 SpringApplication 的消费者函数
         * <p>
         * 该函数可以在应用启动时进行自定义配置
         *
         * @see Consumer
         * @see SpringApplication
         */
        private final Consumer<SpringApplication> configurer;

        /**
         * 存储应用上下文的同步列表
         * <p>
         * 用于安全地管理多个 ApplicationContext 实例, 确保多线程环境下操作安全.
         *
         * @see ApplicationContext
         */
        private final List<ApplicationContext> contexts = Collections.synchronizedList(new ArrayList<>());

        /**
         * 存储失败的 ApplicationContext 实例的列表
         * <p>
         * 该列表是线程安全的, 用于记录在初始化或处理过程中出现异常的上下文对象
         *
         * @see ApplicationContext
         * @see Collections#synchronizedList(List)
         */
        private final List<ApplicationContext> failedContexts = Collections.synchronizedList(new ArrayList<>());

        /**
         * 构造一个 ContextLoaderHook 实例
         * <p>
         * 用于初始化 Spring 应用上下文并配置 SpringApplication
         *
         * @param mode        指定加载上下文的模式
         * @param initializer 应用上下文初始化器, 用于初始化 ConfigurableApplicationContext
         * @param configurer  用于配置 SpringApplication 的消费者函数
         */
        ContextLoaderHook(Mode mode,
                          ApplicationContextInitializer<ConfigurableApplicationContext> initializer,
                          Consumer<SpringApplication> configurer) {
            this.mode = mode;
            this.initializer = initializer;
            this.configurer = configurer;
        }

        /**
         * 获取一个 Spring Boot 应用程序启动监听器
         * <p>
         * 创建并返回一个自定义的 SpringApplicationRunListener 实例, 用于在应用程序启动过程中执行特定操作.
         * 该监听器会在启动时调用配置器, 并根据运行模式添加特定的初始化器或处理上下文加载失败的情况.
         *
         * @param application Spring Boot 应用程序实例
         * @return 返回自定义的 SpringApplicationRunListener 实例
         * @throws SpringApplication.AbandonedRunException 当运行模式为 AOT_PROCESSING 且上下文加载完成后抛出
         */
        @Override
        public SpringApplicationRunListener getRunListener(SpringApplication application) {
            return new SpringApplicationRunListener() {

                /**
                 * 在应用启动时执行初始化操作
                 * <p>
                 * 该方法在应用启动过程中被调用, 用于加载配置并根据运行模式执行相应的初始化逻辑.
                 *
                 * @param bootstrapContext Spring Boot 应用启动的上下文对象
                 */
                @Override
                public void starting(org.springframework.boot.ConfigurableBootstrapContext bootstrapContext) {
                    ContextLoaderHook.this.configurer.accept(application);
                    if (ContextLoaderHook.this.mode == Mode.AOT_RUNTIME) {
                        application.addInitializers(
                            (AotApplicationContextInitializer<?>) ContextLoaderHook.this.initializer::initialize);
                    }
                }

                /**
                 * 当上下文加载完成时调用此方法
                 * <p>
                 * 将提供的应用上下文添加到当前上下文列表中, 并在模式为 AOT_PROCESSING 时抛出异常
                 *
                 * @param context 配置的应用上下文
                 * @throws SpringApplication.AbandonedRunException 当模式为 AOT_PROCESSING 时抛出
                 */
                @Override
                public void contextLoaded(ConfigurableApplicationContext context) {
                    ContextLoaderHook.this.contexts.add(context);
                    if (ContextLoaderHook.this.mode == Mode.AOT_PROCESSING) {
                        throw new SpringApplication.AbandonedRunException(context);
                    }
                }

                /**
                 * 当应用上下文启动失败时调用此方法
                 * <p>
                 * 将失败的上下文添加到失败上下文集合中
                 *
                 * @param context   配置的 Spring 应用上下文
                 * @param exception 导致启动失败的异常
                 */
                @Override
                public void failed(ConfigurableApplicationContext context, Throwable exception) {
                    ContextLoaderHook.this.failedContexts.add(context);
                }
            };
        }

        /**
         * 执行主操作并返回应用上下文
         * <p>
         * 使用给定的 Runnable 执行主操作, 然后返回应用上下文.
         *
         * @param action 要执行的主操作
         * @return 应用上下文
         * @throws Exception 如果执行过程中发生异常
         */
        private ApplicationContext runMain(Runnable action) throws Exception {
            return this.run(() -> {
                action.run();
                return null;
            });
        }

        /**
         * 执行给定的 Spring 应用上下文初始化操作并返回结果
         * <p>
         * 使用提供的 ThrowingSupplier 初始化 Spring 应用上下文, 若初始化成功则返回上下文对象.
         * 若初始化失败且仅有一个失败上下文, 则抛出 ContextLoadException; 否则抛出原始异常.
         * 如果没有找到根应用上下文或存在多个根上下文, 则抛出相应的状态异常.
         *
         * @param action 用于创建 ConfigurableApplicationContext 的函数, 可能抛出异常
         * @return 初始化成功的 ConfigurableApplicationContext 对象
         * @throws Exception 如果初始化过程中发生错误或找不到唯一的根上下文
         */
        private ApplicationContext run(
            org.springframework.util.function.ThrowingSupplier<ConfigurableApplicationContext> action) throws Exception {
            try {
                ConfigurableApplicationContext context = SpringApplication.withHook(this, action);
                if (context != null) {
                    return context;
                }
            } catch (SpringApplication.AbandonedRunException ex) {
                // Ignore
            } catch (Exception ex) {
                if (this.failedContexts.size() == 1) {
                    throw new ContextLoadException(this.failedContexts.get(0), ex);
                }
                throw ex;
            }
            List<ApplicationContext> rootContexts = this.contexts.stream()
                .filter((context) -> context.getParent() == null)
                .toList();
            Assert.state(!rootContexts.isEmpty(), "No root application context located");
            Assert.state(rootContexts.size() == 1, "No unique root application context located");
            return rootContexts.get(0);
        }
    }

    /**
     * 应用 Zeka 默认配置到环境
     * <p>
     * 检查环境是否已包含默认属性源, 如果已包含则直接返回. 否则, 设置测试类的默认属性并将其添加到环境属性源中.
     *
     * @param env 配置环境对象, 用于管理属性源
     * @throws NullPointerException 如果传入的 env 参数为 null
     */
    private void applyZekaDefaults(@NotNull ConfigurableEnvironment env) {
        if (env.getPropertySources().contains(DefaultEnvironment.DEFAULT_PROPERTIES_PROPERTY_SOURCE_NAME)) {
            return;
        }
        // 默认属性配置
        Properties defaultProperties = setUpTestClass(env);
        ZekaTestPropertySourceUtils.addInlinedPropertiesToEnvironment(env, defaultProperties);
    }

    /**
     * 初始化测试类所需的属性配置
     * <p>
     * 为测试类设置基础属性, 包括应用名称, 启动类型, 服务版本等, 并加载和执行所有 {@link LauncherInitiation} 初始化器.
     *
     * @param environment Spring 应用的配置环境对象
     * @return 初始化后的默认属性对象
     * @throws NullPointerException 如果传入的 environment 为 null
     */
    private static @NotNull Properties setUpTestClass(@NotNull ConfigurableEnvironment environment) {
        StartUtils.setFrameworkVersion();
        System.setProperty(App.START_TYPE, App.START_JUNIT);

        MutablePropertySources propertySources = environment.getPropertySources();
        propertySources.addFirst(new SimpleCommandLinePropertySource());

        String appName = System.getProperty(ConfigKey.SpringConfigKey.APPLICATION_NAME);
        System.setProperty(App.START_APPLICATION, App.START_APPLICATION);
        System.setProperty(ConfigKey.SERVICE_VERSION, StringPool.NULL_STRING);

        Properties defaultProperties = new Properties();
        defaultProperties.put(ConfigKey.POM_INFO_VERSION, StringPool.NULL_STRING);
        defaultProperties.put(ConfigKey.POM_INFO_GROUPID, App.BASE_PACKAGES);
        defaultProperties.put(ConfigKey.POM_INFO_ARTIFACTID, appName);
        defaultProperties.put(ConfigKey.SpringConfigKey.MAIN_ALLOW_BEAN_DEFINITION_OVERRIDING, ConfigDefaultValue.TRUE);

        List<LauncherInitiation> launcherList = new ArrayList<>();
        ServiceLoader.load(LauncherInitiation.class).forEach(launcherList::add);
        launcherList.stream().sorted(Comparator.comparing(LauncherInitiation::getOrder))
            .toList()
            .forEach(launcherService -> launcherService.launcherWrapper(environment,
                defaultProperties,
                appName,
                true));

        propertySources.addLast(new MapPropertySource(DefaultEnvironment.DEFAULT_PROPERTIES_PROPERTY_SOURCE_NAME,
            Tools.getMapFromProperties(defaultProperties)));
        return defaultProperties;
    }
}
