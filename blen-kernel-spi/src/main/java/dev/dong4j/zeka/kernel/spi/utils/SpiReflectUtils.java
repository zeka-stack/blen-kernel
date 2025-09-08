package dev.dong4j.zeka.kernel.spi.utils;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtMethod;
import javassist.NotFoundException;

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
public final class SpiReflectUtils {

    /** JVM_VOID */
    public static final char JVM_VOID = 'V';

    /** JVM_BOOLEAN */
    public static final char JVM_BOOLEAN = 'Z';

    /** JVM_BYTE */
    public static final char JVM_BYTE = 'B';

    /** JVM_CHAR */
    public static final char JVM_CHAR = 'C';

    /** JVM_DOUBLE */
    public static final char JVM_DOUBLE = 'D';

    /** JVM_FLOAT */
    public static final char JVM_FLOAT = 'F';

    /** JVM_INT */
    public static final char JVM_INT = 'I';

    /** JVM_LONG */
    public static final char JVM_LONG = 'J';

    /** JVM_SHORT */
    public static final char JVM_SHORT = 'S';

    /** EMPTY_CLASS_ARRAY */
    public static final Class<?>[] EMPTY_CLASS_ARRAY = new Class<?>[0];

    /** JAVA_IDENT_REGEX */
    public static final String JAVA_IDENT_REGEX = "(?:[_$a-zA-Z][_$a-zA-Z0-9]*)";

    /** JAVA_NAME_REGEX */
    public static final String JAVA_NAME_REGEX = "(?:" + JAVA_IDENT_REGEX + "(?:\\." + JAVA_IDENT_REGEX + ")*)";

    /** CLASS_DESC */
    public static final String CLASS_DESC = "(?:L" + JAVA_IDENT_REGEX + "(?:\\/" + JAVA_IDENT_REGEX + ")*;)";

    /** ARRAY_DESC */
    public static final String ARRAY_DESC = "(?:\\[+(?:(?:[VZBCDFIJS])|" + CLASS_DESC + "))";

    /** DESC_REGEX */
    public static final String DESC_REGEX = "(?:(?:[VZBCDFIJS])|" + CLASS_DESC + "|" + ARRAY_DESC + ")";

    /** DESC_PATTERN */
    public static final Pattern DESC_PATTERN = Pattern.compile(DESC_REGEX);

    /** METHOD_DESC_REGEX */
    public static final String METHOD_DESC_REGEX = "(?:(" + JAVA_IDENT_REGEX + ")?\\((" + DESC_REGEX + "*)\\)(" + DESC_REGEX + ")?)";

    /** METHOD_DESC_PATTERN */
    public static final Pattern METHOD_DESC_PATTERN = Pattern.compile(METHOD_DESC_REGEX);

    /** GETTER_METHOD_DESC_PATTERN */
    public static final Pattern GETTER_METHOD_DESC_PATTERN = Pattern.compile("get([A-Z][_a-zA-Z0-9]*)\\(\\)(" + DESC_REGEX + ")");

    /** SETTER_METHOD_DESC_PATTERN */
    public static final Pattern SETTER_METHOD_DESC_PATTERN = Pattern.compile("set([A-Z][_a-zA-Z0-9]*)\\((" + DESC_REGEX + ")\\)V");

    /** IS_HAS_CAN_METHOD_DESC_PATTERN */
    public static final Pattern IS_HAS_CAN_METHOD_DESC_PATTERN = Pattern.compile("(?:is|has|can)([A-Z][_a-zA-Z0-9]*)\\(\\)Z");

    /** DESC_CLASS_CACHE */
    private static final ConcurrentMap<String, Class<?>> DESC_CLASS_CACHE = new ConcurrentHashMap<String, Class<?>>();

    /** NAME_CLASS_CACHE */
    private static final ConcurrentMap<String, Class<?>> NAME_CLASS_CACHE = new ConcurrentHashMap<String, Class<?>>();

    /** Signature_METHODS_CACHE */
    private static final ConcurrentMap<String, Method> Signature_METHODS_CACHE = new ConcurrentHashMap<>();

    /**
     * Reflect utils
     *
     * @since 1.0.0
     */
    private SpiReflectUtils() {
    }

    /**
     * Is primitives
     *
     * @param cls cls
     * @return the boolean
     * @since 1.0.0
     */
    public static boolean isPrimitives(Class<?> cls) {
        if (cls.isArray()) {
            return isPrimitive(cls.getComponentType());
        }
        return isPrimitive(cls);
    }

    /**
     * Is primitive
     *
     * @param cls cls
     * @return the boolean
     * @since 1.0.0
     */
    public static boolean isPrimitive(Class<?> cls) {
        return cls.isPrimitive() || cls == String.class || cls == Boolean.class || cls == Character.class
            || Number.class.isAssignableFrom(cls) || Date.class.isAssignableFrom(cls);
    }

    /**
     * Gets boxed class *
     *
     * @param c c
     * @return the boxed class
     * @since 1.0.0
     */
    public static Class<?> getBoxedClass(Class<?> c) {
        if (c == int.class) {
            c = Integer.class;
        } else if (c == boolean.class) {
            c = Boolean.class;
        } else if (c == long.class) {
            c = Long.class;
        } else if (c == float.class) {
            c = Float.class;
        } else if (c == double.class) {
            c = Double.class;
        } else if (c == char.class) {
            c = Character.class;
        } else if (c == byte.class) {
            c = Byte.class;
        } else if (c == short.class) {
            c = Short.class;
        }
        return c;
    }

    /**
     * Is compatible
     *
     * @param c c
     * @param o o
     * @return the boolean
     * @since 1.0.0
     */
    public static boolean isCompatible(Class<?> c, Object o) {
        boolean pt = c.isPrimitive();
        if (o == null) {
            return !pt;
        }

        if (pt) {
            c = getBoxedClass(c);
        }

        return c == o.getClass() || c.isInstance(o);
    }

    /**
     * Is compatible
     *
     * @param cs cs
     * @param os os
     * @return the boolean
     * @since 1.0.0
     */
    public static boolean isCompatible(Class<?>[] cs, Object[] os) {
        int len = cs.length;
        if (len != os.length) {
            return false;
        }
        if (len == 0) {
            return true;
        }
        for (int i = 0; i < len; i++) {
            if (!isCompatible(cs[i], os[i])) {
                return false;
            }
        }
        return true;
    }

    /**
     * Gets code base *
     *
     * @param cls cls
     * @return the code base
     * @since 1.0.0
     */
    public static String getCodeBase(Class<?> cls) {
        if (cls == null) {
            return null;
        }
        ProtectionDomain domain = cls.getProtectionDomain();
        if (domain == null) {
            return null;
        }
        CodeSource source = domain.getCodeSource();
        if (source == null) {
            return null;
        }
        URL location = source.getLocation();
        if (location == null) {
            return null;
        }
        return location.getFile();
    }

    /**
     * Gets name *
     *
     * @param c c
     * @return the name
     * @since 1.0.0
     */
    public static String getName(Class<?> c) {
        if (c.isArray()) {
            StringBuilder sb = new StringBuilder();
            do {
                sb.append("[]");
                c = c.getComponentType();
            }
            while (c.isArray());

            return c.getName() + sb.toString();
        }
        return c.getName();
    }

    /**
     * Gets generic class *
     *
     * @param cls cls
     * @return the generic class
     * @since 1.0.0
     */
    public static Class<?> getGenericClass(Class<?> cls) {
        return getGenericClass(cls, 0);
    }

    /**
     * Gets generic class *
     *
     * @param cls cls
     * @param i
     * @return the generic class
     * @since 1.0.0
     */
    public static Class<?> getGenericClass(Class<?> cls, int i) {
        try {
            ParameterizedType parameterizedType = ((ParameterizedType) cls.getGenericInterfaces()[0]);
            Object genericClass = parameterizedType.getActualTypeArguments()[i];

            // handle nested generic type
            if (genericClass instanceof ParameterizedType) {
                return (Class<?>) ((ParameterizedType) genericClass).getRawType();
            }

            // handle array generic type
            if (genericClass instanceof GenericArrayType) {
                return (Class<?>) ((GenericArrayType) genericClass).getGenericComponentType();
            }

            // Requires JDK 7 or higher, Foo<int[]> is no longer GenericArrayType
            if (((Class) genericClass).isArray()) {
                return ((Class) genericClass).getComponentType();
            }
            return (Class<?>) genericClass;
        } catch (Throwable e) {
            throw new IllegalArgumentException(cls.getName() + " generic type undefined!", e);
        }
    }

    /**
     * Gets name *
     *
     * @param m m
     * @return the name
     * @since 1.0.0
     */
    public static String getName(Method m) {
        StringBuilder ret = new StringBuilder();
        ret.append(getName(m.getReturnType())).append(' ');
        ret.append(m.getName()).append('(');
        Class<?>[] parameterTypes = m.getParameterTypes();
        for (int i = 0; i < parameterTypes.length; i++) {
            if (i > 0) {
                ret.append(',');
            }
            ret.append(getName(parameterTypes[i]));
        }
        ret.append(')');
        return ret.toString();
    }

    /**
     * Gets signature *
     *
     * @param methodName     method name
     * @param parameterTypes parameter types
     * @return the signature
     * @since 1.0.0
     */
    public static String getSignature(String methodName, Class<?>[] parameterTypes) {
        StringBuilder sb = new StringBuilder(methodName);
        sb.append("(");
        if (parameterTypes != null && parameterTypes.length > 0) {
            boolean first = true;
            for (Class<?> type : parameterTypes) {
                if (first) {
                    first = false;
                } else {
                    sb.append(",");
                }
                sb.append(type.getName());
            }
        }
        sb.append(")");
        return sb.toString();
    }

    /**
     * Gets name *
     *
     * @param c c
     * @return the name
     * @since 1.0.0
     */
    public static String getName(Constructor<?> c) {
        StringBuilder ret = new StringBuilder("(");
        Class<?>[] parameterTypes = c.getParameterTypes();
        for (int i = 0; i < parameterTypes.length; i++) {
            if (i > 0) {
                ret.append(',');
            }
            ret.append(getName(parameterTypes[i]));
        }
        ret.append(')');
        return ret.toString();
    }

    /**
     * Gets desc *
     *
     * @param c c
     * @return the desc
     * @since 1.0.0
     */
    public static String getDesc(Class<?> c) {
        StringBuilder ret = new StringBuilder();

        while (c.isArray()) {
            ret.append('[');
            c = c.getComponentType();
        }

        if (c.isPrimitive()) {
            String t = c.getName();
            if ("void".equals(t)) {
                ret.append(JVM_VOID);
            } else if ("boolean".equals(t)) {
                ret.append(JVM_BOOLEAN);
            } else if ("byte".equals(t)) {
                ret.append(JVM_BYTE);
            } else if ("char".equals(t)) {
                ret.append(JVM_CHAR);
            } else if ("double".equals(t)) {
                ret.append(JVM_DOUBLE);
            } else if ("float".equals(t)) {
                ret.append(JVM_FLOAT);
            } else if ("int".equals(t)) {
                ret.append(JVM_INT);
            } else if ("long".equals(t)) {
                ret.append(JVM_LONG);
            } else if ("short".equals(t)) {
                ret.append(JVM_SHORT);
            }
        } else {
            ret.append('L');
            ret.append(c.getName().replace('.', '/'));
            ret.append(';');
        }
        return ret.toString();
    }

    /**
     * Gets desc *
     *
     * @param cs cs
     * @return the desc
     * @since 1.0.0
     */
    public static String getDesc(Class<?>[] cs) {
        if (cs.length == 0) {
            return "";
        }

        StringBuilder sb = new StringBuilder(64);
        for (Class<?> c : cs) {
            sb.append(getDesc(c));
        }
        return sb.toString();
    }

    /**
     * Gets desc *
     *
     * @param m m
     * @return the desc
     * @since 1.0.0
     */
    public static String getDesc(Method m) {
        StringBuilder ret = new StringBuilder(m.getName()).append('(');
        Class<?>[] parameterTypes = m.getParameterTypes();
        for (int i = 0; i < parameterTypes.length; i++) {
            ret.append(getDesc(parameterTypes[i]));
        }
        ret.append(')').append(getDesc(m.getReturnType()));
        return ret.toString();
    }

    /**
     * Gets desc *
     *
     * @param c c
     * @return the desc
     * @since 1.0.0
     */
    public static String getDesc(Constructor<?> c) {
        StringBuilder ret = new StringBuilder("(");
        Class<?>[] parameterTypes = c.getParameterTypes();
        for (int i = 0; i < parameterTypes.length; i++) {
            ret.append(getDesc(parameterTypes[i]));
        }
        ret.append(')').append('V');
        return ret.toString();
    }

    /**
     * Gets desc without method name *
     *
     * @param m m
     * @return the desc without method name
     * @since 1.0.0
     */
    public static String getDescWithoutMethodName(Method m) {
        StringBuilder ret = new StringBuilder();
        ret.append('(');
        Class<?>[] parameterTypes = m.getParameterTypes();
        for (int i = 0; i < parameterTypes.length; i++) {
            ret.append(getDesc(parameterTypes[i]));
        }
        ret.append(')').append(getDesc(m.getReturnType()));
        return ret.toString();
    }

    /**
     * Gets desc *
     *
     * @param c c
     * @return the desc
     * @throws NotFoundException not found exception
     * @since 1.0.0
     */
    public static String getDesc(CtClass c) throws NotFoundException {
        StringBuilder ret = new StringBuilder();
        if (c.isArray()) {
            ret.append('[');
            ret.append(getDesc(c.getComponentType()));
        } else if (c.isPrimitive()) {
            String t = c.getName();
            if ("void".equals(t)) {
                ret.append(JVM_VOID);
            } else if ("boolean".equals(t)) {
                ret.append(JVM_BOOLEAN);
            } else if ("byte".equals(t)) {
                ret.append(JVM_BYTE);
            } else if ("char".equals(t)) {
                ret.append(JVM_CHAR);
            } else if ("double".equals(t)) {
                ret.append(JVM_DOUBLE);
            } else if ("float".equals(t)) {
                ret.append(JVM_FLOAT);
            } else if ("int".equals(t)) {
                ret.append(JVM_INT);
            } else if ("long".equals(t)) {
                ret.append(JVM_LONG);
            } else if ("short".equals(t)) {
                ret.append(JVM_SHORT);
            }
        } else {
            ret.append('L');
            ret.append(c.getName().replace('.', '/'));
            ret.append(';');
        }
        return ret.toString();
    }

    /**
     * Gets desc *
     *
     * @param m m
     * @return the desc
     * @throws NotFoundException not found exception
     * @since 1.0.0
     */
    public static String getDesc(CtMethod m) throws NotFoundException {
        StringBuilder ret = new StringBuilder(m.getName()).append('(');
        CtClass[] parameterTypes = m.getParameterTypes();
        for (CtClass parameterType : parameterTypes) {
            ret.append(getDesc(parameterType));
        }
        ret.append(')').append(getDesc(m.getReturnType()));
        return ret.toString();
    }

    /**
     * Gets desc *
     *
     * @param c c
     * @return the desc
     * @throws NotFoundException not found exception
     * @since 1.0.0
     */
    public static String getDesc(CtConstructor c) throws NotFoundException {
        StringBuilder ret = new StringBuilder("(");
        CtClass[] parameterTypes = c.getParameterTypes();
        for (int i = 0; i < parameterTypes.length; i++) {
            ret.append(getDesc(parameterTypes[i]));
        }
        ret.append(')').append('V');
        return ret.toString();
    }

    /**
     * Gets desc without method name *
     *
     * @param m m
     * @return the desc without method name
     * @throws NotFoundException not found exception
     * @since 1.0.0
     */
    public static String getDescWithoutMethodName(CtMethod m) throws NotFoundException {
        StringBuilder ret = new StringBuilder();
        ret.append('(');
        CtClass[] parameterTypes = m.getParameterTypes();
        for (int i = 0; i < parameterTypes.length; i++) {
            ret.append(getDesc(parameterTypes[i]));
        }
        ret.append(')').append(getDesc(m.getReturnType()));
        return ret.toString();
    }

    /**
     * Name 2 desc
     *
     * @param name name
     * @return the string
     * @since 1.0.0
     */
    public static String name2desc(String name) {
        StringBuilder sb = new StringBuilder();
        int c = 0, index = name.indexOf('[');
        if (index > 0) {
            c = (name.length() - index) / 2;
            name = name.substring(0, index);
        }
        while (c-- > 0) {
            sb.append("[");
        }
        if ("void".equals(name)) {
            sb.append(JVM_VOID);
        } else if ("boolean".equals(name)) {
            sb.append(JVM_BOOLEAN);
        } else if ("byte".equals(name)) {
            sb.append(JVM_BYTE);
        } else if ("char".equals(name)) {
            sb.append(JVM_CHAR);
        } else if ("double".equals(name)) {
            sb.append(JVM_DOUBLE);
        } else if ("float".equals(name)) {
            sb.append(JVM_FLOAT);
        } else if ("int".equals(name)) {
            sb.append(JVM_INT);
        } else if ("long".equals(name)) {
            sb.append(JVM_LONG);
        } else if ("short".equals(name)) {
            sb.append(JVM_SHORT);
        } else {
            sb.append('L').append(name.replace('.', '/')).append(';');
        }
        return sb.toString();
    }

    /**
     * Desc 2 name
     *
     * @param desc desc
     * @return the string
     * @since 1.0.0
     */
    public static String desc2name(String desc) {
        StringBuilder sb = new StringBuilder();
        int c = desc.lastIndexOf('[') + 1;
        if (desc.length() == c + 1) {
            switch (desc.charAt(c)) {
                case JVM_VOID: {
                    sb.append("void");
                    break;
                }
                case JVM_BOOLEAN: {
                    sb.append("boolean");
                    break;
                }
                case JVM_BYTE: {
                    sb.append("byte");
                    break;
                }
                case JVM_CHAR: {
                    sb.append("char");
                    break;
                }
                case JVM_DOUBLE: {
                    sb.append("double");
                    break;
                }
                case JVM_FLOAT: {
                    sb.append("float");
                    break;
                }
                case JVM_INT: {
                    sb.append("int");
                    break;
                }
                case JVM_LONG: {
                    sb.append("long");
                    break;
                }
                case JVM_SHORT: {
                    sb.append("short");
                    break;
                }
                default:
                    throw new RuntimeException();
            }
        } else {
            sb.append(desc.substring(c + 1, desc.length() - 1).replace('/', '.'));
        }
        while (c-- > 0) {
            sb.append("[]");
        }
        return sb.toString();
    }

    /**
     * For name
     *
     * @param name name
     * @return the class
     * @since 1.0.0
     */
    public static Class<?> forName(String name) {
        try {
            return name2class(name);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Not found class " + name + ", cause: " + e.getMessage(), e);
        }
    }

    /**
     * For name
     *
     * @param cl   cl
     * @param name name
     * @return the class
     * @since 1.0.0
     */
    public static Class<?> forName(ClassLoader cl, String name) {
        try {
            return name2class(cl, name);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Not found class " + name + ", cause: " + e.getMessage(), e);
        }
    }

    /**
     * Name 2 class
     *
     * @param name name
     * @return the class
     * @throws ClassNotFoundException class not found exception
     * @since 1.0.0
     */
    public static Class<?> name2class(String name) throws ClassNotFoundException {
        return name2class(SpiClassUtils.getClassLoader(), name);
    }

    /**
     * Name 2 class
     *
     * @param cl   cl
     * @param name name
     * @return the class
     * @throws ClassNotFoundException class not found exception
     * @since 1.0.0
     */
    private static Class<?> name2class(ClassLoader cl, String name) throws ClassNotFoundException {
        int c = 0, index = name.indexOf('[');
        if (index > 0) {
            c = (name.length() - index) / 2;
            name = name.substring(0, index);
        }
        if (c > 0) {
            StringBuilder sb = new StringBuilder();
            while (c-- > 0) {
                sb.append("[");
            }

            if ("void".equals(name)) {
                sb.append(JVM_VOID);
            } else if ("boolean".equals(name)) {
                sb.append(JVM_BOOLEAN);
            } else if ("byte".equals(name)) {
                sb.append(JVM_BYTE);
            } else if ("char".equals(name)) {
                sb.append(JVM_CHAR);
            } else if ("double".equals(name)) {
                sb.append(JVM_DOUBLE);
            } else if ("float".equals(name)) {
                sb.append(JVM_FLOAT);
            } else if ("int".equals(name)) {
                sb.append(JVM_INT);
            } else if ("long".equals(name)) {
                sb.append(JVM_LONG);
            } else if ("short".equals(name)) {
                sb.append(JVM_SHORT);
            } else {
                // "java.lang.Object" ==> "Ljava.lang.Object;"
                sb.append('L').append(name).append(';');
            }
            name = sb.toString();
        } else {
            if ("void".equals(name)) {
                return void.class;
            }
            if ("boolean".equals(name)) {
                return boolean.class;
            }
            if ("byte".equals(name)) {
                return byte.class;
            }
            if ("char".equals(name)) {
                return char.class;
            }
            if ("double".equals(name)) {
                return double.class;
            }
            if ("float".equals(name)) {
                return float.class;
            }
            if ("int".equals(name)) {
                return int.class;
            }
            if ("long".equals(name)) {
                return long.class;
            }
            if ("short".equals(name)) {
                return short.class;
            }
        }

        if (cl == null) {
            cl = SpiClassUtils.getClassLoader();
        }
        Class<?> clazz = NAME_CLASS_CACHE.get(name);
        if (clazz == null) {
            clazz = Class.forName(name, true, cl);
            NAME_CLASS_CACHE.put(name, clazz);
        }
        return clazz;
    }

    /**
     * Desc 2 class
     *
     * @param desc desc
     * @return the class
     * @throws ClassNotFoundException class not found exception
     * @since 1.0.0
     */
    public static Class<?> desc2class(String desc) throws ClassNotFoundException {
        return desc2class(SpiClassUtils.getClassLoader(), desc);
    }

    /**
     * Desc 2 class
     *
     * @param cl   cl
     * @param desc desc
     * @return the class
     * @throws ClassNotFoundException class not found exception
     * @since 1.0.0
     */
    private static Class<?> desc2class(ClassLoader cl, String desc) throws ClassNotFoundException {
        switch (desc.charAt(0)) {
            case JVM_VOID:
                return void.class;
            case JVM_BOOLEAN:
                return boolean.class;
            case JVM_BYTE:
                return byte.class;
            case JVM_CHAR:
                return char.class;
            case JVM_DOUBLE:
                return double.class;
            case JVM_FLOAT:
                return float.class;
            case JVM_INT:
                return int.class;
            case JVM_LONG:
                return long.class;
            case JVM_SHORT:
                return short.class;
            case 'L':
                // "Ljava/lang/Object;" ==> "java.lang.Object"
                desc = desc.substring(1, desc.length() - 1).replace('/', '.');
                break;
            case '[':
                // "[[Ljava/lang/Object;" ==> "[[Ljava.lang.Object;"
                desc = desc.replace('/', '.');
                break;
            default:
                throw new ClassNotFoundException("Class not found: " + desc);
        }

        if (cl == null) {
            cl = SpiClassUtils.getClassLoader();
        }
        Class<?> clazz = DESC_CLASS_CACHE.get(desc);
        if (clazz == null) {
            clazz = Class.forName(desc, true, cl);
            DESC_CLASS_CACHE.put(desc, clazz);
        }
        return clazz;
    }

    /**
     * Desc 2 class array
     *
     * @param desc desc
     * @return the class [ ]
     * @throws ClassNotFoundException class not found exception
     * @since 1.0.0
     */
    public static Class<?>[] desc2classArray(String desc) throws ClassNotFoundException {
        Class<?>[] ret = desc2classArray(SpiClassUtils.getClassLoader(), desc);
        return ret;
    }

    /**
     * Desc 2 class array
     *
     * @param cl   cl
     * @param desc desc
     * @return the class [ ]
     * @throws ClassNotFoundException class not found exception
     * @since 1.0.0
     */
    private static Class<?>[] desc2classArray(ClassLoader cl, String desc) throws ClassNotFoundException {
        if (desc.length() == 0) {
            return EMPTY_CLASS_ARRAY;
        }

        List<Class<?>> cs = new ArrayList<Class<?>>();
        Matcher m = DESC_PATTERN.matcher(desc);
        while (m.find()) {
            cs.add(desc2class(cl, m.group()));
        }
        return cs.toArray(EMPTY_CLASS_ARRAY);
    }

    /**
     * Find method by method signature
     *
     * @param clazz          clazz
     * @param methodName     method name
     * @param parameterTypes parameter types
     * @return the method
     * @throws NoSuchMethodException  no such method exception
     * @throws ClassNotFoundException class not found exception
     * @since 1.0.0
     */
    public static Method findMethodByMethodSignature(Class<?> clazz, String methodName, String[] parameterTypes)
        throws NoSuchMethodException, ClassNotFoundException {
        String signature = clazz.getName() + "." + methodName;
        if (parameterTypes != null && parameterTypes.length > 0) {
            signature += SpiStringUtils.join(parameterTypes);
        }
        Method method = Signature_METHODS_CACHE.get(signature);
        if (method != null) {
            return method;
        }
        if (parameterTypes == null) {
            List<Method> finded = new ArrayList<Method>();
            for (Method m : clazz.getMethods()) {
                if (m.getName().equals(methodName)) {
                    finded.add(m);
                }
            }
            if (finded.isEmpty()) {
                throw new NoSuchMethodException("No such method " + methodName + " in class " + clazz);
            }
            if (finded.size() > 1) {
                String msg = String.format("Not unique method for method name(%s) in class(%s), find %d methods.",
                    methodName, clazz.getName(), finded.size());
                throw new IllegalStateException(msg);
            }
            method = finded.get(0);
        } else {
            Class<?>[] types = new Class<?>[parameterTypes.length];
            for (int i = 0; i < parameterTypes.length; i++) {
                types[i] = SpiReflectUtils.name2class(parameterTypes[i]);
            }
            method = clazz.getMethod(methodName, types);

        }
        Signature_METHODS_CACHE.put(signature, method);
        return method;
    }

    /**
     * Find method by method name
     *
     * @param clazz      clazz
     * @param methodName method name
     * @return the method
     * @throws NoSuchMethodException  no such method exception
     * @throws ClassNotFoundException class not found exception
     * @since 1.0.0
     */
    public static Method findMethodByMethodName(Class<?> clazz, String methodName)
        throws NoSuchMethodException, ClassNotFoundException {
        return findMethodByMethodSignature(clazz, methodName, null);
    }

    /**
     * Find constructor
     *
     * @param clazz     clazz
     * @param paramType param type
     * @return the constructor
     * @throws NoSuchMethodException no such method exception
     * @since 1.0.0
     */
    public static Constructor<?> findConstructor(Class<?> clazz, Class<?> paramType) throws NoSuchMethodException {
        Constructor<?> targetConstructor;
        try {
            targetConstructor = clazz.getConstructor(new Class<?>[]{paramType});
        } catch (NoSuchMethodException e) {
            targetConstructor = null;
            Constructor<?>[] constructors = clazz.getConstructors();
            for (Constructor<?> constructor : constructors) {
                if (Modifier.isPublic(constructor.getModifiers())
                    && constructor.getParameterTypes().length == 1
                    && constructor.getParameterTypes()[0].isAssignableFrom(paramType)) {
                    targetConstructor = constructor;
                    break;
                }
            }
            if (targetConstructor == null) {
                throw e;
            }
        }
        return targetConstructor;
    }

    /**
     * Is instance
     *
     * @param obj                obj
     * @param interfaceClazzName interface clazz name
     * @return the boolean
     * @since 1.0.0
     */
    public static boolean isInstance(Object obj, String interfaceClazzName) {
        for (Class<?> clazz = obj.getClass();
             clazz != null && !clazz.equals(Object.class);
             clazz = clazz.getSuperclass()) {
            Class<?>[] interfaces = clazz.getInterfaces();
            for (Class<?> itf : interfaces) {
                if (itf.getName().equals(interfaceClazzName)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Gets empty object *
     *
     * @param returnType return type
     * @return the empty object
     * @since 1.0.0
     */
    public static Object getEmptyObject(Class<?> returnType) {
        return getEmptyObject(returnType, new HashMap<>(), 0);
    }

    /**
     * Gets empty object *
     *
     * @param returnType     return type
     * @param emptyInstances empty instances
     * @param level          level
     * @return the empty object
     * @since 1.0.0
     */
    private static Object getEmptyObject(Class<?> returnType, Map<Class<?>, Object> emptyInstances, int level) {
        if (level > 2) {
            return null;
        }
        if (returnType == null) {
            return null;
        }
        if (returnType == boolean.class || returnType == Boolean.class) {
            return false;
        }
        if (returnType == char.class || returnType == Character.class) {
            return '\0';
        }
        if (returnType == byte.class || returnType == Byte.class) {
            return (byte) 0;
        }
        if (returnType == short.class || returnType == Short.class) {
            return (short) 0;
        }
        if (returnType == int.class || returnType == Integer.class) {
            return 0;
        }
        if (returnType == long.class || returnType == Long.class) {
            return 0L;
        }
        if (returnType == float.class || returnType == Float.class) {
            return 0F;
        }
        if (returnType == double.class || returnType == Double.class) {
            return 0D;
        }
        if (returnType.isArray()) {
            return Array.newInstance(returnType.getComponentType(), 0);
        }
        if (returnType.isAssignableFrom(ArrayList.class)) {
            return Collections.emptyList();
        }
        if (returnType.isAssignableFrom(HashSet.class)) {
            return Collections.emptySet();
        }
        if (returnType.isAssignableFrom(HashMap.class)) {
            return Collections.emptyMap();
        }
        if (String.class.equals(returnType)) {
            return "";
        }
        if (returnType.isInterface()) {
            return null;
        }

        try {
            Object value = emptyInstances.get(returnType);
            if (value == null) {
                value = returnType.newInstance();
                emptyInstances.put(returnType, value);
            }
            Class<?> cls = value.getClass();
            while (cls != null && cls != Object.class) {
                Field[] fields = cls.getDeclaredFields();
                for (Field field : fields) {
                    if (field.isSynthetic()) {
                        continue;
                    }
                    Object property = getEmptyObject(field.getType(), emptyInstances, level + 1);
                    if (property != null) {
                        try {
                            if (!field.isAccessible()) {
                                field.setAccessible(true);
                            }
                            field.set(value, property);
                        } catch (Throwable ignored) {
                        }
                    }
                }
                cls = cls.getSuperclass();
            }
            return value;
        } catch (Throwable e) {
            return null;
        }
    }

    /**
     * Is bean property read method
     *
     * @param method method
     * @return the boolean
     * @since 1.0.0
     */
    public static boolean isBeanPropertyReadMethod(Method method) {
        return method != null
            && Modifier.isPublic(method.getModifiers())
            && !Modifier.isStatic(method.getModifiers())
            && method.getReturnType() != void.class
            && method.getDeclaringClass() != Object.class
            && method.getParameterTypes().length == 0
            && ((method.getName().startsWith("get") && method.getName().length() > 3)
            || (method.getName().startsWith("is") && method.getName().length() > 2));
    }

    /**
     * Gets property name from bean read method *
     *
     * @param method method
     * @return the property name from bean read method
     * @since 1.0.0
     */
    public static String getPropertyNameFromBeanReadMethod(Method method) {
        if (isBeanPropertyReadMethod(method)) {
            if (method.getName().startsWith("get")) {
                return method.getName().substring(3, 4).toLowerCase()
                    + method.getName().substring(4);
            }
            if (method.getName().startsWith("is")) {
                return method.getName().substring(2, 3).toLowerCase()
                    + method.getName().substring(3);
            }
        }
        return null;
    }

    /**
     * Is bean property write method
     *
     * @param method method
     * @return the boolean
     * @since 1.0.0
     */
    public static boolean isBeanPropertyWriteMethod(Method method) {
        return method != null
            && Modifier.isPublic(method.getModifiers())
            && !Modifier.isStatic(method.getModifiers())
            && method.getDeclaringClass() != Object.class
            && method.getParameterTypes().length == 1
            && method.getName().startsWith("set")
            && method.getName().length() > 3;
    }

    /**
     * Gets property name from bean write method *
     *
     * @param method method
     * @return the property name from bean write method
     * @since 1.0.0
     */
    public static String getPropertyNameFromBeanWriteMethod(Method method) {
        if (isBeanPropertyWriteMethod(method)) {
            return method.getName().substring(3, 4).toLowerCase()
                + method.getName().substring(4);
        }
        return null;
    }

    /**
     * Is public instance field
     *
     * @param field field
     * @return the boolean
     * @since 1.0.0
     */
    public static boolean isPublicInstanceField(Field field) {
        return Modifier.isPublic(field.getModifiers())
            && !Modifier.isStatic(field.getModifiers())
            && !Modifier.isFinal(field.getModifiers())
            && !field.isSynthetic();
    }

    /**
     * Gets bean property fields *
     *
     * @param cl cl
     * @return the bean property fields
     * @since 1.0.0
     */
    public static Map<String, Field> getBeanPropertyFields(Class cl) {
        Map<String, Field> properties = new HashMap<String, Field>();
        for (; cl != null; cl = cl.getSuperclass()) {
            Field[] fields = cl.getDeclaredFields();
            for (Field field : fields) {
                if (Modifier.isTransient(field.getModifiers())
                    || Modifier.isStatic(field.getModifiers())) {
                    continue;
                }

                field.setAccessible(true);

                properties.put(field.getName(), field);
            }
        }

        return properties;
    }

    /**
     * Gets bean property read methods *
     *
     * @param cl cl
     * @return the bean property read methods
     * @since 1.0.0
     */
    public static Map<String, Method> getBeanPropertyReadMethods(Class cl) {
        Map<String, Method> properties = new HashMap<String, Method>();
        for (; cl != null; cl = cl.getSuperclass()) {
            Method[] methods = cl.getDeclaredMethods();
            for (Method method : methods) {
                if (isBeanPropertyReadMethod(method)) {
                    method.setAccessible(true);
                    String property = getPropertyNameFromBeanReadMethod(method);
                    properties.put(property, method);
                }
            }
        }

        return properties;
    }

    /**
     * Get return types
     *
     * @param method method
     * @return the type [ ]
     * @since 1.0.0
     */
    public static Type[] getReturnTypes(Method method) {
        Class<?> returnType = method.getReturnType();
        Type genericReturnType = method.getGenericReturnType();
        if (Future.class.isAssignableFrom(returnType)) {
            if (genericReturnType instanceof ParameterizedType) {
                Type actualArgType = ((ParameterizedType) genericReturnType).getActualTypeArguments()[0];
                if (actualArgType instanceof ParameterizedType) {
                    returnType = (Class<?>) ((ParameterizedType) actualArgType).getRawType();
                    genericReturnType = actualArgType;
                } else {
                    returnType = (Class<?>) actualArgType;
                    genericReturnType = returnType;
                }
            } else {
                returnType = null;
                genericReturnType = null;
            }
        }
        return new Type[]{returnType, genericReturnType};
    }
}
