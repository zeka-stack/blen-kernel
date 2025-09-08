package dev.dong4j.zeka.kernel.common.event;

import java.io.Serial;
import java.util.Map;

/**
 * <p>Description: sql 执行超时发送的事件</p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2021.04.27 18:30
 * @since 1.0.0
 */
public class SqlExecuteTimeoutEvent extends BaseEvent<Map<String, Object>> {
    /** serialVersionUID */
    @Serial
    private static final long serialVersionUID = 5423843143989945987L;

    /**
     * Instantiates a new Base event.
     *
     * @param source the source
     * @since 1.0.0
     */
    public SqlExecuteTimeoutEvent(Map<String, Object> source) {
        super(source);
    }
}
