package dev.dong4j.zeka.kernel.spi.utils;


import dev.dong4j.zeka.kernel.common.util.Jsons;
import dev.dong4j.zeka.kernel.spi.UnsafeStringWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;

import static dev.dong4j.zeka.kernel.spi.constants.CommonConstants.COMMA_SEPARATOR;
import static dev.dong4j.zeka.kernel.spi.constants.CommonConstants.COMMA_SPLIT_PATTERN;
import static dev.dong4j.zeka.kernel.spi.constants.CommonConstants.DOT_REGEX;
import static dev.dong4j.zeka.kernel.spi.constants.CommonConstants.GROUP_KEY;
import static dev.dong4j.zeka.kernel.spi.constants.CommonConstants.HIDE_KEY_PREFIX;
import static dev.dong4j.zeka.kernel.spi.constants.CommonConstants.INTERFACE_KEY;
import static dev.dong4j.zeka.kernel.spi.constants.CommonConstants.SEPARATOR_REGEX;
import static dev.dong4j.zeka.kernel.spi.constants.CommonConstants.UNDERLINE_SEPARATOR;
import static dev.dong4j.zeka.kernel.spi.constants.CommonConstants.VERSION_KEY;

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmaidl.com"
 * @date 2021.02.26 17:47
 * @since 1.0.0
 */
@Slf4j
@SuppressWarnings("all")
public final class SpiStringUtils {

    /** EMPTY */
    public static final String EMPTY = "";
    /** INDEX_NOT_FOUND */
    public static final int INDEX_NOT_FOUND = -1;
    /** EMPTY_STRING_ARRAY */
    public static final String[] EMPTY_STRING_ARRAY = new String[0];

    /** KVP_PATTERN */
    private static final Pattern KVP_PATTERN = Pattern.compile("([_.a-zA-Z0-9][-_.a-zA-Z0-9]*)[=](.*)"); // key value pair pattern.
    /** INT_PATTERN */
    private static final Pattern INT_PATTERN = Pattern.compile("^\\d+$");
    /** PAD_LIMIT */
    private static final int PAD_LIMIT = 8192;

    /**
     * String utils
     *
     * @since 1.0.0
     */
    private SpiStringUtils() {
    }

    /**
     * Length
     *
     * @param cs cs
     * @return the int
     * @since 1.0.0
     */
    public static int length(final CharSequence cs) {
        return cs == null ? 0 : cs.length();
    }

    /**
     * Repeat
     *
     * @param str    str
     * @param repeat repeat
     * @return the string
     * @since 1.0.0
     */
    public static String repeat(final String str, final int repeat) {
        // Performance tuned for 2.0 (JDK1.4)

        if (str == null) {
            return null;
        }
        if (repeat <= 0) {
            return EMPTY;
        }
        final int inputLength = str.length();
        if (repeat == 1 || inputLength == 0) {
            return str;
        }
        if (inputLength == 1 && repeat <= PAD_LIMIT) {
            return repeat(str.charAt(0), repeat);
        }

        final int outputLength = inputLength * repeat;
        switch (inputLength) {
            case 1:
                return repeat(str.charAt(0), repeat);
            case 2:
                final char ch0 = str.charAt(0);
                final char ch1 = str.charAt(1);
                final char[] output2 = new char[outputLength];
                for (int i = repeat * 2 - 2; i >= 0; i--, i--) {
                    output2[i] = ch0;
                    output2[i + 1] = ch1;
                }
                return new String(output2);
            default:
                final StringBuilder buf = new StringBuilder(outputLength);
                for (int i = 0; i < repeat; i++) {
                    buf.append(str);
                }
                return buf.toString();
        }
    }

    /**
     * Repeat
     *
     * @param str       str
     * @param separator separator
     * @param repeat    repeat
     * @return the string
     * @since 1.0.0
     */
    public static String repeat(final String str, final String separator, final int repeat) {
        if (str == null || separator == null) {
            return repeat(str, repeat);
        }
        // given that repeat(String, int) is quite optimized, better to rely on it than try and splice this into it
        final String result = repeat(str + separator, repeat);
        return removeEnd(result, separator);
    }

    /**
     * Remove end
     *
     * @param str    str
     * @param remove remove
     * @return the string
     * @since 1.0.0
     */
    public static String removeEnd(final String str, final String remove) {
        if (isAnyEmpty(str, remove)) {
            return str;
        }
        if (str.endsWith(remove)) {
            return str.substring(0, str.length() - remove.length());
        }
        return str;
    }

    /**
     * Repeat
     *
     * @param ch     ch
     * @param repeat repeat
     * @return the string
     * @since 1.0.0
     */
    public static String repeat(final char ch, final int repeat) {
        final char[] buf = new char[repeat];
        for (int i = repeat - 1; i >= 0; i--) {
            buf[i] = ch;
        }
        return new String(buf);
    }

    /**
     * Strip end
     *
     * @param str        str
     * @param stripChars strip chars
     * @return the string
     * @since 1.0.0
     */
    public static String stripEnd(final String str, final String stripChars) {
        int end;
        if (str == null || (end = str.length()) == 0) {
            return str;
        }

        if (stripChars == null) {
            while (end != 0 && Character.isWhitespace(str.charAt(end - 1))) {
                end--;
            }
        } else if (stripChars.isEmpty()) {
            return str;
        } else {
            while (end != 0 && stripChars.indexOf(str.charAt(end - 1)) != INDEX_NOT_FOUND) {
                end--;
            }
        }
        return str.substring(0, end);
    }

    /**
     * Replace
     *
     * @param text         text
     * @param searchString search string
     * @param replacement  replacement
     * @return the string
     * @since 1.0.0
     */
    public static String replace(final String text, final String searchString, final String replacement) {
        return replace(text, searchString, replacement, -1);
    }

    /**
     * Replace
     *
     * @param text         text
     * @param searchString search string
     * @param replacement  replacement
     * @param max          max
     * @return the string
     * @since 1.0.0
     */
    public static String replace(final String text, final String searchString, final String replacement, int max) {
        if (isAnyEmpty(text, searchString) || replacement == null || max == 0) {
            return text;
        }
        int start = 0;
        int end = text.indexOf(searchString, start);
        if (end == INDEX_NOT_FOUND) {
            return text;
        }
        final int replLength = searchString.length();
        int increase = replacement.length() - replLength;
        increase = increase < 0 ? 0 : increase;
        increase *= max < 0 ? 16 : max > 64 ? 64 : max;
        final StringBuilder buf = new StringBuilder(text.length() + increase);
        while (end != INDEX_NOT_FOUND) {
            buf.append(text, start, end).append(replacement);
            start = end + replLength;
            if (--max == 0) {
                break;
            }
            end = text.indexOf(searchString, start);
        }
        buf.append(text.substring(start));
        return buf.toString();
    }

    /**
     * Is blank
     *
     * @param cs cs
     * @return the boolean
     * @since 1.0.0
     */
    public static boolean isBlank(CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Is empty
     *
     * @param str str
     * @return the boolean
     * @since 1.0.0
     */
    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    /**
     * Is none empty
     *
     * @param ss ss
     * @return the boolean
     * @since 1.0.0
     */
    public static boolean isNoneEmpty(final String... ss) {
        if (ArrayUtils.isEmpty(ss)) {
            return false;
        }
        for (final String s : ss) {
            if (isEmpty(s)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Is any empty
     *
     * @param ss ss
     * @return the boolean
     * @since 1.0.0
     */
    public static boolean isAnyEmpty(final String... ss) {
        return !isNoneEmpty(ss);
    }

    /**
     * Is not empty
     *
     * @param str str
     * @return the boolean
     * @since 1.0.0
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * Is equals
     *
     * @param s1 s 1
     * @param s2 s 2
     * @return the boolean
     * @since 1.0.0
     */
    public static boolean isEquals(String s1, String s2) {
        if (s1 == null && s2 == null) {
            return true;
        }
        if (s1 == null || s2 == null) {
            return false;
        }
        return s1.equals(s2);
    }

    /**
     * Is integer
     *
     * @param str str
     * @return the boolean
     * @since 1.0.0
     */
    public static boolean isInteger(String str) {
        return isNotEmpty(str) && INT_PATTERN.matcher(str).matches();
    }

    /**
     * Parse integer
     *
     * @param str str
     * @return the int
     * @since 1.0.0
     */
    public static int parseInteger(String str) {
        return isInteger(str) ? Integer.parseInt(str) : 0;
    }

    /**
     * Is java identifier
     *
     * @param s s
     * @return the boolean
     * @since 1.0.0
     */
    public static boolean isJavaIdentifier(String s) {
        if (isEmpty(s) || !Character.isJavaIdentifierStart(s.charAt(0))) {
            return false;
        }
        for (int i = 1; i < s.length(); i++) {
            if (!Character.isJavaIdentifierPart(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Is contains
     *
     * @param values values
     * @param value  value
     * @return the boolean
     * @since 1.0.0
     */
    public static boolean isContains(String values, String value) {
        return isNotEmpty(values) && isContains(COMMA_SPLIT_PATTERN.split(values), value);
    }

    /**
     * Is contains
     *
     * @param values values
     * @param value  value
     * @return the boolean
     * @since 1.0.0
     */
    public static boolean isContains(String[] values, String value) {
        if (isNotEmpty(value) && ArrayUtils.isNotEmpty(values)) {
            for (String v : values) {
                if (value.equals(v)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Is numeric
     *
     * @param str      str
     * @param allowDot allow dot
     * @return the boolean
     * @since 1.0.0
     */
    public static boolean isNumeric(String str, boolean allowDot) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        boolean hasDot = false;
        int sz = str.length();
        for (int i = 0; i < sz; i++) {
            if (str.charAt(i) == '.') {
                if (hasDot || !allowDot) {
                    return false;
                }
                hasDot = true;
                continue;
            }
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }


    /**
     * To string
     *
     * @param e e
     * @return the string
     * @since 1.0.0
     */
    public static String toString(Throwable e) {
        UnsafeStringWriter w = new UnsafeStringWriter();
        PrintWriter p = new PrintWriter(w);
        p.print(e.getClass().getName());
        if (e.getMessage() != null) {
            p.print(": " + e.getMessage());
        }
        p.println();
        try {
            e.printStackTrace(p);
            return w.toString();
        } finally {
            p.close();
        }
    }

    /**
     * To string
     *
     * @param msg msg
     * @param e   e
     * @return the string
     * @since 1.0.0
     */
    public static String toString(String msg, Throwable e) {
        UnsafeStringWriter w = new UnsafeStringWriter();
        w.write(msg + "\n");
        PrintWriter p = new PrintWriter(w);
        try {
            e.printStackTrace(p);
            return w.toString();
        } finally {
            p.close();
        }
    }

    /**
     * Translate
     *
     * @param src  src
     * @param from from
     * @param to   to
     * @return the string
     * @since 1.0.0
     */
    public static String translate(String src, String from, String to) {
        if (isEmpty(src)) {
            return src;
        }
        StringBuilder sb = null;
        int ix;
        char c;
        for (int i = 0, len = src.length(); i < len; i++) {
            c = src.charAt(i);
            ix = from.indexOf(c);
            if (ix == -1) {
                if (sb != null) {
                    sb.append(c);
                }
            } else {
                if (sb == null) {
                    sb = new StringBuilder(len);
                    sb.append(src, 0, i);
                }
                if (ix < to.length()) {
                    sb.append(to.charAt(ix));
                }
            }
        }
        return sb == null ? src : sb.toString();
    }

    /**
     * Split
     *
     * @param str str
     * @param ch  ch
     * @return the string [ ]
     * @since 1.0.0
     */
    public static String[] split(String str, char ch) {
        List<String> list = null;
        char c;
        int ix = 0, len = str.length();
        for (int i = 0; i < len; i++) {
            c = str.charAt(i);
            if (c == ch) {
                if (list == null) {
                    list = new ArrayList<String>();
                }
                list.add(str.substring(ix, i));
                ix = i + 1;
            }
        }
        if (ix > 0) {
            list.add(str.substring(ix));
        }
        return list == null ? EMPTY_STRING_ARRAY : list.toArray(EMPTY_STRING_ARRAY);
    }

    /**
     * Join
     *
     * @param array array
     * @return the string
     * @since 1.0.0
     */
    public static String join(String[] array) {
        if (ArrayUtils.isEmpty(array)) {
            return EMPTY;
        }
        StringBuilder sb = new StringBuilder();
        for (String s : array) {
            sb.append(s);
        }
        return sb.toString();
    }

    /**
     * Join
     *
     * @param array array
     * @param split split
     * @return the string
     * @since 1.0.0
     */
    public static String join(String[] array, char split) {
        if (ArrayUtils.isEmpty(array)) {
            return EMPTY;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            if (i > 0) {
                sb.append(split);
            }
            sb.append(array[i]);
        }
        return sb.toString();
    }

    /**
     * Join
     *
     * @param array array
     * @param split split
     * @return the string
     * @since 1.0.0
     */
    public static String join(String[] array, String split) {
        if (ArrayUtils.isEmpty(array)) {
            return EMPTY;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            if (i > 0) {
                sb.append(split);
            }
            sb.append(array[i]);
        }
        return sb.toString();
    }

    /**
     * Join
     *
     * @param coll  coll
     * @param split split
     * @return the string
     * @since 1.0.0
     */
    public static String join(Collection<String> coll, String split) {
        if (CollectionUtils.isEmpty(coll)) {
            return EMPTY;
        }

        StringBuilder sb = new StringBuilder();
        boolean isFirst = true;
        for (String s : coll) {
            if (isFirst) {
                isFirst = false;
            } else {
                sb.append(split);
            }
            sb.append(s);
        }
        return sb.toString();
    }

    /**
     * Parse key value pair
     *
     * @param str           str
     * @param itemSeparator item separator
     * @return the map
     * @since 1.0.0
     */
    private static Map<String, String> parseKeyValuePair(String str, String itemSeparator) {
        String[] tmp = str.split(itemSeparator);
        Map<String, String> map = new HashMap<String, String>(tmp.length);
        for (int i = 0; i < tmp.length; i++) {
            Matcher matcher = KVP_PATTERN.matcher(tmp[i]);
            if (!matcher.matches()) {
                continue;
            }
            map.put(matcher.group(1), matcher.group(2));
        }
        return map;
    }

    /**
     * Gets query string value *
     *
     * @param qs  qs
     * @param key key
     * @return the query string value
     * @since 1.0.0
     */
    public static String getQueryStringValue(String qs, String key) {
        Map<String, String> map = SpiStringUtils.parseQueryString(qs);
        return map.get(key);
    }

    /**
     * Parse query string
     *
     * @param qs qs
     * @return the map
     * @since 1.0.0
     */
    public static Map<String, String> parseQueryString(String qs) {
        if (isEmpty(qs)) {
            return new HashMap<String, String>();
        }
        return parseKeyValuePair(qs, "\\&");
    }

    /**
     * Gets service key *
     *
     * @param ps ps
     * @return the service key
     * @since 1.0.0
     */
    public static String getServiceKey(Map<String, String> ps) {
        StringBuilder buf = new StringBuilder();
        String group = ps.get(GROUP_KEY);
        if (isNotEmpty(group)) {
            buf.append(group).append("/");
        }
        buf.append(ps.get(INTERFACE_KEY));
        String version = ps.get(VERSION_KEY);
        if (isNotEmpty(group)) {
            buf.append(":").append(version);
        }
        return buf.toString();
    }

    /**
     * To query string
     *
     * @param ps ps
     * @return the string
     * @since 1.0.0
     */
    public static String toQueryString(Map<String, String> ps) {
        StringBuilder buf = new StringBuilder();
        if (ps != null && ps.size() > 0) {
            for (Map.Entry<String, String> entry : new TreeMap<String, String>(ps).entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (isNoneEmpty(key, value)) {
                    if (buf.length() > 0) {
                        buf.append("&");
                    }
                    buf.append(key);
                    buf.append("=");
                    buf.append(value);
                }
            }
        }
        return buf.toString();
    }

    /**
     * Camel to split name
     *
     * @param camelName camel name
     * @param split     split
     * @return the string
     * @since 1.0.0
     */
    public static String camelToSplitName(String camelName, String split) {
        if (isEmpty(camelName)) {
            return camelName;
        }
        StringBuilder buf = null;
        for (int i = 0; i < camelName.length(); i++) {
            char ch = camelName.charAt(i);
            if (ch >= 'A' && ch <= 'Z') {
                if (buf == null) {
                    buf = new StringBuilder();
                    if (i > 0) {
                        buf.append(camelName, 0, i);
                    }
                }
                if (i > 0) {
                    buf.append(split);
                }
                buf.append(Character.toLowerCase(ch));
            } else if (buf != null) {
                buf.append(ch);
            }
        }
        return buf == null ? camelName : buf.toString();
    }

    /**
     * To argument string
     *
     * @param args args
     * @return the string
     * @since 1.0.0
     */
    public static String toArgumentString(Object[] args) {
        StringBuilder buf = new StringBuilder();
        for (Object arg : args) {
            if (buf.length() > 0) {
                buf.append(COMMA_SEPARATOR);
            }
            if (arg == null || SpiReflectUtils.isPrimitives(arg.getClass())) {
                buf.append(arg);
            } else {
                try {
                    buf.append(Jsons.toJson(arg));
                } catch (Exception e) {
                    log.warn(e.getMessage(), e);
                    buf.append(arg);
                }
            }
        }
        return buf.toString();
    }

    /**
     * Trim
     *
     * @param str str
     * @return the string
     * @since 1.0.0
     */
    public static String trim(String str) {
        return str == null ? null : str.trim();
    }

    /**
     * To url key
     *
     * @param key key
     * @return the string
     * @since 1.0.0
     */
    public static String toURLKey(String key) {
        return key.toLowerCase().replaceAll(SEPARATOR_REGEX, HIDE_KEY_PREFIX);
    }

    /**
     * To os style key
     *
     * @param key key
     * @return the string
     * @since 1.0.0
     */
    public static String toOSStyleKey(String key) {
        key = key.toUpperCase().replaceAll(DOT_REGEX, UNDERLINE_SEPARATOR);
        if (!key.startsWith("DUBBO_")) {
            key = "DUBBO_" + key;
        }
        return key;
    }

    /**
     * Is all upper case
     *
     * @param str str
     * @return the boolean
     * @since 1.0.0
     */
    public static boolean isAllUpperCase(String str) {
        if (str != null && !isEmpty(str)) {
            int sz = str.length();

            for (int i = 0; i < sz; ++i) {
                if (!Character.isUpperCase(str.charAt(i))) {
                    return false;
                }
            }

            return true;
        } else {
            return false;
        }
    }

    /**
     * Parse parameters
     *
     * @param rawParameters raw parameters
     * @return the map
     * @since 1.0.0
     */
    public static Map<String, String> parseParameters(String rawParameters) {
        Pattern pattern = Pattern.compile("^\\[((\\s*\\{\\s*[\\w_\\-\\.]+\\s*:\\s*.+?\\s*\\}\\s*,?\\s*)+)\\s*\\]$");
        Pattern pairPattern = Pattern.compile("^\\{\\s*([\\w-_\\.]+)\\s*:\\s*(.+)\\s*\\}$");

        Matcher matcher = pattern.matcher(rawParameters);
        if (!matcher.matches()) {
            return Collections.emptyMap();
        }

        String pairs = matcher.group(1);
        String[] pairArr = pairs.split("\\s*,\\s*");

        Map<String, String> parameters = new HashMap<>();
        for (String pair : pairArr) {
            Matcher pairMatcher = pairPattern.matcher(pair);
            if (pairMatcher.matches()) {
                parameters.put(pairMatcher.group(1), pairMatcher.group(2));
            }
        }
        return parameters;
    }
}
