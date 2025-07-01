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

import dev.dong4j.zeka.kernel.devtools.core.config.po.TableInfo;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 输出文件配置
 *
 * @author hubin
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2024.04.02 23:58
 * @since 2017 -01-18
 */
@Data
@Accessors(chain = true)
public abstract class FileOutConfig {

    /**
     * 模板
     */
    private String templatePath;

    /**
     * File out config
     *
     * @since 2024.2.0
     */
    public FileOutConfig() {
        // to do nothing
    }

    /**
     * File out config
     *
     * @param templatePath template path
     * @since 2024.2.0
     */
    public FileOutConfig(String templatePath) {
        this.templatePath = templatePath;
    }

    /**
     * 输出文件
     *
     * @param tableInfo table info
     * @return the string
     * @since 2024.2.0
     */
    public abstract String outputFile(TableInfo tableInfo);
}
