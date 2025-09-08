package dev.dong4j.zeka.kernel.common.reflection.factory;

import java.util.List;
import java.util.Properties;

/**
 * MyBatis uses an ObjectFactory to create all needed new Objects.
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.04.12 11:46
 * @since 1.0.0
 */
public interface ObjectFactory {

    /**
     * Sets configuration properties.
     *
     * @param properties configuration properties
     * @since 1.0.0
     */
    default void setProperties(Properties properties) {
    }

    /**
     * Creates a new object with default constructor.
     *
     * @param <T>  parameter
     * @param type Object type
     * @return t t
     * @since 1.0.0
     */
    <T> T create(Class<T> type);

    /**
     * Creates a new object with the specified constructor and params.
     *
     * @param <T>                 parameter
     * @param type                Object type
     * @param constructorArgTypes Constructor argument types
     * @param constructorArgs     Constructor argument values
     * @return t t
     * @since 1.0.0
     */
    <T> T create(Class<T> type, List<Class<?>> constructorArgTypes, List<Object> constructorArgs);

    /**
     * Returns true if this object can have a set of other objects.
     * It's main purpose is to support non-java.util.Collection objects like Scala collections.
     *
     * @param <T>  parameter
     * @param type Object type
     * @return whether it is a collection or not
     * @since 1.0.0
     */
    <T> boolean isCollection(Class<T> type);

}
