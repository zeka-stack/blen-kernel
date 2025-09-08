/*
 * Copyright 1999-2018 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.dong4j.zeka.kernel.sentinel.rule.nacos;

import com.alibaba.csp.sentinel.dashboard.rule.DynamicRuleProvider;
import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.csp.sentinel.util.StringUtil;
import com.alibaba.nacos.api.config.ConfigService;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.List;

/**
 * Sentinel规则Nacos提供者实现类
 * <p>
 * 该类实现了DynamicRuleProvider接口，负责从Nacos配置中心获取Sentinel规则配置
 * 支持泛型，可以处理多种类型的规则配置，如流量控制规则、熔断降级规则等
 * </p>
 *
 * @param <T> 规则类型参数，可以是FlowRuleEntity、DegradeRuleEntity等各种规则实体类型
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2019.12.21 23:37
 * @since 1.0.0
 */
public class RuleNacosProvider<T> implements DynamicRuleProvider<List<T>> {
    /** Nacos配置服务实例，用于获取配置信息 */
    private final ConfigService configService;
    /** 配置转换器，将字符串配置转换为指定类型的规则列表 */
    private final Converter<String, List<T>> converter;
    /** 规则数据ID后缀，与应用名组合形成完整的数据ID */
    private final String dataId;

    /**
     * 构造函数，初始化RuleNacosProvider实例
     *
     * @param configService Nacos配置服务实例
     * @param converter     配置转换器，用于将字符串配置转换为规则列表
     * @param dataId        规则数据ID后缀
     * @since 1.0.0
     */
    @Contract(pure = true)
    public RuleNacosProvider(ConfigService configService,
                             Converter<String, List<T>> converter,
                             String dataId) {
        this.configService = configService;
        this.converter = converter;
        this.dataId = dataId;
    }

    /**
     * 获取指定应用的规则列表
     * <p>
     * 从Nacos配置中心获取指定应用的规则配置，并通过转换器转换为规则对象列表
     * 如果配置为空，则返回空列表
     * </p>
     *
     * @param appName 应用名称，用于构建完整的数据ID
     * @return 规则对象列表，如果没有配置则返回空列表
     * @throws Exception 获取配置失败或转换失败时抛出异常
     * @since 1.0.0
     */
    @Override
    public List<T> getRules(String appName) throws Exception {
        String rules = configService.getConfig(appName + dataId,
                                                     NacosConfigUtil.GROUP_ID, 3000);
        if (StringUtil.isEmpty(rules)) {
            return new ArrayList<>();
        }
        return converter.convert(rules);
    }
}
