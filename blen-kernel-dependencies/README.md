# Blen Kernel Dependencies

## 概述

`blen-kernel-dependencies` 是 Zeka.Stack 框架的依赖管理模块，统一管理了框架所有模块的依赖版本。该模块基于 Maven 的 `dependencyManagement`
机制，确保整个框架的依赖版本一致性。

## 主要功能

### 1. 依赖版本管理

- 统一管理所有模块的依赖版本
- 确保依赖版本一致性
- 简化依赖配置

### 2. 模块依赖声明

- 声明所有子模块的依赖关系
- 支持依赖范围控制
- 支持可选依赖管理

### 3. 版本冲突解决

- 自动解决依赖版本冲突
- 提供版本选择策略
- 支持依赖排除

## 核心特性

### 1. 统一版本管理

- 所有模块使用相同的版本号
- 支持版本号统一升级
- 避免版本不一致问题

### 2. 依赖范围控制

- 合理设置依赖范围
- 避免不必要的依赖传递
- 优化依赖树结构

### 3. 模块化设计

- 支持模块独立使用
- 支持按需引入依赖
- 支持依赖隔离

## 依赖关系

### 父模块

- **arco-dependencies-parent**: Arco 依赖父模块

### 子模块

- **blen-kernel-autoconfigure**: 自动配置模块
- **blen-kernel-auth**: 认证模块
- **blen-kernel-common**: 通用模块
- **blen-kernel-devtools**: 开发工具模块
- **blen-kernel-notify**: 通知模块
- **blen-kernel-spi**: SPI 模块
- **blen-kernel-test**: 测试模块
- **blen-kernel-tracer**: 链路追踪模块
- **blen-kernel-validation**: 验证模块
- **blen-kernel-web**: Web 模块

## 使用方式

### 1. 作为父模块使用

```xml
<parent>
    <groupId>dev.dong4j</groupId>
    <artifactId>blen-kernel-dependencies</artifactId>
    <version>${blen-kernel.version}</version>
</parent>
```

### 2. 引入特定模块

```xml
<dependency>
    <groupId>dev.dong4j</groupId>
    <artifactId>blen-kernel-common</artifactId>
</dependency>

<dependency>
    <groupId>dev.dong4j</groupId>
    <artifactId>blen-kernel-auth</artifactId>
</dependency>
```

### 3. 引入所有模块

```xml
<dependency>
    <groupId>dev.dong4j</groupId>
    <artifactId>blen-kernel</artifactId>
    <version>${blen-kernel.version}</version>
    <type>pom</type>
</dependency>
```

## 配置说明

### 版本管理

```xml
<properties>
    <blen-kernel-dependencies.version>${global.version}</blen-kernel-dependencies.version>
</properties>
```

### 依赖管理

```xml
<dependencyManagement>
    <dependencies>
        <!-- 子模块依赖声明 -->
        <dependency>
            <groupId>dev.dong4j</groupId>
            <artifactId>blen-kernel-common</artifactId>
            <version>${blen-kernel-dependencies.version}</version>
        </dependency>
        <!-- 其他模块依赖... -->
    </dependencies>
</dependencyManagement>
```

## 版本策略

### 1. 版本号规则

- 主版本号: 重大功能变更
- 次版本号: 新功能添加
- 修订版本号: 问题修复

### 2. 兼容性策略

- 主版本号变更: 不兼容
- 次版本号变更: 向后兼容
- 修订版本号变更: 完全兼容

### 3. 发布策略

- 定期发布稳定版本
- 及时修复安全问题
- 提供长期支持版本

## 最佳实践

### 1. 依赖管理

- 使用依赖管理模块
- 避免直接指定版本号
- 定期更新依赖版本

### 2. 模块使用

- 按需引入模块
- 避免引入不必要的依赖
- 使用合适的依赖范围

### 3. 版本控制

- 使用版本变量
- 统一版本号管理
- 及时更新版本

## 注意事项

1. **版本一致性**: 确保所有模块使用相同版本
2. **依赖范围**: 合理设置依赖范围
3. **冲突解决**: 及时解决依赖冲突
4. **安全更新**: 及时更新安全相关依赖

## 版本历史

- **1.0.0**: 初始版本，基础依赖管理
- **2.0.0**: 升级到 Spring Boot 3.x

## 贡献指南

欢迎提交 Issue 和 Pull Request 来改进这个模块。

## 许可证

MIT License
