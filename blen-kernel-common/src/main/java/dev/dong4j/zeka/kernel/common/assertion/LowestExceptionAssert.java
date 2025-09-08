package dev.dong4j.zeka.kernel.common.assertion;

import dev.dong4j.zeka.kernel.common.api.IResultCode;
import dev.dong4j.zeka.kernel.common.exception.LowestException;
import java.io.Serial;

/**
 * <p>Description: 全局错误异常断言 </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2023.11.11 12:20
 * @since 1.0.0
 */
public interface LowestExceptionAssert extends IResultCode, IAssert {
    /** serialVersionUID */
    @Serial
    long serialVersionUID = 3077918845714343375L;

    /**
     * New exceptions base exception.
     *
     * @param args the args
     * @return the base exception
     * @since 1.0.0
     */
    @Override
    default LowestException newException(Object... args) {
        return new LowestException(this, args, this.getMessage());
    }

    /**
     * New exceptions base exception.
     *
     * @param t    the t
     * @param args the args
     * @return the base exception
     * @since 1.0.0
     */
    @Override
    default LowestException newException(Throwable t, Object... args) {
        return new LowestException(this, args, this.getMessage(), t);
    }

}
