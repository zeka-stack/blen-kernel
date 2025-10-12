package dev.dong4j.zeka.kernel.common.context;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Data;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * <p>Description: 扩展出来的tenantId clientId </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmaidl.com"
 * @date 2021.03.08 15:04
 * @since 1.0.0
 */
public class ExpandIds implements Serializable {

    /** serialVersionUID */
    @Serial
    private static final long serialVersionUID = -1449027854558101307L;

    /** EMPTY_ATTRIBUTION */
    public static final InnerAttribution<?> EMPTY_ATTRIBUTION = new InnerAttribution<>();

    /** USER_ID */
    public static final InnerAttribution<Long> USER_ID = new InnerAttribution<>();

    /** DEPT_ID */
    public static final InnerAttribution<Long> DEPT_ID = new InnerAttribution<>();

    /**
     * 如果 header 中存在 AgentConstant#X_AGENT_TENANTID, 则会在接收到请求后自动写入到此字段中.
     *
     * @since 2024.1.1
     */
    @Nullable
    private Long tenantId;

    /**
     * 如果 header 中存在 AgentConstant#X_AGENT_APPID, 则会在接收到请求后自动写入到此字段中.
     *
     * @since 2024.1.1
     */
    @Nullable
    private String clientId;

    /** Attributions */
    private final Map<InnerAttribution<?>, InnerAttribution<?>> attributions = new ConcurrentHashMap<>();

    /**
     * Get tenant id
     *
     * @return the long
     * @since 2024.1.1
     */
    public Optional<Long> getTenantId() {
        return Optional.ofNullable(this.tenantId);
    }

    /**
     * Get client id
     *
     * @return the string
     * @since 2024.1.1
     */
    public Optional<String> getClientId() {
        return Optional.ofNullable(this.clientId);
    }

    /**
     * Sets tenant id *
     *
     * @param tenantId tenant id
     * @since 2024.1.1
     */
    public void setTenantId(@NotNull Long tenantId) {
        this.tenantId = tenantId;
    }

    /**
     * Sets client id *
     *
     * @param clientId client id
     * @since 2024.1.1
     */
    public void setClientId(@NotNull String clientId) {
        this.clientId = clientId;
    }

    /**
     * Gets attribution *
     *
     * @param <T> parameter
     * @param key key
     * @since 2024.2.0
     */
    @SuppressWarnings("all")
    public <T> T getAttribution(InnerAttribution<T> key) {
        InnerAttribution<T> attribution = (InnerAttribution<T>) this.attributions.get(key);
        return Optional.ofNullable(attribution).orElseGet(() -> (InnerAttribution<T>) EMPTY_ATTRIBUTION).getValue();
    }

    /**
     * Sets attribution *
     *
     * @param <T>         parameter
     * @param attribution attribution
     * @since 2024.2.0
     */
    public <T> void setAttribution(InnerAttribution<T> attribution) {
        this.attributions.put(attribution, attribution);
    }


    /**
     * 内部属性对象
     *
     * @param <T> parameter
     * @author zhonghaijun
     * @version 1.0.0
     * @email "mailto:zhonghaijun@zhxx.com"
     * @date 2024.07.22 18:12
     * @since 2024.2.0
     */
    @Data
    @Accessors(chain = true)
    public static final class InnerAttribution<T> {
        /** Key */
        private String key;

        /** Value */
        private T value;
    }
}
