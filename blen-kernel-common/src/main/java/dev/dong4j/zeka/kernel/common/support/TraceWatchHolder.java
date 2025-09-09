package dev.dong4j.zeka.kernel.common.support;

import java.util.function.Supplier;
import org.springframework.util.StopWatch;

/**
 * <p>性能监控计时器的工具类.
 * <p>提供静态方法来简化 StopWatch 的使用，封装了常见的计时场景和错误处理.
 * <p>自动处理计时器的启动和停止，确保在异常情况下也能正确结束计时.
 * <p>主要功能：
 * <ul>
 *     <li>支持有返回值和无返回值的方法计时</li>
 *     <li>自动异常处理和资源清理</li>
 *     <li>简化的 API 设计，降低使用门槛</li>
 *     <li>线程安全的计时操作</li>
 * </ul>
 * <p>使用优势：
 * <ul>
 *     <li>避免手动管理计时器的生命周期</li>
 *     <li>统一的异常处理机制</li>
 *     <li>支持函数式编程风格</li>
 *     <li>代码更加简洁和易读</li>
 * </ul>
 * <p>使用场景：
 * <ul>
 *     <li>单个方法或代码块的性能监控</li>
 *     <li>业务方法的执行时间统计</li>
 *     <li>简单的性能测试和分析</li>
 *     <li>生产环境的性能监控</li>
 * </ul>
 * <p>使用示例：
 * <pre>{@code
 * StopWatch watch = new StopWatch();
 *
 * // 有返回值的方法
 * String result = TraceWatchHolder.run(watch, "calculate", () -> {
 *     return complexCalculation();
 * });
 *
 * // 无返回值的方法
 * TraceWatchHolder.run(watch, "process", () -> {
 *     processData();
 * });
 * }</pre>
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
