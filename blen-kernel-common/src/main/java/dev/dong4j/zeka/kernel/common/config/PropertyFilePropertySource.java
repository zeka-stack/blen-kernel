package dev.dong4j.zeka.kernel.common.config;

import dev.dong4j.zeka.kernel.common.util.LoaderUtils;
import dev.dong4j.zeka.kernel.common.util.LowLevelLogUtils;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

/**
 * <p>Description: 通过配置文件加载配置资源 </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.06.04 19:21
 * @since 1.4.0
 */
@Slf4j
public class PropertyFilePropertySource extends PropertiesPropertySource {

    /**
     * Property file property source
     *
     * @param fileName file name
     * @since 1.5.0
     */
    public PropertyFilePropertySource(String fileName) {
        super(loadPropertiesFile(fileName));
    }

    /**
     * Load properties file
     *
     * @param fileName file name
     * @return the properties
     * @since 1.5.0
     */
    private static @NotNull Properties loadPropertiesFile(String fileName) {
        Properties props = new Properties();
        for (URL url : LoaderUtils.findResources(fileName)) {
            log.info("Loading properties file: {}", url.toString());
            try (InputStream in = url.openStream()) {
                props.load(in);
            } catch (IOException e) {
                LowLevelLogUtils.logException("Unable to read " + url, e);
            }
        }
        return props;
    }

    /**
     * Gets priority *
     *
     * @return the priority
     * @since 1.5.0
     */
    @Override
    public int getPriority() {
        return 0;
    }

}
