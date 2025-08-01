package dev.dong4j.zeka.kernel.common.reflection.wrapper;

import dev.dong4j.zeka.kernel.common.reflection.MetaObject;
import dev.dong4j.zeka.kernel.common.reflection.ReflectionException;
import dev.dong4j.zeka.kernel.common.reflection.property.PropertyTokenizer;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.3.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.04.12 11:50
 * @since 1.0.0
 */
public abstract class BaseWrapper implements ObjectWrapper {

    /** NO_ARGUMENTS */
    protected static final Object[] NO_ARGUMENTS = new Object[0];
    /** Meta object */
    protected final MetaObject metaObject;

    /**
     * Base wrapper
     *
     * @param metaObject meta object
     * @since 1.0.0
     */
    @Contract(pure = true)
    protected BaseWrapper(MetaObject metaObject) {
        this.metaObject = metaObject;
    }

    /**
     * Resolve collection object
     *
     * @param prop   prop
     * @param object object
     * @return the object
     * @since 1.0.0
     */
    protected Object resolveCollection(@NotNull PropertyTokenizer prop, Object object) {
        if ("".equals(prop.getName())) {
            return object;
        } else {
            return this.metaObject.getValue(prop.getName());
        }
    }

    /**
     * Gets collection value *
     *
     * @param prop       prop
     * @param collection collection
     * @return the collection value
     * @since 1.0.0
     */
    protected Object getCollectionValue(PropertyTokenizer prop, Object collection) {
        if (collection instanceof Map) {
            return ((Map<?, ?>) collection).get(prop.getIndex());
        } else {
            int i = Integer.parseInt(prop.getIndex());
            if (collection instanceof List) {
                return ((List<?>) collection).get(i);
            } else if (collection instanceof Object[]) {
                return ((Object[]) collection)[i];
            } else if (collection instanceof char[]) {
                return ((char[]) collection)[i];
            } else if (collection instanceof boolean[]) {
                return ((boolean[]) collection)[i];
            } else if (collection instanceof byte[]) {
                return ((byte[]) collection)[i];
            } else if (collection instanceof double[]) {
                return ((double[]) collection)[i];
            } else if (collection instanceof float[]) {
                return ((float[]) collection)[i];
            } else if (collection instanceof int[]) {
                return ((int[]) collection)[i];
            } else if (collection instanceof long[]) {
                return ((long[]) collection)[i];
            } else if (collection instanceof short[]) {
                return ((short[]) collection)[i];
            } else {
                throw new ReflectionException("The '{}' property of {} is not a List or Array.", prop.getName(), collection);
            }
        }
    }

    /**
     * Sets collection value *
     *
     * @param prop       prop
     * @param collection collection
     * @param value      value
     * @since 1.0.0
     */
    @SuppressWarnings(value = {"unchecked", "rawtypes"})
    protected void setCollectionValue(PropertyTokenizer prop, Object collection, Object value) {
        if (collection instanceof Map) {
            ((Map) collection).put(prop.getIndex(), value);
        } else {
            int i = Integer.parseInt(prop.getIndex());
            if (collection instanceof List) {
                ((List) collection).set(i, value);
            } else if (collection instanceof Object[]) {
                ((Object[]) collection)[i] = value;
            } else if (collection instanceof char[]) {
                ((char[]) collection)[i] = (Character) value;
            } else if (collection instanceof boolean[]) {
                ((boolean[]) collection)[i] = (Boolean) value;
            } else if (collection instanceof byte[]) {
                ((byte[]) collection)[i] = (Byte) value;
            } else if (collection instanceof double[]) {
                ((double[]) collection)[i] = (Double) value;
            } else if (collection instanceof float[]) {
                ((float[]) collection)[i] = (Float) value;
            } else if (collection instanceof int[]) {
                ((int[]) collection)[i] = (Integer) value;
            } else if (collection instanceof long[]) {
                ((long[]) collection)[i] = (Long) value;
            } else if (collection instanceof short[]) {
                ((short[]) collection)[i] = (Short) value;
            } else {
                throw new ReflectionException("The '{}' property of {} is not a List or Array.", prop.getName(), collection);
            }
        }
    }

}
