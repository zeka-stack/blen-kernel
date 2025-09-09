package dev.dong4j.zeka.kernel.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Queue;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentLinkedQueue;
import lombok.Getter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * <p>安全的时间格式化.
 * <p>参考tomcat8中的并发DateFormat，{@link SimpleDateFormat}的线程安全包装器.
 * <p>不使用ThreadLocal，创建足够的SimpleDateFormat对象来满足并发性要求.
 * <p>主要功能：
 * <ul>
 *     <li>线程安全的日期格式化和解析</li>
 *     <li>避免SimpleDateFormat的线程安全问题</li>
 *     <li>高性能的日期处理能力</li>
 *     <li>支持自定义格式、时区和语言环境</li>
 * </ul>
 * <p>使用示例：
 * <pre>
 * // 创建线程安全的日期格式化器
 * ConcurrentDateFormat formatter = ConcurrentDateFormat.of("yyyy-MM-dd HH:mm:ss");
 *
 * // 格式化日期
 * Date date = new Date();
 * String formatted = formatter.format(date);
 *
 * // 解析日期字符串
 * String dateStr = "2023-01-01 12:00:00";
 * Date parsed = formatter.parse(dateStr);
 *
 * // 使用特定时区和语言环境
 * TimeZone timeZone = TimeZone.getTimeZone("UTC");
 * Locale locale = Locale.CHINA;
 * ConcurrentDateFormat formatter2 = ConcurrentDateFormat.of("yyyy-MM-dd HH:mm:ss", locale, timeZone);
 * </pre>
 * <p>技术特性：
 * <ul>
 *     <li>基于队列的对象池模式，避免频繁创建SimpleDateFormat实例</li>
 *     <li>使用ConcurrentLinkedQueue实现线程安全的对象池</li>
 *     <li>支持自定义日期格式、时区和语言环境</li>
 *     <li>高性能，适用于高并发场景</li>
 *     <li>自动回收和复用SimpleDateFormat实例</li>
 * </ul>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.27 18:18
 * @since 1.0.0
 */
public final class ConcurrentDateFormat {
    /** Format */
    @Getter
    private final String format;
    /** Locale */
    private final Locale locale;
    /** Timezone */
    private final TimeZone timezone;
    /** Queue */
    private final Queue<SimpleDateFormat> queue = new ConcurrentLinkedQueue<>();

    /**
     * Concurrent date format
     *
     * @param format   format
     * @param locale   locale
     * @param timezone timezone
     * @since 1.0.0
     */
    private ConcurrentDateFormat(String format, Locale locale, TimeZone timezone) {
        this.format = format;
        this.locale = locale;
        this.timezone = timezone;
        SimpleDateFormat initial = this.createInstance();
        this.queue.add(initial);
    }

    /**
     * Create instance simple date format
     *
     * @return the simple date format
     * @since 1.0.0
     */
    @NotNull
    private SimpleDateFormat createInstance() {
        SimpleDateFormat sdf = new SimpleDateFormat(this.format, this.locale);
        sdf.setTimeZone(this.timezone);
        return sdf;
    }

    /**
     * Of concurrent date format.
     *
     * @param format the format
     * @return the concurrent date format
     * @since 1.0.0
     */
    @NotNull
    @Contract("_ -> new")
    public static ConcurrentDateFormat of(String format) {
        return new ConcurrentDateFormat(format, Locale.getDefault(), TimeZone.getDefault());
    }

    /**
     * Of concurrent date format.
     *
     * @param format   the format
     * @param timezone the timezone
     * @return the concurrent date format
     * @since 1.0.0
     */
    @NotNull
    @Contract("_, _ -> new")
    public static ConcurrentDateFormat of(String format, TimeZone timezone) {
        return new ConcurrentDateFormat(format, Locale.getDefault(), timezone);
    }

    /**
     * Of concurrent date format
     *
     * @param format   format
     * @param locale   locale
     * @param timezone timezone
     * @return the concurrent date format
     * @since 1.0.0
     */
    @NotNull
    @Contract("_, _, _ -> new")
    public static ConcurrentDateFormat of(String format, Locale locale, TimeZone timezone) {
        return new ConcurrentDateFormat(format, locale, timezone);
    }

    /**
     * Format string
     *
     * @param date date
     * @return the string
     * @since 1.0.0
     */
    public @NotNull String format(Date date) {
        SimpleDateFormat sdf = this.queue.poll();
        if (sdf == null) {
            sdf = this.createInstance();
        }
        String result = sdf.format(date);
        this.queue.add(sdf);
        return result;
    }

    /**
     * Parse date
     *
     * @param source source
     * @return the date
     * @throws ParseException parse exception
     * @since 1.0.0
     */
    public Date parse(String source) throws ParseException {
        SimpleDateFormat sdf = this.queue.poll();
        if (sdf == null) {
            sdf = this.createInstance();
        }
        Date result = sdf.parse(source);
        this.queue.add(sdf);
        return result;
    }
}
