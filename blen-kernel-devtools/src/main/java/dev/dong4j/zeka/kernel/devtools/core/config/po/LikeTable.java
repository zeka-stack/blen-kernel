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
package dev.dong4j.zeka.kernel.devtools.core.config.po;

import dev.dong4j.zeka.kernel.common.util.StringPool;

/**
 * 表名拼接
 *
 * @author nieqiuqiu
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2019 -11-26
 * @since 3.3.0
 */
public class LikeTable {

    /** Value */
    private final String value;

    /** Like */
    private SqlLike like = SqlLike.DEFAULT;

    /**
     * Like table
     *
     * @param value value
     * @since 2024.2.0
     */
    public LikeTable(String value) {
        this.value = value;
    }

    /**
     * Like table
     *
     * @param value value
     * @param like  like
     * @since 2024.2.0
     */
    public LikeTable(String value, SqlLike like) {
        this.value = value;
        this.like = like;
    }

    /**
     * To string
     *
     * @return the string
     * @since 2024.2.0
     */
    @Override
    public String toString() {
        return getValue();
    }

    /**
     * Gets value *
     *
     * @return the value
     * @since 2024.2.0
     */
    public String getValue() {
        return concatLike(this.value, like);
    }


    /**
     * Concat like
     *
     * @param str  str
     * @param type type
     * @return the string
     * @since 2024.2.0
     */
    public static String concatLike(Object str, SqlLike type) {
        switch (type) {
            case LEFT:
                return StringPool.PERCENT + str;
            case RIGHT:
                return str + StringPool.PERCENT;
            default:
                return StringPool.PERCENT + str + StringPool.PERCENT;
        }
    }
}
