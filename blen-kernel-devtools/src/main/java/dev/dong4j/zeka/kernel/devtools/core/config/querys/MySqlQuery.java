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
 * MySql 表数据查询
 *
 * @author hubin
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2024.04.02 23:58
 * @since 2018 -01-16
 */
public class MySqlQuery extends AbstractDbQuery {

    /**
     * Tables sql
     *
     * @return the string
     * @since 2024.2.0
     */
    @Override
    public String tablesSql() {
        return "show table status WHERE 1=1 ";
    }


    /**
     * Table fields sql
     *
     * @return the string
     * @since 2024.2.0
     */
    @Override
    public String tableFieldsSql() {
        return "show full fields from `%s`";
    }


    /**
     * Table name
     *
     * @return the string
     * @since 2024.2.0
     */
    @Override
    public String tableName() {
        return "NAME";
    }


    /**
     * Table comment
     *
     * @return the string
     * @since 2024.2.0
     */
    @Override
    public String tableComment() {
        return "COMMENT";
    }


    /**
     * Field name
     *
     * @return the string
     * @since 2024.2.0
     */
    @Override
    public String fieldName() {
        return "FIELD";
    }


    /**
     * Field type
     *
     * @return the string
     * @since 2024.2.0
     */
    @Override
    public String fieldType() {
        return "TYPE";
    }


    /**
     * Field comment
     *
     * @return the string
     * @since 2024.2.0
     */
    @Override
    public String fieldComment() {
        return "COMMENT";
    }


    /**
     * Field key
     *
     * @return the string
     * @since 2024.2.0
     */
    @Override
    public String fieldKey() {
        return "KEY";
    }


    /**
     * Is key identity
     *
     * @param results results
     * @return the boolean
     * @throws SQLException sql exception
     * @since 2024.2.0
     */
    @Override
    public boolean isKeyIdentity(ResultSet results) throws SQLException {
        return "auto_increment".equals(results.getString("Extra"));
    }
}
