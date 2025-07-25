package dev.dong4j.zeka.kernel.common.reflection.wrapper;

import com.google.common.collect.Maps;
import dev.dong4j.zeka.kernel.common.reflection.DefaultMetaObject;
import dev.dong4j.zeka.kernel.common.reflection.MetaObject;
import dev.dong4j.zeka.kernel.common.reflection.factory.ObjectFactory;
import dev.dong4j.zeka.kernel.common.reflection.property.PropertyTokenizer;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.3.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.04.12 11:52
 * @since 1.0.0
 */
public class MapWrapper extends BaseWrapper {

    /** Map */
    private final Map<String, Object> map;

    /**
     * Map wrapper
     *
     * @param metaObject meta object
     * @param map        map
     * @since 1.0.0
     */
    public MapWrapper(MetaObject metaObject, Map<String, Object> map) {
        super(metaObject);
        this.map = map;
    }

    /**
     * Get object
     *
     * @param prop prop
     * @return the object
     * @since 1.0.0
     */
    @Override
    public Object get(@NotNull PropertyTokenizer prop) {
        if (prop.getIndex() != null) {
            Object collection = this.resolveCollection(prop, this.map);
            return this.getCollectionValue(prop, collection);
        } else {
            return this.map.get(prop.getName());
        }
    }

    /**
     * Set *
     *
     * @param prop  prop
     * @param value value
     * @since 1.0.0
     */
    @Override
    public void set(@NotNull PropertyTokenizer prop, Object value) {
        if (prop.getIndex() != null) {
            Object collection = this.resolveCollection(prop, this.map);
            this.setCollectionValue(prop, collection, value);
        } else {
            this.map.put(prop.getName(), value);
        }
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
        return name;
    }

    /**
     * Get getter names string [ ]
     *
     * @return the string [ ]
     * @since 1.0.0
     */
    @Override
    public String[] getGetterNames() {
        return this.map.keySet().toArray(new String[0]);
    }

    /**
     * Get setter names string [ ]
     *
     * @return the string [ ]
     * @since 1.0.0
     */
    @Override
    public String[] getSetterNames() {
        return this.map.keySet().toArray(new String[0]);
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
        PropertyTokenizer prop = new PropertyTokenizer(name);
        if (prop.hasNext()) {
            MetaObject metaValue = this.metaObject.metaObjectForProperty(prop.getIndexedName());
            if (metaValue == DefaultMetaObject.NULL_META_OBJECT) {
                return Object.class;
            } else {
                return metaValue.getSetterType(prop.getChildren());
            }
        } else {
            if (this.map.get(name) != null) {
                return this.map.get(name).getClass();
            } else {
                return Object.class;
            }
        }
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
        PropertyTokenizer prop = new PropertyTokenizer(name);
        if (prop.hasNext()) {
            MetaObject metaValue = this.metaObject.metaObjectForProperty(prop.getIndexedName());
            if (metaValue == DefaultMetaObject.NULL_META_OBJECT) {
                return Object.class;
            } else {
                return metaValue.getGetterType(prop.getChildren());
            }
        } else {
            if (this.map.get(name) != null) {
                return this.map.get(name).getClass();
            } else {
                return Object.class;
            }
        }
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
        return true;
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
        PropertyTokenizer prop = new PropertyTokenizer(name);
        if (prop.hasNext()) {
            if (this.map.containsKey(prop.getIndexedName())) {
                MetaObject metaValue = this.metaObject.metaObjectForProperty(prop.getIndexedName());
                if (metaValue == DefaultMetaObject.NULL_META_OBJECT) {
                    return true;
                } else {
                    return metaValue.hasGetter(prop.getChildren());
                }
            } else {
                return false;
            }
        } else {
            return this.map.containsKey(prop.getName());
        }
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
        HashMap<String, Object> map = Maps.newHashMap();
        this.set(prop, map);
        return MetaObject.forObject(map,
            this.metaObject.getObjectFactory(),
            this.metaObject.getObjectWrapperFactory(),
            this.metaObject.getReflectorFactory());
    }

    /**
     * Is collection boolean
     *
     * @return the boolean
     * @since 1.0.0
     */
    @Override
    public boolean isCollection() {
        return false;
    }

    /**
     * Add *
     *
     * @param element element
     * @since 1.0.0
     */
    @Override
    public void add(Object element) {
        throw new UnsupportedOperationException();
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
        throw new UnsupportedOperationException();
    }

}
