package dev.dong4j.zeka.kernel.notify;

import java.io.Serial;
import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * 消息抽象基类，提供消息接口的默认实现
 * 作为所有具体消息类型的父类，实现了通用的消息ID管理功能
 * 使用SuperBuilder模式支持建造者模式，简化复杂对象的构建过程
 *
 * @param <ID> 消息ID类型参数，必须实现Serializable接口
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dongshijie@gmail.com"
 * @date 2020.05.07 21:21
 * @since 1.0.0
 */
@SuperBuilder
@NoArgsConstructor
@SuppressWarnings("all")
public abstract class AbstractMessage<ID extends Serializable> implements Message<ID> {
    /** serialVersionUID */
    @Serial
    private static final long serialVersionUID = -1021368546633555716L;
    /** Message id */
    @Getter
    @Setter
    protected ID messageId;

}
