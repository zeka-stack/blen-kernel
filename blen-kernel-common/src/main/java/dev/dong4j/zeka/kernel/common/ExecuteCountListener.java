package dev.dong4j.zeka.kernel.common;

import dev.dong4j.zeka.processor.annotation.AutoListener;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.context.event.ApplicationStartingEvent;
import org.springframework.core.Ordered;

/**
 * <p>Description: listener 执行次数统计 </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.05.19 19:46
 * @since 1.0.0
 */
@Slf4j
@AutoListener
public class ExecuteCountListener implements ZekaApplicationListener {

    /**
     * Gets order *
     *
     * @return the order
     * @since 1.0.0
     */
    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    /**
     * On application starting event
     *
     * @param event event
     * @since 1.0.0
     */
    @Override
    public void onApplicationStartingEvent(@NotNull ApplicationStartingEvent event) {
        // 统计被执行的次数
        ZekaApplicationListener.Runner.executeCount();
        ZekaApplicationListener.Runner.execution(this.key(event, this.getClass()));
    }
}
