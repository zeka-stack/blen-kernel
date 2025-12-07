---
published: 2025.01.01
---

# 异常处理器

## 概述

`blen-kernel-web` 模块提供了 `ServletGlobalExceptionHandler`，用于统一处理 Web 应用中的异常，将异常转换为统一的 API 响应格式，提供友好的错误信息。

## 核心组件

### ServletGlobalExceptionHandler

Servlet 全局异常处理器，继承自 `GlobalExceptionHandler`，专门处理 Servlet 环境下的异常。

#### 设计原理

`ServletGlobalExceptionHandler` 基于 Spring 的 `@RestControllerAdvice` 机制，通过以下方式实现全局异常处理：

1. **异常捕获**: 使用 `@ExceptionHandler` 注解捕获特定类型的异常
2. **统一响应**: 将异常转换为统一的 `Result` 响应格式
3. **错误日志**: 记录详细的异常信息，便于问题排查
4. **优先级控制**: 通过 `@Order` 注解控制处理器的优先级

#### 实现细节

```java
@Slf4j
@RestControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE - 100)
@ConditionalOnClass(value = {Servlet.class, DispatcherServlet.class})
public class ServletGlobalExceptionHandler extends GlobalExceptionHandler {
    
    // 继承父类的通用异常处理逻辑
    // 添加 Web 特定的异常处理
}
```

#### 支持的异常类型

##### 1. IllegalStateException

处理非法状态异常，通常由数据传输错误引起：

```java
@ExceptionHandler(value = IllegalStateException.class)
public Result<?> bizExceptionHandler(HttpServletRequest req, 
                                    IllegalStateException e) {
    log.error("接收了一个非法参数的异常", e);
    return result(e, req, "数据传输错误，导致非法参数异常，请联系管理员处理！");
}
```

##### 2. NullPointerException

处理空指针异常：

```java
@ExceptionHandler(value = NullPointerException.class)
public Result<?> exceptionHandler(HttpServletRequest req, 
                                  NullPointerException e) {
    log.error("接收了一个空指针的异常", e);
    return result(e, req, "空指针异常，请联系管理员处理！");
}
```

##### 3. NumberFormatException

处理数字格式异常：

```java
@ExceptionHandler(value = NumberFormatException.class)
public Result<?> numberFormatException(HttpServletRequest req, 
                                      NumberFormatException e) {
    log.error("接收了一个数字格式的异常", e);
    return result(e, req, "数字转换异常！请检查传参的数据类型！");
}
```

##### 4. HttpRequestMethodNotSupportedException

处理请求方法不支持异常：

```java
@ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
public Result<?> httpRequestMethodNotSupportedException(
        HttpServletRequest req, 
        HttpRequestMethodNotSupportedException e) {
    log.error("请求方式错误的异常：{}", e.getMessage());
    return result(e, req, "请求方式不对！请检查请求方式！");
}
```

##### 5. ClassCastException

处理类型转换异常：

```java
@ExceptionHandler(value = ClassCastException.class)
public Result<?> classCastException(HttpServletRequest req, 
                                  ClassCastException e) {
    log.error("类型转换异常", e);
    return result(e, req, "类型转换异常！请检查传参的数据类型！");
}
```

## 继承关系

### GlobalExceptionHandler

`ServletGlobalExceptionHandler` 继承自 `GlobalExceptionHandler`，继承了以下功能：

1. **通用异常处理**: 处理业务异常、参数验证异常等
2. **响应格式统一**: 统一的 `Result` 响应格式
3. **异常信息脱敏**: 生产环境下的异常信息脱敏
4. **链路追踪**: 异常信息的链路追踪支持

## 使用示例

### 基础使用

```java
@RestController
public class UserController {
    
    @GetMapping("/users/{id}")
    public Result<User> getUser(@PathVariable Long id) {
        // 如果抛出异常，会被 ServletGlobalExceptionHandler 捕获
        User user = userService.findById(id);
        return R.succeed(user);
    }
}
```

### 自定义异常处理

```java
@RestControllerAdvice
public class CustomExceptionHandler extends ServletGlobalExceptionHandler {
    
    @ExceptionHandler(BusinessException.class)
    public Result<Void> handleBusinessException(BusinessException e, 
                                                HttpServletRequest request) {
        log.error("业务异常: {}", e.getMessage(), e);
        return R.failed(e.getCode(), e.getMessage());
    }
    
    @ExceptionHandler(DataAccessException.class)
    public Result<Void> handleDataAccessException(DataAccessException e, 
                                                  HttpServletRequest request) {
        log.error("数据访问异常: {}", e.getMessage(), e);
        return R.failed("数据访问失败", "请稍后重试");
    }
}
```

## 响应格式

所有异常都会被转换为统一的 `Result` 响应格式：

```json
{
  "code": 5000,
  "success": false,
  "data": null,
  "message": "异常信息",
  "traceId": "1484501823002316800",
  "extend": null
}
```

## 配置说明

### 优先级配置

通过 `@Order` 注解控制异常处理器的优先级：

```java
@Order(Ordered.LOWEST_PRECEDENCE - 100) // 优先级较高
public class ServletGlobalExceptionHandler extends GlobalExceptionHandler {
    // ...
}
```

### 条件配置

通过 `@ConditionalOnClass` 注解控制处理器的生效条件：

```java
@ConditionalOnClass(value = {Servlet.class, DispatcherServlet.class})
public class ServletGlobalExceptionHandler extends GlobalExceptionHandler {
    // 仅在 Servlet 环境下生效
}
```

## 最佳实践

### 1. 异常分类处理

- 业务异常: 使用自定义异常类，提供友好的错误信息
- 系统异常: 记录详细日志，返回通用错误信息
- 参数异常: 提供具体的参数错误信息

### 2. 错误信息设计

- 开发环境: 提供详细的异常堆栈信息
- 生产环境: 提供友好的错误提示，避免暴露敏感信息
- 统一格式: 所有错误信息遵循统一的格式

### 3. 日志记录

- 记录完整的异常堆栈
- 记录请求信息（URL、参数、Header 等）
- 记录用户信息（如果可用）
- 使用合适的日志级别

## 扩展开发

### 添加自定义异常处理

```java
@RestControllerAdvice
public class CustomExceptionHandler extends ServletGlobalExceptionHandler {
    
    @ExceptionHandler(CustomException.class)
    public Result<Void> handleCustomException(CustomException e, 
                                              HttpServletRequest request) {
        log.error("自定义异常: {}", e.getMessage(), e);
        return R.failed(e.getErrorCode(), e.getMessage());
    }
}
```

### 异常信息国际化

```java
@RestControllerAdvice
public class I18nExceptionHandler extends ServletGlobalExceptionHandler {
    
    @Autowired
    private MessageSource messageSource;
    
    @ExceptionHandler(BusinessException.class)
    public Result<Void> handleBusinessException(BusinessException e, 
                                                HttpServletRequest request,
                                                Locale locale) {
        String message = messageSource.getMessage(
            e.getMessageKey(), 
            e.getArgs(), 
            locale);
        return R.failed(e.getCode(), message);
    }
}
```

## 注意事项

1. **异常顺序**: 确保异常处理器的优先级正确，避免异常被错误处理
2. **信息脱敏**: 生产环境下避免暴露敏感异常信息
3. **性能影响**: 异常处理不应影响系统性能，避免在异常处理中执行耗时操作
4. **日志记录**: 确保异常信息被正确记录，便于问题排查

