package dev.dong4j.zeka.kernel.common.mapstruct;

import com.baomidou.mybatisplus.core.metadata.IPage;
import dev.dong4j.zeka.kernel.common.mybatis.support.Page;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>Description: 视图转换包装基类 </p>
 *
 * @param <F> parameter form/query
 * @param <D> parameter dto
 * @param <V> parameter vo/page
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.09.30 16:40
 * @since 1.6.0
 */
public interface ViewConverter<F, D, V> {

    /**
     * form 转 dto
     *
     * @param form form 接口入参
     * @return the d    service 层入参
     * @since 1.0.0
     */
    D dto(F form);

    /**
     * 服务层返回的实体必须是 dto, 在 controller 层用于将 dto 转为 vo, 此接口只能用在 controller 层
     *
     * @param dto dto   service 层出参
     * @return the v    controller 层响应结果
     * @since 1.0.0
     */
    V vo(D dto);

    /**
     * 分页实体类集合包装
     *
     * @param pages pages
     * @return page page
     * @since 1.6.0
     */
    default IPage<V> vo(@NotNull IPage<D> pages) {
        List<V> records = this.vo(pages.getRecords());
        IPage<V> pageVo = new Page<>(pages.getCurrent(), pages.getSize(), pages.getTotal());
        pageVo.setRecords(records);
        return pageVo;
    }

    /**
     * 实体类集合包装
     *
     * @param list list
     * @return list list
     * @since 1.6.0
     */
    default List<V> vo(@NotNull List<D> list) {
        return list.stream().map(this::vo).collect(Collectors.toList());
    }

    /**
     * Vo
     *
     * @param collection collection
     * @return the collection
     * @since 1.8.0
     */
    default Collection<V> vo(@NotNull Collection<D> collection) {
        return collection.stream().map(this::vo).collect(Collectors.toCollection(HashSet::new));
    }

    /**
     * 实体类集合包装
     *
     * @param list list
     * @return list list
     * @since 1.6.0
     */
    default List<D> dto(@NotNull List<F> list) {
        return list.stream().map(this::dto).collect(Collectors.toList());
    }

    /**
     * dto
     *
     * @param collection collection
     * @return the collection
     * @since 1.8.0
     */
    default Collection<D> dto(@NotNull Collection<F> collection) {
        return collection.stream().map(this::dto).collect(Collectors.toCollection(HashSet::new));
    }
}
