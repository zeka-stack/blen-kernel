package dev.dong4j.zeka.kernel.common.basic.util;

import com.fasterxml.jackson.core.json.PackageVersion;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import dev.dong4j.zeka.kernel.common.constant.ConfigDefaultValue;
import dev.dong4j.zeka.kernel.common.util.Jsons;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * <p>Description:  </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.04.10 14:44
 * @since 1.0.0
 */
@Slf4j
class JsonsDateTest {

    /**
     * Test date 1
     *
     * @since 1.0.0
     */
    @Test
    void test_date_1() {


        log.info("{}", Jsons.toJson(Demo.builder().date(new Date()).build()));
    }

    /**
     * Test date 2
     *
     * @since 1.0.0
     */
    @Test
    void test_date_2() {
        Demo parse = Jsons.parse("{\"date\":\"2020-04-10 15:06:33\"}", Demo.class);
        log.info("{}", parse);
    }


    /**
     * Test local datetime 1
     *
     * @since 1.0.0
     */
    @Test
    void test_local_datetime_1() {
        log.info("{}", Jsons.toJson(Demo.builder().localDateTime(LocalDateTime.now()).build()));
    }

    /**
     * Test local datetime 2
     *
     * @since 1.0.0
     */
    @Test
    void test_local_datetime_2() {
        String json = "{\"date\":null,\"localDateTime\":\"2020-04-10T15:09:42.097\"}";

        log.info("{}", Jsons.parse(json, Demo.class));
    }

    /**
     * Test local datetime 3
     *
     * @since 1.0.0
     */
    @Test
    void test_local_datetime_3() {
        ObjectMapper mapper = Jsons.getCopyMapper();
        mapper.registerModule(new JavaTimeModule());
        log.info("{}", Jsons.toJson(mapper, Demo.builder().localDateTime(LocalDateTime.now()).build()));
    }

    /**
     * Test local datetime 4
     *
     * @since 1.0.0
     */
    @Test
    void test_local_datetime_4() {
        String json = "{\"date\":null,\"localDateTime\":\"2020-04-10 15:12:43\"}";

        ObjectMapper mapper = Jsons.getCopyMapper();
        mapper.registerModule(new JavaTimeModule());
        log.info("{}", Jsons.parse(mapper, json, Demo.class));
    }

    /**
     * Test local datetime 5
     *
     * @since 1.0.0
     */
    @Test
    void test_local_datetime_5() {
        String json = "{\"date\":null,\"localDateTime\":\"2020-04-10T15:09:42.097\"}";

        log.info("{}", Jsons.parse(json, Demo.class));
    }


    /**
     * <p>Description: </p>
     *
     * @author dong4j
     * @version 1.0.0
     * @email "mailto:dong4j@gmail.com"
     * @date 2020.04.10 15:05
     * @since 1.0.0
     */
    public static class JavaTimeModule extends SimpleModule {

        /** The constant PATTERN_DATE. */
        static final String PATTERN_DATE = "yyyy-MM-dd";
        /** 时分秒 */
        static final String PATTERN_TIME = "HH:mm:ss";
        /**
         * The constant DATETIME_FORMAT.
         */
        static final DateTimeFormatter DATETIME_FORMAT = DateTimeFormatter.ofPattern(ConfigDefaultValue.DEFAULT_DATE_FORMAT);
        /**
         * The constant DATE_FORMAT.
         */
        static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern(PATTERN_DATE);
        /**
         * The constant TIME_FORMAT.
         */
        static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern(PATTERN_TIME);
        /** serialVersionUID */
        @Serial
        private static final long serialVersionUID = -8312156928465504942L;

        /**
         * Java time module
         *
         * @since 1.0.0
         */
        JavaTimeModule() {
            super(PackageVersion.VERSION);
            this.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DATETIME_FORMAT));
            this.addDeserializer(LocalDate.class, new LocalDateDeserializer(DATE_FORMAT));
            this.addDeserializer(LocalTime.class, new LocalTimeDeserializer(TIME_FORMAT));
            this.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DATETIME_FORMAT));
            this.addSerializer(LocalDate.class, new LocalDateSerializer(DATE_FORMAT));
            this.addSerializer(LocalTime.class, new LocalTimeSerializer(TIME_FORMAT));
        }

    }

    /**
     * <p>Description: </p>
     *
     * @author dong4j
     * @version 1.0.0
     * @email "mailto:dong4j@gmail.com"
     * @date 2020.04.10 15:05
     * @since 1.0.0
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    private static class Demo implements Serializable {
        /** serialVersionUID */
        @Serial
        private static final long serialVersionUID = -4584878817888466245L;
        /** Date */
        private Date date;
        /** Local date time */
        private LocalDateTime localDateTime;
    }


}
