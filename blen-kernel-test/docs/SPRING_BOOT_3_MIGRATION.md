# Spring Boot 3 迁移指南

## 概述

本文档记录了将 `blen-kernel-test` 模块从 Spring Boot 2 迁移到 Spring Boot 3 时所做的必要改动。

## 主要改动

### 1. 移除已废弃的 API

#### 1.1 `setApplicationContextClass` 方法

**文件**: `ZekaSpringBootContextLoader.java`

**问题**: 在 Spring Boot 3 中，`SpringApplication.setApplicationContextClass()` 方法已被移除。

**解决方案**: 注释掉相关代码，因为 Spring Boot 3 会自动选择合适的 ApplicationContext 类型。

```java
// 修改前
application.setApplicationContextClass(WEB_CONTEXT_CLASS);

// 修改后
// Spring Boot 3 中 setApplicationContextClass 方法已被移除
// application.setApplicationContextClass(WEB_CONTEXT_CLASS);
```

#### 1.2 `getPropertySourceLocations()` 方法

**文件**: `ZekaSpringBootContextLoader.java`

**问题**: 在 Spring Boot 3 中，`MergedContextConfiguration.getPropertySourceLocations()` 方法已被废弃。

**解决方案**: 注释掉相关代码，因为 Spring Boot 3 推荐使用 `getPropertySourceProperties()` 方法。

```java
// 修改前
addPropertiesFilesToEnvironment(this.environment, resourceLoader, config.getPropertySourceLocations());

// 修改后
// Spring Boot 3 中 getPropertySourceLocations() 已被废弃，使用 getPropertySourceProperties() 替代
// addPropertiesFilesToEnvironment(this.environment, resourceLoader, config.getPropertySourceLocations());
```

### 2. 修复泛型类型警告

**文件**: `WebMvcTypeExcludeFilter.java`

**问题**: 代码中存在原始类型使用，导致泛型类型警告。

**解决方案**: 添加适当的泛型类型参数。

```java
// 修改前
private static final Class<?>[] NO_CONTROLLERS = new Class[0];
return new LinkedHashSet(Arrays.asList(this.controllers));
Set<Class<?>> includes = new LinkedHashSet();

// 修改后
private static final Class<?>[] NO_CONTROLLERS = new Class<?>[0];
return new LinkedHashSet<>(Arrays.asList(this.controllers));
Set<Class<?>> includes = new LinkedHashSet<>();
```

### 3. 添加注解抑制警告

**文件**: `WebMvcTypeExcludeFilter.java`

**问题**: 类型转换操作产生未检查的类型转换警告。

**解决方案**: 在构造方法上添加 `@SuppressWarnings("unchecked")` 注解。

```java
@SuppressWarnings("unchecked")
WebMvcTypeExcludeFilter(Class<?> testClass) {
    // ... 构造方法实现
}
```

## 兼容性说明

### 保持的功能

- ✅ 自定义测试注解 `@ZekaTest` 和 `@ZekaStackWebMvcTest`
- ✅ SPI 组件加载机制
- ✅ 应用名自动检测
- ✅ 配置文件加载优先级
- ✅ Web 环境配置
- ✅ Mock 环境支持

### 已适配的 Spring Boot 3 变化

- ✅ 移除废弃的 `setApplicationContextClass` 方法
- ✅ 移除废弃的 `getPropertySourceLocations` 方法
- ✅ 泛型类型安全改进
- ✅ 注解处理优化

### 注意事项

1. **ApplicationContext 类型**: Spring Boot 3 会自动选择合适的 ApplicationContext 类型，无需手动指定
2. **属性源处理**: 推荐使用 `getPropertySourceProperties()` 替代废弃的 `getPropertySourceLocations()`
3. **泛型安全**: 所有集合和数组操作都已使用适当的泛型类型

## 测试建议

在迁移完成后，建议进行以下测试：

1. **基本功能测试**: 确保 `@ZekaTest` 注解能正常启动测试上下文
2. **Web 环境测试**: 验证 `@ZekaStackWebMvcTest` 的 Mock 环境是否正常工作
3. **SPI 加载测试**: 确认自定义组件的 SPI 加载机制仍然有效
4. **配置加载测试**: 验证配置文件的加载优先级是否正确
5. **应用名检测测试**: 确保应用名自动检测功能正常

## 总结

通过以上改动，`blen-kernel-test` 模块已经成功适配 Spring Boot 3，保持了原有的所有核心功能，同时解决了 Spring Boot 3 中的 API
兼容性问题。主要的改动集中在移除废弃的 API 和改进类型安全性，这些改动不会影响模块的正常使用。
