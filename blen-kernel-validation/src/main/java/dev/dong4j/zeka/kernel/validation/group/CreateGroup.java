package dev.dong4j.zeka.kernel.validation.group;

/**
 * 创建操作验证分组接口
 *
 * 用于标识在创建操作时需要验证的字段和规则
 * 在Bean Validation中作为分组标识，允许针对不同业务场景应用不同的验证规则
 *
 * 使用场景：
 * - 新增用户时的参数验证
 * - 创建记录时的必填字段检查
 * - 与更新操作区分的验证规则
 *
 * 使用示例：
 * {@code @NotBlank(groups = CreateGroup.class) private String name;}
 * {@code @Valid private CreateUserDto createUser;}
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.27 14:56
 * @since 1.0.0
 */
public interface CreateGroup {

}
