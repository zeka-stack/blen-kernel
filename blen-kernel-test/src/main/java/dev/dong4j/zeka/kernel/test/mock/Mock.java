package dev.dong4j.zeka.kernel.test.mock;

import dev.dong4j.zeka.kernel.test.mock.mocker.BaseMocker;
import org.jetbrains.annotations.NotNull;

/**
 * <p>Description: 模拟对象门面类 </p>
 *
 * @author jsonzou, kanyuxia, TaoYu
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.27 18:07
 * @since 1.0.0
 */
public class Mock {

    /**
     * 模拟数据
     *
     * @param <T>   parameter
     * @param clazz 模拟数据类型
     * @return 模拟数据对象 t
     * @since 1.0.0
     */
    public static <T> T mock(Class<T> clazz) {
        return mock(clazz, new MockConfig());
    }

    /**
     * 模拟数据
     *
     * @param <T>        parameter
     * @param clazz      模拟数据类型
     * @param mockConfig 模拟数据配置
     * @return 模拟数据对象 t
     * @since 1.0.0
     */
    public static <T> T mock(Class<T> clazz, MockConfig mockConfig) {
        return new BaseMocker<T>(clazz).mock(mockConfig);
    }

    /**
     * 模拟数据
     * <pre>
     * 注意typeReference必须以{}结尾
     * </pre>
     *
     * @param <T>           parameter
     * @param typeReference 模拟数据包装类型
     * @return 模拟数据对象 t
     * @since 1.0.0
     */
    public static <T> T mock(TypeKit<T> typeReference) {
        return mock(typeReference, new MockConfig());
    }

    /**
     * 模拟数据
     * <pre>
     * 注意typeReference必须以{}结尾
     * </pre>
     *
     * @param <T>           parameter
     * @param typeReference 模拟数据类型
     * @param mockConfig    模拟数据配置
     * @return 模拟数据对象 t
     * @since 1.0.0
     */
    public static <T> T mock(@NotNull TypeKit<T> typeReference, @NotNull MockConfig mockConfig) {
        return new BaseMocker<T>(typeReference.getType()).mock(mockConfig.init(typeReference.getType()));
    }

}
