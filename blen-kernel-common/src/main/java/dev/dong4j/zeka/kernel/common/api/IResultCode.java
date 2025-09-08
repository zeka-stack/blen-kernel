package dev.dong4j.zeka.kernel.common.api;

import java.io.Serializable;

/**
 * 请求响应代码接口，定义API返回结果的统一结构
 * <p>
 * 该接口定义了所有API返回结果中需要包含的基本信息：状态码和消息
 * 作为统一响应码的基础接口，保证所有返回结果的一致性
 * <p>
 * 主要功能：
 * - 定义统一的状态码获取方法
 * - 定义统一的消息获取方法
 * - 支持名称标识和序列化
 * - 为枚举实现提供基础规范
 * <p>
 * 实现类通常为枚举类型，如BaseCodes等
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.26 20:42
 * @since 1.0.0
 */
public interface IResultCode extends Serializable {

    /**
     * 获取返回消息内容
     * <p>
     * 返回的消息内容可以包含占位符，用于支持参数化消息
     * 支持国际化，可以根据不同语言返回对应的消息内容
     *
     * @return 返回消息内容
     * @since 1.0.0
     */
    String getMessage();

    /**
     * 获取返回状态码
     * <p>
     * 返回的状态码用于标识请求的处理结果
     * 常见的状态码规则：2000表示成功，4xxx表示客户端错误，5xxx表示服务端错误
     *
     * @return 状态码
     * @since 1.0.0
     */
    Integer getCode();

    /**
     * 获取结果码的名称标识
     * <p>
     * 返回一个可读的名称标识，用于日志记录和调试
     * 默认返回"FAILURE"，子类可以重写这个方法提供更具体的名称
     *
     * @return 结果码名称
     * @since 1.0.0
     */
    default String name() {
        return "FAILURE";
    }

}
