package dev.dong4j.zeka.kernel.common.mapstruct;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;

/**
 * <p>Description: 添加 form -- dto -- po 转换 </p>
 *
 * @param <F> form 实体
 * @param <D> dto 实体
 * @param <P> po 实体
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2024.03.25 15:11
 * @since 1.0.0
 */
public interface ExtendConverter<F, D, P> extends ServiceConverter<D, P> {

    /**
     * from 转 po
     *
     * @param form form
     * @return the p
     * @since 2024.2.0
     */
    P f2p(F form);

    /**
     * F 2 p
     *
     * @param list list
     * @return the list
     * @since 2024.2.0
     */
    default List<P> f2p(@NotNull List<F> list) {
        return list.stream().map(this::f2p).collect(Collectors.toList());
    }

    /**
     * F 2 p
     *
     * @param collection collection
     * @return the collection
     * @since 2024.2.0
     */
    default Collection<P> f2p(@NotNull Collection<F> collection) {
        return collection.stream().map(this::f2p).collect(Collectors.toCollection(HashSet::new));
    }
}
