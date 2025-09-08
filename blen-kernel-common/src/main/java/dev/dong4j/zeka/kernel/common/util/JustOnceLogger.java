package dev.dong4j.zeka.kernel.common.util;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>Description: 只会输出一次的日志 </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.04.13 01:14
 * @since 1.0.0
 */
@Slf4j
@UtilityClass
public class JustOnceLogger {

    /** KNOWN_LOGS */
    private static final Map<String, Set<String>> KNOWN_LOGS = new ConcurrentHashMap<>();

    /**
     * Info once
     *
     * @param loggerName logger name
     * @param message    message
     * @since 1.0.0
     */
    public static void infoOnce(String loggerName, String message) {
        if (!log.isInfoEnabled()) {
            return;
        }
        if (check(loggerName, message)) {
            return;
        }
        log.info(message);
    }

    /**
     * Warn once *
     *
     * @param loggerName logger name
     * @param message    message
     * @since 1.0.0
     */
    public static void warnOnce(String loggerName, String message) {
        if (!log.isWarnEnabled()) {
            return;
        }
        if (check(loggerName, message)) {
            return;
        }
        log.warn(message);
    }

    /**
     * Print once
     *
     * @param loggerName logger name
     * @param message    message
     * @since 1.0.0
     */
    public static void printOnce(String loggerName, String message) {
        if (check(loggerName, message)) {
            return;
        }
        System.err.println(message);
    }

    /**
     * Check
     *
     * @param loggerName logger name
     * @param message    message
     * @return the boolean
     * @since 1.0.0
     */
    private static boolean check(String loggerName, String message) {
        if (!KNOWN_LOGS.containsKey(loggerName)) {
            KNOWN_LOGS.put(loggerName, new ConcurrentSkipListSet<>(Collections.singleton(message)));
        } else {

            Set<String> messages = KNOWN_LOGS.get(loggerName);
            if (messages.contains(message)) {
                return true;
            }
            messages.add(message);
        }
        return false;
    }

}
