package dev.dong4j.zeka.kernel.auth.constant;

import dev.dong4j.zeka.kernel.common.constant.BasicConstant;
import lombok.experimental.UtilityClass;

/**
 * <p>Description: 认证相关常量类，定义了OAuth2认证、用户登录、令牌管理等过程中使用的常量</p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2019.12.26 21:43
 * @since 1.0.0
 */
@UtilityClass
public class AuthConstant {
    /** 登录接口URL */
    public static final String LOGIN_URL = "/login";
    /** LOGOUT_URL */
    public static final String LOGOUT_URL = "/logout";
    /** REFRESH_TOKEN_URL */
    public static final String REFRESH_TOKEN_URL = "/refresh/token";
    /** 验证码 key */
    public static final String VERIFICATION_CODE = "code";
    /** 与验证码关联的 uuid 存入 header 中的 key */
    public static final String VERIFICATION_UUID = "uuid";
    /** CURRENT_USER_NAME_KEY */
    public static final String CURRENT_LOGIN_USER = "currentUser:";
    /** AJAX_REQUEST_JSON */
    public static final String AJAX_REQUEST_JSON = "json";
    /** AJAX_REQUEST_HTML */
    public static final String AJAX_REQUEST_HTML = "html";
    /** OAuth2 返回的 token 字段名 */
    public static final String ACCESS_TOKEN = "access_token";
    /** OAuth2 返回的 刷新 token 字段名 */
    public static final String REFRESH_TOKEN = "refresh_token";
    /** OAUTH_GRANT_TYPE */
    public static final String OAUTH_GRANT_TYPE = "grant_type";
    /** OAUTH_SCOPE_DEFAULT_VALUE */
    public static final String OAUTH_SCOPE_DEFAULT_VALUE = "all";
    /** OAUTH_SCOPE */
    public static final String OAUTH_SCOPE = "scope";
    /** CLIENT_ID */
    public static final String CLIENT_ID = "client_id";
    /** CLIENT_SECRET */
    public static final String CLIENT_SECRET = "client_secret";
    /** OAuth2 认证的 header 参数类型 */
    public static final String OAUTH_HEADER_TYPE = "Authorization";
    /** BEARER */
    public static final String BEARER = "Bearer ";
    /** Authorization认证开头是"bearer " */
    public static final Integer BEARER_BEGIN_LENGHT = BEARER.length();
    /** 服务间的 token key */
    public static final String X_CLIENT_TOKEN = "X-Client-Token";
    /** X_CLIENT_REFRESH_TOKEN */
    public static final String X_CLIENT_REFRESH_TOKEN = "X-Client-Refresh-Token";
    /** X_CLIENT_ID */
    public static final String X_CLIENT_ID = BasicConstant.HEADER_CLIENT_ID;
    /** X_REAL_PATH */
    public static final String X_REAL_PATH = "X-Real-Path";
    /** 是否刷新 token 的开关: true/false */
    public static final String X_REFRESH_TOKEN = "X-Refresh-Token";
    /** APP_ID */
    public static final String APP_ID = "appId";
    /** SYS_APP_ID */
    public static final String SYS_APP_ID = "sysAppId";
    /** ORIZATION_SERVICE_NAME */
    public static final String ORIZATION_SERVICE_NAME = "orization-service";
    /** TOKEN_API_NAME */
    public static final String TOKEN_API_NAME = "orization.token";
    /** OAUTH_CLIENT */
    public static final String OAUTH_CLIENT = "orization.details";
    /** CURRENT_USER_RESOURCE */
    public static final String APP_V4_TOKEN = "token";
}
