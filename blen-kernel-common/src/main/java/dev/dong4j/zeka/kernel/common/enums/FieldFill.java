package dev.dong4j.zeka.kernel.common.enums;

/**
 * <p>Description: 字段填充策略枚举类 </p>
 *
 * @author dong4j
 * @version 1.3.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.04.08 12:41
 * @since 1.0.0
 */
public enum FieldFill {
    /** 默认不处理 */
    DEFAULT,
    /** 插入时填充字段 */
    INSERT,
    /** 更新时填充字段 */
    UPDATE,
    /** 插入和更新时填充字段 */
    INSERT_UPDATE
}
