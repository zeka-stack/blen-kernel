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
package dev.dong4j.zeka.kernel.devtools.core.config.querys;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * H2Database 表数据查询
 *
 * @author yuxiaobin
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2024.04.02 23:58
 * @since 2019 -01-8
 */
public class H2Query extends AbstractDbQuery {

    /** PK_QUERY_SQL */
    public static final String PK_QUERY_SQL = "select * from INFORMATION_SCHEMA.INDEXES WHERE TABLE_NAME = '%s'";

    /**
     * Tables sql
     *
     * @return the string
     * @since 1.0.0
     */
    @Override
    public String tablesSql() {
        return "SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE 1=1 ";
    }


    /**
     * Table fields sql
     *
     * @return the string
     * @since 1.0.0
     */
    @Override
    public String tableFieldsSql() {
        return "SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME= '%s' ";
    }


    /**
     * Table name
     *
     * @return the string
     * @since 1.0.0
     */
    @Override
    public String tableName() {
        return "TABLE_NAME";
    }


    /**
     * Table comment
     *
     * @return the string
     * @since 1.0.0
     */
    @Override
    public String tableComment() {
        return "REMARKS";
    }


    /**
     * Field name
     *
     * @return the string
     * @since 1.0.0
     */
    @Override
    public String fieldName() {
        return "COLUMN_NAME";
    }


    /**
     * Field type
     *
     * @return the string
     * @since 1.0.0
     */
    @Override
    public String fieldType() {
        return "TYPE_NAME";
    }


    /**
     * Field comment
     *
     * @return the string
     * @since 1.0.0
     */
    @Override
    public String fieldComment() {
        return "REMARKS";
    }


    /**
     * Field key
     *
     * @return the string
     * @since 1.0.0
     */
    @Override
    public String fieldKey() {
        return "PRIMARY_KEY";
    }


    /**
     * Is key identity
     *
     * @param results results
     * @return the boolean
     * @throws SQLException sql exception
     * @since 1.0.0
     */
    @Override
    public boolean isKeyIdentity(ResultSet results) throws SQLException {
        return "auto_increment".equals(results.getString("Extra"));
    }
}
