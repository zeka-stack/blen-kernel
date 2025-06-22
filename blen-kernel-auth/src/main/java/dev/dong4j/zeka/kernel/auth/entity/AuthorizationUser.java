package dev.dong4j.zeka.kernel.auth.entity;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.Maps;
import dev.dong4j.zeka.kernel.auth.CurrentUser;
import dev.dong4j.zeka.kernel.auth.enums.UserType;
import dev.dong4j.zeka.kernel.auth.util.JwtUtils;
import dev.dong4j.zeka.kernel.common.exception.BasicException;
import dev.dong4j.zeka.kernel.common.util.StringPool;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * <p>Description: 认证用户实体</p>
 *
 * @author dong4j
 * @version 1.2.3
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
    /** serialVersionUID */
    private static final long serialVersionUID = -4911273233539916137L;
    /** USER_NAME */
    public static final String USER_NAME = "username";
    /** 无租户用户的 tenantId 默认为 0 */
    public static final Long DEFAULT_ID = 0L;
    /** DEFAULT_TENANT_ID */
    public static final Long DEFAULT_TENANT_ID = DEFAULT_ID;
    /** NOT_REQUIRED_ORIZATION */
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
            throw new BasicException("to json error", e);
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

    /** Id */
    private Long id;
    /** Username */
    private String username;
    /** Mobile */
    private String mobile;
    /** Email */
    private String email;
    /** 用户类型 */
    private UserType userType;
    /** 企业类型 1=货主企业、2=承运企业、3=车队企业 */
    private Integer enterpriseType;
    /** 租户 id */
    private Long tenantId;
    /** 当前用户从哪个租户的哪个应用登录 */
    private Long tenantAppId;
    /** 当前应用属于哪个应用 */
    private Long systemAppId;
    /** Marked */
    private String marked;
    /** 权限列表 */
    private Set<AuthorizationRole> roles;
    /** 公司 id */
    private Long companyId;
    /** 业务扩展字段 */
    private Object extend;
}
