package dev.dong4j.zeka.kernel.common.type;

import dev.dong4j.zeka.kernel.common.type.exception.TypeException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import lombok.Getter;

/**
 * <p>类型令牌抽象类.
 * <p>用于在运行时获取泛型参数的实际类型信息，解决 Java 泛型擦除问题.
 * <p>通过继承该抽象类并传入具体的泛型参数，可以在运行时保留完整的类型信息.
 * <p>主要功能：
 * <ul>
 *     <li>捕获和保存泛型参数的实际类型</li>
 *     <li>在运行时提供完整的类型信息</li>
 *     <li>支持复杂泛型结构的解析</li>
 *     <li>防止泛型擦除导致的类型信息丢失</li>
 * </ul>
 * <p>使用场景：
 * <ul>
 *     <li>JSON 序列化和反序列化</li>
 *     <li>泛型集合的类型安全操作</li>
 *     <li>泛型反射和动态代理</li>
 *     <li>框架级别的类型处理</li>
 * </ul>
 * <p>使用示例：
 * <pre>{@code
 * TypeToken<List<String>> token = new TypeToken<List<String>>() {};
 * Type type = token.getType(); // 获取完整的类型信息
 * }</pre>
 *
 * @param <T> 泛型参数，表示需要捕获类型信息的目标类型
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.27 14:47
 * @since 1.0.0
 */
@SuppressWarnings("checkyle:HiddenField")
public abstract class TypeToken<T> {
    /** Type */
    @Getter
    private final Type type;

    /**
     * Instantiates a new Type token.
     *
     * @since 1.0.0
     */
    protected TypeToken() {
        Type superclass = this.getClass().getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new TypeException("No generics found!");
        }
        this.type = ((ParameterizedType) superclass).getActualTypeArguments()[0];
    }
}
