package dev.dong4j.zeka.kernel.common.context;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>Description:  </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.10.30 23:27
 * @since 1.7.0
 */
@Slf4j
final class InnerContext {

    /**
     * 从静态变量 applicationContext 中取得 Bean, 自动转型为所赋值对象的类型.
     *
     * @param <T>                the type parameter
     * @param applicationContext application context
     * @param name               the name
     * @return the bean
     * @since 1.0.0
     */
    @NotNull
    @SuppressWarnings("unchecked")
    static <T> T getInstance(ApplicationContext applicationContext, String name) {
        assertContextInjected(applicationContext);
        return (T) applicationContext.getBean(name);
    }

    /**
     * 从静态变量 applicationContext 中取得 Bean, 自动转型为所赋值对象的类型.
     *
     * @param <T>                the type parameter
     * @param applicationContext application context
     * @param requiredType       the required type
     * @return the bean
     * @since 1.0.0
     */
    @NotNull
    static <T> T getInstance(ApplicationContext applicationContext, Class<T> requiredType) {
        assertContextInjected(applicationContext);
        return applicationContext.getBean(requiredType);
    }

    /**
     * 通过 beanName 和 beanClass 获取 bean, 一般用于多个不同名称的相同 bean
     *
     * @param <T>                the type parameter
     * @param applicationContext application context
     * @param beanClass          the bean class
     * @param beanName           the bean name
     * @return the instance
     * @since 1.0.0
     */
    @NotNull
    static <T> T getInstance(ApplicationContext applicationContext, Class<T> beanClass, String beanName) {
        assertContextInjected(applicationContext);
        return applicationContext.getBean(beanName, beanClass);
    }

    /**
     * 获取容器中特定注解的所有 bean
     *
     * @param applicationContext application context
     * @param annotationType     the annotation type
     * @return the map
     * @since 1.0.0
     */
    @NotNull
    static Map<String, Object> getBeansWithAnnotation(ApplicationContext applicationContext, Class<? extends Annotation> annotationType) {
        assertContextInjected(applicationContext);
        return applicationContext.getBeansWithAnnotation(annotationType);
    }

    /**
     * 获取指定 class 的所有实现类
     *
     * @param <T>                parameter
     * @param applicationContext application context
     * @param clazz              the clazz
     * @return the impl class
     * @since 1.0.0
     */
    @NotNull
    @SuppressWarnings("unchecked")
    static <T> List<Class<T>> getImplClass(ApplicationContext applicationContext, Class<T> clazz) {
        assertContextInjected(applicationContext);
        List<Class<T>> list = new ArrayList<>();
        Map<String, T> map = applicationContext.getBeansOfType(clazz);
        for (Object obj : map.values()) {
            String name = obj.getClass().getName();
            int pos = name.indexOf("$$");
            if (pos > 0) {
                name = name.substring(0, name.indexOf("$$"));
            }
            // 手动加载 class, 如果被 IOC 管理, 则直接返回, 因为通过类加载机制知道, 如果是 bean, Spring 将会加载
            // 如果再使用 Class.forName(beanName), 将直接从缓存从返回, 不会重复加载
            Class<T> cls = null;
            try {
                cls = (Class<T>) Class.forName(name);
            } catch (ClassNotFoundException ignored) {
            }
            list.add(cls);
        }
        return list;
    }

    /**
     * 获取指定 class 的所有实现类
     *
     * @param <T>                the type parameter
     * @param applicationContext application context
     * @param clazz              the clazz
     * @return the impl instance
     * @since 1.0.0
     */
    @NotNull
    static <T> Map<String, T> getImplInstance(ApplicationContext applicationContext, Class<T> clazz) {
        assertContextInjected(applicationContext);
        return applicationContext.getBeansOfType(clazz);
    }

    /**
     * 在应用启动过程中, applicationContext 执行了 refresh 之后才能调用此方法.
     *
     * @param applicationContext application context
     * @param applicationEvent   application event
     * @since 1.6.0
     */
    static void publishEvent(ApplicationContext applicationContext, ApplicationEvent applicationEvent) {
        assertContextInjected(applicationContext);
        applicationContext.publishEvent(applicationEvent);
    }

    /**
     * 检查 ApplicationContext 不为空.
     *
     * @param applicationContext application context
     * @since 1.0.0
     */
    @Contract(pure = true)
    static void assertContextInjected(ApplicationContext applicationContext) {
        if (applicationContext == null) {
            throw new IllegalStateException("SpringContext's applicationContext is null");
        }
    }

    /**
     * Destroy
     *
     * @param applicationContext application context
     * @since 1.7.0
     */
    static void destroy(ApplicationContext applicationContext) {
        if (log.isDebugEnabled()) {
            log.debug("clear SpringContext's ApplicationContext: {}", applicationContext);
        }
        applicationContext = null;
    }

}
