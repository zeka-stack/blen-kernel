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

import com.baomidou.mybatisplus.annotation.DbType;
import dev.dong4j.zeka.kernel.devtools.core.config.IDbQuery;
import java.util.EnumMap;
import java.util.Map;

/**
 * <p>Description: </p>
 *
 * @author nieqiuqiu
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020 -01-09
 * @since 3.3.1
 */
public class DbQueryRegistry {

    /** Db query enum map */
    private final Map<DbType, IDbQuery> dbTypeIDbQueryEnumMap = new EnumMap<>(DbType.class);

    /**
     * Db query registry
     *
     * @since 2024.2.0
     */
    public DbQueryRegistry() {
        dbTypeIDbQueryEnumMap.put(DbType.MARIADB, new MariadbQuery());
        dbTypeIDbQueryEnumMap.put(DbType.H2, new H2Query());
        dbTypeIDbQueryEnumMap.put(DbType.MYSQL, new MySqlQuery());
        dbTypeIDbQueryEnumMap.put(DbType.FIREBIRD, new FirebirdQuery());
        dbTypeIDbQueryEnumMap.put(DbType.XU_GU, new XuguQuery());
    }

    /**
     * Gets db query *
     *
     * @param dbType db type
     * @return the db query
     * @since 2024.2.0
     */
    public IDbQuery getDbQuery(DbType dbType) {
        return dbTypeIDbQueryEnumMap.get(dbType);
    }
}
