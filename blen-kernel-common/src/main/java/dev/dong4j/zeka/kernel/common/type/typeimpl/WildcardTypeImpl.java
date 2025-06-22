package dev.dong4j.zeka.kernel.common.type.typeimpl;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.Arrays;

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.27 14:53
 * @since 1.0.0
 */
public class WildcardTypeImpl implements WildcardType {
    /** Upper */
    private final Class<?>[] upper;
    /** Lower */
    private final Class<?>[] lower;

    /**
     * Wildcard type
     *
     * @param lower lower
     * @param upper upper
     * @since 1.0.0
     */
    public WildcardTypeImpl(Class[] lower, Class[] upper) {
        this.lower = lower != null ? lower : new Class[0];
        this.upper = upper != null ? upper : new Class[0];

        this.checkArgs();
    }

    /**
     * Check args
     *
     * @since 1.0.0
     */
    private void checkArgs() {
        if (this.lower.length == 0 && this.upper.length == 0) {
            throw new IllegalArgumentException("lower or upper can't be null");
        }

        this.checkArgs(this.lower);
        this.checkArgs(this.upper);
    }

    /**
     * Check args *
     *
     * @param args args
     * @since 1.0.0
     */
    private void checkArgs(@NotNull Class[] args) {
        for (int i = 1; i < args.length; i++) {
            Class clazz = args[i];
            if (!clazz.isInterface()) {
                throw new IllegalArgumentException(clazz.getName() + " not a interface!");
            }
        }
    }

    /**
     * Get upper bounds type [ ]
     *
     * @return the type [ ]
     * @since 1.0.0
     */
    @Override
    public Type[] getUpperBounds() {
        return this.upper;
    }

    /**
     * Get lower bounds type [ ]
     *
     * @return the type [ ]
     * @since 1.0.0
     */
    @Override
    public Type[] getLowerBounds() {
        return this.lower;
    }

    /**
     * Hash code int
     *
     * @return the int
     * @since 1.0.0
     */
    @Override
    public int hashCode() {
        int result = Arrays.hashCode(this.upper);
        result = 31 * result + Arrays.hashCode(this.lower);
        return result;
    }

    /**
     * Equals boolean
     *
     * @param o o
     * @return the boolean
     * @since 1.0.0
     */
    @Contract(value = "null -> false", pure = true)
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }

        WildcardTypeImpl that = (WildcardTypeImpl) o;

        return Arrays.equals(this.upper, that.upper) && Arrays.equals(this.lower, that.lower);

    }

    /**
     * To string string
     *
     * @return the string
     * @since 1.0.0
     */
    @Override
    public String toString() {
        if (this.upper.length > 0) {
            if (this.upper[0] == Object.class) {
                return "?";
            }
            return this.getTypeString("? extends ", this.upper);
        } else {
            return this.getTypeString("? super ", this.lower);
        }
    }

    /**
     * Gets type string *
     *
     * @param prefix prefix
     * @param type   type
     * @return the type string
     * @since 1.0.0
     */
    @NotNull
    private String getTypeString(String prefix, @NotNull Class[] type) {
        StringBuilder sb = new StringBuilder();
        sb.append(prefix);

        for (int i = 0; i < type.length; i++) {
            if (i != 0) {
                sb.append(" & ");
            }
            sb.append(type[i].getName());
        }

        return sb.toString();

    }
}
