package dev.dong4j.zeka.kernel.common.type;

import dev.dong4j.zeka.kernel.common.type.exception.TypeException;
import dev.dong4j.zeka.kernel.common.type.typeimpl.ParameterizedTypeImpl;
import dev.dong4j.zeka.kernel.common.type.typeimpl.WildcardTypeImpl;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.27 18:10
 * @since 1.0.0
 */
public final class TypeBuilder {
    /** Parent */
    private final TypeBuilder parent;
    /** Raw */
    private final Class<?> raw;
    /** Args */
    private final List<Type> args = new ArrayList<>();


    /**
     * Type builder
     *
     * @param raw    raw
     * @param parent parent
     * @since 1.0.0
     */
    @Contract("null, _ -> fail")
    private TypeBuilder(Class<?> raw, TypeBuilder parent) {
        assert raw != null;
        this.raw = raw;
        this.parent = parent;
    }

    /**
     * New instance type builder.
     *
     * @param raw the raw
     * @return the type builder
     * @since 1.0.0
     */
    @NotNull
    @Contract("_ -> new")
    public static TypeBuilder newInstance(Class<?> raw) {
        return new TypeBuilder(raw, null);
    }

    /**
     * Begin sub type type builder.
     *
     * @param raw the raw
     * @return the type builder
     * @since 1.0.0
     */
    @NotNull
    public TypeBuilder beginSubType(Class<?> raw) {
        return newInstance(raw, this);
    }

    /**
     * New instance type builder
     *
     * @param raw    raw
     * @param parent parent
     * @return the type builder
     * @since 1.0.0
     */
    @NotNull
    @Contract("_, _ -> new")
    public static TypeBuilder newInstance(Class<?> raw, TypeBuilder parent) {
        return new TypeBuilder(raw, parent);
    }

    /**
     * End sub type type builder.
     *
     * @return the type builder
     * @since 1.0.0
     */
    public TypeBuilder endSubType() {
        if (this.parent == null) {
            throw new TypeException("expect beginSubType() before endSubType()");
        }

        this.parent.addTypeParam(this.getType());

        return this.parent;
    }

    /**
     * Add type param type builder.
     *
     * @param type the type
     * @return the type builder
     * @since 1.0.0
     */
    @Contract("null -> fail")
    private TypeBuilder addTypeParam(Type type) {
        if (type == null) {
            throw new NullPointerException("addTypeParam expect not null Type");
        }

        this.args.add(type);

        return this;
    }

    /**
     * Gets type *
     *
     * @return the type
     * @since 1.0.0
     */
    private Type getType() {
        if (this.args.isEmpty()) {
            return this.raw;
        }
        return new ParameterizedTypeImpl(this.raw, this.args.toArray(new Type[0]), null);
    }

    /**
     * Add type param type builder.
     *
     * @param clazz the clazz
     * @return the type builder
     * @since 1.0.0
     */
    public TypeBuilder addTypeParam(Class<?> clazz) {
        return this.addTypeParam((Type) clazz);
    }

    /**
     * Add type param extends type builder.
     *
     * @param classes the classes
     * @return the type builder
     * @since 1.0.0
     */
    @Contract("null -> fail")
    public TypeBuilder addTypeParamExtends(Class<?>... classes) {
        if (classes == null) {
            throw new NullPointerException("addTypeParamExtends() expect not null Class");
        }

        WildcardTypeImpl wildcardType = new WildcardTypeImpl(null, classes);

        return this.addTypeParam(wildcardType);
    }

    /**
     * Add type param super type builder.
     *
     * @param classes the classes
     * @return the type builder
     * @since 1.0.0
     */
    @Contract("null -> fail")
    public TypeBuilder addTypeParamSuper(Class<?>... classes) {
        if (classes == null) {
            throw new NullPointerException("addTypeParamSuper() expect not null Class");
        }

        WildcardTypeImpl wildcardType = new WildcardTypeImpl(classes, null);

        return this.addTypeParam(wildcardType);
    }

    /**
     * Build type.
     *
     * @return the type
     * @since 1.0.0
     */
    public Type build() {
        if (this.parent != null) {
            throw new TypeException("expect endSubType() before build()");
        }

        return this.getType();
    }
}
