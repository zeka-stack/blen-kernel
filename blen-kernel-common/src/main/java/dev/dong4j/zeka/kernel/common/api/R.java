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
 * <p>Description: 将标准返回的字段全部放在了 {@link Result} </p>
 *
 * @param <T> parameter
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.02.06 19:58
 * @see Result
 * @since 1.0.0
 */
@SuppressWarnings({"PMD.ClassNamingShouldBeCamelRule"})
public final class R<T> extends Result<T> {
    /** serialVersionUID */
    @Serial
    private static final long serialVersionUID = 3077918845714343375L;

    /**
     * R
     *
     * @param code    code
     * @param message message
     * @param data    data
     * @since 1.0.0
     */
    @Contract(pure = true)
    private R(Integer code, String message, T data) {
        super(code, message, data, Trace.context().get());
    }

    /**
     * Succeed result
     *
     * @param <T> parameter
     * @return the result
     * @since 1.0.0
     */
    @Contract(pure = true)
    @NotNull
    @SuppressWarnings("unchecked")
    public static <T> Result<T> succeed() {
        return succeed((T) Collections.emptyMap());
    }

    /**
     * Succeed result
     *
     * @param <T>  parameter
     * @param data data
     * @return the result
     * @since 1.0.0
     */
    @Contract(pure = true)
    @NotNull
    public static <T> Result<T> succeed(T data) {
        return succeed(SUCCESS_CODE, SUCCESS_MESSAGE, data);
    }

    /**
     * Succeed result
     *
     * @param <T>  parameter
     * @param code code
     * @param msg  msg
     * @param data data
     * @return the result
     * @since 1.0.0
     */
    @Contract(pure = true)
    @NotNull
    public static <T> Result<T> succeed(Integer code, String msg, T data) {
        return build(code, msg, data);
    }

    /**
     * Failed result
     *
     * @param <T> parameter
     * @return the result
     * @since 1.0.0
     */
    @Contract(pure = true)
    @NotNull
    public static <T> Result<T> failed() {
        return failed(FAILURE_MESSAGE);
    }

    /**
     * Failed result
     *
     * @param <T> parameter
     * @param msg msg
     * @return the result
     * @since 1.0.0
     */
    @Contract(pure = true)
    @NotNull
    public static <T> Result<T> failed(String msg) {
        return failed(FAILURE_CODE, msg);
    }

    /**
     * Failed result
     *
     * @param <T>  parameter
     * @param code code
     * @param msg  msg
     * @return the result
     * @since 1.0.0
     */
    @NotNull
    @Contract(pure = true)
    @SuppressWarnings("unchecked")
    public static <T> Result<T> failed(Integer code, String msg) {
        return failed(code, msg, (T) Collections.emptyMap());
    }

    /**
     * Failed result
     *
     * @param <T>        parameter
     * @param resultCode result code
     * @return the result
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
     * Status result
     *
     * @param <T>        parameter
     * @param expression expression
     * @return the result
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
     * Values result
     *
     * @param args 键值对一一对应
     * @return the result
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
