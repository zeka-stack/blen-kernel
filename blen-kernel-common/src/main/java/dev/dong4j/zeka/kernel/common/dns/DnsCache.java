package dev.dong4j.zeka.kernel.common.dns;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.concurrent.Immutable;
import org.jetbrains.annotations.Contract;

/**
 * <p>Description: DNS 缓存</p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.06.09 19:52
 * @since 1.5.0
 */
@Immutable
@SuppressWarnings("all")
public class DnsCache implements Serializable {
    /** serialVersionUID */
    @Serial
    private static final long serialVersionUID = -8614746635950970028L;

    /** Cache */
    private final List<DnsCacheEntry> cache;
    /** Negative cache */
    private final List<DnsCacheEntry> negativeCache;

    /**
     * Dns cache
     *
     * @param cache         cache
     * @param negativeCache negative cache
     * @since 1.5.0
     */
    @Contract(pure = true)
    public DnsCache(List<DnsCacheEntry> cache, List<DnsCacheEntry> negativeCache) {
        this.cache = cache;
        this.negativeCache = negativeCache;
    }

    /**
     * Gets cache *
     *
     * @return the cache
     * @since 1.5.0
     */
    public List<DnsCacheEntry> getCache() {
        // defensive copy
        return new ArrayList<DnsCacheEntry>(this.cache);
    }

    /**
     * Gets negative cache *
     *
     * @return the negative cache
     * @since 1.5.0
     */
    public List<DnsCacheEntry> getNegativeCache() {
        // defensive copy
        return new ArrayList<DnsCacheEntry>(this.negativeCache);
    }

    /**
     * To string
     *
     * @return the string
     * @since 1.5.0
     */
    @Override
    public String toString() {
        return "DnsCache{" +
            "cache=" + this.cache +
            ", negativeCache=" + this.negativeCache +
            '}';
    }

    /**
     * Equals
     *
     * @param o o
     * @return the boolean
     * @since 1.5.0
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

        DnsCache dnsCache = (DnsCache) o;

        if (this.cache != null ? !this.cache.equals(dnsCache.cache) : dnsCache.cache != null) {
            return false;
        }
        return !(this.negativeCache != null ? !this.negativeCache.equals(dnsCache.negativeCache) : dnsCache.negativeCache != null);
    }

    /**
     * Hash code
     *
     * @return the int
     * @since 1.5.0
     */
    @Override
    public int hashCode() {
        int result = this.cache != null ? this.cache.hashCode() : 0;
        result = 31 * result + (this.negativeCache != null ? this.negativeCache.hashCode() : 0);
        return result;
    }
}
