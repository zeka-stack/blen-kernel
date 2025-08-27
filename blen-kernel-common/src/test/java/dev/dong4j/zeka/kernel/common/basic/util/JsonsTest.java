package dev.dong4j.zeka.kernel.common.basic.util;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import dev.dong4j.zeka.kernel.common.util.Jsons;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * <p>Description: Jackson.enableDefaultTyping 测试 </p>
 *
 * @author dong4j
 * @version 1.2.4
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.02.19 02:37
 * @since 1.0.0
 */
public class JsonsTest {
    /** userString */
    private static final String userString =
        "{\"id\":null,\"username\":\"admin\",\"password\":\"admin\"}";
    /** userStringWithDefaultType */
    private static final String userStringWithDefaultType =
        "[\"dev.dong4j.zeka.kernel.common.util.basic.UserInfo\",{\"id\":null,\"username\":\"admin\",\"password\":\"admin\"}]";


    /**
     * 直接使用 ObjectMapping
     *
     * @throws JsonProcessingException json processing exception
     * @since 1.0.0
     */
    @Test
    public void serialize() throws JsonProcessingException {
        CustomSerializer<?> serializer = new CustomSerializer<>(Object.class);
        String s = serializer.serialize(this.createUser());
        assertEquals(s, userString);
    }

    /**
     * Create user user info
     *
     * @return the user info
     * @since 1.0.0
     */
    private @NotNull UserInfo createUser() {
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername("admin");
        userInfo.setPassword("admin");
        return userInfo;
    }

    /**
     * 设置了 DefaultTyping 的 ObjectMapper
     *
     * @throws JsonProcessingException json processing exception
     * @since 1.0.0
     */
    @Test
    public void serializeWithDefaultType() throws JsonProcessingException {
        CustomSerializer<?> serializer = new CustomSerializer<>(Object.class);
        serializer.setObjectMapper(this.createObjectMapping());
        String s = serializer.serialize(this.createUser());
        assertEquals(s, userStringWithDefaultType);
    }

    /**
     * Create object mapping object mapper
     *
     * @return the object mapper
     * @since 1.0.0
     */
    private @NotNull ObjectMapper createObjectMapping() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        return objectMapper;
    }

    /**
     * 直接使用 ObjectMapping 的反序列化,需要给定序列化的类
     *
     * @throws IOException io exception
     * @since 1.0.0
     */
    @Test
    public void deserialize() throws IOException {
        CustomSerializer<UserInfo> serializer = new CustomSerializer<>(UserInfo.class);
        UserInfo userInfo = serializer.deserialize(userString);
        System.out.println(userInfo);
        assertEquals(userInfo, this.createUser());
    }

    /**
     * 使用设置了 DefaultTyping 的 ObjectMapping 来反序列化,可以使用 Object 达到想要的效果
     *
     * @throws IOException io exception
     * @since 1.0.0
     */
    @Test
    public void deserializeWithDefaultTyping() throws IOException {
        CustomSerializer<?> serializer = new CustomSerializer<>(Object.class);
        serializer.setObjectMapper(this.createObjectMapping());
        UserInfo userInfo = (UserInfo) serializer.deserialize(userStringWithDefaultType);
        System.out.println(userInfo);
        assertEquals(userInfo, this.createUser());
    }

    /**
     * 测试 isjson() 是否为将string字符串
     * {"name": "zhangfei"}___xxxx
     * 这样格式的数据判断为true
     *
     * @since 2022.1.1
     */
    @Test
    void test_jackson_isJson() {
        String str = "xxxxx_{\n" +
            "    \"a\": \"{\\\"name\\\": \\\"zhangfei\\\"}\",\n" +
            "    \"b\": \"asdasd\",\n" +
            "    \"c\": \"qwdqwdq\",\n" +
            "    \"d\": {\n" +
            "        \"name\": \"name\",\n" +
            "        \"bbbb\": \"bbbb\",\n" +
            "        \"cccc\": {\n" +
            "            \"aaaa\": [{\n" +
            "                \"ccc\": \"dddd\"\n" +
            "            }]\n" +
            "        }\n" +
            "    }\n" +
            "}_____xxxxx";
        System.out.println(Jsons.toJson(str));
        assertFalse(Jsons.isJson(str));
    }

    /**
     * 测试正常情况下的json判断
     *
     * @since 2022.1.1
     */
    @Test
    void test_normal_json() {
        String str = "{\n" +
            "    \"a\": \"{\\\"name\\\": \\\"zhangfei\\\"}____json\",\n" +
            "    \"b\": \"asdasd\",\n" +
            "    \"c\": \"qwdqwdq\",\n" +
            "    \"d\": {\n" +
            "        \"name\": \"name\",\n" +
            "        \"bbbb\": \"bbbb\",\n" +
            "        \"cccc\": {\n" +
            "            \"aaaa\": [{\n" +
            "                \"ccc\": \"dddd\"\n" +
            "            }]\n" +
            "        }____xxxxx\n" +
            "    }\n" +
            "}";
        System.out.println(Jsons.toJson(str));
        assertTrue(Jsons.isJson(str));
    }

    /**
     * 测试jsonmap的方式是否判断通过
     *
     * @since 2022.1.1
     */
    @Test
    void test_json_map() {
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("key1", "kkkk");
        jsonMap.put("key2", "kkkk");
        jsonMap.put("key3", "kkkk");
        jsonMap.put("key4", "kkkk");
        Map<String, Object> jsonMap1 = new HashMap<>();
        jsonMap1.put("jmap1", "123");
        Map<String, Object> jsonMap2 = new HashMap<>();
        jsonMap.put("kkkkkkkkk2", "kkkkkkkkkkkk");
        jsonMap1.put("jmap2", jsonMap2);
        jsonMap.put("key5", jsonMap1);
        String json = Jsons.toJson(jsonMap);
        System.out.println(json);
        assertTrue(Jsons.isJson(json));
    }

    /**
     * <p>Description: </p>
     *
     * @param <T> parameter
     * @author dong4j
     * @version 1.2.4
     * @email "mailto:dong4j@gmail.com"
     * @date 2020.02.19 14:38
     * @since 1.0.0
     */
    private static class CustomSerializer<T> {
        /** Java type */
        private final JavaType javaType;
        /** Object mapper */
        private ObjectMapper objectMapper = new ObjectMapper();

        /**
         * Custom serializer
         *
         * @param javaType java type
         * @since 1.0.0
         */
        public CustomSerializer(JavaType javaType) {
            this.javaType = javaType;
        }

        /**
         * Custom serializer
         *
         * @param clazz clazz
         * @since 1.0.0
         */
        CustomSerializer(Class<T> clazz) {
            this.javaType = TypeFactory.defaultInstance().constructType(clazz);
        }

        /**
         * Sets object mapper *
         *
         * @param objectMapper object mapper
         * @since 1.0.0
         */
        void setObjectMapper(ObjectMapper objectMapper) {
            this.objectMapper = objectMapper;
        }

        /**
         * Serialize string
         *
         * @param o o
         * @return the string
         * @throws JsonProcessingException json processing exception
         * @since 1.0.0
         */
        String serialize(Object o) throws JsonProcessingException {
            return this.objectMapper.writeValueAsString(o);
        }

        /**
         * Deserialize t
         *
         * @param s s
         * @return the t
         * @throws IOException io exception
         * @since 1.0.0
         */
        T deserialize(String s) throws IOException {
            return this.objectMapper.readValue(s, this.javaType);
        }
    }
}
