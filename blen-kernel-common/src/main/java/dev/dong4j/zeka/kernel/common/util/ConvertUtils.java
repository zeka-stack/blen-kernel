package dev.dong4j.zeka.kernel.common.util;

import dev.dong4j.zeka.kernel.common.convert.CustomConversionService;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.Contract;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.lang.Nullable;

/**
 * <p>Description: 基于 spring ConversionService 类型转换 </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.27 18:18
 * @since 1.0.0
 */
@UtilityClass
@SuppressWarnings("unchecked")
public class ConvertUtils {

    /**
     * Convenience operation for converting a source object to the specified targetType.
     * {@link TypeDescriptor#forObject(Object)}.
     *
     * @param <T>        泛型标记
     * @param source     the source object
     * @param targetType the target type
     * @return the converted value
     * @throws IllegalArgumentException if targetType is {@code null}, or sourceType is {@code null} but source is not {@code null}
     * @since 1.0.0
     */
    @Contract("null, _ -> null")
    @Nullable
    public static <T> T convert(@Nullable Object source, Class<T> targetType) {
        if (source == null) {
            return null;
        }
        if (ClassUtils.isAssignableValue(targetType, source)) {
            return (T) source;
        }
        GenericConversionService conversionService = CustomConversionService.getInstance();
        return conversionService.convert(source, targetType);
    }

    /**
     * Convenience operation for converting a source object to the specified targetType,
     * where the target type is a descriptor that provides additional conversion context.
     * {@link TypeDescriptor#forObject(Object)}.
     *
     * @param <T>        泛型标记
     * @param source     the source object
     * @param sourceType the source type
     * @param targetType the target type
     * @return the converted value
     * @throws IllegalArgumentException if targetType is {@code null},  or sourceType is {@code null} but source is not {@code null}
     * @since 1.0.0
     */
    @Contract("null, _, _ -> null")
    @Nullable
    public static <T> T convert(@Nullable Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        if (source == null) {
            return null;
        }
        GenericConversionService conversionService = CustomConversionService.getInstance();
        return (T) conversionService.convert(source, sourceType, targetType);
    }

    /**
     * Convenience operation for converting a source object to the specified targetType,
     * where the target type is a descriptor that provides additional conversion context.
     * Simply delegates to {@link #convert(Object, TypeDescriptor, TypeDescriptor)} and
     * encapsulates the construction of the source type descriptor using
     * {@link TypeDescriptor#forObject(Object)}.
     *
     * @param <T>        泛型标记
     * @param source     the source object
     * @param targetType the target type
     * @return the converted value
     * @throws IllegalArgumentException if targetType is {@code null}, or sourceType is {@code null} but source is not {@code null}
     * @since 1.0.0
     */
    @Contract("null, _ -> null")
    @Nullable
    public static <T> T convert(@Nullable Object source, TypeDescriptor targetType) {
        if (source == null) {
            return null;
        }
        GenericConversionService conversionService = CustomConversionService.getInstance();
        return (T) conversionService.convert(source, targetType);
    }

}
