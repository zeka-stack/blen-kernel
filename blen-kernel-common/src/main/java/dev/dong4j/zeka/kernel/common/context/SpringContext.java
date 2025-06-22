package dev.dong4j.zeka.kernel.common.context;

import dev.dong4j.zeka.kernel.common.constant.App;
import dev.dong4j.zeka.kernel.common.util.ConfigKit;
import dev.dong4j.zeka.kernel.common.util.GsonUtils;
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

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>Description: 以静态变量保存 Spring ApplicationContext, 可在任何代码任何地方任何时候取出 ApplicaitonContext. </p>
 * 与 {@link EarlySpringContext} 的区别在于获取的 ApplicationContext 是否执行了 {@link ConfigurableApplicationContext#refresh()}:
 * 未执行前不能发送事件, {@link ConfigurableApplicationContext#isActive()} 为 false, 当调用 ConfigurableApplicationContext 获取 bean 时将通过
 * {org.springframework.context.support.AbstractApplicationContext#assertBeanFactoryActive()} 判断是否可用;
 * {@link ApplicationContextAware} 则是在执行 {@link ConfigurableApplicationContext#refresh()} 之后,
 * 在 {org.springframework.context.support.ApplicationContextAwareProcessor} 被调用.
 *
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dong4j@gmail.com"
 * @date 2019.11.27 15:55
 * @see AbstractApplicationContext#prepareBeanFactory(org.springframework.beans.factory.config.ConfigurableListableBeanFactory)
 * @since 1.0.0
 */
@Slf4j
@Component
@SuppressWarnings("all")
public class SpringContext implements ApplicationContextAware, DisposableBean {
    /** CALL_BACKS */
    private static final List<ContextCallback> CALL_BACKS = new ArrayList<>();
    /** allowAddCallback */
    private static boolean allowAddCallback = true;
    /** applicationContext */
    private static ApplicationContext applicationContext = null;

    /**
     * Sets application context *
     *
     * @param applicationContext application context
     * @throws BeansException beans exception
     * @since 1.6.0
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
     * 针对 某些初始化方法, SpringContext 未初始化时 提交回调方法.
     * 在 SpringContext 初始化后, 进行回调使用, 比如在一些静态方法中实现某段逻辑, 当 spring 执行 {@link SpringContext#setApplicationContext} 将被调用
     *
     * @param callBack 回调函数
     * @since 1.7.0
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
