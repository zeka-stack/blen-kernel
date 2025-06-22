package dev.dong4j.zeka.kernel.test;

import dev.dong4j.zeka.kernel.common.constant.ConfigKey;
import dev.dong4j.zeka.kernel.common.util.ConfigKit;
import dev.dong4j.zeka.kernel.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.AnnotatedClassFinder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTestContextBootstrapper;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.core.annotation.MergedAnnotations.SearchStrategy;
import org.springframework.test.context.ContextLoader;
import org.springframework.test.context.MergedContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.util.Assert;

import java.io.File;

/**
 * <p>Description:  </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.03.18 07:38
 * @since 1.0.0
 */
@Slf4j
class ZekaTestContextBootstrapper extends SpringBootTestContextBootstrapper {

    /**
     * 加载自定义上下文加载器
     *
     * @param testClass test class
     * @return the default context loader class
     * @since 1.0.0
     */
    @Override
    protected @NotNull Class<? extends ContextLoader> getDefaultContextLoaderClass(Class<?> testClass) {
        return ZekaSpringBootContextLoader.class;
    }

    /**
     * 加载测试环境的配置
     *
     * @param testClass test class
     * @return the string [ ]
     * @since 1.0.0
     */
    @Override
    protected String[] getProperties(Class<?> testClass) {
        this.getApplicationName(testClass);
        return MergedAnnotations.from(testClass, SearchStrategy.INHERITED_ANNOTATIONS)
            .get(ZekaTest.class).getValue("properties", String[].class)
            .orElse(new String[0]);

    }

    /**
     * 获取应用名
     *
     * @param testClass test class
     * @since 1.5.0
     */
    protected void getApplicationName(Class<?> testClass) {
        String applicationName = MergedAnnotations.from(testClass, SearchStrategy.INHERITED_ANNOTATIONS)
            .get(ZekaTest.class).getValue("appName", String.class)
            .orElse("");

        if (StringUtils.isBlank(applicationName)) {
            String configFilePath = ConfigKit.getConfigPath();
            File file = new File(configFilePath);
            // 直接解析文件目录, 使用当前目录名作为应用名 (target 上一级目录)
            applicationName = file.getParentFile().getParentFile().getName();
            log.warn("未显式设置 application name, 读取当前模块名作为应用名: [{}]", applicationName);
        }
        System.setProperty(ConfigKey.SpringConfigKey.APPLICATION_NAME, applicationName);
        System.setProperty(ConfigKey.SpringConfigKey.PACKAGE_NAME, applicationName);
    }

    /**
     * Gets web environment *
     *
     * @param testClass test class
     * @return the web environment
     * @since 1.0.0
     */
    @Override
    protected SpringBootTest.WebEnvironment getWebEnvironment(Class<?> testClass) {
        ZekaTest annotation = this.getZekaTestAnnotation(testClass);
        return (annotation != null) ? annotation.webEnvironment() : null;
    }

    /**
     * 解析 classes 属性, 如果未配置则查找 @SpringBootConfiguration 注解, 未找到则抛出 IllegalStateException 异常:
     * Unable to find a @SpringBootConfiguration, you need to use @ContextConfiguration or @SpringBootTest(classes=...) with your test
     * 1. 如果启动类使用 @SpringBootApplication 注解, 可不配置 classes 属性;
     * 2. 如果启动类使用 @EnableAutoConfiguration 则必须指定 classes
     *
     * @param testClass test class
     * @return the class [ ]
     * @since 1.0.0
     */
    @Override
    protected Class<?>[] getClasses(Class<?> testClass) {
        ZekaTest annotation = this.getZekaTestAnnotation(testClass);
        return (annotation != null) ? annotation.classes() : null;
    }

    /**
     * Gets test annotation *
     *
     * @param testClass test class
     * @return the  test annotation
     * @since 1.0.0
     */
    private ZekaTest getZekaTestAnnotation(Class<?> testClass) {
        return MergedAnnotations.from(testClass, SearchStrategy.INHERITED_ANNOTATIONS)
            .get(ZekaTest.class).synthesize(MergedAnnotation::isPresent)
            .orElse(null);
    }

    /**
     * Verify configuration *
     *
     * @param testClass test class
     * @since 1.0.0
     */
    @Override
    protected void verifyConfiguration(Class<?> testClass) {

        ZekaTest springBootTest = this.getZekaTestAnnotation(testClass);

        if (springBootTest != null
            && this.isListeningOnPortOverride(springBootTest.webEnvironment())
            && MergedAnnotations.from(testClass, SearchStrategy.INHERITED_ANNOTATIONS)
            .isPresent(WebAppConfiguration.class)) {
            throw new IllegalStateException("@WebAppConfiguration should only be used "
                + "with @ZekaTest when @ZekaTest is configured with a "
                + "mock web environment. Please remove @WebAppConfiguration or reconfigure @ZekaTest.");
        }
    }

    /**
     * Is listening on port boolean
     *
     * @param webEnvironment web environment
     * @return the boolean
     * @since 1.0.0
     */
    @Contract(pure = true)
    private boolean isListeningOnPortOverride(SpringBootTest.WebEnvironment webEnvironment) {
        return webEnvironment == SpringBootTest.WebEnvironment.DEFINED_PORT || webEnvironment == SpringBootTest.WebEnvironment.RANDOM_PORT;
    }

    /**
     * Get or find configuration classes
     *
     * @param mergedConfig merged config
     * @return the class [ ]
     * @since 1.4.0
     */
    @Override
    protected Class<?>[] getOrFindConfigurationClasses(@NotNull MergedContextConfiguration mergedConfig) {
        Class<?>[] classes = mergedConfig.getClasses();
        if (this.containsNonTestComponentOverride(classes) || mergedConfig.hasLocations()) {
            return classes;
        }

        Class<?> found = new AnnotatedClassFinder(SpringBootConfiguration.class)
            .findFromClass(mergedConfig.getTestClass());

        Assert.state(found != null, "未找到使用 @SpringBootApplication 的启动类, "
            + "如果启动类使用 @EnableAutoConfiguration 标识,"
            + "请使用 classes 属性指定此类, 请见 dev.dong4j.zeka.kernel.test.ZekaTest.classes 说明");

        return super.getOrFindConfigurationClasses(mergedConfig);
    }

    /**
     * Contains non test component
     *
     * @param classes classes
     * @return the boolean
     * @since 1.4.0
     */
    private boolean containsNonTestComponentOverride(@NotNull Class<?>[] classes) {
        for (Class<?> candidate : classes) {
            if (!MergedAnnotations.from(candidate, SearchStrategy.INHERITED_ANNOTATIONS)
                .isPresent(TestConfiguration.class)) {
                return true;
            }
        }
        return false;
    }

}
