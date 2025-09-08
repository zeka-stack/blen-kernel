package dev.dong4j.zeka.kernel.spi.extension;

/**
 * 扩展工厂接口，定义扩展实例的创建和获取标准
 * <p>
 * 该接口作为SPI扩展点，允许不同的实现提供不同的扩展创建策略
 * 可以用于实现复杂的扩展对象生命周期管理和依赖注入
 * <p>
 * 主要功能：
 * - 提供统一的扩展实例获取接口
 * - 支持按类型和名称查找扩展
 * - 允许定制化的扩展创建逻辑
 * - 支持扩展对象的统一管理
 * <p>
 * 使用场景：需要定制扩展实例创建逻辑或集成第三方框架的情况
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2021.02.26 17:47
 * @since 1.0.0
 */
@SPI
public interface ExtensionFactory {

    /**
     * 获取指定类型和名称的扩展实例
     * <p>
     * 根据给定的类型和名称查找并返回对应的扩展实例
     * 实现类可以根据需要提供不同的查找和创建策略
     *
     * @param <T>  扩展实例的类型
     * @param type 扩展接口的Class对象
     * @param name 扩展实现的名称
     * @return 对应的扩展实例，如果找不到可能返回null
     * @since 1.0.0
     */
    <T> T getExtension(Class<T> type, String name);

}
