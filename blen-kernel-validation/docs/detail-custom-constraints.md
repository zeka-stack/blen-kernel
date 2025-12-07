---
published: 2025.01.01
---

# 自定义验证注解

## 概述

`blen-kernel-validation` 模块提供了丰富的自定义验证注解，用于满足各种业务场景的数据验证需求。这些注解基于 Jakarta Validation (JSR-303)
标准，提供了手机号、身份证号、车牌号等常见格式的验证。

## 核心注解

### @Phone

手机号验证注解，用于验证手机号格式。

#### 使用示例

```java
public class User {
    @Phone(message = "手机号格式不正确")
    private String phone;
}
```

#### 验证规则

- 支持中国大陆手机号格式
- 11 位数字
- 以 1 开头

### @IdCard

身份证号验证注解，用于验证身份证号格式。

#### 使用示例

```java
public class User {
    @IdCard(message = "身份证号格式不正确")
    private String idCard;
}
```

#### 验证规则

- 支持 18 位身份证号
- 支持 15 位身份证号（旧版）
- 验证校验位

### @VehicleNumber

车牌号验证注解，用于验证车牌号格式。

#### 使用示例

```java
public class Vehicle {
    @VehicleNumber(message = "车牌号格式不正确")
    private String plateNumber;
}
```

#### 验证规则

- 支持标准车牌号格式
- 支持新能源车牌号格式

### @IPAddress

IP 地址验证注解，用于验证 IP 地址格式。

#### 使用示例

```java
public class Server {
    @IPAddress(message = "IP地址格式不正确")
    private String ipAddress;
}
```

#### 验证规则

- 支持 IPv4 格式
- 支持 IPv6 格式

### @Date

日期格式验证注解，用于验证日期字符串格式。

#### 使用示例

```java
public class Event {
    @Date(pattern = "yyyy-MM-dd", message = "日期格式不正确")
    private String dateStr;
}
```

#### 验证规则

- 支持自定义日期格式
- 验证日期有效性

### @Json

JSON 格式验证注解，用于验证 JSON 字符串格式。

#### 使用示例

```java
public class Config {
    @Json(message = "JSON格式不正确")
    private String jsonData;
}
```

#### 验证规则

- 验证 JSON 字符串格式
- 验证 JSON 语法正确性

### @EnumX

枚举值验证注解，用于验证字段值是否在指定的枚举值列表中。

#### 使用示例

```java
public class User {
    @EnumX(value = {"MALE", "FEMALE"}, message = "性别只能是MALE或FEMALE")
    private String gender;
}
```

#### 验证规则

- 验证值是否在指定列表中
- 支持字符串类型
- 支持大小写敏感/不敏感

## 使用示例

### 基础使用

```java
public class User {
    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 20, message = "用户名长度必须在3-20个字符之间")
    private String username;

    @Email(message = "邮箱格式不正确")
    private String email;

    @Phone(message = "手机号格式不正确")
    private String phone;

    @IdCard(message = "身份证号格式不正确")
    private String idCard;
}
```

### 分组验证

```java
public class User {
    @NotBlank(groups = CreateGroup.class, message = "用户名不能为空")
    private String username;

    @Phone(groups = {CreateGroup.class, UpdateGroup.class}, message = "手机号格式不正确")
    private String phone;
}

// 使用分组验证
ValidatorUtils.validate(user, CreateGroup.class);
```

### 编程式验证

```java
@Service
public class UserService {

    public void createUser(User user) {
        // 使用 ValidatorUtils 进行验证
        ValidatorUtils.validate(user);

        // 或者使用 BeanValidator
        Map<String, String> errors = BeanValidator.validate(user);
        if (!errors.isEmpty()) {
            throw new ValidationException("验证失败", errors);
        }

        // 创建用户逻辑
    }
}
```

## 扩展开发

### 自定义验证注解

```java
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CustomValidator.class)
public @interface CustomConstraint {
    String message() default "验证失败";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String value() default "";
}
```

### 自定义验证器

```java
public class CustomValidator implements ConstraintValidator<CustomConstraint, String> {
    private String value;

    @Override
    public void initialize(CustomConstraint constraintAnnotation) {
        this.value = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // 自定义验证逻辑
        return value != null && value.matches(this.value);
    }
}
```

## 最佳实践

### 1. 错误消息

- 提供清晰的错误消息
- 支持国际化错误消息
- 使用消息键而不是硬编码消息

### 2. 验证分组

- 使用验证分组区分不同场景
- 创建、更新、查询等场景使用不同的分组
- 避免不必要的验证

### 3. 性能优化

- 验证器实例会被缓存，避免重复创建
- 使用快速失败模式，遇到第一个错误就停止
- 合理使用验证分组，减少不必要的验证

## 注意事项

1. **验证顺序**: 验证注解的执行顺序可能影响验证结果
2. **性能影响**: 复杂的验证逻辑会影响性能
3. **错误消息**: 确保错误消息清晰易懂
4. **国际化**: 支持多语言错误消息

