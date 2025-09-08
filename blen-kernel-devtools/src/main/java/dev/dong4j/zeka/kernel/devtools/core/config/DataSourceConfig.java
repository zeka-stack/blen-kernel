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
import dev.dong4j.zeka.kernel.common.exception.LowestException;
import dev.dong4j.zeka.kernel.devtools.core.config.converts.MySqlTypeConvert;
import dev.dong4j.zeka.kernel.devtools.core.config.converts.TypeConverts;
import dev.dong4j.zeka.kernel.devtools.core.config.querys.DbQueryRegistry;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 数据库配置
 *
 * @author YangHu, hcl
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2024.04.02 23:58
 * @since 2016 /8/30
 */
@Data
@Accessors(chain = true)
public class DataSourceConfig {

    /**
     * 数据库信息查询
     */
    private IDbQuery dbQuery;
    /**
     * 数据库类型
     */
    private DbType dbType;
    /**
     * schemaName
     */
    private String schemaName;
    /**
     * 类型转换
     */
    private ITypeConvert typeConvert;
    /**
     * 关键字处理器
     *
     * @since 1.0.0
     */
    private IKeyWordsHandler keyWordsHandler;
    /**
     * 驱动连接的URL
     */
    private String url;
    /**
     * 驱动名称
     */
    private String driverName;
    /**
     * 数据库连接用户名
     */
    private String username;
    /**
     * 数据库连接密码
     */
    private String password;

    /**
     * Gets db query *
     *
     * @return the db query
     * @since 1.0.0
     */
    public IDbQuery getDbQuery() {
        if (null == dbQuery) {
            DbType dbType = getDbType();
            DbQueryRegistry dbQueryRegistry = new DbQueryRegistry();
            // 默认 MYSQL
            dbQuery = Optional.ofNullable(dbQueryRegistry.getDbQuery(dbType))
                .orElseGet(() -> dbQueryRegistry.getDbQuery(DbType.MYSQL));
        }
        return dbQuery;
    }

    /**
     * 判断数据库类型
     *
     * @return 类型枚举值 db type
     * @since 1.0.0
     */
    public DbType getDbType() {
        if (null == this.dbType) {
            this.dbType = this.getDbType(this.url.toLowerCase());
            if (null == this.dbType) {
                throw new LowestException("Unknown type of database!");
            }
        }

        return this.dbType;
    }

    /**
     * 判断数据库类型
     *
     * @param str 用于寻找特征的字符串，可以是 driverName 或小写后的 url
     * @return 类型枚举值 ，如果没找到，则返回 null
     * @since 1.0.0
     */
    private DbType getDbType(String str) {
        if (str.contains(":mysql:") || str.contains(":cobar:")) {
            return DbType.MYSQL;
        } else if (str.contains(":oracle:")) {
            return DbType.ORACLE;
        } else if (str.contains(":postgresql:")) {
            return DbType.POSTGRE_SQL;
        } else if (str.contains(":sqlserver:")) {
            return DbType.SQL_SERVER;
        } else if (str.contains(":db2:")) {
            return DbType.DB2;
        } else if (str.contains(":mariadb:")) {
            return DbType.MARIADB;
        } else if (str.contains(":sqlite:")) {
            return DbType.SQLITE;
        } else if (str.contains(":h2:")) {
            return DbType.H2;
        } else if (str.contains(":kingbase:") || str.contains(":kingbase8:")) {
            return DbType.KINGBASE_ES;
        } else if (str.contains(":dm:")) {
            return DbType.DM;
        } else if (str.contains(":zenith:")) {
            return DbType.GAUSS;
        } else if (str.contains(":oscar:")) {
            return DbType.OSCAR;
        } else if (str.contains(":firebird:")) {
            return DbType.FIREBIRD;
        } else if (str.contains(":xugu:")) {
            return DbType.XU_GU;
        } else {
            return DbType.OTHER;
        }
    }

    /**
     * Gets type convert *
     *
     * @return the type convert
     * @since 1.0.0
     */
    public ITypeConvert getTypeConvert() {
        if (null == typeConvert) {
            DbType dbType = getDbType();
            // 默认 MYSQL
            typeConvert = TypeConverts.getTypeConvert(dbType);
            if (null == typeConvert) {
                typeConvert = MySqlTypeConvert.INSTANCE;
            }
        }
        return typeConvert;
    }

    /**
     * 创建数据库连接对象
     *
     * @return Connection conn
     * @since 1.0.0
     */
    public Connection getConn() {
        Connection conn;
        try {
            Class.forName(driverName);
            conn = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
        return conn;
    }
}
