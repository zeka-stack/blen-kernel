package dev.dong4j.zeka.kernel.common.base.query;

import com.baomidou.mybatisplus.core.metadata.IPage;
import dev.dong4j.zeka.kernel.common.base.BaseDTO;
import dev.dong4j.zeka.kernel.common.base.BaseQuery;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/**
 * <p>查询委托服务接口.
 * <p>为查询模式提供统一的接口规范，封装了所有的数据查询操作（单条、列表、分页、统计）.
 * <p>通过委托模式使用 IRepositoryService 桥接到 DAO 层，实现了查询与命令的分离.
 * <p>主要功能：
 * <ul>
 *     <li>提供完整的数据查询操作</li>
 *     <li>支持单条和批量数据查询</li>
 *     <li>支持灵活的查询条件和分页参数</li>
 *     <li>提供数据统计和聚合查询</li>
 * </ul>
 * <p>支持的查询类型：
 * <ul>
 *     <li>按 ID 查询：find(id)、find(ids)</li>
 *     <li>按条件查询：find(query)、list(query)</li>
 *     <li>分页查询：page(query)</li>
 *     <li>统计查询：counts()、counts(query)</li>
 * </ul>
 * <p>设计优势：
 * <ul>
 *     <li>统一的查询接口和参数规范</li>
 *     <li>支持 Dubbo Provider 和 Agent Service 的继承</li>
 *     <li>高度的类型安全和泛型支持</li>
 *     <li>灵活的扩展能力和自定义查询</li>
 * </ul>
 *
 * @param <DTO> 数据传输对象类型
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2021.02.28 01:53
 * @since 1.0.0
 */
@SuppressWarnings("java:S119")
public interface IQueryDelegateService<DTO> {

    /**
     * 计数
     * 查询总记录数
     *
     * @return the int
     * @since 1.0.0
     */
    long counts();

    /**
     * 计数
     * 查询总记录数
     *
     * @param <Q>   parameter
     * @param query query
     * @return the int
     * @since 1.0.0
     */
    <Q extends BaseQuery<? extends Serializable>> int counts(@NotNull Q query);

    /**
     * 找到
     * 通过 id 查询 DTO 实体
     *
     * @param <I> parameter
     * @param id  id
     * @return the t
     * @since 1.0.0
     */
    <I extends Serializable> DTO find(@NotNull I id);

    /**
     * 找到
     *
     * @param <Q>   parameter
     * @param query 查询
     * @return {@link DTO}
     * @since 1.0.0
     */
    <Q extends BaseQuery<? extends Serializable>> DTO find(@NotNull Q query);

    /**
     * 找到
     * 查询（根据ID 批量查询）
     *
     * @param <I> parameter
     * @param ids 主键ID列表
     * @return the list
     * @since 1.0.0
     */
    <I extends Serializable> List<DTO> find(Collection<I> ids);

    /**
     * 找到所有
     * 查询所有的 DTO 数据
     *
     * @return the list
     * @since 1.0.0
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
     * @since 1.0.0
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
     * @since 1.0.0
     */
    <D extends BaseDTO<? extends Serializable>, Q extends BaseQuery<? extends Serializable>> List<D> list(@NotNull Q query);
}
