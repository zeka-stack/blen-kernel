package dev.dong4j.zeka.kernel.validation.group;

/**
 * 查询操作验证分组接口
 *
 * 用于标识在查询操作时需要验证的字段和规则
 * 在Bean Validation中作为分组标识，允许针对不同业务场景应用不同的验证规则
 *
 * 使用场景：
 * - 查询用户信息时的参数验证
 * - 分页查询的参数检查
 * - 搜索条件的格式验证
 *
 * 使用示例：
 * {@code @Min(value = 1, groups = GetGroup.class) private Integer pageNo;}
 * {@code @Valid private QueryDto queryDto;}
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.26 20:43
 * @since 1.0.0
 */
public interface GetGroup {

}
