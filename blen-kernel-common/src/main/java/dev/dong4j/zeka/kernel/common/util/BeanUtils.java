package dev.dong4j.zeka.kernel.common.util;

import com.google.common.collect.Maps;
import dev.dong4j.zeka.kernel.common.convert.CustomConverter;
import dev.dong4j.zeka.kernel.common.exception.LowestException;
import dev.dong4j.zeka.kernel.common.support.BaseBeanCopier;
import dev.dong4j.zeka.kernel.common.support.BeanProperty;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.cglib.beans.BeanGenerator;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.cglib.core.CodeGenerationException;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * <p>Description: Bean工具类，扩展了Spring BeanUtils的功能，提供强大的Java Bean操作功能</p>
 * <p>
 * 主要功能：
 * <ul>
 *     <li>对象实例化（通过类名或Class对象）</li>
 *     <li>属性访问（获取和设置 Bean 属性）</li>
 *     <li>对象深度复制（clone方法）</li>
 *     <li>单个对象复制（支持不同类型间转换）</li>
 *     <li>批量对象复制（集合元素类型转换）</li>
 *     <li>类型转换复制（支持自定义转换器）</li>
 *     <li>Map与Bean互转功能</li>
 *     <li>动态Bean创建和操作</li>
 * </ul>
 * </p>
 * <p>
 * 使用示例：
 * <pre>
 * // 实例化对象
 * MyClass instance = BeanUtils.newInstance(MyClass.class);
 *
 * // 属性访问
 * Object value = BeanUtils.getProperty(bean, "propertyName");
 * BeanUtils.setProperty(bean, "propertyName", newValue);
 *
 * // 对象复制
 * MyTargetClass target = BeanUtils.copy(source, MyTargetClass.class);
 *
 * // 批量对象复制
 * List<MyTargetClass> targets = BeanUtils.copy(sourceList, MyTargetClass.class);
 *
 * // Map与Bean转换
 * Map<String, Object> map = BeanUtils.toMap(bean);
 * MyClass bean = BeanUtils.toBean(map, MyClass.class);
 *
 * // 对象克隆
 * MyClass cloned = BeanUtils.clone(source);
 * </pre>
 * </p>
 * <p>
 * 技术特性：
 * <ul>
 *     <li>继承Spring的BeanUtils功能</li>
 *     <li>基于CGLib实现高性能的对象复制</li>
 *     <li>支持批量处理和类型转换</li>
 *     <li>提供自定义转换器支持</li>
 *     <li>支持Map与Bean互转</li>
 *     <li>支持动态Bean创建</li>
 *     <li>安全的类型转换机制</li>
 * </ul>
 * </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2019.12.11 18:47
 * @since 1.0.0
 */
@SuppressWarnings("all")
@UtilityClass
public class BeanUtils extends org.springframework.beans.BeanUtils {

    /**
     * 实例化对象
     *
     * @param <T>      泛型标记
     * @param clazzStr 类名
     * @return 对象实例
     * @since 1.0.0
     */
    @NotNull
    public static <T> T newInstance(String clazzStr) {
        try {
            Class<?> clazz = Class.forName(clazzStr);
            return newInstance(clazz);
        } catch (ClassNotFoundException e) {
            throw new LowestException(e);
        }
    }

    /**
     * 实例化对象
     *
     * @param <T>   泛型标记
     * @param clazz 类
     * @return 对象实例
     * @since 1.0.0
     */
    @NotNull
    public static <T> T newInstance(Class<?> clazz) {
        return (T) instantiateClass(clazz);
    }

    /**
     * 获取Bean的属性
     *
     * @param bean         Bean对象
     * @param propertyName 属性名
     * @return 属性值
     * @since 1.0.0
     */
    public static Object getProperty(Object bean, String propertyName) {
        Assert.notNull(bean, "bean Could not null");
        return BeanMap.create(bean).get(propertyName);
    }

    /**
     * 设置Bean属性
     *
     * @param bean         Bean对象
     * @param propertyName 属性名
     * @param value        属性值
     * @since 1.0.0
     */
    public static void setProperty(Object bean, String propertyName, Object value) {
        Assert.notNull(bean, "bean Could not null");
        BeanMap.create(bean).put(propertyName, value);
    }

    /**
     * 深复制对象
     * 注意: 不支持链式Bean
     *
     * @param <T>    泛型标记
     * @param source 源对象
     * @return 复制的对象
     * @since 1.0.0
     */
    public static <T> T clone(T source) {
        return (T) BeanUtils.copy(source, source.getClass());
    }

    /**
     * 复制对象属性到另一个对象,默认不使用Convert
     * 注意: 不支持链式Bean,链式用 copyProperties
     *
     * @param <T>    泛型标记
     * @param source 源对象
     * @param clazz  目标类
     * @return 目标对象
     * @since 1.0.0
     */
    @Contract("null, _ -> null")
    public static <T> T copy(Object source, Class<T> clazz) {
        if (source == null) {
            return null;
        }
        return BeanUtils.copy(source, source.getClass(), clazz);
    }

    /**
     * 复制对象属性,默认不使用Convert
     * <p>
     * 支持 map bean copy
     * </p>
     *
     * @param <T>         泛型标记
     * @param source      源对象
     * @param sourceClazz 源类型
     * @param targetClazz 转换成的类型
     * @return 目标对象
     * @since 1.0.0
     */
    @Contract("null, _, _ -> null")
    public static <T> T copy(Object source, Class<?> sourceClazz, Class<T> targetClazz) {
        BaseBeanCopier copier = BaseBeanCopier.create(sourceClazz, targetClazz, false);
        T to = newInstance(targetClazz);
        copier.copy(source, to, null);
        return to;
    }

    /**
     * 复制列表对象,默认不使用Convert
     * <p>
     * 支持 map bean copy
     * </p>
     *
     * @param <T>         泛型标记
     * @param sourceList  源列表
     * @param targetClazz 转换成的类型
     * @return 目标列表
     * @since 1.0.0
     */
    @Contract("null, _ -> !null")
    public static <T> List<T> copy(@Nullable Collection<?> sourceList, Class<T> targetClazz) {
        if (sourceList == null || sourceList.isEmpty()) {
            return Collections.emptyList();
        }
        List<T> outList = new ArrayList<>(sourceList.size());
        Class<?> sourceClazz = null;
        for (Object source : sourceList) {
            if (source == null) {
                continue;
            }
            if (sourceClazz == null) {
                sourceClazz = source.getClass();
            }
            T bean = BeanUtils.copy(source, sourceClazz, targetClazz);
            outList.add(bean);
        }
        return outList;
    }

    /**
     * 拷贝对象并对不同类型属性进行转换
     * <p>
     * 支持 map bean copy
     * </p>
     *
     * @param <T>         泛型标记
     * @param source      源对象
     * @param targetClazz 转换成的类
     * @return 目标对象
     * @since 1.0.0
     */
    @Contract("null, _ -> null")
    @Nullable
    public static <T> T copyWithConvert(@Nullable Object source, Class<T> targetClazz) {
        if (source == null) {
            return null;
        }
        return BeanUtils.copyWithConvert(source, source.getClass(), targetClazz);
    }

    /**
     * 拷贝对象并对不同类型属性进行转换
     * <p>
     * 支持 map bean copy
     * </p>
     *
     * @param <T>         泛型标记
     * @param source      源对象
     * @param sourceClazz 源类
     * @param targetClazz 转换成的类
     * @return 目标对象
     * @since 1.0.0
     */
    @Contract("null, _, _ -> null")
    @Nullable
    public static <T> T copyWithConvert(@Nullable Object source, Class<?> sourceClazz, Class<T> targetClazz) {
        if (source == null) {
            return null;
        }
        BaseBeanCopier copier = BaseBeanCopier.create(sourceClazz, targetClazz, true);
        T to = newInstance(targetClazz);
        copier.copy(source, to, new CustomConverter(sourceClazz, targetClazz));
        return to;
    }

    /**
     * 拷贝列表并对不同类型属性进行转换
     * <p>
     * 支持 map bean copy
     * </p>
     *
     * @param <T>         泛型标记
     * @param sourceList  源对象列表
     * @param targetClazz 转换成的类
     * @return 列表
     * @since 1.0.0
     */
    @Contract("null, _ -> !null")
    public static <T> List<T> copyWithConvert(@Nullable Collection<?> sourceList, Class<T> targetClazz) {
        if (sourceList == null || sourceList.isEmpty()) {
            return Collections.emptyList();
        }
        List<T> outList = new ArrayList<>(sourceList.size());
        Class<?> sourceClazz = null;
        for (Object source : sourceList) {
            if (source == null) {
                continue;
            }
            if (sourceClazz == null) {
                sourceClazz = source.getClass();
            }
            T bean = BeanUtils.copyWithConvert(source, sourceClazz, targetClazz);
            outList.add(bean);
        }
        return outList;
    }

    /**
     * 复制属性值到目标类
     * <p>注意: 源类和目标类不必匹配或继承自彼此, 只要属性匹配即可.
     * 源Bean暴露但目标Bean没有的任何Bean属性将被忽略.</p>
     * <p>这只是为了方便的方法. 对于更复杂的传输需求,</p>
     *
     * @param <T>         泛型标记
     * @param sourceList  源列表Bean
     * @param targetClazz 目标Bean类
     * @return 列表
     * @throws BeansException 如果复制失败
     * @since 1.0.0
     */
    @Contract("null, _ -> !null")
    public static <T> List<T> copyProperties(@Nullable Collection<?> sourceList, Class<T> targetClazz) {
        if (sourceList == null || sourceList.isEmpty()) {
            return Collections.emptyList();
        }
        List<T> outList = new ArrayList<>(sourceList.size());
        for (Object source : sourceList) {
            if (source == null) {
                continue;
            }
            T bean = BeanUtils.copyProperties(source, targetClazz);
            outList.add(bean);
        }
        return outList;
    }

    /**
     * 复制属性值到目标类
     * <p>注意: 源类和目标类不必匹配或继承自彼此, 只要属性匹配即可.
     * 源Bean暴露但目标Bean没有的任何Bean属性将被忽略.</p>
     * <p>这只是为了方便的方法. 对于更复杂的传输需求,</p>
     *
     * @param <T>         泛型标记
     * @param source      源Bean
     * @param targetClazz 目标Bean类
     * @return 目标对象
     * @throws BeansException 如果复制失败
     * @since 1.0.0
     */
    @Contract("null, _ -> null")
    @Nullable
    public static <T> T copyProperties(@Nullable Object source, Class<T> targetClazz) {
        if (source == null) {
            return null;
        }
        T to = newInstance(targetClazz);
        BeanUtils.copyProperties(source, to);
        return to;
    }

    /**
     * 将对象装成 map 形式.
     * 注意: 返回的 Map 不能使用 put 添加键值对, 因为 BeanMap 未做实现, 所以不会添加成功, 必须使用强转为 BeanMap 后才使用 put
     *
     * @param bean 源对象
     * @return Map映射
     * @since 1.0.0
     */
    @Contract("null -> new")
    @SuppressWarnings("unchecked")
    public static Map<String, Object> toMap(Object bean) {
        if (bean == null) {
            return Maps.newHashMapWithExpectedSize(0);
        }
        return BeanMap.create(bean);
    }

    /**
     * 将对象转换为字符串Map
     *
     * @param bean Bean对象
     * @return 字符串Map
     * @since 1.0.0
     */
    @Contract("null -> !null")
    public static Map<String, String> toStringMap(Object bean) {
        Map<String, Object> map = toMap(bean);

        Map<String, String> stringMap = Maps.newHashMapWithExpectedSize(map.size());
        map.forEach((k, v) -> {
            if (ObjectUtils.isNotNull(v)) {
                stringMap.put(k, String.valueOf(v));
            }
        });
        return stringMap;
    }

    /**
     * 将map 转为 bean
     *
     * @param <T>       泛型标记
     * @param beanMap   Map映射
     * @param valueType 对象类型
     * @return 目标对象
     * @since 1.0.0
     */
    public static <T> T toBean(Map<String, Object> beanMap, Class<T> valueType) {
        Objects.requireNonNull(beanMap, "beanMap Could not null");
        T bean = BeanUtils.newInstance(valueType);
        if (beanMap.isEmpty()) {
            return bean;
        }
        BeanMap.create(bean).putAll(beanMap);
        return bean;
    }

    /**
     * 给一个Bean添加字段
     *
     * @param superBean 父级Bean
     * @param props     新增属性
     * @return 新的Bean对象
     * @since 1.0.0
     */
    public static Object generator(@NotNull Object superBean, BeanProperty... props) {
        Class<?> superclass = superBean.getClass();
        Object genBean = generator(superclass, props);
        BeanUtils.copy(superBean, genBean);
        return genBean;
    }

    /**
     * 给一个class添加字段
     *
     * @param superclass 父级类
     * @param props      新增属性
     * @return 新的Bean对象
     * @since 1.0.0
     */
    public static Object generator(Class<?> superclass, @NotNull BeanProperty... props) {
        BeanGenerator generator = new BeanGenerator();
        generator.setSuperclass(superclass);
        generator.setUseCache(true);
        for (BeanProperty prop : props) {
            generator.addProperty(prop.name(), prop.type());
        }
        return generator.create();
    }

    /**
     * 拷贝对象
     * 注意: 不支持链式Bean,链式用 copyProperties
     *
     * @param source     源对象
     * @param targetBean 需要赋值的对象
     * @since 1.0.0
     */
    public static void copy(@NotNull Object source, @NotNull Object targetBean) {
        BaseBeanCopier copier = BaseBeanCopier
            .create(source.getClass(), targetBean.getClass(), false);

        copier.copy(source, targetBean, null);
    }

    /**
     * 获取 Bean 的所有 getter 方法
     *
     * @param type 类
     * @return PropertyDescriptor数组
     * @since 1.0.0
     */
    public static PropertyDescriptor[] getBeanGetters(Class<?> type) {
        return getPropertiesHelper(type, true, false);
    }

    /**
     * 获取属性帮助器
     *
     * @param type  类型
     * @param read  是否读取
     * @param write 是否写入
     * @return PropertyDescriptor数组
     * @since 1.0.0
     */
    private static PropertyDescriptor[] getPropertiesHelper(Class<?> type, boolean read, boolean write) {
        try {
            PropertyDescriptor[] all = BeanUtils.getPropertyDescriptors(type);
            if (read && write) {
                return all;
            } else {
                List<PropertyDescriptor> properties = new ArrayList<>(all.length);
                for (PropertyDescriptor pd : all) {
                    if (read && pd.getReadMethod() != null) {
                        properties.add(pd);
                    } else if (write && pd.getWriteMethod() != null) {
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
     * @param type 类
     * @return PropertyDescriptor数组
     * @since 1.0.0
     */
    public static PropertyDescriptor[] getBeanSetters(Class<?> type) {
        return getPropertiesHelper(type, false, true);
    }

}
