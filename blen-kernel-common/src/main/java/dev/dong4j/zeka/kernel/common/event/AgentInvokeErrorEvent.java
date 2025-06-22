package dev.dong4j.zeka.kernel.common.event;

/**
 * <p>Description: agent service 处理失败事件 </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2021.02.08 11:04
 * @since 1.8.0
 */
public class AgentInvokeErrorEvent extends BaseEvent<Object> {
    /** serialVersionUID */
    private static final long serialVersionUID = -3522445460251460111L;

    /**
     * Instantiates a new Base event.
     *
     * @param source the source
     * @since 1.8.0
     */
    public AgentInvokeErrorEvent(Object source) {
        super(source);
    }
}
