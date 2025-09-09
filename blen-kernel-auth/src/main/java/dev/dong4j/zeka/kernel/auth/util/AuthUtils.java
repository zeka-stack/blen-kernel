package dev.dong4j.zeka.kernel.auth.util;

import dev.dong4j.zeka.kernel.auth.constant.AuthConstant;
import dev.dong4j.zeka.kernel.common.util.StringPool;
import dev.dong4j.zeka.kernel.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

/**
 * 认证工具类，提供与 HTTP 请求相关的认证信息提取功能
 * <p>
 * 该工具类主要用于从 HTTP 请求中提取各种认证相关的信息
 * 支持多种客户端类型（v8、app、h5、v4）的 token 获取方式
 * 提供统一的用户名、客户端 ID 等信息提取方法，简化上层应用的认证处理
 * <p>
 * 主要功能：
 * - 多渠道 Token 获取：支持从 Header、Parameter 等多个位置获取
 * - 客户端兼容性：兼容不同版本和类型的客户端
 * - 用户信息提取：从 Token 中解析用户名和客户端信息
 * - 容错处理：对缺失或错误的 Token 进行友好处理
 * <p>
 * 支持的 Token 来源：
 * - Authorization Header（标准 OAuth2 方式）
 * - X-Client-Token Header（自定义 Header）
 * - token Parameter（URL 参数或表单参数）
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.06.30 11:49
 * @since 1.0.0
 */
@Slf4j
@UtilityClass
public class AuthUtils {

    /**
     * 从 HTTP 请求中获取 JWT 令牌，兼容 v8、app、h5 等多种客户端
     * <p>
     * 按优先级顺序尝试从以下位置获取 Token：
     * 1. Authorization Header（标准 OAuth2 方式）
     * 2. X-Client-Token Header（自定义 Header）
     * 3. token Parameter（兼容 v4 客户端）
     *
     * @param request HTTP 请求对象
     * @return JWT 令牌字符串，如果所有位置都没有找到则返回空字符串
     * @since 1.0.0
     */
    public String getToken(@NotNull HttpServletRequest request) {
        // 从 header 中获取 token
        String token = JwtUtils.getToken(request);
        // 兼容v4,如果header里面没有, 则从request的session里面获取
        if (StringUtils.isBlank(token)) {
            log.trace("header: {} 不存在, 使用 {}", AuthConstant.OAUTH_HEADER_TYPE, AuthConstant.X_CLIENT_TOKEN);
            String headerToken = request.getHeader(AuthConstant.X_CLIENT_TOKEN);
            token = StringUtils.isBlank(headerToken) ? StringPool.EMPTY : headerToken;
        }

        if (StringUtils.isBlank(token)) {
            log.trace("header: {} 不存在, 使用 {}", AuthConstant.X_CLIENT_TOKEN, AuthConstant.APP_V4_TOKEN);
            String tokenObj = request.getHeader(AuthConstant.APP_V4_TOKEN);
            token = StringUtils.isBlank(tokenObj)
                ? request.getParameter(AuthConstant.APP_V4_TOKEN)
                : tokenObj;
            token = StringUtils.isBlank(tokenObj) ? StringPool.EMPTY : token;
        }

        return token;
    }

    /**
     * 从 HTTP 请求中获取用户名
     *
     * @param request HTTP 请求对象
     * @return 用户名，如果获取失败则返回 null
     * @since 1.0.0
     */
    public String getUsername(@NotNull HttpServletRequest request) {
        String token = getToken(request);
        return JwtUtils.PlayGround.getUsername(token);
    }

    /**
     * 从 HTTP 请求中获取客户端 ID
     *
     * @param request HTTP 请求对象
     * @return 客户端 ID，如果获取失败则返回 null
     * @since 1.0.0
     */
    public String getClientId(@NotNull HttpServletRequest request) {
        String token = getToken(request);
        return JwtUtils.PlayGround.getClientId(token);
    }
}
