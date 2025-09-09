package dev.dong4j.zeka.kernel.common.asserts;

import dev.dong4j.zeka.kernel.common.exception.AssertionFailedException;
import dev.dong4j.zeka.kernel.common.function.CheckedRunnable;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>正则表达式匹配断言工具类.
 * <p>专门用于验证字符串是否符合指定的正则表达式模式，支持大小写不敏感匹配.
 * <p>提供了灵活的错误处理机制，支持自定义错误消息和异常后操作.
 * <p>主要功能：
 * <ul>
 *     <li>正则表达式模式匹配验证</li>
 *     <li>大小写不敏感的匹配方式</li>
 *     <li>自定义错误消息和异常类型</li>
 *     <li>支持异常后的清理操作</li>
 * </ul>
 * <p>使用场景：
 * <ul>
 *     <li>用户输入数据的格式校验</li>
 *     <li>邮箱、手机号等信息的规范性检查</li>
 *     <li>密码强度、编码格式等规则验证</li>
 *     <li>文本处理和数据清洗中的模式匹配</li>
 * </ul>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.11.18 17:12
 * @since 1.0.0
 */
final class AssertMatch {

    /**
     * Assert not null
     *
     * @since 1.0.0
     */
    private AssertMatch() {
    }

    /**
     * 编译传入正则表达式和字符串去匹配,忽略大小写
     *
     * @param regex        正则
     * @param beTestString 字符串
     * @since 1.0.0
     */
    static void isMatch(String regex, String beTestString) {
        isMatch(regex, beTestString, "字符串无法与指定正则匹配");
    }

    /**
     * Is match
     *
     * @param regex        regex
     * @param beTestString be test string
     * @param message      message
     * @since 1.0.0
     */
    static void isMatch(String regex, String beTestString, String message) {
        isMatch(regex, beTestString, () -> new AssertionFailedException(message));
    }

    /**
     * Is match
     *
     * @param regex           regex
     * @param beTestString    be test string
     * @param messageSupplier message supplier
     * @since 1.0.0
     */
    static void isMatch(String regex, String beTestString, Supplier<? extends RuntimeException> messageSupplier) {
        isMatch(regex, beTestString, messageSupplier, () -> {
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
    static void isMatch(String regex, String beTestString, Supplier<? extends RuntimeException> messageSupplier, CheckedRunnable runnable) {
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(beTestString);
        if (!matcher.matches()) {
            AssertUtils.fail(messageSupplier, runnable);
        }
    }
}
