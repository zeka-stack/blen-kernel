package dev.dong4j.zeka.kernel.common.reflection;

import dev.dong4j.zeka.kernel.common.reflection.factory.DefaultObjectFactory;
import dev.dong4j.zeka.kernel.common.reflection.factory.ObjectFactory;
import dev.dong4j.zeka.kernel.common.reflection.wrapper.DefaultObjectWrapperFactory;
import dev.dong4j.zeka.kernel.common.reflection.wrapper.ObjectWrapperFactory;
import org.jetbrains.annotations.Contract;

/**
 * <p>Description: 默认的实体元数据包装对象, 使用 {@link DefaultMetaObject#forObject} 生成 {@link MetaObject}</p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.04.12 11:30
 * @since 1.0.0
 */
public final class DefaultMetaObject {

    /** DEFAULT_OBJECT_FACTORY */
    public static final ObjectFactory DEFAULT_OBJECT_FACTORY = new DefaultObjectFactory();
    /** DEFAULT_OBJECT_WRAPPER_FACTORY */
    public static final ObjectWrapperFactory DEFAULT_OBJECT_WRAPPER_FACTORY = new DefaultObjectWrapperFactory();
    /** NULL_META_OBJECT */
    public static final MetaObject NULL_META_OBJECT = MetaObject.forObject(Object.class,
        DEFAULT_OBJECT_FACTORY,
        DEFAULT_OBJECT_WRAPPER_FACTORY,
        new DefaultReflectorFactory());

    /**
     * Default meta object
     *
     * @since 1.0.0
     */
    @Contract(pure = true)
    private DefaultMetaObject() {
    }

    /**
     * For object meta object
     *
     * @param object object
     * @return the meta object
     * @since 1.0.0
     */
    @Contract("!null -> new")
    public static MetaObject forObject(Object object) {
        return MetaObject.forObject(object, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY, new DefaultReflectorFactory());
    }

}
