package dev.dong4j.zeka.kernel.autoconfigure.task;

import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;

/**
 * <p>Description: 线程池内的线程间 MDC 参数传递 </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.11.21 22:49
 * @since 1.0.0
 */
public class MdcTaskDecorator implements TaskDecorator {
    /**
     * Decorate
     *
     * @param runnable runnable
     * @return the runnable
     * @since 1.0.0
     */
    @Override
    public @NotNull Runnable decorate(@NotNull Runnable runnable) {
        // 获取父线程 MDC 中的内容, 必须在 run 方法之前, 否则等异步线程执行的时候有可能 MDC 里面的值已经被清空了, 这个时候就会返回 null
        Map<String, String> context = MDC.getCopyOfContextMap();
        return () -> {
            // 将父线程的 MDC 内容传给子线程
            MDC.setContextMap(context);
            try {
                // 执行异步操作
                runnable.run();
            } finally {
                // 清空MDC内容
                MDC.clear();
            }
        };
    }
}
