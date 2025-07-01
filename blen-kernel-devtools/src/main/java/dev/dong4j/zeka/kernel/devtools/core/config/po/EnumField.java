package dev.dong4j.zeka.kernel.devtools.core.config.po;

import java.util.List;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>Description: 枚举字段 </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2024.04.03 10:33
 * @since x.x.x
 */
@Data
@Accessors(chain = true)
public class EnumField {
    /** Field */
    private TableField field;
    /** Name */
    private String name;
    /** Value type */
    private String valueType;
    /** Items */
    private List<String> items;
    /** Type */
    private EnumType type;

}
