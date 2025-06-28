package dev.dong4j.zeka.kernel.test;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.servlet.Filter;
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

public final class WebMvcTypeExcludeFilter extends StandardAnnotationCustomizableTypeExcludeFilter<WebMvcTest> {
    private static final Class<?>[] NO_CONTROLLERS = new Class[0];
    private static final String[] OPTIONAL_INCLUDES = new String[]{"org.springframework.security.config.annotation.web.WebSecurityConfigurer"};
    private static final Set<Class<?>> DEFAULT_INCLUDES;
    private static final Set<Class<?>> DEFAULT_INCLUDES_AND_CONTROLLER;
    private final Class<?>[] controllers;

    WebMvcTypeExcludeFilter(Class<?> testClass) {
        super(testClass);
        this.controllers = (Class[]) this.getAnnotation().getValue("controllers", Class[].class).orElse(NO_CONTROLLERS);
    }

    protected Set<Class<?>> getDefaultIncludes() {
        return ObjectUtils.isEmpty(this.controllers) ? DEFAULT_INCLUDES_AND_CONTROLLER : DEFAULT_INCLUDES;
    }

    protected Set<Class<?>> getComponentIncludes() {
        return new LinkedHashSet(Arrays.asList(this.controllers));
    }

    static {
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

        for (String optionalInclude : OPTIONAL_INCLUDES) {
            try {
                includes.add(ClassUtils.forName(optionalInclude, null));
            } catch (Exception var6) {
            }
        }

        DEFAULT_INCLUDES = Collections.unmodifiableSet(includes);
        includes = new LinkedHashSet(DEFAULT_INCLUDES);
        includes.add(Controller.class);
        includes.add(RestController.class);
        DEFAULT_INCLUDES_AND_CONTROLLER = Collections.unmodifiableSet(includes);
    }
}
