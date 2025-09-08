package dev.dong4j.zeka.kernel.spi.compiler;


import dev.dong4j.zeka.kernel.spi.extension.SPI;

/**
 * Java代码编译器SPI接口，提供动态编译Java源代码的能力
 * <p>
 * 该接口定义了统一的代码编译标准，支持多种编译器实现
 * 默认使用Javassist编译器，同时支持JDK原生编译器等其他实现
 * <p>
 * 主要功能：
 * - 动态编译Java源代码
 * - 支持自定义类加载器
 * - 提供多种编译器实现选择
 * - 高性能的运行时编译
 * <p>
 * 支持的编译器实现：
 * - javassist：基于Javassist库的字节码生成编译器
 * - jdk：基于JDK原生的动态编译器
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2021.02.26 17:47
 * @since 1.0.0
 */
@SPI("javassist")
public interface Compiler {

    /**
     * 编译Java源代码为字节码类
     *
     * 将给定的Java源代码字符串编译为可执行的Class对象
     * 支持自定义类加载器，用于控制类的加载环境
     *
     * @param code        要编译的Java源代码字符串
     * @param classLoader 类加载器，用于加载编译后的类
     * @return 编译后的Class对象
     * @since 1.0.0
     */
    Class<?> compile(String code, ClassLoader classLoader);

}
