package dev.dong4j.zeka.kernel.auth.entity;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.Maps;
import dev.dong4j.zeka.kernel.auth.CurrentUser;
import dev.dong4j.zeka.kernel.auth.enums.UserType;
import dev.dong4j.zeka.kernel.auth.util.JwtUtils;
import dev.dong4j.zeka.kernel.common.exception.LowestException;
import dev.dong4j.zeka.kernel.common.util.StringPool;
import java.io.Serial;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 认证用户实体类，{@link CurrentUser} 接口的默认实现
 * <p>
 * 该类作为系统中用户认证信息的核心载体，包含了用户的基本信息、权限信息和租户信息
 * 支持多租户架构，可在不同的应用和租户之间进行用户身份隔离
 * <p>
 * 主要功能：
 * - 用户基本信息管理（ID、用户名、手机号、邮箱等）
 * - 用户类型和企业类型管理
 * - 多租户环境下的租户和应用隔离
 * - 角色权限系统支持
 * - 业务扩展字段支持
 * <p>
 * 使用 Jackson 多态序列化支持，类型标识为 "authorizationUser"
 * 包含静态常量定义和默认不需要授权的 Token 生成逻辑
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2019.12.26 21:44
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeName(value = "authorizationUser")
public class AuthorizationUser implements CurrentUser, Serializable {
    /** 序列化版本号 */
    @Serial
    private static final long serialVersionUID = -4911273233539916137L;
    /** 用户名字段名称常量 */
    public static final String USER_NAME = "username";
    /** 无租户用户的 tenantId 默认为 0 */
    public static final Long DEFAULT_ID = 0L;
    /** 默认租户 ID 常量 */
    public static final Long DEFAULT_TENANT_ID = DEFAULT_ID;
    /** 不需要授权的默认 Token，用于公开接口或无需认证的场景 */
    public static final String NOT_REQUIRED_ORIZATION;

    static {
        Map<String, Object> claims = Maps.newHashMapWithExpectedSize(10);
        claims.put(ZekaClaims.USER_NAME, StringPool.NULL_STRING);
        try {
            claims.put(ZekaClaims.USER, JwtUtils.MAPPER.writeValueAsString(AuthorizationUser.builder()
                .id(-1L)
                .companyId(-1L)
                .mobile(StringPool.NULL_STRING)
                .username(StringPool.NULL_STRING)
                .userType(UserType.N_A)
                .tenantId(DEFAULT_TENANT_ID)
                .email(StringPool.NULL_STRING)
                .build()));
        } catch (JsonProcessingException e) {
            throw new LowestException("to json error", e);
        }
        List<AuthorizationRole> authorizationRoles = Collections.singletonList(AuthorizationRole
            .builder()
            .id(-1L)
            .roleName(StringPool.NULL_STRING)
            .roleKey(StringPool.NULL_STRING)
            .build());
        claims.put(ZekaClaims.ROLES, authorizationRoles);
        List<String> scopes = Collections.singletonList(StringPool.NULL_STRING);
        claims.put(ZekaClaims.SCOPE, scopes);
        claims.put(ZekaClaims.AUTHORITIES, Collections.singletonList(StringPool.NULL_STRING));
        claims.put(ZekaClaims.JTI, UUID.randomUUID().toString().replace("-", ""));
        claims.put(ZekaClaims.CLIENT_ID, StringPool.NULL_STRING);
        claims.put(ZekaClaims.TENANT_APP_ID, DEFAULT_ID);
        claims.put(ZekaClaims.SYSTEM_APP_ID, DEFAULT_ID);
        claims.put(ZekaClaims.CLIENT_TYPE, StringPool.EMPTY_JSON);
        NOT_REQUIRED_ORIZATION = JwtUtils.generateToken(claims, "NO_SECITYKEY");
    }

    /** 用户唯一标识 */
    private Long id;
    /** 用户登录名 */
    private String username;
    /** 用户手机号码 */
    private String mobile;
    /** 用户电子邮箱 */
    private String email;
    /** 用户类型枚举 */
    private UserType userType;
    /** 企业类型 */
    private Integer enterpriseType;
    /** 租户唯一标识 */
    private Long tenantId;
    /** 租户应用 ID，标识用户从哪个租户的哪个应用登录 */
    private Long tenantAppId;
    /** 系统应用 ID，标识当前应用所属的系统应用 */
    private Long systemAppId;
    /** 用户标记，通常为登录邮箱后缀 */
    private String marked;
    /** 用户所拥有的角色集合 */
    private Set<AuthorizationRole> roles;
    /** 用户所属公司唯一标识 */
    private Long companyId;
    /** 业务扩展字段，用于存储业务特定的用户附加数据 */
    private Object extend;
}
