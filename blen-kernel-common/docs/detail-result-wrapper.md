---
published: 2025.01.01
---

# 响应封装

## 概述

`blen-kernel-common` 模块提供了统一的 API 响应封装机制，通过 `Result` 类和 `R` 工具类，为所有 API 响应提供统一的数据结构和便捷的构建方法。

## 核心组件

### Result 类

统一的 API 响应结构封装类。

#### 数据结构

```java
public class Result<T> implements Serializable {
    /** 响应码 */
    private Integer code;
    /** 是否成功 */
    private Boolean success;
    /** 响应数据 */
    private T data;
    /** 响应消息 */
    private String message;
    /** 链路追踪 ID */
    private String traceId;
    /** 扩展字段 */
    private Object extend;
}
```

#### 响应格式

所有 API 响应都遵循统一的格式：

```json
{
  "code": 2000,
  "success": true,
  "data": {},
  "message": "操作成功",
  "traceId": "1484501823002316800",
  "extend": null
}
```

### R 工具类

提供便捷的响应构建方法。

#### 核心方法

##### succeed

构建成功响应：

```java
public static <T> Result<T> succeed(T data) {
    return Result.<T>builder()
        .code(BaseCodes.SUCCESS.getCode())
        .success(true)
        .data(data)
        .message(BaseCodes.SUCCESS.getMessage())
        .traceId(Trace.getTraceId())
        .build();
}
```

##### failed

构建失败响应：

```java
public static <T> Result<T> failed(String message) {
    return Result.<T>builder()
        .code(BaseCodes.FAILED.getCode())
        .success(false)
        .message(message)
        .traceId(Trace.getTraceId())
        .build();
}
```

##### build

构建自定义响应：

```java
public static <T> Result<T> build(Integer code, String message, T data) {
    return Result.<T>builder()
        .code(code)
        .success(code.equals(BaseCodes.SUCCESS.getCode()))
        .data(data)
        .message(message)
        .traceId(Trace.getTraceId())
        .build();
}
```

## 使用示例

### 成功响应

```java
@RestController
public class UserController {
    
    @GetMapping("/users/{id}")
    public Result<User> getUser(@PathVariable Long id) {
        User user = userService.findById(id);
        return R.succeed(user);
    }
    
    @PostMapping("/users")
    public Result<User> createUser(@Valid @RequestBody User user) {
        User createdUser = userService.createUser(user);
        return R.succeed(createdUser);
    }
}
```

### 失败响应

```java
@RestController
public class UserController {
    
    @DeleteMapping("/users/{id}")
    public Result<Void> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return R.succeed(null);
        } catch (Exception e) {
            return R.failed("删除用户失败: " + e.getMessage());
        }
    }
}
```

### 自定义响应

```java
@RestController
public class UserController {
    
    @GetMapping("/users/count")
    public Result<Long> getUserCount() {
        Long count = userService.getUserCount();
        return R.build(2000, "查询成功", count);
    }
}
```

## 错误码定义

### BaseCodes

基础错误码枚举，定义了常用的错误码：

```java
public enum BaseCodes implements SerializeEnum {
    /** 成功 */
    SUCCESS(2000, "操作成功"),
    /** 失败 */
    FAILED(5000, "操作失败"),
    /** 参数错误 */
    PARAM_ERROR(4000, "参数错误"),
    /** 未授权 */
    UNAUTHORIZED(4010, "未授权"),
    /** 禁止访问 */
    FORBIDDEN(4030, "禁止访问"),
    /** 资源不存在 */
    NOT_FOUND(4040, "资源不存在"),
    /** 服务器错误 */
    SERVER_ERROR(5000, "服务器错误");
}
```

### 自定义错误码

```java
public enum UserCodes implements SerializeEnum {
    USER_NOT_FOUND(4041, "用户不存在"),
    USER_ALREADY_EXISTS(4001, "用户已存在"),
    USER_PASSWORD_ERROR(4002, "密码错误");
}
```

## 最佳实践

### 1. 响应格式统一

- 所有 API 响应都使用 `Result` 封装
- 使用 `R` 工具类构建响应
- 保持响应格式的一致性

### 2. 错误码管理

- 使用枚举定义错误码
- 错误码应该具有唯一性
- 提供清晰的错误消息

### 3. 链路追踪

- 自动添加 `traceId` 到响应中
- 使用 `Trace` 工具类管理链路追踪
- 支持分布式链路追踪

## 注意事项

1. **响应格式**: 确保所有 API 响应都遵循统一格式
2. **错误码**: 错误码应该具有唯一性和可读性
3. **链路追踪**: 确保 `traceId` 正确传递
4. **扩展字段**: 合理使用 `extend` 字段，避免过度使用

