package dev.dong4j.zeka.kernel.spi.extension.factory;


import dev.dong4j.zeka.kernel.spi.extension.Adaptive;
import dev.dong4j.zeka.kernel.spi.extension.ExtensionFactory;
import dev.dong4j.zeka.kernel.spi.extension.SPILoader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.8.0
 * @email "mailto:dong4j@gmaidl.com"
 * @date 2021.02.26 17:47
 * @since 1.8.0
 */
@Adaptive
public class AdaptiveExtensionFactory implements ExtensionFactory {

    /** Factories */
    private final List<ExtensionFactory> factories;

    /**
     * Adaptive extension factory
     *
     * @since 1.8.0
     */
    public AdaptiveExtensionFactory() {
        SPILoader<ExtensionFactory> loader = SPILoader.getExtensionLoader(ExtensionFactory.class);
        List<ExtensionFactory> list = new ArrayList<>();
        for (String name : loader.getSupportedExtensions()) {
            list.add(loader.getExtension(name));
        }
        this.factories = Collections.unmodifiableList(list);
    }

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
        for (ExtensionFactory factory : this.factories) {
            T extension = factory.getExtension(type, name);
            if (extension != null) {
                return extension;
            }
        }
        return null;
    }

}
