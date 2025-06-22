package dev.dong4j.zeka.kernel.common.util;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

/**
 * <p>Description: DateTime 工具类 </p>
 *
 * @author dong4j
 * @version 1.2.3
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
