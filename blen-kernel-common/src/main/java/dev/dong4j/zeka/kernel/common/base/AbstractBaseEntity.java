package dev.dong4j.zeka.kernel.common.base;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

/**
 * <p>Description: </p>
 *
 * @param <T> parameter
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2019.12.18 16:11
 * @since 1.0.0
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ToString(callSuper = true)
public abstract class AbstractBaseEntity<T extends Serializable> implements IBaseEntity<T> {
    /** serialVersionUID */
    @Serial
    private static final long serialVersionUID = -3550589993340031894L;

    /** 实体 Id */
    @Schema(description = "实体ID 新增时可不填,修改时必填")
    private T id;

    /**
     * Pk val serializable
     *
     * @return the serializable
     * @since 1.0.0
     */
    @Override
    public T getId() {
        return this.id;
    }
}
