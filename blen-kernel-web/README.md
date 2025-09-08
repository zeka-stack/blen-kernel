# Blen Kernel Web

## 概述

`blen-kernel-web` 是 Zeka.Stack 框架的 Web 模块，提供了 Web 应用开发所需的核心功能，包括全局异常处理、错误控制器、请求响应增强、过滤器等。该模块基于
Spring Boot 和 Servlet 技术，为 Web 应用提供统一的处理机制。

## 主要功能

### 1. 全局异常处理

- **ServletGlobalExceptionHandler**: Servlet 全局异常处理器
- 继承自 `GlobalExceptionHandler`，提供 Web 特定的异常处理
- 支持多种异常类型的统一处理

### 2. 错误控制器

- **ServletErrorController**: Servlet 错误控制器
- 处理 404、500 等 HTTP 错误
- 提供统一的错误响应格式

### 3. 请求响应增强

- **CacheRequestEnhanceWrapper**: 请求增强包装器
- **CacheResponseEnhanceWrapper**: 响应增强包装器
- 支持请求响应的缓存和增强处理

### 4. 过滤器支持

- **ExceptionFilter**: 异常过滤器
- **ServletGlobalCacheFilter**: 全局缓存过滤器
- **AbstractSkipFilter**: 抽象跳过过滤器

### 5. Web 工具类

- **InnerWebUtils**: 内部 Web 工具类
- 提供 Web 相关的工具方法

### 6. 配置支持

- **WebProperties**: Web 属性配置
- 支持 Web 相关的配置管理

## 核心特性

### 1. 统一的异常处理

- 继承基础异常处理器
- 提供 Web 特定的异常处理逻辑
- 支持多种异常类型的分类处理

### 2. 请求响应增强

- 支持请求体的缓存和重复读取
- 支持响应体的增强处理
- 提供统一的包装器接口

### 3. 过滤器链支持

- 提供异常处理过滤器
- 支持全局缓存过滤器
- 支持过滤器的跳过机制

### 4. 错误页面处理

- 统一的错误页面响应
- 支持自定义错误页面
- 提供友好的错误信息

## 依赖关系

### 核心依赖

- **blen-kernel-common**: 基础功能依赖
- **spring-boot-autoconfigure**: Spring Boot 自动配置
- **spring-webmvc**: Spring Web MVC 支持
- **jakarta.servlet:jakarta.servlet-api**: Servlet API

## 使用方式

### 1. 添加依赖

```xml
<dependency>
    <groupId>dev.dong4j</groupId>
    <artifactId>blen-kernel-web</artifactId>
    <version>${blen-kernel.version}</version>
</dependency>
```

### 2. 全局异常处理

```java
@RestControllerAdvice
public class CustomExceptionHandler extends ServletGlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public Result<Void> handleBusinessException(BusinessException e, HttpServletRequest request) {
        log.error("业务异常: {}", e.getMessage(), e);
        return R.failed(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(DataAccessException.class)
    public Result<Void> handleDataAccessException(DataAccessException e, HttpServletRequest request) {
        log.error("数据访问异常: {}", e.getMessage(), e);
        return R.failed("数据访问失败", "请稍后重试");
    }
}
```

### 3. 自定义错误控制器

```java
@RestController
public class CustomErrorController extends ServletErrorController {

    @RequestMapping("/error")
    public Result<Void> handleError(HttpServletRequest request) {
        HttpStatus status = getStatus(request);

        if (status == HttpStatus.NOT_FOUND) {
            return R.failed(404, "请求的资源不存在");
        } else if (status == HttpStatus.INTERNAL_SERVER_ERROR) {
            return R.failed(500, "服务器内部错误");
        }

        return R.failed(status.value(), "请求处理失败");
    }
}
```

### 4. 使用请求响应增强

```java
@Component
public class RequestResponseEnhancer {

    public void enhanceRequest(HttpServletRequest request) {
        if (request instanceof CacheRequestEnhanceWrapper) {
            CacheRequestEnhanceWrapper wrapper = (CacheRequestEnhanceWrapper) request;
            // 处理增强的请求
            String body = wrapper.getCachedBody();
            // 进行业务处理
        }
    }

    public void enhanceResponse(HttpServletResponse response) {
        if (response instanceof CacheResponseEnhanceWrapper) {
            CacheResponseEnhanceWrapper wrapper = (CacheResponseEnhanceWrapper) response;
            // 处理增强的响应
            // 进行业务处理
        }
    }
}
```

### 5. 自定义过滤器

```java
@Component
@Order(1)
public class CustomFilter extends AbstractSkipFilter {

    @Override
    protected boolean shouldSkip(HttpServletRequest request) {
        // 定义跳过条件
        String path = request.getRequestURI();
        return path.startsWith("/static/") || path.startsWith("/public/");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                  HttpServletResponse response,
                                  FilterChain filterChain) throws ServletException, IOException {

        // 前置处理
        log.info("请求开始: {}", request.getRequestURI());

        try {
            filterChain.doFilter(request, response);
        } finally {
            // 后置处理
            log.info("请求结束: {}", request.getRequestURI());
        }
    }
}
```

### 6. 使用 Web 工具类

```java
@Service
public class WebService {

    public void processRequest(HttpServletRequest request) {
        // 获取客户端 IP
        String clientIp = InnerWebUtils.getClientIp(request);

        // 获取用户代理
        String userAgent = InnerWebUtils.getUserAgent(request);

        // 检查是否为 AJAX 请求
        boolean isAjax = InnerWebUtils.isAjaxRequest(request);

        // 处理请求
        log.info("客户端IP: {}, 用户代理: {}, 是否AJAX: {}", clientIp, userAgent, isAjax);
    }
}
```

## 配置说明

### Web 配置

```yaml
blen:
  web:
    # 错误页面配置
    error:
      enabled: true
      path: /error
    # 缓存配置
    cache:
      enabled: true
      max-size: 1024
    # 过滤器配置
    filter:
      enabled: true
      order: 1
```

### Spring Boot 配置

```yaml
spring:
  web:
    resources:
      add-mappings: true
      static-locations: classpath:/static/
  mvc:
    throw-exception-if-no-handler-found: true
  servlet:
    multipart:
      enabled: true
      max-file-size: 10MB
      max-request-size: 10MB
```

## 高级用法

### 1. 自定义异常处理策略

```java
@Configuration
public class WebConfig {

    @Bean
    public ServletGlobalExceptionHandler exceptionHandler() {
        return new ServletGlobalExceptionHandler() {
            @Override
            public Result<?> handleException(Exception e, HttpServletRequest request) {
                // 自定义异常处理逻辑
                if (e instanceof CustomException) {
                    return R.failed("自定义异常", e.getMessage());
                }
                return super.handleException(e, request);
            }
        };
    }
}
```

### 2. 请求响应拦截器

```java
@Component
public class RequestResponseInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object handler) throws Exception {
        // 请求前处理
        log.info("请求开始: {} {}", request.getMethod(), request.getRequestURI());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request,
                          HttpServletResponse response,
                          Object handler,
                          ModelAndView modelAndView) throws Exception {
        // 请求后处理
        log.info("请求处理完成: {} {}", request.getMethod(), request.getRequestURI());
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                              HttpServletResponse response,
                              Object handler,
                              Exception ex) throws Exception {
        // 请求完成处理
        if (ex != null) {
            log.error("请求处理异常: {}", ex.getMessage(), ex);
        }
    }
}
```

### 3. 自定义错误页面

```java
@Controller
public class ErrorController {

    @RequestMapping("/error/404")
    public String error404(Model model) {
        model.addAttribute("errorCode", 404);
        model.addAttribute("errorMessage", "页面不存在");
        return "error/404";
    }

    @RequestMapping("/error/500")
    public String error500(Model model) {
        model.addAttribute("errorCode", 500);
        model.addAttribute("errorMessage", "服务器内部错误");
        return "error/500";
    }
}
```

## 异常处理优先级

1. **自定义异常处理器**: 最高优先级
2. **ServletGlobalExceptionHandler**: 中等优先级
3. **GlobalExceptionHandler**: 基础优先级
4. **默认异常处理器**: 最低优先级

## 性能优化

1. **过滤器顺序**: 合理设置过滤器顺序，避免不必要的处理
2. **异常处理**: 避免在异常处理中执行耗时操作
3. **缓存策略**: 合理使用请求响应缓存

## 安全注意事项

1. **异常信息**: 生产环境避免暴露敏感异常信息
2. **错误页面**: 提供友好的错误页面，避免信息泄露
3. **请求验证**: 在过滤器中验证请求的合法性

## 版本历史

- **1.0.0**: 初始版本，基础 Web 功能
- **1.2.3**: 增加请求响应增强功能
- **1.3.0**: 优化异常处理机制

## 贡献指南

欢迎提交 Issue 和 Pull Request 来改进这个模块。

## 许可证

MIT License
