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

import com.baomidou.mybatisplus.annotation.DbType;
import dev.dong4j.zeka.kernel.devtools.core.config.ITypeConvert;
import dev.dong4j.zeka.kernel.devtools.core.config.converts.select.BranchBuilder;
import dev.dong4j.zeka.kernel.devtools.core.config.converts.select.Selector;
import dev.dong4j.zeka.kernel.devtools.core.config.rules.IColumnType;

/**
 * 该注册器负责注册并查询类型注册器
 *
 * @author nieqiuqiu, hanchunlin
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2024.04.02 23:58
 * @since 3.3.1
 */
public class TypeConverts {

    /**
     * 查询数据库类型对应的类型转换器
     *
     * @param dbType 数据库类型
     * @return 返回转换器 type convert
     * @since 2024.2.0
     */
    public static ITypeConvert getTypeConvert(DbType dbType) {
        switch (dbType) {
            case MYSQL:
            case MARIADB:
                return MySqlTypeConvert.INSTANCE;
            default:
                return null;
        }
    }

    /**
     * 使用指定参数构建一个选择器
     *
     * @param param 参数
     * @return 返回选择器 selector
     * @since 2024.2.0
     */
    static Selector<String, IColumnType> use(String param) {
        return new Selector<>(param.toLowerCase());
    }

    /**
     * 这个分支构建器用于构建用于支持 {@link String#contains(CharSequence)} 的分支
     *
     * @param value 分支的值
     * @return 返回分支构建器 branch builder
     * @see #containsAny(CharSequence...)
     * @since 2024.2.0
     */
    static BranchBuilder<String, IColumnType> contains(CharSequence value) {
        return BranchBuilder.of(s -> s.contains(value));
    }

    /**
     * Contains any
     *
     * @param values values
     * @return the branch builder
     * @see #contains(CharSequence)
     * @since 2024.2.0
     */
    static BranchBuilder<String, IColumnType> containsAny(CharSequence... values) {
        return BranchBuilder.of(s -> {
            for (CharSequence value : values) {
                if (s.contains(value)) {
                    return true;
                }
            }
            return false;
        });
    }

}
