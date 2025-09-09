package dev.dong4j.zeka.kernel.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>业务层错误码标识注解.
 * <p>用于标识来源于业务逻辑层的错误码，主要针对用户操作和业务规则验证产生的错误.
 * <p>主要功能：
 * <ul>
 *     <li>标识业务逻辑层产生的错误码类型</li>
 *     <li>区分用户操作错误与系统内部错误</li>
 *     <li>为错误处理和日志记录提供分类依据</li>
 *     <li>支持错误码的自动化管理和统计</li>
 * </ul>
 * <p>适用场景：
 * <ul>
 *     <li>参数校验失败：如必填参数为空、格式不正确</li>
 *     <li>业务规则违反：如余额不足、权限不够</li>
 *     <li>数据状态异常：如订单状态不允许操作</li>
 *     <li>业务逻辑冲突：如重复提交、操作时序错误</li>
 * </ul>
 * <p>错误码分类体系：
 * <ul>
 *     <li>A: 业务层错误（用户操作、业务规则）</li>
 *     <li>B: 系统层错误（技术异常、环境问题）</li>
 *     <li>C: 第三方错误（外部服务、网络通信）</li>
 * </ul>
 * <p>使用方式：
 * <ul>
 *     <li>在错误码常量字段上添加此注解</li>
 *     <li>配合错误码管理框架自动分类</li>
 *     <li>支持错误监控和统计分析</li>
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
public @interface BusinessLevel {

}
