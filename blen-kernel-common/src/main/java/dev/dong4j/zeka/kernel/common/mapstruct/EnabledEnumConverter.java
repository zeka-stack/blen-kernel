package dev.dong4j.zeka.kernel.common.mapstruct;

import dev.dong4j.zeka.kernel.common.enums.EnabledEnum;
import org.mapstruct.Named;

/**
 * <p>Description:  枚举与 value, desc 转换关系 </p>
 *
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.27 18:11
 * @since 1.0.0
 */
@Named("EnabledEnumConverter")
public class EnabledEnumConverter extends EntityEnumConverter<EnabledEnum, Boolean> {
    /**
     * Enable enum converter
     *
     * @since 1.9.0
     */
    public EnabledEnumConverter() {
        super(EnabledEnum.class);
    }
}
