package dev.dong4j.zeka.kernel.common.reflection.factory;

import dev.dong4j.zeka.kernel.common.reflection.ReflectionException;
import dev.dong4j.zeka.kernel.common.reflection.Reflector;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.3.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.04.12 11:46
 * @since 1.0.0
 */
public class DefaultObjectFactory implements ObjectFactory, Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = -8855120656740914948L;

    /**
     * Create t
     *
     * @param <T>  parameter
     * @param type type
     * @return the t
     * @since 1.0.0
     */
    @Override
    public <T> T create(Class<T> type) {
        return this.create(type, null, null);
    }

    /**
     * Create t
     *
     * @param <T>                 parameter
     * @param type                type
     * @param constructorArgTypes constructor arg types
     * @param constructorArgs     constructor args
     * @return the t
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T create(Class<T> type, List<Class<?>> constructorArgTypes, List<Object> constructorArgs) {
        Class<?> classToCreate = this.resolveInterface(type);
        // we know types are assignable
        return (T) this.instantiateClass(classToCreate, constructorArgTypes, constructorArgs);
    }

    /**
     * Is collection boolean
     *
     * @param <T>  parameter
     * @param type type
     * @return the boolean
     * @since 1.0.0
     */
    @Override
    public <T> boolean isCollection(Class<T> type) {
        return Collection.class.isAssignableFrom(type);
    }

    /**
     * Resolve interface class
     *
     * @param type type
     * @return the class
     * @since 1.0.0
     */
    protected Class<?> resolveInterface(Class<?> type) {
        Class<?> classToCreate;
        if (type == List.class || type == Collection.class || type == Iterable.class) {
            classToCreate = ArrayList.class;
        } else if (type == Map.class) {
            classToCreate = HashMap.class;
        } else if (type == SortedSet.class) {
            classToCreate = TreeSet.class;
        } else if (type == Set.class) {
            classToCreate = HashSet.class;
        } else {
            classToCreate = type;
        }
        return classToCreate;
    }

    /**
     * Instantiate class t
     *
     * @param <T>                 parameter
     * @param type                type
     * @param constructorArgTypes constructor arg types
     * @param constructorArgs     constructor args
     * @return the t
     * @since 1.0.0
     */
    @SuppressWarnings("all")
    private <T> @NotNull T instantiateClass(Class<T> type, List<Class<?>> constructorArgTypes, List<Object> constructorArgs) {
        try {
            Constructor<T> constructor;
            if (constructorArgTypes == null || constructorArgs == null) {
                constructor = type.getDeclaredConstructor();
                try {
                    return constructor.newInstance();
                } catch (IllegalAccessException e) {
                    if (Reflector.canControlMemberAccessible()) {
                        constructor.setAccessible(true);
                        return constructor.newInstance();
                    } else {
                        throw e;
                    }
                }
            }
            constructor = type.getDeclaredConstructor(constructorArgTypes.toArray(new Class[0]));
            try {
                return constructor.newInstance(constructorArgs.toArray(new Object[0]));
            } catch (IllegalAccessException e) {
                if (Reflector.canControlMemberAccessible()) {
                    constructor.setAccessible(true);
                    return constructor.newInstance(constructorArgs.toArray(new Object[0]));
                } else {
                    throw e;
                }
            }
        } catch (Exception e) {
            String argTypes = Optional.ofNullable(constructorArgTypes).orElseGet(Collections::emptyList)
                .stream().map(Class::getSimpleName).collect(Collectors.joining(","));
            String argValues = Optional.ofNullable(constructorArgs).orElseGet(Collections::emptyList)
                .stream().map(String::valueOf).collect(Collectors.joining(","));
            throw new ReflectionException("Error instantiating {}  with invalid types ({}) or values ({}). Cause: ",
                type, argTypes, argValues, e, e);
        }
    }

}
