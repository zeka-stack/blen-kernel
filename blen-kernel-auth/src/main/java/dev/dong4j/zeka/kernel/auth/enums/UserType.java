package dev.dong4j.zeka.kernel.auth.enums;

import dev.dong4j.zeka.kernel.common.annotation.SerializeValue;
import dev.dong4j.zeka.kernel.common.enums.SerializeEnum;
import dev.dong4j.zeka.kernel.common.util.StringPool;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>Description: 全平台用户类型 </p>
 *
 * @author dong4j
 * @version 1.4.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.05.24 15:20
 * @since 1.4.0
 */
@Getter
@AllArgsConstructor
public enum UserType implements SerializeEnum<Integer> {
    /** N a user type */
    N_A(0, StringPool.NULL_STRING),
    /** Tenant own user type */
    TENANT(10, "租户"),
    /** Platform driver user type */
    DRIVER(1, "平台用户-驾驶员"),
    /** Platform fleet user type */
    FLEET(7, "平台用户-车队物流公司"),
    /** Platform shipper user type */
    SHIPPER(3, "平台用户-货主"),
    /** Platform freightage user type */
    FREIGHTAGE(2, "平台用户-信息部"),
    /** System admin ser type */
    SYSTEM(6, "系统管理员"),
    /** Company vehicle owner user type */
    COMPANY_VEHICLE_OWNER(9, "平台用户-企业车主"),
    /** Shipper child user type */
    SHIPPER_CHILD(31, "平台用户-货主子账户"),
    /** Oil user type */
    OIL(101, "平台用户-油气账户"),
    /** Earthwork user type */
    EARTHWORK(102, "平台用户-土石方账户"),
    /** Flow user type */
    FLOW(103, "平台用户-流向管控账户");

    /** Value */
    @SerializeValue
    private final Integer value;
    /** Desc */
    private final String desc;

}
