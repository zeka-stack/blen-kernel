## 多态序列化框架使用说明

### 1. 框架概述

这是一个基于Jackson的多态序列化框架，主要包含以下组件：

- **[IPolymorphic](src/main/java/dev/dong4j/zeka/kernel/common/jackson/IPolymorphic.java#L26-L28)** - 多态序列化标记接口
- **[@Polymorphic](src/main/java/dev/dong4j/zeka/kernel/common/jackson/Polymorphic.java#L32-L113)** - 多态序列化配置注解
- **[PolymorphicSerialize](src/main/java/dev/dong4j/zeka/kernel/common/jackson/serialize/PolymorphicSerialize.java#L45-L177)** - 多态对象序列化器
- **[PolymorphicDeserialize](src/main/java/dev/dong4j/zeka/kernel/common/jackson/serialize/PolymorphicDeserialize.java#L50-L194)** -
  多态对象反序列化器
- **[PolymorphicModule](src/main/java/dev/dong4j/zeka/kernel/common/jackson/serialize/PolymorphicModule.java#L31-L51)** - Jackson模块注册器

### 2. 使用步骤

#### 步骤1：实现IPolymorphic接口

所有需要进行多态序列化的类都必须实现[IPolymorphic](src/main/java/dev/dong4j/zeka/kernel/common/jackson/IPolymorphic.java#L26-L28)接口：

java

```java
// 基类
public abstract class Animal implements IPolymorphic {
    protected String name;
    // getter/setter...
}

// 具体实现类
public class Dog extends Animal {
    private String breed;
    // getter/setter...
}

public class Cat extends Animal {
    private boolean isIndoor;
    // getter/setter...
}
```

#### 步骤2：创建枚举映射（推荐方式）

实现[SerializeEnum](src/main/java/dev/dong4j/zeka/kernel/common/enums/SerializeEnum.java#L32-L170)接口创建类型映射枚举：

java

```java
public enum AnimalType implements SerializeEnum {
    DOG("dog", Dog.class),
    CAT("cat", Cat.class);

    private final String desc;
    private final Class<?> clazz;

    AnimalType(String desc, Class<?> clazz) {
        this.desc = desc;
        this.clazz = clazz;
    }

    @Override
    public String getDesc() {
        return desc;
    }

    @Override
    public Serializable getValue() {
        return clazz;
    }
}
```

#### 步骤3：使用@Polymorphic注解

在包含多态对象的字段上添加注解：

java

```java
public class Zoo {
    // 使用枚举映射的方式
    @Polymorphic(value = "type", enumClass = AnimalType.class)
    private Animal animal;

    // 使用配置映射的方式
    @Polymorphic(
        value = "type",
        types = {
            @Polymorphic.Type(value = "dog", clz = Dog.class),
            @Polymorphic.Type(value = "cat", clz = Cat.class)
        }
    )
    private Animal anotherAnimal;

    // getter/setter...
}
```

### 3. JSON格式示例

#### 序列化结果

json

```java
{
  "animal": {
    "type": "dog",
    "name": "Buddy",
    "breed": "Golden Retriever"
  },
  "anotherAnimal": {
    "type": "cat",
    "name": "Whiskers",
    "isIndoor": true
  }
}
```

### 4. 配置和集成

#### 自动配置（推荐）

框架使用[@AutoService]
注解，Jackson会自动发现并注册[PolymorphicModule](src/main/java/dev/dong4j/zeka/kernel/common/jackson/serialize/PolymorphicModule.java#L31-L51)：

java

```java
// 无需手动配置，只要类路径中包含该模块即可自动生效
ObjectMapper mapper = new ObjectMapper();
// 模块会自动注册
```

#### 手动配置

如果需要手动配置：

java

```java
ObjectMapper mapper = new ObjectMapper();
mapper.registerModule(new PolymorphicModule());
```

### 5. 支持的场景

1. **单个多态对象**：直接在字段上使用[@Polymorphic](src/main/java/dev/dong4j/zeka/kernel/common/jackson/Polymorphic.java#L32-L113)注解
2. **集合中的多态对象**：框架会自动处理List、Set等集合中的多态对象
3. **嵌套多态对象**：支持多层嵌套的多态对象处理
4. **混合类型映射**：同一个项目中可以使用枚举和配置两种映射方式

### 6. 最佳实践

1. **优先使用枚举映射**：类型安全，易于维护
2. **合理命名类型标识**：使用简洁明了的字符串标识
3. **统一类型字段名**：建议在项目中使用统一的类型字段名（如"type"）
4. **异常处理**：框架会在无法识别类型时记录日志并返回null，业务代码需要做好null值检查

### 7. 注意事项

- 所有参与多态序列化的类必须实现[IPolymorphic](src/main/java/dev/dong4j/zeka/kernel/common/jackson/IPolymorphic.java#L26-L28)接口
- 类型标识字符串在同一个映射配置中必须唯一
- 框架会进行类型验证，确保目标类实现了[IPolymorphic](src/main/java/dev/dong4j/zeka/kernel/common/jackson/IPolymorphic.java#L26-L28)接口
- 集合类型中的多态对象会被递归处理

这个框架为复杂的多态对象序列化提供了灵活而强大的解决方案，特别适用于需要在JSON中保持类型信息的场景。
