package dev.dong4j.zeka.kernel.common.config;

import dev.dong4j.zeka.kernel.common.constant.ConfigKey;
import java.util.Map;
import java.util.Properties;
import java.util.function.BiConsumer;
import org.jetbrains.annotations.Contract;

/**
 * 基于Properties的属性源实现，用于加载非Spring Boot默认配置文件的属性源
 * <p>
 * 该类主要用于处理不在application.yml或application.properties等Spring Boot默认配置文件中的配置项
 * 支持从java.util.Properties对象中加载配置，并按照框架的命名约定转换属性名
 * <p>
 * 主要特性：
 * - 支持从外部Properties对象加载配置
 * - 使用统一的配置前缀（ConfigKey.PREFIX）
 * - 支持驼峰命名转换，与Zeka框架的命名约定保持一致
 * - 低优先级（优先级为0），适合作为基本配置源
 * - 线程安全，支持并发访问
 * <p>
 * 使用场景：
 * - 加载第三方库的配置文件
 * - 加载遗留系统的.properties文件
 * - 加载运行时动态生成的配置
 * - 作为默认配置的基础层
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2019.12.26 21:43
 * @since 1.0.0
 */
public class PropertiesPropertySource implements PropertySource {

    /** 配置前缀，所有的属性名都会加上该前缀 */
    private static final String PREFIX = ConfigKey.PREFIX;

    /** 内部的Properties对象，存储所有的配置项 */
    private final Properties properties;

    /**
     * 构造一个基于Properties的属性源
     * <p>
     * 接收一个外部的Properties对象作为配置数据源
     * 该Properties对象中包含的所有键值对都会被作为配置项
     * <p>
     * 注意：使用@Contract(pure = true)注解表示该构造函数不会修改传入的properties对象
     *
     * @param properties 包含配置数据的Properties对象
     * @since 1.0.0
     */
    @Contract(pure = true)
    public PropertiesPropertySource(Properties properties) {
        this.properties = properties;
    }

    /**
     * 获取该属性源的优先级
     * <p>
     * 返回固定值0，表示该属性源具有最低的优先级
     * 通常作为基本配置源或默认配置源，会被其他高优先级的配置源覆盖
     *
     * @return 优先级值（固定为0）
     * @since 1.0.0
     */
    @Override
    public int getPriority() {
        return 0;
    }

    /**
     * 遍历所有属性并执行指定操作
     * <p>
     * 遍历Properties对象中的所有键值对，对每个键值对执行传入的action操作
     * 键和值都会被转换为字符串类型后传递给action函数
     * <p>
     * 该方法支持各种对配置项的批量处理操作，如验证、转换、导出等
     *
     * @param action 对每个键值对执行的操作函数
     * @since 1.0.0
     */
    @Override
    public void forEach(BiConsumer<String, String> action) {
        for (Map.Entry<Object, Object> entry : this.properties.entrySet()) {
            action.accept(((String) entry.getKey()), ((String) entry.getValue()));
        }
    }

    /**
     * 获取规范化后的属性名称
     * <p>
     * 将传入的属性名称标记列表转换为符合Zeka框架命名约定的属性名
     * 使用配置前缀 + 驼峰命名的格式，如：zeka.maxPoolSize
     * <p>
     * 转换示例：
     * - ["max", "pool", "size"] → "zeka.maxPoolSize"
     * - ["data", "source", "url"] → "zeka.dataSourceUrl"
     * - ["log", "level"] → "zeka.logLevel"
     *
     * @param tokens 属性名称标记列表
     * @return 规范化后的属性名称（带前缀的驼峰命名格式）
     * @since 1.0.0
     */
    @Override
    public CharSequence getNormalForm(Iterable<? extends CharSequence> tokens) {
        return PREFIX + Util.joinAsCamelCase(tokens);
    }
}
