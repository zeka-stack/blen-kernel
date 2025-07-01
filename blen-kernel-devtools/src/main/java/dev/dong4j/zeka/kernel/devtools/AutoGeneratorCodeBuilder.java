package dev.dong4j.zeka.kernel.devtools;

import dev.dong4j.zeka.kernel.common.constant.App;
import dev.dong4j.zeka.kernel.common.util.StringUtils;
import dev.dong4j.zeka.kernel.common.util.SystemUtils;
import dev.dong4j.zeka.kernel.devtools.core.AutoGenerator;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dong4j@gmail.com"
 * @date 2019.12.26 21:45
 * @since 1.0.0
 */
@Slf4j
@SuppressWarnings("all")
public final class AutoGeneratorCodeBuilder {
    /** Model path */
    private String modelPath;
    /** Author */
    private String author = App.ZEKA_NAME_SPACE;
    /** Version */
    private String version = "1.0.0";
    private String company = "Zeka.Stack Inc.";
    private String email = "gmail.com";
    /** Tables */
    private String[] tables;
    /** 数据表前缀 */
    private String[] prefix;
    /** 自定义实体基类 */
    private String superBaseEntityClass = "dev.dong4j.zeka.starter.mybatis.base.BasePO";
    private String superExtendEntityClass = "dev.dong4j.zeka.starter.mybatis.base.BaseExtendPO";
    private String superWithTimeEntityClass = "dev.dong4j.zeka.starter.mybatis.base.BaseWithTimePO";
    private String superWithLogicEntityClass = "dev.dong4j.zeka.starter.mybatis.base.BaseWithLogicPO";
    private String superMapperClass = "dev.dong4j.zeka.starter.mybatis.base.BaseDao";
    /** 自定义 service 基类 */
    private String superServiceClass = "dev.dong4j.zeka.starter.mybatis.service.BaseService";
    /** 自定义 serviceImpl 基类 */
    private String superServiceImplClass = "dev.dong4j.zeka.starter.mybatis.service.impl.BaseServiceImpl";
    /** 自动以 controller 父类 */
    private String superControllerClass = "dev.dong4j.zeka.starter.rest.ServletController";
    /** 需要生成的 package */
    private String packageName;
    /** Componets */
    private String[] componets;
    /** Template */
    private String[] template;
    /** Module type */
    private ModuleConfig.ModuleType moduleType;
    /** Webapp */
    private boolean webapp = false;

    /**
     * Auto generator code builder
     *
     * @since 1.0.0
     */
    @Contract(pure = true)
    private AutoGeneratorCodeBuilder() {

    }

    /**
     * An auto generator code auto generator code builder.
     *
     * @return the auto generator code builder
     * @since 1.0.0
     */
    @NotNull
    @Contract(value = " -> new", pure = true)
    public static AutoGeneratorCodeBuilder onAutoGeneratorCode() {
        return new AutoGeneratorCodeBuilder();
    }

    /**
     * With model path auto generator code builder.
     *
     * @param modelPath the model path
     * @return the auto generator code builder
     * @since 1.0.0
     */
    @Contract("_ -> this")
    public AutoGeneratorCodeBuilder withModelPath(String modelPath) {
        this.modelPath = modelPath;
        return this;
    }

    /**
     * With author auto generator code builder
     *
     * @param author author
     * @return the auto generator code builder
     * @since 1.0.0
     */
    @Contract("_ -> this")
    public AutoGeneratorCodeBuilder withAuthor(String author) {
        this.author = author;
        return this;
    }

    /**
     * With version auto generator code builder
     *
     * @param version version
     * @return the auto generator code builder
     * @since 1.0.0
     */
    @Contract("_ -> this")
    public AutoGeneratorCodeBuilder withVersion(String version) {
        this.version = version;
        return this;
    }

    @Contract("_ -> this")
    public AutoGeneratorCodeBuilder withCompany(String companyName) {
        this.company = company;
        return this;
    }

    @Contract("_ -> this")
    public AutoGeneratorCodeBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    /**
     * With tables auto generator code builder.
     *
     * @param tables the tables'
     * @return the auto generator code builder
     * @since 1.0.0
     */
    @Contract("_ -> this")
    public AutoGeneratorCodeBuilder withTables(String[] tables) {
        this.tables = tables;
        return this;
    }

    /**
     * With prefix auto generator code builder
     *
     * @param prefix prefix
     * @return the auto generator code builder
     * @since 1.0.0
     */
    @Contract("_ -> this")
    public AutoGeneratorCodeBuilder withPrefix(String[] prefix) {
        this.prefix = prefix;
        return this;
    }

    /**
     * With super entity class auto generator code builder.
     *
     * @param superEntityClass the super entity class
     * @return the auto generator code builder
     * @since 1.0.0
     */
    @Contract("_ -> this")
    public AutoGeneratorCodeBuilder withSuperEntityClass(String superEntityClass) {
        this.superBaseEntityClass = superEntityClass;
        return this;
    }

    /**
     * With super mapper class auto generator code builder.
     *
     * @param superMapperClass the super mapper class
     * @return the auto generator code builder
     * @since 1.0.0
     */
    @Contract("_ -> this")
    public AutoGeneratorCodeBuilder withSuperMapperClass(String superMapperClass) {
        this.superMapperClass = superMapperClass;
        return this;
    }

    /**
     * With super service class auto generator code builder
     *
     * @param superServiceClass super service class
     * @return the auto generator code builder
     * @since 1.0.0
     */
    @Contract("_ -> this")
    public AutoGeneratorCodeBuilder withSuperServiceClass(String superServiceClass) {
        this.superServiceClass = superServiceClass;
        return this;
    }

    /**
     * With super service impl class auto generator code builder
     *
     * @param superServiceImplClass super service impl class
     * @return the auto generator code builder
     * @since 1.0.0
     */
    @Contract("_ -> this")
    public AutoGeneratorCodeBuilder withSuperServiceImplClass(String superServiceImplClass) {
        this.superServiceImplClass = superServiceImplClass;
        return this;
    }

    /**
     * With super controller class auto generator code builder.
     *
     * @param superControllerClass the super controller class
     * @return the auto generator code builder
     * @since 1.0.0
     */
    @Contract("_ -> this")
    public AutoGeneratorCodeBuilder withSuperControllerClass(String superControllerClass) {
        this.superControllerClass = superControllerClass;
        return this;
    }

    /**
     * With package name auto generator code builder.
     *
     * @param packageName the package name
     * @return the auto generator code builder
     * @since 1.0.0
     */
    @Contract("_ -> this")
    public AutoGeneratorCodeBuilder withPackageName(String packageName) {
        this.packageName = packageName;
        return this;
    }

    /**
     * With componets auto generator code builder.
     *
     * @param componets the componets
     * @return the auto generator code builder
     * @since 1.0.0
     */
    @Contract("_ -> this")
    public AutoGeneratorCodeBuilder withComponets(String... componets) {
        this.componets = componets;
        return this;
    }

    /**
     * With module type auto generator code builder
     *
     * @param moduleType module type
     * @return the auto generator code builder
     * @since 1.0.0
     */
    @Contract("_ -> this")
    public AutoGeneratorCodeBuilder withModuleType(ModuleConfig.ModuleType moduleType) {
        this.moduleType = moduleType;
        return this;
    }

    /**
     * With webapp auto generator code builder.
     *
     * @param webapp the webapp
     * @return the auto generator code builder
     * @since 1.0.0
     */
    @Contract("_ -> this")
    public AutoGeneratorCodeBuilder withWebapp(boolean webapp) {
        this.webapp = webapp;
        return this;
    }

    /**
     * With template auto generator code builder
     *
     * @param template template
     * @return the auto generator code builder
     * @since 1.0.0
     */
    @Contract("_ -> this")
    public AutoGeneratorCodeBuilder withTemplate(String... template) {
        this.template = template;
        return this;
    }

    /**
     * Build.
     *
     * @since 1.0.0
     */
    public void build() {
        AutoGeneratorCode autoGeneratorCode = new AutoGeneratorCode();
        String path = new File(this.modelPath).getAbsolutePath();
        autoGeneratorCode.setModelPath(path);

        Properties properties = this.getProperties();
        autoGeneratorCode.setDriverName(properties.getProperty("spring.datasource.driver-class-name"));
        autoGeneratorCode.setUserName(properties.getProperty("spring.datasource.username"));
        autoGeneratorCode.setPassWord(properties.getProperty("spring.datasource.password"));
        autoGeneratorCode.setUrl(properties.getProperty("spring.datasource.url"));
        autoGeneratorCode.setAuthor(StringUtils.isBlank(this.author) ? SystemUtils.USER_NAME : this.author);
        autoGeneratorCode.setVersion(this.version);
        autoGeneratorCode.setCompany(this.company);
        autoGeneratorCode.setEmail(this.email);

        autoGeneratorCode.setTables(this.tables);
        autoGeneratorCode.setPrefix(this.prefix);
        autoGeneratorCode.setSuperBaseEntityClass(this.superBaseEntityClass);
        autoGeneratorCode.setSuperExtendEntityClass(this.superExtendEntityClass);
        autoGeneratorCode.setSuperWithTimeEntityClass(this.superWithTimeEntityClass);
        autoGeneratorCode.setSuperWithLogicEntityClass(this.superWithLogicEntityClass);
        autoGeneratorCode.setSuperMapperClass(this.superMapperClass);
        autoGeneratorCode.setSuperServiceClass(this.superServiceClass);
        autoGeneratorCode.setSuperServiceImplClass(this.superServiceImplClass);
        autoGeneratorCode.setSuperControllerClass(this.superControllerClass);
        autoGeneratorCode.setPackageName(this.packageName);
        autoGeneratorCode.setComponets(this.componets);
        autoGeneratorCode.setWebapp(this.webapp);
        autoGeneratorCode.setTemplates(this.template);
        autoGeneratorCode.setModuleType(this.moduleType);

        AutoGenerator autoGenerator = new AutoGenerator();
        autoGenerator.setGlobalConfig(autoGeneratorCode.buildGlobalConfig())
            .setDataSource(autoGeneratorCode.buildDataSourceConfig())
            .setStrategy(autoGeneratorCode.buildStrategyConfig())
            .setPackageInfo(autoGeneratorCode.buildPackageConfig())
            .setTemplate(autoGeneratorCode.buildTemplateConfig())
            .setCfg(autoGeneratorCode.buildInjectionConfig())
            .setTemplateEngine(new ZekaVelocityTemplateEngine())
            .execute();

        log.debug("{}", autoGenerator.getCfg().getMap().get("app_name"));
    }

    /**
     * 获取配置文件
     *
     * @return 配置Props properties
     * @since 1.0.0
     */
    @NotNull
    private Properties getProperties() {
        InputStream inputStream;
        inputStream = this.getClass().getClassLoader().getResourceAsStream("jdbc.properties");
        if (inputStream == null) {
            URL url = this.getClass().getClassLoader().getResource("jdbc-default.properties");
            if (url != null) {
                try {
                    inputStream = url.openStream();
                } catch (IOException ignored) {
                    // nothing to do
                }
            }
        }
        // 读取配置文件
        Properties props = new Properties();
        try {
            props.load(inputStream);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        }
        return props;
    }
}
