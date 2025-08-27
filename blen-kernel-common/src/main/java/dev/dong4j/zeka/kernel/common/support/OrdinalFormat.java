package dev.dong4j.zeka.kernel.common.support;

import java.io.Serial;
import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.Format;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Locale;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.util.StringUtils;

/**
 * <p>Description: 占位符替换 </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.05.19 09:42
 * @since 1.4.0
 */
public class OrdinalFormat {
    /** LOCATION_EN */
    private static final String LOCATION_EN = "en";

    /**
     * Replaces all instances of {@code "{?,number,ordinal}"} format elements with the ordinal format for the locale.
     *
     * @param format format
     * @since 1.4.0
     */
    public static void apply(@NotNull MessageFormat format) {
        Format[] formats = format.getFormats();
        NumberFormat ordinal = null;
        for (int i = 0; i < formats.length; i++) {
            Format element = formats[i];
            if (element instanceof DecimalFormat && "ordinal".equals(((DecimalFormat) element).getPositivePrefix())) {
                if (ordinal == null) {
                    ordinal = getOrdinalFormat(format.getLocale());
                }
                format.setFormat(i, ordinal);
            }
        }
    }

    /**
     * Gets ordinal format *
     *
     * @param locale locale
     * @return the ordinal format
     * @since 1.4.0
     */
    @Contract("null -> new")
    private static @NotNull NumberFormat getOrdinalFormat(Locale locale) {
        if (locale != null) {
            String language = locale.getLanguage();
            if (LOCATION_EN.equals(language) || !StringUtils.hasText(language)) {
                return new EnglishOrdinalFormat();
            }
        }

        return new DecimalFormat();
    }

    /**
     * Format english
     *
     * @param num num
     * @return the string
     * @since 1.4.0
     */
    @Contract(pure = true)
    @SuppressWarnings("PMD.UndefineMagicConstantRule")
    public static @NotNull String formatEnglish(long num) {
        long mod = Math.abs(num) % 100;
        if (mod < 11 || mod > 13) {
            mod = mod % 10;
            if (mod == 1) {
                return num + "st";
            }
            if (mod == 2) {
                return num + "nd";
            }
            if (mod == 3) {
                return num + "rd";
            }
        }
        return num + "th";
    }

    /**
     * <p>Description: </p>
     *
     * @author dong4j
     * @version 1.0.0
     * @email "mailto:dong4j@gmail.com"
     * @date 2020.05.19 09:42
     * @since 1.4.0
     */
    private static class EnglishOrdinalFormat extends NumberFormat {
        /** serialVersionUID */
        @Serial
        private static final long serialVersionUID = 6915075795861536376L;

        /**
         * Format
         *
         * @param number     number
         * @param toAppendTo to append to
         * @param pos        pos
         * @return the string buffer
         * @since 1.4.0
         */
        @Override
        public StringBuffer format(double number, StringBuffer toAppendTo, FieldPosition pos) {
            throw new IllegalArgumentException("Cannot format non-integer number");
        }

        /**
         * Format
         *
         * @param number     number
         * @param toAppendTo to append to
         * @param pos        pos
         * @return the string buffer
         * @since 1.4.0
         */
        @Override
        public StringBuffer format(long number, StringBuffer toAppendTo, FieldPosition pos) {
            return new MessageFormat("{0}").format(new Object[]{formatEnglish(number)}, toAppendTo, pos);
        }

        /**
         * Parse
         *
         * @param source        source
         * @param parsePosition parse position
         * @return the number
         * @since 1.4.0
         */
        @Override
        public Number parse(String source, ParsePosition parsePosition) {
            throw new UnsupportedOperationException();
        }
    }
}
