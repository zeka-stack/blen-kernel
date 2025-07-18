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
import dev.dong4j.zeka.kernel.devtools.core.config.po.TableInfo;

/**
 * 名称转换接口类
 *
 * @author hubin
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2024.04.02 23:58
 * @since 2017 -01-20
 */
public interface INameConvert {

    /**
     * 执行实体名称转换
     *
     * @param tableInfo 表信息对象
     * @return string
     * @since 2024.2.0
     */
    String entityNameConvert(TableInfo tableInfo);

    /**
     * 执行属性名称转换
     *
     * @param field 表字段对象，如果属性表字段命名不一致注意 convert 属性的设置
     * @return string
     * @since 2024.2.0
     */
    String propertyNameConvert(TableField field);
}
