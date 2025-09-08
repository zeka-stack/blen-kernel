package dev.dong4j.zeka.kernel.common.context;

import java.io.Serializable;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * <p>Description: 只读类型的 map </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.06.04 19:27
 * @since 1.0.0
 */
public interface ReadOnlyStringMap extends Serializable {

    /**
     * 返回包含此数据结构快照的非空可变映射
     *
     * @return the map
     * @since 1.0.0
     */
    Map<String, String> toMap();

    /**
     * 如果此数据结构包含指定的键, 则返回true; 否则返回false.
     *
     * @param key key
     * @return the boolean
     * @since 1.0.0
     */
    boolean containsKey(String key);

    /**
     * 对该数据结构中的每个键值对执行给定操作, 直到处理完所有条目或操作引发异常.
     * 有些实现在遍历内容时可能不支持结构修改 (添加新元素或删除元素) .
     *
     * @param <V>    parameter
     * @param action action
     * @throws java.util.ConcurrentModificationException 执行 {@link BiConsumer#accept} 时抛出异常
     * @since 1.0.0
     */
    <V> void forEach(BiConsumer<String, ? super V> action);

    /**
     * 对该数据结构中的每个键值对执行给定操作, 直到处理完所有条目或操作引发异常.
     * 第三个参数允许调用者传入一个有状态的对象, 用键值对进行修改, 因此TriConsumer实现本身可以是无状态的, 并且可能是可重用的.
     * 有些实现在遍历内容时可能不支持结构修改 (添加新元素或删除元素) .
     *
     * @param <V>    parameter
     * @param <S>    parameter
     * @param action action
     * @param state  state
     * @throws java.util.ConcurrentModificationException 从 TriConsumer的 accept 方法中添加或删除元素可能会导致抛出此异常
     * @since 1.0.0
     */
    <V, S> void forEach(TriConsumer<String, ? super V, S> action, S state);

    /**
     * 返回指定键的值, 如果此集合中不存在指定键, 则返回null.
     *
     * @param <V> parameter
     * @param key the key whose value to return.
     * @return the value for the specified key or {@code null}.
     * @since 1.0.0
     */
    <V> V getValue(String key);

    /**
     * 如果此集合为空 (大小为零) , 则返回true; 否则返回false.
     *
     * @return {@code true} if this collection is empty (size is zero).
     * @since 1.0.0
     */
    boolean isEmpty();

    /**
     * 返回此集合中的键值对数.
     *
     * @return the number of key-value pairs in this collection.
     * @since 1.0.0
     */
    int size();
}
