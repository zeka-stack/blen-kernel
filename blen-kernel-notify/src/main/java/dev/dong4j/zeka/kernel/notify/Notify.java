package dev.dong4j.zeka.kernel.notify;

import java.util.concurrent.CompletableFuture;

/**
 * 通知服务接口，定义了消息通知的核心功能
 * 支持同步和异步两种通知方式，满足不同场景的使用需求
 * 通过泛型设计支持不同类型的消息对象，提供灵活的扩展性
 *
 * @param <T> 消息类型参数，必须继承Message接口
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.27 18:21
 * @since 1.0.0
 */
public interface Notify<T extends Message<?>> {


    /**
     * 同步执行消息通知
     * 在当前线程中直接执行通知操作，需要等待执行完成后返回结果
     *
     * @param content 要发送的消息内容
     * @return 处理后的消息对象，可能包含更新的状态信息
     * @since 1.0.0
     */
    T notify(T content);

    /**
     * 异步执行消息通知
     * 在独立的线程中执行通知操作，不会阻塞当前线程，适用于耗时操作
     *
     * @param content 要发送的消息内容
     * @return CompletableFuture对象，可用于获取异步执行结果
     * @since 1.0.0
     */
    default CompletableFuture<T> asyncNotify(T content) {
        return CompletableFuture.supplyAsync(() -> this.notify(content));
    }

}
