package dev.dong4j.zeka.kernel.common.reflection;

import dev.dong4j.zeka.kernel.common.reflection.invoker.GetFieldInvoker;
import dev.dong4j.zeka.kernel.common.reflection.invoker.Invoker;
import dev.dong4j.zeka.kernel.common.reflection.invoker.MethodInvoker;
import dev.dong4j.zeka.kernel.common.reflection.property.PropertyTokenizer;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.3.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.04.12 11:45
 * @since 1.0.0
 */
public final class MetaClass {

    /** Reflector factory */
    private final ReflectorFactory reflectorFactory;
    /** Reflector */
    private final Reflector reflector;

    /**
     * Meta class
     *
     * @param type             type
     * @param reflectorFactory reflector factory
     * @since 1.0.0
     */
    private MetaClass(Class<?> type, @NotNull ReflectorFactory reflectorFactory) {
        this.reflectorFactory = reflectorFactory;
        this.reflector = reflectorFactory.findForClass(type);
    }

    /**
     * For class meta class
     *
     * @param type             type
     * @param reflectorFactory reflector factory
     * @return the meta class
     * @since 1.0.0
     */
    @Contract("_, _ -> new")
    public static @NotNull MetaClass forClass(Class<?> type, ReflectorFactory reflectorFactory) {
        return new MetaClass(type, reflectorFactory);
    }

    /**
     * Meta class for property meta class
     *
     * @param name name
     * @return the meta class
     * @since 1.0.0
     */
    public MetaClass metaClassForProperty(String name) {
        Class<?> propType = this.reflector.getGetterType(name);
        return MetaClass.forClass(propType, this.reflectorFactory);
    }

    /**
     * Find property string
     *
     * @param name name
     * @return the string
     * @since 1.0.0
     */
    public String findProperty(String name) {
        StringBuilder prop = this.buildProperty(name, new StringBuilder());
        return !prop.isEmpty() ? prop.toString() : null;
    }

    /**
     * Find property string
     *
     * @param name                name
     * @param useCamelCaseMapping use camel case mapping
     * @return the string
     * @since 1.0.0
     */
    public String findProperty(String name, boolean useCamelCaseMapping) {
        if (useCamelCaseMapping) {
            name = name.replace("_", "");
        }
        return this.findProperty(name);
    }

    /**
     * Get getter names string [ ]
     *
     * @return the string [ ]
     * @since 1.0.0
     */
    public String[] getGetterNames() {
        return this.reflector.getGetablePropertyNames();
    }

    /**
     * Get setter names string [ ]
     *
     * @return the string [ ]
     * @since 1.0.0
     */
    public String[] getSetterNames() {
        return this.reflector.getSetablePropertyNames();
    }

    /**
     * Gets setter type *
     *
     * @param name name
     * @return the setter type
     * @since 1.0.0
     */
    public Class<?> getSetterType(String name) {
        PropertyTokenizer prop = new PropertyTokenizer(name);
        if (prop.hasNext()) {
            MetaClass metaProp = this.metaClassForProperty(prop.getName());
            return metaProp.getSetterType(prop.getChildren());
        } else {
            return this.reflector.getSetterType(prop.getName());
        }
    }

    /**
     * Gets getter type *
     *
     * @param name name
     * @return the getter type
     * @since 1.0.0
     */
    public Class<?> getGetterType(String name) {
        PropertyTokenizer prop = new PropertyTokenizer(name);
        if (prop.hasNext()) {
            MetaClass metaProp = this.metaClassForProperty(prop);
            return metaProp.getGetterType(prop.getChildren());
        }
        return this.getGetterType(prop);
    }

    /**
     * Meta class for property meta class
     *
     * @param prop prop
     * @return the meta class
     * @since 1.0.0
     */
    private @NotNull MetaClass metaClassForProperty(PropertyTokenizer prop) {
        Class<?> propType = this.getGetterType(prop);
        return MetaClass.forClass(propType, this.reflectorFactory);
    }

    /**
     * Gets getter type *
     *
     * @param prop prop
     * @return the getter type
     * @since 1.0.0
     */
    private Class<?> getGetterType(@NotNull PropertyTokenizer prop) {
        Class<?> type = this.reflector.getGetterType(prop.getName());
        if (prop.getIndex() != null && Collection.class.isAssignableFrom(type)) {
            Type returnType = this.getGenericGetterType(prop.getName());
            if (returnType instanceof ParameterizedType) {
                Type[] actualTypeArguments = ((ParameterizedType) returnType).getActualTypeArguments();
                if (actualTypeArguments.length == 1) {
                    returnType = actualTypeArguments[0];
                    if (returnType instanceof Class) {
                        type = (Class<?>) returnType;
                    } else if (returnType instanceof ParameterizedType) {
                        type = (Class<?>) ((ParameterizedType) returnType).getRawType();
                    }
                }
            }
        }
        return type;
    }

    /**
     * Gets generic getter type *
     *
     * @param propertyName property name
     * @return the generic getter type
     * @since 1.0.0
     */
    private @Nullable Type getGenericGetterType(String propertyName) {
        try {
            Invoker invoker = this.reflector.getGetInvoker(propertyName);
            if (invoker instanceof MethodInvoker) {
                Field methodField = MethodInvoker.class.getDeclaredField("method");
                methodField.setAccessible(true);
                Method method = (Method) methodField.get(invoker);
                return TypeParameterResolver.resolveReturnType(method, this.reflector.getType());
            } else if (invoker instanceof GetFieldInvoker) {
                Field field = GetFieldInvoker.class.getDeclaredField("field");
                field.setAccessible(true);
                return TypeParameterResolver.resolveFieldType((Field) field.get(invoker), this.reflector.getType());
            }
        } catch (NoSuchFieldException | IllegalAccessException ignored) {
        }
        return null;
    }

    /**
     * Has setter boolean
     *
     * @param name name
     * @return the boolean
     * @since 1.0.0
     */
    public boolean hasSetter(String name) {
        PropertyTokenizer prop = new PropertyTokenizer(name);
        if (prop.hasNext()) {
            if (this.reflector.hasSetter(prop.getName())) {
                MetaClass metaProp = this.metaClassForProperty(prop.getName());
                return metaProp.hasSetter(prop.getChildren());
            } else {
                return false;
            }
        } else {
            return this.reflector.hasSetter(prop.getName());
        }
    }

    /**
     * Has getter boolean
     *
     * @param name name
     * @return the boolean
     * @since 1.0.0
     */
    public boolean hasGetter(String name) {
        PropertyTokenizer prop = new PropertyTokenizer(name);
        if (prop.hasNext()) {
            if (this.reflector.hasGetter(prop.getName())) {
                MetaClass metaProp = this.metaClassForProperty(prop);
                return metaProp.hasGetter(prop.getChildren());
            } else {
                return false;
            }
        } else {
            return this.reflector.hasGetter(prop.getName());
        }
    }

    /**
     * Gets get invoker *
     *
     * @param name name
     * @return the get invoker
     * @since 1.0.0
     */
    public Invoker getGetInvoker(String name) {
        return this.reflector.getGetInvoker(name);
    }

    /**
     * Gets set invoker *
     *
     * @param name name
     * @return the set invoker
     * @since 1.0.0
     */
    public Invoker getSetInvoker(String name) {
        return this.reflector.getSetInvoker(name);
    }

    /**
     * Build property string builder
     *
     * @param name    name
     * @param builder builder
     * @return the string builder
     * @since 1.0.0
     */
    private StringBuilder buildProperty(String name, StringBuilder builder) {
        PropertyTokenizer prop = new PropertyTokenizer(name);
        if (prop.hasNext()) {
            String propertyName = this.reflector.findPropertyName(prop.getName());
            if (propertyName != null) {
                builder.append(propertyName);
                builder.append(".");
                MetaClass metaProp = this.metaClassForProperty(propertyName);
                metaProp.buildProperty(prop.getChildren(), builder);
            }
        } else {
            String propertyName = this.reflector.findPropertyName(name);
            if (propertyName != null) {
                builder.append(propertyName);
            }
        }
        return builder;
    }

    /**
     * Has default constructor boolean
     *
     * @return the boolean
     * @since 1.0.0
     */
    public boolean hasDefaultConstructor() {
        return this.reflector.hasDefaultConstructor();
    }

}
