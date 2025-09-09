package dev.dong4j.zeka.kernel.common;

import com.google.common.collect.Maps;
import dev.dong4j.zeka.kernel.common.exception.StarterException;
import dev.dong4j.zeka.kernel.common.function.CheckedRunnable;
import dev.dong4j.zeka.kernel.common.util.StringPool;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.ConfigurableBootstrapContext;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.boot.context.event.ApplicationContextInitializedEvent;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.boot.context.event.ApplicationFailedEvent;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.boot.context.event.ApplicationStartingEvent;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.ContextStoppedEvent;
import org.springframework.context.event.GenericApplicationListener;
import org.springframework.core.ResolvableType;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * Zeka应用程序生命周期事件监听器接口
 * <p>
 * 该接口抽象了所有Spring Boot应用程序事件处理器的公共代码
 * 提供统一的事件处理机制，解决Spring Cloud环境下多上下文重复执行的问题
 * <p>
 * 主要特性：
 * - 完整的生命周期支持：覆盖从应用启动到关闭的所有阶段
 * - Spring Cloud兼容：解决多上下文环境下的事件重复执行问题
 * - 执行次数统计：提供精确的执行次数控制和统计
 * - 事件类型过滤：只处理指定类型的事件，提高性能
 * - 灵活的执行策略：支持首次、末次、指定次数执行
 * <p>
 * 支持的事件类型：
 * <b>应用启动相关事件：</b>
 * - {@link ApplicationStartingEvent}: 应用开始启动
 * - {@link ApplicationEnvironmentPreparedEvent}: 环境配置准备完成
 * - {@link ApplicationContextInitializedEvent}: 上下文初始化完成
 * - {@link ApplicationPreparedEvent}: 应用准备完成
 * - {@link ApplicationStartedEvent}: 应用启动完成
 * - {@link ApplicationReadyEvent}: 应用就绪完成
 * - {@link ApplicationFailedEvent}: 应用启动失败
 * <p>
 * <b>上下文生命周期事件：</b>
 * - {@link ContextRefreshedEvent}: 上下文刷新完成
 * - {@link ContextStartedEvent}: 上下文启动完成
 * - {@link ContextStoppedEvent}: 上下文停止完成
 * - {@link ContextClosedEvent}: 上下文关闭完成
 * <p>
 * <b>Web服务器事件：</b>
 * - {@link WebServerInitializedEvent}: Web服务器初始化完成
 * <p>
 * 使用场景：
 * - 应用启动时的初始化操作，如缓存预热、连接池初始化
 * - 配置加载和验证，特别是Spring Cloud环境下的配置处理
 * - 微服务注册和发现，在适当时机执行服务注册
 * - 数据库连接和缓存系统的生命周期管理
 * - 应用性能监控和指标采集的初始化
 * - 业务事件的发布和处理的初始化
 * <p>
 * Spring Cloud多上下文问题：
 * 由于Spring Cloud会通过{@code RestartListener}再次发布部分事件，
 * 导致自定义事件被重复处理。该接口提供了Runner工具类
 * 来解决这个问题，支持精确控制执行次数。
 * <p>
 * 使用示例：
 * <pre>{@code
 * @Component
 * public class MyApplicationListener implements ZekaApplicationListener {
 *
 *     @Override
 *     public void onApplicationReadyEvent(ApplicationReadyEvent event) {
 *         String key = key(event, this.getClass());
 *         // 只在第一次执行
 *         Runner.executeAtFirst(key, () -> {
 *             // 初始化逻辑
 *             initializeApplication();
 *         });
 *     }
 *
 *     @Override
 *     public void onApplicationFailedEvent(ApplicationFailedEvent event) {
 *         // 每次失败都执行
 *         Runner.execute(() -> {
 *             cleanupResources();
 *         });
 *     }
 * }
 * }</pre>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2019.12.26 21:37
 * @since 1.0.0
 */
public interface ZekaApplicationListener extends GenericApplicationListener {

    /**
     * 主事件分发器，根据事件类型调用相应的处理方法
     * <p>
     * 该方法会自动识别事件类型并调用对应的处理方法
     * 支持Spring Boot全生命周期的所有事件类型
     * <p>
     * 注意：Spring Cloud会通过RestartListener再次发布部分事件：
     * - ApplicationPreparedEvent
     * - ContextRefreshedEvent
     * - ContextClosedEvent
     * 导致自定义事件被重复处理，需要使用Runner工具类进行控制
     *
     * @param event 应用事件实例
     * @since 1.0.0
     */
    @Override
    default void onApplicationEvent(@NotNull ApplicationEvent event) {

        if (event instanceof ApplicationStartingEvent) {
            this.onApplicationStartingEvent((ApplicationStartingEvent) event);
        } else if (event instanceof ApplicationEnvironmentPreparedEvent) {
            this.onApplicationEnvironmentPreparedEvent((ApplicationEnvironmentPreparedEvent) event);
        } else if (event instanceof ApplicationContextInitializedEvent) {
            this.onApplicationContextInitializedEvent((ApplicationContextInitializedEvent) event);
        } else if (event instanceof ApplicationPreparedEvent) {
            this.onApplicationPreparedEvent((ApplicationPreparedEvent) event);
        } else if (event instanceof ApplicationStartedEvent) {
            this.onApplicationStartedEvent((ApplicationStartedEvent) event);
        } else if (event instanceof ApplicationReadyEvent) {
            this.onApplicationReadyEvent((ApplicationReadyEvent) event);
        } else if (event instanceof ApplicationFailedEvent) {
            this.onApplicationFailedEvent((ApplicationFailedEvent) event);
        } else if (event instanceof WebServerInitializedEvent) {
            this.onWebServerInitializedEvent((WebServerInitializedEvent) event);
        } else if (event instanceof ContextRefreshedEvent) {
            this.onContextRefreshedEvent((ContextRefreshedEvent) event);
        } else if (event instanceof ContextStartedEvent) {
            this.onContextStartedEvent((ContextStartedEvent) event);
        } else if (event instanceof ContextStoppedEvent) {
            this.onContextStoppedEvent((ContextStoppedEvent) event);
        } else if (event instanceof ContextClosedEvent) {
            this.onContextClosedEvent((ContextClosedEvent) event);
        }

    }

    /**
     * 应用启动时事件处理器
     * <p>
     * 在{@link org.springframework.boot.SpringApplication#run(String...)}中被调用
     * 该事件在应用程序开始启动时触发，是最早的事件
     * 在此阶段，环境配置和上下文都还未创建
     * <p>
     * 适用场景：
     * - 应用启动时的早期初始化操作
     * - 系统环境检查和验证
     * - JVM参数设置和优化
     * - 第三方库的早期初始化
     * <p>
     * 对应的SpringApplicationRunListener方法：
     * {@link SpringApplicationRunListener#starting(ConfigurableBootstrapContext)}
     *
     * @param event 应用启动事件，包含应用实例和启动参数
     * @since 1.0.0
     */
    default void onApplicationStartingEvent(ApplicationStartingEvent event) {

    }

    /**
     * 所有环境配置准备完成后被调用，且在ApplicationContext被创建之前执行
     * <p>
     * 该事件在环境配置（Environment）准备完成后触发
     * 此时可以访问完整的环境配置，但上下文还未创建
     * <p>
     * 适用场景：
     * - 基于配置的初始化操作
     * - 环境参数的验证和调整
     * - 条件化的组件初始化
     * - 在Spring Cloud中处理bootstrap配置
     * <p>
     * 对应的SpringApplicationRunListener方法：
     * {@link SpringApplicationRunListener#environmentPrepared(ConfigurableBootstrapContext, ConfigurableEnvironment)}
     *
     * @param event 环境准备事件，包含配置好的环境信息
     * @since 1.0.0
     */
    default void onApplicationEnvironmentPreparedEvent(ApplicationEnvironmentPreparedEvent event) {

    }

    /**
     * 当 {@link ApplicationContext} 被创建且初始化完成后执行
     * {@link org.springframework.boot.SpringApplicationRunListener#contextPrepared(ConfigurableApplicationContext)}
     *
     * @param event the event
     * @since 1.0.0
     */
    default void onApplicationContextInitializedEvent(ApplicationContextInitializedEvent event) {
    }

    /**
     * 当 {@link ApplicationContext} 被加载后(全部的 bean 都加载到 ICO)执行
     * {@link org.springframework.boot.SpringApplicationRunListener#contextLoaded(ConfigurableApplicationContext)}
     *
     * @param event the event
     * @since 1.0.0
     */
    default void onApplicationPreparedEvent(ApplicationPreparedEvent event) {
    }

    /**
     * {@link ApplicationContext} 的所有 bean 处理器处理完成后执行,
     * 这发生在 {@link org.springframework.boot.CommandLineRunner} 和 {@link org.springframework.boot.ApplicationRunner} 之前
     * {@link SpringApplicationRunListener#started(ConfigurableApplicationContext, Duration)}
     * refresh --> {@link org.springframework.context.support.AbstractApplicationContext#refresh()}
     *
     * @param event the event
     * @since 1.0.0
     */
    default void onApplicationStartedEvent(ApplicationStartedEvent event) {
    }

    /**
     * 应用就绪完成事件处理器（应用启动的最后一个阶段）
     * <p>
     * 当所有的CommandLineRunner、ApplicationRunner和ApplicationContext的refresh()
     * 执行完成之后，且在SpringApplication.run()执行完成之前调用
     * 该事件表示应用已经完全准备好，可以对外提供服务
     * <p>
     * 适用场景：
     * - 应用启动成功的通知和日志记录
     * - 性能监控和健康检查的启动
     * - 微服务注册中心的服务注册
     * - 缓存预热和数据预加载
     * - 定时任务和后台服务的启动
     * <p>
     * 对应的SpringApplicationRunListener方法：
     * {@link SpringApplicationRunListener#ready(ConfigurableApplicationContext, Duration)}
     *
     * @param event 应用就绪事件，表示应用已经完全准备好
     * @since 1.0.0
     */
    default void onApplicationReadyEvent(ApplicationReadyEvent event) {
    }

    /**
     * 运行应用程序时发生故障时调用
     * {@link org.springframework.boot.SpringApplicationRunListener#failed(ConfigurableApplicationContext, Throwable)}
     *
     * @param event the event
     * @since 1.0.0
     */
    default void onApplicationFailedEvent(ApplicationFailedEvent event) {
    }

    /**
     * Event to be published after the application context is refreshed and the
     * {@link org.springframework.boot.web.server.WebServer} is ready.
     * Useful for obtaining the local port of a running server.
     *
     * @param event the event
     * @since 1.0.0
     */
    default void onWebServerInitializedEvent(WebServerInitializedEvent event) {
    }

    /**
     * 完成 refresh 之后执行 (所有对象已被实例化和初始化之后)
     * {org.springframework.context.support.AbstractApplicationContext#finishRefresh()}
     *
     * @param event the event
     * @since 1.0.0
     */
    default void onContextRefreshedEvent(ContextRefreshedEvent event) {
    }

    /**
     * 当 Spring 容器加载所有 bean 并完成初始化之后(refreshed), 接着回调实现 {@link org.springframework.context.SmartLifecycle#start()} 接口的类中 start()
     * 当上下文被刷新 (所有对象已被实例化和初始化之后) 时,将调用该方法,默认生命周期处理器将检查每个 SmartLifecycle 对象的 isAutoStartup()方法返回的布尔值,
     * 如果为 true 则该方法会被调用,而不是等待显式调用自己的start()方法
     *
     * @param event the event
     * @since 1.0.0
     */
    default void onContextStartedEvent(ContextStartedEvent event) {
    }

    /**
     * 接口 Lifecycle 的子类的方法,只有非 SmartLifecycle 的子类才会执行该方法
     * 1. 该方法只对直接实现接口 Lifecycle 的类才起作用,对实现 SmartLifecycle 接口的类无效
     * 2. 方法 stop() 和方法 stop(Runnable callback) 的区别只在于后者是 SmartLifecycle 子类的专属.
     * {@link org.springframework.context.SmartLifecycle#stop()}
     *
     * @param event the event
     * @since 1.0.0
     */
    default void onContextStoppedEvent(ContextStoppedEvent event) {
    }

    /**
     * 当容器关闭时执行, {@link org.springframework.context.support.AbstractApplicationContext#doClose()}
     *
     * @param event the event
     * @since 1.0.0
     */
    default void onContextClosedEvent(ContextClosedEvent event) {
    }

    /**
     * Supports event types boolean.
     *
     * @param resolvableType the resolvable type
     * @return the boolean
     * @since 1.0.0
     */
    @Override
    default boolean supportsEventType(@NotNull ResolvableType resolvableType) {
        return this.isAssignableFrom(resolvableType.getRawClass(), Constant.EVENT_TYPES);
    }

    /**
     * Supports source types boolean.
     *
     * @param sourceType the source type
     * @return the boolean
     * @since 1.0.0
     */
    @Override
    default boolean supportsSourceType(Class<?> sourceType) {
        return true;
    }

    /**
     * Is assignable from boolean.
     *
     * @param type           the type
     * @param supportedTypes the supported types
     * @return the boolean
     * @since 1.0.0
     */
    @Contract("null, _ -> false")
    default boolean isAssignableFrom(Class<?> type, Class<?>... supportedTypes) {
        if (type != null) {
            for (Class<?> supportedType : supportedTypes) {
                if (supportedType.isAssignableFrom(type)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * <p>应用监听器常量定义类.
     * <p>定义了应用监听器中使用的各种常量，包括支持的事件类型列表.
     * <p>主要功能：
     * <ul>
     *     <li>定义 Spring Boot 应用生命周期事件类型</li>
     *     <li>支持应用启动、准备、就绪等各阶段事件</li>
     *     <li>包含 Web 服务器和上下文相关事件</li>
     *     <li>为事件处理提供类型约束和过滤</li>
     * </ul>
     * <p>支持的事件类型：
     * <ul>
     *     <li>应用启动相关：ApplicationStartingEvent、ApplicationStartedEvent、ApplicationReadyEvent</li>
     *     <li>环境准备相关：ApplicationEnvironmentPreparedEvent、ApplicationPreparedEvent</li>
     *     <li>上下文相关：ApplicationContextInitializedEvent、ContextRefreshedEvent 等</li>
     *     <li>异常处理：ApplicationFailedEvent</li>
     * </ul>
     *
     * @author dong4j
     * @version 1.0.0
     * @email "mailto:dong4j@gmail.com"
     * @date 2019.12.26 21:37
     * @since 1.0.0
     */
    @UtilityClass
    class Constant {

        /** 定义需要处理的事件类型 */
        static final Class<?>[] EVENT_TYPES = {
            ApplicationStartingEvent.class,
            ApplicationEnvironmentPreparedEvent.class,
            ApplicationContextInitializedEvent.class,
            ApplicationPreparedEvent.class,
            ApplicationStartedEvent.class,
            ApplicationReadyEvent.class,
            ApplicationFailedEvent.class,
            WebServerInitializedEvent.class,
            ContextRefreshedEvent.class,
            ContextStartedEvent.class,
            ContextStoppedEvent.class,
            ContextClosedEvent.class
        };
    }

    /**
     * 生成唯一 key
     *
     * @param event    event
     * @param subClass sub class
     * @return the string
     * @since 1.0.0
     */
    default String key(@NotNull ApplicationEvent event, @NotNull Class<? extends ZekaApplicationListener> subClass) {
        return String.join(StringPool.COLON, event.getClass().getName(), subClass.getName());
    }

    /**
     * <p>监听器执行统计和控制器.
     * <p>提供监听器执行次数统计和执行时机控制功能，主要用于解决 Spring Cloud 环境下监听器多次执行的问题.
     * <p>核心功能：
     * <ul>
     *     <li>统计监听器的执行次数和总体执行计数</li>
     *     <li>控制监听器在特定时机执行（首次、最后一次、指定次数）</li>
     *     <li>提供执行状态查询和重复执行防护</li>
     *     <li>支持异常安全的监听器执行包装</li>
     * </ul>
     * <p>应用场景：
     * <ul>
     *     <li>Spring Cloud 环境下的重复执行控制</li>
     *     <li>配置文件加载时机的精确控制</li>
     *     <li>应用启动阶段的资源初始化管理</li>
     *     <li>监听器执行状态的追踪和调试</li>
     * </ul>
     * <p>注意事项：
     * <ul>
     *     <li>首次执行只能读取 bootstrap.yml 配置</li>
     *     <li>最后一次执行只能读取 application.yml 配置</li>
     *     <li>所有执行都提供异常捕获和日志记录</li>
     * </ul>
     *
     * @author dong4j
     * @version 1.0.0
     * @email "mailto:dong4j@gmail.com"
     * @date 2020.05.19 20:10
     * @since 1.0.0
     */
    @Slf4j
    class Runner {
        /** 执行次数统计 */
        private static final Map<String, Integer> EXECTIE_COUNT_MAP = Maps.newConcurrentMap();
        /** listener 执行次数 */
        private static final AtomicInteger EXECUTE_COUNT = new AtomicInteger(0);

        /**
         * 执行后增加次数
         *
         * @param key key
         * @since 1.0.0
         */
        public static void execution(String key) {
            EXECTIE_COUNT_MAP.merge(key, 1, Integer::sum);
        }

        /**
         * 是否执行过
         *
         * @param key key
         * @return the boolean      执行过返回 true
         * @since 1.0.0
         */
        @Contract(pure = true)
        public static boolean executed(String key) {
            return EXECTIE_COUNT_MAP.get(key) != null;
        }

        /**
         * 统计执行次数 (使用有个优先级最高的 listener 统计执行次数)
         *
         * @since 1.0.0
         */
        public static void executeCount() {
            EXECUTE_COUNT.getAndIncrement();
        }

        /**
         * Get execute count
         *
         * @return the int
         * @since 1.0.0
         */
        public static int getExecuteTotalCount() {
            return EXECUTE_COUNT.get();
        }

        /**
         * 获取已经执行过的次数
         *
         * @param key key
         * @return the execute current count
         * @since 1.0.0
         */
        @Contract(pure = true)
        public static int getExecuteCurrentCount(String key) {
            return EXECTIE_COUNT_MAP.get(key) == null ? 0 : EXECTIE_COUNT_MAP.get(key);
        }

        /**
         * 第一次执行, 需要注意的是如果是 spring cloud 项目, 第一次执行只能读取到 bootstrap.yml 配置, application.yml 配置只能在第二次才能读取到.
         *
         * @param key      key
         * @param consumer consumer
         * @since 1.0.0
         */
        @Contract(pure = true)
        public static void executeAtFirst(String key, CheckedRunnable consumer) {
            executeAt(key, 1, consumer);
        }

        /**
         * 在最后一次执行, 需要注意的是, 如果是 spring cloud 项目, 最后一次执行只能读取到 application[-{env}].yml 配置, 不会再读取 bootstrap.yml.
         *
         * @param key      key
         * @param consumer consumer
         * @since 1.0.0
         */
        @Contract(pure = true)
        public static void executeAtLast(String key, CheckedRunnable consumer) {
            executeAt(key, EXECUTE_COUNT.get(), consumer);
        }

        /**
         * 在指定的 index 执行
         *
         * @param key      key
         * @param index    index
         * @param consumer consumer
         * @since 1.0.0
         */
        public static void executeAt(String key, int index, CheckedRunnable consumer) {
            execution(key);
            if (index < 0 || index > EXECUTE_COUNT.get()) {
                throw new StarterException("index error");
            }

            if (getExecuteCurrentCount(key) == index) {
                execute(consumer);
            }
        }

        /**
         * 每次都执行
         *
         * @param consumer consumer
         * @since 1.0.0
         */
        public static void execute(CheckedRunnable consumer) {
            try {
                consumer.run();
            } catch (Throwable throwable) {
                log.error("", throwable);
            }
        }
    }
}
