# Blen Kernel Common

## 概述

`blen-kernel-common` 是 Zeka.Stack 框架的核心通用模块，提供了基础的工具类、异常处理、API 响应封装、上下文管理等核心功能。该模块是整个框架的基础依赖，其他模块都依赖于它。

## 主要功能

### 1. API 响应封装

- **Result 类**: 统一的 API 响应结构封装
- **R 类**: 提供便捷的响应构建方法
- 支持成功/失败状态、错误码、消息、数据载荷等

```java
// 成功响应
Result<User> result = R.succeed(user);

// 失败响应
Result<Void> result = R.failed("操作失败");

// 自定义响应
Result<String> result = R.build(2000, "成功", "数据");
```

### 2. 异常处理体系

- **GlobalExceptionHandler**: 全局异常处理器
- **LowestException**: 基础异常类
- **ExceptionInfo**: 异常信息封装
- 支持生产环境异常信息脱敏

### 3. 工具类集合

- **StringUtils**: 字符串处理工具
- **BeanUtils**: Bean 操作工具
- **DateUtils**: 日期时间工具
- **JsonUtils**: JSON 序列化工具
- **WebUtils**: Web 相关工具
- **ReflectionUtils**: 反射工具
- **CollectionUtils**: 集合操作工具

### 4. 上下文管理

- **Trace**: 链路追踪上下文
- **SpringContext**: Spring 上下文工具
- **ThreadContextMap**: 线程上下文管理

### 5. 基础实体类

- **AbstractBaseEntity**: 基础实体抽象类
- **BaseQuery**: 查询基类
- **BasePage**: 分页基类
- **Bridge**: 桥接模式实现

### 6. 枚举和常量

- **SerializeEnum**: 序列化枚举
- **ZekaEnv**: 环境枚举
- **BaseCodes**: 基础错误码

### 7. 配置管理

- **ConfigKit**: 配置工具类
- **PropertyFilePropertySource**: 属性文件源
- **RefreshScopeRefresher**: 配置刷新器

## 核心特性

### 1. 统一的响应格式

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

### 2. 完善的异常处理

- 支持多种异常类型的统一处理
- 生产环境异常信息脱敏
- 异常链路追踪支持

### 3. 丰富的工具类

- 基于 Apache Commons Lang3
- 集成 Hutool 工具库
- 提供 MapStruct 支持

### 4. 线程安全

- 使用 TransmittableThreadLocal 实现父子线程参数透传
- 线程上下文管理

## 依赖关系

### 核心依赖

- Spring Boot (provided)
- Spring Web (provided)
- Jakarta Servlet API (provided)
- Jakarta Validation API (provided)
- Jackson (JSON 处理)
- Hutool (工具库)
- Apache Commons Lang3
- MapStruct (对象映射)

### 测试依赖

- JUnit 5
- Mockito
- Hamcrest
- Awaitility

## 使用方式

### 1. 添加依赖

```xml
<dependency>
    <groupId>dev.dong4j</groupId>
    <artifactId>blen-kernel-common</artifactId>
    <version>${blen-kernel.version}</version>
</dependency>
```

### 2. 使用响应封装

```java
@RestController
public class UserController {

    @GetMapping("/users/{id}")
    public Result<User> getUser(@PathVariable Long id) {
        User user = userService.findById(id);
        return R.succeed(user);
    }
}
```

### 3. 使用工具类

```java
// 字符串工具
String result = StringUtils.isBlank(input) ? "default" : input;

// JSON 工具
String json = Jsons.toJson(user);
User user = Jsons.parse(json, User.class);

// 日期工具
String dateStr = DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss");
```

### 4. 异常处理

```java
// 抛出业务异常
throw new LowestException(BaseCodes.DATA_ERROR, "数据不存在");

// 全局异常处理器会自动捕获并转换为统一响应格式
```

## 配置说明

### 环境配置

```properties
# 环境标识
zeka.env=dev
# 日志管理地址
zeka.logcat.admin.url=http://logcat.server:5555/
```

### 日志配置

支持通过 `CoreBundle` 进行国际化消息管理。

## 注意事项

1. 该模块是基础依赖，其他模块都会依赖它
2. 工具类方法都是静态方法，可以直接调用
3. 异常处理需要配合 `@RestControllerAdvice` 使用
4. 响应格式遵循统一标准，便于前端处理

## 版本历史

- **1.0.0**: 初始版本，提供基础功能
- **1.2.4**: 优化响应封装，增加链路追踪
- **1.3.0**: 增加配置管理功能

## 贡献指南

欢迎提交 Issue 和 Pull Request 来改进这个模块。

## 许可证

MIT License
