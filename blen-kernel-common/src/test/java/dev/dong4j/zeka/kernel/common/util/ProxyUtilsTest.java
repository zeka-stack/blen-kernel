package dev.dong4j.zeka.kernel.common.util;

import cn.hutool.aop.ProxyUtil;
import cn.hutool.aop.aspects.TimeIntervalAspect;
import cn.hutool.core.lang.Console;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * <p>Description: java 动态代理, 每次都会创建一个新的实例 </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.03.27 10:53
 * @since 1.0.0
 */
@Slf4j
class ProxyUtilsTest {

    /**
     * Test 1
     *
     * @since 1.0.0
     */
    @Test
    void test_1() {
        Tools.repeat(1000, () -> {
            Animal cat = ProxyUtil.proxy(new Cat(), TimeIntervalAspect.class);
            cat.eat();
            log.info("{}", cat.getClass());
        });
    }

    /**
     * <p>Description: </p>
     *
     * @author dong4j
     * @version 1.0.0
     * @email "mailto:dong4j@gmail.com"
     * @date 2020.03.27 11:00
     * @since 1.0.0
     */
    public interface Animal {
        /**
         * Eat
         *
         * @since 1.0.0
         */
        void eat();
    }

    /**
     * <p>Description: </p>
     *
     * @author dong4j
     * @version 1.0.0
     * @email "mailto:dong4j@gmail.com"
     * @date 2020.03.27 11:00
     * @since 1.0.0
     */
    public static class Cat implements Animal {
        /**
         * Eat
         *
         * @since 1.0.0
         */
        @Override
        public void eat() {
            Console.log("猫吃鱼");
        }
    }
}
