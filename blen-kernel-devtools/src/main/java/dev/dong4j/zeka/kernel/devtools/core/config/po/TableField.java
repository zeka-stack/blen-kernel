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

import dev.dong4j.zeka.kernel.devtools.core.config.StrategyConfig;
import dev.dong4j.zeka.kernel.devtools.core.config.rules.IColumnType;
import dev.dong4j.zeka.kernel.devtools.core.config.rules.NamingStrategy;
import java.util.List;
import java.util.Map;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 表字段信息
 *
 * @author YangHu
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2024.04.02 23:58
 * @since 2016 -12-03
 */
@Data
@Accessors(chain = true)
public class TableField {
    /** Convert */
    private boolean convert;
    /** Key flag */
    private boolean keyFlag;
    /** 字段是否为 String 类型 */
    private boolean stringType;
    /**
     * 主键是否为自增类型
     */
    private boolean keyIdentityFlag;
    /** Name */
    private String name;
    /** Type */
    private String type;
    /** Property name */
    private String propertyName;
    /** Column type */
    private IColumnType columnType;
    /** Comment */
    private String comment;
    /** Fill */
    private String fill;
    /** Enums */
    private boolean enums;
    /** Query */
    private boolean query;
    /** Enum properties */
    private EnumProperties enumProperties;

    /**
     * <p>Description: 枚举类型字段的其他属性</p>
     *
     * @author dong4j
     * @version 1.0.0
     * @email "mailto:dong4j@gmail.com"
     * @date 2024.04.15 22:21
     * @since 2024.2.0
     */
    @Data
    public static class EnumProperties {
        /** Name */
        private String name;
        /** Value type */
        private String valueType;
        /** Items */
        private List<String> items;
        /** Type */
        private EnumType type;
    }

    /**
     * 是否关键字
     *
     * @since 3.3.2
     */
    private boolean keyWords;
    /**
     * 数据库字段（关键字含转义符号）
     *
     * @since 3.3.2
     */
    private String columnName;
    /**
     * 自定义查询字段列表
     */
    private Map<String, Object> customMap;

    /**
     * Sets convert *
     *
     * @param convert convert
     * @return the convert
     * @since 2024.2.0
     */
    public TableField setConvert(boolean convert) {
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
    protected TableField setConvert(StrategyConfig strategyConfig) {
        if (strategyConfig.isEntityTableFieldAnnotationEnable() || isKeyWords()) {
            this.convert = true;
            return this;
        }
        if (strategyConfig.isCapitalModeNaming(name)) {
            this.convert = false;
        } else {
            // 转换字段
            if (NamingStrategy.underline_to_camel == strategyConfig.getColumnNaming()) {
                // 包含大写处理
                if (containsUpperCase(name)) {
                    this.convert = true;
                }
            } else if (!name.equals(propertyName)) {
                this.convert = true;
            }
        }
        return this;
    }

    /**
     * Sets property name *
     *
     * @param strategyConfig strategy config
     * @param propertyName   property name
     * @return the property name
     * @since 2024.2.0
     */
    public TableField setPropertyName(StrategyConfig strategyConfig, String propertyName) {
        this.propertyName = propertyName;
        this.setConvert(strategyConfig);
        return this;
    }

    /**
     * Gets property type *
     *
     * @return the property type
     * @since 2024.2.0
     */
    public String getPropertyType() {
        if (null != columnType) {
            return columnType.getType();
        }
        return null;
    }

    /**
     * 按 JavaBean 规则来生成 get 和 set 方法后面的属性名称
     * 需要处理一下特殊情况：
     * <p>
     * 1、如果只有一位，转换为大写形式
     * 2、如果多于 1 位，只有在第二位是小写的情况下，才会把第一位转为小写
     * <p>
     * 我们并不建议在数据库对应的对象中使用基本类型，因此这里不会考虑基本类型的情况
     *
     * @return the capital name
     * @since 2024.2.0
     */
    public String getCapitalName() {
        if (propertyName.length() == 1) {
            return propertyName.toUpperCase();
        }
        if (Character.isLowerCase(propertyName.charAt(1))) {
            return Character.toUpperCase(propertyName.charAt(0)) + propertyName.substring(1);
        }
        return propertyName;
    }

    /**
     * 获取注解字段名称
     *
     * @return 字段 annotation column name
     * @since 3.3.2
     */
    public String getAnnotationColumnName() {
        if (keyWords) {
            if (columnName.startsWith("\"")) {
                return String.format("\\\"%s\\\"", name);
            }
        }
        return columnName;
    }

    /**
     * Contains upper case
     *
     * @param word word
     * @return the boolean
     * @since 2024.2.0
     */
    public static boolean containsUpperCase(String word) {
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            if (Character.isUpperCase(c)) {
                return true;
            }
        }
        return false;
    }

}
