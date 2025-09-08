package dev.dong4j.zeka.kernel.auth.util;

import dev.dong4j.zeka.kernel.auth.constant.AuthConstant;
import dev.dong4j.zeka.kernel.common.util.StringPool;
import dev.dong4j.zeka.kernel.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

/**
 * 认证工具类，提供与HTTP请求相关的认证信息提取功能
 * 支持多种客户端类型（v8、app、h5、v4）的token获取方式
 * 提供统一的用户名、客户端ID等信息提取方法，简化上层应用的认证处理
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
     * 从请求中获取token,兼容v8,app,h5
     *
     * @param request request
     * @return token token
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
     * Get username
     *
     * @param request request
     * @return the string
     * @since 1.0.0
     */
    public String getUsername(@NotNull HttpServletRequest request) {
        String token = getToken(request);
        return JwtUtils.PlayGround.getUsername(token);
    }

    /**
     * Gets client id *
     *
     * @param request request
     * @return the client id
     * @since 1.0.0
     */
    public String getClientId(@NotNull HttpServletRequest request) {
        String token = getToken(request);
        return JwtUtils.PlayGround.getClientId(token);
    }
}
