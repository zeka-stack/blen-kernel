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

import com.baomidou.mybatisplus.annotation.DbType;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 表数据查询接口
 *
 * @author hubin
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2024.04.02 23:58
 * @since 2018 -01-16
 */
public interface IDbQuery {

    /**
     * 数据库类型
     *
     * @return the db type
     * @since 2024.2.0
     * @deprecated 3.3.1 {@link DataSourceConfig#setDbType(DbType)}
     */
    @Deprecated
    default DbType dbType() {
        return null;
    }


    /**
     * 表信息查询 SQL
     *
     * @return the string
     * @since 2024.2.0
     */
    String tablesSql();


    /**
     * 表字段信息查询 SQL
     *
     * @return the string
     * @since 2024.2.0
     */
    String tableFieldsSql();


    /**
     * 表名称
     *
     * @return the string
     * @since 2024.2.0
     */
    String tableName();


    /**
     * 表注释
     *
     * @return the string
     * @since 2024.2.0
     */
    String tableComment();


    /**
     * 字段名称
     *
     * @return the string
     * @since 2024.2.0
     */
    String fieldName();


    /**
     * 字段类型
     *
     * @return the string
     * @since 2024.2.0
     */
    String fieldType();


    /**
     * 字段注释
     *
     * @return the string
     * @since 2024.2.0
     */
    String fieldComment();


    /**
     * 主键字段
     *
     * @return the string
     * @since 2024.2.0
     */
    String fieldKey();


    /**
     * 判断主键是否为identity
     *
     * @param results ResultSet
     * @return 主键是否为identity boolean
     * @throws SQLException ignore
     * @since 2024.2.0
     */
    boolean isKeyIdentity(ResultSet results) throws SQLException;


    /**
     * 自定义字段名称
     *
     * @return the string [ ]
     * @since 2024.2.0
     */
    String[] fieldCustom();
}
