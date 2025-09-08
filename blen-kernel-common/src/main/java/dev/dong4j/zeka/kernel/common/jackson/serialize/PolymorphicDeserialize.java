package dev.dong4j.zeka.kernel.common.jackson.serialize;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.TextNode;
import dev.dong4j.zeka.kernel.common.enums.SerializeEnum;
import dev.dong4j.zeka.kernel.common.jackson.IPolymorphic;
import dev.dong4j.zeka.kernel.common.jackson.Polymorphic;
import dev.dong4j.zeka.kernel.common.util.StringUtils;
import jakarta.validation.Payload;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;

/**
 * 多态对象反序列化器，用于将JSON数据反序列化为具体的多态对象
 * <p>
 * 该反序列化器实现了Jackson的上下文相关反序列化机制，能够根据 JSON 中的类型标识字段
 * 动态确定并创建对应的具体类型实例。支持两种多态识别机制：基于枚举和基于配置的类型映射
 * <p>
 * 主要特性：
 * - 继承自StdDeserializer，提供标准的Jackson反序列化能力
 * - 实现ContextualDeserializer接口，支持上下文相关的反序列化
 * - 支持基于SerializeEnum枚举的类型映射
 * - 支持基于@Polymorphic.Type注解的类型映射
 * - 自动验证目标类型是否实现IPolymorphic接口
 * - 提供详细的日志记录和错误处理
 * <p>
 * 反序列化流程：
 * 1. 从字段上获取@Polymorphic注解配置
 * 2. 从 JSON 中提取类型标识字段的值
 * 3. 根据枚举或配置映射确定对应的Java类型
 * 4. 验证目标类型是否实现IPolymorphic接口
 * 5. 使用确定的类型进行 JSON 反序列化
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2019.12.26 21:43
 * @since 1.0.0
 */
@Slf4j
public class PolymorphicDeserialize extends StdDeserializer<IPolymorphic> implements ContextualDeserializer {

    /** 当前反序列化上下文中的字段属性信息，用于获取@Polymorphic注解配置 */
    private BeanProperty property;

    /**
     * 默认构造函数
     * <p>
     * 创建一个基本的多态反序列化器实例，不包含任何上下文信息
     * 通常用于初始化，具体的上下文信息会在createContextual方法中设置
     *
     * @since 1.0.0
     */
    public PolymorphicDeserialize() {
        this(null);
    }

    /**
     * 复制构造函数，用于创建包含上下文信息的反序列化器实例
     * <p>
     * 此构造函数由createContextual方法调用，用于传递当前字段的属性信息
     * 这些信息包含了@Polymorphic注解的配置，是进行多态反序列化的关键依据
     *
     * @param src  源反序列化器实例
     * @param prop 字段属性信息，包含@Polymorphic注解配置
     * @since 1.0.0
     */
    public PolymorphicDeserialize(PolymorphicDeserialize src, BeanProperty prop) {
        super(src);
        this.property = prop;
    }


    /**
     * 基类型构造函数（受保护）
     * <p>
     * 继承自父类的构造函数，用于初始化序列化的目标类型
     * 此构造函数主要用于满足Jackson框架的要求
     *
     * @param t 目标类型的Class对象
     * @since 1.0.0
     */
    protected PolymorphicDeserialize(Class<Payload> t) {
        super(t);
    }

    /**
     * 执行多态对象的反序列化操作
     * <p>
     * 该方法是多态反序列化的核心逻辑，负责从JSON数据中解析出类型标识
     * 并根据配置的映射关系找到对应的具体类型，最终创建出对应的对象实例
     * <p>
     * 反序列化步骤：
     * 1. 从字段上获取@Polymorphic注解
     * 2. 从 JSON 中提取类型标识字段的值
     * 3. 根据枚举或Type映射找到对应的Java类型
     * 4. 验证类型是否实现IPolymorphic接口
     * 5. 执行具体的反序列化操作
     *
     * @param p    JSON解析器，用于读取JSON数据
     * @param ctxt 反序列化上下文，提供类型构建和配置信息
     * @return 反序列化后的多态对象实例，如果解析失败则返回null
     * @throws IOException 如果 JSON 解析过程中出现I/O错误
     * @since 1.0.0
     */
    @Override
    @SuppressWarnings("all")
    public IPolymorphic deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        ObjectCodec codec = p.getCodec();
        TreeNode treeNode = codec.readTree(p);
        Polymorphic polymorphic = this.property.getAnnotation(Polymorphic.class);
        if (polymorphic == null) {
            return null;
        }
        String value = polymorphic.value();
        TreeNode identifierNode = treeNode.get(value);
        if (identifierNode == null) {
            return null;
        }
        String identifier = ((TextNode) identifierNode).asText();
        if (StringUtils.isEmpty(identifier)) {
            return null;
        }
        Class<? extends SerializeEnum> enumClass = polymorphic.enumClass();
        if (enumClass != SerializeEnum.class) {
            SerializeEnum[] enumConstants = enumClass.getEnumConstants();
            if (enumConstants == null || enumConstants.length == 0) {
                return null;
            }
            SerializeEnum serializeEnum = Arrays.stream(enumConstants).filter(e -> identifier.equals(e.getDesc())).findFirst().orElse(null);
            if (serializeEnum == null) {
                log.debug("多态反序列化未找到实现类:{}", identifier);
                return null;
            }
            Serializable enumValue = serializeEnum.getValue();
            JavaType javaType = ctxt.constructType((Class<?>) enumValue);
            if (javaType.isTypeOrSubTypeOf(IPolymorphic.class)) {
                JsonParser traverse = treeNode.traverse();
                traverse.setCodec(codec);
                return codec.readValue(traverse, javaType);
            }
        } else {
            Polymorphic.Type[] types = polymorphic.types();
            if (types == null || types.length == 0) {
                return null;
            }
            Polymorphic.Type typeIdentifier = Arrays.stream(types).filter(type -> identifier.equals(type.value())).findFirst().orElse(null);
            if (typeIdentifier == null) {
                log.debug("多态反序列化未找到实现类:{}", identifier);
                return null;
            }
            Class<?> clz = typeIdentifier.clz();
            JavaType javaType = ctxt.constructType(clz);
            if (javaType.isTypeOrSubTypeOf(IPolymorphic.class)) {
                JsonParser traverse = treeNode.traverse();
                traverse.setCodec(codec);
                return codec.readValue(traverse, javaType);
            }
        }
        return null;
    }

    /**
     * 创建上下文相关的反序列化器实例
     * <p>
     * 该方法实现了ContextualDeserializer接口，允许反序列化器根据当前的上下文信息
     * （特别是字段属性信息）创建一个新的反序列化器实例。这使得不同的字段可以有不同的配置
     * <p>
     * 这个机制允许：
     * - 每个字段都可以有独立的@Polymorphic注解配置
     * - 反序列化器可以获取到当前字段的元数据信息
     * - 实现更精细的多态处理逻辑
     *
     * @param ctxt     反序列化上下文
     * @param property 当前字段的属性信息，包含注解和类型信息
     * @return 新的上下文相关反序列化器实例
     * @throws JsonMappingException 如果创建过程中出现映射错误
     * @since 1.0.0
     */
    @Override
    public JsonDeserializer<IPolymorphic> createContextual(DeserializationContext ctxt, BeanProperty property) throws JsonMappingException {
        return new PolymorphicDeserialize(this, property);
    }
}
