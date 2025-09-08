package dev.dong4j.zeka.kernel.common.util;

import dev.dong4j.zeka.kernel.common.exception.TimeoutUtilsExecuteException;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Contract;

/**
 * <p>Description: 简单超时工具类</p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.07.02 23:10
 * @since 1.0.0
 */
@Slf4j
@UtilityClass
@SuppressWarnings("PMD.ThreadPoolCreationRule")
public final class TimeoutUtils {
    /** executor */
    private static final ExecutorService TIMEOUT_EXECUTOR = Executors.newSingleThreadExecutor();

    /**
     * Process
     *
     * @param <T>     parameter
     * @param task    task
     * @param timeout timeout
     * @return the t
     * @throws TimeoutUtilsExecuteException timeout utils execute exception
     * @since 1.0.0
     */
    @Contract("null, _ -> null")
    public static <T> T process(Callable<T> task, long timeout) throws Exception {
        return process(task, timeout, TimeUnit.SECONDS);
    }

    /**
     * Process
     *
     * @param <T>     parameter
     * @param task    task
     * @param timeout timeout
     * @param unit    unit
     * @return the t
     * @throws TimeoutUtilsExecuteException timeout utils execute exception
     * @since 1.0.0
     */
    @Contract("null, _, _ -> null")
    public static <T> T process(Callable<T> task, long timeout, TimeUnit unit) throws Exception {
        return process(TIMEOUT_EXECUTOR, task, timeout, unit);
    }

    /**
     * Process
     * todo-dong4j : (2021.09.18 16:17) [executorService.submit(TtlCallable.get(task))]
     *
     * @param <T>             parameter
     * @param executorService executor service
     * @param task            task
     * @param timeout         timeout
     * @param unit            unit
     * @return the t
     * @throws TimeoutUtilsExecuteException timeout utils execute exception
     * @since 1.0.0
     */
    @Contract("_, null, _, _ -> null")
    public static <T> T process(ExecutorService executorService,
                                Callable<T> task,
                                long timeout,
                                TimeUnit unit) throws Exception {
        if (task == null) {
            return null;
        }
        Future<T> futureRet = executorService.submit(task);
        try {
            return futureRet.get(timeout, unit);
        } catch (InterruptedException e) {
            log.error("任务被中断: {}", e.getMessage());
            throw e;
        } catch (ExecutionException e) {
            // 任务抛出异常时将被包装为 ExecutionException
            Throwable cause = e.getCause();
            if (cause instanceof InvocationTargetException) {
                // 如果通过反射调用将会抛出 InvocationTargetException, 这里抛出原始异常
                throw (InvocationTargetException) cause;
            }
            // 否则抛出 ExecutionException, 由业务端处理
            throw e;
        } catch (TimeoutException e) {
            if (!futureRet.isCancelled()) {
                futureRet.cancel(true);
            }
        }

        throw new TimeoutUtilsExecuteException("process timeout: [" + timeout + " " + unit.name().toLowerCase() + "]");
    }
}
