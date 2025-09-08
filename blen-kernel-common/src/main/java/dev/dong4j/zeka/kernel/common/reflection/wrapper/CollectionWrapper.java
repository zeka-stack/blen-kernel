package dev.dong4j.zeka.kernel.common.reflection.wrapper;

import dev.dong4j.zeka.kernel.common.reflection.MetaObject;
import dev.dong4j.zeka.kernel.common.reflection.factory.ObjectFactory;
import dev.dong4j.zeka.kernel.common.reflection.property.PropertyTokenizer;
import java.util.Collection;
import java.util.List;
import org.jetbrains.annotations.Contract;

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.04.12 11:52
 * @since 1.0.0
 */
public class CollectionWrapper implements ObjectWrapper {

    /** Object */
    private final Collection<Object> object;

    /**
     * Collection wrapper
     *
     * @param metaObject meta object
     * @param object     object
     * @since 1.0.0
     */
    @Contract(pure = true)
    public CollectionWrapper(MetaObject metaObject, Collection<Object> object) {
        this.object = object;
    }

    /**
     * Get object
     *
     * @param prop prop
     * @return the object
     * @since 1.0.0
     */
    @Override
    public Object get(PropertyTokenizer prop) {
        throw new UnsupportedOperationException();
    }

    /**
     * Set *
     *
     * @param prop  prop
     * @param value value
     * @since 1.0.0
     */
    @Override
    public void set(PropertyTokenizer prop, Object value) {
        throw new UnsupportedOperationException();
    }

    /**
     * Find property string
     *
     * @param name                name
     * @param useCamelCaseMapping use camel case mapping
     * @return the string
     * @since 1.0.0
     */
    @Override
    public String findProperty(String name, boolean useCamelCaseMapping) {
        throw new UnsupportedOperationException();
    }

    /**
     * Get getter names string [ ]
     *
     * @return the string [ ]
     * @since 1.0.0
     */
    @Override
    public String[] getGetterNames() {
        throw new UnsupportedOperationException();
    }

    /**
     * Get setter names string [ ]
     *
     * @return the string [ ]
     * @since 1.0.0
     */
    @Override
    public String[] getSetterNames() {
        throw new UnsupportedOperationException();
    }

    /**
     * Gets setter type *
     *
     * @param name name
     * @return the setter type
     * @since 1.0.0
     */
    @Override
    public Class<?> getSetterType(String name) {
        throw new UnsupportedOperationException();
    }

    /**
     * Gets getter type *
     *
     * @param name name
     * @return the getter type
     * @since 1.0.0
     */
    @Override
    public Class<?> getGetterType(String name) {
        throw new UnsupportedOperationException();
    }

    /**
     * Has setter boolean
     *
     * @param name name
     * @return the boolean
     * @since 1.0.0
     */
    @Override
    public boolean hasSetter(String name) {
        throw new UnsupportedOperationException();
    }

    /**
     * Has getter boolean
     *
     * @param name name
     * @return the boolean
     * @since 1.0.0
     */
    @Override
    public boolean hasGetter(String name) {
        throw new UnsupportedOperationException();
    }

    /**
     * Instantiate property value meta object
     *
     * @param name          name
     * @param prop          prop
     * @param objectFactory object factory
     * @return the meta object
     * @since 1.0.0
     */
    @Override
    public MetaObject instantiatePropertyValue(String name, PropertyTokenizer prop, ObjectFactory objectFactory) {
        throw new UnsupportedOperationException();
    }

    /**
     * Is collection boolean
     *
     * @return the boolean
     * @since 1.0.0
     */
    @Override
    public boolean isCollection() {
        return true;
    }

    /**
     * Add *
     *
     * @param element element
     * @since 1.0.0
     */
    @Override
    public void add(Object element) {
        this.object.add(element);
    }

    /**
     * Add all *
     *
     * @param <E>     parameter
     * @param element element
     * @since 1.0.0
     */
    @Override
    public <E> void addAll(List<E> element) {
        this.object.addAll(element);
    }

}
