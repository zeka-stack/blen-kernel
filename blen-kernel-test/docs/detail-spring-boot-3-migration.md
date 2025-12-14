---
published: 2025.12.14
---

# Spring Boot 3 迁移指南

## 概述

本文档记录了将 `blen-kernel-test` 模块从 Spring Boot 2 迁移到 Spring Boot 3 的核心改动与最新修复，重点覆盖自定义测试加载器的重写、环境初始化、AOT
支持以及占位符兼容性问题。

## 主要改动

### 1. 自定义 SpringBootContextLoader 全量重写

**文件**: `ZekaSpringBootContextLoader.java`

**变化亮点**:

- 基于 Spring Boot 3 官方 `SpringBootContextLoader` 重新实现，兼容 AOT（`AotContextLoader`）流程。
- 通过 `ZekaSpringBootAttributes` 解析 `@ZekaTest` 与 `@SpringBootTest`，统一获取 `args`、`useMainMethod`、`webEnvironment`。
- 使用 Spring Boot 3 的 `SpringApplication.withHook`/`SpringApplicationHook` 机制捕获上下文，支持 AOT 运行与处理。
- 注入 Zeka 默认环境：`DefaultEnvironment` + SPI (`LauncherInitiation`) 加载的默认属性，仍保持最低优先级。
- 对非嵌入式 Web 场景新增 `server.port=-1` 回退，但通过遍历 PropertySource 检查是否已声明端口，避免解析占位符时抛出异常（修复
  `${range.random.int(...)}` 无法解析的问题）。
- Web 环境使用 Boot 3 的防御式 `ServletContextApplicationContextInitializer` 包装，避免 root context 重复注册。

### 2. 属性源与占位符处理

**问题**: 非嵌入式 Web 模式下，若存在 `${range.random.int(...)}` 这类未解析占位符，直接调用 `getProperty("server.port")` 会触发解析失败。

**解决方案**:

- 不再直接解析 `server.port`，改为遍历 PropertySource 检查是否已有 `server.port`，仅在缺失时注入 `server.port=-1`。
- 继续保留 `spring.jmx.enabled=false` 及测试内联属性的默认注入顺序。

### 3. AOT 支持

**变化**:

- `loadContextForAotProcessing` 与 `loadContextForAotRuntime` 全量适配 Spring Boot 3 的 Hook/AOT initializer 机制。
- 通过 `RuntimeHints` 注册 main 方法反射访问（当 `useMainMethod` 触发 main 启动时）。

### 4. WebMvcTypeExcludeFilter 更新

**文件**: `WebMvcTypeExcludeFilter.java`

**变化**:

- 泛型与常量声明统一使用参数化类型，解决原始类型警告。
- 常量行分割（`OPTIONAL_INCLUDES`）以满足编码规范。

### 5. 废弃 API 清理与行为调整（沿用但描述更新）

- Spring Boot 3 自动选择 ApplicationContext 类型，移除 `setApplicationContextClass`。
- 使用 `getPropertySourceProperties()` 取代废弃的 `getPropertySourceLocations()`。
- 默认 JMX 关闭、Profile 激活逻辑与测试属性注入顺序保持一致。

## 兼容性说明

### 保持的功能

- ✅ 自定义测试注解 `@ZekaTest` 和 `@ZekaStackWebMvcTest`
- ✅ SPI 组件加载机制
- ✅ 应用名自动检测
- ✅ 配置文件加载优先级
- ✅ Web 环境配置
- ✅ Mock 环境支持
- ✅ AOT 处理与运行支持
- ✅ 非嵌入式 Web 场景的端口回退（避免占位符解析失败）

### 已适配的 Spring Boot 3 变化

- ✅ 移除废弃的 `setApplicationContextClass` 方法
- ✅ 移除废弃的 `getPropertySourceLocations` 方法
- ✅ 泛型类型安全改进 & 注解处理优化
- ✅ SpringApplication Hook + AOT initializer 适配
- ✅ ServletContext 初始化防重复（避免 “root application context present” 报错）
- ✅ 非嵌入式 server.port 回退逻辑兼容占位符

### 注意事项

1. **ApplicationContext 类型**: Spring Boot 3 会自动选择合适的 ApplicationContext 类型，无需手动指定
2. **属性源处理**: 推荐使用 `getPropertySourceProperties()` 替代废弃的 `getPropertySourceLocations()`
3. **占位符与端口**: 非嵌入式 Web 环境如果未声明 `server.port`，框架会注入 `server.port=-1`，但仅在 PropertySource 未声明端口时生效，避免占位符解析失败
4. **AOT**: 若启用 `useMainMethod`，会为 main 方法注册运行时反射 hints

## 测试建议

在迁移完成后，建议进行以下测试：

1. **基本功能测试**: 确保 `@ZekaTest` 注解能正常启动测试上下文
2. **Web 环境测试**: 验证 `@ZekaStackWebMvcTest` 的 Mock 环境是否正常工作
3. **SPI 加载测试**: 确认自定义组件的 SPI 加载机制仍然有效
4. **配置加载测试**: 验证配置文件的加载优先级是否正确
5. **应用名检测测试**: 确保应用名自动检测功能正常
6. **占位符兼容测试**: 在非嵌入式 Web 模式下，包含 `${range.random.int(...)}` 等占位符时，应能正常解析或回退到 `server.port=-1`
7. **AOT 启动测试**: 验证 `loadContextForAotProcessing` / `loadContextForAotRuntime` 在自定义注解下的启动路径

## 总结

通过以上改动，`blen-kernel-test` 模块已经适配 Spring Boot 3，完成了测试上下文加载器的重写、AOT 支持、默认环境注入与占位符兼容性的修复，保持了原有的核心功能与自定义注解体验。
