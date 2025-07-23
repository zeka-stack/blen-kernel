package dev.dong4j.zeka.kernel.auth.context;

import dev.dong4j.zeka.kernel.common.constant.ConfigKey;
import java.lang.reflect.Constructor;
import lombok.Getter;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.09.15 01:41
 * @since 1.6.0
 */
public class SecurityContextHolder {

    /** MODE_THREADLOCAL */
    public static final String MODE_THREADLOCAL = "MODE_THREADLOCAL";
    /** MODE_INHERITABLETHREADLOCAL */
    public static final String MODE_INHERITABLETHREADLOCAL = "MODE_INHERITABLETHREADLOCAL";
    /** MODE_GLOBAL */
    public static final String MODE_GLOBAL = "MODE_GLOBAL";
    /** SYSTEM_PROPERTY */
    public static final String SYSTEM_PROPERTY = ConfigKey.PREFIX + "security.strategy";
    /** strategyName */
    private static String strategyName = System.getProperty(SYSTEM_PROPERTY);
    /** strategy */
    private static SecurityContextHolderStrategy strategy;
    /** initializeCount */
    @Getter
    private static int initializeCount = 0;

    static {
        initialize();
    }

    /**
     * Clear context
     *
     * @since 1.6.0
     */
    public static void clearContext() {
        strategy.clearContext();
    }

    /**
     * Gets context *
     *
     * @return the context
     * @since 1.6.0
     */
    public static SecurityContext getContext() {
        return strategy.getContext();
    }

    /**
     * Initialize
     *
     * @since 1.6.0
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
     * Sets context *
     *
     * @param context context
     * @since 1.6.0
     */
    public static void setContext(SecurityContext context) {
        strategy.setContext(context);
    }

    /**
     * Sets strategy name *
     *
     * @param strategyName strategy name
     * @since 1.6.0
     */
    public static void setStrategyName(String strategyName) {
        SecurityContextHolder.strategyName = strategyName;
        initialize();
    }

    /**
     * Gets context holder strategy *
     *
     * @return the context holder strategy
     * @since 1.6.0
     */
    public static SecurityContextHolderStrategy getContextHolderStrategy() {
        return strategy;
    }

    /**
     * Create empty context
     *
     * @return the security context
     * @since 1.6.0
     */
    public static SecurityContext createEmptyContext() {
        return strategy.createEmptyContext();
    }

    /**
     * To string
     *
     * @return the string
     * @since 1.6.0
     */
    @Override
    public String toString() {
        return "SecurityContextHolder[strategy='" + strategyName + "'; initializeCount="
            + initializeCount + "]";
    }
}
