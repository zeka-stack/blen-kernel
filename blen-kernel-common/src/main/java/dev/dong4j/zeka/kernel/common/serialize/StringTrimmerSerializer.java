package dev.dong4j.zeka.kernel.common.serialize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;

/**
 * <p>Description:  </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2021.05.26 16:40
 * @since 1.9.0
 */
public class StringTrimmerSerializer extends JsonSerializer<String> {

    /**
     * Serialize
     *
     * @param value              value
     * @param jsonGenerator      json generator
     * @param serializerProvider serializer provider
     * @throws IOException io exception
     * @since 1.9.0
     */
    @Override
    @SuppressWarnings("java:S3252")
    public void serialize(String value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(value.strip());
    }
}
