package dev.dong4j.zeka.kernel.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.27 18:23
 * @since 1.0.0
 */
@Getter
@AllArgsConstructor
public enum EnabledEnum implements SerializeEnum<Boolean> {
    /** On enable enum */
    ON(Boolean.TRUE, "可用状态"),
    /** Off enable enum */
    OFF(Boolean.FALSE, "不可用状态");

    /** 数据库存储的值 */
    private final Boolean value;
    /** 枚举描述 */
    private final String desc;
}
