# Blen Kernel AutoConfigure

## 概述

`blen-kernel-autoconfigure` 是 Zeka.Stack 框架的自动配置模块，基于 Spring Boot 的自动配置机制，提供了框架组件的自动配置功能。该模块简化了框架的配置过程，让开发者能够快速集成和使用框架功能。

## 主要功能

### 1. 自动配置支持

- 基于 Spring Boot 自动配置机制
- 支持条件化配置
- 支持配置属性绑定

### 2. 配置验证

- 集成 `blen-kernel-validation` 模块
- 支持配置参数验证
- 提供配置错误提示

### 3. 配置管理

- 支持外部化配置
- 支持配置热更新
- 支持配置优先级管理

## 核心特性

### 1. 零配置启动

- 提供合理的默认配置
- 支持开箱即用
- 减少配置复杂度

### 2. 条件化配置

- 基于条件注解
- 支持环境相关配置
- 支持依赖相关配置

### 3. 配置验证

- 自动验证配置参数
- 提供配置错误提示
- 支持配置修复建议

## 依赖关系

### 核心依赖

- **blen-kernel-validation**: 验证功能依赖
- **spring-boot-autoconfigure**: Spring Boot 自动配置

## 使用方式

### 1. 添加依赖

```xml
<dependency>
    <groupId>dev.dong4j</groupId>
    <artifactId>blen-kernel-autoconfigure</artifactId>
    <version>${blen-kernel.version}</version>
</dependency>
```

### 2. 自动配置

```java
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

### 3. 配置属性

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

## 配置说明

### 基础配置

```yaml
blen:
  kernel:
    # 是否启用框架
    enabled: true
    # 配置验证
    config:
      validate: true
    # 缓存配置
    cache:
      enabled: true
      size: 1000
      ttl: 3600
```

### 环境配置

```yaml
spring:
  profiles:
    active: dev
  config:
    activate:
      on-profile: dev
```

## 版本历史

- **1.0.0**: 初始版本，基础自动配置功能

## 贡献指南

欢迎提交 Issue 和 Pull Request 来改进这个模块。

## 许可证

MIT License
