package dev.dong4j.zeka.kernel.common.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * <p>Description:  </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2019.12.16 21:40
 * @since 1.0.0
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(fluent = true, chain = true)
public abstract class Super implements Serializable {
    /** serialVersionUID */
    private static final long serialVersionUID = 3552360700930105551L;
    /** Name */
    protected String name;
    /** Password */
    protected String password;
}
