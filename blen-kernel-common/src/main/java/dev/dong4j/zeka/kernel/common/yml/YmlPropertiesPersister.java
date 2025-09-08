package dev.dong4j.zeka.kernel.common.yml;

import com.google.common.collect.Maps;
import dev.dong4j.zeka.kernel.common.constant.ConfigKey;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.util.DefaultPropertiesPersister;

/**
 * <p>Description:  </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dongshijie@gmail.com"
 * @date 2020.01.27 01:05
 * @since 1.0.0
 */
@Slf4j
public class YmlPropertiesPersister extends DefaultPropertiesPersister {

    /** Config name */
    static final String CONFIG_NAME = ConfigKey.PREFIX + "consumer.config.file.name";

    /**
     * Load *
     *
     * @param props props
     * @param is    is
     * @since 1.0.0
     */
    @Override
    public void load(@NotNull Properties props, @NotNull InputStream is) {
        List<PropertySource<?>> sources = null;
        try {
            sources = new YamlPropertySourceLoader().load(props.getProperty(CONFIG_NAME), new InputStreamResource(is));
        } catch (IOException e) {
            log.error("", e);
        }
        if (sources == null || sources.isEmpty()) {
            return;
        }
        // yml 数据存储,合成一个 PropertySource
        Map<String, Object> ymlDataMap = Maps.newHashMapWithExpectedSize(32);
        for (PropertySource<?> source : sources) {
            ymlDataMap.putAll(((MapPropertySource) source).getSource());
        }

        props.putAll(ymlDataMap);
    }
}
