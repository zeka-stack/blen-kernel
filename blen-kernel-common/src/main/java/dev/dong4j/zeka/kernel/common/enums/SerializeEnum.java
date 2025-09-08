package dev.dong4j.zeka.kernel.common.enums;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.collect.Sets;
import dev.dong4j.zeka.kernel.common.annotation.SerializeValue;
import dev.dong4j.zeka.kernel.common.enums.serialize.EntityEnumDeserializer;
import dev.dong4j.zeka.kernel.common.enums.serialize.EntityEnumSerializer;
import dev.dong4j.zeka.kernel.common.exception.LowestException;
import dev.dong4j.zeka.kernel.common.support.StrFormatter;
import dev.dong4j.zeka.kernel.common.util.EnumUtils;
import java.io.Serializable;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 可序列化为JSON的枚举接口，提供统一的枚举序列化和反序列化能力
 * <p>
 * 该接口为枚举类型提供了统一的JSON序列化和反序列化支持
 * 通过自定义的序列化器和反序列化器，实现枚举值的精确控制
 * <p>
 * 主要特性：
 * - 自定义序列化：使用getValue()方法返回的值进行序列化
 * - 智能反序列化：支持多种反序列化策略（值、名称、下标）
 * - 类型安全：通过泛型参数保证类型安全
 * - 错误处理：提供详细的错误信息和异常处理
 * - 注解支持：支持@SerializeValue注解自定义序列化字段
 * <p>
 * 序列化机制：
 * 1. 序列化时优先使用getValue()方法返回的值
 * 2. 支持@SerializeValue注解指定自定义序列化字段
 * 3. 使用EntityEnumSerializer实现自定义序列化逻辑
 * <p>
 * 反序列化机制：
 * 1. 首先尝试通过getValue()返回值进行匹配
 * 2. 如果失败，则使用枚举名称（name()）进行匹配
 * 3. 最后使用枚举下标（ordinal()）进行匹配（已废弃）
 * 4. 使用EntityEnumDeserializer实现自定义反序列化逻辑
 * <p>
 * 使用场景：
 * - API接口中的枚举参数和返回值，需要精确控制JSON格式
 * - 数据库存储的枚举值，需要与JSON表示保持一致
 * - 前后端数据交互中的枚举类型，需要统一的表示方式
 * - 微服务间的数据传输，需要保证枚举值的一致性
 * - 配置文件中的枚举配置，需要可读性和稳定性
 * <p>
 * 使用示例：
 * <pre>{@code
 * public enum Status implements SerializeEnum<Integer> {
 *     ACTIVE(1, "激活"),
 *     INACTIVE(0, "未激活");
 *
 *     private final Integer value;
 *     private final String desc;
 *
 *     Status(Integer value, String desc) {
 *         this.value = value;
 *         this.desc = desc;
 *     }
 *
 *     @Override
 *     public Integer getValue() {
 *         return value;
 *     }
 *
 *     @Override
 *     public String getDesc() {
 *         return desc;
 *     }
 * }
 *
 * // JSON序列化结果：{"status": 1} 而不是 {"status": "ACTIVE"}
 * // 反序列化支持：1 -> ACTIVE, "ACTIVE" -> ACTIVE
 * }</pre>
 * <p>
 * 注意事项：
 * - 枚举类必须实现该接口才能使用自定义序列化功能
 * - getValue()方法返回的值必须实现Serializable接口
 * - 不同枚举值的getValue()返回值必须唯一，否则反序列化可能不准确
 * - 建议不使用枚举下标进行反序列化，因为下标容易变化
 *
 * @param <T> 枚举值的类型，必须实现Serializable接口
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.03 19:31
 * @since 1.0.0
 */
@JsonSerialize(using = EntityEnumSerializer.class)
@JsonDeserialize(using = EntityEnumDeserializer.class)
public interface SerializeEnum<T extends Serializable> {
    /** log */
    Logger LOG = LoggerFactory.getLogger(SerializeEnum.class);
    /** 子类枚举的 vaule 字段的字段名 */
    String VALUE_FILED_NAME = "value";
    /** VALUE_METHOD_NAME */
    String VALUE_METHOD_NAME = "getValue";

    /**
     * 存入数据库的值
     *
     * @return the values
     * @since 1.0.0
     */
    T getValue();

    /**
     * Type
     *
     * @return the class
     * @since 1.0.0
     */
    default Class<?> valueClass() {
        return this.getValue().getClass();
    }

    /**
     * 描述
     *
     * @return the desc
     * @since 1.0.0
     */
    String getDesc();

    /**
     * 返回枚举名
     *
     * @return the string
     * @since 1.0.0
     */
    String name();

    /**
     * 返回枚举下标
     *
     * @return the int
     * @since 1.0.0
     */
    int ordinal();

    /**
     * Value of e.
     *
     * @param <E>   the type parameter
     * @param clazz the clazz
     * @param value the value
     * @return the e
     * @since 1.0.0
     */
    static <E extends Enum<E> & SerializeEnum<?>> E valueOf(Class<E> clazz, Serializable value) {
        String errorMessage = StrFormatter.format("枚举类型转换错误: 没有找到对应枚举. value = {}, SerializeEnum = {}",
            value,
            clazz);
        return EnumUtils.of(clazz, e -> e.getValue().equals(value)).orElseThrow(() -> new LowestException(errorMessage));
    }

    /**
     * 如果无法使用 {@link SerializeEnum#getValue()} 解析枚举, 将再次使用 name() 获取枚举, 最后是 ordinal().
     *
     * @param <T>        parameter
     * @param clz        clz
     * @param finalValue final value
     * @return the enum by order
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    static <T> @NotNull T getEnumByNameOrOrder(Class<? extends Enum<?>> clz, Serializable finalValue) {
        T result;
        String value = String.valueOf(finalValue);
        // 使用 name() 匹配
        Optional<? extends Enum<?>> getForName = EnumUtils.of(clz, e -> e.name().equals(value));
        if (getForName.isPresent()) {
            result = (T) getForName.get();
        } else {
            LOG.debug("无法通过 name 找到枚举, 尝试使用枚举下标查找, name: {}", value);
            throw new LowestException(StrFormatter.format("未找到匹配的枚举: [{}]", value));
        }
        return result;
    }

    /**
     * Gets enum by order *
     *
     * @param <T>   parameter
     * @param clz   clz
     * @param value value
     * @return the enum by order
     * @since 1.0.0
     * @deprecated 不再使用下标查找枚举
     */
    @SuppressWarnings("unchecked")
    @Deprecated
    static <T> T getEnumByOrder(Class<? extends Enum<?>> clz, String value) {
        int index;
        try {
            index = Integer.parseInt(value);
        } catch (Exception e) {
            throw new LowestException(StrFormatter.format("通过枚举下标查找枚举出错: [{}] 无法转换为下标", value));
        }
        // 使用下标匹配
        Optional<? extends Enum<?>> getForOrdinal = EnumUtils.of(clz, e -> e.ordinal() == index);
        return (T) getForOrdinal.orElseThrow(() -> new LowestException("无法将 [{}] 转换为 [{}]", value, clz));
    }

    /**
     * 查找被 {@link SerializeValue} 标识的元素
     *
     * @param clazz clazz
     * @return the annotation
     * @since 1.0.0
     */
    @Nullable
    static AccessibleObject getAnnotation(@NotNull Class<?> clazz) {
        Set<AccessibleObject> accessibleObjects = Sets.newHashSetWithExpectedSize(8);
        Field[] fields = clazz.getDeclaredFields();
        Collections.addAll(accessibleObjects, fields);
        for (AccessibleObject accessibleObject : accessibleObjects) {
            SerializeValue jsonCreator = accessibleObject.getAnnotation(SerializeValue.class);
            if (jsonCreator != null) {
                accessibleObject.setAccessible(true);
                return accessibleObject;
            }
        }
        return null;
    }

}
