package dev.dong4j.zeka.kernel.validation.constraints;

import dev.dong4j.zeka.kernel.common.util.StringPool;
import dev.dong4j.zeka.kernel.validation.AuthContextConfiguration;
import dev.dong4j.zeka.kernel.validation.util.BeanValidator;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import javax.validation.constraints.NotBlank;
import java.util.Map;

/**
 * <p>Description:  </p>
 *
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dong4j@gmail.com"
 * @date 2019.12.27 13:19
 * @since 1.0.0
 */
@Slf4j
class JsonTest extends AuthContextConfiguration {

    /**
     * <p>Description: </p>
     *
     * @author dong4j
     * @version 1.2.3
     * @email "mailto:dong4j@gmail.com"
     * @date 2019.12.27 13:25
     * @since 1.0.0
     */
    @Data
    @Builder
    private static class TestForm1 {
        /** serialVersionUID */
        private static final long serialVersionUID = -5274383672719713886L;
        /** Json */
        @Json
        private String json;
    }

    /**
     * 只使用 @Json , 可以为空
     *
     * @since 1.0.0
     */
    @Test
    void test_1() {
        try {
            Map<String, String> validateobject = BeanValidator.validateobject(TestForm1.builder().json("").build());
            if (validateobject != null && validateobject.size() > 0) {
                for (Map.Entry<String, String> entry : validateobject.entrySet()) {
                    log.error("[{}]->[{}]", entry.getKey(), entry.getValue());
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * json 字段不能为空
     *
     * @since 1.0.0
     */
    @Test
    void test_2() {
        try {
            Map<String, String> validateobject = BeanValidator.validateobject(TestForm2.builder().json("").build());
            if (validateobject != null && validateobject.size() > 0) {
                for (Map.Entry<String, String> entry : validateobject.entrySet()) {
                    log.error("[{}]->[{}]", entry.getKey(), entry.getValue());
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * <p>Description: </p>
     *
     * @author dong4j
     * @version 1.2.3
     * @email "mailto:dong4j@gmail.com"
     * @date 2019.12.27 13:25
     * @since 1.0.0
     */
    @Data
    @Builder
    private static class TestForm2 {
        /** serialVersionUID */
        private static final long serialVersionUID = -5274383672719713886L;
        /** Json */
        @Json
        @NotBlank(message = "json 字段不能为空")
        private String json;
    }

    /**
     * json 验证失败
     *
     * @since 1.0.0
     */
    @Test
    void test_3() {
        try {
            Map<String, String> validateobject = BeanValidator.validateobject(TestForm3.builder().json("xxx").build());
            if (validateobject != null && validateobject.size() > 0) {
                for (Map.Entry<String, String> entry : validateobject.entrySet()) {
                    log.error("[{}]->[{}]", entry.getKey(), entry.getValue());
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * json 验证成功
     *
     * @since 1.0.0
     */
    @Test
    void test_4() {
        try {
            Map<String, String> validateobject = BeanValidator.validateobject(TestForm3.builder().json(StringPool.EMPTY_JSON).build());
            if (validateobject != null && validateobject.size() > 0) {
                for (Map.Entry<String, String> entry : validateobject.entrySet()) {
                    log.error("[{}]->[{}]", entry.getKey(), entry.getValue());
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * <p>Description: </p>
     *
     * @author dong4j
     * @version 1.2.3
     * @email "mailto:dong4j@gmail.com"
     * @date 2019.12.27 13:25
     * @since 1.0.0
     */
    @Data
    @Builder
    private static class TestForm3 {
        /** serialVersionUID */
        private static final long serialVersionUID = -5274383672719713886L;
        /** Json */
        @Json
        private String json;
    }
}
