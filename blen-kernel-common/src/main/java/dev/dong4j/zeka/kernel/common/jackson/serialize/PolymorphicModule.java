package dev.dong4j.zeka.kernel.common.jackson.serialize;

import com.fasterxml.jackson.core.json.PackageVersion;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleModule;
import dev.dong4j.zeka.kernel.common.jackson.IPolymorphic;
import dev.dong4j.zeka.processor.annotation.AutoService;

/**
 * 多态序列化Jackson模块，用于注册多态序列化和反序列化器
 * <p>
 * 该模块继承自Jackson的SimpleModule，通过@AutoService注解实现自动注册
 * 主要负责将多态序列化和反序列化器注册到Jackson的ObjectMapper中
 * <p>
 * 主要功能：
 * - 自动注册多态序列化器（PolymorphicSerialize）
 * - 自动注册多态反序列化器（PolymorphicDeserialize）
 * - 提供对IPolymorphic接口及其子类的统一序列化支持
 * - 通过SPI机制实现自动发现和加载
 * <p>
 * 使用方式：
 * - 通过@AutoService注解自动被 Jackson 发现和加载
 * - 无需手动注册，只要类路径中包含该模块即可自动生效
 * - 支持所有实现了IPolymorphic接口的对象
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2019.12.26 21:43
 * @since 1.0.0
 */
@AutoService(Module.class)
public class PolymorphicModule extends SimpleModule {

    /**
     * 构造多态序列化模块
     * <p>
     * 初始化模块并注册多态序列化和反序列化器
     * 使用Jackson的PackageVersion作为模块版本信息
     * <p>
     * 注册的组件：
     * - IPolymorphic.class → PolymorphicSerialize（序列化器）
     * - IPolymorphic.class → PolymorphicDeserialize（反序列化器）
     *
     * @since 1.0.0
     */
    public PolymorphicModule() {
        super(PackageVersion.VERSION);
        this.addSerializer(IPolymorphic.class, new PolymorphicSerialize());
        this.addDeserializer(IPolymorphic.class, new PolymorphicDeserialize());
    }
}
