package dev.dong4j.zeka.kernel.validation.constraints;

import dev.dong4j.zeka.kernel.validation.AuthContextConfiguration;
import dev.dong4j.zeka.kernel.validation.util.BeanValidator;
import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Map;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

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
class EnumXTest extends AuthContextConfiguration {

    /**
     * 只使用 @VehicleNumber , 可以为空
     *
     * @since 1.0.0
     */
    @Test
    void test_1() {
        try {
            Map<String, String> validateobject = BeanValidator.validateobject(TestForm1.builder()
                .state(null)
                .statusStr("")
                .statusName("")
                .build());
            if (validateobject != null && !validateobject.isEmpty()) {
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
    private static class TestForm1 implements Serializable {
        /** serialVersionUID */
        private static final long serialVersionUID = -5274383672719713886L;
        /** Plate no */
        @EnumX(value = Test3Status.class, message = "可选值: value:1/2/3, name:XXXX/YYYY/ZZZZ")
        private Test3Status state;
        @EnumX(value = Test3Status.class, message = "可选值: value:1/2/3")
        private String statusStr;
        @EnumX(value = Test3Status.class, message = "可选值: name:XXXX/YYYY/ZZZZ")
        private String statusName;
    }

    /**
     * 车牌号不能为空
     *
     * @since 1.0.0
     */
    @Test
    void test_2() {
        try {
            Map<String, String> validateobject = BeanValidator.validateobject(TestForm2.builder()
                .state(null)
                .statusStr("")
                .statusName("")
                .build());
            if (validateobject != null && !validateobject.isEmpty()) {
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
    private static class TestForm2 implements Serializable {
        /** serialVersionUID */
        private static final long serialVersionUID = -5274383672719713886L;
        /** Plate no */
        @EnumX(value = Test3Status.class, message = "可选值: value:1/2/3, name:XXXX/YYYY/ZZZZ")
        private Test3Status state;
        @EnumX(value = Test3Status.class, message = "可选值: value:1/2/3")
        @NotBlank(message = "状态(value)不能为空")
        private String statusStr;
        @EnumX(value = Test3Status.class, message = "可选值: name:XXXX/YYYY/ZZZZ")
        @NotBlank(message = "状态(name)不能为空")
        private String statusName;
    }

    /**
     * 车牌号格式错误
     *
     * @since 1.0.0
     */
    @Test
    void test_3() {
        try {
            Map<String, String> validateobject = BeanValidator.validateobject(TestForm3.builder().state(Test3Status.XXXX).build());
            if (validateobject != null && !validateobject.isEmpty()) {
                for (Map.Entry<String, String> entry : validateobject.entrySet()) {
                    log.error("[{}]->[{}]", entry.getKey(), entry.getValue());
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 车牌号格式正确
     *
     * @since 1.0.0
     */
    @Test
    void test_4() {
        try {
            Map<String, String> validateobject = BeanValidator.validateobject(TestForm3.builder().statusStr("1").build());
            if (validateobject != null && !validateobject.isEmpty()) {
                for (Map.Entry<String, String> entry : validateobject.entrySet()) {
                    log.error("[{}]->[{}]", entry.getKey(), entry.getValue());
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Test
    void test_5() {
        try {
            Map<String, String> validateobject = BeanValidator.validateobject(TestForm3.builder().statusStr("4").build());
            if (validateobject != null && !validateobject.isEmpty()) {
                for (Map.Entry<String, String> entry : validateobject.entrySet()) {
                    log.error("[{}]->[{}]", entry.getKey(), entry.getValue());
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Test
    void test_6() {
        try {
            Map<String, String> validateobject = BeanValidator.validateobject(TestForm3.builder().statusName("XXXX").build());
            if (validateobject != null && !validateobject.isEmpty()) {
                for (Map.Entry<String, String> entry : validateobject.entrySet()) {
                    log.error("[{}]->[{}]", entry.getKey(), entry.getValue());
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Test
    void test_7() {
        try {
            Map<String, String> validateobject = BeanValidator.validateobject(TestForm3.builder().statusName("OOOO").build());
            if (validateobject != null && !validateobject.isEmpty()) {
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
    private static class TestForm3 implements Serializable {
        private static final long serialVersionUID = -5274383672719713886L;
        @EnumX(value = Test3Status.class, message = "可选值: value:1/2/3, name:XXXX/YYYY/ZZZZ")
        private Test3Status state;
        @EnumX(value = Test3Status.class, message = "可选值: value:1/2/3")
        private String statusStr;
        @EnumX(value = Test3Status.class, message = "可选值: name:XXXX/YYYY/ZZZZ")
        private String statusName;
    }
}
