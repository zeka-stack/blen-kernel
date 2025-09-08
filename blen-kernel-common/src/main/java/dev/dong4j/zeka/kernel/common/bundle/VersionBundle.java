package dev.dong4j.zeka.kernel.common.bundle;

import java.util.function.Supplier;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 * <p>Description: 框架版本, 通过 maven 编译时将 ${project.version} 替换为当前版本, 因此不需要每次手动修改 </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.05.22 14:55
 * @since 1.0.0
 */
public final class VersionBundle extends DynamicBundle {
    /** BUNDLE */
    @NonNls
    private static final String BUNDLE = "messages.VersionBundle";
    /** INSTANCE */
    private static final VersionBundle INSTANCE = new VersionBundle();

    /**
     * Plugin bundle
     *
     * @since 1.0.0
     */
    @Contract(pure = true)
    private VersionBundle() {
        super(BUNDLE);
    }

    /**
     * Message
     *
     * @param key    key
     * @param params params
     * @return the string
     * @since 1.0.0
     */
    @NotNull
    public static String message(@NotNull String key, Object... params) {
        return INSTANCE.getMessage(key, params);
    }

    /**
     * Message pointer
     *
     * @param key    key
     * @param params params
     * @return the supplier
     * @since 1.0.0
     */
    public static @NotNull Supplier<String> messagePointer(@NotNull String key, Object... params) {
        return INSTANCE.getLazyMessage(key, params);
    }
}

