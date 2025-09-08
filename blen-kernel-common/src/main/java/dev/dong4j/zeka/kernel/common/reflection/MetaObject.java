package dev.dong4j.zeka.kernel.common.reflection;

import dev.dong4j.zeka.kernel.common.reflection.factory.ObjectFactory;
import dev.dong4j.zeka.kernel.common.reflection.property.PropertyTokenizer;
import dev.dong4j.zeka.kernel.common.reflection.wrapper.BeanWrapper;
import dev.dong4j.zeka.kernel.common.reflection.wrapper.CollectionWrapper;
import dev.dong4j.zeka.kernel.common.reflection.wrapper.MapWrapper;
import dev.dong4j.zeka.kernel.common.reflection.wrapper.ObjectWrapper;
import dev.dong4j.zeka.kernel.common.reflection.wrapper.ObjectWrapperFactory;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import org.jetbrains.annotations.Contract;

/**
 * <p>Description: 实体元数据包装对象 </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.04.12 10:55
 * @since 1.0.0
 */
public final class MetaObject {

    /** Original object */
    @Getter
    private final Object originalObject;
    /** Object wrapper */
    @Getter
    private final ObjectWrapper objectWrapper;
    /** Object factory */
    @Getter
    private final ObjectFactory objectFactory;
    /** Object wrapper factory */
    @Getter
    private final ObjectWrapperFactory objectWrapperFactory;
    /** Reflector factory */
    @Getter
    private final ReflectorFactory reflectorFactory;

    /**
     * Meta object
     *
     * @param object               object
     * @param objectFactory        object factory
     * @param objectWrapperFactory object wrapper factory
     * @param reflectorFactory     reflector factory
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    private MetaObject(Object object,
                       ObjectFactory objectFactory,
                       ObjectWrapperFactory objectWrapperFactory,
                       ReflectorFactory reflectorFactory) {
        this.originalObject = object;
        this.objectFactory = objectFactory;
        this.objectWrapperFactory = objectWrapperFactory;
        this.reflectorFactory = reflectorFactory;

        if (object instanceof ObjectWrapper) {
            this.objectWrapper = (ObjectWrapper) object;
        } else if (objectWrapperFactory.hasWrapperFor(object)) {
            this.objectWrapper = objectWrapperFactory.getWrapperFor(this, object);
        } else if (object instanceof Map) {
            this.objectWrapper = new MapWrapper(this, (Map<String, Object>) object);
        } else if (object instanceof Collection) {
            this.objectWrapper = new CollectionWrapper(this, (Collection<Object>) object);
        } else {
            this.objectWrapper = new BeanWrapper(this, object);
        }
    }

    /**
     * For object meta object
     *
     * @param object               object
     * @param objectFactory        object factory
     * @param objectWrapperFactory object wrapper factory
     * @param reflectorFactory     reflector factory
     * @return the meta object
     * @since 1.0.0
     */
    @Contract("!null, _, _, _ -> new")
    public static MetaObject forObject(Object object,
                                       ObjectFactory objectFactory,
                                       ObjectWrapperFactory objectWrapperFactory,
                                       ReflectorFactory reflectorFactory) {
        if (object == null) {
            return DefaultMetaObject.NULL_META_OBJECT;
        } else {
            return new MetaObject(object, objectFactory, objectWrapperFactory, reflectorFactory);
        }
    }

    /**
     * Find property string
     *
     * @param propName            prop name
     * @param useCamelCaseMapping use camel case mapping
     * @return the string
     * @since 1.0.0
     */
    public String findProperty(String propName, boolean useCamelCaseMapping) {
        return this.objectWrapper.findProperty(propName, useCamelCaseMapping);
    }

    /**
     * Get getter names string [ ]
     *
     * @return the string [ ]
     * @since 1.0.0
     */
    public String[] getGetterNames() {
        return this.objectWrapper.getGetterNames();
    }

    /**
     * Get setter names string [ ]
     *
     * @return the string [ ]
     * @since 1.0.0
     */
    public String[] getSetterNames() {
        return this.objectWrapper.getSetterNames();
    }

    /**
     * Gets setter type *
     *
     * @param name name
     * @return the setter type
     * @since 1.0.0
     */
    public Class<?> getSetterType(String name) {
        return this.objectWrapper.getSetterType(name);
    }

    /**
     * Gets getter type *
     *
     * @param name name
     * @return the getter type
     * @since 1.0.0
     */
    public Class<?> getGetterType(String name) {
        return this.objectWrapper.getGetterType(name);
    }

    /**
     * Has setter boolean
     *
     * @param name name
     * @return the boolean
     * @since 1.0.0
     */
    public boolean hasSetter(String name) {
        return this.objectWrapper.hasSetter(name);
    }

    /**
     * Has getter boolean
     *
     * @param name name
     * @return the boolean
     * @since 1.0.0
     */
    public boolean hasGetter(String name) {
        return this.objectWrapper.hasGetter(name);
    }

    /**
     * Gets value *
     *
     * @param name name
     * @return the value
     * @since 1.0.0
     */
    public Object getValue(String name) {
        PropertyTokenizer prop = new PropertyTokenizer(name);
        if (prop.hasNext()) {
            MetaObject metaValue = this.metaObjectForProperty(prop.getIndexedName());
            if (metaValue == DefaultMetaObject.NULL_META_OBJECT) {
                return null;
            } else {
                return metaValue.getValue(prop.getChildren());
            }
        } else {
            return this.objectWrapper.get(prop);
        }
    }

    /**
     * Sets value *
     *
     * @param name  name
     * @param value value
     * @since 1.0.0
     */
    public void setValue(String name, Object value) {
        PropertyTokenizer prop = new PropertyTokenizer(name);
        if (prop.hasNext()) {
            MetaObject metaValue = this.metaObjectForProperty(prop.getIndexedName());
            if (metaValue == DefaultMetaObject.NULL_META_OBJECT) {
                if (value == null) {
                    // don't instantiate child path if value is null
                    return;
                } else {
                    metaValue = this.objectWrapper.instantiatePropertyValue(name, prop, this.objectFactory);
                }
            }
            metaValue.setValue(prop.getChildren(), value);
        } else {
            this.objectWrapper.set(prop, value);
        }
    }

    /**
     * Meta object for property meta object
     *
     * @param name name
     * @return the meta object
     * @since 1.0.0
     */
    public MetaObject metaObjectForProperty(String name) {
        Object value = this.getValue(name);
        return MetaObject.forObject(value, this.objectFactory, this.objectWrapperFactory, this.reflectorFactory);
    }


    /**
     * Is collection boolean
     *
     * @return the boolean
     * @since 1.0.0
     */
    public boolean isCollection() {
        return this.objectWrapper.isCollection();
    }

    /**
     * Add *
     *
     * @param element element
     * @since 1.0.0
     */
    public void add(Object element) {
        this.objectWrapper.add(element);
    }

    /**
     * Add all *
     *
     * @param <E>  parameter
     * @param list list
     * @since 1.0.0
     */
    public <E> void addAll(List<E> list) {
        this.objectWrapper.addAll(list);
    }

}
