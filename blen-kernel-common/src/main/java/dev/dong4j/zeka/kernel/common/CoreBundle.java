package dev.dong4j.zeka.kernel.common;

import dev.dong4j.zeka.kernel.common.bundle.DynamicBundle;
import java.util.function.Supplier;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 * <p>Description: 消息外置, 需要在 resources/messages 创建对应的 [CoreBundle.properties] 文件 </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.05.19 09:26
 * @since 1.0.0
 */
public final class CoreBundle extends DynamicBundle {
    /** BUNDLE */
    @NonNls
    private static final String BUNDLE = "messages.CoreBundle";
    /** INSTANCE */
    private static final CoreBundle INSTANCE = new CoreBundle();

    /**
     * Plugin bundle
     *
     * @since 1.0.0
     */
    @Contract(pure = true)
    private CoreBundle() {
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
