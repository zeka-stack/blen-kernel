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
package dev.dong4j.zeka.kernel.devtools.core.config;

import dev.dong4j.zeka.kernel.devtools.core.config.po.TableField;
import dev.dong4j.zeka.kernel.devtools.core.config.rules.IColumnType;

/**
 * 数据库字段类型转换
 *
 * @author hubin
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2024.04.02 23:58
 * @since 2017 -01-20
 */
public interface ITypeConvert {

    /**
     * 执行类型转换
     *
     * @param globalConfig 全局配置
     * @param tableField   字段列信息
     * @return ignore column type
     * @since 1.0.0
     */
    default IColumnType processTypeConvert(GlobalConfig globalConfig, TableField tableField) {
        return processTypeConvert(globalConfig, tableField.getType());
    }

    /**
     * 执行类型转换
     *
     * @param globalConfig 全局配置
     * @param fieldType    字段类型
     * @return ignore column type
     * @since 1.0.0
     */
    IColumnType processTypeConvert(GlobalConfig globalConfig, String fieldType);

}
