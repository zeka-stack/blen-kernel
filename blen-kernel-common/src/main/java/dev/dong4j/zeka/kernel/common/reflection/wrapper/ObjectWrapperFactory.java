package dev.dong4j.zeka.kernel.common.reflection.wrapper;

import dev.dong4j.zeka.kernel.common.reflection.MetaObject;

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.04.12 11:54
 * @since 1.0.0
 */
public interface ObjectWrapperFactory {

    /**
     * Has wrapper for boolean
     *
     * @param object object
     * @return the boolean
     * @since 1.0.0
     */
    boolean hasWrapperFor(Object object);

    /**
     * Gets wrapper for *
     *
     * @param metaObject meta object
     * @param object     object
     * @return the wrapper for
     * @since 1.0.0
     */
    ObjectWrapper getWrapperFor(MetaObject metaObject, Object object);

}
