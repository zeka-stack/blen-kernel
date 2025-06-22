package dev.dong4j.zeka.kernel.devtools;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.toolkit.JdbcUtils;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.FileOutConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.ITypeConvert;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.TemplateConfig;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.converts.OracleTypeConvert;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.IColumnType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.google.common.collect.Maps;
import dev.dong4j.zeka.kernel.common.asserts.Assertions;
import dev.dong4j.zeka.kernel.common.constant.App;
import dev.dong4j.zeka.kernel.common.exception.PropertiesException;
import dev.dong4j.zeka.kernel.common.util.CollectionUtils;
import dev.dong4j.zeka.kernel.common.util.DateUtils;
import dev.dong4j.zeka.kernel.common.util.GsonUtils;
import dev.dong4j.zeka.kernel.common.util.StringPool;
import dev.dong4j.zeka.kernel.common.util.StringUtils;
import dev.dong4j.zeka.kernel.common.util.Tools;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.2.3
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
    public static final String TEMPLATE_CONTROLLER = "/templates/controller.java";
    /** TEMPLATE_CONTROLLER_EX */
    public static final String TEMPLATE_CONTROLLER_EX = "/templates/controller-ext.java";
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
    /** TEMPLATE_MAPPER */
    public static final String TEMPLATE_MAPPER = "/templates/mapper.java";
    /** TEMPLATE_XML */
    public static final String TEMPLATE_XML = "/templates/mapper.xml" + VM_SUFFIX;
    /** TEMPLATE_SERVICE */
    public static final String TEMPLATE_SERVICE = "/templates/service.java";
    /** TEMPLATE_SERVICEIMPL */
    public static final String TEMPLATE_SERVICEIMPL = "/templates/serviceImpl.java";
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
    private String superEntityClass;
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
     * 生成全局变量
     *
     * @return the global config
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
            .setServiceName("%sRepositoryService")
            // 设置 serviceImpl 名称
            .setServiceImplName("%sRepositoryServiceImpl")
            // 设置 mapper 名称
            .setMapperName("%sDao")
            // 设置 xml 名称
            .setXmlName("%sMapper")
            // 生成后是否打开目录
            .setOpen(false);
        log.info("GlobalConfig = {}", GsonUtils.toJson(globalConfig, true));
        return globalConfig;
    }

    /**
     * Build data source config data source config.
     * 生成 数据源
     *
     * @return the data source config
     * @since 1.0.0
     */
    @SuppressWarnings("PMD.UndefineMagicConstantRule")
    DataSourceConfig buildDataSourceConfig() {
        ITypeConvert convert;
        DbType dbType = JdbcUtils.getDbType(this.url);
        switch (dbType) {
            case ORACLE:
                convert = new OracleTypeConvert() {
                    // 自定义数据库表字段类型转换【可选】
                    @Override
                    public IColumnType processTypeConvert(GlobalConfig globalConfig, String fieldType) {
                        log.info("转换类型: {}", fieldType);
                        // 注意！！processTypeConvert 存在默认类型转换,如果不是你要的效果请自定义返回、非如下直接返回.
                        return super.processTypeConvert(globalConfig, fieldType);
                    }
                };
                break;
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
                throw new UnsupportedOperationException("暂时不支持其他数据库类型转换");
        }

        DataSourceConfig dataSourceConfig = new DataSourceConfig().setDriverName(this.driverName)
            .setUsername(this.userName)
            .setPassword(this.passWord)
            .setUrl(this.url)
            .setTypeConvert(convert);

        log.info("dataSourceConfig = {}", GsonUtils.toJson(dataSourceConfig, true));
        return dataSourceConfig;
    }

    /**
     * 生成 策略配置
     *
     * @return the strategy config
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
            .setSuperEntityClass(this.superEntityClass)
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
            .setEntityBuilderModel(false);

        log.info("strategyConfig = {}", GsonUtils.toJson(strategyConfig, true));
        return strategyConfig;
    }

    /**
     * 生成 包配置
     *
     * @return the package config
     * @since 1.0.0
     */
    PackageConfig buildPackageConfig() {
        Assertions.notBlank(this.packageName, "未配置项目 package");
        String[] names = this.packageName.split("\\.");
        if (names.length < 1) {
            throw new PropertiesException("配置错误, 至少配置一个模块名吧!");
        }
        this.modelName = names[names.length - 1];
        this.modelName = Tools.isBlank(this.modelName) ? "auto" : this.modelName;
        // 首字母大写
        this.modelName = StringUtils.firstCharToUpper(this.modelName);

        this.packageConfig = new PackageConfig()
            .setParent(ROOT_PACKAGE + this.packageName)
            .setMapper("dao");

        log.info("packageConfig = {}", GsonUtils.toJson(this.packageConfig, true));
        return this.packageConfig;
    }

    /**
     * 自定义模板配置,可以 copy 源码 mybatis-plus/src/main/resources/template 下面内容修改,
     * 放置自己项目的 src/main/resources/template 目录下, 默认名称一下可以不配置,也可以自定义模板名称
     *
     * @return the template config
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
                    case TemplatesConfig.VO:
                        this.templatesConfig.setVo(true);
                        log.info("生成 vo 文件");
                        break;
                    case TemplatesConfig.DTO:
                        this.templatesConfig.setDto(true);
                        log.info("生成 dto 文件");
                        break;
                    // noinspection deprecation
                    case TemplatesConfig.WRAPPER:
                        log.error("请使用 TemplatesConfig.CONVERTER 代替");
                    case TemplatesConfig.CONVERTER:
                        this.templatesConfig.setConverter(true);
                        log.info("生成 converter 文件");
                        break;
                    case TemplatesConfig.START:
                        // 生成
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
     * Build template template config
     *
     * @return the template config
     * @since 1.0.0
     */
    private TemplateConfig buildTemplate() {
        TemplateConfig templateConfig = new TemplateConfig()
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
        if (this.templatesConfig.isVo()) {
            log.info("开启自定义 vo 生成");
        }
        if (this.templatesConfig.isConverter()) {
            log.info("开启自定义 converter 生成");
        }
        if (this.templatesConfig.isXml()) {
            log.info("开启自定义 xml 生成");
        }

        log.info("templateConfig = {}", GsonUtils.toJson(templateConfig, true));
        return templateConfig;
    }

    /**
     * 自定义配置
     *
     * @return the injection config
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
                map.put("package_converter", AutoGeneratorCode.this.packageConfig.getParent() + StringPool.DOT + "converter");
                map.put("version", AutoGeneratorCode.this.getVersion());
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
     * 生成常用的模板
     *
     * @param focList    foc list
     * @param parentPath parent path
     * @since 1.0.0
     */
    private void buildCommonTemplate(List<FileOutConfig> focList, String parentPath) {
        // 调整 xml 生成目录
        if (this.templatesConfig.isXml()) {
            focList.add(buildFileOutConfig(TEMPLATE_XML,
                this.modelPath + RESOURCE_ROOT_PATH + "mapper",
                "Mapper.xml",
                true));
        }
        // 调整 entity 的生成目录
        if (this.templatesConfig.isEntity()) {
            focList.add(buildFileOutConfig(TEMPLATE_ENTITY_JAVA,
                this.modelPath + JAVA_ROOT_PATH + parentPath + File.separator + "entity/po",
                ".java",
                true));
            // 生成一个 enums 目录
            File dir = new File(this.modelPath + JAVA_ROOT_PATH + parentPath + File.separator + "enums");
            if (!dir.mkdirs()) {
                log.error("[{}] 创建失败", dir);
            }
        }
        // 生成 vo, form, query, page
        if (this.templatesConfig.isVo()) {
            focList.add(buildFileOutConfig(TEMPLATE_VO_JAVA,
                this.modelPath + JAVA_ROOT_PATH + parentPath + File.separator + "entity/vo",
                "VO.java",
                true));
            focList.add(buildFileOutConfig(TEMPLATE_PAGE_JAVA,
                this.modelPath + JAVA_ROOT_PATH + parentPath + File.separator + "entity/vo",
                "Page.java",
                true));

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
                focList.add(buildFileOutConfig(TEMPLATE_SINGLE_WRAPPER_JAVA,
                    this.modelPath + JAVA_ROOT_PATH + parentPath + File.separator + "converter",
                    "Converter.java",
                    true));
            } else {
                focList.add(buildFileOutConfig(TEMPLATE_OUTER_WRAPPER_JAVA,
                    this.modelPath + JAVA_ROOT_PATH + parentPath + File.separator + "converter",
                    "ViewConverter.java",
                    true));
                focList.add(buildFileOutConfig(TEMPLATE_INNER_WRAPPER_JAVA,
                    this.modelPath + JAVA_ROOT_PATH + parentPath + File.separator + "converter",
                    "ServiceConverter.java",
                    true));
            }

        }
    }

    /**
     * 生成扩展模板
     *
     * @param focList    foc list
     * @param parentPath parent path
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
                    "application.yml",
                    false));
            }

        }
    }

    /**
     * 初始化配置模板
     *
     * @since 1.0.0
     */
    private void buildPropertiesTemplate() {
        this.propertiesConfig = new PropertiesConfig();
        if (CollectionUtils.isNotEmpty(Collections.singleton(this.componets))) {
            Arrays.stream(this.componets).forEach(c -> {
                switch (c) {
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
                File dir = new File(finalPath);
                if (!dir.exists()) {
                    boolean result = dir.mkdirs();
                    if (result) {
                        log.info("创建目录: [{}]", dir);
                    }
                }
                if (isSuffix) {
                    return dir + File.separator + tableInfo.getEntityName() + suffix;
                }
                return dir + File.separator + suffix;
            }
        };
    }
}
