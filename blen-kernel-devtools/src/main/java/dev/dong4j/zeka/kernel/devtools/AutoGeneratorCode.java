package dev.dong4j.zeka.kernel.devtools;

import com.baomidou.mybatisplus.annotation.DbType;
import com.google.common.collect.Maps;
import dev.dong4j.zeka.kernel.common.asserts.Assertions;
import dev.dong4j.zeka.kernel.common.constant.App;
import dev.dong4j.zeka.kernel.common.exception.PropertiesException;
import dev.dong4j.zeka.kernel.common.support.StrFormatter;
import dev.dong4j.zeka.kernel.common.util.CollectionUtils;
import dev.dong4j.zeka.kernel.common.util.ConfigKit;
import dev.dong4j.zeka.kernel.common.util.DateUtils;
import dev.dong4j.zeka.kernel.common.util.Jsons;
import dev.dong4j.zeka.kernel.common.util.StringPool;
import dev.dong4j.zeka.kernel.common.util.StringUtils;
import dev.dong4j.zeka.kernel.devtools.core.InjectionConfig;
import dev.dong4j.zeka.kernel.devtools.core.JdbcUtils;
import dev.dong4j.zeka.kernel.devtools.core.config.DataSourceConfig;
import dev.dong4j.zeka.kernel.devtools.core.config.FileOutConfig;
import dev.dong4j.zeka.kernel.devtools.core.config.GlobalConfig;
import dev.dong4j.zeka.kernel.devtools.core.config.ITypeConvert;
import dev.dong4j.zeka.kernel.devtools.core.config.PackageConfig;
import dev.dong4j.zeka.kernel.devtools.core.config.StrategyConfig;
import dev.dong4j.zeka.kernel.devtools.core.config.TemplateConfig;
import dev.dong4j.zeka.kernel.devtools.core.config.converts.MySqlTypeConvert;
import dev.dong4j.zeka.kernel.devtools.core.config.po.TableInfo;
import dev.dong4j.zeka.kernel.devtools.core.config.rules.DateType;
import dev.dong4j.zeka.kernel.devtools.core.config.rules.DbColumnType;
import dev.dong4j.zeka.kernel.devtools.core.config.rules.IColumnType;
import dev.dong4j.zeka.kernel.devtools.core.config.rules.NamingStrategy;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * 自动代码生成器核心配置类，提供MyBatis-Plus代码生成的完整配置和执行功能
 * <p>
 * 该类封装了代码生成器的所有配置项，包括数据源配置、全局配置、策略配置、包配置等
 * 支持生成Entity、Mapper、Service、Controller等各层代码，以及相关的配置文件
 * <p>
 * 主要功能：
 * - 数据库表结构扫描和代码生成
 * - 支持多种数据库类型（MySQL、PostgreSQL等）
 * - 自定义模板和生成策略
 * - 支持单模块和多模块项目结构
 * - 生成Spring Boot应用的完整项目结构
 * <p>
 * 使用示例：
 * {@code AutoGeneratorCode generator = new AutoGeneratorCode();}
 * {@code generator.setModelPath("/path/to/project").setAuthor("developer");}
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2019.12.26 21:45
 * @since 1.0.0
 */
@Data
@Slf4j
@Accessors(chain = true)
@SuppressWarnings("all")
public class AutoGeneratorCode {
    /** ROOT_PACKAGE */
    public static final String ROOT_PACKAGE = App.BASE_PACKAGES + StringPool.DOT;
    /** 自定义生成文件时需要指定文件后缀 */
    public static final String VM_SUFFIX = ".vm";
    /** TEMPLATE_CONTROLLER */
    public static final String TEMPLATE_ENUM = "/templates/enums.java" + VM_SUFFIX;
    /** TEMPLATE_CONTROLLER */
    public static final String TEMPLATE_CONTROLLER = "/templates/controller.java" + VM_SUFFIX;
    /** TEMPLATE_CONTROLLER_EX */
    public static final String TEMPLATE_CONTROLLER_EX = "/templates/controller-ext.java" + VM_SUFFIX;
    /** TEMPLATE_ENTITY_JAVA */
    public static final String TEMPLATE_ENTITY_JAVA = "/templates/entity.java" + VM_SUFFIX;
    /** TEMPLATE_VO_JAVA */
    public static final String TEMPLATE_VO_JAVA = "/templates/vo.java" + VM_SUFFIX;
    /** TEMPLATE_PAGE_JAVA */
    public static final String TEMPLATE_PAGE_JAVA = "/templates/page.java" + VM_SUFFIX;
    /** TEMPLATE_DTO_JAVA */
    public static final String TEMPLATE_DTO_JAVA = "/templates/dto.java" + VM_SUFFIX;
    /** TEMPLATE_FORM_JAVA */
    public static final String TEMPLATE_FORM_JAVA = "/templates/form.java" + VM_SUFFIX;
    /** TEMPLATE_QUERY_JAVA */
    public static final String TEMPLATE_QUERY_JAVA = "/templates/query.java" + VM_SUFFIX;
    /** TEMPLATE_WRAPPER_JAVA */
    public static final String TEMPLATE_WRAPPER_JAVA = "/templates/wrapper.java" + VM_SUFFIX;
    /** TEMPLATE_SINGLE_WRAPPER_JAVA */
    public static final String TEMPLATE_SINGLE_WRAPPER_JAVA = "/templates/single-wrapper.java" + VM_SUFFIX;
    /** TEMPLATE_OUTER_WRAPPER_JAVA */
    public static final String TEMPLATE_OUTER_WRAPPER_JAVA = "/templates/outer-wrapper.java" + VM_SUFFIX;
    /** TEMPLATE_INNER_WRAPPER_JAVA */
    public static final String TEMPLATE_INNER_WRAPPER_JAVA = "/templates/inner-wrapper.java" + VM_SUFFIX;
    /** TEMPLATE_EXTEND_WRAPPER_JAVA */
    public static final String TEMPLATE_EXTEND_WRAPPER_JAVA = "/templates/extend-wrapper.java" + VM_SUFFIX;
    /** TEMPLATE_MAPPER */
    public static final String TEMPLATE_MAPPER = "/templates/mapper.java" + VM_SUFFIX;
    /** TEMPLATE_XML */
    public static final String TEMPLATE_XML = "/templates/mapper.xml" + VM_SUFFIX;
    /** TEMPLATE_SERVICE */
    public static final String TEMPLATE_SERVICE = "/templates/service.java" + VM_SUFFIX;
    /** TEMPLATE_SERVICEIMPL */
    public static final String TEMPLATE_SERVICEIMPL = "/templates/serviceImpl.java" + VM_SUFFIX;
    /** TEMPLATE_GLOBAL_PROPERTIES */
    public static final String TEMPLATE_GLOBAL_PROPERTIES = "/templates/application.properties" + VM_SUFFIX;
    /** TEMPLATE_APPLICATION */
    public static final String TEMPLATE_APPLICATION = "/templates/Application.java" + VM_SUFFIX;
    /** TEMPLATE_APPLICATION_TEST */
    public static final String TEMPLATE_APPLICATION_TEST = "/templates/ApplicationTest.java" + VM_SUFFIX;
    /** TEMPLATE_CLOUD_YML */
    public static final String TEMPLATE_CLOUD_YML = "/templates/bootstrap.yml" + VM_SUFFIX;
    /** TEMPLATE_BOOT_YML */
    public static final String TEMPLATE_BOOT_YML = "/templates/application.yml" + VM_SUFFIX;
    /** RESOURCE_ROOT_PATH */
    public static final String RESOURCE_ROOT_PATH = "/src/main/resources/";
    /** ASSEMBLY_PATH */
    public static final String ASSEMBLY_PATH = "/src/main/assembly/";
    /** BIN_PATH */
    public static final String BIN_PATH = "/src/main/bin/";
    /** RESOURCE_TEST_PATH */
    public static final String RESOURCE_TEST_PATH = "/src/test/resources/";
    /** JAVA_ROOT_PATH */
    public static final String JAVA_ROOT_PATH = "/src/main/java/";
    /** JAVA_TEST_PATH */
    public static final String JAVA_TEST_PATH = "/src/test/java/";
    /** 设置 model 路径, 最后一级为 src 上一级 */
    private String modelPath;
    /** Author */
    private String author;
    /** Version */
    private String version;
    /** Company */
    private String company;
    /** Email */
    private String email;
    /** Model name */
    private String modelName;
    /** Driver name */
    private String driverName;
    /** User name */
    private String userName;
    /** Pass word */
    private String passWord;
    /** Url */
    private String url;
    /** Tables */
    private String[] tables;
    /** Prefix */
    private String[] prefix;
    /** 自定义实体父类 */
    private String superBaseEntityClass;
    /** Super extend entity class */
    private String superExtendEntityClass;
    /** Super with time entity class */
    private String superWithTimeEntityClass;
    /** Super with logic entity class */
    private String superWithLogicEntityClass;
    /** 自定义 dao 父类 */
    private String superMapperClass;
    /** 自定义 service 基类 */
    private String superServiceClass;
    /** 自定义 serviceImpl 基类 */
    private String superServiceImplClass;
    /** 自定义 controller 父类 */
    private String superControllerClass;
    /** 需要生成的 package */
    private String packageName;
    /** 需要生成的组件的配置 */
    private String[] componets;
    /** Templates */
    private String[] templates;
    /** 是否为 web 应用 */
    private boolean webapp;
    /** Package config */
    private PackageConfig packageConfig;
    /** Templates config */
    private TemplatesConfig templatesConfig;
    /** Properties config */
    private PropertiesConfig propertiesConfig;
    /** Module type */
    private ModuleConfig.ModuleType moduleType;

    /**
     * 生成全局配置对象
     *
     * 配置代码生成器的全局参数，包括输出路径、作者信息、文件覆盖策略等
     * 设置各个组件的名称格式和ActiveRecord支持
     *
     * @return 全局配置对象
     * @since 1.0.0
     */
    GlobalConfig buildGlobalConfig() {
        // 输出路径
        GlobalConfig globalConfig = new GlobalConfig()
            .setDateType(DateType.ONLY_DATE)
            .setOutputDir(this.modelPath + "/src/main/java")
            // 开启 ActiveRecord 支持
            .setActiveRecord(true)
            // 设置 author
            .setAuthor(this.author)
            // 是否覆盖文件
            .setFileOverride(false)
            // 是否开始二级缓存
            .setEnableCache(false)
            // 是否设置 sql 的 columnlist
            .setBaseColumnList(true)
            // 是否设置 resultmap
            .setBaseResultMap(true)
            // 设置 controller 名称 (%s 会自动填充表实体属性)
            .setControllerName("%sController")
            // 设置 service 名称
            .setServiceName("%sService")
            // 设置 serviceImpl 名称
            .setServiceImplName("%sServiceImpl")
            // 设置 mapper 名称
            .setMapperName("%sMapper")
            // 设置 xml 名称
            .setXmlName("%sMapper")
            // 生成后是否打开目录
            .setOpen(false);
        log.info("GlobalConfig = {}", Jsons.toJson(globalConfig, true));
        return globalConfig;
    }

    /**
     * 构建数据源配置对象
     *
     * 根据数据库URL自动识别数据库类型，配置相应的类型转换器
     * 支持MySQL等主流数据库，并提供自定义的类型映射规则
     *
     * @return 数据源配置对象
     * @since 1.0.0
     */
    @SuppressWarnings("PMD.UndefineMagicConstantRule")
    DataSourceConfig buildDataSourceConfig() {
        ITypeConvert convert = null;
        DbType dbType = JdbcUtils.getDbType(this.url);
        switch (dbType) {
            case MYSQL:
                convert = new MySqlTypeConvert() {
                    @Override
                    public IColumnType processTypeConvert(GlobalConfig globalConfig, String fieldType) {
                        log.info("转换类型: {}", fieldType);
                        String t = fieldType.toLowerCase();
                        // tinyint 转换为 Integer 类型
                        if (t.contains("tinyint")) {
                            log.info("转换类型: {} -> Integer", fieldType);
                            return DbColumnType.INTEGER;
                        }
                        // 注意！！processTypeConvert 存在默认类型转换,如果不是你要的效果请自定义返回、非如下直接返回.
                        return super.processTypeConvert(globalConfig, fieldType);
                    }
                };
                break;
            default:
                break;
        }

        DataSourceConfig dataSourceConfig = new DataSourceConfig().setDriverName(this.driverName)
            .setUsername(this.userName)
            .setPassword(this.passWord)
            .setUrl(this.url);

        if (convert != null) {
            dataSourceConfig.setTypeConvert(convert);
        }

        log.info("dataSourceConfig = {}", dataSourceConfig);
        return dataSourceConfig;
    }

    /**
     * 构建策略配置对象
     *
     * 配置代码生成的策略参数，包括命名策略、表前缀处理、父类配置等
     * 设置Entity、Mapper、Service、Controller的父类，支持Lombok和RestController风格
     *
     * @return 策略配置对象
     * @since 1.0.0
     */
    StrategyConfig buildStrategyConfig() {
        StrategyConfig strategyConfig = new StrategyConfig()
            // 全局大写命名 ORACLE 注意
            .setCapitalMode(true)
            // 是否开启 lombok 支持
            .setEntityLombokModel(true)
            // 是否开启 @RestContriller 风格
            .setRestControllerStyle(true)
            // 此处可以修改为您的表前缀
            .setTablePrefix(this.prefix)
            // 表名生成策略 (下划线转驼峰)
            .setNaming(NamingStrategy.underline_to_camel)
            // 需要生成的表
            .setInclude(this.tables)
            // 自定义实体父类
            .setSuperBaseEntityClass(this.superBaseEntityClass)
            .setSuperExtendEntityClass(this.superExtendEntityClass)
            .setSuperWithTimeEntityClass(this.superWithTimeEntityClass)
            .setSuperWithLogicEntityClass(this.superWithLogicEntityClass)
            // 自定义实体,公共字段
            .setSuperEntityColumns("id", "deleted", "update_time", "create_time")
            // 自定义 dao 父类
            .setSuperMapperClass(this.superMapperClass)
            // 自定义 service 父类
            .setSuperServiceClass(this.superServiceClass)
            // 自定义 service 实现类父类
            .setSuperServiceImplClass(this.superServiceImplClass)
            // 自定义 controller 父类
            .setSuperControllerClass(this.superControllerClass)
            // 实体是否生成字段常量 (默认 false)
            .setEntityColumnConstant(true)
            // 实体是否为构建者模型 (默认 false)
            .setChainModel(false);

        log.info("strategyConfig = {}", Jsons.toJson(strategyConfig, true));
        return strategyConfig;
    }

    /**
     * 构建包配置对象
     *
     * 根据指定的包名生成各个组件的包路径
     * 自动解析模块名称，设置根包和各层组件的包路径
     *
     * @return 包配置对象
     * @since 1.0.0
     */
    PackageConfig buildPackageConfig() {
        Assertions.notBlank(this.packageName, "未配置项目 package");
        String[] names = this.packageName.split("\\.");
        if (names.length < 1) {
            throw new PropertiesException("配置错误, 至少配置一个模块名吧!");
        }
        this.modelName = names[names.length - 1];
        this.modelName = StringUtils.isBlank(this.modelName) ? "auto" : this.modelName;
        // 首字母大写
        this.modelName = StringUtils.firstCharToUpper(this.modelName);

        this.packageConfig = new PackageConfig()
            .setParent(ROOT_PACKAGE + this.packageName)
            .setMapper("dao");

        log.info("packageConfig = {}", Jsons.toJson(this.packageConfig, true));
        return this.packageConfig;
    }

    /**
     * 构建模板配置对象
     *
     * 根据指定的模板类型配置需要生成的文件类型
     * 支持Controller、Service、Mapper、Entity等各种组件的生成
     * 可以自定义模板路径，默认使用内置模板
     *
     * @return 模板配置对象
     * @since 1.0.0
     */
    TemplateConfig buildTemplateConfig() {
        this.templatesConfig = new TemplatesConfig();
        if (this.templates == null || this.templates.length == 0) {
            this.templatesConfig.setController(true)
                .setDao(true)
                .setEntity(true)
                .setImpl(true)
                .setService(true)
                .setImpl(true)
                .setStart(true);
        } else {
            // 是否生成其他全局配置
            for (String template : this.templates) {
                switch (template) {
                    case TemplatesConfig.ENUM:
                        this.templatesConfig.setEnums(true);
                        log.info("生成 enum 文件");
                        break;
                    case TemplatesConfig.CONTROLLER:
                        this.templatesConfig.setController(true);
                        log.info("生成 controller 文件");
                        break;
                    case TemplatesConfig.SERVICE:
                        this.templatesConfig.setService(true);
                        log.info("生成 service 文件");
                        break;
                    case TemplatesConfig.IMPL:
                        this.templatesConfig.setImpl(true);
                        log.info("生成 service impl  文件");
                        break;
                    case TemplatesConfig.DAO:
                        this.templatesConfig.setDao(true);
                        log.info("生成 dao 文件");
                        break;
                    case TemplatesConfig.XML:
                        this.templatesConfig.setXml(true);
                        log.info("生成 mapper.xml 文件");
                        break;
                    case TemplatesConfig.ENTITY:
                        this.templatesConfig.setEntity(true);
                        log.info("生成 entity 文件");
                        break;
                    case TemplatesConfig.QUERY:
                        this.templatesConfig.setQuery(true);
                        log.info("生成 query 和 form 文件");
                        break;
                    case TemplatesConfig.DTO:
                        this.templatesConfig.setDto(true);
                        log.info("生成 dto 文件");
                        break;
                    case TemplatesConfig.CONVERTER:
                        this.templatesConfig.setConverter(true);
                        log.info("生成 converter 文件");
                        break;
                    case TemplatesConfig.START:
                        this.templatesConfig.setStart(true);
                        log.info("生成启动类");
                        break;
                    default:
                        break;
                }
            }
        }

        return this.buildTemplate();
    }

    /**
     * 构建模板配置对象（内部方法）
     *
     * 根据TemplatesConfig的配置设置各个组件的模板路径
     * 对于启用的组件设置相应的模板文件路径
     *
     * @return 模板配置对象
     * @since 1.0.0
     */
    private TemplateConfig buildTemplate() {
        TemplateConfig templateConfig = new TemplateConfig()
            .setEnums(null)
            .setController(null)
            .setService(null)
            .setEntity(null)
            .setMapper(null)
            .setServiceImpl(null)
            .setXml(null);

        if (this.templatesConfig.isController()) {
            templateConfig.setController(TEMPLATE_CONTROLLER_EX);
        }
        if (this.templatesConfig.isService()) {
            templateConfig.setService(TEMPLATE_SERVICE);
        }
        if (this.templatesConfig.isImpl()) {
            templateConfig.setServiceImpl(TEMPLATE_SERVICEIMPL);
        }
        if (this.templatesConfig.isDao()) {
            templateConfig.setMapper(TEMPLATE_MAPPER);
        }
        if (this.templatesConfig.isEntity()) {
            log.info("开启自定义 entity 生成");
        }
        if (this.templatesConfig.isDto()) {
            log.info("开启自定义 dto 生成");
        }
        if (this.templatesConfig.isQuery()) {
            log.info("开启自定义 vo 生成");
        }
        if (this.templatesConfig.isConverter()) {
            log.info("开启自定义 converter 生成");
        }
        if (this.templatesConfig.isXml()) {
            log.info("开启自定义 xml 生成");
        }
        if (this.templatesConfig.isEnums()) {
            log.info("开启自定义 enum 生成");
        }

        log.info("templateConfig = {}", Jsons.toJson(templateConfig, true));
        return templateConfig;
    }

    /**
     * 构建注入配置对象
     *
     * 配置自定义变量和文件输出配置，用于模板中的变量替换
     * 支持生成各种自定义文件，如DTO、VO、Converter等
     *
     * @return 注入配置对象
     * @since 1.0.0
     */
    InjectionConfig buildInjectionConfig() {
        log.info("开始处理自定义配置");
        // 注入自定义配置,可以在 VM 中使用 cfg.abc 【可无】
        InjectionConfig injectionConfig = new InjectionConfig() {
            @Override
            public void initMap() {
                Map<String, Object> map = Maps.newHashMapWithExpectedSize(3);
                map.put("app_name", "${project.artifactId}");
                map.put("app_version", "${project.version}");
                // 替换包变量名, 避免被替换
                map.put("profile_active", "${profile.active}");
                map.put("package_dto",
                    AutoGeneratorCode.this.packageConfig.getParent()
                        + StringPool.DOT
                        + AutoGeneratorCode.this.packageConfig.getEntity()
                        + StringPool.DOT + "dto");
                map.put("package_vo",
                    AutoGeneratorCode.this.packageConfig.getParent()
                        + StringPool.DOT
                        + AutoGeneratorCode.this.packageConfig.getEntity()
                        + StringPool.DOT + "vo");
                map.put("package_form",
                    AutoGeneratorCode.this.packageConfig.getParent()
                        + StringPool.DOT
                        + AutoGeneratorCode.this.packageConfig.getEntity()
                        + StringPool.DOT + "form");
                map.put("package_po",
                    AutoGeneratorCode.this.packageConfig.getParent()
                        + StringPool.DOT
                        + AutoGeneratorCode.this.packageConfig.getEntity()
                        + StringPool.DOT
                        + "po");
                map.put("package_enums",
                    AutoGeneratorCode.this.packageConfig.getParent()
                        + StringPool.DOT + "enums");
                map.put("package_converter", AutoGeneratorCode.this.packageConfig.getParent() + StringPool.DOT + "entity" + StringPool.DOT + "converter");
                map.put("version", AutoGeneratorCode.this.getVersion());
                map.put("company", AutoGeneratorCode.this.getCompany());
                map.put("email", AutoGeneratorCode.this.getEmail());
                map.put("date", DateUtils.format(new Date(), "yyyy.MM.dd HH:mm"));
                map.put("model_name", AutoGeneratorCode.this.modelName);
                map.put("package_name", AutoGeneratorCode.this.packageConfig.getParent());
                // 生成 application.properties
                map.put("gloabl", true);
                map.put("webapp", AutoGeneratorCode.this.webapp);
                map.put("pc", AutoGeneratorCode.this.propertiesConfig);
                this.setMap(map);
            }
        };

        // 调用 initMap(), 将 map 注入到 InjectionConfig
        injectionConfig.initMap();

        List<FileOutConfig> focList = new ArrayList<>();
        String parentPath = this.packageConfig.getParent().replace(StringPool.DOT, File.separator);
        this.buildCommonTemplate(focList, parentPath);

        this.buildExpandTemplate(focList, parentPath);

        injectionConfig.setFileOutConfigList(focList);

        return injectionConfig;
    }

    /**
     * 构建常用模板文件配置
     *
     * 配置常用的模板文件输出路径和文件名称
     * 包括XML映射文件、Entity、Form、Query、DTO、Converter等
     *
     * @param focList    文件输出配置列表
     * @param parentPath 父级包路径
     * @since 1.0.0
     */
    private void buildCommonTemplate(List<FileOutConfig> focList, String parentPath) {
        // 调整 xml 生成目录
        if (this.templatesConfig.isXml()) {
            focList.add(buildFileOutConfig(TEMPLATE_XML,
                this.modelPath + RESOURCE_ROOT_PATH + "mappers",
                "Mapper.xml",
                true));
        }
        // 调整 entity 的生成目录
        if (this.templatesConfig.isEntity()) {
            focList.add(buildFileOutConfig(TEMPLATE_ENTITY_JAVA,
                this.modelPath + JAVA_ROOT_PATH + parentPath + File.separator + "entity/po",
                ".java",
                true));
        }
        // 生成 form, query
        if (this.templatesConfig.isQuery()) {
            focList.add(buildFileOutConfig(TEMPLATE_FORM_JAVA,
                this.modelPath + JAVA_ROOT_PATH + parentPath + File.separator + "entity/form",
                "Form.java",
                true));
            focList.add(buildFileOutConfig(TEMPLATE_QUERY_JAVA,
                this.modelPath + JAVA_ROOT_PATH + parentPath + File.separator + "entity/form",
                "Query.java",
                true));
        }
        // 生成 dto
        if (this.templatesConfig.isDto()) {
            focList.add(buildFileOutConfig(TEMPLATE_DTO_JAVA,
                this.modelPath + JAVA_ROOT_PATH + parentPath + File.separator + "entity/dto",
                "DTO.java",
                true));
        }

        // 生成  converter
        if (this.templatesConfig.isConverter()) {
            if (ModuleConfig.ModuleType.SINGLE_MODULE.equals(this.moduleType)) {
                focList.add(buildFileOutConfig(TEMPLATE_EXTEND_WRAPPER_JAVA,
                    this.modelPath + JAVA_ROOT_PATH + parentPath + File.separator + "entity/converter",
                    "Converter.java",
                    true));
            } else {
                focList.add(buildFileOutConfig(TEMPLATE_OUTER_WRAPPER_JAVA,
                    this.modelPath + JAVA_ROOT_PATH + parentPath + File.separator + "entity/converter",
                    "ViewConverter.java",
                    true));
                focList.add(buildFileOutConfig(TEMPLATE_INNER_WRAPPER_JAVA,
                    this.modelPath + JAVA_ROOT_PATH + parentPath + File.separator + "entity/converter",
                    "ServiceConverter.java",
                    true));
            }

        }

        // 生成枚举
        if (this.templatesConfig.isEnums()) {
            focList.add(buildFileOutConfig(TEMPLATE_ENUM,
                this.modelPath + JAVA_ROOT_PATH + parentPath + File.separator + "enums",
                "",
                false));
        }

    }

    /**
     * 构建扩展模板文件配置
     *
     * 配置项目的扩展模板文件，如启动类、单元测试、配置文件等
     * 支持Spring Boot和Spring Cloud的配置文件生成
     *
     * @param focList    文件输出配置列表
     * @param parentPath 父级包路径
     * @since 1.0.0
     */
    private void buildExpandTemplate(List<FileOutConfig> focList, String parentPath) {
        this.buildPropertiesTemplate();

        if (this.templatesConfig.isStart()) {
            log.info("生成启动类");
            focList.add(buildFileOutConfig(TEMPLATE_APPLICATION,
                this.modelPath + JAVA_ROOT_PATH + parentPath,
                this.modelName + "Application.java",
                false));

            log.info("生成单元测试主类");
            focList.add(buildFileOutConfig(TEMPLATE_APPLICATION_TEST,
                this.modelPath + JAVA_TEST_PATH + parentPath,
                this.modelName + "ApplicationTest.java",
                false));

            if (this.propertiesConfig.isCloudConfig()) {
                log.info("生成 spring cloud 配置文件");
                focList.add(buildFileOutConfig(TEMPLATE_CLOUD_YML,
                    this.modelPath + RESOURCE_ROOT_PATH,
                    "bootstrap.yml",
                    false));
            }
            if (this.propertiesConfig.isBootConfig()) {
                log.info("生成 spring boot 配置文件");
                focList.add(buildFileOutConfig(TEMPLATE_BOOT_YML,
                    this.modelPath + RESOURCE_ROOT_PATH,
                    ConfigKit.BOOT_CONFIG_FILE_NAME,
                    false));
            }

        }

    }

    /**
     * 初始化属性配置模板
     *
     * 根据指定的组件数组初始化相应的属性配置
     * 支持ActiveMQ、Redis、MyBatis、Dubbo等多种组件的配置
     *
     * @since 1.0.0
     */
    private void buildPropertiesTemplate() {
        this.propertiesConfig = new PropertiesConfig();
        if (CollectionUtils.isNotEmpty(Collections.singleton(this.componets))) {
            Arrays.stream(this.componets).forEach(c -> {
                switch (c) {
                    case PropertiesConfig.ACTIVEMQ:
                        this.propertiesConfig.setActivemq(true);
                        log.info("生成 activemq 相关配置, 需要 cubo-activemq 依赖");
                        break;
                    case PropertiesConfig.EMAIL:
                        this.propertiesConfig.setEmail(true);
                        log.info("生成 email 相关配置, 需要 cubo-email 依赖");
                        break;
                    case PropertiesConfig.REDIS:
                        this.propertiesConfig.setRedis(true);
                        log.info("生成 redis 相关配置, 需要 cubo-redis 依赖, "
                            + "默认为 standalone 模式, 其他模式配置参照 cubo-redis/src/test/resource 配置");
                        break;
                    case PropertiesConfig.ROCKTEMQ:
                        this.propertiesConfig.setRocktemq(true);
                        log.info("生成 rocketmq 相关配置, 需要 cubo-rocketmq 依赖");
                        break;
                    case PropertiesConfig.WEBSERVICE:
                        this.propertiesConfig.setWebservice(true);
                        log.info("生成 webservice 相关配置, 需要 cubo-webservice 依赖");
                        break;
                    case PropertiesConfig.WEBSOCKET:
                        this.propertiesConfig.setWebsocket(true);
                        log.info("生成 websocklet 相关配置, 需要 cubo-websocket 依赖");
                        break;
                    case PropertiesConfig.MYBATIS:
                        this.propertiesConfig.setMybatis(true);
                        log.info("生成 mybatis + jdbc + druid 相关配置, 需要 cubo-mybatis 依赖");
                        log.info("如果是多数据源, 需要 ms-mybatis-multi 依赖, 如果不能满足需求, 可以参照 cubo-mybatis-multi");
                        break;
                    case PropertiesConfig.DUBBO:
                        this.propertiesConfig.setDubbo(true);
                        log.info("生成 dubbo 相关配置, 需要 ms-dubbo 依赖");
                        break;
                    case PropertiesConfig.ADMIN:
                        this.propertiesConfig.setAdmin(true);
                        log.warn("需要 spring-boot-starter-web, spring-boot-starter-actuator, "
                            + "spring-boot-admin-starter-client, spring-boot-starter-security 依赖");
                        break;
                    case PropertiesConfig.BOOT_CONFIG:
                        this.propertiesConfig.setBootConfig(true);
                        break;
                    case PropertiesConfig.CLOUD_CONFIG:
                        this.propertiesConfig.setCloudConfig(true);
                        break;
                    default:
                        break;
                }
            });
        }
    }

    /**
     * Build file out config file out config.
     *
     * @param template  the template
     * @param finalPath the final path
     * @param suffix    the suffix
     * @param isSuffix  is suffix
     * @return the file out config
     * @since 1.0.0
     */
    @Contract(value = "_, _, _, _ -> new", pure = true)
    @NotNull
    private static FileOutConfig buildFileOutConfig(String template, String finalPath, String suffix, boolean isSuffix) {
        return new FileOutConfig(template) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                String filePath = finalPath;
                if (finalPath.contains("views/{}")) {
                    filePath = StrFormatter.format(finalPath, tableInfo.getEntityName());
                }
                File dir = new File(filePath);
                if (!dir.exists()) {
                    boolean result = dir.mkdirs();
                    if (result) {
                        log.info("创建目录: [{}]", dir);
                    }
                }
                if (isSuffix) {
                    if (suffix.equals(".js")) {
                        return dir + File.separator + StringUtils.firstCharToLower(tableInfo.getEntityName()) + suffix;
                    } else {
                        return dir + File.separator + tableInfo.getEntityName() + suffix;
                    }
                }
                return dir + File.separator + suffix;
            }
        };
    }

}
