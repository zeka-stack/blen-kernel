package dev.dong4j.zeka.kernel.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>Description: 逻辑删除枚举</p>
 *
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.26 20:43
 * @since 1.0.0
 */
@Getter
@AllArgsConstructor
public enum DeletedEnum implements SerializeEnum<Boolean> {
    /** N delete enum */
    N(Boolean.FALSE, "未删除"),
    /** Y delete enum */
    Y(Boolean.TRUE, "已删除");

    /** 数据库存储的值 */
    private final Boolean value;
    /** 枚举描述 */
    private final String desc;
}
