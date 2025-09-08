# Blen Kernel Test

## 概述

`blen-kernel-test` 是 Zeka.Stack 框架的测试模块，提供了丰富的测试工具和注解，支持单元测试、集成测试、性能测试等。该模块基于 Spring Boot Test 和
JUnit 5，提供了完整的测试解决方案。

## 主要功能

### 1. 测试上下文加载器

- **ZekaSpringBootContextLoader**: Zeka Spring Boot 上下文加载器
- **ZekaStackTestContextBootstrapper**: Zeka Stack 测试上下文引导器
- 支持自定义测试上下文加载

### 2. 测试工具类

- **WebMvcTypeExcludeFilter**: Web MVC 类型排除过滤器
- 支持测试类型过滤
- 支持测试配置优化

### 3. 异常处理

- **ZekaBootTestException**: Zeka Boot 测试异常
- **MockException**: Mock 异常
- 支持测试异常处理

### 4. 性能测试支持

- 集成 JMH (Java Microbenchmark Harness)
- 支持微基准测试
- 支持性能分析

### 5. 异步测试支持

- 集成 Awaitility
- 支持异步操作测试
- 支持超时控制

## 核心特性

### 1. Spring Boot 测试集成

- 基于 Spring Boot Test
- 支持自动配置测试
- 支持测试切片

### 2. JUnit 5 支持

- 基于 JUnit 5 平台
- 支持参数化测试
- 支持嵌套测试

### 3. 性能测试

- 集成 JMH 框架
- 支持微基准测试
- 支持性能分析工具

### 4. 异步测试

- 支持异步操作测试
- 支持超时控制
- 支持条件等待

## 依赖关系

### 核心依赖

- **blen-kernel-common**: 基础功能依赖
- **spring-boot-starter-test**: Spring Boot 测试支持
- **junit-jupiter**: JUnit 5 支持
- **junit-platform-launcher**: JUnit 平台启动器
- **org.openjdk.jmh:jmh-core**: JMH 核心
- **org.openjdk.jmh:jmh-generator-annprocess**: JMH 注解处理器
- **org.awaitility:awaitility**: 异步测试支持

### 可选依赖

- **nacos-client**: Nacos 客户端
- **spring-web**: Spring Web 支持
- **spring-webmvc**: Spring Web MVC 支持
- **jakarta.servlet:jakarta.servlet-api**: Servlet API

## 使用方式

### 1. 添加依赖

```xml
<dependency>
    <groupId>dev.dong4j</groupId>
    <artifactId>blen-kernel-test</artifactId>
    <version>${blen-kernel.version}</version>
    <scope>test</scope>
</dependency>
```

### 2. 基础单元测试

```java
@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    void testCreateUser() {
        User user = new User();
        user.setUsername("test");
        user.setEmail("test@example.com");

        User result = userService.createUser(user);

        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo("test");
    }
}
```

### 3. Web 层测试

```java
@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    void testGetUser() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setUsername("test");

        when(userService.findById(1L)).thenReturn(user);

        mockMvc.perform(get("/users/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.username").value("test"));
    }
}
```

### 4. 集成测试

```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
class UserIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testUserCrud() {
        // 创建用户
        User user = new User();
        user.setUsername("test");
        user.setEmail("test@example.com");

        ResponseEntity<Result<User>> createResponse = restTemplate.postForEntity(
            "/users", user, new ParameterizedTypeReference<Result<User>>() {});

        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(createResponse.getBody().getData().getUsername()).isEqualTo("test");

        // 查询用户
        ResponseEntity<Result<User>> getResponse = restTemplate.getForEntity(
            "/users/1", new ParameterizedTypeReference<Result<User>>() {});

        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResponse.getBody().getData().getUsername()).isEqualTo("test");
    }
}
```

### 5. 性能测试

```java
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Benchmark)
public class UserServiceBenchmark {

    private UserService userService;
    private User testUser;

    @Setup
    public void setup() {
        // 初始化测试数据
        testUser = new User();
        testUser.setUsername("test");
        testUser.setEmail("test@example.com");
    }

    @Benchmark
    public void benchmarkCreateUser() {
        userService.createUser(testUser);
    }

    @Benchmark
    public void benchmarkFindUser() {
        userService.findById(1L);
    }
}
```

### 6. 异步测试

```java
@SpringBootTest
class AsyncServiceTest {

    @Autowired
    private AsyncService asyncService;

    @Test
    void testAsyncOperation() {
        CompletableFuture<String> future = asyncService.asyncOperation();

        await().atMost(5, TimeUnit.SECONDS)
            .until(future::isDone);

        assertThat(future.get()).isEqualTo("async result");
    }
}
```

## 配置说明

### 测试配置

```yaml
spring:
  test:
    database:
      replace: none
  datasource:
    url: jdbc:h2:mem:testdb
    driver-class-name: org.h2.Driver
  jpa:
    hibernate:
      ddl-auto: create-drop
```

### JMH 配置

```java
@Configuration
public class JmhConfig {

    @Bean
    public Options jmhOptions() {
        return new OptionsBuilder()
            .include(".*Benchmark")
            .warmupIterations(5)
            .measurementIterations(10)
            .forks(1)
            .build();
    }
}
```

## 测试注解

### 1. 基础测试注解

- `@SpringBootTest`: Spring Boot 测试
- `@WebMvcTest`: Web MVC 测试
- `@DataJpaTest`: 数据 JPA 测试
- `@JsonTest`: JSON 测试

### 2. 自定义测试注解

- `@ZekaTest`: Zeka 测试注解
- `@PerformanceTest`: 性能测试注解
- `@IntegrationTest`: 集成测试注解

### 3. 测试配置注解

- `@TestPropertySource`: 测试属性源
- `@TestConfiguration`: 测试配置
- `@MockBean`: Mock Bean

## 最佳实践

### 1. 测试分层

- 单元测试: 测试单个方法或类
- 集成测试: 测试多个组件协作
- 端到端测试: 测试完整业务流程

### 2. 测试数据管理

- 使用测试数据库
- 使用测试数据构建器
- 使用测试数据清理

### 3. 测试性能

- 使用测试切片
- 避免不必要的 Spring 上下文加载
- 使用 Mock 对象

### 4. 测试覆盖率

- 目标覆盖率: 80% 以上
- 关注关键业务逻辑
- 定期检查覆盖率报告

## 注意事项

1. **测试隔离**: 确保测试之间相互独立
2. **测试数据**: 使用测试专用的数据源
3. **测试清理**: 及时清理测试数据
4. **性能测试**: 在独立环境中运行性能测试

## 版本历史

- **1.0.0**: 初始版本，基础测试功能
- **1.6.0**: 增加性能测试支持

## 贡献指南

欢迎提交 Issue 和 Pull Request 来改进这个模块。

## 许可证

MIT License
