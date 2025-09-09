package dev.dong4j.zeka.kernel.common.util;

import cn.hutool.core.util.StrUtil;
import dev.dong4j.zeka.kernel.common.asserts.Assertions;
import dev.dong4j.zeka.kernel.common.exception.LowestException;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.BeansException;
import org.springframework.cglib.core.CodeGenerationException;
import org.springframework.core.convert.Property;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.util.Assert;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

/**
 * <p>Description: 反射工具类，扩展了Spring ReflectionUtils的功能，提供更强大的Java反射操作功能</p>
 * <p>
 * 主要功能：
 * <ul>
 *     <li>字段访问（获取字段、字段值）</li>
 *     <li>方法调用（获取方法、调用方法）</li>
 *     <li>注解处理（获取字段注解）</li>
 *     <li>泛型解析（获取泛型类型）</li>
 *     <li>属性描述符处理（获取Bean属性）</li>
 *     <li>类信息获取（获取字段映射）</li>
 *     <li>对象实例化（通过反射创建对象）</li>
 * </ul>
 * </p>
 * <p>
 * 使用示例：
 * <pre>
 * // 获取类的所有字段
 * List<Field> fields = ReflectionUtils.getFieldList(MyClass.class);
 *
 * // 获取字段上的注解
 * MyAnnotation annotation = ReflectionUtils.getAnnotation(MyClass.class, "fieldName", MyAnnotation.class);
 *
 * // 获取字段值
 * Object value = ReflectionUtils.getFieldValue(myObject, "fieldName");
 *
 * // 设置字段值
 * ReflectionUtils.setFieldValue(myObject, "fieldName", newValue);
 *
 * // 调用方法
 * Object result = ReflectionUtils.invokeMethod(myObject, "methodName", new Class[]{String.class}, new Object[]{"param"});
 *
 * // 获取泛型类型
 * Class<?> genericType = ReflectionUtils.getSuperClassGenericType(MyClass.class, 0);
 *
 * // 获取Bean的getter方法
 * PropertyDescriptor[] getters = ReflectionUtils.getBeanGetters(MyClass.class);
 * </pre>
 * </p>
 * <p>
 * 技术特性：
 * <ul>
 *     <li>继承Spring的ReflectionUtils功能</li>
 *     <li>提供字段缓存机制提升性能</li>
 *     <li>支持父类字段继承处理</li>
 *     <li>支持静态字段和瞬态字段过滤</li>
 *     <li>提供泛型类型解析功能</li>
 *     <li>支持方法名猜测（getter/setter）</li>
 *     <li>安全的反射操作（忽略访问修饰符）</li>
 * </ul>
 * </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.27 18:18
 * @since 1.0.0
 */
@Slf4j
@UtilityClass
public class ReflectionUtils extends org.springframework.util.ReflectionUtils {

    /** 类字段缓存 */
    private static final Map<Class<?>, List<Field>> CLASS_FIELD_CACHE = new ConcurrentHashMap<>();

    /** 基本类型包装类型映射 */
    private static final Map<Class<?>, Class<?>> PRIMITIVE_WRAPPER_TYPE_MAP = new IdentityHashMap<>(8);

    static {
        PRIMITIVE_WRAPPER_TYPE_MAP.put(Boolean.class, boolean.class);
        PRIMITIVE_WRAPPER_TYPE_MAP.put(Byte.class, byte.class);
        PRIMITIVE_WRAPPER_TYPE_MAP.put(Character.class, char.class);
        PRIMITIVE_WRAPPER_TYPE_MAP.put(Double.class, double.class);
        PRIMITIVE_WRAPPER_TYPE_MAP.put(Float.class, float.class);
        PRIMITIVE_WRAPPER_TYPE_MAP.put(Integer.class, int.class);
        PRIMITIVE_WRAPPER_TYPE_MAP.put(Long.class, long.class);
        PRIMITIVE_WRAPPER_TYPE_MAP.put(Short.class, short.class);
    }

    /**
     * 获取 Bean 的所有 getter 方法
     *
     * @param type 类类型
     * @return PropertyDescriptor数组
     * @since 1.0.0
     */
    public static PropertyDescriptor[] getBeanGetters(Class<?> type) {
        return getPropertiesHelper(type, true, false);
    }

    /**
     * 获取 Bean 的所有 PropertyDescriptor
     *
     * @param type  类类型
     * @param read  是否包含读方法
     * @param write 是否包含写方法
     * @return PropertyDescriptor数组
     * @since 1.0.0
     */
    public static PropertyDescriptor[] getPropertiesHelper(Class<?> type, boolean read, boolean write) {
        try {
            PropertyDescriptor[] all = BeanUtils.getPropertyDescriptors(type);
            if (read && write) {
                return all;
            } else {
                List<PropertyDescriptor> properties = new ArrayList<>(all.length);
                for (PropertyDescriptor pd : all) {
                    boolean canRead = read && pd.getReadMethod() != null;
                    boolean canWrite = write && pd.getWriteMethod() != null;
                    if (canRead || canWrite) {
                        properties.add(pd);
                    }
                }
                return properties.toArray(new PropertyDescriptor[0]);
            }
        } catch (BeansException ex) {
            throw new CodeGenerationException(ex);
        }
    }

    /**
     * 获取 Bean 的所有 setter 方法
     *
     * @param type 类类型
     * @return PropertyDescriptor数组
     * @since 1.0.0
     */
    public static PropertyDescriptor[] getBeanSetters(Class<?> type) {
        return getPropertiesHelper(type, false, true);
    }

    /**
     * 获取 bean 的类型描述符
     *
     * @param propertyType 类型
     * @param propertyName 属性名
     * @return 类型描述符
     * @since 1.0.0
     */
    @Nullable
    public static TypeDescriptor getTypeDescriptor(Class<?> propertyType, String propertyName) {
        Property property = ReflectionUtils.getProperty(propertyType, propertyName);
        if (property == null) {
            return null;
        }
        return new TypeDescriptor(property);
    }

    /**
     * 获取 bean 的属性信息
     *
     * @param propertyType 类型
     * @param propertyName 属性名
     * @return 属性对象
     * @since 1.0.0
     */
    @Nullable
    public static Property getProperty(Class<?> propertyType, String propertyName) {
        PropertyDescriptor propertyDescriptor = BeanUtils.getPropertyDescriptor(propertyType, propertyName);
        if (propertyDescriptor == null) {
            return null;
        }
        return ReflectionUtils.getProperty(propertyType, propertyDescriptor, propertyName);
    }

    /**
     * 获取 bean 的属性信息
     *
     * @param propertyType       类型
     * @param propertyDescriptor 属性描述符
     * @param propertyName       属性名
     * @return 属性对象
     * @since 1.0.0
     */
    @NotNull
    public static Property getProperty(Class<?> propertyType, @NotNull PropertyDescriptor propertyDescriptor, String propertyName) {
        Method readMethod = propertyDescriptor.getReadMethod();
        Method writeMethod = propertyDescriptor.getWriteMethod();
        return new Property(propertyType, readMethod, writeMethod, propertyName);
    }

    /**
     * 获取类属性信息
     *
     * @param propertyType       类型
     * @param propertyDescriptor 属性描述符
     * @param propertyName       属性名
     * @return 类型描述符
     * @since 1.0.0
     */
    @NotNull
    public static TypeDescriptor getTypeDescriptor(Class<?> propertyType,
                                                   @NotNull PropertyDescriptor propertyDescriptor,
                                                   String propertyName) {
        Method readMethod = propertyDescriptor.getReadMethod();
        Method writeMethod = propertyDescriptor.getWriteMethod();
        Property property = new Property(propertyType, readMethod, writeMethod, propertyName);
        return new TypeDescriptor(property);
    }

    /**
     * 获取所有字段属性上的注解
     *
     * @param <T>             注解泛型
     * @param clazz           类
     * @param fieldName       属性名
     * @param annotationClass 注解类型
     * @return 注解对象
     * @since 1.0.0
     */
    @Nullable
    public static <T extends Annotation> T getAnnotation(Class<?> clazz, String fieldName, Class<T> annotationClass) {
        Field field = ReflectionUtils.getField(clazz, fieldName);
        if (field == null) {
            return null;
        }
        return field.getAnnotation(annotationClass);
    }

    /**
     * 获取类属性
     *
     * @param clazz     类信息
     * @param fieldName 属性名
     * @return 字段对象
     * @since 1.0.0
     */
    @Nullable
    public static Field getField(Class<?> clazz, String fieldName) {
        while (clazz != Object.class) {
            try {
                return clazz.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            }
        }
        return null;
    }

    /**
     * 反射 method 方法名, 例如 getId
     *
     * @param field 字段
     * @param str   属性字符串内容
     * @return 方法名
     * @since 1.0.0
     * @deprecated 3.3.0 使用 {@link #guessGetterName(Field, String)} 替代
     */
    @Deprecated
    public static String getMethodCapitalize(@NotNull Field field, String str) {
        Class<?> fieldType = field.getType();
        return guessGetterName(str, fieldType);
    }

    /**
     * 反射 method 方法名, 例如 setVersion
     *
     * @param field 字段
     * @param str   JavaBean类的version属性名
     * @return version属性的setter方法名称 , e.g. setVersion
     * @since 1.0.0
     * @deprecated 3.0.8
     */
    @Deprecated
    public static String setMethodCapitalize(Field field, String str) {
        return concatCapitalize("set", str);
    }

    /**
     * 拼接字符串第二个字符串第一个字母大写
     *
     * @param concatStr 连接字符串
     * @param str       字符串
     * @return 拼接后的字符串
     * @since 1.0.0
     */
    private static String concatCapitalize(String concatStr, String str) {
        if (StringUtils.isBlank(concatStr)) {
            concatStr = StringPool.EMPTY;
        }
        if (str == null || str.isEmpty()) {
            return str;
        }

        char firstChar = str.charAt(0);
        if (Character.isTitleCase(firstChar)) {
            // already capitalized
            return str;
        }
        return concatStr + Character.toTitleCase(firstChar) + str.substring(1);
    }

    /**
     * <p>
     * 获取 public get方法的值
     * </p>
     *
     * @param cls    类
     * @param entity 实体对象
     * @param str    属性字符串内容
     * @return 方法值
     * @since 1.0.0
     */
    public static Object getMethodValue(Class<?> cls, Object entity, String str) {
        Map<String, Field> fieldMaps = getFieldMap(cls);
        try {
            Assert.notEmpty(fieldMaps, StringUtils.format("Error: NoSuchField in {} for {}.  Cause:", cls.getSimpleName(), str));
            Method method = cls.getMethod(guessGetterName(fieldMaps.get(str), str));
            return method.invoke(entity);
        } catch (NoSuchMethodException e) {
            throw new LowestException("Error: NoSuchMethod in %s.  Cause:", e, cls.getSimpleName());
        } catch (IllegalAccessException e) {
            throw new LowestException("Error: Cannot execute a private method. in %s.  Cause:", e, cls.getSimpleName());
        } catch (InvocationTargetException e) {
            throw new LowestException("Error: InvocationTargetException on getMethodValue.  Cause:" + e);
        }
    }

    /**
     * 猜测方法名
     *
     * @param field 字段
     * @param str   属性字符串内容
     * @return 方法名
     * @since 1.0.0
     */
    private static String guessGetterName(@NotNull Field field, String str) {
        return guessGetterName(str, field.getType());
    }

    /**
     * 猜测方法属性对应的 Getter 名称, 具体规则请参考 JavaBeans 规范
     *
     * @param name 属性名称
     * @param type 属性类型
     * @return 返回猜测的名称
     * @since 1.0.0
     */
    private static String guessGetterName(String name, Class<?> type) {
        return boolean.class == type ? name.startsWith("is") ? name : "is" + StrUtil.upperFirst(name) : "get" + StrUtil.upperFirst(name);
    }

    /**
     * 获取 public get方法的值
     *
     * @param entity 实体对象
     * @param str    属性字符串内容
     * @return 方法值
     * @since 1.0.0
     */
    @Contract("null, _ -> null")
    public static Object getMethodValue(Object entity, String str) {
        if (null == entity) {
            return null;
        }
        return getMethodValue(entity.getClass(), entity, str);
    }

    /**
     * 反射对象获取泛型
     *
     * @param clazz 类对象
     * @param index 泛型所在位置
     * @return 泛型类
     * @since 1.0.0
     */
    public static Class<?> getSuperClassGenericType(@NotNull Class<?> clazz, int index) {
        Type genType = clazz.getGenericSuperclass();
        if (!(genType instanceof ParameterizedType)) {
            log.warn("Warn: [{}] superclass not ParameterizedType", clazz.getSimpleName());
            return Object.class;
        }
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        if (index >= params.length || index < 0) {
            log.warn("Warn: Index: [{}], Size of [{}] Parameterized Type: [{}] .", index, clazz.getSimpleName(), params.length);
            return Object.class;
        }
        if (!(params[index] instanceof Class)) {
            log.warn("Warn: [{}] not set the actual class on superclass generic parameter", clazz.getSimpleName());
            return Object.class;
        }
        return (Class<?>) params[index];
    }

    /**
     * 获取该类的所有属性列表
     *
     * @param clazz 反射类
     * @return 字段映射
     * @since 1.0.0
     */
    public static Map<String, Field> getFieldMap(Class<?> clazz) {
        List<Field> fieldList = getFieldList(clazz);
        return CollectionUtils.isNotEmpty(fieldList) ? fieldList.stream()
            .collect(Collectors.toMap(Field::getName, field -> field)) : Collections.emptyMap();
    }

    /**
     * 获取该类的所有属性列表
     *
     * @param clazz 反射类
     * @return 字段列表
     * @since 1.0.0
     */
    public static List<Field> getFieldList(Class<?> clazz) {
        if (Objects.isNull(clazz)) {
            return Collections.emptyList();
        }
        List<Field> fields = CLASS_FIELD_CACHE.get(clazz);
        if (CollectionUtils.isEmpty(fields)) {
            synchronized (CLASS_FIELD_CACHE) {
                fields = doGetFieldList(clazz);
                CLASS_FIELD_CACHE.put(clazz, fields);
            }
        }
        return fields;
    }

    /**
     * 获取该类的所有属性列表
     *
     * @param clazz 反射类
     * @return 字段列表
     * @since 1.0.0
     */
    public static @NotNull List<Field> doGetFieldList(@NotNull Class<?> clazz) {
        if (clazz.getSuperclass() != null) {
            // 排除重载属性
            Map<String, Field> fieldMap = excludeOverrideSuperField(clazz.getDeclaredFields(),
                // 处理父类字段
                getFieldList(clazz.getSuperclass()));
            List<Field> fieldList = new ArrayList<>();
            /*
             * 重写父类属性过滤后处理忽略部分, 支持过滤父类属性功能
             * 场景: 中间表不需要记录创建时间, 忽略父类 createTime 公共属性
             * 中间表实体重写父类属性 ` private transient Date createTime; `
             */
            fieldMap.forEach((k, v) -> {
                // 过滤静态属性
                if (!Modifier.isStatic(v.getModifiers())
                    // 过滤 transient关键字修饰的属性
                    && !Modifier.isTransient(v.getModifiers())) {
                    fieldList.add(v);
                }
            });
            return fieldList;
        } else {
            return Collections.emptyList();
        }
    }

    /**
     * 排序重置父类属性
     *
     * @param fields         子类属性
     * @param superFieldList 父类属性列表
     * @return 字段映射
     * @since 1.0.0
     */
    public static Map<String, Field> excludeOverrideSuperField(Field[] fields, @NotNull List<Field> superFieldList) {
        // 子类属性
        Map<String, Field> fieldMap = Stream.of(fields)
            .collect(toMap(Field::getName, identity(), (u, v) -> {
                throw new IllegalStateException(String.format("Duplicate key %s", u));
            }, LinkedHashMap::new));
        superFieldList.stream()
            .filter(field -> !fieldMap.containsKey(field.getName()))
            .forEach(f -> fieldMap.put(f.getName(), f));
        return fieldMap;
    }

    /**
     * 获取字段get方法
     *
     * @param cls   类
     * @param field 字段
     * @return Get方法
     * @since 1.0.0
     */
    public static @NotNull Method getMethod(@NotNull Class<?> cls, Field field) {
        try {
            return cls.getDeclaredMethod(guessGetterName(field, field.getName()));
        } catch (NoSuchMethodException e) {
            throw new LowestException("Error: NoSuchMethod in %s.  Cause:", e, cls.getName());
        }
    }

    /**
     * 判断是否为基本类型或基本包装类型
     *
     * @param clazz 类
     * @return 是否基本类型或基本包装类型
     * @since 1.0.0
     */
    public static boolean isPrimitiveOrWrapper(Class<?> clazz) {
        Assertions.notNull(clazz, "Class must not be null");
        return (clazz.isPrimitive() || PRIMITIVE_WRAPPER_TYPE_MAP.containsKey(clazz));
    }

    /**
     * 循环向上转型, 获取对象的 DeclaredMethod
     *
     * @param object         子类对象
     * @param methodName     父类中的方法名
     * @param parameterTypes 父类中的方法参数类型
     * @return 父类中的方法对象
     * @since 1.0.0
     */
    public static @Nullable Method getDeclaredMethod(@NotNull Object object, String methodName, Class<?>... parameterTypes) {
        Method method;
        for (Class<?> clazz = object.getClass(); clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                method = clazz.getDeclaredMethod(methodName, parameterTypes);
                return method;
            } catch (Exception e) {
                // 这里甚么都不要做！并且这里的异常必须这样写, 不能抛出去.
                // 如果这里的异常打印或者往外抛, 则就不会执行clazz=clazz.getSuperclass(),最后就不会进入到父类中了
            }
        }
        return null;
    }

    /**
     * 直接调用对象方法, 而忽略修饰符(private, protected, default)
     *
     * @param object         子类对象
     * @param methodName     父类中的方法名
     * @param parameterTypes 父类中的方法参数类型
     * @param parameters     父类中的方法参数
     * @return 父类中方法的执行结果
     * @since 1.0.0
     */
    public static @Nullable Object invokeMethod(Object object,
                                                String methodName,
                                                Class<?>[] parameterTypes,
                                                Object[] parameters) {
        // 根据 对象、方法名和对应的方法参数 通过反射 调用上面的方法获取 Method 对象
        Method method = getDeclaredMethod(object, methodName, parameterTypes);
        try {
            if (null != method) {
                // 抑制Java对方法进行检查,主要是针对私有方法而言
                method.setAccessible(true);
                // 调用object 的 method 所代表的方法, 其方法的参数是 parameters
                return method.invoke(object, parameters);
            }
        } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
            throw Exceptions.unchecked(e);
        }
        return null;
    }

    /**
     * 循环向上转型, 获取对象的 DeclaredField
     *
     * @param object    子类对象
     * @param fieldName 父类中的属性名
     * @return 父类中的属性对象
     * @since 1.0.0
     */
    public static @Nullable Field getDeclaredField(@NotNull Object object, String fieldName) {
        Field field;
        Class<?> clazz = object.getClass();
        for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                field = clazz.getDeclaredField(fieldName);
                return field;
            } catch (Exception e) {
                // 这里甚么都不要做！并且这里的异常必须这样写, 不能抛出去.
                // 如果这里的异常打印或者往外抛, 则就不会执行clazz = clazz.getSuperclass(),最后就不会进入到父类中了
            }
        }
        return null;
    }

    /**
     * 直接设置对象属性值, 忽略 private/protected 修饰符, 也不经过 setter
     *
     * @param object    子类对象
     * @param fieldName 父类中的属性名
     * @param value     将要设置的值
     * @since 1.0.0
     */
    public static void setFieldValue(Object object, String fieldName, Object value) {
        // 根据 对象和属性名通过反射 调用上面的方法获取 Field对象
        Field field = getDeclaredField(object, fieldName);
        try {
            if (field != null) {
                // 抑制Java对其的检查
                field.setAccessible(true);
                // 将 object 中 field 所代表的值 设置为 value
                field.set(object, value);
            }
        } catch (IllegalArgumentException | IllegalAccessException e) {
            throw Exceptions.unchecked(e);
        }

    }

    /**
     * 直接读取对象的属性值, 忽略 private/protected 修饰符, 也不经过 getter
     *
     * @param object    子类对象
     * @param fieldName 父类中的属性名
     * @return 父类中的属性值
     * @since 1.0.0
     */
    public static @Nullable Object getFieldValue(Object object, String fieldName) {

        // 根据 对象和属性名通过反射 调用上面的方法获取 Field对象
        Field field = getDeclaredField(object, fieldName);

        try {
            if (field != null) {
                // 抑制Java对其的检查
                field.setAccessible(true);
                // 获取 object 中 field 所代表的属性值
                return field.get(object);
            }
        } catch (Exception e) {
            throw Exceptions.unchecked(e);
        }
        return null;
    }

    /**
     * 直接读取对象的属性值, 忽略 private/protected 修饰符, 也不经过 getter
     *
     * @param <T>       泛型类型
     * @param object    对象
     * @param fieldName 字段名
     * @param type      类型
     * @return 字段值
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    public static @Nullable <T> T getFieldValue(Object object, String fieldName, Class<T> type) {
        Object fieldValue = getFieldValue(object, fieldName);
        return fieldValue != null ? (T) fieldValue : null;
    }
}
