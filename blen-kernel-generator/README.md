---
published: 2022.06.06
---

# 代码生成器

## 概述

`blen-kernel-generator` 是 Zeka.Stack 框架的开发工具模块，基于 MyBatis-Plus Generator 提供了强大的代码生成功能。该模块可以帮助开发者快速生成标准的
CRUD 代码，包括实体类、Mapper、Service、Controller 等，大大提升开发效率。

## 主要功能

### 1. 代码生成器

- **AutoGeneratorCode**: 自动代码生成器
- 支持多种代码模板
- 支持自定义模板配置
- 支持单模块和多模块项目

### 2. 数据源配置

- **DataSourceConfig**: 数据源配置
- 支持多种数据库类型
- 支持自定义类型转换
- 支持数据库连接池配置

### 3. 模板系统

- **TemplateConfig**: 模板配置
- 支持自定义模板路径
- 支持多种文件类型生成
- 支持模板变量注入

### 4. 策略配置

- **StrategyConfig**: 生成策略配置
- 支持表名策略
- 支持字段策略
- 支持命名策略

### 5. 包配置

- **PackageConfig**: 包结构配置
- 支持自定义包名
- 支持模块化包结构
- 支持多级包结构

## 核心特性

### 1. 丰富的代码模板

- **Entity**: 实体类模板
- **Controller**: 控制器模板
- **Service**: 服务接口模板
- **ServiceImpl**: 服务实现模板
- **Mapper**: 数据访问层模板
- **XML**: MyBatis XML 模板
- **DTO**: 数据传输对象模板
- **VO**: 视图对象模板
- **Form**: 表单对象模板
- **Query**: 查询对象模板
- **Converter**: 转换器模板
- **Enum**: 枚举类模板

### 2. 灵活的配置选项

- 支持自定义作者信息
- 支持自定义公司信息
- 支持自定义版本信息
- 支持自定义文件覆盖策略

### 3. 多数据库支持

- MySQL
- PostgreSQL
- Oracle
- SQL Server
- 其他数据库类型

### 4. 模块化支持

- 单模块项目
- 多模块项目
- 微服务项目

## 依赖关系

### 核心依赖

- **blen-kernel-common**: 基础功能依赖
- **com.baomidou:mybatis-plus-generator**: MyBatis-Plus 代码生成器
- **org.apache.velocity:velocity-engine-core**: Velocity 模板引擎
- **mysql:mysql-connector-j**: MySQL 驱动
- **spring-core**: Spring 核心支持
- **spring-boot**: Spring Boot 支持

## 使用方式

### 1. 添加依赖

```xml
<dependency>
    <groupId>dev.dong4j</groupId>
    <artifactId>blen-kernel-generator</artifactId>
    <version>${blen-kernel.version}</version>
    <scope>test</scope>
</dependency>
```

### 2. 基础代码生成

```java
@Test
public void generateCode() {
    AutoGeneratorCode generator = new AutoGeneratorCode()
        .setAuthor("dong4j")
        .setVersion("1.0.0")
        .setCompany("Zeka.Stack")
        .setEmail("dong4j@gmail.com")
        .setModelPath("/path/to/project")
        .setPackageName("com.example.user")
        .setDriverName("com.mysql.cj.jdbc.Driver")
        .setUserName("root")
        .setPassWord("password")
        .setUrl("jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=utf8&serverTimezone=GMT%2B8")
        .setTables("user", "role", "permission")
        .setPrefix("t_")
        .setTemplates("entity", "controller", "service", "impl", "dao", "xml");

    generator.generate();
}
```

### 3. 高级配置

```java
@Test
public void generateCodeWithAdvancedConfig() {
    AutoGeneratorCode generator = new AutoGeneratorCode()
        .setAuthor("dong4j")
        .setVersion("1.0.0")
        .setCompany("Zeka.Stack")
        .setEmail("dong4j@gmail.com")
        .setModelPath("/path/to/project")
        .setPackageName("com.example.user")
        .setDriverName("com.mysql.cj.jdbc.Driver")
        .setUserName("root")
        .setPassWord("password")
        .setUrl("jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=utf8&serverTimezone=GMT%2B8")
        .setTables("user", "role", "permission")
        .setPrefix("t_")
        .setTemplates("entity", "controller", "service", "impl", "dao", "xml", "dto", "vo", "form", "query")
        .setSuperBaseEntityClass("com.example.common.BaseEntity")
        .setSuperMapperClass("com.example.common.BaseMapper")
        .setSuperServiceClass("com.example.common.BaseService")
        .setSuperServiceImplClass("com.example.common.BaseServiceImpl")
        .setSuperControllerClass("com.example.common.BaseController")
        .setWebapp(true)
        .setModuleType(ModuleConfig.ModuleType.SINGLE_MODULE);

    generator.generate();
}
```

### 4. 多模块项目生成

```java
@Test
public void generateMultiModuleCode() {
    AutoGeneratorCode generator = new AutoGeneratorCode()
        .setAuthor("dong4j")
        .setVersion("1.0.0")
        .setCompany("Zeka.Stack")
        .setEmail("dong4j@gmail.com")
        .setModelPath("/path/to/project")
        .setPackageName("com.example.user")
        .setDriverName("com.mysql.cj.jdbc.Driver")
        .setUserName("root")
        .setPassWord("password")
        .setUrl("jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=utf8&serverTimezone=GMT%2B8")
        .setTables("user", "role", "permission")
        .setPrefix("t_")
        .setTemplates("entity", "controller", "service", "impl", "dao", "xml", "dto", "vo", "form", "query", "converter")
        .setModuleType(ModuleConfig.ModuleType.MULTI_MODULE)
        .setComponets("mybatis", "redis", "dubbo");

    generator.generate();
}
```

### 5. 自定义模板生成

```java
@Test
public void generateWithCustomTemplates() {
    AutoGeneratorCode generator = new AutoGeneratorCode()
        .setAuthor("dong4j")
        .setVersion("1.0.0")
        .setCompany("Zeka.Stack")
        .setEmail("dong4j@gmail.com")
        .setModelPath("/path/to/project")
        .setPackageName("com.example.user")
        .setDriverName("com.mysql.cj.jdbc.Driver")
        .setUserName("root")
        .setPassWord("password")
        .setUrl("jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=utf8&serverTimezone=GMT%2B8")
        .setTables("user")
        .setPrefix("t_")
        .setTemplates("entity", "controller", "service", "impl", "dao", "xml");

    // 自定义模板配置
    TemplatesConfig templatesConfig = new TemplatesConfig()
        .setController("/templates/custom-controller.java.vm")
        .setService("/templates/custom-service.java.vm")
        .setEntity("/templates/custom-entity.java.vm");

    generator.setTemplatesConfig(templatesConfig);
    generator.generate();
}
```

## 配置说明

### 数据库配置

```properties
# 数据库连接配置
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.url=jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=utf8&serverTimezone=GMT%2B8
spring.datasource.username=root
spring.datasource.password=password
```

### 代码生成配置

```properties
# 代码生成配置
blen.devtools.author=dong4j
blen.devtools.version=1.0.0
blen.devtools.company=Zeka.Stack
blen.devtools.email=dong4j@gmail.com
blen.devtools.package-name=com.example.user
blen.devtools.table-prefix=t_
blen.devtools.templates=entity,controller,service,impl,dao,xml
```

## 模板说明

### 1. 实体类模板 (entity.java.vm)

- 生成标准的实体类
- 支持 Lombok 注解
- 支持 MyBatis-Plus 注解
- 支持自定义字段映射

### 2. 控制器模板 (controller-ext.java.vm)

- 生成 RESTful 风格的控制器
- 支持标准的 CRUD 操作
- 支持分页查询
- 支持条件查询

### 3. 服务接口模板 (service.java.vm)

- 生成服务接口
- 支持继承基础服务接口
- 支持自定义方法

### 4. 服务实现模板 (serviceImpl.java.vm)

- 生成服务实现类
- 支持继承基础服务实现
- 支持自定义业务逻辑

### 5. 数据访问层模板 (mapper.java.vm)

- 生成 Mapper 接口
- 支持继承基础 Mapper
- 支持自定义查询方法

### 6. XML 模板 (mapper.xml.vm)

- 生成 MyBatis XML 文件
- 支持基础 CRUD 操作
- 支持自定义 SQL 语句

## 高级用法

### 1. 自定义类型转换

```java
public class CustomTypeConvert extends MySqlTypeConvert {

    @Override
    public IColumnType processTypeConvert(GlobalConfig globalConfig, String fieldType) {
        String t = fieldType.toLowerCase();

        // 自定义类型转换
        if (t.contains("tinyint")) {
            return DbColumnType.INTEGER;
        }

        if (t.contains("json")) {
            return DbColumnType.STRING;
        }

        return super.processTypeConvert(globalConfig, fieldType);
    }
}
```

### 2. 自定义模板变量

```java
public class CustomInjectionConfig extends InjectionConfig {

    @Override
    public void initMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("customField", "customValue");
        map.put("apiPrefix", "/api/v1");
        map.put("swaggerEnabled", true);
        this.setMap(map);
    }
}
```

### 3. 自定义文件输出配置

```java
public class CustomFileOutConfig extends FileOutConfig {

    public CustomFileOutConfig(String templatePath) {
        super(templatePath);
    }

    @Override
    public String outputFile(TableInfo tableInfo) {
        // 自定义文件输出路径
        String entityName = tableInfo.getEntityName();
        return "/custom/path/" + entityName + "Custom.java";
    }
}
```

## 最佳实践

### 1. 项目结构

```
project/
├── src/main/java/
│   └── com/example/user/
│       ├── entity/
│       │   ├── po/          # 持久化对象
│       │   ├── dto/         # 数据传输对象
│       │   ├── vo/          # 视图对象
│       │   ├── form/        # 表单对象
│       │   └── converter/   # 转换器
│       ├── dao/             # 数据访问层
│       ├── service/         # 服务层
│       ├── controller/      # 控制器层
│       └── enums/           # 枚举类
└── src/main/resources/
    └── mappers/             # MyBatis XML 文件
```

### 2. 命名规范

- 实体类: `User` (驼峰命名)
- 控制器: `UserController` (驼峰命名 + Controller)
- 服务接口: `UserService` (驼峰命名 + Service)
- 服务实现: `UserServiceImpl` (驼峰命名 + ServiceImpl)
- Mapper 接口: `UserMapper` (驼峰命名 + Mapper)

### 3. 代码生成策略

- 使用表前缀避免命名冲突
- 合理设置包结构
- 选择合适的模板组合
- 定期更新模板内容

## 注意事项

为了完成枚举的生成和自动替换, 需要按照一定的规范编写 SQL 脚本.

```
create table rule
(
    `id`            BIGINT AUTO_INCREMENT PRIMARY KEY,
    `ip_from`       BIGINT       NULL COMMENT 'IP范围开始地址',
    `ip_to`         BIGINT       NULL COMMENT 'IP范围结束地址',
    `match_mode`    VARCHAR(20)  NULL COMMENT '域名匹配模式',
    `name`          VARCHAR(100) NULL COMMENT '匹配的域名',
    `priority`      INT          NULL COMMENT '匹配优先级',
    `enabled`       BIT                   DEFAULT b'1' NULL COMMENT '公共枚举:EnableEnum:可用状态',
    `dispatch_mode` VARCHAR(20)  NULL COMMENT '分发模式, 如iphash、round-robin、random',
    `create_time`   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间 (公共字段)',
    `update_time`   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间 (公共字段)',
    `deleted`       BIGINT       NOT NULL DEFAULT 0 COMMENT '通用枚举:删除状态:0: 未删除，删除就是当前数据的主键id用于代表唯一性'
) ENGINE = INNODB
  AUTO_INCREMENT = 0
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '解析规则';

create table user
(
    `id`           BIGINT UNSIGNED AUTO_INCREMENT COMMENT '自增主键' primary key,
    `phone`        VARCHAR(11)  not NULL COMMENT '用户电话号码',
    `user_name`    VARCHAR(64)  not NULL COMMENT '用户名',
    `gender`       TINYINT(4)   not NULL COMMENT '性别',
    `nick_name`    VARCHAR(64)  not NULL COMMENT '姓名',
    `company_name` VARCHAR(64)  not NULL COMMENT '公司名称',
    `password`     VARCHAR(128) not NULL COMMENT '密码',
    `salt`         VARCHAR(128) not NULL COMMENT '密码加密盐值',
    `api_key`      VARCHAR(50)  NULL COMMENT 'open api key',
    `secret_key`   VARCHAR(50)  NULL COMMENT 'open api secret key',
    `email`        VARCHAR(128) NULL COMMENT '用户邮箱',
    `status`       TINYINT(2) UNSIGNED   DEFAULT 0 NOT NULL COMMENT '自定义枚举:Integer:用户状态:AAA(0, "未审核"),:BBB(1, "审核中"),:CCC(2, "审核未通过"),:DDD(3,"已锁定"),:EEE(4,"正常");',
    `create_time`  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间 (公共字段)',
    `update_time`  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间 (公共字段)',
    `deleted`      BIGINT       NOT NULL DEFAULT 0 COMMENT '通用枚举:删除状态:0: 未删除，删除就是当前数据的主键id用于代表唯一性'
) ENGINE = INNODB
  AUTO_INCREMENT = 0
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '用户信息表';
```

以上是一个标注的 SQL 写法, 需要关注 rule.enabled 字段和 user.status 字段
我们将枚举分为 3 类:

1. 自定义枚举: 业务上需要自定义的状态映射为此枚举;
2. 公共枚举: 框架层已提供的状态枚举, 此类枚举与业务无关, 能够 100% 确认枚举值, 比如 EnableEnum;
3. 通用枚举: SQL 中一般会存在的状态, 比如表示数据是否被删除的 DeleteEnum, 此类枚举一般存在与父类实体中, 子类不需要重复定义;

规则:

1. 一般表示可用/不可用的状态可以直接使用框架提供的 EnableEnum 枚举, 注释的写法为固定格式: 公共枚举:EnableEnum:可用状态
2. 如果业务上存在多个状态的属性(即使现在没有多个, 也应该预计将来会不会存在多个的情况), 应该使用 TINYINT(2) 类型 (128 个状态值应该够用了吧,
   如果超过 128 个状态那一定是产品的问题);
3. 对于自定义枚举的注释需要按照固定格式编写: 自定义枚举:{value 类型}:{枚举注释}:枚举名1(value1, "描述1"),:枚举名2(value2, "描述2");
   3.1 「自定义枚举」 是一个固定值, 代码生成时需要通过这个固定值去匹配生成逻辑;
   3.2 「value 类型」需要使用 Java 中的类名, 比如 Integer , Long , String 等;
   3.3 枚举名(枚举 value, "枚举描述") 这个格式是固定的, 不能更改, 需要注意最后一个枚举是分号;

## 版本历史

- **1.0.0**: 初始版本，基础代码生成功能
- **1.2.3**: 增加多模块支持
- **1.3.0**: 优化模板系统

## 贡献指南

欢迎提交 Issue 和 Pull Request 来改进这个模块。

## 许可证

MIT License
