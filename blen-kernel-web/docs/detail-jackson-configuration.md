---
published: 2025.01.01
---

# Jackson 配置

## 概述

`blen-kernel-web` 模块提供了完整的 Jackson JSON 序列化配置，包括时间类型处理、空值处理、API 读写分离等特性，为 Web 应用提供统一的 JSON 序列化方案。

## 核心组件

### JavaTimeModule

Java 8 时间类型序列化模块，提供统一的时间格式处理。

#### 设计原理

Java 8 引入了新的时间 API（`LocalDateTime`、`LocalDate`、`LocalTime`），但 Jackson 默认不支持这些类型的序列化。`JavaTimeModule`
通过自定义序列化器和反序列化器解决这个问题。

#### 实现细节

```java
public class JavaTimeModule extends SimpleModule {
    public JavaTimeModule() {
        super(PackageVersion.VERSION);
        // LocalDateTime 序列化和反序列化
        this.addDeserializer(LocalDateTime.class,
            new LocalDateTimeDeserializer(DateTimeUtils.DATETIME_FORMAT));
        this.addSerializer(LocalDateTime.class,
            new LocalDateTimeSerializer(DateTimeUtils.DATETIME_FORMAT));

        // LocalDate 序列化和反序列化
        this.addDeserializer(LocalDate.class,
            new LocalDateDeserializer(DateTimeUtils.DATE_FORMAT));
        this.addSerializer(LocalDate.class,
            new LocalDateSerializer(DateTimeUtils.DATE_FORMAT));

        // LocalTime 序列化和反序列化
        this.addDeserializer(LocalTime.class,
            new LocalTimeDeserializer(DateTimeUtils.TIME_FORMAT));
        this.addSerializer(LocalTime.class,
            new LocalTimeSerializer(DateTimeUtils.TIME_FORMAT));
    }
}
```

#### 默认格式

- `LocalDateTime`: `yyyy-MM-dd HH:mm:ss`
- `LocalDate`: `yyyy-MM-dd`
- `LocalTime`: `HH:mm:ss`

#### 使用场景

- API 响应中的时间字段序列化
- 请求参数中的时间字段反序列化
- 统一的时间格式输出

### DefaultBeanSerializerModifier

默认 Bean 序列化修改器，用于处理 null 值的序列化，避免移动端出现 null 导致崩溃。

#### 设计原理

移动端（Android/iOS）在解析 JSON 时，如果遇到 null 值可能会导致应用崩溃。`DefaultBeanSerializerModifier` 通过为不同类型的 null 值提供默认值来解决这个问题。

#### Null 值处理规则

| 类型               | Null 值处理  |
|------------------|-----------|
| Number           | 0         |
| String           | "" (空字符串) |
| Boolean          | false     |
| Date/Time        | "" (空字符串) |
| Array/Collection | [] (空数组)  |
| Object           | {} (空对象)  |

#### 实现细节

```java
public class DefaultBeanSerializerModifier extends BeanSerializerModifier {
    @Override
    public List<BeanPropertyWriter> changeProperties(
            SerializationConfig config,
            BeanDescription beanDesc,
            List<BeanPropertyWriter> beanProperties) {
        beanProperties.forEach(writer -> {
            // 如果已经有 null 序列化处理则跳过
            final JsonInclude annotation = writer.getAnnotation(JsonInclude.class);
            if (annotation != null &&
                JsonInclude.Include.NON_NULL.equals(annotation.value()) ||
                writer.hasNullSerializer()) {
                return;
            }

            JavaType type = writer.getType();
            Class<?> clazz = type.getRawClass();

            // 根据类型分配不同的 null 序列化器
            if (type.isTypeOrSubTypeOf(Boolean.class)) {
                writer.assignNullSerializer(NullJsonSerializers.BOOLEAN_JSON_SERIALIZER);
            } else if (type.isTypeOrSubTypeOf(String.class)) {
                writer.assignNullSerializer(NullJsonSerializers.STRING_JSON_SERIALIZER);
            } else if (type.isArrayType() || type.isTypeOrSubTypeOf(Collection.class)) {
                writer.assignNullSerializer(NullJsonSerializers.ARRAY_JSON_SERIALIZER);
            } else if (type.isTypeOrSubTypeOf(Date.class) ||
                       type.isTypeOrSubTypeOf(TemporalAccessor.class)) {
                writer.assignNullSerializer(NullJsonSerializers.STRING_JSON_SERIALIZER);
            } else {
                writer.assignNullSerializer(NullJsonSerializers.OBJECT_JSON_SERIALIZER);
            }
        });
        return super.changeProperties(config, beanDesc, beanProperties);
    }
}
```

#### 使用场景

- 移动端 API 响应处理
- 避免 null 值导致的客户端崩溃
- 统一的数据格式输出

### MappingApiJackson2HttpMessageConverter

API 消息转换器，支持读写分离的 ObjectMapper 配置。

#### 设计原理

在 API 服务中，客户端上报的数据和服务器返回的数据可能需要不同的序列化策略。`MappingApiJackson2HttpMessageConverter` 通过分离读写 ObjectMapper
来实现这个需求：

1. **读 ObjectMapper**: 用于反序列化客户端上报的数据
2. **写 ObjectMapper**: 用于序列化返回给客户端的数据

#### 实现细节

```java
public class MappingApiJackson2HttpMessageConverter
    extends AbstractReadWriteJackson2HttpMessageConverter {

    public MappingApiJackson2HttpMessageConverter(ObjectMapper objectMapper) {
        super(objectMapper,
              initWriteObjectMapper(objectMapper),
              MediaType.APPLICATION_JSON,
              new MediaType("application", "*+json"));
    }

    private static ObjectMapper initWriteObjectMapper(ObjectMapper readObjectMapper) {
        // 拷贝读 ObjectMapper
        ObjectMapper writeObjectMapper = readObjectMapper.copy();
        // 设置序列化修改器，处理 null 值
        writeObjectMapper.setSerializerFactory(
            writeObjectMapper.getSerializerFactory()
                .withSerializerModifier(new DefaultBeanSerializerModifier()));
        return writeObjectMapper;
    }

    // 支持 JSON 前缀，防止 JSON 劫持
    @Override
    protected void writePrefix(JsonGenerator generator, Object object)
        throws IOException {
        if (this.jsonPrefix != null) {
            generator.writeRaw(this.jsonPrefix);
        }
    }
}
```

#### JSON 前缀保护

支持设置 JSON 前缀（如 `")]', "`），用于防止 JSON 劫持攻击：

```java
converter.setPrefixJson(true); // 设置前缀为 ")]}', "
```

#### 使用场景

- API 服务的请求响应处理
- 需要不同序列化策略的场景
- 需要 JSON 安全保护的场景

## 配置说明

### 自动配置

模块会自动配置以下组件：

1. `JavaTimeModule` - 注册到 ObjectMapper
2. `DefaultBeanSerializerModifier` - 应用到写 ObjectMapper
3. `MappingApiJackson2HttpMessageConverter` - 注册为消息转换器

### 自定义配置

```java
@Configuration
public class JacksonConfig {

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = Jackson2ObjectMapperBuilder.json().build();
        // 注册时间模块
        mapper.registerModule(new JavaTimeModule());
        // 其他自定义配置
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper;
    }
}
```

## 最佳实践

### 1. 时间格式统一

- 使用 `JavaTimeModule` 统一时间格式
- 避免在实体类中使用 `@JsonFormat` 注解，统一使用模块配置
- 前后端约定时间格式，避免解析错误

### 2. Null 值处理

- 对于移动端 API，使用 `DefaultBeanSerializerModifier` 处理 null 值
- 对于 Web 端 API，可以使用 `@JsonInclude(JsonInclude.Include.NON_NULL)` 排除 null
- 根据业务需求选择合适的 null 处理策略

### 3. 性能优化

- 复用 ObjectMapper 实例，避免重复创建
- 使用读写分离的 ObjectMapper，提高序列化性能
- 合理配置序列化特性，避免不必要的处理

## 扩展开发

### 自定义序列化器

```java
public class CustomSerializer extends JsonSerializer<CustomType> {
    @Override
    public void serialize(CustomType value, JsonGenerator gen,
                         SerializerProvider serializers) throws IOException {
        gen.writeString(value.toCustomString());
    }
}

// 注册自定义序列化器
SimpleModule module = new SimpleModule();
module.addSerializer(CustomType.class, new CustomSerializer());
objectMapper.registerModule(module);
```

### 自定义反序列化器

```java
public class CustomDeserializer extends JsonDeserializer<CustomType> {
    @Override
    public CustomType deserialize(JsonParser p,
                                  DeserializationContext ctxt)
        throws IOException {
        String value = p.getText();
        return CustomType.fromString(value);
    }
}

// 注册自定义反序列化器
SimpleModule module = new SimpleModule();
module.addDeserializer(CustomType.class, new CustomDeserializer());
objectMapper.registerModule(module);
```

## 注意事项

1. **时间格式**: 确保前后端时间格式一致，避免解析错误
2. **Null 处理**: 根据客户端类型选择合适的 null 处理策略
3. **性能影响**: ObjectMapper 的配置会影响序列化性能，需要合理配置
4. **兼容性**: 注意 Jackson 版本兼容性，避免使用已废弃的 API

