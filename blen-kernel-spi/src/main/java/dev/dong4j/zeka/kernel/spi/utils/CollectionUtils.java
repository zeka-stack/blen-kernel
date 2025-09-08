package dev.dong4j.zeka.kernel.spi.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmaidl.com"
 * @date 2021.02.26 17:47
 * @since 1.0.0
 */
@SuppressWarnings("all")
public final class CollectionUtils {

    /** SIMPLE_NAME_COMPARATOR */
    private static final Comparator<String> SIMPLE_NAME_COMPARATOR = (s1, s2) -> {
        if (s1 == null && s2 == null) {
            return 0;
        }
        if (s1 == null) {
            return -1;
        }
        if (s2 == null) {
            return 1;
        }
        int i1 = s1.lastIndexOf('.');
        if (i1 >= 0) {
            s1 = s1.substring(i1 + 1);
        }
        int i2 = s2.lastIndexOf('.');
        if (i2 >= 0) {
            s2 = s2.substring(i2 + 1);
        }
        return s1.compareToIgnoreCase(s2);
    };

    /**
     * Collection utils
     *
     * @since 1.0.0
     */
    private CollectionUtils() {
    }

    /**
     * Sort
     *
     * @param <T>  parameter
     * @param list list
     * @return the list
     * @since 1.0.0
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T> List<T> sort(List<T> list) {
        if (isNotEmpty(list)) {
            Collections.sort((List) list);
        }
        return list;
    }

    /**
     * Sort simple name
     *
     * @param list list
     * @return the list
     * @since 1.0.0
     */
    public static List<String> sortSimpleName(List<String> list) {
        if (list != null && list.size() > 0) {
            Collections.sort(list, SIMPLE_NAME_COMPARATOR);
        }
        return list;
    }

    /**
     * Split all
     *
     * @param list      list
     * @param separator separator
     * @return the map
     * @since 1.0.0
     */
    public static Map<String, Map<String, String>> splitAll(Map<String, List<String>> list, String separator) {
        if (list == null) {
            return null;
        }
        Map<String, Map<String, String>> result = new HashMap<>();
        for (Map.Entry<String, List<String>> entry : list.entrySet()) {
            result.put(entry.getKey(), split(entry.getValue(), separator));
        }
        return result;
    }

    /**
     * Join all
     *
     * @param map       map
     * @param separator separator
     * @return the map
     * @since 1.0.0
     */
    public static Map<String, List<String>> joinAll(Map<String, Map<String, String>> map, String separator) {
        if (map == null) {
            return null;
        }
        Map<String, List<String>> result = new HashMap<>();
        for (Map.Entry<String, Map<String, String>> entry : map.entrySet()) {
            result.put(entry.getKey(), join(entry.getValue(), separator));
        }
        return result;
    }

    /**
     * Split
     *
     * @param list      list
     * @param separator separator
     * @return the map
     * @since 1.0.0
     */
    public static Map<String, String> split(List<String> list, String separator) {
        if (list == null) {
            return null;
        }
        Map<String, String> map = new HashMap<>();
        if (list.isEmpty()) {
            return map;
        }
        for (String item : list) {
            int index = item.indexOf(separator);
            if (index == -1) {
                map.put(item, "");
            } else {
                map.put(item.substring(0, index), item.substring(index + 1));
            }
        }
        return map;
    }

    /**
     * Join
     *
     * @param map       map
     * @param separator separator
     * @return the list
     * @since 1.0.0
     */
    public static List<String> join(Map<String, String> map, String separator) {
        if (map == null) {
            return null;
        }
        List<String> list = new ArrayList<>();
        if (map.size() == 0) {
            return list;
        }
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (SpiStringUtils.isEmpty(value)) {
                list.add(key);
            } else {
                list.add(key + separator + value);
            }
        }
        return list;
    }

    /**
     * Join
     *
     * @param list      list
     * @param separator separator
     * @return the string
     * @since 1.0.0
     */
    public static String join(List<String> list, String separator) {
        StringBuilder sb = new StringBuilder();
        for (String ele : list) {
            if (sb.length() > 0) {
                sb.append(separator);
            }
            sb.append(ele);
        }
        return sb.toString();
    }

    /**
     * Map equals
     *
     * @param map1 map 1
     * @param map2 map 2
     * @return the boolean
     * @since 1.0.0
     */
    public static boolean mapEquals(Map<?, ?> map1, Map<?, ?> map2) {
        if (map1 == null && map2 == null) {
            return true;
        }
        if (map1 == null || map2 == null) {
            return false;
        }
        if (map1.size() != map2.size()) {
            return false;
        }
        for (Map.Entry<?, ?> entry : map1.entrySet()) {
            Object key = entry.getKey();
            Object value1 = entry.getValue();
            Object value2 = map2.get(key);
            if (!objectEquals(value1, value2)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Object equals
     *
     * @param obj1 obj 1
     * @param obj2 obj 2
     * @return the boolean
     * @since 1.0.0
     */
    private static boolean objectEquals(Object obj1, Object obj2) {
        if (obj1 == null && obj2 == null) {
            return true;
        }
        if (obj1 == null || obj2 == null) {
            return false;
        }
        return obj1.equals(obj2);
    }

    /**
     * To string map
     *
     * @param pairs pairs
     * @return the map
     * @since 1.0.0
     */
    public static Map<String, String> toStringMap(String... pairs) {
        Map<String, String> parameters = new HashMap<>();
        if (pairs.length > 0) {
            if (pairs.length % 2 != 0) {
                throw new IllegalArgumentException("pairs must be even.");
            }
            for (int i = 0; i < pairs.length; i = i + 2) {
                parameters.put(pairs[i], pairs[i + 1]);
            }
        }
        return parameters;
    }

    /**
     * To map
     *
     * @param <K>   parameter
     * @param <V>   parameter
     * @param pairs pairs
     * @return the map
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    public static <K, V> Map<K, V> toMap(Object... pairs) {
        Map<K, V> ret = new HashMap<>();
        if (pairs == null || pairs.length == 0) {
            return ret;
        }

        if (pairs.length % 2 != 0) {
            throw new IllegalArgumentException("Map pairs can not be odd number.");
        }
        int len = pairs.length / 2;
        for (int i = 0; i < len; i++) {
            ret.put((K) pairs[2 * i], (V) pairs[2 * i + 1]);
        }
        return ret;
    }

    /**
     * Is empty
     *
     * @param collection collection
     * @return the boolean
     * @since 1.0.0
     */
    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * Is not empty
     *
     * @param collection collection
     * @return the boolean
     * @since 1.0.0
     */
    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }

    /**
     * Is empty map
     *
     * @param map map
     * @return the boolean
     * @since 1.0.0
     */
    public static boolean isEmptyMap(Map map) {
        return map == null || map.size() == 0;
    }

    /**
     * Is not empty map
     *
     * @param map map
     * @return the boolean
     * @since 1.0.0
     */
    public static boolean isNotEmptyMap(Map map) {
        return !isEmptyMap(map);
    }

}
