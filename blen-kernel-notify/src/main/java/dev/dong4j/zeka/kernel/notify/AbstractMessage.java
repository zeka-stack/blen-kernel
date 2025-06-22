package dev.dong4j.zeka.kernel.notify;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * <p>Description:  </p>
 *
 * @param <ID> parameter
 * @author dong4j
 * @version 1.4.0
 * @email "mailto:dongshijie@gmail.com"
 * @date 2020.05.07 21:21
 * @since 1.4.0
 */
@SuperBuilder
@NoArgsConstructor
@SuppressWarnings("all")
public abstract class AbstractMessage<ID extends Serializable> implements Message<ID> {
    /** serialVersionUID */
    private static final long serialVersionUID = -1021368546633555716L;
    /** Message id */
    @Getter
    @Setter
    protected ID messageId;

}
