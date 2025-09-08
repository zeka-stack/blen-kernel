package dev.dong4j.zeka.kernel.common.context;

import com.alibaba.ttl.TransmittableThreadLocal;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

/**
 * <p>Description: 全局上下文 </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2022.01.20 23:53
 * @since 1.0.0
 */
@Slf4j
public final class GlobalContext {

    /** 公共使用 */
    private static final TransmittableThreadLocal<Map<String, Object>> COMMON = new TransmittableThreadLocal<>();

    /**
     * common
     *
     * @param key key
     * @return the transmittable thread local
     * @since 1.0.0
     */
    @Contract(pure = true)
    public static @Nullable Object get(String key) {
        Map<String, Object> cache = COMMON.get();
        if (cache == null) {
            return null;
        } else {
            return cache.get(key);
        }
    }

    /**
     * Put
     *
     * @param key   key
     * @param value value
     * @since 1.0.0
     */
    @Contract(pure = true)
    public static void put(String key, Object value) {
        Map<String, Object> cache = COMMON.get();
        if (cache == null) {
            cache = new HashMap<>(8);
            cache.put(key, value);
            COMMON.set(cache);
        } else if (cache.get(key) != null) {
            // 直接覆盖
            cache.put(key, value);
        }
    }

    /**
     * Clear
     *
     * @param key key
     * @since 1.0.0
     */
    @Contract(pure = true)
    public static void clear(String key) {
        final Map<String, Object> cache = COMMON.get();
        if (cache != null) {
            cache.remove(key);
        }
    }
}
