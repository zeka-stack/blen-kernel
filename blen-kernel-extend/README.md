# Blen Kernel Extend

## 概述

`blen-kernel-extend` 是 Zeka.Stack 框架的扩展模块，提供了框架的扩展功能和第三方组件集成。该模块集成了 Sentinel 流量控制、Nacos
配置管理等组件，为框架提供更强大的功能支持。

## 主要功能

### 1. 流量控制

- 集成 Sentinel 流量控制
- 支持熔断降级
- 支持流量整形

### 2. 配置管理

- 集成 Nacos 配置管理
- 支持动态配置更新
- 支持配置中心集成

### 3. 服务治理

- 支持服务发现
- 支持负载均衡
- 支持服务监控

## 核心特性

### 1. Sentinel 集成

- 基于 Sentinel 1.8+ 版本
- 支持多种流量控制策略
- 支持熔断降级规则

### 2. Nacos 集成

- 支持配置中心
- 支持服务注册发现
- 支持动态配置

### 3. 扩展性

- 支持自定义扩展
- 支持插件机制
- 支持模块化设计

## 依赖关系

### 核心依赖

- **com.alibaba.csp:sentinel-core**: Sentinel 核心
- **com.alibaba.csp:sentinel-api**: Sentinel API
- **com.alibaba.csp:sentinel-datasource-nacos**: Sentinel Nacos 数据源
- **spring-context**: Spring 上下文支持
- **spring-boot**: Spring Boot 支持

## 使用方式

### 1. 添加依赖

```xml
<dependency>
    <groupId>dev.dong4j</groupId>
    <artifactId>blen-kernel-extend</artifactId>
    <version>${blen-kernel.version}</version>
</dependency>
```

### 2. Sentinel 流量控制

```java
@Service
public class UserService {

    @SentinelResource(value = "getUser", fallback = "getUserFallback")
    public User getUser(Long id) {
        return userRepository.findById(id);
    }

    public User getUserFallback(Long id, Throwable ex) {
        log.error("获取用户失败: {}", ex.getMessage());
        return new User();
    }
}
```

### 3. Nacos 配置管理

```java
@Configuration
@EnableNacosConfig
public class NacosConfig {

    @NacosValue("${user.name:default}")
    private String userName;

    @NacosValue("${user.age:18}")
    private Integer userAge;
}
```

### 4. 自定义扩展

```java
@Component
public class CustomExtension {

    @PostConstruct
    public void init() {
        // 自定义扩展初始化
        log.info("自定义扩展初始化完成");
    }
}
```

## 配置说明

### Sentinel 配置

```yaml
spring:
  cloud:
    sentinel:
      enabled: true
      transport:
        dashboard: localhost:8080
        port: 8719
      datasource:
        ds1:
          nacos:
            server-addr: localhost:8848
            dataId: sentinel-rules
            groupId: SENTINEL_GROUP
            rule-type: flow
```

### Nacos 配置

```yaml
spring:
  cloud:
    nacos:
      config:
        server-addr: localhost:8848
        file-extension: yaml
        namespace: dev
        group: DEFAULT_GROUP
      discovery:
        server-addr: localhost:8848
        namespace: dev
        group: DEFAULT_GROUP
```

## 高级用法

### 1. 自定义流量控制规则

```java
@Configuration
public class SentinelConfig {

    @PostConstruct
    public void initRules() {
        List<FlowRule> rules = new ArrayList<>();
        FlowRule rule = new FlowRule();
        rule.setResource("getUser");
        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        rule.setCount(10);
        rules.add(rule);
        FlowRuleManager.loadRules(rules);
    }
}
```

### 2. 自定义降级规则

```java
@Configuration
public class SentinelConfig {

    @PostConstruct
    public void initDegradeRules() {
        List<DegradeRule> rules = new ArrayList<>();
        DegradeRule rule = new DegradeRule();
        rule.setResource("getUser");
        rule.setGrade(RuleConstant.DEGRADE_GRADE_RT);
        rule.setCount(100);
        rule.setTimeWindow(10);
        rules.add(rule);
        DegradeRuleManager.loadRules(rules);
    }
}
```

### 3. 自定义配置监听

```java
@Component
public class ConfigListener {

    @NacosConfigListener(dataId = "user-config", groupId = "DEFAULT_GROUP")
    public void onConfigChange(String configInfo) {
        log.info("配置更新: {}", configInfo);
        // 处理配置更新
    }
}
```

## 注意事项

1. **版本兼容性**: 确保 Sentinel 和 Nacos 版本兼容
2. **配置管理**: 合理管理配置参数
3. **性能影响**: 注意流量控制对性能的影响
4. **监控告警**: 配置合适的监控和告警

## 版本历史

- **1.0.0**: 初始版本，基础扩展功能

## 贡献指南

欢迎提交 Issue 和 Pull Request 来改进这个模块。

## 许可证

MIT License
