package dev.dong4j.zeka.kernel.common.serialize;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
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
public class StringTrimmerDeserializer extends JsonDeserializer<String> {

    /**
     * Deserialize
     *
     * @param jsonParser             json parser
     * @param deserializationContext deserialization context
     * @return the string
     * @throws IOException io exception
     * @since 1.9.0
     */
    @Override
    @SuppressWarnings("java:S3252")
    public String deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        return jsonParser.getValueAsString().strip();
    }

}
