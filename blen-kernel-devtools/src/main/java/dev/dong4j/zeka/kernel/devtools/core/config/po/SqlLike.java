/*
 * Copyright (c) 2011-2021, baomidou (jobob@qq.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dev.dong4j.zeka.kernel.devtools.core.config.po;

/**
 * SQL like 枚举
 *
 * @author Caratacus
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2024.04.02 23:58
 * @since 2016 -12-4
 */
public enum SqlLike {
    /**
     * %值
     */
    LEFT,
    /**
     * 值%
     */
    RIGHT,
    /**
     * %值%
     */
    DEFAULT
}
