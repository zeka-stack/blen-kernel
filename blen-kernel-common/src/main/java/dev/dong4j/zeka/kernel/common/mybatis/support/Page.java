package dev.dong4j.zeka.kernel.common.mybatis.support;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.io.Serial;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import lombok.Data;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.util.CollectionUtils;

/**
 * <p>Description:  </p>
 *
 * @param <T> parameter
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dong4j@gmail.com"
 * @date 2019.12.24 11:54
 * @since 1.0.0
 */
@Data
@ToString(callSuper = true)
@JsonTypeName(value = "generalPage")
public class Page<T> implements IPage<T> {

    /** serialVersionUID */
    @Serial
    private static final long serialVersionUID = -7759805099824817556L;
    /** 查询数据列表 */
    private List<T> records = Collections.emptyList();
    /** 总数 */
    private long total = 0;
    /** 每页显示条数, 默认 10 */
    private long size = 10;
    /** 当前页 */
    private long current = 1;
    /** 排序字段信息 */
    private List<OrderItem> orders = new ArrayList<>();
    /** 自动优化 COUNT SQL */
    @JsonIgnore
    private boolean optimizeCountSql = true;
    /** 是否进行 count 查询 */
    @JsonIgnore
    private boolean isSearchCount = true;
    /** 是否命中 count 缓存 */
    @JsonIgnore
    private boolean hitCount = false;
    /** Count id */
    @JsonIgnore
    private String countId;
    /** Max limit */
    @JsonIgnore
    private Long maxLimit;

    /**
     * Page
     *
     * @since 1.0.0
     */
    public Page() {
    }

    /**
     * 分页构造函数
     *
     * @param current 当前页
     * @param size    每页显示条数
     * @since 1.0.0
     */
    public Page(long current, long size) {
        this(current, size, 0);
    }

    /**
     * Page
     *
     * @param current current
     * @param size    size
     * @param total   total
     * @since 1.0.0
     */
    public Page(long current, long size, long total) {
        this(current, size, total, true);
    }

    /**
     * Page
     *
     * @param current       current
     * @param size          size
     * @param isSearchCount is search count
     * @since 1.0.0
     */
    public Page(long current, long size, boolean isSearchCount) {
        this(current, size, 0, isSearchCount);
    }

    /**
     * Page
     *
     * @param current       current
     * @param size          size
     * @param total         total
     * @param isSearchCount is search count
     * @since 1.0.0
     */
    public Page(long current, long size, long total, boolean isSearchCount) {
        if (current > 1) {
            this.current = current;
        }
        this.size = size;
        this.total = total;
        this.isSearchCount = isSearchCount;
    }

    /**
     * 是否存在上一页
     *
     * @return true / false
     * @since 1.0.0
     */
    public boolean hasPrevious() {
        return this.current > 1;
    }

    /**
     * 是否存在下一页
     *
     * @return true / false
     * @since 1.0.0
     */
    public boolean hasNext() {
        return this.current < this.getPages();
    }

    /**
     * Gets records *
     *
     * @return the records
     * @since 1.0.0
     */
    @Override
    public List<T> getRecords() {
        return this.records;
    }

    /**
     * Sets records *
     *
     * @param records records
     * @return the records
     * @since 1.0.0
     */
    @Override
    public Page<T> setRecords(List<T> records) {
        this.records = records;
        return this;
    }

    /**
     * Gets total *
     *
     * @return the total
     * @since 1.0.0
     */
    @Override
    public long getTotal() {
        return this.total;
    }

    /**
     * Sets total *
     *
     * @param total total
     * @return the total
     * @since 1.0.0
     */
    @Override
    public Page<T> setTotal(long total) {
        this.total = total;
        return this;
    }

    /**
     * Gets size *
     *
     * @return the size
     * @since 1.0.0
     */
    @Override
    public long getSize() {
        return this.size;
    }

    /**
     * Sets size *
     *
     * @param size size
     * @return the size
     * @since 1.0.0
     */
    @Override
    public Page<T> setSize(long size) {
        this.size = size;
        return this;
    }

    /**
     * Gets current *
     *
     * @return the current
     * @since 1.0.0
     */
    @Override
    public long getCurrent() {
        return this.current;
    }

    /**
     * Sets current *
     *
     * @param current current
     * @return the current
     * @since 1.0.0
     */
    @Override
    public Page<T> setCurrent(long current) {
        this.current = current;
        return this;
    }

    /**
     * Count id
     *
     * @return the string
     * @since 2.0.0 mybatis-plus 3.4.0 @2020-06-19
     */
    @Override
    public String countId() {
        return getCountId();
    }

    /**
     * Max limit
     *
     * @return the long
     * @since 2.0.0 mybatis-plus 3.4.0 @2020-07-17
     */
    @Override
    public Long maxLimit() {
        return getMaxLimit();
    }

    /**
     * 获取当前正序排列的字段集合
     * <p>
     * 为了兼容, 将在不久后废弃
     *
     * @return 正序排列的字段集合 string [ ]
     * @see #getOrders()
     * @since 1.0.0
     * @deprecated 3.2.0
     */
    @Override
    @Nullable
    @Deprecated
    public String[] ascs() {
        if (CollectionUtils.isEmpty(this.orders)) {
            return this.mapOrderToArray(OrderItem::isAsc);
        }
        return null;
    }

    /**
     * 查找 order 中正序排序的字段数组
     *
     * @param filter 过滤器
     * @return 返回正序排列的字段数组 string [ ]
     * @since 1.0.0
     */
    @NotNull
    private String[] mapOrderToArray(Predicate<OrderItem> filter) {
        List<String> columns = new ArrayList<>(this.orders.size());
        this.orders.forEach(i -> {
            if (filter.test(i)) {
                columns.add(i.getColumn());
            }
        });
        return columns.toArray(new String[0]);
    }

    /**
     * 移除符合条件的条件
     *
     * @param filter 条件判断
     * @since 1.0.0
     */
    private void removeOrder(Predicate<OrderItem> filter) {
        for (int i = this.orders.size() - 1; i >= 0; i--) {
            if (filter.test(this.orders.get(i))) {
                this.orders.remove(i);
            }
        }
    }

    /**
     * 添加新的排序条件, 构造条件可以使用工厂
     *
     * @param items 条件
     * @return 返回分页参数本身 page
     * @since 1.0.0
     */
    public Page<T> addOrder(OrderItem... items) {
        this.orders.addAll(Arrays.asList(items));
        return this;
    }

    /**
     * 添加新的排序条件, 构造条件可以使用工厂
     *
     * @param items 条件
     * @return 返回分页参数本身 page
     * @since 1.0.0
     */
    public Page<T> addOrder(List<OrderItem> items) {
        this.orders.addAll(items);
        return this;
    }

    /**
     * 设置需要进行正序排序的字段
     * <p>
     * Replaced:{@link #addOrder(OrderItem...)}
     *
     * @param ascs 字段
     * @return 返回自身 ascs
     * @since 1.0.0
     * @deprecated 3.2.0
     */
    @Deprecated
    public Page<T> setAscs(List<String> ascs) {
        if (!CollectionUtils.isEmpty(ascs)) {
            return this.setAsc(ascs.toArray(new String[0]));
        }
        return this;
    }

    /**
     * 升序
     * <p>
     * Replaced:{@link #addOrder(OrderItem...)}
     *
     * @param ascs 多个升序字段
     * @return the asc
     * @since 1.0.0
     * @deprecated 3.2.0
     */
    @Deprecated
    public Page<T> setAsc(@NotNull String... ascs) {
        // 保证原来方法 set 的语意
        this.removeOrder(OrderItem::isAsc);
        for (String s : ascs) {
            this.addOrder(OrderItem.asc(s));
        }
        return this;
    }

    /**
     * 获取需简要倒序排列的字段数组
     * <p>
     *
     * @return 倒序排列的字段数组 string [ ]
     * @see #getOrders() #getOrders()#getOrders()#getOrders()#getOrders()
     * @since 1.0.0
     * @deprecated 3.2.0
     */
    @Override
    @Deprecated
    public String[] descs() {
        return this.mapOrderToArray(i -> !i.isAsc());
    }

    /**
     * Replaced:{@link #addOrder(OrderItem...)}
     *
     * @param descs 需要倒序排列的字段
     * @return 自身 descs
     * @since 1.0.0
     * @deprecated 3.2.0
     */
    @Deprecated
    public Page<T> setDescs(List<String> descs) {
        // 保证原来方法 set 的语意
        if (!CollectionUtils.isEmpty(descs)) {
            this.removeOrder(item -> !item.isAsc());
            for (String s : descs) {
                this.addOrder(OrderItem.desc(s));
            }
        }

        return this;
    }

    /**
     * 降序, 这方法名不知道是谁起的
     * <p>
     * Replaced:{@link #addOrder(OrderItem...)}
     *
     * @param descs 多个降序字段
     * @return the desc
     * @since 1.0.0
     * @deprecated 3.2.0
     */
    @Deprecated
    public Page<T> setDesc(String... descs) {
        this.setDescs(Arrays.asList(descs));
        return this;
    }

    /**
     * Orders list
     *
     * @return the list
     * @since 1.0.0
     */
    @Override
    public List<OrderItem> orders() {
        return this.getOrders();
    }

    /**
     * Optimize count sql boolean
     *
     * @return the boolean
     * @since 1.0.0
     */
    @Override
    public boolean optimizeCountSql() {
        return this.optimizeCountSql;
    }

    /**
     * Is search count boolean
     *
     * @return the boolean
     * @since 1.0.0
     */
    @Override
    public boolean isSearchCount() {
        if (this.total < 0) {
            return false;
        }
        return this.isSearchCount;
    }

    /**
     * Sets search count *
     *
     * @param isSearchCount is search count
     * @return the search count
     * @since 1.0.0
     */
    public Page<T> setSearchCount(boolean isSearchCount) {
        this.isSearchCount = isSearchCount;
        return this;
    }

    /**
     * Sets optimize count sql *
     *
     * @param optimizeCountSql optimize count sql
     * @return the optimize count sql
     * @since 1.0.0
     */
    public Page<T> setOptimizeCountSql(boolean optimizeCountSql) {
        this.optimizeCountSql = optimizeCountSql;
        return this;
    }

    /**
     * Hit count *
     *
     * @param hit hit
     * @since 1.0.0
     */
    @Override
    public void hitCount(boolean hit) {
        this.hitCount = hit;
    }

    /**
     * Is hit count boolean
     *
     * @return the boolean
     * @since 1.0.0
     */
    @Override
    public boolean isHitCount() {
        return this.hitCount;
    }
}
