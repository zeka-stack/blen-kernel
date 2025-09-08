package dev.dong4j.zeka.kernel.validation.group;

/**
 * 删除操作验证分组接口
 *
 * 用于标识在删除操作时需要验证的字段和规则
 * 在Bean Validation中作为分组标识，允许针对不同业务场景应用不同的验证规则
 *
 * 使用场景：
 * - 删除用户时的ID验证
 * - 批量删除操作的参数检查
 * - 删除前的权限和关联数据验证
 *
 * 使用示例：
 * {@code @NotNull(groups = DeleteGroup.class) private Long id;}
 * {@code @Valid private DeleteDto deleteDto;}
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.26 20:42
 * @since 1.0.0
 */
public interface DeleteGroup {

}
