package dev.dong4j.zeka.kernel.common.util;

import cn.hutool.core.text.StrSplitter;
import dev.dong4j.zeka.kernel.common.support.StrFormatter;
import dev.dong4j.zeka.kernel.common.support.StrSpliter;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import org.apache.commons.text.StringSubstitutor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.util.ObjectUtils;
import org.springframework.web.util.HtmlUtils;

/**
 * 字符串工具类，扩展了Spring的StringUtils功能
 *
 * 提供了丰富的字符串操作方法，包括空值检查、数字验证、格式化、连接、分割、编码解码等
 * 结合了Apache Commons、Hutool等第三方库的优秀功能，提供统一的字符串处理接口
 *
 * 主要功能：
 * - 空值和空白字符检查（isBlank、isNotBlank、isAnyBlank等）
 * - 字符串连接和分割（join、split系列方法）
 * - 数字和格式验证（isNumeric、特殊字符正则等）
 * - 字符串格式化和模板替换
 * - HTML转义和URL编码处理
 * - 随机字符串生成
 * - 字符串比较和搜索功能
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2019.12.27 09:50
 * @since 1.0.0
 */
@SuppressWarnings("all")
public class StringUtils extends org.springframework.util.StringUtils {
    /** INDEX_NOT_FOUND */
    public static final int INDEX_NOT_FOUND = -1;
    /** 特殊字符正则,sql特殊字符和空白符 */
    public static final Pattern SPECIAL_CHARS_REGEX = Pattern.compile("[`'\"|/,;()-+*%#·•�　\\s]");
    /**
     * 字符串去除空白内容
     *
     * <ul> <li>'"<>&*+=#-; sql注入黑名单</li> <li>\n 回车</li> <li>\t 水平制表符</li> <li>\s 空格</li> <li>\r 换行</li> </ul>
     */
    private static final Pattern REPLACE_BLANK = Pattern.compile("'|\"|\\<|\\>|&|\\*|\\+|=|#|-|;|\\s*|\t|\r|\n");
    /** BLANK_SYMBOL */
    public static final String BLANK_SYMBOL = "\\s*|\t|\r|\n";
    /** 随机字符串因子 */
    public static final String INT_STR = "0123456789";
    /** STR_STR */
    public static final String STR_STR = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    /** ALL_STR */
    public static final String ALL_STR = INT_STR + STR_STR;

    /**
     * 有 任意 一个 Blank
     *
     * @param css CharSequence
     * @return boolean boolean
     * @since 1.0.0
     */
    public static boolean isAnyBlank(CharSequence... css) {
        if (ObjectUtils.isEmpty(css)) {
            return true;
        }
        return Stream.of(css).anyMatch(StringUtils::isBlank);
    }

    /**
     * Check whether the given {@code CharSequence} contains actual <em>text</em>.
     * <p>More specifically, this method returns {@code true} if the
     * {@code CharSequence} is not {@code null}, its length is greater than
     * 0, and it contains at least one non-whitespace character.
     * <pre class="code">
     * StringUtil.isBlank(null) = true
     * StringUtil.isBlank("") = true
     * StringUtil.isBlank(" ") = true
     * StringUtil.isBlank("12345") = false
     * StringUtil.isBlank(" 12345 ") = false
     * </pre>
     *
     * @param cs the {@code CharSequence} to check (may be {@code null})
     * @return {@code true} if the {@code CharSequence} is not {@code null},     its length is greater than 0, and it does not contain
     * whitespace only
     * @see Character#isWhitespace Character#isWhitespaceCharacter#isWhitespace
     * @since 1.0.0
     */
    @Contract("null -> true")
    public static boolean isBlank(CharSequence cs) {
        return !StringUtils.hasText(cs);
    }

    /**
     * 是否全部都不是 blank
     *
     * @param css CharSequence
     * @return boolean boolean
     * @since 1.0.0
     */
    public static boolean isNoneBlank(CharSequence... css) {
        if (ObjectUtils.isEmpty(css)) {
            return false;
        }
        return Stream.of(css).allMatch(StringUtils::isNotBlank);
    }

    /**
     * 是否全部都是 blank
     *
     * @param css css
     * @return the boolean
     * @since 1.0.0
     */
    public static boolean isAllBlank(CharSequence... css) {
        if (ObjectUtils.isEmpty(css)) {
            return true;
        }
        return Stream.of(css).allMatch(StringUtils::isBlank);
    }

    /**
     * <p>Checks if a CharSequence is not empty (""), not null and not whitespace only.</p>
     * <pre>
     * StringUtil.isNotBlank(null)      = false
     * StringUtil.isNotBlank("")        = false
     * StringUtil.isNotBlank(" ")       = false
     * StringUtil.isNotBlank("bob")     = true
     * StringUtil.isNotBlank("  bob  ") = true
     * </pre>
     *
     * @param cs the CharSequence to check, may be null
     * @return {@code true} if the CharSequence is not empty and not null and not whitespace
     * @see Character#isWhitespace Character#isWhitespaceCharacter#isWhitespace
     * @since 1.0.0
     */
    @Contract("null -> false")
    public static boolean isNotBlank(CharSequence cs) {
        return StringUtils.hasText(cs);
    }

    /**
     * 判断一个字符串是否是数字
     *
     * @param cs the CharSequence to check, may be null
     * @return {boolean}
     * @since 1.0.0
     */
    public static boolean isNumeric(CharSequence cs) {
        if (isBlank(cs)) {
            return false;
        }
        for (int i = cs.length(); --i >= 0; ) {
            int chr = cs.charAt(i);
            if (chr < 48 || chr > 57) {
                return false;
            }
        }
        return true;
    }

    /**
     * Convert a {@code Collection} into a delimited {@code String} (e.g., CSV).
     * <p>Useful for {@code toString()} implementations.
     *
     * @param coll the {@code Collection} to convert
     * @return the delimited {@code String}
     * @since 1.0.0
     */
    @NotNull
    public static String join(Collection<?> coll) {
        return StringUtils.collectionToCommaDelimitedString(coll);
    }

    /**
     * Convert a {@code Collection} into a delimited {@code String} (e.g. CSV).
     * <p>Useful for {@code toString()} implementations.
     *
     * @param coll  the {@code Collection} to convert
     * @param delim the delimiter to use (typically a ",")
     * @return the delimited {@code String}
     * @since 1.0.0
     */
    @NotNull
    public static String join(Collection<?> coll, String delim) {
        return StringUtils.collectionToDelimitedString(coll, delim);
    }

    /**
     * Convert a {@code String} array into a comma delimited {@code String}
     * (i.e., CSV).
     * <p>Useful for {@code toString()} implementations.
     *
     * @param arr the array to display
     * @return the delimited {@code String}
     * @since 1.0.0
     */
    @NotNull
    @Contract(pure = true)
    public static String join(Object[] arr) {
        return StringUtils.arrayToCommaDelimitedString(arr);
    }

    /**
     * Convert a {@code String} array into a delimited {@code String} (e.g. CSV).
     * <p>Useful for {@code toString()} implementations.
     *
     * @param arr   the array to display
     * @param delim the delimiter to use (typically a ",")
     * @return the delimited {@code String}
     * @since 1.0.0
     */
    @NotNull
    @Contract(pure = true)
    public static String join(Object[] arr, String delim) {
        return StringUtils.arrayToDelimitedString(arr, delim);
    }

    /**
     * 生成uuid
     *
     * @return UUID string
     * @since 1.0.0
     */
    @NotNull
    public static String randomUid() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        return new UUID(random.nextLong(), random.nextLong()).toString().replace(StringPool.DASH, StringPool.EMPTY);
    }

    /**
     * 转义HTML用于安全过滤
     *
     * @param html html
     * @return {String}
     * @since 1.0.0
     */
    @NotNull
    public static String escapeHtml(String html) {
        return HtmlUtils.htmlEscape(html);
    }

    /**
     * 清理字符串,清理出某些不可见字符
     *
     * @param txt 字符串
     * @return {String}
     * @since 1.0.0
     */
    @NotNull
    @Contract(pure = true)
    public static String cleanChars(@NotNull String txt) {
        return txt.replaceAll("[ 　`·•�\\f\\t\\v\\s]", "");
    }


    /**
     * 有序的格式化文本,使用{number}做为占位符<br>
     * 例: <br>
     * 通常使用: format("this is {0} for {1}", "a", "b") =》 this is a for b<br>
     *
     * @param pattern   文本格式
     * @param arguments 参数
     * @return 格式化后的文本 string
     * @since 1.0.0
     */
    @NotNull
    public static String indexedFormat(@NotNull CharSequence pattern, Object... arguments) {
        return MessageFormat.format(pattern.toString(), arguments);
    }

    /**
     * 切分字符串,不去除切分后每个元素两边的空白符,不去除空白项
     *
     * @param str       被切分的字符串
     * @param separator 分隔符字符
     * @param limit     限制分片数,-1不限制
     * @return 切分后的集合 list
     * @since 1.0.0
     */
    @Contract("null, _, _ -> new")
    public static List<String> split(CharSequence str, char separator, int limit) {
        return split(str, separator, limit, false, false);
    }

    /**
     * 切分字符串
     *
     * @param str         被切分的字符串
     * @param separator   分隔符字符
     * @param limit       限制分片数,-1不限制
     * @param isTrim      是否去除切分字符串后每个元素两边的空格
     * @param ignoreEmpty 是否忽略空串
     * @return 切分后的集合 list
     * @since 1.0.0
     */
    @Contract("null, _, _, _, _ -> new")
    public static List<String> split(CharSequence str, char separator, int limit, boolean isTrim, boolean ignoreEmpty) {
        if (null == str) {
            return new ArrayList<>(0);
        }
        return StrSplitter.split(str.toString(), separator, limit, isTrim, ignoreEmpty);
    }

    /**
     * 切分字符串,去除切分后每个元素两边的空白符,去除空白项
     *
     * @param str       被切分的字符串
     * @param separator 分隔符字符
     * @return 切分后的集合 list
     * @since 1.0.0
     */
    @Contract("null, _ -> new")
    public static List<String> splitTrim(CharSequence str, char separator) {
        return splitTrim(str, separator, -1);
    }

    /**
     * 切分字符串,去除切分后每个元素两边的空白符,去除空白项
     *
     * @param str       被切分的字符串
     * @param separator 分隔符字符
     * @param limit     限制分片数,-1不限制
     * @return 切分后的集合 list
     * @since 1.0.0
     */
    @Contract("null, _, _ -> new")
    private static List<String> splitTrim(CharSequence str, char separator, int limit) {
        return split(str, separator, limit, true, true);
    }

    /**
     * 切分字符串,去除切分后每个元素两边的空白符,去除空白项
     *
     * @param str       被切分的字符串
     * @param separator 分隔符字符
     * @return 切分后的集合 list
     * @since 1.0.0
     */
    @Contract("null, _ -> new")
    public static List<String> splitTrim(CharSequence str, CharSequence separator) {
        return splitTrim(str, separator, -1);
    }

    /**
     * 切分字符串,去除切分后每个元素两边的空白符,去除空白项
     *
     * @param str       被切分的字符串
     * @param separator 分隔符字符
     * @param limit     限制分片数,-1不限制
     * @return 切分后的集合 list
     * @since 1.0.0
     */
    @Contract("null, _, _ -> new")
    private static List<String> splitTrim(CharSequence str, CharSequence separator, int limit) {
        return split(str, separator, limit, true, true);
    }

    /**
     * 切分字符串
     *
     * @param str         被切分的字符串
     * @param separator   分隔符字符
     * @param limit       限制分片数,-1不限制
     * @param isTrim      是否去除切分字符串后每个元素两边的空格
     * @param ignoreEmpty 是否忽略空串
     * @return 切分后的集合 list
     * @since 1.0.0
     */
    @Contract("null, _, _, _, _ -> new")
    public static List<String> split(CharSequence str, CharSequence separator, int limit, boolean isTrim, boolean ignoreEmpty) {
        if (null == str) {
            return new ArrayList<>(0);
        }
        String separatorStr = (null == separator) ? null : separator.toString();
        return StrSplitter.split(str.toString(), separatorStr, limit, isTrim, ignoreEmpty);
    }

    /**
     * 切分字符串,不限制分片数量
     *
     * @param str         被切分的字符串
     * @param separator   分隔符字符
     * @param isTrim      是否去除切分字符串后每个元素两边的空格
     * @param ignoreEmpty 是否忽略空串
     * @return 切分后的集合 list
     * @since 1.0.0
     */
    @Contract("null, _, _, _ -> new")
    public static List<String> split(CharSequence str, char separator, boolean isTrim, boolean ignoreEmpty) {
        return split(str, separator, 0, isTrim, ignoreEmpty);
    }

    /**
     * 切分字符串
     *
     * @param str       被切分的字符串
     * @param separator 分隔符
     * @return 字符串 string [ ]
     * @since 1.0.0
     */
    @NotNull
    @Contract("null, _ -> new")
    public static String[] split(CharSequence str, CharSequence separator) {
        if (str == null) {
            return new String[0];
        }

        String separatorStr = (null == separator) ? null : separator.toString();
        return StrSplitter.splitToArray(str.toString(), separatorStr, 0, false, false);
    }

    /**
     * 根据给定长度,将给定字符串截取为多个部分
     *
     * @param str 字符串
     * @param len 每一个小节的长度
     * @return 截取后的字符串数组 string [ ]
     * @see StrSpliter#splitByLength(String, int) StrSpliter#splitByLength(String, int)StrSpliter#splitByLength(String, int)
     * @since 1.0.0
     */
    @Contract("null, _ -> new")
    public static String[] split(CharSequence str, int len) {
        if (null == str) {
            return new String[0];
        }
        return StrSplitter.splitByLength(str.toString(), len);
    }

    /**
     * 指定字符是否在字符串中出现过
     *
     * @param str        字符串
     * @param searchChar 被查找的字符
     * @return 是否包含 boolean
     * @since 1.0.0
     */
    public static boolean contains(CharSequence str, char searchChar) {
        return indexOf(str, searchChar) > -1;
    }

    /**
     * 指定范围内查找指定字符
     *
     * @param str        字符串
     * @param searchChar 被查找的字符
     * @return 位置 int
     * @since 1.0.0
     */
    public static int indexOf(CharSequence str, char searchChar) {
        return indexOf(str, searchChar, 0);
    }

    /**
     * 指定范围内查找指定字符
     *
     * @param str        字符串
     * @param searchChar 被查找的字符
     * @param start      起始位置,如果小于0,从0开始查找
     * @return 位置 int
     * @since 1.0.0
     */
    public static int indexOf(CharSequence str, char searchChar, int start) {
        if (str instanceof String) {
            return ((String) str).indexOf(searchChar, start);
        } else {
            return indexOf(str, searchChar, start, -1);
        }
    }

    /**
     * 指定范围内查找指定字符
     *
     * @param str        字符串
     * @param searchChar 被查找的字符
     * @param start      起始位置,如果小于0,从0开始查找
     * @param end        终止位置,如果超过str.length()则默认查找到字符串末尾
     * @return 位置 int
     * @since 1.0.0
     */
    public static int indexOf(@NotNull CharSequence str, char searchChar, int start, int end) {
        int len = str.length();
        if (start < 0 || start > len) {
            start = 0;
        }
        if (end > len || end < 0) {
            end = len;
        }
        for (int i = start; i < end; i++) {
            if (str.charAt(i) == searchChar) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 截取分隔字符串之前的字符串,不包括分隔字符串<br>
     * 如果给定的字符串为空串 (null或"") 或者分隔字符串为null,返回原字符串<br>
     * 如果分隔字符串为空串"",则返回空串,如果分隔字符串未找到,返回原字符串
     * <p>
     * 栗子:
     * <pre>
     * StringUtil.subBefore(null, *)      = null
     * StringUtil.subBefore("", *)        = ""
     * StringUtil.subBefore("abc", "a")   = ""
     * StringUtil.subBefore("abcba", "b") = "a"
     * StringUtil.subBefore("abc", "c")   = "ab"
     * StringUtil.subBefore("abc", "d")   = "abc"
     * StringUtil.subBefore("abc", "")    = ""
     * StringUtil.subBefore("abc", null)  = "abc"
     * </pre>
     *
     * @param string          被查找的字符串
     * @param separator       分隔字符串 (不包括)
     * @param isLastSeparator 是否查找最后一个分隔字符串 (多次出现分隔字符串时选取最后一个) ,true为选取最后一个
     * @return 切割后的字符串 string
     * @since 1.0.0
     */
    @Nullable
    public static String subBefore(CharSequence string, CharSequence separator, boolean isLastSeparator) {
        if (StringUtils.isEmpty(string) || separator == null) {
            return null == string ? null : string.toString();
        }

        String str = string.toString();
        String sep = separator.toString();
        if (sep.isEmpty()) {
            return StringPool.EMPTY;
        }
        int pos = isLastSeparator ? str.lastIndexOf(sep) : str.indexOf(sep);
        if (pos == INDEX_NOT_FOUND) {
            return str;
        }
        return str.substring(0, pos);
    }

    /**
     * 默认查找最后一个分隔符
     *
     * @param string    string
     * @param separator separator
     * @return the string
     * @since 1.0.0
     */
    public static String subBefore(CharSequence string, CharSequence separator) {
        return subBefore(string, separator, true);
    }

    /**
     * 截取分隔字符串之后的字符串,不包括分隔字符串<br>
     * 如果给定的字符串为空串 (null或"") ,返回原字符串<br>
     * 如果分隔字符串为空串 (null或"") ,则返回空串,如果分隔字符串未找到,返回空串
     * <p>
     * 栗子:
     * <pre>
     * StringUtil.subAfter(null, *)      = null
     * StringUtil.subAfter("", *)        = ""
     * StringUtil.subAfter(*, null)      = ""
     * StringUtil.subAfter("abc", "a")   = "bc"
     * StringUtil.subAfter("abcba", "b") = "cba"
     * StringUtil.subAfter("abc", "c")   = ""
     * StringUtil.subAfter("abc", "d")   = ""
     * StringUtil.subAfter("abc", "")    = "abc"
     * </pre>
     *
     * @param string          被查找的字符串
     * @param separator       分隔字符串 (不包括)
     * @param isLastSeparator 是否查找最后一个分隔字符串 (多次出现分隔字符串时选取最后一个) ,true为选取最后一个
     * @return 切割后的字符串 string
     * @since 1.0.0
     */
    @Nullable
    public static String subAfter(CharSequence string, CharSequence separator, boolean isLastSeparator) {
        if (StringUtils.isEmpty(string)) {
            return null == string ? null : string.toString();
        }
        if (separator == null) {
            return StringPool.EMPTY;
        }
        String str = string.toString();
        String sep = separator.toString();
        int pos = isLastSeparator ? str.lastIndexOf(sep) : str.indexOf(sep);
        if (pos == INDEX_NOT_FOUND) {
            return StringPool.EMPTY;
        }
        return str.substring(pos + separator.length());
    }

    /**
     * 默认查找第一个分隔符
     *
     * @param string    string
     * @param separator separator
     * @return the string
     * @since 1.0.0
     */
    public static String subAfter(CharSequence string, CharSequence separator) {
        return subAfter(string, separator, false);
    }

    /**
     * 截取指定字符串中间部分,不包括标识字符串<br>
     * <p>
     * 栗子:
     * <pre>
     * StringUtil.subBetween(null, *)            = null
     * StringUtil.subBetween("", "")             = ""
     * StringUtil.subBetween("", "tag")          = null
     * StringUtil.subBetween("tagabctag", null)  = null
     * StringUtil.subBetween("tagabctag", "")    = ""
     * StringUtil.subBetween("tagabctag", "tag") = "abc"
     * </pre>
     *
     * @param str            被切割的字符串
     * @param beforeAndAfter 截取开始和结束的字符串标识
     * @return 截取后的字符串 string
     * @since 1.0.0
     */
    @Contract("null, _ -> null; !null, null -> null")
    public static String subBetween(CharSequence str, CharSequence beforeAndAfter) {
        return subBetween(str, beforeAndAfter, beforeAndAfter);
    }

    /**
     * 截取指定字符串中间部分,不包括标识字符串<br>
     * <p>
     * 栗子:
     * <pre>
     * StringUtil.subBetween("wx[b]yz", "[", "]") = "b"
     * StringUtil.subBetween(null, *, *)          = null
     * StringUtil.subBetween(*, null, *)          = null
     * StringUtil.subBetween(*, *, null)          = null
     * StringUtil.subBetween("", "", "")          = ""
     * StringUtil.subBetween("", "", "]")         = null
     * StringUtil.subBetween("", "[", "]")        = null
     * StringUtil.subBetween("yabcz", "", "")     = ""
     * StringUtil.subBetween("yabcz", "y", "z")   = "abc"
     * StringUtil.subBetween("yabczyabcz", "y", "z")   = "abc"
     * </pre>
     *
     * @param str    被切割的字符串
     * @param before 截取开始的字符串标识
     * @param after  截取到的字符串标识
     * @return 截取后的字符串 string
     * @since 1.0.0
     */
    @Contract("null, _, _ -> null; !null, null, _ -> null; !null, !null, null -> null")
    private static String subBetween(CharSequence str, CharSequence before, CharSequence after) {
        if (str == null || before == null || after == null) {
            return null;
        }

        String str2 = str.toString();
        String before2 = before.toString();
        String after2 = after.toString();

        int start = str2.indexOf(before2);
        if (start != INDEX_NOT_FOUND) {
            int end = str2.indexOf(after2, start + before2.length());
            if (end != INDEX_NOT_FOUND) {
                return str2.substring(start + before2.length(), end);
            }
        }
        return null;
    }

    /**
     * 去掉指定前缀
     *
     * @param str    字符串
     * @param prefix 前缀
     * @return 切掉后的字符串 ,若前缀不是 preffix, 返回原字符串
     * @since 1.0.0
     */
    public static String removePrefix(CharSequence str, CharSequence prefix) {
        if (StringUtils.isEmpty(str) || StringUtils.isEmpty(prefix)) {
            return StringPool.EMPTY;
        }

        String str2 = str.toString();
        if (str2.startsWith(prefix.toString())) {
            return subSuf(str2, prefix.length());
        }
        return str2;
    }

    /**
     * 覆写 {@link org.springframework.util.StringUtils#isEmpty(Object)} 过期方法, 避免编译警告
     *
     * @param str str
     * @return boolean
     */
    public static boolean isEmpty(CharSequence str) {
        return hasLength(str);
    }

    /**
     * 切割指定位置之后部分的字符串
     *
     * @param string    字符串
     * @param fromIndex 切割开始的位置 (包括)
     * @return 切割后后剩余的后半部分字符串 string
     * @since 1.0.0
     */
    @Nullable
    public static String subSuf(CharSequence string, int fromIndex) {
        if (StringUtils.isEmpty(string)) {
            return StringPool.EMPTY;
        }
        return sub(string, fromIndex, string.length());
    }

    /**
     * 改进JDK subString<br>
     * index从0开始计算,最后一个字符为-1<br>
     * 如果from和to位置一样,返回 "" <br>
     * 如果from或to为负数,则按照length从后向前数位置,如果绝对值大于字符串长度,则from归到0,to归到length<br>
     * 如果经过修正的index中from大于to,则互换from和to example: <br>
     * abcdefgh 2 3 =》 c <br>
     * abcdefgh 2 -3 =》 cde <br>
     *
     * @param str       String
     * @param fromIndex 开始的index (包括)
     * @param toIndex   结束的index (不包括)
     * @return 字串 string
     * @since 1.0.0
     */
    public static String sub(CharSequence str, int fromIndex, int toIndex) {
        if (StringUtils.isEmpty(str)) {
            return StringPool.EMPTY;
        }
        int len = str.length();

        if (fromIndex < 0) {
            fromIndex = len + fromIndex;
            if (fromIndex < 0) {
                fromIndex = 0;
            }
        } else if (fromIndex > len) {
            fromIndex = len;
        }

        if (toIndex < 0) {
            toIndex = len + toIndex;
            if (toIndex < 0) {
                toIndex = len;
            }
        } else if (toIndex > len) {
            toIndex = len;
        }

        if (toIndex < fromIndex) {
            int tmp = fromIndex;
            fromIndex = toIndex;
            toIndex = tmp;
        }

        if (fromIndex == toIndex) {
            return StringPool.EMPTY;
        }

        return str.toString().substring(fromIndex, toIndex);
    }

    /**
     * 忽略大小写去掉指定前缀
     *
     * @param str    字符串
     * @param prefix 前缀
     * @return 切掉后的字符串 ,若前缀不是 prefix, 返回原字符串
     * @since 1.0.0
     */
    public static String removePrefixIgnoreCase(CharSequence str, CharSequence prefix) {
        if (StringUtils.isEmpty(str) || StringUtils.isEmpty(prefix)) {
            return StringPool.EMPTY;
        }

        String str2 = str.toString();
        if (str2.toLowerCase().startsWith(prefix.toString().toLowerCase())) {
            return subSuf(str2, prefix.length());
        }
        return str2;
    }

    /**
     * 去掉指定后缀,并小写首字母
     *
     * @param str    字符串
     * @param suffix 后缀
     * @return 切掉后的字符串 ,若后缀不是 suffix, 返回原字符串
     * @since 1.0.0
     */
    @NotNull
    public static String removeSufAndLowerFirst(CharSequence str, CharSequence suffix) {
        return firstCharToLower(removeSuffix(str, suffix));
    }

    /**
     * 首字母变小写
     *
     * @param str 字符串
     * @return {String}
     * @since 1.0.0
     */
    @NotNull
    public static String firstCharToLower(@NotNull String str) {
        char firstChar = str.charAt(0);
        if (firstChar >= CharPool.UPPER_A && firstChar <= CharPool.UPPER_Z) {
            char[] arr = str.toCharArray();
            arr[0] += (CharPool.LOWER_A - CharPool.UPPER_A);
            return new String(arr);
        }
        return str;
    }

    /**
     * 去掉指定后缀
     *
     * @param str    字符串
     * @param suffix 后缀
     * @return 切掉后的字符串 ,若后缀不是 suffix, 返回原字符串
     * @since 1.0.0
     */
    public static String removeSuffix(CharSequence str, CharSequence suffix) {
        if (StringUtils.isEmpty(str) || StringUtils.isEmpty(suffix)) {
            return StringPool.EMPTY;
        }

        String str2 = str.toString();
        if (str2.endsWith(suffix.toString())) {
            return subPre(str2, str2.length() - suffix.length());
        }
        return str2;
    }

    /**
     * 切割指定位置之前部分的字符串
     *
     * @param string  字符串
     * @param toIndex 切割到的位置 (不包括)
     * @return 切割后的剩余的前半部分字符串 string
     * @since 1.0.0
     */
    private static String subPre(CharSequence string, int toIndex) {
        return sub(string, 0, toIndex);
    }

    /**
     * 忽略大小写去掉指定后缀
     *
     * @param str    字符串
     * @param suffix 后缀
     * @return 切掉后的字符串 ,若后缀不是 suffix, 返回原字符串
     * @since 1.0.0
     */
    public static String removeSuffixIgnoreCase(CharSequence str, CharSequence suffix) {
        if (StringUtils.isEmpty(str) || StringUtils.isEmpty(suffix)) {
            return StringPool.EMPTY;
        }

        String str2 = str.toString();
        if (str2.toLowerCase().endsWith(suffix.toString().toLowerCase())) {
            return subPre(str2, str2.length() - suffix.length());
        }
        return str2;
    }

    /**
     * 首字母变大写
     *
     * @param str 字符串
     * @return {String}
     * @since 1.0.0
     */
    public static String firstCharToUpper(String str) {
        if (isBlank(str)) {
            return str;
        }
        char firstChar = str.charAt(0);
        if (firstChar >= CharPool.LOWER_A && firstChar <= CharPool.LOWER_Z) {
            char[] arr = str.toCharArray();
            arr[0] -= (CharPool.LOWER_A - CharPool.UPPER_A);
            return new String(arr);
        }
        return str;
    }

    /**
     * 指定范围内查找字符串,忽略大小写<br>
     * <pre>
     * StringUtil.indexOfIgnoreCase(null, *, *)          = -1
     * StringUtil.indexOfIgnoreCase(*, null, *)          = -1
     * StringUtil.indexOfIgnoreCase("", "", 0)           = 0
     * StringUtil.indexOfIgnoreCase("aabaabaa", "A", 0)  = 0
     * StringUtil.indexOfIgnoreCase("aabaabaa", "B", 0)  = 2
     * StringUtil.indexOfIgnoreCase("aabaabaa", "AB", 0) = 1
     * StringUtil.indexOfIgnoreCase("aabaabaa", "B", 3)  = 5
     * StringUtil.indexOfIgnoreCase("aabaabaa", "B", 9)  = -1
     * StringUtil.indexOfIgnoreCase("aabaabaa", "B", -1) = 2
     * StringUtil.indexOfIgnoreCase("aabaabaa", "", 2)   = 2
     * StringUtil.indexOfIgnoreCase("abc", "", 9)        = -1
     * </pre>
     *
     * @param str       字符串
     * @param searchStr 需要查找位置的字符串
     * @return 位置 int
     * @since 1.0.0
     */
    public static int indexOfIgnoreCase(CharSequence str, CharSequence searchStr) {
        return indexOfIgnoreCase(str, searchStr, 0);
    }

    /**
     * 指定范围内查找字符串
     * <pre>
     * StringUtil.indexOfIgnoreCase(null, *, *)          = -1
     * StringUtil.indexOfIgnoreCase(*, null, *)          = -1
     * StringUtil.indexOfIgnoreCase("", "", 0)           = 0
     * StringUtil.indexOfIgnoreCase("aabaabaa", "A", 0)  = 0
     * StringUtil.indexOfIgnoreCase("aabaabaa", "B", 0)  = 2
     * StringUtil.indexOfIgnoreCase("aabaabaa", "AB", 0) = 1
     * StringUtil.indexOfIgnoreCase("aabaabaa", "B", 3)  = 5
     * StringUtil.indexOfIgnoreCase("aabaabaa", "B", 9)  = -1
     * StringUtil.indexOfIgnoreCase("aabaabaa", "B", -1) = 2
     * StringUtil.indexOfIgnoreCase("aabaabaa", "", 2)   = 2
     * StringUtil.indexOfIgnoreCase("abc", "", 9)        = -1
     * </pre>
     *
     * @param str       字符串
     * @param searchStr 需要查找位置的字符串
     * @param fromIndex 起始位置
     * @return 位置 int
     * @since 1.0.0
     */
    private static int indexOfIgnoreCase(CharSequence str, CharSequence searchStr, int fromIndex) {
        return indexOf(str, searchStr, fromIndex, true);
    }

    /**
     * 指定范围内反向查找字符串
     *
     * @param str        字符串
     * @param searchStr  需要查找位置的字符串
     * @param fromIndex  起始位置
     * @param ignoreCase 是否忽略大小写
     * @return 位置 int
     * @since 1.0.0
     */
    public static int indexOf(CharSequence str, CharSequence searchStr, int fromIndex, boolean ignoreCase) {
        if (str == null || searchStr == null) {
            return INDEX_NOT_FOUND;
        }
        if (fromIndex < 0) {
            fromIndex = 0;
        }

        int endLimit = str.length() - searchStr.length() + 1;
        if (fromIndex > endLimit) {
            return INDEX_NOT_FOUND;
        }
        if (searchStr.length() == 0) {
            return fromIndex;
        }

        if (!ignoreCase) {
            // 不忽略大小写调用JDK方法
            return str.toString().indexOf(searchStr.toString(), fromIndex);
        }

        for (int i = fromIndex; i < endLimit; i++) {
            if (isSubEquals(str, i, searchStr, 0, searchStr.length(), true)) {
                return i;
            }
        }
        return INDEX_NOT_FOUND;
    }

    /**
     * 截取两个字符串的不同部分 (长度一致) ,判断截取的子串是否相同<br>
     * 任意一个字符串为null返回false
     *
     * @param str1       第一个字符串
     * @param start1     第一个字符串开始的位置
     * @param str2       第二个字符串
     * @param start2     第二个字符串开始的位置
     * @param length     截取长度
     * @param ignoreCase 是否忽略大小写
     * @return 子串是否相同 boolean
     * @since 1.0.0
     */
    @SuppressWarnings("checkstyle:ParameterNumber")
    @Contract("null, _, _, _, _, _ -> false; !null, _, null, _, _, _ -> false")
    private static boolean isSubEquals(CharSequence str1,
                                       int start1,
                                       CharSequence str2,
                                       int start2,
                                       int length,
                                       boolean ignoreCase) {
        if (null == str1 || null == str2) {
            return false;
        }

        return str1.toString().regionMatches(ignoreCase, start1, str2.toString(), start2, length);
    }

    /**
     * 指定范围内查找字符串,忽略大小写<br>
     *
     * @param str       字符串
     * @param searchStr 需要查找位置的字符串
     * @return 位置 int
     * @since 1.0.0
     */
    public static int lastIndexOfIgnoreCase(CharSequence str, CharSequence searchStr) {
        return lastIndexOfIgnoreCase(str, searchStr, str.length());
    }

    /**
     * 指定范围内查找字符串,忽略大小写<br>
     *
     * @param str       字符串
     * @param searchStr 需要查找位置的字符串
     * @param fromIndex 起始位置,从后往前计数
     * @return 位置 int
     * @since 1.0.0
     */
    private static int lastIndexOfIgnoreCase(CharSequence str, CharSequence searchStr, int fromIndex) {
        return lastIndexOf(str, searchStr, fromIndex, true);
    }

    /**
     * 指定范围内查找字符串<br>
     *
     * @param str        字符串
     * @param searchStr  需要查找位置的字符串
     * @param fromIndex  起始位置,从后往前计数
     * @param ignoreCase 是否忽略大小写
     * @return 位置 int
     * @since 1.0.0
     */
    private static int lastIndexOf(CharSequence str, CharSequence searchStr, int fromIndex, boolean ignoreCase) {
        if (str == null || searchStr == null) {
            return INDEX_NOT_FOUND;
        }
        if (fromIndex < 0) {
            fromIndex = 0;
        }
        fromIndex = Math.min(fromIndex, str.length());

        if (searchStr.length() == 0) {
            return fromIndex;
        }

        if (!ignoreCase) {
            // 不忽略大小写调用JDK方法
            return str.toString().lastIndexOf(searchStr.toString(), fromIndex);
        }

        for (int i = fromIndex; i > 0; i--) {
            if (isSubEquals(str, i, searchStr, 0, searchStr.length(), true)) {
                return i;
            }
        }
        return INDEX_NOT_FOUND;
    }

    /**
     * 返回字符串 searchStr 在字符串 str 中第 ordinal 次出现的位置. <br>
     * 此方法来自: Apache-Commons-Lang
     * <p>
     * 栗子 (*代表任意字符) :
     * <pre>
     * StringUtil.ordinalIndexOf(null, *, *)          = -1
     * StringUtil.ordinalIndexOf(*, null, *)          = -1
     * StringUtil.ordinalIndexOf("", "", *)           = 0
     * StringUtil.ordinalIndexOf("aabaabaa", "a", 1)  = 0
     * StringUtil.ordinalIndexOf("aabaabaa", "a", 2)  = 1
     * StringUtil.ordinalIndexOf("aabaabaa", "b", 1)  = 2
     * StringUtil.ordinalIndexOf("aabaabaa", "b", 2)  = 5
     * StringUtil.ordinalIndexOf("aabaabaa", "ab", 1) = 1
     * StringUtil.ordinalIndexOf("aabaabaa", "ab", 2) = 4
     * StringUtil.ordinalIndexOf("aabaabaa", "", 1)   = 0
     * StringUtil.ordinalIndexOf("aabaabaa", "", 2)   = 0
     * </pre>
     *
     * @param str       被检查的字符串,可以为null
     * @param searchStr 被查找的字符串,可以为null
     * @param ordinal   第几次出现的位置
     * @return 查找到的位置 int
     * @since 1.0.0
     */
    public static int ordinalIndexOf(String str, String searchStr, int ordinal) {
        if (str == null || searchStr == null || ordinal <= 0) {
            return INDEX_NOT_FOUND;
        }
        if (searchStr.length() == 0) {
            return 0;
        }
        int found = 0;
        int index = INDEX_NOT_FOUND;
        do {
            index = str.indexOf(searchStr, index + 1);
            if (index < 0) {
                return index;
            }
            found++;
        } while (found < ordinal);
        return index;
    }

    /**
     * 比较两个字符串 (大小写敏感) .
     * <pre>
     * equalsIgnoreCase(null, null)   = true
     * equalsIgnoreCase(null, &quot;abc&quot;)  = false
     * equalsIgnoreCase(&quot;abc&quot;, null)  = false
     * equalsIgnoreCase(&quot;abc&quot;, &quot;abc&quot;) = true
     * equalsIgnoreCase(&quot;abc&quot;, &quot;ABC&quot;) = true
     * </pre>
     *
     * @param str1 要比较的字符串1
     * @param str2 要比较的字符串2
     * @return 如果两个字符串相同 ,或者都是<code>null</code>,则返回<code>true</code>
     * @since 1.0.0
     */
    @Contract("null, null -> true; null, !null -> false; !null, null -> false")
    public static boolean equals(CharSequence str1, CharSequence str2) {
        return equals(str1, str2, false);
    }

    /**
     * 比较两个字符串是否相等.
     *
     * @param str1       要比较的字符串1
     * @param str2       要比较的字符串2
     * @param ignoreCase 是否忽略大小写
     * @return 如果两个字符串相同 ,或者都是<code>null</code>,则返回<code>true</code>
     * @since 1.0.0
     */
    @Contract("null, null, _ -> true; null, !null, _ -> false; !null, null, _ -> false")
    public static boolean equals(CharSequence str1, CharSequence str2, boolean ignoreCase) {
        if (null == str1) {
            // 只有两个都为null才判断相等
            return str2 == null;
        }
        if (null == str2) {
            // 字符串2空,字符串1非空,直接false
            return false;
        }

        if (ignoreCase) {
            return str1.toString().equalsIgnoreCase(str2.toString());
        } else {
            return str1.equals(str2);
        }
    }

    /**
     * 比较两个字符串 (大小写不敏感) .
     * <pre>
     * equalsIgnoreCase(null, null)   = true
     * equalsIgnoreCase(null, &quot;abc&quot;)  = false
     * equalsIgnoreCase(&quot;abc&quot;, null)  = false
     * equalsIgnoreCase(&quot;abc&quot;, &quot;abc&quot;) = true
     * equalsIgnoreCase(&quot;abc&quot;, &quot;ABC&quot;) = true
     * </pre>
     *
     * @param str1 要比较的字符串1
     * @param str2 要比较的字符串2
     * @return 如果两个字符串相同 ,或者都是<code>null</code>,则返回<code>true</code>
     * @since 1.0.0
     */
    @Contract("null, null -> true; null, !null -> false; !null, null -> false")
    public static boolean equalsIgnoreCase(CharSequence str1, CharSequence str2) {
        return equals(str1, str2, true);
    }

    /**
     * 创建StringBuilder对象
     *
     * @return StringBuilder对象 string builder
     * @since 1.0.0
     */
    @NotNull
    @Contract(value = " -> new", pure = true)
    public static StringBuilder builder() {
        return new StringBuilder();
    }

    /**
     * 创建StringBuilder对象
     *
     * @param capacity 初始大小
     * @return StringBuilder对象 string builder
     * @since 1.0.0
     */
    @NotNull
    @Contract(value = "_ -> new", pure = true)
    public static StringBuilder builder(int capacity) {
        return new StringBuilder(capacity);
    }

    /**
     * 创建StringBuilder对象
     *
     * @param strs 初始字符串列表
     * @return StringBuilder对象 string builder
     * @since 1.0.0
     */
    public static StringBuilder builder(@NotNull CharSequence... strs) {
        StringBuilder sb = new StringBuilder();
        for (CharSequence str : strs) {
            sb.append(str);
        }
        return sb;
    }

    /**
     * 创建StringBuilder对象
     *
     * @param sb   初始StringBuilder
     * @param strs 初始字符串列表
     * @return StringBuilder对象 string builder
     * @since 1.0.0
     */
    @Contract("_, _ -> param1")
    public static StringBuilder appendBuilder(StringBuilder sb, @NotNull CharSequence... strs) {
        for (CharSequence str : strs) {
            sb.append(str);
        }
        return sb;
    }

    /**
     * 获得StringReader
     *
     * @param str 字符串
     * @return StringReader reader
     * @since 1.0.0
     */
    @Contract("null -> null; !null -> new")
    public static StringReader getReader(CharSequence str) {
        if (null == str) {
            return null;
        }
        return new StringReader(str.toString());
    }

    /**
     * 获得StringWriter
     *
     * @return StringWriter writer
     * @since 1.0.0
     */
    @NotNull
    @Contract(value = " -> new", pure = true)
    public static StringWriter getWriter() {
        return new StringWriter();
    }


    /**
     * 统计指定内容中包含指定字符的数量
     *
     * @param content       内容
     * @param charForSearch 被统计的字符
     * @return 包含数量 int
     * @since 1.0.0
     */
    public static int count(CharSequence content, char charForSearch) {
        int count = 0;
        if (StringUtils.isEmpty(content)) {
            return 0;
        }
        int contentLength = content.length();
        for (int i = 0; i < contentLength; i++) {
            if (charForSearch == content.charAt(i)) {
                count++;
            }
        }
        return count;
    }

    /**
     * 下划线转驼峰
     *
     * @param para 字符串
     * @return String string
     * @since 1.0.0
     */
    @NotNull
    public static String underlineToHump(@NotNull String para) {
        return toHump(para, "_");
    }

    /**
     * 驼峰转下划线
     *
     * @param para 字符串
     * @return String string
     * @since 1.0.0
     */
    @NotNull
    public static String humpToUnderline(String para) {
        return fromHump(para, "_");
    }

    /**
     * 横线转驼峰
     *
     * @param para 字符串
     * @return String string
     * @since 1.0.0
     */
    @NotNull
    public static String lineToHump(@NotNull String para) {
        return toHump(para, "-");
    }

    /**
     * 驼峰转横线
     *
     * @param para 字符串
     * @return String string
     * @since 1.0.0
     */
    @NotNull
    public static String humpToLine(String para) {
        return fromHump(para, "-");
    }

    /**
     * To hump string
     *
     * @param para para
     * @param s2   s 2
     * @return the string
     * @since 1.0.0
     */
    @NotNull
    private static String toHump(@NotNull String para, String s2) {
        StringBuilder result = new StringBuilder();
        String[] a = para.split(s2);
        for (String s : a) {
            if (result.length() == 0) {
                result.append(s.toLowerCase());
            } else {
                result.append(s.substring(0, 1).toUpperCase());
                result.append(s.substring(1).toLowerCase());
            }
        }
        return result.toString();
    }

    /**
     * From hump string
     *
     * @param para para
     * @param s    s
     * @return the string
     * @since 1.0.0
     */
    @NotNull
    private static String fromHump(String para, String s) {
        para = firstCharToLower(para);
        StringBuilder sb = new StringBuilder(para);
        int temp = 0;
        for (int i = 0; i < para.length(); i++) {
            if (Character.isUpperCase(para.charAt(i))) {
                sb.insert(i + temp, s);
                temp += 1;
            }
        }
        return sb.toString().toLowerCase();
    }

    /**
     * 重复某个字符串到指定长度
     *
     * @param str    被重复的字符
     * @param padLen 指定长度
     * @return 重复字符字符串 string
     * @since 1.0.0
     */
    @Contract("null, _ -> null")
    private static String repeatByLength(CharSequence str, int padLen) {
        if (null == str) {
            return null;
        }
        if (padLen <= 0) {
            return StringPool.EMPTY;
        }
        int strLen = str.length();
        if (strLen == padLen) {
            return str.toString();
        } else if (strLen > padLen) {
            return subPre(str, padLen);
        }

        // 重复,直到达到指定长度
        char[] padding = new char[padLen];
        for (int i = 0; i < padLen; i++) {
            padding[i] = str.charAt(i % strLen);
        }
        return new String(padding);
    }

    /**
     * 补充字符串以满足最小长度
     * <pre>
     * StrUtil.padPre(null, *, *);//null
     * StrUtil.padPre("1", 3, "ABC");//"AB1"
     * StrUtil.padPre("123", 2, "ABC");//"12"
     * </pre>
     *
     * @param str       字符串
     * @param minLength 最小长度
     * @param padStr    补充的字符
     * @return 补充后的字符串 string
     * @since 1.0.0
     */
    @Contract("null, _, _ -> null")
    private static String padPre(CharSequence str, int minLength, CharSequence padStr) {
        if (null == str) {
            return null;
        }
        int strLen = str.length();
        if (strLen == minLength) {
            return str.toString();
        } else if (strLen > minLength) {
            return subPre(str, minLength);
        }

        return repeatByLength(padStr, minLength - strLen).concat(str.toString());
    }

    /**
     * 切割指定长度的后部分的字符串
     * <pre>
     * StrUtil.subSufByLength("abcde", 3)      =    "cde"
     * StrUtil.subSufByLength("abcde", 0)      =    ""
     * StrUtil.subSufByLength("abcde", -5)     =    ""
     * StrUtil.subSufByLength("abcde", -1)     =    ""
     * StrUtil.subSufByLength("abcde", 5)       =    "abcde"
     * StrUtil.subSufByLength("abcde", 10)     =    "abcde"
     * StrUtil.subSufByLength(null, 3)               =    null
     * </pre>
     *
     * @param string 字符串
     * @param length 切割长度
     * @return 切割后后剩余的后半部分字符串 string
     * @since 1.0.0
     */
    @Nullable
    private static String subSufByLength(CharSequence string, int length) {
        if (StringUtils.isEmpty(string)) {
            return null;
        }
        if (length <= 0) {
            return StringPool.EMPTY;
        }
        return sub(string, -length, string.length());
    }

    /**
     * 补充字符串以满足最小长度
     * <pre>
     * StrUtil.padAfter(null, *, *);//null
     * StrUtil.padAfter("1", 3, "ABC");//"1AB"
     * StrUtil.padAfter("123", 2, "ABC");//"23"
     * </pre>
     *
     * @param str       字符串,如果为<code>null</code>,按照空串处理
     * @param minLength 最小长度
     * @param padStr    补充的字符
     * @return 补充后的字符串 string
     * @since 1.0.0
     */
    @Contract("null, _, _ -> null")
    public static String padAfter(CharSequence str, int minLength, CharSequence padStr) {
        if (null == str) {
            return null;
        }
        int strLen = str.length();
        if (strLen == minLength) {
            return str.toString();
        } else if (strLen > minLength) {
            return subSufByLength(str, minLength);
        }

        return str.toString().concat(repeatByLength(padStr, minLength - strLen));
    }

    /**
     * {@link CharSequence} 转为字符串,null安全
     *
     * @param cs {@link CharSequence}
     * @return 字符串 string
     * @since 1.0.0
     */
    @Contract("null -> null; !null -> !null")
    public static String str(CharSequence cs) {
        return null == cs ? null : cs.toString();
    }

    /**
     * 居中字符串,两边补充指定字符串,如果指定长度小于字符串,则返回原字符串
     * <pre>
     * StrUtil.center(null, *, *)     = null
     * StrUtil.center("", 4, " ")     = "    "
     * StrUtil.center("ab", -1, " ")  = "ab"
     * StrUtil.center("ab", 4, " ")   = " ab "
     * StrUtil.center("abcd", 2, " ") = "abcd"
     * StrUtil.center("a", 4, " ")    = " a  "
     * StrUtil.center("a", 4, "yz")   = "yayz"
     * StrUtil.center("abc", 7, null) = "  abc  "
     * StrUtil.center("abc", 7, "")   = "  abc  "
     * </pre>
     *
     * @param str    字符串
     * @param size   指定长度
     * @param padStr 两边补充的字符串
     * @return 补充后的字符串 string
     * @since 1.0.0
     */
    public static String center(CharSequence str, int size, CharSequence padStr) {
        if (str == null || size <= 0) {
            return str(str);
        }
        if (StringUtils.isEmpty(padStr)) {
            padStr = StringPool.SPACE;
        }
        int strLen = str.length();
        int pads = size - strLen;
        if (pads <= 0) {
            return str.toString();
        }
        str = padPre(str, strLen + pads / 2, padStr);
        str = padAfter(str, size, padStr);
        return str.toString();
    }

    /**
     * 清理字符串,清理出某些不可见字符和一些sql特殊字符
     *
     * @param txt 文本
     * @return {String}
     * @since 1.0.0
     */
    @Contract("null -> null")
    @Nullable
    public static String cleanText(@Nullable String txt) {
        if (txt == null) {
            return null;
        }
        return SPECIAL_CHARS_REGEX.matcher(txt).replaceAll(StringPool.EMPTY);
    }

    /**
     * 获取标识符,用于参数清理
     *
     * @param param 参数
     * @return 清理后的标识符 string
     * @since 1.0.0
     */
    @Contract("null -> null")
    @Nullable
    public static String cleanIdentifier(@Nullable String param) {
        if (param == null) {
            return null;
        }
        StringBuilder paramBuilder = new StringBuilder();
        for (int i = 0; i < param.length(); i++) {
            char c = param.charAt(i);
            if (Character.isJavaIdentifierPart(c)) {
                paramBuilder.append(c);
            }
        }
        return paramBuilder.toString();
    }

    /**
     * 删除空白符
     *
     * @param str the str
     * @return the string
     * @since 1.0.0
     */
    public static String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile(BLANK_SYMBOL);
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    /**
     * 生成uuid,采用 jdk 9 的形式,优化性能
     *
     * @return UUID uuid
     * @since 1.0.0
     */
    @NotNull
    @Contract(" -> new")
    public static String getUid() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        long lsb = random.nextLong();
        long msb = random.nextLong();
        char[] buf = new char[32];
        formatUnsignedLong(lsb, buf, 20, 12);
        formatUnsignedLong(lsb >>> 48, buf, 16, 4);
        formatUnsignedLong(msb, buf, 12, 4);
        formatUnsignedLong(msb >>> 16, buf, 8, 4);
        formatUnsignedLong(msb >>> 32, buf, 0, 8);
        return new String(buf);
    }

    /**
     * Format unsigned long *
     *
     * @param val    val
     * @param buf    buf
     * @param offset offset
     * @param len    len
     * @since 1.0.0
     */
    private static void formatUnsignedLong(long val, @NotNull char @NotNull [] buf, int offset, int len) {
        int charPos = offset + len;
        int radix = 1 << 4;
        int mask = radix - 1;
        do {
            buf[--charPos] = NumberUtils.DIGITS[((int) val) & mask];
            val >>>= 4;
        } while (charPos > offset);
    }

    /**
     * 格式化文本, {} 表示占位符<br>
     * 此方法只是简单将占位符 {} 按照顺序替换为参数<br>
     * 如果想输出 {} 使用 \\转义 { 即可,如果想输出 {} 之前的 \ 使用双转义符 \\\\ 即可<br>
     * 例: <br>
     * 通常使用: format("this is {} for {}", "a", "b") =》 this is a for b<br>
     * 转义{}:  format("this is \\{} for {}", "a", "b") =》 this is \{} for a<br>
     * 转义\:  format("this is \\\\{} for {}", "a", "b") =》 this is \a for b<br>
     *
     * @param template 文本模板,被替换的部分用 {} 表示
     * @param params   参数值
     * @return 格式化后的文本 string
     * @since 1.0.0
     */
    @Contract("null, _ -> null")
    public static String format(CharSequence template, Object... params) {
        if (null == template) {
            return null;
        }
        if (Tools.isEmpty(params) || isBlank(template)) {
            return template.toString();
        }
        return StrFormatter.format(template.toString(), params);
    }

    /**
     * 格式化文本,使用 {varName} 占位<br>
     * map = {a: "aValue", b: "bValue"} format("{a} and {b}", map) ---=》 aValue and bValue
     *
     * @param template 文本模板,被替换的部分用 {key} 表示
     * @param map      参数值对
     * @return 格式化后的文本 string
     * @since 1.0.0
     */
    @Contract("null, _ -> null; !null, null -> !null")
    public static String format(CharSequence template, Map<?, ?> map) {
        if (null == template) {
            return null;
        }
        if (null == map || map.isEmpty()) {
            return template.toString();
        }

        String template2 = template.toString();
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            template2 = template2.replace("{" + entry.getKey() + "}", Tools.toStr(entry.getValue()));
        }
        return template2;
    }

    /**
     * 查找指定字符串是否包含指定字符串列表中的任意一个字符串
     *
     * @param str      指定字符串
     * @param testStrs 需要检查的字符串数组
     * @return 是否包含任意一个字符串 boolean
     * @since 1.0.0
     */
    public static boolean containsAny(CharSequence str, CharSequence... testStrs) {
        return null != getContainsStr(str, testStrs);
    }

    /**
     * 查找指定字符串是否包含指定字符串列表中的任意一个字符串,如果包含返回找到的第一个字符串
     *
     * @param str      指定字符串
     * @param testStrs 需要检查的字符串数组
     * @return 被包含的第一个字符串 contains str
     * @since 1.0.0
     */
    @Nullable
    private static String getContainsStr(CharSequence str, CharSequence... testStrs) {
        if (StringUtils.isEmpty(str) || Tools.isEmpty(testStrs)) {
            return null;
        }
        for (CharSequence checkStr : testStrs) {
            if (str.toString().contains(checkStr)) {
                return checkStr.toString();
            }
        }
        return null;
    }

    /**
     * 查找指定字符串是否包含指定字符串列表中的任意一个字符串<br>
     * 忽略大小写
     *
     * @param str      指定字符串
     * @param testStrs 需要检查的字符串数组
     * @return 是否包含任意一个字符串 boolean
     * @since 1.0.0
     */
    public static boolean containsAnyIgnoreCase(CharSequence str, CharSequence... testStrs) {
        return null != getContainsStrIgnoreCase(str, testStrs);
    }

    /**
     * 查找指定字符串是否包含指定字符串列表中的任意一个字符串,如果包含返回找到的第一个字符串<br>
     * 忽略大小写
     *
     * @param str      指定字符串
     * @param testStrs 需要检查的字符串数组
     * @return 被包含的第一个字符串 contains str ignore case
     * @since 1.0.0
     */
    @Nullable
    private static String getContainsStrIgnoreCase(CharSequence str, CharSequence... testStrs) {
        if (StringUtils.isEmpty(str) || Tools.isEmpty(testStrs)) {
            return null;
        }
        for (CharSequence testStr : testStrs) {
            if (containsIgnoreCase(str, testStr)) {
                return testStr.toString();
            }
        }
        return null;
    }

    /**
     * 是否包含特定字符,忽略大小写,如果给定两个参数都为<code>null</code>,返回true
     *
     * @param str     被检测字符串
     * @param testStr 被测试是否包含的字符串
     * @return 是否包含 boolean
     * @since 1.0.0
     */
    @Contract("null, null -> true; null, !null -> false")
    private static boolean containsIgnoreCase(CharSequence str, CharSequence testStr) {
        if (null == str) {
            // 如果被监测字符串和
            return null == testStr;
        }
        return str.toString().toLowerCase().contains(testStr.toString().toLowerCase());
    }

    /**
     * 统计指定内容中包含指定字符串的数量<br>
     * 参数为 {@code null} 或者 "" 返回 {@code 0}.
     * <pre>
     * StringUtil.count(null, *)       = 0
     * StringUtil.count("", *)         = 0
     * StringUtil.count("abba", null)  = 0
     * StringUtil.count("abba", "")    = 0
     * StringUtil.count("abba", "a")   = 2
     * StringUtil.count("abba", "ab")  = 1
     * StringUtil.count("abba", "xxx") = 0
     * </pre>
     *
     * @param content      被查找的字符串
     * @param strForSearch 需要查找的字符串
     * @return 查找到的个数 int
     * @since 1.0.0
     */
    public static int count(CharSequence content, CharSequence strForSearch) {
        if (Tools.hasEmpty(content, strForSearch) || strForSearch.length() > content.length()) {
            return 0;
        }

        int count = 0;
        int idx = 0;
        String content2 = content.toString();
        String strForSearch2 = strForSearch.toString();
        while ((idx = content2.indexOf(strForSearch2, idx)) > -1) {
            count++;
            idx += strForSearch.length();
        }
        return count;
    }

    /**
     * Replace string
     *
     * @param source    source
     * @param parameter parameter
     * @return the string
     * @since 1.0.0
     */
    public static String replace(String source, Map<String, Object> parameter) {
        return replace(source,
            parameter,
            StringSubstitutor.DEFAULT_VAR_START,
            StringSubstitutor.DEFAULT_VAR_END,
            true);
    }


    /**
     * 替换
     *
     * @param source                        源内容
     * @param parameter                     占位符参数
     * @param prefix                        占位符前缀 例如:${
     * @param suffix                        占位符后缀 例如:}
     * @param enableSubstitutionInVariables 是否在变量名称中进行替换 例如:${system-${版本}} 转义符默认为'$'.
     *                                      如果这个字符放在一个变量引用之前, 这个引用将被忽略, 不会被替换 如$${a}将直接输出${a}
     * @return string string
     * @since 1.0.0
     */
    public static String replace(String source,
                                 Map<String, Object> parameter,
                                 String prefix,
                                 String suffix,
                                 boolean enableSubstitutionInVariables) {

        StringSubstitutor strSubstitutor = new StringSubstitutor(parameter, prefix, suffix);
        strSubstitutor.setEnableSubstitutionInVariables(enableSubstitutionInVariables);
        return strSubstitutor.replace(source);
    }

    /**
     * 字符串去除空白内容：
     * <ul>
     *     <li>\n 回车</li>
     *     <li>\t 水平制表符</li>
     *     <li>\s 空格</li>
     *     <li>\r 换行</li>
     * </ul>
     *
     * @param str 字符串
     */
    public static String replaceAllBlank(String str) {
        Matcher matcher = REPLACE_BLANK.matcher(str);
        return matcher.replaceAll("");
    }

}

