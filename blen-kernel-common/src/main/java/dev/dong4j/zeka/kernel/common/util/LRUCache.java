package dev.dong4j.zeka.kernel.common.util;

import java.util.LinkedHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <p>Description: </p>
 *
 * @param <K> parameter
 * @param <V> parameter
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.07.02 11:58
 * @since 1.5.0
 */
@SuppressWarnings("all")
public class LRUCache<K, V> extends LinkedHashMap<K, V> {

    /** serialVersionUID */
    private static final long serialVersionUID = -5167631809472116969L;
    /** DEFAULT_LOAD_FACTOR */
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    /** DEFAULT_MAX_CAPACITY */
    private static final int DEFAULT_MAX_CAPACITY = 1000;
    /** Lock */
    private final Lock lock = new ReentrantLock();
    /** Max capacity */
    private volatile int maxCapacity;

    /**
     * Lru cache
     *
     * @since 1.5.0
     */
    public LRUCache() {
        this(DEFAULT_MAX_CAPACITY);
    }

    /**
     * Lru cache
     *
     * @param maxCapacity max capacity
     * @since 1.5.0
     */
    public LRUCache(int maxCapacity) {
        super(16, DEFAULT_LOAD_FACTOR, true);
        this.maxCapacity = maxCapacity;
    }

    /**
     * Remove eldest entry
     *
     * @param eldest eldest
     * @return the boolean
     * @since 1.5.0
     */
    @Override
    protected boolean removeEldestEntry(java.util.Map.Entry<K, V> eldest) {
        return this.size() > this.maxCapacity;
    }

    /**
     * Contains key
     *
     * @param key key
     * @return the boolean
     * @since 1.5.0
     */
    @Override
    public boolean containsKey(Object key) {
        this.lock.lock();
        try {
            return super.containsKey(key);
        } finally {
            this.lock.unlock();
        }
    }

    /**
     * Get
     *
     * @param key key
     * @return the v
     * @since 1.5.0
     */
    @Override
    public V get(Object key) {
        this.lock.lock();
        try {
            return super.get(key);
        } finally {
            this.lock.unlock();
        }
    }

    /**
     * Put
     *
     * @param key   key
     * @param value value
     * @return the v
     * @since 1.5.0
     */
    @Override
    public V put(K key, V value) {
        this.lock.lock();
        try {
            return super.put(key, value);
        } finally {
            this.lock.unlock();
        }
    }

    /**
     * Remove
     *
     * @param key key
     * @return the v
     * @since 1.5.0
     */
    @Override
    public V remove(Object key) {
        this.lock.lock();
        try {
            return super.remove(key);
        } finally {
            this.lock.unlock();
        }
    }

    /**
     * Size
     *
     * @return the int
     * @since 1.5.0
     */
    @Override
    public int size() {
        this.lock.lock();
        try {
            return super.size();
        } finally {
            this.lock.unlock();
        }
    }

    /**
     * Clear
     *
     * @since 1.5.0
     */
    @Override
    public void clear() {
        this.lock.lock();
        try {
            super.clear();
        } finally {
            this.lock.unlock();
        }
    }

    /**
     * Gets max capacity *
     *
     * @return the max capacity
     * @since 1.5.0
     */
    public int getMaxCapacity() {
        return this.maxCapacity;
    }

    /**
     * Sets max capacity *
     *
     * @param maxCapacity max capacity
     * @since 1.5.0
     */
    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

}
