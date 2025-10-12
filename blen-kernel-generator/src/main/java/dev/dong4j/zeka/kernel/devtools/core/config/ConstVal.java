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

import java.nio.charset.StandardCharsets;

/**
 * 定义常量
 *
 * @author YangHu, tangguo, hubin
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2024.04.02 23:58
 * @since 2016 -08-31
 */
public interface ConstVal {

    /** MODULE_NAME */
    String MODULE_NAME = "ModuleName";

    /** ENTITY */
    String ENTITY = "Entity";
    /** SERVICE */
    String SERVICE = "Service";
    /** SERVICE_IMPL */
    String SERVICE_IMPL = "ServiceImpl";
    /** MAPPER */
    String MAPPER = "Mapper";
    /** XML */
    String XML = "Xml";
    /** CONTROLLER */
    String CONTROLLER = "Controller";

    /** ENTITY_PATH */
    String ENTITY_PATH = "entity_path";
    /** SERVICE_PATH */
    String SERVICE_PATH = "service_path";
    /** SERVICE_IMPL_PATH */
    String SERVICE_IMPL_PATH = "service_impl_path";
    /** MAPPER_PATH */
    String MAPPER_PATH = "mapper_path";
    /** XML_PATH */
    String XML_PATH = "xml_path";
    /** CONTROLLER_PATH */
    String CONTROLLER_PATH = "controller_path";

    /** JAVA_TMPDIR */
    String JAVA_TMPDIR = "java.io.tmpdir";
    /** UTF8 */
    String UTF8 = StandardCharsets.UTF_8.name();
    /** UNDERLINE */
    String UNDERLINE = "_";

    /** JAVA_SUFFIX */
    String JAVA_SUFFIX = ".java";
    /** KT_SUFFIX */
    String KT_SUFFIX = ".kt";
    /** XML_SUFFIX */
    String XML_SUFFIX = ".xml";

    /** TEMPLATE_ENTITY_JAVA */
    String TEMPLATE_ENTITY_JAVA = "/templates/entity.java";
    /** TEMPLATE_ENTITY_KT */
    String TEMPLATE_ENTITY_KT = "/templates/entity.kt";
    /** TEMPLATE_MAPPER */
    String TEMPLATE_MAPPER = "/templates/mapper.java";
    /** TEMPLATE_XML */
    String TEMPLATE_XML = "/templates/mapper.xml";
    /** TEMPLATE_SERVICE */
    String TEMPLATE_SERVICE = "/templates/service.java";
    /** TEMPLATE_SERVICE_IMPL */
    String TEMPLATE_SERVICE_IMPL = "/templates/serviceImpl.java";
    /** TEMPLATE_CONTROLLER */
    String TEMPLATE_CONTROLLER = "/templates/controller.java";
    /** TEMPLATE_ENUMS */
    String TEMPLATE_ENUMS = "/templates/enums.java";

    /** VM_LOAD_PATH_KEY */
    String VM_LOAD_PATH_KEY = "resource.loader.file.class";
    /** VM_LOAD_PATH_VALUE */
    String VM_LOAD_PATH_VALUE = "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader";

    /** SUPER_MAPPER_CLASS */
    String SUPER_MAPPER_CLASS = "com.baomidou.mybatisplus.core.mapper.BaseMapper";
    /** SUPER_SERVICE_CLASS */
    String SUPER_SERVICE_CLASS = "com.baomidou.mybatisplus.extension.service.IService";
    /** SUPER_SERVICE_IMPL_CLASS */
    String SUPER_SERVICE_IMPL_CLASS = "com.baomidou.mybatisplus.extension.service.impl.ServiceImpl";

}
