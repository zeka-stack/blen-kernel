package dev.dong4j.zeka.kernel.common.enums;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.collect.Sets;
import dev.dong4j.zeka.kernel.common.annotation.SerializeValue;
import dev.dong4j.zeka.kernel.common.enums.serialize.EntityEnumDeserializer;
import dev.dong4j.zeka.kernel.common.enums.serialize.EntityEnumSerializer;
import dev.dong4j.zeka.kernel.common.exception.BasicException;
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
 * <p>Description: 可序列化为 json 的枚举接口 </p>
 *
 * @param <T> the type parameter
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.03 19:31
 * @since 1.0.0
 */
@JsonSerialize(using = EntityEnumSerializer.class)
@JsonDeserialize(using = EntityEnumDeserializer.class)
public interface SerializeEnum<T extends Serializable> {
    /** log */
    Logger LOG = LoggerFactory.getLogger(EntityEnumDeserializer.class);
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
     * @since 1.5.0
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
        return EnumUtils.of(clazz, e -> e.getValue().equals(value)).orElseThrow(() -> new BasicException(errorMessage));
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
            throw new BasicException(StrFormatter.format("未找到匹配的枚举: [{}]", value));
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
     * @since 2022.1.1
     * @deprecated 不再使用下标查找枚举
     */
    @SuppressWarnings("unchecked")
    @Deprecated
    static <T> T getEnumByOrder(Class<? extends Enum<?>> clz, String value) {
        int index;
        try {
            index = Integer.parseInt(value);
        } catch (Exception e) {
            throw new BasicException(StrFormatter.format("通过枚举下标查找枚举出错: [{}] 无法转换为下标", value));
        }
        // 使用下标匹配
        Optional<? extends Enum<?>> getForOrdinal = EnumUtils.of(clz, e -> e.ordinal() == index);
        return (T) getForOrdinal.orElseThrow(() -> new BasicException("无法将 [{}] 转换为 [{}]", value, clz));
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
