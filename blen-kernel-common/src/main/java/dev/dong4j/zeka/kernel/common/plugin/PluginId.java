package dev.dong4j.zeka.kernel.common.plugin;

import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * <p>Description: 插件 Id 定义 </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.05.19 14:38
 * @since 1.0.0
 */
public final class PluginId implements Comparable<PluginId> {
    /** EMPTY_ARRAY */
    public static final PluginId[] EMPTY_ARRAY = new PluginId[0];

    /** ourRegisteredIds */
    private static final Map<String, PluginId> PLUGIN_ID_MAP = new HashMap<>(16);
    /** My id string */
    private final String myIdString;

    /**
     * Gets id *
     *
     * @param idString id string
     * @return the id
     * @since 1.0.0
     */
    @NotNull
    public static synchronized PluginId getId(@NotNull String idString) {
        return PLUGIN_ID_MAP.computeIfAbsent(idString, PluginId::new);
    }

    /**
     * Plugin id
     *
     * @param idString id string
     * @since 1.0.0
     */
    @Contract(pure = true)
    private PluginId(@NotNull String idString) {
        this.myIdString = idString;
    }

    /**
     * Find id
     *
     * @param idStrings id strings
     * @return the plugin id
     * @since 1.0.0
     */
    @Nullable
    public static synchronized PluginId findId(@NotNull String... idStrings) {
        for (String idString : idStrings) {
            PluginId pluginId = PLUGIN_ID_MAP.get(idString);
            if (pluginId != null) {
                return pluginId;
            }
        }
        return null;
    }

    /**
     * Gets registered ids *
     *
     * @return the registered ids
     * @since 1.0.0
     */
    @Contract(" -> new")
    @NotNull
    public static synchronized Map<String, PluginId> getRegisteredIds() {
        return new HashMap<>(PLUGIN_ID_MAP);
    }

    /**
     * Compare to
     *
     * @param o o
     * @return the int
     * @since 1.0.0
     */
    @Contract(pure = true)
    @Override
    public int compareTo(@NotNull PluginId o) {
        return this.myIdString.compareTo(o.myIdString);
    }

    /**
     * To string
     *
     * @return the string
     * @since 1.0.0
     */
    @Contract(pure = true)
    @Override
    public @NotNull String toString() {
        return this.getIdString();
    }

    /**
     * Gets id string *
     *
     * @return the id string
     * @since 1.0.0
     */
    @Contract(pure = true)
    @NotNull
    public String getIdString() {
        return this.myIdString;
    }
}
