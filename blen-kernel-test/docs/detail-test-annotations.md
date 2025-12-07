---
published: 2025.01.01
---

# 测试注解

## 概述

`blen-kernel-test` 模块提供了 `@ZekaTest` 注解，用于简化 Spring Boot 测试的配置，集成了 Zeka 框架的测试支持和自定义的测试上下文引导程序。

## 核心组件

### @ZekaTest

Zeka 框架统一测试注解，集成了 Spring Boot 测试和自定义测试配置。

#### 设计原理

`@ZekaTest` 注解封装了 Spring Boot 测试的常用配置，同时集成了 Zeka 框架特有的测试支持：

1. **Spring Boot 测试集成**: 基于 `@SpringBootTest` 注解
2. **自定义上下文引导**: 使用 `ZekaStackTestContextBootstrapper`
3. **启动扩展支持**: 集成 Zeka 框架的启动扩展
4. **环境配置**: 支持多种测试环境和配置模式

#### 使用示例

```java
@ZekaTest
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

### BaseTest

基础测试类，提供统一的测试基础配置和环境。

#### 使用示例

```java
@ZekaTest
public class BaseTest {
    // 基础测试配置
}

// 继承基础测试类
class UserServiceTest extends BaseTest {
    @Autowired
    private UserService userService;
    
    @Test
    void testCreateUser() {
        // 测试逻辑
    }
}
```

## 核心特性

### 1. 自动配置测试

`@ZekaTest` 注解会自动配置 Spring Boot 测试环境，包括：

- Spring 上下文加载
- 自动配置启用
- 测试切片支持

### 2. 自定义上下文引导

使用 `ZekaStackTestContextBootstrapper` 提供自定义的测试上下文引导：

- 支持自定义配置类
- 支持环境变量配置
- 支持配置文件加载

### 3. 启动扩展支持

集成 Zeka 框架的启动扩展，支持：

- 应用初始化监听器
- 配置刷新机制
- 组件初始化回调

## 使用示例

### 基础单元测试

```java
@ZekaTest
class UserServiceTest {
    
    @Autowired
    private UserService userService;
    
    @MockBean
    private UserRepository userRepository;
    
    @Test
    void testCreateUser() {
        User user = new User();
        user.setUsername("test");
        
        when(userRepository.save(any(User.class))).thenReturn(user);
        
        User result = userService.createUser(user);
        
        assertThat(result).isNotNull();
        verify(userRepository).save(user);
    }
}
```

### Web 层测试

```java
@ZekaTest
@AutoConfigureMockMvc
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

### 集成测试

```java
@ZekaTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
class UserIntegrationTest {
    
    @Autowired
    private TestRestTemplate restTemplate;
    
    @Test
    void testCreateUser() {
        User user = new User();
        user.setUsername("test");
        
        ResponseEntity<Result<User>> response = restTemplate.postForEntity(
            "/users", user, new ParameterizedTypeReference<Result<User>>() {});
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getData()).isNotNull();
    }
}
```

## 最佳实践

### 1. 测试隔离

- 确保测试之间相互独立
- 使用 `@DirtiesContext` 清理上下文
- 使用测试专用的数据源

### 2. 测试数据管理

- 使用测试数据构建器
- 使用 `@Sql` 注解加载测试数据
- 及时清理测试数据

### 3. 性能优化

- 使用测试切片减少上下文加载
- 避免不必要的 Spring 上下文加载
- 使用 Mock 对象减少依赖

## 注意事项

1. **上下文加载**: 注意测试上下文的加载时间
2. **数据隔离**: 确保测试数据相互隔离
3. **清理机制**: 及时清理测试数据
4. **性能影响**: 注意测试对系统性能的影响

