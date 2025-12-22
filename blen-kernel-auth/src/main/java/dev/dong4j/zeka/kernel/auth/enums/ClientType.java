package dev.dong4j.zeka.kernel.auth.enums;

import dev.dong4j.zeka.kernel.common.annotation.SerializeValue;
import dev.dong4j.zeka.kernel.common.enums.SerializeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 客户端类型枚举，定义了系统支持的不同客户端应用类型
 * <p>
 * 该枚举用于区分不同类型的客户端应用，支持多租户架构下的应用分类管理
 * 为不同类型的应用提供不同的认证策略和权限控制
 * <p>
 * 支持的客户端类型：
 * - SAAS：Zeka Stac
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.05.24 15:20
 * @since 1.0.0
 */
@Getter
@AllArgsConstructor
public enum ClientType implements SerializeEnum<Integer> {
    /** SaaS */
    SAAS(1, "Zeka Stack");

    /** 枚举值，用于序列化 */
    @SerializeValue
    private final Integer value;
    /** 客户端类型描述 */
    private final String desc;

}
