package dev.dong4j.zeka.kernel.spi.compiler;


import dev.dong4j.zeka.kernel.spi.extension.SPI;

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.8.0
 * @email "mailto:dong4j@gmaidl.com"
 * @date 2021.02.26 17:47
 * @since 1.8.0
 */
@SPI("javassist")
public interface Compiler {

    /**
     * Compile
     *
     * @param code        code
     * @param classLoader class loader
     * @return the class
     * @since 1.8.0
     */
    Class<?> compile(String code, ClassLoader classLoader);

}
