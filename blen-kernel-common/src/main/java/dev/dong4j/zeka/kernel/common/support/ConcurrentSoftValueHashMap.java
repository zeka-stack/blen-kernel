package dev.dong4j.zeka.kernel.common.support;

import java.lang.ref.ReferenceQueue;
import java.util.Objects;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Concurrent strong key:K -> soft value:V map
 * 不允许使用空键;
 * 不允许使用空值;
 *
 * @param <K> parameter
 * @param <V> parameter
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.05.19 10:22
 * @since 1.0.0
 */
@SuppressWarnings(value = {"checkstyle:EqualsHashCode", "checkstyle:ModifierOrder"})
public final class ConcurrentSoftValueHashMap<K, V> extends ConcurrentRefValueHashMap<K, V> {
    /**
     * Create value reference
     *
     * @param key   key
     * @param value value
     * @return the value reference
     * @since 1.0.0
     */
    @Contract("_, _ -> new")
    @NotNull
    @Override
    ValueReference<K, V> createValueReference(@NotNull K key, @NotNull V value) {
        return new MySoftReference<>(key, value, this.myQueue);
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
    private final static class MySoftReference<K, V> extends SoftReference<V> implements ValueReference<K, V> {

        /** Key */
        private final K key;

        /**
         * My soft reference
         *
         * @param key      key
         * @param referent referent
         * @param q        q
         * @since 1.0.0
         */
        private MySoftReference(@NotNull K key, @NotNull V referent, @NotNull ReferenceQueue<V> q) {
            super(referent, q);
            this.key = key;
        }

        /**
         * Gets key *
         *
         * @return the key
         * @since 1.0.0
         */
        @Contract(pure = true)
        @NotNull
        @Override
        public K getKey() {
            return this.key;
        }

        /**
         * When referent is collected, equality should be identity-based (for the processQueues() remove this very same SoftValue)
         * otherwise it's just canonical equals on referents for replace(K,V,V) to work
         *
         * @param o o
         * @return the boolean
         * @since 1.0.0
         */
        @Contract(value = "null -> false", pure = true)
        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || this.getClass() != o.getClass()) {
                return false;
            }

            // noinspection unchecked
            ValueReference<K, V> that = (ValueReference<K, V>) o;

            V v = this.get();
            V thatV = that.get();
            return this.key.equals(that.getKey()) && v != null && v.equals(thatV);
        }

        /**
         * Hash code
         *
         * @return the int
         * @since 1.0.0
         */
        @Override
        public int hashCode() {
            return Objects.hash(this.key);
        }

    }
}
