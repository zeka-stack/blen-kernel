package dev.dong4j.zeka.kernel.common.plugin;

import org.jetbrains.annotations.NotNull;
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
     * Gets description *
     *
     * @return the description
     * @since 2024.1.1
     */
    @Nullable
    default String getDescription() {
        return null;
    }

    /**
     * Gets name *
     *
     * @return the name
     * @since 2024.1.1
     */
    @NotNull
    String getName();

    /**
     * Gets version *
     *
     * @return the version
     * @since 2024.1.1
     */
    @Nullable
    default String getVersion() {
        return null;
    }
}
