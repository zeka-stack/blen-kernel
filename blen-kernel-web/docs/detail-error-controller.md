---
published: 2025.01.01
---

# 错误控制器

## 概述

`blen-kernel-web` 模块提供了 `ServletErrorController`，用于处理 HTTP 错误（如 404、500 等），将 HTML 错误页面转换为统一的 JSON 响应格式，并提供
Filter 异常的兜底处理。

## 核心组件

### ServletErrorController

Servlet 错误控制器，继承自 Spring Boot 的 `BasicErrorController`，提供统一的错误响应处理。

#### 设计原理

Spring Boot 默认的错误控制器会返回 HTML 页面，但在 API 服务中，我们需要返回 JSON 格式的错误响应。`ServletErrorController` 通过以下方式实现：

1. **错误捕获**: 处理 `/error` 路径的请求
2. **格式转换**: 将 HTML 错误页面转换为 JSON 响应
3. **异常处理**: 处理 Filter 中抛出的异常
4. **统一响应**: 使用 `ServletGlobalExceptionHandler` 处理异常

#### 实现细节

```java
@Slf4j
@Tag(name = "Filter 异常处理")
public class ServletErrorController extends BasicErrorController {

    @Resource
    private ObjectMapper objectMapper;

    private final ServletGlobalExceptionHandler servletGlobalExceptionHandler;

    public ServletErrorController(ErrorAttributes errorAttributes,
                                 ErrorProperties errorProperties,
                                 ServletGlobalExceptionHandler servletGlobalExceptionHandler) {
        super(errorAttributes, errorProperties);
        this.servletGlobalExceptionHandler = servletGlobalExceptionHandler;
    }
}
```

#### 核心功能

##### 1. JSON 错误响应

处理 JSON 格式的错误请求：

```java
@Override
@RequestMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
    final Exception exception = this.checkException(request);
    final Result<?> result = servletGlobalExceptionHandler.handleError(exception, request);
    return new ResponseEntity<>(
        Jsons.toMap(result, String.class, Object.class),
        HttpStatus.INTERNAL_SERVER_ERROR);
}
```

##### 2. HTML 错误响应转换

将 HTML 错误页面转换为 JSON 响应：

```java
@Override
public ModelAndView errorHtml(HttpServletRequest request,
                             HttpServletResponse response) {
    final Exception exception = this.checkException(request);
    final Result<?> result = servletGlobalExceptionHandler.handleError(exception, request);
    response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());

    MappingJackson2JsonView view = new MappingJackson2JsonView();
    view.setObjectMapper(this.objectMapper);
    view.setContentType(MediaType.APPLICATION_JSON_VALUE);
    return new ModelAndView(view, Jsons.toMap(result, String.class, Object.class));
}
```

##### 3. Filter 异常处理

处理 Filter 中抛出的异常：

```java
private Exception checkException(HttpServletRequest request) {
    Exception exception = (Exception) request.getAttribute(
        BasicConstant.REQUEST_EXCEPTION_INFO_ATTR);

    // 如果是框架层异常，往上抛出让全局异常处理器处理
    if (exception instanceof LowestException lowestException) {
        throw lowestException;
    } else {
        log.error("Filter 异常兜底处理, 请在业务系统中处理自定义 Filter 异常", exception);
        return exception;
    }
}
```

## 工作流程

### 1. 异常捕获流程

```
Filter 抛出异常
    ↓
ExceptionFilter 捕获异常
    ↓
将异常存储到 Request 属性
    ↓
转发到 /error 路径
    ↓
ServletErrorController 处理
    ↓
调用 ServletGlobalExceptionHandler 处理异常
    ↓
返回统一的 JSON 响应
```

### 2. HTTP 错误处理流程

```
HTTP 错误（404、500 等）
    ↓
Spring Boot 错误处理机制
    ↓
ServletErrorController 处理
    ↓
转换为 JSON 响应
    ↓
返回统一的错误格式
```

## 使用示例

### 基础使用

错误控制器会自动处理以下场景：

1. **404 错误**: 请求的资源不存在
2. **500 错误**: 服务器内部错误
3. **Filter 异常**: Filter 链中抛出的异常

### 自定义错误处理

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

## 配置说明

### 错误路径配置

```yaml
server:
  error:
    path: /error  # 错误处理路径
    include-message: always  # 包含错误消息
    include-binding-errors: always  # 包含绑定错误
    include-stacktrace: on_param  # 包含堆栈跟踪
    include-exception: false  # 不包含异常类名
```

### 错误页面配置

```yaml
server:
  error:
    whitelabel:
      enabled: false  # 禁用默认错误页面
```

## 响应格式

所有错误都会被转换为统一的 `Result` 响应格式：

```json
{
  "code": 5000,
  "success": false,
  "data": null,
  "message": "错误信息",
  "traceId": "1484501823002316800",
  "extend": null
}
```

## 最佳实践

### 1. 错误分类

- **4xx 错误**: 客户端错误，提供具体的错误信息
- **5xx 错误**: 服务器错误，记录详细日志，返回通用错误信息
- **Filter 异常**: 使用异常处理器统一处理

### 2. 错误信息设计

- 开发环境: 提供详细的错误信息
- 生产环境: 提供友好的错误提示
- 统一格式: 所有错误信息遵循统一的格式

### 3. 异常处理

- Filter 异常: 通过 `ExceptionFilter` 捕获并转发
- Controller 异常: 通过 `ServletGlobalExceptionHandler` 处理
- 系统异常: 通过错误控制器兜底处理

## 扩展开发

### 自定义错误响应

```java
@RestController
public class CustomErrorController extends ServletErrorController {

    @RequestMapping("/error")
    public Result<Void> handleError(HttpServletRequest request) {
        HttpStatus status = getStatus(request);
        String path = (String) request.getAttribute("javax.servlet.error.request_uri");

        // 自定义错误处理逻辑
        if (status == HttpStatus.NOT_FOUND) {
            return R.failed(404, String.format("路径 %s 不存在", path));
        }

        return super.error(request);
    }
}
```

### 错误监控

```java
@RestController
public class ErrorMonitoringController extends ServletErrorController {

    @Autowired
    private ErrorMonitoringService errorMonitoringService;

    @RequestMapping("/error")
    public Result<Void> handleError(HttpServletRequest request) {
        Exception exception = (Exception) request.getAttribute(
            BasicConstant.REQUEST_EXCEPTION_INFO_ATTR);

        // 记录错误监控信息
        if (exception != null) {
            errorMonitoringService.recordError(exception, request);
        }

        return super.error(request);
    }
}
```

## 注意事项

1. **异常处理顺序**: 确保异常处理器的优先级正确
2. **信息脱敏**: 生产环境下避免暴露敏感错误信息
3. **性能影响**: 错误处理不应影响系统性能
4. **日志记录**: 确保错误信息被正确记录

