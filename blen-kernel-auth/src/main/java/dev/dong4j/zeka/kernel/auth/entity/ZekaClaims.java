package dev.dong4j.zeka.kernel.auth.entity;

import dev.dong4j.zeka.kernel.auth.constant.AuthConstant;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * JWT 令牌声明实体类，封装了 JWT 中包含的所有用户认证和授权信息
 * <p>
 * 该类作为 JWT 令牌的数据载体，定义了系统中传递的所有认证相关信息
 * 支持用户信息、角色权限、客户端信息、应用信息等多维度的认证数据
 * <p>
 * 包含的主要信息：
 * - 用户基本信息：用户对象、用户名
 * - 权限信息：角色列表、权限列表、作用域
 * - 客户端信息：客户端 ID、客户端类型
 * - 应用信息：应用 ID、系统应用 ID
 * - 令牌信息：过期时间、令牌 ID
 * <p>
 * 同时定义了 JWT 声明中的所有字段名称常量，保证令牌的统一性和兼容性
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.26 20:41
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ZekaClaims implements Serializable {
    /** 序列化版本号 */
    @Serial
    private static final long serialVersionUID = -5514158284074959646L;
    /** 用户信息字段名 */
    public static final String USER = "user";
    /** 用户名字段名 */
    public static final String USER_NAME = "user_name";
    /** 角色列表字段名 */
    public static final String ROLES = "roles";
    /** 作用域字段名 */
    public static final String SCOPE = AuthConstant.OAUTH_SCOPE;
    /** 过期时间字段名 */
    public static final String EXP = "exp";
    /** 权限列表字段名 */
    public static final String AUTHORITIES = "authorities";
    /** 令牌 ID 字段名 */
    public static final String JTI = "jti";
    /** 客户端 ID 字段名 */
    public static final String CLIENT_ID = "client_id";
    /** 租户应用 ID 字段名 */
    public static final String TENANT_APP_ID = "tenant_app_id";
    /** 系统应用 ID 字段名 */
    public static final String SYSTEM_APP_ID = "system_app_id";
    /** 客户端类型字段名 */
    public static final String CLIENT_TYPE = "client_type";
    /** 客户端密钥字段名 */
    public static final String CLIENT_SECRET = "client_secret";

    /** 用户认证信息 */
    private AuthorizationUser user;
    /** 用户角色列表 */
    private List<AuthorizationRole> roles;
    /** 令牌过期时间 */
    private Date exp;
    /** 防篡改参数，令牌唯一标识 */
    private String jti;
    /** 客户端唯一标识 */
    private String clientId;
    /** 应用唯一标识 */
    private Long appId;
    /** 客户端类型标识 */
    private String clientType;
    /** 令牌作用域列表 */
    private List<String> scope;
    /** 用户权限列表 */
    private List<String> authorities;
}
