package dev.dong4j.zeka.kernel.test.mock.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

/**
 * <p>Description: 反射工具类</p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.27 18:08
 * @since 1.0.0
 */
@UtilityClass
public final class ReflectionUtils {

    /**
     * 反射设置值
     *
     * @param object 对象
     * @param method 方法
     * @param args   方法参数对象
     * @throws ReflectiveOperationException 反射操作异常
     * @since 1.0.0
     */
    public static void setRefValue(Object object, @NotNull Method method, Object... args) throws ReflectiveOperationException {
        method.invoke(object, args);
    }

    /**
     * 有setter方法的字段及其setter方法
     *
     * @param clazz Class对象
     * @return 有setter方法的 字段及其setter方法
     * @throws IntrospectionException 内省异常
     * @since 1.0.0
     */
    public static Map<Field, Method> fieldAndSetterMethod(Class<?> clazz) throws IntrospectionException {
        Map<Field, Method> map = new LinkedHashMap<>();
        BeanInfo beanInfo = Introspector.getBeanInfo(clazz, Object.class);
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (Field field : clazz.getDeclaredFields()) {
            for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                if (propertyDescriptor.getName().equals(field.getName()) && propertyDescriptor.getWriteMethod() != null) {
                    map.put(field, propertyDescriptor.getWriteMethod());
                }
            }
        }
        return map;
    }

}
