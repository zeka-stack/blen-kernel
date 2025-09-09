package dev.dong4j.zeka.kernel.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>字段级别敏感数据脱敏注解.
 * <p>用于标识实体类中包含敏感信息的具体字段，提供更精细化的脱敏控制和安全管理.
 * <p>主要功能：
 * <ul>
 *     <li>精确标识实体中的敏感数据字段</li>
 *     <li>支持字段级别的脱敏规则配置</li>
 *     <li>与存储和序列化框架集成</li>
 *     <li>提供字段级别的访问控制和审计</li>
 * </ul>
 * <p>适用场景：
 * <ul>
 *     <li>用户实体字段：如 User.phone、User.idCard、User.email</li>
 *     <li>订单实体字段：如 Order.amount、Order.address</li>
 *     <li>支付实体字段：如 Payment.bankCard、Payment.password</li>
 *     <li>日志实体字段：如 Log.userInfo、Log.requestBody</li>
 * </ul>
 * <p>与 SensitiveBody 的区别：
 * <ul>
 *     <li>SensitiveField：针对实体具体字段，精确控制</li>
 *     <li>SensitiveBody：针对整个响应体，批量处理</li>
 *     <li>字段级别优先级更高，可覆盖全局规则</li>
 *     <li>支持更细粒度的脱敏策略配置</li>
 * </ul>
 * <p>安全特性：
 * <ul>
 *     <li>数据库存储时自动加密或脱敏</li>
 *     <li>JSON 序列化时自动处理敏感字段</li>
 *     <li>日志输出时自动过滤敏感信息</li>
 *     <li>支持角色权限和数据授权控制</li>
 * </ul>
 * <p>最佳实践：
 * <ul>
 *     <li>结合数据分类标准使用此注解</li>
 *     <li>与数据治理和合规要求对齐</li>
 *     <li>定期审计和更新脱敏规则</li>
 * </ul>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.05.12 15:18
 * @since 1.0.0
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SensitiveField {
}
