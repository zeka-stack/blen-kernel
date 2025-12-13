package dev.dong4j.zeka.kernel.common.config.refresh;

import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import dev.dong4j.zeka.kernel.common.support.StrFormatter;
import dev.dong4j.zeka.kernel.common.util.ConfigKit;
import dev.dong4j.zeka.kernel.common.yml.YmlPropertyLoaderFactory;
import lombok.extern.slf4j.Slf4j;

/**
 * 动态配置加载器类
 * <p>
 * 负责从配置文件中动态加载和合并配置信息, 支持基础配置和环境特定配置的加载与合并, 适用于多环境配置管理场景
 *
 * @author zeka.stack.team
 * @version 1.0.0
 * @email "mailto:zeka.stack@gmail.com"
 * @date 2025.12.13
 * @since 2.0.0
 */
@Slf4j
public class DynamicConfigLoader {

    /**
     * 基础配置文件名
     * <p>
     * 用于指定应用程序的基础配置文件, 其值为 ConfigKit 中定义的 BOOT_CONFIG_FILE_NAME
     */
    private static final String BASE_CONFIG = ConfigKit.BOOT_CONFIG_FILE_NAME;
    /** 配置文件格式, 基于 BOOT_ENV_CONFIG_FILE_NAME 定义 */
    private static final String PROFILE_CONFIG_FORMAT = ConfigKit.BOOT_ENV_CONFIG_FILE_NAME;

    /**
     * 应用程序的环境配置
     * <p>
     * 用于获取和管理应用程序运行时的环境变量和配置信息
     */
    private final Environment environment;

    /**
     * 初始化动态配置加载器
     * <p>
     * 使用提供的环境对象初始化动态配置加载器实例
     *
     * @param environment 环境对象, 用于获取配置信息
     */
    public DynamicConfigLoader(Environment environment) {
        this.environment = environment;
    }

    /**
     * 加载当前环境的配置信息
     * <p>
     * 该方法首先加载基础配置, 然后合并所有激活环境的配置文件, 最终返回一个包含所有配置项的 Map.
     *
     * @return 包含当前环境所有配置项的 Map, 键为配置项名称, 值为对应的配置值
     */
    public Map<String, Object> loadCurrentEnvironmentConfig() {
        // 1. 读取 application.yml
        Map<String, Object> base = load(BASE_CONFIG);
        Map<String, Object> merged = new LinkedHashMap<>(flattenMap(base));

        // 2. 获取当前激活的 profile（如 dev、prod）
        String[] profiles = environment.getActiveProfiles();
        for (String profile : profiles) {
            String profilePath = StrFormatter.format(PROFILE_CONFIG_FORMAT, profile);
            Map<String, Object> profileData = load(profilePath);
            merged.putAll(flattenMap(profileData));
        }

        return merged;
    }

    /**
     * 将嵌套的 Map 展平为一个层级单一的 Map
     * <p>
     * 递归遍历源 Map, 将嵌套的键值对转换为点分格式的键, 并合并到结果 Map 中.
     *
     * @param source 需要展平的源 Map
     * @return 展平后的 Map, 其中键为点分格式, 值为原始值
     */
    private Map<String, Object> flattenMap(Map<String, Object> source) {
        Map<String, Object> result = new LinkedHashMap<>();
        doFlatten("", source, result);
        return result;
    }

    /**
     * 将源映射中的键值对扁平化处理并合并到目标映射中
     * <p>
     * 递归遍历源映射中的每个条目, 如果值是映射类型, 则递归处理, 否则将键值对放入目标映射中.
     * 键的格式为前缀 + "." + 原始键, 若前缀为空则直接使用原始键.
     *
     * @param prefix 用于构建新键的前缀
     * @param source 需要被扁平化的源映射
     * @param target 用于存储扁平化结果的目标映射
     */
    @SuppressWarnings("unchecked")
    private void doFlatten(String prefix, Map<String, Object> source, Map<String, Object> target) {
        for (Map.Entry<String, Object> entry : source.entrySet()) {
            String key = prefix.isEmpty() ? entry.getKey() : prefix + "." + entry.getKey();
            Object value = entry.getValue();
            if (value instanceof Map<?, ?>) {
                doFlatten(key, (Map<String, Object>) value, target);
            } else {
                target.put(key, value);
            }
        }
    }

    /**
     * 加载指定名称的 YAML 配置文件内容
     * <p>
     * 使用 YmlPropertyLoaderFactory 从类路径下加载指定名称的 YAML 配置文件, 并将其转换为 Map 格式返回.
     * 如果加载过程中发生异常, 将记录错误信息并返回空 Map.
     *
     * @param configName 配置文件名称 (不带扩展名)
     * @return 包含配置项的 Map, 键为字符串类型, 值为对应的配置值; 若加载失败则返回空 Map
     * @throws IOException 如果读取或解析配置文件时发生 I/O 异常
     */
    private static Map<String, Object> load(String configName) {
        YmlPropertyLoaderFactory ymlPropertyLoaderFactory = new YmlPropertyLoaderFactory();
        // 从 classpath 加载资源文件
        Resource resource = new ClassPathResource(configName);
        try {
            final PropertySource<?> propertySource = ymlPropertyLoaderFactory.createPropertySource(
                "DynamicProperties", new EncodedResource(resource, StandardCharsets.UTF_8)
            );
            // propertySource 转 Map<String, Object>
            Map<String, Object> result = new LinkedHashMap<>();
            if (propertySource.getSource() instanceof Map<?, ?> sourceMap) {
                // 如果 source 本身就是 Map，则直接转换
                for (Map.Entry<?, ?> entry : sourceMap.entrySet()) {
                    if (entry.getKey() instanceof String) {
                        result.put((String) entry.getKey(), entry.getValue());
                    }
                }
            }
            return result;

        } catch (IOException e) {
            log.error("[{}] 解析失败: {}", configName, e.getMessage());
            return Collections.emptyMap();
        }
    }

}
