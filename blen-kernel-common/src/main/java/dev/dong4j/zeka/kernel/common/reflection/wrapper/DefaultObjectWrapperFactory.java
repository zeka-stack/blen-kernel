package dev.dong4j.zeka.kernel.common.reflection.wrapper;

import dev.dong4j.zeka.kernel.common.reflection.MetaObject;
import dev.dong4j.zeka.kernel.common.reflection.ReflectionException;

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.04.12 11:52
 * @since 1.0.0
 */
public class DefaultObjectWrapperFactory implements ObjectWrapperFactory {

    /**
     * Has wrapper for boolean
     *
     * @param object object
     * @return the boolean
     * @since 1.0.0
     */
    @Override
    public boolean hasWrapperFor(Object object) {
        return false;
    }

    /**
     * Gets wrapper for *
     *
     * @param metaObject meta object
     * @param object     object
     * @return the wrapper for
     * @since 1.0.0
     */
    @Override
    public ObjectWrapper getWrapperFor(MetaObject metaObject, Object object) {
        throw new ReflectionException("The DefaultObjectWrapperFactory should never be called to provide an ObjectWrapper.");
    }

}
