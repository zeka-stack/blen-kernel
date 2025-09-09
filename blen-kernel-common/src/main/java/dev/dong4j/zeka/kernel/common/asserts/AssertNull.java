package dev.dong4j.zeka.kernel.common.asserts;

import dev.dong4j.zeka.kernel.common.exception.AssertionFailedException;
import dev.dong4j.zeka.kernel.common.function.CheckedRunnable;
import java.util.function.Supplier;
import org.springframework.lang.Nullable;

/**
 * <p>空值断言工具类.
 * <p>专门用于验证对象为 null 的断言操作，与 AssertNotNull 类互为补充.
 * <p>提供了灵活的错误处理机制，支持自定义错误消息和异常后操作.
 * <p>主要功能：
 * <ul>
 *     <li>验证对象引用为 null</li>
 *     <li>支持所有类型的对象引用检查</li>
 *     <li>自定义错误消息和异常类型</li>
 *     <li>支持异常后的清理操作</li>
 * </ul>
 * <p>使用场景：
 * <ul>
 *     <li>可选参数的空值验证</li>
 *     <li>资源清理后的状态检查</li>
 *     <li>初始化状态和默认值的验证</li>
 *     <li>单元测试中的期望空值验证</li>
 * </ul>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.11.18 17:03
 * @since 1.0.0
 */
final class AssertNull {

    /**
     * Assert null
     *
     * @since 1.0.0
     */
    private AssertNull() {
    }

    /**
     * Is null
     *
     * @param object object
     * @since 1.0.0
     */
    static void isNull(@Nullable Object object) {
        isNull(object, "参数必须是 null");
    }

    /**
     * Is null
     *
     * @param object  object
     * @param message message
     * @since 1.0.0
     */
    static void isNull(@Nullable Object object, String message) {
        isNull(object, () -> new AssertionFailedException(message));
    }

    /**
     * Is null
     *
     * @param object            object
     * @param exceptionSupplier message supplier
     * @since 1.0.0
     */
    static void isNull(@Nullable Object object, Supplier<? extends RuntimeException> exceptionSupplier) {
        isNull(object, exceptionSupplier, () -> {
        });
    }

    /**
     * Is null
     *
     * @param object            object
     * @param exceptionSupplier exception supplier
     * @param runnable          runnable
     * @since 1.0.0
     */
    static void isNull(@Nullable Object object, Supplier<? extends RuntimeException> exceptionSupplier, CheckedRunnable runnable) {
        if (object != null) {
            AssertUtils.fail(exceptionSupplier, runnable);
        }
    }
}
