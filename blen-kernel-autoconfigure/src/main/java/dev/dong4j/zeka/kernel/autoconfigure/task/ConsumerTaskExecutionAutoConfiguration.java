package dev.dong4j.zeka.kernel.autoconfigure.task;

import dev.dong4j.zeka.kernel.common.constant.BasicConstant;
import dev.dong4j.zeka.kernel.common.start.ZekaAutoConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.task.TaskExecutionProperties;
import org.springframework.boot.autoconfigure.task.TaskExecutionProperties.Shutdown;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.task.TaskExecutorBuilder;
import org.springframework.boot.task.TaskExecutorCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.TaskDecorator;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for {@link TaskExecutor}.
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.11.21 22:34
 * @since 1.7.0
 */
@Slf4j
@ConditionalOnClass(ThreadPoolTaskExecutor.class)
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(TaskExecutionProperties.class)
public class ConsumerTaskExecutionAutoConfiguration implements ZekaAutoConfiguration {

    /**
     * Task executor builder
     *
     * @param properties              properties
     * @param taskExecutorCustomizers ThreadPoolTaskExecutor 业务端配置
     * @param taskDecorator           task decorator
     * @return the task executor builder
     * @since 1.7.0
     */
    @Bean
    @Primary
    public TaskExecutorBuilder taskExecutorBuilder(@NotNull TaskExecutionProperties properties,
                                                   @NotNull ObjectProvider<TaskExecutorCustomizer> taskExecutorCustomizers,
                                                   @NotNull ObjectProvider<TaskDecorator> taskDecorator) {
        TaskExecutionProperties.Pool pool = properties.getPool();
        TaskExecutorBuilder builder = new TaskExecutorBuilder();
        // 配置队列大小
        builder = builder.queueCapacity(pool.getQueueCapacity());
        // 配置核心线程数
        builder = builder.corePoolSize(pool.getCoreSize());
        // 配置最大线程数
        builder = builder.maxPoolSize(pool.getMaxSize());
        builder = builder.allowCoreThreadTimeOut(pool.isAllowCoreThreadTimeout());
        builder = builder.keepAlive(pool.getKeepAlive());
        Shutdown shutdown = properties.getShutdown();
        builder = builder.awaitTermination(shutdown.isAwaitTermination());
        builder = builder.awaitTerminationPeriod(shutdown.getAwaitTerminationPeriod());
        // 配置线程池中的线程的名称前缀
        builder = builder.threadNamePrefix(properties.getThreadNamePrefix());
        builder = builder.customizers(taskExecutorCustomizers.orderedStream()::iterator);
        builder = builder.taskDecorator(taskDecorator.getIfUnique());

        return builder;
    }

    /**
     * Application task executor
     *
     * @param builder builder
     * @return the thread pool task executor
     * @since 1.7.0
     */
    @Lazy
    @Primary
    @Bean(name = BasicConstant.BOOST_EXECUTOR)
    public ThreadPoolTaskExecutor boostExecutor(@NotNull TaskExecutorBuilder builder) {
        return builder.build(VisiableThreadPoolTaskExecutor.class);
    }

    /**
     * Executor service
     *
     * @param boostExecutor boost executor
     * @return the executor service
     * @since 1.7.0
     */
    @Bean(name = BasicConstant.BOOST_EXECUTORSERVICE)
    @ConditionalOnMissingBean(ExecutorService.class)
    public ExecutorService boostExecutorService(@NotNull ThreadPoolTaskExecutor boostExecutor) {
        return boostExecutor.getThreadPoolExecutor();
    }

    /**
     * Mdc task decorator
     *
     * @return the task decorator
     * @since 1.7.0
     */
    @Bean
    public TaskDecorator mdcTaskDecorator() {
        return new MdcTaskDecorator();
    }

    /**
     * Task executor customizer
     *
     * @return the task executor customizer
     * @since 1.7.0
     */
    @Bean
    public TaskExecutorCustomizer consumerTaskExecutorCustomizer() {
        // 当 pool 已经达到 max size 的时候, 如何处理新任务: CALLER_RUNS: 不在新线程中执行任务, 而是由调用者所在的线程来执行
        return taskExecutor -> taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
    }

}
