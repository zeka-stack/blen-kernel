package dev.dong4j.zeka.kernel.common.config;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

/**
 * <p>Description:  </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.06.04 19:21
 * @since 1.4.0
 */
public interface PropertySource {

    /**
     * 返回此属性源具有优先级的顺序. 较高的值意味着稍后将应用源, 以便优先于其他属性源.
     *
     * @return priority value
     * @since 1.5.0
     */
    int getPriority();

    /**
     * 遍历所有属性并对每个键/值对执行操作.
     *
     * @param action action to perform on each key/value pair
     * @since 1.5.0
     */
    void forEach(BiConsumer<String, String> action);

    /**
     * 将属性名称标记列表转换为普通形式.
     *
     * @param tokens list of property name tokens
     * @return a normalized property name using the given tokens
     * @since 1.5.0
     */
    CharSequence getNormalForm(Iterable<? extends CharSequence> tokens);

    /**
     * 用于按优先级对属性源实例排序的比较器.
     *
     * @author dong4j
     * @version 1.0.0
     * @email "mailto:dong4j@gmail.com"
     * @date 2020.06.04 19:21
     * @since 1.4.0
     */
    class Comparator implements java.util.Comparator<PropertySource>, Serializable {
        /** serialVersionUID */
        @Serial
        private static final long serialVersionUID = 1L;

        /**
         * Compare
         *
         * @param o1 o 1
         * @param o2 o 2
         * @return the int
         * @since 1.5.0
         */
        @Override
        public int compare(PropertySource o1, PropertySource o2) {
            return Integer.compare(Objects.requireNonNull(o1).getPriority(), Objects.requireNonNull(o2).getPriority());
        }
    }

    /**
     * Utility methods useful for PropertySource implementations.
     *
     * @author dong4j
     * @version 1.0.0
     * @email "mailto:dong4j@gmail.com"
     * @date 2020.06.04 19:21
     * @since 1.4.0
     */
    final class Util {
        /** PREFIXES */
        private static final String PREFIXES = "(?i:^log4j2?[-._/]?|^org\\.apache\\.logging\\.log4j\\.)?";
        /** PROPERTY_TOKENIZER */
        private static final Pattern PROPERTY_TOKENIZER = Pattern.compile(PREFIXES + "([A-Z]*[a-z0-9]+|[A-Z0-9]+)[-._/]?");
        /** CACHE */
        private static final Map<CharSequence, List<CharSequence>> CACHE = new ConcurrentHashMap<>();

        /**
         * Converts a property name string into a list of tokens. This will strip a prefix of {@code log4j},
         * {@code log4j2}, {@code Log4j}, or {@code org.apache.logging.log4j}, along with separators of
         * dash {@code -}, dot {@code .}, underscore {@code _}, and slash {@code /}. Tokens can also be separated
         * by camel case conventions without needing a separator character in between.
         *
         * @param value property name
         * @return the property broken into lower case tokens
         * @since 1.5.0
         */
        public static List<CharSequence> tokenize(CharSequence value) {
            if (CACHE.containsKey(value)) {
                return CACHE.get(value);
            }
            List<CharSequence> tokens = new ArrayList<>();
            Matcher matcher = PROPERTY_TOKENIZER.matcher(value);
            while (matcher.find()) {
                tokens.add(matcher.group(1).toLowerCase());
            }
            CACHE.put(value, tokens);
            return tokens;
        }

        /**
         * Joins a list of strings using camelCaseConventions.
         *
         * @param tokens tokens to convert
         * @return tokensAsCamelCase char sequence
         * @since 1.5.0
         */
        public static @NotNull @Unmodifiable CharSequence joinAsCamelCase(@NotNull Iterable<? extends CharSequence> tokens) {
            StringBuilder sb = new StringBuilder();
            boolean first = true;
            for (CharSequence token : tokens) {
                if (first) {
                    sb.append(token);
                } else {
                    sb.append(Character.toUpperCase(token.charAt(0)));
                    if (token.length() > 1) {
                        sb.append(token.subSequence(1, token.length()));
                    }
                }
                first = false;
            }
            return sb.toString();
        }

        /**
         * Util
         *
         * @since 1.5.0
         */
        @Contract(pure = true)
        private Util() {
        }
    }
}
