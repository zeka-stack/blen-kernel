package dev.dong4j.zeka.kernel.common.support;

import dev.dong4j.zeka.kernel.common.util.Tools;
import java.io.Serial;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.util.LinkedCaseInsensitiveMap;

/**
 * <p>Description: 链式 map (linkedmap) key 必须为 string </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.27 18:24
 * @since 1.0.0
 */
@SuppressWarnings("all")
public final class ChainMap extends LinkedCaseInsensitiveMap<Object> {
    /** serialVersionUID */
    @Serial
    private static final long serialVersionUID = 3992810369797036508L;

    /**
     * Chain map
     *
     * @since 1.0.0
     */
    private ChainMap() {
        super();
    }

    /**
     * Chain map
     *
     * @param size size
     * @since 1.0.0
     */
    private ChainMap(int size) {
        super(Tools.capacity(size));
    }

    /**
     * Build chain map.
     *
     * @return the chain map
     * @since 1.0.0
     */
    @NotNull
    @Contract(" -> new")
    public static ChainMap build() {
        return new ChainMap();
    }

    /**
     * Build chain map.
     *
     * @param size the size
     * @return the chain map
     * @since 1.0.0
     */
    @NotNull
    @Contract("_ -> new")
    public static ChainMap build(int size) {
        return new ChainMap(size);
    }

    /**
     * 设置列,当键或值为null时忽略
     *
     * @param attr  属性
     * @param value 值
     * @return 本身 ignore null
     * @since 1.0.0
     */
    @Contract("null, _ -> this; !null, null -> this")
    public ChainMap setIgnoreNull(String attr, Object value) {
        if (null != attr && null != value) {
            put(attr, value);
        }
        return this;
    }

    /**
     * 存入 kv
     *
     * @param attr  属性
     * @param value 值
     * @return 本身 kv
     * @since 1.0.0
     */
    @Contract("_, _ -> this")
    @NotNull
    @Override
    public ChainMap put(@NotNull String attr, Object value) {
        super.put(attr, value);
        return this;
    }

    /**
     * Clone chain map
     *
     * @return the chain map
     * @since 1.0.0
     */
    @NotNull
    @Override
    public ChainMap clone() {
        return (ChainMap) super.clone();
    }

    /**
     * Gets obj.
     *
     * @param key the key
     * @return the obj
     * @since 1.0.0
     */
    public Object getObj(String key) {
        return super.get(key);
    }

    /**
     * 获得特定类型值
     *
     * @param attr 字段名
     * @return 字段值 str
     * @since 1.0.0
     */
    public String getStr(String attr) {
        return Tools.toStr(get(attr), null);
    }

    /**
     * 获得特定类型值
     *
     * @param attr 字段名
     * @return 字段值 int
     * @since 1.0.0
     */
    @NotNull
    public Integer getInt(String attr) {
        return Tools.toInt(get(attr), -1);
    }

    /**
     * 获得特定类型值
     *
     * @param attr 字段名
     * @return 字段值 long
     * @since 1.0.0
     */
    @NotNull
    public Long getLong(String attr) {
        return Tools.toLong(get(attr), -1L);
    }

    /**
     * 获得特定类型值
     *
     * @param attr 字段名
     * @return 字段值 float
     * @since 1.0.0
     */
    public Float getFloat(String attr) {
        return Tools.toFloat(get(attr), null);
    }

    /**
     * Gets double.
     *
     * @param attr the attr
     * @return the double
     * @since 1.0.0
     */
    public Double getDouble(String attr) {
        return Tools.toDouble(get(attr), null);
    }

    /**
     * 获得特定类型值
     *
     * @param attr 字段名
     * @return 字段值 bool
     * @since 1.0.0
     */
    public Boolean getBool(String attr) {
        return Tools.toBoolean(get(attr), null);
    }

    /**
     * 获得特定类型值
     *
     * @param attr 字段名
     * @return 字段值 byte [ ]
     * @since 1.0.0
     */
    public byte[] getBytes(String attr) {
        return get(attr, null);
    }

    /**
     * 获得特定类型值
     *
     * @param <T>          值类型
     * @param attr         字段名
     * @param defaultValue 默认值
     * @return 字段值 t
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String attr, T defaultValue) {
        Object result = get(attr);
        return (T) (result != null ? result : defaultValue);
    }

    /**
     * 获得特定类型值
     *
     * @param attr 字段名
     * @return 字段值 date
     * @since 1.0.0
     */
    public Date getDate(String attr) {
        return get(attr, null);
    }

    /**
     * 获得特定类型值
     *
     * @param attr 字段名
     * @return 字段值 time
     * @since 1.0.0
     */
    public Time getTime(String attr) {
        return get(attr, null);
    }

    /**
     * 获得特定类型值
     *
     * @param attr 字段名
     * @return 字段值 timestamp
     * @since 1.0.0
     */
    public Timestamp getTimestamp(String attr) {
        return get(attr, null);
    }

    /**
     * 获得特定类型值
     *
     * @param attr 字段名
     * @return 字段值 number
     * @since 1.0.0
     */
    public Number getNumber(String attr) {
        return get(attr, null);
    }
}
