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
* <p>Description: </p>
 *
 * @param <T> parameter
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dong4j@gmail.com"
 * @date 2019.12.21 23:37
 * @since 1.0.0
 */
public class RuleNacosPublisher<T> implements DynamicRulePublisher<List<T>> {

    /** Config service */
    private final ConfigService configService;
    /** Converter */
    private final Converter<List<T>, String> converter;
    /** Data id */
    private final String dataId;

    /**
     * Rule nacos publisher
     *
     * @param configService config service
     * @param converter     converter
     * @param dataId        data id
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
     * Publish *
     *
     * @param app   app
     * @param rules rules
     * @throws Exception exception
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
