package dev.dong4j.zeka.kernel.common.convert;

import org.jetbrains.annotations.NotNull;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import dev.dong4j.zeka.kernel.common.util.Jsons;
import dev.dong4j.zeka.kernel.common.util.StringUtils;

/**
 * 字符串到 Map 的转换器
 * <p> 实现 ConditionalGenericConverter 接口, 用于将字符串类型的数据转换为 Map 类型, 支持根据目标类型进行条件转换.
 * 该转换器在字符串内容为 JSON 格式时, 使用 Jsons 工具类进行解析, 适用于 Spring 框架中的类型转换场景.
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2025.12.22
 * @since 2.0.0
 */
public class StringToMapConverter implements ConditionalGenericConverter {
    /**
     * Matches
     *
     * @param sourceType source type
     * @param targetType target type
     * @return the boolean
     * @since 2024.2.0
     */
    @Override
    public boolean matches(TypeDescriptor sourceType, @NotNull TypeDescriptor targetType) {
        return sourceType.isAssignableTo(TypeDescriptor.valueOf(String.class)) && targetType.isMap();
    }

    /**
     * Gets convertible types *
     *
     * @return the convertible types
     * @since 2024.2.0
     */
    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        return Collections.singleton(new ConvertiblePair(String.class, Map.class));
    }

    /**
     * Convert
     *
     * @param source     source
     * @param sourceType source type
     * @param targetType target type
     * @return the object
     * @since 2024.2.0
     */
    @Override
    public Object convert(Object source, @NotNull TypeDescriptor sourceType, @NotNull TypeDescriptor targetType) {
        if (source == null) {
            return null;
        }
        String value = (String) source;
        if (StringUtils.isBlank(value)) {
            return null;
        }
        return Jsons.parse(value, targetType.getType());
    }
}
