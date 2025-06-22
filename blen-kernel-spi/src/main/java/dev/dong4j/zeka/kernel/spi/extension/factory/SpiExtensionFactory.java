package dev.dong4j.zeka.kernel.spi.extension.factory;


import dev.dong4j.zeka.kernel.spi.extension.ExtensionFactory;
import dev.dong4j.zeka.kernel.spi.extension.SPI;
import dev.dong4j.zeka.kernel.spi.extension.SPILoader;

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.8.0
 * @email "mailto:dong4j@gmaidl.com"
 * @date 2021.02.26 17:47
 * @since 1.8.0
 */
public class SpiExtensionFactory implements ExtensionFactory {

    /**
     * Gets extension *
     *
     * @param <T>  parameter
     * @param type type
     * @param name name
     * @return the extension
     * @since 1.8.0
     */
    @Override
    public <T> T getExtension(Class<T> type, String name) {
        if (type.isInterface() && type.isAnnotationPresent(SPI.class)) {
            SPILoader<T> loader = SPILoader.getExtensionLoader(type);
            if (!loader.getSupportedExtensions().isEmpty()) {
                return loader.getAdaptiveExtension();
            }
        }
        return null;
    }

}
