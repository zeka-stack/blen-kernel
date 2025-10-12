package dev.dong4j.zeka.kernel.devtools.core;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import dev.dong4j.zeka.kernel.common.util.CollectionUtils;
import dev.dong4j.zeka.kernel.common.util.StringUtils;
import dev.dong4j.zeka.kernel.devtools.core.config.DataSourceConfig;
import dev.dong4j.zeka.kernel.devtools.core.config.GlobalConfig;
import dev.dong4j.zeka.kernel.devtools.core.config.PackageConfig;
import dev.dong4j.zeka.kernel.devtools.core.config.StrategyConfig;
import dev.dong4j.zeka.kernel.devtools.core.config.TemplateConfig;
import dev.dong4j.zeka.kernel.devtools.core.config.builder.ConfigBuilder;
import dev.dong4j.zeka.kernel.devtools.core.config.po.TableField;
import dev.dong4j.zeka.kernel.devtools.core.config.po.TableInfo;
import dev.dong4j.zeka.kernel.devtools.core.engine.AbstractTemplateEngine;
import dev.dong4j.zeka.kernel.devtools.core.engine.VelocityTemplateEngine;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MyBatis-Plus代码自动生成器核心类，负责执行代码生成的主流程
 * <p>
 * 该类是代码生成器的核心入口，整合了数据源配置、策略配置、模板配置等
 * 支持多种模板引擎（Velocity、Freemarker等）和灵活的配置策略
 * <p>
 * 主要功能：
 * - 数据库表结构分析和代码生成
 * - 支持ActiveRecord模式和父类继承
 * - 支持逻辑删除和乐观锁注解
 * - 自动处理Boolean类型字段的is前缀
 * - 支持自定义模板和变量注入
 *
 * @author YangHu, tangguo, hubin
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2024.04.02 23:58
 * @since 2016-08-30
 */
@Data
@Accessors(chain = true)
@SuppressWarnings("all")
public class AutoGenerator {
    /** 日志记录器 */
    private static final Logger logger = LoggerFactory.getLogger(AutoGenerator.class);

    /** 配置信息构建器 */
    protected ConfigBuilder config;
    /** 注入配置，用于自定义变量和文件输出 */
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    protected InjectionConfig injectionConfig;
    /** 数据源配置信息 */
    private DataSourceConfig dataSource;
    /** 数据库表相关的策略配置 */
    private StrategyConfig strategy;
    /** 包结构相关配置 */
    private PackageConfig packageInfo;
    /** 模板相关配置 */
    private TemplateConfig template;
    /** 全局相关配置 */
    private GlobalConfig globalConfig;
    /** 模板引擎实例 */
    private AbstractTemplateEngine templateEngine;

    /**
     * 执行代码生成
     *
     * 初始化配置并执行文件生成流程
     * 默认使用Velocity模板引擎，支持批量生成和自动打开目录
     *
     * @since 1.0.0
     */
    public void execute() {
        logger.debug("==========================准备生成文件...==========================");
        // 初始化配置
        if (null == config) {
            config = new ConfigBuilder(packageInfo, dataSource, strategy, template, globalConfig);
            if (null != injectionConfig) {
                injectionConfig.setConfig(config);
            }
        }
        if (null == templateEngine) {
            // 为了兼容之前逻辑，采用 Velocity 引擎 【 默认 】
            templateEngine = new VelocityTemplateEngine();
        }
        // 模板引擎初始化执行文件输出
        templateEngine.init(this.pretreatmentConfigBuilder(config)).mkdirs().batchOutput().open();
        logger.debug("==========================文件生成完成！！！==========================");
    }

    /**
     * 获取所有表信息列表
     *
     * 开放给子类重写，允许自定义表信息处理逻辑
     * 默认返回配置中的所有表信息
     *
     * @param config 配置信息构建器
     * @return 表信息列表
     * @since 1.0.0
     */
    protected List<TableInfo> getAllTableInfoList(ConfigBuilder config) {
        return config.getTableInfoList();
    }

    /**
     * 预处理配置信息构建器
     *
     * 对配置进行预处理，包括注入自定义配置、处理表信息列表
     * 自动添加必要的导入包和注解，处理父类继承关系
     *
     * @param config 总配置信息
     * @return 解析数据结果集
     * @since 1.0.0
     */
    protected ConfigBuilder pretreatmentConfigBuilder(ConfigBuilder config) {
        /*
         * 注入自定义配置
         */
        if (null != injectionConfig) {
            injectionConfig.initMap();
            config.setInjectionConfig(injectionConfig);
        }
        /*
         * 表信息列表
         */
        List<TableInfo> tableList = this.getAllTableInfoList(config);
        for (TableInfo tableInfo : tableList) {
            /* ---------- 添加导入包 ---------- */
            if (config.getGlobalConfig().isActiveRecord()) {
                // 开启 ActiveRecord 模式 (已经在父类中导入)
                // tableInfo.setImportPackages("com.baomidou.mybatisplus.extension.activerecord.Model");
            }
            if (tableInfo.isConvert()) {
                // 表注解
                tableInfo.setImportPackages(TableName.class.getCanonicalName());
            }
            if (config.getStrategyConfig().getLogicDeleteFieldName() != null && tableInfo.isLogicDelete(config.getStrategyConfig().getLogicDeleteFieldName())) {
                // 逻辑删除注解
                tableInfo.setImportPackages(TableLogic.class.getCanonicalName());
            }
            if (StringUtils.isNotBlank(config.getStrategyConfig().getVersionFieldName())) {
                // 乐观锁注解
                tableInfo.setImportPackages(Version.class.getCanonicalName());
            }
            boolean importSerializable = true;
            if (StringUtils.isNotBlank(config.getStrategyConfig().getSuperBaseEntityClass())) {
                final String superBaseEntityClass = config.getStrategyConfig().getSuperBaseEntityClass();
                // 移除原来的 entity 父类
                tableInfo.getImportPackages().remove(superBaseEntityClass);

                final Map<String, TableField> collect = tableInfo.getCommonFields().stream().collect(Collectors.toMap(TableField::getColumnName, field -> field));
                final boolean withLogicPo = collect.containsKey("deleted");
                if (withLogicPo) {
                    config.getStrategyConfig().setSuperBaseEntityClass(config.getStrategyConfig().getSuperWithLogicEntityClass());
                }
                final boolean withTimePo = collect.containsKey("create_time") && collect.containsKey("update_time");
                if (withTimePo) {
                    config.getStrategyConfig().setSuperBaseEntityClass(config.getStrategyConfig().getSuperWithTimeEntityClass());
                }
                final boolean extendPo = collect.containsKey("deleted") && collect.containsKey("create_time") && collect.containsKey("update_time");
                if (extendPo) {
                    config.getStrategyConfig().setSuperBaseEntityClass(config.getStrategyConfig().getSuperExtendEntityClass());
                }
                tableInfo.setImportPackages(config.getStrategyConfig().getSuperBaseEntityClass());
                importSerializable = false;
            }
            if (config.getGlobalConfig().isActiveRecord()) {
                importSerializable = true;
            }
            if (importSerializable) {
                tableInfo.setImportPackages(Serializable.class.getCanonicalName());
            }
            // Boolean类型is前缀处理
            if (config.getStrategyConfig().isEntityBooleanColumnRemoveIsPrefix()
                && CollectionUtils.isNotEmpty(tableInfo.getFields())) {
                List<TableField> tableFields = tableInfo.getFields()
                    .stream()
                    .filter(field -> "boolean".equalsIgnoreCase(field.getPropertyType()))
                    .filter(field -> field.getPropertyName().startsWith("is")).collect(Collectors.toList());

                tableFields.forEach(field -> {
                    // 主键为is的情况基本上是不存在的.
                    if (field.isKeyFlag()) {
                        tableInfo.setImportPackages(TableId.class.getCanonicalName());
                    } else {
                        tableInfo.setImportPackages(com.baomidou.mybatisplus.annotation.TableField.class.getCanonicalName());
                    }
                    field.setConvert(true);
                    field.setPropertyName(removePrefixAfterPrefixToLower(field.getPropertyName(), 2));
                });
            }
        }
        return config.setTableInfoList(tableList);
    }

    /**
     * Gets cfg *
     *
     * @return the cfg
     * @since 1.0.0
     */
    public InjectionConfig getCfg() {
        return injectionConfig;
    }

    /**
     * Sets cfg *
     *
     * @param injectionConfig injection config
     * @return the cfg
     * @since 1.0.0
     */
    public AutoGenerator setCfg(InjectionConfig injectionConfig) {
        this.injectionConfig = injectionConfig;
        return this;
    }

    /**
     * 删除字符前缀之后,首字母小写,之后字符大小写的不变
     * <p>StringUtils.removePrefixAfterPrefixToLower( "isUser", 2 )     = user</p>
     * <p>StringUtils.removePrefixAfterPrefixToLower( "isUserInfo", 2 ) = userInfo</p>
     *
     * @param rawString 需要处理的字符串
     * @param index     删除多少个字符(从左至右)
     * @return ignore string
     * @since 1.0.0
     */
    public static String removePrefixAfterPrefixToLower(String rawString, int index) {
        return prefixToLower(rawString.substring(index), 1);
    }

    /**
     * 前n个首字母小写,之后字符大小写的不变
     *
     * @param rawString 需要处理的字符串
     * @param index     多少个字符(从左至右)
     * @return ignore string
     * @since 1.0.0
     */
    public static String prefixToLower(String rawString, int index) {
        return rawString.substring(0, index).toLowerCase() +
            rawString.substring(index);
    }
}
