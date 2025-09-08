package dev.dong4j.zeka.kernel.common.base.command;

import dev.dong4j.zeka.kernel.common.base.IRepositoryService;
import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

/**
 * <p>Description: 命令类接口, 使用 {@link IRepositoryService} 桥接到 DAO 层. </p>
 * 可由 dubbo provider, agent service 等继承
 *
 * @param <DTO> parameter
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2021.02.28 01:53
 * @since 1.0.0
 */
@SuppressWarnings("java:S119")
public interface ICommandDelegateService<DTO> {

    /**
     * 创建
     * 根据 DTO 新增数据
     *
     * @param <I> parameter
     * @param dto dto
     * @return the boolean
     * @throws Exception 异常
     * @since 1.0.0
     */
    <I extends Serializable> I create(@NotNull DTO dto) throws Exception;

    /**
     * 创建批处理
     * 插入（批量）
     *
     * @param dtos dtos
     * @throws Exception exception
     * @since 1.0.0
     */
    void createBatch(Collection<DTO> dtos) throws Exception;

    /**
     * 创建批处理
     * 插入（批量）
     *
     * @param dtos      dtos
     * @param batchSize 插入批次数量
     * @throws Exception exception
     * @since 1.0.0
     */
    void createBatch(Collection<DTO> dtos, int batchSize) throws Exception;

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
    <I extends Serializable> I createIgnore(@NotNull DTO dto) throws Exception;

    /**
     * 创建忽略批
     * 插入 (批量) ,插入如果中已经存在相同的记录,则忽略当前新数据
     *
     * @param dtos dats
     * @throws Exception exception
     * @since 1.0.0
     */
    void createIgnoreBatch(Collection<DTO> dtos) throws Exception;

    /**
     * 创建忽略批
     * 插入 (批量) ,插入如果中已经存在相同的记录,则忽略当前新数据
     *
     * @param dtos      dats
     * @param batchSize 批次大小
     * @throws Exception exception
     * @since 1.0.0
     */
    void createIgnoreBatch(Collection<DTO> dtos, int batchSize) throws Exception;

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
    <I extends Serializable> I createReplace(@NotNull DTO dto) throws Exception;

    /**
     * 创建取代批
     * 插入 (批量) ,表示插入替换数据,需求表中有PrimaryKey,或者unique索引,如果数据库已经存在数据,则用新数据替换,如果没有数据效果则和insert into一样;
     *
     * @param dtos dats
     * @throws Exception exception
     * @since 1.0.0
     */
    void createReplaceBatch(Collection<DTO> dtos) throws Exception;

    /**
     * 创建取代批
     * 插入 (批量) ,表示插入替换数据,需求表中有PrimaryKey,或者unique索引,如果数据库已经存在数据,则用新数据替换,如果没有数据效果则和insert into一样;
     *
     * @param dtos      dats
     * @param batchSize 批次大小
     * @throws Exception exception
     * @since 1.0.0
     */
    void createReplaceBatch(Collection<DTO> dtos, int batchSize) throws Exception;

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
    <I extends Serializable> I createOrUpdate(@NotNull DTO dto) throws Exception;

    /**
     * 创建或更新批
     * Create or update batch
     *
     * @param dtos dtos
     * @throws Exception exception
     * @since 1.0.0
     */
    void createOrUpdateBatch(Collection<DTO> dtos) throws Exception;

    /**
     * 创建或更新批
     * 批量修改插入
     *
     * @param dtos      dtos
     * @param batchSize 每次的数量
     * @throws Exception exception
     * @since 1.0.0
     */
    void createOrUpdateBatch(Collection<DTO> dtos, int batchSize) throws Exception;

    /**
     * 更新
     * 通过 DTO 更新数据
     *
     * @param entity entity
     * @throws Exception exception
     * @since 1.0.0
     */
    void update(@NotNull DTO entity) throws Exception;

    /**
     * 批处理更新
     * 根据ID 批量更新
     *
     * @param dtos dtos
     * @throws Exception exception
     * @since 1.0.0
     */
    void updateBatch(Collection<DTO> dtos) throws Exception;

    /**
     * 批处理更新
     * 根据ID 批量更新
     *
     * @param dtos      dtos
     * @param batchSize 更新批次数量
     * @throws Exception exception
     * @since 1.0.0
     */
    void updateBatch(Collection<DTO> dtos, int batchSize) throws Exception;

    /**
     * 删除
     * 通过 id 删除数据
     *
     * @param <I> parameter
     * @param id  id
     * @throws Exception exception
     * @since 1.0.0
     */
    <I extends Serializable> void delete(@NotNull I id) throws Exception;

    /**
     * 删除
     * 批量删除
     *
     * @param <I> parameter
     * @param ids ids
     * @throws Exception exception
     * @since 1.0.0
     */
    <I extends Serializable> void delete(Collection<I> ids) throws Exception;

    /**
     * 删除
     * Delete
     *
     * @param columnMap column map
     * @throws Exception exception
     * @since 1.0.0
     */
    void delete(@NotNull Map<String, Object> columnMap) throws Exception;
}
