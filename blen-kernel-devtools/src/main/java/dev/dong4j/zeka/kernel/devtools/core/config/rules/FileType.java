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
package dev.dong4j.zeka.kernel.devtools.core.config.rules;

/**
 * 生成文件类型
 *
 * @author hubin
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2024.04.02 23:58
 * @since 2018 -08-07
 */
public enum FileType {
    /** Entity file type */
    ENTITY,
    /** Mapper file type */
    MAPPER,
    /** Xml file type */
    XML,
    /** Service file type */
    SERVICE,
    /** Service file type */
    SERVICE_IMPL,
    /** Controller file type */
    CONTROLLER,
    /** Other file type */
    OTHER
}
