package dev.dong4j.zeka.kernel.auth.constant;

import dev.dong4j.zeka.kernel.common.constant.BasicConstant;
import lombok.experimental.UtilityClass;

/**
 * 认证相关常量类，定义了 OAuth2 认证、用户登录、令牌管理等过程中使用的所有常量
 * <p>
 * 该常量类作为认证系统的配置中心，集中管理所有与认证相关的字符串常量
 * 包括 URL 路径、HTTP Header 名称、OAuth2 参数、令牌格式等各种配置项
 * <p>
 * 包含的常量类型：
 * - 登录相关：登录、登出、刷新令牌的 URL 路径
 * - 验证码：验证码字段名和 UUID 关联键
 * - OAuth2 参数：授权类型、作用域、客户端信息等
 * - HTTP Header：认证 Header、自定义 Header 等
 * - 令牌相关：令牌类型、前缀、长度等
 * - 服务名称：微服务名称和 API 名称
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2019.12.26 21:43
 * @since 1.0.0
 */
@UtilityClass
public class AuthConstant {
    /** 用户登录接口 URL 路径 */
    public static final String LOGIN_URL = "/login";
    /** 用户登出接口 URL 路径 */
    public static final String LOGOUT_URL = "/logout";
    /** 令牌刷新接口 URL 路径 */
    public static final String REFRESH_TOKEN_URL = "/refresh/token";
    /** 验证码字段名 */
    public static final String VERIFICATION_CODE = "code";
    /** 与验证码关联的 UUID 字段名，存入 HTTP Header 中 */
    public static final String VERIFICATION_UUID = "uuid";
    /** 当前登录用户缓存键前缀 */
    public static final String CURRENT_LOGIN_USER = "currentUser:";
    /** AJAX 请求返回 JSON 格式标识 */
    public static final String AJAX_REQUEST_JSON = "json";
    /** AJAX 请求返回 HTML 格式标识 */
    public static final String AJAX_REQUEST_HTML = "html";
    /** OAuth2 返回的访问令牌字段名 */
    public static final String ACCESS_TOKEN = "access_token";
    /** OAuth2 返回的刷新令牌字段名 */
    public static final String REFRESH_TOKEN = "refresh_token";
    /** OAuth2 授权类型参数名 */
    public static final String OAUTH_GRANT_TYPE = "grant_type";
    /** OAuth2 默认作用域值 */
    public static final String OAUTH_SCOPE_DEFAULT_VALUE = "all";
    /** OAuth2 作用域参数名 */
    public static final String OAUTH_SCOPE = "scope";
    /** OAuth2 客户端 ID 参数名 */
    public static final String CLIENT_ID = "client_id";
    /** OAuth2 客户端密钥参数名 */
    public static final String CLIENT_SECRET = "client_secret";
    /** OAuth2 认证的 HTTP Header 参数名 */
    public static final String OAUTH_HEADER_TYPE = "Authorization";
    /** Bearer 令牌类型前缀 */
    public static final String BEARER = "Bearer ";
    /** Bearer 前缀的字符长度 */
    public static final Integer BEARER_BEGIN_LENGHT = BEARER.length();
    /** 服务间调用的令牌 Header 名 */
    public static final String X_CLIENT_TOKEN = "X-Client-Token";
    /** 服务间调用的刷新令牌 Header 名 */
    public static final String X_CLIENT_REFRESH_TOKEN = "X-Client-Refresh-Token";
    /** 客户端 ID Header 名 */
    public static final String X_CLIENT_ID = BasicConstant.HEADER_CLIENT_ID;
    /** 真实请求路径 Header 名 */
    public static final String X_REAL_PATH = "X-Real-Path";
    /** 是否刷新令牌的控制开关 Header 名 */
    public static final String X_REFRESH_TOKEN = "X-Refresh-Token";
    /** 应用 ID 参数名 */
    public static final String APP_ID = "appId";
    /** 系统应用 ID 参数名 */
    public static final String SYS_APP_ID = "sysAppId";
    /** 授权服务名称 */
    public static final String ORIZATION_SERVICE_NAME = "orization-service";
    /** 令牌 API 名称 */
    public static final String TOKEN_API_NAME = "orization.token";
    /** OAuth2 客户端详情 API 名称 */
    public static final String OAUTH_CLIENT = "orization.details";
    /** 当前用户资源令牌参数名 */
    public static final String APP_V4_TOKEN = "token";
}
