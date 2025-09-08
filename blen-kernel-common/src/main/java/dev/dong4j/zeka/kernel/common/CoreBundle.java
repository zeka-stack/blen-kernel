package dev.dong4j.zeka.kernel.common;

import dev.dong4j.zeka.kernel.common.bundle.DynamicBundle;
import java.util.function.Supplier;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 * 核心消息国际化管理器，提供框架级别的国际化支持
 * <p>
 * 该类继承自DynamicBundle，为Zeka框架的核心模块提供国际化消息管理能力
 * 支持多语言消息配置、动态消息加载和参数化消息格式化
 * <p>
 * 主要特性：
 * - 单例模式：保证全局只有一个消息管理器实例
 * - 静态访问：提供便捷的静态方法访问消息资源
 * - 参数化支持：支持MessageFormat风格的参数替换
 * - 懒加载：支持按需加载消息内容，提高性能
 * - 国际化支持：支持多语言和本地化配置
 * <p>
 * 配置要求：
 * 需要在resources/messages目录下创建对应的CoreBundle.properties文件
 * 支持多语言配置：
 * - CoreBundle.properties（默认语言）
 * - CoreBundle_zh.properties（中文）
 * - CoreBundle_en.properties（英文）
 * <p>
 * 使用场景：
 * - 框架内部的错误消息和提示信息的国际化
 * - 系统日志中的多语言消息输出
 * - 异常信息的本地化处理
 * - API返回结果中的国际化文本
 * - 用户界面的多语言支持
 * <p>
 * 使用示例：
 * <pre>{@code
 * // 获取简单消息
 * String msg = CoreBundle.message("error.invalid.param");
 *
 * // 获取参数化消息
 * String msg = CoreBundle.message("user.not.found", "userId", 1001);
 *
 * // 懒加载消息（适用于性能敏感场景）
 * Supplier<String> msgSupplier = CoreBundle.messagePointer("operation.success");
 * String msg = msgSupplier.get(); // 在需要时才实际加载
 * }</pre>
 * <p>
 * 注意事项：
 * - 该类使用final修饰，不允许继承
 * - 单例实例在类加载时创建，线程安全
 * - 消息文件缺失时会使用默认的错误处理机制
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.05.19 09:26
 * @since 1.0.0
 */
public final class CoreBundle extends DynamicBundle {
    /** 消息资源文件的基本名称，位于resources/messages目录下 */
    @NonNls
    private static final String BUNDLE = "messages.CoreBundle";
    /** 单例实例，保证全局只有一个消息管理器 */
    private static final CoreBundle INSTANCE = new CoreBundle();

    /**
     * 私有构造函数，实现单例模式
     * <p>
     * 调用父类DynamicBundle的构造函数，初始化消息资源
     * 使用Contract注解标记为纯函数，不产生副作用
     *
     * @since 1.0.0
     */
    @Contract(pure = true)
    private CoreBundle() {
        super(BUNDLE);
    }

    /**
     * 获取国际化消息内容（静态方法）
     * <p>
     * 根据消息键和参数获取对应的本地化消息内容
     * 支持MessageFormat风格的参数替换，可以处理动态内容
     * <p>
     * 使用示例：
     * <pre>{@code
     * // 简单消息
     * String msg = CoreBundle.message("welcome.message");
     *
     * // 参数化消息
     * String msg = CoreBundle.message("user.login.success", userName, loginTime);
     * }</pre>
     *
     * @param key    消息键，对应properties文件中的属性名
     * @param params 消息参数数组，用于替换消息中的占位符
     * @return 格式化后的消息内容
     * @since 1.0.0
     */
    @NotNull
    public static String message(@NotNull String key, Object... params) {
        return INSTANCE.getMessage(key, params);
    }

    /**
     * 获取消息的懒加载供应商（静态方法）
     * <p>
     * 返回一个Supplier函数式接口，允许在需要时才实际加载和格式化消息
     * 这种方式适用于性能敏感的场景，避免不必要的消息处理
     * <p>
     * 使用示例：
     * <pre>{@code
     * // 创建懒加载供应商
     * Supplier<String> msgSupplier = CoreBundle.messagePointer("expensive.operation.result", result);
     *
     * // 在需要时才获取消息
     * if (shouldShowMessage) {
     *     String message = msgSupplier.get();
     *     logger.info(message);
     * }
     * }</pre>
     *
     * @param key    消息键，对应properties文件中的属性名
     * @param params 消息参数数组，用于替换消息中的占位符
     * @return 消息内容的懒加载供应商
     * @since 1.0.0
     */
    public static @NotNull Supplier<String> messagePointer(@NotNull String key, Object... params) {
        return INSTANCE.getLazyMessage(key, params);
    }
}
