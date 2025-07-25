package dev.dong4j.zeka.kernel.common.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import dev.dong4j.zeka.kernel.common.util.StringPool;
import dev.dong4j.zeka.kernel.common.util.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.temporal.TemporalAccessor;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * <p>Description: jackson 默认值为 null 时的处理, 主要是为了避免 app 端出现null导致闪退
 * 规则:
 * {@code
 * number: 0
 * string: null
 * date: null
 * boolean: false
 * array: []
 * Object: {}
 * }
 * todo-dong4j : (2020-07-10 14:5) [提供扩展接口, 可自定义 changeProperties ]
 *
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dong4j@gmail.com"
 * @date 2019.12.24 01:46
 * @since 1.0.0
 */
public class DefaultBeanSerializerModifier extends com.fasterxml.jackson.databind.ser.BeanSerializerModifier {
    /**
     * Change properties list
     *
     * @param config         config
     * @param beanDesc       bean desc
     * @param beanProperties bean properties
     * @return the list
     * @see DefaultSerializerProvider#_serializeNull(com.fasterxml.jackson.core.JsonGenerator)
     * @since 1.0.0
     */
    @Override
    @SuppressWarnings("all")
    public List<BeanPropertyWriter> changeProperties(SerializationConfig config,
                                                     BeanDescription beanDesc,
                                                     @NotNull List<BeanPropertyWriter> beanProperties) {
        beanProperties.forEach(writer -> {
            // 如果已经有 null 序列化处理如注解: @JsonSerialize(nullsUsing = xxx) 跳过
            if (writer.hasNullSerializer()) {
                return;
            }
            JavaType type = writer.getType();
            Class<?> clazz = type.getRawClass();
            if (type.isTypeOrSubTypeOf(Number.class)) {
                // writer.assignNullSerializer(NullJsonSerializers.NUMBER_JSON_SERIALIZER);
            } else if (type.isTypeOrSubTypeOf(Boolean.class)) {
                writer.assignNullSerializer(NullJsonSerializers.BOOLEAN_JSON_SERIALIZER);
            } else if (type.isTypeOrSubTypeOf(Character.class)) {
                writer.assignNullSerializer(NullJsonSerializers.STRING_JSON_SERIALIZER);
            } else if (type.isTypeOrSubTypeOf(String.class)) {
                writer.assignNullSerializer(NullJsonSerializers.STRING_JSON_SERIALIZER);
            } else if (type.isArrayType() || clazz.isArray() || type.isTypeOrSubTypeOf(Collection.class)) {
                writer.assignNullSerializer(NullJsonSerializers.ARRAY_JSON_SERIALIZER);
            } else if (type.isTypeOrSubTypeOf(OffsetDateTime.class)) {
                writer.assignNullSerializer(NullJsonSerializers.STRING_JSON_SERIALIZER);
            } else if (type.isTypeOrSubTypeOf(Date.class) || type.isTypeOrSubTypeOf(TemporalAccessor.class)) {
                writer.assignNullSerializer(NullJsonSerializers.STRING_JSON_SERIALIZER);
            } else {
                writer.assignNullSerializer(NullJsonSerializers.OBJECT_JSON_SERIALIZER);
            }
        });
        return super.changeProperties(config, beanDesc, beanProperties);
    }

    /**
     * The interface Null json serializers.
     *
     * @author dong4j
     * @version 1.2.3
     * @email "mailto:dong4j@gmail.com"
     * @date 2019.12.24 01:46
     * @since 1.0.0
     */
    @SuppressWarnings("checkstyle:InterfaceIsType")
    public interface NullJsonSerializers {

        /**
         * The constant STRING_JSON_SERIALIZER.
         */
        JsonSerializer<Object> STRING_JSON_SERIALIZER = new JsonSerializer<Object>() {
            @Override
            public void serialize(Object value, @NotNull JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeString(StringPool.EMPTY);
            }
        };

        /**
         * The constant NUMBER_JSON_SERIALIZER.
         */
        JsonSerializer<Object> NUMBER_JSON_SERIALIZER = new JsonSerializer<Object>() {
            @Override
            public void serialize(Object value, @NotNull JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeNumber(StringUtils.INDEX_NOT_FOUND);
            }
        };

        /**
         * The constant BOOLEAN_JSON_SERIALIZER.
         */
        JsonSerializer<Object> BOOLEAN_JSON_SERIALIZER = new JsonSerializer<Object>() {
            @Override
            public void serialize(Object value, @NotNull JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeObject(Boolean.FALSE);
            }
        };

        /**
         * The constant ARRAY_JSON_SERIALIZER.
         */
        JsonSerializer<Object> ARRAY_JSON_SERIALIZER = new JsonSerializer<Object>() {
            @Override
            public void serialize(Object value, @NotNull JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeStartArray();
                gen.writeEndArray();
            }
        };

        /**
         * The constant OBJECT_JSON_SERIALIZER.
         */
        JsonSerializer<Object> OBJECT_JSON_SERIALIZER = new JsonSerializer<Object>() {
            @Override
            public void serialize(Object value, @NotNull JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeStartObject();
                gen.writeEndObject();
            }
        };

    }

}
