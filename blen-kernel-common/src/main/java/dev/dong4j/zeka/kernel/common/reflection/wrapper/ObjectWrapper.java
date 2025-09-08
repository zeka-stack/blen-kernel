package dev.dong4j.zeka.kernel.common.reflection.wrapper;

import dev.dong4j.zeka.kernel.common.reflection.MetaObject;
import dev.dong4j.zeka.kernel.common.reflection.factory.ObjectFactory;
import dev.dong4j.zeka.kernel.common.reflection.property.PropertyTokenizer;
import java.util.List;

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.04.12 11:54
 * @since 1.0.0
 */
public interface ObjectWrapper {

    /**
     * Get object
     *
     * @param prop prop
     * @return the object
     * @since 1.0.0
     */
    Object get(PropertyTokenizer prop);

    /**
     * Set *
     *
     * @param prop  prop
     * @param value value
     * @since 1.0.0
     */
    void set(PropertyTokenizer prop, Object value);

    /**
     * Find property string
     *
     * @param name                name
     * @param useCamelCaseMapping use camel case mapping
     * @return the string
     * @since 1.0.0
     */
    String findProperty(String name, boolean useCamelCaseMapping);

    /**
     * Get getter names string [ ]
     *
     * @return the string [ ]
     * @since 1.0.0
     */
    String[] getGetterNames();

    /**
     * Get setter names string [ ]
     *
     * @return the string [ ]
     * @since 1.0.0
     */
    String[] getSetterNames();

    /**
     * Gets setter type *
     *
     * @param name name
     * @return the setter type
     * @since 1.0.0
     */
    Class<?> getSetterType(String name);

    /**
     * Gets getter type *
     *
     * @param name name
     * @return the getter type
     * @since 1.0.0
     */
    Class<?> getGetterType(String name);

    /**
     * Has setter boolean
     *
     * @param name name
     * @return the boolean
     * @since 1.0.0
     */
    boolean hasSetter(String name);

    /**
     * Has getter boolean
     *
     * @param name name
     * @return the boolean
     * @since 1.0.0
     */
    boolean hasGetter(String name);

    /**
     * Instantiate property value meta object
     *
     * @param name          name
     * @param prop          prop
     * @param objectFactory object factory
     * @return the meta object
     * @since 1.0.0
     */
    MetaObject instantiatePropertyValue(String name, PropertyTokenizer prop, ObjectFactory objectFactory);

    /**
     * Is collection boolean
     *
     * @return the boolean
     * @since 1.0.0
     */
    boolean isCollection();

    /**
     * Add *
     *
     * @param element element
     * @since 1.0.0
     */
    void add(Object element);

    /**
     * Add all *
     *
     * @param <E>     parameter
     * @param element element
     * @since 1.0.0
     */
    <E> void addAll(List<E> element);

}
