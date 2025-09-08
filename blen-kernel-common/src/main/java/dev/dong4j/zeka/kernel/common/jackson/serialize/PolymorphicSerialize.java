package dev.dong4j.zeka.kernel.common.jackson.serialize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import dev.dong4j.zeka.kernel.common.enums.SerializeEnum;
import dev.dong4j.zeka.kernel.common.jackson.IPolymorphic;
import dev.dong4j.zeka.kernel.common.jackson.Polymorphic;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import lombok.extern.slf4j.Slf4j;

/**
 * 多态对象序列化器，用于将多态对象序列化为JSON数据
 * <p>
 * 该序列化器实现了Jackson的上下文相关序列化机制，能够在序列化过程中
 * 自动添加类型标识字段，并根据对象的具体类型找到对应的类型标识符
 * <p>
 * 主要特性：
 * - 继承自StdSerializer，提供标准的Jackson序列化能力
 * - 实现ContextualSerializer接口，支持上下文相关的序列化
 * - 支持基于SerializeEnum枚举的类型映射
 * - 支持基于@Polymorphic.Type注解的类型映射
 * - 自动处理嵌套的多态对象和集合类型
 * - 提供详细的日志记录和错误处理
 * <p>
 * 序列化流程：
 * 1. 从字段上获取@Polymorphic注解配置
 * 2. 根据对象的具体类型查找对应的类型标识
 * 3. 在JSON中先写入类型标识字段
 * 4. 然后序列化对象的所有属性字段
 * 5. 特殊处理集合类型中的多态对象
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2019.12.26 21:43
 * @since 1.0.0
 */
@Slf4j
public class PolymorphicSerialize extends StdSerializer<IPolymorphic> implements ContextualSerializer {

    /** 当前序列化上下文中的字段属性信息，用于获取@Polymorphic注解配置 */
    private BeanProperty beanProperty;

    /**
     * 默认构造函数
     * <p>
     * 创建一个基本的多态序列化器实例，不包含任何上下文信息
     * 通常用于初始化，具体的上下文信息会在createContextual方法中设置
     *
     * @since 1.0.0
     */
    public PolymorphicSerialize() {
        this(null);
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
    protected PolymorphicSerialize(Class<IPolymorphic> t) {
        super(t);
    }

    /**
     * 复制构造函数，用于创建包含上下文信息的序列化器实例
     * <p>
     * 此构造函数由createContextual方法调用，用于传递当前字段的属性信息
     * 这些信息包含了@Polymorphic注解的配置，是进行多态序列化的关键依据
     *
     * @param src          源序列化器实例
     * @param beanProperty 字段属性信息，包含@Polymorphic注解配置
     * @since 1.0.0
     */
    protected PolymorphicSerialize(PolymorphicSerialize src, BeanProperty beanProperty) {
        super(src);
        this.beanProperty = beanProperty;
    }


    /**
     * 执行多态对象的序列化操作
     * <p>
     * 该方法是多态序列化的核心逻辑，负责根据对象的具体类型查找对应的类型标识
     * 并在JSON中先写入类型标识字段，然后序列化对象的所有属性
     * <p>
     * 序列化步骤：
     * 1. 从字段上获取@Polymorphic注解
     * 2. 根据对象的类型从枚举或Type映射中找到对应的标识
     * 3. 在JSON中先写入类型标识字段
     * 4. 序列化对象的所有属性字段
     * 5. 特殊处理集合类型中的多态对象
     *
     * @param polymorphic 要序列化的多态对象实例
     * @param gen         JSON生成器，用于写入JSON数据
     * @param provider    序列化提供者，提供类型信息和配置
     * @throws IOException 如果JSON写入过程中出现I/O错误
     * @since 1.0.0
     */
    @Override
    @SuppressWarnings("all")
    public void serialize(IPolymorphic polymorphic, JsonGenerator gen, SerializerProvider provider) throws IOException {
        if (this.beanProperty == null) {
            return;
        }
        Polymorphic polymorphicAnnotation = this.beanProperty.getAnnotation(Polymorphic.class);
        if (polymorphicAnnotation == null) {
            gen.writeObject(polymorphic);
            return;
        }
        String value = polymorphicAnnotation.value();
        Class<? extends SerializeEnum> enumClass = polymorphicAnnotation.enumClass();
        if (enumClass != SerializeEnum.class) {
            SerializeEnum[] enumConstants = enumClass.getEnumConstants();
            if (enumConstants == null || enumConstants.length == 0) {
                gen.writeObject(polymorphic);
                return;
            }
            SerializeEnum serializeEnum = Arrays.stream(enumConstants)
                .filter(e -> polymorphic.getClass() == e.getValue()).findFirst().orElse(null);
            if (serializeEnum == null) {
                log.debug("多态序列化未找到实现类标识:{}", polymorphic.getClass());
                gen.writeObject(polymorphic);
                return;
            }
            String identifier = serializeEnum.getDesc();
            gen.writeStartObject();
            gen.writeStringField(value, identifier);
            JavaType javaType = provider.constructType(polymorphic.getClass());
            BeanDescription beanDescription = provider.getConfig().introspect(javaType);
            beanDescription.findProperties().forEach(property -> {
                String propertyName = property.getName();
                Object fieldValue = property.getAccessor().getValue(polymorphic);
                if (fieldValue == null) {
                    return;
                }
                try {
                    JavaType type = property.getPrimaryType();
                    if (type.isCollectionLikeType()) {
                        JavaType contentType = type.getContentType();
                        gen.writeArrayFieldStart(propertyName);
                        if (contentType.isTypeOrSubTypeOf(IPolymorphic.class)) {
                            for (Object o : (Collection<?>) fieldValue) {
                                this.serialize((IPolymorphic) o, gen, provider);
                            }
                        } else {
                            for (Object o : (Collection<?>) fieldValue) {
                                gen.writeObject(o);
                            }
                        }
                        gen.writeEndArray();
                    } else {
                        gen.writeObjectField(propertyName, fieldValue);
                    }
                } catch (Exception e) {
                    log.error("字段:{},序列化异常:{}", propertyName, e.getMessage());
                }
            });
            gen.writeEndObject();
        }
    }


    /**
     * 创建上下文相关的序列化器实例
     * <p>
     * 该方法实现了ContextualSerializer接口，允许序列化器根据当前的上下文信息
     * （特别是字段属性信息）创建一个新的序列化器实例。这使得不同的字段可以有不同的配置
     * <p>
     * 这个机制允许：
     * - 每个字段都可以有独立的@Polymorphic注解配置
     * - 序列化器可以获取到当前字段的元数据信息
     * - 实现更精细的多态处理逻辑
     *
     * @param prov     序列化提供者
     * @param property 当前字段的属性信息，包含注解和类型信息
     * @return 新的上下文相关序列化器实例
     * @since 1.0.0
     */
    @Override
    public JsonSerializer<IPolymorphic> createContextual(SerializerProvider prov, BeanProperty property) {
        return new PolymorphicSerialize(this, property);
    }
}
