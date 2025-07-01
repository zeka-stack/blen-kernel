package dev.dong4j.zeka.kernel.devtools.core.engine;

import dev.dong4j.zeka.kernel.common.util.CollectionUtils;
import dev.dong4j.zeka.kernel.common.util.StringPool;
import dev.dong4j.zeka.kernel.common.util.StringUtils;
import dev.dong4j.zeka.kernel.devtools.AutoGeneratorCode;
import dev.dong4j.zeka.kernel.devtools.core.InjectionConfig;
import dev.dong4j.zeka.kernel.devtools.core.config.ConstVal;
import dev.dong4j.zeka.kernel.devtools.core.config.FileOutConfig;
import dev.dong4j.zeka.kernel.devtools.core.config.GlobalConfig;
import dev.dong4j.zeka.kernel.devtools.core.config.TemplateConfig;
import dev.dong4j.zeka.kernel.devtools.core.config.builder.ConfigBuilder;
import dev.dong4j.zeka.kernel.devtools.core.config.po.EnumType;
import dev.dong4j.zeka.kernel.devtools.core.config.po.TableField;
import dev.dong4j.zeka.kernel.devtools.core.config.po.TableInfo;
import dev.dong4j.zeka.kernel.devtools.core.config.rules.FileType;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 模板引擎抽象类
 *
 * @author hubin
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2024.04.02 23:58
 * @since 2018 -01-10
 */
@SuppressWarnings("all")
public abstract class AbstractTemplateEngine {

    /** logger */
    protected static final Logger logger = LoggerFactory.getLogger(AbstractTemplateEngine.class);
    /**
     * 配置信息
     */
    private ConfigBuilder configBuilder;


    /**
     * 模板引擎初始化
     *
     * @param configBuilder config builder
     * @return the abstract template engine
     * @since 2024.2.0
     */
    public AbstractTemplateEngine init(ConfigBuilder configBuilder) {
        this.configBuilder = configBuilder;
        return this;
    }


    /**
     * 输出 java xml 文件
     *
     * @return the abstract template engine
     * @since 2024.2.0
     */
    public AbstractTemplateEngine batchOutput() {
        try {
            List<TableInfo> tableInfoList = getConfigBuilder().getTableInfoList();
            for (TableInfo tableInfo : tableInfoList) {
                Map<String, Object> objectMap = getObjectMap(tableInfo);
                Map<String, String> pathInfo = getConfigBuilder().getPathInfo();
                TemplateConfig template = getConfigBuilder().getTemplate();
                // 自定义内容
                InjectionConfig injectionConfig = getConfigBuilder().getInjectionConfig();
                if (null != injectionConfig) {
                    injectionConfig.initTableMap(tableInfo);
                    objectMap.put("cfg", injectionConfig.getMap());
                    List<FileOutConfig> focList = injectionConfig.getFileOutConfigList();
                    if (CollectionUtils.isNotEmpty(focList)) {
                        for (FileOutConfig foc : focList) {
                            // 如果是枚举模板, 则根据枚举字段个数生成对应的枚举类
                            if (foc.getTemplatePath().equals(AutoGeneratorCode.TEMPLATE_ENUM) && tableInfo.isHaveEnumField()) {
                                // 只有不存在或允许覆写才生成 (不能将这个判断逻辑放到上一个 if 前, 因为这个是通过 foc.outputFile(tableInfo) 来判断的, 枚举的 foc.outputFile(tableInfo) 只是一个目录路径, 需要在下面的逻辑中拼接枚举名)
                                tableInfo.getFields().forEach(field -> {
                                    if (field.isEnums()) {
                                        final TableField.EnumProperties enumProperties = field.getEnumProperties();
                                        // 只生成自定义枚举
                                        if (enumProperties.getType() == EnumType.CUSTOM) {
                                            final String fieldName = StringUtils.firstCharToUpper(field.getName());
                                            String enumClassName = tableInfo.getEntityName() + fieldName;
                                            Map<String, Object> enumMap = new HashMap<>();
                                            enumMap.put("enumName", enumClassName);
                                            enumMap.put("enumComment", field.getName());
                                            enumMap.put("valueType", enumProperties.getValueType());
                                            enumMap.put("enumItems", enumProperties.getItems());

                                            try {
                                                objectMap.put("enums", enumMap);
                                                writerFile(objectMap, foc.getTemplatePath(), foc.outputFile(tableInfo) + enumClassName + ".java");
                                                objectMap.remove("enums");
                                            } catch (Exception e) {
                                                logger.error("无法创建文件，请检查配置信息！", e);
                                            }
                                        }
                                    }
                                });
                            } else if (foc.getTemplatePath().equals(AutoGeneratorCode.TEMPLATE_ENTITY_JAVA) && tableInfo.isHaveEnumField()) {
                                // 生成需要导入的枚举类
                                tableInfo.getFields().forEach(field -> {
                                    if (field.isEnums()) {
                                        addEnumImport(tableInfo, field, injectionConfig, tableInfo.getImportPackages());
                                    }
                                });
                                writerFile(objectMap, foc.getTemplatePath(), foc.outputFile(tableInfo));
                            } else if (foc.getTemplatePath().equals(AutoGeneratorCode.TEMPLATE_QUERY_JAVA) && tableInfo.isHaveQueryField()) {
                                // 生成需要导入的枚举类
                                tableInfo.getFields().forEach(field -> {
                                    if (field.isQuery() && field.isEnums()) {
                                        addEnumImport(tableInfo, field, injectionConfig, tableInfo.getQueryImportPackages());
                                    }
                                });
                                writerFile(objectMap, foc.getTemplatePath(), foc.outputFile(tableInfo));
                            } else if (isCreate(FileType.OTHER, foc.outputFile(tableInfo))) {
                                // 如果是其他模板就走原有逻辑
                                writerFile(objectMap, foc.getTemplatePath(), foc.outputFile(tableInfo));
                            }
                        }
                    }
                }
                // Mp.java
                String entityName = tableInfo.getEntityName();
                if (null != entityName && null != pathInfo.get(ConstVal.ENTITY_PATH)) {
                    String entityFile = String.format((pathInfo.get(ConstVal.ENTITY_PATH) + File.separator + "%s" + suffixJavaOrKt()), entityName);
                    if (isCreate(FileType.ENTITY, entityFile)) {
                        logger.info("生成实体");
                        writerFile(objectMap, templateFilePath(template.getEntity(getConfigBuilder().getGlobalConfig().isKotlin())), entityFile);
                    }
                }
                // MpMapper.java
                if (null != tableInfo.getMapperName() && null != pathInfo.get(ConstVal.MAPPER_PATH)) {
                    String mapperFile = String.format((pathInfo.get(ConstVal.MAPPER_PATH) + File.separator + tableInfo.getMapperName() + suffixJavaOrKt()), entityName);
                    if (isCreate(FileType.MAPPER, mapperFile)) {
                        writerFile(objectMap, templateFilePath(template.getMapper()), mapperFile);
                    }
                }
                // MpMapper.xml
                if (null != tableInfo.getXmlName() && null != pathInfo.get(ConstVal.XML_PATH)) {
                    String xmlFile = String.format((pathInfo.get(ConstVal.XML_PATH) + File.separator + tableInfo.getXmlName() + ConstVal.XML_SUFFIX), entityName);
                    if (isCreate(FileType.XML, xmlFile)) {
                        writerFile(objectMap, templateFilePath(template.getXml()), xmlFile);
                    }
                }
                // IMpService.java
                if (null != tableInfo.getServiceName() && null != pathInfo.get(ConstVal.SERVICE_PATH)) {
                    String serviceFile = String.format((pathInfo.get(ConstVal.SERVICE_PATH) + File.separator + tableInfo.getServiceName() + suffixJavaOrKt()), entityName);
                    if (isCreate(FileType.SERVICE, serviceFile)) {
                        writerFile(objectMap, templateFilePath(template.getService()), serviceFile);
                    }
                }
                // MpServiceImpl.java
                if (null != tableInfo.getServiceImplName() && null != pathInfo.get(ConstVal.SERVICE_IMPL_PATH)) {
                    String implFile = String.format((pathInfo.get(ConstVal.SERVICE_IMPL_PATH) + File.separator + tableInfo.getServiceImplName() + suffixJavaOrKt()), entityName);
                    if (isCreate(FileType.SERVICE_IMPL, implFile)) {
                        writerFile(objectMap, templateFilePath(template.getServiceImpl()), implFile);
                    }
                }
                // MpController.java
                if (null != tableInfo.getControllerName() && null != pathInfo.get(ConstVal.CONTROLLER_PATH)) {
                    String controllerFile = String.format((pathInfo.get(ConstVal.CONTROLLER_PATH) + File.separator + tableInfo.getControllerName() + suffixJavaOrKt()), entityName);
                    if (isCreate(FileType.CONTROLLER, controllerFile)) {
                        writerFile(objectMap, templateFilePath(template.getController()), controllerFile);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("无法创建文件，请检查配置信息！", e);
        }
        return this;
    }

    /**
     * Add enum import
     *
     * @param tableInfo       table info
     * @param field           field
     * @param injectionConfig injection config
     * @since 2024.2.0
     */
    private static void addEnumImport(TableInfo tableInfo,
                                      TableField field,
                                      InjectionConfig injectionConfig,
                                      Set<String> importPackages) {
        final TableField.EnumProperties enumProperties = field.getEnumProperties();
        if (enumProperties.getType() == EnumType.COMMON) {
            // 公共枚举需要将框架层的 package import 到实体类中
            importPackages.add(EnumType.CACHE.get(enumProperties.getName()));
        } else if (enumProperties.getType() == EnumType.CUSTOM) {
            final String fieldName = StringUtils.firstCharToUpper(field.getName());
            String enumClassName = tableInfo.getEntityName() + fieldName;
            enumProperties.setName(enumClassName);
            // 自定义枚举需要将当前的枚举 package import 到实体类中
            importPackages.add(injectionConfig.getMap().get("package_enums") + "." + enumClassName);
        }
    }

    /**
     * Writer file
     *
     * @param objectMap    object map
     * @param templatePath template path
     * @param outputFile   output file
     * @throws Exception exception
     * @since 2024.2.0
     */
    protected void writerFile(Map<String, Object> objectMap, String templatePath, String outputFile) throws Exception {
        if (StringUtils.isNotBlank(templatePath)) this.writer(objectMap, templatePath, outputFile);
    }

    /**
     * 将模板转化成为文件
     *
     * @param objectMap    渲染对象 MAP 信息
     * @param templatePath 模板文件
     * @param outputFile   文件生成的目录
     * @throws Exception exception
     * @since 2024.2.0
     */
    public abstract void writer(Map<String, Object> objectMap, String templatePath, String outputFile) throws Exception;

    /**
     * 处理输出目录
     *
     * @return the abstract template engine
     * @since 2024.2.0
     */
    public AbstractTemplateEngine mkdirs() {
        getConfigBuilder().getPathInfo().forEach((key, value) -> {
            File dir = new File(value);
            if (!dir.exists()) {
                boolean result = dir.mkdirs();
                if (result) {
                    logger.debug("创建目录： [" + value + "]");
                }
            }
        });
        return this;
    }


    /**
     * 打开输出目录
     *
     * @since 2024.2.0
     */
    public void open() {
        String outDir = getConfigBuilder().getGlobalConfig().getOutputDir();
        if (getConfigBuilder().getGlobalConfig().isOpen()
            && StringUtils.isNotBlank(outDir)) {
            try {
                String osName = System.getProperty("os.name");
                if (osName != null) {
                    if (osName.contains("Mac")) {
                        Runtime.getRuntime().exec("open " + outDir);
                    } else if (osName.contains("Windows")) {
                        Runtime.getRuntime().exec("cmd /c start " + outDir);
                    } else {
                        logger.debug("文件输出目录:" + outDir);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 渲染对象 MAP 信息
     *
     * @param tableInfo 表信息对象
     * @return ignore object map
     * @since 2024.2.0
     */
    public Map<String, Object> getObjectMap(TableInfo tableInfo) {
        Map<String, Object> objectMap = new HashMap<>();
        ConfigBuilder config = getConfigBuilder();
        // 驼峰转 - 分隔
        objectMap.put("controllerMappingHyphenStyle", config.getStrategyConfig().isControllerMappingHyphenStyle());
        String restapi = camelToHyphen(StringUtils.firstCharToLower(tableInfo.getEntityName()));
        if (!restapi.contains("-")) {
            restapi += "s";
        }
        objectMap.put("controllerMappingHyphen", restapi);
        // 处理实体引用名
        tableInfo.setReferenceName(StringUtils.firstCharToLower(tableInfo.getEntityName()));

        objectMap.put("restControllerStyle", config.getStrategyConfig().isRestControllerStyle());
        objectMap.put("config", config);
        objectMap.put("package", config.getPackageInfo());
        GlobalConfig globalConfig = config.getGlobalConfig();
        objectMap.put("author", globalConfig.getAuthor());
        objectMap.put("idType", globalConfig.getIdType() == null ? null : globalConfig.getIdType().toString());
        objectMap.put("logicDeleteFieldName", config.getStrategyConfig().getLogicDeleteFieldName());
        objectMap.put("versionFieldName", config.getStrategyConfig().getVersionFieldName());
        objectMap.put("activeRecord", globalConfig.isActiveRecord());
        objectMap.put("kotlin", globalConfig.isKotlin());
        objectMap.put("swagger2", globalConfig.isSwagger2());
        objectMap.put("date", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        objectMap.put("table", tableInfo);
        objectMap.put("enableCache", globalConfig.isEnableCache());
        objectMap.put("baseResultMap", globalConfig.isBaseResultMap());
        objectMap.put("baseColumnList", globalConfig.isBaseColumnList());
        objectMap.put("entity", tableInfo.getEntityName());
        objectMap.put("entitySerialVersionUID", config.getStrategyConfig().isEntitySerialVersionUID());
        objectMap.put("entityColumnConstant", config.getStrategyConfig().isEntityColumnConstant());
        objectMap.put("entityBuilderModel", config.getStrategyConfig().isChainModel());
        objectMap.put("chainModel", config.getStrategyConfig().isChainModel());
        objectMap.put("entityLombokModel", config.getStrategyConfig().isEntityLombokModel());
        objectMap.put("entityBooleanColumnRemoveIsPrefix", config.getStrategyConfig().isEntityBooleanColumnRemoveIsPrefix());
        objectMap.put("superEntityClass", getSuperClassName(config.getStrategyConfig().getSuperBaseEntityClass()));
        objectMap.put("superMapperClassPackage", config.getStrategyConfig().getSuperMapperClass());
        objectMap.put("superMapperClass", getSuperClassName(config.getStrategyConfig().getSuperMapperClass()));
        objectMap.put("superServiceClassPackage", config.getStrategyConfig().getSuperServiceClass());
        objectMap.put("superServiceClass", getSuperClassName(config.getStrategyConfig().getSuperServiceClass()));
        objectMap.put("superServiceImplClassPackage", config.getStrategyConfig().getSuperServiceImplClass());
        objectMap.put("superServiceImplClass", getSuperClassName(config.getStrategyConfig().getSuperServiceImplClass()));
        objectMap.put("superControllerClassPackage", verifyClassPacket(config.getStrategyConfig().getSuperControllerClass()));
        objectMap.put("superControllerClass", getSuperClassName(config.getStrategyConfig().getSuperControllerClass()));
        return Objects.isNull(config.getInjectionConfig()) ? objectMap : config.getInjectionConfig().prepareObjectMap(objectMap);
    }

    /**
     * 用于渲染对象MAP信息 {@link #getObjectMap(TableInfo)} 时的superClassPacket非空校验
     *
     * @param classPacket ignore
     * @return ignore string
     * @since 2024.2.0
     */
    private String verifyClassPacket(String classPacket) {
        return StringUtils.isBlank(classPacket) ? null : classPacket;
    }

    /**
     * 获取类名
     *
     * @param classPath ignore
     * @return ignore super class name
     * @since 2024.2.0
     */
    private String getSuperClassName(String classPath) {
        if (StringUtils.isBlank(classPath)) {
            return null;
        }
        return classPath.substring(classPath.lastIndexOf(StringPool.DOT) + 1);
    }


    /**
     * 模板真实文件路径
     *
     * @param filePath 文件路径
     * @return ignore string
     * @since 2024.2.0
     */
    public abstract String templateFilePath(String filePath);


    /**
     * 检测文件是否存在
     *
     * @param fileType file type
     * @param filePath file path
     * @return 文件是否存在 boolean
     * @since 2024.2.0
     */
    protected boolean isCreate(FileType fileType, String filePath) {
        ConfigBuilder cb = getConfigBuilder();
        // 自定义判断
        InjectionConfig ic = cb.getInjectionConfig();
        if (null != ic && null != ic.getFileCreate()) {
            return ic.getFileCreate().isCreate(cb, fileType, filePath);
        }
        // 全局判断【默认】
        File file = new File(filePath);
        boolean exist = file.exists();
        if (!exist) {
            file.getParentFile().mkdirs();
        }
        return !exist || getConfigBuilder().getGlobalConfig().isFileOverride();
    }

    /**
     * 文件后缀
     *
     * @return the string
     * @since 2024.2.0
     */
    protected String suffixJavaOrKt() {
        return getConfigBuilder().getGlobalConfig().isKotlin() ? ConstVal.KT_SUFFIX : ConstVal.JAVA_SUFFIX;
    }


    /**
     * Gets config builder *
     *
     * @return the config builder
     * @since 2024.2.0
     */
    public ConfigBuilder getConfigBuilder() {
        return configBuilder;
    }

    /**
     * Sets config builder *
     *
     * @param configBuilder config builder
     * @return the config builder
     * @since 2024.2.0
     */
    public AbstractTemplateEngine setConfigBuilder(ConfigBuilder configBuilder) {
        this.configBuilder = configBuilder;
        return this;
    }

    /**
     * Camel to hyphen
     *
     * @param input input
     * @return the string
     * @since 2024.2.0
     */
    public static String camelToHyphen(String input) {
        return wordsToHyphenCase(wordsAndHyphenAndCamelToConstantCase(input));
    }

    /**
     * Words to hyphen case
     *
     * @param s s
     * @return the string
     * @since 2024.2.0
     */
    private static String wordsToHyphenCase(String s) {
        StringBuilder buf = new StringBuilder();
        char lastChar = 'a';
        for (char c : s.toCharArray()) {
            if ((Character.isWhitespace(lastChar)) && (!Character.isWhitespace(c))
                && ('-' != c) && (StringUtils.isNotBlank(buf))
                && (buf.charAt(buf.length() - 1) != '-')) {
                buf.append(StringPool.DASH);
            }
            if ('_' == c) {
                buf.append('-');
            } else if ('.' == c) {
                buf.append('-');
            } else if (!Character.isWhitespace(c)) {
                buf.append(Character.toLowerCase(c));
            }
            lastChar = c;
        }
        if (Character.isWhitespace(lastChar)) {
            buf.append(StringPool.DASH);
        }
        return buf.toString();
    }

    /**
     * Words and hyphen and camel to constant case
     *
     * @param input input
     * @return the string
     * @since 2024.2.0
     */
    private static String wordsAndHyphenAndCamelToConstantCase(String input) {
        StringBuilder buf = new StringBuilder();
        char previousChar = ' ';
        char[] chars = input.toCharArray();
        for (char c : chars) {
            boolean isUpperCaseAndPreviousIsLowerCase = (Character.isLowerCase(previousChar)) && (Character.isUpperCase(c));

            boolean previousIsWhitespace = Character.isWhitespace(previousChar);
            boolean lastOneIsNotUnderscore = (StringUtils.isNotBlank(buf)) && (buf.charAt(buf.length() - 1) != '_');
            boolean isNotUnderscore = c != '_';
            if (lastOneIsNotUnderscore && (isUpperCaseAndPreviousIsLowerCase || previousIsWhitespace)) {
                buf.append(StringPool.UNDERSCORE);
            } else if ((Character.isDigit(previousChar) && Character.isLetter(c))) {
                buf.append('_');
            }
            if ((shouldReplace(c)) && (lastOneIsNotUnderscore)) {
                buf.append('_');
            } else if (!Character.isWhitespace(c) && (isNotUnderscore || lastOneIsNotUnderscore)) {
                buf.append(Character.toUpperCase(c));
            }
            previousChar = c;
        }
        if (Character.isWhitespace(previousChar)) {
            buf.append(StringPool.UNDERSCORE);
        }
        return buf.toString();
    }

    /**
     * Should replace
     *
     * @param c c
     * @return the boolean
     * @since 2024.2.0
     */
    private static boolean shouldReplace(char c) {
        return (c == '.') || (c == '_') || (c == '-');
    }
}
