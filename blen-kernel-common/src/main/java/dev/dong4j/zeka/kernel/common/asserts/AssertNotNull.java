package dev.dong4j.zeka.kernel.common.asserts;

import dev.dong4j.zeka.kernel.common.exception.AssertionFailedException;
import dev.dong4j.zeka.kernel.common.function.CheckedRunnable;
import java.util.function.Supplier;
import org.springframework.lang.Nullable;

/**
 * <p>非空断言工具类.
 * <p>专门用于验证对象不为 null 的断言操作，是最常用的参数校验方式之一.
 * <p>提供了灵活的错误处理机制，支持自定义错误消息和异常后操作.
 * <p>主要功能：
 * <ul>
 *     <li>验证对象引用不为 null</li>
 *     <li>支持所有类型的对象引用检查</li>
 *     <li>自定义错误消息和异常类型</li>
 *     <li>支持异常后的清理操作</li>
 * </ul>
 * <p>使用场景：
 * <ul>
 *     <li>方法参数的预条件检查</li>
 *     <li>依赖注入和对象初始化的验证</li>
 *     <li>API 输入参数的有效性检查</li>
 *     <li>业务逻辑中的空指针防护</li>
 * </ul>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.11.18 17:12
 * @since 1.0.0
 */
final class AssertNotNull {

    /**
     * Assert not null
     *
     * @since 1.0.0
     */
    private AssertNotNull() {
    }

    /**
     * Not null
     *
     * @param actual actual
     * @since 1.0.0
     */
    static void notNull(Object actual) {
        notNull(actual, "参数不能为 null");
    }

    /**
     * Not null
     *
     * @param actual  actual
     * @param message message
     * @since 1.0.0
     */
    static void notNull(Object actual, String message) {
        notNull(actual, () -> new AssertionFailedException(message));
    }

    /**
     * Not null
     *
     * @param actual          actual
     * @param messageSupplier message supplier
     * @since 1.0.0
     */
    static void notNull(Object actual, Supplier<? extends RuntimeException> messageSupplier) {
        notNull(actual, messageSupplier, () -> {
        });
    }

    /**
     * Not null
     *
     * @param object            object
     * @param exceptionSupplier exception supplier
     * @param runnable          runnable
     * @since 1.0.0
     */
    static void notNull(@Nullable Object object, Supplier<? extends RuntimeException> exceptionSupplier, CheckedRunnable runnable) {
        if (object == null) {
            AssertUtils.fail(exceptionSupplier, runnable);
        }
    }

}
