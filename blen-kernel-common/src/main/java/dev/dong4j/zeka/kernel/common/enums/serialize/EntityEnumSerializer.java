package dev.dong4j.zeka.kernel.common.enums.serialize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.google.common.collect.Maps;
import dev.dong4j.zeka.kernel.common.enums.SerializeEnum;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>Description: EntityEnum 及子类,序列化成 json 时, 指定序列值 </p>
 * 实体枚举子类会被序列化为 {"value": 1,"desc": "男"} 的形式
 *
 * @param <T> parameter
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.27 18:22
 * @since 1.0.0
 */
@Slf4j
public class EntityEnumSerializer<T extends SerializeEnum<?>> extends JsonSerializer<T> {
    /** 缓存枚举类的 field */
    private static final Map<String, List<Field>> FIELD_MAP = Maps.newConcurrentMap();

    /**
     * 将 {@link SerializeEnum} 实现枚举类型序列化为 json
     *
     * @param entityEnum         entity enum
     * @param jsonGenerator      json generator
     * @param serializerProvider serializer provider
     * @throws IOException io exception
     * @since 1.0.0
     */
    @Override
    @SuppressWarnings("all")
    public void serialize(@NotNull T entityEnum,
                          @NotNull JsonGenerator jsonGenerator,
                          SerializerProvider serializerProvider) throws IOException {
        log.trace("执行自定义枚举序列化: Enum = [{}], value = [{}], desc = [{}]", entityEnum.getClass(), entityEnum.getValue(), entityEnum.getDesc());

        Class<? extends SerializeEnum<?>> enumClass = (Class<? extends SerializeEnum<?>>) entityEnum.getClass();

        List<Field> fieldList = FIELD_MAP.get(enumClass.getName());
        if (CollectionUtils.isEmpty(fieldList)) {
            Field[] fields = enumClass.getDeclaredFields();
            fieldList = Arrays.stream(fields).filter(f -> !Modifier.isStatic(f.getModifiers())).collect(Collectors.toList());
            FIELD_MAP.put(enumClass.getName(), fieldList);
        }

        if (CollectionUtils.isEmpty(fieldList)) {
            jsonGenerator.writeObject(entityEnum.getDesc());
        } else {
            jsonGenerator.writeStartObject();
            for (Field field : fieldList) {
                field.setAccessible(true);
                jsonGenerator.writeFieldName(field.getName());
                try {
                    jsonGenerator.writeObject(field.get(entityEnum));
                } catch (IllegalAccessException e) {
                    throw new IOException(e);
                }
            }
            jsonGenerator.writeEndObject();
        }
    }
}
