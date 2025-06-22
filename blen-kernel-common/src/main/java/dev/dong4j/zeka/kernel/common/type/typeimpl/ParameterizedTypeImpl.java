package dev.dong4j.zeka.kernel.common.type.typeimpl;

import dev.dong4j.zeka.kernel.common.type.exception.TypeException;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.Objects;

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.27 14:53
 * @since 1.0.0
 */
public class ParameterizedTypeImpl implements ParameterizedType {
    /** Raw */
    private final Class<?> raw;
    /** Args */
    private final Type[] args;
    /** Owner */
    private final Type owner;

    /**
     * Parameterized type
     *
     * @param raw   raw
     * @param args  args
     * @param owner owner
     * @since 1.0.0
     */
    public ParameterizedTypeImpl(Class<?> raw, Type[] args, Type owner) {
        this.raw = raw;
        this.args = args != null ? args : new Type[0];
        this.owner = owner;
        this.checkArgs();
    }

    /**
     * Check args
     *
     * @since 1.0.0
     */
    private void checkArgs() {
        if (this.raw == null) {
            throw new TypeException("raw class can't be null");
        }
        TypeVariable<? extends Class<?>>[] typeParameters = this.raw.getTypeParameters();
        if (this.args.length != 0 && typeParameters.length != this.args.length) {
            throw new TypeException(this.raw.getName() + " expect " + typeParameters.length + " arg(s), got " + this.args.length);
        }
    }

    /**
     * Get actual type arguments type [ ]
     *
     * @return the type [ ]
     * @since 1.0.0
     */
    @Override
    public Type[] getActualTypeArguments() {
        return this.args;
    }

    /**
     * Gets raw type *
     *
     * @return the raw type
     * @since 1.0.0
     */
    @Override
    public Type getRawType() {
        return this.raw;
    }

    /**
     * Gets owner type *
     *
     * @return the owner type
     * @since 1.0.0
     */
    @Override
    public Type getOwnerType() {
        return this.owner;
    }

    /**
     * Hash code int
     *
     * @return the int
     * @since 1.0.0
     */
    @Override
    public int hashCode() {
        int result = this.raw.hashCode();
        result = 31 * result + Arrays.hashCode(this.args);
        result = 31 * result + (this.owner != null ? this.owner.hashCode() : 0);
        return result;
    }

    /**
     * Equals boolean
     *
     * @param o o
     * @return the boolean
     * @since 1.0.0
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }

        ParameterizedTypeImpl that = (ParameterizedTypeImpl) o;

        if (!this.raw.equals(that.raw)) {
            return false;
        }
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(this.args, that.args)) {
            return false;
        }
        return Objects.equals(this.owner, that.owner);

    }

    /**
     * To string string
     *
     * @return the string
     * @since 1.0.0
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.raw.getName());
        if (this.args.length != 0) {
            sb.append('<');
            for (int i = 0; i < this.args.length; i++) {
                if (i != 0) {
                    sb.append(", ");
                }
                Type type = this.args[i];
                if (type instanceof Class) {
                    Class<?> clazz = (Class<?>) type;

                    if (clazz.isArray()) {
                        int count = 0;
                        do {
                            count++;
                            clazz = clazz.getComponentType();
                        } while (clazz.isArray());

                        sb.append(clazz.getName());

                        for (int j = count; j > 0; j--) {
                            sb.append("[]");
                        }
                    } else {
                        sb.append(clazz.getName());
                    }
                } else {
                    sb.append(this.args[i].toString());
                }
            }
            sb.append('>');
        }
        return sb.toString();
    }
}
