package dev.dong4j.zeka.kernel.auth.context;

import dev.dong4j.zeka.kernel.common.constant.ConfigKey;
import java.lang.reflect.Constructor;
import lombok.Getter;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

/**
 * 安全上下文持有者，提供多种策略来存储和管理安全上下文信息
 * <p>
 * 该类作为安全框架的中心组件，负责管理用户认证信息的存储和访问策略
 * 支持三种不同的存储模式，适应不同的应用场景和线程安全需求
 * <p>
 * 支持的存储策略：
 * - ThreadLocal：每个线程拥有独立的安全上下文，不支持子线程继承
 * - InheritableThreadLocal：支持子线程继承父线程的安全上下文
 * - Global：全局共享安全上下文，适用于单线程应用
 * <p>
 * 线程安全的用户认证信息传递和管理，确保多线程环境下的数据安全
 * 可通过系统属性或配置动态选择存储策略，也支持自定义策略实现
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.09.15 01:41
 * @since 1.0.0
 */
public class SecurityContextHolder {

    /** ThreadLocal 存储模式常量 */
    public static final String MODE_THREADLOCAL = "MODE_THREADLOCAL";
    /** InheritableThreadLocal 存储模式常量 */
    public static final String MODE_INHERITABLETHREADLOCAL = "MODE_INHERITABLETHREADLOCAL";
    /** 全局存储模式常量 */
    public static final String MODE_GLOBAL = "MODE_GLOBAL";
    /** 系统属性键，用于配置存储策略 */
    public static final String SYSTEM_PROPERTY = ConfigKey.PREFIX + "security.strategy";
    /** 当前使用的策略名称 */
    private static String strategyName = System.getProperty(SYSTEM_PROPERTY);
    /** 当前的安全上下文持有策略实例 */
    private static SecurityContextHolderStrategy strategy;
    /** 初始化计数器 */
    @Getter
    private static int initializeCount = 0;

    static {
        initialize();
    }

    /**
     * 清除当前线程的安全上下文
     *
     * @since 1.0.0
     */
    public static void clearContext() {
        strategy.clearContext();
    }

    /**
     * 获取当前线程的安全上下文
     *
     * @return 安全上下文对象，包含用户认证信息
     * @since 1.0.0
     */
    public static SecurityContext getContext() {
        return strategy.getContext();
    }

    /**
     * 初始化安全上下文持有者，根据配置选择合适的存储策略
     *
     * @since 1.0.0
     */
    private static void initialize() {
        if (!StringUtils.hasText(strategyName)) {
            // Set default
            strategyName = MODE_THREADLOCAL;
        }

        switch (strategyName) {
            case MODE_THREADLOCAL:
                strategy = new ThreadLocalSecurityContextHolderStrategy();
                break;
            case MODE_INHERITABLETHREADLOCAL:
                strategy = new InheritableThreadLocalSecurityContextHolderStrategy();
                break;
            case MODE_GLOBAL:
                strategy = new GlobalSecurityContextHolderStrategy();
                break;
            default:
                try {
                    Class<?> clazz = Class.forName(strategyName);
                    Constructor<?> customStrategy = clazz.getConstructor();
                    strategy = (SecurityContextHolderStrategy) customStrategy.newInstance();
                } catch (Exception ex) {
                    ReflectionUtils.handleReflectionException(ex);
                }
                break;
        }

        initializeCount++;
    }

    /**
     * 设置当前线程的安全上下文
     *
     * @param context 安全上下文对象，包含用户认证信息
     * @since 1.0.0
     */
    public static void setContext(SecurityContext context) {
        strategy.setContext(context);
    }

    /**
     * 设置存储策略名称并重新初始化
     *
     * @param strategyName 策略名称，支持预定义策略或自定义类名
     * @since 1.0.0
     */
    public static void setStrategyName(String strategyName) {
        SecurityContextHolder.strategyName = strategyName;
        initialize();
    }

    /**
     * 获取当前使用的上下文持有策略
     *
     * @return 安全上下文持有策略实例
     * @since 1.0.0
     */
    public static SecurityContextHolderStrategy getContextHolderStrategy() {
        return strategy;
    }

    /**
     * 创建空的安全上下文实例
     *
     * @return 新的空安全上下文对象
     * @since 1.0.0
     */
    public static SecurityContext createEmptyContext() {
        return strategy.createEmptyContext();
    }

    /**
     * 返回对象的字符串表示
     *
     * @return 包含策略名称和初始化计数的字符串
     * @since 1.0.0
     */
    @Override
    public String toString() {
        return "SecurityContextHolder[strategy='" + strategyName + "'; initializeCount="
            + initializeCount + "]";
    }
}
