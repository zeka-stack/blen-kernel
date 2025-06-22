package dev.dong4j.zeka.kernel.common.util;

import lombok.experimental.UtilityClass;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Objects;

/**
 * <p>Description: 用于还未初始化日志系统时使用的日志记录器 </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.06.04 19:20
 * @since 1.4.0
 */
@UtilityClass
public final class LowLevelLogUtils {

    /** writer */
    private static PrintWriter writer = new PrintWriter(System.err, true);

    /**
     * Logs the given message.
     *
     * @param message the message to log
     * @since 1.4.0
     */
    public static void log(String message) {
        if (message != null) {
            writer.println(message);
        }
    }

    /**
     * Log exception
     *
     * @param exception exception
     * @since 1.5.0
     */
    @SuppressWarnings("checkstyle:Regexp")
    public static void logException(Throwable exception) {
        if (exception != null) {
            exception.printStackTrace(writer);
        }
    }

    /**
     * Log exception
     *
     * @param message   message
     * @param exception exception
     * @since 1.5.0
     */
    public static void logException(String message, Throwable exception) {
        log(message);
        logException(exception);
    }

    /**
     * Sets the underlying OutputStream where exceptions are printed to.
     *
     * @param out the OutputStream to log to
     * @since 1.5.0
     */
    public static void setOutputStream(OutputStream out) {
        LowLevelLogUtils.writer = new PrintWriter(Objects.requireNonNull(out), true);
    }

    /**
     * Sets the underlying Writer where exceptions are printed to.
     *
     * @param writer the Writer to log to
     * @since 1.5.0
     */
    public static void setWriter(Writer writer) {
        LowLevelLogUtils.writer = new PrintWriter(Objects.requireNonNull(writer), true);
    }

}
