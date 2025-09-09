package dev.dong4j.zeka.kernel.common.bundle;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

/**
 * <p>国际化配置文件动态绑定基类.
 * <p>为业务端提供动态国际化配置的统一基类，所有业务端都可以继承此类实现自己的业务配置.
 * <p>主要功能：
 * <ul>
 *     <li>继承 AbstractBundle 的所有国际化功能</li>
 *     <li>为业务端提供统一的继承和扩展入口</li>
 *     <li>支持动态路径配置和资源加载</li>
 *     <li>提供默认的单例实现供快速使用</li>
 * </ul>
 * <p>适用场景：
 * <ul>
 *     <li>各个业务模块的国际化配置管理</li>
 *     <li>需要动态配置国际化资源的场景</li>
 *     <li>多租户、多语言应用的资源管理</li>
 *     <li>插件化系统的国际化支持</li>
 * </ul>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.05.19 11:22
 * @since 1.0.0
 */
@Slf4j
public abstract class DynamicBundle extends AbstractBundle {

    /** INSTANCE */
    public static final DynamicBundle INSTANCE = new DynamicBundle("") {
    };

    /**
     * Dynamic bundle
     *
     * @param pathToBundle path to bundle
     * @since 1.0.0
     */
    protected DynamicBundle(@NotNull String pathToBundle) {
        super(pathToBundle);
    }

}
