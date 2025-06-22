package dev.dong4j.zeka.kernel.common.assertion;

import dev.dong4j.zeka.kernel.common.asserts.Assertions;
import dev.dong4j.zeka.kernel.common.exception.BaseException;
import dev.dong4j.zeka.kernel.common.function.CheckedCallable;
import dev.dong4j.zeka.kernel.common.function.CheckedRunnable;

import java.util.Collection;
import java.util.Map;

/**
 * <p>Description: 枚举类异常断言,提供简便的方式判断条件,并在条件满足时抛出异常
 * 错误码和错误信息定义在枚举类中,在本断言方法中,传递错误信息需要的参数
 * 底层会使用 {@link Assertions}
 * </p>
 *
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.26 20:06
 * @since 1.0.0
 */
public interface IAssert {

    /**
     * New exception
     *
     * @param args args
     * @return the base exception
     * @since 1.7.0
     */
    BaseException newException(Object... args);

    /**
     * New exception
     *
     * @param t    t
     * @param args args
     * @return the base exception
     * @since 1.7.0
     */
    BaseException newException(Throwable t, Object... args);

    /**
     * Not blank
     *
     * @param str  str
     * @param args args
     * @since 1.7.0
     */
    default void notBlank(String str, Object... args) {
        Assertions.notBlank(str, () -> this.newException(args));
    }

    /**
     * Not empty
     *
     * @param arrays arrays
     * @param args   args
     * @since 1.7.0
     */
    default void notEmpty(Object[] arrays, Object... args) {
        Assertions.notEmpty(arrays, () -> this.newException(args));
    }

    /**
     * Not empty
     *
     * @param c    c
     * @param args args
     * @since 1.7.0
     */
    default void notEmpty(Collection<?> c, Object... args) {
        Assertions.notEmpty(c, () -> this.newException(args));
    }

    /**
     * Not empty
     *
     * @param map  map
     * @param args args
     * @since 1.7.0
     */
    default void notEmpty(Map<?, ?> map, Object... args) {
        Assertions.notEmpty(map, () -> this.newException(args));
    }

    /**
     * Is false
     *
     * @param expression expression
     * @param args       args
     * @since 1.7.0
     */
    default void isFalse(boolean expression, Object... args) {
        Assertions.isFalse(expression, () -> this.newException(args));
    }

    /**
     * Is true
     *
     * @param expression expression
     * @param args       args
     * @since 1.7.0
     */
    default void isTrue(boolean expression, Object... args) {
        Assertions.isTrue(expression, () -> this.newException(args));
    }

    /**
     * Is null
     *
     * @param obj  obj
     * @param args args
     * @since 1.7.0
     */
    default void isNull(Object obj, Object... args) {
        Assertions.isNull(obj, () -> this.newException(args));
    }

    /**
     * Not null
     *
     * @param obj  obj
     * @param args args
     * @since 1.7.0
     */
    default void notNull(Object obj, Object... args) {
        Assertions.notNull(obj, () -> this.newException(args));
    }

    /**
     * 适用于没有占位符的错误消息
     *
     * @param obj      obj
     * @param runnable runnable
     * @since 1.7.0
     */
    default void notNull(Object obj, CheckedRunnable runnable) {
        Assertions.notNull(obj, this::newException, runnable);
    }

    /**
     * Equals
     *
     * @param o1   o 1
     * @param o2   o 2
     * @param args args
     * @since 1.7.0
     */
    default void equals(Object o1, Object o2, Object... args) {
        Assertions.equals(o1, o2, () -> this.newException(args));
    }

    /**
     * Not equals
     *
     * @param o1   o 1
     * @param o2   o 2
     * @param args args
     * @since 1.7.0
     */
    default void notEquals(Object o1, Object o2, Object... args) {
        Assertions.notEquals(o1, o2, () -> this.newException(args));
    }

    /**
     * Wrapper
     *
     * @param runnable runnable
     * @param args     args
     * @since 1.7.0
     */
    default void wrapper(CheckedRunnable runnable, Object... args) {
        try {
            runnable.run();
        } catch (Throwable throwable) {
            this.fail(throwable, args);
        }
    }

    /**
     * Wrapper
     *
     * @param <T>      parameter
     * @param callable callable
     * @param args     args
     * @return the t
     * @since 1.8.0
     */
    default <T> T wrapper(CheckedCallable<T> callable, Object... args) {
        try {
            return callable.call();
        } catch (Throwable throwable) {
            this.fail(throwable, args);
        }
        return null;
    }

    /**
     * Fail
     *
     * @param args args
     * @since 1.7.0
     */
    default void fail(Object... args) {
        this.fail(this.newException(args));
    }

    /**
     * Fail
     *
     * @param t    t
     * @param args args
     * @since 1.7.0
     */
    default void fail(Throwable t, Object... args) {
        Assertions.fail(() -> this.newException(t, args));
    }


}
