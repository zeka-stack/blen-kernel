package dev.dong4j.zeka.kernel.spi.compiler.support;

import dev.dong4j.zeka.kernel.spi.compiler.Compiler;
import dev.dong4j.zeka.kernel.spi.extension.Adaptive;
import dev.dong4j.zeka.kernel.spi.extension.SPILoader;
import java.util.Objects;


/**
 * 自适应编译器，根据配置动态选择具体的编译器实现
 * <p>
 * 该类使用@Adaptive注解标记，作为SPI框架的自适应扩展实现
 * 它不直接执行编译操作，而是作为代理委托给具体的编译器实现
 * <p>
 * 主要功能：
 * - 支持动态配置默认编译器
 * - 通过SPILoader加载指定的编译器实现
 * - 提供编译器切换的统一入口
 * - 支持默认编译器的回退机制
 * <p>
 * 使用场景：在需要根据运行时环境或配置动态选择编译器的情况下使用
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2021.02.26 17:47
 * @since 1.0.0
 */
@Adaptive
public class AdaptiveCompiler implements Compiler {

    /** 默认编译器名称，支持运行时动态设置和更改 */
    private static volatile String defaultCompiler;

    /**
     * 设置默认编译器
     * <p>
     * 该方法允许在运行时动态更改默认编译器实现
     * 设置后，所有新的编译请求都将使用指定的编译器
     *
     * @param compiler 编译器名称（如"javassist"、"jdk"等）
     * @since 1.0.0
     */
    public static void setDefaultCompiler(String compiler) {
        defaultCompiler = compiler;
    }

    /**
     * 编译Java源代码
     * <p>
     * 该方法首先检查是否设置了默认编译器，如果有则使用指定的编译器
     * 否则使用SPI框架配置的默认编译器实现
     * <p>
     * 编译流程：
     * 1. 获取SPILoader实例
     * 2. 检查默认编译器设置
     * 3. 加载对应的编译器实现
     * 4. 委托具体编译器执行编译
     *
     * @param code        Java源代码字符串
     * @param classLoader 类加载器
     * @return 编译后的Class对象
     * @throws RuntimeException 如果编译器加载失败或编译过程出错
     * @since 1.0.0
     */
    @Override
    public Class<?> compile(String code, ClassLoader classLoader) {
        Compiler compiler;
        SPILoader<Compiler> loader = SPILoader.getExtensionLoader(Compiler.class);
        String name = defaultCompiler;
        if (name != null && name.length() > 0) {
            compiler = loader.getExtension(name);
        } else {
            compiler = loader.getDefaultExtension();
        }
        return Objects.requireNonNull(compiler).compile(code, classLoader);
    }

}
