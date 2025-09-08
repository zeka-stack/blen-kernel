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

import com.alibaba.csp.sentinel.dashboard.rule.DynamicRulePublisher;
import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.csp.sentinel.util.AssertUtil;
import com.alibaba.nacos.api.config.ConfigService;

import org.jetbrains.annotations.Contract;

import java.util.List;

/**
 * Sentinel规则Nacos发布者实现类
 * <p>
 * 该类实现了DynamicRulePublisher接口，负责将Sentinel规则配置发布到Nacos配置中心
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
public class RuleNacosPublisher<T> implements DynamicRulePublisher<List<T>> {

    /** Nacos配置服务实例，用于发布配置信息 */
    private final ConfigService configService;
    /** 配置转换器，将规则列表转换为字符串配置 */
    private final Converter<List<T>, String> converter;
    /** 规则数据ID后缀，与应用名组合形成完整的数据ID */
    private final String dataId;

    /**
     * 构造函数，初始化RuleNacosPublisher实例
     *
     * @param configService Nacos配置服务实例
     * @param converter     配置转换器，用于将规则列表转换为字符串配置
     * @param dataId        规则数据ID后缀
     * @since 1.0.0
     */
    @Contract(pure = true)
    public RuleNacosPublisher(ConfigService configService,
                              Converter<List<T>, String> converter,
                              String dataId) {
        this.configService = configService;
        this.converter = converter;
        this.dataId = dataId;
    }

    /**
     * 发布指定应用的规则列表到Nacos配置中心
     * <p>
     * 将规则列表通过转换器转换为字符串配置，并发布到Nacos配置中心
     * 如果应用名为空，将抛出异常；如果规则列表为空，则不执行任何操作
     * </p>
     *
     * @param app   应用名称，用于构建完整的数据ID
     * @param rules 要发布的规则对象列表
     * @throws Exception 应用名为空或发布配置失败时抛出异常
     * @since 1.0.0
     */
    @Override
    public void publish(String app, List<T> rules) throws Exception {
        AssertUtil.notEmpty(app, "app name cannot be empty");
        if (rules == null) {
            return;
        }
        configService.publishConfig(app + dataId,
                                    NacosConfigUtil.GROUP_ID, converter.convert(rules));
    }
}
