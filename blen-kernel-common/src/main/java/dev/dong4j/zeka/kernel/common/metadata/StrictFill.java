package dev.dong4j.zeka.kernel.common.metadata;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

/**
 * 严格填充模式 model
 *
 * @author miemie
 * @version 1.3.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.04.12 12:06
 * @since 1.0.0
 */
@Data
@AllArgsConstructor
public class StrictFill {
    /** 字段名 */
    private String fieldName;
    /** 字段类型 */
    private Class<?> fieldType;
    /** 获取字段值的函数 */
    private Supplier<Object> fieldVal;

    /**
     * Of strict fill
     *
     * @param fieldName field name
     * @param fieldType field type
     * @param fieldVal  field val
     * @return the strict fill
     * @since 1.0.0
     */
    @Contract("_, _, _ -> new")
    public static @NotNull StrictFill of(String fieldName, Class<?> fieldType, Object fieldVal) {
        return new StrictFill(fieldName, fieldType, () -> fieldVal);
    }

    /**
     * Of strict fill
     *
     * @param fieldName field name
     * @param fieldType field type
     * @param fieldVal  field val
     * @return the strict fill
     * @since 1.0.0
     */
    @Contract("_, _, _ -> new")
    public static @NotNull StrictFill of(String fieldName, Class<?> fieldType, Supplier<Object> fieldVal) {
        return new StrictFill(fieldName, fieldType, fieldVal);
    }
}
