package dev.dong4j.zeka.kernel.common.asserts;

import dev.dong4j.zeka.kernel.common.exception.AssertionFailedException;
import dev.dong4j.zeka.kernel.common.function.CheckedRunnable;

import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.11.18 17:12
 * @since 1.7.0
 */
final class AssertMatch {

    /**
     * Assert not null
     *
     * @since 1.7.0
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
     * @since 1.7.0
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
     * @since 1.7.0
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
     * @since 1.7.0
     */
    static void isMatch(String regex, String beTestString, Supplier<? extends RuntimeException> messageSupplier, CheckedRunnable runnable) {
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(beTestString);
        if (!matcher.matches()) {
            AssertUtils.fail(messageSupplier, runnable);
        }
    }
}
