---
published: 2025.01.01
---

# 安全上下文

## 概述

`blen-kernel-auth` 模块提供了 `SecurityContextHolder` 和相关的安全上下文管理机制，用于在多线程环境下安全地存储和传递用户认证信息。

## 核心组件

### SecurityContextHolder

安全上下文持有者，提供多种策略来存储和管理安全上下文信息。

#### 设计原理

`SecurityContextHolder` 采用策略模式，支持三种不同的存储策略：

1. **ThreadLocal**: 每个线程拥有独立的安全上下文，不支持子线程继承
2. **InheritableThreadLocal**: 支持子线程继承父线程的安全上下文
3. **Global**: 全局共享安全上下文，适用于单线程应用

#### 存储策略

##### MODE_THREADLOCAL

使用 `ThreadLocal` 存储，每个线程独立：

```java
public static final String MODE_THREADLOCAL = "MODE_THREADLOCAL";
```

**特点**:

- 线程隔离，安全性高
- 不支持子线程继承
- 适用于大多数 Web 应用场景

##### MODE_INHERITABLETHREADLOCAL

使用 `InheritableThreadLocal` 存储，支持子线程继承：

```java
public static final String MODE_INHERITABLETHREADLOCAL = "MODE_INHERITABLETHREADLOCAL";
```

**特点**:

- 支持子线程继承父线程的安全上下文
- 适用于异步任务场景
- 需要注意内存泄漏问题

##### MODE_GLOBAL

使用全局变量存储，所有线程共享：

```java
public static final String MODE_GLOBAL = "MODE_GLOBAL";
```

**特点**:

- 所有线程共享同一个安全上下文
- 适用于单线程应用
- 不适用于多线程环境

#### 配置方式

##### 系统属性配置

```bash
-Dblen.kernel.security.strategy=MODE_THREADLOCAL
```

##### 代码配置

```java
SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_THREADLOCAL);
```

##### 自定义策略

```java
// 实现 SecurityContextHolderStrategy 接口
public class CustomSecurityContextHolderStrategy implements SecurityContextHolderStrategy {
    // 实现接口方法
}

// 配置自定义策略
SecurityContextHolder.setStrategyName("com.example.CustomSecurityContextHolderStrategy");
```

### SecurityContext

安全上下文接口，定义了安全上下文的基本操作。

#### 核心方法

```java
public interface SecurityContext {
    /**
     * 获取认证信息
     */
    Authentication getAuthentication();

    /**
     * 设置认证信息
     */
    void setAuthentication(Authentication authentication);
}
```

### SecurityContextImpl

安全上下文的默认实现。

#### 实现细节

```java
public class SecurityContextImpl implements SecurityContext {
    private Authentication authentication;

    @Override
    public Authentication getAuthentication() {
        return authentication;
    }

    @Override
    public void setAuthentication(Authentication authentication) {
        this.authentication = authentication;
    }
}
```

## 使用示例

### 设置安全上下文

```java
@Service
public class AuthService {

    public void setSecurityContext(AuthorizationUser user) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = new Authentication(user);
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }
}
```

### 获取安全上下文

```java
@Service
public class UserService {

    public AuthorizationUser getCurrentUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        if (authentication != null) {
            return authentication.getPrincipal();
        }
        return null;
    }
}
```

### 清除安全上下文

```java
@Filter
public class SecurityContextFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                        FilterChain chain) throws IOException, ServletException {
        try {
            // 设置安全上下文
            setSecurityContext(request);
            chain.doFilter(request, response);
        } finally {
            // 清除安全上下文，避免内存泄漏
            SecurityContextHolder.clearContext();
        }
    }
}
```

## 最佳实践

### 1. 线程安全

- 使用 `ThreadLocal` 策略确保线程安全
- 在请求结束时清除安全上下文
- 避免在异步任务中共享安全上下文

### 2. 内存管理

- 及时清除不再使用的安全上下文
- 使用 `InheritableThreadLocal` 时注意内存泄漏
- 避免在全局策略下存储大量数据

### 3. 异步任务

- 使用 `InheritableThreadLocal` 策略支持异步任务
- 在异步任务开始时设置安全上下文
- 在异步任务结束时清除安全上下文

## 注意事项

1. **内存泄漏**: 使用 `InheritableThreadLocal` 时需要注意内存泄漏问题
2. **线程安全**: 确保在多线程环境下正确使用安全上下文
3. **清理时机**: 在请求结束时及时清除安全上下文
4. **策略选择**: 根据应用场景选择合适的存储策略

