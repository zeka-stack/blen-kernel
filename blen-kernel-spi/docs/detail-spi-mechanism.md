---
published: 2025.01.01
---

# SPI 机制

## 概述

`blen-kernel-spi` 模块提供了 SPI (Service Provider Interface) 机制的实现，支持动态服务发现和加载。该模块基于 Java SPI 机制，提供了更强大的服务扩展能力。

## 核心组件

### @SPI

SPI 扩展点注解，用于标记一个接口为 SPI 扩展点。

#### 设计原理

`@SPI` 注解标识哪些接口是可扩展的，被标记的接口可以有多个实现，并通过 SPI 机制进行动态加载。

#### 使用示例

```java
@SPI("default")
public interface ExtensionService {
    void execute();
}
```

### ExtensionFactory

扩展工厂接口，用于创建和管理扩展实例。

#### 核心方法

```java
public interface ExtensionFactory {
    <T> T getExtension(Class<T> type, String name);
    <T> List<T> getExtensions(Class<T> type);
}
```

### SpiExtensionFactory

SPI 扩展工厂实现，基于 Java SPI 机制实现扩展加载。

#### 实现细节

```java
public class SpiExtensionFactory implements ExtensionFactory {
    
    @Override
    public <T> T getExtension(Class<T> type, String name) {
        // 从 SPI 配置文件中加载扩展实现
        ServiceLoader<T> serviceLoader = ServiceLoader.load(type);
        for (T extension : serviceLoader) {
            if (name.equals(getExtensionName(extension))) {
                return extension;
            }
        }
        return null;
    }
    
    @Override
    public <T> List<T> getExtensions(Class<T> type) {
        List<T> extensions = new ArrayList<>();
        ServiceLoader<T> serviceLoader = ServiceLoader.load(type);
        for (T extension : serviceLoader) {
            extensions.add(extension);
        }
        return extensions;
    }
}
```

## 使用示例

### 定义扩展接口

```java
@SPI("default")
public interface PaymentService {
    void pay(BigDecimal amount);
}
```

### 实现扩展

```java
public class AlipayPaymentService implements PaymentService {
    @Override
    public void pay(BigDecimal amount) {
        // 支付宝支付逻辑
    }
}

public class WechatPaymentService implements PaymentService {
    @Override
    public void pay(BigDecimal amount) {
        // 微信支付逻辑
    }
}
```

### 配置 SPI

在 `META-INF/spi/` 目录下创建配置文件：

```
META-INF/spi/com.example.PaymentService
```

文件内容：

```
alipay=com.example.AlipayPaymentService
wechat=com.example.WechatPaymentService
default=com.example.AlipayPaymentService
```

### 使用扩展

```java
@Service
public class OrderService {
    
    @Autowired
    private ExtensionFactory extensionFactory;
    
    public void payOrder(Order order, String paymentType) {
        PaymentService paymentService = extensionFactory.getExtension(
            PaymentService.class, paymentType);
        paymentService.pay(order.getAmount());
    }
}
```

## 核心特性

### 1. 服务发现

基于 Java SPI 机制，支持动态服务加载：

- 自动扫描 SPI 配置文件
- 支持多个实现类
- 支持默认实现

### 2. 扩展管理

提供扩展工厂管理扩展实例：

- 按名称获取扩展
- 获取所有扩展
- 支持扩展优先级

### 3. 动态编译

支持运行时代码编译：

- 支持动态代码生成
- 支持多种编译器实现
- 支持类加载器管理

## 最佳实践

### 1. 扩展设计

- 定义清晰的扩展接口
- 使用 `@SPI` 注解标记扩展点
- 提供默认实现

### 2. 扩展管理

- 使用扩展工厂管理扩展
- 支持扩展的优先级
- 提供扩展的配置机制

### 3. 性能优化

- 缓存扩展实例
- 延迟加载扩展
- 避免重复创建扩展

## 注意事项

1. **类加载**: 注意扩展类的类加载器
2. **线程安全**: 确保扩展实现的线程安全
3. **配置管理**: 确保 SPI 配置文件正确
4. **性能影响**: 注意扩展加载对性能的影响

