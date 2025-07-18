package dev.dong4j.zeka.kernel.common.util;

import lombok.Getter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Queue;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * <p>Description: 安全的时间格式化</p>
 * 参考 tomcat8 中的并发 DateFormat, {@link SimpleDateFormat}的线程安全包装器. 不使用ThreadLocal,创建足够的SimpleDateFormat对象来满足并发性要求.
 *
 * @author dong4j
 * @version 1.2.3
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
