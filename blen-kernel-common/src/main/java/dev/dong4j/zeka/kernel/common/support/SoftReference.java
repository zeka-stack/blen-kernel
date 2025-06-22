package dev.dong4j.zeka.kernel.common.support;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;

/**
 * <p>Description: </p>
 *
 * @param <T> parameter
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.05.19 09:29
 * @since 1.4.0
 */
@SuppressWarnings("ClassNameSameAsAncestorName")
public class SoftReference<T> extends java.lang.ref.SoftReference<T> {

    /**
     * Soft reference
     *
     * @param referent referent
     * @since 1.4.0
     */
    public SoftReference(T referent) {
        super(referent);
    }

    /**
     * Soft reference
     *
     * @param referent referent
     * @param q        q
     * @since 1.4.0
     */
    public SoftReference(T referent, ReferenceQueue<? super T> q) {
        super(referent, q);
    }

    /**
     * Dereference
     *
     * @param <T> parameter
     * @param ref ref
     * @return the t
     * @since 1.4.0
     */
    @Contract("null -> null")
    @Nullable
    public static <T> T dereference(@Nullable Reference<T> ref) {
        return ref == null ? null : ref.get();
    }

}
