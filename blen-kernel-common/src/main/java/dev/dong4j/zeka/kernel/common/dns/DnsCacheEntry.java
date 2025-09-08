package dev.dong4j.zeka.kernel.common.dns;

import java.io.Serial;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;
import javax.annotation.concurrent.Immutable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * <p>Description: DNS 缓存实体 </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.06.09 19:52
 * @since 1.0.0
 */
@Immutable
public final class DnsCacheEntry implements Serializable {
    /** serialVersionUID */
    @Serial
    private static final long serialVersionUID = -7476648934387757732L;

    /** Host */
    private final String host;
    /** Ips */
    private final String[] ips;
    /** Expiration */
    private final Date expiration;

    /**
     * Gets host *
     *
     * @return the host
     * @since 1.0.0
     */
    @Contract(pure = true)
    public String getHost() {
        return this.host;
    }

    /**
     * Get ips
     *
     * @return the string [ ]
     * @since 1.0.0
     */
    public String @NotNull [] getIps() {
        String[] copy = new String[this.ips.length];
        System.arraycopy(this.ips, 0, copy, 0, this.ips.length);
        return copy;
    }

    /**
     * Gets ip *
     *
     * @return the ip
     * @since 1.0.0
     */
    @Contract(pure = true)
    public String getIp() {
        return this.ips[0];
    }

    /**
     * Gets expiration *
     *
     * @return the expiration
     * @since 1.0.0
     */
    @Contract(pure = true)
    public Date getExpiration() {
        return this.expiration;
    }

    /**
     * Dns cache entry
     *
     * @param host       host
     * @param ips        ips
     * @param expiration expiration
     * @since 1.0.0
     */
    @Contract(pure = true)
    public DnsCacheEntry(String host, String[] ips, Date expiration) {
        this.host = host;
        this.ips = ips;
        this.expiration = expiration;
    }

    /**
     * To string
     *
     * @return the string
     * @since 1.0.0
     */
    @Override
    public @NotNull String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

        return "DnsCacheEntry{"
            + "host='" + this.host + '\''
            + ", ips=" + Arrays.toString(this.ips)
            + ", expiration=" + dateFormat.format(this.expiration)
            + '}';
    }

    /**
     * Equals
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

        DnsCacheEntry that = (DnsCacheEntry) o;

        if (!Objects.equals(this.host, that.host)) {
            return false;
        }
        if (!Arrays.equals(this.ips, that.ips)) {
            return false;
        }
        return Objects.equals(this.expiration, that.expiration);
    }

    /**
     * Hash code
     *
     * @return the int
     * @since 1.0.0
     */
    @Override
    public int hashCode() {
        int result = this.host != null ? this.host.hashCode() : 0;
        result = 31 * result + (this.ips != null ? Arrays.hashCode(this.ips) : 0);
        result = 31 * result + (this.expiration != null ? this.expiration.hashCode() : 0);
        return result;
    }
}
