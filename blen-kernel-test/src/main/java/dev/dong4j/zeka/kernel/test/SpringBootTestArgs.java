package dev.dong4j.zeka.kernel.test;

import org.jetbrains.annotations.NotNull;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.test.context.ContextCustomizer;
import org.springframework.test.context.MergedContextConfiguration;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;

/**
 * Spring Boot 测试参数记录类
 * <p>
 * 该类用于定义 Spring Boot 测试时的参数配置. 通过实现 ContextCustomizer 接口,
 * 提供自定义上下文的能力. 支持从类注解中提取测试参数, 并在测试上下文中进行自定义.
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2025.12.28
 * @since 1.0.0
 */
public record SpringBootTestArgs(String[] args) implements ContextCustomizer {
    /**
     * 表示没有参数的空字符串数组
     * <p>
     * 用于在没有提供具体参数时作为默认值
     */
    private static final String[] NO_ARGS = new String[0];

    /**
     * 构造函数, 初始化 SpringBootTestArgs 对象
     * <p> 根据传入的 args 数组创建对象, 如果 args 为 null, 则使用默认的空数组 NO_ARGS
     *
     * @param args 测试类的命令行参数数组
     * @since 1.0.0
     */
    public SpringBootTestArgs(String[] args) {
        this.args = args != null ? Arrays.copyOf(args, args.length) : NO_ARGS;
    }

    /**
     * 构造函数, 根据测试类创建 SpringBootTestArgs 对象
     * <p> 从给定的测试类中获取 SpringBootTest 注解中的 "args" 值, 并使用这些值初始化对象.
     * 如果没有指定 "args", 则使用默认的空数组.
     *
     * @param clazz 测试类
     * @since 1.0.0
     */
    public SpringBootTestArgs(Class<?> clazz) {
        this(MergedAnnotations.from(clazz,
                MergedAnnotations.SearchStrategy.TYPE_HIERARCHY)
            .get(SpringBootTest.class)
            .getValue("args", String[].class).orElse(NO_ARGS));
    }

    /**
     * 自定义应用程序上下文
     * <p> 根据给定的上下文和合并配置对 Spring Boot 测试进行自定义设置
     *
     * @param context      应用程序上下文
     * @param mergedConfig 合并后的配置
     * @since 1.0.0
     */
    public void customizeContext(@NotNull ConfigurableApplicationContext context,
                                 @NotNull MergedContextConfiguration mergedConfig) {
    }

    /**
     * 获取当前对象的参数数组
     * <p> 返回存储在当前对象中的参数数组, 如果参数为空, 则返回一个空数组 </p>
     *
     * @return 参数数组, 如果为空则返回一个空数组
     * @since 1.0.0
     */
    @Override
    public String[] args() {
        return this.args != null ? Arrays.copyOf(this.args, this.args.length) : NO_ARGS;
    }

    /**
     * 判断当前对象是否与指定对象相等
     * <p> 两个对象相等的条件是: 对象不为空, 且类相同, 并且内部的 args 数组相等
     *
     * @param obj 要比较的对象
     * @return 如果对象相等则返回 true, 否则返回 false
     */
    public boolean equals(Object obj) {
        return obj != null && this.getClass() == obj.getClass() && Arrays.equals(this.args, ((SpringBootTestArgs) obj).args);
    }

    /**
     * 返回当前对象的哈希码
     * <p> 该方法调用 {@link Arrays#hashCode(Object[])} 来计算由 {@code args} 字段组成的数组的哈希码.
     *
     * @return 当前对象的哈希码
     * @since 1.0.0
     */
    public int hashCode() {
        return Arrays.hashCode(this.args);
    }

    /**
     * 获取指定上下文中第一个符合条件的 SpringBootTestArgs 对象的参数数组
     * <p> 遍历给定的上下文自定义器集合, 找到第一个实现 SpringBootTestArgs 的对象, 并返回其参数数组. 如果没有找到, 则返回空数组.
     *
     * @param customizers 上下文自定义器集合
     * @return 符合条件的 SpringBootTestArgs 对象的参数数组, 如果没有找到则返回空数组
     * @since 1.0.0
     */
    static String[] get(@NotNull Set<ContextCustomizer> customizers) {
        Iterator<ContextCustomizer> customizerIterator = customizers.iterator();

        ContextCustomizer customizer;
        do {
            if (!customizerIterator.hasNext()) {
                return NO_ARGS;
            }

            customizer = customizerIterator.next();
        } while (!(customizer instanceof SpringBootTestArgs));

        return ((SpringBootTestArgs) customizer).args;
    }
}
