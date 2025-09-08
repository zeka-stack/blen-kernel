package dev.dong4j.zeka.kernel.common.context;

import com.alibaba.ttl.TransmittableThreadLocal;
import dev.dong4j.zeka.kernel.common.util.PropertiesUtils;
import dev.dong4j.zeka.kernel.common.util.StringPool;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * <p>Description: 实际的ThreadContext映射.
 * 每次更新时都会创建一个新的 ThreadContext 映射, 并且存储的映射始终是不可变的.
 * 这意味着可以将映射传递给其他线程, 而不必担心它会被更新. </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.06.04 19:28
 * @since 1.0.0
 */
public class DefaultThreadContextMap implements ThreadContextMap {

    /** INHERITABLE_MAP */
    public static final String INHERITABLE_MAP = "isThreadContextMapInheritable";

    /** Use map */
    private final boolean useMap;
    /** Local map */
    @SuppressWarnings("PMD.ThreadLocalShouldRemoveRule")
    private final ThreadLocal<Map<String, String>> localMap;

    /** inheritableMap */
    private static boolean inheritableMap;

    static {
        init();
    }

    /**
     * 使用 TransmittableThreadLocal 增强父子线程可传递性
     *
     * @param isMapEnabled is map enabled
     * @return the thread local
     * @since 1.0.0
     */
    static @NotNull
    ThreadLocal<Map<String, String>> createThreadLocalMap(boolean isMapEnabled) {
        if (inheritableMap) {
            return new TransmittableThreadLocal<>() {
                @Override
                protected Map<String, String> childValue(Map<String, String> parentValue) {
                    return parentValue != null && isMapEnabled
                        ? Map.copyOf(parentValue)
                        : null;
                }
            };
        }
        // if not inheritable, return plain ThreadLocal with null as initial value
        return new ThreadLocal<>();
    }

    /**
     * Init
     *
     * @since 1.0.0
     */
    static void init() {
        inheritableMap = PropertiesUtils.getProperties().getBooleanProperty(INHERITABLE_MAP);
    }

    /**
     * Default thread context map
     *
     * @since 1.0.0
     */
    public DefaultThreadContextMap() {
        this(true);
    }

    /**
     * Default thread context map
     *
     * @param useMap use map
     * @since 1.0.0
     */
    public DefaultThreadContextMap(boolean useMap) {
        this.useMap = useMap;
        this.localMap = createThreadLocalMap(useMap);
    }

    /**
     * Put
     *
     * @param key   key
     * @param value value
     * @since 1.0.0
     */
    @Override
    public void put(String key, String value) {
        if (!this.useMap) {
            return;
        }
        Map<String, String> map = this.localMap.get();
        map = map == null ? new HashMap<>(1) : new HashMap<>(map);
        map.put(key, value);
        this.localMap.set(Collections.unmodifiableMap(map));
    }

    /**
     * Put all
     *
     * @param m m
     * @since 1.0.0
     */
    public void putAll(Map<String, String> m) {
        if (!this.useMap) {
            return;
        }
        Map<String, String> map = this.localMap.get();
        map = map == null ? new HashMap<>(m.size()) : new HashMap<>(map);
        map.putAll(m);
        this.localMap.set(Collections.unmodifiableMap(map));
    }

    /**
     * Get
     *
     * @param key key
     * @return the string
     * @since 1.0.0
     */
    @Override
    public String get(String key) {
        Map<String, String> map = this.localMap.get();
        return map == null ? null : map.get(key);
    }

    /**
     * Remove
     *
     * @param key key
     * @since 1.0.0
     */
    @Override
    public void remove(String key) {
        Map<String, String> map = this.localMap.get();
        if (map != null) {
            Map<String, String> copy = new HashMap<>(map);
            copy.remove(key);
            this.localMap.set(Collections.unmodifiableMap(copy));
        }
    }

    /**
     * Remove all
     *
     * @param keys keys
     * @since 1.0.0
     */
    public void removeAll(Iterable<String> keys) {
        Map<String, String> map = this.localMap.get();
        if (map != null) {
            Map<String, String> copy = new HashMap<>(map);
            for (String key : keys) {
                copy.remove(key);
            }
            this.localMap.set(Collections.unmodifiableMap(copy));
        }
    }

    /**
     * Clear
     *
     * @since 1.0.0
     */
    @Override
    public void clear() {
        this.localMap.remove();
    }

    /**
     * To map
     *
     * @return the map
     * @since 1.0.0
     */
    public Map<String, String> toMap() {
        return this.getCopy();
    }

    /**
     * Contains key
     *
     * @param key key
     * @return the boolean
     * @since 1.0.0
     */
    @Override
    public boolean containsKey(String key) {
        Map<String, String> map = this.localMap.get();
        return map != null && map.containsKey(key);
    }

    /**
     * For each
     *
     * @param <V>    parameter
     * @param action action
     * @since 1.0.0
     */
    public <V> void forEach(BiConsumer<String, ? super V> action) {
        Map<String, String> map = this.localMap.get();
        if (map == null) {
            return;
        }
        for (Map.Entry<String, String> entry : map.entrySet()) {
            // BiConsumer should be able to handle values of any type V. In our case the values are of type String.
            @SuppressWarnings("unchecked") V value = (V) entry.getValue();
            action.accept(entry.getKey(), value);
        }
    }

    /**
     * For each
     *
     * @param <V>    parameter
     * @param <S>    parameter
     * @param action action
     * @param state  state
     * @since 1.0.0
     */
    public <V, S> void forEach(TriConsumer<String, ? super V, S> action, S state) {
        Map<String, String> map = this.localMap.get();
        if (map == null) {
            return;
        }
        for (Map.Entry<String, String> entry : map.entrySet()) {
            @SuppressWarnings("unchecked") V value = (V) entry.getValue();
            action.accept(entry.getKey(), value, state);
        }
    }

    /**
     * Gets value *
     *
     * @param <V> parameter
     * @param key key
     * @return the value
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    public <V> V getValue(String key) {
        Map<String, String> map = this.localMap.get();
        return (V) (map == null ? null : map.get(key));
    }

    /**
     * Gets copy *
     *
     * @return the copy
     * @since 1.0.0
     */
    @Override
    public Map<String, String> getCopy() {
        Map<String, String> map = this.localMap.get();
        return map == null ? Collections.emptyMap() : new HashMap<>(map);
    }

    /**
     * Gets immutable map or null *
     *
     * @return the immutable map or null
     * @since 1.0.0
     */
    @Override
    public Map<String, String> getImmutableMapOrNull() {
        return this.localMap.get();
    }

    /**
     * Is empty
     *
     * @return the boolean
     * @since 1.0.0
     */
    @Override
    public boolean isEmpty() {
        Map<String, String> map = this.localMap.get();
        return map == null || map.isEmpty();
    }

    /**
     * Size
     *
     * @return the int
     * @since 1.0.0
     */
    public int size() {
        Map<String, String> map = this.localMap.get();
        return map == null ? 0 : map.size();
    }

    /**
     * To string
     *
     * @return the string
     * @since 1.0.0
     */
    @Override
    public String toString() {
        Map<String, String> map = this.localMap.get();
        return map == null ? StringPool.EMPTY_JSON : map.toString();
    }

    /**
     * Hash code
     *
     * @return the int
     * @since 1.0.0
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        Map<String, String> map = this.localMap.get();
        result = prime * result + ((map == null) ? 0 : map.hashCode());
        result = prime * result + Boolean.valueOf(this.useMap).hashCode();
        return result;
    }

    /**
     * Equals
     *
     * @param obj obj
     * @return the boolean
     * @since 1.0.0
     */
    @Contract(value = "null -> false", pure = true)
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (obj instanceof DefaultThreadContextMap other) {
            if (this.useMap != other.useMap) {
                return false;
            }
        }
        if (!(obj instanceof ThreadContextMap other)) {
            return false;
        }
        Map<String, String> map = this.localMap.get();
        Map<String, String> otherMap = other.getImmutableMapOrNull();

        return Objects.equals(map, otherMap);
    }
}
