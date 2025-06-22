package dev.dong4j.zeka.kernel.common.api;

import java.io.Serializable;

/**
 * <p>Description: 请求响应代码接口 </p>
 *
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.26 20:42
 * @since 1.0.0
 */
public interface IResultCode extends Serializable {

    /**
     * 获取返回消息, 可使用占位符
     *
     * @return String message
     * @since 1.0.0
     */
    String getMessage();

    /**
     * 获取返回状态码
     *
     * @return String code
     * @since 1.0.0
     */
    Integer getCode();

    /**
     * Name
     *
     * @return the string
     * @since 1.5.0
     */
    default String name() {
        return "FAILURE";
    }

}
