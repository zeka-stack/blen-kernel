package dev.dong4j.zeka.kernel.common.base.command;

import dev.dong4j.zeka.kernel.common.asserts.Assertions;
import dev.dong4j.zeka.kernel.common.base.IRepositoryService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

/**
 * <p>Description:  </p>
 *
 * @param <S>   {@link IRepositoryService} 子类
 * @param <DTO> DTO 实体
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.11.23 21:05
 * @see IRepositoryService
 * @since 1.8.0
 */
@SuppressWarnings(value = {"java:S119", "SpringJavaAutowiredMembersInspection"})
public class CommandDelegateServiceImpl<S extends IRepositoryService<DTO>, DTO> implements ICommandDelegateService<DTO> {
    /** 服务 */
    @Autowired
    protected S service;

    /**
     * 创建
     * 根据 DTO 新增数据
     *
     * @param <I> parameter
     * @param dto dto
     * @return the serializable
     * @throws Exception 异常
     * @since 1.8.0
     */
    @Override
    public <I extends Serializable> I create(@NotNull DTO dto) throws Exception {
        Assertions.notNull(dto);
        return this.service.create(dto);
    }

    /**
     * 创建忽略
     * 插入如果中已经存在相同的记录,则忽略当前新数据
     *
     * @param <I> parameter
     * @param dto dto
     * @return 是否成功 boolean
     * @throws Exception exception
     * @since 1.0.0
     */
    @Override
    public <I extends Serializable> I createIgnore(@NotNull DTO dto) throws Exception {
        Assertions.notNull(dto);
        return this.service.createIgnore(dto);
    }

    /**
     * 创建替代
     * 表示插入替换数据,需求表中有PrimaryKey,或者unique索引,如果数据库已经存在数据,则用新数据替换,如果没有数据效果则和insert into一样;
     *
     * @param <I> parameter
     * @param dto dto
     * @return 是否成功 boolean
     * @throws Exception exception
     * @since 1.0.0
     */
    @Override
    public <I extends Serializable> I createReplace(@NotNull DTO dto) throws Exception {
        Assertions.notNull(dto);
        return this.service.createReplace(dto);
    }

    /**
     * 创建或更新
     * TableId 注解存在更新记录，否插入一条记录
     *
     * @param <I> parameter
     * @param dto dto
     * @return the boolean
     * @throws Exception exception
     * @since 1.8.0
     */
    @Override
    public <I extends Serializable> I createOrUpdate(@NotNull DTO dto) throws Exception {
        Assertions.notNull(dto);
        return this.service.createOrUpdate(dto);
    }

    /**
     * 创建忽略批
     * 插入 (批量) ,插入如果中已经存在相同的记录,则忽略当前新数据
     *
     * @param dats dats
     * @throws Exception exception
     * @since 1.0.0
     */
    @Override
    public void createIgnoreBatch(Collection<DTO> dats) throws Exception {
        Assertions.notNull(dats);
        this.service.createIgnoreBatch(dats);
    }

    /**
     * 创建忽略批
     * 插入 (批量) ,插入如果中已经存在相同的记录,则忽略当前新数据
     *
     * @param dtos      dtos
     * @param batchSize 批次大小
     * @throws Exception exception
     * @since 1.0.0
     */
    @Override
    public void createIgnoreBatch(Collection<DTO> dtos, int batchSize) throws Exception {
        Assertions.notEmpty(dtos);
        this.service.createIgnoreBatch(dtos, batchSize);
    }

    /**
     * 创建取代批
     * 插入 (批量) ,表示插入替换数据,需求表中有PrimaryKey,或者unique索引,如果数据库已经存在数据,则用新数据替换,如果没有数据效果则和insert into一样;
     *
     * @param dtos dats
     * @throws Exception exception
     * @since 1.0.0
     */
    @Override
    public void createReplaceBatch(Collection<DTO> dtos) throws Exception {
        Assertions.notEmpty(dtos);
        this.service.createReplaceBatch(dtos);
    }

    /**
     * 创建取代批
     * 插入 (批量) ,表示插入替换数据,需求表中有PrimaryKey,或者unique索引,如果数据库已经存在数据,则用新数据替换,如果没有数据效果则和insert into一样;
     *
     * @param dtos      dtos
     * @param batchSize 批次大小
     * @throws Exception exception
     * @since 1.0.0
     */
    @Override
    public void createReplaceBatch(Collection<DTO> dtos, int batchSize) throws Exception {
        this.service.createReplaceBatch(dtos, batchSize);
    }

    /**
     * 创建批处理
     * 插入（批量）
     *
     * @param dtos dtos
     * @throws Exception exception
     * @since 1.8.0
     */
    @Override
    public void createBatch(Collection<DTO> dtos) throws Exception {
        Assertions.notEmpty(dtos);
        this.service.createBatch(dtos);
    }

    /**
     * 创建批处理
     * 插入（批量）
     *
     * @param dtos      dtos
     * @param batchSize 插入批次数量
     * @throws Exception exception
     * @since 1.8.0
     */
    @Override
    public void createBatch(Collection<DTO> dtos, int batchSize) throws Exception {
        Assertions.notEmpty(dtos);
        this.service.createBatch(dtos, batchSize);
    }

    /**
     * 更新
     * 通过 DTO 更新数据
     *
     * @param dto dto
     * @throws Exception exception
     * @since 1.8.0
     */
    @Override
    public void update(@NotNull DTO dto) throws Exception {
        Assertions.notNull(dto);
        this.service.update(dto);
    }

    /**
     * 批处理更新
     * 根据ID 批量更新
     *
     * @param dtos dtos
     * @throws Exception exception
     * @since 1.8.0
     */
    @Override
    public void updateBatch(Collection<DTO> dtos) throws Exception {
        Assertions.notEmpty(dtos);
        this.service.updateBatch(dtos);
    }

    /**
     * 批处理更新
     * 根据ID 批量更新
     *
     * @param dtos      dtos
     * @param batchSize 更新批次数量
     * @throws Exception exception
     * @since 1.8.0
     */
    @Override
    public void updateBatch(Collection<DTO> dtos, int batchSize) throws Exception {
        Assertions.notEmpty(dtos);
        this.service.updateBatch(dtos, batchSize);
    }

    /**
     * 创建或更新批
     * 批量修改插入
     *
     * @param dtos dtos
     * @throws Exception exception
     * @since 1.8.0
     */
    @Override
    public void createOrUpdateBatch(Collection<DTO> dtos) throws Exception {
        Assertions.notEmpty(dtos);
        this.service.createOrUpdateBatch(dtos);
    }

    /**
     * 创建或更新批
     * 批量修改插入
     *
     * @param dtos      dtos
     * @param batchSize 每次的数量
     * @throws Exception exception
     * @since 1.8.0
     */
    @Override
    public void createOrUpdateBatch(Collection<DTO> dtos, int batchSize) throws Exception {
        Assertions.notEmpty(dtos);
        this.service.createOrUpdateBatch(dtos, batchSize);
    }

    /**
     * 删除
     * 通过 id 删除数据
     *
     * @param <I> parameter
     * @param id  id
     * @throws Exception exception
     * @since 1.8.0
     */
    @Override
    public <I extends Serializable> void delete(@NotNull I id) throws Exception {
        Assertions.notNull(id);
        this.service.delete(id);
    }

    /**
     * 删除
     * Remove
     *
     * @param <I> parameter
     * @param ids ids
     * @throws Exception exception
     * @since 1.8.0
     */
    @Override
    public <I extends Serializable> void delete(Collection<I> ids) throws Exception {
        Assertions.notEmpty(ids);
        this.service.delete(ids);
    }

    /**
     * 删除
     * 根据 columnMap 条件，删除记录
     *
     * @param columnMap 表字段 map 对象
     * @throws Exception exception
     * @since 1.8.0
     */
    @Override
    public void delete(@NotNull Map<String, Object> columnMap) throws Exception {
        Assertions.notEmpty(columnMap);
        this.service.delete(columnMap);
    }

}
