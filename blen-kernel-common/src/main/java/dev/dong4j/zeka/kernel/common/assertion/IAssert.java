package dev.dong4j.zeka.kernel.common.assertion;

import dev.dong4j.zeka.kernel.common.asserts.Assertions;
import dev.dong4j.zeka.kernel.common.exception.LowestException;
import dev.dong4j.zeka.kernel.common.function.CheckedCallable;
import dev.dong4j.zeka.kernel.common.function.CheckedRunnable;
import java.util.Collection;
import java.util.Map;

/**
 * <p>平象异常断言接口.
 * <p>为枚举类提供统一的异常断言能力，简化条件判断和异常抛出的代码编写.
 * <p>通过在枚举类中定义错误码和错误信息，在断言方法中传递参数即可灵活构建异常.
 * <p>底层使用 {@link Assertions} 工具类提供具体的断言实现.
 * <p>主要特性：
 * <ul>
 *     <li>统一的异常创建接口</li>
 *     <li>丰富的常用断言方法</li>
 *     <li>支持参数化错误消息</li>
 *     <li>支持异常包装和链式调用</li>
 *     <li>与枚举类结合使用，提供类型安全的错误处理</li>
 * </ul>
 * <p>适用场景：
 * <ul>
 *     <li>业务参数校验</li>
 *     <li>前置条件检查</li>
 *     <li>业务规则验证</li>
 *     <li>异常状态处理</li>
 * </ul>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.26 20:06
 * @since 1.0.0
 */
public interface IAssert {

    /**
     * <p>创建新的异常实例.
     * <p>基于当前的错误码和参数创建对应的异常对象.
     *
     * @param args 可变参数，用于格式化错误消息
     * @return 创建的异常实例
     * @since 1.0.0
     */
    LowestException newException(Object... args);

    /**
     * <p>创建包装原始异常的新异常实例.
     * <p>将原始异常作为因果链，创建包含上下文信息的新异常.
     *
     * @param t    原始异常，作为异常因果链
     * @param args 可变参数，用于格式化错误消息
     * @return 包装后的异常实例
     * @since 1.0.0
     */
    LowestException newException(Throwable t, Object... args);

    /**
     * <p>断言字符串非空白.
     * <p>检查字符串不为 null、空字符串或只包含空白字符.
     *
     * @param str  待检查的字符串
     * @param args 异常参数
     * @since 1.0.0
     */
    default void notBlank(String str, Object... args) {
        Assertions.notBlank(str, () -> this.newException(args));
    }

    /**
     * <p>断言对象数组非空.
     * <p>检查数组不为 null 且长度大于 0.
     *
     * @param arrays 待检查的数组
     * @param args   异常参数
     * @since 1.0.0
     */
    default void notEmpty(Object[] arrays, Object... args) {
        Assertions.notEmpty(arrays, () -> this.newException(args));
    }

    /**
     * <p>断言集合非空.
     * <p>检查集合不为 null 且不为空集合.
     *
     * @param c    待检查的集合
     * @param args 异常参数
     * @since 1.0.0
     */
    default void notEmpty(Collection<?> c, Object... args) {
        Assertions.notEmpty(c, () -> this.newException(args));
    }

    /**
     * <p>断言 Map 非空.
     * <p>检查 Map 不为 null 且不为空 Map.
     *
     * @param map  待检查的 Map
     * @param args 异常参数
     * @since 1.0.0
     */
    default void notEmpty(Map<?, ?> map, Object... args) {
        Assertions.notEmpty(map, () -> this.newException(args));
    }

    /**
     * <p>断言表达式为 false.
     * <p>当表达式为 true 时抛出异常.
     *
     * @param expression 布尔表达式
     * @param args       异常参数
     * @since 1.0.0
     */
    default void isFalse(boolean expression, Object... args) {
        Assertions.isFalse(expression, () -> this.newException(args));
    }

    /**
     * <p>断言表达式为 true.
     * <p>当表达式为 false 时抛出异常.
     *
     * @param expression 布尔表达式
     * @param args       异常参数
     * @since 1.0.0
     */
    default void isTrue(boolean expression, Object... args) {
        Assertions.isTrue(expression, () -> this.newException(args));
    }

    /**
     * <p>断言对象为 null.
     * <p>当对象不为 null 时抛出异常.
     *
     * @param obj  待检查的对象
     * @param args 异常参数
     * @since 1.0.0
     */
    default void isNull(Object obj, Object... args) {
        Assertions.isNull(obj, () -> this.newException(args));
    }

    /**
     * <p>断言对象不为 null.
     * <p>当对象为 null 时抛出异常.
     *
     * @param obj  待检查的对象
     * @param args 异常参数
     * @since 1.0.0
     */
    default void notNull(Object obj, Object... args) {
        Assertions.notNull(obj, () -> this.newException(args));
    }

    /**
     * <p>断言对象不为 null (自定义操作).
     * <p>适用于没有占位符的错误消息，在检查通过后执行自定义操作.
     *
     * @param obj      待检查的对象
     * @param runnable 检查通过后执行的操作
     * @since 1.0.0
     */
    default void notNull(Object obj, CheckedRunnable runnable) {
        Assertions.notNull(obj, this::newException, runnable);
    }

    /**
     * <p>断言两个对象相等.
     * <p>使用 Objects.equals() 进行比较，当不相等时抛出异常.
     *
     * @param o1   第一个对象
     * @param o2   第二个对象
     * @param args 异常参数
     * @since 1.0.0
     */
    default void equals(Object o1, Object o2, Object... args) {
        Assertions.equals(o1, o2, () -> this.newException(args));
    }

    /**
     * <p>断言两个对象不相等.
     * <p>使用 Objects.equals() 进行比较，当相等时抛出异常.
     *
     * @param o1   第一个对象
     * @param o2   第二个对象
     * @param args 异常参数
     * @since 1.0.0
     */
    default void notEquals(Object o1, Object o2, Object... args) {
        Assertions.notEquals(o1, o2, () -> this.newException(args));
    }

    /**
     * <p>包装执行可能抛出异常的操作.
     * <p>捕获执行过程中的异常并重新包装为业务异常.
     *
     * @param runnable 可能抛出异常的操作
     * @param args     异常参数
     * @since 1.0.0
     */
    default void wrapper(CheckedRunnable runnable, Object... args) {
        try {
            runnable.run();
        } catch (Throwable throwable) {
            this.fail(throwable, args);
        }
    }

    /**
     * <p>包装执行可能抛出异常的函数调用.
     * <p>捕获执行过程中的异常并重新包装为业务异常，返回计算结果.
     *
     * @param <T>      返回值类型
     * @param callable 可能抛出异常的函数调用
     * @param args     异常参数
     * @return 函数执行结果
     * @since 1.0.0
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
     * <p>直接抛出异常.
     * <p>基于参数创建异常并直接抛出.
     *
     * @param args 异常参数
     * @since 1.0.0
     */
    default void fail(Object... args) {
        this.fail(this.newException(args));
    }

    /**
     * <p>包装原始异常并抛出.
     * <p>将原始异常作为因果链，创建新的业务异常并抛出.
     *
     * @param t    原始异常
     * @param args 异常参数
     * @since 1.0.0
     */
    default void fail(Throwable t, Object... args) {
        Assertions.fail(() -> this.newException(t, args));
    }


}
