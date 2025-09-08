package dev.dong4j.zeka.kernel.common.config;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

/**
 * 属性源接口，定义了配置属性的统一访问和管理规范
 * <p>
 * 该接口抽象了配置属性的来源，支持多种不同的配置源（如文件、环境变量、系统属性等）
 * 提供了优先级机制、属性遍历、名称规范化等核心功能，是配置管理系统的基础抽象
 * <p>
 * 主要特性：
 * - 支持优先级排序，高优先级的配置会覆盖低优先级的配置
 * - 提供属性遍历机制，支持批量处理配置项
 * - 支持属性名称规范化，统一不同格式的属性名
 * - 内置工具类提供属性名解析和转换功能
 * - 支持Log4j2风格的属性名解析
 * <p>
 * 设计模式：
 * - 策略模式：不同的实现类提供不同的配置加载策略
 * - 模板方法：定义了配置处理的标准流程
 * - 工具类模式：提供静态工具方法处理通用操作
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2019.12.26 21:43
 * @since 1.0.0
 */
public interface PropertySource {

    /**
     * 返回此属性源的优先级顺序
     * <p>
     * 优先级决定了多个属性源之间的覆盖关系，数值越高表示优先级越高
     * 高优先级的属性源会在配置加载时后处理，从而覆盖低优先级源的同名属性
     * <p>
     * 优先级使用场景：
     * - 系统属性 > 环境变量 > 配置文件
     * - 用户配置 > 默认配置
     * - 命令行参数 > 配置文件参数
     *
     * @return 优先级数值，越大优先级越高
     * @since 1.0.0
     */
    int getPriority();

    /**
     * 遍历所有属性并对每个键值对执行指定操作
     * <p>
     * 该方法提供了对属性源中所有配置项的批量访问能力
     * 通过传入的BiConsumer函数式接口，可以对每个配置项进行自定义处理
     * <p>
     * 常见使用场景：
     * - 配置项验证和检查
     * - 配置项格式转换
     * - 配置项统计和分析
     * - 配置项导出和备份
     *
     * @param action 对每个键值对执行的操作函数
     * @since 1.0.0
     */
    void forEach(BiConsumer<String, String> action);

    /**
     * 将属性名称标记列表转换为规范化形式
     * <p>
     * 该方法负责将解析后的属性名称标记重新组合成符合当前属性源规范的完整属性名
     * 不同的属性源可能有不同的命名约定（如驼峰命名、下划线分隔、点分隔等）
     * <p>
     * 转换示例：
     * - ["log", "level", "root"] → "log.level.root"
     * - ["max", "pool", "size"] → "maxPoolSize"
     * - ["data", "source", "url"] → "data_source_url"
     *
     * @param tokens 属性名称标记列表
     * @return 规范化后的属性名称
     * @since 1.0.0
     */
    CharSequence getNormalForm(Iterable<? extends CharSequence> tokens);

    /**
     * 属性源优先级比较器，用于对多个属性源实例按优先级进行排序
     * <p>
     * 该比较器实现了标准的Comparator接口，支持对PropertySource实例的排序操作
     * 排序规则基于getPriority()方法返回的优先级数值，数值越小排在前面
     * <p>
     * 使用场景：
     * - 配置加载时对多个属性源进行排序
     * - 确保配置覆盖的正确顺序
     * - 集合框架中的自动排序支持
     * <p>
     * 注意：该类实现了Serializable接口，支持序列化操作
     *
     * @author dong4j
     * @version 1.0.0
     * @email "mailto:dong4j@gmail.com"
     * @date 2019.12.26 21:43
     * @since 1.0.0
     */
    class Comparator implements java.util.Comparator<PropertySource>, Serializable {
        /** 序列化版本号，用于控制序列化兼容性 */
        @Serial
        private static final long serialVersionUID = 1L;

        /**
         * 比较两个属性源的优先级
         * <p>
         * 根据两个属性源的优先级数值进行比较，返回比较结果
         * 该方法确保优先级低的属性源排在前面，优先级高的排在后面
         *
         * @param o1 第一个属性源
         * @param o2 第二个属性源
         * @return 比较结果：负数表示o1优先级低于o2，0表示相等，正数表示o1优先级高于o2
         * @since 1.0.0
         */
        @Override
        public int compare(PropertySource o1, PropertySource o2) {
            return Integer.compare(Objects.requireNonNull(o1).getPriority(), Objects.requireNonNull(o2).getPriority());
        }
    }

    /**
     * 属性源工具类，提供属性名称解析和转换的通用功能
     * <p>
     * 该工具类专门用于处理各种格式的属性名称，支持Log4j2风格的属性命名约定
     * 提供了属性名称的标记化解析和驼峰命名转换功能，并内置缓存机制提高性能
     * <p>
     * 主要功能：
     * - 支持Log4j2前缀的自动识别和剔除
     * - 支持多种分隔符（连字符、点、下划线、斜线）
     * - 支持驼峰命名约定的自动识别
     * - 内置缓存机制，提高重复解析的性能
     * - 线程安全的并发访问支持
     * <p>
     * 支持的属性名称格式：
     * - log4j2.rootLogger.level
     * - LOG4J2_ROOT_LOGGER_LEVEL  
     * - log4j-root-logger-level
     * - rootLoggerLevel（驼峰命名）
     * - org.apache.logging.log4j.rootLogger.level
     *
     * @author dong4j
     * @version 1.0.0
     * @email "mailto:dong4j@gmail.com"
     * @date 2019.12.26 21:43
     * @since 1.0.0
     */
    final class Util {
        /** Log4j2前缀正则表达式，用于识别和剔除常见的Log4j2相关前缀 */
        private static final String PREFIXES = "(?i:^log4j2?[-._/]?|^org\\.apache\\.logging\\.log4j\\.)?";
        /** 属性名称标记化正则表达式，用于将属性名分解成一个个标记 */
        private static final Pattern PROPERTY_TOKENIZER = Pattern.compile(PREFIXES + "([A-Z]*[a-z0-9]+|[A-Z0-9]+)[-._/]?");
        /** 标记解析结果缓存，用于提高重复解析的性能 */
        private static final Map<CharSequence, List<CharSequence>> CACHE = new ConcurrentHashMap<>();

        /**
         * 将属性名称字符串转换为标记列表
         * <p>
         * 该方法会自动剔除Log4j2相关的前缀（如log4j、log4j2、Log4j或org.apache.logging.log4j）
         * 同时处理各种分隔符（连字符-、点.、下划线_和斜线/）
         * 还支持驼峰命名约定，无需显式分隔符即可识别单词边界
         * <p>
         * 解析示例：
         * - "log4j2.rootLogger.level" → ["root", "logger", "level"]
         * - "LOG4J_ROOT_LOGGER_LEVEL" → ["root", "logger", "level"]
         * - "rootLoggerLevel" → ["root", "logger", "level"]
         * - "org.apache.logging.log4j.async.bufferSize" → ["async", "buffer", "size"]
         * <p>
         * 性能优化：该方法内置缓存机制，相同的输入只会解析一次
         *
         * @param value 原始属性名称
         * @return 解析后的小写标记列表
         * @since 1.0.0
         */
        public static List<CharSequence> tokenize(CharSequence value) {
            if (CACHE.containsKey(value)) {
                return CACHE.get(value);
            }
            List<CharSequence> tokens = new ArrayList<>();
            Matcher matcher = PROPERTY_TOKENIZER.matcher(value);
            while (matcher.find()) {
                tokens.add(matcher.group(1).toLowerCase());
            }
            CACHE.put(value, tokens);
            return tokens;
        }

        /**
         * 使用驼峰命名约定连接字符串列表
         * <p>
         * 该方法将多个字符串标记按照Java的驼峰命名约定连接成一个完整的属性名
         * 第一个标记保持全小写，后续标记的首字母大写，其余字母小写
         * <p>
         * 转换示例：
         * - ["root", "logger", "level"] → "rootLoggerLevel"
         * - ["max", "pool", "size"] → "maxPoolSize"
         * - ["data", "source", "url"] → "dataSourceUrl"
         * - ["async", "buffer", "size"] → "asyncBufferSize"
         * <p>
         * 该方法返回不可变的CharSequence，保证线程安全和内存效率
         *
         * @param tokens 要转换的字符串标记列表
         * @return 驼峰命名格式的字符序列
         * @since 1.0.0
         */
        public static @NotNull @Unmodifiable CharSequence joinAsCamelCase(@NotNull Iterable<? extends CharSequence> tokens) {
            StringBuilder sb = new StringBuilder();
            boolean first = true;
            for (CharSequence token : tokens) {
                if (first) {
                    sb.append(token);
                } else {
                    sb.append(Character.toUpperCase(token.charAt(0)));
                    if (token.length() > 1) {
                        sb.append(token.subSequence(1, token.length()));
                    }
                }
                first = false;
            }
            return sb.toString();
        }

        /**
         * 私有构造函数，防止实例化
         * <p>
         * 该构造函数使用@Contract(pure = true)注解标记为纯函数
         * 表明该构造函数不会产生任何副作用，也不会修改任何状态
         * <p>
         * 作为工具类，所有方法都是静态的，不需要创建实例
         *
         * @since 1.0.0
         */
        @Contract(pure = true)
        private Util() {
        }
    }
}
