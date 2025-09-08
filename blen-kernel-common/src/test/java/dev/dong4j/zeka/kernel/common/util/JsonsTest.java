package dev.dong4j.zeka.kernel.common.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import dev.dong4j.zeka.kernel.common.api.R;
import dev.dong4j.zeka.kernel.common.api.Result;
import dev.dong4j.zeka.kernel.common.exception.LowestException;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.27 18:25
 * @since 1.0.0
 */
@Slf4j
class JsonsTest {
    /**
     * Test
     *
     * @since 1.0.0
     */
    @Test
    void test() {
        log.info("{}", Jsons.toJson(null));
    }

    /**
     * Test 1
     *
     * @since 1.0.0
     */
    @Test
    void test_1() {
        Result<?> result = R.succeed();

        log.info("{}", Jsons.toJson(result));
        log.info("{}", Jsons.toJson(result, true));

        String json = "{\"code\":2000,\"success\":true,\"data\":null,\"message\":\"处理成功\",\"traceId\":\"\"}";

        log.info("{}", Jsons.toJson(json));
        log.info("{}", Jsons.toJson(json, true));

    }

    /**
     * Test 2
     *
     * @since 1.0.0
     */
    @Test
    void test_2() {
        String json = "{}";
        log.info("{}", Jsons.parse(json, User.class));

        log.info("{}", Jsons.toJson(User.builder().username("xxxx").bigDecimal(null).build()));
        log.info("{}", new Gson().toJson(User.builder().username("xxxx").bigDecimal(null).build()));
    }


    /**
     * Test 3
     *
     * @since 1.0.0
     */
    @Test
    void test_3() {
        String json = "{\n" +
            "\t\"code\": 2000,\n" +
            "\t\"success\": true,\n" +
            "\t\"message\": \"xxxx\",\n" +
            "\t\"data\": \"\"\n" +
            "}";
        log.info("{}", Jsons.parse(Jsons.readTree(json).path("data").toString(), User.class));
    }

    /**
     * Test 4
     *
     * @since 1.0.0
     */
    @Test
    void test_4() {
        String json = "{\n" +
            "\t\"code\": 2000,\n" +
            "\t\"success\": true,\n" +
            "\t\"message\": \"xxxx\",\n" +
            "\t\"data\": {}\n" +
            "}";
        log.info("{}", Jsons.parse(Jsons.readTree(json).path("data").toString(), Void.class));
    }

    /**
     * <p>Description: </p>
     *
     * @author dong4j
     * @version 1.0.0
     * @email "mailto:dong4j@gmail.com"
     * @date 2020.01.27 18:25
     * @since 1.0.0
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    private static class User {
        /** Username */
        private String username;
        /** Big decimal */
        private BigDecimal bigDecimal;
    }

    /**
     * Test 5
     *
     * @since 1.0.0
     */
    @Test
    void test_5() {
        String json = "{\n" +
            "\t\"code\": 2000,\n" +
            "\t\"success\": true,\n" +
            "\t\"message\": \"xxxx\",\n" +
            "\t\"data\": {}\n" +
            "}";
        log.info("{}", convertor(json, new TypeReference<Result<Void>>() {
        }));
    }

    /**
     * Test 6
     *
     * @since 1.0.0
     */
    @Test
    void test_6() {
        String json = "{\n" +
            "\t\"code\": 2000,\n" +
            "\t\"success\": true,\n" +
            "\t\"message\": \"xxxx\",\n" +
            "\t\"data\": \"\"\n" +
            "}";
        log.info("{}", convertor(json, new TypeReference<Result<Void>>() {
        }));
    }

    /**
     * Test 7
     *
     * @since 1.0.0
     */
    @Test
    void test_7() {
        String json = "{\n" +
            "\t\"code\": 2000,\n" +
            "\t\"success\": true,\n" +
            "\t\"message\": \"xxxx\",\n" +
            "\t\"data\": {}\n" +
            "}";
        log.info("{}", convertor(json, new TypeReference<Result<User>>() {
        }));
    }

    /**
     * Test 8
     *
     * @since 1.0.0
     */
    @Test
    void test_8() {
        String json = "{\n" +
            "\t\"code\": 2000,\n" +
            "\t\"success\": true,\n" +
            "\t\"message\": \"xxxx\",\n" +
            "\t\"data\": \"\"\n" +
            "}";
        log.info("{}", convertor(json, new TypeReference<Result<User>>() {
        }));
    }

    /**
     * Test 9
     *
     * @since 1.0.0
     */
    @Test
    void test_9() {
        String json = "{\n" +
            "\t\"code\": 2000,\n" +
            "\t\"success\": true,\n" +
            "\t\"message\": \"xxxx\",\n" +
            "\t\"data\": {}\n" +
            "}";
        log.info("{}", convertor2(json, new TypeReference<User>() {
        }));
    }

    /**
     * Test 10
     *
     * @since 1.0.0
     */
    @Test
    void test_10() {
        String json = "{\n" +
            "\t\"code\": 2000,\n" +
            "\t\"success\": true,\n" +
            "\t\"message\": \"xxxx\",\n" +
            "\t\"data\": \"\"\n" +
            "}";
        log.info("{}", convertor2(json, new TypeReference<User>() {
        }));
    }

    /**
     * Test 11
     *
     * @since 1.0.0
     */
    @Test
    void test_11() {
        String json = "{\n" +
            "  \"code\": 2000,\n" +
            "  \"success\": true,\n" +
            "  \"message\": \"xxxx\",\n" +
            "  \"data\": \"xxxxx\"\n" +
            "}";
        log.info("{}", convertor2(json, new TypeReference<String>() {
        }));
    }

    /**
     * Test trim
     *
     * @since 1.0.0
     */
    @Test
    void test_trim() {
        log.info("{}", Jsons.toJson(User.builder().username("  xxxx  ").bigDecimal(null).build()));
    }

    /**
     * Convertor result
     *
     * @param <T>           parameter
     * @param json          json
     * @param typeReference type reference
     * @return the result
     * @since 1.0.0
     */
    private static <T> Result<T> convertor(String json, TypeReference<Result<T>> typeReference) {
        return R.succeed(convertor2(json, new TypeReference<T>() {
        }));
    }

    /**
     * 推荐使用的转换方法, 返回 data
     *
     * @param <T> parameter
     * @param str str
     * @param clz clz
     * @return the t
     * @since 1.0.0
     */
    private static <T> T convertor2(String str, TypeReference<T> clz) {
        log.debug("[{}]", str);
        if (Jsons.readTree(str).path(R.SUCCESS).asBoolean()) {
            return Jsons.parse(Jsons.readTree(str).path(R.DATA).toString(), clz);
        } else {
            // 只要 code != 2000, 则全部抛出异常 unchecked 异常
            throw new LowestException(Jsons.readTree(str).path(R.CODE).asInt(), Jsons.readTree(str).path(R.MESSAGE).asText());
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class Bean1 {

        private String p1;
        private BigDecimal p2;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class Bean2 {

        private String p1;
        private Object p2;

    }

    /**
     * 测试处理 BigDecimal 精度丢失问题
     */
    @Test
    void test_bigdecimal1() {
        ObjectMapper mapper = Jsons.getInstance();
        mapper.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);

        Bean1 bean1 = new Bean1("haha1", new BigDecimal("1.00"));
        Bean2 bean2 = new Bean2("haha2", new BigDecimal("2.00"));

        String bs1 = Jsons.toJson(bean1);
        String bs2 = Jsons.toJson(bean2);

        log.info("{}", bs1);
        log.info("{}", bs2);

        Bean1 b1 = Jsons.parse(bs1, Bean1.class);
        log.info("{}", b1);

        Bean2 b2 = Jsons.parse(bs2, Bean2.class);
        log.info("{}", b2);

        Assertions.assertEquals(bean1.getP2(), b1.getP2());
        Assertions.assertEquals(bean2.getP2(), b2.getP2());
    }

    /**
     * 另一种方式处理 BigDecimal 精度丢失
     */
    @Test
    void test_bigdecimal2() {
        ObjectMapper mapper = Jsons.getCopyMapper();
        mapper.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);

        Bean1 bean1 = new Bean1("haha1", new BigDecimal("1.00"));
        Bean2 bean2 = new Bean2("haha2", new BigDecimal("2.00"));

        String bs1 = Jsons.toJson(mapper, bean1);
        String bs2 = Jsons.toJson(mapper, bean2);

        log.info("{}", bs1);
        log.info("{}", bs2);

        Bean1 b1 = Jsons.parse(mapper, bs1, Bean1.class);
        log.info("{}", b1);

        Bean2 b2 = Jsons.parse(mapper, bs2, Bean2.class);
        log.info("{}", b2);

        Assertions.assertEquals(bean1.getP2(), b1.getP2());
        Assertions.assertEquals(bean2.getP2(), b2.getP2());
    }


    @Test
    void test_xx() {
        String xx = "{\"sysCode\":\"srm\",\"orgUsers\":[{\"projectCode\":\"FCXM202209050003\",\"orgName\":\"供应商名字：马上消费\"," +
            "\"userName\":\"jun.li-n@msxf.com\",\"realName\":\"张安\",\"mobilePhone\":\"13212345167\"}]}";


        log.info("{}", Jsons.readTree(xx).path("orgUsers").toString());
    }

    @Test
    void xxx() {
        int aa = 10 % 1000 == 0 ? 10 / 1000 : 1;
        for (int i = 0; i < aa; i++) {
            log.info("xxx");
        }
    }

}
