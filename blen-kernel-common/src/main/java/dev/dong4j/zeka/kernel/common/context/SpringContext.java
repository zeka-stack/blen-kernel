package dev.dong4j.zeka.kernel.common.context;

import dev.dong4j.zeka.kernel.common.constant.App;
import dev.dong4j.zeka.kernel.common.util.ConfigKit;
import dev.dong4j.zeka.kernel.common.util.GsonUtils;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.stereotype.Component;

/**
 * Spring上下文静态访问工具类，提供全局ApplicationContext的静态访问能力
 * <p>
 * 该类通过静态变量保存Spring ApplicationContext，使得在任何代码位置都能够获取到Spring容器
 * 与{@link EarlySpringContext}的区别在于获取的ApplicationContext是否执行了{@link ConfigurableApplicationContext#refresh()}：
 * <p>
 * <b>主要特性：</b>
 * - 在{@link ConfigurableApplicationContext#refresh()}之后初始化，容器已完全激活
 * - 可以正常发送事件，{@link ConfigurableApplicationContext#isActive()}为true
 * - 支持回调机制，在初始化前注册的回调会在初始化后执行
 * - 提供Bean获取、事件发布、调试信息等丰富功能
 * - 线程安全的单例模式，支持并发访问
 * <p>
 * <b>初始化时机：</b>
 * 通过{@link ApplicationContextAware}接口在{@link ConfigurableApplicationContext#refresh()}之后，
 * 在{org.springframework.context.support.ApplicationContextAwareProcessor}中被调用
 * <p>
 * <b>使用场景：</b>
 * - 需要在非管理Bean中获取Spring容器中的Bean
 * - 需要发布自定义事件的场景
 * - 需要获取所有实现类或带特定注解的Bean
 * - 在静态方法中需要访问Spring容器的情况
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2019.12.26 21:43
 * @see AbstractApplicationContext#prepareBeanFactory(org.springframework.beans.factory.config.ConfigurableListableBeanFactory)
 * @see EarlySpringContext
 * @since 1.0.0
 */
@Slf4j
@Component
@SuppressWarnings("all")
public class SpringContext implements ApplicationContextAware, DisposableBean {
    /** 回调函数列表，用于存储在ApplicationContext初始化前注册的回调 */
    private static final List<ContextCallback> CALL_BACKS = new ArrayList<>();
    /** 是否允许添加回调的标志，初始化后将设置为false */
    private static boolean allowAddCallback = true;
    /** Spring应用上下文，静态保存以便全局访问 */
    private static ApplicationContext applicationContext = null;

    /**
     * 设置Spring应用上下文（ApplicationContextAware接口实现）
     * <p>
     * 该方法会在Spring容器初始化过程中被自动调用，用于注入ApplicationContext
     * 初始化完成后会执行所有的回调函数，并禁止后续回调的注册
     * <p>
     * 注意：如果多次调用该方法（在Spring Cloud环境中可能发生），会记录警告日志
     *
     * @param applicationContext Spring应用上下文
     * @throws BeansException 如果设置过程中出现Bean相关错误
     * @since 1.0.0
     */
    @Override
    public void setApplicationContext(@NotNull ApplicationContext applicationContext) throws BeansException {
        if (SpringContext.applicationContext != null) {
            log.warn("SpringContext 中的 ApplicationContext 被覆盖, 原有 ApplicationContext 为:" + SpringContext.applicationContext);
        }
        SpringContext.applicationContext = applicationContext;

        if (allowAddCallback) {
            for (ContextCallback callBack : SpringContext.CALL_BACKS) {
                callBack.executor();
            }
            CALL_BACKS.clear();
        }
        SpringContext.allowAddCallback = false;
    }

    /**
     * 添加上下文初始化回调函数
     * <p>
     * 针对某些初始化方法，在SpringContext未初始化时可以提交回调方法
     * 在SpringContext初始化后，会自动执行这些回调函数
     * <p>
     * 使用场景：
     * - 在静态方法中需要使用Spring容器，但容器可能还未初始化
     * - 需要在Spring容器初始化后立即执行的逻辑
     * - 解决初始化顺序问题导致的依赖注入失败
     * <p>
     * 注意：该方法使用synchronized保证线程安全
     *
     * @param callBack 要添加的回调函数
     * @since 1.0.0
     */
    public static synchronized void addCallBacks(ContextCallback callBack) {
        if (allowAddCallback) {
            SpringContext.CALL_BACKS.add(callBack);
        } else {
            log.warn("CallBack: {} 已无法添加", callBack.getCallbackName());
            callBack.executor();
        }
    }

    /**
     * 取得存储在静态变量中的 ApplicationContext.
     *
     * @return the application context
     * @since 1.0.0
     */
    @Contract(pure = true)
    public static ApplicationContext getApplicationContext() {
        InnerContext.assertContextInjected(applicationContext);
        return applicationContext;
    }

    /**
     * Set application context.
     *
     * @param context the context
     * @since 1.0.0
     */
    public static void setApplicationContext(ConfigurableApplicationContext context) {
        applicationContext = context;
    }

    /**
     * 从静态变量 applicationContext 中取得 Bean, 自动转型为所赋值对象的类型.
     *
     * @param <T>  the type parameter
     * @param name the name
     * @return the bean
     * @since 1.0.0
     */
    @NotNull
    public static <T> T getInstance(String name) {
        return InnerContext.getInstance(applicationContext, name);
    }

    /**
     * 从静态变量 applicationContext 中取得 Bean, 自动转型为所赋值对象的类型.
     *
     * @param <T>          the type parameter
     * @param requiredType the required type
     * @return the bean
     * @since 1.0.0
     */
    @NotNull
    public static <T> T getInstance(Class<T> requiredType) {
        return InnerContext.getInstance(applicationContext, requiredType);
    }

    /**
     * 通过 beanName 和 beanClass 获取 bean, 一般用于多个不同名称的相同 bean
     *
     * @param <T>       the type parameter
     * @param beanClass the bean class
     * @param beanName  the bean name
     * @return the instance
     * @since 1.0.0
     */
    @NotNull
    public static <T> T getInstance(Class<T> beanClass, String beanName) {
        return InnerContext.getInstance(applicationContext, beanClass, beanName);
    }

    /**
     * 获取容器中特定注解的所有 bean
     *
     * @param annotationType the annotation type
     * @return the map
     * @since 1.0.0
     */
    @NotNull
    public static Map<String, Object> getBeansWithAnnotation(Class<? extends Annotation> annotationType) {
        return InnerContext.getBeansWithAnnotation(applicationContext, annotationType);
    }

    /**
     * 获取指定 class 的所有实现类
     *
     * @param <T>   parameter
     * @param clazz the clazz
     * @return the impl class
     * @since 1.0.0
     */
    @NotNull
    public static <T> List<Class<T>> getImplClass(Class<T> clazz) {
        return InnerContext.getImplClass(applicationContext, clazz);
    }

    /**
     * 获取指定 class 的所有实现类
     *
     * @param <T>   the type parameter
     * @param clazz the clazz
     * @return the impl instance
     * @since 1.0.0
     */
    @NotNull
    public static <T> Map<String, T> getImplInstance(Class<T> clazz) {
        return InnerContext.getImplInstance(applicationContext, clazz);
    }

    /**
     * 手动发布自定义事件
     *
     * @param applicationEvent the application event
     * @since 1.0.0
     */
    public static void publishEvent(ApplicationEvent applicationEvent) {
        applicationContext.publishEvent(applicationEvent);
    }

    /**
     * Show debug info.
     *
     * @since 1.0.0
     */
    public static void showDebugInfo() {
        // 日志配置文件: MarkerPatternSelector.PatternMatch.key = properties
        Marker marker = MarkerFactory.getMarker("properties");

        // 脚本启动
        if (ConfigKit.isDebugModel()) {
            String[] beanNames = applicationContext.getBeanDefinitionNames();
            log.debug(marker, "=============== 所有的 Bean: {} ===============", beanNames.length);
            log.debug(marker, "{}", GsonUtils.toJson(Arrays.stream(beanNames)
                    .filter(s -> s.contains(App.BASE_PACKAGES))
                    .collect(Collectors.toList()),
                true));

            // 查看所有注册的 listener
            List<String> listenerList = SpringFactoriesLoader.loadFactoryNames(ApplicationListener.class, null);
            log.debug(marker, "=============== 所有的 Listener: {} ===============", listenerList.size());
            List<String> collect = listenerList.stream()
                .filter(l -> l.contains(App.BASE_PACKAGES))
                .map(Object::toString)
                .collect(Collectors.toList());
            log.debug(marker, "{}", GsonUtils.toJson(collect, true));
        }
    }

    /**
     * 实现 DisposableBean 接口, 在Context关闭时清理静态变量.
     *
     * @since 1.0.0
     */
    @Override
    public void destroy() {
        InnerContext.destroy(applicationContext);
    }

}
