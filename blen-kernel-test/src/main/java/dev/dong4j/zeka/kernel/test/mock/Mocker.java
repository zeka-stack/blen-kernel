package dev.dong4j.zeka.kernel.test.mock;

/**
 * <p>Description: 模拟器接口</p>
 *
 * @param <T> the type parameter
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.27 18:06
 * @since 1.0.0
 */
public interface Mocker<T> {

    /**
     * 模拟数据
     *
     * @param mockConfig 模拟数据配置
     * @return 模拟数据对象 t
     * @since 1.0.0
     */
    T mock(MockConfig mockConfig);

}
