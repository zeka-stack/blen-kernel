package dev.dong4j.zeka.kernel.common.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * 测试类
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.04.30 00:40
 * @since 1.0.0
 */
@Slf4j
class ReflectionUtilsTest {

    /**
     * 测试获取父类的各个方法对象
     *
     * @since 1.0.0
     */
    @Test
    void testGetDeclaredMethod() {

        Object obj = new Son();

        // 获取公共方法名
        Method publicMethod = ReflectionUtils.getDeclaredMethod(obj, "publicMethod");
        log.info("{}", publicMethod.getName());

        // 获取默认方法名
        Method defaultMethod = ReflectionUtils.getDeclaredMethod(obj, "defaultMethod");
        log.info("{}", defaultMethod.getName());

        // 获取被保护方法名
        Method protectedMethod = ReflectionUtils.getDeclaredMethod(obj, "protectedMethod");
        log.info("{}", protectedMethod.getName());

        // 获取私有方法名
        Method privateMethod = ReflectionUtils.getDeclaredMethod(obj, "privateMethod");
        log.info("{}", privateMethod.getName());
    }

    /**
     * 测试调用父类的方法
     *
     * @throws Exception exception
     * @since 1.0.0
     */
    @Test
    void testInvokeMethod() throws Exception {
        Object obj = new Son();

        // 调用父类的公共方法
        ReflectionUtils.invokeMethod(obj, "publicMethod", null, null);

        // 调用父类的默认方法
        ReflectionUtils.invokeMethod(obj, "defaultMethod", null, null);

        // 调用父类的被保护方法
        ReflectionUtils.invokeMethod(obj, "protectedMethod", null, null);

        // 调用父类的私有方法
        ReflectionUtils.invokeMethod(obj, "privateMethod", null, null);
    }

    /**
     * 测试获取父类的各个属性名
     *
     * @since 1.0.0
     */
    @Test
    void testGetDeclaredField() {

        Object obj = new Son();

        // 获取公共属性名
        Field publicField = ReflectionUtils.getDeclaredField(obj, "publicField");
        log.info("{}", publicField.getName());

        // 获取默认属性名
        Field defaultField = ReflectionUtils.getDeclaredField(obj, "defaultField");
        log.info("{}", defaultField.getName());

        // 获取受保护属性名
        Field protectedField = ReflectionUtils.getDeclaredField(obj, "protectedField");
        log.info("{}", protectedField.getName());

        // 获取私有属性名
        Field privateField = ReflectionUtils.getDeclaredField(obj, "privateField");
        log.info("{}", privateField.getName());

    }

    /**
     * Test set field value
     *
     * @since 1.0.0
     */
    @Test
    void testSetFieldValue() {

        Object obj = new Son();

        log.info("{}", "原来的各个属性的值: ");
        log.info("{}", "publicField = " + ReflectionUtils.getFieldValue(obj, "publicField"));
        log.info("{}", "defaultField = " + ReflectionUtils.getFieldValue(obj, "defaultField"));
        log.info("{}", "protectedField = " + ReflectionUtils.getFieldValue(obj, "protectedField"));
        log.info("{}", "privateField = " + ReflectionUtils.getFieldValue(obj, "privateField"));

        ReflectionUtils.setFieldValue(obj, "publicField", "a");
        ReflectionUtils.setFieldValue(obj, "defaultField", "b");
        ReflectionUtils.setFieldValue(obj, "protectedField", "c");
        ReflectionUtils.setFieldValue(obj, "privateField", "d");

        log.info("{}", "***********************************************************");

        log.info("{}", "将属性值改变后的各个属性值: ");
        log.info("{}", "publicField = " + ReflectionUtils.getFieldValue(obj, "publicField"));
        log.info("{}", "defaultField = " + ReflectionUtils.getFieldValue(obj, "defaultField"));
        log.info("{}", "protectedField = " + ReflectionUtils.getFieldValue(obj, "protectedField"));
        log.info("{}", "privateField = " + ReflectionUtils.getFieldValue(obj, "privateField"));

    }

    /**
     * 获取字段的值
     *
     * @since 1.0.0
     */
    @Test
    void testGetFieldValue() {

        Object obj = new Son();

        log.info("{}", "publicField = " + ReflectionUtils.getFieldValue(obj, "publicField"));
        log.info("{}", "defaultField = " + ReflectionUtils.getFieldValue(obj, "defaultField"));
        log.info("{}", "protectedField = " + ReflectionUtils.getFieldValue(obj, "protectedField"));
        log.info("{}", "privateField = " + ReflectionUtils.getFieldValue(obj, "privateField"));
    }

    /**
     * Test main
     *
     * @since 1.0.0
     */
    @Test
    void test_Main() {
        Object obj = new Son();

        log.info("{}", "main = " + ReflectionUtils.getDeclaredMethod(obj, "main", String[].class));
        log.info("{}", "main = " + ReflectionUtils.getDeclaredMethod(Son.class, "main", String[].class));
    }

    /**
     * Test 1
     *
     * @since 1.0.0
     */
    @Test
    void test_1() {
        Object obj = new Son();
        log.info("{}", "privateField = " + ReflectionUtils.getFieldValue(obj, "privateField", String.class));
    }

    /**
     * 基类
     *
     * @author dong4j
     * @version 1.0.0
     * @email "mailto:dong4j@gmail.com"
     * @date 2020.04.30 00:40
     * @since 1.0.0
     */
    private static class GrandParent {
        /** field */
        String publicField = "1";
        /** Default field */
        String defaultField = "2";
        /** Protected field */
        protected String protectedField = "3";
        /** Private field */
        private final String privateField = "4";

        /**
         * method
         *
         * @since 1.0.0
         */
        void publicMethod() {
            log.info("{}", "publicMethod...");
        }

        /**
         * Default method
         *
         * @since 1.0.0
         */
        void defaultMethod() {
            log.info("{}", "defaultMethod...");
        }

        /**
         * Protected method
         *
         * @since 1.0.0
         */
        protected void protectedMethod() {
            log.info("{}", "protectedMethod...");
        }

        /**
         * Private method
         *
         * @since 1.0.0
         */
        private void privateMethod() {
            log.info("{}", "privateMethod...");
        }

        /**
         * Main *
         *
         * @param args args
         * @since 1.0.0
         */
        static void main(String[] args) {
            log.info("main");
        }

    }

    /**
     * 父类
     *
     * @author dong4j
     * @version 1.0.0
     * @email "mailto:dong4j@gmail.com"
     * @date 2020.04.30 00:40
     * @since 1.0.0
     */
    private static class Parent extends GrandParent {

    }

    /**
     * 子类
     *
     * @author dong4j
     * @version 1.0.0
     * @email "mailto:dong4j@gmail.com"
     * @date 2020.04.30 00:40
     * @since 1.0.0
     */
    private static class Son extends Parent {

    }
}
