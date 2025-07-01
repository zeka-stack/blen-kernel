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

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * 模板路径配置项
 *
 * @author tzg hubin
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2024.04.02 23:58
 * @since 2017 -06-17
 */
@Data
@Accessors(chain = true)
public class TemplateConfig {

    /** Entity */
    @Getter(AccessLevel.NONE)
    private String entity = ConstVal.TEMPLATE_ENTITY_JAVA;

    /** Entity kt */
    private String entityKt = ConstVal.TEMPLATE_ENTITY_KT;

    /** Service */
    private String service = ConstVal.TEMPLATE_SERVICE;

    /** Service */
    private String serviceImpl = ConstVal.TEMPLATE_SERVICE_IMPL;

    /** Mapper */
    private String mapper = ConstVal.TEMPLATE_MAPPER;

    /** Xml */
    private String xml = ConstVal.TEMPLATE_XML;

    /** Controller */
    private String controller = ConstVal.TEMPLATE_CONTROLLER;
    /** Enums */
    private String enums = ConstVal.TEMPLATE_ENUMS;

    /**
     * Gets entity *
     *
     * @param kotlin kotlin
     * @return the entity
     * @since 2024.2.0
     */
    public String getEntity(boolean kotlin) {
        return kotlin ? entityKt : entity;
    }

    /**
     * 禁用模板
     *
     * @param templateTypes 模板类型
     * @return this template config
     * @since 3.3.2
     */
    public TemplateConfig disable(TemplateType... templateTypes) {
        if (templateTypes != null) {
            for (TemplateType templateType : templateTypes) {
                switch (templateType) {
                    case XML:
                        setXml(null);
                        break;
                    case ENTITY:
                        setEntity(null).setEntityKt(null);
                        break;
                    case MAPPER:
                        setMapper(null);
                        break;
                    case SERVICE:
                        setService(null).setServiceImpl(null);
                        break;
                    case CONTROLLER:
                        setController(null);
                        break;
                    default:
                        break;
                }
            }
        }
        return this;
    }

}
