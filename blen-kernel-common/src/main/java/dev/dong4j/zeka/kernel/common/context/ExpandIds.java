package dev.dong4j.zeka.kernel.common.context;

import java.io.Serial;
import java.io.Serializable;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * <p>Description: 扩展出来的tenantId clientId </p>
 *
 * @author dong4j
 * @version 1.8.0
 * @email "mailto:dong4j@gmaidl.com"
 * @date 2021.03.08 15:04
 * @since 1.8.0
 */
public class ExpandIds implements Serializable {

    /** serialVersionUID */
    @Serial
    private static final long serialVersionUID = -1449027854558101307L;

    /**
     * 如果 header 中存在 AgentConstant#X_AGENT_TENANTID, 则会在接收到请求后自动写入到此字段中.
     *
     * @since 1.8.0
     */
    @Nullable
    private Long tenantId;

    /**
     * 如果 header 中存在 AgentConstant#X_AGENT_APPID, 则会在接收到请求后自动写入到此字段中.
     *
     * @since 1.8.0
     */
    @Nullable
    private String clientId;

    /**
     * Get tenant id
     *
     * @return the long
     * @since 1.8.0
     */
    public Optional<Long> getTenantId() {
        return Optional.ofNullable(this.tenantId);
    }

    /**
     * Get client id
     *
     * @return the string
     * @since 1.8.0
     */
    public Optional<String> getClientId() {
        return Optional.ofNullable(this.clientId);
    }

    /**
     * Sets tenant id *
     *
     * @param tenantId tenant id
     * @since 1.8.0
     */
    public void setTenantId(@NotNull Long tenantId) {
        this.tenantId = tenantId;
    }

    /**
     * Sets client id *
     *
     * @param clientId client id
     * @since 1.8.0
     */
    public void setClientId(@NotNull String clientId) {
        this.clientId = clientId;
    }
}
