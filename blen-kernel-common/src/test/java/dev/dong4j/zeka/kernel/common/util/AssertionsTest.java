package dev.dong4j.zeka.kernel.common.util;

import dev.dong4j.zeka.kernel.common.asserts.Assertions;
import dev.dong4j.zeka.kernel.common.exception.LowestException;
import java.io.Serial;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * <p>Description:  </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.11.17 23:32
 * @since 1.0.0
 */
@Slf4j
@SuppressWarnings("all")
class AssertionsTest {

    @Test
    void notBlank() {
        org.junit.jupiter.api.Assertions.assertThrows(LowestException.class, () -> Assertions.notBlank(""));
        org.junit.jupiter.api.Assertions.assertThrows(LowestException.class, () -> Assertions.notBlank("    "));
        org.junit.jupiter.api.Assertions.assertThrows(LowestException.class, () -> Assertions.notBlank(null));
        org.junit.jupiter.api.Assertions.assertThrows(LowestException.class, () -> Assertions.notBlank("", "自定义消息"));
        org.junit.jupiter.api.Assertions.assertThrows(ConsumerException.class, () -> Assertions.notBlank("", () -> new ConsumerException(
            "自定义消息")));

        Assertions.notBlank("aaa");
    }

    @Test
    void notNull() {
        org.junit.jupiter.api.Assertions.assertThrows(LowestException.class, () -> Assertions.notNull(null));
        org.junit.jupiter.api.Assertions.assertThrows(LowestException.class, () -> Assertions.notNull(null, "自定义消息"));
        org.junit.jupiter.api.Assertions.assertThrows(ConsumerException.class, () -> Assertions.notNull(null,
            () -> new ConsumerException(
                "自定义消息")));

        Assertions.notNull(new Object());
    }


    @Test
    void isNull() {
        org.junit.jupiter.api.Assertions.assertThrows(LowestException.class, () -> Assertions.isNull(new Object()));
        org.junit.jupiter.api.Assertions.assertThrows(LowestException.class, () -> Assertions.isNull(new Object(), "自定义消息"));
        org.junit.jupiter.api.Assertions.assertThrows(ConsumerException.class, () -> Assertions.isNull(new Object(),
            () -> new ConsumerException(
                "自定义消息")));

        Assertions.isNull(null);
    }

    @Test
    void isTrue() {
        org.junit.jupiter.api.Assertions.assertThrows(LowestException.class, () -> Assertions.isTrue(false));
        org.junit.jupiter.api.Assertions.assertThrows(LowestException.class, () -> Assertions.isTrue(false, "自定义消息"));
        org.junit.jupiter.api.Assertions.assertThrows(ConsumerException.class, () -> Assertions.isTrue(false,
            () -> new ConsumerException(
                "自定义消息")));

        Assertions.isTrue(true);
    }

    @Test
    void isFalse() {
        org.junit.jupiter.api.Assertions.assertThrows(LowestException.class, () -> Assertions.isFalse(true));
        org.junit.jupiter.api.Assertions.assertThrows(LowestException.class, () -> Assertions.isFalse(true, "自定义消息"));
        org.junit.jupiter.api.Assertions.assertThrows(ConsumerException.class, () -> Assertions.isFalse(true,
            () -> new ConsumerException(
                "自定义消息")));

        Assertions.isFalse(false);
    }

    @Test
    void doesNotContain() {
        org.junit.jupiter.api.Assertions.assertThrows(LowestException.class, () -> Assertions.doesNotContain("abc", "b"));
        org.junit.jupiter.api.Assertions.assertThrows(LowestException.class, () -> Assertions.doesNotContain("abc", "b", "自定义消息"));
        org.junit.jupiter.api.Assertions.assertThrows(ConsumerException.class, () -> Assertions.doesNotContain("abc", "b",
            () -> new ConsumerException("自定义消息")));

        Assertions.doesNotContain("abc", "d");
    }


    @Test
    void notEmpty_map() {
        Map<String, String> map = null;
        org.junit.jupiter.api.Assertions.assertThrows(LowestException.class, () -> Assertions.notEmpty(map));
        org.junit.jupiter.api.Assertions.assertThrows(LowestException.class, () -> Assertions.notEmpty(map, "自定义消息"));
        org.junit.jupiter.api.Assertions.assertThrows(ConsumerException.class, () -> Assertions.notEmpty(map,
            () -> new ConsumerException(
                "自定义消息")));

        Map<String, String> map2 = Collections.emptyMap();
        Assertions.notEmpty(map2);
    }

    @Test
    void notEmpty_collection() {
        Collection<String> collection = null;
        org.junit.jupiter.api.Assertions.assertThrows(LowestException.class, () -> Assertions.notEmpty(collection));
        org.junit.jupiter.api.Assertions.assertThrows(LowestException.class, () -> Assertions.notEmpty(collection, "自定义消息"));
        org.junit.jupiter.api.Assertions.assertThrows(ConsumerException.class, () -> Assertions.notEmpty(collection,
            () -> new ConsumerException(
                "自定义消息")));

        Collection<String> collection2 = Collections.singletonList("test");
        Assertions.notEmpty(collection2);
    }

    @Test
    void notEmpty_object_array() {

    }

    @Test
    void notEmpty_byte_array() {

    }

    @Test
    void noNullElements() {
    }

    @Test
    void testNoNullElements() {
    }

    @Test
    void testNoNullElements1() {
    }

    @Test
    void testNotEmpty5() {
    }

    @Test
    void testNotEmpty6() {
    }

    @Test
    void testNotEmpty7() {
    }

    @Test
    void testNoNullElements2() {
    }

    @Test
    void testNoNullElements3() {
    }

    @Test
    void testNoNullElements4() {
    }

    @Test
    void testNotEmpty8() {
    }

    @Test
    void testNotEmpty9() {
    }

    @Test
    void testNotEmpty10() {
    }

    @Test
    void isInstanceOf() {
    }

    @Test
    void testIsInstanceOf() {
    }

    @Test
    void isAssignable() {
    }

    @Test
    void testIsAssignable() {
    }

    @Test
    void fail() {
    }

    @Test
    void testFail() {
    }

    @Test
    void testFail1() {
    }

    public static class ConsumerException extends LowestException {
        @Serial
        private static final long serialVersionUID = 3168856664507650214L;

        public ConsumerException() {
            super();
        }

        /**
         * Consumer exception
         *
         * @param message message
         * @since 1.0.0
         */
        public ConsumerException(String message) {
            super(message);
        }


    }
}
