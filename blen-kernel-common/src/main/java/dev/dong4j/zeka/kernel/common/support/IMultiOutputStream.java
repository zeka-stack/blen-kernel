package dev.dong4j.zeka.kernel.common.support;

import java.io.OutputStream;

/**
 * <p>Description: A factory for creating MultiOutputStream objects. </p>
 *
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.27 14:53
 * @since 1.0.0
 */
public interface IMultiOutputStream {

    /**
     * Builds the output stream.
     *
     * @param params the params
     * @return the output stream
     * @since 1.0.0
     */
    OutputStream buildOutputStream(Integer... params);

}
