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
 * - SAAS：协同平台，面向多租户的 SaaS 平台应用
 * - SYSTEM：公司管理后台，内部管理系统
 * - TENANT_BUSINESS：租户业务系统，面向租户的业务应用
 * - TENANT_ADMIN：租户管理系统，面向租户的管理应用
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
    /** SaaS 协同平台应用 */
    SAAS(1, "协同平台"),
    /** 公司管理后台系统 */
    SYSTEM(2, "公司管理后台"),
    /** 多租户子应用 - 业务系统 */
    TENANT_BUSINESS(3, "多租户子应用-业务系统"),
    /** 多租户子应用 - 管理系统 */
    TENANT_ADMIN(4, "多租户子应用-管理系统");

    /** 枚举值，用于序列化 */
    @SerializeValue
    private final Integer value;
    /** 客户端类型描述 */
    private final String desc;

}
