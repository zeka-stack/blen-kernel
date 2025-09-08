package dev.dong4j.zeka.kernel.common.plugin;

import java.io.File;
import java.nio.file.Path;
import org.jetbrains.annotations.Nullable;

/**
 * <p>Description: 插件元数据接口</p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.05.19 14:39
 * @since 1.0.0
 */
public interface PluginDescriptor {

    /**
     * Gets plugin id *
     *
     * @return the plugin id
     * @since 1.0.0
     */
    PluginId getPluginId();

    /**
     * Gets plugin class loader *
     *
     * @return the plugin class loader
     * @since 1.0.0
     */
    ClassLoader getPluginClassLoader();

    /**
     * Is bundled
     *
     * @return the boolean
     * @since 1.0.0
     */
    default boolean isBundled() {
        return false;
    }

    /**
     * Gets path *
     *
     * @return the path
     * @since 1.0.0
     */
    File getPath();

    /**
     * Gets plugin path *
     *
     * @return the plugin path
     * @since 1.0.0
     */
    Path getPluginPath();

    /**
     * Gets description *
     *
     * @return the description
     * @since 1.0.0
     */
    @Nullable
    String getDescription();

    /**
     * Gets change notes *
     *
     * @return the change notes
     * @since 1.0.0
     */
    String getChangeNotes();

    /**
     * Gets name *
     *
     * @return the name
     * @since 1.0.0
     */
    String getName();

    /**
     * Gets vendor *
     *
     * @return the vendor
     * @since 1.0.0
     */
    String getVendor();

    /**
     * Gets version *
     *
     * @return the version
     * @since 1.0.0
     */
    String getVersion();

    /**
     * Is enabled
     *
     * @return the boolean
     * @since 1.0.0
     */
    boolean isEnabled();

    /**
     * Sets enabled *
     *
     * @param enabled enabled
     * @since 1.0.0
     */
    void setEnabled(boolean enabled);
}
