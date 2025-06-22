package dev.dong4j.zeka.kernel.common.mapstruct;

import dev.dong4j.zeka.kernel.common.enums.DeleteEnum;
import org.mapstruct.Named;

/**
 * <p>Description:  枚举与 value, desc 转换关系 </p>
 *
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.27 18:10
 * @since 1.0.0
 */
@Named("DeleteEnumConverter")
public class DeleteEnumConverter extends EntityEnumConverter<DeleteEnum, Boolean> {

    /**
     * Delete enum converter
     *
     * @since 1.9.0
     */
    public DeleteEnumConverter() {
        super(DeleteEnum.class);
    }
}
