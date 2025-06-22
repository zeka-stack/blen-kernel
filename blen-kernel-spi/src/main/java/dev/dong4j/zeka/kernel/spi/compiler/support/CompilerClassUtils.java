package dev.dong4j.zeka.kernel.spi.compiler.support;

import dev.dong4j.zeka.kernel.spi.utils.SpiStringUtils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.8.0
 * @email "mailto:dong4j@gmaidl.com"
 * @date 2021.02.26 17:47
 * @since 1.8.0
 */
@SuppressWarnings("all")
public final class CompilerClassUtils {

    /** CLASS_EXTENSION */
    public static final String CLASS_EXTENSION = ".class";

    /** JAVA_EXTENSION */
    public static final String JAVA_EXTENSION = ".java";
    /** JIT_LIMIT */
    private static final int JIT_LIMIT = 5 * 1024;

    /**
     * Class utils
     *
     * @since 1.8.0
     */
    private CompilerClassUtils() {
    }

    /**
     * New instance
     *
     * @param name name
     * @return the object
     * @since 1.8.0
     */
    public static Object newInstance(String name) {
        try {
            return forName(name).newInstance();
        } catch (InstantiationException e) {
            throw new IllegalStateException(e.getMessage(), e);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    /**
     * For name
     *
     * @param packages  packages
     * @param className class name
     * @return the class
     * @since 1.8.0
     */
    public static Class<?> forName(String[] packages, String className) {
        try {
            return classForName(className);
        } catch (ClassNotFoundException e) {
            if (packages != null && packages.length > 0) {
                for (String pkg : packages) {
                    try {
                        return classForName(pkg + "." + className);
                    } catch (ClassNotFoundException e2) {
                    }
                }
            }
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    /**
     * For name
     *
     * @param className class name
     * @return the class
     * @since 1.8.0
     */
    public static Class<?> forName(String className) {
        try {
            return classForName(className);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    /**
     * Class for name
     *
     * @param className class name
     * @return the class
     * @throws ClassNotFoundException class not found exception
     * @since 1.8.0
     */
    public static Class<?> classForName(String className) throws ClassNotFoundException {
        switch (className) {
            case "boolean":
                return boolean.class;
            case "byte":
                return byte.class;
            case "char":
                return char.class;
            case "short":
                return short.class;
            case "int":
                return int.class;
            case "long":
                return long.class;
            case "float":
                return float.class;
            case "double":
                return double.class;
            case "boolean[]":
                return boolean[].class;
            case "byte[]":
                return byte[].class;
            case "char[]":
                return char[].class;
            case "short[]":
                return short[].class;
            case "int[]":
                return int[].class;
            case "long[]":
                return long[].class;
            case "float[]":
                return float[].class;
            case "double[]":
                return double[].class;
            default:
        }
        try {
            return arrayForName(className);
        } catch (ClassNotFoundException e) {
            // try to load from java.lang package
            if (className.indexOf('.') == -1) {
                try {
                    return arrayForName("java.lang." + className);
                } catch (ClassNotFoundException e2) {
                    // ignore, let the original exception be thrown
                }
            }
            throw e;
        }
    }

    /**
     * Array for name
     *
     * @param className class name
     * @return the class
     * @throws ClassNotFoundException class not found exception
     * @since 1.8.0
     */
    private static Class<?> arrayForName(String className) throws ClassNotFoundException {
        return Class.forName(className.endsWith("[]")
            ? "[L" + className.substring(0, className.length() - 2) + ";"
            : className, true, Thread.currentThread().getContextClassLoader());
    }

    /**
     * Gets boxed class *
     *
     * @param type type
     * @return the boxed class
     * @since 1.8.0
     */
    public static Class<?> getBoxedClass(Class<?> type) {
        if (type == boolean.class) {
            return Boolean.class;
        } else if (type == char.class) {
            return Character.class;
        } else if (type == byte.class) {
            return Byte.class;
        } else if (type == short.class) {
            return Short.class;
        } else if (type == int.class) {
            return Integer.class;
        } else if (type == long.class) {
            return Long.class;
        } else if (type == float.class) {
            return Float.class;
        } else if (type == double.class) {
            return Double.class;
        } else {
            return type;
        }
    }

    /**
     * Boxed
     *
     * @param v v
     * @return the boolean
     * @since 1.8.0
     */
    public static Boolean boxed(boolean v) {
        return Boolean.valueOf(v);
    }

    /**
     * Boxed
     *
     * @param v v
     * @return the character
     * @since 1.8.0
     */
    public static Character boxed(char v) {
        return Character.valueOf(v);
    }

    /**
     * Boxed
     *
     * @param v v
     * @return the byte
     * @since 1.8.0
     */
    public static Byte boxed(byte v) {
        return Byte.valueOf(v);
    }

    /**
     * Boxed
     *
     * @param v v
     * @return the short
     * @since 1.8.0
     */
    public static Short boxed(short v) {
        return Short.valueOf(v);
    }

    /**
     * Boxed
     *
     * @param v v
     * @return the integer
     * @since 1.8.0
     */
    public static Integer boxed(int v) {
        return Integer.valueOf(v);
    }

    /**
     * Boxed
     *
     * @param v v
     * @return the long
     * @since 1.8.0
     */
    public static Long boxed(long v) {
        return Long.valueOf(v);
    }

    /**
     * Boxed
     *
     * @param v v
     * @return the float
     * @since 1.8.0
     */
    public static Float boxed(float v) {
        return Float.valueOf(v);
    }

    /**
     * Boxed
     *
     * @param v v
     * @return the double
     * @since 1.8.0
     */
    public static Double boxed(double v) {
        return Double.valueOf(v);
    }

    /**
     * Boxed
     *
     * @param v v
     * @return the object
     * @since 1.8.0
     */
    public static Object boxed(Object v) {
        return v;
    }

    /**
     * Unboxed
     *
     * @param v v
     * @return the boolean
     * @since 1.8.0
     */
    public static boolean unboxed(Boolean v) {
        return v == null ? false : v.booleanValue();
    }

    /**
     * Unboxed
     *
     * @param v v
     * @return the char
     * @since 1.8.0
     */
    public static char unboxed(Character v) {
        return v == null ? '\0' : v.charValue();
    }

    /**
     * Unboxed
     *
     * @param v v
     * @return the byte
     * @since 1.8.0
     */
    public static byte unboxed(Byte v) {
        return v == null ? 0 : v.byteValue();
    }

    /**
     * Unboxed
     *
     * @param v v
     * @return the short
     * @since 1.8.0
     */
    public static short unboxed(Short v) {
        return v == null ? 0 : v.shortValue();
    }

    /**
     * Unboxed
     *
     * @param v v
     * @return the int
     * @since 1.8.0
     */
    public static int unboxed(Integer v) {
        return v == null ? 0 : v.intValue();
    }

    /**
     * Unboxed
     *
     * @param v v
     * @return the long
     * @since 1.8.0
     */
    public static long unboxed(Long v) {
        return v == null ? 0 : v.longValue();
    }

    /**
     * Unboxed
     *
     * @param v v
     * @return the float
     * @since 1.8.0
     */
    public static float unboxed(Float v) {
        return v == null ? 0 : v.floatValue();
    }

    /**
     * Unboxed
     *
     * @param v v
     * @return the double
     * @since 1.8.0
     */
    public static double unboxed(Double v) {
        return v == null ? 0 : v.doubleValue();
    }

    /**
     * Unboxed
     *
     * @param v v
     * @return the object
     * @since 1.8.0
     */
    public static Object unboxed(Object v) {
        return v;
    }

    /**
     * Is not empty
     *
     * @param object object
     * @return the boolean
     * @since 1.8.0
     */
    public static boolean isNotEmpty(Object object) {
        return getSize(object) > 0;
    }

    /**
     * Gets size *
     *
     * @param object object
     * @return the size
     * @since 1.8.0
     */
    public static int getSize(Object object) {
        if (object == null) {
            return 0;
        }
        if (object instanceof Collection<?>) {
            return ((Collection<?>) object).size();
        } else if (object instanceof Map<?, ?>) {
            return ((Map<?, ?>) object).size();
        } else if (object.getClass().isArray()) {
            return Array.getLength(object);
        } else {
            return -1;
        }
    }

    /**
     * To uri
     *
     * @param name name
     * @return the uri
     * @since 1.8.0
     */
    public static URI toURI(String name) {
        try {
            return new URI(name);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets generic class *
     *
     * @param cls cls
     * @return the generic class
     * @since 1.8.0
     */
    public static Class<?> getGenericClass(Class<?> cls) {
        return getGenericClass(cls, 0);
    }

    /**
     * Gets generic class *
     *
     * @param cls cls
     * @param i   i
     * @return the generic class
     * @since 1.8.0
     */
    public static Class<?> getGenericClass(Class<?> cls, int i) {
        try {
            ParameterizedType parameterizedType = ((ParameterizedType) cls.getGenericInterfaces()[0]);
            Object genericClass = parameterizedType.getActualTypeArguments()[i];
            if (genericClass instanceof ParameterizedType) {
                return (Class<?>) ((ParameterizedType) genericClass).getRawType();
            } else if (genericClass instanceof GenericArrayType) {
                Type type = ((GenericArrayType) genericClass).getGenericComponentType();
                if (type instanceof TypeVariable) {
                    return type.getClass();
                }
                return (((GenericArrayType) genericClass).getGenericComponentType() instanceof Class<?>)
                    ? (Class<?>) ((GenericArrayType) genericClass).getGenericComponentType()
                    : ((GenericArrayType) genericClass).getGenericComponentType().getClass();
            } else if (genericClass != null) {
                if (genericClass instanceof TypeVariable) {
                    return genericClass.getClass();
                }
                return (Class<?>) genericClass;
            }
        } catch (Throwable e) {

        }
        if (cls.getSuperclass() != null) {
            return getGenericClass(cls.getSuperclass(), i);
        } else {
            throw new IllegalArgumentException(cls.getName() + " generic type undefined!");
        }
    }

    /**
     * Is before java 5
     *
     * @param javaVersion java version
     * @return the boolean
     * @since 1.8.0
     */
    public static boolean isBeforeJava5(String javaVersion) {
        return (SpiStringUtils.isEmpty(javaVersion) || "1.0".equals(javaVersion)
            || "1.1".equals(javaVersion) || "1.2".equals(javaVersion)
            || "1.3".equals(javaVersion) || "1.4".equals(javaVersion));
    }

    /**
     * Is before java 6
     *
     * @param javaVersion java version
     * @return the boolean
     * @since 1.8.0
     */
    public static boolean isBeforeJava6(String javaVersion) {
        return isBeforeJava5(javaVersion) || "1.5".equals(javaVersion);
    }

    /**
     * To string
     *
     * @param e e
     * @return the string
     * @since 1.8.0
     */
    public static String toString(Throwable e) {
        StringWriter w = new StringWriter();
        PrintWriter p = new PrintWriter(w);
        p.print(e.getClass().getName() + ": ");
        if (e.getMessage() != null) {
            p.print(e.getMessage() + "\n");
        }
        p.println();
        try {
            return w.toString();
        } finally {
            p.close();
        }
    }

    /**
     * Check bytecode
     *
     * @param name     name
     * @param bytecode bytecode
     * @since 1.8.0
     */
    public static void checkBytecode(String name, byte[] bytecode) {
        if (bytecode.length > JIT_LIMIT) {
            System.err.println("The template bytecode too long, may be affect the JIT compiler. template class: " + name);
        }
    }

    /**
     * Gets size method *
     *
     * @param cls cls
     * @return the size method
     * @since 1.8.0
     */
    public static String getSizeMethod(Class<?> cls) {
        try {
            return cls.getMethod("size", new Class<?>[0]).getName() + "()";
        } catch (NoSuchMethodException e) {
            try {
                return cls.getMethod("length", new Class<?>[0]).getName() + "()";
            } catch (NoSuchMethodException e2) {
                try {
                    return cls.getMethod("getSize", new Class<?>[0]).getName() + "()";
                } catch (NoSuchMethodException e3) {
                    try {
                        return cls.getMethod("getLength", new Class<?>[0]).getName() + "()";
                    } catch (NoSuchMethodException e4) {
                        return null;
                    }
                }
            }
        }
    }

    /**
     * Gets method name *
     *
     * @param method           method
     * @param parameterClasses parameter classes
     * @param rightCode        right code
     * @return the method name
     * @since 1.8.0
     */
    public static String getMethodName(Method method, Class<?>[] parameterClasses, String rightCode) {
        if (method.getParameterTypes().length > parameterClasses.length) {
            Class<?>[] types = method.getParameterTypes();
            StringBuilder buf = new StringBuilder(rightCode);
            for (int i = parameterClasses.length; i < types.length; i++) {
                if (buf.length() > 0) {
                    buf.append(",");
                }
                Class<?> type = types[i];
                String def;
                if (type == boolean.class) {
                    def = "false";
                } else if (type == char.class) {
                    def = "\'\\0\'";
                } else if (type == byte.class
                    || type == short.class
                    || type == int.class
                    || type == long.class
                    || type == float.class
                    || type == double.class) {
                    def = "0";
                } else {
                    def = "null";
                }
                buf.append(def);
            }
        }
        return method.getName() + "(" + rightCode + ")";
    }

    /**
     * Search method
     *
     * @param currentClass   current class
     * @param name           name
     * @param parameterTypes parameter types
     * @return the method
     * @throws NoSuchMethodException no such method exception
     * @since 1.8.0
     */
    public static Method searchMethod(Class<?> currentClass, String name, Class<?>[] parameterTypes) throws NoSuchMethodException {
        if (currentClass == null) {
            throw new NoSuchMethodException("class == null");
        }
        try {
            return currentClass.getMethod(name, parameterTypes);
        } catch (NoSuchMethodException e) {
            for (Method method : currentClass.getMethods()) {
                if (method.getName().equals(name)
                    && parameterTypes.length == method.getParameterTypes().length
                    && Modifier.isPublic(method.getModifiers())) {
                    if (parameterTypes.length > 0) {
                        Class<?>[] types = method.getParameterTypes();
                        boolean match = true;
                        for (int i = 0; i < parameterTypes.length; i++) {
                            if (!types[i].isAssignableFrom(parameterTypes[i])) {
                                match = false;
                                break;
                            }
                        }
                        if (!match) {
                            continue;
                        }
                    }
                    return method;
                }
            }
            throw e;
        }
    }

    /**
     * Gets init code *
     *
     * @param type type
     * @return the init code
     * @since 1.8.0
     */
    public static String getInitCode(Class<?> type) {
        if (byte.class.equals(type)
            || short.class.equals(type)
            || int.class.equals(type)
            || long.class.equals(type)
            || float.class.equals(type)
            || double.class.equals(type)) {
            return "0";
        } else if (char.class.equals(type)) {
            return "'\\0'";
        } else if (boolean.class.equals(type)) {
            return "false";
        } else {
            return "null";
        }
    }

    /**
     * To map
     *
     * @param <K>     parameter
     * @param <V>     parameter
     * @param entries entries
     * @return the map
     * @since 1.8.0
     */
    public static <K, V> Map<K, V> toMap(Map.Entry<K, V>[] entries) {
        Map<K, V> map = new HashMap<K, V>(16);
        if (entries != null && entries.length > 0) {
            for (Map.Entry<K, V> entry : entries) {
                map.put(entry.getKey(), entry.getValue());
            }
        }
        return map;
    }

    /**
     * Gets simple class name *
     *
     * @param qualifiedName qualified name
     * @return the simple class name
     * @since 1.8.0
     */
    public static String getSimpleClassName(String qualifiedName) {
        if (null == qualifiedName) {
            return null;
        }

        int i = qualifiedName.lastIndexOf('.');
        return i < 0 ? qualifiedName : qualifiedName.substring(i + 1);
    }

}
