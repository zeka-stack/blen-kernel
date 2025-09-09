package dev.dong4j.zeka.kernel.common.base;

import com.baomidou.mybatisplus.core.metadata.IPage;
import dev.dong4j.zeka.kernel.common.api.BaseCodes;
import dev.dong4j.zeka.kernel.common.assertion.IAssert;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

/**
 * <p>数据操作桥接适配器.
 * <p>解决不同返回类型和泛型问题的桥接适配器，为业务层提供统一的数据操作接口.
 * <p>主要功能：
 * <ul>
 *     <li>封装和简化 CRUD 操作的调用方式</li>
 *     <li>提供统一的参数校验和异常处理</li>
 *     <li>支持事务和断言的集成处理</li>
 *     <li>解决泛型类型擦除导致的兼容性问题</li>
 * </ul>
 * <p>设计优势：
 * <ul>
 *     <li>使用抽象类避免后期接口变更对业务代码的影响</li>
 *     <li>统一的异常处理和参数校验机制</li>
 *     <li>支持多种数据操作策略（忽略、替换、批量等）</li>
 *     <li>提供完整的类型安全保障</li>
 * </ul>
 * <p>适用场景：
 * <ul>
 *     <li>业务服务层需要统一数据访问接口</li>
 *     <li>需要集成断言和事务处理的数据操作</li>
 *     <li>多种数据源的统一访问适配</li>
 *     <li>复杂业务逻辑的数据操作封装</li>
 * </ul>
 * <p>技术特性：
 * <ul>
 *     <li>委托模式实现，降低耦合度</li>
 *     <li>支持断言链式调用和异常包装</li>
 *     <li>完整的泛型约束和类型推断</li>
 *     <li>灵活的批量操作和事务控制</li>
 * </ul>
 *
 * @param <D> 数据传输对象类型，必须继承 BaseDTO
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2021.10.14 20:23
 * @since 1.0.0
 */
public abstract class Bridge<D extends BaseDTO<? extends Serializable>> {
    /** 服务 */
    private final ICrudDelegate<D> service;

    /**
     * 桥
     *
     * @param service 服务
     * @since 1.0.0
     */
    public Bridge(ICrudDelegate<D> service) {
        this.service = service;
    }

    /**
     * 创建数据
     *
     * @param <I>        parameter
     * @param d          d
     * @param assertions 断言
     * @return {@link I}
     * @since 1.0.0
     */
    public <I extends Serializable> I create(@NotNull D d, @NotNull IAssert assertions) {
        BaseCodes.PARAM_VERIFY_ERROR.notNull(d, "新增数据实体不能为 null");
        BaseCodes.PARAM_VERIFY_ERROR.isNull(d.getId(), "新增数据时不能传 id");
        return assertions.wrapper(() -> service.create(d));
    }

    /**
     * 创建批处理
     *
     * @param ds         ds
     * @param assertions 断言
     * @since 1.0.0
     */
    public void createBatch(Collection<D> ds, @NotNull IAssert assertions) {
        BaseCodes.PARAM_VERIFY_ERROR.notEmpty(ds, "批量新增的数据不能为空");
        assertions.wrapper(() -> service.createBatch(ds));
    }

    /**
     * Create batch
     *
     * @param dtos       dtos
     * @param batchSize  batch size
     * @param assertions assertions
     * @since 1.0.0
     */
    public void createBatch(Collection<D> dtos, int batchSize, @NotNull IAssert assertions) {
        BaseCodes.PARAM_VERIFY_ERROR.notEmpty(dtos, "批量新增的数据不能为空");
        BaseCodes.PARAM_VERIFY_ERROR.isTrue(batchSize > 0, "批量新增操作数不正确");
        assertions.wrapper(() -> service.createBatch(dtos, batchSize));
    }

    /**
     * 创建忽略
     *
     * @param <I>        parameter
     * @param d          d
     * @param assertions assertions
     * @return {@link I}
     * @since 1.0.0
     */
    public <I extends Serializable> I createIgnore(@NotNull D d, @NotNull IAssert assertions) {
        BaseCodes.PARAM_VERIFY_ERROR.notNull(d, "批量新增的数据不能为空");
        BaseCodes.PARAM_VERIFY_ERROR.isNull(d.getId(), "");
        return assertions.wrapper(() -> service.createIgnore(d));
    }

    /**
     * 创建忽略批
     *
     * @param ds         ds
     * @param assertions assertions
     * @since 1.0.0
     */
    public void createIgnoreBatch(Collection<D> ds, @NotNull IAssert assertions) {
        BaseCodes.PARAM_VERIFY_ERROR.notEmpty(ds);
        assertions.wrapper(() -> service.createIgnoreBatch(ds));
    }

    /**
     * 创建忽略批
     *
     * @param ds         ds
     * @param batchSize  批量大小
     * @param assertions assertions
     * @since 1.0.0
     */
    public void createIgnoreBatch(Collection<D> ds, int batchSize, @NotNull IAssert assertions) {
        BaseCodes.PARAM_VERIFY_ERROR.notEmpty(ds);
        BaseCodes.PARAM_VERIFY_ERROR.isTrue(batchSize > 0);
        assertions.wrapper(() -> service.createIgnoreBatch(ds, batchSize));
    }

    /**
     * 创建替代
     *
     * @param <I>        parameter
     * @param d          d
     * @param assertions assertions
     * @return {@link I}
     * @since 1.0.0
     */
    public <I extends Serializable> I createReplace(@NotNull D d, @NotNull IAssert assertions) {
        BaseCodes.PARAM_VERIFY_ERROR.notNull(d, "新增实体不能为 null");
        BaseCodes.PARAM_VERIFY_ERROR.isNull(d.getId(), "新增数据时不能传入 id");
        return assertions.wrapper(() -> service.createReplace(d));
    }

    /**
     * 创建取代批
     *
     * @param ds         ds
     * @param assertions assertions
     * @since 1.0.0
     */
    public void createReplaceBatch(Collection<D> ds, @NotNull IAssert assertions) {
        BaseCodes.PARAM_VERIFY_ERROR.notEmpty(ds);
        assertions.wrapper(() -> service.createReplaceBatch(ds));
    }

    /**
     * 创建取代批
     *
     * @param ds         ds
     * @param batchSize  批量大小
     * @param assertions assertions
     * @since 1.0.0
     */
    public void createReplaceBatch(Collection<D> ds, int batchSize, @NotNull IAssert assertions) {
        BaseCodes.PARAM_VERIFY_ERROR.notEmpty(ds);
        BaseCodes.PARAM_VERIFY_ERROR.isTrue(batchSize > 0);
        assertions.wrapper(() -> service.createReplaceBatch(ds, batchSize));
    }

    /**
     * 创建或更新
     *
     * @param <I>        parameter
     * @param d          d
     * @param assertions assertions
     * @return {@link I}
     * @since 1.0.0
     */
    public <I extends Serializable> I createOrUpdate(@NotNull D d, @NotNull IAssert assertions) {
        BaseCodes.PARAM_VERIFY_ERROR.notNull(d);
        return assertions.wrapper(() -> service.createOrUpdate(d));
    }

    /**
     * 创建或更新批
     *
     * @param ds         ds
     * @param assertions assertions
     * @since 1.0.0
     */
    public void createOrUpdateBatch(Collection<D> ds, @NotNull IAssert assertions) {
        BaseCodes.PARAM_VERIFY_ERROR.notEmpty(ds);
        assertions.wrapper(() -> service.createOrUpdateBatch(ds));
    }

    /**
     * 创建或更新批
     *
     * @param ds         ds
     * @param batchSize  批量大小
     * @param assertions assertions
     * @since 1.0.0
     */
    public void createOrUpdateBatch(Collection<D> ds, int batchSize, @NotNull IAssert assertions) {
        BaseCodes.PARAM_VERIFY_ERROR.notEmpty(ds);
        BaseCodes.PARAM_VERIFY_ERROR.isTrue(batchSize > 0);
        assertions.wrapper(() -> service.createOrUpdateBatch(ds, batchSize));
    }

    /**
     * 更新
     *
     * @param d          实体
     * @param assertions assertions
     * @since 1.0.0
     */
    public void update(@NotNull D d, @NotNull IAssert assertions) {
        BaseCodes.PARAM_VERIFY_ERROR.notNull(d);
        assertions.wrapper(() -> service.update(d));
    }

    /**
     * 批处理更新
     *
     * @param ds         ds
     * @param assertions assertions
     * @since 1.0.0
     */
    public void updateBatch(Collection<D> ds, @NotNull IAssert assertions) {
        BaseCodes.PARAM_VERIFY_ERROR.notEmpty(ds);
        assertions.wrapper(() -> service.updateBatch(ds));
    }

    /**
     * 批处理更新
     *
     * @param ds         ds
     * @param batchSize  批量大小
     * @param assertions assertions
     * @since 1.0.0
     */
    public void updateBatch(Collection<D> ds, int batchSize, @NotNull IAssert assertions) {
        BaseCodes.PARAM_VERIFY_ERROR.notEmpty(ds);
        BaseCodes.PARAM_VERIFY_ERROR.isTrue(batchSize > 0);
        assertions.wrapper(() -> service.updateBatch(ds, batchSize));
    }

    /**
     * 删除
     *
     * @param <I>        parameter
     * @param id         id
     * @param assertions assertions
     * @since 1.0.0
     */
    public <I extends Serializable> void delete(@NotNull I id, @NotNull IAssert assertions) {
        BaseCodes.PARAM_VERIFY_ERROR.notNull(id);
        assertions.wrapper(() -> service.delete(id));
    }

    /**
     * 删除
     *
     * @param <I>        parameter
     * @param ids        id
     * @param assertions assertions
     * @since 1.0.0
     */
    public <I extends Serializable> void delete(Collection<I> ids, @NotNull IAssert assertions) {
        BaseCodes.PARAM_VERIFY_ERROR.notEmpty(ids);
        assertions.wrapper(() -> service.delete(ids));
    }

    /**
     * 删除
     *
     * @param columnMap  列映射
     * @param assertions assertions
     * @since 1.0.0
     */
    public void delete(@NotNull Map<String, Object> columnMap, @NotNull IAssert assertions) {
        BaseCodes.PARAM_VERIFY_ERROR.notEmpty(columnMap);
        assertions.wrapper(() -> service.delete(columnMap));
    }

    /**
     * 计数
     *
     * @param assertions assertions
     * @return int int
     * @since 1.0.0
     */
    public long counts(@NotNull IAssert assertions) {
        return assertions.wrapper(() -> service.counts());
    }

    /**
     * 计数
     *
     * @param <E>        parameter
     * @param query      查询
     * @param assertions assertions
     * @return int int
     * @since 1.0.0
     */
    public <E extends BaseQuery<? extends Serializable>> int counts(@NotNull E query, @NotNull IAssert assertions) {
        BaseCodes.PARAM_VERIFY_ERROR.notNull(query);
        return assertions.wrapper(() -> service.counts(query));
    }

    /**
     * 找到
     *
     * @param <I>        parameter
     * @param id         id
     * @param assertions assertions
     * @return {@link D}
     * @since 1.0.0
     */
    public <I extends Serializable> D find(@NotNull I id, @NotNull IAssert assertions) {
        BaseCodes.PARAM_VERIFY_ERROR.notNull(id);
        return assertions.wrapper(() -> service.find(id));
    }

    /**
     * 找到
     *
     * @param <E>        parameter
     * @param query      查询
     * @param assertions assertions
     * @return {@link D}
     * @since 1.0.0
     */
    public <E extends BaseQuery<? extends Serializable>> D find(@NotNull E query, @NotNull IAssert assertions) {
        BaseCodes.PARAM_VERIFY_ERROR.notNull(query);
        return assertions.wrapper(() -> service.find(query));
    }

    /**
     * 找到
     *
     * @param <I>        parameter
     * @param ids        id
     * @param assertions assertions
     * @return {@link List}<{@link D}>
     * @since 1.0.0
     */
    public <I extends Serializable> List<D> find(Collection<I> ids, @NotNull IAssert assertions) {
        BaseCodes.PARAM_VERIFY_ERROR.notEmpty(ids);
        return assertions.wrapper(() -> service.find(ids));
    }

    /**
     * 找到所有
     *
     * @param assertions assertions
     * @return {@link List}<{@link D}>
     * @since 1.0.0
     */
    public List<D> find(@NotNull IAssert assertions) {
        return assertions.wrapper(() -> service.find());
    }

    /**
     * 页面
     *
     * @param <D1>       parameter
     * @param <E>        parameter
     * @param query      查询
     * @param assertions assertions
     * @return {@link IPage}<{@link D1}>
     * @since 1.0.0
     */
    public <D1 extends BaseDTO<? extends Serializable>,
        E extends BaseQuery<? extends Serializable>> IPage<D1> page(@NotNull E query, @NotNull IAssert assertions) {
        BaseCodes.PARAM_VERIFY_ERROR.notNull(query);
        return assertions.wrapper(() -> service.page(query));
    }

    /**
     * 列表
     *
     * @param <D1>       parameter
     * @param <E>        parameter
     * @param query      查询
     * @param assertions assertions
     * @return {@link List}<{@link D1}>
     * @since 1.0.0
     */
    public <D1 extends BaseDTO<? extends Serializable>,
        E extends BaseQuery<? extends Serializable>> List<D1> list(@NotNull E query, @NotNull IAssert assertions) {
        BaseCodes.PARAM_VERIFY_ERROR.notNull(query);
        return assertions.wrapper(() -> service.list(query));
    }
}
