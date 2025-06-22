package dev.dong4j.zeka.kernel.spi.utils;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * <p>Description: </p>
 *
 * @param <E> parameter
 * @author dong4j
 * @version 1.8.0
 * @email "mailto:dong4j@gmaidl.com"
 * @date 2021.02.26 17:47
 * @since 1.8.0
 */
@SuppressWarnings("all")
public class ConcurrentHashSet<E> extends AbstractSet<E> implements Set<E>, java.io.Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = -8672117787651310382L;

    /** PRESENT */
    private static final Object PRESENT = new Object();

    /** Map */
    private final ConcurrentMap<E, Object> map;

    /**
     * Concurrent hash set
     *
     * @since 1.8.0
     */
    public ConcurrentHashSet() {
        map = new ConcurrentHashMap<E, Object>();
    }

    /**
     * Concurrent hash set
     *
     * @param initialCapacity initial capacity
     * @since 1.8.0
     */
    public ConcurrentHashSet(int initialCapacity) {
        map = new ConcurrentHashMap<E, Object>(initialCapacity);
    }

    /**
     * Iterator
     *
     * @return the iterator
     * @since 1.8.0
     */
    @Override
    public Iterator<E> iterator() {
        return map.keySet().iterator();
    }

    /**
     * Size
     *
     * @return the int
     * @since 1.8.0
     */
    @Override
    public int size() {
        return map.size();
    }

    /**
     * Is empty
     *
     * @return the boolean
     * @since 1.8.0
     */
    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    /**
     * Contains
     *
     * @param o o
     * @return the boolean
     * @since 1.8.0
     */
    @Override
    public boolean contains(Object o) {
        return map.containsKey(o);
    }

    /**
     * Add
     *
     * @param e e
     * @return the boolean
     * @since 1.8.0
     */
    @Override
    public boolean add(E e) {
        return map.put(e, PRESENT) == null;
    }

    /**
     * Remove
     *
     * @param o o
     * @return the boolean
     * @since 1.8.0
     */
    @Override
    public boolean remove(Object o) {
        return map.remove(o) == PRESENT;
    }

    /**
     * Clear
     *
     * @since 1.8.0
     */
    @Override
    public void clear() {
        map.clear();
    }

}
