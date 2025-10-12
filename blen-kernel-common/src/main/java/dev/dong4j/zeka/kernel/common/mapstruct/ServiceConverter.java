package dev.dong4j.zeka.kernel.common.mapstruct;

import com.baomidou.mybatisplus.core.metadata.IPage;
import dev.dong4j.zeka.kernel.common.mybatis.support.Page;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;

/**
 * <p>Description: 服务层转换包装基类 </p>
 *
 * @param <D> parameter DTO
 * @param <P> parameter PO (mysql/mongo)
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.09.30 16:42
 * @since 1.0.0
 */
public interface ServiceConverter<D, P> {

    /**
     * 服务层入参必须是 dto, 在操作数据库时, 入参必须是 po, 此接口用于 dto -> po, 且只能用于服务层.
     *
     * @param dto dto
     * @return the p
     * @since 2024.1.1
     * @deprecated use {@link ServiceConverter#d2p(Object)}
     */
    @Deprecated
    P po(D dto);

    /**
     * 服务层入参必须是 dto, 在操作数据库时, 入参必须是 po, 此接口用于 dto -> po, 且只能用于服务层.
     *
     * @param dto dto
     * @return the p
     * @since 2024.1.1
     */
    P d2p(D dto);

    /**
     * 服务层操作数据库时返回的可能是 po 或者是 dto, 此接口用于 op -> dto, 且只能用于服务层.
     *
     * @param po po
     * @return the d
     * @since 2024.1.1
     * @deprecated use {@link ServiceConverter#p2d(Object)}
     */
    @Deprecated
    D dto(P po);

    /**
     * 服务层操作数据库时返回的可能是 po 或者是 dto, 此接口用于 op -> dto, 且只能用于服务层.
     *
     * @param po po
     * @return the d
     * @since 2024.1.1
     */
    D p2d(P po);

    /**
     * 分页实体类集合包装
     *
     * @param pages pages
     * @return page page
     * @since 2024.1.1
     * @deprecated use {@link ServiceConverter#p2d(IPage)}
     */
    @Deprecated
    default IPage<D> dto(@NotNull IPage<P> pages) {
        return p2d(pages);
    }

    /**
     * P 2 d
     *
     * @param pages pages
     * @return the page
     * @since 2024.2.0
     */
    default IPage<D> p2d(@NotNull IPage<P> pages) {
        List<D> records = this.p2d(pages.getRecords());
        IPage<D> pageVo = new Page<>(pages.getCurrent(), pages.getSize(), pages.getTotal());
        pageVo.setRecords(records);
        return pageVo;
    }

    /**
     * 实体类集合包装
     *
     * @param list list
     * @return list list
     * @since 2024.1.1
     * @deprecated use {@link ServiceConverter#p2d(List)}
     */
    @Deprecated
    default List<D> dto(@NotNull List<P> list) {
        return p2d(list);
    }

    /**
     * 实体类集合包装
     *
     * @param list list
     * @return list list
     * @since 2024.1.1
     */
    default List<D> p2d(@NotNull List<P> list) {
        return list.stream().map(this::p2d).collect(Collectors.toList());
    }

    /**
     * Dto
     *
     * @param collection collection
     * @return the collection
     * @since 2024.1.1
     * @deprecated use {@link ServiceConverter#p2d(Collection)}
     */
    @Deprecated
    default Collection<D> dto(@NotNull Collection<P> collection) {
        return p2d(collection);
    }

    /**
     * P 2 d
     *
     * @param collection collection
     * @return the collection
     * @since 2024.2.0
     */
    default Collection<D> p2d(@NotNull Collection<P> collection) {
        return collection.stream().map(this::p2d).collect(Collectors.toCollection(HashSet::new));
    }

    /**
     * Po
     *
     * @param list list
     * @return the list
     * @since 2024.1.1
     */
    default List<P> po(@NotNull List<D> list) {
        return d2p(list);
    }

    /**
     * D 2 p
     *
     * @param list list
     * @return the list
     * @since 2024.2.0
     */
    default List<P> d2p(@NotNull List<D> list) {
        return list.stream().map(this::d2p).collect(Collectors.toList());
    }

    /**
     * Po
     *
     * @param collection collection
     * @return the collection
     * @since 2024.1.1
     */
    @Deprecated
    default Collection<P> po(@NotNull Collection<D> collection) {
        return d2p(collection);
    }

    /**
     * D 2 p
     *
     * @param collection collection
     * @return the collection
     * @since 2024.2.0
     */
    default Collection<P> d2p(@NotNull Collection<D> collection) {
        return collection.stream().map(this::d2p).collect(Collectors.toCollection(HashSet::new));
    }
}
