package dev.dong4j.zeka.kernel.common.mapstruct;

import dev.dong4j.zeka.kernel.common.enums.SerializeEnum;
import dev.dong4j.zeka.kernel.common.util.EnumUtils;
import org.jetbrains.annotations.Contract;
import org.mapstruct.Named;

import java.io.Serializable;
import java.util.Optional;

/**
 * <p>Description: 实体枚举类 {@link SerializeEnum} 与 {@link SerializeEnum#getValue()} 和 {@link SerializeEnum#getDesc()} ()} 的转换关系 </p>
 * 在使用 MapStruct 时, 需要一个子类, 并给 enumType 赋值
 * demo:
 * {@code
 * class UserEnumConverter extends EntityEnumConverter<GenderEnum, Integer> {
 * {
 * super.enumType = GenderEnum.class;}
 * }
 * }
 *
 * @param <E> the type parameter
 * @param <V> the type parameter
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.27 18:08
 * @since 1.0.0
 */
@SuppressWarnings(value = {"unchecked", "checkyle:HiddenField"})
public abstract class EntityEnumConverter<E extends SerializeEnum<?>, V extends Serializable> {
    /**
     * The Enum type.
     */
    private final Class<E> enumType;

    /**
     * Entity enum converter
     *
     * @param enumType enum type
     * @since 1.9.0
     */
    @Contract(pure = true)
    protected EntityEnumConverter(Class<E> enumType) {
        this.enumType = enumType;
    }

    /**
     * 通过枚举转换为 value
     *
     * @param enumType the gender enum
     * @return the v
     * @since 1.0.0
     */
    public V toValue(E enumType) {
        if (enumType == null) {
            return null;
        }
        return (V) enumType.getValue();
    }

    /**
     * 通过枚举转换为 desc
     *
     * @param enumType the gender enum
     * @return the string
     * @since 1.0.0
     */
    public String toDesc(E enumType) {
        if (enumType == null) {
            return null;
        }
        return enumType.getDesc();
    }

    /**
     * value 转 desc
     *
     * @param value the value
     * @return the string
     * @since 1.0.0
     */
    @Named("EnumValueToDesc")
    public String valutToDesc(V value) {
        E tempEnumType = this.fromValue(value);
        if (tempEnumType == null) {
            return null;
        }
        return tempEnumType.getDesc();
    }

    /**
     * 根据 value 转换为枚举
     *
     * @param value the value
     * @return the e
     * @since 1.0.0
     */
    public E fromValue(V value) {
        Optional<E> m = EnumUtils.of(this.enumType, e -> e.getValue().equals(value));
        return m.orElse(null);
    }

    /**
     * desc 转 value
     *
     * @param desc the desc
     * @return the v
     * @since 1.0.0
     */
    @Named("EnumDescToValue")
    public V descToValue(String desc) {
        E tempEnumType = this.fromDesc(desc);
        if (tempEnumType == null) {
            return null;
        }
        return (V) tempEnumType.getValue();
    }

    /**
     * 根据 desc 转换为枚举
     *
     * @param desc the desc
     * @return the e
     * @since 1.0.0
     */
    public E fromDesc(String desc) {
        Optional<E> m = EnumUtils.of(this.enumType, e -> e.getDesc().equals(desc));
        return m.orElse(null);
    }
}
