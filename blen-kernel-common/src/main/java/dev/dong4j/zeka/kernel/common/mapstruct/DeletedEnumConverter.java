package dev.dong4j.zeka.kernel.common.mapstruct;

import dev.dong4j.zeka.kernel.common.enums.DeletedEnum;
import org.mapstruct.Named;

/**
 * <p>Description:  枚举与 value, desc 转换关系 </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.27 18:10
 * @since 1.0.0
 */
@Named("DeletedEnumConverter")
public class DeletedEnumConverter extends EntityEnumConverter<DeletedEnum, Boolean> {

    /**
     * Delete enum converter
     *
     * @since 1.0.0
     */
    public DeletedEnumConverter() {
        super(DeletedEnum.class);
    }
}
