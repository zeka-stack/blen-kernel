package dev.dong4j.zeka.kernel.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>系统层错误码标识注解.
 * <p>用于标识来源于系统内部技术层面的错误码，主要针对程序异常和环境问题产生的错误.
 * <p>主要功能：
 * <ul>
 *     <li>标识系统内部技术层面产生的错误码</li>
 *     <li>区分技术异常与业务逻辑错误</li>
 *     <li>为系统监控和故障诊断提供分类依据</li>
 *     <li>支持系统稳定性和性能监控</li>
 * </ul>
 * <p>适用场景：
 * <ul>
 *     <li>程序运行异常：如空指针、数组越界</li>
 *     <li>资源访问异常：如数据库连接失败、文件操作失败</li>
 *     <li>系统配置错误：如参数配置不正确、环境异常</li>
 *     <li>内部组件故障：如缓存失效、消息队列异常</li>
 * </ul>
 * <p>错误码分类体系：
 * <ul>
 *     <li>A: 业务层错误（用户操作、业务规则）</li>
 *     <li>B: 系统层错误（技术异常、环境问题）</li>
 *     <li>C: 第三方错误（外部服务、网络通信）</li>
 * </ul>
 * <p>处理策略：
 * <ul>
 *     <li>系统错误通常需要立即修复或回滚</li>
 *     <li>应该记录详细的错误日志和堆栈信息</li>
 *     <li>必要时进行系统降级或故障转移</li>
 *     <li>需要触发系统监控和报警机制</li>
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
public @interface SystemLevel {

}
