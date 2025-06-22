package dev.dong4j.zeka.kernel.notify;

import java.io.Serializable;

/**
 * <p>Description: 发送的消息 </p>
 *
 * @param <ID> parameter
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dong4j@gmail.com"
 * @date 2019.12.26 21:38
 * @since 1.2.3
 */
public interface Message<ID extends Serializable> extends Serializable {

    /**
     * Gets message id *
     *
     * @return the message id
     * @since 1.4.0
     */
    ID getMessageId();

    /**
     * Sets message id *
     *
     * @param id id
     * @since 1.4.0
     */
    void setMessageId(ID id);
}
