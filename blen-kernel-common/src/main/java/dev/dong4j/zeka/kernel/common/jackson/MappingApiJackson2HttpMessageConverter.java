package dev.dong4j.zeka.kernel.common.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.io.IOException;

/**
 * <p>Description: 针对 api 服务对 android 和 ios 和 web 处理的 分读写的 jackson 处理 </p>
 * 1. app 端上报数据是 使用 readObjectMapper
 * 2. 返回给 app 端的数据使用 writeObjectMapper
 *
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.27 18:08
 * @since 1.0.0
 */
public class MappingApiJackson2HttpMessageConverter extends AbstractReadWriteJackson2HttpMessageConverter {

    /** Json prefix */
    @Nullable
    private String jsonPrefix;

    /**
     * Construct a new {@link MappingApiJackson2HttpMessageConverter} using default configuration
     * provided by {@link Jackson2ObjectMapperBuilder}.
     *
     * @since 1.0.0
     */
    public MappingApiJackson2HttpMessageConverter() {
        this(Jackson2ObjectMapperBuilder.json().build());
    }

    /**
     * Construct a new {@link MappingApiJackson2HttpMessageConverter} with a custom {@link ObjectMapper}.
     * You can use {@link Jackson2ObjectMapperBuilder} to build it easily.
     *
     * @param objectMapper ObjectMapper
     * @see Jackson2ObjectMapperBuilder#json() Jackson2ObjectMapperBuilder#json()Jackson2ObjectMapperBuilder#json()
     * @since 1.0.0
     */
    public MappingApiJackson2HttpMessageConverter(ObjectMapper objectMapper) {
        super(objectMapper,
            initWriteObjectMapper(objectMapper),
            MediaType.APPLICATION_JSON,
            new MediaType("application", "*+json"));
    }

    /**
     * 初始化负责序列化的 ObjectMapper, 设置 null 处理.
     * 如果需要扩展, 只需要重写 setSerializerFactory 即可.
     *
     * @param readObjectMapper read object mapper
     * @return the object mapper
     * @since 1.0.0
     */
    private static @NotNull ObjectMapper initWriteObjectMapper(@NotNull ObjectMapper readObjectMapper) {
        // 拷贝 readObjectMapper
        ObjectMapper writeObjectMapper = readObjectMapper.copy();
        writeObjectMapper.setSerializerFactory(writeObjectMapper.getSerializerFactory()
            .withSerializerModifier(new DefaultBeanSerializerModifier()));
        return writeObjectMapper;
    }

    /**
     * Specify a custom prefix to use for this view's JSON output.
     * Default is none.
     *
     * @param jsonPrefix jsonPrefix
     * @see #setPrefixJson #setPrefixJson#setPrefixJson#setPrefixJson#setPrefixJson#setPrefixJson
     * @since 1.0.0
     */
    public void setJsonPrefix(@Nullable String jsonPrefix) {
        this.jsonPrefix = jsonPrefix;
    }

    /**
     * Indicate whether the JSON output by this view should be prefixed with ")]}', ". Default is false.
     * <p>Prefixing the JSON string in this manner is used to help prevent JSON Hijacking.
     * The prefix renders the string syntactically invalid as a script so that it cannot be hijacked.
     * This prefix should be stripped before parsing the string as JSON.
     *
     * @param prefixJson prefixJson
     * @see #setJsonPrefix #setJsonPrefix#setJsonPrefix#setJsonPrefix#setJsonPrefix#setJsonPrefix
     * @since 1.0.0
     */
    public void setPrefixJson(boolean prefixJson) {
        this.jsonPrefix = (prefixJson ? ")]}', " : null);
    }

    /**
     * Write prefix *
     *
     * @param generator generator
     * @param object    object
     * @throws IOException io exception
     * @since 1.0.0
     */
    @Override
    protected void writePrefix(@NotNull JsonGenerator generator, @NotNull Object object) throws IOException {
        if (this.jsonPrefix != null) {
            generator.writeRaw(this.jsonPrefix);
        }
    }

}
