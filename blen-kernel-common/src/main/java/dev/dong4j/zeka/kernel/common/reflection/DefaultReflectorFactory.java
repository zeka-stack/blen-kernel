package dev.dong4j.zeka.kernel.common.reflection;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.3.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.04.12 11:54
 * @since 1.0.0
 */
public class DefaultReflectorFactory implements ReflectorFactory {
    /** Reflector map */
    private final ConcurrentMap<Class<?>, Reflector> reflectorMap = new ConcurrentHashMap<>();
    /** Class cache enabled */
    private boolean classCacheEnabled = true;

    /**
     * Is class cache enabled boolean
     *
     * @return the boolean
     * @since 1.0.0
     */
    @Override
    public boolean isClassCacheEnabled() {
        return this.classCacheEnabled;
    }

    /**
     * Sets class cache enabled *
     *
     * @param classCacheEnabled class cache enabled
     * @since 1.0.0
     */
    @Override
    public void setClassCacheEnabled(boolean classCacheEnabled) {
        this.classCacheEnabled = classCacheEnabled;
    }

    /**
     * Find for class reflector
     *
     * @param type type
     * @return the reflector
     * @since 1.0.0
     */
    @Override
    public Reflector findForClass(Class<?> type) {
        if (this.classCacheEnabled) {
            // synchronized (type) removed see issue #461
            return this.reflectorMap.computeIfAbsent(type, Reflector::new);
        } else {
            return new Reflector(type);
        }
    }

}
