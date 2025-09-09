package dev.dong4j.zeka.kernel.common.base.command;

import dev.dong4j.zeka.kernel.common.asserts.Assertions;
import dev.dong4j.zeka.kernel.common.base.IRepositoryService;
import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>命令委托服务实现类.
 * <p>为命令模式提供完整的实现，封装了所有的数据修改操作（创建、更新、删除）.
 * <p>通过委托模式将具体的数据操作委托给 IRepositoryService，实现了分层架构的解耦.
 * <p>主要功能：
 * <ul>
 *     <li>提供完整的 CRUD 操作实现</li>
 *     <li>支持单条和批量数据操作</li>
 *     <li>支持多种插入策略（忽略、替换、新增或更新）</li>
 *     <li>提供参数校验和异常处理</li>
 * </ul>
 * <p>支持的操作类型：
 * <ul>
 *     <li>创建操作：create、createIgnore、createReplace、createOrUpdate</li>
 *     <li>批量创建：createBatch、createIgnoreBatch、createReplaceBatch</li>
 *     <li>更新操作：update、updateBatch、createOrUpdateBatch</li>
 *     <li>删除操作：按 ID、按条件、批量删除</li>
 * </ul>
 * <p>设计优势：
 * <ul>
 *     <li>统一的参数校验和异常处理</li>
 *     <li>支持微服务架构下的服务分层</li>
 *     <li>灵活的数据操作策略选择</li>
 *     <li>高性能的批量操作支持</li>
 * </ul>
 *
 * @param <S>   IRepositoryService 的子类，提供具体的数据操作实现
 * @param <DTO> 数据传输对象类型
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.11.23 21:05
 * @see IRepositoryService
 * @since 1.0.0
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
     * @since 1.0.0
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
     * @since 1.0.0
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
     * @since 1.0.0
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
     * @since 1.0.0
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
     * @since 1.0.0
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
     * @since 1.0.0
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
     * @since 1.0.0
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
     * @since 1.0.0
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
     * @since 1.0.0
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
     * @since 1.0.0
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
     * @since 1.0.0
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
     * @since 1.0.0
     */
    @Override
    public void delete(@NotNull Map<String, Object> columnMap) throws Exception {
        Assertions.notEmpty(columnMap);
        this.service.delete(columnMap);
    }

}
