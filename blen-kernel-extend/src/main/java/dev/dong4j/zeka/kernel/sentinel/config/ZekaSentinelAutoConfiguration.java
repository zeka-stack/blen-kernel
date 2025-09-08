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
 * Sentinel自动配置类，提供Sentinel与Nacos集成的核心配置
 * <p>
 * 该类负责初始化Sentinel相关的动态规则提供者和发布者，实现规则的动态加载和持久化
 * 通过与Nacos配置中心集成，支持规则的实时更新和分布式一致性
 * </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2019.12.21 23:43
 * @since 1.0.0
 */
@Slf4j
@AutoConfiguration
@EnableConfigurationProperties(SentinelProperties.class)
public class ZekaSentinelAutoConfiguration {

    /**
     * 创建Nacos配置服务实例
     * <p>
     * 根据配置的Nacos服务器地址初始化配置服务，用于后续规则的读取和发布
     * </p>
     *
     * @param properties Sentinel配置属性，包含Nacos服务器地址等配置信息
     * @return Nacos配置服务实例
     * @throws Exception 配置服务初始化失败时抛出异常
     * @since 1.0.0
     */
    @Bean
    public ConfigService nacosConfigService(@NotNull SentinelProperties properties) throws Exception {
        return ConfigFactory.createConfigService(properties.getNacos().getServerAddr());
    }

    /**
     * 创建流量规则提供者
     * <p>
     * 负责从Nacos配置中心加载流量控制规则，将配置内容解析为FlowRuleEntity对象列表
     * </p>
     *
     * @param configService Nacos配置服务实例
     * @return 流量规则动态提供者，用于获取最新的流量控制规则
     * @since 1.0.0
     */
    @Bean
    public DynamicRuleProvider<List<FlowRuleEntity>> flowRuleProvider(ConfigService configService) {
        log.info("加载自定义处理器: {}", RuleNacosProvider.class);
        return new RuleNacosProvider<>(configService, s -> JSON.parseArray(s, FlowRuleEntity.class), NacosConfigUtil.FLOW_DATA_ID_POSTFIX);
    }

    /**
     * 创建流量规则发布者
     * <p>
     * 负责将流量控制规则发布到Nacos配置中心，实现规则的持久化和分布式同步
     * </p>
     *
     * @param configService Nacos配置服务实例
     * @return 流量规则动态发布者，用于发布更新后的流量控制规则
     * @since 1.0.0
     */
    @Bean
    public DynamicRulePublisher<List<FlowRuleEntity>> flowRulePublisher(ConfigService configService) {
        log.info("加载自定义处理器: {}", RuleNacosPublisher.class);
        return new RuleNacosPublisher<>(configService, JSON::toJSONString, NacosConfigUtil.FLOW_DATA_ID_POSTFIX);
    }

    /**
     * 创建网关流量规则提供者
     * <p>
     * 负责从Nacos配置中心加载网关流量控制规则，将配置内容解析为GatewayFlowRuleEntity对象列表
     * </p>
     *
     * @param configService Nacos配置服务实例
     * @return 网关流量规则动态提供者，用于获取最新的网关流量控制规则
     * @since 1.0.0
     */
    @Bean
    public DynamicRuleProvider<List<GatewayFlowRuleEntity>> gatewayFlowRuleProvider(ConfigService configService) {
        log.info("加载自定义处理器: {}", RuleNacosProvider.class);
        return new RuleNacosProvider<>(configService, s -> JSON.parseArray(s, GatewayFlowRuleEntity.class), NacosConfigUtil.GATEWAY_FLOW_DATA_ID_POSTFIX);
    }

    /**
     * 创建网关流量规则发布者
     * <p>
     * 负责将网关流量控制规则发布到Nacos配置中心，实现规则的持久化和分布式同步
     * </p>
     *
     * @param configService Nacos配置服务实例
     * @return 网关流量规则动态发布者，用于发布更新后的网关流量控制规则
     * @since 1.0.0
     */
    @Bean
    public DynamicRulePublisher<List<GatewayFlowRuleEntity>> gatewayFlowRulePublisher(ConfigService configService) {
        log.info("加载自定义处理器: {}", RuleNacosPublisher.class);
        return new RuleNacosPublisher<>(configService, JSON::toJSONString, NacosConfigUtil.GATEWAY_FLOW_DATA_ID_POSTFIX);
    }

    /**
     * 创建授权规则提供者
     * <p>
     * 负责从Nacos配置中心加载授权规则，将配置内容解析为AuthorityRule对象列表
     * </p>
     *
     * @param configService Nacos配置服务实例
     * @return 授权规则动态提供者，用于获取最新的授权规则
     * @since 1.0.0
     */
    @Bean
    public DynamicRuleProvider<List<AuthorityRule>> authorityRuleProvider(ConfigService configService) {
        log.info("加载自定义处理器: {}", RuleNacosProvider.class);
        return new RuleNacosProvider<>(configService, s -> JSON.parseArray(s, AuthorityRule.class), NacosConfigUtil.AUTHORITY_DATA_ID_POSTFIX);
    }

    /**
     * 创建授权规则发布者
     * <p>
     * 负责将授权规则发布到Nacos配置中心，实现规则的持久化和分布式同步
     * </p>
     *
     * @param configService Nacos配置服务实例
     * @return 授权规则动态发布者，用于发布更新后的授权规则
     * @since 1.0.0
     */
    @Bean
    public DynamicRulePublisher<List<AuthorityRule>> authorityRulePublisher(ConfigService configService) {
        log.info("加载自定义处理器: {}", RuleNacosPublisher.class);
        return new RuleNacosPublisher<>(configService, JSON::toJSONString, NacosConfigUtil.AUTHORITY_DATA_ID_POSTFIX);
    }

    /**
     * 创建降级规则提供者
     * <p>
     * 负责从Nacos配置中心加载熔断降级规则，将配置内容解析为DegradeRuleEntity对象列表
     * </p>
     *
     * @param configService Nacos配置服务实例
     * @return 降级规则动态提供者，用于获取最新的熔断降级规则
     * @since 1.0.0
     */
    @Bean
    public DynamicRuleProvider<List<DegradeRuleEntity>> degradeRuleProvider(ConfigService configService) {
        log.info("加载自定义处理器: {}", RuleNacosProvider.class);
        return new RuleNacosProvider<>(configService, s -> JSON.parseArray(s, DegradeRuleEntity.class), NacosConfigUtil.DEGRADE_DATA_ID_POSTFIX);
    }

    /**
     * 创建降级规则发布者
     * <p>
     * 负责将熔断降级规则发布到Nacos配置中心，实现规则的持久化和分布式同步
     * </p>
     *
     * @param configService Nacos配置服务实例
     * @return 降级规则动态发布者，用于发布更新后的熔断降级规则
     * @since 1.0.0
     */
    @Bean
    public DynamicRulePublisher<List<DegradeRuleEntity>> degradeRulePublisher(ConfigService configService) {
        log.info("加载自定义处理器: {}", RuleNacosPublisher.class);
        return new RuleNacosPublisher<>(configService, JSON::toJSONString, NacosConfigUtil.DEGRADE_DATA_ID_POSTFIX);
    }

    /**
     * 创建热点参数规则提供者
     * <p>
     * 负责从Nacos配置中心加载热点参数规则，将配置内容解析为ParamFlowRule对象列表
     * </p>
     *
     * @param configService Nacos配置服务实例
     * @return 热点参数规则动态提供者，用于获取最新的热点参数规则
     * @since 1.0.0
     */
    @Bean
    public DynamicRuleProvider<List<ParamFlowRule>> paramRuleProvider(ConfigService configService) {
        log.info("加载自定义处理器: {}", RuleNacosProvider.class);
        return new RuleNacosProvider<>(configService, s -> JSON.parseArray(s, ParamFlowRule.class), NacosConfigUtil.PARAM_FLOW_DATA_ID_POSTFIX);
    }

    /**
     * 创建热点参数规则发布者
     * <p>
     * 负责将热点参数规则发布到Nacos配置中心，实现规则的持久化和分布式同步
     * </p>
     *
     * @param configService Nacos配置服务实例
     * @return 热点参数规则动态发布者，用于发布更新后的热点参数规则
     * @since 1.0.0
     */
    @Bean
    public DynamicRulePublisher<List<ParamFlowRule>> paramRulePublisher(ConfigService configService) {
        log.info("加载自定义处理器: {}", RuleNacosPublisher.class);
        return new RuleNacosPublisher<>(configService, JSON::toJSONString, NacosConfigUtil.PARAM_FLOW_DATA_ID_POSTFIX);
    }

    /**
     * 创建系统规则提供者
     * <p>
     * 负责从Nacos配置中心加载系统保护规则，将配置内容解析为SystemRuleEntity对象列表
     * </p>
     *
     * @param configService Nacos配置服务实例
     * @return 系统规则动态提供者，用于获取最新的系统保护规则
     * @since 1.0.0
     */
    @Bean
    public DynamicRuleProvider<List<SystemRuleEntity>> systemRuleProvider(ConfigService configService) {
        log.info("加载自定义处理器: {}", RuleNacosProvider.class);
        return new RuleNacosProvider<>(configService, s -> JSON.parseArray(s, SystemRuleEntity.class), NacosConfigUtil.SYSTEM_FLOW_DATA_ID_POSTFIX);
    }

    /**
     * 创建系统规则发布者
     * <p>
     * 负责将系统保护规则发布到Nacos配置中心，实现规则的持久化和分布式同步
     * </p>
     *
     * @param configService Nacos配置服务实例
     * @return 系统规则动态发布者，用于发布更新后的系统保护规则
     * @since 1.0.0
     */
    @Bean
    public DynamicRulePublisher<List<SystemRuleEntity>> systemRulePublisher(ConfigService configService) {
        log.info("加载自定义处理器: {}", RuleNacosPublisher.class);
        return new RuleNacosPublisher<>(configService, JSON::toJSONString, NacosConfigUtil.SYSTEM_FLOW_DATA_ID_POSTFIX);
    }
}
