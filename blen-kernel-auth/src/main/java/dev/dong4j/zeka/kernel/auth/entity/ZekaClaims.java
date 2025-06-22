package dev.dong4j.zeka.kernel.auth.entity;

import dev.dong4j.zeka.kernel.auth.constant.AuthConstant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>Description: JWT 包含的数据</p>
 *
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.26 20:41
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ZekaClaims implements Serializable {
    /** serialVersionUID */
    private static final long serialVersionUID = -5514158284074959646L;
    /** USER */
    public static final String USER = "user";
    /** USER_NAME */
    public static final String USER_NAME = "user_name";
    /** ROLES */
    public static final String ROLES = "roles";
    /** SCOPE */
    public static final String SCOPE = AuthConstant.OAUTH_SCOPE;
    /** EXP */
    public static final String EXP = "exp";
    /** AUTHORITIES */
    public static final String AUTHORITIES = "authorities";
    /** JTI */
    public static final String JTI = "jti";
    /** CLIENT_ID */
    public static final String CLIENT_ID = "client_id";
    /** TENANT_APP_ID */
    public static final String TENANT_APP_ID = "tenant_app_id";
    /** SYS_APP_ID */
    public static final String SYSTEM_APP_ID = "system_app_id";
    /** CLIENT_TYPE */
    public static final String CLIENT_TYPE = "client_type";
    /** CLIENT_SECRET */
    public static final String CLIENT_SECRET = "client_secret";

    /** User */
    private AuthorizationUser user;
    /** Roles */
    private List<AuthorizationRole> roles;
    /** 过期时间 */
    private Date exp;
    /** 防篡改参数 */
    private String jti;
    /** 客户端 id */
    private String clientId;
    /** App id */
    private Long appId;
    /** Client type */
    private String clientType;
    /** Scope */
    private List<String> scope;
    /** 角色列表 */
    private List<String> authorities;
}
