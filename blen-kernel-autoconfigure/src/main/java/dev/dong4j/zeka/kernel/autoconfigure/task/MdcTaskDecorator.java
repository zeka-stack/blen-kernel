package dev.dong4j.zeka.kernel.autoconfigure.task;

import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;

/**
 * <p>MDC 上下文任务装饰器.
 * <p>实现 Spring TaskDecorator 接口，用于在线程池中传递 MDC 日志上下文.
 * <p>解决在线程池环境中，MDC 上下文丢失的问题，确保异步任务能够继承父线程的日志上下文.
 * <p>实现原理：
 * <ul>
 *     <li>在任务提交时，获取当前线程的 MDC 上下文</li>
 *     <li>将 MDC 上下文传递给子线程</li>
 *     <li>在子线程执行完成后，清理 MDC 上下文</li>
 * </ul>
 * <p>适用场景：异步任务、线程池任务、需要保持请求链路追踪的场景.
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.11.21 22:49
 * @since 1.0.0
 */
public class MdcTaskDecorator implements TaskDecorator {
    /**
     * <p>任务装饰方法.
     * <p>对原始任务进行包装，添加 MDC 上下文传递逻辑.
     *
     * @param runnable 原始任务
     * @return 包装后的任务，包含 MDC 上下文传递逻辑
     * @since 1.0.0
     */
    @Override
    public @NotNull Runnable decorate(@NotNull Runnable runnable) {
        // 获取父线程 MDC 中的内容，必须在 run 方法之前获取
        // 避免在异步线程执行时 MDC 中的值被清空，导致返回 null
        Map<String, String> context = MDC.getCopyOfContextMap();
        return () -> {
            // 将父线程的 MDC 内容传给子线程
            MDC.setContextMap(context);
            try {
                // 执行异步操作
                runnable.run();
            } finally {
                // 清空 MDC 内容，避免内存泄漏
                MDC.clear();
            }
        };
    }
}
