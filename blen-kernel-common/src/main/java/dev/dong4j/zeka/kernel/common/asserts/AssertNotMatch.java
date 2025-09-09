package dev.dong4j.zeka.kernel.common.asserts;

import dev.dong4j.zeka.kernel.common.exception.AssertionFailedException;
import dev.dong4j.zeka.kernel.common.function.CheckedRunnable;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>正则表达式不匹配断言工具类.
 * <p>专门用于验证字符串不符合指定的正则表达式模式，支持大小写不敏感匹配.
 * <p>与 AssertMatch 类互为补充，提供反向的正则匹配验证能力.
 * <p>主要功能：
 * <ul>
 *     <li>验证字符串不符合正则表达式模式</li>
 *     <li>大小写不敏感的匹配方式</li>
 *     <li>自定义错误消息和异常类型</li>
 *     <li>支持异常后的清理操作</li>
 * </ul>
 * <p>使用场景：
 * <ul>
 *     <li>非法字符或禁止内容的过滤检查</li>
 *     <li>安全性校验和反向验证</li>
 *     <li>数据清洗中的异常模式检测</li>
 *     <li>输入限制和格式约束的验证</li>
 * </ul>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.11.18 17:12
 * @since 1.0.0
 */
final class AssertNotMatch {

    /**
     * Assert not null
     *
     * @since 1.0.0
     */
    private AssertNotMatch() {
    }

    /**
     * 编译传入正则表达式和字符串去匹配,忽略大小写
     *
     * @param regex        正则
     * @param beTestString 字符串
     * @since 1.0.0
     */
    static void notMatch(String regex, String beTestString) {
        notMatch(regex, beTestString, "字符串与指定正则匹配");
    }

    /**
     * Is match
     *
     * @param regex        regex
     * @param beTestString be test string
     * @param message      message
     * @since 1.0.0
     */
    static void notMatch(String regex, String beTestString, String message) {
        notMatch(regex, beTestString, () -> new AssertionFailedException(message));
    }

    /**
     * Is match
     *
     * @param regex           regex
     * @param beTestString    be test string
     * @param messageSupplier message supplier
     * @since 1.0.0
     */
    static void notMatch(String regex, String beTestString, Supplier<? extends RuntimeException> messageSupplier) {
        notMatch(regex, beTestString, messageSupplier, () -> {
        });
    }

    /**
     * Is match
     *
     * @param regex           regex
     * @param beTestString    be test string
     * @param messageSupplier message supplier
     * @param runnable        runnable
     * @since 1.0.0
     */
    static void notMatch(String regex, String beTestString, Supplier<? extends RuntimeException> messageSupplier,
                         CheckedRunnable runnable) {
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(beTestString);
        if (matcher.matches()) {
            AssertUtils.fail(messageSupplier, runnable);
        }
    }
}
