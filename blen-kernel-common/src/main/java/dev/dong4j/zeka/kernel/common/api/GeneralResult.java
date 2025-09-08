package dev.dong4j.zeka.kernel.common.api;

/**
 * <p>Description:  </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.10.14 00:55
 * @since 1.0.0
 */
public interface GeneralResult {

    /**
     * 请求成功
     *
     * @param <T> parameter
     * @return the result
     * @since 1.0.0
     */
    default <T> Result<T> ok() {
        return this.ok(null);
    }

    /**
     * 请求成功
     *
     * @param <T>  对象泛型
     * @param data 数据内容
     * @return the result
     * @since 1.0.0
     */
    default <T> Result<T> ok(T data) {
        return R.succeed(data);
    }

    /**
     * 请求失败
     *
     * @param <T> parameter
     * @param msg 提示内容
     * @return the result
     * @since 1.0.0
     */
    default <T> Result<T> fail(String msg) {
        return R.failed(msg);
    }

    /**
     * 请求失败
     *
     * @param <T>       parameter
     * @param errorCode 请求错误码
     * @return the result
     * @since 1.0.0
     */
    default <T> Result<T> fail(IResultCode errorCode) {
        return R.failed(errorCode);
    }

    /**
     * Status result.
     *
     * @param flag the flag
     * @return the result
     * @since 1.0.0
     */
    default Result<Boolean> status(boolean flag) {
        return this.status(flag, BaseCodes.FAILURE);
    }

    /**
     * Status result
     *
     * @param flag       flag
     * @param resultCode result code
     * @return the result
     * @since 1.0.0
     */
    default Result<Boolean> status(boolean flag, IResultCode resultCode) {
        return R.status(flag, resultCode);
    }

    /**
     * Status result
     *
     * @param flag    flag
     * @param message message
     * @return the result
     * @since 1.0.0
     */
    default Result<Boolean> status(boolean flag, String message) {
        return R.status(flag, message);
    }
}
