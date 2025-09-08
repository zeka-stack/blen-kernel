package dev.dong4j.zeka.kernel.common.bundle;

import java.lang.reflect.Method;
import java.util.Locale;
import java.util.ResourceBundle;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * <p>Description: 国际化配置文件绑定工具类 </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.05.19 09:40
 * @since 1.0.0
 */
@Slf4j
@UtilityClass
@SuppressWarnings("checkstyle:ModifierOrder")
public class BundleUtils {
    /** SET_PARENT */
    private static final Method SET_PARENT = getDeclaredMethod(ResourceBundle.class, "setParent", ResourceBundle.class);

    /**
     * Load language bundle
     *
     * @param pluginClassLoader plugin class loader
     * @param name              name
     * @return the resource bundle
     * @since 1.0.0
     */
    @Contract("null, _ -> null")
    @Nullable
    public static ResourceBundle loadLanguageBundle(@Nullable ClassLoader pluginClassLoader, String name) {
        if (pluginClassLoader == null) {
            return null;
        }
        ResourceBundle.Control control = ResourceBundle.Control.getControl(ResourceBundle.Control.FORMAT_PROPERTIES);
        ResourceBundle pluginBundle = ResourceBundle.getBundle(name, Locale.getDefault(), pluginClassLoader, control);

        if (pluginBundle == null) {
            return null;
        }
        ResourceBundle base = ResourceBundle.getBundle(name);
        try {
            if (SET_PARENT != null) {
                SET_PARENT.invoke(pluginBundle, base);
            }
        } catch (Exception e) {
            log.warn("", e);
            return null;
        }

        return pluginBundle;
    }

    /**
     * Gets declared method *
     *
     * @param clazz      a class
     * @param name       name
     * @param parameters parameters
     * @return the declared method
     * @since 1.0.0
     */
    @Nullable
    public static Method getDeclaredMethod(@NotNull Class<?> clazz, @NonNls @NotNull String name, Class<?>... parameters) {
        try {
            return makeAccessible(clazz.getDeclaredMethod(name, parameters));
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    /**
     * Make accessible
     *
     * @param method method
     * @return the method
     * @since 1.0.0
     */
    @Contract("_ -> param1")
    @SuppressWarnings("java:S3011")
    private static @NotNull Method makeAccessible(@NotNull Method method) {
        method.setAccessible(true);
        return method;
    }

}
