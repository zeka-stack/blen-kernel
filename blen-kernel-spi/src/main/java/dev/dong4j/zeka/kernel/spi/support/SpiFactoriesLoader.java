package dev.dong4j.zeka.kernel.spi.support;

import dev.dong4j.zeka.kernel.common.context.SpringContext;
import dev.dong4j.zeka.kernel.common.util.StringPool;
import dev.dong4j.zeka.kernel.common.util.StringUtils;
import dev.dong4j.zeka.kernel.spi.extension.SPILoader;
import dev.dong4j.zeka.kernel.spi.extension.factory.SpringExtensionFactory;
import lombok.experimental.UtilityClass;

/**
 * <p>Description: loader </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmaidl.com"
 * @date 2021.02.25 13:50
 * @since 1.0.0
 */
@UtilityClass
public class SpiFactoriesLoader {

    // 第一次调用时，将spring context 放入 SPI Factory 里面
    static {
        SpringExtensionFactory.addApplicationContext(SpringContext.getApplicationContext());
    }

    /**
     * 修改为使用 blen-kernel-spi
     *
     * @param <D>      parameter
     * @param clazz    clazz
     * @param strategy strategy
     * @return the d
     * @since 1.0.0
     */
    public static <D> D shovel(Class<D> clazz, String strategy) {
        SPILoader<D> extensionLoader = SPILoader.getExtensionLoader(clazz);
        return extensionLoader.getExtension(StringUtils.isBlank(strategy) ? StringPool.TRUE : strategy);
    }
}
