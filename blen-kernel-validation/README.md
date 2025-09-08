# Blen Kernel Validation

## 概述

`blen-kernel-validation` 是 Zeka.Stack 框架的数据验证模块，基于 Jakarta Validation (JSR-303) 标准，提供了丰富的验证注解和工具类。该模块不仅支持标准的
Bean Validation，还提供了自定义的验证注解，满足各种业务场景的数据验证需求。

## 主要功能

### 1. 验证工具类

- **ValidatorUtils**: 核心验证工具类
- **BeanValidator**: Bean 验证器
- **RegexUtils**: 正则表达式工具

### 2. 自定义验证注解

- **@Phone**: 手机号验证
- **@IdCard**: 身份证号验证
- **@VehicleNumber**: 车牌号验证
- **@IPAddress**: IP 地址验证
- **@Date**: 日期格式验证
- **@Json**: JSON 格式验证
- **@EnumX**: 枚举值验证

### 3. 验证分组

- **ValidatorGroup.First**: 第一组验证
- **ValidatorGroup.Second**: 第二组验证
- **ValidatorGroup.Third**: 第三组验证

### 4. 错误信息处理

- 支持自定义错误消息
- 支持 JSON 格式的错误信息
- 支持带 ID 的错误信息

## 核心特性

### 1. 标准 Bean Validation 支持

- 基于 Jakarta Validation API
- 集成 Hibernate Validator 实现
- 支持分组验证
- 支持级联验证

### 2. 丰富的自定义验证注解

- 手机号格式验证
- 身份证号格式验证
- 车牌号格式验证
- IP 地址格式验证
- 日期格式验证
- JSON 格式验证
- 枚举值验证

### 3. 灵活的验证工具

- 支持编程式验证
- 支持异常抛出式验证
- 支持带 ID 的验证结果

### 4. 完善的错误处理

- 统一的错误信息格式
- 支持国际化错误消息
- 支持详细的验证错误信息

## 依赖关系

### 核心依赖

- **blen-kernel-common**: 基础功能依赖
- **jakarta.validation:jakarta.validation-api**: Jakarta Validation API
- **org.hibernate.validator:hibernate-validator**: Hibernate Validator 实现
- **com.google.code.findbugs:jsr305**: JSR-305 注解支持
- **spring-core**: Spring 核心支持
- **spring-context**: Spring 上下文支持
- **jackson-databind**: JSON 处理支持

### 测试依赖

- **blen-kernel-test**: 测试工具支持
- **spring-boot-starter-validation**: Spring Boot 验证支持
- **spring-web**: Web 验证支持

## 使用方式

### 1. 添加依赖

```xml
<dependency>
    <groupId>dev.dong4j</groupId>
    <artifactId>blen-kernel-validation</artifactId>
    <version>${blen-kernel.version}</version>
</dependency>
```

### 2. 使用标准验证注解

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

    @Min(value = 18, message = "年龄不能小于18岁")
    @Max(value = 100, message = "年龄不能大于100岁")
    private Integer age;
}
```

### 3. 使用自定义验证注解

```java
public class Vehicle {

    @VehicleNumber(message = "车牌号格式不正确")
    private String plateNumber;

    @IPAddress(message = "IP地址格式不正确")
    private String ipAddress;

    @Date(pattern = "yyyy-MM-dd", message = "日期格式不正确")
    private String dateStr;

    @Json(message = "JSON格式不正确")
    private String jsonData;

    @EnumX(value = {"MALE", "FEMALE"}, message = "性别只能是MALE或FEMALE")
    private String gender;
}
```

### 4. 编程式验证

```java
@Service
public class UserService {

    public void createUser(User user) {
        // 验证并抛出异常
        ValidatorUtils.validateResultProcessWithException(user);

        // 或者获取验证结果
        Optional<String> errors = ValidatorUtils.validateResultProcess(user);
        if (errors.isPresent()) {
            throw new ValidationException(errors.get());
        }

        // 带ID的验证
        Optional<String> errorsWithId = ValidatorUtils.validateResultProcesWithId(user, "id");
        if (errorsWithId.isPresent()) {
            log.error("用户验证失败: {}", errorsWithId.get());
        }
    }
}
```

### 5. 分组验证

```java
public class User {

    @NotBlank(groups = ValidatorGroup.First.class, message = "用户名不能为空")
    private String username;

    @Email(groups = ValidatorGroup.Second.class, message = "邮箱格式不正确")
    private String email;

    @Phone(groups = ValidatorGroup.Third.class, message = "手机号格式不正确")
    private String phone;
}

// 使用分组验证
public void validateUser(User user) {
    // 只验证第一组
    ValidatorUtils.validateResultProcessWithException(user, ValidatorGroup.First.class);

    // 验证所有组
    ValidatorUtils.validateResultProcessWithException(user,
        ValidatorGroup.First.class,
        ValidatorGroup.Second.class,
        ValidatorGroup.Third.class);
}
```

### 6. 在 Controller 中使用

```java
@RestController
public class UserController {

    @PostMapping("/users")
    public Result<User> createUser(@Valid @RequestBody User user) {
        // Spring 会自动进行验证
        userService.createUser(user);
        return R.succeed(user);
    }

    @PostMapping("/users/validate")
    public Result<String> validateUser(@RequestBody User user) {
        Optional<String> errors = ValidatorUtils.validateResultProcess(user);
        if (errors.isPresent()) {
            return R.failed("验证失败", errors.get());
        }
        return R.succeed("验证通过");
    }
}
```

## 自定义验证注解

### 1. 创建自定义验证注解

```java
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CustomValidator.class)
public @interface CustomValidation {
    String message() default "自定义验证失败";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
```

### 2. 实现验证器

```java
public class CustomValidator implements ConstraintValidator<CustomValidation, String> {

    @Override
    public void initialize(CustomValidation constraintAnnotation) {
        // 初始化
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // 空值由 @NotNull 等注解处理
        }

        // 自定义验证逻辑
        return value.startsWith("CUSTOM_");
    }
}
```

## 配置说明

### Spring Boot 配置

```yaml
spring:
  validation:
    fail-fast: true  # 快速失败模式
```

### 自定义错误消息

```properties
# 在 messages.properties 中定义
user.username.notblank=用户名不能为空
user.email.email=邮箱格式不正确
user.phone.phone=手机号格式不正确
```

## 高级用法

### 1. 条件验证

```java
public class User {

    @NotBlank(message = "用户名不能为空")
    private String username;

    @AssertTrue(message = "用户名和邮箱不能同时为空")
    public boolean isUsernameOrEmailValid() {
        return StringUtils.isNotBlank(username) || StringUtils.isNotBlank(email);
    }
}
```

### 2. 自定义验证消息

```java
public class User {

    @NotBlank(message = "{user.username.notblank}")
    private String username;

    @Size(min = 6, max = 20, message = "密码长度必须在{min}-{max}个字符之间")
    private String password;
}
```

### 3. 级联验证

```java
public class User {

    @Valid
    @NotNull(message = "用户详情不能为空")
    private UserDetail userDetail;
}

public class UserDetail {

    @NotBlank(message = "真实姓名不能为空")
    private String realName;

    @Phone(message = "手机号格式不正确")
    private String phone;
}
```

## 错误处理

### 1. 验证异常处理

```java
@RestControllerAdvice
public class ValidationExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    public Result<Void> handleValidationException(ValidationException e) {
        return R.failed("数据验证失败", e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        String message = bindingResult.getFieldErrors().stream()
            .map(FieldError::getDefaultMessage)
            .collect(Collectors.joining(", "));
        return R.failed("参数验证失败", message);
    }
}
```

### 2. 自定义错误格式

```java
public class ValidationError {
    private String field;
    private String message;
    private Object rejectedValue;

    // getters and setters
}
```

## 性能优化

1. **验证器缓存**: 验证器实例会被缓存，避免重复创建
2. **快速失败**: 启用 `fail-fast` 模式，遇到第一个错误就停止
3. **分组验证**: 使用分组验证减少不必要的验证

## 版本历史

- **1.0.0**: 初始版本，基础验证功能
- **1.3.0**: 增加自定义验证注解
- **1.6.0**: 优化验证工具类

## 贡献指南

欢迎提交 Issue 和 Pull Request 来改进这个模块。

## 许可证

MIT License
