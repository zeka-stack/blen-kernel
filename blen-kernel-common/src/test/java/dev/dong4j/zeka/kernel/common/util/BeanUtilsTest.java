package dev.dong4j.zeka.kernel.common.util;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * <p>Description:  </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.06 17:49
 * @since 1.0.0
 */
@Slf4j
class BeanUtilsTest {

    /**
     * Test 1
     *
     * @since 1.0.0
     */
    @Test
    void test_1() {
        A a = BeanUtils.newInstance(A.class);
        a.setItems(new ArrayList<A.B>() {
            @Serial
            private static final long serialVersionUID = 7635515497616076074L;

            {
                this.add(A.B.builder().agl("aaaa").alc("xxxxx").build());
            }
        });
        C c = BeanUtils.newInstance(C.class);
        BeanUtils.copy(a, c);
        log.info("{}", c);
    }

    /**
     * Test 2
     *
     * @since 1.0.0
     */
    @Test
    void test_2() {
        A a = BeanUtils.newInstance(A.class);
        C c = BeanUtils.newInstance(C.class);


        a.setItems(new ArrayList<A.B>() {
            @Serial
            private static final long serialVersionUID = 7635515497616076074L;

            {
                this.add(A.B.builder().agl("1111").alc("xxxxx").build());
                this.add(A.B.builder().agl("222").alc("xxxxx").build());
                this.add(A.B.builder().agl("333").alc("xxxxx").build());
                this.add(A.B.builder().agl("444").alc("xxxxx").build());
                this.add(A.B.builder().agl("555").alc("xxxxx").build());
                this.add(A.B.builder().agl("666").alc("xxxxx").build());
                this.add(A.B.builder().agl("777").alc("xxxxx").build());
            }
        });


        BeanUtils.copy(a, c);

        log.info("{}", c);
    }

    /**
     * Test apache beanutils
     *
     * @since 1.0.0
     */
    @Test
    void test_apache_beanutils() {

    }


    /**
     * <p>Description: </p>
     *
     * @author dong4j
     * @version 1.0.0
     * @email "mailto:dong4j@gmail.com"
     * @date 2020.05.08 21:40
     * @since 1.0.0
     */
    @Data
    static class A {
        /** Items */
        private List<B> items;

        /**
         * <p>Description: </p>
         *
         * @author dong4j
         * @version 1.0.0
         * @email "mailto:dong4j@gmail.com"
         * @date 2019.12.25 00:34
         * @since 1.0.0
         */
        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        @Accessors(chain = true)
        @ToString(callSuper = true)
        static class B implements Serializable {
            /** serialVersionUID */
            @Serial
            private static final long serialVersionUID = 1564103911360804698L;
            /** 正北方向夹角 */
            private String agl;
            /** 报警状态位 */
            private String alc;
        }
    }

    /**
     * <p>Description: </p>
     *
     * @author dong4j
     * @version 1.0.0
     * @email "mailto:dong4j@gmail.com"
     * @date 2020.05.08 21:40
     * @since 1.0.0
     */
    @Data
    private static class C {
        /** Items */
        private List<D> items;

        /**
         * <p>Description: </p>
         *
         * @author dong4j
         * @version 1.0.0
         * @email "mailto:dong4j@gmail.com"
         * @date 2019.12.25 00:34
         * @since 1.0.0
         */
        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        @Accessors(chain = true)
        @ToString(callSuper = true)
        static class D implements Serializable {
            /** serialVersionUID */
            @Serial
            private static final long serialVersionUID = 1564103911360804698L;
            /** 正北方向夹角 */
            private String agl;
            /** 报警状态位 */
            private String alc;
        }
    }
}
