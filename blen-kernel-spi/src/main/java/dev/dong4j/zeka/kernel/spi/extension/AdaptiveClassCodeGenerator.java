package dev.dong4j.zeka.kernel.spi.extension;

import dev.dong4j.zeka.kernel.spi.URL;
import dev.dong4j.zeka.kernel.spi.utils.SpiStringUtils;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmaidl.com"
 * @date 2021.02.26 17:47
 * @since 1.0.0
 */
@Slf4j
@SuppressWarnings("all")
public class AdaptiveClassCodeGenerator {

    /** CLASSNAME_INVOCATION */
    private static final String CLASSNAME_INVOCATION = "org.apache.dubbo.rpc.Invocation";

    /** CODE_PACKAGE */
    private static final String CODE_PACKAGE = "package %s;\n";

    /** CODE_IMPORTS */
    private static final String CODE_IMPORTS = "import %s;\n";

    /** CODE_CLASS_DECLARATION */
    private static final String CODE_CLASS_DECLARATION = "public class %s$Adaptive implements %s {\n";

    /** CODE_METHOD_DECLARATION */
    private static final String CODE_METHOD_DECLARATION = "public %s %s(%s) %s {\n%s}\n";

    /** CODE_METHOD_ARGUMENT */
    private static final String CODE_METHOD_ARGUMENT = "%s arg%d";

    /** CODE_METHOD_THROWS */
    private static final String CODE_METHOD_THROWS = "throws %s";

    /** CODE_UNSUPPORTED */
    private static final String CODE_UNSUPPORTED = "throw new UnsupportedOperationException(\"The method %s of interface %s is not "
        + "adaptive method!\");\n";

    /** CODE_URL_NULL_CHECK */
    private static final String CODE_URL_NULL_CHECK = "if (arg%d == null) throw new IllegalArgumentException(\"url == null\");\n%s url = "
        + "arg%d;\n";

    /** CODE_EXT_NAME_ASSIGNMENT */
    private static final String CODE_EXT_NAME_ASSIGNMENT = "String extName = %s;\n";

    /** CODE_EXT_NAME_NULL_CHECK */
    private static final String CODE_EXT_NAME_NULL_CHECK = "if(extName == null) "
        + "throw new IllegalStateException(\"Failed to get extension (%s) name from "
        + "url (\" + url.toString() + \") use keys(%s)\");\n";

    /** CODE_INVOCATION_ARGUMENT_NULL_CHECK */
    private static final String CODE_INVOCATION_ARGUMENT_NULL_CHECK = "if (arg%d == null) throw new IllegalArgumentException(\"invocation"
        + " == null\"); "
        + "String methodName = arg%d.getMethodName();\n";


    /** CODE_EXTENSION_ASSIGNMENT */
    private static final String CODE_EXTENSION_ASSIGNMENT = "%s extension = (%<s)%s.getExtensionLoader(%s.class).getExtension(extName);\n";

    /** CODE_EXTENSION_METHOD_INVOKE_ARGUMENT */
    private static final String CODE_EXTENSION_METHOD_INVOKE_ARGUMENT = "arg%d";

    /** Type */
    private final Class<?> type;

    /** Default ext name */
    private String defaultExtName;

    /**
     * Adaptive class code generator
     *
     * @param type           type
     * @param defaultExtName default ext name
     * @since 1.0.0
     */
    public AdaptiveClassCodeGenerator(Class<?> type, String defaultExtName) {
        this.type = type;
        this.defaultExtName = defaultExtName;
    }

    /**
     * Has adaptive method
     *
     * @return the boolean
     * @since 1.0.0
     */
    private boolean hasAdaptiveMethod() {
        return Arrays.stream(type.getMethods()).anyMatch(m -> m.isAnnotationPresent(Adaptive.class));
    }

    /**
     * Generate
     *
     * @return the string
     * @since 1.0.0
     */
    public String generate() {
        // no need to generate adaptive class since there's no adaptive method found.
        if (!hasAdaptiveMethod()) {
            throw new IllegalStateException("No adaptive method exist on extension " + type.getName() + ", refuse to create the adaptive "
                + "class!");
        }

        StringBuilder code = new StringBuilder();
        code.append(generatePackageInfo());
        code.append(generateImports());
        code.append(generateClassDeclaration());

        Method[] methods = type.getMethods();
        for (Method method : methods) {
            code.append(generateMethod(method));
        }
        code.append("}");

        if (log.isDebugEnabled()) {
            log.debug(code.toString());
        }
        return code.toString();
    }

    /**
     * Generate package info
     *
     * @return the string
     * @since 1.0.0
     */
    private String generatePackageInfo() {
        return String.format(CODE_PACKAGE, type.getPackage().getName());
    }

    /**
     * Generate imports
     *
     * @return the string
     * @since 1.0.0
     */
    private String generateImports() {
        return String.format(CODE_IMPORTS, SPILoader.class.getName());
    }

    /**
     * Generate class declaration
     *
     * @return the string
     * @since 1.0.0
     */
    private String generateClassDeclaration() {
        return String.format(CODE_CLASS_DECLARATION, type.getSimpleName(), type.getCanonicalName());
    }

    /**
     * Generate unsupported
     *
     * @param method method
     * @return the string
     * @since 1.0.0
     */
    private String generateUnsupported(Method method) {
        return String.format(CODE_UNSUPPORTED, method, type.getName());
    }

    /**
     * Gets url type index *
     *
     * @param method method
     * @return the url type index
     * @since 1.0.0
     */
    private int getUrlTypeIndex(Method method) {
        int urlTypeIndex = -1;
        Class<?>[] pts = method.getParameterTypes();
        for (int i = 0; i < pts.length; ++i) {
            if (pts[i].equals(URL.class)) {
                urlTypeIndex = i;
                break;
            }
        }
        return urlTypeIndex;
    }

    /**
     * Generate method
     *
     * @param method method
     * @return the string
     * @since 1.0.0
     */
    private String generateMethod(Method method) {
        String methodReturnType = method.getReturnType().getCanonicalName();
        String methodName = method.getName();
        String methodContent = generateMethodContent(method);
        String methodArgs = generateMethodArguments(method);
        String methodThrows = generateMethodThrows(method);
        return String.format(CODE_METHOD_DECLARATION, methodReturnType, methodName, methodArgs, methodThrows, methodContent);
    }

    /**
     * Generate method arguments
     *
     * @param method method
     * @return the string
     * @since 1.0.0
     */
    private String generateMethodArguments(Method method) {
        Class<?>[] pts = method.getParameterTypes();
        return IntStream.range(0, pts.length)
            .mapToObj(i -> String.format(CODE_METHOD_ARGUMENT, pts[i].getCanonicalName(), i))
            .collect(Collectors.joining(", "));
    }

    /**
     * Generate method throws
     *
     * @param method method
     * @return the string
     * @since 1.0.0
     */
    private String generateMethodThrows(Method method) {
        Class<?>[] ets = method.getExceptionTypes();
        if (ets.length > 0) {
            String list = Arrays.stream(ets).map(Class::getCanonicalName).collect(Collectors.joining(", "));
            return String.format(CODE_METHOD_THROWS, list);
        } else {
            return "";
        }
    }

    /**
     * Generate url null check
     *
     * @param index index
     * @return the string
     * @since 1.0.0
     */
    private String generateUrlNullCheck(int index) {
        return String.format(CODE_URL_NULL_CHECK, index, URL.class.getName(), index);
    }

    /**
     * Generate method content
     *
     * @param method method
     * @return the string
     * @since 1.0.0
     */
    private String generateMethodContent(Method method) {
        Adaptive adaptiveAnnotation = method.getAnnotation(Adaptive.class);
        StringBuilder code = new StringBuilder(512);
        if (adaptiveAnnotation == null) {
            return generateUnsupported(method);
        } else {
            int urlTypeIndex = getUrlTypeIndex(method);

            // found parameter in URL type
            if (urlTypeIndex != -1) {
                // Null Point check
                code.append(generateUrlNullCheck(urlTypeIndex));
            } else {
                // did not find parameter in URL type
                code.append(generateUrlAssignmentIndirectly(method));
            }

            String[] value = getMethodAdaptiveValue(adaptiveAnnotation);

            boolean hasInvocation = hasInvocationArgument(method);

            code.append(generateInvocationArgumentNullCheck(method));

            code.append(generateExtNameAssignment(value, hasInvocation));
            // check extName == null?
            code.append(generateExtNameNullCheck(value));

            code.append(generateExtensionAssignment());

            // return statement
            code.append(generateReturnAndInvocation(method));
        }

        return code.toString();
    }

    /**
     * Generate ext name null check
     *
     * @param value value
     * @return the string
     * @since 1.0.0
     */
    private String generateExtNameNullCheck(String[] value) {
        return String.format(CODE_EXT_NAME_NULL_CHECK, type.getName(), Arrays.toString(value));
    }

    /**
     * Generate ext name assignment
     *
     * @param value         value
     * @param hasInvocation has invocation
     * @return the string
     * @since 1.0.0
     */
    private String generateExtNameAssignment(String[] value, boolean hasInvocation) {
        // TODO: refactor it
        String getNameCode = null;
        for (int i = value.length - 1; i >= 0; --i) {
            if (i == value.length - 1) {
                if (null != defaultExtName) {
                    if (!"protocol".equals(value[i])) {
                        if (hasInvocation) {
                            getNameCode = String.format("url.getMethodParameter(methodName, \"%s\", \"%s\")", value[i], defaultExtName);
                        } else {
                            getNameCode = String.format("url.getParameter(\"%s\", \"%s\")", value[i], defaultExtName);
                        }
                    } else {
                        getNameCode = String.format("( url.getProtocol() == null ? \"%s\" : url.getProtocol() )", defaultExtName);
                    }
                } else {
                    if (!"protocol".equals(value[i])) {
                        if (hasInvocation) {
                            getNameCode = String.format("url.getMethodParameter(methodName, \"%s\", \"%s\")", value[i], defaultExtName);
                        } else {
                            getNameCode = String.format("url.getParameter(\"%s\")", value[i]);
                        }
                    } else {
                        getNameCode = "url.getProtocol()";
                    }
                }
            } else {
                if (!"protocol".equals(value[i])) {
                    if (hasInvocation) {
                        getNameCode = String.format("url.getMethodParameter(methodName, \"%s\", \"%s\")", value[i], defaultExtName);
                    } else {
                        getNameCode = String.format("url.getParameter(\"%s\", %s)", value[i], getNameCode);
                    }
                } else {
                    getNameCode = String.format("url.getProtocol() == null ? (%s) : url.getProtocol()", getNameCode);
                }
            }
        }

        return String.format(CODE_EXT_NAME_ASSIGNMENT, getNameCode);
    }

    /**
     * Generate extension assignment
     *
     * @return the string
     * @since 1.0.0
     */
    private String generateExtensionAssignment() {
        return String.format(CODE_EXTENSION_ASSIGNMENT, type.getName(), SPILoader.class.getSimpleName(), type.getName());
    }

    /**
     * Generate return and invocation
     *
     * @param method method
     * @return the string
     * @since 1.0.0
     */
    private String generateReturnAndInvocation(Method method) {
        String returnStatement = method.getReturnType().equals(void.class) ? "" : "return ";

        String args = IntStream.range(0, method.getParameters().length)
            .mapToObj(i -> String.format(CODE_EXTENSION_METHOD_INVOKE_ARGUMENT, i))
            .collect(Collectors.joining(", "));

        return returnStatement + String.format("extension.%s(%s);\n", method.getName(), args);
    }

    /**
     * Has invocation argument
     *
     * @param method method
     * @return the boolean
     * @since 1.0.0
     */
    private boolean hasInvocationArgument(Method method) {
        Class<?>[] pts = method.getParameterTypes();
        return Arrays.stream(pts).anyMatch(p -> CLASSNAME_INVOCATION.equals(p.getName()));
    }

    /**
     * Generate invocation argument null check
     *
     * @param method method
     * @return the string
     * @since 1.0.0
     */
    private String generateInvocationArgumentNullCheck(Method method) {
        Class<?>[] pts = method.getParameterTypes();
        return IntStream.range(0, pts.length).filter(i -> CLASSNAME_INVOCATION.equals(pts[i].getName()))
            .mapToObj(i -> String.format(CODE_INVOCATION_ARGUMENT_NULL_CHECK, i, i))
            .findFirst().orElse("");
    }

    /**
     * Get method adaptive value
     *
     * @param adaptiveAnnotation adaptive annotation
     * @return the string [ ]
     * @since 1.0.0
     */
    private String[] getMethodAdaptiveValue(Adaptive adaptiveAnnotation) {
        String[] value = adaptiveAnnotation.value();
        // value is not set, use the value generated from class name as the key
        if (value.length == 0) {
            String splitName = SpiStringUtils.camelToSplitName(type.getSimpleName(), ".");
            value = new String[]{splitName};
        }
        return value;
    }

    /**
     * Generate url assignment indirectly
     *
     * @param method method
     * @return the string
     * @since 1.0.0
     */
    private String generateUrlAssignmentIndirectly(Method method) {
        Class<?>[] pts = method.getParameterTypes();

        // find URL getter method
        for (int i = 0; i < pts.length; ++i) {
            for (Method m : pts[i].getMethods()) {
                String name = m.getName();
                if ((name.startsWith("get") || name.length() > 3)
                    && Modifier.isPublic(m.getModifiers())
                    && !Modifier.isStatic(m.getModifiers())
                    && m.getParameterTypes().length == 0
                    && m.getReturnType() == URL.class) {
                    return generateGetUrlNullCheck(i, pts[i], name);
                }
            }
        }

        // getter method not found, throw
        throw new IllegalStateException("Failed to create adaptive class for interface " + type.getName()
            + ": not found url parameter or url attribute in parameters of method " + method.getName());

    }

    /**
     * Generate get url null check
     *
     * @param index  index
     * @param type   type
     * @param method method
     * @return the string
     * @since 1.0.0
     */
    private String generateGetUrlNullCheck(int index, Class<?> type, String method) {
        // Null point check
        StringBuilder code = new StringBuilder();
        code.append(String.format("if (arg%d == null) throw new IllegalArgumentException(\"%s argument == null\");\n",
            index, type.getName()));
        code.append(String.format("if (arg%d.%s() == null) throw new IllegalArgumentException(\"%s argument %s() == null\");\n",
            index, method, type.getName(), method));

        code.append(String.format("%s url = arg%d.%s();\n", URL.class.getName(), index, method));
        return code.toString();
    }

}
