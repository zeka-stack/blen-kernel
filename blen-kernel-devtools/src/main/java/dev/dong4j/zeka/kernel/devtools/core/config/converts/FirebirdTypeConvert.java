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
package dev.dong4j.zeka.kernel.devtools.core.config.converts;

import dev.dong4j.zeka.kernel.devtools.core.config.GlobalConfig;
import dev.dong4j.zeka.kernel.devtools.core.config.ITypeConvert;
import dev.dong4j.zeka.kernel.devtools.core.config.rules.IColumnType;

import static dev.dong4j.zeka.kernel.devtools.core.config.converts.TypeConverts.contains;
import static dev.dong4j.zeka.kernel.devtools.core.config.converts.TypeConverts.containsAny;
import static dev.dong4j.zeka.kernel.devtools.core.config.rules.DbColumnType.BLOB;
import static dev.dong4j.zeka.kernel.devtools.core.config.rules.DbColumnType.DOUBLE;
import static dev.dong4j.zeka.kernel.devtools.core.config.rules.DbColumnType.FLOAT;
import static dev.dong4j.zeka.kernel.devtools.core.config.rules.DbColumnType.LONG;
import static dev.dong4j.zeka.kernel.devtools.core.config.rules.DbColumnType.SHORT;
import static dev.dong4j.zeka.kernel.devtools.core.config.rules.DbColumnType.STRING;

/**
 * MYSQL 数据库字段类型转换
 *
 * @author hubin, hanchunlin
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2024.04.02 23:58
 * @since 2017 -01-20
 */
public class FirebirdTypeConvert implements ITypeConvert {
    /** INSTANCE */
    public static final FirebirdTypeConvert INSTANCE = new FirebirdTypeConvert();

    /**
     * Process type convert
     *
     * @param config    config
     * @param fieldType field type
     * @return the column type
     * @inheritDoc
     * @since 2024.2.0
     */
    @Override
    public IColumnType processTypeConvert(GlobalConfig config, String fieldType) {
        return TypeConverts.use(fieldType)
            .test(containsAny("cstring", "text").then(STRING))
            .test(contains("short").then(SHORT))
            .test(contains("long").then(LONG))
            .test(contains("float").then(FLOAT))
            .test(contains("double").then(DOUBLE))
            .test(contains("blob").then(BLOB))
            .test(contains("int64").then(LONG))
            .test(containsAny("date", "time", "year").then(t -> MySqlTypeConvert.toDateType(config, t)))
            .or(STRING);
    }

}
