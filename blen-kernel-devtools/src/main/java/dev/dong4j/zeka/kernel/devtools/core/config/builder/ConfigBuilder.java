/*
 * Copyright (c) 2011-2020, baomidou (jobob@qq.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package dev.dong4j.zeka.kernel.devtools.core.config.builder;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.Version;
import dev.dong4j.zeka.kernel.common.util.CharPool;
import dev.dong4j.zeka.kernel.common.util.CollectionUtils;
import dev.dong4j.zeka.kernel.common.util.StringPool;
import dev.dong4j.zeka.kernel.devtools.core.InjectionConfig;
import dev.dong4j.zeka.kernel.devtools.core.config.ConstVal;
import dev.dong4j.zeka.kernel.devtools.core.config.DataSourceConfig;
import dev.dong4j.zeka.kernel.devtools.core.config.GlobalConfig;
import dev.dong4j.zeka.kernel.devtools.core.config.IDbQuery;
import dev.dong4j.zeka.kernel.devtools.core.config.IKeyWordsHandler;
import dev.dong4j.zeka.kernel.devtools.core.config.INameConvert;
import dev.dong4j.zeka.kernel.devtools.core.config.PackageConfig;
import dev.dong4j.zeka.kernel.devtools.core.config.StrategyConfig;
import dev.dong4j.zeka.kernel.devtools.core.config.TemplateConfig;
import dev.dong4j.zeka.kernel.devtools.core.config.po.EnumType;
import dev.dong4j.zeka.kernel.devtools.core.config.po.TableField;
import dev.dong4j.zeka.kernel.devtools.core.config.po.TableFill;
import dev.dong4j.zeka.kernel.devtools.core.config.po.TableInfo;
import dev.dong4j.zeka.kernel.devtools.core.config.querys.H2Query;
import dev.dong4j.zeka.kernel.devtools.core.config.rules.NamingStrategy;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * 配置汇总 传递给文件生成工具
 *
 * @author YangHu, tangguo, hubin, Juzi
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2024.04.02 23:58
 * @since 2016 -08-30
 */
@Slf4j
@SuppressWarnings("all")
public class ConfigBuilder {

    /**
     * 模板路径配置信息
     * -- GETTER --
     * 模板路径配置信息
     *
     * @return 所以模板路径配置信息 template
     */
    @Getter
    private final TemplateConfig template;
    /**
     * 数据库配置
     */
    private final DataSourceConfig dataSourceConfig;
    /**
     * SQL连接
     */
    private final Connection connection;
    /**
     * 数据库表信息
     * -- GETTER --
     * 表信息
     *
     * @return 所有表信息 table info list
     */
    @Getter
    private List<TableInfo> tableInfoList;
    /**
     * 包配置详情
     * -- GETTER --
     * 所有包配置信息
     *
     * @return 包配置 package info
     */
    @Getter
    private Map<String, String> packageInfo;
    /**
     * 路径配置信息
     * -- GETTER --
     * 所有路径配置
     *
     * @return 路径配置 path info
     */
    @Getter
    private Map<String, String> pathInfo;
    /**
     * 策略配置
     * -- GETTER --
     * Gets strategy config *
     *
     * @return the strategy config
     */
    @Getter
    private StrategyConfig strategyConfig;
    /**
     * 全局配置信息
     * -- GETTER --
     * Gets global config *
     *
     * @return the global config
     */
    @Getter
    private GlobalConfig globalConfig;
    /**
     * 注入配置信息
     * -- GETTER --
     * Gets injection config *
     *
     * @return the injection config
     */
    @Getter
    private InjectionConfig injectionConfig;

    /** enumCommentPrefix */
    private static final String customEnumCommentPrefix = "自定义枚举:";
    private static final String commonEnumCommentPrefix = "公共枚举:";
    private static final String generalEnumCommentPrefix = "通用枚举:";
    private static final String queryCommentPrefix = "Q@";
    /**
     * 过滤正则
     */
    private static final Pattern REGX = Pattern.compile("[~!/@#$%^&*()+\\\\\\[\\]|{};:'\",<.>?]+");

    /**
     * 在构造器中处理配置
     *
     * @param packageConfig    包配置
     * @param dataSourceConfig 数据源配置
     * @param strategyConfig   表配置
     * @param template         模板配置
     * @param globalConfig     全局配置
     * @since 1.0.0
     */
    public ConfigBuilder(PackageConfig packageConfig,
                         DataSourceConfig dataSourceConfig,
                         StrategyConfig strategyConfig,
                         TemplateConfig template,
                         GlobalConfig globalConfig) {
        // 全局配置
        this.globalConfig = Optional.ofNullable(globalConfig).orElseGet(GlobalConfig::new);
        // 模板配置
        this.template = Optional.ofNullable(template).orElseGet(TemplateConfig::new);
        // 包配置
        if (null == packageConfig) {
            handlerPackage(this.template, this.globalConfig.getOutputDir(), new PackageConfig());
        } else {
            handlerPackage(this.template, this.globalConfig.getOutputDir(), packageConfig);
        }
        this.dataSourceConfig = dataSourceConfig;
        this.connection = dataSourceConfig.getConn();
        // 策略配置
        this.strategyConfig = Optional.ofNullable(strategyConfig).orElseGet(StrategyConfig::new);
        this.tableInfoList = getTablesInfo(this.strategyConfig);
    }

    // ************************ 曝露方法 BEGIN*****************************


    /**
     * Sets table info list *
     *
     * @param tableInfoList table info list
     * @return the table info list
     * @since 1.0.0
     */
    public ConfigBuilder setTableInfoList(List<TableInfo> tableInfoList) {
        this.tableInfoList = tableInfoList;
        return this;
    }

    // ****************************** 曝露方法 END**********************************

    /**
     * 处理包配置
     *
     * @param template  TemplateConfig
     * @param outputDir output dir
     * @param config    PackageConfig
     * @since 1.0.0
     */
    private void handlerPackage(TemplateConfig template, String outputDir, PackageConfig config) {
        // 包信息
        packageInfo = new HashMap<>();
        packageInfo.put(ConstVal.MODULE_NAME, config.getModuleName());
        packageInfo.put(ConstVal.ENTITY, joinPackage(config.getParent(), config.getEntity()));
        packageInfo.put(ConstVal.MAPPER, joinPackage(config.getParent(), config.getMapper()));
        packageInfo.put(ConstVal.XML, joinPackage(config.getParent(), config.getXml()));
        packageInfo.put(ConstVal.SERVICE, joinPackage(config.getParent(), config.getService()));
        packageInfo.put(ConstVal.SERVICE_IMPL, joinPackage(config.getParent(), config.getServiceImpl()));
        packageInfo.put(ConstVal.CONTROLLER, joinPackage(config.getParent(), config.getController()));

        // 自定义路径
        Map<String, String> configPathInfo = config.getPathInfo();
        if (null != configPathInfo) {
            pathInfo = configPathInfo;
        } else {
            // 生成路径信息
            pathInfo = new HashMap<>();
            setPathInfo(pathInfo, template.getEntity(getGlobalConfig().isKotlin()), outputDir, ConstVal.ENTITY_PATH, ConstVal.ENTITY);
            setPathInfo(pathInfo, template.getMapper(), outputDir, ConstVal.MAPPER_PATH, ConstVal.MAPPER);
            setPathInfo(pathInfo, template.getXml(), outputDir, ConstVal.XML_PATH, ConstVal.XML);
            setPathInfo(pathInfo, template.getService(), outputDir, ConstVal.SERVICE_PATH, ConstVal.SERVICE);
            setPathInfo(pathInfo, template.getServiceImpl(), outputDir, ConstVal.SERVICE_IMPL_PATH, ConstVal.SERVICE_IMPL);
            setPathInfo(pathInfo, template.getController(), outputDir, ConstVal.CONTROLLER_PATH, ConstVal.CONTROLLER);
        }
    }

    /**
     * Sets path info *
     *
     * @param pathInfo  path info
     * @param template  template
     * @param outputDir output dir
     * @param path      path
     * @param module    module
     * @since 1.0.0
     */
    private void setPathInfo(Map<String, String> pathInfo, String template, String outputDir, String path, String module) {
        if (StringUtils.isNotBlank(template)) {
            pathInfo.put(path, joinPath(outputDir, packageInfo.get(module)));
        }
    }

    /**
     * 处理表对应的类名称
     *
     * @param tableList 表名称
     * @param config    策略配置项
     * @return 补充完整信息后的表 list
     * @since 1.0.0
     */
    private List<TableInfo> processTable(List<TableInfo> tableList, StrategyConfig config) {
        for (TableInfo tableInfo : tableList) {
            String entityName;
            INameConvert nameConvert = strategyConfig.getNameConvert();
            if (null != nameConvert) {
                // 自定义处理实体名称
                entityName = nameConvert.entityNameConvert(tableInfo);
            } else {
                entityName = NamingStrategy.capitalFirst(processName(tableInfo.getName(), config.getNaming(), config.getTablePrefix()));
            }
            if (StringUtils.isNotBlank(globalConfig.getEntityName())) {
                tableInfo.setConvert(true);
                tableInfo.setEntityName(String.format(globalConfig.getEntityName(), entityName));
            } else {
                tableInfo.setEntityName(strategyConfig, entityName);
            }
            if (StringUtils.isNotBlank(globalConfig.getMapperName())) {
                tableInfo.setMapperName(String.format(globalConfig.getMapperName(), entityName));
            } else {
                tableInfo.setMapperName(entityName + ConstVal.MAPPER);
            }
            if (StringUtils.isNotBlank(globalConfig.getXmlName())) {
                tableInfo.setXmlName(String.format(globalConfig.getXmlName(), entityName));
            } else {
                tableInfo.setXmlName(entityName + ConstVal.MAPPER);
            }
            if (StringUtils.isNotBlank(globalConfig.getServiceName())) {
                tableInfo.setServiceName(String.format(globalConfig.getServiceName(), entityName));
            } else {
                tableInfo.setServiceName("I" + entityName + ConstVal.SERVICE);
            }
            if (StringUtils.isNotBlank(globalConfig.getServiceImplName())) {
                tableInfo.setServiceImplName(String.format(globalConfig.getServiceImplName(), entityName));
            } else {
                tableInfo.setServiceImplName(entityName + ConstVal.SERVICE_IMPL);
            }
            if (StringUtils.isNotBlank(globalConfig.getControllerName())) {
                tableInfo.setControllerName(String.format(globalConfig.getControllerName(), entityName));
            } else {
                tableInfo.setControllerName(entityName + ConstVal.CONTROLLER);
            }
            // 检测导入包
            checkImportPackages(tableInfo);
        }
        return tableList;
    }

    /**
     * 检测导入包
     *
     * @param tableInfo ignore
     * @since 1.0.0
     */
    private void checkImportPackages(TableInfo tableInfo) {
        if (StringUtils.isNotBlank(strategyConfig.getSuperBaseEntityClass())) {
            // 自定义父类
            tableInfo.getImportPackages().add(strategyConfig.getSuperBaseEntityClass());
        } else if (globalConfig.isActiveRecord()) {
            // 无父类开启 AR 模式
            tableInfo.getImportPackages().add("com.baomidou.mybatisplus.extension.activerecord.Model");
        }
        if (null != globalConfig.getIdType() && tableInfo.isHavePrimaryKey()) {
            // 指定需要 IdType 场景
            tableInfo.getImportPackages().add(IdType.class.getCanonicalName());
            tableInfo.getImportPackages().add(TableId.class.getCanonicalName());
        }
        if (StringUtils.isNotBlank(strategyConfig.getVersionFieldName())
            && CollectionUtils.isNotEmpty(tableInfo.getFields())) {
            tableInfo.getFields().forEach(f -> {
                if (strategyConfig.getVersionFieldName().equals(f.getName())) {
                    tableInfo.getImportPackages().add(Version.class.getCanonicalName());
                }
            });
        }
    }


    /**
     * 获取所有的数据库表信息
     *
     * @param config config
     * @return the tables info
     * @since 1.0.0
     */
    public List<TableInfo> getTablesInfo(StrategyConfig config) {
        boolean isInclude = !config.getInclude().isEmpty();
        boolean isExclude = !config.getExclude().isEmpty();
        if (isInclude && isExclude) {
            throw new RuntimeException("<strategy> 标签中 <include> 与 <exclude> 只能配置一项！");
        }
        if (config.getNotLikeTable() != null && config.getLikeTable() != null) {
            throw new RuntimeException("<strategy> 标签中 <likeTable> 与 <notLikeTable> 只能配置一项！");
        }
        // 所有的表信息
        List<TableInfo> tableList = new ArrayList<>();

        // 需要反向生成或排除的表信息
        List<TableInfo> includeTableList = new ArrayList<>();
        List<TableInfo> excludeTableList = new ArrayList<>();

        // 不存在的表名
        Set<String> notExistTables = new HashSet<>();
        DbType dbType = this.dataSourceConfig.getDbType();
        IDbQuery dbQuery = this.dataSourceConfig.getDbQuery();
        try {
            String tablesSql = dataSourceConfig.getDbQuery().tablesSql();
            if (DbType.POSTGRE_SQL == dbType) {
                String schema = dataSourceConfig.getSchemaName();
                if (schema == null) {
                    // pg 默认 schema=public
                    schema = "public";
                    dataSourceConfig.setSchemaName(schema);
                }
                // TODO 还原代码后解决PgSchema的问题.
                this.connection.setSchema(schema);
                tablesSql = String.format(tablesSql, schema);
            } else if (DbType.KINGBASE_ES == dbType) {
                String schema = dataSourceConfig.getSchemaName();
                if (schema == null) {
                    // kingbase 默认 schema=PUBLIC
                    schema = "PUBLIC";
                    dataSourceConfig.setSchemaName(schema);
                }
                tablesSql = String.format(tablesSql, schema);
            } else if (DbType.DB2 == dbType) {
                String schema = dataSourceConfig.getSchemaName();
                if (schema == null) {
                    // db2 默认 schema=current schema
                    schema = "current schema";
                    dataSourceConfig.setSchemaName(schema);
                }
                tablesSql = String.format(tablesSql, schema);
            }
            // oracle数据库表太多，出现最大游标错误
            else if (DbType.ORACLE == dbType) {
                String schema = dataSourceConfig.getSchemaName();
                // oracle 默认 schema=username
                if (schema == null) {
                    schema = dataSourceConfig.getUsername().toUpperCase();
                    dataSourceConfig.setSchemaName(schema);
                }
                tablesSql = String.format(tablesSql, schema);
            }
            StringBuilder sql = new StringBuilder(tablesSql);
            if (config.isEnableSqlFilter()) {
                if (config.getLikeTable() != null) {
                    sql.append(" AND ").append(dbQuery.tableName()).append(" LIKE '").append(config.getLikeTable().getValue()).append("'");
                } else if (config.getNotLikeTable() != null) {
                    sql.append(" AND ").append(dbQuery.tableName()).append(" NOT LIKE '").append(config.getNotLikeTable().getValue()).append("'");
                }
                if (isInclude) {
                    sql.append(" AND ").append(dbQuery.tableName()).append(" IN (")
                        .append(config.getInclude().stream().map(tb -> "'" + tb + "'").collect(Collectors.joining(","))).append(")");
                } else if (isExclude) {
                    sql.append(" AND ").append(dbQuery.tableName()).append(" NOT IN (")
                        .append(config.getExclude().stream().map(tb -> "'" + tb + "'").collect(Collectors.joining(","))).append(")");
                }
            }
            TableInfo tableInfo;
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql.toString());
                 ResultSet results = preparedStatement.executeQuery()) {
                while (results.next()) {
                    final String tableName = results.getString(dbQuery.tableName());
                    if (StringUtils.isBlank(tableName)) {
                        System.err.println("当前数据库为空！！！");
                        continue;
                    }
                    tableInfo = new TableInfo();
                    tableInfo.setName(tableName);
                    String commentColumn = dbQuery.tableComment();
                    if (StringUtils.isNotBlank(commentColumn)) {
                        String tableComment = results.getString(commentColumn);
                        if (config.isSkipView() && "VIEW".equals(tableComment)) {
                            // 跳过视图
                            continue;
                        }
                        tableInfo.setComment(formatComment(tableComment));
                    }

                    if (isInclude) {
                        for (String includeTable : config.getInclude()) {
                            // 忽略大小写等于 或 正则 true
                            if (tableNameMatches(includeTable, tableName)) {
                                includeTableList.add(tableInfo);
                            } else {
                                // 过滤正则表名
                                if (!REGX.matcher(includeTable).find()) {
                                    notExistTables.add(includeTable);
                                }
                            }
                        }
                    } else if (isExclude) {
                        for (String excludeTable : config.getExclude()) {
                            // 忽略大小写等于 或 正则 true
                            if (tableNameMatches(excludeTable, tableName)) {
                                excludeTableList.add(tableInfo);
                            } else {
                                // 过滤正则表名
                                if (!REGX.matcher(excludeTable).find()) {
                                    notExistTables.add(excludeTable);
                                }
                            }
                        }
                    }
                    tableList.add(tableInfo);
                }
            }
            // 将已经存在的表移除，获取配置中数据库不存在的表
            for (TableInfo tabInfo : tableList) {
                notExistTables.remove(tabInfo.getName());
            }
            if (!notExistTables.isEmpty()) {
                System.err.println("表 " + notExistTables + " 在数据库中不存在！！！");
            }

            // 需要反向生成的表信息
            if (isExclude) {
                tableList.removeAll(excludeTableList);
                includeTableList = tableList;
            }
            if (!isInclude && !isExclude) {
                includeTableList = tableList;
            }
            // 性能优化，只处理需执行表字段 github issues/219
            includeTableList.forEach(ti -> convertTableFields(ti, config));
        } catch (SQLException e) {
            log.error("", e);
        }
        return processTable(includeTableList, config);
    }


    /**
     * 表名匹配
     *
     * @param setTableName 设置表名
     * @param dbTableName  数据库表单
     * @return ignore boolean
     * @since 1.0.0
     */
    private boolean tableNameMatches(String setTableName, String dbTableName) {
        return setTableName.equalsIgnoreCase(dbTableName)
            || matches(setTableName, dbTableName);
    }

    /**
     * 将字段信息与表信息关联
     *
     * @param tableInfo 表信息
     * @param config    命名策略
     * @since 1.0.0
     */
    private void convertTableFields(TableInfo tableInfo, StrategyConfig config) {
        boolean haveId = false;
        List<TableField> fieldList = new ArrayList<>();
        List<TableField> commonFieldList = new ArrayList<>();
        DbType dbType = this.dataSourceConfig.getDbType();
        IDbQuery dbQuery = dataSourceConfig.getDbQuery();
        String tableName = tableInfo.getName();
        try {
            String tableFieldsSql = dbQuery.tableFieldsSql();
            Set<String> h2PkColumns = new HashSet<>();
            if (DbType.POSTGRE_SQL == dbType) {
                tableFieldsSql = String.format(tableFieldsSql, tableName);
            } else if (DbType.KINGBASE_ES == dbType) {
                tableFieldsSql = String.format(tableFieldsSql, dataSourceConfig.getSchemaName(), tableName);
            } else if (DbType.DB2 == dbType) {
                tableFieldsSql = String.format(tableFieldsSql, dataSourceConfig.getSchemaName(), tableName);
            } else if (DbType.ORACLE == dbType) {
                tableName = tableName.toUpperCase();
                tableFieldsSql = String.format(tableFieldsSql.replace("#schema", dataSourceConfig.getSchemaName()), tableName);
            } else if (DbType.DM == dbType) {
                tableName = tableName.toUpperCase();
                tableFieldsSql = String.format(tableFieldsSql, tableName);
            } else if (DbType.H2 == dbType) {
                try (PreparedStatement pkQueryStmt = connection.prepareStatement(String.format(H2Query.PK_QUERY_SQL, tableName));
                     ResultSet pkResults = pkQueryStmt.executeQuery()) {
                    while (pkResults.next()) {
                        String primaryKey = pkResults.getString(dbQuery.fieldKey());
                        if (Boolean.parseBoolean(primaryKey)) {
                            h2PkColumns.add(pkResults.getString(dbQuery.fieldName()));
                        }
                    }
                }
                tableFieldsSql = String.format(tableFieldsSql, tableName);
            } else {
                tableFieldsSql = String.format(tableFieldsSql, tableName);
            }
            try (
                PreparedStatement preparedStatement = connection.prepareStatement(tableFieldsSql);
                ResultSet results = preparedStatement.executeQuery()) {
                while (results.next()) {
                    TableField field = new TableField();
                    String columnName = results.getString(dbQuery.fieldName());
                    // 避免多重主键设置，目前只取第一个找到ID，并放到list中的索引为0的位置
                    boolean isId;
                    if (DbType.H2 == dbType) {
                        isId = h2PkColumns.contains(columnName);
                    } else {
                        String key = results.getString(dbQuery.fieldKey());
                        if (DbType.DB2 == dbType || DbType.SQLITE == dbType) {
                            isId = StringUtils.isNotBlank(key) && "1".equals(key);
                        } else {
                            isId = StringUtils.isNotBlank(key) && "PRI".equalsIgnoreCase(key);
                        }
                    }

                    // 处理ID
                    if (isId && !haveId) {
                        haveId = true;
                        field.setKeyFlag(true);
                        tableInfo.setHavePrimaryKey(true);
                        field.setKeyIdentityFlag(dbQuery.isKeyIdentity(results));
                    } else {
                        field.setKeyFlag(false);
                    }
                    // 自定义字段查询
                    String[] fcs = dbQuery.fieldCustom();
                    if (null != fcs) {
                        Map<String, Object> customMap = new HashMap<>();
                        for (String fc : fcs) {
                            customMap.put(fc, results.getObject(fc));
                        }
                        field.setCustomMap(customMap);
                    }
                    // 处理其它信息
                    field.setName(columnName);
                    String newColumnName = columnName;
                    IKeyWordsHandler keyWordsHandler = dataSourceConfig.getKeyWordsHandler();
                    if (keyWordsHandler != null && keyWordsHandler.isKeyWords(columnName)) {
                        System.err.printf("当前表[%s]存在字段[%s]为数据库关键字或保留字!%n", tableName, columnName);
                        field.setKeyWords(true);
                        newColumnName = keyWordsHandler.formatColumn(columnName);
                    }
                    field.setColumnName(newColumnName);
                    field.setType(results.getString(dbQuery.fieldType()));
                    INameConvert nameConvert = strategyConfig.getNameConvert();
                    if (null != nameConvert) {
                        field.setPropertyName(nameConvert.propertyNameConvert(field));
                    } else {
                        field.setPropertyName(strategyConfig, processName(field.getName(), config.getColumnNaming()));
                    }
                    field.setColumnType(dataSourceConfig.getTypeConvert().processTypeConvert(globalConfig, field));
                    String fieldCommentColumn = dbQuery.fieldComment();
                    if (StringUtils.isNotBlank(fieldCommentColumn)) {
                        String commentString = results.getString(fieldCommentColumn);
                        field.setComment(commentString);

                        processQueryField(tableInfo, commentString, field);
                        // 为保持原逻辑, 这里重设一次注释(删除 Q@ 前缀)
                        commentString = field.getComment();

                        try {
                            if (commentString.startsWith(customEnumCommentPrefix)) {
                                log.info("存在自定义枚举字段: {}", field);
                                field.setEnums(true);
                                // 自定义枚举:Integer:用户状态:AAA(0, "未审核"),:BBB(1, "审核中"),:CCC(2, "审核未通过"),:DDD(3,"已锁定"),:EEE(4,"正常");
                                final String[] split = commentString.split(":");
                                TableField.EnumProperties properties = new TableField.EnumProperties();
                                properties.setType(EnumType.CUSTOM);
                                properties.setValueType(split[1]);
                                field.setComment(split[2]);
                                // 遍历枚举值
                                List<String> items = new ArrayList<>(Arrays.asList(split).subList(3, split.length));
                                properties.setItems(items);
                                field.setEnumProperties(properties);
                                tableInfo.setHaveEnumField(true);

                            } else if (commentString.startsWith(commonEnumCommentPrefix)) {
                                log.info("存在公共枚举字段: {}", field);
                                field.setEnums(true);
                                // 公共枚举:EnabledEnum:可用状态
                                final String[] split = commentString.split(":");
                                TableField.EnumProperties properties = new TableField.EnumProperties();
                                properties.setType(EnumType.COMMON);
                                field.setComment(split[2]);
                                properties.setName(split[1]);
                                field.setEnumProperties(properties);
                                tableInfo.setHaveEnumField(true);
                            }
                        } catch (Exception e) {
                            log.error("SQL 枚举注释格式不正确: {}", field);
                        }
                    }

                    // 判断字段是否为 String 类型
                    field.setStringType(StringUtils.containsIgnoreCase(field.getType(), "varchar"));

                    // 填充逻辑判断
                    List<TableFill> tableFillList = getStrategyConfig().getTableFillList();
                    if (null != tableFillList) {
                        // 忽略大写字段问题
                        tableFillList.stream().filter(tf -> tf.getFieldName().equalsIgnoreCase(field.getName()))
                            .findFirst().ifPresent(tf -> field.setFill(tf.getFieldFill().name()));
                    }
                    if (strategyConfig.includeSuperEntityColumns(field.getName())) {
                        // 跳过公共字段
                        commonFieldList.add(field);
                        continue;
                    }
                    fieldList.add(field);
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL Exception：" + e.getMessage());
        }
        tableInfo.setFields(fieldList);
        tableInfo.setCommonFields(commonFieldList);
    }

    /**
     * 处理查询字段
     *
     * @param commentString comment string
     * @param field         field
     * @param queryFields   query fields
     * @since 1.0.0
     */
    private static void processQueryField(TableInfo tableInfo, String commentString, TableField field) {
        // 查询参数字段处理
        if (commentString.contains(queryCommentPrefix)) {
            log.info("存在查询参数字段: {}", field);
            field.setQuery(true);
            // Q@xxx
            field.setComment(field.getComment().replace(queryCommentPrefix, ""));
            tableInfo.setHaveQueryField(true);
        }
    }


    /**
     * 连接路径字符串
     *
     * @param parentDir   路径常量字符串
     * @param packageName 包名
     * @return 连接后的路径 string
     * @since 1.0.0
     */
    private String joinPath(String parentDir, String packageName) {
        if (StringUtils.isBlank(parentDir)) {
            parentDir = System.getProperty(ConstVal.JAVA_TMPDIR);
        }
        if (!StringUtils.endsWith(parentDir, File.separator)) {
            parentDir += File.separator;
        }
        packageName = packageName.replaceAll("\\.", CharPool.BACK_SLASH + File.separator);
        return parentDir + packageName;
    }


    /**
     * 连接父子包名
     *
     * @param parent     父包名
     * @param subPackage 子包名
     * @return 连接后的包名 string
     * @since 1.0.0
     */
    private String joinPackage(String parent, String subPackage) {
        return StringUtils.isBlank(parent) ? subPackage : (parent + StringPool.DOT + subPackage);
    }


    /**
     * 处理字段名称
     *
     * @param name     name
     * @param strategy strategy
     * @return 根据策略返回处理后的名称 string
     * @since 1.0.0
     */
    private String processName(String name, NamingStrategy strategy) {
        return processName(name, strategy, strategyConfig.getFieldPrefix());
    }


    /**
     * 处理表/字段名称
     *
     * @param name     ignore
     * @param strategy ignore
     * @param prefix   ignore
     * @return 根据策略返回处理后的名称 string
     * @since 1.0.0
     */
    private String processName(String name, NamingStrategy strategy, Set<String> prefix) {
        String propertyName;
        if (!prefix.isEmpty()) {
            if (strategy == NamingStrategy.underline_to_camel) {
                // 删除前缀、下划线转驼峰
                propertyName = NamingStrategy.removePrefixAndCamel(name, prefix);
            } else {
                // 删除前缀
                propertyName = NamingStrategy.removePrefix(name, prefix);
            }
        } else if (strategy == NamingStrategy.underline_to_camel) {
            // 下划线转驼峰
            propertyName = NamingStrategy.underlineToCamel(name);
        } else {
            // 不处理
            propertyName = name;
        }
        return propertyName;
    }


    /**
     * Sets strategy config *
     *
     * @param strategyConfig strategy config
     * @return the strategy config
     * @since 1.0.0
     */
    public ConfigBuilder setStrategyConfig(StrategyConfig strategyConfig) {
        this.strategyConfig = strategyConfig;
        return this;
    }

    /**
     * Sets global config *
     *
     * @param globalConfig global config
     * @return the global config
     * @since 1.0.0
     */
    public ConfigBuilder setGlobalConfig(GlobalConfig globalConfig) {
        this.globalConfig = globalConfig;
        return this;
    }


    /**
     * Sets injection config *
     *
     * @param injectionConfig injection config
     * @return the injection config
     * @since 1.0.0
     */
    public ConfigBuilder setInjectionConfig(InjectionConfig injectionConfig) {
        this.injectionConfig = injectionConfig;
        return this;
    }

    /**
     * 格式化数据库注释内容
     *
     * @param comment 注释
     * @return 注释 string
     * @since 1.0.0
     */
    public String formatComment(String comment) {
        return StringUtils.isBlank(comment) ? StringPool.EMPTY : comment.replaceAll("\r\n", "\t");
    }

    /**
     * Matches
     *
     * @param regex regex
     * @param input input
     * @return the boolean
     * @since 1.0.0
     */
    private static boolean matches(String regex, String input) {
        if (null == regex || null == input) {
            return false;
        }
        return Pattern.matches(regex, input);
    }

}
