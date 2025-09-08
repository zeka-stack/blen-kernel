# Blen Kernel Notify

## 概述

`blen-kernel-notify` 是 Zeka.Stack 框架的通知模块，提供了消息通知、操作日志记录等功能。该模块基于 Spring AOP 技术，支持异步消息处理和日志记录。

## 主要功能

### 1. 消息通知

- **AbstractMessage**: 抽象消息类
- 支持多种消息类型
- 支持异步消息处理

### 2. 操作日志

- **OperateLog**: 操作日志实体
- 支持操作日志记录
- 支持日志查询和分析

### 3. AOP 支持

- **MessageIdAspect**: 消息 ID 切面
- 支持方法级别的日志记录
- 支持自定义切面逻辑

### 4. 异常处理

- **NotifyException**: 通知异常
- 支持通知相关的异常处理

## 核心特性

### 1. 异步消息处理

- 支持异步消息发送
- 支持消息队列集成
- 支持消息重试机制

### 2. 操作日志记录

- 自动记录用户操作
- 支持操作参数记录
- 支持操作结果记录

### 3. AOP 集成

- 基于 Spring AOP
- 支持方法拦截
- 支持自定义切面

## 依赖关系

### 核心依赖

- **blen-kernel-common**: 基础功能依赖
- **org.aspectj:aspectjweaver**: AspectJ 支持
- **spring-context**: Spring 上下文支持

## 使用方式

### 1. 添加依赖

```xml
<dependency>
    <groupId>dev.dong4j</groupId>
    <artifactId>blen-kernel-notify</artifactId>
    <version>${blen-kernel.version}</version>
</dependency>
```

### 2. 消息通知

```java
@Component
public class MessageService {

    @Async
    public void sendMessage(AbstractMessage message) {
        // 发送消息逻辑
        log.info("发送消息: {}", message);
    }
}
```

### 3. 操作日志记录

```java
@Service
public class UserService {

    @MessageId
    public void createUser(User user) {
        // 创建用户逻辑
        log.info("创建用户: {}", user.getUsername());
    }
}
```

## 配置说明

### AOP 配置

```java
@Configuration
@EnableAspectJAutoProxy
public class NotifyConfig {

    @Bean
    public MessageIdAspect messageIdAspect() {
        return new MessageIdAspect();
    }
}
```

## 版本历史

- **1.0.0**: 初始版本，基础通知功能

## 贡献指南

欢迎提交 Issue 和 Pull Request 来改进这个模块。

## 许可证

MIT License
