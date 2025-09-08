package dev.dong4j.zeka.kernel.spi.config;

/**
 * 配置管理接口，提供统一的配置访问和类型转换功能
 * <p>
 * 该接口定义了配置项的获取、类型转换和默认值处理等核心功能
 * 支持多种数据类型的自动转换，包括基本类型、包装类型和枚举类型
 * <p>
 * 主要功能：
 * - 提供统一的配置访问接口
 * - 支持多种数据类型的自动转换
 * - 支持默认值机制
 * - 支持配置项存在性检查
 * <p>
 * 支持的数据类型：
 * - 字符串类型（String）
 * - 布尔类型（Boolean）
 * - 数值类型（Integer、Long、Float、Double等）
 * - 枚举类型（Enum）
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2021.02.26 17:47
 * @since 1.0.0
 */
public interface Configuration {
    /**
     * 获取字符串类型的配置值
     *
     * 从配置中获取指定键对应的字符串值
     * 如果配置不存在则返回null
     *
     * @param key 配置项的键
     * @return 配置值或null
     * @since 1.0.0
     */
    default String getString(String key) {
        return convert(String.class, key, null);
    }

    /**
     * 获取字符串类型的配置值（带默认值）
     *
     * 从配置中获取指定键对应的字符串值
     * 如果配置不存在则返回默认值
     *
     * @param key          配置项的键
     * @param defaultValue 默认值
     * @return 配置值或默认值
     * @since 1.0.0
     */
    default String getString(String key, String defaultValue) {
        return convert(String.class, key, defaultValue);
    }

    /**
     * 获取配置属性值
     *
     * 从配置中获取指定键对应的属性值
     * 如果配置不存在则返回null
     *
     * @param key 配置项的键
     * @return 属性值或null
     * @since 1.0.0
     */
    default Object getProperty(String key) {
        return getProperty(key, null);
    }

    /**
     * 获取配置属性值（带默认值）
     *
     * 从配置中获取指定键对应的属性值
     * 如果配置不存在则返回默认值
     *
     * @param key          配置项的键
     * @param defaultValue 默认值
     * @return 属性值或默认值
     * @since 1.0.0
     */
    default Object getProperty(String key, Object defaultValue) {
        Object value = getInternalProperty(key);
        return value != null ? value : defaultValue;
    }

    /**
     * 获取内部属性值
     *
     * 直接从内部存储中获取指定键的原始值
     * 该方法由具体实现类提供，不包含默认值处理
     *
     * @param key 配置项的键
     * @return 内部属性值
     * @since 1.0.0
     */
    Object getInternalProperty(String key);

    /**
     * 检查配置项是否存在
     *
     * 判断指定的配置键是否存在于配置中
     * 通过检查属性值是否为null来判断
     *
     * @param key 配置项的键
     * @return true-存在，false-不存在
     * @since 1.0.0
     */
    default boolean containsKey(String key) {
        return getProperty(key) != null;
    }


    /**
     * 类型转换方法
     *
     * 将指定键的配置值转换为目标类型
     * 支持基本类型、包装类型和枚举类型的自动转换
     * 如果转换失败或配置不存在则返回默认值
     *
     * @param <T>          目标类型
     * @param cls          目标类型的Class对象
     * @param key          配置项的键
     * @param defaultValue 默认值
     * @return 转换后的值或默认值
     * @since 1.0.0
     */
    default <T> T convert(Class<T> cls, String key, T defaultValue) {
        // we only process String properties for now
        String value = (String) getProperty(key);

        if (value == null) {
            return defaultValue;
        }

        Object obj = value;
        if (cls.isInstance(value)) {
            return cls.cast(value);
        }

        if (Boolean.class.equals(cls) || Boolean.TYPE.equals(cls)) {
            obj = Boolean.valueOf(value);
        } else if (Number.class.isAssignableFrom(cls) || cls.isPrimitive()) {
            if (Integer.class.equals(cls) || Integer.TYPE.equals(cls)) {
                obj = Integer.valueOf(value);
            } else if (Long.class.equals(cls) || Long.TYPE.equals(cls)) {
                obj = Long.valueOf(value);
            } else if (Byte.class.equals(cls) || Byte.TYPE.equals(cls)) {
                obj = Byte.valueOf(value);
            } else if (Short.class.equals(cls) || Short.TYPE.equals(cls)) {
                obj = Short.valueOf(value);
            } else if (Float.class.equals(cls) || Float.TYPE.equals(cls)) {
                obj = Float.valueOf(value);
            } else if (Double.class.equals(cls) || Double.TYPE.equals(cls)) {
                obj = Double.valueOf(value);
            }
        } else if (cls.isEnum()) {
            obj = Enum.valueOf(cls.asSubclass(Enum.class), value);
        }

        return cls.cast(obj);
    }


}
