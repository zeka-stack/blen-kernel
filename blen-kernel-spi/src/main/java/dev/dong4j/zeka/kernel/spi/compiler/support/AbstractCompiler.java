package dev.dong4j.zeka.kernel.spi.compiler.support;

import dev.dong4j.zeka.kernel.spi.compiler.Compiler;
import dev.dong4j.zeka.kernel.spi.utils.SpiClassUtils;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 抽象编译器基类，提供Java源代码编译的通用功能和模板方法
 * <p>
 * 该类实现了Compiler接口，提供了Java源代码解析和编译的基础框架
 * 包括包名和类名的解析、类加载尝试和编译委托等核心逻辑
 * <p>
 * 主要功能：
 * - 解析Java源代码中的包名和类名
 * - 尝试从类加载器中加载已存在的类
 * - 委托具体实现进行源代码编译
 * - 提供统一的错误处理和异常封装
 * <p>
 * 设计模式：采用模板方法模式，子类需要实现具体的编译逻辑
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2021.02.26 17:47
 * @since 1.0.0
 */
@SuppressWarnings("all")
public abstract class AbstractCompiler implements Compiler {

    /** 包声明匹配正则表达式，用于从Java源代码中提取包名 */
    private static final Pattern PACKAGE_PATTERN = Pattern.compile("package\\s+([$_a-zA-Z][$_a-zA-Z0-9\\.]*);");

    /** 类声明匹配正则表达式，用于从Java源代码中提取类名 */
    private static final Pattern CLASS_PATTERN = Pattern.compile("class\\s+([$_a-zA-Z][$_a-zA-Z0-9]*)\\s+");

    /**
     * 编译Java源代码为Class对象
     * <p>
     * 该方法首先尝试解析源代码中的包名和类名，然后尝试从类加载器中加载已存在的类
     * 如果类不存在，则调用具体的编译实现进行源代码编译
     * <p>
     * 编译流程：
     * 1. 解析包名和类名
     * 2. 构造完整的类名
     * 3. 尝试加载已存在的类
     * 4. 如果类不存在，进行源代码编译
     *
     * @param code        Java源代码字符串
     * @param classLoader 用于加载类的类加载器
     * @return 编译或加载的Class对象
     * @throws IllegalArgumentException 如果源代码中没有找到类名
     * @throws IllegalStateException    如果编译失败或源代码格式错误
     * @since 1.0.0
     */
    @Override
    public Class<?> compile(String code, ClassLoader classLoader) {
        code = code.trim();
        Matcher matcher = PACKAGE_PATTERN.matcher(code);
        String pkg;
        if (matcher.find()) {
            pkg = matcher.group(1);
        } else {
            pkg = "";
        }
        matcher = CLASS_PATTERN.matcher(code);
        String cls;
        if (matcher.find()) {
            cls = matcher.group(1);
        } else {
            throw new IllegalArgumentException("No such class name in " + code);
        }
        String className = pkg != null && pkg.length() > 0 ? pkg + "." + cls : cls;
        try {
            return Class.forName(className, true, SpiClassUtils.getCallerClassLoader(this.getClass()));
        } catch (ClassNotFoundException e) {
            if (!code.endsWith("}")) {
                throw new IllegalStateException("The java code not endsWith \"}\", code: \n" + code + "\n");
            }
            try {
                return this.doCompile(className, code);
            } catch (RuntimeException t) {
                throw t;
            } catch (Throwable t) {
                throw new IllegalStateException("Failed to compile class, cause: " + t.getMessage() + ", class: " + className + ", code: "
                    + "\n" + code + "\n, stack: " + CompilerClassUtils.toString(t));
            }
        }
    }

    /**
     * 执行具体的编译操作（模板方法）
     * <p>
     * 该抽象方法由具体的编译器实现类提供编译逻辑
     * 不同的编译器可以使用不同的编译技术（如Javassist、JDK编译器等）
     *
     * @param name   完整的类名（包含包名）
     * @param source Java源代码字符串
     * @return 编译后的Class对象
     * @throws Throwable 编译过程中可能出现的任何异常
     * @since 1.0.0
     */
    protected abstract Class<?> doCompile(String name, String source) throws Throwable;

}
