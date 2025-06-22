package dev.dong4j.zeka.kernel.common.event;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationEvent;

/**
 * <p>Description: 框架事件处理基类.</p>
 *
 * @param <T> the type parameter
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dong4j@gmail.com"
 * @date 2019.11.20 17:01
 * @since 1.0.0
 */
public abstract class BaseEventHandler<T extends ApplicationEvent> {
    /**
     * Handler.
     *
     * @param event the event
     * @since 1.0.0
     */
    protected abstract void handler(@NotNull T event);
}
