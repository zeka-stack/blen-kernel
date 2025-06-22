package dev.dong4j.zeka.kernel.notify;

import java.util.concurrent.CompletableFuture;

/**
 * <p>Description: </p>
 *
 * @param <T> 消息体
 * @author dong4j
 * @version 1.4.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.27 18:21
 * @since 1.4.0
 */
public interface Notify<T extends Message<?>> {


    /**
     * 同步通知
     *
     * @param content 发送内容
     * @return the string
     * @since 1.4.0
     */
    T notify(T content);

    /**
     * 异步通知
     *
     * @param content 发送内容
     * @return the future
     * @since 1.4.0
     */
    default CompletableFuture<T> asyncNotify(T content) {
        return CompletableFuture.supplyAsync(() -> this.notify(content));
    }

}
