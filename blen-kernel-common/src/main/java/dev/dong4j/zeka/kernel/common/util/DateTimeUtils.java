package dev.dong4j.zeka.kernel.common.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

/**
 * <p>DateTime 工具类.
 * <p>专门处理Java 8新的时间API（LocalDateTime、LocalDate等）的工具类.
 * <p>提供日期时间格式化、解析、转换等功能，简化Java 8时间API的使用.
 * <p>主要功能：
 * <ul>
 *     <li>日期时间格式化（支持多种格式）</li>
 *     <li>字符串解析为时间对象</li>
 *     <li>Date与LocalDateTime互转</li>
 *     <li>时间格式化器的统一管理</li>
 * </ul>
 * <p>使用示例：
 * <pre>
 * // 日期时间格式化
 * LocalDateTime now = LocalDateTime.now();
 * String dateTimeStr = DateTimeUtils.formatDateTime(now); // "yyyy-MM-dd HH:mm:ss"
 * String dateStr = DateTimeUtils.formatDate(now); // "yyyy-MM-dd"
 * String timeStr = DateTimeUtils.formatTime(now); // "HH:mm:ss"
 *
 * // 自定义格式化
 * String customFormat = DateTimeUtils.format(now, "yyyy/MM/dd HH:mm");
 *
 * // 字符串解析
 * String dateStr = "2023-01-01 12:00:00";
 * TemporalAccessor parsed = DateTimeUtils.parse(dateStr, DateUtils.PATTERN_DATETIME);
 *
 * // Date与LocalDateTime转换
 * Date date = new Date();
 * LocalDateTime localDateTime = DateTimeUtils.toDateTime(date.toInstant());
 * Instant instant = DateTimeUtils.toInstant(localDateTime);
 * </pre>
 * <p>技术特性：
 * <ul>
 *     <li>专门针对Java 8新的时间API设计</li>
 *     <li>提供常用的日期时间格式化器</li>
 *     <li>支持自定义日期格式的解析和格式化</li>
 *     <li>提供Date与LocalDateTime的互转功能</li>
 *     <li>使用lombok的@UtilityClass注解，确保工具类的正确性</li>
 * </ul>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.27 18:18
 * @since 1.0.0
 */
@UtilityClass
public class DateTimeUtils {
    /**
     * The constant DATETIME_FORMAT.
     */
    public static final DateTimeFormatter DATETIME_FORMAT = DateTimeFormatter.ofPattern(DateUtils.PATTERN_DATETIME);
    /**
     * The constant DATE_FORMAT.
     */
    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern(DateUtils.PATTERN_DATE);
    /**
     * The constant TIME_FORMAT.
     */
    public static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern(DateUtils.PATTERN_TIME);

    /**
     * 日期时间格式化
     *
     * @param temporal 时间
     * @return 格式化后的时间 string
     * @since 1.0.0
     */
    @NotNull
    public static String formatDateTime(TemporalAccessor temporal) {
        return DATETIME_FORMAT.format(temporal);
    }

    /**
     * 日期时间格式化
     *
     * @param temporal 时间
     * @return 格式化后的时间 string
     * @since 1.0.0
     */
    @NotNull
    public static String formatDate(TemporalAccessor temporal) {
        return DATE_FORMAT.format(temporal);
    }

    /**
     * 时间格式化
     *
     * @param temporal 时间
     * @return 格式化后的时间 string
     * @since 1.0.0
     */
    @NotNull
    public static String formatTime(TemporalAccessor temporal) {
        return TIME_FORMAT.format(temporal);
    }

    /**
     * 日期格式化
     *
     * @param temporal 时间
     * @param pattern  表达式
     * @return 格式化后的时间 string
     * @since 1.0.0
     */
    @NotNull
    public static String format(TemporalAccessor temporal, String pattern) {
        return DateTimeFormatter.ofPattern(pattern).format(temporal);
    }

    /**
     * 将字符串转换为时间
     *
     * @param dateStr 时间字符串
     * @param pattern 表达式
     * @return 时间 temporal accessor
     * @since 1.0.0
     */
    @NotNull
    public static TemporalAccessor parse(String dateStr, String pattern) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern(pattern);
        return format.parse(dateStr);
    }

    /**
     * 将字符串转换为时间
     *
     * @param dateStr   时间字符串
     * @param formatter DateTimeFormatter
     * @return 时间 temporal accessor
     * @since 1.0.0
     */
    @NotNull
    public static TemporalAccessor parse(String dateStr, @NotNull DateTimeFormatter formatter) {
        return formatter.parse(dateStr);
    }

    /**
     * 时间转 Instant
     *
     * @param dateTime 时间
     * @return Instant instant
     * @since 1.0.0
     */
    public static Instant toInstant(@NotNull LocalDateTime dateTime) {
        return dateTime.atZone(ZoneId.systemDefault()).toInstant();
    }

    /**
     * Instant 转 时间
     *
     * @param instant Instant
     * @return Instant local date time
     * @since 1.0.0
     */
    @NotNull
    public static LocalDateTime toDateTime(Instant instant) {
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }
}
