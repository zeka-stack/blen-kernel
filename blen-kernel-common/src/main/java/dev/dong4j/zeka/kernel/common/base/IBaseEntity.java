package dev.dong4j.zeka.kernel.common.base;

import java.io.Serializable;

/**
 * <p>Description: </p>
 *
 * @param <T> parameter
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2019.12.18 16:11
 * @since 1.0.0
 */
public interface IBaseEntity<T extends Serializable> extends Serializable {
    /** ID */
    String ID = "id";
    /** GET_ID */
    String GET_ID = "getId";

    /**
     * Gets id *
     *
     * @return the serializable
     * @since 1.0.0
     */
    T getId();
}
