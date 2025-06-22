package dev.dong4j.zeka.kernel.common.base;

import dev.dong4j.zeka.kernel.common.base.command.ICommandDelegateService;
import dev.dong4j.zeka.kernel.common.base.query.IQueryDelegateService;

/**
 * <p>Description: 简化 dubbo client, api 层的 service 和 agent service 的基础查询操作 </p>
 *
 * @param <DTO> DTO 实体
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.11.23 20:31
 * @see CrudDelegateImpl
 * @see IRepositoryService
 * @since 1.7.0
 */
@SuppressWarnings("java:S119")
public interface ICrudDelegate<DTO> extends ICommandDelegateService<DTO>, IQueryDelegateService<DTO> {

}
