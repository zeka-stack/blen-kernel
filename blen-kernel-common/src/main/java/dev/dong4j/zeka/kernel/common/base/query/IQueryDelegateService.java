package dev.dong4j.zeka.kernel.common.base.query;

import com.baomidou.mybatisplus.core.metadata.IPage;
import dev.dong4j.zeka.kernel.common.base.BaseDTO;
import dev.dong4j.zeka.kernel.common.base.BaseQuery;
import dev.dong4j.zeka.kernel.common.base.IRepositoryService;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * <p>Description: </p>命令类接口, 使用 {@link IRepositoryService} 桥接到 DAO 层. </p>
 * 可由 dubbo provider, agent service 等继承
 *
 * @param <DTO> parameter
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2021.02.28 01:53
 * @since 1.8.0
 */
@SuppressWarnings("java:S119")
public interface IQueryDelegateService<DTO> {

    /**
     * 计数
     * 查询总记录数
     *
     * @return the int
     * @since 1.8.0
     */
    int counts();

    /**
     * 计数
     * 查询总记录数
     *
     * @param <Q>   parameter
     * @param query query
     * @return the int
     * @since 2.1.0
     */
    <Q extends BaseQuery<? extends Serializable>> int counts(@NotNull Q query);

    /**
     * 找到
     * 通过 id 查询 DTO 实体
     *
     * @param <I> parameter
     * @param id  id
     * @return the t
     * @since 1.8.0
     */
    <I extends Serializable> DTO find(@NotNull I id);

    /**
     * 找到
     *
     * @param <Q>   parameter
     * @param query 查询
     * @return {@link DTO}
     * @since 2.1.0
     */
    <Q extends BaseQuery<? extends Serializable>> DTO find(@NotNull Q query);

    /**
     * 找到
     * 查询（根据ID 批量查询）
     *
     * @param <I> parameter
     * @param ids 主键ID列表
     * @return the list
     * @since 1.8.0
     */
    <I extends Serializable> List<DTO> find(Collection<I> ids);

    /**
     * 找到所有
     * 查询所有的 DTO 数据
     *
     * @return the list
     * @since 1.8.0
     */
    List<DTO> find();

    /**
     * 页面
     * 通过参数查询 DTO 分页数据
     *
     * @param <D>   parameter
     * @param <Q>   parameter
     * @param query 查询参数
     * @return the page
     * @since 1.8.0
     */
    <D extends BaseDTO<? extends Serializable>, Q extends BaseQuery<? extends Serializable>> IPage<D> page(@NotNull Q query);

    /**
     * 列表
     * 通过参数查询 DTO list 数据
     *
     * @param <D>   parameter
     * @param <Q>   parameter
     * @param query 查询参数
     * @return the list
     * @since 1.8.0
     */
    <D extends BaseDTO<? extends Serializable>, Q extends BaseQuery<? extends Serializable>> List<D> list(@NotNull Q query);
}
