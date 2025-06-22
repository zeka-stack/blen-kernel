package dev.dong4j.zeka.kernel.common.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * <p>Description: controller 出参, 使用 swagger annotation 相关注解对字段进行标识, 只能在视图层 </p>
 *
 * @param <T> parameter
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dong4j@gmail.com"
 * @date 2019.12.18 16:11
 * @since 1.0.0
 */
@Data
@SuperBuilder
@NoArgsConstructor
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public abstract class BaseVO<T extends Serializable> extends AbstractBaseEntity<T> {
    /** serialVersionUID */
    private static final long serialVersionUID = -3550589993340031894L;
}
