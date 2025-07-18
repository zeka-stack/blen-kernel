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
import dev.dong4j.zeka.kernel.common.util.JsonUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.26 20:16
 * @since 1.0.0
 */
@Slf4j
@UtilityClass
@SuppressWarnings(value = {"PMD:MethodLimit", "checkstyle:MethodLimit"})
public class JwtUtils {

    /** MAPPER */
    public static final ObjectMapper MAPPER;
    /** ERROR_MESSAGE */
    private static final String ERROR_MESSAGE = "解析 token 失败: {}, token = [{}]";

    static {
        MAPPER = JsonUtils.getCopyMapper();
        SimpleModule simpleModule = new SimpleModule("EntityEnum-Converter", PackageVersion.VERSION);
        // 设置枚举序列化/反序列化处理器
        simpleModule.addDeserializer(SerializeEnum.class, new EntityEnumDeserializer<>());
        simpleModule.addSerializer(SerializeEnum.class, new EntityEnumSerializer<>());
        MAPPER.registerModule(simpleModule);
        MAPPER.findAndRegisterModules();
    }

    /**
     * 从认证信息中提取 jwt token 对象
     *
     * @param authentication 认证信息  Authorization: bearer header.payload.signature
     * @return Jwt 对象
     * @since 1.0.0
     */
    @Contract("null -> fail")
    public static @NotNull Jwt getJwt(String authentication) {
        if (authentication == null || authentication.isEmpty()) {
            throw new IllegalArgumentException("提取认证信息失败, 认证信息不存在");
        }
        if (!authentication.startsWith(AuthConstant.BEARER)) {
            return JwtHelper.decode(Objects.requireNonNull(authentication));
        }
        String token = getToken(authentication);
        return JwtHelper.decode(Objects.requireNonNull(token));
    }

    /**
     * Gets token *
     *
     * @param request request
     * @return the token
     * @since 1.6.0
     */
    public static String getToken(@NotNull HttpServletRequest request) {
        return getToken(request.getHeader(AuthConstant.OAUTH_HEADER_TYPE));
    }

    /**
     * 从 header 中获取 token 串, authentication = Authorization: bearer header.payload.signature
     *
     * @param authentication token
     * @return String token
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
     * Get claims. (使用 key 解密, 解密失败或者 token 超时都会抛出以下异常)
     * ExpiredJwtException UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException
     *
     * @param key   the key
     * @param token the token
     * @return the claims
     * @since 1.0.0
     */
    @Nullable
    public static Claims getClaims(@NotNull String key, String token) throws ExpiredJwtException,
        UnsupportedJwtException,
        MalformedJwtException,
        SignatureException,
        IllegalArgumentException {
        Jws<Claims> jws = Jwts.parser()
            .setSigningKey(key.getBytes(StandardCharsets.UTF_8))
            .parseClaimsJws(token);
        return jws.getBody();
    }


    /**
     * <p>Description: token 内部数据 </p>
     *
     * @author dong4j
     * @version 1.0.0
     * @email "mailto:dong4j@gmail.com"
     * @date 2020.09.12 17:54
     * @since 1.6.0
     */
    @UtilityClass
    public static class PlayGround {
        /**
         * Gets username.
         *
         * @param key   the key
         * @param token the token
         * @return the username
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
         * Gets username.
         *
         * @param token the token
         * @return the username
         * @since 1.0.0
         */
        @Nullable
        public static String getUsername(String token) {
            try {
                return MAPPER.readTree(getJwt(token).getClaims()).get(ZekaClaims.USER_NAME).asText();
            } catch (Exception e) {
                log.error(ERROR_MESSAGE, e.getMessage(), token);
            }
            return null;
        }

        /**
         * Gets sysAppId.
         *
         * @param token the token
         * @return the sysAppId
         * @since 1.0.0
         */
        @Nullable
        public static String getSysAppId(String token) {
            try {
                return MAPPER.readTree(getJwt(token).getClaims()).get(ZekaClaims.SYSTEM_APP_ID).asText();
            } catch (Exception e) {
                log.error(ERROR_MESSAGE, e.getMessage(), token);
            }
            return null;
        }

        /**
         * Gets user.
         *
         * @param key   the key
         * @param token the token
         * @return the user
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
         * Gets user.
         *
         * @param token the token
         * @return the user
         * @since 1.0.0
         */
        @Nullable
        @SuppressWarnings("checkstyle:Indentation")
        public static AuthorizationUser getUser(String token) {
            if (!StringUtils.hasText(token)) {
                return null;
            }
            try {
                return MAPPER.readValue(MAPPER.readTree(getJwt(token).getClaims()).get(ZekaClaims.USER).asText(),
                    AuthorizationUser.class);
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
                return MAPPER.convertValue(MAPPER.readTree(getJwt(token).getClaims()).get(ZekaClaims.ROLES),
                    new TypeReference<List<AuthorizationRole>>() {
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
                return MAPPER.readTree(getJwt(token).getClaims()).get(ZekaClaims.CLIENT_ID).asText();
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
         * @since 1.5.0
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
                return MAPPER.readTree(getJwt(token).getClaims()).get(ZekaClaims.TENANT_APP_ID).asLong();
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
                return MAPPER.convertValue(MAPPER.readTree(getJwt(token).getClaims()).get(ZekaClaims.AUTHORITIES),
                    new TypeReference<List<String>>() {
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
     * @since 1.5.0
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
            return MAPPER.readTree(getJwt(token).getClaims()).get(ZekaClaims.CLIENT_TYPE).asText();
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
            return MAPPER.convertValue(MAPPER.readTree(getJwt(token).getClaims()).get(ZekaClaims.SCOPE),
                new TypeReference<List<String>>() {
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
            return MAPPER.convertValue(MAPPER.readTree(getJwt(token).getClaims()).get(ZekaClaims.EXP).asLong() * 1000L,
                new TypeReference<Date>() {
                });
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
            return MAPPER.readTree(getJwt(token).getClaims()).get(ZekaClaims.JTI).asText();
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
            .setClaims(claims)
            .setExpiration(expiration)
            .signWith(SignatureAlgorithm.HS256, signKey)
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
            .setClaims(claims)
            .signWith(SignatureAlgorithm.HS256, signKey)
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
