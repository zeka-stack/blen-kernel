package dev.dong4j.zeka.kernel.common.util;

import com.google.common.collect.Maps;
import dev.dong4j.zeka.kernel.common.exception.LowestException;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

/**
 * <p>Description: 限制方法执行次数 </p>
 *
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.27 14:54
 * @since 1.0.0
 */
@Slf4j
@UtilityClass
public class MethodLimit {
    /** LOCK */
    private static final Object LOCK = new Object();
    /** LIMIT_COUNT */
    private static final Map<String, AtomicInteger> LIMIT_COUNT = Maps.newConcurrentMap();

    /**
     * 限制执行
     *
     * @param clz          the clz              执行方法的类名
     * @param executeCount the execute count    执行次数
     * @return the boolean
     * @since 1.0.0
     */
    public static boolean execute(@NotNull Class<?> clz, int executeCount) {
        synchronized (LOCK) {
            if (executeCount < 0) {
                throw new LowestException("执行次数不能小于 0");
            }

            AtomicInteger executedCount = LIMIT_COUNT.get(clz.getName());

            if (executedCount == null) {
                LIMIT_COUNT.put(clz.getName(), new AtomicInteger(0));
                return true;
            } else if (executedCount.get() < executeCount) {
                log.info("execute: {}", clz.getName());
                executedCount.getAndIncrement();
                LIMIT_COUNT.put(clz.getName(), executedCount);
                return true;
            }
            return false;
        }
    }
}
