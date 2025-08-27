package dev.dong4j.zeka.kernel.sentinel.config;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.gateway.GatewayFlowRuleEntity;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.DegradeRuleEntity;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.FlowRuleEntity;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.SystemRuleEntity;
import com.alibaba.csp.sentinel.dashboard.rule.DynamicRuleProvider;
import com.alibaba.csp.sentinel.dashboard.rule.DynamicRulePublisher;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityRule;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRule;
import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.config.ConfigFactory;
import com.alibaba.nacos.api.config.ConfigService;
import dev.dong4j.zeka.kernel.sentinel.rule.nacos.NacosConfigUtil;
import dev.dong4j.zeka.kernel.sentinel.rule.nacos.RuleNacosProvider;
import dev.dong4j.zeka.kernel.sentinel.rule.nacos.RuleNacosPublisher;

import org.jetbrains.annotations.NotNull;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.autoconfigure.AutoConfiguration;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

/**
* <p>Description:  可通过配置选择不同的客户端 </p>
 *
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dong4j@gmail.com"
 * @date 2019.12.21 23:43
 * @since 1.0.0
 */
@Slf4j
@AutoConfiguration
@EnableConfigurationProperties(SentinelProperties.class)
public class ZekaSentinelAutoConfiguration {

    /**
     * Nacos config service config service
     *
     * @param properties properties
     * @return the config service
     * @throws Exception exception
     * @since 1.0.0
     */
    @Bean
    public ConfigService nacosConfigService(@NotNull SentinelProperties properties) throws Exception {
        return ConfigFactory.createConfigService(properties.getNacos().getServerAddr());
    }

    /**
     * Flow rule provider dynamic rule provider
     *
     * @param configService config service
     * @return the dynamic rule provider
     * @since 1.0.0
     */
    @Bean
    public DynamicRuleProvider<List<FlowRuleEntity>> flowRuleProvider(ConfigService configService) {
        log.info("加载自定义处理器: {}", RuleNacosProvider.class);
        return new RuleNacosProvider<>(configService, s -> JSON.parseArray(s, FlowRuleEntity.class), NacosConfigUtil.FLOW_DATA_ID_POSTFIX);
    }

    /**
     * Flow rule publisher dynamic rule publisher
     *
     * @param configService config service
     * @return the dynamic rule publisher
     * @since 1.0.0
     */
    @Bean
    public DynamicRulePublisher<List<FlowRuleEntity>> flowRulePublisher(ConfigService configService) {
        log.info("加载自定义处理器: {}", RuleNacosPublisher.class);
        return new RuleNacosPublisher<>(configService, JSON::toJSONString, NacosConfigUtil.FLOW_DATA_ID_POSTFIX);
    }

    /**
     * Gateway flow rule provider dynamic rule provider
     *
     * @param configService config service
     * @return the dynamic rule provider
     * @since 1.0.0
     */
    @Bean
    public DynamicRuleProvider<List<GatewayFlowRuleEntity>> gatewayFlowRuleProvider(ConfigService configService) {
        log.info("加载自定义处理器: {}", RuleNacosProvider.class);
        return new RuleNacosProvider<>(configService, s -> JSON.parseArray(s, GatewayFlowRuleEntity.class), NacosConfigUtil.GATEWAY_FLOW_DATA_ID_POSTFIX);
    }

    /**
     * Gateway flow rule publisher dynamic rule publisher
     *
     * @param configService config service
     * @return the dynamic rule publisher
     * @since 1.0.0
     */
    @Bean
    public DynamicRulePublisher<List<GatewayFlowRuleEntity>> gatewayFlowRulePublisher(ConfigService configService) {
        log.info("加载自定义处理器: {}", RuleNacosPublisher.class);
        return new RuleNacosPublisher<>(configService, JSON::toJSONString, NacosConfigUtil.GATEWAY_FLOW_DATA_ID_POSTFIX);
    }

    /**
     * Authority rule provider dynamic rule provider
     *
     * @param configService config service
     * @return the dynamic rule provider
     * @since 1.0.0
     */
    @Bean
    public DynamicRuleProvider<List<AuthorityRule>> authorityRuleProvider(ConfigService configService) {
        log.info("加载自定义处理器: {}", RuleNacosProvider.class);
        return new RuleNacosProvider<>(configService, s -> JSON.parseArray(s, AuthorityRule.class), NacosConfigUtil.AUTHORITY_DATA_ID_POSTFIX);
    }

    /**
     * Authority rule publisher dynamic rule publisher
     *
     * @param configService config service
     * @return the dynamic rule publisher
     * @since 1.0.0
     */
    @Bean
    public DynamicRulePublisher<List<AuthorityRule>> authorityRulePublisher(ConfigService configService) {
        log.info("加载自定义处理器: {}", RuleNacosPublisher.class);
        return new RuleNacosPublisher<>(configService, JSON::toJSONString, NacosConfigUtil.AUTHORITY_DATA_ID_POSTFIX);
    }

    /**
     * Degrade rule provider dynamic rule provider
     *
     * @param configService config service
     * @return the dynamic rule provider
     * @since 1.0.0
     */
    @Bean
    public DynamicRuleProvider<List<DegradeRuleEntity>> degradeRuleProvider(ConfigService configService) {
        log.info("加载自定义处理器: {}", RuleNacosProvider.class);
        return new RuleNacosProvider<>(configService, s -> JSON.parseArray(s, DegradeRuleEntity.class), NacosConfigUtil.DEGRADE_DATA_ID_POSTFIX);
    }

    /**
     * Degrade rule publisher dynamic rule publisher
     *
     * @param configService config service
     * @return the dynamic rule publisher
     * @since 1.0.0
     */
    @Bean
    public DynamicRulePublisher<List<DegradeRuleEntity>> degradeRulePublisher(ConfigService configService) {
        log.info("加载自定义处理器: {}", RuleNacosPublisher.class);
        return new RuleNacosPublisher<>(configService, JSON::toJSONString, NacosConfigUtil.DEGRADE_DATA_ID_POSTFIX);
    }

    /**
     * Param rule provider dynamic rule provider
     *
     * @param configService config service
     * @return the dynamic rule provider
     * @since 1.0.0
     */
    @Bean
    public DynamicRuleProvider<List<ParamFlowRule>> paramRuleProvider(ConfigService configService) {
        log.info("加载自定义处理器: {}", RuleNacosProvider.class);
        return new RuleNacosProvider<>(configService, s -> JSON.parseArray(s, ParamFlowRule.class), NacosConfigUtil.PARAM_FLOW_DATA_ID_POSTFIX);
    }

    /**
     * Param rule publisher dynamic rule publisher
     *
     * @param configService config service
     * @return the dynamic rule publisher
     * @since 1.0.0
     */
    @Bean
    public DynamicRulePublisher<List<ParamFlowRule>> paramRulePublisher(ConfigService configService) {
        log.info("加载自定义处理器: {}", RuleNacosPublisher.class);
        return new RuleNacosPublisher<>(configService, JSON::toJSONString, NacosConfigUtil.PARAM_FLOW_DATA_ID_POSTFIX);
    }

    /**
     * System rule provider dynamic rule provider
     *
     * @param configService config service
     * @return the dynamic rule provider
     * @since 1.0.0
     */
    @Bean
    public DynamicRuleProvider<List<SystemRuleEntity>> systemRuleProvider(ConfigService configService) {
        log.info("加载自定义处理器: {}", RuleNacosProvider.class);
        return new RuleNacosProvider<>(configService, s -> JSON.parseArray(s, SystemRuleEntity.class), NacosConfigUtil.SYSTEM_FLOW_DATA_ID_POSTFIX);
    }

    /**
     * System rule publisher dynamic rule publisher
     *
     * @param configService config service
     * @return the dynamic rule publisher
     * @since 1.0.0
     */
    @Bean
    public DynamicRulePublisher<List<SystemRuleEntity>> systemRulePublisher(ConfigService configService) {
        log.info("加载自定义处理器: {}", RuleNacosPublisher.class);
        return new RuleNacosPublisher<>(configService, JSON::toJSONString, NacosConfigUtil.SYSTEM_FLOW_DATA_ID_POSTFIX);
    }
}
