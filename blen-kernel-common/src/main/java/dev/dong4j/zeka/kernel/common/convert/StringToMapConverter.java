package dev.dong4j.zeka.kernel.common.convert;

import dev.dong4j.zeka.kernel.common.util.Jsons;
import dev.dong4j.zeka.kernel.common.util.StringUtils;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;

/**
 * 将字符串(String)转换为Map类型
 * <code>
 * @formatter:off
 * @RestController
 * public class MyController {
 *     @PostMapping("/api/data")
 *     public ResponseEntity<?> handleData(@RequestParam Map<String, Object> dataMap) {
 *         // 字符串参数会自动转换为Map
 *         return ResponseEntity.ok(dataMap);
 *     }
 * }
 * </code>
 * @formatter:on
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2025.02.28 11:50
 * @since 1.0.0
 */
@SuppressWarnings("JavadocDeclaration")
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
