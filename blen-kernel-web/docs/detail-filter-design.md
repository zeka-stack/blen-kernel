---
published: 2025.01.01
---

# Filter 设计

## 概述

`blen-kernel-web` 模块提供了完整的 Filter 过滤器体系，用于处理 Web 请求的预处理和后处理。该模块包含异常过滤器、全局缓存过滤器等核心组件，为 Web
应用提供统一的请求处理机制。

## 核心组件

### ExceptionFilter

异常过滤器用于捕获 Filter 链中抛出的异常，并将其转发到错误控制器进行处理。

#### 设计原理

由于在 Filter 中抛出的异常无法被 Controller 层的异常处理器捕获，`ExceptionFilter` 通过以下机制解决这个问题：

1. **异常捕获**: 在 Filter 链执行过程中捕获所有异常
2. **异常存储**: 将异常信息存储到 Request 属性中
3. **请求转发**: 将请求转发到 `/error` 路径，由 `ServletErrorController` 处理

#### 实现细节

```java
@Override
public void doFilter(ServletRequest request, ServletResponse response,
                    FilterChain chain) throws IOException, ServletException {
    // 检查是否已经处理过异常，避免循环
    boolean isRethrow = !Objects.isNull(
        request.getAttribute(BasicConstant.REQUEST_EXCEPTION_INFO_ATTR));
    if (isRethrow) {
        chain.doFilter(request, response);
        return;
    }
    try {
        chain.doFilter(request, response);
    } catch (Exception e) {
        // 保存异常信息到 Request 属性
        request.setAttribute(BasicConstant.REQUEST_EXCEPTION_INFO_ATTR, e);
        // 转发到错误控制器
        request.getRequestDispatcher(
            this.serverProperties.getError().getPath()).forward(request, response);
    }
}
```

#### 使用场景

- Filter 链中的异常处理
- 安全验证失败时的异常捕获
- 请求预处理失败时的异常处理

### ServletGlobalCacheFilter

全局缓存过滤器用于缓存请求和响应内容，解决多次读取请求体或响应体的问题。

#### 设计原理

Servlet 的 `HttpServletRequest` 和 `HttpServletResponse` 的输入输出流只能读取一次，这在需要多次访问请求体或响应体的场景下会造成问题。
`ServletGlobalCacheFilter` 通过包装器模式解决这个问题：

1. **请求包装**: 使用 `ContentCachingRequestWrapper` 包装原始请求
2. **响应包装**: 使用 `ContentCachingResponseWrapper` 包装原始响应
3. **内容缓存**: 将请求体和响应体内容缓存到内存中
4. **内容回写**: 在 Filter 链执行完成后，将响应内容回写到原始响应

#### 实现细节

```java
@Override
public void doFilterInternal(HttpServletRequest request,
                            HttpServletResponse response,
                            FilterChain chain) throws ServletException, IOException {
    CacheRequestEnhanceWrapper cacheRequest;
    CacheResponseEnhanceWrapper cacheResponse;

    // 包装请求，支持多次读取
    if (request instanceof CacheRequestEnhanceWrapper) {
        cacheRequest = (CacheRequestEnhanceWrapper) request;
    } else {
        cacheRequest = new CacheRequestEnhanceWrapper(
            new ContentCachingRequestWrapper(request));
    }

    // 包装响应，支持多次读取
    if (response instanceof CacheResponseEnhanceWrapper) {
        cacheResponse = (CacheResponseEnhanceWrapper) response;
    } else {
        cacheResponse = new CacheResponseEnhanceWrapper(
            new ContentCachingResponseWrapper(response));
    }

    chain.doFilter(cacheRequest, cacheResponse);
    // 将响应内容回写到原始响应
    cacheResponse.copyBodyToResponse();
}
```

#### 使用场景

- 请求日志记录（需要读取请求体）
- 请求参数验证（需要多次读取请求参数）
- 响应日志记录（需要读取响应体）
- API 网关场景下的请求响应处理

### AbstractSkipFilter

抽象跳过过滤器提供了 URL 匹配和跳过机制，允许某些 URL 不经过特定的 Filter 处理。

#### 设计原理

通过正则表达式匹配 URL，支持灵活的 URL 过滤规则：

1. **URL 匹配**: 使用正则表达式匹配请求 URL
2. **跳过逻辑**: 如果 URL 匹配跳过规则，则直接放行
3. **继承扩展**: 子类实现 `doFilterInternal` 方法处理具体逻辑

#### 实现细节

```java
public abstract class AbstractSkipFilter implements Filter {
    private final Pattern skipUrlPattern;

    public AbstractSkipFilter(String skipUrl) {
        this.skipUrlPattern = StringUtils.isNotBlank(skipUrl)
            ? Pattern.compile(skipUrl) : null;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                        FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();

        // 检查是否需要跳过
        if (shouldSkip(requestURI)) {
            chain.doFilter(request, response);
            return;
        }

        // 执行具体的过滤逻辑
        doFilterInternal(httpRequest, (HttpServletResponse) response, chain);
    }

    protected boolean shouldSkip(String requestURI) {
        return skipUrlPattern != null && skipUrlPattern.matcher(requestURI).matches();
    }

    protected abstract void doFilterInternal(HttpServletRequest request,
                                           HttpServletResponse response,
                                           FilterChain chain)
        throws ServletException, IOException;
}
```

#### 使用场景

- 静态资源请求跳过处理
- 健康检查接口跳过认证
- 公开 API 跳过某些过滤器

## 配置说明

### WebProperties 配置

```yaml
blen:
  kernel:
    web:
      # 启用全局缓存过滤器
      enable-global-cache-filter: true
      # 启用异常过滤器
      enable-exception-filter: true
      # 不需要缓存请求的 URL 正则表达式
      ignore-cache-request-url: "/actuator/.*|/health.*"
```

### Filter 顺序配置

Filter 的执行顺序很重要，建议的配置顺序：

1. `ServletGlobalCacheFilter` - 优先级最高，最先执行
2. `ExceptionFilter` - 用于捕获后续 Filter 的异常
3. 其他业务 Filter

## 最佳实践

### 1. Filter 性能优化

- 使用 `AbstractSkipFilter` 跳过不需要处理的请求
- 避免在 Filter 中执行耗时操作
- 合理使用请求响应缓存，避免内存溢出

### 2. 异常处理

- 在 Filter 中捕获异常并转发到错误控制器
- 避免在 Filter 中直接返回响应，统一由错误控制器处理
- 记录详细的异常日志，便于问题排查

### 3. 请求响应缓存

- 仅对需要多次读取的请求启用缓存
- 配置合理的 URL 跳过规则，避免不必要的缓存
- 注意大文件请求的内存占用

## 扩展开发

### 自定义 Filter

```java
@Component
public class CustomFilter extends AbstractSkipFilter {

    public CustomFilter() {
        super("/api/public/.*"); // 跳过公开 API
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                  HttpServletResponse response,
                                  FilterChain chain)
        throws ServletException, IOException {
        // 自定义处理逻辑
        String token = request.getHeader("Authorization");
        if (StringUtils.isBlank(token)) {
            throw new AuthenticationException("未授权");
        }
        chain.doFilter(request, response);
    }
}
```

## 注意事项

1. **循环引用**: `ExceptionFilter` 需要检查是否已经处理过异常，避免循环转发
2. **内存占用**: 请求响应缓存会占用内存，需要合理配置跳过规则
3. **性能影响**: Filter 链的执行会影响请求性能，需要优化 Filter 逻辑
4. **线程安全**: Filter 是单例的，需要注意线程安全问题

