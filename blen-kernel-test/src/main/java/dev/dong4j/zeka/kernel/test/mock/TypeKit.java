package dev.dong4j.zeka.kernel.test.mock;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * <p>Description: </p>
 * 类型工具  抽象类包装 泛型类
 * 用以获取泛型的类型
 *
 * @param <T> the type parameter
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.27 18:22
 * @since 1.0.0
 */
public abstract class TypeKit<T> {

    /** Type */
    private final Type type;

    /**
     * Instantiates a new Type kit.
     *
     * @since 1.0.0
     */
    public TypeKit() {
        Type superClass = getClass().getGenericSuperclass();
        if (superClass instanceof Class) {
            throw new MockException("不支持的类型或者检查参数是否已经添加{},eg: Mock.mock(new TypeKit<Integer>(){})");
        }
        type = ((ParameterizedType) superClass).getActualTypeArguments()[0];
    }

    /**
     * Gets type.
     *
     * @return the type
     * @since 1.0.0
     */
    public Type getType() {
        return type;
    }

}
