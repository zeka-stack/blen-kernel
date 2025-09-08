# Blen Kernel Auth

## 概述

`blen-kernel-auth` 是 Zeka.Stack 框架的认证授权模块，基于现代化的 JWT (JSON Web Token) 技术实现用户认证和权限管理。该模块提供了完整的 JWT
生成、解析、验证功能，以及安全上下文管理。

## 主要功能

### 1. JWT 令牌管理

- **JwtUtils**: JWT 工具类，提供令牌的生成、解析、验证
- 支持 HMAC-SHA 签名算法
- 支持令牌过期时间管理
- 支持无签名解析（用于客户端解析）

```java
// 生成 JWT 令牌
String token = JwtUtils.generateToken(claims, signKey, expiration);

// 解析 JWT 令牌
Claims claims = JwtUtils.getClaims(signKey, token);

// 无签名解析（客户端使用）
Claims claims = JwtUtils.getUnsignedClaims(token);
```

### 2. 认证实体

- **AuthorizationUser**: 授权用户实体
- **AuthorizationRole**: 授权角色实体
- **ZekaClaims**: JWT 声明封装

### 3. 安全上下文管理

- **SecurityContextHolder**: 安全上下文持有者
- **SecurityContextImpl**: 安全上下文实现
- **GlobalSecurityContextHolderStrategy**: 全局安全上下文策略

### 4. 加密工具

- **CryptoUtils**: 加密解密工具
- **DigestUtils**: 摘要工具
- **AuthUtils**: 认证工具

### 5. 常量定义

- **AuthConstant**: 认证相关常量
- 支持 Bearer Token 格式
- 预定义声明字段

## 核心特性

### 1. 现代化 JWT 支持

- 基于 `io.jsonwebtoken:jjwt` 0.12.6 版本
- 支持最新的 JWT 标准
- 提供完整的 API 支持

### 2. 灵活的令牌解析

- 支持签名验证解析
- 支持无签名解析（客户端场景）
- 自动处理令牌格式和错误

### 3. 丰富的声明支持

- 用户信息 (user)
- 角色列表 (roles)
- 权限列表 (authorities)
- 客户端信息 (clientId, clientType)
- 应用信息 (appId)
- 作用域 (scope)
- 过期时间 (exp)
- 令牌 ID (jti)

### 4. 安全上下文集成

- 与 Spring Security 兼容
- 支持线程本地存储
- 提供全局访问接口

## 依赖关系

### 核心依赖

- **blen-kernel-common**: 基础功能依赖
- **io.jsonwebtoken:jjwt-api**: JWT API
- **io.jsonwebtoken:jjwt-impl**: JWT 实现
- **io.jsonwebtoken:jjwt-jackson**: JWT Jackson 支持
- **jakarta.servlet:jakarta.servlet-api**: Servlet API
- **spring-web**: Spring Web 支持

## 使用方式

### 1. 添加依赖

```xml
<dependency>
    <groupId>dev.dong4j</groupId>
    <artifactId>blen-kernel-auth</artifactId>
    <version>${blen-kernel.version}</version>
</dependency>
```

### 2. 生成 JWT 令牌

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

        Date expiration = new Date(System.currentTimeMillis() + 3600000); // 1小时
        return JwtUtils.generateToken(claims, jwtSecret, expiration);
    }
}
```

### 3. 解析 JWT 令牌

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

### 4. 从请求中提取令牌

```java
@RestController
public class UserController {

    @GetMapping("/profile")
    public Result<User> getProfile(HttpServletRequest request) {
        String token = JwtUtils.getToken(request);
        if (token != null) {
            AuthorizationUser user = JwtUtils.PlayGround.getUser(token);
            return R.succeed(user);
        }
        return R.failed("未找到认证令牌");
    }
}
```

### 5. 安全上下文使用

```java
@Service
public class UserService {

    public User getCurrentUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication auth = context.getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof AuthorizationUser) {
            return (AuthorizationUser) auth.getPrincipal();
        }
        return null;
    }
}
```

## 配置说明

### JWT 配置

```properties
# JWT 密钥
jwt.secret=your-secret-key-here-must-be-at-least-256-bits

# JWT 过期时间（秒）
jwt.expiration=3600

# 令牌前缀
jwt.prefix=Bearer
```

### 安全配置

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityContextHolderStrategy securityContextHolderStrategy() {
        return new GlobalSecurityContextHolderStrategy();
    }
}
```

## 高级用法

### 1. 自定义声明

```java
Map<String, Object> claims = new HashMap<>();
claims.put("customField", "customValue");
claims.put(ZekaClaims.USER, user);
// ... 其他声明

String token = JwtUtils.generateToken(claims, signKey, expiration);
```

### 2. 令牌刷新

```java
public String refreshToken(String oldToken) {
    if (JwtUtils.isExpiration(oldToken)) {
        throw new AuthException("令牌已过期");
    }

    // 从旧令牌中提取用户信息
    AuthorizationUser user = JwtUtils.PlayGround.getUser(oldToken);
    List<AuthorizationRole> roles = JwtUtils.PlayGround.getRoles(oldToken);

    // 生成新令牌
    return generateToken(user, roles);
}
```

### 3. 多客户端支持

```java
public String generateTokenForClient(User user, String clientType) {
    Map<String, Object> claims = new HashMap<>();
    claims.put(ZekaClaims.USER, user);
    claims.put(ZekaClaims.CLIENT_TYPE, clientType);
    claims.put(ZekaClaims.CLIENT_ID, generateClientId(clientType));

    return JwtUtils.generateToken(claims, signKey, expiration);
}
```

## 安全注意事项

1. **密钥管理**: JWT 密钥必须足够复杂，建议至少 256 位
2. **令牌存储**: 客户端应安全存储令牌，避免 XSS 攻击
3. **HTTPS**: 生产环境必须使用 HTTPS 传输令牌
4. **过期时间**: 合理设置令牌过期时间，平衡安全性和用户体验
5. **令牌撤销**: 考虑实现令牌黑名单机制

## 错误处理

```java
try {
    Claims claims = JwtUtils.getClaims(signKey, token);
    // 处理令牌
} catch (ExpiredJwtException e) {
    // 令牌过期
    throw new AuthException("令牌已过期");
} catch (SignatureException e) {
    // 签名验证失败
    throw new AuthException("令牌签名无效");
} catch (MalformedJwtException e) {
    // 令牌格式错误
    throw new AuthException("令牌格式错误");
}
```

## 版本历史

- **1.0.0**: 初始版本，基础 JWT 功能
- **1.2.3**: 增加无签名解析支持
- **1.6.0**: 优化安全上下文管理

## 贡献指南

欢迎提交 Issue 和 Pull Request 来改进这个模块。

## 许可证

MIT License
