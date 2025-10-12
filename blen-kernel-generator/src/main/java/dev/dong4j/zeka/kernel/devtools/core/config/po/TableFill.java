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

import com.baomidou.mybatisplus.annotation.FieldFill;
import lombok.Data;

/**
 * 字段填充
 *
 * @author hubin
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2024.04.02 23:58
 * @since 2017 -06-26
 */
@Data
public class TableFill {

    /**
     * 字段名称
     */
    private String fieldName;
    /**
     * 忽略类型
     */
    private FieldFill fieldFill;

    /**
     * Table fill
     *
     * @param fieldName field name
     * @param ignore    ignore
     * @since 1.0.0
     */
    public TableFill(String fieldName, FieldFill ignore) {
        this.fieldName = fieldName;
        this.fieldFill = ignore;
    }
}
