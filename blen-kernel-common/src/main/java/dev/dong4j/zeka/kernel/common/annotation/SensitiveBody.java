package dev.dong4j.zeka.kernel.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>响应体敏感数据脱敏注解.
 * <p>用于标识需要在响应输出时进行脱敏处理的敏感字段，保护用户隐私和数据安全.
 * <p>主要功能：
 * <ul>
 *     <li>标识响应数据中需要脱敏的敏感字段</li>
 *     <li>在数据输出前自动执行脱敏处理</li>
 *     <li>支持多种脱敏策略和规则</li>
 *     <li>保证日志和响应数据的安全性</li>
 * </ul>
 * <p>适用场景：
 * <ul>
 *     <li>用户信息输出：如手机号、身份证、邮箱地址</li>
 *     <li>金融数据展示：如银行卡号、支付密码</li>
 *     <li>商业信息输出：如合同金额、客户资料</li>
 *     <li>API 响应数据：如用户列表、详情接口</li>
 * </ul>
 * <p>脱敏策略：
 * <ul>
 *     <li>手机号：138****8888（保留前3位和后4位）</li>
 *     <li>身份证：110101******1234（保留前6位和后4位）</li>
 *     <li>邮箱：u***@gmail.com（保留用户名首字母和域名）</li>
 *     <li>银行卡：6222 **** **** 1234（保留前4位和后4位）</li>
 * </ul>
 * <p>使用方式：
 * <ul>
 *     <li>在需要脱敏的字段上添加此注解</li>
 *     <li>配合脱敏处理框架自动生效</li>
 *     <li>支持自定义脱敏规则和策略</li>
 *     <li>可与日志脱敏系统集成</li>
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
public @interface SensitiveBody {
}
