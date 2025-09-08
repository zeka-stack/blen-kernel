package dev.dong4j.zeka.kernel.common.context;

import com.alibaba.ttl.TransmittableThreadLocal;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.Contract;

/**
 * <p>Description: ExpandIdsContext 用来存储 clientId、tenantId 相关信息 </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmaidl.com"
 * @date 2021.03.08 14:59
 * @since 1.0.0
 */
@UtilityClass
public class ExpandIdsContext {

    /** CONTEXT */
    private static final TransmittableThreadLocal<ExpandIds> CONTEXT = new TransmittableThreadLocal<>();


    /**
     * Context
     *
     * @return the transmittable thread local
     * @since 1.0.0
     */
    @Contract(pure = true)
    public static TransmittableThreadLocal<ExpandIds> context() {
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
