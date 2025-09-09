package dev.dong4j.zeka.kernel.common.context;

import dev.dong4j.zeka.kernel.common.constant.ConfigKey;
import dev.dong4j.zeka.kernel.common.util.LoaderUtils;
import dev.dong4j.zeka.kernel.common.util.LowLevelLogUtils;
import dev.dong4j.zeka.kernel.common.util.PropertiesUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.06.04 19:59
 * @since 1.0.0
 */
public final class ThreadContextMapFactory {
    /** THREAD_CONTEXT_KEY */
    private static final String THREAD_CONTEXT_KEY = ConfigKey.PREFIX + "threadContextMap";
    /** ThreadContextMapName */
    private static String threadContextMapName;

    static {
        initPrivate();
    }

    /**
     * Initializes static variables based on system properties. Normally called when this class is initialized by the VM
     * and when Log4j is reconfigured.
     *
     * @since 1.0.0
     */
    public static void init() {
        DefaultThreadContextMap.init();
        initPrivate();
    }

    /**
     * Initializes static variables based on system properties. Normally called when this class is initialized by the VM
     * and when Log4j is reconfigured.
     *
     * @since 1.0.0
     */
    private static void initPrivate() {
        PropertiesUtils properties = PropertiesUtils.getProperties();
        threadContextMapName = properties.getStringProperty(THREAD_CONTEXT_KEY);
    }

    /**
     * Thread context map factory
     *
     * @since 1.0.0
     */
    @Contract(pure = true)
    private ThreadContextMapFactory() {
    }

    /**
     * Create thread context map
     *
     * @return the thread context map
     * @since 1.0.0
     */
    public static @NotNull ThreadContextMap createThreadContextMap() {
        ClassLoader cl = findClassLoader();
        ThreadContextMap result = null;
        if (threadContextMapName != null) {
            try {
                Class<?> clazz = cl.loadClass(threadContextMapName);
                if (ThreadContextMap.class.isAssignableFrom(clazz)) {
                    result = (ThreadContextMap) clazz.getDeclaredConstructor().newInstance();
                }
            } catch (ClassNotFoundException cnfe) {
                LowLevelLogUtils.log("Unable to locate configured ThreadContextMap " + threadContextMapName);
            } catch (Exception ex) {
                LowLevelLogUtils.logException("Unable to create configured ThreadContextMap " + threadContextMapName, ex);
            }
        }

        if (result == null) {
            result = createDefaultThreadContextMap();
        }
        return result;
    }

    /**
     * Create default thread context map
     *
     * @return the thread context map
     * @since 1.0.0
     */
    @Contract(" -> new")
    private static @NotNull ThreadContextMap createDefaultThreadContextMap() {
        return new DefaultThreadContextMap(true);
    }

    /**
     * Find class loader
     *
     * @return the class loader
     * @since 1.0.0
     */
    public static ClassLoader findClassLoader() {
        return LoaderUtils.getThreadContextClassLoader();
    }
}
