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
 * <p>Description: 查询类接口, 使用 {@link IRepositoryService} 桥接到 DAO 层 </p>
 *
 * @param <S>   {@link IRepositoryService} 子类
 * @param <DTO> DTO 实体
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
