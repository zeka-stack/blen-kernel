package dev.dong4j.zeka.kernel.common.base;

import com.baomidou.mybatisplus.core.metadata.IPage;
import dev.dong4j.zeka.kernel.common.asserts.Assertions;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>Description: 合并 command 和 query 接口 </p>
 *
 * @param <S>   {@link IRepositoryService} 子类
 * @param <DTO> DTO 实体
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.11.23 21:05
 * @see IRepositoryService
 * @since 1.7.0
 */
@SuppressWarnings(value = {"java:S119", "SpringJavaAutowiredMembersInspection"})
public class CrudDelegateImpl<S extends IRepositoryService<DTO>, DTO> implements ICrudDelegate<DTO> {
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    protected S service;

    /**
     * 根据 DTO 新增数据
     *
     * @param dto dto
     * @return the serializable
     * @since 1.8.0
     */
    @Override
    public <I extends Serializable> I create(@NotNull DTO dto) throws Exception {
        Assertions.notNull(dto);
        return this.service.create(dto);
    }

    /**
     * 插入（批量）
     *
     * @param dtos dtos
     * @since 1.8.0
     */
    @Override
    public void createBatch(Collection<DTO> dtos) throws Exception {
        Assertions.notEmpty(dtos);
        this.service.createBatch(dtos);
    }

    /**
     * 插入（批量）
     *
     * @param dtos      dtos
     * @param batchSize 插入批次数量
     * @since 1.8.0
     */
    @Override
    public void createBatch(Collection<DTO> dtos, int batchSize) throws Exception {
        Assertions.notEmpty(dtos);
        this.service.createBatch(dtos, batchSize);
    }

    /**
     * 插入如果中已经存在相同的记录,则忽略当前新数据
     *
     * @param dto dto
     * @return 是否成功 boolean
     * @since 1.0.0
     */
    @Override
    public <I extends Serializable> I createIgnore(@NotNull DTO dto) throws Exception {
        Assertions.notNull(dto);
        return this.service.createIgnore(dto);
    }

    /**
     * 插入 (批量) ,插入如果中已经存在相同的记录,则忽略当前新数据
     *
     * @param dtos dats
     * @since 1.0.0
     */
    @Override
    public void createIgnoreBatch(Collection<DTO> dtos) throws Exception {
        Assertions.notNull(dtos);
        this.service.createIgnoreBatch(dtos);
    }

    /**
     * 插入 (批量) ,插入如果中已经存在相同的记录,则忽略当前新数据
     *
     * @param dtos      dtos
     * @param batchSize 批次大小
     * @since 1.0.0
     */
    @Override
    public void createIgnoreBatch(Collection<DTO> dtos, int batchSize) throws Exception {
        Assertions.notEmpty(dtos);
        this.service.createIgnoreBatch(dtos, batchSize);
    }

    /**
     * 表示插入替换数据,需求表中有PrimaryKey,或者unique索引,如果数据库已经存在数据,则用新数据替换,如果没有数据效果则和insert into一样;
     *
     * @param dto dto
     * @return 是否成功 boolean
     * @since 1.0.0
     */
    @Override
    public <I extends Serializable> I createReplace(@NotNull DTO dto) throws Exception {
        Assertions.notNull(dto);
        return this.service.createReplace(dto);
    }

    /**
     * 插入 (批量) ,表示插入替换数据,需求表中有PrimaryKey,或者unique索引,如果数据库已经存在数据,则用新数据替换,如果没有数据效果则和insert into一样;
     *
     * @param dtos dats
     * @since 1.0.0
     */
    @Override
    public void createReplaceBatch(Collection<DTO> dtos) throws Exception {
        Assertions.notEmpty(dtos);
        this.service.createReplaceBatch(dtos);
    }

    /**
     * 插入 (批量) ,表示插入替换数据,需求表中有PrimaryKey,或者unique索引,如果数据库已经存在数据,则用新数据替换,如果没有数据效果则和insert into一样;
     *
     * @param dtos      dtos
     * @param batchSize 批次大小
     * @since 1.0.0
     */
    @Override
    public void createReplaceBatch(Collection<DTO> dtos, int batchSize) throws Exception {
        this.service.createReplaceBatch(dtos, batchSize);
    }

    /**
     * TableId 注解存在更新记录，否插入一条记录
     *
     * @param dto dto
     * @return the boolean
     * @since 1.8.0
     */
    @Override
    public <I extends Serializable> I createOrUpdate(@NotNull DTO dto) throws Exception {
        Assertions.notNull(dto);
        return this.service.createOrUpdate(dto);
    }

    /**
     * 批量修改插入
     *
     * @param dtos dtos
     * @since 1.8.0
     */
    @Override
    public void createOrUpdateBatch(Collection<DTO> dtos) throws Exception {
        Assertions.notEmpty(dtos);
        this.service.createOrUpdateBatch(dtos);
    }

    /**
     * 批量修改插入
     *
     * @param dtos      dtos
     * @param batchSize 每次的数量
     * @since 1.8.0
     */
    @Override
    public void createOrUpdateBatch(Collection<DTO> dtos, int batchSize) throws Exception {
        Assertions.notEmpty(dtos);
        this.service.createOrUpdateBatch(dtos, batchSize);
    }

    /**
     * 通过 DTO 更新数据
     *
     * @param dto dto
     * @since 1.8.0
     */
    @Override
    public void update(@NotNull DTO dto) throws Exception {
        Assertions.notNull(dto);
        this.service.update(dto);
    }

    /**
     * 根据ID 批量更新
     *
     * @param dtos dtos
     * @since 1.8.0
     */
    @Override
    public void updateBatch(Collection<DTO> dtos) throws Exception {
        Assertions.notEmpty(dtos);
        this.service.updateBatch(dtos);
    }

    /**
     * 根据ID 批量更新
     *
     * @param dtos      dtos
     * @param batchSize 更新批次数量
     * @since 1.8.0
     */
    @Override
    public void updateBatch(Collection<DTO> dtos, int batchSize) throws Exception {
        Assertions.notEmpty(dtos);
        this.service.updateBatch(dtos, batchSize);
    }

    /**
     * 通过 id 删除数据
     *
     * @param id id
     * @since 1.8.0
     */
    @Override
    public void delete(@NotNull Serializable id) throws Exception {
        Assertions.notNull(id);
        this.service.delete(id);
    }

    /**
     * Remove
     *
     * @param ids ids
     * @since 1.8.0
     */
    @Override
    public <I extends Serializable> void delete(Collection<I> ids) throws Exception {
        Assertions.notEmpty(ids);
        this.service.delete(ids);
    }

    /**
     * 根据 columnMap 条件，删除记录
     *
     * @param columnMap 表字段 map 对象
     * @since 1.8.0
     */
    @Override
    public void delete(@NotNull Map<String, Object> columnMap) throws Exception {
        Assertions.notEmpty(columnMap);
        this.service.delete(columnMap);
    }

    /**
     * 查询总记录数
     *
     * @return the int
     * @since 1.8.0
     */
    @Override
    public int counts() {
        return this.service.counts();
    }

    /**
     * 查询总记录数
     *
     * @param query query
     * @return the int
     * @since 1.8.0
     */
    @Override
    public <Q extends BaseQuery<? extends Serializable>> int counts(@NotNull Q query) {
        return this.service.counts(query);
    }

    /**
     * 通过 id 查询 DTO 实体
     *
     * @param id id
     * @return the t
     * @since 1.8.0
     */
    @Override
    public <I extends Serializable> DTO find(@NotNull I id) {
        Assertions.notNull(id);
        return this.service.find(id);
    }

    /**
     * 找到
     *
     * @param query 查询
     * @return {@link DTO}
     */
    @Override
    public <Q extends BaseQuery<? extends Serializable>> DTO find(@NotNull Q query) {
        Assertions.notNull(query);
        return this.service.find(query);
    }

    /**
     * 查询（根据ID 批量查询）
     *
     * @param ids 主键ID列表
     * @return the list
     * @since 1.8.0
     */
    @Override
    public <I extends Serializable> List<DTO> find(Collection<I> ids) {
        Assertions.notEmpty(ids);
        return this.service.find(ids);
    }

    /**
     * 查询所有的 DTO 数据
     *
     * @return the list
     * @since 1.8.0
     */
    @Override
    public List<DTO> find() {
        return this.service.find();
    }

    /**
     * 通过参数查询 DTO 分页数据
     *
     * @param <D>   parameter
     * @param <Q>   BaseQuery 子类
     * @param query 查询参数
     * @return the page
     * @since 1.8.0
     */
    @Override
    public <D extends BaseDTO<? extends Serializable>, Q extends BaseQuery<? extends Serializable>> IPage<D> page(@NotNull Q query) {
        Assertions.notNull(query);
        return this.service.page(query);
    }

    /**
     * 通过参数查询 DTO list 数据
     *
     * @param <D>   parameter
     * @param <Q>   BaseQuery 子类
     * @param query 查询参数
     * @return the list
     * @since 1.8.0
     */
    @Override
    public <D extends BaseDTO<? extends Serializable>, Q extends BaseQuery<? extends Serializable>> List<D> list(@NotNull Q query) {
        Assertions.notNull(query);
        return this.service.list(query);
    }
}
