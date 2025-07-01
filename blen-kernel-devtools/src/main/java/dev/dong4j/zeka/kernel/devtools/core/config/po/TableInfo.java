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
package dev.dong4j.zeka.kernel.devtools.core.config.po;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import dev.dong4j.zeka.kernel.common.util.CollectionUtils;
import dev.dong4j.zeka.kernel.common.util.StringUtils;
import dev.dong4j.zeka.kernel.devtools.core.config.StrategyConfig;
import dev.dong4j.zeka.kernel.devtools.core.config.rules.NamingStrategy;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 表信息，关联到当前字段信息
 *
 * @author YangHu
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2024.04.02 23:58
 * @since 2016 /8/30
 */
@Data
@Accessors(chain = true)
public class TableInfo {

    /** Import packages */
    private final Set<String> importPackages = new HashSet<>();
    private final Set<String> queryImportPackages = new HashSet<>();
    /** Convert */
    private boolean convert;
    /** Name */
    private String name;
    /** Comment */
    private String comment;
    /** Entity name */
    private String entityName;
    /** 引用名, 驼峰命名+首字母小写 */
    private String referenceName;
    /** Mapper name */
    private String mapperName;
    /** Xml name */
    private String xmlName;
    /** Service name */
    private String serviceName;
    /** Service impl name */
    private String serviceImplName;
    /** Controller name */
    private String controllerName;
    /** Fields */
    private List<TableField> fields;
    /** 枚举字段 */
    private List<EnumField> enumFields;
    /** 查询参数字段 */
    private List<TableField> queryFields;
    /** Have primary key */
    private boolean havePrimaryKey;
    private boolean haveEnumField;
    private boolean haveQueryField;
    /**
     * 公共字段
     */
    private List<TableField> commonFields;
    /** Field names */
    private String fieldNames;

    /**
     * Sets convert *
     *
     * @param convert convert
     * @return the convert
     * @since 2024.2.0
     */
    public TableInfo setConvert(boolean convert) {
        this.convert = convert;
        return this;
    }

    /**
     * Sets convert *
     *
     * @param strategyConfig strategy config
     * @return the convert
     * @since 2024.2.0
     */
    protected TableInfo setConvert(StrategyConfig strategyConfig) {
        if (strategyConfig.startsWithTablePrefix(name) || strategyConfig.isEntityTableFieldAnnotationEnable()) {
            // 包含前缀
            this.convert = true;
        } else if (strategyConfig.isCapitalModeNaming(name)) {
            // 包含
            this.convert = false;
        } else {
            // 转换字段
            if (NamingStrategy.underline_to_camel == strategyConfig.getColumnNaming()) {
                // 包含大写处理
                if (TableField.containsUpperCase(name)) {
                    this.convert = true;
                }
            } else if (!entityName.equalsIgnoreCase(name)) {
                this.convert = true;
            }
        }
        return this;
    }

    /**
     * Sets entity name *
     *
     * @param strategyConfig strategy config
     * @param entityName     entity name
     * @return the entity name
     * @since 2024.2.0
     */
    public TableInfo setEntityName(StrategyConfig strategyConfig, String entityName) {
        this.entityName = entityName;
        this.setConvert(strategyConfig);
        return this;
    }

    /**
     * Sets fields *
     *
     * @param fields fields
     * @return the fields
     * @since 2024.2.0
     */
    public TableInfo setFields(List<TableField> fields) {
        this.fields = fields;
        if (CollectionUtils.isNotEmpty(fields)) {
            // 收集导入包信息
            for (TableField field : fields) {
                if (null != field.getColumnType() && null != field.getColumnType().getPkg()) {
                    importPackages.add(field.getColumnType().getPkg());
                }
                if (field.isKeyFlag()) {
                    // 主键
                    if (field.isConvert() || field.isKeyIdentityFlag()) {
                        importPackages.add(TableId.class.getCanonicalName());
                    }
                    // 自增
                    if (field.isKeyIdentityFlag()) {
                        importPackages.add(IdType.class.getCanonicalName());
                    }
                } else if (field.isConvert()) {
                    // 普通字段
                    importPackages.add(TableField.class.getCanonicalName());
                }
                if (null != field.getFill()) {
                    // 填充字段
                    importPackages.add(TableField.class.getCanonicalName());
                    importPackages.add(FieldFill.class.getCanonicalName());
                }
            }
        }
        return this;
    }

    /**
     * Sets import packages *
     *
     * @param pkg pkg
     * @return the import packages
     * @since 2024.2.0
     */
    public TableInfo setImportPackages(String pkg) {
        if (importPackages.contains(pkg)) {
            return this;
        } else {
            importPackages.add(pkg);
            return this;
        }
    }

    /**
     * 逻辑删除
     *
     * @param logicDeletePropertyName logic delete property name
     * @return the boolean
     * @since 2024.2.0
     */
    public boolean isLogicDelete(String logicDeletePropertyName) {
        return fields.parallelStream().anyMatch(tf -> tf.getName().equals(logicDeletePropertyName));
    }

    /**
     * 转换filed实体为 xml mapper 中的 base column 字符串信息
     *
     * @return the field names
     * @since 2024.2.0
     */
    public String getFieldNames() {
        if (StringUtils.isBlank(fieldNames)
            && CollectionUtils.isNotEmpty(fields)) {
            StringBuilder names = new StringBuilder();
            IntStream.range(0, fields.size()).forEach(i -> {
                TableField fd = fields.get(i);
                if (i == fields.size() - 1) {
                    names.append(fd.getColumnName());
                } else {
                    names.append(fd.getColumnName()).append(", ");
                }
            });
            fieldNames = names.toString();
        }
        return fieldNames;
    }

}
