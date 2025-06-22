package dev.dong4j.zeka.kernel.common.util;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Contract;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.27 18:25
 * @since 1.0.0
 */
@Slf4j
class EnumUtilsTest {

    /**
     * Test enums utils
     *
     * @since 1.0.0
     */
    @Test
    void testEnumsUtils() {

        Optional<SexEnum> m = EnumUtils.of(SexEnum.class, e -> e.getCode().equals("W"));
        log.info(m.map(SexEnum::getDesc).orElse(null));
        Optional<SexEnum> m1 = EnumUtils.of(SexEnum.class, e -> e.getValue().equals(0));
        log.info(m1.map(SexEnum::getDesc).orElse(null));

    }

    /**
     * Test add
     *
     * @since 1.7.0
     */
    @Test
    void test_add() {
        log.info("{}", Arrays.toString(CodeInfoEnum.values()));

        EnumUtils.DynamicEnum.addEnum(CodeInfoEnum.class,
            "UNKNOW",
            new Class<?>[]{Long.class, Long.class, String.class, String.class},
            new Object[]{2L, 3L, "ActiveStatus", "Active"});

        log.info("{}", Arrays.toString(CodeInfoEnum.values()));
        log.info("{}", CodeInfoEnum.valueOf("UNKNOW"));
    }

    /**
     * <p>Description: </p>
     *
     * @author dong4j
     * @version 1.0.0
     * @email "mailto:dong4j@gmail.com"
     * @date 2020.12.05 14:44
     * @since 1.7.0
     */
    public enum CodeInfoEnum {
        /** Lock code info enum */
        LOCK(1L, 1L, "LOCK_TYPE", "LOCK"),
        /** Unlock code info enum */
        UNLOCK(1L, 2L, "LOCK_TYPE", "LOCK");

        /** Class id */
        public Long classId;
        /** Info id */
        public Long infoId;
        /** Class code */
        public String classCode;
        /** Info code */
        public String infoCode;

        /**
         * Code info enum
         *
         * @param classId   class id
         * @param infoId    info id
         * @param classCode class code
         * @param infoCode  info code
         * @since 1.7.0
         */
        CodeInfoEnum(Long classId, Long infoId, String classCode, String infoCode) {
            this.classId = classId;
            this.infoId = infoId;
            this.classCode = classCode;
            this.infoCode = infoCode;
        }

        /**
         * Gets by info id *
         *
         * @param infoId info id
         * @return the by info id
         * @since 1.7.0
         */
        public static CodeInfoEnum getByInfoId(Long infoId) {
            return CodeInfoEnum.valueOf(infoId + "");
        }

        /**
         * Gets by class id *
         *
         * @param classId class id
         * @return the by class id
         * @since 1.7.0
         */
        public static List<CodeInfoEnum> getByClassId(Long classId) {
            return Arrays.stream(CodeInfoEnum.values()).filter(item -> item.classId.equals(classId)).collect(Collectors.toList());
        }

        /**
         * Gets by class code and info code *
         *
         * @param classCode class code
         * @param infoCode  info code
         * @return the by class code and info code
         * @since 1.7.0
         */
        public static CodeInfoEnum getByClassCodeAndInfoCode(String classCode, String infoCode) {
            Optional<CodeInfoEnum> opt =
                Arrays.stream(CodeInfoEnum.values()).filter(item -> item.classCode.equals(classCode) && item.infoCode.equals(infoCode)).findFirst();
            return opt.orElse(null);
        }

        /**
         * To string
         *
         * @return the string
         * @since 1.7.0
         */
        @Override
        public String toString() {
            return "CodeInfoEnum{" +
                "classId=" + this.classId +
                ", infoId=" + this.infoId +
                ", classCode='" + this.classCode + '\'' +
                ", infoCode='" + this.infoCode + '\'' +
                '}';
        }
    }

    /**
     * <p>Description: </p>
     *
     * @author dong4j
     * @version 1.2.3
     * @email "mailto:dong4j@gmail.com"
     * @date 2020.01.27 18:25
     * @since 1.0.0
     */
    enum SexEnum {

        /** Man sex enum */
        MAN(0, "M", "男"),
        /** Women sex enum */
        WOMEN(1, "W", "女");
        /** Value */
        private final Integer value;
        /** Code */
        private final String code;
        /** Desc */
        private final String desc;

        /**
         * Sex enum
         *
         * @param value value
         * @param code  code
         * @param desc  desc
         * @since 1.0.0
         */
        @Contract(pure = true)
        SexEnum(Integer value, String code, String desc) {
            this.value = value;
            this.code = code;
            this.desc = desc;
        }

        /**
         * Gets value *
         *
         * @return the value
         * @since 1.0.0
         */
        @Contract(pure = true)
        Integer getValue() {
            return this.value;
        }

        /**
         * Gets code *
         *
         * @return the code
         * @since 1.0.0
         */
        @Contract(pure = true)
        String getCode() {
            return this.code;
        }

        /**
         * Gets desc *
         *
         * @return the desc
         * @since 1.0.0
         */
        @Contract(pure = true)
        String getDesc() {
            return this.desc;
        }
    }

}
