package dev.dong4j.zeka.kernel.common.util;

import dev.dong4j.zeka.kernel.common.asserts.Assertions;
import dev.dong4j.zeka.kernel.common.constant.ConfigDefaultValue;
import java.text.ParseException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalQuery;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.util.Assert;

/**
 * <p>日期工具类.
 * <p>提供丰富的日期时间操作功能，包括日期格式化、解析、计算、转换等.
 * <p>结合Java 8新的时间API和传统Date类，提供统一的日期处理接口.
 * <p>主要功能：
 * <ul>
 *     <li>日期格式化和解析（支持多种日期格式）</li>
 *     <li>日期计算（加减年、月、日、时、分、秒等）</li>
 *     <li>日期转换（Date与LocalDateTime、LocalDate等互转）</li>
 *     <li>日期比较和验证</li>
 *     <li>线程安全的日期格式化器</li>
 *     <li>日期常量定义</li>
 * </ul>
 * <p>使用示例：
 * <pre>
 * // 日期计算
 * Date now = new Date();
 * Date tomorrow = DateUtils.plusDays(now, 1); // 明天
 * Date nextMonth = DateUtils.plusMonths(now, 1); // 下个月
 *
 * // 日期格式化
 * String formatted = DateUtils.DATETIME_FORMAT.format(now); // "yyyy-MM-dd HH:mm:ss"
 * String dateOnly = DateUtils.DATE_FORMAT.format(now); // "yyyy-MM-dd"
 *
 * // 字符串转日期
 * String dateStr = "2023-01-01 12:00:00";
 * Date date = DateUtils.DATETIME_FORMAT.parse(dateStr);
 *
 * // Java 8时间API
 * LocalDateTime localDateTime = LocalDateTime.now();
 * String formatted8 = DateUtils.DATETIME_FORMATTER.format(localDateTime);
 * </pre>
 * <p>技术特性：
 * <ul>
 *     <li>结合Java 8新的时间API（LocalDateTime、LocalDate等）</li>
 *     <li>提供线程安全的日期格式化器（ConcurrentDateFormat）</li>
 *     <li>支持多种日期格式的常量定义</li>
 *     <li>提供日期计算和转换功能</li>
 *     <li>集成Spring的日期处理功能</li>
 * </ul>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2019.12.09 10:38
 * @since 1.0.0
 */
@UtilityClass
@SuppressWarnings("checkstyle:MethodLimit")
public class DateUtils {

    /** PATTERN_MS_DATETIME */
    public static final String PATTERN_MS_DATETIME = ConfigDefaultValue.DEFAULT_DATE_FORMAT.concat(":SSS");
    /** The constant PATTERN_DATETIME. */
    public static final String PATTERN_DATETIME = ConfigDefaultValue.DEFAULT_DATE_FORMAT;
    /** The constant PATTERN_DATE. */
    public static final String PATTERN_DATE = "yyyy-MM-dd";
    /** The constant PATTERN_DATE_HOUR. */
    public static final String PATTERN_DATE_HOUR = "yyyy-MM-dd HH";
    /** PATTERN_MONTH */
    public static final String PATTERN_MONTH = "yyyyMM";
    /** 时分秒 */
    public static final String PATTERN_TIME = "HH:mm:ss";
    /** PATTERN_DATE_NO_SEPARATOR */
    public static final String PATTERN_DATE_NO_SEPARATOR = "yyyyMMdd";
    /** PATTERN_TIME_NO_SEPARATOR */
    public static final String PATTERN_TIME_NO_SEPARATOR = "HHmmss";
    /** PATTERN_DATETIME_NO_SEPARATOR */
    public static final String PATTERN_DATETIME_NO_SEPARATOR = PATTERN_DATE_NO_SEPARATOR + PATTERN_TIME_NO_SEPARATOR;
    /** 老 date 格式化 */
    public static final ConcurrentDateFormat DATETIME_FORMAT = ConcurrentDateFormat.of(PATTERN_DATETIME);
    /** DATETIME_FORMAT_NO_SEPARATOR */
    public static final ConcurrentDateFormat DATETIME_FORMAT_NO_SEPARATOR = ConcurrentDateFormat.of(PATTERN_DATETIME_NO_SEPARATOR);
    /** The constant DATE_FORMAT. */
    public static final ConcurrentDateFormat DATE_FORMAT = ConcurrentDateFormat.of(PATTERN_DATE);
    /** DATE_FORMAT_NO_SEPARATOR */
    public static final ConcurrentDateFormat DATE_FORMAT_NO_SEPARATOR = ConcurrentDateFormat.of(PATTERN_DATE_NO_SEPARATOR);
    /** The constant TIME_FORMAT. */
    public static final ConcurrentDateFormat TIME_FORMAT = ConcurrentDateFormat.of(PATTERN_TIME);
    /** TIME_FORMAT_NO_SEPARATOR */
    public static final ConcurrentDateFormat TIME_FORMAT_NO_SEPARATOR = ConcurrentDateFormat.of(PATTERN_TIME_NO_SEPARATOR);
    /** java 8 时间格式化 */
    public static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern(DateUtils.PATTERN_DATETIME);
    /** DATETIME_FORMATTER_NO_SEPARATOR */
    public static final DateTimeFormatter DATETIME_FORMATTER_NO_SEPARATOR =
        DateTimeFormatter.ofPattern(DateUtils.PATTERN_DATETIME_NO_SEPARATOR);
    /** The constant DATE_FORMATTER. */
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DateUtils.PATTERN_DATE);
    /** DATE_FORMATTER_NO_SEPARATOR */
    public static final DateTimeFormatter DATE_FORMATTER_NO_SEPARATOR = DateTimeFormatter.ofPattern(DateUtils.PATTERN_DATE_NO_SEPARATOR);
    /** The constant TIME_FORMATTER. */
    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(DateUtils.PATTERN_TIME);
    /** TIME_FORMATTER_NO_SEPARATOR */
    public static final DateTimeFormatter TIME_FORMATTER_NO_SEPARATOR = DateTimeFormatter.ofPattern(DateUtils.PATTERN_TIME_NO_SEPARATOR);

    /**
     * 添加年
     *
     * @param date       时间
     * @param yearsToAdd 添加的年数
     * @return 设置后的时间 date
     * @since 1.0.0
     */
    @NotNull
    public static Date plusYears(Date date, int yearsToAdd) {
        return DateUtils.set(date, Calendar.YEAR, yearsToAdd);
    }

    /**
     * 设置日期属性
     *
     * @param date          时间
     * @param calendarField 更改的属性
     * @param amount        更改数,-1表示减少
     * @return 设置后的时间 date
     * @since 1.0.0
     */
    @NotNull
    private static Date set(Date date, int calendarField, int amount) {
        Assert.notNull(date, "The date must not be null");
        Calendar c = Calendar.getInstance();
        c.setLenient(false);
        c.setTime(date);
        c.add(calendarField, amount);
        return c.getTime();
    }

    /**
     * 添加月
     *
     * @param date        时间
     * @param monthsToAdd 添加的月数
     * @return 设置后的时间 date
     * @since 1.0.0
     */
    @NotNull
    public static Date plusMonths(Date date, int monthsToAdd) {
        return DateUtils.set(date, Calendar.MONTH, monthsToAdd);
    }

    /**
     * 添加周
     *
     * @param date       时间
     * @param weeksToAdd 添加的周数
     * @return 设置后的时间 date
     * @since 1.0.0
     */
    @NotNull
    public static Date plusWeeks(Date date, int weeksToAdd) {
        return DateUtils.plus(date, Period.ofWeeks(weeksToAdd));
    }

    /**
     * 日期添加时间量
     *
     * @param date   时间
     * @param amount 时间量
     * @return 设置后的时间 date
     * @since 1.0.0
     */
    @NotNull
    public static Date plus(@NotNull Date date, TemporalAmount amount) {
        Instant instant = date.toInstant();
        return Date.from(instant.plus(amount));
    }

    /**
     * 添加天
     *
     * @param date      时间
     * @param daysToAdd 添加的天数
     * @return 设置后的时间 date
     * @since 1.0.0
     */
    @NotNull
    public static Date plusDays(Date date, long daysToAdd) {
        return DateUtils.plus(date, Duration.ofDays(daysToAdd));
    }

    /**
     * 添加小时
     *
     * @param date       时间
     * @param hoursToAdd 添加的小时数
     * @return 设置后的时间 date
     * @since 1.0.0
     */
    @NotNull
    public static Date plusHours(Date date, long hoursToAdd) {
        return DateUtils.plus(date, Duration.ofHours(hoursToAdd));
    }

    /**
     * 添加分钟
     *
     * @param date         时间
     * @param minutesToAdd 添加的分钟数
     * @return 设置后的时间 date
     * @since 1.0.0
     */
    @NotNull
    public static Date plusMinutes(Date date, long minutesToAdd) {
        return DateUtils.plus(date, Duration.ofMinutes(minutesToAdd));
    }

    /**
     * 添加秒
     *
     * @param date         时间
     * @param secondsToAdd 添加的秒数
     * @return 设置后的时间 date
     * @since 1.0.0
     */
    @NotNull
    public static Date plusSeconds(Date date, long secondsToAdd) {
        return DateUtils.plus(date, Duration.ofSeconds(secondsToAdd));
    }

    /**
     * 添加毫秒
     *
     * @param date        时间
     * @param millisToAdd 添加的毫秒数
     * @return 设置后的时间 date
     * @since 1.0.0
     */
    @NotNull
    public static Date plusMillis(Date date, long millisToAdd) {
        return DateUtils.plus(date, Duration.ofMillis(millisToAdd));
    }

    /**
     * 添加纳秒
     *
     * @param date       时间
     * @param nanosToAdd 添加的纳秒数
     * @return 设置后的时间 date
     * @since 1.0.0
     */
    @NotNull
    public static Date plusNanos(Date date, long nanosToAdd) {
        return DateUtils.plus(date, Duration.ofNanos(nanosToAdd));
    }

    /**
     * 减少年
     *
     * @param date  时间
     * @param years 减少的年数
     * @return 设置后的时间 date
     * @since 1.0.0
     */
    @NotNull
    public static Date minusYears(Date date, int years) {
        return DateUtils.set(date, Calendar.YEAR, -years);
    }

    /**
     * 减少月
     *
     * @param date   时间
     * @param months 减少的月数
     * @return 设置后的时间 date
     * @since 1.0.0
     */
    @NotNull
    public static Date minusMonths(Date date, int months) {
        return DateUtils.set(date, Calendar.MONTH, -months);
    }

    /**
     * 减少周
     *
     * @param date  时间
     * @param weeks 减少的周数
     * @return 设置后的时间 date
     * @since 1.0.0
     */
    @NotNull
    public static Date minusWeeks(Date date, int weeks) {
        return DateUtils.minus(date, Period.ofWeeks(weeks));
    }

    /**
     * 日期减少时间量
     *
     * @param date   时间
     * @param amount 时间量
     * @return 设置后的时间 date
     * @since 1.0.0
     */
    @NotNull
    public static Date minus(@NotNull Date date, TemporalAmount amount) {
        Instant instant = date.toInstant();
        return Date.from(instant.minus(amount));
    }

    /**
     * 减少天
     *
     * @param date 时间
     * @param days 减少的天数
     * @return 设置后的时间 date
     * @since 1.0.0
     */
    @NotNull
    public static Date minusDays(Date date, long days) {
        return DateUtils.minus(date, Duration.ofDays(days));
    }

    /**
     * 减少小时
     *
     * @param date  时间
     * @param hours 减少的小时数
     * @return 设置后的时间 date
     * @since 1.0.0
     */
    @NotNull
    public static Date minusHours(Date date, long hours) {
        return DateUtils.minus(date, Duration.ofHours(hours));
    }

    /**
     * 减少分钟
     *
     * @param date    时间
     * @param minutes 减少的分钟数
     * @return 设置后的时间 date
     * @since 1.0.0
     */
    @NotNull
    public static Date minusMinutes(Date date, long minutes) {
        return DateUtils.minus(date, Duration.ofMinutes(minutes));
    }

    /**
     * 减少秒
     *
     * @param date    时间
     * @param seconds 减少的秒数
     * @return 设置后的时间 date
     * @since 1.0.0
     */
    @NotNull
    public static Date minusSeconds(Date date, long seconds) {
        return DateUtils.minus(date, Duration.ofSeconds(seconds));
    }

    /**
     * 减少毫秒
     *
     * @param date   时间
     * @param millis 减少的毫秒数
     * @return 设置后的时间 date
     * @since 1.0.0
     */
    @NotNull
    public static Date minusMillis(Date date, long millis) {
        return DateUtils.minus(date, Duration.ofMillis(millis));
    }

    /**
     * 减少纳秒
     *
     * @param date  时间
     * @param nanos 减少的纳秒数
     * @return 设置后的时间 date
     * @since 1.0.0
     */
    @NotNull
    public static Date minusNanos(Date date, long nanos) {
        return DateUtils.minus(date, Duration.ofNanos(nanos));
    }

    /**
     * 日期时间格式化
     *
     * @param date 时间
     * @return 格式化后的时间 string
     * @since 1.0.0
     */
    public static String formatDateTime(Date date) {
        return DATETIME_FORMAT.format(date);
    }

    /**
     * 日期格式化
     *
     * @param date 时间
     * @return 格式化后的时间 string
     * @since 1.0.0
     */
    public static String formatDate(Date date) {
        return DATE_FORMAT.format(date);
    }

    /**
     * 时间格式化
     *
     * @param date 时间
     * @return 格式化后的时间 string
     * @since 1.0.0
     */
    public static String formatTime(Date date) {
        return TIME_FORMAT.format(date);
    }

    /**
     * java8 日期时间格式化
     *
     * @param temporal 时间
     * @return 格式化后的时间 string
     * @since 1.0.0
     */
    @NotNull
    public static String formatDateTime(TemporalAccessor temporal) {
        return DATETIME_FORMATTER.format(temporal);
    }

    /**
     * java8 日期时间格式化
     *
     * @param temporal 时间
     * @return 格式化后的时间 string
     * @since 1.0.0
     */
    @NotNull
    public static String formatDate(TemporalAccessor temporal) {
        return DATE_FORMATTER.format(temporal);
    }

    /**
     * java8 时间格式化
     *
     * @param temporal 时间
     * @return 格式化后的时间 string
     * @since 1.0.0
     */
    @NotNull
    public static String formatTime(TemporalAccessor temporal) {
        return TIME_FORMATTER.format(temporal);
    }

    /**
     * java8 日期格式化
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
     * Parse date time
     *
     * @param dateStr date str
     * @return the date
     * @since 1.0.0
     */
    public static Date parseDateTime(String dateStr) {
        return parse(dateStr, PATTERN_DATETIME);
    }

    /**
     * 将字符串转换为时间
     *
     * @param dateStr 时间字符串
     * @param pattern 表达式
     * @return 时间 date
     * @since 1.0.0
     */
    @NotNull
    public static Date parse(String dateStr, String pattern) {
        Assertions.notBlank(dateStr, "参数错误, 时间字符串不能为空");
        ConcurrentDateFormat format = ConcurrentDateFormat.of(pattern);
        try {
            return format.parse(dateStr);
        } catch (ParseException e) {
            throw Exceptions.unchecked(StringUtils.format("不能将 {} 以 {} 格式转换为 Date 类型", dateStr, pattern), e);
        }
    }

    /**
     * 将字符串转换为时间
     *
     * @param dateStr 时间字符串
     * @param format  ConcurrentDateFormat
     * @return 时间 date
     * @since 1.0.0
     */
    public static Date parse(String dateStr, @NotNull ConcurrentDateFormat format) {
        try {
            return format.parse(dateStr);
        } catch (ParseException e) {
            throw Exceptions.unchecked(StringUtils.format("不能将 {} 以 {} 格式转换为 Date 类型", dateStr, format.getFormat()), e);
        }
    }

    /**
     * 将字符串转换为时间
     *
     * @param <T>     the type parameter
     * @param dateStr 时间字符串
     * @param pattern 表达式
     * @param query   the query
     * @return 时间 t
     * @since 1.0.0
     */
    public static <T> T parse(String dateStr, String pattern, TemporalQuery<T> query) {
        return DateTimeFormatter.ofPattern(pattern).parse(dateStr, query);
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

    /**
     * 转换成 date
     *
     * @param dateTime LocalDateTime
     * @return Date date
     * @since 1.0.0
     */
    @NotNull
    public static Date toDate(LocalDateTime dateTime) {
        return Date.from(DateUtils.toInstant(dateTime));
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
     * 转换成 date
     *
     * @param localDate LocalDate
     * @return Date date
     * @since 1.0.0
     */
    @NotNull
    public static Date toDate(@NotNull LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Converts local date time to Calendar.
     *
     * @param localDateTime the local date time
     * @return the calendar
     * @since 1.0.0
     */
    @NotNull
    public static Calendar toCalendar(LocalDateTime localDateTime) {
        return GregorianCalendar.from(ZonedDateTime.of(localDateTime, ZoneId.systemDefault()));
    }

    /**
     * localDate 转换成毫秒数
     *
     * @param localDate LocalDate
     * @return long long
     * @since 1.0.0
     */
    public static long toMilliseconds(@NotNull LocalDate localDate) {
        return toMilliseconds(localDate.atStartOfDay());
    }

    /**
     * localDateTime 转换成毫秒数
     *
     * @param localDateTime LocalDateTime
     * @return long long
     * @since 1.0.0
     */
    public static long toMilliseconds(@NotNull LocalDateTime localDateTime) {
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    /**
     * 转换成java8 时间
     *
     * @param calendar 日历
     * @return LocalDateTime local date time
     * @since 1.0.0
     */
    @NotNull
    public static LocalDateTime fromCalendar(@NotNull Calendar calendar) {
        TimeZone tz = calendar.getTimeZone();
        ZoneId zid = tz == null ? ZoneId.systemDefault() : tz.toZoneId();
        return LocalDateTime.ofInstant(calendar.toInstant(), zid);
    }

    /**
     * 转换成java8 时间
     *
     * @param instant Instant
     * @return LocalDateTime local date time
     * @since 1.0.0
     */
    @NotNull
    public static LocalDateTime fromInstant(Instant instant) {
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    /**
     * 转换成java8 时间
     *
     * @param date Date
     * @return LocalDateTime local date time
     * @since 1.0.0
     */
    @NotNull
    public static LocalDateTime fromDate(@NotNull Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    /**
     * 转换成java8 时间
     *
     * @param milliseconds 毫秒数
     * @return LocalDateTime local date time
     * @since 1.0.0
     */
    @NotNull
    public static LocalDateTime fromMilliseconds(long milliseconds) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(milliseconds), ZoneId.systemDefault());
    }

    /**
     * 比较2个时间差,跨度比较小
     *
     * @param startInclusive 开始时间
     * @param endExclusive   结束时间
     * @return 时间间隔 duration
     * @since 1.0.0
     */
    public static Duration between(Temporal startInclusive, Temporal endExclusive) {
        return Duration.between(startInclusive, endExclusive);
    }

    /**
     * 比较2个时间差,跨度比较大,年月日为单位
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return 时间间隔 period
     * @since 1.0.0
     */
    public static Period between(LocalDate startDate, LocalDate endDate) {
        return Period.between(startDate, endDate);
    }

    /**
     * 比较2个 时间差
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return 时间间隔 duration
     * @since 1.0.0
     */
    public static Duration between(@NotNull Date startDate, @NotNull Date endDate) {
        return Duration.between(startDate.toInstant(), endDate.toInstant());
    }

    /**
     * Between
     *
     * @param startTime  start time
     * @param targetDate target date
     * @param endTime    end time
     * @return the boolean
     * @since 1.0.0
     */
    public static boolean between(Date startTime, Date targetDate, Date endTime) {
        return startTime.getTime() <= targetDate.getTime()
            && endTime.getTime() >= targetDate.getTime();
    }

    /**
     * 将秒数转换为日时分秒
     *
     * @param second 秒数
     * @return 时间 string
     * @since 1.0.0
     */
    public static String secondToTime(Long second) {
        // 判断是否为空
        if (second == null || second == 0L) {
            return StringPool.EMPTY;
        }
        // 转换天数
        long days = second / 86400;
        // 剩余秒数
        second = second % 86400;
        // 转换小时
        long hours = second / 3600;
        // 剩余秒数
        second = second % 3600;
        // 转换分钟
        long minutes = second / 60;
        // 剩余秒数
        second = second % 60;
        if (days > 0) {
            return StringUtils.format("{}天{}小时{}分{}秒", days, hours, minutes, second);
        } else {
            return StringUtils.format("{}小时{}分{}秒", hours, minutes, second);
        }
    }

    /**
     * 将字符串格式的时间转换为 long
     *
     * @param dateString the date string
     * @return the long
     * @since 1.0.0
     */
    @NotNull
    @Contract(pure = true)
    public static Long timeToSecond(String dateString) {
        return parse(dateString, PATTERN_DATETIME).getTime();
    }

    /**
     * Time to millisecond
     *
     * @param dateString date string
     * @return the long
     * @since 1.0.0
     */
    @NotNull
    @Contract(pure = true)
    public static Long timeToMillisecond(String dateString) {
        return parse(dateString, PATTERN_MS_DATETIME).getTime();
    }

    /**
     * 获取今天的日期
     *
     * @return 时间 string
     * @since 1.0.0
     */
    public static String today() {
        return format(new Date(), PATTERN_DATE_NO_SEPARATOR);
    }

    /**
     * Now date
     *
     * @return the date
     * @since 1.0.0
     */
    @NotNull
    @Contract(" -> new")
    public static Date now() {
        return new Date();
    }

    /**
     * 日期格式化
     *
     * @param date    时间
     * @param pattern 表达式
     * @return 格式化后的时间 string
     * @since 1.0.0
     */
    public static String format(Date date, String pattern) {
        return ConcurrentDateFormat.of(pattern).format(date);
    }

    /**
     * 获取当前时间到当日最后时间相差的分钟数
     *
     * @param nowTime the now time
     * @return int int
     * @since 1.0.0
     */
    public static int diffMinutesTime(Date nowTime) {
        nowTime = null == nowTime ? now() : nowTime;

        Calendar todayLastTime = DateUtils.getCurrentCalendar();
        todayLastTime.set(Calendar.HOUR_OF_DAY, 23);
        todayLastTime.set(Calendar.MINUTE, 59);
        todayLastTime.set(Calendar.SECOND, 59);

        return (int) ((todayLastTime.getTimeInMillis() - nowTime.getTime()) / 1000 / 60);
    }

    /**
     * Diff minutes time int
     *
     * @return the int
     * @since 1.0.0
     */
    public static int diffMinutesTime() {
        return diffMinutesTime(now());
    }

    /**
     * Gets current calendar *
     *
     * @return the current calendar
     * @since 1.0.0
     */
    private static Calendar getCurrentCalendar() {
        Calendar calendar = Calendar.getInstance();
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Shanghai");
        calendar.setTimeZone(timeZone);
        return calendar;
    }

}
