package dev.dong4j.zeka.kernel.auth.enums;

import dev.dong4j.zeka.kernel.common.annotation.SerializeValue;
import dev.dong4j.zeka.kernel.common.enums.SerializeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>Description: 应用类型 </p>
 *
 * @author dong4j
 * @version 1.4.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.05.24 15:20
 * @since 1.4.0
 */
@Getter
@AllArgsConstructor
public enum ClientType implements SerializeEnum<Integer> {
    /** Tenant own user type */
    SAAS(1, "协同平台"),
    /** Platform driver user type */
    SYSTEM(2, "公司管理后台"),
    /** Platform fleet user type */
    TENANT_BUSINESS(3, "多租户子应用-业务系统"),
    /** Platform shipper user type */
    TENANT_ADMIN(4, "多租户子应用-管理系统");

    /** Value */
    @SerializeValue
    private final Integer value;
    /** Desc */
    private final String desc;

}
