package dev.dong4j.zeka.kernel.common.support;

import dev.dong4j.zeka.kernel.common.api.IResultCode;
import dev.dong4j.zeka.kernel.common.asserts.Assertions;
import lombok.experimental.UtilityClass;

/**
 * 断言工具类扩展，添加IResultCode参数支持（已废弃）
 * <p>
 * 该类是对{@link Assertions}的扩展，主要添加了{@link IResultCode}参数支持
 * 允许在断言失败时抛出带有特定错误码的异常，提供更精确的错误信息
 * <p>
 * 使用示例：
 * <pre>{@code
 * AssertUtils.notEmpty(map, BaseCodes.DATA_ERROR);
 * AssertUtils.notNull(object, CustomCodes.PARAM_ERROR);
 * }</pre>
 * <p>
 * <b>废弃说明：</b>
 * 该类已被标记为废弃，推荐使用以下替代方案：
 * - 使用{@link Assertions}进行基础断言操作
 * - 使用ErrorCodes结合异常处理机制进行错误处理
 * - 使用Spring Boot的验证框架进行参数校验
 * <p>
 * <b>设计模式：</b>
 * - 工厂模式：提供静态方法创建断言操作
 * - 装饰器模式：对原有Assertions功能进行扩展
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2019.12.26 21:42
 * @since 1.0.0
 * @deprecated 请使用{@link Assertions}或ErrorCodes的方式代替，该类将在后续版本中移除
 */
@UtilityClass
@Deprecated
public class AssertUtils {

}
