package dev.dong4j.zeka.kernel.spi.utils;


import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmaidl.com"
 * @date 2021.02.26 17:47
 * @since 1.0.0
 */
@SuppressWarnings("all")
public class SpiClassUtils {
    /** ARRAY_SUFFIX */
    public static final String ARRAY_SUFFIX = "[]";
    /** INTERNAL_ARRAY_PREFIX */
    private static final String INTERNAL_ARRAY_PREFIX = "[L";
    /** PRIMITIVE_TYPE_NAME_MAP */
    private static final Map<String, Class<?>> PRIMITIVE_TYPE_NAME_MAP = new HashMap<String, Class<?>>(32);
    /** PRIMITIVE_WRAPPER_TYPE_MAP */
    private static final Map<Class<?>, Class<?>> PRIMITIVE_WRAPPER_TYPE_MAP = new HashMap<Class<?>, Class<?>>(16);

    /** PACKAGE_SEPARATOR_CHAR */
    private static final char PACKAGE_SEPARATOR_CHAR = '.';

    static {
        PRIMITIVE_WRAPPER_TYPE_MAP.put(Boolean.class, boolean.class);
        PRIMITIVE_WRAPPER_TYPE_MAP.put(Byte.class, byte.class);
        PRIMITIVE_WRAPPER_TYPE_MAP.put(Character.class, char.class);
        PRIMITIVE_WRAPPER_TYPE_MAP.put(Double.class, double.class);
        PRIMITIVE_WRAPPER_TYPE_MAP.put(Float.class, float.class);
        PRIMITIVE_WRAPPER_TYPE_MAP.put(Integer.class, int.class);
        PRIMITIVE_WRAPPER_TYPE_MAP.put(Long.class, long.class);
        PRIMITIVE_WRAPPER_TYPE_MAP.put(Short.class, short.class);

        Set<Class<?>> primitiveTypeNames = new HashSet<>(32);
        primitiveTypeNames.addAll(PRIMITIVE_WRAPPER_TYPE_MAP.values());
        primitiveTypeNames.addAll(Arrays
            .asList(boolean[].class, byte[].class, char[].class, double[].class,
                float[].class, int[].class, long[].class, short[].class));
        for (Class<?> primitiveTypeName : primitiveTypeNames) {
            PRIMITIVE_TYPE_NAME_MAP.put(primitiveTypeName.getName(), primitiveTypeName);
        }
    }

    /**
     * For name with thread context class loader
     *
     * @param name name
     * @return the class
     * @throws ClassNotFoundException class not found exception
     * @since 1.0.0
     */
    public static Class<?> forNameWithThreadContextClassLoader(String name)
        throws ClassNotFoundException {
        return forName(name, Thread.currentThread().getContextClassLoader());
    }

    /**
     * For name with caller class loader
     *
     * @param name   name
     * @param caller caller
     * @return the class
     * @throws ClassNotFoundException class not found exception
     * @since 1.0.0
     */
    public static Class<?> forNameWithCallerClassLoader(String name, Class<?> caller)
        throws ClassNotFoundException {
        return forName(name, caller.getClassLoader());
    }

    /**
     * Gets caller class loader *
     *
     * @param caller caller
     * @return the caller class loader
     * @since 1.0.0
     */
    public static ClassLoader getCallerClassLoader(Class<?> caller) {
        return caller.getClassLoader();
    }

    /**
     * Gets class loader *
     *
     * @param clazz clazz
     * @return the class loader
     * @since 1.0.0
     */
    public static ClassLoader getClassLoader(Class<?> clazz) {
        ClassLoader cl = null;
        try {
            cl = Thread.currentThread().getContextClassLoader();
        } catch (Throwable ex) {
            // Cannot access thread context ClassLoader - falling back to system class loader...
        }
        if (cl == null) {
            // No thread context class loader -> use class loader of this class.
            cl = clazz.getClassLoader();
            if (cl == null) {
                // getClassLoader() returning null indicates the bootstrap ClassLoader
                try {
                    cl = ClassLoader.getSystemClassLoader();
                } catch (Throwable ex) {
                    // Cannot access system ClassLoader - oh well, maybe the caller can live with null...
                }
            }
        }

        return cl;
    }

    /**
     * Gets class loader *
     *
     * @return the class loader
     * @since 1.0.0
     */
    public static ClassLoader getClassLoader() {
        return getClassLoader(SpiClassUtils.class);
    }

    /**
     * For name
     *
     * @param name name
     * @return the class
     * @throws ClassNotFoundException class not found exception
     * @since 1.0.0
     */
    public static Class<?> forName(String name) throws ClassNotFoundException {
        return forName(name, getClassLoader());
    }

    /**
     * For name
     *
     * @param name        name
     * @param classLoader class loader
     * @return the class
     * @throws ClassNotFoundException class not found exception
     * @throws LinkageError           linkage error
     * @since 1.0.0
     */
    public static Class<?> forName(String name, ClassLoader classLoader)
        throws ClassNotFoundException, LinkageError {

        Class<?> clazz = resolvePrimitiveClassName(name);
        if (clazz != null) {
            return clazz;
        }

        // "java.lang.String[]" style arrays
        if (name.endsWith(ARRAY_SUFFIX)) {
            String elementClassName = name.substring(0, name.length() - ARRAY_SUFFIX.length());
            Class<?> elementClass = forName(elementClassName, classLoader);
            return Array.newInstance(elementClass, 0).getClass();
        }

        // "[Ljava.lang.String;" style arrays
        int internalArrayMarker = name.indexOf(INTERNAL_ARRAY_PREFIX);
        if (internalArrayMarker != -1 && name.endsWith(";")) {
            String elementClassName = null;
            if (internalArrayMarker == 0) {
                elementClassName = name
                    .substring(INTERNAL_ARRAY_PREFIX.length(), name.length() - 1);
            } else if (name.startsWith("[")) {
                elementClassName = name.substring(1);
            }
            Class<?> elementClass = forName(elementClassName, classLoader);
            return Array.newInstance(elementClass, 0).getClass();
        }

        ClassLoader classLoaderToUse = classLoader;
        if (classLoaderToUse == null) {
            classLoaderToUse = getClassLoader();
        }
        return classLoaderToUse.loadClass(name);
    }

    /**
     * Resolve primitive class name
     *
     * @param name name
     * @return the class
     * @since 1.0.0
     */
    public static Class<?> resolvePrimitiveClassName(String name) {
        Class<?> result = null;
        // Most class names will be quite long, considering that they
        // SHOULD sit in a package, so a length check is worthwhile.
        if (name != null && name.length() <= 8) {
            // Could be a primitive - likely.
            result = (Class<?>) PRIMITIVE_TYPE_NAME_MAP.get(name);
        }
        return result;
    }

    /**
     * To short string
     *
     * @param obj obj
     * @return the string
     * @since 1.0.0
     */
    public static String toShortString(Object obj) {
        if (obj == null) {
            return "null";
        }
        return obj.getClass().getSimpleName() + "@" + System.identityHashCode(obj);

    }

    /**
     * Simple class name
     *
     * @param clazz clazz
     * @return the string
     * @since 1.0.0
     */
    public static String simpleClassName(Class<?> clazz) {
        if (clazz == null) {
            throw new NullPointerException("clazz");
        }
        String className = clazz.getName();
        final int lastDotIdx = className.lastIndexOf(PACKAGE_SEPARATOR_CHAR);
        if (lastDotIdx > -1) {
            return className.substring(lastDotIdx + 1);
        }
        return className;
    }


    /**
     * Is primitive
     *
     * @param type type
     * @return the boolean
     * @since 1.0.0
     */
    public static boolean isPrimitive(Class<?> type) {
        return type.isPrimitive()
            || type == String.class
            || type == Character.class
            || type == Boolean.class
            || type == Byte.class
            || type == Short.class
            || type == Integer.class
            || type == Long.class
            || type == Float.class
            || type == Double.class
            || type == Object.class;
    }

    /**
     * Convert primitive
     *
     * @param type  type
     * @param value value
     * @return the object
     * @since 1.0.0
     */
    public static Object convertPrimitive(Class<?> type, String value) {
        if (value == null) {
            return null;
        } else if (type == char.class || type == Character.class) {
            return value.length() > 0 ? value.charAt(0) : '\0';
        } else if (type == boolean.class || type == Boolean.class) {
            return Boolean.valueOf(value);
        }
        try {
            if (type == byte.class || type == Byte.class) {
                return Byte.valueOf(value);
            } else if (type == short.class || type == Short.class) {
                return Short.valueOf(value);
            } else if (type == int.class || type == Integer.class) {
                return Integer.valueOf(value);
            } else if (type == long.class || type == Long.class) {
                return Long.valueOf(value);
            } else if (type == float.class || type == Float.class) {
                return Float.valueOf(value);
            } else if (type == double.class || type == Double.class) {
                return Double.valueOf(value);
            }
        } catch (NumberFormatException e) {
            return null;
        }
        return value;
    }


    /**
     * Is type match
     *
     * @param type  type
     * @param value value
     * @return the boolean
     * @since 1.0.0
     */
    public static boolean isTypeMatch(Class<?> type, String value) {
        if ((type == boolean.class || type == Boolean.class)
            && !("true".equals(value) || "false".equals(value))) {
            return false;
        }
        return true;
    }
}
