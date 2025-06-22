package com.baomidou.mybatisplus.core.metadata;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import dev.dong4j.zeka.kernel.common.base.BasePage;
import dev.dong4j.zeka.kernel.common.mybatis.support.Page;
import dev.dong4j.zeka.kernel.common.util.ClassUtils;
import dev.dong4j.zeka.kernel.common.util.StringPool;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

/**
 * 分页 Page 对象接口
 *
 * @param <T> parameter
 * @author hubin
 * @version 1.3.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.03.11 01:34
 * @since 1.0.0
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", defaultImpl = Page.class)
public interface IPage<T> extends Serializable {

    /**
     * 降序字段数组
     *
     * @return order by desc 的字段数组
     * @see #orders() #orders()#orders()#orders()
     * @since 1.0.0
     */
    @Deprecated
    default String[] descs() {
        return null;
    }

    /**
     * 升序字段数组
     *
     * @return order by asc 的字段数组
     * @see #orders() #orders()#orders()#orders()
     * @since 1.0.0
     */
    @Deprecated
    default String[] ascs() {
        return null;
    }

    /**
     * 获取排序信息, 排序的字段和正反序
     *
     * @return 排序信息 list
     * @since 1.0.0
     */
    List<OrderItem> orders();

    /**
     * KEY/VALUE 条件
     *
     * @return ignore map
     * @since 1.0.0
     */
    default Map<Object, Object> condition() {
        return null;
    }

    /**
     * 自动优化 COUNT SQL【 默认: true 】
     *
     * @return true 是 / false 否
     * @since 1.0.0
     */
    default boolean optimizeCountSql() {
        return true;
    }

    /**
     * 进行 count 查询 【 默认: true 】
     *
     * @return true 是 / false 否
     * @since 1.0.0
     */
    default boolean isSearchCount() {
        return true;
    }

    /**
     * 计算当前分页偏移量
     *
     * @return the long
     * @since 1.0.0
     */
    default long offset() {
        return this.getCurrent() > 0 ? (this.getCurrent() - 1) * this.getSize() : 0;
    }

    /**
     * 当前分页总页数
     *
     * @return the pages
     * @since 1.0.0
     */
    default long getPages() {
        if (this.getSize() == 0) {
            return 0L;
        }
        long pages = this.getTotal() / this.getSize();
        if (this.getTotal() % this.getSize() != 0) {
            pages++;
        }
        return pages;
    }

    /**
     * 内部什么也不干
     * <p>只是为了 json 反序列化时不报错</p>
     *
     * @param pages pages
     * @return the pages
     * @since 1.0.0
     */
    default IPage<T> setPages(long pages) {
        // to do nothing
        return this;
    }

    /**
     * 设置是否命中count缓存
     *
     * @param hit 是否命中
     * @since 3.3.1
     */
    default void hitCount(boolean hit) {

    }

    /**
     * 是否命中count缓存
     *
     * @return 是否命中count缓存 boolean
     * @since 3.3.1
     */
    default boolean isHitCount() {
        return false;
    }

    /**
     * 分页记录列表
     *
     * @return 分页对象记录列表 records
     * @since 1.0.0
     */
    List<T> getRecords();

    /**
     * 设置分页记录列表
     *
     * @param records records
     * @return the records
     * @since 1.0.0
     */
    IPage<T> setRecords(List<T> records);

    /**
     * 当前满足条件总行数
     *
     * @return 总条数 total
     * @since 1.0.0
     */
    long getTotal();

    /**
     * 设置当前满足条件总行数
     *
     * @param total total
     * @return the total
     * @since 1.0.0
     */
    IPage<T> setTotal(long total);

    /**
     * 获取每页显示条数
     *
     * @return 每页显示条数 size
     * @since 1.0.0
     */
    long getSize();

    /**
     * 设置每页显示条数
     *
     * @param size size
     * @return the size
     * @since 1.0.0
     */
    IPage<T> setSize(long size);

    /**
     * 当前页, 默认 1
     *
     * @return 当前页 current
     * @since 1.0.0
     */
    long getCurrent();

    /**
     * 设置当前页
     *
     * @param current current
     * @return the current
     * @since 1.0.0
     */
    IPage<T> setCurrent(long current);

    /**
     * IPage 的泛型转换
     *
     * @param <R>    转换后的泛型
     * @param mapper 转换函数
     * @return 转换泛型后的 IPage
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    default <R> IPage<R> convert(Function<? super T, ? extends R> mapper) {
        List<R> collect = this.getRecords().stream().map(mapper).collect(toList());
        return ((IPage<R>) this).setRecords(collect);
    }

    /**
     * 最大每页分页数限制,优先级高于分页插件内的 maxLimit
     *
     * @since 3.4.0 @2020-07-17
     */
    default Long maxLimit() {
        return null;
    }

    /**
     * 老分页插件不支持
     * <p>
     * MappedStatement 的 id
     *
     * @return id
     * @since 3.4.0 @2020-06-19
     */
    default String countId() {
        return null;
    }

    /**
     * 生成缓存key值
     *
     * @return 缓存key值
     * @since 3.3.2
     * @deprecated 3.4.0 @2020-06-30
     */
    @Deprecated
    default String cacheKey() {
        StringBuilder key = new StringBuilder();
        key.append(offset()).append(StringPool.COLON).append(getSize());
        List<OrderItem> orders = orders();
        if (!CollectionUtils.isEmpty(orders)) {
            for (OrderItem item : orders) {
                key.append(StringPool.COLON).append(item.getColumn()).append(StringPool.COLON).append(item.isAsc());
            }
        }
        return key.toString();
    }

    /**
     * 包装为 BasePage 对象
     *
     * @param <P>   parameter
     * @param clazz clazz
     * @return the p
     * @since 1.6.0
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    default <P extends BasePage<?>> P wrapper(Class<P> clazz) {
        P wrapper = ClassUtils.newInstance(clazz);
        wrapper.setPagination((IPage) this);
        return wrapper;
    }
}
