package dev.dong4j.zeka.kernel.common.base;

import java.io.Serializable;

/**
 * <p>Description:  </p>
 *
 * @param <D> 数据传输对象
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2021.10.31 14:51
 * @since 1.0.0
 */
public interface IDubboClient<D extends BaseDTO<? extends Serializable>> extends ICrudDelegate<D> {

}
