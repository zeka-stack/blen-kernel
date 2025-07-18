package dev.dong4j.zeka.kernel.common.support;

import com.google.common.collect.Maps;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * <p>Description: 线程安全的 Map 工厂类</p>
 *
 * @param <K> parameter
 * @param <V> parameter
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.05.19 10:04
 * @since 1.4.0
 */
@SuppressWarnings("checkstyle:ModifierOrder")
public abstract class ConcurrentFactoryMap<K, V> implements ConcurrentMap<K, V> {
    /** NULL */
    private static final Object NULL = sentinel("ObjectUtils.NULL");
    /** My map */
    private final ConcurrentMap<K, V> myMap = this.createMap();

    /**
     * Concurrent factory map
     *
     * @since 1.4.0
     */
    private ConcurrentFactoryMap() {

    }

    /**
     * Sentinel
     *
     * @param name name
     * @return the object
     * @since 1.4.0
     */
    @Contract(value = "_ -> new", pure = true)
    @NotNull
    public static Object sentinel(@NotNull @NonNls String name) {
        return new Sentinel(name);
    }

    /**
     * Create map
     *
     * @param <T>          parameter
     * @param <V>          parameter
     * @param computeValue compute value
     * @return the concurrent map
     * @since 1.4.0
     */
    @Contract("_ -> new")
    @NotNull
    public static <T, V> ConcurrentMap<T, V> createMap(@NotNull Function<? super T, ? extends V> computeValue) {
        return new ConcurrentFactoryMap<T, V>() {
            @Nullable
            @Override
            protected V create(T key) {
                return computeValue.apply(key);
            }
        };
    }

    /**
     * Create weak map
     *
     * @param <T>     parameter
     * @param <V>     parameter
     * @param compute compute
     * @return Concurrent factory map with weak keys, strong values
     * @since 1.4.0
     */
    @NotNull
    public static <T, V> ConcurrentMap<T, V> createWeakMap(@NotNull Function<? super T, ? extends V> compute) {
        return create(compute, ConcurrentHashMap::new);
    }

    /**
     * Create
     *
     * @param <K>          parameter
     * @param <V>          parameter
     * @param computeValue compute value
     * @param mapCreator   map creator
     * @return the concurrent map
     * @since 1.4.0
     */
    @Contract("_, _ -> new")
    @NotNull
    public static <K, V> ConcurrentMap<K, V> create(@NotNull Function<? super K, ? extends V> computeValue,
                                                    @NotNull Supplier<? extends ConcurrentMap<K, V>> mapCreator) {
        return new ConcurrentFactoryMap<K, V>() {
            @Nullable
            @Override
            protected V create(K key) {
                return computeValue.apply(key);
            }

            @NotNull
            @Override
            protected ConcurrentMap<K, V> createMap() {
                return mapCreator.get();
            }
        };
    }

    /**
     * Remove value
     *
     * @param value value
     * @return the boolean
     * @since 1.4.0
     */
    public boolean removeValue(Object value) {
        Object t = notNull(value);
        // noinspection SuspiciousMethodCalls
        return this.myMap.values().remove(t);
    }

    /**
     * Size
     *
     * @return the int
     * @since 1.4.0
     */
    @Override
    public int size() {
        return this.myMap.size();
    }

    /**
     * Is empty
     *
     * @return the boolean
     * @since 1.4.0
     */
    @Contract(pure = true)
    @Override
    public boolean isEmpty() {
        return this.myMap.isEmpty();
    }

    /**
     * Contains key
     *
     * @param key key
     * @return the boolean
     * @since 1.4.0
     */
    @Contract(pure = true)
    @Override
    public final boolean containsKey(Object key) {
        return this.myMap.containsKey(notNull(key));
    }

    /**
     * Contains value
     *
     * @param value value
     * @return the boolean
     * @since 1.4.0
     */
    @Contract(pure = true)
    @Override
    public boolean containsValue(Object value) {
        return this.myMap.containsValue(notNull(value));
    }

    /**
     * Get
     *
     * @param key key
     * @return the v
     * @since 1.4.0
     */
    @Override
    public V get(Object key) {
        ConcurrentMap<K, V> map = this.myMap;
        K k = notNull(key);
        V value = map.get(k);
        if (value == null) {
            // noinspection unchecked
            value = this.create((K) key);
            V v = notNull(value);
            value = cacheOrGet(map, k, v);
        }
        return nullize(value);
    }

    /**
     * Not null
     *
     * @param <T> parameter
     * @param key key
     * @return the t
     * @since 1.4.0
     */
    @Contract(value = "!null -> param1", pure = true)
    private static <T> T notNull(Object key) {
        // noinspection unchecked
        return key == null ? fakeNull() : (T) key;
    }

    /**
     * Create
     *
     * @param key key
     * @return the v
     * @since 1.4.0
     */
    @Nullable
    protected abstract V create(K key);

    /**
     * Cache or get
     *
     * @param <K>          parameter
     * @param <V>          parameter
     * @param map          map
     * @param key          key
     * @param defaultValue default value
     * @return the v
     * @since 1.4.0
     */
    @NotNull
    public static <K, V> V cacheOrGet(@NotNull ConcurrentMap<K, V> map, @NotNull K key, @NotNull V defaultValue) {
        V v = map.get(key);
        if (v != null) {
            return v;
        }
        V prev = map.putIfAbsent(key, defaultValue);
        return prev == null ? defaultValue : prev;
    }

    /**
     * Nullize
     *
     * @param <T>   parameter
     * @param value value
     * @return the t
     * @since 1.4.0
     */
    @Contract(pure = true)
    @Nullable
    private static <T> T nullize(T value) {
        return value == fakeNull() ? null : value;
    }

    /**
     * Fake null
     *
     * @param <T> parameter
     * @return the t
     * @since 1.4.0
     */
    @Contract(pure = true)
    @SuppressWarnings("unchecked")
    private static <T> T fakeNull() {
        return (T) NULL;
    }

    /**
     * Put
     *
     * @param key   key
     * @param value value
     * @return the v
     * @since 1.4.0
     */
    @Override
    public V put(K key, V value) {
        K k = notNull(key);
        V v = notNull(value);
        v = this.myMap.put(k, v);
        return nullize(v);
    }

    /**
     * Remove
     *
     * @param key key
     * @return the v
     * @since 1.4.0
     */
    @Override
    public V remove(Object key) {
        V v = this.myMap.remove(notNull(key));
        return nullize(v);
    }

    /**
     * Put all
     *
     * @param m m
     * @since 1.4.0
     */
    @Override
    public void putAll(@NotNull Map<? extends K, ? extends V> m) {
        for (Entry<? extends K, ? extends V> entry : m.entrySet()) {
            this.put(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Clear
     *
     * @since 1.4.0
     */
    @Override
    public void clear() {
        this.myMap.clear();
    }

    /**
     * Key set
     *
     * @return the set
     * @since 1.4.0
     */
    @NotNull
    @Override
    public Set<K> keySet() {
        return new CollectionWrapper.Set<>(this.myMap.keySet());
    }

    /**
     * Values
     *
     * @return the collection
     * @since 1.4.0
     */
    @NotNull
    @Override
    public Collection<V> values() {
        return new CollectionWrapper<>(this.myMap.values());
    }

    /**
     * Entry set
     *
     * @return the set
     * @since 1.4.0
     */
    @NotNull
    @Override
    public Set<Entry<K, V>> entrySet() {
        return new CollectionWrapper.Set<Entry<K, V>>(this.myMap.entrySet()) {
            @Override
            public Object wrap(Object val) {
                // noinspection unchecked
                return val instanceof EntryWrapper ? ((EntryWrapper<K, V>) val).myEntry : val;
            }

            @Override
            public Entry<K, V> unwrap(Entry<K, V> val) {
                return val.getKey() == fakeNull() || val.getValue() == fakeNull() ? new EntryWrapper<>(val) : val;
            }
        };
    }

    /**
     * Create map
     *
     * @return the concurrent map
     * @since 1.4.0
     */
    @NotNull
    protected ConcurrentMap<K, V> createMap() {
        return Maps.newConcurrentMap();
    }

    /**
     * Put if absent
     *
     * @param key   key
     * @param value value
     * @return the v
     * @since 1.4.0
     */
    @Override
    public V putIfAbsent(@NotNull K key, V value) {
        return nullize(this.myMap.putIfAbsent(notNull(key), notNull(value)));
    }

    /**
     * Remove
     *
     * @param key   key
     * @param value value
     * @return the boolean
     * @since 1.4.0
     */
    @Override
    public boolean remove(@NotNull Object key, Object value) {
        return this.myMap.remove(ConcurrentFactoryMap.<K>notNull(key), ConcurrentFactoryMap.<V>notNull(value));
    }

    /**
     * Replace
     *
     * @param key      key
     * @param oldValue old value
     * @param newValue new value
     * @return the boolean
     * @since 1.4.0
     */
    @Override
    public boolean replace(@NotNull K key, @NotNull V oldValue, @NotNull V newValue) {
        return this.myMap.replace(notNull(key), notNull(oldValue), notNull(newValue));
    }

    /**
     * Replace
     *
     * @param key   key
     * @param value value
     * @return the v
     * @since 1.4.0
     */
    @Override
    public V replace(@NotNull K key, @NotNull V value) {
        return nullize(this.myMap.replace(notNull(key), notNull(value)));
    }

    /**
     * To string
     *
     * @return the string
     * @since 1.4.0
     */
    @Override
    public String toString() {
        return this.myMap.toString();
    }

    /**
     * <p>Description: </p>
     *
     * @param <K> parameter
     * @author dong4j
     * @version 1.0.0
     * @email "mailto:dong4j@gmail.com"
     * @date 2020.05.19 10:04
     * @since 1.4.0
     */
    private static class CollectionWrapper<K> extends AbstractCollection<K> {
        /** My delegate */
        private final Collection<K> myDelegate;

        /**
         * Collection wrapper
         *
         * @param delegate delegate
         * @since 1.4.0
         */
        CollectionWrapper(Collection<K> delegate) {
            this.myDelegate = delegate;
        }

        /**
         * Iterator
         *
         * @return the iterator
         * @since 1.4.0
         */
        @NotNull
        @Override
        public Iterator<K> iterator() {
            return new Iterator<K>() {
                final Iterator<K> it = CollectionWrapper.this.myDelegate.iterator();

                @Override
                public boolean hasNext() {
                    return this.it.hasNext();
                }

                @Override
                public K next() {
                    return CollectionWrapper.this.unwrap(this.it.next());
                }

                @Override
                public void remove() {
                    this.it.remove();
                }
            };
        }

        /**
         * Size
         *
         * @return the int
         * @since 1.4.0
         */
        @Override
        public int size() {
            return this.myDelegate.size();
        }

        /**
         * Contains
         *
         * @param o o
         * @return the boolean
         * @since 1.4.0
         */
        @Override
        public boolean contains(Object o) {
            return this.myDelegate.contains(this.wrap(o));
        }

        /**
         * Remove
         *
         * @param o o
         * @return the boolean
         * @since 1.4.0
         */
        @Override
        public boolean remove(Object o) {
            return this.myDelegate.remove(this.wrap(o));
        }

        /**
         * Wrap
         *
         * @param val val
         * @return the object
         * @since 1.4.0
         */
        protected Object wrap(Object val) {
            return notNull(val);
        }

        /**
         * Unwrap
         *
         * @param val val
         * @return the k
         * @since 1.4.0
         */
        protected K unwrap(K val) {
            return nullize(val);
        }

        /**
         * <p>Description: </p>
         *
         * @param <K> parameter
         * @author dong4j
         * @version 1.0.0
         * @email "mailto:dong4j@gmail.com"
         * @date 2020.05.19 10:04
         * @since 1.4.0
         */
        private static class Set<K> extends CollectionWrapper<K> implements java.util.Set<K> {
            /**
             * Set
             *
             * @param delegate delegate
             * @since 1.4.0
             */
            Set(Collection<K> delegate) {
                super(delegate);
            }
        }

        /**
         * <p>Description: </p>
         *
         * @param <K> parameter
         * @param <V> parameter
         * @author dong4j
         * @version 1.0.0
         * @email "mailto:dong4j@gmail.com"
         * @date 2020.05.19 10:04
         * @since 1.4.0
         */
        protected static final class EntryWrapper<K, V> implements Entry<K, V> {
            /** My entry */
            final Entry<? extends K, ? extends V> myEntry;

            /**
             * Entry wrapper
             *
             * @param entry entry
             * @since 1.4.0
             */
            @Contract(pure = true)
            private EntryWrapper(Entry<? extends K, ? extends V> entry) {
                this.myEntry = entry;
            }

            /**
             * Gets key *
             *
             * @return the key
             * @since 1.4.0
             */
            @Override
            public K getKey() {
                return nullize(this.myEntry.getKey());
            }

            /**
             * Gets value *
             *
             * @return the value
             * @since 1.4.0
             */
            @Override
            public V getValue() {
                return nullize(this.myEntry.getValue());
            }

            /**
             * Sets value *
             *
             * @param value value
             * @return the value
             * @since 1.4.0
             */
            @Override
            public V setValue(V value) {
                return this.myEntry.setValue(notNull(value));
            }

            /**
             * Hash code
             *
             * @return the int
             * @since 1.4.0
             */
            @Override
            public int hashCode() {
                return this.myEntry.hashCode();
            }

            /**
             * Equals
             *
             * @param obj obj
             * @return the boolean
             * @since 1.4.0
             */
            @Override
            public boolean equals(Object obj) {
                // noinspection unchecked
                return this.myEntry.equals(obj instanceof EntryWrapper ? ((EntryWrapper<K, V>) obj).myEntry : obj);
            }
        }
    }

    /**
     * <p>Description: </p>
     *
     * @author dong4j
     * @version 1.0.0
     * @email "mailto:dong4j@gmail.com"
     * @date 2020.05.19 10:04
     * @since 1.4.0
     */
    private static final class Sentinel {
        /** My name */
        private final String myName;

        /**
         * Sentinel
         *
         * @param name name
         * @since 1.4.0
         */
        Sentinel(@NotNull String name) {
            this.myName = name;
        }

        /**
         * To string
         *
         * @return the string
         * @since 1.4.0
         */
        @Override
        public String toString() {
            return this.myName;
        }
    }
}
