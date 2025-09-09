package dev.dong4j.zeka.kernel.common.util;

import dev.dong4j.zeka.kernel.common.constant.ConfigKey;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * <p>Description: 类加载器工具类，提供类加载相关的工具方法</p>
 * <p>
 * 主要功能：
 * <ul>
 *     <li>类加载器获取</li>
 *     <li>类实例化</li>
 *     <li>资源配置查找</li>
 *     <li>类可用性检查</li>
 *     <li>线程上下文类加载器处理</li>
 * </ul>
 * </p>
 * <p>
 * 使用示例：
 * <pre>
 * // 获取线程上下文类加载器
 * ClassLoader classLoader = LoaderUtils.getThreadContextClassLoader();
 *
 * // 加载类
 * Class<?> clazz = LoaderUtils.loadClass("com.example.MyClass");
 *
 * // 实例化对象
 * MyClass instance = LoaderUtils.newInstanceOf("com.example.MyClass");
 *
 * // 检查类是否可用
 * boolean isAvailable = LoaderUtils.isClassAvailable("com.example.MyClass");
 *
 * // 查找资源
 * Collection<URL> resources = LoaderUtils.findResources("config.properties");
 * </pre>
 * </p>
 * <p>
 * 技术特性：
 * <ul>
 *     <li>支持线程上下文类加载器</li>
 *     <li>提供类加载器链处理</li>
 *     <li>支持资源配置查找</li>
 *     <li>安全的类实例化机制</li>
 *     <li>兼容不同Java版本</li>
 *     <li>处理类加载器权限问题</li>
 * </ul>
 * </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.06.04 19:21
 * @see ClassLoader
 * @see RuntimePermission
 * @see Thread#getContextClassLoader()
 * @see ClassLoader#getSystemClassLoader()
 * @since 1.0.0
 */
@UtilityClass
public final class LoaderUtils {

    /** 如果设置为 true, 则只使用本地类加载器, 如果没有设置则使用线程上下文加载器 */
    public static final String IGNORE_TCCL_PROPERTY = ConfigKey.PREFIX + "ignoreTCL";
    /** 忽略TCCL标志 */
    private static Boolean ignoreTCCL;
    /**
     * GET_CLASS_LOADER_DISABLED
     * JDK 17+ 默认允许获取 ClassLoader，无需安全检查
     */
    private static final boolean GET_CLASS_LOADER_DISABLED = false;
    /** 线程上下文类加载器获取器 */
    private static final PrivilegedAction<ClassLoader> TCCL_GETTER = new ThreadContextClassLoaderGetter();

    /**
     * 获取当前线程类加载器.
     * 如果TCCL为空, 则返回系统类加载器.
     * 如果系统类加载器也为空, 则返回该类的类加载器.
     * 如果使用不允许访问线程类加载器或系统类加载器的SecurityManager运行, 则返回该类的类加载器.
     *
     * @return 当前线程上下文类加载器
     * @since 1.0.0
     */
    public static ClassLoader getThreadContextClassLoader() {
        if (GET_CLASS_LOADER_DISABLED) {
            // 如果禁止获取 TCCL，就返回当前类的 ClassLoader
            return LoaderUtils.class.getClassLoader();
        }
        // 直接运行 PrivilegedAction，不再使用 AccessController
        return TCCL_GETTER.run();
    }

    /**
     * 线程上下文类加载器获取器
     *
     * @author dong4j
     * @version 1.0.0
     * @email "mailto:dong4j@gmail.com"
     * @date 2020.06.04 19:21
     * @since 1.0.0
     */
    private static class ThreadContextClassLoaderGetter implements PrivilegedAction<ClassLoader> {
        /**
         * 运行获取类加载器
         *
         * @return 类加载器
         * @since 1.0.0
         */
        @Override
        public ClassLoader run() {
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            if (cl != null) {
                return cl;
            }
            ClassLoader ccl = LoaderUtils.class.getClassLoader();
            return ccl == null && !GET_CLASS_LOADER_DISABLED ? ClassLoader.getSystemClassLoader() : ccl;
        }
    }

    /**
     * 获取类加载器数组
     *
     * @return 类加载器数组
     * @since 1.0.0
     */
    public static ClassLoader[] getClassLoaders() {
        List<ClassLoader> classLoaders = new ArrayList<>();
        ClassLoader tcl = getThreadContextClassLoader();
        classLoaders.add(tcl);
        // Some implementations may use null to represent the bootstrap class loader.
        ClassLoader current = LoaderUtils.class.getClassLoader();
        if (current != null && current != tcl) {
            classLoaders.add(current);
            ClassLoader parent = current.getParent();
            while (parent != null && !classLoaders.contains(parent)) {
                classLoaders.add(parent);
            }
        }
        ClassLoader parent = tcl == null ? null : tcl.getParent();
        while (parent != null && !classLoaders.contains(parent)) {
            classLoaders.add(parent);
            parent = parent.getParent();
        }
        ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
        if (!classLoaders.contains(systemClassLoader)) {
            classLoaders.add(systemClassLoader);
        }
        return classLoaders.toArray(new ClassLoader[0]);
    }

    /**
     * 确定命名类是否可以加载
     *
     * @param className 类名
     * @return 如果可以找到类则返回true，否则返回false
     * @since 1.0.0
     */
    public static boolean isClassAvailable(String className) {
        try {
            Class<?> clazz = loadClass(className);
            return clazz != null;
        } catch (ClassNotFoundException | LinkageError e) {
            return false;
        } catch (Throwable e) {
            LowLevelLogUtils.logException("Unknown error checking for existence of class: " + className, e);
            return false;
        }
    }

    /**
     * 按名称加载类, 如果指定 IGNORE_TCCL_PROPERTY 属性并将其设置为除 false 之外的任何值, 则将使用默认的类加载器
     *
     * @param className 类名
     * @return 给定名称的类
     * @throws ClassNotFoundException 如果找不到指定的类名
     * @since 1.0.0
     */
    public static Class<?> loadClass(String className) throws ClassNotFoundException {
        if (isIgnoreTccl()) {
            return Class.forName(className);
        }
        try {
            return getThreadContextClassLoader().loadClass(className);
        } catch (Throwable ignored) {
            return Class.forName(className);
        }
    }

    /**
     * 使用默认构造函数加载和实例化类
     *
     * @param <T>   泛型类型
     * @param clazz 类
     * @return 类的新实例
     * @throws InstantiationException    如果实例化类时出现异常
     * @throws IllegalAccessException    如果无法通过公共构造函数实例化类
     * @throws InvocationTargetException 如果构造类时出现异常
     * @since 1.0.0
     */
    public static <T> @NotNull T newInstanceOf(@NotNull Class<T> clazz)
        throws InstantiationException, IllegalAccessException, InvocationTargetException {
        try {
            return clazz.getConstructor().newInstance();
        } catch (NoSuchMethodException ignored) {
            try {
                return clazz.getDeclaredConstructor().newInstance();
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 使用默认构造函数加载和实例化类
     *
     * @param <T>       泛型类型
     * @param className 类名
     * @return 类的新实例
     * @throws ClassNotFoundException    如果该类对常规ClassLoader不可用
     * @throws IllegalAccessException    如果无法通过公共构造函数实例化类
     * @throws InstantiationException    如果实例化类时出现异常
     * @throws NoSuchMethodException     如果该类上没有无参构造函数
     * @throws InvocationTargetException 如果构造类时出现异常
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    public static <T> @NotNull T newInstanceOf(String className) throws ClassNotFoundException, IllegalAccessException,
        InstantiationException, NoSuchMethodException,
        InvocationTargetException {
        return newInstanceOf((Class<T>) loadClass(className));
    }

    /**
     * 使用默认构造函数加载和实例化派生类
     *
     * @param <T>       要检查的类型
     * @param className 类名
     * @param clazz     要转换为的类
     * @return 类的新实例并转换为 {@code T}
     * @throws ClassNotFoundException    如果该类对常规ClassLoader不可用
     * @throws NoSuchMethodException     如果该类上没有无参构造函数
     * @throws InvocationTargetException 如果构造类时出现异常
     * @throws InstantiationException    如果实例化类时出现异常
     * @throws IllegalAccessException    如果无法通过公共构造函数实例化类
     * @since 1.0.0
     */
    @Contract("_, _ -> param1")
    public static <T> T newCheckedInstanceOf(String className, @NotNull Class<T> clazz)
        throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException,
        IllegalAccessException {
        return clazz.cast(newInstanceOf(className));
    }

    /**
     * 加载并实例化由属性名给出的类
     *
     * @param <T>          要转换为的类型
     * @param propertyName 要查找类名的属性名
     * @param clazz        要转换为的类
     * @return 属性中给定类的新实例，如果属性未设置则返回 {@code null}
     * @throws ClassNotFoundException    如果该类对常规ClassLoader不可用
     * @throws NoSuchMethodException     如果该类上没有无参构造函数
     * @throws InvocationTargetException 如果构造类时出现异常
     * @throws InstantiationException    如果实例化类时出现异常
     * @throws IllegalAccessException    如果无法通过公共构造函数实例化类
     * @since 1.0.0
     */
    public static <T> @Nullable T newCheckedInstanceOfProperty(String propertyName, Class<T> clazz)
        throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException,
        IllegalAccessException {
        String className = PropertiesUtils.getProperties().getStringProperty(propertyName);
        if (className == null) {
            return null;
        }
        return newCheckedInstanceOf(className, clazz);
    }

    /**
     * 是否忽略线程上下文类加载器
     *
     * @return 是否忽略TCCL
     * @since 1.0.0
     */
    private static boolean isIgnoreTccl() {
        if (ignoreTCCL == null) {
            String ignoreTccl = PropertiesUtils.getProperties().getStringProperty(IGNORE_TCCL_PROPERTY, null);
            ignoreTCCL = ignoreTccl != null && !"false".equalsIgnoreCase(ignoreTccl.trim());
        }
        return ignoreTCCL;
    }

    /**
     * 查找类路径资源
     *
     * @param resource 要查找的资源名称
     * @return 匹配资源名称的URL集合。如果找不到资源，则为空
     * @since 1.0.0
     */
    public static @NotNull Collection<URL> findResources(String resource) {
        Collection<UrlResource> urlResources = findUrlResources(resource);
        Collection<URL> resources = new LinkedHashSet<>(urlResources.size());
        for (UrlResource urlResource : urlResources) {
            resources.add(urlResource.url());
        }
        return resources;
    }

    /**
     * 查找URL资源
     *
     * @param resource 资源
     * @return 集合
     * @since 1.0.0
     */
    @SuppressWarnings("PMD.Indentation")
    static @NotNull Collection<UrlResource> findUrlResources(String resource) {
        // @formatter:off
        ClassLoader[] candidates = {
            getThreadContextClassLoader(),
            LoaderUtils.class.getClassLoader(),
            GET_CLASS_LOADER_DISABLED ? null : ClassLoader.getSystemClassLoader()};
        // @formatter:on
        Collection<UrlResource> resources = new LinkedHashSet<>();
        for (ClassLoader cl : candidates) {
            if (cl != null) {
                try {
                    Enumeration<URL> resourceEnum = cl.getResources(resource);
                    while (resourceEnum.hasMoreElements()) {
                        resources.add(new UrlResource(cl, resourceEnum.nextElement()));
                    }
                } catch (IOException e) {
                    LowLevelLogUtils.logException(e);
                }
            }
        }
        return resources;
    }

    /**
     * URL和类加载器对
     *
     * @param classLoader 类加载器
     * @param url         URL
     * @author dong4j
     * @version 1.0.0
     * @email "mailto:dong4j@gmail.com"
     * @date 2020.06.04 19:21
     * @since 1.0.0
     */
    record UrlResource(ClassLoader classLoader, URL url) {
        /**
         * URL资源
         *
         * @param classLoader 类加载器
         * @param url         URL
         * @since 1.0.0
         */
        @Contract(pure = true)
        UrlResource {
        }

        /**
         * 判断是否相等
         *
         * @param o 对象
         * @return 是否相等
         * @since 1.0.0
         */
        @Contract(value = "null -> false", pure = true)
        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || this.getClass() != o.getClass()) {
                return false;
            }

            UrlResource that = (UrlResource) o;

            if (!Objects.equals(this.classLoader, that.classLoader)) {
                return false;
            }
            return Objects.equals(this.url, that.url);
        }

    }
}
