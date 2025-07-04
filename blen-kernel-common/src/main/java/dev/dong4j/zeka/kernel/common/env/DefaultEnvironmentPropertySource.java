package dev.dong4j.zeka.kernel.common.env;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.env.MapPropertySource;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.util.Map;

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.3.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.04.10 16:58
 * @since 1.0.0
 */
@Slf4j
public class DefaultEnvironmentPropertySource extends MapPropertySource {

    /**
     * Create a new {@code SystemEnvironmentPropertySource} with the given name and
     * delegating to the given {@code MapPropertySource}.
     *
     * @param name   name
     * @param source source
     * @since 1.0.0
     */
    public DefaultEnvironmentPropertySource(String name, Map<String, Object> source) {
        super(name, source);
    }


    /**
     * Return {@code true} if a property with the given name or any underscore/uppercase variant
     * thereof exists in this property source.
     *
     * @param name name
     * @return the boolean
     * @since 1.0.0
     */
    @Override
    public boolean containsProperty(@NotNull String name) {
        return (this.getProperty(name) != null);
    }

    /**
     * This implementation returns {@code true} if a property with the given name or
     * any underscore/uppercase variant thereof exists in this property source.
     *
     * @param name name
     * @return the property
     * @since 1.0.0
     */
    @Override
    @Nullable
    public Object getProperty(@NotNull String name) {
        String actualName = this.resolvePropertyName(name);
        log.debug("PropertySource [{}] does not contain property [{}], but found equivalent [{}]",
            this.getName(),
            name,
            actualName);
        return super.getProperty(actualName);
    }

    /**
     * Check to see if this property source contains a property with the given name, or
     * any underscore / uppercase variation thereof. Return the resolved name if one is
     * found or otherwise the original name. Never returns {@code null}.
     *
     * @param name name
     * @return the string
     * @since 1.0.0
     */
    protected final @NotNull String resolvePropertyName(String name) {
        Assert.notNull(name, "Property name must not be null");
        String resolvedName = this.checkPropertyName(name);
        if (resolvedName != null) {
            return resolvedName;
        }
        String uppercasedName = name.toUpperCase();
        if (!name.equals(uppercasedName)) {
            resolvedName = this.checkPropertyName(uppercasedName);
            if (resolvedName != null) {
                return resolvedName;
            }
        }
        return name;
    }

    /**
     * Check property name string
     *
     * @param name name
     * @return the string
     * @since 1.0.0
     */
    @Nullable
    private String checkPropertyName(String name) {
        // Check name as-is
        if (this.containsKey(name)) {
            return name;
        }
        // Check name with just dots replaced
        String noDotName = name.replace('.', '_');
        if (!name.equals(noDotName) && this.containsKey(noDotName)) {
            return noDotName;
        }
        // Check name with just hyphens replaced
        String noHyphenName = name.replace('-', '_');
        if (!name.equals(noHyphenName) && this.containsKey(noHyphenName)) {
            return noHyphenName;
        }
        // Check name with dots and hyphens replaced
        String noDotNoHyphenName = noDotName.replace('-', '_');
        if (!noDotName.equals(noDotNoHyphenName) && this.containsKey(noDotNoHyphenName)) {
            return noDotNoHyphenName;
        }
        // Give up
        return null;
    }

    /**
     * Contains key boolean
     *
     * @param name name
     * @return the boolean
     * @since 1.0.0
     */
    private boolean containsKey(String name) {
        return this.source.containsKey(name);
    }

}
