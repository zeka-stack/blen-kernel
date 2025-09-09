package dev.dong4j.zeka.kernel.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>第三方服务错误码标识注解.
 * <p>用于标识来源于第三方服务调用的错误码，主要针对外部系统集成和网络通信产生的错误.
 * <p>主要功能：
 * <ul>
 *     <li>标识第三方服务调用产生的错误码</li>
 *     <li>区分外部依赖错误与内部系统错误</li>
 *     <li>为第三方服务监控和管理提供依据</li>
 *     <li>支持服务降级和熊断机制</li>
 * </ul>
 * <p>适用场景：
 * <ul>
 *     <li>消息通知服务：如短信、邮件、推送服务异常</li>
 *     <li>支付服务集成：如支付宝、微信支付接口异常</li>
 *     <li>第三方 API 调用：如地图服务、天气接口等</li>
 *     <li>云服务集成：如 OSS、CDN、第三方数据库等</li>
 * </ul>
 * <p>错误码分类体系：
 * <ul>
 *     <li>A: 业务层错误（用户操作、业务规则）</li>
 *     <li>B: 系统层错误（技术异常、环境问题）</li>
 *     <li>C: 第三方错误（外部服务、网络通信）</li>
 * </ul>
 * <p>处理策略：
 * <ul>
 *     <li>实现重试机制和超时控制</li>
 *     <li>提供降级方案和默认值处理</li>
 *     <li>记录第三方服务调用日志和性能指标</li>
 *     <li>配置熊断器防止雪崩效应</li>
 * </ul>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.06.10 16:09
 * @since 1.0.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ThirdLevel {
}
