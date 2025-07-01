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
 * MySql 表数据查询
 *
 * @author steven ma
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2024.04.02 23:58
 * @since 2020 -08-20
 */
public class FirebirdQuery extends AbstractDbQuery {

    /**
     * Tables sql
     *
     * @return the string
     * @since 2024.2.0
     */
    @Override
    public String tablesSql() {
        return "select trim(rdb$relation_name) as rdb$relation_name\n" +
            "                from rdb$relations\n" +
            "                where rdb$view_blr is null\n" +
            "                and (rdb$system_flag is null or rdb$system_flag = 0)";
    }


    /**
     * Table fields sql
     *
     * @return the string
     * @since 2024.2.0
     */
    @Override
    public String tableFieldsSql() {
        return "select trim(f.rdb$relation_name) AS rdb$relation_name,\n" +
            "                trim(f.rdb$field_name) AS FIELD, t.rdb$type_name AS  TYPE,\n" +
            "                (CASE WHEN (\n" +
            "                   SELECT count(*)\n" +
            "                   FROM RDB$RELATION_CONSTRAINTS RC\n" +
            "                   LEFT JOIN RDB$INDICES I ON (I.RDB$INDEX_NAME = RC.RDB$INDEX_NAME)\n" +
            "                   LEFT JOIN RDB$INDEX_SEGMENTS S ON (S.RDB$INDEX_NAME = I.RDB$INDEX_NAME)\n" +
            "                   WHERE (RC.RDB$CONSTRAINT_TYPE = 'PRIMARY KEY')\n" +
            "                   AND (I.RDB$RELATION_NAME = f.rdb$relation_name  )\n" +
            "                   AND (S.RDB$FIELD_NAME = f.rdb$field_name)\n" +
            "                ) > 0 THEN 'PRI' ELSE '' END) AS pk\n" +
            "                from rdb$relation_fields f\n" +
            "                join rdb$relations r on f.rdb$relation_name = r.rdb$relation_name\n" +
            "                JOIN rdb$fields fs ON f.rdb$field_source = fs.rdb$field_name\n" +
            "                JOIN rdb$types  t ON fs.rdb$field_type = t.rdb$type\n" +
            "                and r.rdb$view_blr is NULL\n" +
            "                AND t.rdb$field_name = 'RDB$FIELD_TYPE'\n" +
            "                and (r.rdb$system_flag is null or r.rdb$system_flag = 0)\n" +
            "                AND f.rdb$relation_name = '%s'\n" +
            "                order by 1, f.rdb$field_position";
    }


    /**
     * Table name
     *
     * @return the string
     * @since 2024.2.0
     */
    @Override
    public String tableName() {
        return "rdb$relation_name";
    }


    /**
     * Table comment
     *
     * @return the string
     * @since 2024.2.0
     */
    @Override
    public String tableComment() {
        return "";
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
        return "";
    }


    /**
     * Field key
     *
     * @return the string
     * @since 2024.2.0
     */
    @Override
    public String fieldKey() {
        return "PK";
    }

}
