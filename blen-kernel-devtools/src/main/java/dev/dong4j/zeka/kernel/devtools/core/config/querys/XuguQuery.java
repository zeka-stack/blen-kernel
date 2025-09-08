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

/**
 * <p>
 * Xugu 表数据查询
 * </p>
 *
 * @author unique1319 lanjerry
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2024.04.02 23:58
 * @since 2020 -10-26
 */
@SuppressWarnings("all")
public class XuguQuery extends AbstractDbQuery {

    /**
     * Tables sql
     *
     * @return the string
     * @since 1.0.0
     */
    @Override
    public String tablesSql() {
        return "SELECT * FROM ALL_TABLES WHERE 1 = 1";
    }

    /**
     * Table fields sql
     *
     * @return the string
     * @since 1.0.0
     */
    @Override
    public String tableFieldsSql() {
        return "SELECT B.COL_NAME,B.TYPE_NAME,B.COMMENTS, '' AS KEY FROM ALL_TABLES A INNER JOIN ALL_COLUMNS B ON A.TABLE_ID = B.TABLE_ID WHERE A.TABLE_NAME = '%s'";
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
        return "COMMENTS";
    }

    /**
     * Field name
     *
     * @return the string
     * @since 1.0.0
     */
    @Override
    public String fieldName() {
        return "COL_NAME";
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
        return "COMMENTS";
    }

    /**
     * Field key
     *
     * @return the string
     * @since 1.0.0
     */
    @Override
    public String fieldKey() {
        return "KEY";
    }
}
