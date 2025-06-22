package dev.dong4j.zeka.kernel.common.mapstruct;

import com.baomidou.mybatisplus.core.metadata.IPage;
import dev.dong4j.zeka.kernel.common.mybatis.support.Page;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>Description: 服务层转换包装基类 </p>
 *
 * @param <D> parameter DTO
 * @param <P> parameter PO (mysql/mongo)
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.09.30 16:42
 * @since 1.6.0
 */
public interface ServiceConverter<D, P> {

    /**
     * 服务层入参必须是 dto, 在操作数据库时, 入参必须是 po, 此接口用于 dto -> po, 且只能用于服务层.
     *
     * @param dto dto
     * @return the p
     * @since 1.0.0
     */
    P po(D dto);

    /**
     * 服务层操作数据库时返回的可能是 po 或者是 dto, 此接口用于 op -> dto, 且只能用于服务层.
     *
     * @param po po
     * @return the d
     * @since 1.0.0
     */
    D dto(P po);

    /**
     * 分页实体类集合包装
     *
     * @param pages pages
     * @return page page
     * @since 1.6.0
     */
    default IPage<D> dto(@NotNull IPage<P> pages) {
        List<D> records = this.dto(pages.getRecords());
        IPage<D> pageVo = new Page<>(pages.getCurrent(), pages.getSize(), pages.getTotal());
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
    default List<D> dto(@NotNull List<P> list) {
        return list.stream().map(this::dto).collect(Collectors.toList());
    }

    /**
     * Dto
     *
     * @param collection collection
     * @return the collection
     * @since 1.8.0
     */
    default Collection<D> dto(@NotNull Collection<P> collection) {
        return collection.stream().map(this::dto).collect(Collectors.toCollection(HashSet::new));
    }

    /**
     * Po
     *
     * @param list list
     * @return the list
     * @since 1.6.0
     */
    default List<P> po(@NotNull List<D> list) {
        return list.stream().map(this::po).collect(Collectors.toList());
    }

    /**
     * Po
     *
     * @param collection collection
     * @return the collection
     * @since 1.8.0
     */
    default Collection<P> po(@NotNull Collection<D> collection) {
        return collection.stream().map(this::po).collect(Collectors.toCollection(HashSet::new));
    }
}
