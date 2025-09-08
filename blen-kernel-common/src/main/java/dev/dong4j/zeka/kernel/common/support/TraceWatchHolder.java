package dev.dong4j.zeka.kernel.common.support;

import java.util.function.Supplier;
import org.springframework.util.StopWatch;

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2021.05.11 15:19
 * @since 1.0.0
 */
public class TraceWatchHolder {
    /**
     * 有返回值调用
     *
     * @param <T>        parameter
     * @param traceWatch trace watch
     * @param taskName   task name
     * @param supplier   supplier
     * @return the t
     * @since 1.0.0
     */
    public static <T> T run(StopWatch traceWatch, String taskName, Supplier<T> supplier) {
        try {
            traceWatch.start(taskName);
            return supplier.get();
        } finally {
            traceWatch.stop();
        }
    }

    /**
     * 无返回值调用
     *
     * @param traceWatch trace watch
     * @param taskName   task name
     * @param function   function
     * @since 1.0.0
     */
    public static void run(StopWatch traceWatch, String taskName, Runnable function) {
        try {
            traceWatch.start(taskName);
            function.run();
        } finally {
            traceWatch.stop();
        }
    }
}
