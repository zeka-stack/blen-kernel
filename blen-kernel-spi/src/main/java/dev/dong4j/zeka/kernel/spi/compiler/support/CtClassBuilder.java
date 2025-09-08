package dev.dong4j.zeka.kernel.spi.compiler.support;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtNewConstructor;
import javassist.CtNewMethod;
import javassist.LoaderClassPath;
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
public class CtClassBuilder {

    /** Class name */
    private String className;

    /** Super class name */
    private String superClassName = "java.lang.Object";

    /** Imports */
    private final List<String> imports = new ArrayList<>();

    /** Full names */
    private final Map<String, String> fullNames = new HashMap<>();

    /** Ifaces */
    private final List<String> ifaces = new ArrayList<>();

    /** Constructors */
    private final List<String> constructors = new ArrayList<>();

    /** Fields */
    private final List<String> fields = new ArrayList<>();

    /** Methods */
    private final List<String> methods = new ArrayList<>();

    /**
     * Gets class name *
     *
     * @return the class name
     * @since 1.0.0
     */
    public String getClassName() {
        return this.className;
    }

    /**
     * Sets class name *
     *
     * @param className class name
     * @since 1.0.0
     */
    public void setClassName(String className) {
        this.className = className;
    }

    /**
     * Gets super class name *
     *
     * @return the super class name
     * @since 1.0.0
     */
    public String getSuperClassName() {
        return this.superClassName;
    }

    /**
     * Sets super class name *
     *
     * @param superClassName super class name
     * @since 1.0.0
     */
    public void setSuperClassName(String superClassName) {
        this.superClassName = this.getQualifiedClassName(superClassName);
    }

    /**
     * Gets imports *
     *
     * @return the imports
     * @since 1.0.0
     */
    public List<String> getImports() {
        return this.imports;
    }

    /**
     * Add imports
     *
     * @param pkg pkg
     * @since 1.0.0
     */
    public void addImports(String pkg) {
        int pi = pkg.lastIndexOf('.');
        if (pi > 0) {
            String pkgName = pkg.substring(0, pi);
            this.imports.add(pkgName);
            if (!pkg.endsWith(".*")) {
                this.fullNames.put(pkg.substring(pi + 1), pkg);
            }
        }
    }

    /**
     * Gets interfaces *
     *
     * @return the interfaces
     * @since 1.0.0
     */
    public List<String> getInterfaces() {
        return this.ifaces;
    }

    /**
     * Add interface
     *
     * @param iface iface
     * @since 1.0.0
     */
    public void addInterface(String iface) {
        this.ifaces.add(this.getQualifiedClassName(iface));
    }

    /**
     * Gets constructors *
     *
     * @return the constructors
     * @since 1.0.0
     */
    public List<String> getConstructors() {
        return this.constructors;
    }

    /**
     * Add constructor
     *
     * @param constructor constructor
     * @since 1.0.0
     */
    public void addConstructor(String constructor) {
        this.constructors.add(constructor);
    }

    /**
     * Gets fields *
     *
     * @return the fields
     * @since 1.0.0
     */
    public List<String> getFields() {
        return this.fields;
    }

    /**
     * Add field
     *
     * @param field field
     * @since 1.0.0
     */
    public void addField(String field) {
        this.fields.add(field);
    }

    /**
     * Gets methods *
     *
     * @return the methods
     * @since 1.0.0
     */
    public List<String> getMethods() {
        return this.methods;
    }

    /**
     * Add method
     *
     * @param method method
     * @since 1.0.0
     */
    public void addMethod(String method) {
        this.methods.add(method);
    }

    /**
     * Gets qualified class name *
     *
     * @param className class name
     * @return the qualified class name
     * @since 1.0.0
     */
    protected String getQualifiedClassName(String className) {
        if (className.contains(".")) {
            return className;
        }

        if (this.fullNames.containsKey(className)) {
            return this.fullNames.get(className);
        }

        return CompilerClassUtils.forName(this.imports.toArray(new String[0]), className).getName();
    }

    /**
     * Build
     *
     * @param classLoader class loader
     * @return the ct class
     * @throws NotFoundException      not found exception
     * @throws CannotCompileException cannot compile exception
     * @since 1.0.0
     */
    public CtClass build(ClassLoader classLoader) throws NotFoundException, CannotCompileException {
        ClassPool pool = new ClassPool(true);
        pool.appendClassPath(new LoaderClassPath(classLoader));

        // create class
        CtClass ctClass = pool.makeClass(this.className, pool.get(this.superClassName));

        // add imported packages
        this.imports.stream().forEach(pool::importPackage);

        // add implemented interfaces
        for (String iface : this.ifaces) {
            ctClass.addInterface(pool.get(iface));
        }

        // add constructors
        for (String constructor : this.constructors) {
            ctClass.addConstructor(CtNewConstructor.make(constructor, ctClass));
        }

        // add fields
        for (String field : this.fields) {
            ctClass.addField(CtField.make(field, ctClass));
        }

        // add methods
        for (String method : this.methods) {
            ctClass.addMethod(CtNewMethod.make(method, ctClass));
        }

        return ctClass;
    }

}
