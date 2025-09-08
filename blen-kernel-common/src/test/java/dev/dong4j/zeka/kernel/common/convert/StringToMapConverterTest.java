package dev.dong4j.zeka.kernel.common.convert;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.convert.TypeDescriptor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * StringToMapConverter 单元测试
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2025.09.08 20:29
 * @since x.x.x
 */
@SuppressWarnings("unchecked")
class StringToMapConverterTest {

    private StringToMapConverter converter;
    private TypeDescriptor stringTypeDescriptor;
    private TypeDescriptor valueTypeDescriptor;
    private TypeDescriptor mapTypeDescriptor;

    @BeforeEach
    void setUp() {
        converter = new StringToMapConverter();
        stringTypeDescriptor = TypeDescriptor.valueOf(String.class);
        valueTypeDescriptor = TypeDescriptor.valueOf(Object.class);
        mapTypeDescriptor = TypeDescriptor.map(Map.class, stringTypeDescriptor, valueTypeDescriptor);
    }

    @Test
    void convert_ValidJsonString_ReturnsMap() {
        // Given
        String jsonString = "{\"name\":\"test\",\"age\":18,\"active\":true}";
        TypeDescriptor targetType = TypeDescriptor.map(Map.class, stringTypeDescriptor, valueTypeDescriptor);

        // When
        Object result = converter.convert(jsonString, stringTypeDescriptor, targetType);

        // Then
        assertNotNull(result);
        assertInstanceOf(Map.class, result);
        Map<String, Object> resultMap = (Map<String, Object>) result;
        assertEquals("test", resultMap.get("name"));
        assertEquals(18, resultMap.get("age"));
        assertEquals(true, resultMap.get("active"));
    }

    @Test
    void convert_NullSource_ReturnsNull() {
        // When
        Object result = converter.convert(null, stringTypeDescriptor, mapTypeDescriptor);

        // Then
        assertNull(result);
    }

    @Test
    void convert_EmptyString_ReturnsNull() {
        // When
        Object result = converter.convert("", stringTypeDescriptor, mapTypeDescriptor);

        // Then
        assertNull(result);
    }

    @Test
    void convert_BlankString_ReturnsNull() {
        // When
        Object result = converter.convert("   ", stringTypeDescriptor, mapTypeDescriptor);

        // Then
        assertNull(result);
    }

    @Test
    void convert_InvalidJsonString_ThrowsException() {
        // Given
        String invalidJson = "{invalid json}";

        // When & Then
        assertThrows(Exception.class, () -> {
            converter.convert(invalidJson, stringTypeDescriptor, mapTypeDescriptor);
        });
    }

    @Test
    void convert_SpecificMapType_ReturnsCorrectType() {
        // Given
        String jsonString = "{\"id\":1,\"name\":\"test\"}";
        TypeDescriptor specificMapType = TypeDescriptor.map(HashMap.class, stringTypeDescriptor, valueTypeDescriptor);

        // When
        Object result = converter.convert(jsonString, stringTypeDescriptor, specificMapType);

        // Then
        assertNotNull(result);
        assertInstanceOf(HashMap.class, result);
        HashMap<String, Object> resultMap = (HashMap<String, Object>) result;
        assertEquals(1, resultMap.get("id"));
        assertEquals("test", resultMap.get("name"));
    }
}

