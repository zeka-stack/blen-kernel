package dev.dong4j.zeka.kernel.common.util;

import com.google.common.collect.Maps;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>Description: 集合工具类 </p>
 *
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dong4j@gmail.com"
 * @date 2019.12.24 15:24
 * @since 1.0.0
 */
@UtilityClass
@SuppressWarnings("java:S2176")
public class CollectionUtils extends org.springframework.util.CollectionUtils {

    /** THREAD_LOCAL_RANDOM */
    public static final ThreadLocalRandom THREAD_LOCAL_RANDOM = ThreadLocalRandom.current();

    /**
     * Return {@code true} if the supplied Collection is not {@code null} or empty.
     * Otherwise, return {@code false}.
     *
     * @param collection the Collection to check
     * @return whether the given Collection is not empty
     * @since 1.0.0
     */
    @Contract("null -> false")
    public static boolean isNotEmpty(@Nullable Collection<?> collection) {
        return !isEmpty(collection);
    }

    /**
     * Return {@code true} if the supplied Map is not {@code null} or empty.
     * Otherwise, return {@code false}.
     *
     * @param map the Map to check
     * @return whether the given Map is not empty
     * @since 1.0.0
     */
    @Contract("null -> false")
    public static boolean isNotEmpty(@Nullable Map<?, ?> map) {
        return !isEmpty(map);
    }

    /**
     * Check whether the given Array contains the given element.
     *
     * @param <T>     The generic tag
     * @param array   the Array to check
     * @param element the element to look for
     * @return {@code true} if found, {@code false} else
     * @since 1.0.0
     */
    @Contract("null, _ -> false")
    public static <T> boolean contains(@Nullable T[] array, T element) {
        if (array == null) {
            return false;
        }
        return Arrays.stream(array).anyMatch(x -> ObjectUtils.nullSafeEquals(x, element));
    }

    /**
     * Concatenates 2 arrays
     *
     * @param one   数组1
     * @param other 数组2
     * @return 新数组 string [ ]
     * @since 1.0.0
     */
    public static String[] concat(String[] one, String[] other) {
        return concat(one, other, String.class);
    }

    /**
     * Concatenates 2 arrays
     *
     * @param <T>   the type parameter
     * @param one   数组1
     * @param other 数组2
     * @param clazz 数组类
     * @return 新数组 t [ ]
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] concat(@NotNull T[] one, @NotNull T[] other, Class<T> clazz) {
        T[] target = (T[]) Array.newInstance(clazz, one.length + other.length);
        System.arraycopy(one, 0, target, 0, one.length);
        System.arraycopy(other, 0, target, one.length, other.length);
        return target;
    }

    /**
     * 不可变 Set
     *
     * @param <E> 泛型
     * @param es  对象
     * @return 集合 set
     * @since 1.0.0
     */
    public static <E> Set<E> ofImmutableSet(E[] es) {
        Objects.requireNonNull(es, "args es is null.");
        return Arrays.stream(es).collect(Collectors.toSet());
    }

    /**
     * 不可变 List
     *
     * @param <E> 泛型
     * @param es  对象
     * @return 集合 list
     * @since 1.0.0
     */
    public static <E> List<E> ofImmutableList(E[] es) {
        Objects.requireNonNull(es, "args es is null.");
        return Arrays.stream(es).collect(Collectors.toList());
    }

    /**
     * Iterable 转换为List集合
     *
     * @param <E>      泛型
     * @param elements Iterable
     * @return 集合 list
     * @since 1.0.0
     */
    public static <E> List<E> toList(Iterable<E> elements) {
        Objects.requireNonNull(elements, "elements es is null.");
        if (elements instanceof Collection) {
            return new ArrayList<>((Collection<E>) elements);
        }
        Iterator<E> iterator = elements.iterator();
        List<E> list = new ArrayList<>();
        while (iterator.hasNext()) {
            list.add(iterator.next());
        }
        return list;
    }

    /**
     * 将key value 数组转为 map
     *
     * @param <K>        key
     * @param <V>        value
     * @param keysValues key value 数组
     * @return map 集合
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    public static <K, V> Map<K, V> toMap(@NotNull Object... keysValues) {
        int separate = 2;
        if (keysValues.length % separate != 0) {
            throw new IllegalArgumentException("wrong number of arguments for met, keysValues length can not be odd");
        }
        Map<K, V> keyValueMap = Maps.newHashMapWithExpectedSize(keysValues.length / 2);
        for (int i = keysValues.length - separate; i >= 0; i -= separate) {
            keyValueMap.put((K) keysValues[i], (V) keysValues[i + 1]);
        }
        return keyValueMap;
    }

    /**
     * 对象是否为数组对象
     *
     * @param obj 对象
     * @return 是否为数组对象 ,如果为{@code null} 返回false
     * @since 1.0.0
     */
    @Contract("null -> false")
    public static boolean isArray(Object obj) {
        if (null == obj) {
            return false;
        }
        return obj.getClass().isArray();
    }

    /**
     * Find value of type t
     *
     * @param <T>  parameter
     * @param map  map
     * @param key  key
     * @param type type
     * @return the t
     * @since 1.0.0
     */
    @Contract("null, _, _ -> null")
    @Nullable
    public static <T> T findValueOfType(Map<String, Object> map, String key, @Nullable Class<T> type) {
        if (map == null || !map.containsKey(key) || StringUtils.isBlank(String.valueOf(map.get(key)))) {
            return null;
        }
        return DataTypeUtils.convert(type, map.get(key));
    }

    /**
     * 通过 map 的 value 排序
     *
     * @param <K> parameter
     * @param <V> parameter
     * @param map map
     * @param asc asc
     * @return the map
     * @version jdk1.8
     * @since 1.0.0
     */
    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(@NotNull Map<K, V> map, boolean asc) {
        Map<K, V> result = new LinkedHashMap<>();
        Stream<Map.Entry<K, V>> stream = map.entrySet().stream();
        // 升序
        if (asc) {
            stream.sorted(Map.Entry.comparingByValue())
                .forEachOrdered(e -> result.put(e.getKey(), e.getValue()));
        } else {
            // 降序
            stream.sorted(Map.Entry.<K, V>comparingByValue().reversed())
                .forEachOrdered(e -> result.put(e.getKey(), e.getValue()));
        }
        return result;
    }

    /**
     * 通过 map 的 key 排序
     *
     * @param <K> parameter
     * @param <V> parameter
     * @param map map
     * @param asc asc
     * @return the map
     * @version jdk1.8
     * @since 1.0.0
     */
    public static <K extends Comparable<? super K>, V> Map<K, V> sortByKey(@NotNull Map<K, V> map, boolean asc) {
        Map<K, V> result = new LinkedHashMap<>();
        Stream<Map.Entry<K, V>> stream = map.entrySet().stream();
        if (asc) {
            stream.sorted(Map.Entry.comparingByKey())
                .forEachOrdered(e -> result.put(e.getKey(), e.getValue()));
        } else {
            stream.sorted(Map.Entry.<K, V>comparingByKey().reversed())
                .forEachOrdered(e -> result.put(e.getKey(), e.getValue()));
        }
        return result;
    }

    /**
     * 获取多条集合中随机值
     *
     * @param <T>  parameter
     * @param list list
     * @param num  num
     * @return the random list
     * @Description list 随机取数据
     * @params list list集合           num     随机取多少条
     * @since 2.0.0
     */
    public static <T> List<T> getRandomList(@NotNull List<T> list, int num) {
        List<T> randomList = new ArrayList<>();
        if (list.size() <= num) {
            return list;
        } else {
            for (int i = 0; i < num; i++) {
                int intRandom = THREAD_LOCAL_RANDOM.nextInt(list.size() - 1);
                randomList.add(list.get(intRandom));
                list.remove(list.get(intRandom));
            }
            return randomList;
        }
    }

    /**
     * 获取集合中随机值
     *
     * @param <T>  parameter
     * @param list list
     * @return the random list
     * @Description list 随机取数据
     * @params list list集合           num     随机取多少条
     * @since 2.0.0
     */
    public static <T> T getRandom(@NotNull List<T> list) {
        if (isEmpty(list)) {
            return null;
        }
        int size = list.size();
        if (size == 1) {
            return list.get(0);
        }
        return list.get(THREAD_LOCAL_RANDOM.nextInt(size - 1));
    }

}
