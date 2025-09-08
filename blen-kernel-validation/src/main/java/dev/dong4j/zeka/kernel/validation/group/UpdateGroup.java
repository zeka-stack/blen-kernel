package dev.dong4j.zeka.kernel.validation.group;

/**
 * 更新操作验证分组接口
 *
 * 用于标识在更新操作时需要验证的字段和规则
 * 在Bean Validation中作为分组标识，允许针对不同业务场景应用不同的验证规则
 *
 * 使用场景：
 * - 修改用户信息时的参数验证
 * - 更新记录时的必填字段检查
 * - 与创建操作区分的验证规则
 *
 * 使用示例：
 * {@code @NotNull(groups = UpdateGroup.class) private Long id;}
 * {@code @Valid private UpdateUserDto updateUser;}
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.26 20:42
 * @since 1.0.0
 */
public interface UpdateGroup {

}
