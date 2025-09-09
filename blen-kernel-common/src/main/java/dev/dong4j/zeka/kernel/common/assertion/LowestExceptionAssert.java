package dev.dong4j.zeka.kernel.common.assertion;

import dev.dong4j.zeka.kernel.common.api.IResultCode;
import dev.dong4j.zeka.kernel.common.exception.LowestException;
import java.io.Serial;

/**
 * <p>最底层全局异常断言接口.
 * <p>结合 IResultCode 和 IAssert 接口，为枚举类提供完整的异常断言能力.
 * <p>通过实现该接口，枚举常量可以直接作为断言对象使用，简化错误处理代码.
 * <p>基于 LowestException 实现异常创建，保证异常信息的一致性和可追溯性.
 * <p>使用示例：
 * <pre>{@code
 * public enum BusinessCodes implements LowestExceptionAssert {
 *     USER_NOT_FOUND(4001, "用户不存在"),
 *     INVALID_PARAMETER(4002, "参数无效: {}");
 *
 *     // 在业务代码中直接使用
 *     BusinessCodes.USER_NOT_FOUND.notNull(user, userId);
 *     BusinessCodes.INVALID_PARAMETER.isTrue(age > 0, age);
 * }
 * }</pre>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2023.11.11 12:20
 * @since 1.0.0
 */
public interface LowestExceptionAssert extends IResultCode, IAssert {
    /** 序列化版本号 */
    @Serial
    long serialVersionUID = 3077918845714343375L;

    /**
     * <p>创建新的最底层异常实例.
     * <p>基于当前的错误码和参数创建 LowestException 实例.
     *
     * @param args 可变参数，用于格式化错误消息
     * @return 创建的 LowestException 实例
     * @since 1.0.0
     */
    @Override
    default LowestException newException(Object... args) {
        return new LowestException(this, args, this.getMessage());
    }

    /**
     * <p>创建包装原始异常的最底层异常实例.
     * <p>将原始异常作为因果链，创建包含上下文信息的 LowestException 实例.
     *
     * @param t    原始异常，作为异常因果链
     * @param args 可变参数，用于格式化错误消息
     * @return 包装后的 LowestException 实例
     * @since 1.0.0
     */
    @Override
    default LowestException newException(Throwable t, Object... args) {
        return new LowestException(this, args, this.getMessage(), t);
    }

}
