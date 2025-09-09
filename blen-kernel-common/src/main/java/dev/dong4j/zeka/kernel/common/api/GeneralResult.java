package dev.dong4j.zeka.kernel.common.api;

/**
 * <p>通用响应结果接口.
 * <p>为服务层、控制器层提供便捷的 API 响应构建方法.
 * <p>封装了常用的成功、失败、状态响应构建方法，简化业务代码编写.
 * <p>推荐在 Controller 或 Service 类中实现该接口，获得便捷的响应构建能力.
 * <p>使用示例：
 * <pre>{@code
 * @RestController
 * public class UserController implements GeneralResult {
 *     public Result<User> getUser(Long id) {
 *         User user = userService.findById(id);
 *         return user != null ? ok(user) : fail("用户不存在");
 *     }
 * }
 * }</pre>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.10.14 00:55
 * @since 1.0.0
 */
public interface GeneralResult {

    /**
     * <p>请求成功 (无数据).
     * <p>返回一个不包含数据内容的成功响应.
     *
     * @param <T> 返回数据类型
     * @return 成功的 Result 实例
     * @since 1.0.0
     */
    default <T> Result<T> ok() {
        return this.ok(null);
    }

    /**
     * <p>请求成功 (包含数据).
     * <p>返回一个包含指定数据内容的成功响应.
     *
     * @param <T>  返回数据类型
     * @param data 响应数据内容
     * @return 成功的 Result 实例
     * @since 1.0.0
     */
    default <T> Result<T> ok(T data) {
        return R.succeed(data);
    }

    /**
     * <p>请求失败 (自定义消息).
     * <p>返回一个包含自定义失败消息的失败响应.
     *
     * @param <T> 返回数据类型
     * @param msg 失败消息描述
     * @return 失败的 Result 实例
     * @since 1.0.0
     */
    default <T> Result<T> fail(String msg) {
        return R.failed(msg);
    }

    /**
     * <p>请求失败 (使用错误码枚举).
     * <p>返回一个基于错误码枚举的失败响应.
     *
     * @param <T>       返回数据类型
     * @param errorCode 错误码枚举，包含状态码和消息
     * @return 失败的 Result 实例
     * @since 1.0.0
     */
    default <T> Result<T> fail(IResultCode errorCode) {
        return R.failed(errorCode);
    }

    /**
     * <p>条件化状态响应 (默认失败消息).
     * <p>根据布尔值返回成功或失败响应，使用默认失败消息.
     *
     * @param flag 布尔条件值，true 返回成功，false 返回失败
     * @return 条件化的 Result 实例
     * @since 1.0.0
     */
    default Result<Boolean> status(boolean flag) {
        return this.status(flag, BaseCodes.FAILURE);
    }

    /**
     * <p>条件化状态响应 (使用结果码枚举).
     * <p>根据布尔值返回成功或指定的失败响应.
     *
     * @param flag       布尔条件值，true 返回成功，false 返回失败
     * @param resultCode 失败时使用的结果码枚举
     * @return 条件化的 Result 实例
     * @since 1.0.0
     */
    default Result<Boolean> status(boolean flag, IResultCode resultCode) {
        return R.status(flag, resultCode);
    }

    /**
     * <p>条件化状态响应 (自定义消息).
     * <p>根据布尔值返回成功或包含自定义消息的失败响应.
     *
     * @param flag    布尔条件值，true 返回成功，false 返回失败
     * @param message 失败时使用的自定义消息
     * @return 条件化的 Result 实例
     * @since 1.0.0
     */
    default Result<Boolean> status(boolean flag, String message) {
        return R.status(flag, message);
    }
}
