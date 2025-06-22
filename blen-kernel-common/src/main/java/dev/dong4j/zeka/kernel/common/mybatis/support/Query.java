package dev.dong4j.zeka.kernel.common.mybatis.support;

import dev.dong4j.zeka.kernel.common.base.BaseQuery;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import org.jetbrains.annotations.Contract;

import java.io.Serializable;

/**
 * <p>Description: 分页工具 </p>
 *
 * @param <T> id 类型
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dong4j@gmail.com"
 * @date 2019.12.24 11:58
 * @since 1.0.0
 * @deprecated 请直接使用 {@link BaseQuery}
 */
@Data
@Deprecated
@SuperBuilder
@Accessors(chain = true)
@ApiModel(description = "查询条件")
@EqualsAndHashCode(callSuper = true)
public class Query<T extends Serializable> extends BaseQuery<T> {

    /** serialVersionUID */
    private static final long serialVersionUID = -6390231593223847519L;

    /**
     * Query
     *
     * @since 1.6.0
     */
    @Contract(pure = true)
    public Query() {
    }

    /**
     * Query
     *
     * @param page  page
     * @param limit limit
     * @since 1.0.0
     */
    @Contract(pure = true)
    public Query(Integer page, Integer limit) {
        this.page = page;
        this.limit = limit;
    }
}
