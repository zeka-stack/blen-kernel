package dev.dong4j.zeka.kernel.autoconfigure.task;

import dev.dong4j.zeka.kernel.common.constant.BasicConstant;
import dev.dong4j.zeka.kernel.common.start.ZekaAutoConfiguration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.task.TaskExecutionProperties;
import org.springframework.boot.autoconfigure.task.TaskExecutionProperties.Shutdown;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.task.ThreadPoolTaskExecutorBuilder;
import org.springframework.boot.task.ThreadPoolTaskExecutorCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.TaskDecorator;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 消费者任务执行自动配置类，为 Spring Boot 应用提供定制化的线程池任务执行器
 * <p>
 * 该自动配置类基于 Spring Boot 的任务执行配置，提供了增强版的线程池管理功能
 * 包括 MDC 日志上下文传递、线程池状态监控、自定义拒绝策略等特性
 * <p>
 * 主要功能：
 * - 提供可配置的线程池构建器
 * - 支持 MDC 日志上下文在线程间传递
 * - 提供统一的线程池实例管理
 * - 支持业务端自定义线程池配置
 * - 提供合理的默认拒绝策略
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.11.21 22:34
 * @since 1.0.0
 */
@Slf4j
@ConditionalOnClass(ThreadPoolTaskExecutor.class)
@AutoConfiguration
@EnableConfigurationProperties(TaskExecutionProperties.class)
public class ConsumerTaskExecutionAutoConfiguration implements ZekaAutoConfiguration {

    /**
     * 任务执行器构建器，用于创建可配置的线程池任务执行器
     * <p>
     * 根据 Spring Boot 的任务执行属性进行配置，支持自定义线程池参数
     * 支持注入业务端的自定义配置和任务装饰器
     *
     * @param properties              Spring Boot 任务执行属性配置
     * @param taskExecutorCustomizers 线程池任务执行器业务端自定义配置器
     * @param taskDecorator           任务装饰器，用于在任务执行前后添加额外逻辑
     * @return 配置好的任务执行器构建器
     * @since 1.0.0
     */
    @Bean
    @Primary
    public ThreadPoolTaskExecutorBuilder taskExecutorBuilder(@NotNull TaskExecutionProperties properties,
                                                             @NotNull ObjectProvider<ThreadPoolTaskExecutorCustomizer> taskExecutorCustomizers,
                                                             @NotNull ObjectProvider<TaskDecorator> taskDecorator) {
        TaskExecutionProperties.Pool pool = properties.getPool();
        ThreadPoolTaskExecutorBuilder builder = new ThreadPoolTaskExecutorBuilder();

        // 配置队列大小
        builder.queueCapacity(pool.getQueueCapacity());
        // 配置核心线程数
        builder.corePoolSize(pool.getCoreSize());
        // 配置最大线程数
        builder.maxPoolSize(pool.getMaxSize());
        builder.allowCoreThreadTimeOut(pool.isAllowCoreThreadTimeout());
        builder.keepAlive(pool.getKeepAlive());

        Shutdown shutdown = properties.getShutdown();
        builder.awaitTermination(shutdown.isAwaitTermination());
        builder.awaitTerminationPeriod(shutdown.getAwaitTerminationPeriod());
        // 配置线程池中的线程的名称前缀
        builder.threadNamePrefix(properties.getThreadNamePrefix());
        builder.customizers(taskExecutorCustomizers.orderedStream()::iterator);
        builder.taskDecorator(taskDecorator.getIfUnique());

        return builder;
    }

    /**
     * 创建增强型线程池任务执行器
     * <p>
     * 使用懒加载和主要 Bean 注解，确保在需要时才创建实例
     * 提供统一的线程池实例，供全局使用
     *
     * @param builder 预配置的任务执行器构建器
     * @return 配置好的线程池任务执行器
     * @since 1.0.0
     */
    @Lazy
    @Primary
    @Bean(name = BasicConstant.BOOST_EXECUTOR)
    public ThreadPoolTaskExecutor boostExecutor(@NotNull ThreadPoolTaskExecutorBuilder builder) {
        return builder.build();
    }

    /**
     * 创建线程池执行器服务
     * <p>
     * 将 Spring 的 ThreadPoolTaskExecutor 包装为标准的 ExecutorService
     * 便于与原生 JDK 线程池 API 集成使用
     *
     * @param boostExecutor 增强型线程池任务执行器
     * @return 标准的 JDK ExecutorService 实例
     * @since 1.0.0
     */
    @Bean(name = BasicConstant.BOOST_EXECUTORSERVICE)
    @ConditionalOnMissingBean(ExecutorService.class)
    public ExecutorService boostExecutorService(@NotNull ThreadPoolTaskExecutor boostExecutor) {
        return boostExecutor.getThreadPoolExecutor();
    }

    /**
     * 创建 MDC 任务装饰器
     * <p>
     * 用于在线程池中传递 MDC 日志上下文，确保在异步任务中能够正常记录日志
     * 解决多线程环境下日志上下文丢失的问题
     *
     * @return MDC 任务装饰器实例
     * @since 1.0.0
     */
    @Bean
    public TaskDecorator mdcTaskDecorator() {
        return new MdcTaskDecorator();
    }

    /**
     * 创建消费者任务执行器自定义配置器
     * <p>
     * 配置线程池的拒绝策略为 CallerRunsPolicy，即当线程池满时
     * 由调用者线程来执行任务，避免任务丢失并提供反压效果
     *
     * @return 线程池任务执行器自定义配置器
     * @since 1.0.0
     */
    @Bean
    public ThreadPoolTaskExecutorCustomizer consumerTaskExecutorCustomizer() {
        // 当 pool 已经达到 max size 的时候, 如何处理新任务: CALLER_RUNS: 不在新线程中执行任务, 而是由调用者所在的线程来执行
        return taskExecutor -> taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
    }

}
