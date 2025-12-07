---
published: 2025.01.01
---

# 请求响应包装器

## 概述

`blen-kernel-web` 模块提供了请求和响应包装器，用于增强 Servlet 请求和响应的功能，支持请求体的多次读取、响应体的缓存、参数的自定义等功能。

## 核心组件

### CacheRequestEnhanceWrapper

请求增强包装器，扩展了 `HttpServletRequestWrapper` 的功能，支持请求体的缓存和多次读取。

#### 设计原理

Servlet 的 `HttpServletRequest` 的输入流只能读取一次，这在需要多次访问请求体的场景下会造成问题。`CacheRequestEnhanceWrapper` 通过以下机制解决这个问题：

1. **请求体缓存**: 在构造时读取并缓存请求体内容
2. **多次读取**: 通过缓存的字节数组提供多次读取能力
3. **参数增强**: 支持自定义请求参数和 Header
4. **内容类型识别**: 根据 Content-Type 自动选择参数获取方式

#### 实现细节

```java
public class CacheRequestEnhanceWrapper extends HttpServletRequestWrapper {
    protected byte[] body;
    protected String bodyString;
    protected Map<String, String> headerMap = Maps.newHashMap();
    protected Map<String, String[]> params = Maps.newHashMap();
    
    public CacheRequestEnhanceWrapper(ContentCachingRequestWrapper request) {
        super(request);
        // 设置字符编码
        request.setCharacterEncoding(Charsets.UTF_8.name());
        this.params.putAll(request.getParameterMap());
        
        // 读取并缓存请求体
        if (request.getContentAsByteArray().length == 0) {
            try (ServletInputStream inputStream = request.getInputStream()) {
                this.body = IoUtils.toByteArray(inputStream);
            } catch (IOException e) {
                log.error("读取 inputStream 错误", e);
            }
        } else {
            this.body = request.getContentAsByteArray();
        }
        
        this.bodyString = new String(this.body, StandardCharsets.UTF_8);
    }
}
```

#### 核心功能

##### 1. 请求体读取

```java
@Override
public ServletInputStream getInputStream() {
    return WebUtils.getCacheInputStream(this.body);
}

@Override
public BufferedReader getReader() {
    return new BufferedReader(new InputStreamReader(this.getInputStream()));
}

public String getBodyString() {
    return this.bodyString;
}

public byte[] getBody() {
    return this.body;
}
```

##### 2. 参数获取

根据 Content-Type 自动选择参数获取方式：

- `application/x-www-form-urlencoded`: 从 `getParameter()` 获取
- `application/json`: 从 `getInputStream()` 获取

```java
@Override
public String getParameter(String name) {
    String[] values = this.params.get(name);
    return values == null || values.length == 0 ? null : values[0];
}

@Override
public Map<String, String[]> getParameterMap() {
    return this.params;
}
```

##### 3. Header 增强

支持自定义 Header，优先级高于原始 Header：

```java
public void addHeader(String name, String value) {
    this.headerMap.put(name, value);
}

@Override
public String getHeader(String name) {
    String headerValue = super.getHeader(name);
    if (this.headerMap.containsKey(name)) {
        headerValue = this.headerMap.get(name);
    }
    return headerValue;
}
```

##### 4. 参数设置

支持动态设置请求参数：

```java
public void setParameter(String key, String value) {
    if (value == null) {
        return;
    }
    this.params.put(key, new String[]{value});
}

public void setParameter(String key, String[] value) {
    if (value == null) {
        return;
    }
    this.params.put(key, value);
}
```

#### 使用场景

- 请求日志记录（需要读取请求体）
- 请求参数验证（需要多次读取请求参数）
- API 网关场景下的请求处理
- 请求参数动态修改

### CacheResponseEnhanceWrapper

响应增强包装器，扩展了 `HttpServletResponseWrapper` 的功能，支持响应体的缓存和多次读取。

#### 设计原理

Servlet 的 `HttpServletResponse` 的输出流只能写入一次，这在需要读取响应体的场景下会造成问题。`CacheResponseEnhanceWrapper` 通过以下机制解决这个问题：

1. **响应体缓存**: 使用 `ContentCachingResponseWrapper` 缓存响应体内容
2. **多次读取**: 通过缓存的字节数组提供多次读取能力
3. **内容回写**: 在 Filter 链执行完成后，将响应内容回写到原始响应

#### 实现细节

```java
public class CacheResponseEnhanceWrapper extends HttpServletResponseWrapper {
    private final ContentCachingResponseWrapper response;
    
    public CacheResponseEnhanceWrapper(ContentCachingResponseWrapper response) {
        super(response);
        this.response = response;
    }
    
    public byte[] getContent() {
        return this.response.getContentAsByteArray();
    }
    
    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return this.response.getOutputStream();
    }
    
    public void copyBodyToResponse() {
        try {
            this.response.copyBodyToResponse();
        } catch (IOException e) {
            throw new LowestException("缓存 Response 失败", e);
        }
    }
}
```

#### 核心功能

##### 1. 响应体读取

```java
public byte[] getContent() {
    return this.response.getContentAsByteArray();
}

public String getContentAsString() {
    return new String(this.getContent(), StandardCharsets.UTF_8);
}
```

##### 2. 内容回写

在 Filter 链执行完成后，需要将响应内容回写到原始响应：

```java
public void copyBodyToResponse() {
    try {
        this.response.copyBodyToResponse();
    } catch (IOException e) {
        throw new LowestException("缓存 Response 失败", e);
    }
}
```

#### 使用场景

- 响应日志记录（需要读取响应体）
- API 网关场景下的响应处理
- 响应内容加密/解密
- 响应内容压缩

## 使用示例

### 请求包装器使用

```java
@Filter
public class RequestLogFilter implements Filter {
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                        FilterChain chain) throws IOException, ServletException {
        if (request instanceof CacheRequestEnhanceWrapper) {
            CacheRequestEnhanceWrapper wrapper = 
                (CacheRequestEnhanceWrapper) request;
            
            // 读取请求体
            String body = wrapper.getBodyString();
            log.info("请求体: {}", body);
            
            // 读取请求参数
            Map<String, String[]> params = wrapper.getParameterMap();
            log.info("请求参数: {}", params);
            
            // 动态修改参数
            wrapper.setParameter("traceId", UUID.randomUUID().toString());
        }
        
        chain.doFilter(request, response);
    }
}
```

### 响应包装器使用

```java
@Filter
public class ResponseLogFilter implements Filter {
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                        FilterChain chain) throws IOException, ServletException {
        if (response instanceof CacheResponseEnhanceWrapper) {
            CacheResponseEnhanceWrapper wrapper = 
                (CacheResponseEnhanceWrapper) response;
            
            chain.doFilter(request, wrapper);
            
            // 读取响应体
            byte[] content = wrapper.getContent();
            log.info("响应体: {}", new String(content, StandardCharsets.UTF_8));
            
            // 回写响应
            wrapper.copyBodyToResponse();
        } else {
            chain.doFilter(request, response);
        }
    }
}
```

## 配置说明

### 自动配置

`ServletGlobalCacheFilter` 会自动包装请求和响应：

```java
@Override
public void doFilterInternal(HttpServletRequest request,
                            HttpServletResponse response,
                            FilterChain chain) throws ServletException, IOException {
    CacheRequestEnhanceWrapper cacheRequest = 
        new CacheRequestEnhanceWrapper(
            new ContentCachingRequestWrapper(request));
    CacheResponseEnhanceWrapper cacheResponse = 
        new CacheResponseEnhanceWrapper(
            new ContentCachingResponseWrapper(response));
    
    chain.doFilter(cacheRequest, cacheResponse);
    cacheResponse.copyBodyToResponse();
}
```

### 跳过配置

可以通过配置跳过某些 URL 的缓存处理：

```yaml
blen:
  kernel:
    web:
      ignore-cache-request-url: "/actuator/.*|/health.*"
```

## 最佳实践

### 1. 内存管理

- 请求响应缓存会占用内存，需要合理配置跳过规则
- 对于大文件请求，应该跳过缓存处理
- 定期监控内存使用情况

### 2. 性能优化

- 仅在需要多次读取的场景下使用包装器
- 避免在包装器中执行耗时操作
- 合理使用缓存，避免不必要的内存占用

### 3. 错误处理

- 在读取请求体时处理 IO 异常
- 在回写响应时处理 IO 异常
- 记录详细的错误日志

## 注意事项

1. **内存占用**: 请求响应缓存会占用内存，需要注意大文件请求
2. **性能影响**: 包装器会增加一定的性能开销，需要合理使用
3. **线程安全**: 包装器是线程安全的，可以在多线程环境下使用
4. **内容回写**: 使用响应包装器时，必须在 Filter 链执行完成后调用 `copyBodyToResponse()`

