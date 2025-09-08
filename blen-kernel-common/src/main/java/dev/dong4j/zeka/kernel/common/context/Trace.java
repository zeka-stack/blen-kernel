package dev.dong4j.zeka.kernel.common.context;

import com.alibaba.ttl.TransmittableThreadLocal;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.Contract;

/**
 * <p>Description: TraceId 用来存储traceID相关信息 </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.06.23 23:54
 * @since 1.0.0
 */
@UtilityClass
public class Trace {
    /** context */
    private static final TransmittableThreadLocal<String> CONTEXT = new TransmittableThreadLocal<>();

    /**
     * Context
     *
     * @return the thread context map
     * @since 1.0.0
     */
    @Contract(pure = true)
    public static TransmittableThreadLocal<String> context() {
        return CONTEXT;
    }

    /**
     * Clear
     *
     * @since 1.0.0
     */
    @Contract(pure = true)
    public static void clear() {
        CONTEXT.remove();
    }

}
