package dev.dong4j.zeka.kernel.common.util;

import dev.dong4j.zeka.kernel.common.exception.LowestException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.MethodParameter;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.SynthesizingMethodParameter;
import org.springframework.util.Assert;
import org.springframework.web.method.HandlerMethod;

/**
 * <p>Description: 类工具类，扩展了Spring ClassUtils的功能，提供更强大的类操作能力</p>
 * <p>
 * 主要功能：
 * <ul>
 *     <li>方法参数信息获取（支持构造器和普通方法）</li>
 *     <li>注解获取和解析（支持组合注解和Spring的注解合并）</li>
 *     <li>泛型类型解析（接口泛型和父类泛型）</li>
 *     <li>代理对象检测（CGLIB、Javassist等）</li>
 *     <li>类型判断和检查工具</li>
 *     <li>对象实例化和类加载操作</li>
 * </ul>
 * </p>
 * <p>
 * 使用示例：
 * <pre>
 * // 获取方法参数信息
 * Method method = MyClass.class.getMethod("myMethod", String.class, int.class);
 * MethodParameter param = ClassUtils.getMethodParameter(method, 0);
 *
 * // 获取方法上的注解
 * MyAnnotation annotation = ClassUtils.getAnnotation(method, MyAnnotation.class);
 *
 * // 解析泛型类型
 * Class<?> genericType = ClassUtils.getSuperClassT(MyClass.class, 0);
 *
 * // 检查是否为代理对象
 * boolean isProxy = ClassUtils.isProxy(MyClass.class);
 *
 * // 实例化对象
 * MyClass instance = ClassUtils.newInstance(MyClass.class);
 *
 * // 获取类名包路径
 * String packageName = ClassUtils.getPackageName(MyClass.class);
 * </pre>
 * </p>
 * <p>
 * 技术特性：
 * <ul>
 *     <li>继承Spring的ClassUtils功能</li>
 *     <li>支持方法参数名发现</li>
 *     <li>提供注解合并解析功能</li>
 *     <li>支持泛型类型递归解析</li>
 *     <li>检测多种代理类型（CGLIB、Javassist等）</li>
 *     <li>安全的对象实例化机制</li>
 *     <li>类型兼容性检查</li>
 * </ul>
 * </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2019.12.30 22:37
 * @since 1.0.0
 */
@Slf4j
@SuppressWarnings("all")
public class ClassUtils extends org.springframework.util.ClassUtils {

    /** 参数名发现器 */
    public static final ParameterNameDiscoverer PARAMETER_NAME_DISCOVERER = new DefaultParameterNameDiscoverer();
    /** 包分隔符 */
    public static final char PACKAGE_SEPARATOR = '.';
    /** 代理类名称列表 */
    protected static final List<String> PROXY_CLASS_NAMES = Arrays.asList(
        "net.sf.cglib.proxy.Factory",
        "org.springframework.cglib.proxy.Factory",
        "javassist.util.proxy.ProxyObject",
        "org.apache.ibatis.javassist.util.proxy.ProxyObject");

    /**
     * 获取方法参数信息
     *
     * @param constructor    构造器
     * @param parameterIndex 参数序号
     * @return 方法参数对象
     * @since 1.0.0
     */
    public static @NotNull MethodParameter getMethodParameter(Constructor<?> constructor, int parameterIndex) {
        MethodParameter methodParameter = new SynthesizingMethodParameter(constructor, parameterIndex);
        methodParameter.initParameterNameDiscovery(PARAMETER_NAME_DISCOVERER);
        return methodParameter;
    }

    /**
     * 获取方法参数信息
     *
     * @param method         方法
     * @param parameterIndex 参数序号
     * @return 方法参数对象
     * @since 1.0.0
     */
    public static @NotNull MethodParameter getMethodParameter(Method method, int parameterIndex) {
        MethodParameter methodParameter = new SynthesizingMethodParameter(method, parameterIndex);
        methodParameter.initParameterNameDiscovery(PARAMETER_NAME_DISCOVERER);
        return methodParameter;
    }

    /**
     * 获取方法上的注解
     *
     * @param <A>            注解泛型
     * @param method         方法对象
     * @param annotationType 注解类
     * @return 注解对象
     * @since 1.0.0
     */
    public static <A extends Annotation> A getAnnotation(@NotNull Method method, Class<A> annotationType) {
        Class<?> targetClass = method.getDeclaringClass();
        // The method may be on an interface, but we need attributes from the target class.
        // If the target class is null, the method will be unchanged.
        Method specificMethod = ClassUtils.getMostSpecificMethod(method, targetClass);
        // If we are dealing with method with generic parameters, find the original method.
        specificMethod = BridgeMethodResolver.findBridgedMethod(specificMethod);
        // 先找方法,再找方法上的类
        A annotation = AnnotatedElementUtils.findMergedAnnotation(specificMethod, annotationType);
        if (null != annotation) {
            return annotation;
        }
        // 获取类上面的Annotation,可能包含组合注解,故采用spring的工具类
        return AnnotatedElementUtils.findMergedAnnotation(specificMethod.getDeclaringClass(), annotationType);
    }

    /**
     * 获取处理器方法上的注解
     *
     * @param <A>            注解泛型
     * @param handlerMethod  处理器方法
     * @param annotationType 注解类
     * @return 注解对象
     * @since 1.0.0
     */
    public static <A extends Annotation> A getAnnotation(@NotNull HandlerMethod handlerMethod, Class<A> annotationType) {
        // 先找方法,再找方法上的类
        A annotation = handlerMethod.getMethodAnnotation(annotationType);
        if (null != annotation) {
            return annotation;
        }
        // 获取类上面的Annotation,可能包含组合注解,故采用spring的工具类
        Class<?> beanType = handlerMethod.getBeanType();
        return AnnotatedElementUtils.findMergedAnnotation(beanType, annotationType);
    }

    /**
     * 获取接口上的泛型类型
     *
     * @param clazz          类
     * @param interfaceClass 接口类
     * @param index          泛型索引
     * @return 泛型类
     * @since 1.0.0
     */
    public static @NotNull Class<?> getInterfaceT(@NotNull Class<?> clazz, Class<?> interfaceClass, int index) {
        Type[] types = clazz.getGenericInterfaces();
        Type targetType = Arrays.stream(types).filter(type -> type.toString().contains(interfaceClass.getName())).findAny().orElse(null);
        if (targetType == null) {
            throw new LowestException("[{}] 未实现 [{}] 接口", clazz, interfaceClass);
        }
        ParameterizedType parameterizedType = (ParameterizedType) targetType;
        Type type = parameterizedType.getActualTypeArguments()[index];
        return checkType(type, index);
    }

    /**
     * 获取父类上的泛型
     *
     * @param clazz 类
     * @param index 索引
     * @return 泛型类
     * @since 1.0.0
     */
    public static @NotNull Class<?> getSuperClassT(@NotNull Class<?> clazz, @NotNull Integer index) {
        Type type = clazz.getGenericSuperclass();
        ParameterizedType parameterizedType = (ParameterizedType) type;
        Type actType = parameterizedType.getActualTypeArguments()[index];
        return checkType(actType, index);
    }

    /**
     * 递归获取泛型类型
     *
     * @param type  类型
     * @param index 索引
     * @return 类
     * @since 1.0.0
     */
    @Contract("null, _ -> fail")
    private static @NotNull Class<?> checkType(Type type, int index) {
        if (type instanceof Class<?>) {
            return (Class<?>) type;
        } else if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type t = parameterizedType.getActualTypeArguments()[index];
            return checkType(t, index);
        } else {
            String className = type == null ? "null" : type.getClass().getName();
            throw new LowestException("Expected a Class, ParameterizedType" + ", but <" + type + "> is of type " + className);
        }
    }

    /**
     * 判断传入的类型是否是布尔类型
     *
     * @param type 类型
     * @return 如果是原生布尔或者包装类型布尔 , 均返回 true
     * @since 1.0.0
     */
    @Contract(pure = true)
    public static boolean isBoolean(Class<?> type) {
        return type == boolean.class || Boolean.class == type;
    }

    /**
     * 判断是否为代理对象
     *
     * @param clazz 传入 class 对象
     * @return 如果对象class是代理 class, 返回 true
     * @since 1.0.0
     */
    @Contract("null -> false")
    public static boolean isProxy(Class<?> clazz) {
        if (clazz != null) {
            for (Class<?> cls : clazz.getInterfaces()) {
                if (PROXY_CLASS_NAMES.contains(cls.getName())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 获取当前对象的 class
     *
     * @param clazz 传入类
     * @return 如果是代理的class , 返回父 class, 否则返回自身
     * @since 1.0.0
     */
    public static @NotNull Class<?> getClass(Class<?> clazz) {
        return isProxy(clazz) ? clazz.getSuperclass() : clazz;
    }

    /**
     * 获取当前对象的class
     *
     * @param object 对象
     * @return 返回对象的 user class
     * @since 1.0.0
     */
    public static @NotNull Class<?> getClass(Object object) {
        Assert.notNull(object, "Error: Instance must not be null");
        return getClass(object.getClass());
    }

    /**
     * 根据指定的 class ,  实例化一个对象, 根据构造参数来实例化
     * 在 java9 及其之后的版本 Class.newInstance() 方法已被废弃
     *
     * @param <T>   类型, 由输入类型决定
     * @param clazz 需要实例化的对象
     * @return 返回新的实例
     * @since 1.0.0
     */
    public static <T> @NotNull T newInstance(@NotNull Class<T> clazz) {
        try {
            Constructor<T> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new LowestException("实例化对象时出现错误,请尝试给 %s 添加无参的构造方法", e, clazz.getName());
        }
    }

    /**
     * 请仅在确定类存在的情况下调用该方法
     *
     * @param name 类名称
     * @return 返回转换后的 Class
     * @since 1.0.0
     */
    public static @NotNull Class<?> toClassConfident(String name) {
        try {
            return Class.forName(name);
        } catch (ClassNotFoundException e) {
            throw new LowestException("找不到指定的class！请仅在明确确定会有 class 的时候, 调用该方法", e);
        }
    }


    /**
     * 获取类的包名
     *
     * @param clazz 类
     * @return 包名
     * @since 1.0.0
     */
    public static @NotNull String getPackageName(Class<?> clazz) {
        Assert.notNull(clazz, "Class must not be null");
        return getPackageName(clazz.getName());
    }

    /**
     * 获取完全限定类名的包名
     *
     * @param fqClassName 完全限定类名
     * @return 包名
     * @since 1.0.0
     */
    public static @NotNull String getPackageName(String fqClassName) {
        Assert.notNull(fqClassName, "Class name must not be null");
        int lastDotIndex = fqClassName.lastIndexOf(PACKAGE_SEPARATOR);
        return (lastDotIndex != -1 ? fqClassName.substring(0, lastDotIndex) : "");
    }

}
