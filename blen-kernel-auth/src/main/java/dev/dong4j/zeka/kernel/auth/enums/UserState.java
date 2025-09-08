package dev.dong4j.zeka.kernel.auth.enums;

import dev.dong4j.zeka.kernel.common.annotation.SerializeValue;
import dev.dong4j.zeka.kernel.common.enums.DeletedEnum;
import dev.dong4j.zeka.kernel.common.enums.SerializeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>Description: 全平台用户状态, 未删除/已删除不属于用户状态, 请使用 {@link DeletedEnum}</p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.05.28 14:35
 * @since 1.0.0
 */
@Getter
@AllArgsConstructor
public enum UserState implements SerializeEnum<Integer> {

    /** 用户状态: 已停用 */
    INVALID(2, "已停用"),
    /** 用户状态: 有效 */
    VALID(1, "有效"),
    /** 用户状态:  待激活 */
    UNACTIVATED(0, "待激活"),
    /** 用户状态: 已删除 */
    DELETE(9, "已删除");

    /** Value */
    @SerializeValue
    private final Integer value;
    /** Desc */
    private final String desc;
}

