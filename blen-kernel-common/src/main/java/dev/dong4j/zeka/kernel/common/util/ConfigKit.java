package dev.dong4j.zeka.kernel.common.util;

import com.google.common.collect.Maps;
import dev.dong4j.zeka.kernel.common.constant.App;
import dev.dong4j.zeka.kernel.common.constant.ConfigDefaultValue;
import dev.dong4j.zeka.kernel.common.constant.ConfigKey;
import dev.dong4j.zeka.kernel.common.enums.ZekaEnv;
import dev.dong4j.zeka.kernel.common.env.DefaultEnvironment;
import dev.dong4j.zeka.kernel.common.env.ZekaEnvironment;
import dev.dong4j.zeka.kernel.common.exception.BaseException;
import dev.dong4j.zeka.kernel.common.exception.PropertiesException;
import dev.dong4j.zeka.kernel.common.support.StrFormatter;
import dev.dong4j.zeka.kernel.common.yml.YmlPropertyLoaderFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.ConfigurationPropertyNameAliases;
import org.springframework.boot.context.properties.source.ConfigurationPropertySource;
import org.springframework.boot.context.properties.source.ConfigurationPropertySources;
import org.springframework.boot.context.properties.source.MapConfigurationPropertySource;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.DefaultPropertySourceFactory;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;

/**
 * <p>Description: 全局配置工具类, 用于获取整个应用的配置 </p>
 * 生命周期:
 * 1. 初始化 {@link #init(ConfigurableEnvironment)}
 * 2. 运行期间 (特指应用已经启动完成, 输出了 start finished 之后)
 * 此工具类中的环境在系统环境准备完成后, 初始化日志配置之前初始化.
 * 因此在这之前, 通过此工具类获取配置都将直接从 Java 环境变量中获取 {@link #getProperty(String)}.
 * 在使用此工具类时, 最好要了解配置的声明周期, 不然可能会造成获取不到配置的尴尬问题
 *
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.27 00:43
 * @since 1.0.0
 */
@Slf4j
@UtilityClass
@SuppressWarnings(value = {"checkstyle:MethodLimit", "checkstyle:Indentation"})
public class ConfigKit {
    /** 自定义配置 */
    private static final Map<String, Object> CONSUMER_MAP = Maps.newHashMapWithExpectedSize(16);
    /** CONSUMER_PROPERTIES_NAME */
    private static final String CONSUMER_PROPERTIES_NAME = "consumerProperties";
    /** JUNIT_FLAG */
    private static final String JUNIT_FLAG = "test-classes";
    /** DEFAULT_PROFILE */
    public static final String DEFAULT_PROFILE = "default";
    /** 默认的配置文件解析器 */
    private static final PropertySourceFactory DEFAULT_PROPERTY_SOURCE_FACTORY = new DefaultPropertySourceFactory();
    /** environment */
    private static ConfigurableEnvironment environment;
    /** yaml 类型的配置文件 */
    public static final String YAML_FILE_EXTENSION = "yml";
    /** spring cloud 启动配置文件名 */
    public static final String CLOUD_CONFIG_FILE_NAME = "bootstrap.yml";
    /** spring boot 启动配置文件名 */
    public static final String BOOT_CONFIG_FILE_NAME = "application.yml";
    /** properties 类型的配置文件 */
    public static final String PROPERTIES_FILE_EXTENSION = "properties";
    /** SYSTEM_ENVIRONMENT_NAME */
    public static final String SYSTEM_ENVIRONMENT_NAME = "systemEnvironment";

    /**
     * 在初始化日志配置之后执行
     * 1. 从默认配置 map 中获取配置
     * 2. 解析 environment
     * 3. 缓存到 ctxPropertiesMap
     * 使用时, 优先从 map 缓存中取, 然后才是 environment {@link #getProperty(String)}
     *
     * @param env the environment
     * @since 1.0.0
     */
    public static void init(@NotNull ConfigurableEnvironment env) {
        ConfigKit.environment = env;
    }

    /**
     * Show debug info.
     *
     * @since 1.0.0
     */
    public static void showDebugInfo() {
        if (isDebugModel()) {
            ZekaEnvironment zekaEnvironment = new ZekaEnvironment(environment);
            ZekaEnvironment.EnvironmentDescriptor environmentDescriptor = zekaEnvironment.environment("");

            // 日志配置文件: MarkerPatternSelector.PatternMatch.key = properties
            Marker marker = MarkerFactory.getMarker(PROPERTIES_FILE_EXTENSION);

            environmentDescriptor.getActiveProfiles().forEach(a -> log.info(marker, "active profiles: {}", a));

            List<String> list = new ArrayList<>();
            environmentDescriptor.getPropertySources()
                .forEach(e -> e.getProperties()
                    .forEach((k, v) -> list.add(StrFormatter.format("{} : {}", k, v.getValue()))));
            log.info(marker, "{}", GsonUtils.toJson(list, true));

        }
    }

    /**
     * 是否为 debug 模式
     *
     * @return the boolean
     * @since 1.0.0
     */
    public static boolean isDebugModel() {
        String debugModel = System.getProperty(App.DEBUG_MODEL);
        return StringUtils.isNotBlank(debugModel) && (Boolean.parseBoolean(debugModel) || App.DEBUG_MODEL.equalsIgnoreCase(debugModel));
    }

    /**
     * 开启 debug 模式
     *
     * @since 1.0.0
     */
    public static void openDebug() {
        System.setProperty(App.DEBUG_MODEL, ConfigDefaultValue.TRUE_STRING);
    }

    /**
     * 获取启动模式
     * 本地开发模式还是服务器部署模式
     *
     * @return the string
     * @since 1.0.0
     */
    @NotNull
    public static Boolean isLocalLaunch() {
        return !notLocalLaunch();
    }

    /**
     * 非本地开发环境 (只要 start.type = shell 都认为是非开发环境)
     *
     * @return the boolean
     * @since 1.0.0
     */
    @NotNull
    public static Boolean notLocalLaunch() {
        return App.START_SHELL.equals(System.getProperty(App.START_TYPE));
    }

    /**
     * 是否为单元测试
     *
     * @return the boolean
     * @since 1.4.0
     */
    public static @NotNull Boolean isStartedByJunit() {
        return App.START_JUNIT.equals(System.getProperty(App.START_TYPE));
    }

    /**
     * 设置 JVM 环境变量
     *
     * @param key   key
     * @param value value
     * @since 1.0.0
     */
    public static void setSystemProperties(String key, String value) {
        System.setProperty(key, value);
    }

    /**
     * Gets property.
     *
     * @param key          the key
     * @param defaultValue the default value
     * @return the property
     * @since 1.0.0
     */
    public static String getProperty(String key, String defaultValue) {
        return getProperty(environment, key, defaultValue);
    }

    /**
     * Gets property *
     *
     * @param environment  environment
     * @param key          key
     * @param defaultValue default value
     * @return the property
     * @since 1.5.0
     */
    public static String getProperty(ConfigurableEnvironment environment, String key, String defaultValue) {
        String value = getProperty(environment, key);
        return value == null ? defaultValue : value;
    }

    /**
     * 优先获取本地维护的配置, 因为会动态刷新, 然后才是 environment, 最后是环境变量
     *
     * @param key the key
     * @return the property
     * @since 1.0.0
     */
    public static String getProperty(String key) {
        return getProperty(environment, key);
    }

    /**
     * Gets property *
     *
     * @param environment environment
     * @param key         key
     * @return the property
     * @since 1.5.0
     */
    public static String getProperty(ConfigurableEnvironment environment, String key) {
        String value;
        if (environment != null) {
            value = environment.getProperty(key);
            if (StringUtils.isNotBlank(value)) {
                return value;
            }
        } else {
            return System.getProperty(key);
        }
        return value;
    }

    /**
     * 是否为生成环境
     *
     * @return the boolean
     * @since 1.0.0
     */
    public static boolean isProd() {
        return ZekaEnv.PROD.equals(getEnv());
    }

    /**
     * 获取 environment
     *
     * @return the environment
     * @since 1.0.0
     */
    public static ZekaEnv getEnv() {
        return ZekaEnv.of(getProfile());
    }

    /**
     * 在未初始化 ConfigKit 之前, 可通过 environment 获取当前的 profile
     *
     * @param environment environment
     * @return the string
     * @since 1.0.0
     */
    public static String getProfile(@NotNull ConfigurableEnvironment environment) {
        String profile = getProfile();
        if (StringUtils.isNotBlank(profile) && !DEFAULT_PROFILE.equals(profile)) {
            return profile;
        }

        processorProfileActive(environment);
        // 获取配置文件中的环境变量
        String activeProfile = environment.getProperty(ConfigKey.SpringConfigKey.PROFILE_ACTIVE, ZekaEnv.LOCAL.getName());
        List<String> profiles = StringUtils.splitTrim(activeProfile, CharPool.COMMA);

        // 预设的环境
        List<String> presetProfiles = ZekaEnv.getEnvList();
        // 交集
        presetProfiles.retainAll(profiles);
        // 当前使用
        List<String> activeProfileList = new ArrayList<>(profiles);

        if (activeProfileList.isEmpty()) {
            // 默认 profile = local
            profile = ZekaEnv.LOCAL.getName();
            activeProfileList.add(profile);
        } else if (activeProfileList.size() == 1) {
            profile = activeProfileList.get(0);
        } else {
            // 同时存在多个环境时
            throw new PropertiesException("不能同时使用多个环境配置: " + profiles);
        }
        System.setProperty(ConfigKey.SpringConfigKey.PROFILE_ACTIVE, profile);
        return profile;
    }

    /**
     * 处理 spring.profiles.active.
     * 如果在配置文件中配置了 spring.profile.active=${profile.active}, 在没有经过 maven 编译后就执行时, 会造成启动失败, 因为现在已经将所有的 profile
     * 删除, 因此这里将 profile.active 设置为 local, 兼容以前的项目.
     *
     * @param environment environment
     * @since 1.0.0
     */
    @SuppressWarnings("java:S1171")
    private static void processorProfileActive(@NotNull ConfigurableEnvironment environment) {
        environment.getPropertySources()
            .addLast(new MapPropertySource("local.profile.active",
                new HashMap<String, Object>(2) {
                    private static final long serialVersionUID = 7174510826497115206L;

                    {
                        this.put("profile.active", ZekaEnv.LOCAL.getName());
                    }
                }));
    }


    /**
     * 获取当前激活的 profile name
     *
     * @return the profile name
     * @since 1.0.0
     */
    public static String getProfile() {
        String profile = getProperty(ConfigKey.SpringConfigKey.PROFILE_ACTIVE);
        return StringUtils.isBlank(profile) ? DEFAULT_PROFILE : profile;
    }

    /**
     * Get app name string.
     *
     * @return the string
     * @since 1.0.0
     */
    public static String getAppName() {
        return getProperty(ConfigKey.SpringConfigKey.APPLICATION_NAME);
    }

    /**
     * Get property source property source
     *
     * @param configFileName config file name
     * @return the property source
     * @since 1.0.0
     */
    @NotNull
    @SuppressWarnings("java:S1452")
    public static PropertySource<?> getPropertySource(@NotNull String configFileName) {
        String configPath = ConfigKit.getConfigPath();
        String propertiesPath = configPath + configFileName;

        if (configFileName.endsWith(YAML_FILE_EXTENSION)) {
            try {
                return YmlPropertyLoaderFactory.createPropertySource(propertiesPath);
            } catch (IOException e) {
                // 如果单元测试时在 test-classes 下不存在 configFileName 配置, 则查找 target/classes 下的 configFileName 配置
                if (!configPath.contains(JUNIT_FLAG)) {
                    throw new PropertiesException("classpath 下不存在 " + configFileName);
                }
                configPath = configPath.replace(JUNIT_FLAG, "classes");
                try {
                    return YmlPropertyLoaderFactory.createPropertySource(configPath + configFileName);
                } catch (Exception ex) {
                    // 如果单元测试时在 test-classes 下不存在 configFileName 配置, 则查找 target/classes 下的 configFileName 配置
                    throw new PropertiesException("classpath 下不存在 " + configFileName);
                }
            } catch (Exception e) {
                throw new PropertiesException(e);
            }
        } else if (configFileName.endsWith(PROPERTIES_FILE_EXTENSION)) {
            String resolvedLocation = environment.resolveRequiredPlaceholders(configFileName);
            Resource resource = new DefaultResourceLoader().getResource(resolvedLocation);
            try {
                return DEFAULT_PROPERTY_SOURCE_FACTORY.createPropertySource("", new EncodedResource(resource, StandardCharsets.UTF_8));
            } catch (IOException e) {
                throw new BaseException("未找到文件: [{}]", configFileName);
            }
        } else {
            throw new BaseException("不支持的配置文件类型: [{}]", configFileName);
        }
    }

    /**
     * Gets property source *
     *
     * @param resource resource
     * @return the property source
     * @since 1.5.0
     */
    @SuppressWarnings("java:S1452")
    public static @NotNull PropertySource<?> getPropertySource(Resource resource) {
        try {
            return YmlPropertyLoaderFactory.createPropertySource(resource);
        } catch (Exception e) {
            throw new BaseException(e);
        }
    }

    /**
     * 通过文件全路径名获取资源文件
     *
     * @param configFileName config file name
     * @return the resource
     * @since 1.0.0
     */
    public static Resource getResource(String configFileName) {
        String configPath = ConfigKit.getConfigPath();
        String fullPathFileName = configPath + configFileName;
        Resource resource;
        InputStream inputStream;
        try {
            inputStream = new FileInputStream(fullPathFileName);
            resource = new InputStreamResource(inputStream);
        } catch (IOException ex) {
            throw new PropertiesException("未找到文件: [{}]", fullPathFileName);
        }
        return resource;
    }

    /**
     * 获取配置文件路径, 兼容本地开发和 jar 运行
     * -D 参数使用 Systerm.getProperty 获取
     * <code>
     * nohup ${JAVA_HOME}/bin/java -jar \
     * ${JVM_OPTS} \
     * -Ddeploy.path=${DEPLOY_DIR} \
     * -Dstart.type=shell \
     * ${DEBUG_OPTS} \
     * ${JAR_FILE} \
     * --spring.profiles.active=${ENV} \
     * --spring.config.location=${DEPLOY_DIR}/config/ &
     * </code>
     *
     * @return the config path
     * @since 1.0.0
     */
    public static @NotNull String getConfigPath() {
        String configPath;
        String startType = System.getProperty(App.START_TYPE);
        // 脚本启动
        if (StringUtils.isNotBlank(startType) && App.START_SHELL.equals(startType)) {
            // 获取 config 路径
            configPath = System.getProperty(App.APP_CONFIG_PATH);
            // 这种情况是本地开发时设置了 -Dstart.path 参数的情况
            if (StringUtils.isBlank(configPath)) {
                configPath = StartUtils.getClasspath();
            }
        } else {
            // 本地运行
            configPath = StartUtils.getClasspath();
        }
        if (!configPath.endsWith(File.separator)) {
            configPath += File.separator;
        }
        return configPath;
    }

    /**
     * 获取 classpath 或 config 目录下的文件.
     * 比如文件存在以下的目录:
     * src/main/resrources/xx/file.txt
     * src/main/resrources/file.txt
     * 打包后会移动到此目录下:
     * xxxx/config/xx/file.txt: file = xx/file.txt
     * xxxx/config/file.txt: file = file.txt
     *
     * @param fileName file name
     * @return the file
     * @since 1.0.0
     */
    @NotNull
    @SneakyThrows
    public static InputStream getConfigFile(String fileName) {
        String configPath = ConfigKit.getConfigPath();
        String filePath = configPath + fileName;
        File file = new File(filePath);
        return new FileInputStream(file);
    }

    /**
     * 获取 includes 目录绝对路径
     *
     * @return the includes path
     * @since 1.0.0
     */
    public static @NotNull String getIncludesPath() {
        return FileUtils.appendPath(getConfigPath(), "includes");
    }

    /**
     * 获取 includes 目录下的文件.
     * 比如文件存在以下的目录:
     * src/main/resrources/includes/xx/file.txt
     * src/main/resrources/includes/file.txt
     * 打包后会移动到此目录:
     * xxxx/config/includes/xx/file.txt: file = xx/file.txt
     * xxxx/config/includes/file.txt: file = file.txt
     *
     * @param fileName file name
     * @return the file
     * @since 1.0.0
     */
    @SneakyThrows
    public static @NotNull InputStream getIncludesStream(String fileName) {
        File file = getIncludesFile(fileName);
        return new FileInputStream(file);
    }

    /**
     * Gets includes file *
     *
     * @param fileName file name
     * @return the includes file
     * @since 1.6.0
     */
    @SneakyThrows
    public static @NotNull File getIncludesFile(String fileName) {
        String filePath = getIncludesFilePath(fileName);
        return new File(filePath);
    }

    /**
     * Gets includes path *
     *
     * @param fileName file name
     * @return the includes path
     * @since 1.6.0
     */
    public static @NotNull String getIncludesFilePath(String fileName) {
        return FileUtils.appendPath(getIncludesPath(), fileName);
    }

    /**
     * 应用启动完成后获取真实的 port
     *
     * @return the string
     * @since 1.0.0
     */
    public static Integer getPort() {
        // rest port
        Integer port = getRestPort();
        // dubbo port
        port = Objects.equals(-1, port) ? getManagementPort() : port;
        return Objects.equals(-1, port) ? getDubboPort() : port;
    }

    /**
     * Gets rest port *
     *
     * @return the rest port
     * @since 1.6.0
     */
    public static Integer getRestPort() {
        return getPort(ConfigKey.SpringConfigKey.SERVER_PORT);
    }

    /**
     * Gets management port *
     *
     * @return the management port
     * @since 1.6.0
     */
    public static Integer getManagementPort() {
        return getPort(ConfigKey.SpringConfigKey.MANAGEMENT_SERVER_PORT);
    }

    /**
     * Gets dubbo port *
     *
     * @return the dubbo port
     * @since 1.6.0
     */
    public static Integer getDubboPort() {
        return getPort(ConfigKey.DubboConfigKey.PROTOCOL_PORT);
    }

    /**
     * Gets port *
     *
     * @param key key
     * @return the port
     * @since 1.6.0
     */
    public static Integer getPort(String key) {
        String port = getProperty(key);
        if (StringUtils.isBlank(port)) {
            log.debug("获取 [{}] 未空, 默认返回 -1", key);
            return -1;
        }
        return Integer.parseInt(port);
    }

    /**
     * 获取 context path, 未指定默认为 /
     *
     * @return the string
     * @since 1.0.0
     */
    public static String getContextPath() {
        String path = ConfigKit.getProperty(ConfigKey.SpringConfigKey.SERVER_CONTEXT_PATH);
        if (StringUtils.isBlank(path)) {
            return "";
        }

        return path;
    }

    /**
     * 添加自定义配置到环境变量中
     *
     * @param map the map
     * @since 1.0.0
     */
    public static void addProperty(Map<String, Object> map) {
        // 配置加解密的时候使用的密钥
        CONSUMER_MAP.putAll(map);
        MapPropertySource mapPropertySource = new MapPropertySource(CONSUMER_PROPERTIES_NAME, CONSUMER_MAP);
        // 默认优先级为最低, 可通过外部配置覆盖
        environment.getPropertySources().addLast(mapPropertySource);
    }

    /**
     * Gets int value.
     *
     * @param key          the key
     * @param defaultValue the default value
     * @return the int value
     * @since 1.0.0
     */
    public static int getIntValue(String key, int defaultValue) {
        int i = getIntValue(key);
        return i == 0 ? defaultValue : i;
    }

    /**
     * Gets int value.
     *
     * @param key the key
     * @return the int value
     * @since 1.0.0
     */
    public static int getIntValue(String key) {
        Object value = getProperty(key);
        if (value == null) {
            return 0;
        } else {
            return DataTypeUtils.convert(int.class, value);
        }
    }

    /**
     * Gets int value.
     *
     * @param key          the key
     * @param defaultValue the default value
     * @return the int value
     * @since 1.0.0
     */
    public static long getLongValue(String key, long defaultValue) {
        long i = getLongValue(key);
        return i == 0L ? defaultValue : i;
    }

    /**
     * Gets int value.
     *
     * @param key the key
     * @return the int value
     * @since 1.0.0
     */
    public static long getLongValue(String key) {
        Object value = getProperty(key);
        if (value == null) {
            return 0L;
        } else {
            return DataTypeUtils.convert(long.class, value);
        }
    }

    /**
     * Gets boolean.
     *
     * @param key          the key
     * @param defaultValue the default value
     * @return the boolean
     * @since 1.0.0
     */
    public static Boolean getBoolean(String key, boolean defaultValue) {
        Boolean b = getBoolean(key);
        return b == null ? defaultValue : b;
    }

    /**
     * Gets boolean.
     *
     * @param key the key
     * @return the boolean
     * @since 1.0.0
     */
    @Nullable
    public static Boolean getBoolean(String key) {
        Object value = getProperty(key);
        if (value == null) {
            return null;
        } else {
            return DataTypeUtils.convert(Boolean.class, value);
        }
    }

    /**
     * Gets boolean value.
     * 未找到 key 则返回 false
     *
     * @param key the key
     * @return the boolean value
     * @since 1.0.0
     */
    public static boolean getBooleanValue(String key) {
        Object value = getProperty(key);
        if (value == null) {
            return false;
        } else {
            return DataTypeUtils.convert(boolean.class, value);
        }
    }

    /**
     * Get bytes byte [ ].
     *
     * @param key          the key
     * @param defaultValue the default value
     * @return the byte [ ]
     * @since 1.0.0
     */
    public static byte[] getBytes(String key, byte[] defaultValue) {
        byte[] b = getBytes(key);
        return b.length == 0 ? defaultValue : b;
    }

    /**
     * Get bytes byte [ ].
     *
     * @param key the key
     * @return the byte [ ]
     * @since 1.0.0
     */
    public static byte[] getBytes(String key) {
        Object value = getProperty(key);
        if (value == null) {
            return new byte[0];
        } else {
            return DataTypeUtils.convert(byte[].class, value);
        }
    }

    /**
     * Gets byte.
     *
     * @param key          the key
     * @param defaultValue the default value
     * @return the byte
     * @since 1.0.0
     */
    public static Byte getByte(String key, byte defaultValue) {
        Byte b = getByte(key);
        return b == null ? defaultValue : b;
    }

    /**
     * Gets byte.
     *
     * @param key the key
     * @return the byte
     * @since 1.0.0
     */
    public static Byte getByte(String key) {
        Object value = getProperty(key);
        return DataTypeUtils.convert(Byte.class, value);
    }

    /**
     * Gets byte value.
     *
     * @param key          the key
     * @param defaultValue the default value
     * @return the byte value
     * @since 1.0.0
     */
    public static byte getByteValue(String key, byte defaultValue) {
        byte b = getByteValue(key);
        return b == 0 ? defaultValue : b;
    }

    /**
     * Gets byte value.
     *
     * @param key the key
     * @return the byte value
     * @since 1.0.0
     */
    public static byte getByteValue(String key) {
        Object value = getProperty(key);
        if (value == null) {
            return 0;
        } else {
            return DataTypeUtils.convert(byte.class, value);
        }
    }

    /**
     * Gets short.
     *
     * @param key          the key
     * @param defaultValue the default value
     * @return the short
     * @since 1.0.0
     */
    public static Short getShort(String key, short defaultValue) {
        Short s = getShort(key);
        return s == null ? defaultValue : s;
    }

    /**
     * Gets short.
     *
     * @param key the key
     * @return the short
     * @since 1.0.0
     */
    public static Short getShort(String key) {
        Object value = getProperty(key);
        return DataTypeUtils.convert(Short.class, value);
    }

    /**
     * Gets short value.
     *
     * @param key          the key
     * @param defaultValue the default value
     * @return the short value
     * @since 1.0.0
     */
    public static short getShortValue(String key, short defaultValue) {
        short s = getShortValue(key);
        return s == 0 ? defaultValue : s;
    }

    /**
     * Gets short value.
     *
     * @param key the key
     * @return the short value
     * @since 1.0.0
     */
    public static short getShortValue(String key) {
        Object value = getProperty(key);
        if (value == null) {
            return 0;
        } else {
            return DataTypeUtils.convert(short.class, value);
        }
    }

    /**
     * Gets integer.
     *
     * @param key          the key
     * @param defaultValue the default value
     * @return the integer
     * @since 1.0.0
     */
    public static Integer getInteger(String key, int defaultValue) {
        Integer i = getInteger(key);
        return i == null ? defaultValue : i;
    }

    /**
     * Gets integer.
     *
     * @param key the key
     * @return the integer
     * @since 1.0.0
     */
    public static Integer getInteger(String key) {
        Object value = getProperty(key);
        return DataTypeUtils.convert(Integer.class, value);
    }

    /**
     * Gets long.
     *
     * @param key          the key
     * @param defaultValue the default value
     * @return the long
     * @since 1.0.0
     */
    public static Long getLong(String key, long defaultValue) {
        Long i = getLong(key);
        return i == null ? defaultValue : i;
    }

    /**
     * Gets long.
     *
     * @param key the key
     * @return the long
     * @since 1.0.0
     */
    public static Long getLong(String key) {
        Object value = getProperty(key);
        return DataTypeUtils.convert(Long.class, value);
    }

    /**
     * Gets float.
     *
     * @param key          the key
     * @param defaultValue the default value
     * @return the float
     * @since 1.0.0
     */
    public static Float getFloat(String key, float defaultValue) {
        Float i = getFloat(key);
        return i == null ? defaultValue : i;
    }

    /**
     * Gets float.
     *
     * @param key the key
     * @return the float
     * @since 1.0.0
     */
    public static Float getFloat(String key) {
        Object value = getProperty(key);
        return DataTypeUtils.convert(Float.class, value);
    }

    /**
     * Gets float value.
     *
     * @param key          the key
     * @param defaultValue the default value
     * @return the float value
     * @since 1.0.0
     */
    @SuppressWarnings("PMD.AvoidDoubleOrFloatEqualCompareRule")
    public static float getFloatValue(String key, float defaultValue) {
        float i = getFloatValue(key);
        return i == 0.0F ? defaultValue : i;
    }

    /**
     * Gets float value.
     *
     * @param key the key
     * @return the float value
     * @since 1.0.0
     */
    public static float getFloatValue(String key) {
        Object value = getProperty(key);
        if (value == null) {
            return 0.0F;
        } else {
            return DataTypeUtils.convert(float.class, value);
        }
    }

    /**
     * Gets double.
     *
     * @param key          the key
     * @param defaultValue the default value
     * @return the double
     * @since 1.0.0
     */
    public static Double getDouble(String key, double defaultValue) {
        Double i = getDouble(key);
        return i == null ? defaultValue : i;
    }

    /**
     * Gets double.
     *
     * @param key the key
     * @return the double
     * @since 1.0.0
     */
    public static Double getDouble(String key) {
        Object value = getProperty(key);
        return DataTypeUtils.convert(Double.class, value);
    }

    /**
     * Gets double value.
     *
     * @param key          the key
     * @param defaultValue the default value
     * @return the double value
     * @since 1.0.0
     */
    @SuppressWarnings("PMD.AvoidDoubleOrFloatEqualCompareRule")
    public static double getDoubleValue(String key, double defaultValue) {
        double i = getDoubleValue(key);
        return i == 0.0D ? defaultValue : i;
    }

    /**
     * Gets double value.
     *
     * @param key the key
     * @return the double value
     * @since 1.0.0
     */
    @SuppressWarnings("PMD.AvoidDoubleOrFloatEqualCompareRule")
    public static double getDoubleValue(String key) {
        Object value = getProperty(key);
        if (value == null) {
            return 0.0D;
        } else {
            return DataTypeUtils.convert(double.class, value);
        }
    }

    /**
     * Gets big decimal.
     *
     * @param key          the key
     * @param defaultValue the default value
     * @return the big decimal
     * @since 1.0.0
     */
    public static BigDecimal getBigDecimal(String key, BigDecimal defaultValue) {
        BigDecimal i = getBigDecimal(key);
        return i == null ? defaultValue : i;
    }

    /**
     * Gets big decimal.
     *
     * @param key the key
     * @return the big decimal
     * @since 1.0.0
     */
    public static BigDecimal getBigDecimal(String key) {
        Object value = getProperty(key);
        return DataTypeUtils.convert(BigDecimal.class, value);
    }

    /**
     * Gets big integer.
     *
     * @param key          the key
     * @param defaultValue the default value
     * @return the big integer
     * @since 1.0.0
     */
    public static BigInteger getBigInteger(String key, BigInteger defaultValue) {
        BigInteger i = getBigInteger(key);
        return i == null ? defaultValue : i;
    }

    /**
     * Gets big integer.
     *
     * @param key the key
     * @return the big integer
     * @since 1.0.0
     */
    public static BigInteger getBigInteger(String key) {
        Object value = getProperty(key);
        return DataTypeUtils.convert(BigInteger.class, value);
    }

    /**
     * Gets string.
     *
     * @param key          the key
     * @param defaultValue the default value
     * @return the string
     * @since 1.0.0
     */
    public static String getString(String key, String defaultValue) {
        String s = getString(key);
        return StringUtils.isBlank(s) ? defaultValue : s;
    }

    /**
     * Gets string.
     *
     * @param key the key
     * @return the string
     * @since 1.0.0
     */
    @Nullable
    public static String getString(String key) {
        Object value = getProperty(key);
        if (value == null) {
            return null;
        } else {
            return value.toString();
        }
    }

    /**
     * Gets date.
     *
     * @param key          the key
     * @param defaultValue the default value
     * @return the date
     * @since 1.0.0
     */
    public static Date getDate(String key, Date defaultValue) {
        Date s = getDate(key);
        return s == null ? defaultValue : s;
    }

    /**
     * Gets date.
     *
     * @param key the key
     * @return the date
     * @since 1.0.0
     */
    public static Date getDate(String key) {
        Object value = getProperty(key);
        return DataTypeUtils.convert(Date.class, value);
    }

    /**
     * Gets properties.
     *
     * @param symbol the symbol
     * @param env    the environment
     * @return the properties
     * @since 1.0.0
     */
    public static String getProperties(String symbol, ConfigurableEnvironment env) {
        String value = "";
        String key;
        if (StringUtils.startsWithIgnoreCase(symbol, StringPool.DOLLAR_LEFT_BRACE)
            && StringUtils.endsWithIgnoreCase(symbol, StringPool.RIGHT_BRACE)) {
            key = symbol.replace(StringPool.DOLLAR_LEFT_BRACE, StringPool.EMPTY).replace(StringPool.RIGHT_BRACE, StringPool.EMPTY);
            if (env != null) {
                value = env.getProperty(key);
            }
            if (StringUtils.isBlank(value)) {
                value = ConfigKit.getProperty(key);
            }
        }
        return value;
    }

    /**
     * Gets host name *
     *
     * @return the host name
     * @since 1.0.0
     */
    public static String getHostName() {
        return NetUtils.getLocalHost();
    }

    /**
     * Gets ip with port *
     *
     * @return the ip with port
     * @since 1.0.0
     */
    @NotNull
    public static String getIpWithPort() {
        return getHostName() + ":" + getPort();
    }

    /**
     * 设置 nacos 的 namespace, 一般用于本地调试时连接 dev,test 服务.
     *
     * @param namespace namespace
     * @since 1.0.0
     */
    public void setNamespace(String namespace) {
        System.setProperty(App.ZEKA_NAME_SPACE, namespace);
    }

    /**
     * Sets properties *
     *
     * @param properties properties
     * @since 1.4.0
     */
    public void setProperties(Properties properties) {
        setProperties(environment, properties);
    }

    /**
     * Sets properties *
     *
     * @param environment environment
     * @param properties  properties
     * @since 1.4.0
     */
    public void setProperties(@NotNull ConfigurableEnvironment environment, Properties properties) {
        setProperties(environment, Tools.getMapFromProperties(properties));
    }

    /**
     * Sets properties *
     *
     * @param map map
     * @since 1.4.0
     */
    public void setProperties(Map<String, Object> map) {
        setProperties(environment, map);
    }

    /**
     * Sets properties *
     *
     * @param environment environment
     * @param map         map
     * @since 1.4.0
     */
    public void setProperties(@NotNull ConfigurableEnvironment environment, Map<String, Object> map) {
        setProperties(environment, map, false);

    }

    /**
     * Sets properties *
     *
     * @param environment environment
     * @param map         map
     * @param first       first
     * @since 1.5.0
     */
    public void setProperties(@NotNull ConfigurableEnvironment environment, Map<String, Object> map, boolean first) {
        if (first) {
            environment.getPropertySources().addFirst(new MapPropertySource("highestPriorityProperties", map));
        } else {
            MapPropertySource ps = (MapPropertySource)
                environment.getPropertySources().get(DefaultEnvironment.DEFAULT_PROPERTIES_PROPERTY_SOURCE_NAME);
            if (ps == null) {
                environment.getPropertySources()
                    .addLast(new MapPropertySource(DefaultEnvironment.DEFAULT_EXTEND_PROPERTIES_PROPERTY_SOURCE_NAME, map));
            } else {
                environment.getPropertySources()
                    .addBefore(DefaultEnvironment.DEFAULT_PROPERTIES_PROPERTY_SOURCE_NAME,
                        new MapPropertySource(DefaultEnvironment.DEFAULT_EXTEND_PROPERTIES_PROPERTY_SOURCE_NAME, map));
            }
        }
    }

    /**
     * environment 转 map
     *
     * @param environment environment
     * @return the map
     * @since 1.4.0
     */
    public static @NotNull @UnmodifiableView Map<String, Object> extractProperties(ConfigurableEnvironment environment) {
        return Collections.unmodifiableMap(doExtraProperties(environment));
    }

    /**
     * Do extra properties
     *
     * @param environment environment
     * @return the map
     * @since 1.4.0
     */
    private static @NotNull Map<String, Object> doExtraProperties(ConfigurableEnvironment environment) {
        Map<String, Object> properties = new LinkedHashMap<>();
        Map<String, PropertySource<?>> map = doGetPropertySources(environment);
        for (PropertySource<?> source : map.values()) {
            if (source instanceof EnumerablePropertySource) {
                EnumerablePropertySource<?> propertySource = (EnumerablePropertySource<?>) source;
                String[] propertyNames = propertySource.getPropertyNames();
                if (ObjectUtils.isEmpty(propertyNames)) {
                    continue;
                }
                for (String propertyName : propertyNames) {
                    map.computeIfAbsent(propertyName, k -> (PropertySource<?>) propertySource.getProperty(k));
                }
            }
        }

        return properties;
    }

    /**
     * Do get property sources
     *
     * @param environment environment
     * @return the map
     * @since 1.4.0
     */
    private static @NotNull Map<String, PropertySource<?>> doGetPropertySources(@NotNull ConfigurableEnvironment environment) {
        Map<String, PropertySource<?>> map = new LinkedHashMap<>();
        MutablePropertySources sources = environment.getPropertySources();
        for (PropertySource<?> source : sources) {
            extract("", map, source);
        }
        return map;
    }

    /**
     * Extract
     *
     * @param root   root
     * @param map    map
     * @param source source
     * @since 1.4.0
     */
    private static void extract(String root, Map<String, PropertySource<?>> map, PropertySource<?> source) {
        if (source instanceof CompositePropertySource) {
            for (PropertySource<?> nest : ((CompositePropertySource) source)
                .getPropertySources()) {
                extract(source.getName() + ":", map, nest);
            }
        } else {
            map.put(root + source.getName(), source);
        }
    }

    /**
     * 将配置项绑定到配置类
     *
     * @param <T>         parameter
     * @param name        name
     * @param clazz       clazz
     * @param environment environment
     * @return the @ nullable t
     * @since 1.4.0
     */
    public static <T> @Nullable T bind(String name, Class<T> clazz, @NotNull ConfigurableEnvironment environment) {
        List<ConfigurationPropertySource> configs = new ArrayList<>();
        for (PropertySource<?> source : environment.getPropertySources()) {
            ConfigurationPropertySources.from(source).forEach(configs::add);
        }

        Binder binder = new Binder(configs);
        try {
            // 解析属性
            BindResult<T> response = binder.bind(name, clazz);
            return response.get();
        } catch (Exception ignored) {
            // nothing to do
        }
        return null;
    }

    /**
     * Bind
     *
     * @param <T>         parameter
     * @param name        name
     * @param instance    instance
     * @param environment environment
     * @param aliases     aliases
     * @return the @ nullable t
     * @since 1.4.0
     */
    public static <T> @Nullable T bind(String name,
                                       T instance,
                                       @NotNull ConfigurableEnvironment environment,
                                       ConfigurationPropertyNameAliases aliases) {
        ConfigurationPropertySource source = new MapConfigurationPropertySource(ConfigKit.extractProperties(environment));

        if (aliases != null) {
            Binder binder = new Binder(source.withAliases(aliases));
            // 通过类型绑定参数并获得实例对象
            return binder.bind(name, Bindable.ofInstance(instance)).get();
        }

        return null;
    }

    /**
     * Is v 5 framework
     *
     * @return the boolean
     * @since 1.4.0
     */
    public static boolean isV5Framework() {
        return BasicUtils.isV5Framework();
    }

    /**
     * Gets app version *
     *
     * @return the app version
     * @since 1.4.0
     */
    public static String getAppVersion() {
        return BasicUtils.getAppVersion();
    }

    /**
     * Gets framework version *
     *
     * @return the framework version
     * @since 1.4.0
     */
    public static String getFrameworkVersion() {
        return BasicUtils.getFrameworkVersion();
    }

}
