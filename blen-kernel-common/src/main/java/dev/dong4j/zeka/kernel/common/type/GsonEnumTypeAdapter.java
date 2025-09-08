package dev.dong4j.zeka.kernel.common.type;

import com.google.common.collect.Maps;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import dev.dong4j.zeka.kernel.common.enums.SerializeEnum;
import dev.dong4j.zeka.kernel.common.util.Jsons;
import dev.dong4j.zeka.kernel.common.util.StringUtils;
import java.lang.reflect.Type;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

/**
 * <p>Description: </p>
 *
 * @param <E> parameter
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.06.29 22:03
 * @since 1.0.0
 */
@Slf4j
public class GsonEnumTypeAdapter<E extends Enum<E> & SerializeEnum<?>> implements JsonSerializer<E>, JsonDeserializer<E> {

    /** Enum map */
    private final Map<String, E> enumMap = Maps.newHashMap();
    /** Enum type */
    private final Class<? extends SerializeEnum<?>> enumType;

    /**
     * Gson enum type adapter
     *
     * @param enumType enum type
     * @since 1.0.0
     */
    public GsonEnumTypeAdapter(@NotNull Class<E> enumType) {
        this.enumType = enumType;
        // 获取当前枚举类型的所有已定义的枚举, 如果是 SerializeEnum 的子接口, 将返回 null
        E[] enums = enumType.getEnumConstants();
        if (enums != null) {
            for (E e : enums) {
                // 将 getValue 转为 String, 因为前端传过来的都是 String 类型的参数
                this.enumMap.put(String.valueOf(e.getValue()), e);
            }
        }
    }

    /**
     * Deserialize
     *
     * @param jsonElement                json element
     * @param type                       type
     * @param jsonDeserializationContext json deserialization context
     * @return the e
     * @throws JsonParseException json parse exception
     * @since 1.0.0
     */
    @Override
    @SuppressWarnings("unchecked")
    public E deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext)
        throws JsonParseException {
        if (type instanceof Class) {
            // 处理为 json 对象的情况
            if (jsonElement instanceof JsonObject) {
                return Jsons.parse(jsonElement.toString(), type);
            }
            String key;
            if (((JsonPrimitive) jsonElement).isString()) {
                key = jsonElement.getAsString();
            } else {
                key = jsonElement.toString();
            }

            // 如果存在 value 字段但是没有值, 则返回 null
            if (StringUtils.isBlank(key)) {
                return null;
            }

            // 处理时 json 字符串的情况
            if (Jsons.isJson(key)) {
                return Jsons.parse(key, type);
            }

            E result = this.enumMap.get(key);

            if (result == null) {
                result = SerializeEnum.getEnumByNameOrOrder((Class<? extends Enum<?>>) this.enumType, key);
                this.enumMap.put(key, result);
            }
            return result;
        } else {
            throw new RuntimeException(String.format("json %s cannot convert to type %s", jsonElement, type));
        }
    }

    /**
     * Serialize
     *
     * @param e                        e
     * @param type                     type
     * @param jsonSerializationContext json serialization context
     * @return the json element
     * @since 1.0.0
     */
    @Override
    public JsonElement serialize(E e, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(Jsons.toJson(e));
    }
}
