package dev.dong4j.zeka.kernel.common.bundle;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

/**
 * <p>Description: 国际化配置文件动态绑定基类, 所有的业务端都继承此类实现自己的业务配置</p>
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
