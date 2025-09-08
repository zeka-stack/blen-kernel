package dev.dong4j.zeka.kernel.common.support;

import com.google.common.collect.Lists;
import dev.dong4j.zeka.kernel.common.util.StringPool;
import dev.dong4j.zeka.kernel.common.util.StringUtils;
import dev.dong4j.zeka.kernel.common.util.Tools;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.util.ObjectUtils;

/**
 * 字符串切分器，提供强大而灵活的字符串分割功能
 * <p>
 * 该工具类提供了比JDK原生String.split()更加丰富和灵活的字符串分割功能
 * 支持多种分隔符类型、大小写处理、空格处理和数量限制等高级特性
 * <p>
 * 主要特性：
 * - 多种分隔符支持：字符、字符串、正则表达式、空白符
 * - 大小写控制：支持忽略或敏感大小写的分割
 * - 空格处理：可以选择是否移除分割后元素的前后空格
 * - 空串过滤：支持忽略或保留空字符串元素
 * - 数量限制：可以限制分割结果的最大元素数量
 * - 路径分割：专门针对Unix风格路径的分割优化
 * - 按长度分割：支持按固定字符数将字符串分段
 * <p>
 * 使用场景：
 * - CSV/TSV文件的解析，需要精确控制分割行为
 * - 配置文件的参数解析，支持多种分隔符
 * - URL和路径的解析，需要处理特殊字符
 * - 数据库查询结果的处理，处理字段分割
 * - 文本挖掘和日志分析，提取结构化信息
 * - 内容管理系统的标签和关键词处理
 * <p>
 * 性能优势：
 * - 相比JDK原生方法有更高的性能和灵活性
 * - 优化的内存分配策略，减少不必要的对象创建
 * - 支持早期返回优化，特殊情况下直接返回结果
 * - 针对常用场景的专项优化，如路径分割等
 * <p>
 * 使用示例：
 * <pre>{@code
 * // 基本字符串分割
 * List<String> result = StrSpliter.split("a,b,c", ',', true, true);
 * // 结果: ["a", "b", "c"]
 *
 * // 路径分割
 * List<String> parts = StrSpliter.splitPath("/usr/local/bin");
 * // 结果: ["usr", "local", "bin"]
 *
 * // 限制分割数量
 * List<String> limited = StrSpliter.split("a.b.c.d", '.', 2, true, true);
 * // 结果: ["a", "b.c.d"]
 *
 * // 大小写不敏感分割
 * List<String> ignoreCase = StrSpliter.splitIgnoreCase("A;b;C", ';', true, true);
 *
 * // 按长度分割
 * String[] chunks = StrSpliter.splitByLength("123456789", 3);
 * // 结果: ["123", "456", "789"]
 * }</pre>
 * <p>
 * 注意事项：
 * - 该工具类的所有方法都是线程安全的
 * - 空字符串和null值会被安全处理，返回空集合
 * - 限制数量参数为-1表示不限制，0表示使用默认行为
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.27 18:18
 * @since 1.0.0
 */
@UtilityClass
public class StrSpliter {

    /**
     * 切分字符串路径,仅支持Unix分界符: /
     *
     * @param str 被切分的字符串
     * @return 切分后的集合 string [ ]
     * @since 1.0.0
     */
    @NotNull
    public static String[] splitPathToArray(String str) {
        return toArray(splitPath(str));
    }

    /**
     * List转Array
     *
     * @param list List
     * @return Array string [ ]
     * @since 1.0.0
     */
    @NotNull
    private static String[] toArray(@NotNull List<String> list) {
        return list.toArray(new String[0]);
    }

    /**
     * 切分字符串路径,仅支持Unix分界符: /
     *
     * @param str 被切分的字符串
     * @return 切分后的集合 list
     * @since 1.0.0
     */
    public static List<String> splitPath(String str) {
        return splitPath(str, 0);
    }

    /**
     * 切分字符串路径,仅支持Unix分界符: /
     *
     * @param str   被切分的字符串
     * @param limit 限制分片数
     * @return 切分后的集合 list
     * @since 1.0.0
     */
    public static List<String> splitPath(String str, int limit) {
        return split(str, StringPool.SLASH, limit, true, true);
    }

    /**
     * 切分字符串,不忽略大小写
     *
     * @param str         被切分的字符串
     * @param separator   分隔符字符串
     * @param limit       限制分片数
     * @param isTrim      是否去除切分字符串后每个元素两边的空格
     * @param ignoreEmpty 是否忽略空串
     * @return 切分后的集合 list
     * @since 1.0.0
     */
    public static List<String> split(String str, String separator, int limit, boolean isTrim, boolean ignoreEmpty) {
        return split(str, separator, limit, isTrim, ignoreEmpty, false);
    }

    /**
     * 切分字符串
     *
     * @param str         被切分的字符串
     * @param separator   分隔符字符串
     * @param limit       限制分片数
     * @param isTrim      是否去除切分字符串后每个元素两边的空格
     * @param ignoreEmpty 是否忽略空串
     * @param ignoreCase  是否忽略大小写
     * @return 切分后的集合 list
     * @since 1.0.0
     */
    @SuppressWarnings(value = {"checkstyle:ReturnCount", "checkstyle:ParameterNumber"})
    public static List<String> split(String str, String separator, int limit, boolean isTrim, boolean ignoreEmpty, boolean ignoreCase) {
        if (ObjectUtils.isEmpty(str)) {
            return Lists.newArrayListWithCapacity(0);
        }
        if (limit == 1) {
            return addToList(Lists.newArrayListWithCapacity(1), str, isTrim, ignoreEmpty);
        }

        if (ObjectUtils.isEmpty(separator)) {
            return split(str, limit);
        } else if (separator.length() == 1) {
            return split(str, separator.charAt(0), limit, isTrim, ignoreEmpty, ignoreCase);
        }

        ArrayList<String> list = Lists.newArrayList();
        int len = str.length();
        int separatorLen = separator.length();
        int start = 0;
        int i = 0;
        while (i < len) {
            i = StringUtils.indexOf(str, separator, start, ignoreCase);
            if (i > -1) {
                addToList(list, str.substring(start, i), isTrim, ignoreEmpty);
                start = i + separatorLen;

                // 检查是否超出范围 (最大允许limit-1个,剩下一个留给末尾字符串)
                if (limit > 0 && list.size() > limit - 2) {
                    break;
                }
            } else {
                break;
            }
        }
        return addToList(list, str.substring(start, len), isTrim, ignoreEmpty);
    }

    /**
     * 将字符串加入List中
     *
     * @param list        列表
     * @param part        被加入的部分
     * @param isTrim      是否去除两端空白符
     * @param ignoreEmpty 是否略过空字符串 (空字符串不做为一个元素)
     * @return 列表 list
     * @since 1.0.0
     */
    @Contract("_, _, _, _ -> param1")
    private static List<String> addToList(List<String> list, String part, boolean isTrim, boolean ignoreEmpty) {
        if (isTrim) {
            part = part.trim();
        }
        if (!ignoreEmpty || !part.isEmpty()) {
            list.add(part);
        }
        return list;
    }

    /**
     * 使用空白符切分字符串<br>
     * 切分后的字符串两边不包含空白符,空串或空白符串并不做为元素之一
     *
     * @param str   被切分的字符串
     * @param limit 限制分片数
     * @return 切分后的集合 list
     * @since 1.0.0
     */
    public static List<String> split(String str, int limit) {
        if (ObjectUtils.isEmpty(str)) {
            return Lists.newArrayListWithCapacity(0);
        }
        if (limit == 1) {
            return addToList(Lists.newArrayListWithCapacity(1), str, true, true);
        }

        ArrayList<String> list = Lists.newArrayList();
        int len = str.length();
        int start = 0;
        for (int i = 0; i < len; i++) {
            if (Tools.isEmpty(str.charAt(i))) {
                addToList(list, str.substring(start, i), true, true);
                start = i + 1;
                if (limit > 0 && list.size() > limit - 2) {
                    break;
                }
            }
        }
        return addToList(list, str.substring(start, len), true, true);
    }

    /**
     * 切分字符串
     *
     * @param str         被切分的字符串
     * @param separator   分隔符字符
     * @param limit       限制分片数,-1不限制
     * @param isTrim      是否去除切分字符串后每个元素两边的空格
     * @param ignoreEmpty 是否忽略空串
     * @param ignoreCase  是否忽略大小写
     * @return 切分后的集合 list
     * @since 1.0.0
     */
    @SuppressWarnings("checkstyle:ParameterNumber")
    public static List<String> split(String str, char separator, int limit, boolean isTrim, boolean ignoreEmpty, boolean ignoreCase) {
        if (ObjectUtils.isEmpty(str)) {
            return Lists.newArrayListWithCapacity(0);
        }
        if (limit == 1) {
            return addToList(Lists.newArrayListWithCapacity(1), str, isTrim, ignoreEmpty);
        }

        ArrayList<String> list = Lists.newArrayListWithCapacity(limit > 0 ? limit : 16);
        int len = str.length();
        int start = 0;
        for (int i = 0; i < len; i++) {
            if (Tools.equals(separator, str.charAt(i))) {
                addToList(list, str.substring(start, i), isTrim, ignoreEmpty);
                start = i + 1;

                // 检查是否超出范围 (最大允许limit-1个,剩下一个留给末尾字符串)
                if (limit > 0 && list.size() > limit - 2) {
                    break;
                }
            }
        }
        return addToList(list, str.substring(start, len), isTrim, ignoreEmpty);
    }

    /**
     * 切分字符串路径,仅支持Unix分界符: /
     *
     * @param str   被切分的字符串
     * @param limit 限制分片数
     * @return 切分后的集合 string [ ]
     * @since 1.0.0
     */
    @NotNull
    public static String[] splitPathToArray(String str, int limit) {
        return toArray(splitPath(str, limit));
    }

    /**
     * 切分字符串
     *
     * @param str         被切分的字符串
     * @param separator   分隔符字符
     * @param ignoreEmpty 是否忽略空串
     * @return 切分后的集合 list
     * @since 1.0.0
     */
    public static List<String> splitTrim(String str, char separator, boolean ignoreEmpty) {
        return split(str, separator, 0, true, ignoreEmpty);
    }

    /**
     * 切分字符串,大小写敏感
     *
     * @param str         被切分的字符串
     * @param separator   分隔符字符
     * @param limit       限制分片数,-1不限制
     * @param isTrim      是否去除切分字符串后每个元素两边的空格
     * @param ignoreEmpty 是否忽略空串
     * @return 切分后的集合 list
     * @since 1.0.0
     */
    public static List<String> split(String str, char separator, int limit, boolean isTrim, boolean ignoreEmpty) {
        return split(str, separator, limit, isTrim, ignoreEmpty, false);
    }

    /**
     * 切分字符串
     *
     * @param str         被切分的字符串
     * @param separator   分隔符字符
     * @param isTrim      是否去除切分字符串后每个元素两边的空格
     * @param ignoreEmpty 是否忽略空串
     * @return 切分后的集合 list
     * @since 1.0.0
     */
    public static List<String> split(String str, char separator, boolean isTrim, boolean ignoreEmpty) {
        return split(str, separator, 0, isTrim, ignoreEmpty);
    }

    /**
     * 切分字符串,大小写敏感,去除每个元素两边空白符
     *
     * @param str         被切分的字符串
     * @param separator   分隔符字符
     * @param limit       限制分片数,-1不限制
     * @param ignoreEmpty 是否忽略空串
     * @return 切分后的集合 list
     * @since 1.0.0
     */
    public static List<String> splitTrim(String str, char separator, int limit, boolean ignoreEmpty) {
        return split(str, separator, limit, true, ignoreEmpty, false);
    }

    /**
     * 切分字符串,忽略大小写
     *
     * @param str         被切分的字符串
     * @param separator   分隔符字符
     * @param limit       限制分片数,-1不限制
     * @param isTrim      是否去除切分字符串后每个元素两边的空格
     * @param ignoreEmpty 是否忽略空串
     * @return 切分后的集合 list
     * @since 1.0.0
     */
    public static List<String> splitIgnoreCase(String str, char separator, int limit, boolean isTrim, boolean ignoreEmpty) {
        return split(str, separator, limit, isTrim, ignoreEmpty, true);
    }

    /**
     * 切分字符串为字符串数组
     *
     * @param str         被切分的字符串
     * @param separator   分隔符字符
     * @param limit       限制分片数
     * @param isTrim      是否去除切分字符串后每个元素两边的空格
     * @param ignoreEmpty 是否忽略空串
     * @return 切分后的集合 string [ ]
     * @since 1.0.0
     */
    @NotNull
    public static String[] splitToArray(String str, char separator, int limit, boolean isTrim, boolean ignoreEmpty) {
        return toArray(split(str, separator, limit, isTrim, ignoreEmpty));
    }

    /**
     * 切分字符串,去除每个元素两边空格,忽略大小写
     *
     * @param str         被切分的字符串
     * @param separator   分隔符字符串
     * @param ignoreEmpty 是否忽略空串
     * @return 切分后的集合 list
     * @since 1.0.0
     */
    public static List<String> splitTrim(String str, String separator, boolean ignoreEmpty) {
        return split(str, separator, true, ignoreEmpty);
    }

    /**
     * 切分字符串,不忽略大小写
     *
     * @param str         被切分的字符串
     * @param separator   分隔符字符串
     * @param isTrim      是否去除切分字符串后每个元素两边的空格
     * @param ignoreEmpty 是否忽略空串
     * @return 切分后的集合 list
     * @since 1.0.0
     */
    public static List<String> split(String str, String separator, boolean isTrim, boolean ignoreEmpty) {
        return split(str, separator, -1, isTrim, ignoreEmpty, false);
    }

    /**
     * 切分字符串,去除每个元素两边空格,忽略大小写
     *
     * @param str         被切分的字符串
     * @param separator   分隔符字符串
     * @param limit       限制分片数
     * @param ignoreEmpty 是否忽略空串
     * @return 切分后的集合 list
     * @since 1.0.0
     */
    public static List<String> splitTrim(String str, String separator, int limit, boolean ignoreEmpty) {
        return split(str, separator, limit, true, ignoreEmpty);
    }

    /**
     * 切分字符串,忽略大小写
     *
     * @param str         被切分的字符串
     * @param separator   分隔符字符串
     * @param limit       限制分片数
     * @param isTrim      是否去除切分字符串后每个元素两边的空格
     * @param ignoreEmpty 是否忽略空串
     * @return 切分后的集合 list
     * @since 1.0.0
     */
    public static List<String> splitIgnoreCase(String str, String separator, int limit, boolean isTrim, boolean ignoreEmpty) {
        return split(str, separator, limit, isTrim, ignoreEmpty, true);
    }

    /**
     * 切分字符串,去除每个元素两边空格,忽略大小写
     *
     * @param str         被切分的字符串
     * @param separator   分隔符字符串
     * @param limit       限制分片数
     * @param ignoreEmpty 是否忽略空串
     * @return 切分后的集合 list
     * @since 1.0.0
     */
    public static List<String> splitTrimIgnoreCase(String str, String separator, int limit, boolean ignoreEmpty) {
        return split(str, separator, limit, true, ignoreEmpty, true);
    }

    /**
     * 切分字符串为字符串数组
     *
     * @param str         被切分的字符串
     * @param separator   分隔符字符
     * @param limit       限制分片数
     * @param isTrim      是否去除切分字符串后每个元素两边的空格
     * @param ignoreEmpty 是否忽略空串
     * @return 切分后的集合 string [ ]
     * @since 1.0.0
     */
    @NotNull
    public static String[] splitToArray(String str, String separator, int limit, boolean isTrim, boolean ignoreEmpty) {
        return toArray(split(str, separator, limit, isTrim, ignoreEmpty));
    }

    /**
     * 切分字符串为字符串数组
     *
     * @param str   被切分的字符串
     * @param limit 限制分片数
     * @return 切分后的集合 string [ ]
     * @since 1.0.0
     */
    @NotNull
    public static String[] splitToArray(String str, int limit) {
        return toArray(split(str, limit));
    }

    /**
     * 通过正则切分字符串为字符串数组
     *
     * @param str              被切分的字符串
     * @param separatorPattern 分隔符正则{@link Pattern}
     * @param limit            限制分片数
     * @param isTrim           是否去除切分字符串后每个元素两边的空格
     * @param ignoreEmpty      是否忽略空串
     * @return 切分后的集合 string [ ]
     * @since 1.0.0
     */
    @NotNull
    public static String[] splitToArray(String str, Pattern separatorPattern, int limit, boolean isTrim, boolean ignoreEmpty) {
        return toArray(split(str, separatorPattern, limit, isTrim, ignoreEmpty));
    }

    /**
     * 通过正则切分字符串
     *
     * @param str              字符串
     * @param separatorPattern 分隔符正则{@link Pattern}
     * @param limit            限制分片数
     * @param isTrim           是否去除切分字符串后每个元素两边的空格
     * @param ignoreEmpty      是否忽略空串
     * @return 切分后的集合 list
     * @since 1.0.0
     */
    @SuppressWarnings("checkstyle:ReturnCount")
    public static List<String> split(String str, Pattern separatorPattern, int limit, boolean isTrim, boolean ignoreEmpty) {
        if (ObjectUtils.isEmpty(str)) {
            return Lists.newArrayListWithCapacity(0);
        }
        if (limit == 1) {
            return addToList(Lists.newArrayListWithCapacity(1), str, isTrim, ignoreEmpty);
        }

        if (null == separatorPattern) {
            return split(str, limit);
        }

        Matcher matcher = separatorPattern.matcher(str);
        ArrayList<String> list = Lists.newArrayList();
        int len = str.length();
        int start = 0;
        while (matcher.find()) {
            addToList(list, str.substring(start, matcher.start()), isTrim, ignoreEmpty);
            start = matcher.end();

            if (limit > 0 && list.size() > limit - 2) {
                break;
            }
        }
        return addToList(list, str.substring(start, len), isTrim, ignoreEmpty);
    }

    /**
     * 根据给定长度,将给定字符串截取为多个部分
     *
     * @param str 字符串
     * @param len 每一个小节的长度
     * @return 截取后的字符串数组 string [ ]
     * @since 1.0.0
     */
    public static String @NotNull [] splitByLength(@NotNull String str, int len) {
        int partCount = str.length() / len;
        int lastPartCount = str.length() % len;
        int fixPart = 0;
        if (lastPartCount != 0) {
            fixPart = 1;
        }

        String[] strs = new String[partCount + fixPart];
        for (int i = 0; i < partCount + fixPart; i++) {
            if (i == partCount + fixPart - 1 && lastPartCount != 0) {
                strs[i] = str.substring(i * len, i * len + lastPartCount);
            } else {
                strs[i] = str.substring(i * len, i * len + len);
            }
        }
        return strs;
    }
}
