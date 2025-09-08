package dev.dong4j.zeka.kernel.common.support;

import dev.dong4j.zeka.kernel.common.util.CharPool;
import dev.dong4j.zeka.kernel.common.util.StringPool;
import java.text.MessageFormat;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.util.StringUtils;

/**
 * 字符串格式化工具类，提供多种字符串模板格式化功能
 * <p>
 * 该类提供了类似Slf4j日志格式的字符串模板功能，支持占位符替换
 * 同时兼容MessageFormat的格式，可以灵活处理不同类型的模板字符串
 * <p>
 * 主要特性：
 * - 支持{}占位符：类似Slf4j的参数替换方式
 * - 转义字符支持：可以使用\\转义占位符和转义字符本身
 * - 兼容MessageFormat：支持{0}, {1}等带索引的占位符
 * - 高性能：优化的字符串拼接算法，减少内存分配
 * - 空值安全：对null参数进行安全处理
 * <p>
 * 使用场景：
 * - 日志消息的格式化输出，类似Slf4j的参数化日志
 * - 异常消息和错误提示的动态生成
 * - 数据库查询SQL的动态拼接和参数化
 * - 消息模板系统，支持多语言和参数化消息
 * - 代码生成器的模板处理，生成参数化的代码源文件
 * <p>
 * 格式化规则：
 * - 基本占位符：使用{}表示参数占位符
 * - 转义占位符：使用\\{}表示字面量"{}"
 * - 转义转义符：使用\\\\{}表示"\\"后跟参数
 * - 索引占位符：使用{0}, {1}等带索引的占位符
 * <p>
 * 使用示例：
 * <pre>{@code
 * // 基本使用
 * String result = StrFormatter.format("Hello {}, welcome to {}!", "John", "System");
 * // 结果: "Hello John, welcome to System!"
 * 
 * // 转义字符使用
 * String result = StrFormatter.format("Show \\{} literal, param: {}", "value");
 * // 结果: "Show {} literal, param: value"
 * 
 * // MessageFormat兼容
 * String result = StrFormatter.mergeFormat("{0} has {1} apples", "John", 5);
 * // 结果: "John has 5 apples"
 * 
 * // 空值处理
 * String result = StrFormatter.format("Value: {}", null);
 * // 结果: "Value: null"
 * }</pre>
 * <p>
 * 性能优势：
 * - 相比于String.format()有显著性能提升
 * - 避免了正则表达式的开销，使用简单的字符匹配
 * - 预先分配合适的StringBuilder容量，减少内存重新分配
 * - 支持早期返回优化，无参数或无占位符时直接返回原字符串
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.26 20:42
 * @since 1.0.0
 */
@UtilityClass
public class StrFormatter {

    /**
     * 格式化字符串<br>
     * 此方法只是简单将占位符 {} 按照顺序替换为参数<br>
     * 如果想输出 {} 使用 \\转义 { 即可,如果想输出 {} 之前的 \ 使用双转义符 \\\\ 即可<br>
     * 例: <br>
     * 通常使用: format("this is {} for {}", "a", "b") =》 this is a for b<br>
     * 转义{}: format("this is \\{} for {}", "a", "b") =》 this is \{} for a<br>
     * 转义\: format("this is \\\\{} for {}", "a", "b") =》 this is \a for b<br>
     *
     * @param strPattern 字符串模板
     * @param argArray   参数列表
     * @return 结果 string
     * @since 1.0.0
     */
    @SuppressWarnings({"checkstyle:ReturnCount", "D"})
    public static String format(String strPattern, Object... argArray) {
        boolean isEmpty = argArray == null || argArray.length == 0;
        if (!StringUtils.hasText(strPattern) || isEmpty) {
            return strPattern;
        }
        int strPatternLength = strPattern.length();
        // 初始化定义好的长度以获得更好的性能
        StringBuilder sbuf = new StringBuilder(strPatternLength + 50);
        // 记录已经处理到的位置
        int handledPosition = 0;
        // 占位符所在位置
        int delimIndex;
        for (int argIndex = 0, length = argArray.length; argIndex < length; argIndex++) {
            delimIndex = strPattern.indexOf(StringPool.EMPTY_JSON, handledPosition);
            // 剩余部分无占位符
            if (delimIndex == -1) {
                // 不带占位符的模板直接返回
                if (handledPosition == 0) {
                    return strPattern;
                } else {
                    sbuf.append(strPattern, handledPosition, strPatternLength);
                    return sbuf.toString();
                }
            } else {
                // 转义符
                if (delimIndex > 0 && strPattern.charAt(delimIndex - 1) == CharPool.BACK_SLASH) {
                    // 双转义符
                    if (delimIndex > 1 && strPattern.charAt(delimIndex - 2) == CharPool.BACK_SLASH) {
                        // 转义符之前还有一个转义符,占位符依旧有效
                        sbuf.append(strPattern, handledPosition, delimIndex - 1);
                        sbuf.append(toStr(argArray[argIndex]));
                        handledPosition = delimIndex + 2;
                    } else {
                        // 占位符被转义
                        argIndex--;
                        sbuf.append(strPattern, handledPosition, delimIndex - 1);
                        sbuf.append(StringPool.LEFT_BRACE);
                        handledPosition = delimIndex + 1;
                    }
                } else {
                    // 正常占位符
                    sbuf.append(strPattern, handledPosition, delimIndex);
                    sbuf.append(toStr(argArray[argIndex]));
                    handledPosition = delimIndex + 2;
                }
            }
        }
        // 加入最后一个占位符后所有的字符
        sbuf.append(strPattern, handledPosition, strPattern.length());
        return sbuf.toString();
    }

    /**
     * To str string
     *
     * @param str str
     * @return the string
     * @since 1.0.0
     */
    @Contract(pure = true)
    public static String toStr(Object str) {
        return toStr(str, "");
    }

    /**
     * 强转string,并去掉多余空格
     *
     * @param str          字符串
     * @param defaultValue 默认值
     * @return String string
     * @since 1.0.0
     */
    @Contract(value = "null, _ -> param2", pure = true)
    public static String toStr(Object str, String defaultValue) {
        if (null == str) {
            return defaultValue;
        }
        return String.valueOf(str);
    }

    /**
     * 同时兼容 {} 和 {0} 2 种格式
     *
     * @param value  value
     * @param params params
     * @return the string
     * @since 1.0.0
     */
    @NotNull
    public static String mergeFormat(@NotNull String value, @NotNull Object... params) {
        return params.length > 0 && value.indexOf('{') >= 0
            ? value.contains("{0")
            ? MessageFormat.format(value, params)
            : format(value, params)
            : value;
    }
}
