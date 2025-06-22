package dev.dong4j.zeka.kernel.common.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * <p>Description: 服务层的数据交换实体, 只能存在于服务层 </p>
 *
 * @param <T> parameter
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dong4j@gmail.com"
 * @date 2019.12.08 21:49
 * @since 1.0.0
 */
@Data
@SuperBuilder
@NoArgsConstructor
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public abstract class BaseDTO<T extends Serializable> extends AbstractBaseEntity<T> {
    /** serialVersionUID */
    private static final long serialVersionUID = 4484918372546552703L;

}
