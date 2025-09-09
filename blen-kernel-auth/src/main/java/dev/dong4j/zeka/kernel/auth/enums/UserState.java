package dev.dong4j.zeka.kernel.auth.enums;

import dev.dong4j.zeka.kernel.common.annotation.SerializeValue;
import dev.dong4j.zeka.kernel.common.enums.DeletedEnum;
import dev.dong4j.zeka.kernel.common.enums.SerializeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 全平台用户状态枚举，定义了用户在系统中的可用状态
 * <p>
 * 该枚举类用于标识用户在系统中的当前状态，影响用户的登录和使用权限
 * 注意：未删除/已删除不属于用户状态，请使用 {@link DeletedEnum}
 * <p>
 * 支持的用户状态：
 * - UNACTIVATED：待激活，用户已注册但未激活
 * - VALID：有效，用户正常可用状态
 * - INVALID：已停用，用户被管理员停用
 * - DELETE：已删除，用户已被删除（建议使用单独的删除标记）
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

    /** 用户状态：已停用 */
    INVALID(2, "已停用"),
    /** 用户状态：有效 */
    VALID(1, "有效"),
    /** 用户状态：待激活 */
    UNACTIVATED(0, "待激活"),
    /** 用户状态：已删除 */
    DELETE(9, "已删除");

    /** 枚举值，用于序列化 */
    @SerializeValue
    private final Integer value;
    /** 状态描述 */
    private final String desc;
}

