package dev.dong4j.zeka.kernel.notify;

import java.io.Serializable;

/**
 * 消息接口，定义了通知消息的基本结构和行为
 * 作为所有通知消息类型的顶级接口，提供消息唯一标识的管理
 * 支持泛型定义，允许不同类型的消息ID，如Long、String等
 *
 * @param <ID> 消息ID类型参数，必须实现Serializable接口
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2019.12.26 21:38
 * @since 1.0.0
 */
public interface Message<ID extends Serializable> extends Serializable {

    /**
     * 获取消息的唯一标识符
     *
     * @return 消息ID，用于唯一标识一条消息
     * @since 1.0.0
     */
    ID getMessageId();

    /**
     * 设置消息的唯一标识符
     *
     * @param id 消息ID，用于唯一标识一条消息
     * @since 1.0.0
     */
    void setMessageId(ID id);
}
