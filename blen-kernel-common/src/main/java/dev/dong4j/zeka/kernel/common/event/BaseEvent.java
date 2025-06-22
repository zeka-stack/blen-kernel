package dev.dong4j.zeka.kernel.common.event;

import org.springframework.context.ApplicationEvent;

/**
 * <p>Description: 框架事件基类.</p>
 *
 * @param <T> parameter
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dong4j@gmail.com"
 * @date 2019.11.20 19:20
 * @since 1.0.0
 */
@SuppressWarnings("java:S2387")
public abstract class BaseEvent<T> extends ApplicationEvent {

    /** serialVersionUID */
    private static final long serialVersionUID = 8827571122622596500L;

    /** Source */
    protected transient T source;

    /**
     * Instantiates a new Base event.
     *
     * @param source the source
     * @since 1.0.0
     */
    protected BaseEvent(T source) {
        super(source);
        this.source = source;
    }

    /**
     * Gets source *
     *
     * @return the source
     * @since 1.0.0
     */
    @Override
    public T getSource() {
        return this.source;
    }
}
