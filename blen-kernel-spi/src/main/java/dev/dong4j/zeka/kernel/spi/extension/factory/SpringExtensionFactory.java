package dev.dong4j.zeka.kernel.spi.extension.factory;


import dev.dong4j.zeka.kernel.spi.extension.ExtensionFactory;
import dev.dong4j.zeka.kernel.spi.extension.SPI;
import dev.dong4j.zeka.kernel.spi.utils.ConcurrentHashSet;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmaidl.com"
 * @date 2021.02.26 17:47
 * @since 1.0.0
 */
@Slf4j
public class SpringExtensionFactory implements ExtensionFactory {

    /** CONTEXTS */
    private static final Set<ApplicationContext> CONTEXTS = new ConcurrentHashSet<>();

    /**
     * Add application context
     *
     * @param context context
     * @since 1.0.0
     */
    public static void addApplicationContext(ApplicationContext context) {
        CONTEXTS.add(context);
        if (context instanceof ConfigurableApplicationContext) {
            ((ConfigurableApplicationContext) context).registerShutdownHook();
        }
    }

    /**
     * Remove application context
     *
     * @param context context
     * @since 1.0.0
     */
    public static void removeApplicationContext(ApplicationContext context) {
        CONTEXTS.remove(context);
    }

    /**
     * Gets contexts *
     *
     * @return the contexts
     * @since 1.0.0
     */
    public static Set<ApplicationContext> getContexts() {
        return CONTEXTS;
    }

    /**
     * Clear contexts
     *
     * @since 1.0.0
     */
    public static void clearContexts() {
        CONTEXTS.clear();
    }

    /**
     * Gets extension *
     *
     * @param <T>  parameter
     * @param type type
     * @param name name
     * @return the extension
     * @since 1.0.0
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> T getExtension(Class<T> type, String name) {

        // SPI should be get from SpiExtensionFactory
        if (type.isInterface() && type.isAnnotationPresent(SPI.class)) {
            return null;
        }

        for (ApplicationContext context : CONTEXTS) {
            if (context.containsBean(name)) {
                Object bean = context.getBean(name);
                if (type.isInstance(bean)) {
                    return (T) bean;
                }
            }
        }

        log.warn("No spring extension (bean) named:" + name + ", try to find an extension (bean) of type " + type.getName());

        if (Object.class == type) {
            return null;
        }

        for (ApplicationContext context : CONTEXTS) {
            try {
                return context.getBean(type);
            } catch (NoUniqueBeanDefinitionException multiBeanExe) {
                log.warn("Find more than 1 spring extensions (beans) of type " + type.getName() + ", will stop auto injection. Please "
                    + "make sure you have specified the concrete parameter type and there's only one extension of that type.");
            } catch (NoSuchBeanDefinitionException noBeanExe) {
                if (log.isDebugEnabled()) {
                    log.debug("Error when get spring extension(bean) for type:" + type.getName(), noBeanExe);
                }
            }
        }

        log.warn("No spring extension (bean) named:" + name + ", type:" + type.getName() + " found, stop get bean.");

        return null;
    }

}
