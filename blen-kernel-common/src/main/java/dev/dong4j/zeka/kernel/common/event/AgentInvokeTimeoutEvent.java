package dev.dong4j.zeka.kernel.common.event;

import java.io.Serial;

/**
 * <p>Description: agent service 处理超时事件 </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2021.02.08 11:04
 * @since 1.0.0
 */
public class AgentInvokeTimeoutEvent extends BaseEvent<Object> {
    /** serialVersionUID */
    @Serial
    private static final long serialVersionUID = -3522445460251460111L;

    /**
     * Instantiates a new Base event.
     *
     * @param source the source
     * @since 1.0.0
     */
    public AgentInvokeTimeoutEvent(Object source) {
        super(source);
    }
}
