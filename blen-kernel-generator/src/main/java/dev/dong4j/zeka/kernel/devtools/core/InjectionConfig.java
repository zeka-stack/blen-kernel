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
package dev.dong4j.zeka.kernel.devtools.core;

import dev.dong4j.zeka.kernel.devtools.core.config.FileOutConfig;
import dev.dong4j.zeka.kernel.devtools.core.config.IFileCreate;
import dev.dong4j.zeka.kernel.devtools.core.config.builder.ConfigBuilder;
import dev.dong4j.zeka.kernel.devtools.core.config.po.TableInfo;
import java.util.List;
import java.util.Map;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 抽象的对外接口
 *
 * @author hubin
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2024.04.02 23:58
 * @since 2016 -12-07
 */
@Data
@Accessors(chain = true)
public abstract class InjectionConfig {

    /**
     * 全局配置
     */
    private ConfigBuilder config;

    /**
     * 自定义返回配置 Map 对象
     */
    private Map<String, Object> map;

    /**
     * 自定义输出文件
     */
    private List<FileOutConfig> fileOutConfigList;

    /**
     * 自定义判断是否创建文件
     */
    private IFileCreate fileCreate;

    /**
     * 注入自定义 Map 对象，针对所有表的全局参数
     *
     * @since 1.0.0
     */
    public abstract void initMap();

    /**
     * 依据表相关信息，从三方获取到需要元数据，处理方法环境里面
     *
     * @param tableInfo table info
     * @since 1.0.0
     */
    public void initTableMap(TableInfo tableInfo) {
        // 子类重写注入表对应补充信息
    }

    /**
     * 模板待渲染 Object Map 预处理
     *
     * @param objectMap object map
     * @return the map
     * @since 1.0.0
     */
    public Map<String, Object> prepareObjectMap(Map<String, Object> objectMap) {
        return objectMap;
    }
}
