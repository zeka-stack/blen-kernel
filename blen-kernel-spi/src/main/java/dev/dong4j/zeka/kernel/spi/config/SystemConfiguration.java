package dev.dong4j.zeka.kernel.spi.config;


/**
 * 系统属性配置类，提供对Java系统属性的访问能力
 * <p>
 * 该类继承自AbstractPrefixConfiguration，支持带前缀的配置访问
 * 内部通过System.getProperty()方法获取JVM系统属性
 * <p>
 * 主要功能：
 * - 访问JVM系统属性（如java.version、os.name等）
 * - 支持配置键名前缀过滤
 * - 提供统一的配置访问接口
 * - 支持多实例模式（不同前缀）
 * <p>
 * 使用场景：需要访问系统属性或环境变量的配置场景
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2021.02.26 17:47
 * @since 1.0.0
 */
public class SystemConfiguration extends AbstractPrefixConfiguration {

    /**
     * 构造一个带前缀和ID的系统配置实例
     * <p>
     * 适用于需要对特定前缀的系统属性进行访问的场景
     * 前缀可以用于过滤或分组管理配置项
     *
     * @param prefix 配置键名前缀
     * @param id     配置实例标识
     * @since 1.0.0
     */
    public SystemConfiguration(String prefix, String id) {
        super(prefix, id);
    }

    /**
     * 构造一个默认的系统配置实例
     * <p>
     * 使用空前缀和ID，可以访问所有系统属性
     * 适用于通用系统属性访问场景
     *
     * @since 1.0.0
     */
    public SystemConfiguration() {
        this(null, null);
    }

    /**
     * 获取内部属性值
     * <p>
     * 重写父类的抽象方法，实际调用System.getProperty()获取JVM系统属性
     * 这些属性包括系统环境信息、JVM参数等
     *
     * @param key 属性键名
     * @return 属性值或null（如果不存在）
     * @since 1.0.0
     */
    @Override
    public Object getInternalProperty(String key) {
        return System.getProperty(key);
    }

}
