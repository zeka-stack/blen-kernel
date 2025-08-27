package dev.dong4j.zeka.kernel.common.event;

import java.io.Serial;
import java.util.Map;
import java.util.Set;

/**
 * <p>Description:  </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.09.03 14:36
 * @since 1.6.0
 */
public class AgentRegisteredEvent extends BaseEvent<Map<String, Set<String>>> {
    /** serialVersionUID */
    @Serial
    private static final long serialVersionUID = -61402775341122728L;

    /**
     * Instantiates a new Base event.
     *
     * @param source the source
     * @since 1.6.0
     */
    public AgentRegisteredEvent(Map<String, Set<String>> source) {
        super(source);
    }
}
