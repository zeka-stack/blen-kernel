package dev.dong4j.zeka.kernel.common.serialize;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;

/**
 * <p>字符串去空格反序列化器.
 * <p>用于在 JSON 反序列化过程中自动去除字符串值的前后空格和空白字符.
 * <p>继承自 Jackson 的 JsonDeserializer，专门处理字符串类型的反序列化操作.
 * <p>主要功能：
 * <ul>
 *     <li>自动去除输入字符串的前后空格</li>
 *     <li>支持 Unicode 空白字符的识别和处理</li>
 *     <li>保证数据输入的一致性和纯净性</li>
 *     <li>提高数据验证的准确性</li>
 * </ul>
 * <p>使用场景：
 * <ul>
 *     <li>API 请求参数的自动清理</li>
 *     <li>配置文件参数的解析</li>
 *     <li>用户输入数据的格式化</li>
 *     <li>防止空格字符干扰业务逻辑</li>
 * </ul>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2021.05.26 16:40
 * @since 1.0.0
 */
public class StringTrimmerDeserializer extends JsonDeserializer<String> {

    /**
     * Deserialize
     *
     * @param jsonParser             json parser
     * @param deserializationContext deserialization context
     * @return the string
     * @throws IOException io exception
     * @since 1.0.0
     */
    @Override
    @SuppressWarnings("java:S3252")
    public String deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        return jsonParser.getValueAsString().strip();
    }

}
