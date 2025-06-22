package dev.dong4j.zeka.kernel.common.context;

import java.util.Map;

/**
 * //  * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.06.04 19:06
 * @since 1.4.0
 */
public interface ThreadContextMap {

    /**
     * Clears the context.
     *
     * @since 1.5.0
     */
    void clear();

    /**
     * Determines if the key is in the context.
     *
     * @param key The key to locate.
     * @return True if the key is in the context, false otherwise.
     * @since 1.5.0
     */
    boolean containsKey(String key);

    /**
     * Gets the context identified by the <code>key</code> parameter.
     *
     * <p>This method has no side effects.</p>
     *
     * @param key The key to locate.
     * @return The value associated with the key or null.
     * @since 1.5.0
     */
    String get(String key);

    /**
     * Gets a non-{@code null} mutable copy of current thread's context Map.
     *
     * @return a mutable copy of the context.
     * @since 1.5.0
     */
    Map<String, String> getCopy();

    /**
     * Returns an immutable view on the context Map or {@code null} if the context map is empty.
     *
     * @return an immutable context Map or {@code null}.
     * @since 1.5.0
     */
    Map<String, String> getImmutableMapOrNull();

    /**
     * Returns true if the Map is empty.
     *
     * @return true if the Map is empty, false otherwise.
     * @since 1.5.0
     */
    boolean isEmpty();

    /**
     * Puts a context value (the <code>o</code> parameter) as identified
     * with the <code>key</code> parameter into the current thread's
     * context map.
     *
     * <p>If the current thread does not have a context map it is
     * created as a side effect.</p>
     *
     * @param key   The key name.
     * @param value The key value.
     * @since 1.5.0
     */
    void put(String key, String value);

    /**
     * Removes the the context identified by the <code>key</code>
     * parameter.
     *
     * @param key The key to remove.
     * @since 1.5.0
     */
    void remove(String key);
}
