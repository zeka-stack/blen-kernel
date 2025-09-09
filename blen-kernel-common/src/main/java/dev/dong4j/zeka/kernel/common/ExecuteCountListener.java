package dev.dong4j.zeka.kernel.common;

import dev.dong4j.zeka.processor.annotation.AutoListener;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.context.event.ApplicationStartingEvent;
import org.springframework.core.Ordered;

/**
 * <p>监听器执行次数统计器.
 * <p>用于统计和记录应用程序生命周期事件监听器的执行次数和调用情况.
 * <p>作为系统初始化阶段的重要组件，负责监控和记录各种监听器的运行状态.
 * <p>主要功能：
 * <ul>
 *     <li>统计监听器的总执行次数</li>
 *     <li>记录单个监听器的执行情况</li>
 *     <li>提供应用启动过程的调试信息</li>
 *     <li>支持性能监控和分析</li>
 * </ul>
 * <p>特性说明：
 * <ul>
 *     <li>具有最高优先级，确保最先执行</li>
 *     <li>自动注册为监听器，无需手动配置</li>
 *     <li>统计信息可用于性能调优和问题诊断</li>
 * </ul>
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
