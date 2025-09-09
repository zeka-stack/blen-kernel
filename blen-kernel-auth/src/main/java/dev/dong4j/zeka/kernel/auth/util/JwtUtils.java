package dev.dong4j.zeka.kernel.auth.util;

import com.fasterxml.jackson.core.json.PackageVersion;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import dev.dong4j.zeka.kernel.auth.constant.AuthConstant;
import dev.dong4j.zeka.kernel.auth.entity.AuthorizationRole;
import dev.dong4j.zeka.kernel.auth.entity.AuthorizationUser;
import dev.dong4j.zeka.kernel.auth.entity.ZekaClaims;
import dev.dong4j.zeka.kernel.common.enums.SerializeEnum;
import dev.dong4j.zeka.kernel.common.enums.serialize.EntityEnumDeserializer;
import dev.dong4j.zeka.kernel.common.enums.serialize.EntityEnumSerializer;
import dev.dong4j.zeka.kernel.common.util.Jsons;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.util.StringUtils;

/**
 * JWT 令牌工具类，提供 JWT 令牌的解析、生成、验证等全套功能
 * <p>
 * 该工具类作为认证系统的核心组件，支持现代化的 JWT 标准和安全策略
 * 包含签名验证和非签名验证两种模式，支持用户信息提取、权限解析、过期时间检查等操作
 * <p>
 * 主要功能：
 * - JWT 令牌的生成和解析
 * - 签名验证和安全检查
 * - 用户信息和权限数据提取
 * - 无签名解析（客户端场景）
 * - 令牌生命周期管理
 * <p>
 * 支持的操作模式：
 * - 签名模式：需要密钥验证，适用于服务端验证
 * - 无签名模式：不需要密钥，适用于客户端解析
 * <p>
 * 基于 JJWT 0.12.6 版本，支持最新的 JWT 标准和安全特性
 * 使用 HMAC-SHA 签名算法，支持多种签名密钥格式
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.26 20:16
 * @since 1.0.0
 */
@Slf4j
@UtilityClass
@SuppressWarnings(value = {"PMD:MethodLimit", "checkstyle:MethodLimit"})
public class JwtUtils {

    /** Jackson 对象映射器，用于 JSON 序列化和反序列化 */
    public static final ObjectMapper MAPPER;
    /** 错误信息模板 */
    private static final String ERROR_MESSAGE = "解析 token 失败: {}, token = [{}]";

    static {
        MAPPER = Jsons.getCopyMapper();
        SimpleModule simpleModule = new SimpleModule("EntityEnum-Converter", PackageVersion.VERSION);
        // 设置枚举序列化/反序列化处理器
        simpleModule.addDeserializer(SerializeEnum.class, new EntityEnumDeserializer<>());
        simpleModule.addSerializer(SerializeEnum.class, new EntityEnumSerializer<>());
        MAPPER.registerModule(simpleModule);
        MAPPER.findAndRegisterModules();
    }

    /**
     * 从认证信息中提取 JWT 令牌字符串（无需验证签名）
     *
     * @param authentication 认证信息  Authorization: bearer header.payload.signature
     * @return 纯净的 JWT 令牌字符串，已去除 Bearer 前缀
     * @throws IllegalArgumentException 当认证信息不存在或格式错误时抛出
     * @since 1.0.0
     */
    @Contract("null -> fail")
    public static @NotNull String getJwtToken(String authentication) {
        if (authentication == null || authentication.isEmpty()) {
            throw new IllegalArgumentException("提取认证信息失败, 认证信息不存在");
        }
        if (!authentication.startsWith(AuthConstant.BEARER)) {
            return Objects.requireNonNull(authentication);
        }
        String token = getToken(authentication);
        return Objects.requireNonNull(token);
    }

    /**
     * 解析 JWT 令牌获取声明信息（无需验证签名）
     * <p>
     * 该方法直接解析 JWT 令牌的 payload 部分，不进行签名验证
     * 适用于客户端场景或不需要安全验证的情况
     *
     * @param token JWT 令牌字符串
     * @return Claims 对象，包含 JWT 中的所有声明信息
     * @throws IllegalArgumentException 当令牌格式错误时抛出
     * @since 1.0.0
     */
    @SneakyThrows
    public static Claims getUnsignedClaims(String token) {
        // 分割 JWT token
        String[] chunks = token.split("\\.");
        if (chunks.length != 3) {
            throw new IllegalArgumentException("Invalid JWT token format");
        }

        // 解码 payload (claims)
        String payload = chunks[1];
        byte[] decodedPayload = java.util.Base64.getUrlDecoder().decode(payload);
        String claimsJson = new String(decodedPayload, StandardCharsets.UTF_8);

        // 使用 Jackson 解析为 Map，然后创建 Claims
        @SuppressWarnings("unchecked")
        Map<String, Object> claimsMap = MAPPER.readValue(claimsJson, Map.class);
        final Claims build = Jwts.claims().build();
        build.putAll(claimsMap);
        return build;
    }

    /**
     * 从 HTTP 请求中获取 JWT 令牌
     *
     * @param request HTTP 请求对象
     * @return JWT 令牌字符串，如果不存在则返回 null
     * @since 1.0.0
     */
    public static String getToken(@NotNull HttpServletRequest request) {
        return getToken(request.getHeader(AuthConstant.OAUTH_HEADER_TYPE));
    }

    /**
     * 从 Authorization Header 中提取 JWT 令牌
     * <p>
     * 支持 Bearer 格式的 Authorization Header，自动去除 Bearer 前缀
     *
     * @param authentication 认证信息字符串，格式为 "Bearer token"
     * @return JWT 令牌字符串，如果格式不正确则返回 null
     * @since 1.0.0
     */
    @Contract("null -> null")
    public static String getToken(String authentication) {
        if ((authentication != null) && (authentication.length() > AuthConstant.BEARER_BEGIN_LENGHT)) {
            String headStr = authentication.substring(0, AuthConstant.BEARER_BEGIN_LENGHT);
            if (headStr.compareToIgnoreCase(AuthConstant.BEARER) == 0) {
                authentication = authentication.substring(AuthConstant.BEARER_BEGIN_LENGHT);
                return authentication;
            }
        }
        return null;
    }

    /**
     * 使用密钥解析和验证 JWT 令牌获取声明信息
     * <p>
     * 该方法会验证 JWT 令牌的签名和过期时间，确保令牌的安全性和有效性
     * 解密失败或令牌过期都会抛出相应的异常
     *
     * @param key   签名密钥，用于验证令牌的真实性
     * @param token JWT 令牌字符串
     * @return Claims 对象，包含验证后的声明信息
     * @throws ExpiredJwtException      当令牌已过期时抛出
     * @throws UnsupportedJwtException  当令牌不支持时抛出
     * @throws MalformedJwtException    当令牌格式错误时抛出
     * @throws SignatureException       当签名验证失败时抛出
     * @throws IllegalArgumentException 当参数非法时抛出
     * @since 1.0.0
     */
    @Nullable
    public static Claims getClaims(@NotNull String key, String token) throws ExpiredJwtException,
        UnsupportedJwtException,
        MalformedJwtException,
        SignatureException,
        IllegalArgumentException {

        Jws<Claims> jws = Jwts.parser()
            .verifyWith(Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8)))
            .build()
            .parseSignedClaims(token);
        return jws.getPayload();
    }


    /**
     * JWT 令牌数据提取工具类，提供了从 JWT 中获取各种用户信息的便捷方法
     * <p>
     * 该内部类将 JWT 解析后的复杂操作封装为简单的方法调用
     * 同时支持签名验证和无签名解析两种模式，满足不同的使用场景
     * <p>
     * 提供的主要功能：
     * - 用户基本信息获取（用户名、用户对象等）
     * - 权限相关信息获取（角色、权限列表等）
     * - 客户端和应用信息获取
     * - 令牌元数据获取（过期时间、令牌ID等）
     *
     * @author dong4j
     * @version 1.0.0
     * @email "mailto:dong4j@gmail.com"
     * @date 2020.09.12 17:54
     * @since 1.0.0
     */
    @SuppressWarnings({"LoggingSimilarMessage", "DuplicatedCode"})
    @UtilityClass
    public static class PlayGround {
        /**
         * 从 JWT 令牌中获取用户名（需要签名验证）
         *
         * @param key   签名密钥
         * @param token JWT 令牌
         * @return 用户名，如果解析失败则返回 null
         * @since 1.0.0
         */
        @Nullable
        public static String getUsername(String key, String token) {
            Claims claims = getClaims(key, token);
            if (claims == null) {
                return null;
            }
            return claims.get(ZekaClaims.USER_NAME).toString();
        }

        /**
         * 从 JWT 令牌中获取用户名（无需签名验证）
         *
         * @param token JWT 令牌
         * @return 用户名，如果解析失败则返回 null
         * @since 1.0.0
         */
        @Nullable
        public static String getUsername(String token) {
            try {
                Claims claims = getUnsignedClaims(token);
                return (String) claims.get(ZekaClaims.USER_NAME);
            } catch (Exception e) {
                log.error(ERROR_MESSAGE, e.getMessage(), token);
            }
            return null;
        }

        /**
         * 从 JWT 令牌中获取系统应用ID（无需签名验证）
         *
         * @param token JWT 令牌
         * @return 系统应用ID，如果解析失败则返回 null
         * @since 1.0.0
         */
        @Nullable
        public static String getSysAppId(String token) {
            try {
                Claims claims = getUnsignedClaims(token);
                return (String) claims.get(ZekaClaims.SYSTEM_APP_ID);
            } catch (Exception e) {
                log.error(ERROR_MESSAGE, e.getMessage(), token);
            }
            return null;
        }

        /**
         * 从 JWT 令牌中获取用户信息对象（需要签名验证）
         *
         * @param key   签名密钥
         * @param token JWT 令牌
         * @return 用户信息对象，如果解析失败则返回 null
         * @since 1.0.0
         */
        @SneakyThrows
        @Nullable
        public static AuthorizationUser getUser(String key, String token) {
            Claims claims = getClaims(key, token);
            if (claims == null) {
                return null;
            }
            return MAPPER.readValue(claims.get(ZekaClaims.USER).toString(), AuthorizationUser.class);
        }

        /**
         * 从 JWT 令牌中获取用户信息对象（无需签名验证）
         *
         * @param token JWT 令牌
         * @return 用户信息对象，如果解析失败则返回 null
         * @since 1.0.0
         */
        @Nullable
        @SuppressWarnings("checkstyle:Indentation")
        public static AuthorizationUser getUser(String token) {
            if (!StringUtils.hasText(token)) {
                return null;
            }
            try {
                Claims claims = getUnsignedClaims(token);
                String userJson = (String) claims.get(ZekaClaims.USER);
                return MAPPER.readValue(userJson, AuthorizationUser.class);
            } catch (Exception e) {
                log.error(ERROR_MESSAGE, e.getMessage(), token);
            }
            return null;
        }

        /**
         * Gets roles.
         *
         * @param key   the key
         * @param token the token
         * @return the roles
         * @since 1.0.0
         */
        @Nullable
        public static List<AuthorizationRole> getRoles(String key, String token) {
            Claims claims = getClaims(key, token);
            if (claims == null) {
                return null;
            }
            return MAPPER.convertValue(claims.get(ZekaClaims.ROLES), new TypeReference<List<AuthorizationRole>>() {
            });
        }

        /**
         * 不解密直接获取数据
         *
         * @param token the token
         * @return the roles
         * @since 1.0.0
         */
        @Nullable
        @SuppressWarnings("checkstyle:Indentation")
        public static List<AuthorizationRole> getRoles(String token) {
            if (!StringUtils.hasText(token)) {
                return null;
            }
            try {
                Claims claims = getUnsignedClaims(token);
                Object roles = claims.get(ZekaClaims.ROLES);
                return MAPPER.convertValue(roles, new TypeReference<List<AuthorizationRole>>() {
                });
            } catch (Exception e) {
                log.error(ERROR_MESSAGE, e.getMessage(), token);
            }
            return null;
        }

        /**
         * Gets client id.
         *
         * @param key   the key
         * @param token the token
         * @return the client id
         * @since 1.0.0
         */
        @Nullable
        public static String getClientId(String key, String token) {
            Claims claims = getClaims(key, token);
            if (claims == null) {
                return null;
            }
            return claims.get(ZekaClaims.CLIENT_ID).toString();
        }

        /**
         * 不解密直接获取 playground 数据
         *
         * @param token the token
         * @return the string
         * @since 1.0.0
         */
        @Nullable
        public static String getClientId(String token) {
            if (!StringUtils.hasText(token)) {
                return null;
            }
            try {
                Claims claims = getUnsignedClaims(token);
                return (String) claims.get(ZekaClaims.CLIENT_ID);
            } catch (Exception e) {
                log.error(ERROR_MESSAGE, e.getMessage(), token);
            }
            return null;
        }

        /**
         * Gets application id *
         *
         * @param key   key
         * @param token token
         * @return the application id
         * @since 1.0.0
         */
        @Nullable
        public static Long getAppId(String key, String token) {
            Claims claims = getClaims(key, token);
            if (claims == null) {
                return null;
            }
            return Long.parseLong(String.valueOf(claims.get(ZekaClaims.TENANT_APP_ID)));
        }

        /**
         * 不解密直接获取 playground 数据
         *
         * @param token the token
         * @return the application id
         * @since 1.0.0
         */
        @Nullable
        public static Long getAppId(String token) {
            if (!StringUtils.hasText(token)) {
                return null;
            }
            try {
                Claims claims = getUnsignedClaims(token);
                return ((Number) claims.get(ZekaClaims.TENANT_APP_ID)).longValue();
            } catch (Exception e) {
                log.error(ERROR_MESSAGE, e.getMessage(), token);
            }
            return null;
        }

        /**
         * 角色列表
         *
         * @param key   the key
         * @param token the token
         * @return the authorities
         * @since 1.0.0
         */
        @Nullable
        public static List<String> getAuthorities(String key, String token) {
            Claims claims = getClaims(key, token);
            if (claims == null) {
                return null;
            }
            return MAPPER.convertValue(claims.get(ZekaClaims.AUTHORITIES), new TypeReference<List<String>>() {
            });
        }

        /**
         * Gets authorities.
         *
         * @param token the token
         * @return the authorities
         * @since 1.0.0
         */
        @Nullable
        public static List<String> getAuthorities(String token) {
            if (!StringUtils.hasText(token)) {
                return null;
            }
            try {
                Claims claims = getUnsignedClaims(token);
                Object authorities = claims.get(ZekaClaims.AUTHORITIES);
                return MAPPER.convertValue(authorities, new TypeReference<List<String>>() {
                });
            } catch (Exception e) {
                log.error(ERROR_MESSAGE, e.getMessage(), token);
            }
            return null;
        }

        /**
         * Convert claims.
         *
         * @param key   the key
         * @param token the token
         * @return the  claims
         * @since 1.0.0
         */
        public static ZekaClaims convert(String key, String token) {
            return ZekaClaims.builder().user(getUser(key, token))
                .roles(getRoles(key, token))
                .authorities(getAuthorities(key, token))
                .clientId(getClientId(key, token))
                .exp(getExpiration(key, token))
                .scope(getScope(key, token))
                .jti(getJti(key, token))
                .build();
        }

    }

    /**
     * Gets client type *
     *
     * @param key   key
     * @param token token
     * @return the application id
     * @since 1.0.0
     */
    @Nullable
    public static String getClientType(String key, String token) {
        Claims claims = getClaims(key, token);
        if (claims == null) {
            return null;
        }
        return String.valueOf(claims.get(ZekaClaims.CLIENT_TYPE));
    }

    /**
     * 不解密直接获取 playground 数据
     *
     * @param token the token
     * @return the application id
     * @since 1.0.0
     */
    @Nullable
    public static String getClientType(String token) {
        if (!StringUtils.hasText(token)) {
            return null;
        }
        try {
            Claims claims = getUnsignedClaims(token);
            return (String) claims.get(ZekaClaims.CLIENT_TYPE);
        } catch (Exception e) {
            log.error(ERROR_MESSAGE, e.getMessage(), token);
        }
        return null;
    }

    /**
     * Gets scope.
     *
     * @param key   the key
     * @param token the token
     * @return the scope
     * @since 1.0.0
     */
    @Nullable
    public static List<String> getScope(String key, String token) {
        Claims claims = getClaims(key, token);
        if (claims == null) {
            return null;
        }
        return MAPPER.convertValue(claims.get(ZekaClaims.SCOPE), new TypeReference<List<String>>() {
        });
    }

    /**
     * Gets scope.
     *
     * @param token the token
     * @return the scope
     * @since 1.0.0
     */
    @Nullable
    @SuppressWarnings("checkstyle:Indentation")
    public static List<String> getScope(String token) {
        if (!StringUtils.hasText(token)) {
            return null;
        }
        try {
            Claims claims = getUnsignedClaims(token);
            Object scope = claims.get(ZekaClaims.SCOPE);
            return MAPPER.convertValue(scope, new TypeReference<List<String>>() {
            });
        } catch (Exception e) {
            log.error(ERROR_MESSAGE, e.getMessage(), token);
        }
        return null;
    }

    /**
     * 获取过期时间 (Claims 自动将秒转换为毫秒)
     *
     * @param key   the key
     * @param token the token
     * @return the expiration
     * @since 1.0.0
     */
    @Nullable
    public static Date getExpiration(String key, String token) {
        Claims claims = getClaims(key, token);
        if (claims == null) {
            return null;
        }
        return claims.getExpiration();
    }

    /**
     * token 中的时间单位为 秒, 这里需要转换为毫秒
     *
     * @param token the token
     * @return the expiration
     * @since 1.0.0
     */
    @Nullable
    @SuppressWarnings("checkstyle:Indentation")
    public static Date getExpiration(String token) {
        if (!StringUtils.hasText(token)) {
            return null;
        }
        try {
            Claims claims = getUnsignedClaims(token);
            long exp = ((Number) claims.get(ZekaClaims.EXP)).longValue();
            return new Date(exp * 1000L);
        } catch (Exception e) {
            log.error(ERROR_MESSAGE, e.getMessage(), token);
        }
        return null;
    }

    /**
     * Gets jti.
     *
     * @param key   the key
     * @param token the token
     * @return the jti
     * @since 1.0.0
     */
    @Nullable
    public static String getJti(String key, String token) {
        Claims claims = getClaims(key, token);
        if (claims == null) {
            return null;
        }
        return claims.getId();
    }

    /**
     * Gets jti.
     *
     * @param token the token
     * @return the jti
     * @since 1.0.0
     */
    @Nullable
    public static String getJti(String token) {
        if (!StringUtils.hasText(token)) {
            return null;
        }
        try {
            Claims claims = getUnsignedClaims(token);
            return (String) claims.get(ZekaClaims.JTI);
        } catch (Exception e) {
            log.error(ERROR_MESSAGE, e.getMessage(), token);
        }
        return null;
    }

    /**
     * Get value object.
     *
     * @param key      the key
     * @param token    the token
     * @param valueKey the value key
     * @return the object
     * @since 1.0.0
     */
    @Nullable
    public static Object getValue(String key, String token, String valueKey) {
        Claims claims = getClaims(key, token);
        if (claims == null) {
            return null;
        }
        return claims.get(valueKey);
    }

    /**
     * 生成 token, 只是在测试使用, 正式的 token 已由 oauth 生成
     * 设置 signKey 时要与 解密时的方式对应 {@link JwtUtils#getClaims(String, String)}
     *
     * @param claims     the claims
     * @param signKey    the sign key   密钥
     * @param expiration the expiration 过期时间
     * @return the string
     * @since 1.0.0
     */
    public static String generateToken(Map<String, Object> claims, @NotNull String signKey, Date expiration) {
        return generateToken(claims, signKey.getBytes(StandardCharsets.UTF_8), expiration);
    }

    /**
     * Generate token string.
     *
     * @param claims     the claims
     * @param signKey    the sign key
     * @param expiration the expiration
     * @return the string
     * @since 1.0.0
     */
    public static String generateToken(Map<String, Object> claims, byte[] signKey, Date expiration) {

        return Jwts.builder()
            .claims(claims)
            .expiration(expiration)
            .signWith(Keys.hmacShaKeyFor(signKey))
            .compact();
    }

    /**
     * Generate token string.
     *
     * @param claims  the claims
     * @param signKey the sign key
     * @return the string
     * @since 1.0.0
     */
    public static String generateToken(Map<String, Object> claims, @NotNull String signKey) {
        return generateToken(claims, signKey.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 生成永不过期的 token
     *
     * @param claims  the claims
     * @param signKey the sign key
     * @return the string
     * @since 1.0.0
     */
    public static String generateToken(Map<String, Object> claims, byte[] signKey) {

        return Jwts.builder()
            .claims(claims)
            .signWith(Keys.hmacShaKeyFor(signKey))
            .compact();
    }

    /**
     * token 是否过期
     *
     * @param token the token
     * @return the boolean
     * @since 1.0.0
     */
    public static boolean isExpiration(String token) {
        Date expirationDate = getExpiration(token);
        return expirationDate == null || expirationDate.before(new Date());
    }

}
