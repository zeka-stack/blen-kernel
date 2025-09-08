package dev.dong4j.zeka.kernel.common.config;

import dev.dong4j.zeka.kernel.common.constant.ConfigKey;
import java.util.Map;
import java.util.Properties;
import java.util.function.BiConsumer;
import org.jetbrains.annotations.Contract;

/**
 * <p>Description: 定义非 application.yml 等 spring boot 默认的配置资源类</p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.06.04 19:21
 * @since 1.0.0
 */
public class PropertiesPropertySource implements PropertySource {

    /** PREFIX */
    private static final String PREFIX = ConfigKey.PREFIX;

    /** Properties */
    private final Properties properties;

    /**
     * Properties property source
     *
     * @param properties properties
     * @since 1.0.0
     */
    @Contract(pure = true)
    public PropertiesPropertySource(Properties properties) {
        this.properties = properties;
    }

    /**
     * Gets priority *
     *
     * @return the priority
     * @since 1.0.0
     */
    @Override
    public int getPriority() {
        return 0;
    }

    /**
     * For each
     *
     * @param action action
     * @since 1.0.0
     */
    @Override
    public void forEach(BiConsumer<String, String> action) {
        for (Map.Entry<Object, Object> entry : this.properties.entrySet()) {
            action.accept(((String) entry.getKey()), ((String) entry.getValue()));
        }
    }

    /**
     * Gets normal form *
     *
     * @param tokens tokens
     * @return the normal form
     * @since 1.0.0
     */
    @Override
    public CharSequence getNormalForm(Iterable<? extends CharSequence> tokens) {
        return PREFIX + Util.joinAsCamelCase(tokens);
    }
}
