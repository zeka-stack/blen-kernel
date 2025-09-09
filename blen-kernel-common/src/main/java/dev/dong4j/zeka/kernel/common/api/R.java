package dev.dong4j.zeka.kernel.common.api;

import dev.dong4j.zeka.kernel.common.context.Trace;
import dev.dong4j.zeka.kernel.common.support.ChainMap;
import dev.dong4j.zeka.kernel.common.util.CollectionUtils;
import dev.dong4j.zeka.kernel.common.util.StringUtils;
import java.io.Serial;
import java.util.Collections;
import java.util.Map;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * <p>轻量级静态工厂类，用于快速构建 API 返回结果对象.
 * <p>提供静态方法快速创建成功和失败的 {@link Result} 实例，简化 API 开发和响应处理.
 * <p>所有方法都返回标准化的 {@link Result} 对象，保证 API 响应的一致性.
 * <p>主要特性：
 * <ul>
 *     <li>静态工厂方法，无需实例化</li>
 *     <li>让通不同成功和失败场景</li>
 *     <li>支持自定义状态码和消息</li>
 *     <li>自动添加运踪标识 (traceId)</li>
 *     <li>支持链式调用和灵活组合</li>
 * </ul>
 * <p>适用场景：
 * <ul>
 *     <li>REST API 接口响应</li>
 *     <li>内部服务调用结果封装</li>
 *     <li>业务逻辑处理结果返回</li>
 *     <li>统一错误处理和响应</li>
 * </ul>
 *
 * @param <T> 返回数据的类型参数
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.02.06 19:58
 * @see Result
 * @since 1.0.0
 */
@SuppressWarnings({"PMD.ClassNamingShouldBeCamelRule"})
public final class R<T> extends Result<T> {
    /** 序列化版本号 */
    @Serial
    private static final long serialVersionUID = 3077918845714343375L;

    /**
     * <p>私有构造方法.
     * <p>创建一个新的 Result 实例，自动添加运踪标识.
     *
     * @param code    状态码
     * @param message 响应消息
     * @param data    响应数据
     * @since 1.0.0
     */
    @Contract(pure = true)
    private R(Integer code, String message, T data) {
        super(code, message, data, Trace.context().get());
    }

    /**
     * <p>创建成功响应结果 (无数据).
     * <p>返回一个空的 Map 作为数据内容.
     *
     * @param <T> 返回数据类型
     * @return 成功的 Result 实例
     * @since 1.0.0
     */
    @Contract(pure = true)
    @NotNull
    @SuppressWarnings("unchecked")
    public static <T> Result<T> succeed() {
        return succeed((T) Collections.emptyMap());
    }

    /**
     * <p>创建成功响应结果 (包含数据).
     * <p>使用默认的成功状态码和消息.
     *
     * @param <T>  返回数据类型
     * @param data 响应数据内容
     * @return 成功的 Result 实例
     * @since 1.0.0
     */
    @Contract(pure = true)
    @NotNull
    public static <T> Result<T> succeed(T data) {
        return succeed(SUCCESS_CODE, SUCCESS_MESSAGE, data);
    }

    /**
     * <p>创建成功响应结果 (自定义参数).
     * <p>允许指定自定义的状态码、消息和数据.
     *
     * @param <T>  返回数据类型
     * @param code 自定义状态码
     * @param msg  自定义响应消息
     * @param data 响应数据内容
     * @return 成功的 Result 实例
     * @since 1.0.0
     */
    @Contract(pure = true)
    @NotNull
    public static <T> Result<T> succeed(Integer code, String msg, T data) {
        return build(code, msg, data);
    }

    /**
     * <p>创建失败响应结果 (默认消息).
     * <p>使用默认的失败状态码和消息.
     *
     * @param <T> 返回数据类型
     * @return 失败的 Result 实例
     * @since 1.0.0
     */
    @Contract(pure = true)
    @NotNull
    public static <T> Result<T> failed() {
        return failed(FAILURE_MESSAGE);
    }

    /**
     * <p>创建失败响应结果 (自定义消息).
     * <p>使用默认的失败状态码，但允许指定自定义消息.
     *
     * @param <T> 返回数据类型
     * @param msg 自定义失败消息
     * @return 失败的 Result 实例
     * @since 1.0.0
     */
    @Contract(pure = true)
    @NotNull
    public static <T> Result<T> failed(String msg) {
        return failed(FAILURE_CODE, msg);
    }

    /**
     * <p>创建失败响应结果 (自定义状态码和消息).
     * <p>允许指定自定义的失败状态码和消息，返回空数据.
     *
     * @param <T>  返回数据类型
     * @param code 自定义失败状态码
     * @param msg  自定义失败消息
     * @return 失败的 Result 实例
     * @since 1.0.0
     */
    @NotNull
    @Contract(pure = true)
    @SuppressWarnings("unchecked")
    public static <T> Result<T> failed(Integer code, String msg) {
        return failed(code, msg, (T) Collections.emptyMap());
    }

    /**
     * <p>创建失败响应结果 (使用结果码枚举).
     * <p>使用预定义的 IResultCode 枚举来创建失败响应.
     *
     * @param <T>        返回数据类型
     * @param resultCode 结果码枚举，包含状态码和消息
     * @return 失败的 Result 实例
     * @since 1.0.0
     */
    @NotNull
    public static <T> Result<T> failed(@NotNull IResultCode resultCode) {
        return failed(resultCode.getCode(), resultCode.getMessage());
    }

    /**
     * Failed result
     *
     * @param <T>        parameter
     * @param resultCode result code
     * @param msg        msg
     * @return the result
     * @since 1.0.0
     */
    @NotNull
    public static <T> Result<T> failed(@NotNull IResultCode resultCode, String msg) {
        return failed(resultCode.getCode(), StringUtils.format(resultCode.getMessage(), msg));
    }

    /**
     * Failed result
     *
     * @param <T>        parameter
     * @param resultCode result code
     * @param msg        msg
     * @param data       data
     * @return the result
     * @since 1.0.0
     */
    @NotNull
    @Contract(value = "_, _, _ -> new", pure = true)
    public static <T> Result<T> failed(@NotNull IResultCode resultCode, String msg, T data) {
        return failed(resultCode.getCode(), StringUtils.format(resultCode.getMessage(), msg), data);
    }

    /**
     * Failed result
     *
     * @param <T>  parameter
     * @param code code
     * @param msg  msg
     * @param data data
     * @return the result
     * @since 1.0.0
     */
    @NotNull
    @Contract(value = "_, _, _ -> new", pure = true)
    public static <T> Result<T> failed(Integer code, String msg, T data) {
        return build(code, msg, data);
    }

    /**
     * Build result
     *
     * @param <T>  parameter
     * @param code code
     * @param msg  msg
     * @return the result
     * @since 1.0.0
     */
    @Contract("_, _ -> new")
    @NotNull
    @SuppressWarnings("unchecked")
    public static <T> Result<T> build(Integer code, String msg) {
        return build(code, msg, (T) Collections.emptyMap());
    }

    /**
     * Build result
     *
     * @param <T>  parameter
     * @param code code
     * @param msg  msg
     * @param data data
     * @return the result
     * @since 1.0.0
     */
    @Contract(value = "_, _, _ -> new", pure = true)
    @NotNull
    public static <T> Result<T> build(Integer code, String msg, T data) {
        return new R<>(code, msg, data);
    }

    /**
     * <p>创建条件化响应结果.
     * <p>根据布尔表达式的结果返回成功或失败的响应.
     *
     * @param <T>        返回数据类型
     * @param expression 布尔表达式，true 返回成功，false 返回失败
     * @return Result 实例
     * @since 1.0.0
     */
    @Contract("_ -> !null")
    public static <T> Result<T> status(boolean expression) {
        return status(expression, "");
    }

    /**
     * Status result
     *
     * @param <T>        parameter
     * @param expression expression
     * @param resultCode result code
     * @return the result
     * @since 1.0.0
     */
    @Contract("_, _ -> !null")
    public static <T> Result<T> status(boolean expression, @NotNull IResultCode resultCode) {
        return status(expression, resultCode.getMessage());
    }

    /**
     * Status result
     *
     * @param <T>        parameter
     * @param expression expression
     * @param message    message
     * @return the result
     * @since 1.0.0
     */
    @Contract("_, _ -> !null")
    public static <T> Result<T> status(boolean expression, String message) {
        return expression ? succeed() : failed(message);
    }

    /**
     * <p>创建键值对数据响应结果.
     * <p>将可变参数转换为 Map 对象并封装为成功响应.
     *
     * @param args 键值对参数，应该成对出现 (key1, value1, key2, value2, ...)
     * @return 包含 Map 数据的成功 Result 实例
     * @since 1.0.0
     */
    @NotNull
    public static Result<Map<String, Object>> values(@NotNull Object... args) {
        return succeed(CollectionUtils.toMap(args));
    }

    /**
     * Map map
     *
     * @param <T>  parameter
     * @param data data
     * @return the map
     * @since 1.0.0
     */
    @NotNull
    public static <T> Map<String, Object> map(T data) {
        return map(Result.SUCCESS_CODE,
            true,
            data,
            BaseCodes.SUCCESS.getMessage(),
            Trace.context().get());
    }

    /**
     * Map map
     *
     * @param <T>     parameter
     * @param code    code
     * @param success success
     * @param data    data
     * @param message message
     * @param traceId trace id
     * @return the map
     * @since 1.0.0
     */
    @NotNull
    public static <T> Map<String, Object> map(Integer code,
                                              boolean success,
                                              T data,
                                              String message,
                                              String traceId) {

        return ChainMap.build(5)
            .put(CODE, code)
            .put(SUCCESS, success)
            .put(DATA, data)
            .put(MESSAGE, message)
            .put(TRACE_ID, traceId);

    }

    /**
     * Fail map map
     *
     * @param resultCode result code
     * @return the map
     * @since 1.0.0
     */
    @NotNull
    public static Map<String, Object> failMap(@NotNull IResultCode resultCode) {
        return map(resultCode.getCode(), false, null, resultCode.getMessage(), Trace.context().get());
    }

    /**
     * Map map
     *
     * @param <T>        parameter
     * @param resultCode result code
     * @param data       data
     * @return the map
     * @since 1.0.0
     */
    @NotNull
    public static <T> Map<String, Object> map(@NotNull IResultCode resultCode, T data) {
        return map(resultCode.getCode(),
            BaseCodes.SUCCESS.getCode().equals(resultCode.getCode()),
            data, resultCode.getMessage(), Trace.context().get());
    }
}
