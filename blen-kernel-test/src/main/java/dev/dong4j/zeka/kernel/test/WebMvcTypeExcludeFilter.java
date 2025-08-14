package dev.dong4j.zeka.kernel.test;

import jakarta.servlet.Filter;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.boot.test.autoconfigure.filter.StandardAnnotationCustomizableTypeExcludeFilter;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.web.servlet.DelegatingFilterProxyRegistrationBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * WebMvc 类型排除过滤器, 用于 @WebMvcTest 注解的组件过滤
 *
 * @author dong4j
 * @version 1.0.0
 * @since 1.8.0
 */
public final class WebMvcTypeExcludeFilter extends StandardAnnotationCustomizableTypeExcludeFilter<WebMvcTest> {
    /** 空控制器数组常量 */
    private static final Class<?>[] NO_CONTROLLERS = new Class[0];
    /** 可选包含的类名数组 */
    private static final String[] OPTIONAL_INCLUDES = new String[]{"org.springframework.security.config.annotation.web.WebSecurityConfigurer"};
    /** 默认包含的组件集合 */
    private static final Set<Class<?>> DEFAULT_INCLUDES;
    /** 默认包含的组件和控制器集合 */
    private static final Set<Class<?>> DEFAULT_INCLUDES_AND_CONTROLLER;
    /** 控制器类数组 */
    private final Class<?>[] controllers;

    /**
     * 构造方法
     *
     * @param testClass 测试类
     */
    WebMvcTypeExcludeFilter(Class<?> testClass) {
        super(testClass);
        // 从注解中获取controllers属性值
        this.controllers = (Class[]) this.getAnnotation().getValue("controllers", Class[].class).orElse(NO_CONTROLLERS);
    }

    /**
     * 获取默认包含的组件集合
     *
     * @return 默认包含的组件集合
     */
    protected Set<Class<?>> getDefaultIncludes() {
        // 如果controllers为空则返回包含控制器的集合,否则返回默认集合
        return ObjectUtils.isEmpty(this.controllers) ? DEFAULT_INCLUDES_AND_CONTROLLER : DEFAULT_INCLUDES;
    }

    /**
     * 获取组件包含集合
     *
     * @return 组件包含集合
     */
    protected Set<Class<?>> getComponentIncludes() {
        return new LinkedHashSet(Arrays.asList(this.controllers));
    }

    // 静态初始化块
    static {
        // 初始化默认包含的组件集合
        Set<Class<?>> includes = new LinkedHashSet();
        includes.add(ControllerAdvice.class);
        includes.add(JsonComponent.class);
        includes.add(WebMvcConfigurer.class);
        includes.add(Filter.class);
        includes.add(FilterRegistrationBean.class);
        includes.add(DelegatingFilterProxyRegistrationBean.class);
        includes.add(HandlerMethodArgumentResolver.class);
        includes.add(HttpMessageConverter.class);
        includes.add(ErrorAttributes.class);
        includes.add(Converter.class);
        includes.add(GenericConverter.class);
        includes.add(HandlerInterceptor.class);

        // 添加可选的包含组件
        for (String optionalInclude : OPTIONAL_INCLUDES) {
            try {
                includes.add(ClassUtils.forName(optionalInclude, null));
            } catch (Exception var6) {
                // 忽略类加载异常
            }
        }

        // 设置不可修改的组件集合
        DEFAULT_INCLUDES = Collections.unmodifiableSet(includes);
        includes = new LinkedHashSet(DEFAULT_INCLUDES);
        includes.add(Controller.class);
        includes.add(RestController.class);
        DEFAULT_INCLUDES_AND_CONTROLLER = Collections.unmodifiableSet(includes);
    }
}
