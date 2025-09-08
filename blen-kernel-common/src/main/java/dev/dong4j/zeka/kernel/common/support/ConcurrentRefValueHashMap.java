package dev.dong4j.zeka.kernel.common.support;

import dev.dong4j.zeka.kernel.common.exception.LowestException;
import java.lang.ref.ReferenceQueue;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * <p>Description: </p>
 *
 * @param <K> parameter
 * @param <V> parameter
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.05.19 10:22
 * @since 1.0.0
 */
abstract class ConcurrentRefValueHashMap<K, V> implements ConcurrentMap<K, V> {
    /** My queue */
    protected final ReferenceQueue<V> myQueue = new ReferenceQueue<>();
    /** My map */
    private final ConcurrentMap<K, ValueReference<K, V>> myMap = new ConcurrentHashMap<>();

    /**
     * Put if absent
     *
     * @param key   key
     * @param value value
     * @return the v
     * @since 1.0.0
     */
    @Override
    public V putIfAbsent(@NotNull K key, @NotNull V value) {
        ValueReference<K, V> newRef = this.createValueReference(key, value);
        while (true) {
            this.processQueue();
            ValueReference<K, V> oldRef = this.myMap.putIfAbsent(key, newRef);
            if (oldRef == null) {
                return null;
            }
            V oldVal = oldRef.get();
            if (oldVal == null) {
                if (this.myMap.replace(key, oldRef, newRef)) {
                    return null;
                }
            } else {
                return oldVal;
            }
        }
    }

    /**
     * Create value reference
     *
     * @param key   key
     * @param value value
     * @return the value reference
     * @since 1.0.0
     */
    @NotNull
    abstract ValueReference<K, V> createValueReference(@NotNull K key, @NotNull V value);

    /**
     * Process queue
     *
     * @return the boolean
     * @since 1.0.0
     */
    @SuppressWarnings("UnusedReturnValue")
    boolean processQueue() {
        boolean processed = false;

        while (true) {
            @SuppressWarnings("unchecked")
            ValueReference<K, V> ref = (ValueReference<K, V>) this.myQueue.poll();
            if (ref == null) {
                break;
            }
            this.myMap.remove(ref.getKey(), ref);
            processed = true;
        }
        return processed;
    }

    /**
     * Remove
     *
     * @param key   key
     * @param value value
     * @return the boolean
     * @since 1.0.0
     */
    @Override
    public boolean remove(@NotNull Object key, @NotNull Object value) {
        this.processQueue();
        // noinspection unchecked
        return this.myMap.remove(key, this.createValueReference((K) key, (V) value));
    }

    /**
     * Replace
     *
     * @param key      key
     * @param oldValue old value
     * @param newValue new value
     * @return the boolean
     * @since 1.0.0
     */
    @Override
    public boolean replace(@NotNull K key, @NotNull V oldValue, @NotNull V newValue) {
        this.processQueue();
        return this.myMap.replace(key, this.createValueReference(key, oldValue), this.createValueReference(key, newValue));
    }

    /**
     * Replace
     *
     * @param key   key
     * @param value value
     * @return the v
     * @since 1.0.0
     */
    @Override
    public V replace(@NotNull K key, @NotNull V value) {
        this.processQueue();
        ValueReference<K, V> ref = this.myMap.replace(key, this.createValueReference(key, value));
        return ref == null ? null : ref.get();
    }

    /**
     * To string
     *
     * @return the string
     * @since 1.0.0
     */
    @Override
    public String toString() {
        return "map size:" + this.size() + " [" + join(this.entrySet(), ",") + "]";
    }

    /**
     * Size
     *
     * @return the int
     * @since 1.0.0
     */
    @Override
    public int size() {
        this.processQueue();
        return this.myMap.size();
    }

    /**
     * Is empty
     *
     * @return the boolean
     * @since 1.0.0
     */
    @Contract(pure = true)
    @Override
    public boolean isEmpty() {
        this.processQueue();
        return this.myMap.isEmpty();
    }

    /**
     * Contains key
     *
     * @param key key
     * @return the boolean
     * @since 1.0.0
     */
    @Contract(pure = true)
    @Override
    public boolean containsKey(@NotNull Object key) {
        throw new LowestException("containsKey() makes no sense for weak/soft map because GC can clear the value any moment now");
    }

    /**
     * Contains value
     *
     * @param value value
     * @return the boolean
     * @since 1.0.0
     */
    @Contract(pure = true)
    @Override
    public boolean containsValue(@NotNull Object value) {
        throw new LowestException("containsValue() makes no sense for weak/soft map because GC can clear the key any moment now");
    }

    /**
     * Get
     *
     * @param key key
     * @return the v
     * @since 1.0.0
     */
    @Override
    public V get(@NotNull Object key) {
        ValueReference<K, V> ref = this.myMap.get(key);
        if (ref == null) {
            return null;
        }
        return ref.get();
    }

    /**
     * Put
     *
     * @param key   key
     * @param value value
     * @return the v
     * @since 1.0.0
     */
    @Override
    public V put(@NotNull K key, @NotNull V value) {
        this.processQueue();
        ValueReference<K, V> oldRef = this.myMap.put(key, this.createValueReference(key, value));
        return oldRef != null ? oldRef.get() : null;
    }

    /**
     * Remove
     *
     * @param key key
     * @return the v
     * @since 1.0.0
     */
    @Override
    public V remove(@NotNull Object key) {
        this.processQueue();
        ValueReference<K, V> ref = this.myMap.remove(key);
        return ref == null ? null : ref.get();
    }

    /**
     * Put all
     *
     * @param t t
     * @since 1.0.0
     */
    @Override
    public void putAll(@NotNull Map<? extends K, ? extends V> t) {
        this.processQueue();
        for (Entry<? extends K, ? extends V> entry : t.entrySet()) {
            V v = entry.getValue();
            if (v != null) {
                K key = entry.getKey();
                this.put(key, v);
            }
        }
    }

    /**
     * Clear
     *
     * @since 1.0.0
     */
    @Override
    public void clear() {
        this.myMap.clear();
        this.processQueue();
    }

    /**
     * Key set
     *
     * @return the set
     * @since 1.0.0
     */
    @NotNull
    @Override
    public Set<K> keySet() {
        return this.myMap.keySet();
    }

    /**
     * Values
     *
     * @return the collection
     * @since 1.0.0
     */
    @NotNull
    @Override
    public Collection<V> values() {
        Collection<V> result = new ArrayList<>();
        Collection<ValueReference<K, V>> refs = this.myMap.values();
        for (ValueReference<K, V> ref : refs) {
            V value = ref.get();
            if (value != null) {
                result.add(value);
            }
        }
        return result;
    }

    /**
     * Entry set
     *
     * @return the set
     * @since 1.0.0
     */
    @NotNull
    @Override
    public Set<Entry<K, V>> entrySet() {
        Set<K> keys = this.keySet();
        Set<Entry<K, V>> entries = new HashSet<>();

        for (K key : keys) {
            V value = this.get(key);
            if (value != null) {
                entries.add(new Entry<>() {
                    @Override
                    public String toString() {
                        return "(" + this.getKey() + " : " + this.getValue() + ")";
                    }

                    @Contract(pure = true)
                    @Override
                    public K getKey() {
                        return key;
                    }

                    @Contract(pure = true)
                    @Override
                    public V getValue() {
                        return value;
                    }

                    @Override
                    public V setValue(@NotNull V value) {
                        throw new UnsupportedOperationException("setValue is not implemented");
                    }
                });
            }
        }

        return entries;
    }

    /**
     * Join
     *
     * @param items     items
     * @param separator separator
     * @return the string
     * @since 1.0.0
     */
    @NotNull
    @Contract(pure = true)
    public static String join(@NotNull Iterable<?> items, @NotNull String separator) {
        StringBuilder result = new StringBuilder();
        for (Object item : items) {
            result.append(item).append(separator);
        }
        if (!result.isEmpty()) {
            result.setLength(result.length() - separator.length());
        }
        return result.toString();
    }

    /**
     * <p>Description: </p>
     *
     * @param <K> parameter
     * @param <V> parameter
     * @author dong4j
     * @version 1.0.0
     * @email "mailto:dong4j@gmail.com"
     * @date 2020.05.19 10:22
     * @since 1.0.0
     */
    interface ValueReference<K, V> {
        /**
         * Gets key *
         *
         * @return the key
         * @since 1.0.0
         */
        @NotNull
        K getKey();

        /**
         * Get
         *
         * @return the v
         * @since 1.0.0
         */
        V get();
    }
}
