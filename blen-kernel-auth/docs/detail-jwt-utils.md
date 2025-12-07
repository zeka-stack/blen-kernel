---
published: 2025.01.01
---

# JWT 工具类

## 概述

`blen-kernel-auth` 模块提供了 `JwtUtils` 工具类，用于 JWT 令牌的生成、解析、验证等全套功能。该工具类基于 JJWT 0.12.6 版本，支持现代化的 JWT
标准和安全策略。

## 核心功能

### 令牌生成

#### generateToken

生成 JWT 令牌，支持自定义声明和过期时间：

```java
public static String generateToken(Map<String, Object> claims,
                                  String signKey,
                                  Date expiration) {
    return Jwts.builder()
        .claims(claims)
        .expiration(expiration)
        .signWith(Keys.hmacShaKeyFor(signKey.getBytes(StandardCharsets.UTF_8)))
        .compact();
}
```

#### 使用示例

```java
Map<String, Object> claims = new HashMap<>();
claims.put(ZekaClaims.USER, user);
claims.put(ZekaClaims.ROLES, roles);
claims.put(ZekaClaims.USER_NAME, user.getUsername());
claims.put(ZekaClaims.CLIENT_ID, "web-client");

Date expiration = new Date(System.currentTimeMillis() + 3600000); // 1小时
String token = JwtUtils.generateToken(claims, jwtSecret, expiration);
```

### 令牌解析

#### getClaims

使用密钥解析和验证 JWT 令牌，会验证签名和过期时间：

```java
public static Claims getClaims(String key, String token)
    throws ExpiredJwtException, UnsupportedJwtException,
           MalformedJwtException, SignatureException {
    Jws<Claims> jws = Jwts.parser()
        .verifyWith(Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8)))
        .build()
        .parseSignedClaims(token);
    return jws.getPayload();
}
```

#### getUnsignedClaims

无签名解析 JWT 令牌，适用于客户端场景：

```java
public static Claims getUnsignedClaims(String token) {
    String[] chunks = token.split("\\.");
    if (chunks.length != 3) {
        throw new IllegalArgumentException("Invalid JWT token format");
    }

    // 解码 payload
    String payload = chunks[1];
    byte[] decodedPayload = Base64.getUrlDecoder().decode(payload);
    String claimsJson = new String(decodedPayload, StandardCharsets.UTF_8);

    // 解析为 Claims
    Map<String, Object> claimsMap = MAPPER.readValue(claimsJson, Map.class);
    Claims build = Jwts.claims().build();
    build.putAll(claimsMap);
    return build;
}
```

### 令牌提取

#### getToken

从 HTTP 请求中提取 JWT 令牌：

```java
public static String getToken(HttpServletRequest request) {
    return getToken(request.getHeader(AuthConstant.OAUTH_HEADER_TYPE));
}
```

#### getJwtToken

从认证信息中提取 JWT 令牌（去除 Bearer 前缀）：

```java
public static String getJwtToken(String authentication) {
    if (authentication == null || authentication.isEmpty()) {
        throw new IllegalArgumentException("提取认证信息失败, 认证信息不存在");
    }
    if (!authentication.startsWith(AuthConstant.BEARER)) {
        return authentication;
    }
    return getToken(authentication);
}
```

### 令牌验证

#### isExpiration

检查令牌是否已过期：

```java
public static boolean isExpiration(String token) {
    try {
        Claims claims = getUnsignedClaims(token);
        Date expiration = claims.getExpiration();
        return expiration != null && expiration.before(new Date());
    } catch (Exception e) {
        return true;
    }
}
```

#### 使用示例

```java
public boolean isTokenValid(String token) {
    try {
        JwtUtils.getClaims(jwtSecret, token);
        return !JwtUtils.isExpiration(token);
    } catch (Exception e) {
        return false;
    }
}
```

### 用户信息提取

#### PlayGround 工具类

提供了便捷的用户信息提取方法：

```java
public static class PlayGround {
    public static AuthorizationUser getUser(String key, String token) {
        Claims claims = getClaims(key, token);
        return MAPPER.convertValue(claims.get(ZekaClaims.USER), AuthorizationUser.class);
    }

    public static List<AuthorizationRole> getRoles(String key, String token) {
        Claims claims = getClaims(key, token);
        return MAPPER.convertValue(claims.get(ZekaClaims.ROLES),
            new TypeReference<List<AuthorizationRole>>() {});
    }

    public static String getUsername(String key, String token) {
        Claims claims = getClaims(key, token);
        return claims.get(ZekaClaims.USER_NAME, String.class);
    }
}
```

## 设计特性

### 1. 签名算法

使用 HMAC-SHA 签名算法，支持多种密钥格式：

- HMAC-SHA256
- HMAC-SHA384
- HMAC-SHA512

### 2. 声明字段

支持标准的 JWT 声明字段和自定义字段：

- `user`: 用户信息
- `user_name`: 用户名
- `roles`: 角色列表
- `authorities`: 权限列表
- `client_id`: 客户端 ID
- `client_type`: 客户端类型
- `app_id`: 应用 ID
- `scope`: 作用域
- `exp`: 过期时间
- `jti`: 令牌 ID

### 3. 序列化支持

使用 Jackson 进行 JSON 序列化和反序列化，支持枚举类型的序列化：

```java
static {
    MAPPER = Jsons.getCopyMapper();
    SimpleModule simpleModule = new SimpleModule("EntityEnum-Converter", PackageVersion.VERSION);
    simpleModule.addDeserializer(SerializeEnum.class, new EntityEnumDeserializer<>());
    simpleModule.addSerializer(SerializeEnum.class, new EntityEnumSerializer<>());
    MAPPER.registerModule(simpleModule);
    MAPPER.findAndRegisterModules();
}
```

## 使用示例

### 生成令牌

```java
@Service
public class AuthService {

    @Value("${jwt.secret}")
    private String jwtSecret;

    public String generateToken(User user, List<Role> roles) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(ZekaClaims.USER, user);
        claims.put(ZekaClaims.ROLES, roles);
        claims.put(ZekaClaims.USER_NAME, user.getUsername());
        claims.put(ZekaClaims.CLIENT_ID, "web-client");

        Date expiration = new Date(System.currentTimeMillis() + 3600000);
        return JwtUtils.generateToken(claims, jwtSecret, expiration);
    }
}
```

### 解析令牌

```java
@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String jwtSecret;

    public AuthorizationUser getUserFromToken(String token) {
        return JwtUtils.PlayGround.getUser(jwtSecret, token);
    }

    public List<AuthorizationRole> getRolesFromToken(String token) {
        return JwtUtils.PlayGround.getRoles(jwtSecret, token);
    }

    public boolean isTokenValid(String token) {
        try {
            JwtUtils.getClaims(jwtSecret, token);
            return !JwtUtils.isExpiration(token);
        } catch (Exception e) {
            return false;
        }
    }
}
```

### 从请求中提取令牌

```java
@RestController
public class UserController {

    @GetMapping("/profile")
    public Result<User> getProfile(HttpServletRequest request) {
        String token = JwtUtils.getToken(request);
        if (token == null) {
            throw new AuthenticationException("未授权");
        }

        AuthorizationUser user = JwtUtils.PlayGround.getUser(jwtSecret, token);
        return R.succeed(user);
    }
}
```

## 最佳实践

### 1. 密钥管理

- 使用强密钥，长度至少 256 位
- 密钥应该存储在安全的地方，不要硬编码
- 定期轮换密钥，提高安全性

### 2. 过期时间

- 设置合理的过期时间，平衡安全性和用户体验
- 使用刷新令牌机制，延长用户会话
- 实现令牌刷新逻辑，自动续期

### 3. 异常处理

- 捕获并处理各种 JWT 异常
- 提供友好的错误信息
- 记录详细的异常日志

### 4. 性能优化

- 缓存解析后的 Claims，避免重复解析
- 使用无签名解析进行快速验证
- 合理使用令牌缓存机制

## 注意事项

1. **安全性**: 密钥必须保密，不要泄露
2. **过期时间**: 设置合理的过期时间，避免令牌长期有效
3. **异常处理**: 正确处理各种 JWT 异常，避免信息泄露
4. **性能影响**: 令牌解析会有一定的性能开销，需要合理使用

