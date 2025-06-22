package dev.dong4j.zeka.kernel.common.context;

import com.alibaba.ttl.TransmittableThreadLocal;
import lombok.experimental.UtilityClass;
import org.springframework.lang.Nullable;
import org.springframework.web.context.request.RequestAttributes;

/**
 * <p>Description: 处理 agent 服务层在多线程的情况下正确获取 HttpServletRequest 的问题 </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2021.03.11 11:39
 * @since 1.8.0
 */
@UtilityClass
public class AgentRequestContextHolder {

    /** inheritableRequestAttributesHolder */
    private static final TransmittableThreadLocal<RequestAttributes> INHERITABLE_REQUEST_ATTRIBUTES_HOLDER
        = new TransmittableThreadLocal<>();

    /**
     * Reset the RequestAttributes for the current thread.
     *
     * @since 1.8.0
     */
    public static void resetRequestAttributes() {
        INHERITABLE_REQUEST_ATTRIBUTES_HOLDER.remove();
    }

    /**
     * Bind the given RequestAttributes to the current thread,
     * <i>not</i> exposing it as inheritable for child threads.
     *
     * @param attributes the RequestAttributes to expose
     * @since 1.8.0
     */
    public static void setRequestAttributes(@Nullable RequestAttributes attributes) {
        if (attributes == null) {
            resetRequestAttributes();
        } else {
            INHERITABLE_REQUEST_ATTRIBUTES_HOLDER.set(attributes);
        }
    }

    /**
     * Return the RequestAttributes currently bound to the thread.
     *
     * @return the RequestAttributes currently bound to the thread, or {@code null} if none bound
     * @since 1.8.0
     */
    @Nullable
    public static RequestAttributes getRequestAttributes() {
        return INHERITABLE_REQUEST_ATTRIBUTES_HOLDER.get();
    }


    /**
     * Current request attributes
     *
     * @return the request attributes
     * @throws IllegalStateException illegal state exception
     * @since 1.8.0
     */
    public static RequestAttributes currentRequestAttributes() throws IllegalStateException {
        RequestAttributes attributes = getRequestAttributes();
        if (attributes == null) {
            throw new IllegalStateException("当前线程不存在 Request 对象");
        }
        return attributes;
    }

}
