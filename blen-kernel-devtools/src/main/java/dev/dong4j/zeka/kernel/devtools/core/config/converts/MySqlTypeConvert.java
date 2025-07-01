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
import dev.dong4j.zeka.kernel.devtools.core.config.rules.DateType;
import dev.dong4j.zeka.kernel.devtools.core.config.rules.DbColumnType;
import dev.dong4j.zeka.kernel.devtools.core.config.rules.IColumnType;

import static dev.dong4j.zeka.kernel.devtools.core.config.converts.TypeConverts.contains;
import static dev.dong4j.zeka.kernel.devtools.core.config.converts.TypeConverts.containsAny;
import static dev.dong4j.zeka.kernel.devtools.core.config.rules.DbColumnType.BIG_DECIMAL;
import static dev.dong4j.zeka.kernel.devtools.core.config.rules.DbColumnType.BLOB;
import static dev.dong4j.zeka.kernel.devtools.core.config.rules.DbColumnType.BOOLEAN;
import static dev.dong4j.zeka.kernel.devtools.core.config.rules.DbColumnType.BYTE_ARRAY;
import static dev.dong4j.zeka.kernel.devtools.core.config.rules.DbColumnType.CLOB;
import static dev.dong4j.zeka.kernel.devtools.core.config.rules.DbColumnType.DOUBLE;
import static dev.dong4j.zeka.kernel.devtools.core.config.rules.DbColumnType.FLOAT;
import static dev.dong4j.zeka.kernel.devtools.core.config.rules.DbColumnType.INTEGER;
import static dev.dong4j.zeka.kernel.devtools.core.config.rules.DbColumnType.LONG;
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
public class MySqlTypeConvert implements ITypeConvert {
    /** INSTANCE */
    public static final MySqlTypeConvert INSTANCE = new MySqlTypeConvert();

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
            .test(containsAny("char", "text", "json", "enum").then(STRING))
            .test(contains("bigint").then(LONG))
            .test(containsAny("tinyint(1)", "bit").then(BOOLEAN))
            .test(contains("int").then(INTEGER))
            .test(contains("decimal").then(BIG_DECIMAL))
            .test(contains("clob").then(CLOB))
            .test(contains("blob").then(BLOB))
            .test(contains("binary").then(BYTE_ARRAY))
            .test(contains("float").then(FLOAT))
            .test(contains("double").then(DOUBLE))
            .test(containsAny("date", "time", "year").then(t -> toDateType(config, t)))
            .or(STRING);
    }

    /**
     * 转换为日期类型
     *
     * @param config 配置信息
     * @param type   类型
     * @return 返回对应的列类型 column type
     * @since 2024.2.0
     */
    public static IColumnType toDateType(GlobalConfig config, String type) {
        DateType dateType = config.getDateType();

        if (DateType.ONLY_DATE == dateType) {
            return DbColumnType.DATE;
        } else if (DateType.SQL_PACK == dateType) {
            switch (type) {
                case "date":
                case "year":
                    return DbColumnType.DATE_SQL;
                case "time":
                    return DbColumnType.TIME;
                default:
                    return DbColumnType.TIMESTAMP;
            }
        } else { // TIME_PACK
            switch (type) {
                case "date":
                    return DbColumnType.LOCAL_DATE;
                case "time":
                    return DbColumnType.LOCAL_TIME;
                case "year":
                    return DbColumnType.YEAR;
                default:
                    return DbColumnType.LOCAL_DATE_TIME;
            }
        }
    }
}
