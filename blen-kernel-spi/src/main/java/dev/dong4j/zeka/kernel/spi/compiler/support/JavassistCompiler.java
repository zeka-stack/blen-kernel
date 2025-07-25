package dev.dong4j.zeka.kernel.spi.compiler.support;

import dev.dong4j.zeka.kernel.spi.utils.SpiClassUtils;
import javassist.CtClass;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
public class JavassistCompiler extends AbstractCompiler {

    /** IMPORT_PATTERN */
    private static final Pattern IMPORT_PATTERN = Pattern.compile("import\\s+([\\w\\.\\*]+);\n");

    /** EXTENDS_PATTERN */
    private static final Pattern EXTENDS_PATTERN = Pattern.compile("\\s+extends\\s+([\\w\\.]+)[^\\{]*\\{\n");

    /** IMPLEMENTS_PATTERN */
    private static final Pattern IMPLEMENTS_PATTERN = Pattern.compile("\\s+implements\\s+([\\w\\.]+)\\s*\\{\n");

    /** METHODS_PATTERN */
    private static final Pattern METHODS_PATTERN = Pattern.compile("\n(private|public|protected)\\s+");

    /** FIELD_PATTERN */
    private static final Pattern FIELD_PATTERN = Pattern.compile("[^\n]+=[^\n]+;");

    /**
     * Do compile
     *
     * @param name   name
     * @param source source
     * @return the class
     * @throws Throwable throwable
     * @since 1.8.0
     */
    @Override
    public Class<?> doCompile(String name, String source) throws Throwable {
        CtClassBuilder builder = new CtClassBuilder();
        builder.setClassName(name);

        // process imported classes
        Matcher matcher = IMPORT_PATTERN.matcher(source);
        while (matcher.find()) {
            builder.addImports(matcher.group(1).trim());
        }

        // process extended super class
        matcher = EXTENDS_PATTERN.matcher(source);
        if (matcher.find()) {
            builder.setSuperClassName(matcher.group(1).trim());
        }

        // process implemented interfaces
        matcher = IMPLEMENTS_PATTERN.matcher(source);
        if (matcher.find()) {
            String[] ifaces = matcher.group(1).trim().split("\\,");
            Arrays.stream(ifaces).forEach(i -> builder.addInterface(i.trim()));
        }

        // process constructors, fields, methods
        String body = source.substring(source.indexOf('{') + 1, source.length() - 1);
        String[] methods = METHODS_PATTERN.split(body);
        String className = CompilerClassUtils.getSimpleClassName(name);
        Arrays.stream(methods).map(String::trim).filter(m -> !m.isEmpty()).forEach(method -> {
            if (method.startsWith(className)) {
                builder.addConstructor("public " + method);
            } else if (FIELD_PATTERN.matcher(method).matches()) {
                builder.addField("private " + method);
            } else {
                builder.addMethod("public " + method);
            }
        });

        // compile
        ClassLoader classLoader = SpiClassUtils.getCallerClassLoader(getClass());
        CtClass cls = builder.build(classLoader);
        return cls.toClass(classLoader, JavassistCompiler.class.getProtectionDomain());
    }

}
