package dev.dong4j.zeka.kernel.common.enums.serialize;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.google.common.collect.Maps;
import dev.dong4j.zeka.kernel.common.enums.SerializeEnum;
import dev.dong4j.zeka.kernel.common.exception.BasicException;
import dev.dong4j.zeka.kernel.common.support.StrFormatter;
import dev.dong4j.zeka.kernel.common.util.StringPool;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

/**
 * <p>Description: 使用 jackson 作为前端到后端参数反序列化时, 默认是按照索引值处理</p>
 * 这里采用按照 value 处理, 前端只需要传实体枚举对应的 value 值, 后端使用实体枚举接收参数即可, 自动通过 value 转换为枚举
 * 支持 List<>
 * 支持 单独的 value 解析
 * 支持 {"value":1,"desc":"男"} json 解析
 *
 * @param <T> parameter
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dong4j@gmail.com"
 * @date 2019.12.14 18:41
 * @since 1.0.0
 */
@Slf4j
@Data
@SuppressWarnings("unchecked")
@EqualsAndHashCode(callSuper = true)
public class EntityEnumDeserializer<T extends SerializeEnum<?>> extends JsonDeserializer<T> implements ContextualDeserializer {
    /** 当前被处理的枚举类 */
    private Class<T> clz;
    /** 缓存枚举反序列化器 */
    private static final Map<String, EntityEnumDeserializer<? extends SerializeEnum<?>>> DESERIALIZER_MAP = Maps.newConcurrentMap();
    /** 缓存枚举值 */
    private static final Map<String, Map<Serializable, SerializeEnum<?>>> ALL_ENUM_MAP = Maps.newHashMap();
    /** EMPTY_JSON */
    private static final String EMPTY_JSON = StringPool.EMPTY_JSON;

    /**
     * 反序列化优先级: value > 枚举名 > 枚举索引
     *
     * @param jsonParser json parser
     * @param ctx        ctx
     * @return the entity enum
     * @throws IOException io exception
     * @since 1.0.0
     */
    @Override
    public T deserialize(@NotNull JsonParser jsonParser, DeserializationContext ctx) throws IOException {
        if (StringUtils.isEmpty(jsonParser.getText())) {
            return null;
        }
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        log.trace("执行自定义枚举反序列化: Enum = {}, valueType = {}, value = {}", this.clz, node.getNodeType(), node);

        Serializable finalValue = getValue(node);

        if (finalValue == null) {
            return null;
        }

        SerializeEnum<?> result = ALL_ENUM_MAP.get(this.clz.getName()).get(finalValue);

        if (result == null) {
            log.debug("无法通过 value 找到枚举, 尝试使用枚举名查找: value: {}", finalValue);
            result = SerializeEnum.getEnumByNameOrOrder((Class<? extends Enum<?>>) this.clz, finalValue);
            ALL_ENUM_MAP.get(this.clz.getName()).put(finalValue, result);
        }

        return (T) result;
    }

    /**
     * 通过 {@link SerializeEnum#getValue()} 反序列化枚举.
     *
     * @param node node
     * @return the value
     * @since 1.0.0
     */
    @Contract("null -> fail")
    private static @Nullable Serializable getValue(JsonNode node) {
        // POST 请求传入的 json 字符串中没有 value 节点, 则直接抛出异常
        if (node == null) {
            log.debug("未找到枚举类型的 value 节点");
            return null;
        }
        JsonNodeType jsonNodeType = node.getNodeType();
        Serializable value;
        switch (jsonNodeType) {
            case OBJECT:
                // 如果是 "{}" 类型, 则直接返回 null
                if (EMPTY_JSON.equals(node.asText())) {
                    value = null;
                    break;
                }
                // 枚举的 json 类型, 需要递归解析找出 value
                value = getValue(node.get(SerializeEnum.VALUE_FILED_NAME));
                break;
            case NUMBER:
                if (node.isDouble()) {
                    value = node.doubleValue();
                } else if (node.isInt()) {
                    value = node.intValue();
                } else if (node.isFloat()) {
                    value = node.floatValue();
                } else if (node.isLong()) {
                    value = node.longValue();
                } else if (node.isShort()) {
                    value = node.shortValue();
                } else if (node.isBigInteger()) {
                    value = node.bigIntegerValue();
                } else {
                    value = node.decimalValue();
                }
                break;
            case STRING:
                // 枚举名或者 value 为 string 类型
                value = node.asText();
                break;
            case BOOLEAN:
                // value 为 boolean 类型
                value = node.asBoolean();
                break;
            case NULL:
            case ARRAY:
                // 不可能是 pojo, 因为只能是可序列化的值
            case POJO:
                // 不可能是 pojo, 因为只能是可序列化的值
            case BINARY:
                // 不可能是 binary, 因为只能是可序列化的值
            case MISSING:
            default:
                throw new BasicException(StrFormatter.format("不支持的枚举转换: {}", node.toString()));
        }
        return value;
    }

    /**
     * 获取合适的解析器, 把当前解析的属性 Class 对象存起来, 以便反序列化的转换类型.
     *
     * @param ctx      ctx
     * @param property property
     * @return json deserializer
     * @since 1.0.0
     */
    @Override
    public JsonDeserializer<?> createContextual(@NotNull DeserializationContext ctx, BeanProperty property) {
        Class<T> rawCls = (Class<T>) ctx.getContextualType().getRawClass();
        // 当 key 存在返回当前 value 值, 不存在执行函数并保存到 map 中
        EntityEnumDeserializer<?> entityEnumJsonDeserializer = DESERIALIZER_MAP.get(rawCls.getName());

        if (entityEnumJsonDeserializer == null) {
            EntityEnumDeserializer<T> deserializer = new EntityEnumDeserializer<>();
            DESERIALIZER_MAP.put(rawCls.getName(), deserializer);

            T[] enums = rawCls.getEnumConstants();
            // 使用 getValue 作为 key 缓存当前枚举类的所有枚举
            Map<Serializable, SerializeEnum<?>> currentEnumMap = Maps.newHashMap();
            for (T e : enums) {
                currentEnumMap.put(e.getValue(), e);
            }

            ALL_ENUM_MAP.put(rawCls.getName(), currentEnumMap);

            deserializer.setClz(rawCls);
            DESERIALIZER_MAP.put(rawCls.getName(), deserializer);

            entityEnumJsonDeserializer = deserializer;
        }

        return entityEnumJsonDeserializer;
    }
}
