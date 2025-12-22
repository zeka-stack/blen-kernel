package dev.dong4j.zeka.kernel.common.mybatis.support;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Setter;

/**
 * 分页查询实体类
 * <p>
 * 用于封装分页查询的结果数据, 包括分页参数 (当前页, 每页数量, 总记录数) 以及排序信息等, 适用于需要分页展示数据的业务场景.
 *
 * @param <T> 实体类型
 * @author zeka.stack.team
 * @version 1.0.0
 * @email "mailto:zeka.stack@gmail.com"
 * @date 2025.12.13
 * @since 2.0.0
 */
@Data
@JsonTypeName(value = "generalPage")
@Schema(name = "查询-分页实体")
public class Page<T> implements IPage<T> {

    /**
     * 序列化版本 UID
     * <p>
     * 用于版本控制, 确保序列化兼容性
     */
    @Serial
    private static final long serialVersionUID = 8545996863226528798L;

    /**
     * 分页数据
     * <p>
     * 用于存储当前页的数据集合
     *
     * @see Collections#emptyList()
     */
    @Schema(description = "分页数据")
    private List<T> records = Collections.emptyList();

    /**
     * 总记录数量
     * <p>
     * 用于表示数据集中的总记录数, 初始值为 0
     *
     * @see Schema
     */
    @Schema(description = "总记录数量")
    private long total = 0;
    /**
     * 每页显示的条数
     *
     * @see Schema
     */
    @Schema(description = "每页显示条数")
    private long size = 10;

    /**
     * 当前页数
     *
     * @see Schema
     */
    @Schema(description = "当前页数")
    private long current = 1;

    /**
     * 订单项列表
     * <p>
     * 用于存储当前订单所包含的所有订单项
     *
     * @see OrderItem
     */
    @Setter
    private List<OrderItem> orders = new ArrayList<>();

    /** 是否优化 SQL 计数语句 */
    private boolean optimizeCountSql = true;
    /** 是否启用搜索计数功能 */
    private boolean searchCount = true;
    /**
     * 是否优化 COUNT SQL 的 JOIN 操作
     * <p>
     * 当设置为 true 时, 系统将尝试对包含 COUNT 的 SQL 语句进行 JOIN 优化, 以提高查询性能.
     */
    @Setter
    private boolean optimizeJoinOfCountSql = true;
    /**
     * 最大限制值
     * <p>
     * 用于表示某种操作或条件的最大允许限制
     *
     * @see Setter
     */
    @Setter
    private Long maxLimit;
    /**
     * 计数器的唯一标识符
     *
     * @see Setter
     */
    @Setter
    private String countId;

    /**
     * 构造函数, 用于创建 Page 对象
     * <p>
     * 初始化 Page 实例, 当前实现中未执行任何操作
     */
    public Page() {
    }

    /**
     * 构造分页对象
     * <p>
     * 根据当前页码和每页大小创建分页对象, 并设置默认的偏移量为 0.
     *
     * @param current 当前页码
     * @param size    每页显示的记录数
     * @throws IllegalArgumentException 如果当前页码或每页大小小于等于 0, 则抛出异常
     */
    public Page(long current, long size) {
        this(current, size, 0);
    }

    /**
     * 构造一个分页对象
     * <p>
     * 通过指定当前页码, 每页大小和总记录数创建分页实例, 并设置默认的是否启用分页标志为 true.
     *
     * @param current 当前页码
     * @param size    每页显示的记录数
     * @param total   总记录数
     */
    public Page(long current, long size, long total) {
        this(current, size, total, true);
    }

    /**
     * 构造分页对象
     * <p>
     * 初始化分页参数, 包括当前页码, 每页大小, 偏移量和是否搜索总数
     *
     * @param current     当前页码
     * @param size        每页显示的记录数
     * @param searchCount 是否搜索总记录数
     */
    public Page(long current, long size, boolean searchCount) {
        this(current, size, 0, searchCount);
    }

    /**
     * 构造分页对象
     * <p>
     * 初始化分页参数, 若当前页码大于 1 则设置当前页码, 否则不设置. 其他参数如每页大小, 总记录数和是否查询总数将被直接设置.
     *
     * @param current     当前页码
     * @param size        每页显示的记录数
     * @param total       总记录数
     * @param searchCount 是否查询总数
     */
    public Page(long current, long size, long total, boolean searchCount) {
        if (current > 1) {
            this.current = current;
        }
        this.size = size;
        this.total = total;
        this.searchCount = searchCount;
    }

    /**
     * 判断当前页码是否大于第一页
     * <p>
     * 用于确定是否存在上一页
     *
     * @return 如果当前页码大于 1, 则返回 true, 否则返回 false
     */
    public boolean hasPrevious() {
        return this.current > 1;
    }

    /**
     * 判断是否还有下一页
     * <p>
     * 检查当前页码是否小于总页数, 用于分页遍历场景
     *
     * @return 如果存在下一页则返回 true, 否则返回 false
     */
    public boolean hasNext() {
        return this.current < this.getPages();
    }

    /**
     * 获取记录列表
     * <p>
     * 返回当前对象中的记录集合
     *
     * @return 记录列表
     */
    @Override
    public List<T> getRecords() {
        return this.records;
    }

    /**
     * 设置当前页的数据记录列表
     * <p>
     * 将指定的记录列表赋值给当前页对象, 并返回当前页对象以便链式调用
     *
     * @param records 要设置的数据记录列表
     * @return 当前页对象
     */
    @Override
    public Page<T> setRecords(List<T> records) {
        this.records = records;
        return this;
    }

    /**
     * 获取总数量
     * <p>
     * 返回当前对象中存储的总数量值
     *
     * @return 总数量, 以长整型表示
     */
    @Override
    public long getTotal() {
        return this.total;
    }

    /**
     * 设置当前分页对象的总记录数
     * <p>
     * 该方法用于更新分页对象中的总记录数, 并返回当前对象以便于链式调用
     *
     * @param total 总记录数
     * @return 当前分页对象
     */
    @Override
    public Page<T> setTotal(long total) {
        this.total = total;
        return this;
    }

    /**
     * 获取当前对象的大小
     * <p>
     * 返回存储在对象中的大小值
     *
     * @return 当前对象的大小, 以 long 类型表示
     */
    @Override
    public long getSize() {
        return this.size;
    }

    /**
     * 设置分页对象的大小
     * <p>
     * 该方法用于设置当前分页对象的每页记录数, 并返回当前分页对象以便于链式调用.
     *
     * @param size 每页记录数
     * @return 当前分页对象
     * @throws IllegalArgumentException 如果 size 小于等于 0, 则抛出异常
     */
    @Override
    public Page<T> setSize(long size) {
        this.size = size;
        return this;
    }

    /**
     * 获取当前值
     * <p>
     * 返回当前对象的 current 属性值
     *
     * @return 当前值
     */
    @Override
    public long getCurrent() {
        return this.current;
    }

    /**
     * 设置当前页码
     * <p>
     * 将当前页码设置为指定值, 并返回当前对象以便链式调用
     *
     * @param current 当前页码, 必须为大于等于 1 的正整数
     * @return 当前对象实例
     * @throws IllegalArgumentException 如果 current 小于 1
     */
    @Override
    public Page<T> setCurrent(long current) {
        this.current = current;
        return this;
    }

    /**
     * 获取当前对象的 ID 计数
     * <p>
     * 返回当前对象中存储的 ID 计数值
     *
     * @return 当前对象的 ID 计数
     */
    @Override
    public String countId() {
        return this.countId;
    }

    /**
     * 获取当前实例的最大限制值
     * <p>
     * 返回该对象中存储的最大限制数值
     *
     * @return 当前实例的最大限制值, 以 Long 类型表示
     */
    @Override
    public Long maxLimit() {
        return this.maxLimit;
    }

    /**
     * 将符合指定条件的订单项映射为字符串数组
     * <p>
     * 遍历订单列表, 对每个订单项应用给定的过滤器, 若通过过滤则将其列名添加到结果列表中, 最后转换为字符串数组返回
     *
     * @param filter 用于筛选订单项的谓词函数
     * @return 符合条件的订单项对应的列名数组
     */
    private String[] mapOrderToArray(Predicate<OrderItem> filter) {
        List<String> columns = new ArrayList<>(orders.size());
        orders.forEach(i -> {
            if (filter.test(i)) {
                columns.add(i.getColumn());
            }
        });
        return columns.toArray(new String[0]);
    }

    /**
     * 根据指定的过滤条件移除符合条件的订单项
     * <p>
     * 从订单列表中逆序遍历, 对每个订单项应用给定的过滤器, 若过滤器返回 true, 则移除该订单项.
     *
     * @param filter 用于判断订单项是否需要被移除的条件
     */
    private void removeOrder(Predicate<OrderItem> filter) {
        for (int i = orders.size() - 1; i >= 0; i--) {
            if (filter.test(orders.get(i))) {
                orders.remove(i);
            }
        }
    }

    /**
     * 添加订单项到当前订单
     * <p>
     * 将指定的订单项数组添加到订单集合中, 并返回当前对象以便链式调用
     *
     * @param items 要添加的订单项数组
     * @return 当前订单对象
     */
    public Page<T> addOrder(OrderItem... items) {
        orders.addAll(Arrays.asList(items));
        return this;
    }

    /**
     * 添加订单项到当前订单
     * <p>
     * 将指定的订单项列表添加到当前订单的订单项集合中, 并返回当前订单对象以便链式调用
     *
     * @param items 要添加的订单项列表
     * @return 当前订单对象
     */
    public Page<T> addOrder(List<OrderItem> items) {
        orders.addAll(items);
        return this;
    }

    /**
     * 获取订单项列表
     * <p>
     * 返回当前订单的订单项集合
     *
     * @return 订单项列表
     */
    @Override
    public List<OrderItem> orders() {
        return this.orders;
    }

    /**
     * 判断是否优化计数 SQL
     * <p>
     * 返回一个布尔值, 表示当前是否启用了对计数 SQL 的优化功能.
     *
     * @return 是否优化计数 SQL
     */
    @Override
    public boolean optimizeCountSql() {
        return optimizeCountSql;
    }

    /**
     * 创建一个分页对象
     * <p>
     * 根据当前页码, 每页大小, 总记录数和是否进行计数查询, 生成一个分页实例.
     *
     * @param <T>         实体类型
     * @param current     当前页码
     * @param size        每页显示的记录数
     * @param total       总记录数
     * @param searchCount 是否进行计数查询
     * @return 分页对象实例
     */
    public static <T> Page<T> of(long current, long size, long total, boolean searchCount) {
        return new Page<>(current, size, total, searchCount);
    }

    /**
     * 优化用于计数的 SQL 语句的连接操作
     * <p>
     * 返回是否已对用于计数的 SQL 语句的连接操作进行了优化
     *
     * @return 是否已优化连接操作
     */
    @Override
    public boolean optimizeJoinOfCountSql() {
        return optimizeJoinOfCountSql;
    }

    /**
     * 设置是否启用搜索计数功能
     * <p>
     * 用于控制在执行分页查询时是否统计总记录数. 该方法返回当前对象以支持链式调用.
     *
     * @param searchCount 是否启用搜索计数功能
     * @return 当前对象实例
     */
    public Page<T> setSearchCount(boolean searchCount) {
        this.searchCount = searchCount;
        return this;
    }

    /**
     * 设置是否优化 count SQL 查询
     * <p>
     * 用于控制是否启用 count SQL 的优化策略, 该策略可能影响分页查询的性能或准确性.
     *
     * @param optimizeCountSql 是否启用 count SQL 优化,true 表示启用,false 表示禁用
     * @return 当前对象实例, 用于方法链调用
     */
    public Page<T> setOptimizeCountSql(boolean optimizeCountSql) {
        this.optimizeCountSql = optimizeCountSql;
        return this;
    }

    /**
     * 获取当前分页的总页数
     * <p>
     * 调用父类方法返回分页数据的总页数
     *
     * @return 总页数
     */
    @Override
    public long getPages() {
        // 解决 github issues/3208
        return IPage.super.getPages();
    }

    /* --------------- 以下为静态构造方式 --------------- */

    /**
     * 创建一个分页对象
     * <p>
     * 根据当前页码, 每页大小和默认的偏移量 (0) 创建一个分页对象
     *
     * @param <T>     实体类型
     * @param current 当前页码, 从 1 开始计数
     * @param size    每页显示的记录数
     * @return 分页对象
     * @throws IllegalArgumentException 如果当前页码或每页大小小于等于 0
     */
    public static <T> Page<T> of(long current, long size) {
        return of(current, size, 0);
    }

    /**
     * 根据当前页码, 每页大小和总记录数创建分页对象
     * <p>
     * 该方法用于生成一个分页对象, 其中包含当前页码, 每页大小, 总记录数以及是否需要计算总页数的标志.
     * 此方法调用另一个重载的 of 方法, 并设置计算总页数的标志为 true.
     *
     * @param <T>     实体类型
     * @param current 当前页码 (从 1 开始)
     * @param size    每页显示的记录数
     * @param total   总记录数
     * @return 分页对象
     * @throws IllegalArgumentException 如果页码或每页大小小于 1
     */
    public static <T> Page<T> of(long current, long size, long total) {
        return of(current, size, total, true);
    }

    /**
     * 创建一个分页对象
     * <p>
     * 根据当前页码, 每页大小和是否搜索总数来创建分页对象, 偏移量默认为 0
     *
     * @param <T>         实体类型
     * @param current     当前页码
     * @param size        每页显示的记录数
     * @param searchCount 是否搜索总数
     * @return 分页对象
     */
    public static <T> Page<T> of(long current, long size, boolean searchCount) {
        return of(current, size, 0, searchCount);
    }

    /**
     * 检查是否已进行过搜索计数
     * <p>
     * 如果 total 小于 0, 表示未进行过搜索计数, 返回 false; 否则返回 searchCount 的值.
     *
     * @return true 表示已进行过搜索计数,false 表示未进行过
     */
    @Override
    public boolean searchCount() {
        if (total < 0) {
            return false;
        }
        return searchCount;
    }

    /**
     * 返回当前分页对象的字符串表示形式
     * <p>
     * 该方法将分页对象的各个属性以字符串形式拼接, 用于调试或日志记录
     *
     * @return 分页对象的字符串表示, 包含 records,total,size,current,orders,optimizeCountSql,searchCount,optimizeJoinOfCountSql,maxLimit 和 countId 等属性值
     */
    @Override
    public String toString() {
        return "Page{"
               + "records=" + records
               + ", total=" + total
               + ", size=" + size
               + ", current=" + current
               + ", orders=" + orders
               + ", optimizeCountSql=" + optimizeCountSql
               + ", searchCount=" + searchCount
               + ", optimizeJoinOfCountSql=" + optimizeJoinOfCountSql
               + ", maxLimit=" + maxLimit
               + ", countId='" + countId + '\''
               + '}';
    }

}
