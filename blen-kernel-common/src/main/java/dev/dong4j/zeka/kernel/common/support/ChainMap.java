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
 * 链式Map实现，支持链式调用和大小写不敏感的键名
 * <p>
 * 该类继承自LinkedCaseInsensitiveMap，提供了链式调用的Map操作能力
 * 支持不同类型值的安全获取，并提供了丰富的类型转换方法
 * <p>
 * 主要特性：
 * - 链式调用：put()方法返回当前对象，支持连续调用
 * - 大小写不敏感：键名不区分大小写，"Name"和"name"被视为相同
 * - 类型安全：提供多种类型的getter方法，带有默认值支持
 * - 空值处理：setIgnoreNull()方法可以忽略空值设置
 * - 高性能：基于LinkedHashMap实现，维持插入顺序
 * <p>
 * 使用场景：
 * - API返回结果的封装，支持链式构建
 * - 配置参数的动态处理，不关心键名大小写
 * - 数据库查询结果的封装，支持类型转换
 * - 临时数据存储，需要灵活的键值对操作
 * <p>
 * 使用示例：
 * <pre>{@code
 * ChainMap result = ChainMap.build()
 *     .put("userId", 1001)
 *     .put("userName", "John")
 *     .setIgnoreNull("email", null)  // 忽略空值
 *     .put("age", "25");
 *
 * // 类型安全获取
 * Integer userId = result.getInt("userId");     // 1001
 * String name = result.getStr("userName");     // "John"
 * Integer age = result.getInt("age");          // 25 (自动类型转换)
 * }</pre>
 * <p>
 * 设计模式：
 * - 建造者模式：通过链式调用逐步构建对象
 * - 适配器模式：对LinkedCaseInsensitiveMap的增强适配
 * - 工厂模式：提供build()静态方法创建实例
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
     * 私有构造函数，创建默认容量的ChainMap
     * <p>
     * 使用默认的初始容量创建链式Map实例
     * 适用于不确定数据量大小的场景
     *
     * @since 1.0.0
     */
    private ChainMap() {
        super();
    }

    /**
     * 私有构造函数，创建指定容量的ChainMap
     * <p>
     * 使用指定的初始容量创建链式Map实例
     * 可以避免频繁的内存重新分配，提高性能
     *
     * @param size 初始容量大小
     * @since 1.0.0
     */
    private ChainMap(int size) {
        super(Tools.capacity(size));
    }

    /**
     * 创建默认的ChainMap实例（建造者模式）
     * <p>
     * 提供一个简洁的静态工厂方法，用于创建新的ChainMap实例
     * 返回的对象支持链式调用，可以连续调用put方法
     *
     * @return 新的ChainMap实例
     * @since 1.0.0
     */
    @NotNull
    @Contract(" -> new")
    public static ChainMap build() {
        return new ChainMap();
    }

    /**
     * 创建指定容量的ChainMap实例（建造者模式）
     * <p>
     * 提供一个可以指定初始容量的静态工厂方法
     * 当能够预估数据量时，使用该方法可以显著提高性能
     *
     * @param size 初始容量大小
     * @return 新的ChainMap实例
     * @since 1.0.0
     */
    @NotNull
    @Contract("_ -> new")
    public static ChainMap build(int size) {
        return new ChainMap(size);
    }

    /**
     * 设置键值对，忽略空值（null-safe操作）
     * <p>
     * 该方法提供了安全的键值设置功能，当键或值为null时会自动忽略
     * 防止意null值干扰数据的清洁性，适用于不确定数据完整性的场景
     * <p>
     * 使用场景：
     * - API参数校验时，只保存有效值
     * - 数据库查询结果处理，过滤空字段
     * - 配置参数加载，忽略无效配置项
     *
     * @param attr  属性名（键）
     * @param value 属性值
     * @return 当前ChainMap实例，支持链式调用
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
     * 存入键值对（支持链式调用）
     * <p>
     * 该方法重写了父类的put方法，返回当前对象实例而不是原有值
     * 这样设计使得可以连续调用多个put操作，实现流式编程
     * <p>
     * 使用示例：
     * <pre>{@code
     * ChainMap result = ChainMap.build()
     *     .put("key1", "value1")
     *     .put("key2", "value2")
     *     .put("key3", "value3");
     * }</pre>
     *
     * @param attr  属性名（键）
     * @param value 属性值
     * @return 当前ChainMap实例，支持链式调用
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
