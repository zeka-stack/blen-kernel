package dev.dong4j.zeka.kernel.spi.config;


import dev.dong4j.zeka.kernel.spi.utils.SpiStringUtils;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * 配置工具类，提供配置项获取的便捷方法
 * <p>
 * 该工具类封装了从环境配置中获取属性值的通用操作
 * 支持默认值处理和字符串去空白处理
 * <p>
 * 主要功能：
 * - 提供统一的配置获取接口
 * - 自动处理字符串去空白操作
 * - 支持默认值机制
 * - 集成Environment单例的配置能力
 * <p>
 * 使用@UtilityClass注解确保为工具类，不能实例化
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2021.02.26 17:47
 * @since 1.0.0
 */
@Slf4j
@UtilityClass
public class ConfigurationUtils {
    /**
     * 获取指定属性值
     * <p>
     * 从环境配置中获取指定键的属性值
     * 如果属性不存在则返回null
     *
     * @param property 属性键名
     * @return 属性值或null
     * @since 1.0.0
     */
    public static String getProperty(String property) {
        return getProperty(property, null);
    }

    /**
     * 获取指定属性值（带默认值）
     * <p>
     * 从环境配置中获取指定键的属性值，如果不存在则返回默认值
     * 返回的字符串会自动进行去空白处理
     *
     * @param property     属性键名
     * @param defaultValue 默认值
     * @return 属性值或默认值（已去空白）
     * @since 1.0.0
     */
    public static String getProperty(String property, String defaultValue) {
        return SpiStringUtils.trim(Environment.getInstance().getConfiguration().getString(property, defaultValue));
    }
}
