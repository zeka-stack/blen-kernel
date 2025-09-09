package dev.dong4j.zeka.kernel.common.base.query;

import com.baomidou.mybatisplus.core.metadata.IPage;
import dev.dong4j.zeka.kernel.common.asserts.Assertions;
import dev.dong4j.zeka.kernel.common.base.BaseDTO;
import dev.dong4j.zeka.kernel.common.base.BaseQuery;
import dev.dong4j.zeka.kernel.common.base.IRepositoryService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>查询委托服务实现类.
 * <p>为查询模式提供完整的实现，使用 IRepositoryService 桥接到 DAO 层，实现命令与查询的分离.
 * <p>主要功能：
 * <ul>
 *     <li>实现 IQueryDelegateService 接口的所有查询操作</li>
 *     <li>提供完整的数据查询功能（单条、列表、分页、统计）</li>
 *     <li>通过委托模式将具体查询实现交给 IRepositoryService</li>
 *     <li>统一的参数校验和异常处理机制</li>
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
 *     <li>实现了 CQRS（命令查询职责分离）中的查询部分</li>
 *     <li>通过委托模式降低与具体实现的耦合度</li>
 *     <li>提供统一的查询接口和参数规范</li>
 *     <li>支持 Spring 依赖注入和自动装配</li>
 * </ul>
 * <p>适用场景：
 * <ul>
 *     <li>需要纯查询功能的业务服务层</li>
 *     <li>读写分离架构中的读操作层</li>
 *     <li>查询专用的微服务或组件</li>
 *     <li>需要统一查询接口的复杂业务</li>
 * </ul>
 * <p>性能特性：
 * <ul>
 *     <li>支持高效的分页查询和批量查询</li>
 *     <li>内置参数校验，避免无效查询</li>
 *     <li>灵活的查询条件组合和过滤</li>
 *     <li>支持查询结果的类型转换和映射</li>
 * </ul>
 *
 * @param <S>   IRepositoryService 的具体实现类
 * @param <DTO> 数据传输对象类型
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.11.23 21:05
 * @see IRepositoryService
 * @since 1.0.0
 */
@SuppressWarnings(value = {"java:S119", "SpringJavaAutowiredMembersInspection"})
public class QueryDelegateServiceImpl<S extends IRepositoryService<DTO>, DTO> implements IQueryDelegateService<DTO> {
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    protected S service;

    /**
     * 计数
     * 查询总记录数
     *
     * @return the int
     * @since 1.0.0
     */
    @Override
    public long counts() {
        return this.service.counts();
    }

    /**
     * 计数
     * 查询总记录数
     *
     * @param <Q>   parameter
     * @param query query
     * @return the int
     * @since 1.0.0
     */
    @Override
    public <Q extends BaseQuery<? extends Serializable>> int counts(@NotNull Q query) {
        return this.service.counts(query);
    }

    /**
     * 找到
     * 通过 id 查询 DTO 实体
     *
     * @param <I> parameter
     * @param id  id
     * @return the t
     * @since 1.0.0
     */
    @Override
    public <I extends Serializable> DTO find(@NotNull I id) {
        Assertions.notNull(id);
        return this.service.find(id);
    }

    /**
     * 找到
     *
     * @param <Q>   parameter
     * @param query 查询
     * @return {@link DTO}
     * @since 1.0.0
     */
    @Override
    public <Q extends BaseQuery<? extends Serializable>> DTO find(@NotNull Q query) {
        Assertions.notNull(query);
        return this.service.find(query);
    }

    /**
     * 找到所有
     * 查询所有的 DTO 数据
     *
     * @return the list
     * @since 1.0.0
     */
    @Override
    public List<DTO> find() {
        return this.service.find();
    }

    /**
     * 找到
     * 查询（根据ID 批量查询）
     *
     * @param <I> parameter
     * @param ids 主键ID列表
     * @return the list
     * @since 1.0.0
     */
    @Override
    public <I extends Serializable> List<DTO> find(Collection<I> ids) {
        Assertions.notEmpty(ids);
        return this.service.find(ids);
    }

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
    @Override
    public <D extends BaseDTO<? extends Serializable>, Q extends BaseQuery<? extends Serializable>> IPage<D> page(@NotNull Q query) {
        Assertions.notNull(query);
        return this.service.page(query);
    }

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
    @Override
    public <D extends BaseDTO<? extends Serializable>, Q extends BaseQuery<? extends Serializable>> List<D> list(@NotNull Q query) {
        Assertions.notNull(query);
        return this.service.list(query);
    }
}
