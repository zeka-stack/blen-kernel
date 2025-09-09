package dev.dong4j.zeka.kernel.common.serialize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;

/**
 * <p>字符串去空格序列化器.
 * <p>用于在 JSON 序列化过程中自动去除字符串值的前后空格和空白字符.
 * <p>继承自 Jackson 的 JsonSerializer，专门处理字符串类型的序列化操作.
 * <p>主要功能：
 * <ul>
 *     <li>自动去除字符串的前后空格</li>
 *     <li>支持 Unicode 空白字符的处理</li>
 *     <li>保证 JSON 输出的一致性和纯净性</li>
 *     <li>降低数据传输的冗余信息</li>
 * </ul>
 * <p>使用场景：
 * <ul>
 *     <li>API 响应数据的自动清理</li>
 *     <li>用户输入数据的格式化</li>
 *     <li>数据库存储前的预处理</li>
 *     <li>防止不可见字符引起的问题</li>
 * </ul>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2021.05.26 16:40
 * @since 1.0.0
 */
public class StringTrimmerSerializer extends JsonSerializer<String> {

    /**
     * Serialize
     *
     * @param value              value
     * @param jsonGenerator      json generator
     * @param serializerProvider serializer provider
     * @throws IOException io exception
     * @since 1.0.0
     */
    @Override
    @SuppressWarnings("java:S3252")
    public void serialize(String value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(value.strip());
    }
}
