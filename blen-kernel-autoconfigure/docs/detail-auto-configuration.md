---
published: 2025.01.01
---

# 自动配置

## 概述

`blen-kernel-autoconfigure` 模块基于 Spring Boot 的自动配置机制，提供了框架组件的自动配置功能，简化了框架的配置过程，让开发者能够快速集成和使用框架功能。

## 核心组件

### ZekaProperties

Zeka 组件通用属性配置类，定义了框架组件的通用配置项。

#### 设计原理

`ZekaProperties` 作为 Zeka 框架中所有组件的基础配置类，提供了统一的组件开关控制机制。

#### 配置属性

```java
@Getter
@Setter
public class ZekaProperties {
    /** 组件全局可用状态，默认为开启 */
    private Boolean enabled = true;
}
```

### @ConditionalOnEnabled

模块启用条件注解，用于控制指定模块的启用状态。

#### 使用示例

```java
@Configuration
@ConditionalOnEnabled("blen.kernel.auth")
public class AuthAutoConfiguration {
    // 认证相关配置
}
```

### ConfigFileWatcherAutoConfiguration

配置文件监控自动配置类，负责初始化和配置文件监控系统。

#### 设计原理

该自动配置类基于 Spring Boot 的自动配置机制，为系统提供文件变更监听和动态刷新功能。

#### 核心功能

- 自动初始化配置文件监控器
- 支持多种自定义配置策略
- 提供统一的配置加载和启动机制
- 支持业务端自定义扩展

## 使用示例

### 基础配置

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

### 模块配置

```yaml
blen:
  kernel:
    auth:
      enabled: true
    web:
      enabled: true
      enable-global-cache-filter: true
```

### 自定义自动配置

```java
@AutoConfiguration
@ConditionalOnEnabled("blen.kernel.custom")
@ConditionalOnClass(CustomService.class)
public class CustomAutoConfiguration {
    
    @Bean
    @ConditionalOnMissingBean
    public CustomService customService() {
        return new CustomService();
    }
}
```

## 核心特性

### 1. 零配置启动

提供合理的默认配置，支持开箱即用：

- 默认启用所有功能
- 提供合理的默认值
- 减少配置复杂度

### 2. 条件化配置

基于条件注解，支持环境相关配置：

- `@ConditionalOnClass`: 类存在时启用
- `@ConditionalOnMissingBean`: Bean 不存在时启用
- `@ConditionalOnEnabled`: 模块启用时生效

### 3. 配置验证

自动验证配置参数：

- 集成 `blen-kernel-validation` 模块
- 提供配置错误提示
- 支持配置修复建议

## 最佳实践

### 1. 配置管理

- 使用统一的配置前缀
- 提供清晰的配置文档
- 支持配置的层次结构

### 2. 条件配置

- 合理使用条件注解
- 支持环境相关配置
- 提供配置的开关控制

### 3. 扩展性

- 支持自定义自动配置
- 提供配置的扩展点
- 支持配置的覆盖机制

## 注意事项

1. **配置优先级**: 注意配置的优先级顺序
2. **条件判断**: 确保条件注解的正确使用
3. **性能影响**: 注意自动配置对启动性能的影响
4. **配置验证**: 确保配置参数的正确性

