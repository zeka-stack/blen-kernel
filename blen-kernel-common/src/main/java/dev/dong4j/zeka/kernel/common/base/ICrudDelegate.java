package dev.dong4j.zeka.kernel.common.base;

import dev.dong4j.zeka.kernel.common.base.command.ICommandDelegateService;
import dev.dong4j.zeka.kernel.common.base.query.IQueryDelegateService;

/**
 * <p>CRUD 委托服务接口.
 * <p>为 Dubbo Client、API 层服务和 Agent 服务提供统一的基础数据操作接口.
 * <p>主要功能：
 * <ul>
 *     <li>整合命令模式和查询模式的所有数据操作</li>
 *     <li>简化微服务架构中的数据访问层调用</li>
 *     <li>提供统一的 CRUD 操作接口规范</li>
 *     <li>支持分布式服务间的数据操作标准化</li>
 * </ul>
 * <p>接口组合：
 * <ul>
 *     <li>继承 ICommandDelegateService：提供数据修改操作（增删改）</li>
 *     <li>继承 IQueryDelegateService：提供数据查询操作（查询）</li>
 *     <li>实现完整的 CRUD 操作能力</li>
 *     <li>支持事务和批量操作</li>
 * </ul>
 * <p>设计优势：
 * <ul>
 *     <li>接口组合模式，职责分离清晰</li>
 *     <li>简化分布式服务的数据操作复杂度</li>
 *     <li>提供统一的类型约束和泛型支持</li>
 *     <li>便于 Mock 测试和接口扩展</li>
 * </ul>
 * <p>适用场景：
 * <ul>
 *     <li>Dubbo 微服务的客户端接口定义</li>
 *     <li>API 网关层的服务接口规范</li>
 *     <li>Agent 服务的基础操作接口</li>
 *     <li>分布式系统的数据访问标准化</li>
 * </ul>
 *
 * @param <DTO> 数据传输对象类型
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.11.23 20:31
 * @see CrudDelegateImpl
 * @see IRepositoryService
 * @since 1.0.0
 */
@SuppressWarnings("java:S119")
public interface ICrudDelegate<DTO> extends ICommandDelegateService<DTO>, IQueryDelegateService<DTO> {

}
