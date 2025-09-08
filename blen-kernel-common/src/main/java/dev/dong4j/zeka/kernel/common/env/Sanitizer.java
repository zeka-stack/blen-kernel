package dev.dong4j.zeka.kernel.common.env;

import java.util.regex.Pattern;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.util.Assert;

/**
 * <p>Description: 安全过滤 </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.26 20:40
 * @since 1.0.0
 */
class Sanitizer {

    /** REGEX_PARTS */
    private static final String[] REGEX_PARTS = {"*", "$", "^", "+"};

    /** Keys to sanitize */
    private Pattern[] keysToSanitize;

    /**
     * Instantiates a new Sanitizer.
     *
     * @since 1.0.0
     */
    Sanitizer() {
        this("password", "secret", "key", "token", ".*credentials.*", "vcap_services", "sun.java.command");
    }

    /**
     * Sanitizer
     *
     * @param keysToSanitize keys to sanitize
     * @since 1.0.0
     */
    private Sanitizer(String... keysToSanitize) {
        setKeysToSanitize(keysToSanitize);
    }

    /**
     * Keys that should be sanitized. Keys can be simple strings that the property ends
     * with or regular expressions.
     *
     * @param keysToSanitize the keys to sanitize
     * @since 1.0.0
     */
    void setKeysToSanitize(String... keysToSanitize) {
        Assert.notNull(keysToSanitize, "KeysToSanitize must not be null");
        this.keysToSanitize = new Pattern[keysToSanitize.length];
        for (int i = 0; i < keysToSanitize.length; i++) {
            this.keysToSanitize[i] = getPattern(keysToSanitize[i]);
        }
    }

    /**
     * Gets pattern *
     *
     * @param value value
     * @return the pattern
     * @since 1.0.0
     */
    @NotNull
    private Pattern getPattern(String value) {
        if (isRegex(value)) {
            return Pattern.compile(value, Pattern.CASE_INSENSITIVE);
        }
        return Pattern.compile(".*" + value + "$", Pattern.CASE_INSENSITIVE);
    }

    /**
     * Is regex boolean
     *
     * @param value value
     * @return the boolean
     * @since 1.0.0
     */
    @Contract(pure = true)
    private boolean isRegex(String value) {
        for (String part : REGEX_PARTS) {
            if (value.contains(part)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Sanitize the given value if necessary.
     *
     * @param key   the key to sanitize
     * @param value the value
     * @return the potentially sanitized value
     * @since 1.0.0
     */
    Object sanitize(String key, Object value) {
        if (value == null) {
            return null;
        }
        for (Pattern pattern : keysToSanitize) {
            if (pattern.matcher(key).matches()) {
                return "******";
            }
        }
        return value;
    }

}
