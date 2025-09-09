package dev.dong4j.zeka.kernel.auth.enums;

import dev.dong4j.zeka.kernel.common.annotation.SerializeValue;
import dev.dong4j.zeka.kernel.common.enums.SerializeEnum;
import dev.dong4j.zeka.kernel.common.util.StringPool;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 全平台用户类型枚举，定义了系统中所有支持的用户类型分类
 * <p>
 * 该枚举类用于区分不同类型的用户，支持多业务场景下的用户角色管理
 * 包括传统物流行业、新兴业务领域等多种用户类型，为不同类型用户提供差异化的服务
 * <p>
 * 支持的用户类型：
 * - 系统类：N_A、SYSTEM、TENANT
 * - 物流行业：DRIVER、FLEET、SHIPPER、FREIGHTAGE、COMPANY_VEHICLE_OWNER
 * - 新兴业务：OIL、EARTHWORK、FLOW
 * - 子账户：SHIPPER_CHILD
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.05.24 15:20
 * @since 1.0.0
 */
@Getter
@AllArgsConstructor
public enum UserType implements SerializeEnum<Integer> {
    /** 未定义用户类型 */
    N_A(0, StringPool.NULL_STRING),
    /** 租户类型用户 */
    TENANT(10, "租户"),
    /** 平台用户 - 驾驶员 */
    DRIVER(1, "平台用户-驾驶员"),
    /** 平台用户 - 车队物流公司 */
    FLEET(7, "平台用户-车队物流公司"),
    /** 平台用户 - 货主 */
    SHIPPER(3, "平台用户-货主"),
    /** 平台用户 - 信息部 */
    FREIGHTAGE(2, "平台用户-信息部"),
    /** 系统管理员类型用户 */
    SYSTEM(6, "系统管理员"),
    /** 平台用户 - 企业车主 */
    COMPANY_VEHICLE_OWNER(9, "平台用户-企业车主"),
    /** 平台用户 - 货主子账户 */
    SHIPPER_CHILD(31, "平台用户-货主子账户"),
    /** 平台用户 - 油气账户 */
    OIL(101, "平台用户-油气账户"),
    /** 平台用户 - 土石方账户 */
    EARTHWORK(102, "平台用户-土石方账户"),
    /** 平台用户 - 流向管控账户 */
    FLOW(103, "平台用户-流向管控账户");

    /** 枚举值，用于序列化 */
    @SerializeValue
    private final Integer value;
    /** 用户类型描述 */
    private final String desc;

}
