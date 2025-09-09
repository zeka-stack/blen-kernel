package dev.dong4j.zeka.kernel.common.base;

import java.io.Serializable;

/**
 * <p>Dubbo 客户端接口.
 * <p>为 Dubbo 分布式服务调用提供统一的客户端接口规范，封装了常用的 CRUD 操作.
 * <p>继承自 ICrudDelegate 接口，提供了完整的数据操作能力和分布式服务支持.
 * <p>主要功能：
 * <ul>
 *     <li>提供统一的 Dubbo 服务接口规范</li>
 *     <li>封装常用的 CRUD 操作方法</li>
 *     <li>支持数据传输对象的类型约束</li>
 *     <li>保证分布式服务调用的一致性</li>
 * </ul>
 * <p>设计优势：
 * <ul>
 *     <li>统一的服务接口设计和规范</li>
 *     <li>简化 Dubbo 服务的开发和维护</li>
 *     <li>支持微服务架构下的服务治理</li>
 *     <li>提供完整的类型安全保障</li>
 * </ul>
 * <p>使用场景：
 * <ul>
 *     <li>Dubbo 服务提供者的接口定义</li>
 *     <li>微服务之间的数据交互和操作</li>
 *     <li>分布式系统中的服务调用</li>
 *     <li>跨应用的数据操作和同步</li>
 * </ul>
 *
 * @param <D> 数据传输对象类型，必须继承 BaseDTO
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2021.10.31 14:51
 * @since 1.0.0
 */
public interface IDubboClient<D extends BaseDTO<? extends Serializable>> extends ICrudDelegate<D> {

}
