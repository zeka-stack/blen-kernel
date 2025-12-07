---
published: 2025.01.01
---

# 代码生成器

## 概述

`blen-kernel-generator` 模块提供了强大的代码生成功能，基于 MyBatis-Plus Generator，可以帮助开发者快速生成标准的 CRUD
代码，包括实体类、Mapper、Service、Controller 等。

## 核心组件

### AutoGeneratorCode

自动代码生成器核心配置类，提供 MyBatis-Plus 代码生成的完整配置和执行功能。

#### 设计原理

`AutoGeneratorCode` 封装了代码生成器的所有配置项，包括：

1. **数据源配置**: 数据库连接信息
2. **全局配置**: 作者、输出路径等
3. **策略配置**: 表名策略、字段策略等
4. **包配置**: 包结构配置
5. **模板配置**: 代码模板配置

#### 使用示例

```java
AutoGeneratorCode generator = new AutoGeneratorCode()
    .setModelPath("/path/to/project")
    .setAuthor("dong4j")
    .setPackageName("com.example.user")
    .setTables("user", "role")
    .setTemplates("entity", "controller", "service", "impl", "dao", "xml");

generator.generate();
```

### AutoGeneratorCodeBuilder

自动代码生成器构建者类，提供流式的 API 来配置和构建代码生成器。

#### 使用示例

```java
AutoGeneratorCodeBuilder.onAutoGeneratorCode()
    .withModelPath("/path/to/project")
    .withAuthor("developer")
    .withTables(new String[]{"user", "role"})
    .build();
```

## 核心功能

### 数据源配置

```java
DataSourceConfig dataSource = new DataSourceConfig();
dataSource.setUrl("jdbc:mysql://localhost:3306/test");
dataSource.setDriverName("com.mysql.cj.jdbc.Driver");
dataSource.setUsername("root");
dataSource.setPassword("password");
```

### 全局配置

```java
GlobalConfig globalConfig = new GlobalConfig();
globalConfig.setAuthor("dong4j");
globalConfig.setOutputDir("/path/to/output");
globalConfig.setOpen(false);
globalConfig.setFileOverride(true);
```

### 策略配置

```java
StrategyConfig strategy = new StrategyConfig();
strategy.setNaming(NamingStrategy.underline_to_camel);
strategy.setColumnNaming(NamingStrategy.underline_to_camel);
strategy.setEntityLombokModel(true);
strategy.setRestControllerStyle(true);
```

### 包配置

```java
PackageConfig packageConfig = new PackageConfig();
packageConfig.setParent("com.example");
packageConfig.setEntity("entity");
packageConfig.setMapper("mapper");
packageConfig.setService("service");
packageConfig.setServiceImpl("service.impl");
packageConfig.setController("controller");
```

### 模板配置

```java
TemplateConfig templateConfig = new TemplateConfig();
templateConfig.setEntity("/templates/entity.java.vm");
templateConfig.setController("/templates/controller.java.vm");
templateConfig.setService("/templates/service.java.vm");
templateConfig.setServiceImpl("/templates/serviceImpl.java.vm");
templateConfig.setMapper("/templates/mapper.java.vm");
templateConfig.setXml("/templates/mapper.xml.vm");
```

## 支持的模板

### 实体类模板

生成实体类，包含字段、注解等：

```java
@TableName("user")
@Data
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String username;
    private String email;
}
```

### Controller 模板

生成 REST 控制器：

```java
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public Result<User> getUser(@PathVariable Long id) {
        return R.succeed(userService.findById(id));
    }
}
```

### Service 模板

生成服务接口和实现类：

```java
public interface UserService {
    User findById(Long id);
    User createUser(User user);
    void deleteUser(Long id);
}
```

### Mapper 模板

生成数据访问层接口和 XML：

```java
@Mapper
public interface UserMapper extends BaseMapper<User> {
    // 自定义方法
}
```

## 使用示例

### 基础使用

```java
@Test
public void generateCode() {
    AutoGeneratorCode generator = new AutoGeneratorCode()
        .setModelPath("/path/to/project")
        .setAuthor("dong4j")
        .setPackageName("com.example.user")
        .setTables("user", "role")
        .setTemplates("entity", "controller", "service", "impl", "dao", "xml");

    generator.generate();
}
```

### 高级配置

```java
@Test
public void generateCodeWithCustomConfig() {
    AutoGeneratorCode generator = new AutoGeneratorCode();

    // 数据源配置
    generator.setDataSourceUrl("jdbc:mysql://localhost:3306/test");
    generator.setDataSourceUsername("root");
    generator.setDataSourcePassword("password");

    // 全局配置
    generator.setAuthor("dong4j");
    generator.setModelPath("/path/to/project");
    generator.setFileOverride(true);

    // 策略配置
    generator.setEntityLombokModel(true);
    generator.setRestControllerStyle(true);
    generator.setNaming(NamingStrategy.underline_to_camel);

    // 包配置
    generator.setPackageName("com.example.user");

    // 表配置
    generator.setTables("user", "role");

    // 模板配置
    generator.setTemplates("entity", "controller", "service", "impl", "dao", "xml");

    generator.generate();
}
```

## 最佳实践

### 1. 模板定制

- 根据项目需求定制代码模板
- 保持模板的一致性
- 使用变量注入提高模板灵活性

### 2. 命名规范

- 使用统一的命名策略
- 遵循 Java 命名规范
- 保持代码风格一致

### 3. 代码质量

- 生成代码后进行检查和优化
- 添加必要的注释
- 遵循最佳实践

## 注意事项

1. **文件覆盖**: 注意 `fileOverride` 配置，避免覆盖已有代码
2. **模板路径**: 确保模板路径正确
3. **数据库连接**: 确保数据库连接信息正确
4. **表结构**: 确保表结构完整，包含必要的字段

