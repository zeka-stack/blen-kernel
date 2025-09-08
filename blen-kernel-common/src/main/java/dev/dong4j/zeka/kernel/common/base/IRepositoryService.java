package dev.dong4j.zeka.kernel.common.base;

/**
 * <p>Description: 作为 service 层与 repository 层的桥阶层, 具有 CURD 接口, 接口申明与 ExchangeService 保持一致 </p>
 *
 * @param <DTO> DTO 实体
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.11.23 21:16
 * @since 1.0.0
 */
@SuppressWarnings("java:S119")
public interface IRepositoryService<DTO> extends ICrudDelegate<DTO> {

}
