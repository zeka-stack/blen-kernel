---
title: ZekaTest 注解说明
published: 2025.12.14
---

# 背景

`@ZekaTest` 旨在替代 `@SpringBootTest`，为 Zeka 框架的测试场景提供统一入口和约定。框架通过 SPI (`LauncherInitiation`)
自动加载各模块的默认配置，为了在单元测试中同样自动生效，需要重写测试引导逻辑并适配 Spring Boot 3。改造目标：

- 启动时自动加载 SPI 默认配置（已在 `ZekaSpringBootContextLoader#setUpTestClass` 通过 `ServiceLoader` 执行）。
- 兼容 Spring Boot 3 的上下文引导、AOT 处理、Web 环境选择。
- 简化测试类配置，统一应用名、Profiles、命令行参数、端口策略等。

# 使用方式

在测试类上直接标注 `@ZekaTest`：

```java
@ZekaTest(
    appName = "order-service",
    properties = {
        "spring.profiles.active=ut",
        "feature.toggle.demo=true"
    },
    args = {"--debug"},
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
class OrderServiceTest {
    @Autowired OrderClient orderClient;
}
```

要点：

- `properties`/`value`: 内联属性，优先级高于默认配置。
- `args`: 传递给 SpringApplication 的启动参数。
- `classes`: 显式指定启动类（当自动扫描不到 `@SpringBootConfiguration` 时必须提供）。
- `appName`: 覆盖自动推断的应用名（默认使用模块目录名）。
- `profiles`: 等价于 `@ActiveProfiles`。
- `webEnvironment`: 控制 Web 环境类型（MOCK、RANDOM_PORT、DEFINED_PORT、NONE）。
- `enableLoader`: 是否启用 SPI Loader（保持默认 true）。

# 实现总览

`@ZekaTest` 关键元件：

- 注解本身：位于 `dev.dong4j.zeka.kernel.test.ZekaTest`，继承 `@BootstrapWith(ZekaStackTestContextBootstrapper.class)`，
  `@ExtendWith(StarterExtension.class)`，`@ActiveProfiles`。
- Bootstrapper：`ZekaStackTestContextBootstrapper` 解析 `@ZekaTest` 元数据（properties/appName/classes/webEnvironment），校验配置，回落到
  `@SpringBootConfiguration` 自动发现。
- ContextLoader：`ZekaSpringBootContextLoader` 基于 Spring Boot 3 的 `SpringBootContextLoader` 语义重写，统一环境构建、AOT 流程、默认属性注入。
- Property 工具：`ZekaTestPropertySourceUtils` 负责将 SPI 默认配置以最低优先级加入 Environment。

# 核心流程

## 1) 元数据解析

- `ZekaStackTestContextBootstrapper#getProperties`：读取 `@ZekaTest.properties/value`。
- `getApplicationName`：`appName` 为空时取模块目录名，写入 `spring.application.name` 与包名。
- `getWebEnvironment`：优先取 `@ZekaTest.webEnvironment`，再回落到 `@SpringBootTest`。
- `getClasses`：优先 `@ZekaTest.classes`，否则扫描 `@SpringBootConfiguration`；未找到则抛出引导错误。
- `verifyConfiguration`：在非嵌入式端口模式下禁止叠加 `@WebAppConfiguration`，避免配置冲突。

## 2) 环境与属性注入

- `ZekaSpringBootContextLoader#getEnvironment`：使用 `DefaultEnvironment`，并通过 `applyZekaDefaults` 注入 SPI 加载的默认属性（
  `LauncherInitiation`）。
- 默认属性来源：`setUpTestClass` 设置版本、标识位、`spring.main.allow-bean-definition-overriding=true` 等；添加命令行 PropertySource，放置在最低优先级。
- TestPropertySource 处理：保留 `spring.jmx.enabled=false` 内联属性，应用测试类 `properties/value` 和 `propertySourceDescriptors`。
- Profiles：`setActiveProfiles` 在环境与应用参数上同步 `spring.profiles.active`。

## 3) Web 环境与端口策略

- `webEnvironment` 判定：`isEmbeddedWebEnvironment` 基于注解元数据决定是否启动内嵌容器。
- 非嵌入式回退：`applyNonEmbeddedServerPortFallback` 通过遍历 PropertySource 判断是否已有 `server.port`，若缺失才注入 `server.port=-1`，避免解析
  `${range.random.int(...)}` 失败。
- ServletContext 初始化：`WebConfigurer` 使用 Boot 3 的防御式 `ServletContextApplicationContextInitializer`，避免 root context 重复导致
  “multiple ServletContextInitializers” 报错。

## 4) AOT 支持

- `loadContextForAotProcessing`/`loadContextForAotRuntime`：遵循 Spring Boot 3 的 `SpringApplication.withHook`/`SpringApplicationHook` 模式。
- `RuntimeHints`：若使用 `useMainMethod` 启动，注册 main 方法反射调用 hint。
- AOT Runtime：在 Hook 的 `starting` 阶段将 AOT initializer 加入 `SpringApplication`。

## 5) 主方法与启动方式

- `UseMainMethod` 支持：`getMainMethod` 在 `@SpringBootConfiguration` 类或其 Kotlin 同名 `Kt` 类上查找 main。
- 优先使用 main 启动（当配置允许），否则直接 `SpringApplication.run`，统一复用 `configure` 配置路径。

## 6) SPI 扩展点

- `LauncherInitiation`：通过 `ServiceLoader` 发现并按照 `getOrder()` 排序执行，向 Environment/Properties 注入模块级默认配置。
- 扩展默认属性的 PropertySource 名称：`DefaultEnvironment.DEFAULT_PROPERTIES_PROPERTY_SOURCE_NAME`，优先级最低，保证用户/测试属性可以覆盖。

# 关键类与位置

- `dev.dong4j.zeka.kernel.test.ZekaTest`
- `dev.dong4j.zeka.kernel.test.ZekaStackTestContextBootstrapper`
- `dev.dong4j.zeka.kernel.test.ZekaSpringBootContextLoader`
- `dev.dong4j.zeka.kernel.test.ZekaTestPropertySourceUtils`
- `dev.dong4j.zeka.kernel.test.WebMvcTypeExcludeFilter`（`@ZekaStackWebMvcTest` 过滤器）

# 实践建议

- **必填 classes 场景**：若启动类未标注 `@SpringBootApplication/@SpringBootConfiguration` 或包结构无法被扫描，必须在 `@ZekaTest(classes=...)`
  指定。
- **非嵌入式测试**：如果需要外部容器或无容器场景，让 `webEnvironment=NONE`，框架会自动注入 `server.port=-1`，并检测已有端口占位符，避免解析冲突。
- **AOT/Native**：开启 `useMainMethod=ALWAYS` 时，确保 main 方法存在；AOT 处理会自动注册反射 hints。
- **自定义属性优先级**：测试类 `properties/value` > JVM/System > SPI 默认属性。需要覆盖默认值时，直接在注解里声明同名属性即可。

# 结论

`@ZekaTest` 在 Spring Boot 3 下已完成全链路适配：从注解元数据解析、环境与属性注入、Web 环境与端口回退、AOT 支持到 SPI 默认配置加载，都与官方新版加载器对齐，同时保留
Zeka 框架的统一约定与扩展能力。
