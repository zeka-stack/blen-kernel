# Blen Kernel

[![License](https://img.shields.io/badge/license-MIT-blue.svg)](https://opensource.org/licenses/MIT)
[![Maven Central](https://img.shields.io/maven-central/v/dev.dong4j/blen-kernel.svg)](https://mvnrepository.com/artifact/dev.dong4j/blen-kernel)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-green.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-17+-orange.svg)](https://openjdk.java.net/)

## 概述

Blen Kernel 是 Zeka.Stack 框架的核心包，提供了企业级 Java 应用开发所需的基础功能和工具。该框架基于 Spring Boot 3.x 和 Java
17+，采用模块化设计，支持按需引入，为开发者提供高效、稳定、易用的开发体验。

## 核心特性

### 🚀 现代化技术栈

- 基于 Spring Boot 3.x 和 Java 17+
- 支持 Jakarta EE 规范
- 集成最新的 Spring 生态

### 🏗️ 模块化设计

- 11 个独立模块，按需引入
- 清晰的模块边界和职责
- 支持模块独立使用

### 🛡️ 企业级功能

- 统一的异常处理机制
- 完善的参数验证体系
- 强大的代码生成工具
- 完整的测试支持

### 🔧 开发友好

- 丰富的工具类集合
- 详细的文档和示例
- 完善的错误提示
- 开箱即用的配置

## 模块介绍

### 核心模块

| 模块                                                     | 功能描述       | 依赖关系 |
|--------------------------------------------------------|------------|------|
| [blen-kernel-common](./blen-kernel-common)             | 基础工具类和通用功能 | 基础模块 |
| [blen-kernel-dependencies](./blen-kernel-dependencies) | 依赖版本管理     | 父模块  |

### 功能模块

| 模块                                                       | 功能描述      | 依赖关系       |
|----------------------------------------------------------|-----------|------------|
| [blen-kernel-auth](./blen-kernel-auth)                   | JWT 认证和授权 | common     |
| [blen-kernel-validation](./blen-kernel-validation)       | 参数验证和校验   | common     |
| [blen-kernel-web](./blen-kernel-web)                     | Web 应用支持  | common     |
| [blen-kernel-autoconfigure](./blen-kernel-autoconfigure) | 自动配置      | validation |

### 工具模块

| 模块                                             | 功能描述    | 依赖关系   |
|------------------------------------------------|---------|--------|
| [blen-kernel-devtools](./blen-kernel-devtools) | 代码生成工具  | common |
| [blen-kernel-test](./blen-kernel-test)         | 测试工具和注解 | common |
| [blen-kernel-notify](./blen-kernel-notify)     | 消息通知    | common |
| [blen-kernel-spi](./blen-kernel-spi)           | 服务提供者接口 | common |

### 扩展模块

| 模块                                         | 功能描述    | 依赖关系   |
|--------------------------------------------|---------|--------|
| [blen-kernel-tracer](./blen-kernel-tracer) | 链路追踪    | common |
| [blen-kernel-extend](./blen-kernel-extend) | 第三方组件集成 | common |

## 快速开始

### 1. 添加依赖

#### 引入所有模块

```xml
<dependency>
    <groupId>dev.dong4j</groupId>
    <artifactId>blen-kernel</artifactId>
    <version>2.0.0-SNAPSHOT</version>
    <type>pom</type>
</dependency>
```

#### 按需引入模块

```xml
<dependency>
    <groupId>dev.dong4j</groupId>
    <artifactId>blen-kernel-common</artifactId>
    <version>2.0.0-SNAPSHOT</version>
</dependency>

<dependency>
    <groupId>dev.dong4j</groupId>
    <artifactId>blen-kernel-auth</artifactId>
    <version>2.0.0-SNAPSHOT</version>
</dependency>
```

### 2. 基础配置

#### application.yml

```yaml
blen:
  kernel:
    enabled: true
    config:
      validate: true
    cache:
      enabled: true
      size: 1000
```

### 3. 创建应用

#### 主启动类

```java
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

#### 示例控制器

```java
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public Result<User> getUser(@PathVariable Long id) {
        User user = userService.findById(id);
        return R.succeed(user);
    }

    @PostMapping
    public Result<User> createUser(@Valid @RequestBody User user) {
        User createdUser = userService.createUser(user);
        return R.succeed(createdUser);
    }
}
```

## 核心功能

### 1. 统一的 API 响应格式

```java
// 成功响应
Result<User> result = R.succeed(user);

// 失败响应
Result<Void> result = R.failed("操作失败");

// 自定义响应
Result<String> result = R.build(2000, "成功", "数据");
```

### 2. 完善的异常处理

```java
@RestControllerAdvice
public class GlobalExceptionHandler extends ServletGlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public Result<Void> handleBusinessException(BusinessException e) {
        return R.failed(e.getCode(), e.getMessage());
    }
}
```

### 3. 强大的参数验证

```java
public class User {

    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 20, message = "用户名长度必须在3-20个字符之间")
    private String username;

    @Email(message = "邮箱格式不正确")
    private String email;

    @Phone(message = "手机号格式不正确")
    private String phone;
}
```

### 4. JWT 认证支持

```java
@Service
public class AuthService {

    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("user", user);
        claims.put("username", user.getUsername());

        Date expiration = new Date(System.currentTimeMillis() + 3600000);
        return JwtUtils.generateToken(claims, jwtSecret, expiration);
    }
}
```

### 5. 代码生成工具

```java
@Test
public void generateCode() {
    AutoGeneratorCode generator = new AutoGeneratorCode()
        .setAuthor("dong4j")
        .setPackageName("com.example.user")
        .setTables("user", "role")
        .setTemplates("entity", "controller", "service", "impl", "dao", "xml");

    generator.generate();
}
```

## 技术栈

### 核心框架

- **Spring Boot 3.x**: 应用框架
- **Spring Framework 6.x**: 核心框架
- **Jakarta EE 9+**: 企业级规范

### 数据访问

- **MyBatis-Plus**: ORM 框架
- **HikariCP**: 连接池
- **MySQL/PostgreSQL**: 数据库

### 工具库

- **Hutool**: 工具库
- **Jackson**: JSON 处理
- **MapStruct**: 对象映射
- **Lombok**: 代码简化

### 测试框架

- **JUnit 5**: 单元测试
- **Spring Boot Test**: 集成测试
- **Mockito**: Mock 框架
- **JMH**: 性能测试

## 版本兼容性

| Blen Kernel | Spring Boot | Java | 状态  |
|-------------|-------------|------|-----|
| 2.0.x       | 3.x         | 17+  | 开发中 |
| 1.x         | 2.x         | 8+   | 维护中 |

## 贡献指南

我们欢迎所有形式的贡献，包括但不限于：

- 🐛 问题报告
- 💡 功能建议
- 🔧 代码贡献
- 📖 文档改进

### 开发环境

1. 克隆项目

```bash
git clone https://github.com/zeka-stack/blen-kernel.git
cd blen-kernel
```

2. 安装依赖

```bash
mvn clean install
```

3. 运行测试

```bash
mvn test
```

### 提交规范

我们使用 [Conventional Commits](https://www.conventionalcommits.org/) 规范：

- `feat`: 新功能
- `fix`: 问题修复
- `docs`: 文档更新
- `style`: 代码格式
- `refactor`: 代码重构
- `test`: 测试相关
- `chore`: 构建过程或辅助工具的变动

## 许可证

本项目采用 [MIT License](LICENSE) 许可证。

## 联系方式

- 作者: dong4j
- 邮箱: dong4j@gmail.com
- 项目地址: https://github.com/zeka-stack/blen-kernel
- 问题反馈: https://github.com/zeka-stack/blen-kernel/issues

## 致谢

感谢所有为 Blen Kernel 项目做出贡献的开发者们！

---

**Blen Kernel** - 让 Java 开发更简单、更高效！
