package dev.dong4j.zeka.kernel.common.yml;

import dev.dong4j.zeka.kernel.common.util.StringUtils;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Properties;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.env.OriginTrackedMapPropertySource;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.DefaultPropertySourceFactory;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.lang.Nullable;
import org.springframework.util.PropertiesPersister;

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.26 20:43
 * @since 1.0.0
 */
@Slf4j
public class YmlPropertyLoaderFactory extends DefaultPropertySourceFactory {

    /** Properties persister */
    private final PropertiesPersister propertiesPersister = new YmlPropertiesPersister();

    /**
     * Create property source property source
     *
     * @param fullPathFileName full path file name
     * @return the property source
     * @throws Exception exception
     * @since 1.0.0
     */
    @NotNull
    public static PropertySource<?> createPropertySource(String fullPathFileName) throws Exception {
        return createPropertySource(getSourceName(""), fullPathFileName);
    }

    /**
     * Create property source property source
     *
     * @param name             name
     * @param fullPathFileName full path file name
     * @return the property source
     * @throws Exception exception
     * @since 1.0.0
     */
    @NotNull
    public static PropertySource<?> createPropertySource(String name, String fullPathFileName) throws Exception {
        YmlPropertyLoaderFactory ymlPropertyLoaderFactory = new YmlPropertyLoaderFactory();
        Resource resource = getResource(fullPathFileName);
        return ymlPropertyLoaderFactory.createPropertySource(name, new EncodedResource(resource, StandardCharsets.UTF_8));
    }

    /**
     * Create property source property source
     *
     * @param resource resource
     * @return the property source
     * @throws Exception exception
     * @since 1.0.0
     */
    @NotNull
    public static PropertySource<?> createPropertySource(Resource resource) throws Exception {
        return createPropertySource(getNameForResource(resource), resource);
    }

    /**
     * Gets name for resource *
     *
     * @param resource resource
     * @return the name for resource
     * @since 1.5.0
     */
    private static String getNameForResource(@NotNull Resource resource) {
        String name = resource.getDescription();
        if (StringUtils.isBlank(name)) {
            name = resource.getClass().getSimpleName() + "@" + System.identityHashCode(resource);
        }
        return name;
    }

    /**
     * Create property source property source
     *
     * @param name     name
     * @param resource resource
     * @return the property source
     * @throws Exception exception
     * @since 1.0.0
     */
    @NotNull
    public static PropertySource<?> createPropertySource(String name, Resource resource) throws Exception {
        YmlPropertyLoaderFactory ymlPropertyLoaderFactory = new YmlPropertyLoaderFactory();
        return ymlPropertyLoaderFactory.createPropertySource(name, new EncodedResource(resource, StandardCharsets.UTF_8));
    }

    /**
     * Gets resource *
     *
     * @param fullPathFileName full path file name
     * @return the resource
     * @throws Exception exception
     * @since 1.0.0
     */
    @NotNull
    public static Resource getResource(String fullPathFileName) throws Exception {
        InputStream inputStream = Files.newInputStream(Paths.get(fullPathFileName));
        return new InputStreamResource(inputStream);
    }

    /**
     * Create property source property source
     *
     * @param name            name
     * @param encodedResource encoded resource
     * @return the property source
     * @throws IOException io exception
     * @since 1.0.0
     */
    @NotNull
    @Override
    public PropertySource<?> createPropertySource(@Nullable String name, @NotNull EncodedResource encodedResource) throws IOException {
        Resource resource = encodedResource.getResource();
        String fileName = resource.getFilename();
        Properties properties = new Properties();
        properties.setProperty(YmlPropertiesPersister.CONFIG_NAME, name);
        this.propertiesPersister.load(properties, resource.getInputStream());
        return new OriginTrackedMapPropertySource(getSourceName(fileName, name), properties);
    }

    /**
     * Empty property source property source
     *
     * @param name name
     * @return the property source
     * @since 1.0.0
     */
    @NotNull
    @Contract("_ -> new")
    private static PropertySource<?> emptyPropertySource(@Nullable String name) {
        return new MapPropertySource(getSourceName(name), Collections.emptyMap());
    }

    /**
     * Gets source name *
     *
     * @param names names
     * @return the source name
     * @since 1.0.0
     */
    @Contract("_ -> !null")
    private static String getSourceName(String... names) {
        return Stream.of(names)
            .filter(StringUtils::isNotBlank)
            .findFirst()
            .orElse("zekaStackCustomPropertySource");
    }

}
