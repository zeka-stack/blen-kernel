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

import java.util.List;

/**
 * 关键字处理接口
 *
 * @author nieqiurong 2020/5/7.
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2024.04.02 23:58
 * @since 3.3.2
 */
public interface IKeyWordsHandler {

    /**
     * 获取关键字
     *
     * @return 关键字集合 key words
     * @since 2024.2.0
     */
    List<String> getKeyWords();

    /**
     * 格式化关键字格式
     *
     * @return 格式 string
     * @since 2024.2.0
     */
    String formatStyle();

    /**
     * 是否为关键字
     *
     * @param columnName 字段名称
     * @return 是否为关键字 boolean
     * @since 2024.2.0
     */
    boolean isKeyWords(String columnName);

    /**
     * 格式化字段
     *
     * @param columnName 字段名称
     * @return 格式化字段 string
     * @since 2024.2.0
     */
    default String formatColumn(String columnName) {
        return String.format(formatStyle(), columnName);
    }

}
