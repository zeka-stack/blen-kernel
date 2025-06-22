package dev.dong4j.zeka.kernel.common.context;

import com.alibaba.ttl.TransmittableThreadLocal;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.Contract;

import java.util.Map;

/**
 * <p>Description:  </p>
 * todo-dong4j : (2021.10.31 17:24) [重构, 只用来保存 agent service]
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2021.01.20 15:53
 * @since 1.7.1
 */
@UtilityClass
public class ComponentThreadLocal {
    /** AGENT_SERVICES */
    public static final String AGENT_SERVICES = "agentService";
    /** AGENT_API */
    public static final String AGENT_API = "api.value";
    /** AGENT_CODE */
    public static final String AGENT_CODE = "api.code";

    /** context */
    private static final TransmittableThreadLocal<Map<String, Object>> CONTEXT = new TransmittableThreadLocal<>();

    /**
     * Context
     *
     * @return the thread context map
     * @since 1.7.1
     */
    @Contract(pure = true)
    public static TransmittableThreadLocal<Map<String, Object>> context() {
        return CONTEXT;
    }

    /**
     * Clear
     *
     * @since 1.7.1
     */
    @Contract(pure = true)
    public static void clear() {
        CONTEXT.remove();
    }

}
